/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * {@link GridWorkService} implementation for Sun Grid Engine.  Tested with Grid Engine v6.1.
 * 
 * @author calder
 * 
 */
public class SgeWorkService implements GridWorkService, ApplicationContextAware {
	
	@Value("${wasp.temporary.dir}")
	private String localTempDir;
	
	private ApplicationContext applicationContext;
	
	private FileService fileService;
	
	/**
	 * This method gets around a circular reference:
	 * 
	 * HostResolver-+
	 *     |        |
	 * WorkService  |
	 *     |        |
	 * FileService -+
	 * @return the fileService
	 */
	public FileService getFileService() {
		if (fileService == null) {
			this.fileService = applicationContext.getBean(FileService.class);
		}
		return fileService;
	}
	
	private static Logger logger = LoggerFactory.getLogger(SgeWorkService.class);

	private String jobNamePrefix = "WASP-";
	
	public void setJobNamePrefix(String np) {
		this.jobNamePrefix = np + "-";
	}

	private GridTransportConnection transportConnection;

	private DirectoryPlaceholderRewriter directoryPlaceholderRewriter = new DefaultDirectoryPlaceholderRewriter();
	
	public SgeWorkService(GridTransportConnection transportConnection) {
		this.transportConnection = transportConnection;
		logger.debug("configured transport service: " + transportConnection.getUserName() + "@" + transportConnection.getHostName());
	}
	
	private GridFileService gridFileService;
	
	@SuppressWarnings("unused")
	private String name;
	
	private List<String> parallelEnvironments;
	
	private String queue;
	private String maxRunTime;
	private String account;
	private String project;
	private String mailRecipient;
	private String mailCircumstances;
	
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getQueue() {
		return queue;
	}
	public void setMaxRunTime(String maxRunTime) {
		this.maxRunTime = maxRunTime;
	}
	public String getMaxRunTime() {
		return maxRunTime;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
	
	/**
	 * Set the project.  If the project is not set, it can be set on the work unit, in which case it will override
	 * the WorkServices setting and fail if the project is not available.
	 * @param project name
	 */
	public void setProject(String project) {
		this.project = project;
	}
	public String getProject() {
		return project;
	}
	public void setMailRecipient(String mailRecipient) {
		this.mailRecipient = mailRecipient;
	}
	public String getMailRecipient() {
		return mailRecipient;
	}
	public void setMailCircumstances(String mailCircumstances) {
		this.mailCircumstances = mailCircumstances;
	}
	public String getMailCircumstances() {
		return mailCircumstances;
	}

	/**
	 * Sun Grid Engine implementation of {@link GridWorkService}.  This method wraps a {@link WorkUnit} in a SGE 
	 * submission and executes using the associated {@link GridTransportService}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public GridResult execute(WorkUnit w) throws GridException {
		logger.debug("executing WorkUnit: " + w.getId());
		try {
			w.prepare();
			
			// This step needs to take place after the workunit's id has been set and before submission.
			// if the results directory is set to the default and there is no runId set
			// (in jobParameters if object was created from the GridHostResolver lookup method 
			// "createWorkUnit") it will throw an exception. 
			directoryPlaceholderRewriter.replaceDirectoryPlaceholders(transportConnection, w);
			
			if (w.getWorkingDirectory().equals(null) || w.getWorkingDirectory().equals("/")) {
				throw new MisconfiguredWorkUnitException("Must configure working directory.");
			}
			if (w.getResultsDirectory().equals(null) || w.getResultsDirectory().equals("/")) {
				throw new MisconfiguredWorkUnitException("Must configure results directory.");
			}
			
			w.remoteWorkingDirectory = transportConnection.prefixRemoteFile(w.getWorkingDirectory());
			w.remoteResultsDirectory = transportConnection.prefixRemoteFile(w.getResultsDirectory());
			//end
			
			return startJob(w);
		} catch (MisconfiguredWorkUnitException e) {
			throw new GridAccessException("Misconfigured work unit", e);
		}
	}
	
	private Document getQstat(GridResult g, String jobname) throws GridException, SAXException, IOException {
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(g.getWorkingDirectory());
		w.setCommand("qstat -xml -j " + jobname + " 2>&1 | sed 's/<\\([/]\\)*>/<\\1a>/g'");
		GridResult result;
		try {
			result = transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridException(e.getLocalizedMessage(), e);
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			dbFactory.setValidating(false);
			docBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to generate xml parser");
		}
		InputStream is = result.getStdOutStream();
		Document doc = docBuilder.parse(new InputSource(is));
		is.close();
		return doc;
	}
	
	private boolean isUnknown(Document stdout) {
		stdout.getDocumentElement().normalize();
		return stdout.getDocumentElement().getNodeName().equals("unknown_jobs");
	}
	
	private boolean isInError(Document stdout) {
		NodeList jatstatus = stdout.getElementsByTagName("JAT_status");
		if (jatstatus.getLength() > 0) {
			String jobname = stdout.getElementsByTagName("JB_job_name").item(0).getTextContent();
			String status = jatstatus.item(0).getTextContent();
			logger.debug(jobname + " status is " + status);
			int bits = new Integer(status).intValue();
			if ((bits & SgeSubmissionScript.ERROR) == SgeSubmissionScript.ERROR) {
				return true;
			}
		}
		return false;
	}
	
	private boolean didDie(GridResult g, String jobname, boolean started, boolean ended) throws GridException, SAXException {
		
		boolean died = false;
		
		
		try {
			Document stdout =  getQstat(g, jobname);
			
			boolean unknown = isUnknown(stdout);
			
			if (unknown) {
				logger.debug(jobname + " is unknown");
			} 
			
			if (!unknown) {
				died = isInError(stdout);
			} else { 
				if (!ended) {
					// TODO: Improve this logic.  This is to handle the case when the scheduler reports 
					// the job as unknown (not running) and the end file is not present because
					// of NFS delays.
					logger.debug("Job unknown and finished semaphore is not present, checking again.");
					stdout = getQstat(g, jobname);
					if (!isUnknown(stdout))
						return isInError(stdout);
					if (g.getMode().equals(ExecutionMode.TASK_ARRAY)) {
						ended = isTaskArrayEnded(g);
					} else { 
						ended = isJobEnded(g);
					}
				}
				if (!ended && started) {
					died = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to parse qstat: problem with IO");
		}
		
		return died;
		
	}

	/**
	 * {@inheritDoc}
	 * @throws GridUnresolvableHostException 
	 */
	@Override
	public boolean isFinished(GridResult g) throws GridException {

		String jobname = this.jobNamePrefix + g.getUuid().toString();
		
		logger.debug("testing for completion of " + jobname);
		
		boolean ended = false;
		
		if (!g.getMode().equals(ExecutionMode.TASK_ARRAY)) {
			ended = isJobEnded(g);
		} else {
			ended = isTaskArrayEnded(g);
		}
		boolean started = isJobStarted(g);
		logger.debug("Job status semaphores (started, ended): " + started + ", " + ended);
		
		boolean died;
		try {
			died = didDie(g, jobname, started, ended);
		} catch (SAXException e) {
			// TODO: improve this logic
			logger.warn("caught SAXException, skipping as if job has not died");
			e.printStackTrace();
			died = false;
		}
		
		logger.debug("Job Status (ended, died): " + ended + ", " + died);
		if (died) {
			cleanUpAbnormallyTerminatedJob(g);
			g.setArchivedResultOutputPath(getFailedArchiveName(g));
			throw new GridExecutionException("abnormally terminated job");
		}
		if (ended) {
			logger.debug("packaging " + jobname);
			cleanUpCompletedJob(g);
			g.setArchivedResultOutputPath(getCompletedArchiveName(g));
		}
		
		return ended;
	}
	
	private boolean isTaskArrayEnded(GridResult g) throws GridException {
		logger.debug("checking for task array completion: " + g.getUuid().toString());
		WorkUnit w = new WorkUnit();
		w.setCommand("shopt -s nullglob");
		w.addCommand("NOF=(*:*.end)");
		w.addCommand("echo ${#NOF[@]}");
		w.addCommand("shopt -u nullglob");
		w.setWorkingDirectory(g.getWorkingDirectory());
		GridResult r;
		try {
			r = transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.error("workunit is not up to muster");
			e.printStackTrace();
			throw new GridAccessException("bad work unit", e);
		}
		InputStream is = r.getStdOutStream();
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(is, writer);
		} catch (IOException e) {
			logger.error("unable to parse task array test");
			e.printStackTrace();
			throw new GridAccessException("unable to determine status of task array", e);
		}
		Integer result = new Integer(StringUtils.chomp(writer.toString()));
		if (result.equals(g.getNumberOfTasks()))
			return true;
		return false;
	}

	private boolean isJobExists(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		return isJobExists(w.getWorkingDirectory(), w.getId());
	}

	@SuppressWarnings("unused")
	private boolean isJobExists(GridResult g) throws GridAccessException {
		return isJobExists(g.getWorkingDirectory(), g.getUuid().toString());
	}

	private boolean isJobExists(String workingDirectory, String id) throws GridAccessException {
		return testSgeFileExists(workingDirectory, id, "sh");
	}

	private boolean isJobEnded(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getWorkingDirectory(), g.getUuid().toString(), "end");
	}

	private boolean isJobStarted(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getWorkingDirectory(), g.getUuid().toString(), "start");
	}
	
	private String getCompletedArchiveName(GridResult g) {
		return g.getWorkingDirectory() + jobNamePrefix + g.getUuid().toString() + ".tar.gz";
	}
	
	private String getFailedArchiveName(GridResult g) {
		return g.getWorkingDirectory() + jobNamePrefix + g.getUuid().toString() + "-FAILED.tar.gz";
	}

	private boolean testSgeFileExists(String workingDirectory, String id, String suffix) throws GridAccessException {
		try {
			String root = workingDirectory + jobNamePrefix + id ;
			if (suffix == "sh") {
				boolean completed = gridFileService.exists(root + ".tar.gz");
				boolean failed = gridFileService.exists(root + "-FAILED.tar.gz");
				if (completed || failed) return true;
			}
			String remote = root + "." + suffix;
			boolean exists = gridFileService.exists(remote);
			logger.debug("testing exists: " + transportConnection.getHostName() + ":" + remote + " = " + exists);
			return exists;
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("Unable to connect to remote host: ", e.getCause());
		}
	}
	
	private void cleanUpCompletedJob(GridResult g) throws GridException {
		cleanUpCompletedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString(), !g.isSecureResults());
		if (g.isSecureResults())
			try {
				secureResultsFiles(g);
			} catch (FileNotFoundException e) {
				logger.warn("File not found: " + e.toString());
				e.printStackTrace();
				throw new GridException("File not found error", e);
			}
	}

	private void cleanUpCompletedJob(String hostname, String workingDirectory, String resultsDirectory, String id, boolean markUnfinished)
			throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.debug("Cleaning successful job " + id + " at " + transportConnection.getHostName() + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(transportConnection.prefixRemoteFile(workingDirectory));
		
		String outputFile = jobNamePrefix + id + ".tar.gz ";
		String manifestFile = jobNamePrefix + id + ".manifest";
		String command = "touch " + manifestFile + " && find . -name '" + jobNamePrefix + id + "*' -print | sed 's/^\\.\\///' > " + manifestFile + " && " + 
				"tar --remove-files -czvf " + outputFile + " -T " + manifestFile;
		if (markUnfinished) {
			command += " && touch " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
		} else {
			command += " && rm -f " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
		}
		String prd = transportConnection.prefixRemoteFile(resultsDirectory);
		if (!w.getWorkingDirectory().equals(prd)) {
			command += " && cp " + outputFile + " " + prd;
		}
		w.setCommand(command);
		
		try {
			if (!gridFileService.exists(resultsDirectory))
				gridFileService.mkdir(resultsDirectory);
		} catch (IOException e) {
			logger.debug("unable to create remote directory");
			throw new GridExecutionException("unable to mkdir " + resultsDirectory);
		}
		
		try {
			transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridExecutionException(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * copy declared result files to results directory and register
	 * 
	 * @param g
	 * @throws GridException 
	 * @throws FileNotFoundException 
	 */
	private void secureResultsFiles(GridResult g) throws GridException, FileNotFoundException {
		if (g.getFileGroupIds() != null && g.getFileGroupIds().size() > 0) {
			copyResultsFiles(g);
			for (Integer id : g.getFileGroupIds()) {
				FileGroup fg = getFileService().getFileGroupById(id);
				getFileService().register(fg);
			}
		}
	}
	
	private void copyResultsFiles(GridResult g) throws GridException {
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(g.getWorkingDirectory());
		w.setResultsDirectory(g.getResultsDirectory());
		w.setRegistering(true);
		w.setMode(ExecutionMode.TASK_ARRAY);
		w.setCommand("touch " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME);
		int files = 0;
		for (Integer id : g.getFileGroupIds()) {
			FileGroup fg = getFileService().getFileGroupById(id);
			for (FileHandle f : fg.getFileHandles()) {
				w.addCommand("COPY[" + files + "]=\""+ WorkUnit.OUTPUT_FILE_PREFIX + "_" + fg.getId() + "." + f.getId() + 
						" " + f.getFileName() + "\"");
				files++;
			}
		}
		w.setNumberOfTasks(files-1);
		w.addCommand("THIS=${COPY[WASP_TASK_ID]}");
		w.addCommand("read -ra FILE <<< \"$THIS\"");
		w.addCommand("cp ${THIS[0]} " + WorkUnit.RESULTS_DIRECTORY + "${THIS[1]}");
		w.addCommand("rm -f " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME);
		GridResult r = execute(w);
		logger.debug("waiting for results file copy");
		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		while (!isFinished(r)) {
			ScheduledFuture<?> md5t = ex.schedule(new Runnable() {
				@Override
				public void run() {
				}
			}, 5, TimeUnit.SECONDS);
			while (!md5t.isDone()) {
				// not done
			}
		}
		ex.shutdownNow();
		logger.debug("copied");

	}
	
	private void cleanUpAbnormallyTerminatedJob(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		cleanUpAbnormallyTerminatedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString());
	}
	
	private void cleanUpAbnormallyTerminatedJob(String hostname, String workingDirectory, String resultsDirectory, String id) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.warn("Cleaning FAILED job " + id + " at " + hostname + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(transportConnection.prefixRemoteFile(workingDirectory));
		
		String outputFile = jobNamePrefix + id + "-FAILED.tar.gz ";
		String manifestFile = jobNamePrefix + id + ".manifest";
		String command = "touch " + manifestFile + " && find . -name '" + jobNamePrefix + id + "*' -print | sed 's/^\\.\\///' > " + manifestFile + " && " + 
				"tar --remove-files -czvf " + outputFile + " -T " + manifestFile;
		command += " && rm -f " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
		String prd = transportConnection.prefixRemoteFile(resultsDirectory);
		if (!w.getWorkingDirectory().equals(prd)) {
				command += " && cp " + outputFile + " " + prd;
		}
		w.setCommand(command);
		
		try {
			if (!gridFileService.exists(resultsDirectory))
				gridFileService.mkdir(resultsDirectory);
		} catch (IOException e) {
			logger.debug("unable to create remote directory");
			throw new GridExecutionException("unable to mkdir " + resultsDirectory);
		}
		
		
		try {
			transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridExecutionException(e.getLocalizedMessage(), e);
		}
	}


	private GridResult startJob(WorkUnit w) throws MisconfiguredWorkUnitException, GridException {
		
		if (isJobExists(w)) {
			throw new GridAccessException("UUID already exists");
		}
		
		logger.debug("begin start job " + w.getId());

		File script;
		try {
			script = File.createTempFile("wasp-", ".sge");
			logger.debug("creating temporary local sge script: " + script.getAbsolutePath().toString());
			BufferedWriter scriptHandle = new BufferedWriter(new FileWriter(script));
			SgeSubmissionScript sss = new SgeSubmissionScript(w);
			// TODO: user specific account settings
			if (getAccount() != null)
				sss.setAccount(getAccount());
			if(getMailRecipient() != null)
				sss.setMailRecipient(getMailRecipient());
			if(getMailCircumstances() != null)
				sss.setMailCircumstances(getMailCircumstances());
			if(getMaxRunTime() != null)
				sss.setMaxRunTime(getMaxRunTime());
			// TODO: PE
			//if(getAvailableParallelEnvironments().size() > 0)
			//	sss.setParallelEnvironment("", w.getProcessorRequirements());
			if(getProject() != null)
				sss.setProject(getProject());
			else if (w.getProject() != null)
				// if the host does not have a project set, attempt to set from the work unit (for accounting).
				sss.setProject(w.getProject());
			if(getQueue() != null)
				sss.setQueue(getQueue());
			if (w.getMemoryRequirements()!= null) 
				sss.setMemory(w.getMemoryRequirements());
			if (w.getProcessorRequirements() != null)
				sss.setProcs(w.getProcessorRequirements());
			scriptHandle.write(sss.toString());
			scriptHandle.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GridAccessException("unable to get local temp file ", e.getCause());
		}
		try {
			logger.debug("putting script file on remote server");
			gridFileService.put(script, w.getWorkingDirectory() + jobNamePrefix + w.getId() + ".sh");
			logger.debug("script file copied");
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to begin grid transaction ", e.getCause());
		} finally {
			logger.debug("deleting local temporary script file: " + script.getAbsolutePath().toString());
			script.delete();
		}
		
		String submit = "cd " +  w.remoteWorkingDirectory + " && qsub " + jobNamePrefix + w.getId() + ".sh 2>&1";
		w.setWrapperCommand(submit);
		GridResultImpl result = (GridResultImpl) transportConnection.sendExecToRemote(w);
		result.setUuid(UUID.fromString(w.getId()));
		result.setWorkingDirectory(w.getWorkingDirectory());
		result.setResultsDirectory(w.getResultsDirectory());
		result.setHostname(transportConnection.getHostName());
		result.setMode(w.getMode());
		result.setSecureResults(w.isSecureResults());
		if (w.isSecureResults()) {
			for (FileGroup fg : w.getResultFiles()) {
				result.getFileGroupIds().add(fg.getId());
			}
			
		}
			
		if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
			result.setNumberOfTasks(w.getNumberOfTasks());
		return (GridResult) result;
	}
	
	@Override
	public void setAvailableParallelEnvironments(List<String> pe) {
		this.parallelEnvironments = pe;
		
	}
	
	@Override
	public List<String> getAvailableParallelEnvironments() {
		return this.parallelEnvironments;
	}

	/**
	 * Inner class representing a standard SGE submission script.
	 * 
	 * @author calder
	 * 
	 */
	@SuppressWarnings("unused")
	private class SgeSubmissionScript {

		public static final int HELD = 16;
		public static final int QUEUED = 64;
		public static final int RUNNING = 128;
		public static final int SUSPENDED = 256;
		public static final int WAITING = 2048;
		public static final int ERROR = 32768;
		public static final int SUSPENDED_ON_THRESHOLD = 65536;

		private WorkUnit w;
		protected String name = "not_set";
		protected String header = "";
		protected String preamble = "";
		protected String configuration = "";
		protected String command = "";
		protected String postscript = "";
		private String account = "";
		private String queue = "";
		private String maxRunTime = "";
		private String parallelEnvironment = "";
		private String project = "";
		private String mailRecipient = "";
		private String mailCircumstances = "";
		private String memory = "";
		private String procs = "";

		private SgeSubmissionScript(WorkUnit w) throws GridException, MisconfiguredWorkUnitException {
			this.w = w;
			this.name = w.getId();
			
			header = "#!/bin/bash\n#\n" +
					"#$ -N " + jobNamePrefix + name + "\n" +
					"#$ -S /bin/bash\n" +
					"#$ -V\n" +
					"#$ -o " + w.remoteWorkingDirectory + jobNamePrefix + name + ".out\n" +
					"#$ -e " + w.remoteWorkingDirectory + jobNamePrefix + name + ".err\n";
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				header += "#$ -t 1-" + w.getNumberOfTasks() + "\n"; 
			}
			
			preamble = "\nset -o errexit\n" + 	// die if any script returns non 0 exit code
					"set -o pipefail\n" + 		// die if any script in a pipe returns non 0 exit code
					"set -o physical\n"; 		// replace symbolic links with physical path
			
			preamble += "\ncd " + w.remoteWorkingDirectory + "\n" +
					WorkUnit.JOB_NAME + "=" + jobNamePrefix + name + "\n" +
					WorkUnit.WORKING_DIRECTORY + "=" + w.remoteWorkingDirectory + "\n" +
					WorkUnit.RESULTS_DIRECTORY + "=" + w.remoteResultsDirectory + "\n";
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				preamble += WorkUnit.TASK_ARRAY_ID + "=$[SGE_TASK_ID-1]\n" + 
						WorkUnit.TASK_OUTPUT_FILE + "=$WASPNAME:${WASP_TASK_ID}.out\n" +
						WorkUnit.TASK_END_FILE + "=$WASPNAME:${WASP_TASK_ID}.end\n";
			}
			
			int fi = 0;
			for (edu.yu.einstein.wasp.model.FileHandle f : w.getRequiredFiles()) {
				
				logger.debug("WorkUnit required file: " + f.getFileURI().toString());
				try {
					preamble += WorkUnit.INPUT_FILE + "[" + fi + "]=" + provisionRemoteFile(f) + "\n";
				} catch (FileNotFoundException e) {
					throw new MisconfiguredWorkUnitException("unknown file " + f.getFileURI());
				} 
				fi++;
			}
			fi = 0;
			for (FileGroup fg : w.getResultFiles()) {
				for (FileHandle f : fg.getFileHandles()) {
					preamble += WorkUnit.OUTPUT_FILE + "[" + fi + "]=" + WorkUnit.OUTPUT_FILE_PREFIX + "_" + fg.getId() +"."+ f.getId() + "\n";
					fi++;
				}
			}
			
			preamble += "\n# Configured environment variables\n\n";
			
			for (String key : w.getEnvironmentVars().keySet()) {
				preamble += key + "=" + w.getEnvironmentVars().get(key) + "\n";
			}
			
			preamble += "\n####\n";
			
			preamble +=	"\necho $JOB_ID >> " + "${" + WorkUnit.JOB_NAME + "}.start\n" +
					"echo submitted to host `hostname -f` `date` 1>&2";
			// if there is a configured setting to prepare the interpreter, do that here 
			String env = transportConnection.getConfiguredSetting("env");
			if (PropertyHelper.isSet(env)) {
				configuration = env + "\n";
			}
			if (transportConnection.getSoftwareManager() != null) {
				String config = transportConnection.getSoftwareManager().getConfiguration(w);
				if (config != null) {
					configuration += config;
				}
			} 
			command = w.getCommand();
			postscript = "echo \"##### begin ${" + WorkUnit.JOB_NAME + "}\" > " + w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.command\n\n" +
					"awk '/^##### preamble/,/^##### postscript|~$/' " + 
						w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.sh | sed 's/^##### .*$//g' | grep -v \"^$\" >> " +
						w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.command\n" +
					"echo \"##### end ${" + WorkUnit.JOB_NAME + "}\" >> " + w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.command\n";
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				postscript += "touch ${" + WorkUnit.WORKING_DIRECTORY + "}/${" + WorkUnit.TASK_END_FILE + "}\n" +
					"echo completed on `hostname -f` `date` 1>&2\n";
			} else {
				postscript += "touch " + w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.end\n" +
					"echo completed on `hostname -f` `date` 1>&2\n";
			}
		}
		
		public String getMemory() {
			return this.memory;
		}

		public void setMemory(int memInGB) {
			this.memory = "#$ -l mem_free=" + memInGB + "G\n" +
					"memory=" + memInGB + "\n";
		}
		
		public String getProcs() {
			return this.procs;
		}

		public void setProcs(Integer threads) {
			this.procs = "#$ -l p=" + threads.toString() + "\n" +
					"threads=" + threads + "\n";
		}

		public String toString() {
			String pe = "";
			if (w.getMode() == ExecutionMode.MPI)
				pe = getParallelEnvironment();
			String numProcs = "";
			if (w.getMode() != ExecutionMode.MPI)
				numProcs = getProcs();
			
			return header + 
					"\n\n##### resource requests\n\n" +
					getAccount() + 
					getQueue() +
					getMaxRunTime() +
					pe +
					getProject() + 
					getMailRecipient() +
					getMailCircumstances() +
					numProcs +
					getMemory() + 
					"\n\n##### preamble \n\n" +
					preamble + 
					"\n\n##### configuration \n\n" +
					configuration +
					"\n\n##### command \n\n" +
					command + 
					"\n\n##### postscript\n\n" +
					postscript;
		}

		/**
		 * @return the account
		 */
		public String getAccount() {
			return account;
		}

		/**
		 * @param account the account to set
		 */
		public void setAccount(String account) {
			if (PropertyHelper.isSet(account)) this.account = "#$ -A " + account + "\n";
		}

		/**
		 * @return the queue
		 */
		public String getQueue() {
			return queue;
		}

		/**
		 * @param queue the queue to set
		 */
		public void setQueue(String queue) {
			if (PropertyHelper.isSet(queue)) this.queue = "#$ -q " + queue + "\n";
		}

		/**
		 * @return the maxRunTime
		 */
		public String getMaxRunTime() {
			return maxRunTime;
		}

		/**
		 * @param maxRunTime the maxRunTime to set
		 */
		public void setMaxRunTime(String maxRunTime) {
			if (PropertyHelper.isSet(maxRunTime)) this.maxRunTime = "#$ -l h_rt=" + maxRunTime + "\n";
		}

		/**
		 * @return the availableParallelEnvironments
		 */
		public String getParallelEnvironment() {
			return parallelEnvironment;
		}

		/**
		 * @param availableParallelEnvironments the availableParallelEnvironments to set
		 */
		public void setParallelEnvironment(String parallelEnvironment, Integer procs) {
			if (PropertyHelper.isSet(parallelEnvironment)) this.parallelEnvironment = "#$ -pe " + parallelEnvironment + " " + procs + "\n";
		}

		/**
		 * @return the project
		 */
		public String getProject() {
			return project;
		}

		/**
		 * @param project the project to set
		 */
		public void setProject(String project) {
			if (PropertyHelper.isSet(project)) this.project = "#$ -P " + project + "\n";
		}

		/**
		 * @return the mailRecipient
		 */
		public String getMailRecipient() {
			return mailRecipient;
		}

		/**
		 * @param mailRecipient the mailRecipient to set
		 */
		public void setMailRecipient(String mailRecipient) {
			if (PropertyHelper.isSet(mailRecipient)) this.mailRecipient = "#$ -M " + mailRecipient + "\n";
		}

		/**
		 * @return the mailCircumstances
		 */
		public String getMailCircumstances() {
			return mailCircumstances;
		}

		/**
		 * @param mailCircumstances the mailCircumstances to set
		 */
		public void setMailCircumstances(String mailCircumstances) {
			if (PropertyHelper.isSet(mailCircumstances)) this.mailCircumstances = "#$ -m " + mailCircumstances + "\n";
		}

	}

	@Override
	public GridFileService getGridFileService() {
		return gridFileService;
	}
	
	public void setGridFileService(GridFileService gridFileService) {
		this.gridFileService = gridFileService;
	}
	@Override
	public GridTransportConnection getTransportConnection() {
		return transportConnection;
	}
	
	/**
	 * This needs to be fleshed out.  Files which are not on the compute host need to be copied into place
	 * prior to execution of the work unit.
	 * 
	 */
	private String provisionRemoteFile(FileHandle file) throws FileNotFoundException, GridException {
		
		String fileName;
		try {
			fileName = transportConnection.prefixRemoteFile(file.getFileURI());
			logger.debug("proceeding with file: " + fileName);
		} catch (GridUnresolvableHostException e) {
			// file is not registered on remote host.
			// provision to temporary directory.
			String tempfile = transportConnection.prefixRemoteFile(file.getFileURI().getPath());
			tempfile = transportConnection.getConfiguredSetting("remote.dir") + "/" + tempfile;
			tempfile = tempfile.replaceAll("//", "").replaceAll("//", "/");
			logger.debug("will attempt to provision file: " + tempfile);
			try {
				if (gridFileService.exists(tempfile)) {
					return tempfile;
				} else {
					doProvisionRemoteFile(file);
					return tempfile;
				}
			} catch (IOException e1) {
				String message = "Unable to test for existence of " + file.getFileURI().toString() + " : " + e.getLocalizedMessage();
				logger.warn(message);
				throw new GridAccessException(message);
			}
		}
		return fileName;
	}
	
	private void doProvisionRemoteFile(edu.yu.einstein.wasp.model.FileHandle file) throws FileNotFoundException, GridException {
		// TODO: provision remote file from other host.
	}
	
	@Override
	public InputStream readResultStdOut(GridResult r) throws IOException {
		return readResultFile(r, ".out", false);
	}
	
	@Override
	public InputStream readResultStdErr(GridResult r) throws IOException {
		return readResultFile(r, ".err", false);
	}
	
	@Override
	public InputStream readTaskOutput(GridResult r, int taskId) throws IOException {
		String suffix = ":" + taskId + ".out";
		return readResultFile(r, suffix, true);
	}
	
	@Override
	public LinkedHashMap<String,String> getMappedTaskOutput(GridResult r) throws IOException {
		LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
		File f = File.createTempFile("wasp", "work");
		String path = r.getArchivedResultOutputPath();
		logger.debug("temporary tar file " + f.getAbsolutePath() + " for " + path);
		gridFileService.get(path, f);
		FileInputStream afis = new FileInputStream(f);
		GZIPInputStream agz = new GZIPInputStream(afis);
		TarArchiveInputStream a = new TarArchiveInputStream(agz);
		logger.debug("tar " + a.toString());
		TarArchiveEntry e;
		while ((e = a.getNextTarEntry()) != null) {
			logger.debug("saw tar file entry " + e.getName());
			Matcher filem = Pattern.compile(":[0-9]+?.out").matcher(e.getName());
			if (!filem.find())
				continue;
			byte[] content = new byte[(int) e.getSize()];
			a.read(content, 0, content.length);
			ByteArrayInputStream bais = new ByteArrayInputStream(content);
			BufferedReader br = new BufferedReader(new InputStreamReader(bais));
 
			StringBuilder sb = new StringBuilder();
			String line;
	    	while ((line = br.readLine()) != null) {
	    		sb.append(line);
	    	}
	    	result.put(e.getName(), sb.toString());
	    	br.close();
		}
		a.close();
	    agz.close();
	    afis.close();
	    f.delete();
		return result;
	}
	
	private InputStream readResultFile(GridResult r, String suffix, boolean keep) throws IOException {
		String path = r.getArchivedResultOutputPath();
		if (! gridFileService.exists(path)) {
			throw new FileNotFoundException("file not found " + path);
		}
		File f;
		if (!keep) {
			f = File.createTempFile("wasp", "work");
		} else {
			f = new File(localTempDir + File.separator + r.getUuid());
		}
		if (!f.exists())
			gridFileService.get(path, f);
		logger.debug("temporary tar file " + f.getAbsolutePath());
		String contentName = jobNamePrefix + r.getUuid() + suffix;
		logger.debug("looking for: " + contentName);
		FileInputStream afis = new FileInputStream(f);
		GZIPInputStream agz = new GZIPInputStream(afis);
		TarArchiveInputStream a = new TarArchiveInputStream(agz);
		logger.debug("tar " + a.toString());
		TarArchiveEntry e;
		while ((e = a.getNextTarEntry()) != null) {
			logger.debug("saw tar file entry " + e.getName());
			if (e.getName().equals(contentName)) {
				byte[] content = new byte[(int) e.getSize()];
				a.read(content, 0, content.length);
				ByteArrayInputStream result = new ByteArrayInputStream(content);
			    a.close();
			    agz.close();
			    afis.close();
			    if (!keep) {
			    	f.delete();
			    } else {
			    	f.deleteOnExit();
			    }
				return result;
			}
		}
		f.deleteOnExit();
		a.close();
		agz.close();
	    afis.close();
		throw new FileNotFoundException("Archive " + f.getAbsolutePath() + " did not appear to contain " + contentName);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
		
	}
	
}
