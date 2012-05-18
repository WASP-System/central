/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * {@link GridTransportConnection} implementation for execution of jobs over a SSH connection.
 * @author calder
 *
 */
public class SshTransportConnection implements GridTransportConnection {

	private Session session;
	private ChannelExec channel;
	private GridHostResolver hostResolver;
	
	private static final Log logger = LogFactory.getLog(SshTransportConnection.class);
	
	public SshTransportConnection(GridHostResolver hostResolver, String hostKeyChecking, File identityFile, WorkUnit w)
			throws GridAccessException, GridUnresolvableHostException {
		JSch.setConfig("StrictHostKeyChecking", hostKeyChecking);
		JSch jsch = new JSch();
		this.hostResolver = hostResolver;
		try {
			jsch.addIdentity(identityFile.getAbsolutePath());
			session = jsch.getSession(hostResolver.getUsername(w), hostResolver.getHostname(w));
			logger.debug("Attempting to create SshTransportConnection to " + session.getHost() + " as " + session.getUserName());
			session.connect();
			session.setHost(hostResolver.getHostname(w));
		} catch (JSchException e) {
			e.printStackTrace();
			logger.error("unable to connect to remote host " + hostResolver.getHostname(w));
			throw new GridAccessException("unable to connect to remote", e.getCause());
		}
		w.setConnection(this);
	}


	public SshTransportConnection(GridHostResolver resolver, String hostname, String hostKeyChecking, File identityFile) 
			throws GridAccessException, GridUnresolvableHostException {
		this.hostResolver = resolver;
		JSch.setConfig("StrictHostKeyChecking", hostKeyChecking);
		JSch jsch = new JSch();
		try {
			jsch.addIdentity(identityFile.getAbsolutePath());
			session = jsch.getSession(hostResolver.getUsername(hostname), hostname);
			logger.debug("Attempting to create SshTransportConnection to " + session.getHost() + " as " + session.getUserName());
			session.connect();
			session.setHost(hostname);
		} catch (JSchException e) {
			e.printStackTrace();
			logger.error("unable to connect to remote host " + hostname);
			throw new GridAccessException("unable to connect to remote", e.getCause());
		}
	}


	@Override
	public void disconnect() throws GridAccessException {
		logger.debug("Disconnectiong SshTransportConnection to " + session.getHost() + " as " + session.getUserName());
		channel.disconnect();
		session.disconnect();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridResult sendExecToRemote(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		
		GridResultImpl result = new GridResultImpl();
		
		try {
			if (channel != null) 
				channel.disconnect();
			channel = (ChannelExec) session.openChannel("exec");
			String command = w.getCommand();
			if (w.getWrapperCommand() != null)
				command = w.getWrapperCommand();
			logger.debug("sending exec: " + command);
			channel.setCommand(command);
			channel.connect();
			result.setExitStatus(channel.getExitStatus());
			result.setStdErrStream(channel.getErrStream());
			result.setStdOutStream(channel.getInputStream());
		} catch (JSchException e) {
			e.printStackTrace();
			logger.error("unable to execute on remote host " + hostResolver.getHostname(w));
			throw new GridAccessException("unable to execute");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("unable to get output strem on remote host " + hostResolver.getHostname(w));
			throw new GridAccessException("unable to get output stream");
		}
		
		return (GridResult) result;
		
		
	}

}
