package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class ModelDetachException extends Exception{
	
	public ModelDetachException(){
		super();
	}
		
	public ModelDetachException(String message){
		super(message);
	}
	
	public ModelDetachException(String message, Throwable cause){
		super(message, cause);
	}
}
