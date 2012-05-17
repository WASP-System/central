/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.yu.einstein.wasp.model.File;

/**
 * Basic unit of work to be executed.   
 * 
 * @author calder
 *
 */
public class WorkUnit {
	
	/**
	 * Unique ID for the job
	 */
	private String id;
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
	 * Number of processors per node, required for determining parallel environment, etc.
	 */
	private String workingDirectory;
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
	 * whether or not to delete the remote working directory after successful completion.
	 */
	private boolean clean = false;
	
	/**
	 * whether or not to copy results files to the remote archive upon completion.  Execution of subsequent steps on the 
	 * same host will greatly benefit by setting this to true.
	 */
	private boolean provisionResults = false;
	
	
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
	 * Set the number of processors/threads required.
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

	/**
	 * @return the connection
	 */
	public GridTransportConnection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(GridTransportConnection connection) {
		this.connection = connection;
	}

	/**
	 * @return the wrapperCommand
	 */
	public String getWrapperCommand() {
		return wrapperCommand;
	}

	/**
	 * Command set by the GridService to dispatch the actual command.
	 * @param wrapperCommand the wrapperCommand to set
	 */
	public void setWrapperCommand(String wrapperCommand) {
		this.wrapperCommand = wrapperCommand;
	}
	

}
