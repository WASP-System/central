package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class ModelCopyException extends Exception{
	
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
