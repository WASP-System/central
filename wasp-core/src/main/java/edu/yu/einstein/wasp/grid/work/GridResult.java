package edu.yu.einstein.wasp.grid.work;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
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
	public int getExitCode();
	
	/**
	 * set the value of exit code
	 * @param exitCode
	 */
	public void setExitCode(int exitCode);
	
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
	 * Some tasks may add one or more child GridResults to monitor i.e. if the task generating this GridResult also spawns another
	 * task. Example: a task invokes a long running file copy job that needs to come under the enclosing logic for hibernating and testing for completion.
	 * @param key
	 * @return Map of child GridResults and associated isComplete status (true / false)
	 */
	public GridResult getChildResult(String key);
	
	/**
	 * Some tasks may add one or more child GridResults to monitor i.e. if the task generating this GridResult also spawns another
	 * task. Example: a task invokes a long running file copy job that needs to come under the enclosing logic for hibernating and testing for completion.
	 * This method facilitates adding of child grid results. Use of a key allows methods to locate GridResults they 
	 * have spawned when called previously. 
	 * @param key
	 * @param result
	 */
	public void addChildResult(String key, GridResult result);
	
	/**
	 * Some tasks may add one or more child GridResults to monitor i.e. if the task generating this GridResult also spawns another. This method returns
	 * the Map of GridResults associated with a key provided when set.
	 * @return
	 */
	public Map<String, GridResult> getChildResults();
	
	
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
	 * Map of metadata stored for the job, e.g. job id, hostname etc
	 * @return
	 */
	public Map<String, String> getJobInfo();
	
	/**
	 * Get current status of job.
	 * @return
	 */
	public GridJobStatus getJobStatus();
	
	/**
	 * Set current status of job.
	 * @return
	 */
	public void setJobStatus(GridJobStatus status);
	
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
	
	/**
	 * Get id of the grid job (no guarantee that this has been set)
	 * @return
	 */
	public Long getGridJobId();
	
	/**
	 * Set id of the grid job
	 * @return
	 */
	public void setGridJobId(Long gridJobId);

	public void addJobInfo(String key, String value);

}
