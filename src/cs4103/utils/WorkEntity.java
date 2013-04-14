package cs4103.utils;

import java.util.LinkedList;

import cs4103.exceptions.DataException;

/**
 * This class is an implementation of the {@link IWorkEntity} interface. It
 * provides the functionality that is needed to read a number of integers from a
 * {@link IReader} object and distribute the amount of computational work EVENLY
 * (or as evenly as possible) among a number of nodes.
 * 
 * @author 120010516
 * 
 */
public class WorkEntity implements IWorkEntity {

	private int numWorkers; // the number of worker nodes that will be working on the data
	private int computationalUnitsPerWorker; // notes how many integers will be assigned per worker
	private int tailUnits; // stores the number of integers that are left after even distribution
	private LinkedList<Integer> data; // all the integers that form the work entity

	/**
	 * This constructor takes care of initializing all variables and reading the
	 * data from the {@link IReader} provided
	 * 
	 * 
	 * @param numberOfWorkers
	 *            the number of workers that will be using this object
	 * @param dataReader
	 *            the {@link IReader} object that abstracts away the retrival of
	 *            data
	 */
	public WorkEntity(int numberOfWorkers, IReader dataReader) {
		try {
			this.data = dataReader.readData(); // trying to read the data
		} catch (DataException e) {

			// if there is a data exception, there is not point in going on since
			// the computation will be invalid
			System.out.println("Exitting after fatal error {" + e.getMessage()
					+ "}");
			System.exit(1);
		}

		this.numWorkers = numberOfWorkers;
		this.computationalUnitsPerWorker = data.size() / this.numWorkers; // this is how many integers will be assigned per node
		this.tailUnits = data.size() % this.numWorkers; // this is how many units are left to be distributed amongst nodes
	}

	/**
	 * 
	 * This method takes care of delivering relatively even pieces of work per
	 * call. The max size of the piece of work returned is sizeOfData /
	 * numOfWorkers + 1 and the minimum is sizeOfData / numOfWorkers. This
	 * guarantees that no node will be underutilized or overloaded
	 * 
	 * @return {@link LinkedList} of integers
	 */
	@Override
	public LinkedList<Integer> getPieceOfWork() {

		/*
		 * What is important to note is that this method takes care of the
		 * distribution in a specific way. if the number of integers does not
		 * divide evenly by the number of nodes the remainder will not be simply
		 * put onto the last node, neither a new node will be created. What is
		 * going to happen is that each of the first callers of this method will
		 * grab the evenly distributed integers per note + 1 more integer which
		 * will be taken from the tail. This goes on until there is no more
		 * integers left.
		 */

		LinkedList<Integer> result = new LinkedList<Integer>(); // the piece that will be returned

		int elementsToRetrieve = this.computationalUnitsPerWorker; // this is how many elements need to be retrieved (AT LEAST)

		if (this.tailUnits != 0) {
			// if there is more units left in the tail, add one more integer to the work returned
			elementsToRetrieve = this.computationalUnitsPerWorker + 1;
			--this.tailUnits; // decrement the number of integers left in the  tail
		}

		int retreived = 0;
		while (retreived < elementsToRetrieve) {

			//fill the list with integers
			result.add(this.data.remove());
			retreived += 1;
		}

		return result;

	}

	@Override
	public int workLeft() {
		return this.data.size();
	}

	@Override
	public boolean thereIsWork() {
		return (!this.data.isEmpty()) ? true : false;
	}

}
