package edu.yu.einstein.wasp.exception;

public class NullResourceTypeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5543805355698088521L;

	public NullResourceTypeException(){
		super();
	}
		
	public NullResourceTypeException(String message){
		super(message);
	}
	
	public NullResourceTypeException(String message, Throwable cause){
		super(message, cause);
	}
}