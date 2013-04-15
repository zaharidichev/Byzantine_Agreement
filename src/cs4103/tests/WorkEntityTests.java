package cs4103.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.LinkedList;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import cs4103.exceptions.DataException;
import cs4103.utils.IReader;
import cs4103.utils.IWorkEntity;
import cs4103.utils.WorkEntity;

/**
 * This set of tests ensures that the functionality of {@link WorkEntity}
 * objects is as expected. The tests aim to check whether, given a predifined
 * input of a set of integers (the work) and a specific number of nodes, the
 * work will be distributed as expected
 * 
 * @author 120010516
 * 
 */
public class WorkEntityTests {

	private int numberOfWorkUnits; // the number of work units
	IWorkEntity workEntity; // the work entity being tested
	private IReader mockedReader; // the mock reader of data
	private final Mockery context = new JUnit4Mockery(); // the JMock mockery

	/**
	 * A private method used for generating a list of integers that will be used
	 * as input to the {@link WorkEntity} object being tested
	 * 
	 * @param amount
	 *            the number of integers
	 * @return a {@link LinkedList} with the integers
	 */
	private LinkedList<Integer> getMockDataContent(int amount) {
		LinkedList<Integer> result = new LinkedList<Integer>();
		for (int i = 0; i < amount; ++i) {
			result.add(i * i);
		}

		return result;
	}

	/**
	 * A builder function for the work entity. Needs to be called by every test.
	 * Note that due to the logic flow, this function cannot be put into a @Before
	 * method
	 * 
	 * @param WorkUnits
	 *            the number of work units (integers)
	 * @param numNodes
	 *            the number of nodes that will be grabbing data from this work
	 *            entity
	 * @return
	 */
	private IWorkEntity constructWorkEntity(int WorkUnits, int numNodes) {
		this.numberOfWorkUnits = WorkUnits; // assign to local private var
		try {
			buildExpectations(); // build of the  expectations of the mocked reader
		} catch (DataException e) {
			System.out.println("Ignored {" + e.getMessage() + "}"); // ignoring this exception
		}
		return new WorkEntity(numNodes, mockedReader);

	}

	@Before
	public void init() throws DataException {
		mockedReader = context.mock(IReader.class);

	}

	/*
	 * This method build the expectations of the mocked reader object. It needs
	 * to be called in every test when choosing how many work entities will be
	 * present in the work entity
	 */
	private void buildExpectations() throws DataException {
		context.checking(new Expectations() {
			{

				// mocking needed methods
				allowing(mockedReader).readData();
				// the amount data depends on the local variable which is assigned in the method responsible for building the work entity
				will(returnValue(getMockDataContent(numberOfWorkUnits)));

			}

		});
	}

	/**
	 * This test ensures that if there are 10 nodes and 15 integers to be
	 * computes, the first file nodes will be assigned 2 integers and the second
	 * file only one
	 */
	@Test
	public void testDistributionWithATailOfFive() {
		workEntity = constructWorkEntity(15, 10);

		int piecesOfSizeTwo = 5;
		int piecesOfSizeOne = 5;

		for (int i = 0; i < piecesOfSizeTwo; i++) {
			assertEquals(2, workEntity.getPieceOfWork().size());
		}

		for (int i = 0; i < piecesOfSizeOne; i++) {
			assertEquals(1, workEntity.getPieceOfWork().size());
		}

	}

	/**
	 * This test ensures that if there are 10 nodes and 10 integers, each node
	 * grabbing a piece of work will receive 1 integer
	 */
	@Test
	public void testDistributionWithNoTail() {
		workEntity = constructWorkEntity(10, 10);

		int piecesOfSizeOne = 10;

		for (int i = 0; i < piecesOfSizeOne; i++) {
			assertEquals(1, workEntity.getPieceOfWork().size());
		}
	}

	/**
	 * Tests whether 20 integers will be evenly separated between two nodes
	 */
	@Test
	public void testEvenDistributionWithTwoNodes() {
		workEntity = constructWorkEntity(20, 2);

		int piecesOfSizeTen = 2;

		for (int i = 0; i < piecesOfSizeTen; i++) {
			assertEquals(10, workEntity.getPieceOfWork().size());
		}
	}

	/**
	 * Tests whether if there are 20 integers and three nodes the first two
	 * nodes will receive 7 integers and the third one - 6
	 */
	@Test
	public void testUnevenDistributionWithThreeNodes() {
		workEntity = constructWorkEntity(20, 3);

		assertEquals(7, workEntity.getPieceOfWork().size());
		assertEquals(7, workEntity.getPieceOfWork().size());
		assertEquals(6, workEntity.getPieceOfWork().size());
	}

	/**
	 * Tests whether an empty workEntity KNOWS that it is empty
	 */
	@Test
	public void testEmptyEntity() {
		workEntity = constructWorkEntity(0, 3);
		assertFalse(workEntity.thereIsWork());
		assertEquals(0, workEntity.workLeft());

	}
}
