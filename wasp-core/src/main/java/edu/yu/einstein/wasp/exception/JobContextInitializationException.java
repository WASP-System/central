package edu.yu.einstein.wasp.exception;

public class JobContextInitializationException extends WaspException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6397931516449303943L;

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
