package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class SampleTypeException extends SampleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4864721268959005129L;

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
