package cs4103.submission;

import java.util.HashSet;
import java.util.LinkedList;

import cs4103.componenets.computeNode.factories.ComputeNodeFactory;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.exceptions.MasterNodeException;

public class JobSubmission implements IJobSubmission {

	private int numNodes;

	public JobSubmission() {
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
		IMasterNode master = new MasterNode(this.numNodes, filename, new ComputeNodeFactory());
		master.StartComputeNodes();
		while (!master.isReady()) {
			System.out.println("Waiting...");
		}

		try {
			return master.getResult();
		} catch (MasterNodeException e) {
			System.out.println("BAAAAD....");
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