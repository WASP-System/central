package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author rdubin
 *
 */
public class RunException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1534779448596947231L;

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
