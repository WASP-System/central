package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleException extends Exception{
	
	public SampleException(){
		super();
	}
		
	public SampleException(String message){
		super(message);
	}
	
	public SampleException(String message, Throwable cause){
		super(message, cause);
	}
}
