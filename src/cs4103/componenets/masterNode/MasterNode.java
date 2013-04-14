package cs4103.componenets.masterNode;

import java.util.HashSet;
import java.util.LinkedList;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.computeNode.IPartialResult;
import cs4103.componenets.computeNode.factories.IComputeNodeFactory;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.IWorkEntity;

/**
 * This class is a concrete implementation of {@link IMasterNode} interface.
 * This implementation takes care of stating a number of nodes, assigning them
 * pieces of work, retrieving the results and computing the final sum of all the
 * {@link Integer} objects
 * 
 * @author 120010516
 * 
 */
public class MasterNode implements IMasterNode {

	private int result; //stores the final result
	int numNodes; //stores the number of nodes that were dispatched
	private int numOfFinished; // keeps track of the number of nodes that have finished
	private IComputeNodeFactory nodeFactory; // the factory class used to produce compute nodes
	private IWorkEntity allTheWork; // the work entity used to deliver work for the nodes that are dispatched
	private HashSet<IComputeNode> listOfNodes; // a list of all the nodes that have been dispatched
	private LinkedList<IPartialResult> partialResults; // a list of all partial results received from the compute nodes

	/**
	 * This constructor takes care for initializing all the variables by getting
	 * the needed information from the {@link IWorkEntity} object
	 * 
	 * @param compFactory
	 *            the {@link IComputeNodeFactory} used to create
	 *            {@link IComputeNode} objects
	 * @param work
	 *            the {@link IWorkEntity} that the master node is responsible
	 *            for
	 */
	public MasterNode(IComputeNodeFactory compFactory, IWorkEntity work) {

		// initialising variables
		this.allTheWork = work;
		this.listOfNodes = new HashSet<IComputeNode>();
		this.partialResults = new LinkedList<IPartialResult>();
		this.nodeFactory = compFactory;
		this.numNodes = work.getNumNodes(); // getting the num of nodes from the work entity
		this.result = 0;
		this.numOfFinished = 0;

	}

	/**
	 * The method creates all the nodes and pulls work from the
	 * {@link IWorkEntity} object in order to assign to them. It also assigns
	 * unique IDs to all the {@link IComputeNode} objects. After creation each
	 * node is started
	 */
	public void StartComputeNodes() {
		int nodeIDsuffix = 1; // the FIRST node
		while (this.allTheWork.thereIsWork()) {
			// loop while there is work in  the entity
			IComputeNode node = this.nodeFactory.getSumComputeNode( // create a node
					this.allTheWork.getPieceOfWork(), nodeIDsuffix, this);
			node.start(); //start it 
			this.listOfNodes.add(node); //add it to the list 
			++nodeIDsuffix; // increment the numeric part of the id of the node
		}
	}

	/**
	 * This method is used by the compute nodes when they need to submit their
	 * partial result to the {@link MasterNode}. The method is synchronized
	 * becouse it can be called by only one thread at a time. This guarantees
	 * that invalid updates on numOfFinished variable will not occur due to Race
	 * conditions
	 */
	public synchronized void submitResult(IPartialResult result) {
		System.out.println("received: " + result);
		numOfFinished++; // increment the number of finished nodes
		this.partialResults.add(result); // add the submitted result to the list of partial results
		if (this.isReady()) {
			// if all the nodes have submitted their result just perform the computation
			this.computeFinal();
			System.out.println("Sum is : [" + this.result + "]");
		}

	}

	/*
	 * Convenience method that performs the final computation over the partial
	 * result (sums them up)
	 */
	private void computeFinal() {
		for (IPartialResult partial : this.partialResults) {
			this.result += partial.getValue();
		}
	}

	@Override
	public boolean isReady() {
		// if all compute nodes have submitted their result, the work is done
		return (this.numOfFinished == this.numNodes) ? true : false;
	}

	/**
	 * The method is used to retrieve the result of a computation. Note that
	 * before the method is called, one needs to, check whether the node is
	 * ready by calling the appropriate method
	 * 
	 * @return the result
	 * @throws MasterNodeException
	 *             in case the computation is not completed
	 */
	public int getResult() throws MasterNodeException {

		if (!this.isReady()) {
			//if it is not ready yet, throw an exception
			throw new MasterNodeException("Computation incomplete");
		}

		return this.result;
	}
}
