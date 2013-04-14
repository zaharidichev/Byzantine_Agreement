package cs4103.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Just a class that holds a collection of static methods, abstracting simple
 * functionality, needed by other classes
 * 
 * @author 120010516
 * 
 */
public class Utils {

	/**
	 * Returns a textual representation of the current time to the millisecond.
	 * 
	 * @return {@link String} containing the timestamp
	 */
	public static String getTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:S");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
