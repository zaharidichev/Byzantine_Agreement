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

public class ReaderTests {

	private static String notExistingFileName;
	private static String existingFileName;

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

		createFile();
	}

	@AfterClass
	public static void tearDown() {
		deleteFile();
	}

	@Test(expected = DataException.class)
	public void test1() throws DataException {
		IReader reader = new Reader(notExistingFileName);
		reader.readData();

	}

	@Test()
	public void test2() throws DataException {
		int val = 0;
		IReader reader = new Reader(existingFileName);

		for (int i : reader.readData()) {
			assertEquals(val * val, i);

			val++;
		}
	}

}
