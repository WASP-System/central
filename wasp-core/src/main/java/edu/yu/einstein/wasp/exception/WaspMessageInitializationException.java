package edu.yu.einstein.wasp.exception;

public class WaspMessageInitializationException extends RuntimeException {
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
