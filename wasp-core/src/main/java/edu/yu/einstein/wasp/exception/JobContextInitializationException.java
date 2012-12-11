package edu.yu.einstein.wasp.exception;

public class JobContextInitializationException extends WaspException {

	public JobContextInitializationException(){
		super();
	}
		
	public JobContextInitializationException(String message){
		super(message);
	}
	
	public JobContextInitializationException(String message, Throwable cause){
		super(message, cause);
	}
	
}
