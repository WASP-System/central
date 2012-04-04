package edu.yu.einstein.wasp.grid.work;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.SingleHostResolver;
import edu.yu.einstein.wasp.grid.file.SshFileService;

public class SshServiceTest {

	GridWorkService sshwork;
	SshService sshtrans;
	SgeWorkService sgeWork;
	LocalhostTransportService localhost;
	GridWorkService localhostWork;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeTest(groups = { "ssh" })
	public void setUp() throws Exception {
		sshtrans = new SshService();
		SingleHostResolver shr = new SingleHostResolver();
		shr.setHostname("frankfurt.aecom.yu.edu");
		shr.setUsername("wasp");
		sshtrans.setHostResolver(shr);
		sshtrans.setIdentityFile("~/.ssh/id_rsa.testing");
		sshwork = new SshService();
		sshwork.setTransportService((GridTransportService) sshtrans);

		SingleHostResolver shr2 = new SingleHostResolver();
		shr2.setHostname("albert.einstein.yu.edu");
		shr2.setUsername("wasp");
		
		SshFileService sfs = new SshFileService();
		sfs.setHostResolver(shr2);
		sfs.setIdentityFile("~/.ssh/id_rsa.testing");
		
		sgeWork = new SgeWorkService();
		sgeWork.setTransportService((GridTransportService) sshtrans);
		sgeWork.waspGridFileService = sfs;
		SshService sshtrans2 = new SshService();		
		sshtrans2.setHostResolver(shr2);
		sshtrans2.setIdentityFile("~/.ssh/id_rsa.testing");
		
		sgeWork.setTransportService(sshtrans2);
		
		localhostWork = new SshService();
		localhostWork.setTransportService(new LocalhostTransportService());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterTest(groups = { "ssh" })
	public void tearDown() throws Exception {

	}

	@Test(groups = { "ssh" })
	public void execute() throws GridAccessException, GridUnresolvableHostException {
		try {
			WorkUnit w = new WorkUnit();
			w.setCommand("hostname -f");
			GridResult result = this.sshwork.execute(w);
			StringWriter writer = new StringWriter();
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
			String theString = writer.toString();
			System.out.println("result: " + theString);
			w.getConnection().disconnect();
			w = new WorkUnit();
			w.setCommand("sleep 1 && echo $SHELL\necho foo\npwd");
			w.addCommand("w");
			result = this.sshwork.execute(w);
			writer = new StringWriter();
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
			w.getConnection().disconnect();
			theString = writer.toString();
			System.out.println("result: " + theString);
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("error", e.getCause());
		}

	}

	@Test(groups = { "ssh" })
	public void executeLocal() throws GridAccessException, GridUnresolvableHostException {
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
		System.out.println("result: " + theString);
	}

	@Test(groups = { "ssh" })
	public void executeSge() throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		try {
			WorkUnit w = new WorkUnit();
			w.setWorkingDirectory("testing");
			w.setCommand("hostname -f");
			w.addCommand("ls -1 /apps1");
			w.addCommand("sleep 10");
			GridResult result = this.sgeWork.execute(w);
			StringWriter writer = new StringWriter();
			IOUtils.copy(result.getStdOutStream(), writer, "UTF-8");
			String theString = writer.toString();
			System.out.println("result sge: " + theString);
			w.getConnection().disconnect();
			while (!this.sgeWork.isFinished(result)) {
				Thread.sleep(2000);
				System.out.println("not finished");
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
