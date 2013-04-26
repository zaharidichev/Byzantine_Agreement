package cs4103.exceptions;

/**
 * Exception that is raised in case something went wrong regarding the
 * computation process
 * 
 * @author 120010516
 * 
 */
public class ComputationException extends Exception {

	private static final long serialVersionUID = 1L;

	public ComputationException(String message) {
		super(message);
	}

}
