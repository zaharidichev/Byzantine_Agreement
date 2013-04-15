package cs4103.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cs4103.componenets.computeNode.factories.IComputeNodeFactory;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.exceptions.MasterNodeException;
import cs4103.tests.mocks.ComputeNodeFactoryMock;
import cs4103.tests.mocks.ComputeNodeMock;
import cs4103.tests.mocks.WorkEntityMock;
import cs4103.utils.IWorkEntity;

/**
 * A set of tests which purpose is to ensure that the {@link MasterNode} class
 * functions as planned. Main subject to testing is the process of starting
 * compute nodes and retrieving results (the use of mocks aids in the process)
 * 
 * @author 120010516
 * 
 */
public class MasterNodeTests {

	private IComputeNodeFactory mockComputeNodeFactory; // need the mocked factory 
	private IMasterNode masterNode;
	private int numComputeNodes = 10; //overall 10 compute nodes

	@Before
	public void init() {
		mockComputeNodeFactory = new ComputeNodeFactoryMock(); // creating the mocked factory
		IWorkEntity work = new WorkEntityMock(); // creating a mocked work entity
		masterNode = new MasterNode(mockComputeNodeFactory, work); // creating the master node to be tested
	}

	/**
	 * Tests whether the starting of nodes goes as expected. If all the nodes
	 * were started, all of them will finish and submit results, therefore at
	 * some point the {@link MasterNode} will be READY
	 */
	@Test
	public void testStartingNodes() {
		masterNode.StartComputeNodes();
		while (!masterNode.isReady()) {
			// wait
		}
	}

	/**
	 * Tests the correctness of the result. This is easy since the
	 * {@link ComputeNodeMock} objects have predictable result returns
	 * 
	 * @throws MasterNodeException
	 */
	@Test
	public void testCorrectnessOfResult() throws MasterNodeException {
		masterNode.StartComputeNodes();
		while (!masterNode.isReady()) {
			// wait
		}

		// becouse we have 10 nodes and each node returns a list containing the number 1, the result will be 10
		assertEquals(numComputeNodes, masterNode.getResult());

	}

}
