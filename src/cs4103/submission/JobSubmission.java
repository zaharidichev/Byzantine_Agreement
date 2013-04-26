package cs4103.submission;

import cs4103.componenets.computeNode.factories.ComputeNodeFactory;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.componenets.network.NodeID;
import cs4103.componenets.types.NodeType;
import cs4103.exceptions.DataException;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.io.Reader;
import cs4103.utils.logger.Log;
import cs4103.utils.logger.SystemLogger;
import cs4103.utils.misc.ProbabilityGenerator;

/**
 * Implementation of the IJobSubmission interface (as required in the
 * assignment). This class takes care of creating a {@link MasterNode} ,
 * starting it, keeping track of progress and delviering back the results
 * 
 * @author 120010516
 * 
 */

public class JobSubmission implements IJobSubmission {

	// variables needed 
	int numberOfNodes;
	int numberOfReplications;
	int nodeFaulureProbability;
	int networkFailureProbability;

	/**
	 * Constructs a default job submission
	 */
	public JobSubmission() {
		this.numberOfNodes = 0;
		this.numberOfReplications = 1;
		this.nodeFaulureProbability = 0;
		this.networkFailureProbability = 0;
	}

	@Override
	public void setNumberOfNodes(int number_of_nodes) {
		// set the number of nodes
		this.numberOfNodes = number_of_nodes;
	}

	@Override
	public void setNumberOfNodes(int number_of_nodes, int replication) {
		//set the number of replication
		this.numberOfNodes = number_of_nodes;
		this.numberOfReplications = replication + 1;
	}

	@Override
	public int submitJob(String filename) {
		int result = -1;
		// set probabilities
		ProbabilityGenerator
				.setProbabilityOfComissionError(this.nodeFaulureProbability);
		ProbabilityGenerator
				.setProbabilityOfNetworkError(this.networkFailureProbability);

		// create a new reader object
		Reader reader = new Reader(filename);
		MasterNode master = null;
		//get an intance of the logger
		SystemLogger logger = SystemLogger.getInstance();

		try {
			//creates the master
			master = new MasterNode(new ComputeNodeFactory(), reader,
					new NodeID(NodeType.MASTER, 1), this.numberOfNodes,
					this.numberOfReplications);
			master.start(); // starts the master
			while (!master.checkIfDone()) {
				// blocks untill the master is done
				Thread.sleep(500);
			}
			result = master.getResult();
			// handles any exceptions that might arise
		} catch (DataException e1) {
			logger.log(e1.getMessage(), Log.PROBLEM);
		} catch (MasterNodeException e2) {
			logger.log(e2.getMessage(), Log.PROBLEM);
		} catch (InterruptedException e3) {
			logger.log(e3.getMessage(), Log.PROBLEM);

		} finally {

			System.out.println(logger.getLog());
		}

		return result;
	}

	@Override
	public void setNodeFailureProbability(double probability) {
		this.nodeFaulureProbability = (int) probability;
	}

	@Override
	public void setNetworkFailureProbability(double probability) {
		this.networkFailureProbability = (int) probability;
	}
}
