package cs4103.componenets.computeNode;

import cs4103.utils.Utils;

/**
 * A concrete implementation of the {@link IPartialResult} interface, this class
 * provides the functionality that is needed in order to associate a numeric
 * result value with the time it was created and the {@link IComputeNode} that
 * was responsible for the calculations.
 * 
 * @author 120010516
 * 
 */

public class PartialResult implements IPartialResult {

	private int value; // stores the value 
	private ComputeNodeID nodeID; // the node ID
	private String timeStamp; // the timestamp

	/**
	 * The constructor used for creating objects of this class.
	 * 
	 * @param value
	 *            the numeric values
	 * @param id
	 *            the {@link ComputeNodeID} of the node responsible for the
	 *            calculation
	 */
	public PartialResult(int value, ComputeNodeID id) {
		this.value = value;
		this.nodeID = id;
		this.timeStamp = Utils.getTimeStamp(); // getting a timestamp
	}

	@Override
	public int getValue() {
		return this.value;
	}

	@Override
	public ComputeNodeID getNodeID() {
		return this.nodeID;
	}

	@Override
	public String getTimeStamp() {
		return this.timeStamp;
	}

	@Override
	public String toString() {
		return "PartialResult [value: " + value + ", NodeID: " + this.nodeID
				+ ", time: " + timeStamp + "]";
	}

}
