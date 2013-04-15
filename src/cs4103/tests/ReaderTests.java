package cs4103.tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cs4103.exceptions.DataException;
import cs4103.utils.IReader;
import cs4103.utils.Reader;

/**
 * This class contains tests that ensure the normal functionality of the
 * {@link Reader} class. Its purpose is to test whether the class can read and
 * parse the contents of a file that exists on disk and whether it will throw an
 * appropriate exception if a file that does not exist is provided as an
 * argument to the reader
 * 
 * @author 120010516
 * 
 */
public class ReaderTests {

	private static String notExistingFileName; // stores the name of a non existing file
	private static String existingFileName; // stores the name of a file that exists

	/*
	 * The function takes care of creating a file with already known contents on
	 * disk
	 */
	private static void createFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					existingFileName));
			for (int i = 0; i < 10; i++) {
				out.write(i * i + "\n");
			}
			out.close();
		} catch (IOException e) {
			System.out.println("Unable to create File");
		}
	}

	/*
	 * The function takes care of deleting the file created specially or those
	 * set of tests
	 */
	private static void deleteFile() {
		File file = new File(notExistingFileName);

		if (file.exists()) {
			file.delete();
		}

	}

	@BeforeClass
	public static void init() {
		notExistingFileName = "SomeFileThatDoesNotExist.xxx";
		existingFileName = "SomeFileThatExists.xxx";

		createFile(); // call the create method
	}

	@AfterClass
	public static void tearDown() {
		deleteFile(); // call the delete method
	}

	//TODO need to write f method that will generate a random name of a file and check whether it exists and will keep trying untill it is sure that no suck file exists

	/**
	 * This test ensures that upon provision of a name of a file that does not
	 * exist, the {@link Reader} throws a {@link DataException}.
	 * 
	 * 
	 * 
	 * @throws DataException
	 */
	@Test(expected = DataException.class)
	public void testExceptionThrowing() throws DataException {
		IReader reader = new Reader(notExistingFileName);
		reader.readData();

	}

	/**
	 * Tests whether the contents of the file created can be properly parsed and
	 * read by the reader
	 * 
	 * @throws DataException
	 */
	@Test()
	public void testReadingFromAFile() throws DataException {
		int val = 0;
		IReader reader = new Reader(existingFileName);

		for (int i : reader.readData()) {
			assertEquals(val * val, i);

			val++;
		}
	}

}
