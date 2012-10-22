package edu.yu.einstein.wasp.exception;


/**
 * @author andymac
 *
 */
public class ParameterValueRetrievalException extends Exception {

	public ParameterValueRetrievalException(String arg0) {
		super(arg0);
	}
	
	ParameterValueRetrievalException(String arg0, Throwable arg1){
		super(arg0, arg1);
	}
	
	ParameterValueRetrievalException(Throwable arg0){
		super(arg0);
	}

	
	public ParameterValueRetrievalException() {
		super();
	}

}
