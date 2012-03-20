package edu.yu.einstein.wasp.exception;

public class NullResourceException extends WaspRuntimeException {
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