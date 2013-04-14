package cs4103.componenets.computeNode;

/**
 * This is an interface to the implementation of partial result classes. The
 * purpose of partial results is to encapsulate an integer value, id of a node
 * that produced it and a timestamp signifying when the computation was produced
 * 
 * @author 120010516
 * 
 */
public interface IPartialResult {

	/**
	 * Retrieves the numeric value of the result
	 * 
	 * @return an integer value
	 */
	public int getValue();

	/**
	 * Used to get the id object of the node that produced this result
	 * 
	 * @return {@link ComputeNodeID} object
	 */
	public ComputeNodeID getNodeID();

	/**
	 * Used to retrieve a textual representation of the time this result object
	 * was created
	 * 
	 * @return a {@link String} object
	 */
	public String getTimeStamp();
}
