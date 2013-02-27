/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * {@link GridWorkService} implementation for Sun Grid Engine.  Tested with Grid Engine v6.1.
 * 
 * @author calder
 * 
 */
public class SgeWorkService implements GridWorkService {
	
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

	/**
	 * {@inheritDoc}
	 * @throws GridUnresolvableHostException 
	 */
	@Override
	public boolean isFinished(GridResult g) throws GridException {

		String jobname = this.jobNamePrefix + g.getUuid().toString();
		
		logger.debug("testing for completion of " + jobname);
		
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
		
		boolean ended = isJobEnded(g);
		boolean started = isJobStarted(g);
		logger.debug("Job status semaphores (started, ended): " + started + ", " + ended);
		
		boolean died = false;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			dbFactory.setValidating(false);
			docBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to generate xml parser");
		}
		try {
			InputStream is = result.getStdOutStream();
			Document stdout = docBuilder.parse(new InputSource(is));
			stdout.getDocumentElement().normalize();

			boolean unknown = stdout.getDocumentElement().getNodeName().equals("unknown_jobs");
			
			if (unknown) {
				logger.debug(jobname + " is unknown");
			} 
			
			if (!unknown) {
				NodeList jatstatus = stdout.getElementsByTagName("JAT_status");
				if (jatstatus.getLength() > 0) {
					String status = jatstatus.item(0).getTextContent();
					logger.debug(jobname + " status is " + status);
					int bits = new Integer(status).intValue();
					if ((bits & SgeSubmissionScript.ERROR) == SgeSubmissionScript.ERROR) {
						died = true;
					}
				}
			} else { 
				if (!ended) {
					// TODO: Improve this logic.  This is to handle the case when the scheduler reports 
					// the job as unknown (not running) and the end file is not present because
					// of NFS delays.
					
					for (int x = 0; x < 3; x++) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							throw new GridAccessException(e.getLocalizedMessage());
						}
						logger.debug("Job finished semaphore is not present, checking again.");
						ended = isJobEnded(g);
						if (ended) 
							break;
					}
					
				}
				if (!ended && started) {
					died = true;
				}
			}

		} catch (SAXException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to parse qstat");
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to parse qstat");
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
	
	private void cleanUpCompletedJob(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		cleanUpCompletedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString());
	}

	private void cleanUpCompletedJob(String hostname, String workingDirectory, String resultsDirectory, String id) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.debug("Cleaning successful job " + id + " at " + transportConnection.getHostName() + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(workingDirectory);
		
		String outputFile = jobNamePrefix + id + ".tar.gz ";
		String command = " tar --remove-files -czvf " + outputFile + " " + jobNamePrefix + id + ".*";
		File wd = new File(workingDirectory);
		File rd = new File(resultsDirectory);
		if (!wd.getPath().equals(rd.getPath())) {
			logger.debug(wd.getPath() + " != " + rd.getPath());
			command += " && cp " + outputFile + " " + resultsDirectory;
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
	
	private void cleanUpAbnormallyTerminatedJob(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		cleanUpAbnormallyTerminatedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString());
	}
	
	private void cleanUpAbnormallyTerminatedJob(String hostname, String workingDirectory, String resultsDirectory, String id) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.warn("Cleaning FAILED job " + id + " at " + hostname + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		String outputFile = jobNamePrefix + id + "-FAILED.tar.gz ";
		String command = "cd " + workingDirectory + " && tar --remove-files -czvf " + outputFile + " " + jobNamePrefix + id + ".*";
		File wd = new File(workingDirectory);
		File rd = new File(resultsDirectory);
		if (!wd.getPath().equals(rd.getPath())) {
				command += " && cp " + outputFile + " " + resultsDirectory;
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
			gridFileService.put(script, w.getWorkingDirectory() + jobNamePrefix + w.getId() + ".sh");
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
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
			
			preamble = "\nset -o errexit\n" + 	// die if any script returns non 0 exit
												// code
					"set -o pipefail\n" + 		// die if any script in a pipe returns
												// non 0 exit code
					"set -o physical\n"; 		// replace symbolic links with
												// physical path
			
			preamble += "\ncd " + w.remoteWorkingDirectory + "\n" +
					"WASPNAME=" + name + "\n" +
					"WASP_WORK_DIR=" + w.remoteWorkingDirectory + "\n" +
					"WASP_RESULT_DIR=" + w.remoteResultsDirectory + "\n";
			
			int fi = 0;
			for (edu.yu.einstein.wasp.model.FileHandle f : w.getRequiredFiles()) {
				
				try {
					preamble += "WASPFILE[" + fi + "]=" + provisionRemoteFile(f) + "\n";
				} catch (FileNotFoundException e) {
					throw new MisconfiguredWorkUnitException("unknown file " + f.getFileURI());
				} 
				fi++;
			}
			fi = 0;
			for (String of : w.getResultFiles()) {
				preamble += "WASPOUTPUT[" + fi + "]=" + of + "\n";
			}
			
			preamble += "\n# Configured environment variables\n\n";
			
			for (String key : w.getEnvironmentVars().keySet()) {
				preamble += key + "=" + w.getEnvironmentVars().get(key) + "\n";
			}
			
			preamble += "\n####\n";
			
			preamble +=	"\necho $JOB_ID >> " + jobNamePrefix + "${WASPNAME}.start\n" +
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
			postscript = "echo \"##### begin ${WASPNAME}\" > " + w.remoteWorkingDirectory + jobNamePrefix + "${WASPNAME}.command\n\n" +
					"awk '/^##### preamble/,/^##### postscript|~$/' " + 
						w.remoteWorkingDirectory + jobNamePrefix + "${WASPNAME}.sh | sed 's/^##### .*$//g' | grep -v \"^$\" >> " +
						w.remoteWorkingDirectory + jobNamePrefix + "${WASPNAME}.command\n" +
					"echo \"##### end ${WASPNAME}\" >> " + w.remoteWorkingDirectory + jobNamePrefix + "${WASPNAME}.command\n" + 
					"touch " + w.remoteWorkingDirectory + jobNamePrefix + "${WASPNAME}.end\n" +
					"echo completed on `hostname -f` `date` 1>&2\n";
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
	 * This needs to be fleshed out.  Files which are not
	 * 
	 */
	private String provisionRemoteFile(edu.yu.einstein.wasp.model.FileHandle file) throws FileNotFoundException, GridException {
		
		String fileName;
		try {
			fileName = transportConnection.prefixRemoteFile(file.getFileURI());
		} catch (GridUnresolvableHostException e) {
			// file is not registered on remote host.
			// provision to temporary directory.
			String tempfile = transportConnection.prefixRemoteFile(file.getFileURI().getPath());
			tempfile = transportConnection.getConfiguredSetting("remote.dir") + "/" + tempfile;
			tempfile = tempfile.replaceAll("//", "").replaceAll("//", "/");
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
		return readResultFile(r, "out");
	}
	
	@Override
	public InputStream readResultStdErr(GridResult r) throws IOException {
		return readResultFile(r, "err");
	}
	
	private InputStream readResultFile(GridResult r, String suffix) throws IOException {
		String path = r.getArchivedResultOutputPath();
		if (! gridFileService.exists(path)) {
			throw new FileNotFoundException("file not found " + path);
		}
		File f = File.createTempFile("wasp", "work");
		gridFileService.get(path, f);
		logger.debug("temporary tar file " + f.getAbsolutePath());
		String contentName = jobNamePrefix + r.getUuid() + "." + suffix;
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
			    f.delete();
				return result;
			}
		}
		f.deleteOnExit();
		a.close();
		agz.close();
	    afis.close();
		throw new FileNotFoundException("Archive " + f.getAbsolutePath() + " did not appear to contain " + contentName);
	}
	
}
