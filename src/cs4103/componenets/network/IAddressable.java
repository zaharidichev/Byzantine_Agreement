package cs4103.componenets.network;

/**
 * Objects implementing this particular interface can be joined to a
 * {@link Network} object and communicate through its methods
 * 
 * @author 120010516
 * 
 */
public interface IAddressable {

	/**
	 * Retrieves the local address of the object
	 * 
	 * @return {@link NodeID}
	 */
	public NodeID getLocalAddress();

	/**
	 * Delivers a message to the object
	 * 
	 * @param m
	 *            {@link Message}
	 */
	public void deliverMessage(Message m);

}
