import java.util.HashSet;
import java.util.LinkedList;

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
		MasterNode master = new MasterNode(this.numNodes, filename);
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
