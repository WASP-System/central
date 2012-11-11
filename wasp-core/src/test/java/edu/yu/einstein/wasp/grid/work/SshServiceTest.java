package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.SingleHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.file.SshFileService;
import edu.yu.einstein.wasp.grid.work.SshTransportService;

public class SshServiceTest {

	GridTransportService gts;
	GridFileService gfs;
	GridWorkService sshws;
	GridWorkService sgews;
	LocalhostTransportService localhost;
	GridWorkService localhostWork;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeTest(groups = { "ssh" })
	public void setUp() throws Exception {
		gts = new SshTransportService();
		gts.setName("frankfurt");
		gts.setHostName("frankfurt.aecom.yu.edu");
		gts.setUserName("wasp");
		gts.setIdentityFile("~/.ssh/id_rsa.testing");
		gts.setUserDirIsRoot(true);
		gts.setSoftwareManager(new NoneManager());
		
		gfs = new SshFileService(gts);
		
		sshws = new SshWorkService(gts);
		sshws.setGridFileService(gfs);
		
		sgews = new SgeWorkService(gts);
		sgews.setGridFileService(gfs);
		
		localhostWork = new SshWorkService(new LocalhostTransportService());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterTest(groups = { "ssh" })
	public void tearDown() throws Exception {

	}

	@Test(groups = { "ssh" })
	public void execute() throws GridAccessException, GridUnresolvableHostException, GridExecutionException {
		try {
			WorkUnit w = new WorkUnit();
			w.setCommand("hostname -f");
			GridResult result = sshws.execute(w);
			StringWriter writer = new StringWriter();
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
			String theString = writer.toString();
			logger.debug("result: " + theString);
			w.getConnection().disconnect();
			w = new WorkUnit();
			w.setCommand("sleep 1 && echo $SHELL\necho foo\npwd");
			w.addCommand("w");
			result = sshws.execute(w);
			writer = new StringWriter();
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
			w.getConnection().disconnect();
			theString = writer.toString();
			logger.debug("result: " + theString);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("error", e.getCause());
		}

	}

	@Test(groups = { "ssh" })
	public void executeLocal() throws GridAccessException, GridUnresolvableHostException, GridExecutionException {
		WorkUnit w = new WorkUnit();
		w.setCommand("hostname -f");
		GridResult result = this.localhostWork.execute(w);
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String theString = writer.toString();
		logger.debug("result: " + theString);
	}

	@Test(groups = { "ssh" })
	public void executeSge() throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		try {
			WorkUnit w = new WorkUnit();
			w.setWorkingDirectory("testing");
			w.setCommand("hostname -f");
			w.addCommand("ls -1 /apps1");
			w.addCommand("sleep 10");
			GridResult result = sgews.execute(w);
			StringWriter writer = new StringWriter();
			IOUtils.copy(result.getStdErrStream(), writer, "UTF-8");
			String theString = writer.toString();
			logger.debug("result sge: " + theString);
			while (!sgews.isFinished(result)) {
				Thread.sleep(2000);
				logger.debug("not finished");
			}
			w.getConnection().disconnect();
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("error", e.getCause());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
