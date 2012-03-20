package edu.yu.einstein.wasp.exception;

public class MetaRangeException extends WaspRuntimeException {
	public MetaRangeException(){
		super();
	}
		
	public MetaRangeException(String message){
		super(message);
	}
	
	public MetaRangeException(String message, Throwable cause){
		super(message, cause);
	}
}
