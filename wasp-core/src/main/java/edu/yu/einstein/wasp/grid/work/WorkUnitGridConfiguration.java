/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Basic unit of work to be executed.   
 * 
 * @author calder / asmclellan
 *
 */
public class WorkUnitGridConfiguration {
	
	public static final String TMP_DIR_PLACEHOLDER = "<<<TMP_DIR>>>";
	public static final String SCRATCH_DIR_PLACEHOLDER = "<<<SCRATCH_DIR>>>";
	public static final String RESULTS_DIR_PLACEHOLDER = "<<<RESULTS_DIR>>>";
	
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
	public static final String NUMBER_OF_THREADS = "NTHREADS";
	public static final String REQUESTED_GB_MEMORY = "MEMORYGB";

	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean WorkingDirectoryRelativeToRoot = false;
	
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
	 * List of software packages that need to be configured by a {@link SoftwareManager}.
	 */
	private List<SoftwarePackage> softwareDependencies = new ArrayList<SoftwarePackage>();
	
	/**
	 * Set of plugins that this workunit is dependent upon, useful for GridHostResolver to determine target system.
	 */
	private Set<String> pluginDependencies = new LinkedHashSet<String>();
	
	
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
	public WorkUnitGridConfiguration() {
		this.executionEnvironments = new LinkedHashSet<String>();
		this.executionEnvironments.add("default");
		this.workingDirectory = SCRATCH_DIR_PLACEHOLDER;
		this.resultsDirectory = RESULTS_DIR_PLACEHOLDER;
		this.tmpDirectory = TMP_DIR_PLACEHOLDER;
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
