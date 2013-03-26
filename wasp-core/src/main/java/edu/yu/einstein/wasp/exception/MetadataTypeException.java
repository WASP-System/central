package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class MetadataTypeException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9214860925142026854L;

	public MetadataTypeException(){
		super();
	}
		
	public MetadataTypeException(String message){
		super(message);
	}
	
	public MetadataTypeException(String message, Throwable cause){
		super(message, cause);
	}
}
