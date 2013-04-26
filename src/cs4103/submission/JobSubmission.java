package cs4103.submission;

import cs4103.componenets.computeNode.factories.ComputeNodeFactory;
import cs4103.componenets.masterNode.MasterNode;
import cs4103.componenets.network.NodeID;
import cs4103.componenets.types.NodeType;
import cs4103.exceptions.DataException;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.io.Reader;
import cs4103.utils.misc.ProbabilityGenerator;

public class JobSubmission implements IJobSubmission {

	int numberOfNodes;
	int numberOfReplications;
	int nodeFaulureProbability;
	int networkFailureProbability;

	public JobSubmission() {
		this.numberOfNodes = 0;
		this.numberOfReplications = 1;
		this.nodeFaulureProbability = 0;
		this.networkFailureProbability = 0;
	}

	@Override
	public void setNumberOfNodes(int number_of_nodes) {
		this.numberOfNodes = number_of_nodes;
	}

	@Override
	public void setNumberOfNodes(int number_of_nodes, int replication) {
		this.numberOfNodes = number_of_nodes;
		this.numberOfReplications = replication;
	}

	@Override
	public int submitJob(String filename) {
		ProbabilityGenerator
				.setProbabilityOfComissionError(this.nodeFaulureProbability);
		ProbabilityGenerator
				.setProbabilityOfNetworkError(this.networkFailureProbability);
		Reader reader = new Reader(filename);
		MasterNode master = null;
		try {
			System.out.println(numberOfNodes);
			master = new MasterNode(new ComputeNodeFactory(), reader,
					new NodeID(NodeType.MASTER, 1), this.numberOfNodes,
					this.numberOfReplications);
			master.start();

			while (!master.checkIfDone()) {
				Thread.sleep(500);
			}
			return master.getResult();
		} catch (DataException e) {
			System.err.println(e.getMessage());
		} catch (MasterNodeException e) {
			System.err.println(e.getMessage());
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());

		}

		int result = -1;
		try {
			result = master.getResult();
		} catch (MasterNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
