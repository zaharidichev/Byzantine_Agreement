package cs4103.tests.mocks;

import java.util.LinkedList;

import cs4103.componenets.computeNode.ComputeNodeID;
import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.computeNode.IPartialResult;
import cs4103.componenets.computeNode.PartialResult;
import cs4103.componenets.masterNode.IMasterNode;

public class ComputeNodeMock extends Thread implements IComputeNode {

	private ComputeNodeID id;
	private IMasterNode parentNode;
	private PartialResult partialRes;

	public ComputeNodeMock(int numberID, IMasterNode parent) {
		super();
		this.parentNode = parent;
		this.id = new ComputeNodeID(numberID);
		this.partialRes = new PartialResult(1, this.id);

	}

	public void start() {
		sendResultToMasterNode(this.partialRes);
	}

	@Override
	public boolean sendResultToMasterNode(IPartialResult result) {
		this.parentNode.submitResult(result);
		return true;
	}

	@Override
	public IPartialResult getResult() {
		return this.partialRes;
	}

}
