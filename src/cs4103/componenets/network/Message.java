package cs4103.componenets.network;

import java.util.LinkedList;

import cs4103.componenets.types.Loggable;
import cs4103.componenets.types.MessageType;

/**
 * Instances of this class are the main method of communication between nodes. A
 * message in this system would be an abstraction for what a datagram is in a
 * TCP connection.
 * 
 * @author 120010516
 * 
 */

public class Message implements Loggable {

	private NodeID source; // the source (the node which sent the message)
	private NodeID destination; // the node to which the message is addressed
	private LinkedList<Integer> payload; // the data that the message is carrying
	private MessageType type; // the type of the message
	private int seqNum;

	/**
	 * Constructs a new message
	 * 
	 * @param src
	 *            the {@link NodeID} signifying the source
	 * @param dest
	 *            the {@link NodeID} signifying the destination
	 * @param payload
	 *            the {@link LinkedList} containing the data
	 * @param type
	 *            the {@link MessageType} value signifying what is the intent of
	 *            this message
	 */
	public Message(NodeID src, NodeID dest, LinkedList<Integer> payload,
			MessageType type, int seqNum) {

		// initializing variables
		this.source = src;
		this.destination = dest;
		this.payload = payload;
		this.type = type;
		this.seqNum = seqNum;

	}

	/**
	 * A copy constructor, needed for deep copying of the object. Useful when
	 * rewriting of messages is needed
	 * 
	 * @param other
	 */
	public Message(Message other) {
		this.source = other.getSrc();
		this.destination = other.getDest();
		this.payload = other.getPayload();
		this.type = other.getType();
		this.seqNum = other.seqNum;
	}

	/**
	 * Returns the size of the message
	 * 
	 * @return {@link Integer} the size
	 */
	public int getSize() {
		return this.payload.size();
	}

	/**
	 * Returns the source
	 * 
	 * @return {@link NodeID}
	 */
	public NodeID getSrc() {
		return this.source;
	}

	/**
	 * Returns the destination
	 * 
	 * @return {@link NodeID}
	 */
	public NodeID getDest() {
		return this.destination;
	}

	/**
	 * Returns the data that the message carries
	 * 
	 * @return {@link LinkedList}
	 */
	public LinkedList<Integer> getPayload() {
		return this.payload;
	}

	/**
	 * Returns the type of the message
	 * 
	 * @return {@link MessageType}
	 */
	public MessageType getType() {
		return this.type;
	}

	/**
	 * Sets the destination of the message
	 * 
	 * @param dest
	 *            {@link NodeID}
	 */
	public void setDest(NodeID dest) {
		this.destination = dest;
	}

	/**
	 * Sets the sequence number of the message. Useful when the message is being
	 * handled by reliable protocols
	 * 
	 * @param s
	 */
	public void setSeq(int s) {

		this.seqNum = s;

	}

	/**
	 * Retrieves the sequence number of the message
	 * 
	 * @return {@link Integer}
	 */
	public int getSeqNum() {
		return this.seqNum;
	}

	@Override
	public String toString() {
		return "{[source: " + source + "] [destination: " + destination
				+ "] [payload: " + payload + "] [type: " + type + "] [ SN: "
				+ this.seqNum + "]}";
	}

	@Override
	public String getLog() {
		return this.toString();
	}

}
