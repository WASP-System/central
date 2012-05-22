package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleTypeException extends SampleException{
	
	public SampleTypeException(){
		super();
	}
		
	public SampleTypeException(String message){
		super(message);
	}
	
	public SampleTypeException(String message, Throwable cause){
		super(message, cause);
	}
}
