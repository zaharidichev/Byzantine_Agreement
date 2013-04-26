package cs4103.entryPoint;

import cs4103.submission.IJobSubmission;
import cs4103.submission.JobSubmission;

public class StartConsoleMode {

	/**
	 * Starts the simulation through the {@link JobSubmission} class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		IJobSubmission dashboard = new JobSubmission();
		dashboard.setNumberOfNodes(10, 7);
		dashboard.setNetworkFailureProbability(1);
		dashboard.setNodeFailureProbability(2);
		int result = dashboard.submitJob("/cs/home/zd2/Desktop/data.csv");
		System.out.println("Result from dashboard: " + result);
	}
}
