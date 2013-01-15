package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

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
	
	abstract void setName(String name);
	public String getName();
	abstract void setHostName(String hostname);
	public String getHostName();
	abstract void setUserName(String username);
	abstract String getUserName();
	abstract void setIdentityFile(String identityFile);
	abstract File getIdentityFile();
	
	public String prefixRemoteFile(String filespec) throws FileNotFoundException;
	public String prefixRemoteFile(URI uri) throws FileNotFoundException, GridUnresolvableHostException;

}
