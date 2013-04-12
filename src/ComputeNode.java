import java.util.LinkedList;

public class ComputeNode extends Thread {

	private LinkedList<Integer> data;
	private ComputeNodeID id;
	private MasterNode parentNode;

	public ComputeNode(LinkedList<Integer> data, int numberID, MasterNode parent) {
		super();
		this.parentNode = parent;
		this.data = data;
		this.id = new ComputeNodeID(numberID);
	}

	private void compute() {
		int result = 0;
		for (int item : this.data) {
			result += item;
		}

		this.sendResultToMasterNode(new PartialResult(result, this.id));
	}

	public void run() {
		compute();
	}

	@Override
	public int hashCode() {

		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComputeNode other = (ComputeNode) obj;
		if (this.hashCode() != other.hashCode())
			return false;
		return true;
	}

	private boolean sendResultToMasterNode(PartialResult result) {
		this.parentNode.submitResult(result);
		return true;
	}

}
