package edu.yu.einstein.wasp.exception;

/**
 * Exception thrown during failure to build WASP messages
 * @author andymac
 *
 */
public class UnexpectedMessagePayloadValueException extends Exception {

	/**
	 * 
	 */
	public UnexpectedMessagePayloadValueException() {
		
	}

	/**
	 * @param arg0
	 */
	public UnexpectedMessagePayloadValueException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public UnexpectedMessagePayloadValueException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public UnexpectedMessagePayloadValueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
