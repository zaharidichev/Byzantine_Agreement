package cs4103.componenets.masterNode.nodeGroup;

import java.util.HashMap;
import java.util.LinkedList;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.componenets.masterNode.Work;
import cs4103.componenets.network.Message;
import cs4103.componenets.network.NodeID;
import cs4103.componenets.network.SocketConnection;
import cs4103.componenets.types.MessageType;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;

/**
 * This class is a container for {@link NodeGroup} objects. In addition to that
 * it provides functionality for delivering messages to the correct group to
 * which they belong. Furthermore it provides accessor methods that facilitate
 * the tracking of the process of agreement for the groups
 * 
 * @author 120010516
 * 
 */
public class NodeGroupManager {

	private LinkedList<NodeGroup> groupsOfNodes; // a list of all groups

	/*
	 * This particular map, maps a node ID to a SocketConnection object. This is
	 * useful when delivering back ACKS and sending messages to the slave nodes
	 */
	private HashMap<NodeID, SocketConnection> router;

	private MasterNode master; // a reference to the master node
	private Work work; // a reference to the work object
	private SystemLogger logger; // an instance of the logger

	/**
	 * Constructor that creates an empty {@link NodeGroupManager}
	 * 
	 * @param master
	 *            reference to the {@link IMasterNode} object
	 * @param w
	 *            Reference to the {@link Work} object
	 */
	public NodeGroupManager(MasterNode master, Work w) {
		// initialising variable
		this.groupsOfNodes = new LinkedList<NodeGroup>();
		this.router = new HashMap<NodeID, SocketConnection>();
		this.master = master;
		this.work = w;
		this.logger = SystemLogger.getInstance(); // get a logger
	}

	/**
	 * Method used for closing all open sockets for all the nodes that are
	 * contained within the groups of this group manager
	 */
	public void closeAllSockets() {
		for (SocketConnection connection : this.router.values()) {
			connection.closeConnection();
		}
	}

	/**
	 * Adds a new group to the manager and opens up {@link SocketConnection}
	 * objects to the {@link IComputeNode} that are within this group
	 * 
	 * @param g
	 *            {@link NodeGroup}
	 */
	public void addGroup(NodeGroup g) {
		this.groupsOfNodes.add(g);

		for (NodeID node : g.getNodeAddresses()) {
			this.router.put(node, new SocketConnection(node, this));
		}

	}

	/**
	 * Removes a node from the group it belongs to. This is used when the
	 * {@link IComputeNode} has failed (for example after N retransmissions).
	 * This allows the system to carry on with the work if possible, without
	 * wasting timeouts on this node
	 * 
	 * @param node
	 */
	public void removeNodeFromGroup(NodeID node) {
		this.logger.log("Node: " + node + " is unreachable", Log.PROBLEM); // logs the problem
		this.router.get(node).closeConnection(); // closes its socket
		this.router.remove(node); // removes the socket from the router 
		for (NodeGroup g : this.groupsOfNodes) {
			//remove the node from the group (if in a group that is managed by this manager object)
			g.removeNode(node);
		}
	}

	/**
	 * Returns a list of all the {@link NodeGroup} objects under management
	 * 
	 * @return {@link LinkedList}
	 */
	public LinkedList<NodeGroup> getGroups() {
		return this.groupsOfNodes;
	}

	/**
	 * Delivers result message to a group. Used when the {@link IMasterNode}
	 * receives a result message and needs to pass it on to the correct group in
	 * order for the {@link NodeGroup} to use it in its decision process.
	 * 
	 * @param mess
	 */
	public void deliverMessToCorrectGroup(Message mess) {
		for (NodeGroup group : this.groupsOfNodes) {
			if (group.belongs(mess.getSrc())) {
				/*
				 * loops until it finds the correct group which contains the
				 * node that actually sent this message
				 */
				group.addResult(mess.getPayload().getFirst());
			}
		}
	}

	/**
	 * Checks whether all groups have reached a decision
	 * 
	 * @return {@link Boolean}
	 * @throws MasterNodeException
	 *             in case a group is in FAILED state (from which it cannot
	 *             recover)
	 */
	public boolean areAllGroupsDecided() throws MasterNodeException {
		Boolean returnValue = true;
		logger.log(
				"----------------------------------STATE OF NODE GROUPS----------------------------------",
				Log.GROUP_STATE);
		for (NodeGroup group : this.groupsOfNodes) {
			logger.log(group, Log.GROUP_STATE);
			if (group.getGroupState() == GroupState.UNDECIDED) {

				returnValue = false;
			} else if (group.getGroupState() == GroupState.FAILED) {
				/*
				 * we need to give up at this point since too many nodes have
				 * given different results, or too many nodes are unreachable
				 * and therefore excluded from the group
				 */
				this.closeAllSockets();
				throw new MasterNodeException("Decision cannot be reached..."
						+ group);
			}
		}

		return returnValue;

	}

	/**
	 * This method is used to send a message to a correct node. It uses the
	 * sockets that are associated with each node
	 * 
	 * @param address
	 * @param m
	 */
	public void sendMessageToANode(NodeID address, Message m) {

		this.router.get(address).sendMess(m); // send thrpough the correct socket
	}

	/**
	 * Retrieves the corresponding {@link SocketConnection} object for a
	 * {@link NodeID}
	 * 
	 * @param node
	 *            {@link NodeID}
	 * @return {@link SocketConnection}
	 */
	public SocketConnection getSocketForNode(NodeID node) {
		return this.router.get(node);
	}

	/**
	 * This method is used to sent the same {@link Message} object to all the
	 * member {@link IComputeNode} objects of a group. This can be used when
	 * dispatching work to a group of nodes
	 * 
	 * @param group
	 *            the {@link NodeGroup} object
	 * @param mess
	 *            the {@link Message} object
	 */
	public void sendMessageToGroup(NodeGroup group, Message mess) {
		for (NodeID node : group.getNodeAddresses()) {
			// send to all nodes withing a group
			Message individual = new Message(mess);
			individual.setDest(node);
			router.get(node).sendMess(individual);
		}

	}

	/**
	 * Method used to dispatch all the work that needs to be computed by nodes
	 */
	public void dispatchWork() {

		while (!this.work.isEmpty()) {
			//loop while there is work
			for (NodeGroup group : this.groupsOfNodes) {
				if (this.work.isEmpty()) {
					//check again for every subIteration
					break;
				}
				//create a new work message
				Message workMessage = new Message(
						this.master.getLocalAddress(),
						this.master.getLocalAddress(), // this will be rewritten later
						this.work.getPieceOfWork(), MessageType.WORK, 0);

				// send the message to the whole group
				this.sendMessageToGroup(group, workMessage);

			}
		}

	}

	/**
	 * Generally after the work is completed by the {@link IComputeNode}, they
	 * wait for a POLL {@link Message} object in order to return the result.
	 * This method allows to send such a message to all the nodes within the
	 * groups under management
	 */
	public void dispatchWorkCollectionMessages() {
		for (NodeGroup group : this.groupsOfNodes) {
			Message poll = new Message(this.master.getLocalAddress(),
					this.master.getLocalAddress(), // will get rewritten
					new LinkedList<Integer>(), MessageType.POLL, 0);
			this.sendMessageToGroup(group, poll);
		}

	}

}
