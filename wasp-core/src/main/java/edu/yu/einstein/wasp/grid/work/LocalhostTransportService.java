/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.SingleHostResolver;

/**
 * Implementation of GridTransportService that simply exec's on the local host.
 * 
 * @author calder
 *
 */
public class LocalhostTransportService implements GridTransportService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private SoftwareManager softwareManager;
	
	private boolean userDirIsRoot;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(WorkUnit w) throws GridAccessException {
		logger.debug("connect to localhost called");
		w.setConnection(new LocalhostTransportConnection());
	}

	@Override
	public void setSoftwareManager(SoftwareManager swm) {
		this.softwareManager = swm;
	}

	@Override
	public SoftwareManager getSoftwareManager() {
		return softwareManager;
	}

	@Override
	public String getConfiguredSetting(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserDirIsRoot() {
		return userDirIsRoot;
	}

	@Override
	public void setUserDirIsRoot(boolean isRoot) {
		this.userDirIsRoot = isRoot;
	}

	@Override
	public void setName(String name) {}

	@Override
	public void setHostName(String hostname) {}

	@Override
	public void setUserName(String username) {}

	@Override
	public void setIdentityFile(String identityFile) {}

	@Override
	public String getName() {
		return "localhost";
	}

	@Override
	public String getHostName() {
		return "localhost";
	}

	@Override
	public String getUserName() {
		return System.getProperty("user.name");
	}

	@Override
	public File getIdentityFile() {
		return null;
	}

	@Override
	public String prefixRemoteFile(String filespec) {
		String prefix = "";
		if (isUserDirIsRoot()) prefix = "$HOME/";
		String retval = prefix + filespec;
		return retval.replaceAll("//", "/");
	}

}
