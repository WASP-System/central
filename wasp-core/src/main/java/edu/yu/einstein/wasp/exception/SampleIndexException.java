package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleIndexException extends SampleException{
	
	public SampleIndexException(){
		super();
	}
		
	public SampleIndexException(String message){
		super(message);
	}
	
	public SampleIndexException(String message, Throwable cause){
		super(message, cause);
	}
}
