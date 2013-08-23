package edu.yu.einstein.wasp.exception;

public class NullSampleSubtypeException extends WaspRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4211613317679290862L;

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