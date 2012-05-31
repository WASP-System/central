package edu.yu.einstein.wasp.grid.work;

import java.io.InputStream;
import java.util.UUID;

/**
 * Wrapper for results returned from an execution of work on a {@link GridWorkService}.
 * @author calder
 *
 */
public interface GridResult {
	
	/**
	 * Exit code from running on {@link GridWorkService}. In the case of a simple exec, this is the exit code
	 * of the command.  In the case of a scheduler, meta-scheduler or other execution engine, it is the exit
	 * code of the submission.
	 * 
	 * @return
	 */
	public int getExitStatus();
	
	/**
	 * STDOUT of GridResult.  Since this can be from the command itself or from the {@link GridWorkService} 
	 * implementation, this may not be of particular use outside of core components.
	 * @return
	 */
	public InputStream getStdOutStream();
	
	/**
	 * STDERR of GridResult.  Since this can be from the command itself or from the {@link GridWorkService} 
	 * implementation, this may not be of particular use outside of core components.
	 * @return
	 */
	public InputStream getStdErrStream();
	
	/**
	 * Method to get a universally unique identifier for this grid job. 
	 * @return
	 */
	public UUID getUuid();
	
	/**
	 * The working directory defined by the {@link WorkUnit}.
	 * @return
	 */
	public String getWorkingDirectory();
	
	/**
	 * The name of the host where the work was performed.
	 * @return
	 */
	public String getHostname();
	
	/**
	 * The name of the user who executed the work.
	 * @return
	 */
	public String getUsername();
	
	/**
	 * Final return value
	 * @return
	 */
	public int getFinalStatus();
	
	/**
	 * Not well defined at the moment.
	 * @return
	 */
	public String getFinalOutput();

}
