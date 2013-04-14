package cs4103.componenets.computeNode;

import java.util.LinkedList;

import cs4103.componenets.masterNode.IMasterNode;

/**
 * This class is an implementation of {@link IComputeNode} interface. Objects of
 * this class are created by a {@link IMasterNode} and a responsible for
 * performing a SUM operation over a list of {@link Integer} objects. The object
 * is the responsible for submitting the result to its master node. The class is
 * an extension of the {@link Thread} class.
 * 
 * @author 120010516
 * 
 */

public class ComputeNode extends Thread implements IComputeNode {

	private LinkedList<Integer> data; // the list of integers
	private ComputeNodeID id; // the id of the node
	private IMasterNode parentNode; // a reference to the parent node (needs for sending the result)
	private PartialResult partialRes; // the partial result that the node produced

	/**
	 * This constructor takes care of initializing all the variables and
	 * creating a proper {@link ComputeNodeID} object
	 * 
	 * @param data
	 *            {@link LinkedList}
	 * @param numberID
	 *            {@link Integer} storing the numeric part of the id
	 * @param parent
	 *            a reference to the {@link IMasterNode} object that is the
	 *            parent of this node
	 */
	public ComputeNode(LinkedList<Integer> data, int numberID,
			IMasterNode parent) {
		super();
		this.parentNode = parent;
		this.data = data;
		this.id = new ComputeNodeID(numberID);
	}

	/*
	 * Convenience method that sums the integers and sends the result
	 */
	private void compute() {
		int result = 0;
		for (int item : this.data) {
			result += item;
		}

		this.partialRes = new PartialResult(result, this.id); // creates a partial result object
		this.sendResultToMasterNode(this.partialRes); // sends it to master node
	}

	@Override
	public void run() {
		compute(); // calls the compute method
	}

	@Override
	public int hashCode() {

		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComputeNode other = (ComputeNode) obj;
		if (this.hashCode() != other.hashCode())
			return false;
		return true;
	}

	/**
	 * This method sends the ready result to the master node. The use of methods
	 * of this sort is to as closely as possible emulate a networked
	 * environment. This is why much of the native Java synchronization
	 * mechanism are not used. The purpose is to provide an abstraction that
	 * mimics the operations of a distributed system over a networked
	 * infrastructure
	 * 
	 * @param result
	 *            the {@link IPartialResult}
	 * @return the outcome in a form of {@link Boolean}
	 */
	public boolean sendResultToMasterNode(IPartialResult result) {
		this.parentNode.submitResult(result);
		return true;
	}

	@Override
	public IPartialResult getResult() {
		return this.partialRes;
	}

}
