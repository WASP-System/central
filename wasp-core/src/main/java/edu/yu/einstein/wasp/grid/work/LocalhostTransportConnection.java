/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;

/**
 * {@link GridTransportConnection} for exec of commands on the local machine.
 * 
 * @author calder
 *
 */
public class LocalhostTransportConnection implements GridTransportConnection {

	private static final Log logger = LogFactory.getLog(LocalhostTransportConnection.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridResult sendExecToRemote(WorkUnit w) throws GridAccessException {
		Runtime runtime = Runtime.getRuntime();
		GridResultImpl result = new GridResultImpl();
		try {
			String command = w.getCommand();
			if (w.getWrapperCommand() != null) {
				command = w.getWrapperCommand();
			}
			Process proc = runtime.exec(command);
			proc.waitFor();
			result.setStdErrStream(proc.getErrorStream());
			result.setStdOutStream(proc.getInputStream());
			result.setExitStatus(proc.exitValue());
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("Unable to exec", e.getCause());
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new GridAccessException("Process interrupted", e.getCause());
		} 
		
		return result;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect() throws GridAccessException {
		logger.debug("disconnect from localhost called");
	}

}
