package cs4103.utils.logger;

import cs4103.componenets.types.Loggable;
import cs4103.utils.misc.Utils;

/**
 * This class is a singleton that is used in the system for logging. It can log
 * text messages as well as any object that implements the {@link Loggable}
 * interface
 * 
 * @author 120010516
 * 
 */
public class SystemLogger {

	private static SystemLogger instance = null;
	private StringBuilder all; // stream for all messages
	private StringBuilder networkActivity; // stream for network related messages
	private StringBuilder problems; // stream for messages indicating problems

	/*
	 * private constructor for initialization
	 */
	private SystemLogger() {
		this.all = new StringBuilder();
		this.networkActivity = new StringBuilder();
		this.problems = new StringBuilder();

	}

	/**
	 * A factory method for grabbing an instance
	 * 
	 * @return {@link SystemLogger}
	 */
	public static SystemLogger getInstance() {
		if (instance == null) {
			instance = new SystemLogger();
		}
		return instance;

	}

	/**
	 * Used to log an object that implements the {@link Loggable} interface
	 * 
	 * @param object
	 * @param l
	 */
	public void log(Loggable object, Log l) {
		String message = "{" + Utils.getTimeStamp() + "}    " + object.getLog();
		all.append(message);

		if (l == Log.NETWORK) {
			networkActivity.append(message);
		} else {
			problems.append(message);
		}
		System.out.println(message);

	}

	/**
	 * Used to log a plain text message
	 * 
	 * @param text
	 * @param l
	 */
	public void log(String text, Log l) {
		String message = "{" + Utils.getTimeStamp() + "}    " + text;
		all.append(message);

		if (l == Log.NETWORK) {
			networkActivity.append(message);
		} else {
			problems.append(message);
		}
		System.out.println(message);

	}

}
