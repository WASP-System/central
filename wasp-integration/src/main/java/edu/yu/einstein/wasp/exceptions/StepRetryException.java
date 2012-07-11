package edu.yu.einstein.wasp.exceptions;

/**
 * Exception thrown during failure to build WASP messages
 * @author andymac
 *
 */
public class StepRetryException extends Exception {

	/**
	 * @param arg0
	 */
	public StepRetryException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public StepRetryException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StepRetryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
