/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Basic unit of work to be executed.   
 * 
 * @author calder
 *
 */
public class WorkUnit {
	
	public static final String SCRATCH_DIR = "<<<SCRATCH_DIR>>>";
	public static final String RESULTS_DIR = "<<<RESULTS_DIR>>>";
	
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
	private String command;
	/**
	 * When implementations need to generate a command to execute a command (e.g. qsub a SGE job), that command
	 * is stored here.
	 */
	private String wrapperCommand;
	/**
	 * Ordered set of possible environments where this job can be executed.  Provides a mechanism for determining
	 * if work can be submitted to a remote computational resource.
	 */
	private Set<String> executionEnvironments;
	/**
	 * Amount of memory required in GB.
	 */
	private Integer memoryRequirements;
	/**
	 * number of processors required.
	 */
	private Integer processors;
	/**
	 * Execution mode, currently only as a process.
	 */
	private ExecutionMode mode = ExecutionMode.PROCESS;
	
	/**
	 * Scratch directory for job execution
	 */
	private String workingDirectory;
	
	/**
	 * Directory to write results to
	 */
	private String resultsDirectory;
	
	/**
	 * Transport specific connection
	 */
	private GridTransportConnection connection;
	
	/**
	 * WASP files, will be available or provisioned to working directory on remote host
	 */
	private Set<File> requiredFiles;
	
	/**
	 * Set of expected output files.  These files will be returned to WASP host and entered as WASP {@link File} objects
	 * upon successful completion of the WorkUnit.
	 */
	private Set<String> resultFiles;
	
	/**
	 * List of software packages that need to be configured by a {@link SoftwareManager}.
	 */
	private List<SoftwarePackage> softwareDependencies;
	
	/**
	 * Set of plugins that this workunit is dependent upon, useful for GridHostResolver to determine target system.
	 */
	private Set<String> pluginDependencies;
	
	/**
	 * whether or not to delete the remote working directory after successful completion.
	 */
	private boolean clean = true;
	
	/**
	 * whether or not to copy results files to the remote archive upon completion.  Execution of subsequent steps on the 
	 * same host will greatly benefit by setting this to true.
	 */
	private boolean provisionResults = false;
	
	/**
	 * String representation of the user requesting the unit of work
	 */
	private String user;
	
	
	/**
	 * Method for determining how many processors are necessary to execute this task.
	 */
	private ProcessMode processMode = ProcessMode.MAX;
	
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
		PROCESS, MPI;
	}
	
	/**
	 * How a remote system should determine the number of processors used. 
	 * 
	 *  SINGLE: WorkUnit will run in a single thread.
	 *  FIXED:  WorkUnit will require one or more processors, specified by the work unit.  It is possible that some
	 *  	work units will never be executed it this value is set higher than the largest machine on the grid.
	 *  MAX:	(DEFAULT) The GridHostResolver determines the number of processors to allocate.  Choosing the maximum value
	 *  	of all configured software. e.g. if host.software.foo.env.processors=8, a work unit declaring a
	 *  	dependency of foo will use 8 processors.
	 *  SUM:	The GridHostResolver sums up all of the configured software requirements to determine the number
	 *      of required processors
	 *  MPI:	The GridHostResolver must determine a number of processes and parallel environment.   
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
	
	public String getResultsDirectory() {
		return resultsDirectory;
	}

	public void setResultsDirectory(String resultsDirectory) {
		this.resultsDirectory = resultsDirectory;
		if (!this.resultsDirectory.endsWith("/")) {
			this.resultsDirectory += "/";
		}
	}

	/**
	 * @return the connection
	 */
	public GridTransportConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	protected void setConnection(GridTransportConnection connection) {
		this.connection = connection;
	}

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
	

}
