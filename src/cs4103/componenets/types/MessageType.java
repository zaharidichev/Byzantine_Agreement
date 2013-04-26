package cs4103.componenets.types;

/**
 * Enum that can express the different types of messages
 * 
 * <li>WORK - is work a message that the master sends to the slave. It contains
 * some payload that expresses the input to a task</li> <li>POLL - again this
 * message is sent by the master, in order to request the result of the
 * computation from the slave</li> <li>RESULT - when the slave is "polled", it
 * sends a RESULT message in reply, which also serves as an ACK for the POLL
 * message</li> <li>ACK - every message that is sent, should be ACKed</li>
 * 
 * 
 * @author 120010516
 * 
 */
public enum MessageType {
	WORK, RESULT, POLL, ACK
}
