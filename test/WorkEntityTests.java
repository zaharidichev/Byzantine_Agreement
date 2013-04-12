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

enum WorkUnits {
	ZERO, TEN, FIFTEEN, TWENTY
}

public class WorkEntityTests {

	IWorkEntity workEntity;
	private IReader mockedReader;
	private final Mockery context = new JUnit4Mockery(); // the mockery

	private LinkedList<Integer> getMockDataContent(int amount) {
		LinkedList<Integer> result = new LinkedList<Integer>();
		for (int i = 0; i < amount; ++i) {
			result.add(i * i);
		}

		return result;
	}

	private IWorkEntity constructWorkEntity(WorkUnits integersToSum,
			int numNodes) {

		return new WorkEntity(integersToSum.toString(), numNodes, mockedReader);
	}

	@Before
	public void init() throws DataException {
		mockedReader = context.mock(IReader.class);

		context.checking(new Expectations() {
			{

				// mocking needed methods
				allowing(mockedReader).readDataFromDisk("ZERO");
				will(returnValue(getMockDataContent(0)));

				allowing(mockedReader).readDataFromDisk("TEN");
				will(returnValue(getMockDataContent(10)));

				allowing(mockedReader).readDataFromDisk("FIFTEEN");
				will(returnValue(getMockDataContent(15)));

				allowing(mockedReader).readDataFromDisk("TWENTY");
				will(returnValue(getMockDataContent(20)));

			}

		});

	}

	@Test
	public void test1() {
		workEntity = constructWorkEntity(WorkUnits.FIFTEEN, 10);

		int piecesOfSizeTwo = 5;
		int piecesOfSizeOne = 5;

		for (int i = 0; i < piecesOfSizeTwo; i++) {
			assertEquals(2, workEntity.getPieceOfWork().size());
		}

		for (int i = 0; i < piecesOfSizeOne; i++) {
			assertEquals(1, workEntity.getPieceOfWork().size());
		}

	}

	@Test
	public void test2() {
		workEntity = constructWorkEntity(WorkUnits.TEN, 10);

		int piecesOfSizeOne = 10;

		for (int i = 0; i < piecesOfSizeOne; i++) {
			assertEquals(1, workEntity.getPieceOfWork().size());
		}
	}

	@Test
	public void test3() {
		workEntity = constructWorkEntity(WorkUnits.TWENTY, 2);

		int piecesOfSizeTen = 2;

		for (int i = 0; i < piecesOfSizeTen; i++) {
			assertEquals(10, workEntity.getPieceOfWork().size());
		}
	}

	@Test
	public void test4() {
		workEntity = constructWorkEntity(WorkUnits.TWENTY, 3);

		assertEquals(7, workEntity.getPieceOfWork().size());
		assertEquals(7, workEntity.getPieceOfWork().size());
		assertEquals(6, workEntity.getPieceOfWork().size());
	}

	@Test
	public void test5() {
		workEntity = constructWorkEntity(WorkUnits.ZERO, 3);
		assertFalse(workEntity.thereIsWork());
		assertEquals(0, workEntity.workLeft());

	}
}
