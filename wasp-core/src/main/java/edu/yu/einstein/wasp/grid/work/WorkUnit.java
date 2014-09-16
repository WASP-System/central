/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Basic unit of work to be executed.   
 * 
 * @author calder
 *
 */
public class WorkUnit {
	
	public static final String TMP_DIR_PLACEHOLDER = "<<<TMP_DIR>>>";
	public static final String SCRATCH_DIR_PLACEHOLDER = "<<<SCRATCH_DIR>>>";
	public static final String RESULTS_DIR_PLACEHOLDER = "<<<RESULTS_DIR>>>";
	
	public static final String PROCESSING_INCOMPLETE_FILENAME = "wasp_processing_incomplete.txt";
	public static final String OUTPUT_FILE_PREFIX = "wasp-output";
	
	public static final String JOB_NAME = "WASPNAME";
	public static final String WORKING_DIRECTORY = "WASP_WORK_DIR";
	public static final String RESULTS_DIRECTORY = "WASP_RESULT_DIR";
	public static final String TMP_DIRECTORY = "WASP_TMP_DIRECTORY";
	
	/**
	 * 1-based task array numbering 
	 */
	public static final String TASK_ARRAY_ID = "WASP_TASK_ID";
	
	/**
	 * 0-based task array numbering
	 */
	public static final String ZERO_TASK_ARRAY_ID = "ZERO_TASK_ID";
	
	public static final String TASK_OUTPUT_FILE = "WASP_TASK_OUTPUT";
	public static final String TASK_END_FILE = "WASP_TASK_END";
	public static final String INPUT_FILE = "WASPFILE";
	public static final String OUTPUT_FILE = "WASPOUTPUT";
	public static final String METADATA_ROOT = "WASP_META";
	public static final String NUMBER_OF_THREADS = "NTHREADS";
	public static final String REQUESTED_GB_MEMORY = "MEMORYGB";

	private boolean isRegistering;
	
	private boolean WorkingDirectoryRelativeToRoot = false;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<String,String> environmentVars = new LinkedHashMap<String,String>();
	
	/**
	 * Unique ID for the job
	 */
	private String id;
	
	/**
	 * String used for accounting on remote computing environments;
	 */
	private String project;
	
	/**
	 * Newline (\n) terminated list of commands to be executed.
	 */
	private String command = "";
	/**
	 * When implementations need to generate a command to execute a command (e.g. qsub a SGE job), that command
	 * is stored here.
	 */
	private String wrapperCommand;
	/**
	 * Ordered set of possible environments where this job can be executed.  Provides a mechanism for determining
	 * if work can be submitted to a remote computational resource.
	 */
	private LinkedHashSet<String> executionEnvironments = new LinkedHashSet<String>();
	/**
	 * Amount of memory required in GB.
	 */
	private Integer memoryRequirements = 1;
	/**
	 * number of processors required.
	 */
	private Integer processors = 1;
	/**
	 * Execution mode, currently only as a process.
	 */
	private ExecutionMode mode = ExecutionMode.PROCESS;
	
	/**
	 * Scratch directory for job execution
	 */
	private String workingDirectory;
	
	protected String remoteWorkingDirectory = null;
	
	/**
	 * Directory to write results to
	 */
	private String resultsDirectory;
	
	protected String remoteResultsDirectory = null;
	
	/**
	 * Directory to for remote temporary work
	 */
	private String tmpDirectory;
	
	/**
	 * Transport specific connection
	 */
	//private GridTransportConnection connection;
	
	/**
	 * WASP files, will be available or provisioned to working directory on remote host
	 */
	private List<FileHandle> requiredFiles = new ArrayList<FileHandle>();
	
	/**
	 * Set of expected output files.  These files will be returned to WASP host and entered as WASP {@link FileHandle} objects
	 * upon successful completion of the WorkUnit.
	 */
	private Set<FileHandle> resultFiles = new LinkedHashSet<FileHandle>();
	
	/**
	 * List of software packages that need to be configured by a {@link SoftwareManager}.
	 */
	private List<SoftwarePackage> softwareDependencies = new ArrayList<SoftwarePackage>();
	
	/**
	 * Set of plugins that this workunit is dependent upon, useful for GridHostResolver to determine target system.
	 */
	private Set<String> pluginDependencies = new LinkedHashSet<String>();
	
	/**
	 * whether or not to delete the remote working directory after successful completion.
	 */
	private boolean clean = true;
	
	/**
	 * If the WorkUnit has been configured with results files, this value indicates whether or not the files should be copied to 
	 * the results folder.  If this value is set to false, a placeholder file will be placed in the working directory
	 * to indicate to the remote system that the files should not be swept up yet.  This file will be deleted when a 
	 * WorkUnit of secureResults = true is run in the folder.  Failure of the WorkUnit will cause the file to be removed.
	 */
	private boolean secureResults = true;
	
	/**
	 * If the WorkUnit has been configured with results files, this value indicates whether or not the files should be copied to 
	 * the results folder.  If this value is set to false, a placeholder file will be placed in the working directory
	 * to indicate to the remote system that the files should not be swept up yet.  This file will be deleted when a 
	 * WorkUnit of secureResults = true is run in the folder.  Failure of the WorkUnit will cause the file to be removed.
	 * @param secure
	 */
	public void setSecureResults(boolean secure) {
		this.secureResults = secure;
	}
	
	/**
	 * If the WorkUnit has been configured with results files, this value indicates whether or not the files should be copied to 
	 * the results folder.  If this value is set to false, a placeholder file will be placed in the working directory
	 * to indicate to the remote system that the files should not be swept up yet.  This file will be deleted when a 
	 * WorkUnit of secureResults = true is run in the folder.  Failure of the WorkUnit will cause the file to be removed.
	 * @return
	 */
	public boolean isSecureResults() {
		return this.secureResults;
	}
	
	/**
	 * String representation of the user requesting the unit of work
	 */
	private String user;
	
	/**
	 * Specify a parallel environment to use. If default and MPI parallel environments are provided in configuration
	 * it is not necessary to set this unless a non-default PE is required.
	 */
	private String parallelEnvironment;
	
	/**
	 * get the parallel environment
	 * @return
	 */
	public String getParallelEnvironment() {
		return parallelEnvironment;
	}

	/**
	 * Specify a parallel environment to use. If default and MPI parallel environments are provided in configuration
	 * it is not necessary to set this unless a non-default PE is required.
	 * @param parallelEnvironment
	 */
	public void setParallelEnvironment(String parallelEnvironment) {
		this.parallelEnvironment = parallelEnvironment;
	}
	
	
	/**
	 * Method for determining how many processors are necessary to execute this task.
	 */
	private ProcessMode processMode = ProcessMode.SINGLE;
	
	/**
	 * get the ProcessMode
	 * @return
	 */
	public ProcessMode getProcessMode() {
		return processMode;
	}
	

	/**
	 * set the ProcessMode
	 * @param mode
	 */
	public void setProcessMode(ProcessMode mode) {
		this.processMode = mode;
	}
	
	private Integer numberOfTasks;
	
	/**
	 * 
	 * @return the numberOfTasks
	 */
	public Integer getNumberOfTasks() {
		return numberOfTasks;
	}
	/**
	 * When the WorkUnit is run in ExecutionMode.TASK_ARRAY, this is the number of tasks that will be requested.
	 * all files will be numbered prefix:$TASK.  Where $TASK is zero based.
	 * 
	 * @param numberOfTasks the numberOfTasks to set
	 */
	public void setNumberOfTasks(Integer numberOfTasks) {
		this.numberOfTasks = numberOfTasks;
	}
	
	
	
	/**
	 * ExecutionMode is the method by which jobs are executed, is handled by the underlying WorkService implementation,
	 * and is not guaranteed to have any additional effects.  Future implementation may include ARRAY, MPI.
	 * 
	 * @author calder
	 *
	 */
	public enum ExecutionMode {
		/**
		 * Handle processing at the process level.  Default.
		 */
		PROCESS, MPI, TASK_ARRAY;
	}
	
	/**
	 * How a remote system should determine the number of processors used. 
	 * 
	 * <pre>
	 *  SINGLE: WorkUnit will run in a single thread.
	 *  FIXED:  WorkUnit will require one or more processors, specified by the work unit.  It is possible that some
	 *  	work units will never be executed it this value is set higher than the largest machine on the grid.
	 *  MAX:	(DEFAULT) The GridHostResolver determines the number of processors to allocate.  Choosing the maximum value
	 *  	of all configured software. e.g. if host.software.foo.env.processors=8, a work unit declaring a
	 *  	dependency of foo will use 8 processors.
	 *  SUM:	The GridHostResolver sums up all of the configured software requirements to determine the number
	 *      of required processors
	 *  MPI:	The GridHostResolver must determine a number of processes and parallel environment.   
	 * </pre>
	 * 
	 * @author calder
	 *
	 */
	public enum ProcessMode {
		SINGLE, FIXED, MAX, SUM, MPI;    
	}
	
	/**
	 * Automatically populate the WorkUnit with executionEnvironment "default".  This is the minimum that a
	 * {@link GridHostResolver} needs to implement.
	 */
	public WorkUnit() {
		this.executionEnvironments = new LinkedHashSet<String>();
		this.executionEnvironments.add("default");
		this.workingDirectory = SCRATCH_DIR_PLACEHOLDER;
		this.resultsDirectory = RESULTS_DIR_PLACEHOLDER;
		this.tmpDirectory = TMP_DIR_PLACEHOLDER;
		UUID resultID = UUID.randomUUID();
		this.setId(resultID.toString());
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command + "\n";
	}
	
	public void addCommand(String command) {
		this.command += command + "\n";
	}
	/**
	 * get the required memory in GB
	 * @return
	 */
	public Integer getMemoryRequirements() {
		return memoryRequirements;
	}
	/**
	 * Set the required memory
	 * @param memoryRequirements in GB
	 */
	public void setMemoryRequirements(Integer memoryRequirements) {
		this.memoryRequirements = memoryRequirements;
	}

	public Integer getProcessorRequirements() {
		return processors;
	}

	/**
	 * Set the number of processors/threads required. It is possible to set this manually, however this
	 * value will override any default setting made in configuration.
	 * @param processors
	 */
	public void setProcessorRequirements(Integer processorRequirements) {
		this.processors = processorRequirements;
	}

	public ExecutionMode getMode() {
		return mode;
	}

	public void setMode(ExecutionMode mode) {
		this.mode = mode;
	}
	
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
		if (!this.workingDirectory.endsWith("/")) {
			this.workingDirectory += "/";
		}
	}
	
	public String getTmpDirectory() {
		return tmpDirectory;
	}

	public void setTmpDirectory(String tmpDirectory) {
		this.tmpDirectory = tmpDirectory;
	}

	public String getResultsDirectory() {
		return resultsDirectory;
	}

	public void setResultsDirectory(String resultsDirectory) {
		this.resultsDirectory = resultsDirectory;
		if (!this.resultsDirectory.endsWith("/")) {
			this.resultsDirectory += "/";
		}
	}

//	/**
//	 * @return the connection
//	 */
//	public GridTransportConnection getConnection() {
//		return connection;
//	}
//
//	/**
//	 * @param connection the connection to set
//	 */
//	protected void setConnection(GridTransportConnection connection) {
//		this.connection = connection;
//	}

	/**
	 * @return the wrapperCommand
	 */
	protected String getWrapperCommand() {
		return wrapperCommand;
	}

	/**
	 * Command set by the GridService to dispatch the actual command.
	 * @param wrapperCommand the wrapperCommand to set
	 */
	protected void setWrapperCommand(String wrapperCommand) {
		this.wrapperCommand = wrapperCommand;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the softwareDependencies
	 */
	public List<SoftwarePackage> getSoftwareDependencies() {
		return softwareDependencies;
	}

	/**
	 * Software dependencies are set to ensure the work unit is sent to an appropriate host (one which
	 * provides the requested software and has the resources to use it)
	 * @param softwareDependencies the softwareDependencies to set
	 */
	public void setSoftwareDependencies(List<SoftwarePackage> softwareDependencies) {
		this.softwareDependencies = softwareDependencies;
	}
	/**
	 * @return the project
	 */
	protected String getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	protected void setProject(String project) {
		this.project = project;
	}
	
	protected void prepare() throws MisconfiguredWorkUnitException {
		for (FileHandle f : getRequiredFiles()) {
			if (f == null) {
				if (!isRegistering()) {
					String message = "FileHandle has not been registered " + f.getFileURI();
					logger.warn(message);
					throw new MisconfiguredWorkUnitException(message);
				}
			}
			if (f.isArchived()) {
				// TODO: implement wait for de-archive step.
				String message = "FileHandle is archived " + f.getFileURI();
				logger.warn(message);
				throw new MisconfiguredWorkUnitException(message);
			}
		}
	}
	/**
	 * FileHandle objects available to the remote host.  Grid host resolver may use these files to determine
	 * which host to go to and the GridWorkService should provision them if they are not present.
	 * Accessible through the WASPFILE bash array.  
	 * @return the requiredFiles
	 */
	public List<FileHandle> getRequiredFiles() {
		return requiredFiles;
	}
	
	/**
	 * @param requiredFiles the requiredFiles to set
	 */
	public void setRequiredFiles(List<FileHandle> requiredFiles) {
		this.requiredFiles = requiredFiles;
	}
	
	public void addRequiredFile(FileHandle file) {
		this.requiredFiles.add(file);
	}
	
	
	/**
	 * Ordered set of relative string paths (in working directory) to result files.  Accessed in bash environment through
	 * the WASPOUTPUT bash array.
	 * @return the resultFiles
	 */
	public Set<FileHandle> getResultFiles() {
		return resultFiles;
	}
	
	/**
	 * @param resultFiles the resultFiles to set
	 */
	public void setResultFiles(LinkedHashSet<FileHandle> resultFiles) {
		this.resultFiles = resultFiles;
	}
	
	public void addResultFile(FileHandle file) {
		this.resultFiles.add(file);
	}
	/**
	 * Internal method to turn off file registration check when file is not yet registered.
	 * 
	 * @return the isRegistering
	 */
	public boolean isRegistering() {
		return isRegistering;
	}
	/**
	 * Internal method to turn off file registration check when file is not yet registered.
	 * 
	 * @param isRegistering the isRegistering to set
	 */
	public void setRegistering(boolean isRegistering) {
		this.isRegistering = isRegistering;
	}
	
	/**
	 * Automatically configured environment variables formatted as key=value.  
	 * These are typically set via AOP, users may set them here if they take
	 * care to generate unique names.
	 * 
	 * @return the environmentVars
	 */
	public Map<String,String> getEnvironmentVars() {
		return environmentVars;
	}
	
	/**
	 * environment variable setting, generally automatically set.
	 * @param name ENVIRONMENT_VAR
	 * @param value host-specific setting
	 */
	public void putEnvironmentVariable(String name, String value) {
		environmentVars.put(name, value);
	}
	
	/**
	 * Variables processed by the {@link GridWorkService}.
	 * @param environmentVars the environmentVars to set
	 */
	public void setEnvironmentVars(Map<String,String> environmentVars) {
		this.environmentVars = environmentVars;
	}

	/**
	 * informs whether the working directory is specified as relative to root. Overrides configuration setting userDirIsRoot
	 * @param workingDirectoryIsRelativeToRoot
	 */
	public boolean isWorkingDirectoryRelativeToRoot() {
		return WorkingDirectoryRelativeToRoot;
	}

	/**
	 * Set whether the working directory is relative to root. Overrides configuration setting userDirIsRoot
	 * @param workingDirectoryIsRelativeToRoot
	 */
	public void setWorkingDirectoryRelativeToRoot(boolean workingDirectoryIsRelativeToRoot) {
		WorkingDirectoryRelativeToRoot = workingDirectoryIsRelativeToRoot;
	}
	

}
