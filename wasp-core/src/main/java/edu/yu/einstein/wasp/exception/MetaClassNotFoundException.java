package edu.yu.einstein.wasp.exception;

public class MetaClassNotFoundException extends RuntimeException {
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
