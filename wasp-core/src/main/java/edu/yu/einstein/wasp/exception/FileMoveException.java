package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class FileMoveException extends Exception{
	
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
