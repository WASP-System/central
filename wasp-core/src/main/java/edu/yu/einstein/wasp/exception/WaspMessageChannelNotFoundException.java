package edu.yu.einstein.wasp.exception;

public class WaspMessageChannelNotFoundException extends WaspRuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 831964799200345424L;

	public WaspMessageChannelNotFoundException(){
		super();
	}
		
	public WaspMessageChannelNotFoundException(String message){
		super(message);
	}
	
	public WaspMessageChannelNotFoundException(String message, Throwable cause){
		super(message, cause);
	}
}
