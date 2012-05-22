package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleDuplicationException extends SampleException{
	
	public SampleDuplicationException(){
		super();
	}
		
	public SampleDuplicationException(String message){
		super(message);
	}
	
	public SampleDuplicationException(String message, Throwable cause){
		super(message, cause);
	}
}
