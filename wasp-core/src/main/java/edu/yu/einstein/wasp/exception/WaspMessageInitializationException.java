package edu.yu.einstein.wasp.exception;

public class WaspMessageInitializationException extends WaspRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9130420184417963265L;

	public WaspMessageInitializationException(){
		super();
	}
		
	public WaspMessageInitializationException(String message){
		super(message);
	}
	
	public WaspMessageInitializationException(String message, Throwable cause){
		super(message, cause);
	}
}
