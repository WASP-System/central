package edu.yu.einstein.wasp.exception;

public class TaskletWaitTimeoutException extends WaspException {

	private static final long serialVersionUID = -2045600269036455595L;

	public TaskletWaitTimeoutException() {
		// TODO Auto-generated constructor stub
	}

	public TaskletWaitTimeoutException(String message) {
		super(message);
	}

	public TaskletWaitTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public TaskletWaitTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TaskletWaitTimeoutException(Throwable cause) {
		super(cause);
	}

}
