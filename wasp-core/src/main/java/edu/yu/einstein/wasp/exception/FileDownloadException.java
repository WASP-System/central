package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class FileDownloadException extends Exception{
	
	public FileDownloadException(){
		super();
	}
		
	public FileDownloadException(String message){
		super(message);
	}
	
	public FileDownloadException(String message, Throwable cause){
		super(message, cause);
	}
}
