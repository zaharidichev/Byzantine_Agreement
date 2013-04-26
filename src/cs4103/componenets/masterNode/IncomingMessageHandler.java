package cs4103.componenets.masterNode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cs4103.componenets.masterNode.nodeGroup.NodeGroup;
import cs4103.componenets.masterNode.nodeGroup.NodeGroupManager;
import cs4103.componenets.network.Message;
import cs4103.componenets.types.MessageType;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;

/**
 * An object of this class is responsible for handling incoming messages that
 * are delivered to a {@link IMasterNode} object. According to the message, the
 * object takes the appropriate action and deliverers it to the correct
 * {@link NodeGroup}.
 * 
 * @author 120010516
 * 
 */
public class IncomingMessageHandler {

	private NodeGroupManager groupManager; // the Group manager that contains all the node groups
	private BlockingQueue<Message> receivedMessages; // a blocking queue holding the incoming messages
	private MasterNode master; // a reference to the master node
	private SystemLogger logger;

	/**
	 * This constructor creates the object and starts it handling thread.
	 * 
	 * @param manager
	 *            the {@link NodeGroupManager} that is used for adminsitrating
	 *            the separate {@link NodeGroup} objects
	 * @param queue
	 *            the {@link LinkedBlockingQueue} that holds all incoming
	 *            messages
	 * @param master
	 *            a {@link IMasterNode} object that owns this handler
	 * 
	 */
	public IncomingMessageHandler(NodeGroupManager manager,
			BlockingQueue<Message> queue, MasterNode master) {

		// Initialises all the variables.
		this.groupManager = manager;
		this.receivedMessages = queue;
		this.master = master;
		this.logger = SystemLogger.getInstance();

		this.startHandler(); // starts the handler thread
	}

	/**
	 * This method creates and starts the handling thread that takes care of
	 * retrieving messages from the queue and treating them accordingly
	 */
	private void startHandler() {
		Thread handler = new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					// loop forever

					try {
						Message received = receivedMessages.take(); // take one message at a time
						if (received.getType() == MessageType.RESULT) {
							// if the message is a result message

							// deliver it to the correct group via the group manager
							groupManager.deliverMessToCorrectGroup(received);

							/*
							 * also deliver it as an ACK to the correct socket.
							 * Remember that a RESULT message is the equivalent
							 * of an ACK for a POLL message
							 */
							groupManager.getSocketForNode(received.getSrc())
									.deliverAck(received);

							if (master.checkIfDone()) {
								/*
								 * for (NodeGroup gr : groupManager.getGroups())
								 * { System.out.println(gr);
								 * 
								 * }
								 */

								/*
								 * check if the master is done and if done.
								 * close all sockets and break out of the loop
								 */
								groupManager.closeAllSockets();
								break;
							}
						}
						if (received.getType() == MessageType.ACK) {
							/*
							 * if an ACK is received, deliver it to the correct
							 * socket
							 */
							groupManager.getSocketForNode(received.getSrc())
									.deliverAck(received);
						}

					} catch (InterruptedException e) {
						// should not really happen
						logger.log(
								"Interuption error while handling messages...",
								Log.PROBLEM);

					} catch (MasterNodeException e) {
						/*
						 * no need to take action here, since this exception
						 * will be thrown again when the client of the
						 * application calls the checkIfDOne() method on the
						 * master
						 */
					}
				}

			}
		});

		handler.start(); // start the thread
	}
}
