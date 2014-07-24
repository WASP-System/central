/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;


/**
 * Default Implementation of {@link GridResult}.
 * 
 * @author calder
 *
 */
public class GridResultImpl implements GridResult, Serializable {
	
	private final static Logger logger = LoggerFactory.getLogger(GridResultImpl.class);
	
	private static final long serialVersionUID = 1423472291111175147L;

	private UUID uuid;
	private String id;
	private String hostname;
	private String username;
	private String workingDirectory;
	private String resultsDirectory;
	protected GridJobStatus status = GridJobStatus.UNKNOWN;
	transient protected String archivedResultOutputPath = "";

	private int exitCode = -1;
	transient private InputStream stdOutStream;
	transient private InputStream stdErrStream;
	
	private ExecutionMode mode = ExecutionMode.PROCESS;
	
	private Map<String, GridResult> childResults = new HashMap<>();;
	
	private int numberOfTasks = 1;
	
	/**
	 * @return the mode
	 */
	@Override
	public ExecutionMode getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(ExecutionMode mode) {
		this.mode = mode;
	}

	/**
	 * @return the numberOfTasks
	 */
	@Override
	public int getNumberOfTasks() {
		return numberOfTasks;
	}

	/**
	 * @param numberOfTasks the numberOfTasks to set
	 */
	public void setNumberOfTasks(int numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
	
	/**
	 * sets exit code and also updates jobStatus accordingly
	 * @param exitCode
	 */
	public void setExitCode(int exitCode) {
		this.exitCode = exitCode;
		if (exitCode == 0)
			setJobStatus(GridJobStatus.COMPLETED);
		else if (exitCode > 0)
			setJobStatus(GridJobStatus.FAILED);
		logger.debug("Set exitCode=" + getExitCode() + " and jobStatus=" + getJobStatus() + " on GridResult with UUID=" + getUuid());
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.GridResult#getOutputStream()
	 */
	@Override
	public InputStream getStdOutStream() {
		return this.stdOutStream;
	}
	
	public void setStdOutStream(InputStream stdOutStream) {
		this.stdOutStream = stdOutStream;
	}
	
	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.GridResult#getOutputStream()
	 */
	@Override
	public InputStream getStdErrStream() {
		return this.stdErrStream;
	}
	
	public void setStdErrStream(InputStream stdErrStream) {
		this.stdErrStream = stdErrStream;
	}

	@Override
	public UUID getUuid() {
		return this.uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the hostname
	 */
	@Override
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the workingDirectory
	 */
	@Override
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	/**
	 * @see edu.yu.einstein.wasp.grid.work.GridResult#getResultsDirectory()
	 */
	@Override
	public String getResultsDirectory() {
		return resultsDirectory;
	}
	
	public void setResultsDirectory(String resultsDirectory) {
		this.resultsDirectory = resultsDirectory;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public GridJobStatus getJobStatus() {
		return this.status;
	}
	
	@Override
	public void setJobStatus(GridJobStatus status) {
		this.status = status;
	}

	@Override
	public String getArchivedResultOutputPath() {
		return this.archivedResultOutputPath;
	}
	
	@Override
	public void setArchivedResultOutputPath(String path) {
		this.archivedResultOutputPath = path;
	}
	
	private boolean secureResults;

	@Override
	public boolean isSecureResults() {
		return secureResults;
	}
	
	public void setSecureResults(boolean secure) {
		this.secureResults = secure;
	}
	
	private List<Integer> fileHandleIds = new ArrayList<Integer>();

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.work.GridResult#getFileGroupIds()
	 */
	@Override
	public List<Integer> getFileHandleIds() {
		return fileHandleIds;
	}
	
	public void setFileHandleIds(List<Integer> ids) {
		this.fileHandleIds = ids;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public GridResult getChildResult(String key) {
		if (!childResults.containsKey(key))
			return null;
		return childResults.get(key);
	}
	
	@Override
	public Map<String, GridResult> getChildResults(){
		return childResults;
	}

	@Override
	public void addChildResult(String key, GridResult result) {
		childResults.put(key, result);
	}
}
