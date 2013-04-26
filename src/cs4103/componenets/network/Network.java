package cs4103.componenets.network;

import java.util.HashMap;

import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;
import cs4103.utils.misc.ProbabilityGenerator;

/**
 * This class is an essential component of the whole system as it is the central
 * mechanism of communication between nodes. An instance of this class acts as
 * an abstraction of a router. It contains functionality for sending and
 * delivering {@link Message} objects to the correct nodes
 * 
 * @author 120010516
 * 
 */
public class Network {

	/*
	 * The two maps hold key - value pairs, where the keys are addresses of
	 * nodes and the values are references to the nodes themselves
	 */
	private HashMap<NodeID, IAddressable> machines;
	private static Network instance = null; // instance for the singleton
	private SystemLogger logger;

	/**
	 * Public method to get an instance of the Singleton object
	 * 
	 * @return an instance of {@link Network}
	 */
	public static Network getInstance() {
		if (instance == null) {
			//if it does not exist, call the private constructor
			instance = new Network();
		}
		return instance;

	}

	/*
	 * Private constructor used to initialise the variables
	 */
	private Network() {
		this.machines = new HashMap<NodeID, IAddressable>();
		this.logger = SystemLogger.getInstance();
	}

	/**
	 * This method is used to connect a node to the network. Once a node is
	 * connected, it is reachable by other nodes
	 * 
	 * @param node
	 */
	public void connectNodeToNetwork(IAddressable node) {
		this.machines.put(node.getLocalAddress(), node);

	}

	/**
	 * This method is used to send messages to the appropriate nodes.
	 * 
	 * @param mess
	 */
	public void sendMessage(Message mess) {

		this.logger.log(mess, Log.NETWORK); // send the messageto the logger 
		if (!ProbabilityGenerator.willNetworkFail()) {
			// tossing a coin to see whether this message will be dropped or not 
			NodeID destAddress = mess.getDest();
			this.machines.get(destAddress).deliverMessage(mess);

		}

	}
}
