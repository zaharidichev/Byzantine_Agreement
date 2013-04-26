package cs4103.exceptions;

/**
 * Any problems, caused by the network are indicated by throwing this type of
 * exception
 * 
 * @author 120010516
 * 
 */
public class NetworkException extends Exception {

	private static final long serialVersionUID = 1L;

	public NetworkException(String message) {
		super(message);
	}

}
