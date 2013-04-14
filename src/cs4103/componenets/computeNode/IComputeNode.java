package cs4103.componenets.computeNode;

import cs4103.componenets.masterNode.IMasterNode;

/**
 * This is an interface to the implementation of compute nodes that are supposed
 * to complete an {@link IPartialResult} object and deliver it to their
 * {@link IMasterNode} parent
 * 
 * @author 120010516
 * 
 */
public interface IComputeNode {

	/**
	 * This method sends the ready result to the master node
	 * 
	 * @param result
	 *            the {@link IPartialResult}
	 * @return the outcome in a form of {@link Boolean}
	 */
	public boolean sendResultToMasterNode(IPartialResult result);

	/**
	 * Starts the computation
	 */
	public void start();

	/**
	 * Retrieves the result that this node computed (useful for testing)
	 * 
	 * @return {@link IPartialResult}
	 */
	public IPartialResult getResult();

}
