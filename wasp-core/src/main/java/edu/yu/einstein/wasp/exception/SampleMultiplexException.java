package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class SampleMultiplexException extends SampleException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7380478181996350278L;

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
