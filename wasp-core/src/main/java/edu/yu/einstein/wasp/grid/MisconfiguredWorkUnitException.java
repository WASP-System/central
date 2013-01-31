/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import edu.yu.einstein.wasp.exception.WaspException;

/**
 * 
 * Work unit can not be executed because the user has neglected to configure it properly.
 * 
 * @author calder
 *
 */
public class MisconfiguredWorkUnitException extends WaspException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3492348601377304232L;

	/**
	 * @param message
	 */
	public MisconfiguredWorkUnitException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MisconfiguredWorkUnitException(String message, Throwable cause) {
		super(message, cause);
	}

}
