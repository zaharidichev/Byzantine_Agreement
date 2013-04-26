package cs4103.componenets.types;

import cs4103.utils.logger.SystemLogger;

/**
 * An interface to the implementation of logging functionality. Any object that
 * implements this interface must provide functionality that summarizes its
 * state in a textual form in order to be used by the {@link SystemLogger}
 * 
 * @author 120010516
 * 
 */
public interface Loggable {

	/**
	 * Returns textual representation of the state of the object
	 * 
	 * @return {@link String}
	 */
	public String getLog();
}
