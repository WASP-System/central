package edu.yu.einstein.wasp.exception;

public class UiFieldParseException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7884256644566312001L;

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