import java.util.HashSet;
import java.util.LinkedList;

public class MasterNode {

	private int result;
	int numNodes;
	private WorkEntity allTheWork;
	private HashSet<ComputeNode> listOfNodes;
	private LinkedList<PartialResult> partialResults;

	private int numOfFinished;

	public MasterNode(int numNodes, String dataFile) {
		this.allTheWork = new WorkEntity(dataFile, numNodes);
		this.listOfNodes = new HashSet<ComputeNode>();
		this.partialResults = new LinkedList<PartialResult>();

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

	public synchronized void submitResult(PartialResult result) {
		System.out.println("received: " + result);
		numOfFinished++;
		this.partialResults.add(result);
		if (this.isReady()) {
			this.computeFinal();
			System.out.println("Sum is : [" + this.result + "]");
		}

	}

	private void computeFinal() {
		for (PartialResult partial : this.partialResults) {
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
