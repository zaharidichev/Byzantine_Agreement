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
		dashboard.setNumberOfNodes(10, 7);
		dashboard.setNetworkFailureProbability(30);
		dashboard.setNodeFailureProbability(30);
		int result = dashboard.submitJob("/cs/home/zd2/Desktop/data.csv");
		System.out.println("Result from dashboard: " + result);
	}
}
