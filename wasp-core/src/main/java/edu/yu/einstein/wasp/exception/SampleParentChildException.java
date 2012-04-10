package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleParentChildException extends Exception{
	
	public SampleParentChildException(){
		super();
	}
		
	public SampleParentChildException(String message){
		super(message);
	}
	
	public SampleParentChildException(String message, Throwable cause){
		super(message, cause);
	}
}
