package edu.yu.einstein.wasp.exception;

public class WaspException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6840569920810982247L;

	public WaspException(){
		super();
	}
		
	public WaspException(String message){
		super(message);
	}
	
	public WaspException(String message, Throwable cause){
		super(message, cause);
	}

	public WaspException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public WaspException(Throwable cause) {
		super(cause);
	}
	
}
