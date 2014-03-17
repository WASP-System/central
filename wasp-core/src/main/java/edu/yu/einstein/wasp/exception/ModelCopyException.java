package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class ModelCopyException extends WaspException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6856575697596625297L;

	public ModelCopyException(){
		super();
	}
		
	public ModelCopyException(String message){
		super(message);
	}
	
	public ModelCopyException(String message, Throwable cause){
		super(message, cause);
	}
}
