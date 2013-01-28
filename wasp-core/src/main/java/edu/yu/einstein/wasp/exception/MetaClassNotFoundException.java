package edu.yu.einstein.wasp.exception;

public class MetaClassNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4413895880208207296L;

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
