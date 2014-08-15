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
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * {@link GridWorkService} implementation for Sun Grid Engine.  Tested with Grid Engine v6.1.
 * 
 * @author calder
 * 
 */
@Transactional("entityManager")
public class SgeWorkService implements GridWorkService, ApplicationContextAware {
	
	private static final String COPY_RESULTS_FILES_KEY = "copyResultsFiles";
	
	private static final String REGISTER_FILES_KEY = "registerFiles";
	
	private static final String CLEAN_COMPLETED_JOB_KEY = "cleanUpCompletedJob";
	
	private static final String GRID_JOB_ID_KEY = "Grid Job Id";
	private static final String GRID_JOB_NAME = "Grid Job Name";
	private static final String HOST_NODE_KEY = "Host Node";
	private static final String START_TIME_KEY = "Start Time";
	
	public static final long NO_FILE_SIZE_LIMIT = -1L;
	public static final long MAX_FILE_SIZE = 1024 * 32;
    
    @Value("${wasp.developermode:false}")
    protected boolean developerMode;
	
	/**
	 * LOCAL temporary directory
	 */
	@Value("${wasp.temporary.dir:/tmp}")
	protected String localTempDir;
	
	@Value("${wasp.nfs.timeout:0}")
	protected Integer nfsTimeout;
	
	protected ApplicationContext applicationContext;
	
	protected FileService fileService;
	
	/**
	 * This method gets around a circular reference:
	 * 
	 * HostResolver-+
	 *     |        |
	 * WorkService  |
	 *     |        |
	 * FileService -+
	 * @return the getFileService()
	 */
	public FileService getFileService() {
		if (fileService == null) 
			setFileService(applicationContext.getBean(FileService.class));
		return fileService;
	}
	
	public void setFileService(FileService fileService) {
		logger.debug("setting file service");
	    this.fileService = fileService;
	}
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String jobNamePrefix = "WASP-";
	
	public void setJobNamePrefix(String np) {
		this.jobNamePrefix = np + "-";
	}

	protected GridTransportConnection transportConnection;

	protected DirectoryPlaceholderRewriter directoryPlaceholderRewriter = new DefaultDirectoryPlaceholderRewriter();
	
	public SgeWorkService(GridTransportConnection transportConnection) {
		this.transportConnection = transportConnection;
		logger.debug("configured transport service: " + transportConnection.getUserName() + "@" + transportConnection.getHostName());
	}
	
	protected GridFileService gridFileService;
	
	protected String name;
	
	protected Set<String> parallelEnvironments = new HashSet<>();
	
	protected String defaultParallelEnvironment;
	
	protected String defaultMpiParallelEnvironment;
	
	protected String queue;
	protected String maxRunTime;
	protected String account;
	protected String project;
	protected String mailRecipient;
	protected String mailCircumstances;
	
	protected boolean isNumProcConsumable = false;
	
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
			w.remoteWorkingDirectory = w.getWorkingDirectory();
			if (!w.isWorkingDirectoryRelativeToRoot())
				w.remoteWorkingDirectory = transportConnection.prefixRemoteFile(w.getWorkingDirectory());
			w.remoteResultsDirectory = transportConnection.prefixRemoteFile(w.getResultsDirectory());
			//end
			
			return submitJob(w);
		} catch (MisconfiguredWorkUnitException e) {
			throw new GridAccessException("Misconfigured work unit", e);
		}
	}
	
	/**
	 * Basis for a stored list of active jobs
	 * @return
	 * @throws GridException
	 * @throws SAXException
	 * @throws IOException
	 */
	protected Document getAllQstat() throws GridException, SAXException, IOException {
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory("$HOME");
		w.setCommand("qstat -xml");
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
	
	/**
	 * @param g
	 * @param jobname
	 * @return
	 * @throws GridException
	 * @throws SAXException
	 * @throws IOException
	 */
	protected Document getQstat(GridResult g, String jobname) throws GridException, SAXException, IOException {
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(g.getWorkingDirectory());
		w.setCommand("qstat -xml -j " + jobname + " 2>&1 | sed 's/<\\([/]\\)*>/<\\1a>/g' | sed 's/  xmlns.*>/>/g' | sed '/<messages>/,/<\\/messages>/d'");
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
	
	protected boolean isUnknown(Document stdout) {
		stdout.getDocumentElement().normalize();
		return stdout.getDocumentElement().getNodeName().equals("unknown_jobs");
	}
	
	protected boolean isInError(Document stdout) {
		NodeList jatstatus = stdout.getElementsByTagName("JAT_status");
		String jobname = stdout.getElementsByTagName("JB_job_name").item(0).getTextContent(); // .getFirstChild().getNodeValue()??
		boolean retval = false;
		for (int n = 0; n < jatstatus.getLength(); n++) {
			String status = jatstatus.item(n).getTextContent();
			logger.trace(jobname + " task " + n + " status is " + status);
			int bits = new Integer(status).intValue();
			if ((bits & SgeSubmissionScript.ERROR) == SgeSubmissionScript.ERROR) {
				retval = true;
			}
		}
		logger.debug("job is in error: " + retval);
		return retval;
	}
	
	protected boolean didDie(GridResult g, String jobname, boolean started, boolean ended) throws GridException, SAXException {
		
		boolean died = false;
		
		
		try {
			Document stdout =  getQstat(g, jobname);
			
			boolean unknown = isUnknown(stdout);
			
			if (!unknown) {
				died = isInError(stdout);
			} else { 
				logger.debug(jobname + " is unknown");
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
	
	private boolean isJobOrTaskArrayEnded(GridResult g) throws GridException{
		if (!g.getMode().equals(ExecutionMode.TASK_ARRAY)) 
			return isJobEnded(g);
		else
			return isTaskArrayEnded(g);
	}
	
	private void recordQaactData(GridResult g){
		logger.trace("saving qacct data in " + g.getId() + ".stats");
		if (g.getGridJobId() == null){
			logger.warn("Unable to get qacct data for grid job id=" + g.getUuid() + " as no grid job id set");
			return;
		}
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(transportConnection.prefixRemoteFile(g.getWorkingDirectory()));
		w.setWrapperCommand("qacct -j " + g.getGridJobId() + " > " + g.getId() + ".stats");
		GridResultImpl result = null;;
		int attempts = 1;
		int maxAttempts = 10;
		int waitMs = 3000;
		
		while ( (result == null || result.getExitStatus() != 0) && attempts <= maxAttempts){
			if (attempts > 1){
				try {
					logger.debug("Goint to sleep for " + waitMs + "ms before trying again");
					Thread.sleep(waitMs); // wait a bit before trying again
				} catch (InterruptedException e) {
					logger.warn("Caught InterruptedException: " + e.getLocalizedMessage());
				}
			}
			try {
				result = (GridResultImpl) transportConnection.sendExecToRemote(w);
				if (result.getExitStatus() > 0){
					logger.warn("Unable to get qacct data for grid job id=" + g.getGridJobId() + 
							"(" + g.getId()  + "): Remote job submission failed (exitCode=" + result.getExitStatus() + ") on attempt " + attempts);
				}
			} catch (MisconfiguredWorkUnitException | GridException e) {
				logger.warn("Unable to get qacct data for grid job id=" + g.getGridJobId() + 
						"(" + g.getId()  + "): Remote job submission failed on attempt " + attempts + 
						" : caught MisconfiguredWorkUnitException: " + e.getLocalizedMessage());
			}
			attempts++;
		}
		if (attempts > maxAttempts)
			logger.warn("Given up trying to to obtain qacct data after " + maxAttempts + " attempts");
		
	}

	/**
	 * {@inheritDoc}
	 * Checks if finished and will update the state of the provided GridResult with the latest information about the job.
	 * @throws GridUnresolvableHostException 
	 */
	@Override
	public boolean isFinished(GridResult g) throws GridException {
		String jobname = this.jobNamePrefix + g.getUuid().toString();
		
		logger.debug("testing for completion of " + jobname);
		
		boolean died = false;
		boolean started;
		boolean	ended;
			
		if (g.getJobStatus().isEnded()){
			started = true;
			ended = true;
		} else {
			started = isJobStarted(g);
			if (started && g.getGridJobId() == null){
				g.setJobStatus(GridJobStatus.STARTED);
				try{
					Map<String, String> gridJobInfo = getJobInfoFromJson(new JSONArray(getUnregisteredFileContents(g, g.getId() + ".start", NO_FILE_SIZE_LIMIT)));
					g.setGridJobId(Long.valueOf(gridJobInfo.get(GRID_JOB_ID_KEY)));
					for (String key : gridJobInfo.keySet()){
						String value = gridJobInfo.get(key);
						g.addJobInfo(key, value);	;
						logger.trace("Registering job info in GridResult [ " + key + " : " + value + " ]");
					}
				} catch(JSONException | IOException e){
					logger.warn("Unable to extract job info from " + g.getId() + ".start: " + e.getLocalizedMessage());
				}
			}
			ended = isJobOrTaskArrayEnded(g);
			logger.debug("Job status semaphores (started, ended): " + started + ", " + ended);
			if (started && !ended){
				final int INCREMENT_FACTOR = 2;
				final int NEVER_TIME_OUT = -1;
				final int ZERO_TIME_OUT = 0;
				Integer waitMs = 1000;
				int totalWaitedMs = 0;
				boolean timedOut = false;
				do {
					try {
						died = didDie(g, jobname, started, ended);
					} catch (SAXException e) {
						// TODO: improve this logic
						logger.warn("caught SAXException, skipping as if job has not died");
						e.printStackTrace();
						died = false;
					}
					if (died) { 
						// maybe end file not there yet due to nfs problem so hasn't actually died. 
						if (nfsTimeout == ZERO_TIME_OUT){
							logger.debug("job unknown and 'end' semaphore missing. Assuming job failed.");
							timedOut = true;
						}
						else {
							logger.debug("job unknown and 'end' semaphore missing. Waiting " + waitMs + " ms then checking status again");
							try {
								Thread.sleep(waitMs); // wait a bit and see if file appears on nfs
							} catch (InterruptedException e) {
								logger.warn(e.getLocalizedMessage());
							}
							if ( totalWaitedMs >= nfsTimeout && nfsTimeout != NEVER_TIME_OUT){
								logger.debug("job unknown and 'end' semaphore missing. Wait timeout of " + nfsTimeout + " ms exceeded so job failure assumed");
								timedOut = true;
							}
							totalWaitedMs += waitMs;
							waitMs *= INCREMENT_FACTOR;
							if (totalWaitedMs + waitMs > nfsTimeout && nfsTimeout != NEVER_TIME_OUT)
								waitMs = nfsTimeout - totalWaitedMs;
							ended = isJobOrTaskArrayEnded(g);
						}
					}
				} while (died && !timedOut);
			} 
			if (ended || died)
				recordQaactData(g);
			logger.debug("Job Status (ended, died): " + ended + ", " + died);
		}
		
		if (died) {
			cleanUpAbnormallyTerminatedJob(g);
			g.setArchivedResultOutputPath(getFailedArchiveName(g));
			try{
				int exitStatus = Integer.parseInt(getParsedFinalJobClusterStats(g).get("exit status"));
				g.setExitStatus(exitStatus);
			} catch (Exception e){
				logger.warn("Unable to parse exception from job cluster stats so using default instead: " + e.getLocalizedMessage());
				g.setExitStatus(g.getExitStatus() > 1 ? g.getExitStatus() : 1);
			}
			throw new GridExecutionException("abnormally terminated job");
		}
		
		if (ended) {
			g.setJobStatus(GridJobStatus.ENDED);
			logger.debug("packaging " + jobname);
			cleanUpCompletedJob(g);
			g.setArchivedResultOutputPath(getCompletedArchiveName(g));
			for (String key : g.getChildResults().keySet()){
				GridResult r = g.getChildResults().get(key);
				if (r == null){
					logger.debug("Parent grid job has ended but child job key=" + key + " has no grid result yet so going to return false");
					return false;
				}
				logger.debug("Child job key=" + key + " has status=" + r.getJobStatus());
				if (!r.getJobStatus().isFinished()){
					logger.debug("Parent grid job has ended but child job key=" + key + " is not finished so going to return false");
					return false;
				}
			}
			logger.debug("Parent grid job has ended and no child jobs or all child jobs are complete so going to return true");
			g.setExitStatus(g.getExitStatus() > 0 ? g.getExitStatus() : 0);
		}
		return ended;
	}
	
	protected boolean isTaskArrayEnded(GridResult g) throws GridException {
		logger.debug("checking for task array completion: " + g.getUuid().toString());
		WorkUnit w = new WorkUnit();
		w.setCommand("shopt -s nullglob");
		w.addCommand("NOF=(" + g.getId() + ":*.end)");
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
		Integer numEndFiles = 0;
		try{
			numEndFiles = Integer.parseInt(StringUtils.chomp(writer.toString()));
		} catch (NumberFormatException e){
			throw new GridException("Unable to convert '" + writer.toString() + "' to Integer trying to set numEndFiles");
		}
		int numTasks = g.getNumberOfTasks();
		logger.debug("number of tasks=" + numTasks + " and number of '.end' files=" + numEndFiles);
		if (numEndFiles.equals(numTasks))
			return true;
		return false;
	}

	protected boolean isJobExists(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		return isJobExists(w.getWorkingDirectory(), w.getId());
	}

	protected boolean isJobExists(GridResult g) throws GridAccessException {
		return isJobExists(g.getWorkingDirectory(), g.getUuid().toString());
	}

	protected boolean isJobExists(String workingDirectory, String id) throws GridAccessException {
		return testSgeFileExists(workingDirectory, id, "sh");
	}

	protected boolean isJobEnded(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getWorkingDirectory(), g.getUuid().toString(), "end");
	}

	protected boolean isJobStarted(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getWorkingDirectory(), g.getUuid().toString(), "start");
	}
	
	protected String getCompletedArchiveName(GridResult g) {
		return g.getWorkingDirectory() + jobNamePrefix + g.getUuid().toString() + ".tar.gz";
	}
	
	protected String getFailedArchiveName(GridResult g) {
		return g.getWorkingDirectory() + jobNamePrefix + g.getUuid().toString() + "-FAILED.tar.gz";
	}

	protected boolean testSgeFileExists(String workingDirectory, String id, String suffix) throws GridAccessException {
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
			throw new GridAccessException("Unable to connect to remote host: ", e);
		}
	}
	
	protected void cleanUpCompletedJob(GridResult g) throws GridException {
		GridResult cleanCompletedJobResult = g.getChildResult(CLEAN_COMPLETED_JOB_KEY);
		if (cleanCompletedJobResult == null){
			cleanCompletedJobResult = cleanUpCompletedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString(), !g.isSecureResults());
			g.addChildResult(CLEAN_COMPLETED_JOB_KEY, cleanCompletedJobResult);
		}
		if (g.isSecureResults())
			try {
				secureResultsFiles(g);
			} catch (FileNotFoundException e) {
				logger.warn("File not found: " + e.toString());
				e.printStackTrace();
				throw new GridException("File not found error", e);
			}
	}

	protected GridResult cleanUpCompletedJob(String hostname, String workingDirectory, String resultsDirectory, String id, boolean markUnfinished)
			throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.debug("Cleaning successful job " + id + " at " + transportConnection.getHostName() + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(transportConnection.prefixRemoteFile(workingDirectory));
		
		String outputFile = jobNamePrefix + id + ".tar.gz ";
		String manifestFile = jobNamePrefix + id + ".manifest";
		String command = "touch " + manifestFile + " && find . -name '" + jobNamePrefix + id + "*' -print | sed 's/^\\.\\///' > " + manifestFile + " && " + 
				"tar --remove-files -czf " + outputFile + " -T " + manifestFile;
		if (markUnfinished) {
			command += " && touch " + id + "." + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
		} else {
			command += " && rm -f " + id + "." + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
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
			return transportConnection.sendExecToRemote(w);
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
	protected void secureResultsFiles(GridResult g) throws GridException, FileNotFoundException {
		if (g.getFileHandleIds() != null && g.getFileHandleIds().size() > 0) {
			// if copy job has not bee initiated initiate it now
			if (g.getChildResult(COPY_RESULTS_FILES_KEY) == null)
				copyResultsFiles(g);
			if (!isFinished(g.getChildResult(COPY_RESULTS_FILES_KEY))){
				logger.debug("found an existing unfinished copy job associated with this grid result so going to return now");
				return;
			}
			// copy job finished. Proceed to register
			List<FileHandle> fhl = new ArrayList<FileHandle>();
			for (Integer id : g.getFileHandleIds()) {
				logger.debug("calling: getFileService().getFileHandleById(" + id + ") ");
			    FileHandle fh = getFileService().getFileHandleById(id);
			    fhl.add(fh);
			}    
			logger.debug("calling: getFileService().register(" + fhl + ") ");
			g.addChildResult(REGISTER_FILES_KEY, getFileService().register(fhl, g.getChildResult(REGISTER_FILES_KEY)));
		}
	}
	
	protected void copyResultsFiles(GridResult g) throws GridException {
		GridResult r = g.getChildResult(COPY_RESULTS_FILES_KEY);
		if (r != null){
			logger.debug("found an existing copy job associated with this grid result so going to return now");
			return;
		}
		logger.debug("No Existing copy results files job registered so going to create one now");
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(g.getWorkingDirectory());
		w.setResultsDirectory(g.getResultsDirectory());
		w.setRegistering(true);
		w.setMode(ExecutionMode.TASK_ARRAY);
		w.addCommand("if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then ls -1 > /dev/null && touch " + w.getId() + "." + WorkUnit.PROCESSING_INCOMPLETE_FILENAME + "; fi");
		int files = 0;
		logger.trace("preparing to copy " + g.getFileHandleIds().size() + " files for GridResult " + g.getUuid().toString());
		for (Integer hid : g.getFileHandleIds()) {
		    logger.trace("adding fileHandleId " + hid);
		    FileHandle fh = getFileService().getFileHandleById(hid);
                    w.addCommand("COPY[" + files + "]=\"" + WorkUnit.OUTPUT_FILE_PREFIX + "_" + fh.getId() + "." + g.getUuid().toString() + " " + fh.getFileName() + "\"");
                    files++;
                    fh.setFileURI(this.gridFileService.remoteFileRepresentationToLocalURI(w.getResultsDirectory() + "/" + fh.getFileName()));
                    getFileService().addFile(fh);
                }
		w.setNumberOfTasks(files);
		w.addCommand("THIS=${COPY[ZERO_TASK_ID]}");
		w.addCommand("read -ra FILE <<< \"$THIS\"");
		w.addCommand("cp -f ${FILE[0]} ${" + WorkUnit.RESULTS_DIRECTORY + "}${FILE[1]}");
		w.addCommand("if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then rm -f " + w.getId() + "." + WorkUnit.PROCESSING_INCOMPLETE_FILENAME + "; fi");
		r = execute(w);
		g.addChildResult(COPY_RESULTS_FILES_KEY, r);
	}
	
	protected void cleanUpAbnormallyTerminatedJob(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		cleanUpAbnormallyTerminatedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString());
	}
	
	protected void cleanUpAbnormallyTerminatedJob(String hostname, String workingDirectory, String resultsDirectory, String id) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.warn("Cleaning FAILED job " + id + " at " + hostname + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(transportConnection.prefixRemoteFile(workingDirectory));
		
		String outputFile = jobNamePrefix + id + "-FAILED.tar.gz ";
		String manifestFile = jobNamePrefix + id + ".manifest";
		String command = "touch " + manifestFile + " && find . -name '" + jobNamePrefix + id + "*' -print | sed 's/^\\.\\///' > " + manifestFile + " && " + 
				"tar --remove-files -czf " + outputFile + " -T " + manifestFile;
		command += " && rm -f " + w.getId() + "." + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
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


	protected GridResult submitJob(WorkUnit w) throws MisconfiguredWorkUnitException, GridException {
		
		if (isJobExists(w)) {
			throw new GridAccessException("UUID already exists");
		}
		
		logger.debug("begin start job " + w.getId());
		// verify parallel environement is valid for this GWS if specified in work unit 
			
		SubmissionScript sss = getSubmissionScript(w);
		// TODO: user specific account settings
		if (getAccount() != null)
			sss.setAccount(getAccount());
		if(getMailRecipient() != null)
			sss.setMailRecipient(getMailRecipient());
		if(getMailCircumstances() != null)
			sss.setMailCircumstances(getMailCircumstances());
		if(getMaxRunTime() != null)
			sss.setMaxRunTime(getMaxRunTime());
		if (!w.getProcessMode().equals(ProcessMode.SINGLE) && w.getProcessorRequirements() > 1){
			if (w.getParallelEnvironment() != null && !w.getParallelEnvironment().isEmpty()){
				if (!getAvailableParallelEnvironments().contains(w.getParallelEnvironment()))
					throw new MisconfiguredWorkUnitException("Parallel environment specified in work unit is not registered as an available parallel environment in the Grid Work Service");
				sss.setParallelEnvironment(w.getParallelEnvironment(), w.getProcessorRequirements());
			} else {
				if (w.getProcessMode().equals(ProcessMode.MPI) && getDefaultMpiParallelEnvironment() != null && !getDefaultMpiParallelEnvironment().isEmpty()){
					logger.info("Seleted default MPI parallel environment");
					sss.setParallelEnvironment(getDefaultMpiParallelEnvironment(), w.getProcessorRequirements());
				} else if (getDefaultParallelEnvironment() != null && !getDefaultParallelEnvironment().isEmpty()){
					logger.info("Selected default parallel environment");
					sss.setParallelEnvironment(getDefaultParallelEnvironment(), w.getProcessorRequirements());
				} else if (getAvailableParallelEnvironments().size() == 1){
					logger.info("No default parallel environments set so falling back to only known parallel environment in set of available parallel environments");
					sss.setParallelEnvironment(getAvailableParallelEnvironments().iterator().next(), w.getProcessorRequirements());
				} else {
					if (!isNumProcConsumable)
						logger.warn("A non-single core process mode was selected but no parallel environment was set and not more than one consumable core was selected. This may be an error.");
				}
			}
		} else {
			if (w.getParallelEnvironment() != null)
				logger.warn("A single core process mode was selected yet a parallel environment was set. This may be an error.");
		}
		sss.setNumProcConsumable(isNumProcConsumable());
		if(getProject() != null)
			sss.setProject(getProject());
		else if (w.getProject() != null)
			// if the host does not have a project set, attempt to set from the work unit (for accounting).
			sss.setProject(w.getProject());
		if(getQueue() != null)
			sss.setQueue(getQueue());
		
		File script;
		try {
			script = File.createTempFile("wasp-", ".sge");
			logger.debug("creating temporary local sge script: " + script.getAbsolutePath().toString());
			BufferedWriter scriptHandle = new BufferedWriter(new FileWriter(script));
			scriptHandle.write(sss.toString());
			scriptHandle.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GridAccessException("unable to get local temp file ", e);
		}
		try {
			logger.debug("putting script file on remote server");
			gridFileService.put(script, w.getWorkingDirectory() + jobNamePrefix + w.getId() + ".sh");
			logger.debug("script file copied");
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to begin grid transaction ", e);
		} finally {
			logger.debug("deleting local temporary script file: " + script.getAbsolutePath().toString());
			script.delete();
		}
		
		//String submit = "cd " +  w.remoteWorkingDirectory + " && qsub " + jobNamePrefix + w.getId() + ".sh 2>&1";
		String submit = "qsub " + jobNamePrefix + w.getId() + ".sh 2>&1";
		w.setWrapperCommand(submit);
		GridResultImpl result = (GridResultImpl) transportConnection.sendExecToRemote(w);
		if (result.getExitStatus() > 0)
			throw new GridAccessException("Remote job submission failed");
		result.setExitStatus(-1); // reset to default value
		result.setJobStatus(GridJobStatus.SUBMITTED);
		result.setUuid(UUID.fromString(w.getId()));
		result.setId(jobNamePrefix + w.getId());
		result.setWorkingDirectory(w.getWorkingDirectory());
		result.setResultsDirectory(w.getResultsDirectory());
		result.setHostname(transportConnection.getHostName());
		result.setMode(w.getMode());
		result.setSecureResults(w.isSecureResults());
		if (w.isSecureResults()) {
			for (FileHandle fh : w.getResultFiles()) {
			    logger.trace("WorkUnit " + w.getId() + " is going to register FileHandle " + fh.getId());
				result.getFileHandleIds().add(fh.getId());
			}
			
		}
			
		if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
			if (w.getNumberOfTasks() == null)
				throw new MisconfiguredWorkUnitException("Task arrays need to set numberOfTasks (cannot be null)");
			result.setNumberOfTasks(w.getNumberOfTasks());
		}
		return (GridResult) result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAvailableParallelEnvironments(String commaDelimitedParallelEnvironments){
		for (String parallelEnv : commaDelimitedParallelEnvironments.split(","))
			this.parallelEnvironments.add(parallelEnv);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getAvailableParallelEnvironments() {
		return this.parallelEnvironments;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultParallelEnvironment() {
		return defaultParallelEnvironment;
	}

	/**
	 * set default parallel environment to be use for non-MPI jobs requiring multi-cores. Also adds this parallel environment to the set of 
	 * available parallel environments
	 * @param defaultParallelEnvironment
	 */
	@Override
	public void setDefaultParallelEnvironment(String defaultParallelEnvironment) {
		this.defaultParallelEnvironment = defaultParallelEnvironment;
		this.parallelEnvironments.add(defaultParallelEnvironment);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDefaultMpiParallelEnvironment() {
		return defaultMpiParallelEnvironment;
	}

	/**
	 * set default parallel environment to be use for MPI jobs. Also adds this parallel environment to the set of 
	 * available parallel environments
	 * @param defaultMpiParallelEnvironment
	 */
	@Override
	public void setDefaultMpiParallelEnvironment(String defaultMpiParallelEnvironment) {
		this.defaultMpiParallelEnvironment = defaultMpiParallelEnvironment;
		this.parallelEnvironments.add(defaultMpiParallelEnvironment);
	}

	protected SubmissionScript getSubmissionScript(WorkUnit w) throws GridException, MisconfiguredWorkUnitException {
		return new SgeSubmissionScript(w);
	}
	
	private JSONArray getJsonForJobInfo(Map<String, String> jobInfo){
		List<String> jobInfoList = new ArrayList<String>();
		for (String key : jobInfo.keySet())
			jobInfoList.add(key + "::" + jobInfo.get(key));
		return new JSONArray(jobInfoList);
	}
	
	private Map<String, String> getJobInfoFromJson(JSONArray json){
		Map<String, String> jobInfo = new LinkedHashMap<String, String>();
		for (int i=0; i< json.length(); i++){
			String[] keyValuePairs = json.getString(i).split("::");
			jobInfo.put(keyValuePairs[0], keyValuePairs[1]);
		}
		return jobInfo;
	}

	/**
	 * Inner class representing a standard SGE submission script.
	 * 
	 * @author calder
	 * 
	 */
	protected class SgeSubmissionScript implements SubmissionScript {
		
		public static final int HELD = 16;
		public static final int QUEUED = 64;
		public static final int RUNNING = 128;
		public static final int SUSPENDED = 256;
		public static final int WAITING = 2048;
		public static final int ERROR = 32768;
		public static final int SUSPENDED_ON_THRESHOLD = 65536;

		protected String schedulerFlag = "#$";

		protected WorkUnit w;
		protected String name = "not_set";
		public String jobName;
		protected String header = "";
		protected String preamble = "";
		protected String configuration = "";
		protected String command = "";
		protected String postscript = "";
		protected String account = "";
		protected String queue = "";
		protected String maxRunTime = "";
		protected String parallelEnvironment = "";
		protected String project = "";
		protected String mailRecipient = "";
		protected String mailCircumstances = "";
		protected boolean isNumProcConsumable = false;
		
		/**
		 * Default no-arg constructor is unused.
		 */
		@Deprecated
		protected SgeSubmissionScript() {
			super();
		}

		protected SgeSubmissionScript(WorkUnit w) throws GridException, MisconfiguredWorkUnitException {
			this.w = w;
			this.name = w.getId();

			String tid = "";
			
			jobName = jobNamePrefix + name;
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
			    tid = "-$TASK_ID";
			StringBuilder headerStrBuf = new StringBuilder();
			headerStrBuf.append("#!/bin/bash\n#\n")
					.append(getFlag()).append(" -N ").append(jobName).append("\n")
					.append(getFlag()).append(" -S /bin/bash\n")
					.append(getFlag()).append(" -V\n")
					.append(getFlag()).append(" -o ").append(w.remoteWorkingDirectory).append(jobNamePrefix).append(name).append(tid).append(".out\n")
					.append(getFlag()).append(" -e ").append(w.remoteWorkingDirectory).append(jobNamePrefix).append(name).append(tid).append(".err\n");
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				headerStrBuf.append(getFlag()).append(" -t 1-").append(w.getNumberOfTasks()).append("\n"); 
			}
			header = headerStrBuf.toString();
			
			StringBuilder preambleStrBuf = new StringBuilder();
			preambleStrBuf.append("\nset -o errexit\n") 	// die if any script returns non 0 exit code
					.append("set -o pipefail\n") 		// die if any script in a pipe returns non 0 exit code
					.append("set -o physical\n") 		// replace symbolic links with physical path
					.append("\ncd ").append(w.remoteWorkingDirectory).append("\n")
					.append(WorkUnit.JOB_NAME).append("=").append(jobNamePrefix).append(name).append("\n")
					.append(WorkUnit.WORKING_DIRECTORY).append("=").append(w.remoteWorkingDirectory).append("\n")
					.append(WorkUnit.TMP_DIRECTORY).append("=").append(w.getTmpDirectory()).append("\n")
					.append(WorkUnit.RESULTS_DIRECTORY).append("=").append(w.remoteResultsDirectory).append("\n");
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				preambleStrBuf.append(WorkUnit.TASK_ARRAY_ID).append("=${SGE_TASK_ID}\n")
					.append(WorkUnit.ZERO_TASK_ARRAY_ID).append("=$[WASP_TASK_ID - 1]\n")
					.append(WorkUnit.TASK_OUTPUT_FILE).append("=$WASPNAME-${WASP_TASK_ID}.out\n")
					.append(WorkUnit.TASK_END_FILE).append("=$WASPNAME:${WASP_TASK_ID}.end\n");
			}
			
			String metadata = transportConnection.getConfiguredSetting("metadata.root");
			if (!PropertyHelper.isSet(metadata)) {
				throw new NullResourceException("metadata folder not configured!");
			}
			preambleStrBuf.append(WorkUnit.METADATA_ROOT).append("=").append(transportConnection.prefixRemoteFile(metadata)).append("\n");
			
			int fi = 0;
			for (edu.yu.einstein.wasp.model.FileHandle f : w.getRequiredFiles()) {
				
				logger.debug("WorkUnit required file: " + f.getFileURI().toString());
				try {
					preambleStrBuf.append(WorkUnit.INPUT_FILE).append("[").append(fi).append("]=").append(provisionRemoteFile(f)).append("\n");
				} catch (FileNotFoundException e) {
					throw new MisconfiguredWorkUnitException("unknown file " + f.getFileURI());
				} 
				fi++;
			}
			fi = 0;
			logger.trace("file inspection for " + w.getResultFiles().size() + " file groups");
			logger.trace("adding file variables for " + w.getResultFiles().size() + " file handles");
			for (FileHandle fh : w.getResultFiles()) {
			    String filestr = WorkUnit.OUTPUT_FILE + "[" + fi + "]=" + WorkUnit.OUTPUT_FILE_PREFIX + "_" + fh.getId() +"."+ w.getId().replaceFirst(jobNamePrefix, "") + "\n";
			    preambleStrBuf.append(filestr);
			    fi++;
			}
			
			// write job info to .start file (as JSON) and also to stderr
			Map<String, String> jobInfo = new LinkedHashMap<String, String>();
			jobInfo.put(GRID_JOB_ID_KEY, "$JOB_ID");
			jobInfo.put(GRID_JOB_NAME, "${" + WorkUnit.JOB_NAME + "}");
			jobInfo.put(HOST_NODE_KEY, "`hostname -f`");
			jobInfo.put(START_TIME_KEY, "`date`");
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
				preambleStrBuf.append("if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then\n");
			preambleStrBuf.append("\necho \"").append(getJsonForJobInfo(jobInfo).toString().replaceAll("\"", "\\\\\"")).append("\" > ").append("${").append(WorkUnit.JOB_NAME).append("}.start\n");
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
				preambleStrBuf.append("fi\n");
			
			preamble = preambleStrBuf.toString();
			
			
			StringBuilder configurationStrBuf = new StringBuilder();
			for (String key : w.getEnvironmentVars().keySet()) {
				configurationStrBuf.append(key).append("=").append(w.getEnvironmentVars().get(key)).append("\n");
			}
			// if there is a configured setting to prepare the interpreter, do that here 
			String env = transportConnection.getConfiguredSetting("env");
			if (PropertyHelper.isSet(env)) {
				configurationStrBuf.append(env).append("\n");
			}
			// If the ProcessMode is set to MAX, get the configuration for this host and set processor reqs
			String pmodeMax = transportConnection.getConfiguredSetting("processmode.max");
			if (!PropertyHelper.isSet(pmodeMax)) {
				pmodeMax = "1";
			}
			logger.debug("WorkUnit was congigured to use " + w.getProcessorRequirements() + " threads");
			if (w.getProcessMode().equals(ProcessMode.MAX)) {
				w.setProcessorRequirements(new Integer(pmodeMax));
				logger.debug("WorkUnit recongigured to use " + w.getProcessorRequirements() + " threads (using ProcessMode.MAX)");
			}
			// If the ProcessMode is set to FIXED, make sure it does not exceed the max setting for this host.
			String pmodeMaximum = transportConnection.getConfiguredSetting("processmode.absolutemaximum");
			if (!PropertyHelper.isSet(pmodeMaximum)) {
				pmodeMaximum = "1";
			}
			
			Integer pmm = new Integer(pmodeMaximum);
			if (w.getProcessorRequirements() > pmm) {
				w.setProcessorRequirements(pmm);
				logger.debug("WorkUnit is recongigured to use " + w.getProcessorRequirements() + " (absolute maximum) threads");
			}
			if (transportConnection.getSoftwareManager() != null) {
				String config = transportConnection.getSoftwareManager().getConfiguration(w);
				if (config != null) {
					configurationStrBuf.append(config);
				}
			}
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
				configurationStrBuf.append("if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then\n");
			configurationStrBuf.append("printenv | sort > ").append("${").append(WorkUnit.JOB_NAME).append("}.env\n");
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
				configurationStrBuf.append("fi\n");
			
			
			configuration = configurationStrBuf.toString();
			command = w.getCommand();
			
			StringBuilder postscriptStrBuf = new StringBuilder();
			postscriptStrBuf.append("echo \"##### begin ${").append(WorkUnit.JOB_NAME).append("}\" > ").append(w.remoteWorkingDirectory).append("${").append(WorkUnit.JOB_NAME).append("}.command\n\n")
					.append("awk '/^##### preamble/,/^##### postscript|~$/' ")
					.append(w.remoteWorkingDirectory).append("${").append(WorkUnit.JOB_NAME).append("}.sh | sed 's/^##### .*$//g' | grep -v \"^$\" >> ")
					.append(w.remoteWorkingDirectory).append("${").append(WorkUnit.JOB_NAME).append("}.command\n")
					.append("echo \"##### end ${").append(WorkUnit.JOB_NAME).append("}\" >> ").append(w.remoteWorkingDirectory).append("${").append(WorkUnit.JOB_NAME).append("}.command\n");
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
				postscriptStrBuf.insert(0,"if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then\n").append("fi\n");
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				postscriptStrBuf.append("touch ${").append(WorkUnit.WORKING_DIRECTORY).append("}/${").append(WorkUnit.TASK_END_FILE).append("}\n")
					.append("echo completed on `hostname -f` `date` 1>&2\n");
			} else {
				postscriptStrBuf.append("touch ").append(w.remoteWorkingDirectory).append("${").append(WorkUnit.JOB_NAME).append("}.end\n")
					.append("echo completed on `hostname -f` `date` 1>&2\n");
			}
			postscript = postscriptStrBuf.toString();
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public boolean isNumProcConsumable() {
			return isNumProcConsumable;
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setNumProcConsumable(boolean isNumProcConsumable) {
			this.isNumProcConsumable = isNumProcConsumable;
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getFlag() {
			return schedulerFlag;
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMemory() {
			// 'mem_free' specifies how much memory should be free on a node before scheduling a job (or task) there 
			// 'h_vmem' specifies maximum memory a scheduled job (or task) may use. Will kill job / task 
			// with a memory allocation error if memory used exceeds this value
			int memRequested = w.getMemoryRequirements(); // allocation per slot
			int totalMemory = memRequested; // total for job
			if (memRequested > 1 && w.getProcessorRequirements() > 1 && getParallelEnvironment() != null && !getParallelEnvironment().isEmpty()){
				memRequested = Math.round(memRequested / w.getProcessorRequirements());
				totalMemory = memRequested * w.getProcessorRequirements(); // may be modified by rounding hence recalculate total
			}
			return getFlag() + " -l mem_free=" + memRequested + "G\n" +	WorkUnit.REQUESTED_GB_MEMORY + "=" + totalMemory + "\n";
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getProcs() {
			String procsStr = WorkUnit.NUMBER_OF_THREADS + "=" + w.getProcessorRequirements().toString() + "\n";
			if (isNumProcConsumable)
				procsStr += getFlag() + " -l p=" + w.getProcessorRequirements().toString() + "\n";
			return procsStr;
		}

		/**
		 * {@inheritDoc}
		 */
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(header)
				.append("\n\n##### resource requests\n\n")
				.append(getAccount())
				.append(getQueue())
				.append(getMaxRunTime());
			if (getParallelEnvironment() != null)
				sb.append(getParallelEnvironment());
			sb.append(getProcs())
				.append(getMemory()) 
				.append(getProject())
				.append(getMailRecipient())
				.append(getMailCircumstances())
				.append("\n\n##### preamble \n\n")
				.append(preamble) 
				.append("\n\n##### configuration \n\n")
				.append(configuration)
				.append("\n\n##### command \n\n")
				.append(command) 
				.append("\n\n##### postscript\n\n")
				.append(postscript);
			logger.debug(sb.toString());
			return sb.toString();
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getAccount() {
			return account;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setAccount(String account) {
			if (PropertyHelper.isSet(account)) this.account = getFlag() + " -A " + account + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getQueue() {
			return queue;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setQueue(String queue) {
			if (PropertyHelper.isSet(queue)) this.queue = getFlag() + " -q " + queue + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMaxRunTime() {
			return maxRunTime;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setMaxRunTime(String maxRunTime) {
			if (PropertyHelper.isSet(maxRunTime)) this.maxRunTime = getFlag() + " -l h_rt=" + maxRunTime + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getParallelEnvironment() {
			if (isNumProcConsumable && !w.getMode().equals(ProcessMode.MPI))
				return ""; // TODO: this is not ideal - satisfies Einstein requirement to reserve resources but not specify PE unless MPI
			return parallelEnvironment;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setParallelEnvironment(String parallelEnvironment, Integer procs) {
			if (PropertyHelper.isSet(parallelEnvironment)) this.parallelEnvironment = getFlag() + " -pe " + parallelEnvironment + " " + procs + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getProject() {
			return project;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setProject(String project) {
			if (PropertyHelper.isSet(project)) this.project = getFlag() + " -P " + project + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMailRecipient() {
			return mailRecipient;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setMailRecipient(String mailRecipient) {
			if (PropertyHelper.isSet(mailRecipient)) this.mailRecipient = getFlag() + " -M " + mailRecipient + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMailCircumstances() {
			return mailCircumstances;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setMailCircumstances(String mailCircumstances) {
			if (PropertyHelper.isSet(mailCircumstances)) this.mailCircumstances = getFlag() + " -m " + mailCircumstances + "\n";
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridFileService getGridFileService() {
		return gridFileService;
	}
	
	public void setGridFileService(GridFileService gridFileService) {
		this.gridFileService = gridFileService;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridTransportConnection getTransportConnection() {
		return transportConnection;
	}
	
	/**
	 * This needs to be fleshed out.  Files which are not on the compute host need to be copied into place
	 * prior to execution of the work unit.
	 * 
	 */
	protected String provisionRemoteFile(FileHandle file) throws FileNotFoundException, GridException {
		
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
	
	protected void doProvisionRemoteFile(edu.yu.einstein.wasp.model.FileHandle file) throws FileNotFoundException, GridException {
		// TODO: provision remote file from other host.
	}

    /** 
     * {@inheritDoc}
     */
    @Override
    public LinkedHashMap<Integer, String> getMappedTaskOutput(GridResult r) throws IOException {
        if (!r.getMode().equals(ExecutionMode.TASK_ARRAY))
            throw new WaspRuntimeException("Mapped task output is only available from TASK_ARRAY results");
        LinkedHashMap<Integer, String> result = new LinkedHashMap<Integer, String>();
        File f = File.createTempFile("wasp", "work");
        String path = r.getArchivedResultOutputPath();
        logger.debug("temporary tar file " + f.getAbsolutePath() + " for " + path);
        gridFileService.get(path, f);
        FileInputStream afis = new FileInputStream(f);
        GZIPInputStream agz = new GZIPInputStream(afis);
        TarArchiveInputStream a = new TarArchiveInputStream(agz);
        try {
            logger.debug("tar " + a.toString());
            TarArchiveEntry e;
            while ((e = a.getNextTarEntry()) != null) {
                logger.trace("saw tar file entry " + e.getName());
                Matcher filem = Pattern.compile("-([0-9]+?).out").matcher(e.getName());
                if (!filem.find())
                    continue;
                Integer record = Integer.parseInt(filem.group(1));
                logger.trace("record number " + record + " size " + e.getSize());
                result.put(record, IOUtils.toString(a, "UTF-8"));
                
            }
        } finally {
            a.close();
            agz.close();
            afis.close();
            f.delete();
        }
        return result;
    }
	
	protected InputStream readResultFile(GridResult r, String suffix, boolean keep) throws IOException {
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
		logger.trace("temporary tar file " + f.getAbsolutePath());
		String contentName = jobNamePrefix + r.getUuid() + suffix;
		logger.trace("looking for: " + contentName);
		FileInputStream afis = new FileInputStream(f);
		GZIPInputStream agz = new GZIPInputStream(afis);
		TarArchiveInputStream a = new TarArchiveInputStream(agz);
		logger.trace("tar " + a.toString());
		TarArchiveEntry e;
		while ((e = a.getNextTarEntry()) != null) {
			logger.trace("saw tar file entry " + e.getName());
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResultStdOut(GridResult r, long tailByteLimit) throws IOException {
		return getResultOutputFile(r, "out", tailByteLimit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getResultStdErr(GridResult r, long tailByteLimit) throws IOException {
		return getResultOutputFile(r, "err", tailByteLimit);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobScript(GridResult r) throws IOException {
		return getResultOutputFile(r, "sh", NO_FILE_SIZE_LIMIT);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getParsedJobSubmissionInfo(GridResult r) throws IOException {
		if (!r.getJobInfo().isEmpty())
			return r.getJobInfo();
		try{
			String info = getResultOutputFile(r, "start", NO_FILE_SIZE_LIMIT);
			if (info.isEmpty())
				throw new IOException(".start file for GridResult with id=" + r.getId() + " contains no data");
			return getJobInfoFromJson(new JSONArray(info));
		} catch (JSONException e){
			throw new IOException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * Filtered results from executing 'qacct -j <jobid>' at the command line
	 */
	@Override
	public Map<String, String> getParsedFinalJobClusterStats(GridResult r) throws IOException {
		Map<String, String> stats = new LinkedHashMap<String, String>();
		String data = getResultOutputFile(r, "stats", NO_FILE_SIZE_LIMIT);
		for (String line : data.split("\n")){
			line = line.trim();
			if (line.isEmpty() || line.startsWith("=") || line.startsWith("ru_") || line.startsWith("arid"))
				continue; // filter lines
			String[] elements = line.split("\\s+", 2);
			if (elements.length != 2)
				continue;
			stats.put(elements[0].replaceAll("_", " "), elements[1]);
		}
		return stats;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String> getParsedEnvironment(GridResult r) throws IOException{
		Map<String, String> env = new LinkedHashMap<String, String>();
		String data = getResultOutputFile(r, "env", NO_FILE_SIZE_LIMIT);
		for (String line : data.split("\n")){
			String[] elements = line.split("=", 2);
			if (elements.length != 2)
				continue;
			env.put(elements[0], elements[1]);
		}
		return env;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<String> getParsedSoftware(GridResult r) throws IOException{
		String data = getResultOutputFile(r, "sw", NO_FILE_SIZE_LIMIT);
		return transportConnection.getSoftwareManager().parseSoftwareListFromText(data);
	}
	
	private String getResultOutputFile(GridResult r, String type, long tailByteLimit) throws IOException {
		String path = r.getArchivedResultOutputPath(); 
		if (!path.isEmpty()){
			logger.debug("found file of type '." + type + "' from GridResult with id=" + r.getId() + " in archivedResultOutputPath");
			return getCompressedOutputFile(path, r.getId(), type, tailByteLimit);  // is a compressed archive registered in the result
		}
		String filename = r.getId() + "." + type;
		path = r.getWorkingDirectory() + "/" + filename;
		if (gridFileService.exists(path)){
			logger.debug("found file of type '." + type + "' from GridResult with id=" + r.getId() + " in unarchived working directory");
			return getUnregisteredFileContents(r, filename, tailByteLimit); // the file is still unregistered
		}
		path = getCompletedArchiveName(r);
		if (gridFileService.exists(path)) {
			logger.debug("found file of type '." + type + "' from GridResult with id=" + r.getId() + " in completed archive (not recorded in GridResult)");
			return getCompressedOutputFile(path, r.getId(), type, tailByteLimit); // is in a completed archive that wasn't appended to this GridResult
		}
		path = getFailedArchiveName(r);
		if (gridFileService.exists(path)) {
			logger.debug("found file of type '." + type + "' from GridResult with id=" + r.getId() + " in failed archive (not recorded in GridResult)");
			return getCompressedOutputFile(path, r.getId(), type, tailByteLimit); // is in a failed archive that wasn't appended to this GridResult
		}
		throw new IOException("Unable to to obtain file of type '." + type + "' from GridResult with id=" + r.getId() + 
				". No archive or working directory found matching GridResult");
	}
	
	private String getCompressedOutputFile(String path, String jobId, String type, long tailByteLimit) throws IOException {
		String result = "";
		File t = File.createTempFile("wasp", "work"); // tar archive
        logger.debug("temporary tar file " + t.getAbsolutePath() + " for " + path);
        gridFileService.get(path, t);
        FileInputStream afis = new FileInputStream(t);
        GZIPInputStream agz = new GZIPInputStream(afis);
        TarArchiveInputStream a = new TarArchiveInputStream(agz);
        try {
            logger.trace("tar " + a.toString());
            TarArchiveEntry e;
            while ((e = a.getNextTarEntry()) != null) {
                logger.trace("saw tar file entry " + e.getName());
                Matcher filem = Pattern.compile(jobId + "." + type).matcher(e.getName());
                if (!filem.find())
                    continue;
                logger.trace("matched " + e.getName() + " size " + e.getSize() + " bytes");
                result = IOUtils.toString(a, "UTF-8");
                if (tailByteLimit == MAX_FILE_SIZE){
                	byte[] utf8bytes = result.getBytes("UTF8");
                	if (utf8bytes.length > MAX_FILE_SIZE){
	        			byte[] trunc = new byte[(int) MAX_FILE_SIZE];
	        			for (int i = utf8bytes.length-1; i >= utf8bytes.length - trunc.length; i--){
	        				int j = i - (utf8bytes.length - trunc.length);
	        				trunc[j] = utf8bytes[i];
	        			}
	        			result = new String(trunc, "UTF8");
                	}
                }
                /* TODO: implement the following instead. Currently e.getFile() returns null for some reason!
                if (tailByteLimit == NO_FILE_SIZE_LIMIT)
                	result = IOUtils.toString(a, "UTF-8");
                else {
                	RandomAccessFile raf = null;
                	try{
	                	raf = new RandomAccessFile(e.getFile(), "r");
	                	long strLen = (raf.length() < MAX_32MB) ? raf.length() : MAX_32MB;
	                		
	                    raf.seek(strLen - MAX_32MB);
	                    byte[] b = new byte[(int) strLen];
	                    raf.readFully(b);
	                    result = new String(b, "UTF-8");
                	} finally {
                		if (raf != null)
                			raf.close();
                	}
                }
                */
                break;
            }
        } finally {
            a.close();
            agz.close();
            afis.close();
            t.delete();
        }
        return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUnregisteredFileContents(GridResult r, String filename, long tailByteLimit) throws IOException {
		
		String result;
		File f = null;
		try {
			result = "";
			f = File.createTempFile("wasp", "work");
			String path = r.getWorkingDirectory() + "/" + filename;
			gridFileService.get(path, f);
			if (tailByteLimit == NO_FILE_SIZE_LIMIT){
				result = IOUtils.toString(new FileInputStream(f), "UTF-8");
        	} else {
				RandomAccessFile raf = null;
	        	try{
	            	raf = new RandomAccessFile(f, "r");
	            	logger.debug("File '" + filename + "' size " + raf.length() + " bytes");
	            	long strLen = raf.length();
	            	long offset = 0;
	            	if (raf.length() > MAX_FILE_SIZE){
	            		strLen = MAX_FILE_SIZE;
	            		offset = strLen - MAX_FILE_SIZE;
	            	} 
	                raf.seek(offset);
	                byte[] b = new byte[(int) strLen];
	                raf.readFully(b);
	                result = new String(b, "UTF-8");
	        	} finally {
	        		if (raf != null)
	        			raf.close();
	        	}
        	}
		} finally {
			f.delete();
		}
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isNumProcConsumable() {
		return isNumProcConsumable;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setNumProcConsumable(boolean isNumProcConsumable) {
		this.isNumProcConsumable = isNumProcConsumable;
	}
	

	
}
