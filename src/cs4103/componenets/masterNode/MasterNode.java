package cs4103.componenets.masterNode;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.computeNode.factories.IComputeNodeFactory;
import cs4103.componenets.masterNode.nodeGroup.NodeGroup;
import cs4103.componenets.masterNode.nodeGroup.NodeGroupManager;
import cs4103.componenets.network.Message;
import cs4103.componenets.network.Network;
import cs4103.componenets.network.NodeID;
import cs4103.componenets.network.SocketConnection;
import cs4103.exceptions.DataException;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.io.IReader;
import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;
import cs4103.utils.misc.Settings;

/**
 * This class is a concrete implementation of {@link IMasterNode} interface.
 * This implementation takes care of stating a number of nodes, assigning them
 * pieces of work, retrieving the results and computing the final sum of all the
 * {@link Integer} objects. Not only that, but and object of this class takes
 * care for the continuous reliable communication with the slave nodes through
 * separate {@link SocketConnection} objects that it opens. Replication of
 * compute nodes is possible in order to avoid commission errors. Agreement
 * between different nodes working on the same input data is achieved with the
 * help of the {@link NodeGroup} objects and an instance of a
 * {@link NodeGroupManager} .
 * 
 * @author 120010516
 * 
 */
public class MasterNode implements IMasterNode {

	private BlockingQueue<Message> receivedMessages; // all the recived messages from the network layer
	private int numNodes; // the number of nodes started
	private int numDuplicates; // number of redundant nodes for each node started

	private int finaleResult; // the final result of the computation
	private IComputeNodeFactory nodeFactory; // the factory class used to produce compute nodes
	private NodeID localAddress; // the local address of this master node
	private Network network; // an instance of the network object used for communication
	private NodeGroupManager groupManager; // the group manager responsible for keeping track of the organisation of Node GRoups
	private Work workData; //the object that holds all the work to be assigned to the nodes
	private SystemLogger logger;

	/**
	 * This constructor takes care of initialising variables and starting an
	 * {@link IncomingMessageHandler} thread to deal with incoming message from
	 * slave nodes
	 * 
	 * @param compFactory
	 *            the {@link IComputeNodeFactory} used for creating the
	 *            {@link IComputeNode} objects (slave)
	 * @param reader
	 *            the {@link IReader} object that will be used for reading all
	 *            the input data to the computation
	 * @param address
	 *            the {@link NodeID} which is the local address of this
	 *            {@link IMasterNode}
	 * @param numNodes
	 *            the number of nodes among which the work will be distributed
	 * @param numDuplicated
	 *            // the number of duplicates for each of the compute nodes
	 * @throws DataException
	 *             in case there is a problem with the {@link IReader} object
	 */
	public MasterNode(IComputeNodeFactory compFactory, IReader reader,
			NodeID address, int numNodes, int numDuplicated)
			throws DataException {

		this.numNodes = numNodes;
		this.finaleResult = 0;
		this.nodeFactory = compFactory;
		this.localAddress = address;
		this.network = Network.getInstance(); // get instance of the network
		this.network.connectNodeToNetwork(this); // connect the node to the network
		this.receivedMessages = new LinkedBlockingQueue<Message>(); // used to store all incoming messages
		numDuplicates = numDuplicated;

		this.workData = new Work(reader.readData(),
				Settings.MAXIMUM_PAYLOAD_SIZE); // create the work object by using the reader
		this.groupManager = new NodeGroupManager(this, this.workData); // create a fresh node group manager
		this.logger = SystemLogger.getInstance();

	}

	/**
	 * Used to retrieve all the {@link NodeGroup} objects that this
	 * {@link IMasterNode} maintains
	 * 
	 * @return {@link LinkedList}
	 */
	public LinkedList<NodeGroup> getGroups() {
		return this.groupManager.getGroups();
	}

	/**
	 * Creates the slave nodes, connects them to a network and groups then in
	 * appropriate groups and starts them
	 */
	private void StartComputeNodes() {
		int nodeIDsuffix = 1; // the FIRST node 

		for (int i = 0; i < this.numNodes; i++) {

			/*
			 * For as many nodes as we need to create, create a separate group.
			 * Node that even if there are no replications of nodes, groups of
			 * one node will still exist, since a node group contains the main
			 * logic for reaching a decision on a result
			 */
			NodeGroup group = new NodeGroup();
			for (int q = 0; q < numDuplicates; q++) {
				// if there are duplicates loop more than ones
				IComputeNode node = this.nodeFactory.getSumComputeNode(
						nodeIDsuffix, this.localAddress); // create a compute node

				node.demonise(); // make its thread a daemon
				node.start(); // start it 
				this.network.connectNodeToNetwork(node); // connect it to the network
				group.joinNode(node.getLocalAddress()); // and most importantly join it to the group
				++nodeIDsuffix;
			}
			// at the end add the group to the group manager
			this.groupManager.addGroup(group);
		}
	}

	/**
	 * This method is used in order to check whether the system is ready with an
	 * answer. An answer is available whenever ALL the {@link NodeGroup} objects
	 * in the {@link NodeGroupManager} have reached agreement. The exception is
	 * thrown if a {@link NodeGroup} is in state FAILED regarding its agreement.
	 * There is nothing to be done in order to recover from that and the whole
	 * computation needs to be interrupted and started over. This situation
	 * arises if for example there are not enough uniform results from separate
	 * nodes in order for the group to reach an agreement, or if too many nodes
	 * have failed (due to network failure)
	 * 
	 * @return {@link Boolean}
	 * @throws MasterNodeException
	 *             in case a {@link NodeGroup} failed to reach an agreement due
	 *             to some reason. In this case the whole job needs to be
	 *             started over.
	 */
	public boolean checkIfDone() throws MasterNodeException {

		if (!this.groupManager.areAllGroupsDecided()) {

			return false;
		}
		// if we are ready, compute the final result
		this.computeFinalResult();

		return true;

	}

	/*
	 * Simple convinicience method for computing the final result
	 */
	private void computeFinalResult() {
		int res = 0;
		for (NodeGroup gr : this.groupManager.getGroups()) {
			res = res + gr.getResult();
		}

		this.finaleResult = res;
	}

	/**
	 * Starts the node process. This method creates all the {@link IComputeNode}
	 * objects, connects them to the network, dispatches all the work, using the
	 * {@link NodeGroupManager}, dispatches collection messages (POLLs) and
	 * starts the {@link IncomingMessageHandler} thread
	 */
	public void start() {

		StartComputeNodes();
		groupManager.dispatchWork();
		groupManager.dispatchWorkCollectionMessages();
		new IncomingMessageHandler(groupManager, this.receivedMessages, this);

	}

	/**
	 * 
	 * @return It returns the result of the completed computation
	 * @throws MasterNodeException
	 *             in case the node is not ready yet
	 */
	public int getResult() throws MasterNodeException {

		if (!this.checkIfDone()) {
			//if it is not ready yet, throw an exception
			throw new MasterNodeException("Computation incomplete");
		}

		return this.finaleResult;
	}

	@Override
	public NodeID getLocalAddress() {
		return this.localAddress;
	}

	@Override
	public void deliverMessage(Message m) {
		try {
			this.receivedMessages.put(m);
		} catch (InterruptedException e) {
			logger.log(
					"Interuption error in:  " + this.localAddress.toString(),
					Log.PROBLEM);
		}
	}
}
