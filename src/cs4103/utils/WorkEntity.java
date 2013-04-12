package cs4103.utils;
import java.util.LinkedList;

import cs4103.exceptions.DataException;

public class WorkEntity implements IWorkEntity{

	private int numWorkers;
	private int computationalUnitsPerWorker;
	private int tailUnits;
	private String file;

	private LinkedList<Integer> data;

	public WorkEntity(String dataFile, int numberOfWorkers, IReader dataReader) {
		this.file = dataFile;
		try {
			this.data = dataReader.readDataFromDisk(this.file);
		} catch (DataException e) {
			System.out.println("Exitting after fatal error {" + e.getMessage()
					+ "}");
			System.exit(1);
		}

		this.numWorkers = numberOfWorkers;
		this.computationalUnitsPerWorker = data.size() / this.numWorkers;
		this.tailUnits = data.size() % this.numWorkers;
	}

	public LinkedList<Integer> getPieceOfWork() {

		LinkedList<Integer> result = new LinkedList<Integer>();

		int elementsToRetrieve = this.computationalUnitsPerWorker;

		if (this.tailUnits != 0) {
			elementsToRetrieve = this.computationalUnitsPerWorker + 1;
			--this.tailUnits;
		}

		int retreived = 0;
		while (retreived < elementsToRetrieve) {
			result.add(this.data.remove());
			retreived += 1;
		}

		return result;

	}

	public int workLeft() {
		return this.data.size();
	}

	public boolean thereIsWork() {
		return (!this.data.isEmpty()) ? true : false;
	}

}
