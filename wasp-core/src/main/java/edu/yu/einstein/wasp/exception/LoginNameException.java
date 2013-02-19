package edu.yu.einstein.wasp.exception;

public class LoginNameException extends WaspException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6731130495969937495L;

	public LoginNameException(){
		super();
	}
		
	public LoginNameException(String message){
		super(message);
	}
	
	public LoginNameException(String message, Throwable cause){
		super(message, cause);
	}
}