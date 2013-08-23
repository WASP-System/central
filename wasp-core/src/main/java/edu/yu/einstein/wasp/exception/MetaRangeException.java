package edu.yu.einstein.wasp.exception;

public class MetaRangeException extends WaspRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1338923470791612803L;

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
