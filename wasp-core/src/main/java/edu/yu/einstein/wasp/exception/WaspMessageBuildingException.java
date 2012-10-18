package edu.yu.einstein.wasp.exception;

/**
 * Exception thrown during failure to build WASP messages
 * @author andymac
 *
 */
public class WaspMessageBuildingException extends Exception {

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
