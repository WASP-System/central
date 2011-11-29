package edu.yu.einstein.wasp.exception;

public class LoginNameException extends WaspException {
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