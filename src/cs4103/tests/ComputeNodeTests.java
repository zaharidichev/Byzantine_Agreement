package cs4103.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import cs4103.componenets.computeNode.ComputeNode;
import cs4103.componenets.computeNode.IPartialResult;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.exceptions.MasterNodeException;

/**
 * The tests in this class are created with the purpose to ensure that the
 * {@link ComputeNode} objects behave as expected. This is achieved by the
 * construction of a {@link IMasterNode} mock
 * 
 * @author 120010516
 * 
 */
public class ComputeNodeTests {

	private IMasterNode masterNodeMock; // the master node mock
	private ComputeNode computeNode; // the compute node tested
	private LinkedList<Integer> data; // the data that will be used as input
	private int sumOfData = 0; // the final result

	/*
	 * this variable will be set to true if the compute node call the method to
	 * send data to the master (which needs to happen)
	 */
	private boolean sendToClientProcedureCalled = false;

	/*
	 * Creates the master mock node as a anonymous class
	 */
	private void createMasterMock() {
		masterNodeMock = new IMasterNode() {

			private IPartialResult mockResult; // the result

			@Override
			public void submitResult(IPartialResult result) {
				/*
				 * If this method is called, we set the value of the
				 * sendToMaster var to true
				 */
				sendToClientProcedureCalled = true;
				this.mockResult = result;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public int getResult() throws MasterNodeException {
				return this.mockResult.getValue();
			}

			@Override
			public void StartComputeNodes() {
				// stub, which we do not need :)
			}
		};
	}

	/*
	 * Generates a Linked List of 10 integers that will be used as an input to
	 * the computation
	 */
	private void createMockData() {
		data = new LinkedList<Integer>();
		for (int i = 0; i < 10; i++) {
			data.add(i * i);
			sumOfData += (i * i);
		}

	}

	@Before
	public void init() {
		createMasterMock();
		createMockData();
		computeNode = new ComputeNode(data, 1, masterNodeMock);
	}

	/**
	 * Tests whether the result of the computation is in fact correct
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void computationTest() throws InterruptedException {
		computeNode.start();
		computeNode.getResult();
		computeNode.join();
		assertEquals(sumOfData, computeNode.getResult().getValue());
	}

	/**
	 * Tests whether after the computation is done, the method that submit job
	 * method on the mock master was called by the {@link ComputeNode} object
	 * 
	 * @throws InterruptedException
	 */

	@Test
	public void masterNodeCommunicationTest() throws InterruptedException {
		assertFalse(sendToClientProcedureCalled); //should be false before any computation is done
		computeNode.start();
		computeNode.getResult();
		computeNode.join(); // wait for the thread
		assertTrue(sendToClientProcedureCalled);
	}
}
