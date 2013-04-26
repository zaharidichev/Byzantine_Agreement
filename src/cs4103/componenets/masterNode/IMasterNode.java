package cs4103.componenets.masterNode;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.network.IAddressable;
import cs4103.exceptions.MasterNodeException;

/**
 * This is an interface to the implementation of master nodes. Those type of
 * objects are responsible for accumulating {@link IPartialResult} objects from
 * the dispatched {@link IComputeNode} objects and keeping track of whether the
 * job is completed or not
 * 
 * @author 120010516
 * 
 */

public interface IMasterNode extends IAddressable {

	/**
	 * Start the computing nodes
	 */
	//	private void StartComputeNodes();

	/**
	 * This method is used by an {@link IComputeNode} to submit a partial result
	 * object to its parent
	 * 
	 * @param result
	 *            {@link IPartialResult} containing the result of the
	 *            computation performed on the {@link IComputeNode}
	 */

	/**
	 * 
	 * @return It returns the result of the completed computation
	 * @throws MasterNodeException
	 *             in case there is something wrong
	 */
	public int getResult() throws MasterNodeException;

}
