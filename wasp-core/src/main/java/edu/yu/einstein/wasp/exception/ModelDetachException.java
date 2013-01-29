package edu.yu.einstein.wasp.exception;

/**
 * Reporting of WASP metadata exceptions
 * @author andymac
 *
 */
public class ModelDetachException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4986686206350471948L;

	public ModelDetachException(){
		super();
	}
		
	public ModelDetachException(String message){
		super(message);
	}
	
	public ModelDetachException(String message, Throwable cause){
		super(message, cause);
	}
}
