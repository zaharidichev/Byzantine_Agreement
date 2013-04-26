package cs4103.utils.misc;

/**
 * A simple collection of constants that are used in the system
 * 
 * @author 120010516
 * 
 */
public class Settings {
	// the maximum retransmits allowed for a message before giving up
	public static final int MAXIMUM_RETRANSMITS = 5;
	// the maximum size of the list of the integers of a WORK message
	public static final int MAXIMUM_PAYLOAD_SIZE = 7;
	// the default retransmission timout for a message
	public static final int RTO_MILLS = 3000;

	//default probabilities for simualted failures 
	public static final int NETWORK_FAIL_PROB = 50;
	public static final int PROCESS_FAIL_PROB = 50;

}
