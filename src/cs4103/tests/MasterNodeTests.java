package cs4103.tests;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import cs4103.componenets.computeNode.factories.IComputeNodeFactory;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.exceptions.MasterNodeException;
import cs4103.tests.mocks.ComputeNodeFactoryMock;
import cs4103.tests.mocks.WorkEntityMock;
import cs4103.utils.IWorkEntity;

public class MasterNodeTests {

	private IComputeNodeFactory mockComputeNodeFactory;
	private IMasterNode masterNode;
	private int numComputeNodes = 10;

	@Before
	public void init() {
		mockComputeNodeFactory = new ComputeNodeFactoryMock();
		IWorkEntity work = new WorkEntityMock();
		masterNode = new MasterNode(numComputeNodes, mockComputeNodeFactory,
				work);
	}

	@Test
	public void testStartingNodes() {
		masterNode.StartComputeNodes();
		while (!masterNode.isReady()) {
			// wait
		}
	}

	@Test
	public void testCorrectnessOfResult() throws MasterNodeException {
		masterNode.StartComputeNodes();
		while (!masterNode.isReady()) {
			// wait
		}

		assertEquals(numComputeNodes, masterNode.getResult());

	}

}
