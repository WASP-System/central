/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;

/**
 * {@link GridWorkService} implementation for Sun Grid Engine.  Tested with Grid Engine v6.1.
 * 
 * @author calder
 * 
 */
public class SgeWorkService implements GridWorkService {
	
	private static final Logger logger = Logger.getLogger(SgeWorkService.class);

	private String namePrefix = "WASP-";
	public void setNamePrefix(String np) {
		this.namePrefix = np + "-";
	}

	private GridTransportService transportService;

	@Autowired
	protected GridFileService waspGridFileService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTransportService(GridTransportService ts) {
		this.transportService = ts;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GridTransportService getTransportService() {
		return this.transportService;
	}

	/**
	 * {@inheritDoc}
	 * @throws GridUnresolvableHostException 
	 */
	@Override
	public GridResult execute(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		if (w.getWorkingDirectory() == null)
			throw new GridAccessException("must set working directory");
		return startJob(w);
	}

	/**
	 * {@inheritDoc}
	 * @throws GridUnresolvableHostException 
	 */
	@Override
	public boolean isFinished(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		if (!isJobExists(g))
			throw new GridAccessException("Job does not exist on remote host");

		boolean ended = isJobEnded(g);
		boolean started = isJobStarted(g);
		boolean died = false;
		
		logger.debug("Job status (started, ended): " + started + ", " + ended); 

		GridTransportConnection conn = transportService.connect(g.getHostname());
		WorkUnit w = new WorkUnit();
		w.setConnection(conn);
		w.setCommand("qstat -xml -j " + this.namePrefix + "-" + g.getUuid().toString() + " | sed 's/<\\([/]\\)*>/<\\1a>/g'");
		GridResult result = conn.sendExecToRemote(w);
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

			if (!unknown) {
				NodeList jatstatus = stdout.getElementsByTagName("JAT_status");
				if (jatstatus.getLength() > 0) {
					String status = jatstatus.item(0).getTextContent();
					int bits = new Integer(status).intValue();
					if ((bits & SgeSubmissionScript.ERROR) == SgeSubmissionScript.ERROR) {
						died = true;
					}
				}
			} else {
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
		} finally {
			w.getConnection().disconnect();
		}
		if (died) {
			cleanUpAbnormallyTerminatedJob(g);
			throw new GridExecutionException("abnormally terminated job");
		}
		if (ended) {
			cleanUpCompletedJob(g);
		}
		return ended;
	}

	private boolean isJobExists(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		return isJobExists(transportService.getHostResolver().getHostname(w), w.getWorkingDirectory(), w.getId());
	}

	private boolean isJobExists(GridResult g) throws GridAccessException {
		return isJobExists(g.getHostname(), g.getWorkingDirectory(), g.getUuid().toString());
	}

	private boolean isJobExists(String hostname, String workingDirectory, String id) throws GridAccessException {
		return testSgeFileExists(hostname, workingDirectory, id, "sh");
	}

	private boolean isJobEnded(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getHostname(), g.getWorkingDirectory(), g.getUuid().toString(), "end");
	}

	private boolean isJobStarted(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getHostname(), g.getWorkingDirectory(), g.getUuid().toString(), "start");
	}

	private boolean testSgeFileExists(String hostname, String workingDirectory, String id, String suffix) throws GridAccessException {
		try {
			return waspGridFileService.exists(hostname, workingDirectory + namePrefix + id + "." + suffix);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("Unable to connect to remote host: ", e.getCause());
		}
	}
	
	private void cleanUpCompletedJob(GridResult g) throws GridAccessException, GridUnresolvableHostException {
		cleanUpCompletedJob(g.getHostname(), g.getWorkingDirectory(), g.getUuid().toString());
	}

	private void cleanUpCompletedJob(String hostname, String workingDirectory, String id) throws GridAccessException, GridUnresolvableHostException {
		GridTransportConnection conn = transportService.connect(hostname);
		WorkUnit w = new WorkUnit();
		w.setConnection(conn);
		w.setCommand("cd $HOME/" + workingDirectory + " && tar --remove-files -czvf " + namePrefix + id + ".tar.gz " + namePrefix + id + ".*");
		conn.sendExecToRemote(w);
	}
	
	private void cleanUpAbnormallyTerminatedJob(GridResult g) throws GridAccessException, GridUnresolvableHostException {
		cleanUpAbnormallyTerminatedJob(g.getHostname(), g.getWorkingDirectory(), g.getUuid().toString());
	}
	
	private void cleanUpAbnormallyTerminatedJob(String hostname, String workingDirectory, String id) throws GridAccessException, GridUnresolvableHostException {
		GridTransportConnection conn = transportService.connect(hostname);
		WorkUnit w = new WorkUnit();
		w.setConnection(conn);
		w.setCommand("cd $HOME/" + workingDirectory + " && tar --remove-files -czvf " + namePrefix + id + "-FAILED-`date +%s`.tar.gz " + namePrefix + id + ".*");
		conn.sendExecToRemote(w);
	}


	private GridResult startJob(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {

		String host;
		UUID resultID = UUID.randomUUID();
		w.setId(resultID.toString());
		if (isJobExists(w)) {
			throw new GridAccessException("UUID already exists");
		}

		File script;
		try {
			script = File.createTempFile("wasp-", ".sge");
			BufferedWriter scriptHandle = new BufferedWriter(new FileWriter(script));
			SgeSubmissionScript sss = new SgeSubmissionScript(w);
			GridHostResolver ghr = transportService.getHostResolver();
			if (ghr.getAccount(w) != null)
				sss.setAccount(ghr.getAccount(w));
			if(ghr.getMailRecipient(w) != null)
				sss.setMailRecipient(ghr.getMailRecipient(w));
			if(ghr.getMailCircumstances(w) != null)
				sss.setMailCircumstances(ghr.getMailCircumstances(w));
			if(ghr.getMaxRunTime(w) != null)
				sss.setMaxRunTime(ghr.getMaxRunTime(w));
			if(ghr.getParallelEnvironmentString(w) != null)
				sss.setParallelEnvironment(ghr.getParallelEnvironmentString(w), w.getProcessorRequirements());
			if(ghr.getProject(w) != null)
				sss.setProject(ghr.getProject(w));
			if(ghr.getQueue(w) != null)
				sss.setQueue(ghr.getQueue(w));
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
			host = transportService.getHostResolver().getHostname(w);
			waspGridFileService.put(script, host, w.getWorkingDirectory() + namePrefix + w.getId() + ".sh");
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to begin grid transaction ", e.getCause());
		} finally {
			script.delete();
		}

		transportService.connect(w);
		String submit = "cd " + w.getWorkingDirectory() + " && qsub " + namePrefix + w.getId() + ".sh";
		w.setWrapperCommand(submit);
		GridResultImpl result = (GridResultImpl) w.getConnection().sendExecToRemote(w);
		result.setUuid(resultID);
		result.setWorkingDirectory(w.getWorkingDirectory());
		result.setHostname(host);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return (GridResult) result;
	}

	/**
	 * Inner class representing a standard SGE submission script.
	 * 
	 * @author calder
	 * 
	 */
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

		public SgeSubmissionScript(WorkUnit w) {
			this.w = w;
			this.name = w.getId();
			header = "#!/bin/bash\n#\n" +
					"#$ -N " + namePrefix + name + "\n" +
					"#$ -S /bin/bash\n" +
					"#$ -V\n" +
					"#$ -o $HOME/" + w.getWorkingDirectory() + namePrefix + name + ".out\n" +
					"#$ -e $HOME/" + w.getWorkingDirectory() + namePrefix + name + ".err\n";
			preamble = "cd $HOME/" + w.getWorkingDirectory() + "\n" +
					"WASPNAME=" + name + "\n" +
					"set -o errexit\n" + // die if any script returns non 0 exit
											// code
					"set -o pipefail\n" + // die if any script in a pipe returns
											// non 0 exit code
					"set -o physical\n" + // replace symbolic links with
											// physical path
					"touch " + namePrefix + "${WASPNAME}.start\n" +
					"echo submitted to host `hostname -f` `date` 1>&2";
			command = w.getCommand();
			postscript = "echo \"##### begin ${WASPNAME}\" > " + namePrefix + "${WASPNAME}.command\n\n" +
					"awk '/^##### preamble/,/^##### postscript|~$/' " + namePrefix + "${WASPNAME}.sh | sed 's/^##### .*$//g' | grep -v \"^$\" >> " + namePrefix + "${WASPNAME}.command\n" +
					"echo \"##### end ${WASPNAME}\" >> " + namePrefix + "${WASPNAME}.command\n" + 
					"touch " + namePrefix + "${WASPNAME}.end\n";
		}
		
		public String getMemory() {
			return this.memory;
		}

		public void setMemory(int memInGB) {
			this.memory = "#$ -l mem_free=" + memInGB + "G\n";
		}
		
		public String getProcs() {
			return this.procs;
		}

		public void setProcs(Integer threads) {
			this.procs = "#$ -l p=" + threads.toString() + "\n";
		}

		public String toString() {
			String pe = "";
			if (w.getMode() == ExecutionMode.MPI)
				pe = getParallelEnvironment();
			
			return header + "\n\n##### resource requests\n\n" +
					getAccount() + 
					getQueue() +
					getMaxRunTime() +
					pe +
					getProject() + 
					getMailRecipient() +
					getMailCircumstances() +
					getProcs() +
					getMemory() + 
					"\n\n##### preamble \n\n" +
					preamble + "\n\n##### command \n\n" +
					command + "\n\n##### postscript\n\n" +
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
			this.account = this.account = "#$ -A " + account + "\n";
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
			this.queue = "#$ -q " + queue + "\n";
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
			this.maxRunTime = "#$ -l h_rt=" + maxRunTime + "\n";
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
			this.parallelEnvironment = "#$ -pe " + parallelEnvironment + " " + procs + "\n";
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
			this.project = "#$ -P " + project + "\n";
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
			this.mailRecipient = "#$ -M " + mailRecipient + "\n";
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
			this.mailCircumstances = "#$ -m " + mailCircumstances + "\n";
		}

	}

}
