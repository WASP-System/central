package edu.yu.einstein.wasp.exception;

public class NullTypeResourceException extends WaspRuntimeException {
	public NullTypeResourceException(){
		super();
	}
		
	public NullTypeResourceException(String message){
		super(message);
	}
	
	public NullTypeResourceException(String message, Throwable cause){
		super(message, cause);
	}
}