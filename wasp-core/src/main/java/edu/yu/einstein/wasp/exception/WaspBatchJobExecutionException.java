package edu.yu.einstein.wasp.exception;

public class WaspBatchJobExecutionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 944343372937871262L;

	public WaspBatchJobExecutionException(){
		super();
	}
		
	public WaspBatchJobExecutionException(String message){
		super(message);
	}
	
	public WaspBatchJobExecutionException(String message, Throwable cause){
		super(message, cause);
	}
}
