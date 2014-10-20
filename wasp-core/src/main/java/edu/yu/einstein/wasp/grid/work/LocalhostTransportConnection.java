/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * {@link GridTransportConnection} for exec of commands on the local machine.
 * 
 * @author calder
 *
 */
public class LocalhostTransportConnection implements GridTransportConnection {

	private static final Logger logger = LoggerFactory.getLogger(LocalhostTransportConnection.class);
	private SoftwareManager softwareManager;
	private Properties localProperties;
	private Map<String, String> settings = new HashMap<String, String>();
	private String name;
	private boolean userDirIsRoot;
	private String hostname;
	private String username;
	private File identityFile;
	private String identityFileName;
	
	private DirectoryPlaceholderRewriter directoryPlaceholderRewriter = new DefaultDirectoryPlaceholderRewriter();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridResult sendExecToRemote(WorkUnit w) throws GridAccessException {
		// ensure set for direct remote execution
		directoryPlaceholderRewriter.replaceDirectoryPlaceholders(this, w);
		if (!w.getConfiguration().isWorkingDirectoryRelativeToRoot())
			w.getConfiguration().remoteWorkingDirectory = prefixRemoteFile(w.getConfiguration().getWorkingDirectory());
		w.getConfiguration().remoteResultsDirectory = prefixRemoteFile(w.getConfiguration().getResultsDirectory());
		
		Runtime runtime = Runtime.getRuntime();
		String command = w.getCommand();
		if (w.getWrapperCommand() != null)
			command = w.getWrapperCommand();
		command = "cd " + w.getConfiguration().remoteWorkingDirectory + " && " + command;
		command = "HOME=" + System.getProperty("user.home") + ";if [ -e /etc/profile ]; then source /etc/profile > /dev/null 2>&1; fi && " + command;
		logger.trace("sending exec: " + command + " at: " + getHostName());
		GridResultImpl result = new GridResultImpl();
		try {
			Process proc = runtime.exec(new String[]{"/bin/bash", "-c", command});
			byte[] outBA = IOUtils.toByteArray(proc.getInputStream()); // extract as Byte[] so that we can read it more than once
			byte[] errBA = IOUtils.toByteArray(proc.getErrorStream()); // extract as Byte[] so that we can read it more than once
			if (logger.isTraceEnabled()){
				StringWriter outWriter = new StringWriter();
				IOUtils.copy(new ByteArrayInputStream(outBA), outWriter);
				logger.trace("stdout:" + outWriter.toString());
				StringWriter errWriter = new StringWriter();
				IOUtils.copy(new ByteArrayInputStream(errBA), errWriter);
				logger.trace("stderr:" + errWriter.toString());
			} 
			result.setStdOutStream(new ByteArrayInputStream(outBA));
			result.setStdErrStream(new ByteArrayInputStream(errBA));
			
			int exitValue = proc.waitFor();
			result.setExitStatus(exitValue);
		} catch (IOException e) {
			logger.warn("caught IOExeption executing '" + command + "' : " + e.getMessage() );
			e.printStackTrace();
			throw new GridAccessException("Unable to exec", e);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new GridAccessException("Process interrupted", e);
		} 
		return (GridResult) result;
		
	}

	@Override
	public SoftwareManager getSoftwareManager() {
		return this.softwareManager;
	}
	
	public void setSoftwareManager(SoftwareManager softwareManager) {
		this.softwareManager = softwareManager;
	}

	@Override
	public String getConfiguredSetting(String key) {
		return settings.get(key);
	}
	
	public Properties getLocalProperties() {
		return this.localProperties;
	}

	public void setLocalProperties(Properties waspSiteProperties) {
		this.localProperties = waspSiteProperties;
		String prefix = this.name + ".settings.";
		for (String key : this.localProperties.stringPropertyNames()) {
			if (key.startsWith(prefix)) {
				String newKey = key.replaceFirst(prefix, "");
				String value = this.localProperties.getProperty(key);
				this.settings.put(newKey, value);
				logger.debug("Configured setting for host \""
						+ this.name + "\": " + newKey + "=" + value);
			}
		}
	}

	@Override
	public boolean isUserDirIsRoot() {
		return this.userDirIsRoot;
	}
	
	public void setUserDirIsRoot(boolean isRoot) {
		this.userDirIsRoot = isRoot;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getHostName() {
		return this.hostname;
	}
	
	public void setHostName(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public String getUserName() {
		return this.username;
	}
	
	public void setUserName(String username) {
		this.username = username;
	}

	@Override
	public File getIdentityFile() {
		return this.identityFile;
	}
	
	public void setIdentityFileName(String identityFileName) {
		this.identityFileName = identityFileName;
		setIdentityFile(this.identityFileName);
	}
	
	public void setIdentityFile(String s) {

		String home = System.getProperty("user.home");
		s = home + s.replaceAll("~(.*)", "$1");
		identityFile = new File(s).getAbsoluteFile();
		logger.debug("set identity file to: " + identityFile.getAbsolutePath());
		
	}

	@Override
	public String prefixRemoteFile(String filespec) {
		String prefix = "";
		if (isUserDirIsRoot() && !filespec.startsWith("$HOME") && !filespec.startsWith("~")) prefix = "$HOME/";
		String retval = prefix + filespec;
		return retval.replaceAll("//", "/");
	}

	@Override
	public String prefixRemoteFile(URI uri) throws FileNotFoundException, GridUnresolvableHostException {
		if ( !uri.getHost().toLowerCase().equals(hostname))
			throw new GridUnresolvableHostException("file " + uri.toString() + " not registered on " + hostname);
		// TODO: implement remote file management
		if ( !uri.getScheme().equals("file"))
			throw new FileNotFoundException("file not found " + uri.toString() + " unknown scheme " + uri.getScheme());
		
		return prefixRemoteFile(uri.getPath());
	}

}
