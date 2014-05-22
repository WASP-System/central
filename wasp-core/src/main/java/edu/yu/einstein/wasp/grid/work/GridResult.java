package edu.yu.einstein.wasp.grid.work;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;

/**
 * Wrapper for results returned from an execution of work on a {@link GridWorkService}.
 * @author calder
 *
 */
public interface GridResult {
	
	public static final String GRID_RESULT_KEY ="gridResult";
	
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
	 * The working directory defined in the {@link WorkUnit}
	 * @return
	 */
	public String getResultsDirectory();
	
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
	 * Get the path to the tarball of results.
	 * @return
	 */
	public String getArchivedResultOutputPath();
	
	/**
	 * Set the path to the tarball of results.
	 * @return
	 */
	public void setArchivedResultOutputPath(String path);
	
	public ExecutionMode getMode();
	
	public int getNumberOfTasks();
	
	/**
	 * Default true.  Whether the job is to be marked as completed and results files copied to the results directory.
	 * 
	 * @return
	 * 
	 */
	public boolean isSecureResults();
	
	/**
	 * List of result FileHandle IDs.  
	 * 
	 * @return
	 */
	public List<Integer> getFileHandleIds();
	
	/**
	 * Unique ID of the job.  Useful for constructing path to output of previous result.
	 * 
	 * @return
	 */
	public String getId();

}
