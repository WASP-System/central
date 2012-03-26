package edu.yu.einstein.wasp.exception;

public class NullResourceTypeException extends RuntimeException {
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