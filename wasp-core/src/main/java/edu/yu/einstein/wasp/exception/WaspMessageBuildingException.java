package edu.yu.einstein.wasp.exception;

/**
 * Exception thrown during failure to build WASP messages
 * @author asmclellan
 *
 */
public class WaspMessageBuildingException extends WaspException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6883849114144054696L;

	/**
	 * 
	 */
	public WaspMessageBuildingException() {
		
	}

	/**
	 * @param arg0
	 */
	public WaspMessageBuildingException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public WaspMessageBuildingException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WaspMessageBuildingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
