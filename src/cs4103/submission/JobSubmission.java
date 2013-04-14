package cs4103.submission;

import cs4103.componenets.computeNode.factories.ComputeNodeFactory;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.IWorkEntity;
import cs4103.utils.Reader;
import cs4103.utils.WorkEntity;

/**
 * This class implements the {@link IJobSubmission} interface and is the main
 * entry point to the computational system. It abstracts away the creation of
 * {@link MasterNode} objects and also keeps track of whether they have
 * completed their work or not. This class is also responsible for retrieving
 * the result from the computation and delivering it to the client of the class
 * 
 * @author 120010516
 * 
 */
public class JobSubmission implements IJobSubmission {

	private int numNodes;

	public JobSubmission() {
		//init the variable to 0
		this.numNodes = 0;
	}

	@Override
	public void setNumberOfNodes(int number_of_nodes) {
		this.numNodes = number_of_nodes;

	}

	@Override
	public void setNumberOfNodes(int number_of_nodes, int replication) {
		// TODO Auto-generated method stub

	}

	@Override
	public int submitJob(String filename) {
		IWorkEntity work = new WorkEntity(this.numNodes, new Reader(filename)); // creates the work entity
		/*
		 * creates the master node and supply it with the work and the factory
		 * needed to produce compute nodes
		 */
		IMasterNode master = new MasterNode(new ComputeNodeFactory(), work);
		master.StartComputeNodes(); // starts the compute nodes
		while (!master.isReady()) {
			//waiting till all the compute nodes are ready and the master has processed their partial results
			System.out.println("Waiting...");
		}

		try {
			return master.getResult(); // Retrieves the result and returns it
		} catch (MasterNodeException e) {
			// if an exception is thrown, we cannot recover from the situation
			System.err.println("System Failure");
			System.exit(1);
		}

		return -1;
	}

	@Override
	public void setNodeFailureProbability(double probability) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setNetworkFailureProbability(double probability) {
		// TODO Auto-generated method stub

	}

}
