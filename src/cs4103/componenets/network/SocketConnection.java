package cs4103.componenets.network;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.masterNode.nodeGroup.NodeGroupManager;
import cs4103.componenets.types.MessageType;
import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;
import cs4103.utils.misc.Settings;

/**
 * This class is an implementation of a reliable one - way socket. Te logic
 * behind the implementation is that of a stop - and - wait protocol. It relies
 * on sending a message and receiving an ACK of that message (sequence numbers
 * used). This implementation is used by {@link IMasterNode} objects in order to
 * communicate with {@link IComputeNode} ones. Each master maintains a separate
 * instance of this class for a one way communication with a slave node.
 * 
 * @author 120010516
 * 
 */

public class SocketConnection {
	private SystemLogger logger;
	private NodeID destination;
	/*
	 * Blocking queues are used to store outstanding messages and acks since
	 * they are thread safe and provides handy mechanisms such as a time-out
	 * take() and put methods
	 */
	private BlockingQueue<Message> outstanding;
	private BlockingQueue<Message> acks;

	private Network network; // an instance of the network, used to send messages around
	/*
	 * this thread is responsible for grabbing messages of the outstanding queue
	 * and sending them to the network, as well as keeping track of sequence
	 * numbers, acks , etc
	 */
	private Thread sender;
	private boolean mustShutDown; // a toggle that indicates when the socket should close the connection
	private NodeGroupManager groupManager; // an instance of the group manager, containing all groups of slave nodes

	/**
	 * A constructor used to initialize all variables and open the connection
	 * 
	 * @param destination
	 *            a {@link NodeID} that stores the destination of this socket .
	 *            After all every socket can be a connection to only one remote
	 *            slave
	 * @param groupManager
	 *            a {@link NodeGroupManager} object that is used by this socket
	 *            in certain circumstances. For example, if the socket decides
	 *            that there is no point of more retransmissions due to a
	 *            network or node failure, it will signal the group to remove
	 *            this particular node from the active nodes in order for the
	 *            system to be able to continue operation (if possible)
	 */
	public SocketConnection(NodeID destination, NodeGroupManager groupManager) {
		this.logger = SystemLogger.getInstance();
		this.destination = destination;
		this.outstanding = new LinkedBlockingQueue<Message>();
		this.acks = new LinkedBlockingQueue<Message>();
		this.network = Network.getInstance(); // getting an instance of the network
		this.mustShutDown = false; // does not need to shut down YET
		this.groupManager = groupManager;
		/*
		 * when all vars are initialized, fire up the sender thread
		 */
		this.startSender();
	}

	/**
	 * Puts the message into the sending window. After this is done, the message
	 * will be reliably sent unless some fatal error in the network occurs
	 * (Retransmission threshold specified in {@link Settings})
	 * 
	 * @param m
	 *            the {@link Message}
	 */
	public void sendMess(Message m) {

		try {
			this.outstanding.put(m); // puts the message in the outstanding queue
		} catch (InterruptedException e) {
			//technically should not happen, but log it 
			this.logger.log("Interuption error in socket " + this.destination,
					Log.PROBLEM);
		}
	}

	/*
	 * increments the sequence number, since this is an alternating bit
	 * protocol, it simply toggles 1 to 0 and the other way around
	 */
	private int incrementSeqNum(int seq) {
		return (seq == 0) ? 1 : 0;
	}

	/**
	 * A convenience function that starts the main sending thread. This thread
	 * keeps track of two queue the ack one and the outstanding messages one. It
	 * sends a message, and waits for an ack for it. If the ack is not received
	 * for a predefines threshold, it simply resends the message.
	 * Retransmissions are allows up to a threshold. After that the thread gives
	 * up and notifies the {@link NodeGroupManager} object that this node is not
	 * available anymore
	 */
	private void startSender() {

		this.sender = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// we start with a seq number of 0 and 0 retransmission

					int seq = 0;
					int retransmits = 0;

					while (!mustShutDown) {
						// loop while it is time to close the socket

						if (outstanding.peek() == null) {
							// if the outstanding messages queue is empty, continue to the next iteration

							continue;
						}
						/*
						 * if not, get the message but to not remove it from the
						 * queue, as it might need to be retransmitted
						 */
						Message toSend = outstanding.peek();
						toSend.setSeq(seq); // rewrite the seq to the current one

						network.sendMessage(toSend); // send the message, using the network

						/*
						 * Now.. poll the ack queue for some predefined time,
						 * which is effectively the RTO timeout. If there is
						 * nothing and the timeout expires, it will return null
						 */

						Message ack = acks.poll(Settings.RTO_MILLS,
								TimeUnit.MILLISECONDS);

						if (ack == null) {
							// if there is no ACK, we need to retransmit
							retransmits++; // increment the retransmission threshold

							logger.log("Retransmitting : " + retransmits + " "
									+ toSend.toString(), Log.PROBLEM);

							if (retransmits == Settings.MAXIMUM_RETRANSMITS) {
								/*
								 * if the retransmission threshold is reaches,
								 * simply give up and notify the group manager
								 * that the node is unavailable
								 */

								groupManager.removeNodeFromGroup(destination);
								break; // break and effectively close the socket
							}

							continue;
						}
						// in the end if everything went well, take, and remove the message from the outstanding queue
						outstanding.take();
						seq = incrementSeqNum(seq); // increment the seq number

						retransmits = 0; // null the retransmit

						if (ack.getType() == MessageType.RESULT) {
							/*
							 * if a result had arrives, (which is the effective
							 * ACK for a POLL message) , we can break of the
							 * loop, since the communication is done
							 */
							logger.log(ack, Log.NETWORK);
							break;
						}

					}

				} catch (InterruptedException e) {
					// log any exceptions
					logger.log("Interuption error in socket " + destination,
							Log.PROBLEM);

				}
			}
		});
		this.sender.setDaemon(true);
		this.sender.start();

	}

	/**
	 * Stops the socket and closes the connection
	 */
	public void closeConnection() {
		this.mustShutDown = true;
	}

	/**
	 * This is a callback used to deliver an Ack message that has arrived for
	 * this particular socket
	 * 
	 * @param m
	 *            {@link Message}
	 */
	public void deliverAck(Message m) {
		try {
			this.acks.put(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
