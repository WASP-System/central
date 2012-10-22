package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author rdubin
 *
 */
public class RunException extends Exception{
	
	public RunException(){
		super();
	}
		
	public RunException(String message){
		super(message);
	}
	
	public RunException(String message, Throwable cause){
		super(message, cause);
	}
}
