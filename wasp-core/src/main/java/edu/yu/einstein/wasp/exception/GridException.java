/**
 * 
 */
package edu.yu.einstein.wasp.exception;

import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;

/**
 * @author calder
 *
 */
public class GridException extends WaspException {
	
	public GridException(){
		super();
	}
		
	public GridException(String message){
		super(message);
	}
	
	public GridException(String message, Throwable cause){
		super(message, cause);
	}

}
