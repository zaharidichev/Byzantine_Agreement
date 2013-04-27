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
		dashboard.setNetworkFailureProbability(5);
		dashboard.setNodeFailureProbability(5);
		int result = dashboard.submitJob("../../data.csv");
		System.out.println("Result from dashboard: " + result);
	}
}
