/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;

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
	 * @throws MisconfiguredWorkUnitException 
	 * @throws GridException
	 */
	public GridResult sendExecToRemote(WorkUnit w) throws GridAccessException, GridExecutionException, GridUnresolvableHostException, MisconfiguredWorkUnitException;
	
	/*
	 * former gridtransportservice methods
	 */
		
	/**
	 * Each transport connection should have access to a configured {@link SoftwareManager} in order to get software into the current path.
	 * @return
	 */
	public SoftwareManager getSoftwareManager();
	
	/**
	 * Key-value access to configured server settings
	 * @param key
	 * @return
	 */
	public String getConfiguredSetting(String key);
	
	/**
	 * Does an absolute path represent a path starting at the home directory?
	 * @return
	 */
	public boolean isUserDirIsRoot();

	/**
	 * get the short name of the host (the host doesn't have to know...)
	 * @return
	 */
	public String getName();

	/**
	 * get the FQDN of the host
	 * @return
	 */
	public String getHostName();

	/**
	 * get the username for logging into this host
	 * @return
	 */
	abstract String getUserName();

	/**
	 * get the SSH identity key file path (absolute)
	 * @return
	 */
	abstract File getIdentityFile();
	
	/**
	 * Prefix a file path so that it can be understood on the remote host
	 * @param filespec
	 * @return
	 */
	public String prefixRemoteFile(String filespec);
	
	/**
	 * prefix a path from a URI
	 * @param uri
	 * @return
	 * @throws FileNotFoundException
	 * @throws GridUnresolvableHostException
	 */
	public String prefixRemoteFile(URI uri) throws FileNotFoundException, GridUnresolvableHostException;
	

}
