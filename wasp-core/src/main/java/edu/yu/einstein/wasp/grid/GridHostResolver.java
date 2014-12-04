package edu.yu.einstein.wasp.grid;

import java.util.List;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;

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
	public String getHostname(WorkUnitGridConfiguration c) throws GridUnresolvableHostException;
	
	/**
	 * Returns the username in use at the destination host.  Username/hostname should be stable for
	 * a given {@link WorkUnit}.
	 * @param w
	 * @return
	 * @throws GridUnresolvableHostException
	 */
	public String getUsername(WorkUnitGridConfiguration c) throws GridUnresolvableHostException;
	
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
	 * @throws GridUnresolvableHostException 
	 */
	public GridWorkService getGridWorkService(WorkUnitGridConfiguration c) throws GridUnresolvableHostException;
	
	/**
	 * Get the work service that is working on the result
	 * @param w
	 * @return
	 * @throws GridUnresolvableHostException 
	 */
	public GridWorkService getGridWorkService(GridResult r) throws GridUnresolvableHostException;
	
	/**
	 * Get the work service from a FQDN.  This should be done only when the host is truly known, e.g. 
	 * when there is a file operation on a particular host.  Normally, you should let the 
	 * GridHostResolver inspect the work unit and determine the host for itself.
	 * 
	 * @param hostname
	 * @return
	 */
	public GridWorkService getGridWorkService(String hostname) throws GridUnresolvableHostException;
	
	/**
	 * get {@link GridWorkSercice}s.
	 * @param gws
	 */
	@SuppressWarnings("rawtypes")
	public List getAvailableWorkServices();
	
	/**
	 * return an appropriate parallel environment string for the host. Should be requested only when required.
	 * @param w
	 * @return string or null
	 */
	public String getParallelEnvironmentString(WorkUnitGridConfiguration c);
	
	/**
	 * Pass through method to execute a {@link WorkUnit}.
	 * @param w
	 * @return
	 * @throws GridAccessException
	 * @throws GridUnresolvableHostException
	 * @throws GridExecutionException 
	 * @throws GridException 
	 */
	public GridResult execute(WorkUnit w) throws GridException;
	
	/**
	 * Pass through method to Test to see if the particular {@link GridWorkService} execution is still running. Throws a @{link GridException}
	 * if it is not running or did not complete.
	 * @param g
	 * @return
	 * @throws GridUnresolvableHostException 
	 * @throws GridException
	 */
	public boolean isFinished(GridResult g) throws GridException;
	
	
	/*
	 * Provision a FileHandle to a remote WorkService
	 * 
	 * @param file
	 * @param destination
	 * @throws GridException
	 */
	//public void move(FileHandle file, GridWorkService destination) throws GridException;
	
	/*
	 * Provision a FileGroup to a remote WorkService
	 * 
	 * @param file
	 * @param destination
	 * @throws GridException
	 */
	//public void move(FileGroup file, GridWorkService destination) throws GridException;
	
	
	/*
	 * Instances of GridHostResolver must declare the following method as abstract.  This provides a mechanism for 
	 * obtaining {@link WorkUnit}s that are configured with a runId from job parameters at runtime using
	 * Spring lookup method injection. The WorkUnit prototype bean is declared in daemon-common-config.xml.
	 * 
	 * @return
	 */
	//public abstract WorkUnit createWorkUnit();
	
}
