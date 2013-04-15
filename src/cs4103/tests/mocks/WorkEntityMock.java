package cs4103.tests.mocks;

import java.util.LinkedList;

import cs4103.utils.IWorkEntity;

/**
 * This implementation of {@link IWorkEntity} serves as a mock class with
 * predictable behavior which is independent of other circumstances. It is used
 * in testing and it always has 10 pieces of work each with on value of 1
 * 
 * @author 120010516
 * 
 */
public class WorkEntityMock implements IWorkEntity {

	private LinkedList<Integer> result;

	int workLeft;

	public WorkEntityMock() {
		result = new LinkedList<Integer>(); //each piece is of val 1
		result.add(1);
		workLeft = 10; // have only 10 pieces 
	}

	@Override
	public LinkedList<Integer> getPieceOfWork() {
		workLeft--; // decrement workLeft
		return result; // return the list
	}

	@Override
	public int workLeft() {
		return workLeft;
	}

	@Override
	public boolean thereIsWork() {
		return (this.workLeft == 0) ? false : true;
	}

	@Override
	public int getNumNodes() {
		return 10;
	}

}
