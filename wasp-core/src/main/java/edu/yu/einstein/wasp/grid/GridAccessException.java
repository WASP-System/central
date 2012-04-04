/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import edu.yu.einstein.wasp.exception.GridException;

/**
 * Exception thrown when there is a problem between the submitter and the grid provider.
 * Attempt at submission has been made but submission has not taken place.
 * 
 * @author calder
 *
 */
public class GridAccessException extends GridException {

	/**
	 * 
	 */
	public GridAccessException() {
		super();
	}

	/**
	 * @param message
	 */
	public GridAccessException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GridAccessException(String message, Throwable cause) {
		super(message, cause);
	}

}
