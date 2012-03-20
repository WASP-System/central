package edu.yu.einstein.wasp.exception;

public class UiFieldParseException extends RuntimeException {
	public UiFieldParseException(){
		super();
	}
		
	public UiFieldParseException(String message){
		super(message);
	}
	
	public UiFieldParseException(String message, Throwable cause){
		super(message, cause);
	}
}