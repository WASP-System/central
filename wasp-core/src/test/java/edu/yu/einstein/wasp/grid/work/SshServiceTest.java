package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.file.SshFileService;

@ContextConfiguration(locations = {"classpath:/META-INF/application-context-ssh-test.xml" })
public class SshServiceTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private SshTransportConnection testGridTransportConnection;
	
	
	// SshTransportConnection stc;
	GridFileService gfs;
	GridWorkService sshws;
	@Autowired
	GridWorkService sgews;
	LocalhostTransportConnection localhost;
	GridWorkService localhostWork;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeTest(groups = { "ssh" })
	public void setUp() throws Exception {
		
//		stc = new SshTransportConnection();
//		stc.setName("frankfurt");
//		stc.setHostName("frankfurt.aecom.yu.edu");
//		stc.setUserName("wasp");
//		stc.setIdentityFile("~/.ssh/id_rsa.testing");
//		stc.setUserDirIsRoot(true);
//		stc.setSoftwareManager(new NoneManager());
//		stc.afterPropertiesSet();
		
//		
		//testGridTransportConnection.toString();
//		
		
//		sshws.setGridFileService(gfs);
//		
//		sgews = new SgeWorkService(gts);
//		sgews.setGridFileService(gfs);
//		
//		localhostWork = new SshWorkService(new LocalhostTransportService());
	}	
	
	@BeforeClass
	public void instantiate() throws Exception {
		gfs = new SshFileService(testGridTransportConnection);
		sshws = new SshWorkService(testGridTransportConnection);
		//sgews = new SgeWorkService(testGridTransportConnection);
		sgews.setGridFileService(gfs);
	}
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@AfterTest(groups = { "ssh" } )
	public void tearDown() throws Exception {
		// stc.doDisconnect();
	}
	
	@Test(groups = { "ssh" })
	public void connect() throws Exception {
		logger.debug(testGridTransportConnection.getHostName());
	}
	
	@Test(groups = {"ssh"})
	public void shellTest() throws Exception {
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory("~");
		w.setCommand("hostname -f && export FOOBAR=1");
		GridResult result;
		try {
			result = testGridTransportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridException(e.getLocalizedMessage(), e);
		}
		
		InputStream is = result.getStdOutStream();
		StringWriter writer = new StringWriter();
		IOUtils.copy(is, writer);
		String hostname = writer.toString();
		logger.debug("hostname: " + hostname);
		
		WorkUnit w2 = new WorkUnit();
		w2.setWorkingDirectory("~");
		w2.setCommand("echo $FOOBAR");
		GridResult result2;
		try {
			result2 = testGridTransportConnection.sendExecToRemote(w2);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridException(e.getLocalizedMessage(), e);
		}
		
		InputStream is2 = result2.getStdOutStream();
		StringWriter writer2 = new StringWriter();
		IOUtils.copy(is2, writer2);
		String echo = writer2.toString();
		logger.debug("echo: " + echo);
	}
	
	@Test(groups = {"ssh"})
	public void proxyTest() throws Exception {
		logger.debug("Proxy: " + testGridTransportConnection.toString());
	}
	

	@Test(groups = { "ssh" })
	public void execute() throws GridException {
		try {
			WorkUnit w = new WorkUnit();
			w.setCommand("hostname -f");
			w.setWorkingDirectory("/testing/");
			w.setResultsDirectory("/testing/");
			GridResult result = sshws.execute(w);
			StringWriter writer = new StringWriter();
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
			String theString = writer.toString();
			logger.debug("result: " + theString);
			w = new WorkUnit();
			w.setWorkingDirectory("/testing/");
			w.setResultsDirectory("/testing/");
			w.setCommand("sleep 1 && echo $SHELL\necho foo\npwd");
			w.addCommand("w");
			result = sshws.execute(w);
			writer = new StringWriter();
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
			theString = writer.toString();
			logger.debug("result: " + theString);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("error", e.getCause());
		}

	}
//
//	@Test(groups = { "ssh" })
//	public void executeLocal() throws GridException {
//		WorkUnit w = new WorkUnit();
//		w.setCommand("hostname -f");
//		w.setWorkingDirectory("/testing/");
//		w.setResultsDirectory("/testing/");
//		GridResult result = this.localhostWork.execute(w);
//		StringWriter writer = new StringWriter();
//		try {
//			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String theString = writer.toString();
//		logger.debug("result: " + theString);
//	}
//
	
	@Test(groups = { "ssh" })
	public void executeSge() throws GridException {
		try {
			WorkUnit w = new WorkUnit();
			w.setWorkingDirectory("/testing");
			w.setResultsDirectory("/testing/");
			w.setCommand("hostname -f");
			w.addCommand("ls -1 /apps1");
			w.addCommand("sleep 10");
			GridTransportConnection gridTransportConnection = sgews.getTransportConnection();
			GridResult result = sgews.execute(w);
			StringWriter writer = new StringWriter();
			IOUtils.copy(result.getStdErrStream(), writer, "UTF-8");
			String theString = writer.toString();
			logger.debug("result sge: " + theString);
			while (!sgews.isFinished(result)) {
				Thread.sleep(2000);
				logger.debug("not finished");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("error", e.getCause());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
