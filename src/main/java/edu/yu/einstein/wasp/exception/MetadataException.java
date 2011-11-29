package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class MetadataException extends Exception{
	
	public MetadataException(){
		super();
	}
		
	public MetadataException(String message){
		super(message);
	}
	
	public MetadataException(String message, Throwable cause){
		super(message, cause);
	}
}
