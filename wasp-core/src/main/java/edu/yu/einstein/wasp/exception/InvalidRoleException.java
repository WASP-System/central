package edu.yu.einstein.wasp.exception;

public class InvalidRoleException extends WaspRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7378593212602883826L;

	public InvalidRoleException(){
		super();
	}
		
	public InvalidRoleException(String message){
		super(message);
	}
	
	public InvalidRoleException(String message, Throwable cause){
		super(message, cause);
	}
}
