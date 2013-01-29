package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleDuplicationException extends SampleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3469138157771399295L;

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
