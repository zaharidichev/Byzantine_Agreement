package cs4103.componenets.computeNode;

import cs4103.utils.Utils;

public class PartialResult implements IPartialResult {
	private int value;
	private ComputeNodeID nodeID;
	private String timeStamp;

	public PartialResult(int value, ComputeNodeID id) {
		this.value = value;
		this.nodeID = id;
		this.timeStamp = Utils.getTimeStamp();
	}

	public int getValue() {
		return this.value;
	}

	public ComputeNodeID getNodeID() {
		return this.nodeID;
	}

	public String getTimeStamp() {
		return this.timeStamp;
	}

	@Override
	public String toString() {
		return "PartialResult [value: " + value + ", NodeID: " + this.nodeID
				+ ", time: " + timeStamp + "]";
	}

}
