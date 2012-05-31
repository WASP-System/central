package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class FileUploadException extends Exception{
	
	public FileUploadException(){
		super();
	}
		
	public FileUploadException(String message){
		super(message);
	}
	
	public FileUploadException(String message, Throwable cause){
		super(message, cause);
	}
}
