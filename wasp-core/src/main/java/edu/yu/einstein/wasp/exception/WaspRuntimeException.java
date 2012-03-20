package edu.yu.einstein.wasp.exception;

public class WaspRuntimeException extends RuntimeException {
	public WaspRuntimeException(){
		super();
	}
		
	public WaspRuntimeException(String message){
		super(message);
	}
	
	public WaspRuntimeException(String message, Throwable cause){
		super(message, cause);
	}
}
