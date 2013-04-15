package cs4103.tests.mocks;

import java.util.LinkedList;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.computeNode.factories.IComputeNodeFactory;
import cs4103.componenets.masterNode.IMasterNode;

/**
 * A mocked factory that produces {@link ComputeNodeMock} objects. Used for
 * dependency injection into a {@link IMasterNode} in order to carry on testing
 * 
 * @author 120010516
 * 
 */

public class ComputeNodeFactoryMock implements IComputeNodeFactory {

	public ComputeNodeFactoryMock() {

	}

	@Override
	public IComputeNode getSumComputeNode(LinkedList<Integer> data, int ID,
			IMasterNode parent) {
		return new ComputeNodeMock(ID, parent);
	}

}
