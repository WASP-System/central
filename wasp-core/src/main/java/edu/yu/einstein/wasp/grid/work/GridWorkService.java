/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;

/**
 * GridWorkService defines an interface for doing work on remote servers over a {@link GridTransportConnection}.
 * Implementations should handle their own configuration and authentication.
 * 
 * @author calder
 *
 */
public interface GridWorkService {
		
	/**
	 * Execute a {@link WorkUnit}.
	 * @param w
	 * @return
	 * @throws GridAccessException
	 * @throws GridUnresolvableHostException
	 * @throws GridExecutionException 
	 * @throws GridException 
	 */
	public GridResult execute(WorkUnit w) throws GridException;
	
	/**
	 * Tests to see if the particular {@link GridWorkService} execution is still running. Throws a @{link GridException}
	 * if it is not running or did not complete.
	 * @param g
	 * @return
	 * @throws GridUnresolvableHostException 
	 * @throws GridException
	 */
	public boolean isFinished(GridResult g) throws GridException;
	
	/**
	 * Prefix for the names of jobs that are sent to the scheduler. 
	 * @param name Defaults to WASP.
	 */
	public void setJobNamePrefix(String name);
	
	/**
	 * Set the parallel environment strings.
	 * @param pe
	 */
	public void setAvailableParallelEnvironments(String commaDelimitedParallelEnvironments);
	
	public Set<String> getAvailableParallelEnvironments();
	
	public void setDefaultParallelEnvironment(String defaultParallelEnvironment);

	public String getDefaultParallelEnvironment();

	public String getDefaultMpiParallelEnvironment();

	public void setDefaultMpiParallelEnvironment(String defaultMpiParallelEnvironment);
	
	/**
	 * Set the grid file service.
	 * 
	 * @param gridFileService
	 */
	public void setGridFileService(GridFileService gridFileService);
	
	public GridFileService getGridFileService();
	
	public GridTransportConnection getTransportConnection();

	/**
	 * Pull the result tarball of a task array job and return contents of .out files in a map.
	 * There should not be excessively large files in the tarball.  Reads entire contents of 
	 * file into a single byte array and puts the strings into a map, numbered from task 1.
	 * 
	 * @param r
	 * @return
	 * @throws IOException
	 */
	public LinkedHashMap<Integer,String> getMappedTaskOutput(GridResult r) throws IOException;
	
	/**
	 * Pull the result tarball of a non task-array job and return the contents of the .out file
	 * in a string. If tailByteLimit is set to -1 the entire
	 * file contents are returned as a string, otherwise the specified number of bytes from the end of the file are returned as a string.
	 * 
	 * @param r
	 * @param tailByteLimit (set to -1 for unlimited)
	 * @return
	 * @throws IOException
	 */
	public String getResultStdOut(GridResult r, long tailByteLimit) throws IOException;
	
	/**
	 * Pull the result tarball of a non task-array job and return the contents of the .err file
	 * in a string. If tailByteLimit is set to -1 the entire
	 * file contents are returned as a string, otherwise the specified number of bytes from the end of the file are returned as a string.
	 * 
	 * @param r
	 * @param tailByteLimit (set to -1 for unlimited)
	 * @return
	 * @throws IOException
	 */
	public String getResultStdErr(GridResult r, long tailByteLimit) throws IOException;
	

	/**
	 * Return the contents of the job script.
	 * 
	 * 
	 * @param r
	 * @return
	 * @throws IOException
	 */
	public String getJobScript(GridResult r) throws IOException;
	
	/**
	 * Return information recorded about a job submission parsed to a Map representation.
	 * @param r
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> getParsedJobSubmissionInfo(GridResult r) throws IOException;

	/**
	 * Return the final cluster job stats parsed to a Map representation. 
	 * 
	 * @param r
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> getParsedFinalJobClusterStats(GridResult r) throws IOException;
	

	public boolean isNumProcConsumable();

	public void setNumProcConsumable(boolean isNumProcConsumable);
	
	/**
	 * Given a GridResult, pull a small named file from the job's working directory as a string. If tailByteLimit is set to -1 the entire
	 * file contents are returned as a string, otherwise the specified number of bytes from the end of the file are returned as a string.
	 * 
	 * @param r
	 * @param filename
	 * @param tailByteLimit (set to -1 for unlimited)
	 * @return
	 * @throws IOException
	 */
	public String getUnregisteredFileContents(GridResult r, String filename, long tailByteLimit) throws IOException;

	
	//public void setGridFileMovers(List<GridFileMover> gridFileMovers);
	
	//public boolean hasFileMover(Class<GridFileMover> c);
	
	//public boolean getFileMover(Class<GridFileMover> c);

}
