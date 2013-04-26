package cs4103.utils.logger;

import java.util.HashMap;
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
	private HashMap<Log, StringBuilder> mapping;
	private Log loggerOutput = Log.ALL;

	/*
	 * private constructor for initialization
	 */
	private SystemLogger() {
		this.all = new StringBuilder();

		this.mapping = new HashMap<Log, StringBuilder>();
		this.mapping.put(Log.PROBLEM, new StringBuilder());
		this.mapping.put(Log.NETWORK, new StringBuilder());
		this.mapping.put(Log.GROUP_STATE, new StringBuilder());

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
		all.append(message);
		this.mapping.get(l).append(message);

		setChanged();
		notifyObservers();

	}

	public void logToAll(String message) {
		this.all.append(message + "\n");
		for (StringBuilder stream : this.mapping.values()) {
			stream.append(message + "\n");
		}
	}

	public void setLoggerOutput(Log l) {
		this.loggerOutput = l;
	}

	public String getLog() {

		if (this.loggerOutput == Log.ALL) {
			return this.all.toString() + "\n";
		}
		return (this.mapping.get(this.loggerOutput).toString() + "\n");

	}
}
