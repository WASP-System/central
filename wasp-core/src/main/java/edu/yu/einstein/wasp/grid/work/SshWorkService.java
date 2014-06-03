package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
/**
 * Service that implements both the {@link GridTransportService} and {@link GridWorkService} interfaces.  
 * Provides methods to execute basic shell commands (GridWorkService) over an key authenticated
 * SSH connection (GridTransportService). 
 * 
 * @author calder
 *
 */
@Deprecated
@SuppressWarnings("unused")
public class SshWorkService implements GridWorkService {
	
	/**
	 * Target source pool
	 */
	private GridTransportConnection transportConnection;
	
	public SshWorkService(GridTransportConnection transportConnection) {
		this.transportConnection = transportConnection;
		logger.debug("configured transport service: " + transportConnection.getUserName() + "@" + transportConnection.getHostName());
	}
	
	private String hostKeyChecking = "no";
	private static File identityFile;
	
	private String name;
	
	private Map<String, String> settings = new HashMap<String, String>();
	
	private SoftwareManager softwareManager;
	
	private GridFileService gridFileService;
	
	private boolean userDirIsRoot = true;
	
	// not used in ssh only implementation
	private String jobNamePrefix;
	
	private Properties localProperties;

	public Properties getLocalProperties() {
		return localProperties;
	}
	
	//TODO: implement bash environment variables, see SgeWorkService
	

	public void setLocalProperties(Properties waspSiteProperties) {
		this.localProperties = waspSiteProperties;
		String prefix = this.name + ".settings.";
		for (String key : this.localProperties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				String newKey = key.replaceFirst(prefix, "");
				String value = this.localProperties.getProperty(key);
				settings.put(newKey, value);
				logger.debug("Configured setting for host \""
						+ this.name + "\": " + newKey + "=" + value);
			}
		}
	}
	
	private static final Logger logger = LoggerFactory.getLogger(SshWorkService.class);

	@Override
	public boolean isFinished(GridResult g) throws GridAccessException {
		return true;
	}

	@Override
	public GridResult execute(WorkUnit w) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.debug("attempting to execute " + w.getCommand());
		
		GridResult result;
		try {
			result = transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridAccessException(e.getLocalizedMessage(),e);
		}
		return result;
		
	}

	
	public void setHostKeyChecking(String s) {
		if (s == "yes" || s == "true") {
			hostKeyChecking = "yes";
		}
	}
	
	public void setIdentityFile(String s) {
		String home = System.getProperty("user.home");
		s = home + s.replaceAll("~(.*)", "$1");
		identityFile = new File(s).getAbsoluteFile();
	}

	@Override
	public void setJobNamePrefix(String name) {
		this.jobNamePrefix = name;
	}

	@Override
	public void setAvailableParallelEnvironments(List<String> pe) {
		// MPI PE not available in direct ssh mode 
	}

	@Override
	public List<String> getAvailableParallelEnvironments() {
		// MPI PE not available in direct ssh mode
		return new ArrayList<String>();
	}

	@Override
	public GridFileService getGridFileService() {
		return gridFileService;
	}

	@Override
	public void setGridFileService(GridFileService gridFileService) {
		this.gridFileService = gridFileService;
		
	}

	@Override
	public GridTransportConnection getTransportConnection() {
		return transportConnection;
	}

	@Override
	public LinkedHashMap<Integer, String> getMappedTaskOutput(GridResult r) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResultStdOut(GridResult r) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResultStdErr(GridResult r) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUnregisteredFileContents(GridResult r, String filename) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
