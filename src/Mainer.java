import java.util.LinkedList;

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
