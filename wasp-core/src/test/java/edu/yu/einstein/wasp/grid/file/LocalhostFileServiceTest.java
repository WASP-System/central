package edu.yu.einstein.wasp.grid.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.grid.work.GridTransportConnection;

@ContextConfiguration(locations = {"classpath:/META-INF/application-context-ssh-test.xml" })
public class LocalhostFileServiceTest extends AbstractTestNGSpringContextTests {
		
	LocalhostFileService lfs;
	
	@Autowired
	GridTransportConnection testGridTransportConnection;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Path testPath = null;
	
	private String testFolder =  "/tmp_" + UUID.randomUUID();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public void setUp() throws Exception {
		testPath = Paths.get(System.getProperty("user.home") + testFolder);
		Files.createDirectory(testPath);
		lfs = new LocalhostFileService(testGridTransportConnection);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public void tearDown() throws Exception {
		Files.deleteIfExists(testPath);
	}
	
	@Test(groups = "unit-tests")
	public void testGetLocalhostFileURI(){
		String path = "/foo/bar.txt";
		Path pathObj = lfs.getLocalhostFilePath(path);
		String userHome = System.getProperty("user.home");
		logger.debug(pathObj.toString());
		Assert.assertEquals(pathObj.toString(), userHome + path);
	}
	
	@Test(groups = "unit-tests")
	public void testPut() throws IOException{
		Path localPath = testPath.resolve("bar.txt");
		Path remotePath = testPath.resolve("baz.txt");
		localPath = Files.createFile(localPath);
		File localFile = localPath.toFile();
		lfs.put(localFile, testFolder + "/baz.txt");
		Assert.assertTrue(Files.exists(remotePath));
		Files.delete(localPath);
		Files.delete(remotePath);
	}
	
	@Test(groups = "unit-tests")
	public void testGet() throws IOException{
		Path localPath = testPath.resolve("bar.txt");
		Path remotePath = testPath.resolve("baz.txt");
		remotePath = Files.createFile(remotePath);
		File localFile = localPath.toFile();
		lfs.get(testFolder + "/baz.txt", localFile);
		Assert.assertTrue(Files.exists(localPath));
		Files.delete(localPath);
		Files.delete(remotePath);
	}
		
}
