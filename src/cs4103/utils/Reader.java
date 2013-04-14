package cs4103.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import cs4103.exceptions.DataException;

/**
 * This is an implementation of {@link IReader} interface for a class that reads
 * data from disk and returns a list of {@link Integer} obejcts
 * 
 * @author 120010516
 * 
 */
public class Reader implements IReader {

	private String location; // stores the location of the file

	/**
	 * Constructor
	 * 
	 * @param dataFile
	 *            the absolute path to the file
	 */
	public Reader(String dataFile) {
		this.location = dataFile;

	}

	/*
	 * Convenience method for constructing a BufferedReader
	 */
	private BufferedReader getReader() throws FileNotFoundException {

		BufferedReader reader = new BufferedReader(
				new FileReader(this.location));

		return reader;

	}

	/**
	 * Reads the Integers from disk and returns a list of them.
	 * 
	 * @return a {@link LinkedList} with all the integers
	 * @throws DataException
	 *             in case there was problem reading or parsing the data
	 */
	public LinkedList<Integer> readData() throws DataException {

		BufferedReader reader = null;
		LinkedList<Integer> data = new LinkedList<Integer>(); // this will be returned
		try {
			reader = getReader(); // get the reader
			String item = reader.readLine(); //reading the first line
			while (item != null) {
				int parsedInt = Integer.parseInt(item); //parse into int
				data.add(parsedInt);
				item = reader.readLine(); //read next line
			}

		} catch (IOException e) {
			//launder exception into a DataException
			throw new DataException("Unable to read file : " + this.location);
		} catch (NumberFormatException e) {
			//in case the parsing did not go as planned, means the format of data is invalid
			throw new DataException("Invalid data format in file : "
					+ this.location);

		}

		finally {
			try {
				//try and close the reader
				reader.close();
			} catch (Exception e) {
				// again notify in case of failure
				throw new DataException("Unable to close file : "
						+ this.location);
			}
		}

		return data;
	}

}
