package edu.yu.einstein.wasp.exception;

public class WaspMessageChannelNotFoundException extends RuntimeException {
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
