package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class SampleIndexException extends SampleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8039737549313881599L;

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
