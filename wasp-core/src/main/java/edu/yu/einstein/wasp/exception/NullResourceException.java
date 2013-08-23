package edu.yu.einstein.wasp.exception;

public class NullResourceException extends WaspRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3183216884295253644L;

	public NullResourceException(){
		super();
	}
		
	public NullResourceException(String message){
		super(message);
	}
	
	public NullResourceException(String message, Throwable cause){
		super(message, cause);
	}
}