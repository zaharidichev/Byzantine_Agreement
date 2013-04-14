package cs4103.exceptions;

/**
 * Any problems that are associated with the masterNode need to be reflected by
 * throwing this exception
 * 
 * @author 120010516
 * 
 */
public class MasterNodeException extends Exception {

	private static final long serialVersionUID = 1L;

	public MasterNodeException(String message) {
		super(message);
	}

}
