/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.transport.verification.OpenSSHKnownHosts;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.userauth.keyprovider.OpenSSHKeyFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;

/**
 * {@link GridTransportConnection} implementation for execution of jobs over a
 * SSH connection.
 * 
 * @author calder
 * 
 */
public class SshTransportConnection implements GridTransportConnection {

	final SSHClient client = new SSHClient();
	private Session session;
	
	// TODO: configure
	private int execTimeout = 30000;

	private GridTransportService transportService;

	private static final Logger logger = LoggerFactory.getLogger(SshTransportConnection.class);

	public SshTransportConnection(GridTransportService transportService, WorkUnit w)
			throws GridAccessException, GridUnresolvableHostException {

		this.transportService = transportService;

		try {
			logger.debug("attempting to configure SSH connection");
			client.loadKnownHosts();
			//client.addHostKeyVerifier("ce:1d:fc:8d:31:26:8f:19:a3:a3:0d:a4:3f:09:da:fe");
//			this.identityFile = new OpenSSHKeyFile();
//			this.identityFile.init(transportService.getIdentityFile());
			logger.debug("attempting to configure SSH connection 2");
			KeyProvider key = client.loadKeys(transportService.getIdentityFile().getAbsolutePath());
			client.connect(transportService.getHostName());
			logger.debug("attempting to configure SSH connection a");
			client.authPublickey(transportService.getUserName(), key);
			logger.debug("attempting to configure SSH connection 3");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("unable to connect to remote host " + transportService.getHostName());
			throw new GridAccessException("unable to connect to remote", e.getCause());
		}

		// JSch.setConfig("StrictHostKeyChecking", hostKeyChecking);
		// JSch jsch = new JSch();
		// this.transportService = transportService;
		// try {
		// jsch.addIdentity(identityFile.getAbsolutePath());
		// session = jsch.getSession(transportService.getUserName(),
		// transportService.getHostName());
		// logger.debug("Attempting to create SshTransportConnection to " +
		// session.getHost() + " as " + session.getUserName());
		// session.connect();
		// session.setHost(transportService.getHostName());
		// } catch (JSchException e) {
		// e.printStackTrace();
		// logger.error("unable to connect to remote host " +
		// transportService.getHostName());
		// throw new GridAccessException("unable to connect to remote",
		// e.getCause());
		// }
		logger.debug("adding sshj connection: " + this.client.toString());
		w.setConnection(this);
	}

	// public SshTransportConnection(GridTransportService transportService,
	// String hostKeyChecking, File identityFile)
	// throws GridAccessException, GridUnresolvableHostException {
	// this.transportService = transportService;
	// JSch.setConfig("StrictHostKeyChecking", hostKeyChecking);
	// JSch jsch = new JSch();
	// try {
	// jsch.addIdentity(identityFile.getAbsolutePath());
	// session = jsch.getSession(transportService.getUserName(),
	// transportService.getHostName());
	// logger.debug("Attempting to create SshTransportConnection to " +
	// session.getHost() + " as " + session.getUserName());
	// session.connect();
	// session.setHost(transportService.getHostName());
	// } catch (JSchException e) {
	// e.printStackTrace();
	// logger.error("unable to connect to remote host " +
	// transportService.getHostName());
	// throw new GridAccessException("unable to connect to remote",
	// e.getCause());
	// }
	// }

	@Override
	public void disconnect() throws GridAccessException {
		logger.debug("Disconnectiong SshTransportConnection to " + transportService.getHostName() + " as " + transportService.getUserName());
		try {
			session.close();
			client.disconnect();
		} catch (IOException e) {
			logger.error("unable to cleanly disconnect: " + e.getLocalizedMessage());
			throw new GridAccessException(e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridResult sendExecToRemote(WorkUnit w) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {

		GridResultImpl result = new GridResultImpl();

		try {
			
			logger.debug("Attempting to create SshTransportConnection to " + transportService.getHostName());

			session = client.startSession();

			String command = w.getCommand();
			if (w.getWrapperCommand() != null)
				command = w.getWrapperCommand();
			command = "source ~/.bash_profile && " + command;
			logger.debug("sending exec: " + command + " at: " + transportService.getHostName());

				final Command exec = session.exec(command);
				//execute command and timeout
				exec.join(this.execTimeout, TimeUnit.MILLISECONDS);
				result.setExitStatus(exec.getExitStatus());
				result.setStdErrStream(exec.getErrorStream());
				result.setStdOutStream(exec.getInputStream());
				logger.debug("sent command");
				if (exec.getExitStatus() != 0) {
					logger.error("exec terminated with non-zero exit status: " + command);
					throw new GridAccessException("exec terminated with non-zero exit status: " + exec.getExitErrorMessage());
				}
						
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("problem sending command");
			throw new GridAccessException("problem closing session", e);
		}

		return (GridResult) result;

	}

}
