/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * GridWorkService defines an interface for doing work on remote servers over a {@link GridTransportConnection}.
 * Implementations should handle their own configuration and authentication.
 * 
 * @author calder
 *
 */
public interface GridWorkService {
	
	/**
	 * Configure the underlying mechanism for transmitting of work exec.
	 * @param ts
	 */
	public void setTransportService(GridTransportService ts);
	
	/**
	 * 
	 * @return
	 */
	public GridTransportService getTransportService();
	
	/**
	 * Execute a {@link WorkUnit}.
	 * @param w
	 * @return
	 * @throws GridAccessException
	 * @throws GridUnresolvableHostException
	 * @throws GridExecutionException 
	 */
	public GridResult execute(WorkUnit w) throws GridAccessException, GridUnresolvableHostException, GridExecutionException;
	
	/**
	 * Tests to see if the particular {@link GridWorkService} execution is still running. Throws a @{link GridException}
	 * if it is not running and did not complete.
	 * @param g
	 * @return
	 * @throws GridUnresolvableHostException 
	 * @throws GridException
	 */
	public boolean isFinished(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException;

}
