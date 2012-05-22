package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleMultiplexException extends SampleException{
	
	public SampleMultiplexException(){
		super();
	}
		
	public SampleMultiplexException(String message){
		super(message);
	}
	
	public SampleMultiplexException(String message, Throwable cause){
		super(message, cause);
	}
}
