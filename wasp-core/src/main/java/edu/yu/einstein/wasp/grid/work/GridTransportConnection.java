/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * Interface that defines a mechanism for transporting commands either to the local machine or some remote machine.
 * Each work unit gets its own configured GridTransportConnection.
 * 
 * @author calder
 *
 */
public interface GridTransportConnection {
	
	/**
	 * Intended to be called by the GridWorkService.
	 * 
	 * @param s
	 * @throws GridUnresolvableHostException 
	 * @throws GridException
	 */
	public GridResult sendExecToRemote(WorkUnit w) throws GridAccessException, GridUnresolvableHostException;
	
	public void disconnect() throws GridAccessException;

}
