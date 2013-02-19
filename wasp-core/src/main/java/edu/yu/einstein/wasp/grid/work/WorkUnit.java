/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * Basic unit of work to be executed.   
 * 
 * @author calder
 *
 */
public class WorkUnit {
	
	public static final String SCRATCH_DIR_PLACEHOLDER = "<<<SCRATCH_DIR>>>";
	public static final String RESULTS_DIR_PLACEHOLDER = "<<<RESULTS_DIR>>>";

	private boolean isRegistering;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
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
	private Set<String> executionEnvironments = new LinkedHashSet<String>();
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
	
	protected String remoteWorkingDirectory = null;
	
	/**
	 * Directory to write results to
	 */
	private String resultsDirectory;
	
	protected String remoteResultsDirectory = null;
	
	/**
	 * Transport specific connection
	 */
	//private GridTransportConnection connection;
	
	/**
	 * WASP files, will be available or provisioned to working directory on remote host
	 */
	private LinkedHashSet<File> requiredFiles = new LinkedHashSet<File>();
	
	/**
	 * Set of expected output files.  These files will be returned to WASP host and entered as WASP {@link File} objects
	 * upon successful completion of the WorkUnit.
	 */
	private LinkedHashSet<String> resultFiles = new LinkedHashSet<String>();
	
	/**
	 * List of software packages that need to be configured by a {@link SoftwareManager}.
	 */
	private List<SoftwarePackage> softwareDependencies = new ArrayList<SoftwarePackage>();
	
	/**
	 * Set of plugins that this workunit is dependent upon, useful for GridHostResolver to determine target system.
	 */
	@SuppressWarnings("unused")
	private Set<String> pluginDependencies = new LinkedHashSet<String>();
	
	/**
	 * whether or not to delete the remote working directory after successful completion.
	 */
	@SuppressWarnings("unused")
	private boolean clean = true;
	
	/**
	 * whether or not to copy results files to the remote archive upon completion.  Execution of subsequent steps on the 
	 * same host will greatly benefit by setting this to true.
	 */
	@SuppressWarnings("unused")
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
		this.workingDirectory = SCRATCH_DIR_PLACEHOLDER;
		this.resultsDirectory = RESULTS_DIR_PLACEHOLDER;
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
		for (File f : getRequiredFiles()) {
			if (f == null || f.getIsActive().equals(0)) {
				if (!isRegistering()) {
					String message = "File has not been registered " + f.getFileURI();
					logger.warn(message);
					throw new MisconfiguredWorkUnitException(message);
				}
			}
			if (f.getIsArchived().equals(1)) {
				// TODO: implement wait for de-archive step.
				String message = "File is archived " + f.getFileURI();
				logger.warn(message);
				throw new MisconfiguredWorkUnitException(message);
			}
		}
	}
	/**
	 * File objects available to the remote host.  Grid host resolver may use these files to determine
	 * which host to go to and the GridWorkService should provision them if they are not present.
	 * Accessible through the WASPFILE bash array.  
	 * @return the requiredFiles
	 */
	public Set<File> getRequiredFiles() {
		return requiredFiles;
	}
	
	/**
	 * @param requiredFiles the requiredFiles to set
	 */
	public void setRequiredFiles(LinkedHashSet<File> requiredFiles) {
		this.requiredFiles = requiredFiles;
	}
	
	public void addRequiredFile(File file) {
		this.requiredFiles.add(file);
	}
	
	
	/**
	 * Ordered set of relative string paths (in working directory) to result files.  Accessed in bash environment through
	 * the WASPOUTPUT bash array.
	 * @return the resultFiles
	 */
	public Set<String> getResultFiles() {
		return resultFiles;
	}
	
	/**
	 * @param resultFiles the resultFiles to set
	 */
	public void setResultFiles(LinkedHashSet<String> resultFiles) {
		this.resultFiles = resultFiles;
	}
	
	public void addRequiredFiles(String file) {
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
	

}
