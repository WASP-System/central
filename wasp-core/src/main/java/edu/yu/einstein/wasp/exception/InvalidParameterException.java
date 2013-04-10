package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP parameter exceptions
 * @author asmclellan
 *
 */
public class InvalidParameterException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1820303546324198526L;

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
