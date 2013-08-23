package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author asmclellan
 *
 */
public class FileDownloadException extends WaspException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6253124859872377745L;

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
