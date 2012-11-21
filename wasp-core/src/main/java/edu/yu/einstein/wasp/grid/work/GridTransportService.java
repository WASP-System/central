package edu.yu.einstein.wasp.grid.work;

import java.io.File;

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
	 * configure a bean for resolving versions of software
	 * @param swm
	 */
	public void setSoftwareManager(SoftwareManager swm);
	
	public SoftwareManager getSoftwareManager();
	
	public String getConfiguredSetting(String key);
	
	public boolean isUserDirIsRoot();
	
	public void setUserDirIsRoot(boolean isRoot);
	
	public void setName(String name);
	public String getName();
	public void setHostName(String hostname);
	public String getHostName();
	public void setUserName(String username);
	public String getUserName();
	public void setIdentityFile(String identityFile);
	public File getIdentityFile();

}
