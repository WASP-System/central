package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class MetadataException extends WaspException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4956616247952542676L;

	public MetadataException(){
		super();
	}
		
	public MetadataException(String message){
		super(message);
	}
	
	public MetadataException(String message, Throwable cause){
		super(message, cause);
	}
}
