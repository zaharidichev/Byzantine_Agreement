package cs4103.tests.mocks;

import java.util.LinkedList;

import cs4103.utils.IWorkEntity;

public class WorkEntityMock implements IWorkEntity {

	private LinkedList<Integer> result;

	int workLeft;

	public WorkEntityMock() {
		result = new LinkedList<Integer>();
		result.add(1);
		workLeft = 10;
	}

	@Override
	public LinkedList<Integer> getPieceOfWork() {
		workLeft--;
		return result;
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
