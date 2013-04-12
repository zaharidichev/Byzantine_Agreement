package cs4103.componenets.masterNode;

import java.util.HashSet;
import java.util.LinkedList;

import cs4103.componenets.computeNode.ComputeNode;
import cs4103.componenets.computeNode.IComputeNode;
import cs4103.componenets.computeNode.IPartialResult;
import cs4103.exceptions.MasterNodeException;
import cs4103.utils.IWorkEntity;
import cs4103.utils.WorkEntity;

public class MasterNode {

	private int result;
	int numNodes;
	private IWorkEntity allTheWork;
	private HashSet<IComputeNode> listOfNodes;
	private LinkedList<IPartialResult> partialResults;

	private int numOfFinished;

	public MasterNode(int numNodes, String dataFile) {
		this.allTheWork = new WorkEntity(dataFile, numNodes);
		this.listOfNodes = new HashSet<IComputeNode>();
		this.partialResults = new LinkedList<IPartialResult>();

		this.numNodes = numNodes;
		this.result = 0;
		this.numOfFinished = 0;

	}

	public void StartComputeNodes() {
		int nodeIDsuffix = 1;
		while (this.allTheWork.thereIsWork()) {
			ComputeNode node = new ComputeNode(
					this.allTheWork.getPieceOfWork(), nodeIDsuffix, this);
			node.start();
			this.listOfNodes.add(node);
			++nodeIDsuffix;
		}
	}

	public synchronized void submitResult(IPartialResult result) {
		System.out.println("received: " + result);
		numOfFinished++;
		this.partialResults.add(result);
		if (this.isReady()) {
			this.computeFinal();
			System.out.println("Sum is : [" + this.result + "]");
		}

	}

	private void computeFinal() {
		for (IPartialResult partial : this.partialResults) {
			this.result += partial.getValue();
		}
	}

	public boolean isReady() {
		return (this.numOfFinished == this.numNodes) ? true : false;
	}

	public int getResult() throws MasterNodeException {

		if (!this.isReady()) {
			throw new MasterNodeException("Computation incomplete");
		}

		return this.result;
	}
}
