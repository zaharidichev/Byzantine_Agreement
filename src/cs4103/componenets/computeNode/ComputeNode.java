package cs4103.componenets.computeNode;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.network.Message;
import cs4103.componenets.network.Network;
import cs4103.componenets.network.NodeID;
import cs4103.componenets.types.MessageType;
import cs4103.componenets.types.NodeType;
import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;
import cs4103.utils.misc.ProbabilityGenerator;

/**
 * This is an implementation of the {@link IComputeNode} interface. This class
 * serves as the slave in the master - slave relationship between a
 * {@link IMasterNode} and an {@link IComputeNode} objects. The node receives
 * {@link Message} objects, containing integers that need to be summed. The
 * integers are summed and a result is accumulated with every message received.
 * Once a POLL message is received, the result is delivered back to the
 * {@link IMasterNode}. The communication between the two objects is possible
 * through a reliable transport protocol that implements stop and wait paradigm
 * via ACKs
 * 
 * @author 120010516
 * 
 */
public class ComputeNode extends Thread implements IComputeNode {

	private BlockingQueue<Message> messages; // the received messages
	private NodeID address; // the local address of the node
	private NodeID parentNode; //  a reference to the master node
	private Network network; // an instance of the network
	private int sum; // the sum accumulated
	private SystemLogger logger;

	/**
	 * A constructor taking in the numeric ID of the node as well as the address
	 * of the master
	 * 
	 * @param numberID
	 *            the numeric id of the node in order to be able to construct a
	 *            {@link NodeID} for itself
	 * @param master
	 *            the {@link NodeID} of the master, which is effectively the
	 *            address
	 */
	public ComputeNode(int numberID, NodeID master) {
		super(); // calling Thread's constructor
		this.address = new NodeID(NodeType.SLAVE, numberID); // constructing its own address
		this.messages = new LinkedBlockingQueue<Message>();
		this.network = Network.getInstance(); // getting instance of the network Singleton
		this.parentNode = master;
		this.sum = 0;
		this.logger = SystemLogger.getInstance();
	}

	/**
	 * Sums up the contents of the payload of a message
	 * 
	 * @param m
	 *            a {@link Message} object
	 * @return the result
	 */
	private int sum(Message m) {

		int result = 0;
		for (int item : m.getPayload()) {
			result += item;
		}
		return result;
	}

	/**
	 * This method delivers a message to the node, by putting it onto its
	 * incoming messages queue.
	 */
	public void deliverMessage(Message m) {
		try {
			this.messages.put(m);
		} catch (InterruptedException e) {
			logger.log("Interuption error in:  " + this.address.toString(),
					Log.PROBLEM);
		}
	}

	/**
	 * Retrieves the result value. This method can in fact fail and deliver a
	 * false value depending on the probability of comission failure set in the
	 * {@link ProbabilityGenerator}
	 * 
	 * @return
	 */
	private int getResultValue() {

		if (ProbabilityGenerator.willProcessFail()) {
			// if decided , made u pa random value
			return (int) (Math.random() * 100);
		}

		return this.sum;
	}

	/**
	 * Increment the sequence number
	 * 
	 * @param seq
	 * @return
	 */
	private int incrementSeqNum(int seq) {
		return (seq == 0) ? 1 : 0;
	}

	/**
	 * Checks whether the message has been already received. In our case, this
	 * will be if the sequence number is different from the one expected
	 * 
	 * @param m
	 *            {@link Message}
	 * @param expecting
	 *            seq number
	 * @return {@link Boolean}
	 */
	public boolean alreadyReceived(Message m, int expecting) {

		if (m.getSeqNum() != expecting) {
			/*
			 * if already delivered, the ACK must have been lost due to network
			 * problems, so just ACK it again
			 */
			this.network.sendMessage(new Message(getLocalAddress(), parentNode,
					new LinkedList<Integer>(), MessageType.ACK, expecting));
			return true;
		}

		return false;

	}

	@Override
	public void run() {
		/*
		 * This method takes care of handling incoming events. Its logic
		 * resembles the one of the dedicated IncomingMessagesHandler class used
		 * by the master node, but the implementation details are slightly
		 * different
		 */
		int expectingSeq = 0; // we start with 0
		while (true) {
			try {

				Message received = this.messages.take(); // take a message from the queue

				if (received.getType() == MessageType.WORK) {
					// if it is a work message, containing integers

					/*
					 * System.out.println("-------------");
					 * System.out.println(expectingSeq);
					 * System.out.println(received.getSeqNum());
					 * System.out.println("-------------");
					 */

					if (!alreadyReceived(received, expectingSeq)) {
						// check if it has not been received before, send an ACK for it... 

						this.network.sendMessage(new Message(getLocalAddress(),
								parentNode, new LinkedList<Integer>(),
								MessageType.ACK, expectingSeq));
						// get its sum 
						int sumOfMessage = this.sum(received);
						// add it to the sum being maintained by the node
						this.sum += sumOfMessage;
						//increment expected seq number
						expectingSeq = incrementSeqNum(expectingSeq);

					}

				}

				if (received.getType() == MessageType.POLL) {
					/*
					 * If it is a poll message, just construct a new message
					 * containing the result of this computation
					 */
					LinkedList<Integer> payload = new LinkedList<Integer>();
					payload.add(getResultValue());

					Message result = new Message(this.address, this.parentNode,
							payload, MessageType.RESULT, received.getSeqNum());

					/*
					 * Send the result message. Keep in mind that we do not need
					 * to send an ACK, since a RESULT message is an implicit
					 * acknowledgment of a POLL message. If the result message
					 * gets lost, another POLL will be sent
					 */
					this.network.sendMessage(result);

				}

			} catch (InterruptedException e) {
				logger.log("Interuption error in:  " + this.address.toString(),
						Log.PROBLEM);
			}

		}

	}

	@Override
	public int hashCode() {
		return this.address.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComputeNode other = (ComputeNode) obj;
		if (this.hashCode() != other.hashCode())
			return false;
		return true;
	}

	@Override
	public NodeID getLocalAddress() {
		return this.address;
	}

	@Override
	public void demonise() {
		this.setDaemon(true);
	}

}
