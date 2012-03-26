package edu.yu.einstein.wasp.exception;

public class NullSampleSubtypeException extends RuntimeException {
	public NullSampleSubtypeException(){
		super();
	}
		
	public NullSampleSubtypeException(String message){
		super(message);
	}
	
	public NullSampleSubtypeException(String message, Throwable cause){
		super(message, cause);
	}
}