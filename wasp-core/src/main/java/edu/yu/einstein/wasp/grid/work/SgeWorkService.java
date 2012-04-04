/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;

/**
 * {@link GridWorkService} implementation for Sun Grid Engine.  Tested with Grid Engine v6.1.
 * 
 * @author calder
 * 
 */
public class SgeWorkService implements GridWorkService {

	private String namePrefix = "WASP-";

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
		boolean died = false;

		GridTransportConnection conn = transportService.connect(g.getHostname());
		WorkUnit w = new WorkUnit();
		w.setConnection(conn);
		w.setCommand("qstat -xml -j " + this.namePrefix + g.getUuid().toString() + " | sed 's/<\\([/]\\)*>/<\\1a>/g'");
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
			Document stdout = docBuilder.parse(result.getStdOutStream());
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
				if (!ended && isJobStarted(g)) {
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
		if (ended) {
			if (died) {
				cleanUpAbnormallyTerminatedJob(g);
				throw new GridExecutionException("abnormally terminated job");
			} else {
				cleanUpCompletedJob(g);
			}
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
		w.setCommand("cd $HOME/" + workingDirectory + " && tar --remove-files -czvf " + namePrefix + id + "failed-`date +%s`.tar.gz " + namePrefix + id + ".*");
		conn.sendExecToRemote(w);
	}
	
	private void cleanUpAbnormallyTerminatedJob(GridResult g) throws GridAccessException, GridUnresolvableHostException {
		cleanUpAbnormallyTerminatedJob(g.getHostname(), g.getWorkingDirectory(), g.getUuid().toString());
	}
	
	private void cleanUpAbnormallyTerminatedJob(String hostname, String workingDirectory, String id) throws GridAccessException, GridUnresolvableHostException {
		GridTransportConnection conn = transportService.connect(hostname);
		WorkUnit w = new WorkUnit();
		w.setConnection(conn);
		w.setCommand("cd $HOME/" + workingDirectory + " && tar --remove-files -czvf " + namePrefix + id + ".tar.gz " + namePrefix + id + ".*");
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

		protected String name = "not_set";
		protected String header = "";
		protected String requests = "";
		protected String preamble = "";
		protected String command = "";
		protected String postscript = "";

		public SgeSubmissionScript(WorkUnit w) {
			this.name = w.getId();
			header = "#!/bin/bash\n#\n" +
					"#$ -N " + namePrefix + name + "\n" +
					"#$ -S /bin/bash\n" +
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

		public void setMemory(int memInGB) {
			requests += "#$ -l mem_free=" + memInGB + "G\n";
		}

		public void setThreads(int threads) {
			requests += "#$ -l p=" + threads + "G\n";
		}

		public String toString() {
			return header + "\n\n##### resource requests\n\n" +
					requests + "\n\n##### preamble \n\n" +
					preamble + "\n\n##### command \n\n" +
					command + "\n\n##### postscript\n\n" +
					postscript;
		}

	}

}
