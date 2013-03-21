package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class FileMoveException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6364663450908194686L;

	public FileMoveException(){
		super();
	}
		
	public FileMoveException(String message){
		super(message);
	}
	
	public FileMoveException(String message, Throwable cause){
		super(message, cause);
	}
}
