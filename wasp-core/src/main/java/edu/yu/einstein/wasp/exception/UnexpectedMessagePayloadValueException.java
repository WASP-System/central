package edu.yu.einstein.wasp.exception;

/**
 * Exception thrown during failure to build WASP messages
 * @author asmclellan
 *
 */
public class UnexpectedMessagePayloadValueException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5692091449415070641L;

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
