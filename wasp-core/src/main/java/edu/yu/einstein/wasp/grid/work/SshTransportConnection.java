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
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SecurityUtils;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;

/**
 * {@link GridTransportConnection} implementation for execution of jobs over a
 * SSH connection.
 * 
 * @author calder
 * 
 */
public class SshTransportConnection implements GridTransportConnection, InitializingBean, DisposableBean {
	
	private static final Logger logger = LoggerFactory.getLogger(SshTransportConnection.class);

	private SSHClient client = new SSHClient();
	
	private Session session;
	
	// TODO: configure

	private int execTimeout = 600000; // 10m, VERY generous
	private int execRetries = 5;

	
	private String identityFileName;
	private File identityFile;

	private String name;
	private String hostname;
	private String username;

	private Map<String, String> settings = new HashMap<String, String>();

	private SoftwareManager softwareManager;

	private boolean userDirIsRoot = true;

	private Properties localProperties;
	
	private DirectoryPlaceholderRewriter directoryPlaceholderRewriter = new DefaultDirectoryPlaceholderRewriter();
	
	public Map<String, String> getSettings() {
		return settings;
	}
	

	public SshTransportConnection() {
		loadKnownHosts();
	} 
	
	private void resetClientAndStartNewSession() throws GridAccessException, ConnectionException, TransportException{
		doDisconnect();
		client = new SSHClient();
		loadKnownHosts();
		initClient();
		session = client.startSession();
	}
	
	private void loadKnownHosts() {
		try {
			client.loadKnownHosts();
		} catch (IOException e) {
			logger.error("unable to load known hosts file");
			e.printStackTrace();
			throw new RuntimeException("sshj unable to load known hosts file");
		}
	}

	private void initClient() throws GridAccessException {
		try {
			logger.trace("attempting to configure and connect SSH connection");

			logger.trace("loading identity file " + getIdentityFile().getAbsolutePath());
			logger.trace("BouncyCastle: " + SecurityUtils.isBouncyCastleRegistered());
			logger.trace("connecting " + getHostName());
			logger.trace("client: " + client.toString());
			
			client.connect(getHostName());
			logger.debug("connected");
			KeyProvider key = client.loadKeys(getIdentityFile().getAbsolutePath());
			client.authPublickey(getUserName(), key);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("unable to connect to remote host " + getHostName());
			throw new GridAccessException("unable to connect to remote", e);
		}
		
		logger.debug("adding sshj connection: " + this.client.toString());
	}
	
	private void disconnectClient() throws IOException {
		logger.debug("disconnecting client");
		client.disconnect();
	}
	
	private void openSession() throws ConnectionException, TransportException, GridAccessException {
		
		if (!client.isConnected()) {
			logger.warn("client for " + getHostName() + " was not connected, reestablishing");
			initClient();
		}
		if (session == null) {
			session = client.startSession();
			return;
		}
		if (session.isOpen()) {
			logger.debug("session already open");
			return;
		}
		logger.debug("opening session");
		try {
			session = client.startSession();
		} catch (ConnectionException e) {
			logger.warn("session apparently timed out, attempting to recover: " + e.toString());
			try{
				initClient();
				session = client.startSession();
			} catch (Exception e1){
				logger.warn("Unable to recover session so going to reset client and start a new session: " + e1.toString());
				resetClientAndStartNewSession();
			}
		}
	}
	
	private void closeSession() throws TransportException, ConnectionException {
		logger.debug("closing session");
		if (session != null && session.isOpen()) {
			session.close();
		} else {
			logger.debug("no open session to close");
		}
	}

	public void doDisconnect() throws GridAccessException {
		logger.debug("Disconnectiong SshTransportConnection to " + getHostName() + " as " + getUserName());
		try {
			closeSession();
		} catch (IOException e) {
			logger.warn("unable to close session " + e.getLocalizedMessage());
		}
		try {
			disconnectClient();
		} catch (IOException e) {
			logger.error("unable to cleanly disconnect: " + e.getLocalizedMessage());
			throw new GridAccessException(e.getLocalizedMessage());
		}
	}

	/**
	 * Ssh implementation of {@link GridTransportConnection}.  This method sends the exec to the target host without wrapping
	 * in the extra methods defined in the {@link GridWorkService} (used by GridWorkService to "execute" work unit).
	 * 
	 * {@inheritDoc}
	 * @throws MisconfiguredWorkUnitException 
	 */
	@Override
	public GridResult sendExecToRemote(WorkUnit w) throws GridAccessException, GridExecutionException, GridUnresolvableHostException, MisconfiguredWorkUnitException {
			
		GridResultImpl result = new GridResultImpl();
		
		try {
			
			logger.trace("Attempting to create SshTransportConnection to " + getHostName());
			
			openSession();
			
			// ensure set for direct remote execution
			directoryPlaceholderRewriter.replaceDirectoryPlaceholders(this, w);
			if (!w.getConfiguration().isWorkingDirectoryRelativeToRoot())
				w.getConfiguration().remoteWorkingDirectory = prefixRemoteFile(w.getConfiguration().getWorkingDirectory());
			w.getConfiguration().remoteResultsDirectory = prefixRemoteFile(w.getConfiguration().getResultsDirectory());
			
			String command = w.getCommand();
			if (w.getWrapperCommand() != null)
				command = w.getWrapperCommand();
			command = "cd " + w.getConfiguration().remoteWorkingDirectory + " && " + command;
			command = "if [ -e /etc/profile ]; then source /etc/profile > /dev/null 2>&1; fi && " + command;
			logger.trace("sending exec: " + command + " at: " + getHostName());
			
			for (int tries=0; tries < execRetries; tries++) {
			    try {
                    doExec(result, command);
                    break;
			    } catch (ConnectionException ce) {
			        logger.warn("Caught ConnectionException on session (" + ce.getLocalizedMessage() + ") try " + tries + 
                                    ". Session open: " + session.isOpen() + ". Will try again up to " + execRetries + " times.");
			        if (!session.isOpen()) {
			            openSession();
			        }
			        continue;
			    }
			}
			try{
				closeSession();
			} catch (ConnectionException | TransportException e) {
				logger.warn("problem closing session to execute command");
				throw new GridAccessException("problem closing session", e);
			}
		} catch (ConnectionException | TransportException e) {
			logger.warn("problem opening session to execute command");
			throw new GridAccessException("problem opening session", e);
		} catch (Exception e) {
			logger.warn("problem executing command");
			throw new GridExecutionException("problem executing command", e);
		}
		logger.trace("returning result");
		return (GridResult) result;

	}
	
    private void doExec(GridResultImpl result, String command) throws ConnectionException, TransportException, GridAccessException, GridExecutionException {
        try {
            final Command exec = session.exec(command);
            // execute command and timeout
            exec.join(this.execTimeout, TimeUnit.MILLISECONDS);
            int exitCode = exec.getExitStatus();
            result.setExitStatus(exitCode);
            if (logger.isTraceEnabled()){
				byte[] outBA = IOUtils.toByteArray(exec.getInputStream()); // extract as Byte[] so that we can read it more than once
				byte[] errBA = IOUtils.toByteArray(exec.getErrorStream()); // extract as Byte[] so that we can read it more than once
				StringWriter outWriter = new StringWriter();
				IOUtils.copy(new ByteArrayInputStream(outBA), outWriter);
				logger.trace("stdout:" + outWriter.toString());
				StringWriter errWriter = new StringWriter();
				IOUtils.copy(new ByteArrayInputStream(errBA), errWriter);
				logger.trace("stderr:" + errWriter.toString());
				result.setStdOutStream(new ByteArrayInputStream(outBA));
				result.setStdErrStream(new ByteArrayInputStream(errBA));
			} else {
				result.setStdOutStream(exec.getInputStream());
				result.setStdErrStream(exec.getErrorStream());
			}
            logger.trace("sent command");
            if (exitCode != 0) {
                logger.error("exec terminated with non-zero exit status (" + exitCode + ") whilst attempting to run command: " + command + ". StdErr=[" + 
                		IOUtils.toString(result.getStdErrStream()).trim() + "]");
                throw new GridAccessException("exec terminated with non-zero exit status: " + exitCode);
            }
        } catch (IOException e) {
			logger.error("caught IOExeption executing '" + command + "' : " + e.getMessage() );
			e.printStackTrace();
			throw new GridExecutionException("Unable to exec", e);
        } finally {
            session.close();
        } 
    }

	@Override
	public SoftwareManager getSoftwareManager() {
		return softwareManager;
	}
	
	public void setSoftwareManager(SoftwareManager softwareManager) {
		this.softwareManager = softwareManager;
	}
	

	@Override
	public String getConfiguredSetting(String key) {
		return settings.get(key);
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
	
	public void setIdentityFile(String s) {

		String home = System.getProperty("user.home");
		s = home + s.replaceAll("~(.*)", "$1");
		identityFile = new File(s).getAbsoluteFile();
		logger.debug("set identity file to: " + identityFile.getAbsolutePath());
		
	}
	
	@Override
	public String prefixRemoteFile(String filespec) {
		String prefix = "";
		if (filespec.startsWith("~/")) filespec = filespec.replaceAll("^~/", "/");
		if (isUserDirIsRoot() && !filespec.startsWith("$")) prefix = "$HOME/";
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




	@Override
	public void afterPropertiesSet() throws Exception {
		logger.debug("created sshTransportConnection");
	}

	@Override
	public void destroy() throws Exception {
		logger.debug("destroying sshTransportConnection");
		doDisconnect();
	}
	

}
