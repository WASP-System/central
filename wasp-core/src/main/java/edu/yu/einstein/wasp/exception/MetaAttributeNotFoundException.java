package edu.yu.einstein.wasp.exception;

public class MetaAttributeNotFoundException extends WaspException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8768014645295785120L;

	public MetaAttributeNotFoundException(){
		super();
	}
		
	public MetaAttributeNotFoundException(String message){
		super(message);
	}
	
	public MetaAttributeNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}