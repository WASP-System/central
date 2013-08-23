package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author rdubin
 *
 */
public class ResourceException extends WaspException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2205022157280444376L;

	public ResourceException(){
		super();
	}
		
	public ResourceException(String message){
		super(message);
	}
	
	public ResourceException(String message, Throwable cause){
		super(message, cause);
	}
}
