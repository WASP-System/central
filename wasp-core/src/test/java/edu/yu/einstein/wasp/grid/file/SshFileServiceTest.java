/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.SingleHostResolver;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SshWorkService;

/**
 * @author calder
 *
 */
@ContextConfiguration(locations = {"classpath:/META-INF/application-context-ssh-test.xml" })
public class SshFileServiceTest extends AbstractTestNGSpringContextTests {
	
	GridFileService gfs;
	
	@Autowired
	GridTransportConnection testGridTransportConnection;
	GridWorkService gws;
	GridHostResolver ghr;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public void setUp() throws Exception {
		
		gfs = new SshFileService(testGridTransportConnection);
		
		gws = new SshWorkService(testGridTransportConnection);
		gws.setGridFileService(gfs);
		
		ghr = new SingleHostResolver(gws);
			
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link edu.yu.einstein.wasp.grid.file.SshFileService#put(java.io.File, java.lang.String, java.lang.String)}.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	@Test (groups = { "ssh" })
	public void testPut() throws IOException, URISyntaxException {
		logger.debug("configured transport service: " + testGridTransportConnection.getUserName() + "@" + testGridTransportConnection.getHostName());
		URL testFile = getClass().getClassLoader().getResource("wasp/grid/file/ssh/test.txt");
		gfs.put(new File(testFile.toURI()).getAbsoluteFile(), "testing/test.txt");
		gfs.put(new File(testFile.toURI()).getAbsoluteFile(), "testing/test2.txt");
	}
	
	/**
	 * Test method for {@link edu.yu.einstein.wasp.grid.file.SshFileService#exists(java.lang.String, java.lang.String)}.
	 */
	@Test (groups = { "ssh" }, dependsOnMethods = { "testPut" } )
	public void testExists() {
		try {
			AssertJUnit.assertTrue(gfs.exists("testing/test.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Test method for {@link edu.yu.einstein.wasp.grid.file.SshFileService#get(java.lang.String, java.lang.String, java.io.File)}.
	 * @throws IOException 
	 */
	@Test (groups = { "ssh" }, dependsOnMethods = { "testPut" } )
	public void testGet() throws IOException {
		File temp = File.createTempFile("junit", "tmp");
		gfs.get("testing/test.txt", temp);
		temp.delete();
	}
	
	/**
	 * Test method for {@link edu.yu.einstein.wasp.grid.file.SshFileService#delete(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test (groups = { "ssh" }, dependsOnMethods = { "testExists" } )
	public void testDelete() throws IOException {
		gfs.delete("testing/test.txt");
		gfs.delete("testing/test2.txt");
	}
	
	@Test
	public void testURI() throws Exception {
		URI remotefile = gfs.remoteFileRepresentationToLocalURI("/illumina/test.txt");
		URI remoteuri = gfs.remoteFileRepresentationToLocalURI("sftp://wasp@remotehost.net/folder/file.txt");
		Assert.assertEquals(remotefile.toString(), "file://" + testGridTransportConnection.getHostName() + "/illumina/test.txt");
		
		// This is the correct behavior, but the remote host should be set by the transport connection
		// not the user. 
		Assert.assertEquals(remoteuri.toString(), "file://remotehost.net/folder/file.txt");
	}

}
