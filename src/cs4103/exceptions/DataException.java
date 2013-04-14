package cs4103.exceptions;

/**
 * A specific exception class, that needs to be used when problems with either
 * the format or the process of reading the data that is needed for the
 * computation occurs
 * 
 * @author 120010516
 * 
 */
public class DataException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataException(String message) {
		super(message);
	}

}
