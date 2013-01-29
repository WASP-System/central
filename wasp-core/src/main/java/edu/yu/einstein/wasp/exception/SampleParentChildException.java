package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class SampleParentChildException extends SampleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1960235309685236141L;

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
