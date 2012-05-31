/**
 * 
 */
package edu.yu.einstein.wasp.grid;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.WorkUnit;

/**
 * WorkUnit is successfully submitted to the grid provider, however the script terminated abnormally.
 * May be due to problems on the grid host or failure of logic in the {@link WorkUnit}.
 * 
 * @author calder
 *
 */
public class GridExecutionException extends GridException {

	/**
	 * 
	 */
	public GridExecutionException() {
		super();
	}

	/**
	 * @param message
	 */
	public GridExecutionException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public GridExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

}
