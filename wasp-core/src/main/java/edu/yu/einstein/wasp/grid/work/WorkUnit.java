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

/**
 * Basic unit of work to be executed.   
 * 
 * @author calder
 *
 */
public class WorkUnit {
	

	public static final String PROCESSING_INCOMPLETE_FILENAME = "wasp_processing_incomplete.txt";
	public static final String OUTPUT_FILE_PREFIX = "wasp-output";
	public static final String INPUT_FILE = "WASPFILE";
	public static final String OUTPUT_FILE = "WASPOUTPUT";
	public static final String METADATA_ROOT = "WASP_META";
	public static final String JOB_NAME = "WASPNAME";
	
	private WorkUnitGridConfiguration configuration;
	
	private boolean isRegistering;

	
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
	 * whether or not to delete the remote working directory after successful completion.
	 */
	//private boolean clean = true;
	
	/**
	 * If the WorkUnit has been configured with results files, this value indicates whether or not the files should be copied to 
	 * the results folder.  If this value is set to false, a placeholder file will be placed in the working directory
	 * to indicate to the remote system that the files should not be swept up yet.  This file will be deleted when a 
	 * WorkUnit of secureResults = true is run in the folder.  Failure of the WorkUnit will cause the file to be removed.
	 */
	private boolean secureResults = true;
	
	public WorkUnitGridConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(WorkUnitGridConfiguration configuration) {
		this.configuration = configuration;
	}

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
	 * Unit of work to be executed on a computational resource.<br /><br />
	 * Automatically populates the WorkUnitGridConfiguration with executionEnvironment "default".  This is the minimum that a
	 * {@link GridHostResolver} needs to implement.
	 */
	public WorkUnit(WorkUnitGridConfiguration configuration) {
		this.setConfiguration(configuration);
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
					String message = "FileHandle has not been registered";
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

}
