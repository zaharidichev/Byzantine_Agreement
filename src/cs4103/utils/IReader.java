package cs4103.utils;

import java.util.LinkedList;

import cs4103.exceptions.DataException;

/**
 * An interface abstracting away the reading of data. Implementation of this
 * interface can be reading from the network, the web or disk.
 * 
 * @author 120010516
 * 
 */
public interface IReader {

	/**
	 * 
	 * Reads the integers and returns a list of them
	 * 
	 * @return a {@link LinkedList} with all the integers
	 * @throws DataException
	 *             in case there was problem reading or parsing the data
	 */
	public LinkedList<Integer> readData() throws DataException;

}
