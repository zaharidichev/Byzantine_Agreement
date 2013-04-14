package cs4103.tests.mocks;

import java.util.LinkedList;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.computeNode.factories.IComputeNodeFactory;
import cs4103.componenets.masterNode.IMasterNode;

public class ComputeNodeFactoryMock implements IComputeNodeFactory {

	public ComputeNodeFactoryMock() {

	}

	@Override
	public IComputeNode getSumComputeNode(LinkedList<Integer> data, int ID,
			IMasterNode parent) {
		return new ComputeNodeMock(ID, parent);
	}

}
