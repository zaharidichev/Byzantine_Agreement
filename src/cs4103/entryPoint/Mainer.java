package cs4103.entryPoint;

import cs4103.exceptions.DataException;
import cs4103.exceptions.MasterNodeException;
import cs4103.submission.IJobSubmission;
import cs4103.submission.JobSubmission;

public class Mainer {

	/**
	 * @param args
	 * @throws DataException
	 * @throws MasterNodeException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws DataException,
			InterruptedException {

		IJobSubmission dashboard = new JobSubmission();
		dashboard.setNumberOfNodes(10, 5);
		dashboard.setNetworkFailureProbability(10);
		dashboard.setNodeFailureProbability(0);
		int result = dashboard
				.submitJob("/home/zahari/Desktop/newWorkspace/CS4103-CW2-120010516/src/data.csv");

		System.out.println("Result from dashboard: " + result);
	}
}
