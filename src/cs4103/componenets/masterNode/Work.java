package cs4103.componenets.masterNode;

import java.util.LinkedList;

import cs4103.utils.misc.Settings;

/**
 * This particular class serves as a distribution mechanism that allows the
 * {@link IMasterNode} object to grab "pieces of work" of a predefined size,
 * specified in the {@link Settings} class;
 * 
 * @author 120010516
 * 
 */
public class Work {

	private LinkedList<Integer> work; // contains the integers that will be summed
	private int mSize; // the size of the pieces retreived

	/**
	 * The constructor takes in the input data that will be used by the
	 * {@link MasterNode} object along with the size of every piece of work
	 * 
	 * @param work
	 *            {@link LinkedList}
	 * @param sizeOfChunk
	 *            {@link Integer}
	 */
	public Work(LinkedList<Integer> work, int sizeOfChunk) {
		this.work = work;
		this.mSize = sizeOfChunk;
	}

	/**
	 * Retrieves pieces of work in the form of a {@link LinkedList}, each of
	 * size predefined in the {@link Settings} class. If there is not enough
	 * integers to form a full piece of work, what is left will be returned
	 * 
	 * @return
	 */
	public LinkedList<Integer> getPieceOfWork() {
		LinkedList<Integer> work = new LinkedList<Integer>(); // the new piece of work 

		int workToTake = (this.work.size() <= mSize) ? this.work.size() : mSize; // decides where to take a full one or just what is left

		for (int i = 0; i < workToTake; i++) {
			work.add(this.work.remove());
		}

		return work;

	}

	/**
	 * Checks whether there is still work left
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.work.isEmpty();
	}

}
