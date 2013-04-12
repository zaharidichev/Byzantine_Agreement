package cs4103.submission;
public interface IJobSubmission {
	/*
	 * Your solution is required to implement this interface. You should have a
	 * JobSubmission class which implements this interface and calls your other
	 * classes. Tests will be applied against this interface to ensure that your
	 * implementation works correctly so no additional methods should be needed
	 * in order to run your solution.
	 */

	// sets the number of nodes, required for part 1 should be called prior to
	// submitJob
	public void setNumberOfNodes(int number_of_nodes);

	// sets the number of nodes and also sets how many times computation is
	// repeated, required for part 2
	public void setNumberOfNodes(int number_of_nodes, int replication);

	// accepts a file and performs the computation, required for all parts
	public int submitJob(String filename);

	// set the chance of commission failure, required for part 2
	public void setNodeFailureProbability(double probability);

	// set the chance of network failure, required for part 3
	public void setNetworkFailureProbability(double probability);

}