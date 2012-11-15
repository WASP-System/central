package edu.yu.einstein.wasp.exception;

public class WaspBatchJobExecutionException extends Exception {
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
