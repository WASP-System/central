package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class MetadataTypeException extends Exception{
	
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
