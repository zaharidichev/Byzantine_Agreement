package cs4103.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import cs4103.exceptions.DataException;

public class Reader {

	private static BufferedReader getReader(String filename)
			throws FileNotFoundException {

		BufferedReader reader = new BufferedReader(new FileReader(filename));

		return reader;

	}

	public static LinkedList<Integer> readDataFromDisk(String filename)
			throws DataException {

		BufferedReader reader = null;
		LinkedList<Integer> data = new LinkedList<Integer>();
		try {
			reader = getReader(filename);

			String item = reader.readLine();

			while (item != null) {
				int parsedInt = Integer.parseInt(item);
				data.add(parsedInt);
				item = reader.readLine();
			}
		} catch (IOException e) {
			throw new DataException("Unable to read file : " + filename);
		} catch (NumberFormatException e) {
			throw new DataException("Invalid data format in file : " + filename);

		}

		finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new DataException("Unable to close file : " + filename);
			}
		}

		return data;
	}

	public static void main(String args[]) throws DataException {

	}
}
