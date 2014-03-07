package edu.yu.einstein.wasp.exception;

public class MetadataRuntimeException extends WaspRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2488156905418259419L;

	public MetadataRuntimeException() {
		
	}

	public MetadataRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MetadataRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MetadataRuntimeException(String message) {
		super(message);
	}

	public MetadataRuntimeException(Throwable cause) {
		super(cause);
	}

}
