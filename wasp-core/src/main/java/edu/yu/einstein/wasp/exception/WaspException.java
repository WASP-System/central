package edu.yu.einstein.wasp.exception;

public class WaspException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -776844035219169113L;

	public WaspException(){
		super();
	}
		
	public WaspException(String message){
		super(message);
	}
	
	public WaspException(String message, Throwable cause){
		super(message, cause);
	}
}
