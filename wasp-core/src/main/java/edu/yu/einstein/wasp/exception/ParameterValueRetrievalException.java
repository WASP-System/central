package edu.yu.einstein.wasp.exception;


/**
 * @author asmclellan
 *
 */
public class ParameterValueRetrievalException extends WaspException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7599675746968882039L;


	public ParameterValueRetrievalException(String arg0) {
		super(arg0);
	}
	
	public ParameterValueRetrievalException(String arg0, Throwable arg1){
		super(arg0, arg1);
	}
	
	public ParameterValueRetrievalException(Throwable arg0){
		super(arg0);
	}

	
	public ParameterValueRetrievalException() {
		super();
	}

}
