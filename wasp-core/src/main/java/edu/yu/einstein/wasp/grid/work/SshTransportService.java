package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.UserInfo;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * Service that implements both the {@link GridTransportService} and
 * {@link GridWorkService} interfaces. Provides methods to execute basic shell
 * commands (GridWorkService) over an key authenticated SSH connection
 * (GridTransportService).
 * 
 * @author calder
 * 
 */
public class SshTransportService implements GridTransportService {

	private String identityFileName;
	private static File identityFile;

	private String name;
	private String hostname;
	private String username;

	private Map<String, String> settings = new HashMap<String, String>();

	private SoftwareManager softwareManager;

	private boolean userDirIsRoot = true;

	private Properties localProperties;

	public Properties getLocalProperties() {
		return localProperties;
	}

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

	private static final Logger logger = LoggerFactory.getLogger(SshTransportService.class);

	@Override
	public void connect(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		SshTransportConnection stc = new SshTransportConnection(this, w);
	}

	private static class SshUserInfo implements UserInfo {

		@Override
		public String getPassphrase() {
			// not implemented
			return null;
		}

		@Override
		public String getPassword() {
			// not implemented
			return null;
		}

		@Override
		public boolean promptPassphrase(String arg0) {
			// not implemented
			return false;
		}

		@Override
		public boolean promptPassword(String arg0) {
			// not implemented
			return false;
		}

		@Override
		public boolean promptYesNo(String arg0) {
			// not implemented
			return false;
		}

		@Override
		public void showMessage(String arg0) {
			// not implemented
		}

	}

	public void setIdentityFileName(String s) {
		this.identityFileName = s;
		setIdentityFile(s);
	}
	
	public String getIdentityFileName() {
		return identityFileName;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setSoftwareManager(SoftwareManager softwareManager) {
		this.softwareManager = softwareManager;

	}

	@Override
	public SoftwareManager getSoftwareManager() {
		return softwareManager;
	}

	@Override
	public String getConfiguredSetting(String key) {
		return settings.get(key);
	}

	@Override
	public boolean isUserDirIsRoot() {
		return userDirIsRoot;
	}

	@Override
	public void setHostName(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public String getHostName() {
		return hostname;
	}

	@Override
	public void setUserName(String username) {
		this.username = username;
	}

	@Override
	public String getUserName() {
		return username;
	}

	@Override
	public File getIdentityFile() {
		return identityFile;
	}

	@Override
	public void setUserDirIsRoot(boolean isRoot) {
		this.userDirIsRoot = isRoot;
	}

	@Override
	public void setIdentityFile(String s) {

		String home = System.getProperty("user.home");
		s = home + s.replaceAll("~(.*)", "$1");
		identityFile = new File(s).getAbsoluteFile();
		logger.debug("set identity file to: " + identityFile.getAbsolutePath());
		
	}

}
