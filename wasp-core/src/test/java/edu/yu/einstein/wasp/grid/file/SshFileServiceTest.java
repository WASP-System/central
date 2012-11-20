/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.SingleHostResolver;
import edu.yu.einstein.wasp.grid.work.GridTransportService;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SshTransportService;
import edu.yu.einstein.wasp.grid.work.SshWorkService;

/**
 * @author calder
 *
 */
public class SshFileServiceTest {
	
	GridFileService gfs;
	GridTransportService gts;
	GridWorkService gws;
	GridHostResolver ghr;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public void setUp() throws Exception {
		
		gts = new SshTransportService();
		gts.setName("frankfurt");
		gts.setHostName("frankfurt.aecom.yu.edu");
		gts.setUserName("wasp");
		gts.setIdentityFile("~/.ssh/id_rsa.testing");
		gts.setUserDirIsRoot(true);
		
		gfs = new SshFileService(gts);
		
		gws = new SshWorkService(gts);
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
		logger.debug("configured transport service: " + gts.getUserName() + "@" + gts.getHostName());
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

}
