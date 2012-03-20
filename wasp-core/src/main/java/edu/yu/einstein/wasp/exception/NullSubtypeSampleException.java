package edu.yu.einstein.wasp.exception;

public class NullSubtypeSampleException extends WaspRuntimeException {
	public NullSubtypeSampleException(){
		super();
	}
		
	public NullSubtypeSampleException(String message){
		super(message);
	}
	
	public NullSubtypeSampleException(String message, Throwable cause){
		super(message, cause);
	}
}