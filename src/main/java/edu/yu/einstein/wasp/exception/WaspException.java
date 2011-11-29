package edu.yu.einstein.wasp.exception;

public class WaspException extends Exception {
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
