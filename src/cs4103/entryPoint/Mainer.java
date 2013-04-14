package cs4103.entryPoint;

import cs4103.submission.JobSubmission;

public class Mainer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JobSubmission dashboard = new JobSubmission();

		dashboard.setNumberOfNodes(10);
		System.out
				.println(dashboard
						.submitJob("/home/zahari/Desktop/newWorkspace/distributedSystem/src/data.csv"));

	}

}
