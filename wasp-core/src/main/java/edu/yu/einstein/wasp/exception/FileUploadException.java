package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class FileUploadException extends WaspRuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 78189755368892196L;

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
