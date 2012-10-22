package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP parameter exceptions
 * @author andymac
 *
 */
public class InvalidParameterException extends RuntimeException{
	
	public InvalidParameterException(){
		super();
	}
		
	public InvalidParameterException(String message){
		super(message);
	}
	
	public InvalidParameterException(String message, Throwable cause){
		super(message, cause);
	}
}
