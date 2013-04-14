package cs4103.componenets.computeNode.factories;

import java.util.LinkedList;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.masterNode.IMasterNode;

/**
 * An interface for a factory class which purpose is to construct objects of
 * type {@link IComputeNode}. Although it might seem that an interface is not
 * needed for such a simple class, it greatly eases in mocking and testing
 * 
 * @author 120010516
 * 
 */
public interface IComputeNodeFactory {

	/**
	 * This method abstracts away the creation of {@link IComputeNode} obejcts
	 * which purpose is to perform summation operation over a list of integers
	 * 
	 * @param data
	 *            {@link LinkedList} the list of ints
	 * @param ID
	 *            {@link Integer} the numeric ID of the node
	 * @param parent
	 *            {@link IMasterNode} a reference to the parent of the node that
	 *            is created
	 * @return an {@link IComputeNode} object
	 */
	public IComputeNode getSumComputeNode(LinkedList<Integer> data, int ID,
			IMasterNode parent);

}
