package edu.yu.einstein.wasp.exception;

/**
 * Exception thrown during failure to build WASP messages
 * @author andymac
 *
 */
public class StatusMetaMessagingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7251670189324084012L;

	/**
	 * 
	 */
	public StatusMetaMessagingException() {
		
	}

	/**
	 * @param arg0
	 */
	public StatusMetaMessagingException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public StatusMetaMessagingException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public StatusMetaMessagingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
