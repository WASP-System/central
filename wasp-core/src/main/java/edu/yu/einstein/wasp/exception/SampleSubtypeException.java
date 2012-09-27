package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleSubtypeException extends SampleException{
	
	public SampleSubtypeException(){
		super();
	}
		
	public SampleSubtypeException(String message){
		super(message);
	}
	
	public SampleSubtypeException(String message, Throwable cause){
		super(message, cause);
	}
}
