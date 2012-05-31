/**
 * 
 */
package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.testng.AssertJUnit;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.grid.SingleHostResolver;

/**
 * @author calder
 *
 */
public class SshFileServiceTest {
	
	SshFileService sfs;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeMethod
	public void setUp() throws Exception {
		sfs = new SshFileService();
		SingleHostResolver shr = new SingleHostResolver("frankfurt.aecom.yu.edu", "wasp");
		sfs.setHostResolver(shr);
		sfs.setIdentityFile("~/.ssh/id_rsa.testing");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterMethod
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link edu.yu.einstein.wasp.grid.file.SshFileService#put(java.io.File, java.lang.String, java.lang.String)}.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	@Test (groups = { "ssh" })
	public void testPut() throws IOException, URISyntaxException {
		URL testFile = getClass().getClassLoader().getResource("wasp/grid/file/ssh/test.txt");
		sfs.put(new File(testFile.toURI()).getAbsoluteFile(), "frankfurt.aecom.yu.edu", "testing/test.txt");
		sfs.put(new File(testFile.toURI()).getAbsoluteFile(), "frankfurt.aecom.yu.edu", "testing/test2.txt");
	}
	
	/**
	 * Test method for {@link edu.yu.einstein.wasp.grid.file.SshFileService#exists(java.lang.String, java.lang.String)}.
	 */
	@Test (groups = { "ssh" }, dependsOnMethods = { "testPut" } )
	public void testExists() {
		try {
			AssertJUnit.assertTrue(sfs.exists("frankfurt.aecom.yu.edu", "testing/test.txt"));
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
		sfs.get("frankfurt.aecom.yu.edu", "testing/test.txt", temp);
		temp.delete();
	}
	
	/**
	 * Test method for {@link edu.yu.einstein.wasp.grid.file.SshFileService#delete(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 */
	@Test (groups = { "ssh" }, dependsOnMethods = { "testExists" } )
	public void testDelete() throws IOException {
		sfs.delete("frankfurt.aecom.yu.edu", "testing/test.txt");
		sfs.delete("frankfurt.aecom.yu.edu", "testing/test2.txt");
	}

}
