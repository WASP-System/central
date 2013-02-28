package edu.yu.einstein.wasp.exception;

public class FormParameterException extends WaspException {

	private static final long serialVersionUID = 3711642725767540004L;

	public FormParameterException() {
		
	}

	public FormParameterException(String message) {
		super(message);
		
	}

	public FormParameterException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
