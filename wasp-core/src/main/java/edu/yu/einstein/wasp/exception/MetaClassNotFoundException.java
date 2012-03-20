package edu.yu.einstein.wasp.exception;

public class MetaClassNotFoundException extends WaspRuntimeException {
	public MetaClassNotFoundException(){
		super();
	}
		
	public MetaClassNotFoundException(String message){
		super(message);
	}
	
	public MetaClassNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
