package edu.yu.einstein.wasp.exception;

public class ModelIdException extends WaspException {

	private static final long serialVersionUID = 6584767379716552795L;

	public ModelIdException() {
		
	}

	public ModelIdException(String message) {
		super(message);
	}

	public ModelIdException(String message, Throwable cause) {
		super(message, cause);
	}

}
