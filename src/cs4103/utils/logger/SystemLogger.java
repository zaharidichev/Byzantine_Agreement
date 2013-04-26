package cs4103.utils.logger;

import java.util.Observable;

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
public class SystemLogger extends Observable {

	private static SystemLogger instance = null;
	private StringBuilder all; // stream for all messages
	private boolean consoleLoging;

	/*
	 * private constructor for initialization
	 */
	private SystemLogger() {
		this.all = new StringBuilder();
		consoleLoging = true;

	}

	/**
	 * Sets the logger for logging to system.out
	 * 
	 * @param toggle
	 */
	public void setCOnsoleLogging(boolean toggle) {
		this.consoleLoging = toggle;
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
		String message = "{" + Utils.getTimeStamp() + "}    " + object.getLog()
				+ "\n";
		System.out.print(message);

		all.append(message);

		//Notify observers that the state has changed
		setChanged();
		notifyObservers();

	}

	/**
	 * Used to log a plain text message
	 * 
	 * @param text
	 * @param l
	 */
	public void log(String text, Log l) {
		String message = "{" + Utils.getTimeStamp() + "}    " + text + "\n";
		if (this.consoleLoging) {
			System.out.print(message);
		}

		all.append(message);

		setChanged();
		notifyObservers();

	}

	public void logToAll(String message) {
		this.all.append(message + "\n");

	}

	/**
	 * Retrieves all the logging information up to this point
	 * 
	 * @return
	 */
	public String getLog() {

		return this.all.toString() + "\n";

	}
}
