package cs4103.submission;

import cs4103.componenets.computeNode.factories.ComputeNodeFactory;
import cs4103.componenets.masterNode.IMasterNode;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.IWorkEntity;
import cs4103.utils.Reader;
import cs4103.utils.WorkEntity;

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
		IWorkEntity work = new WorkEntity(this.numNodes, new Reader(filename));
		IMasterNode master = new MasterNode(this.numNodes,
				new ComputeNodeFactory(), work);
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
