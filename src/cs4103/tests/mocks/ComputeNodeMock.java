package cs4103.tests.mocks;

import cs4103.componenets.computeNode.ComputeNodeID;
import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.computeNode.IPartialResult;
import cs4103.componenets.computeNode.PartialResult;
import cs4103.componenets.masterNode.IMasterNode;

/**
 * This implementation of {@link IComputeNode} is a simplified compute node with
 * predictable behavior that is independent of the input. It always sends a
 * partial result of value 1 to the master node. It is used to test the
 * communication between the master and the compute nodes
 * 
 * @author 120010516
 * 
 */

public class ComputeNodeMock extends Thread implements IComputeNode {

	private ComputeNodeID id;
	private IMasterNode parentNode;
	private PartialResult partialRes;

	public ComputeNodeMock(int numberID, IMasterNode parent) {
		super();
		this.parentNode = parent;
		this.id = new ComputeNodeID(numberID);
		this.partialRes = new PartialResult(1, this.id); // the result will always be 1

	}

	public void start() {
		sendResultToMasterNode(this.partialRes); // do not compute anything, just send the result
	}

	@Override
	public boolean sendResultToMasterNode(IPartialResult result) {
		this.parentNode.submitResult(result); // same as in a typical compute nodeF
		return true;
	}

	@Override
	public IPartialResult getResult() {
		return this.partialRes;
	}

}
