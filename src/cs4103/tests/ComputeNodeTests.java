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

public class ComputeNodeTests {

	private IMasterNode masterNodeMock;
	private ComputeNode computeNode;
	private LinkedList<Integer> data;
	private int sumOfData = 0;
	private boolean sendToClientProcedureCalled = false;

	private void createMasterMock() {
		masterNodeMock = new IMasterNode() {

			private IPartialResult mockResult;

			@Override
			public void submitResult(IPartialResult result) {
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

	@Test
	public void computationTest() throws InterruptedException {
		computeNode.start();
		computeNode.getResult();
		computeNode.join();
		assertEquals(sumOfData, computeNode.getResult().getValue());
	}

	@Test
	public void masterNodeCommunicationTest() throws InterruptedException {
		assertFalse(sendToClientProcedureCalled);
		computeNode.start();
		computeNode.getResult();
		computeNode.join();
		assertTrue(sendToClientProcedureCalled);
	}
}
