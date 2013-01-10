/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.SecurityUtils;
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

			logger.debug("loading identity file " + transportService.getIdentityFile().getAbsolutePath());
			logger.debug("BouncyCastle: " + SecurityUtils.isBouncyCastleRegistered());
			logger.debug("connecting " + transportService.getHostName());
			logger.debug("client: " + client.toString());
			
			client.connect(transportService.getHostName());
			logger.debug("connected");
			KeyProvider key = client.loadKeys(transportService.getIdentityFile().getAbsolutePath());
			client.authPublickey(transportService.getUserName(), key);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("unable to connect to remote host " + transportService.getHostName());
			throw new GridAccessException("unable to connect to remote", e.getCause());
		}
		
		logger.debug("adding sshj connection: " + this.client.toString());
		w.setConnection(this);
	}

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
	 * Ssh implementation of {@link GridTransportConnection}.  This method sends the exec to the target host without wrapping
	 * in the extra methods defined in the {@link GridWorkService} (used by GridWorkService to "execute" work unit).
	 * 
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
			if (w.getWorkingDirectory() != null && !w.getWorkingDirectory().equals("") && !w.getWorkingDirectory().equals("~"))
				w.setWorkingDirectory(transportService.prefixRemoteFile(w.getWorkingDirectory()));
				command = "cd " + w.getWorkingDirectory() + " && " + command;
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
