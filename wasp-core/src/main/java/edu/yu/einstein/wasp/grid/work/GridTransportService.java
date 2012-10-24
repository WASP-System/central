package edu.yu.einstein.wasp.grid.work;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * Interface for defining mechanism for obtaining {@link GridTransportConnection}s.  The transport services
 * should manage all of their own authentication and discover remote hosts using the {@link GridHostResolver}.
 * 
 * @author calder
 *
 */
public interface GridTransportService {
	
	/**
	 * Connect a work unit to an execution host using the {@link GridHostResolver}.  This is the typical
	 * use case where the GridHostResolver chooses the destination for the job.
	 * 
	 * @param w
	 * @throws GridUnresolvableHostException 
	 * @throws GridException
	 */
	public void connect(WorkUnit w) throws GridAccessException, GridUnresolvableHostException;
	
	/**
	 * Obtain a connection to a known host.  The primary use of this function is to do work on a host that is
	 * independent of the {@link GridWorkService} implementation.
	 * 
	 * @param hostname
	 * @return
	 * @throws GridUnresolvableHostException 
	 */
	public GridTransportConnection connect(String hostname) throws GridAccessException, GridUnresolvableHostException;
	
	/**
	 * @return
	 */
	public GridHostResolver getHostResolver();
	
	/**
	 * configure a bean for resolving versions of software
	 * @param swm
	 */
	public void setSoftwareManager(SoftwareManager swm);
	
	public SoftwareManager getSoftwareManager();
	
	public String getConfiguredSetting(String key);

}
