package cs4103.utils;

import java.util.LinkedList;

import cs4103.exceptions.DataException;

public interface IReader {

	public LinkedList<Integer> readDataFromDisk(String filename)
			throws DataException;

}
