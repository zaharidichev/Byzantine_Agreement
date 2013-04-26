package cs4103.utils.misc;

/**
 * A static class that is used in order to simulate some events that are
 * dependant on probability
 * 
 * @author 120010516
 * 
 */
public class ProbabilityGenerator {

	// by default both probabilities are set to 50%
	private static double probabilityOfComissionError = 0.5;
	private static double probabilityOfNetworkError = 0.5;

	/**
	 * Sets the probability of comission failure (AKA node returning an
	 * incorrect result)
	 * 
	 * @param prob
	 *            Integer in the range 0 - 100
	 */
	public static void setProbabilityOfComissionError(int prob) {
		probabilityOfComissionError = prob / 100.0;
	}

	/**
	 * Sets the probability of the network dropping a message
	 * 
	 * @param prob
	 *            Integer in the range 0 - 100
	 */

	public static void setProbabilityOfNetworkError(int prob) {
		probabilityOfNetworkError = prob / 100.0;
	}

	/**
	 * Tosses a coin to determine whether the network will experience failure.
	 * Note that usually a coin is tossed for every message going through the
	 * network
	 * 
	 * @return
	 */
	public static boolean willNetworkFail() {
		return (Math.random() < probabilityOfNetworkError) ? true : false;
	}

	/**
	 * Tosses a coin to determine whether the node will return a false result
	 * 
	 * @return
	 */
	public static boolean willProcessFail() {
		return (Math.random() < probabilityOfComissionError) ? true : false;
	}

}
