package edu.yu.einstein.wasp.grid;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportService;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;

/**
 * Mechanism for fine grained control over where remote jobs are sent.  GridHostResolvers need to implement this 
 * interface and extend the AbstractGridHostResolver class.
 * 
 * @author calder
 *
 */
public interface GridHostResolver {
	
	/**
	 * Returns the name of the host for which the unit of work is to be sent.  
	 * The results of this function should be stable: it is the GridHostResolver's responsibility.
	 * (eg. a pure round-robin approach could create unexpected results).
	 * 
	 * GridHostResolvers need to be able to handle the {@link WorkUnit}s executionEnvironments, which
	 * and implement a strategy for when the host can not be determined (see {@link AbstractGridHostResolver}.
	 * 
	 * @param w
	 * @return
	 */
	public String getHostname(WorkUnit w) throws GridUnresolvableHostException;
	
	/**
	 * Returns the username in use at the destination host.  Username/hostname should be stable for
	 * a given {@link WorkUnit}.
	 * @param w
	 * @return
	 * @throws GridUnresolvableHostException
	 */
	public String getUsername(WorkUnit w) throws GridUnresolvableHostException;
	
	/**
	 * Returns the username at the given host.
	 * @param hostname
	 * @return
	 * @throws GridUnresolvableHostException
	 */
	public String getUsername(String hostname) throws GridUnresolvableHostException;
	
	
		
	/**
	 * Given a work unit, return the appropriate work service.  
	 * @param w
	 */
	public GridWorkService getGridWorkService(WorkUnit w);
	
	/**
	 * get {@link GridWorkSercice}s.
	 * @param gws
	 */
	public List getAvailableWorkServices();
	
	/**
	 * return an appropriate parallel environment string for the host. Should be requested only when required.
	 * @param w
	 * @return string or null
	 */
	public String getParallelEnvironmentString(WorkUnit w);
	
	/**
	 * Pass through method to execute a {@link WorkUnit}.
	 * @param w
	 * @return
	 * @throws GridAccessException
	 * @throws GridUnresolvableHostException
	 * @throws GridExecutionException 
	 */
	public GridResult execute(WorkUnit w) throws GridAccessException, GridUnresolvableHostException, GridExecutionException;
	
	/**
	 * Pass through method to Test to see if the particular {@link GridWorkService} execution is still running. Throws a @{link GridException}
	 * if it is not running or did not complete.
	 * @param g
	 * @return
	 * @throws GridUnresolvableHostException 
	 * @throws GridException
	 */
	public boolean isFinished(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException;
	
}
