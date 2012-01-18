package edu.yu.einstein.wasp.exception;

public class NullResourceCategoryException extends RuntimeException {
	public NullResourceCategoryException(){
		super();
	}
		
	public NullResourceCategoryException(String message){
		super(message);
	}
	
	public NullResourceCategoryException(String message, Throwable cause){
		super(message, cause);
	}
}