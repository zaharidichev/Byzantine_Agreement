package cs4103.componenets.computeNode;

import java.util.LinkedList;

import cs4103.componenets.masterNode.IMasterNode;

public class ComputeNode extends Thread implements IComputeNode {

	private LinkedList<Integer> data;
	private ComputeNodeID id;
	private IMasterNode parentNode;
	private PartialResult partialRes;

	public ComputeNode(LinkedList<Integer> data, int numberID,
			IMasterNode parent) {
		super();
		this.parentNode = parent;
		this.data = data;
		this.id = new ComputeNodeID(numberID);
	}

	private void compute() {
		int result = 0;
		for (int item : this.data) {
			result += item;
		}

		this.partialRes = new PartialResult(result, this.id);
		this.sendResultToMasterNode(this.partialRes);
	}

	public void run() {
		compute();
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

	public boolean sendResultToMasterNode(IPartialResult result) {
		this.parentNode.submitResult(result);
		return true;
	}

	@Override
	public IPartialResult getResult() {
		return this.partialRes;
	}

}
