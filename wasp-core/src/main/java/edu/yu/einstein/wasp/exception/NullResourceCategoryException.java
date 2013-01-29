package edu.yu.einstein.wasp.exception;

public class NullResourceCategoryException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5303437038349491365L;

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