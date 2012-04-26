package edu.yu.einstein.wasp.model;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class TestFile{
	
	private static final Logger logger = Logger.getLogger(TestFile.class);
  
	// paths to test
	private static final String TestPath1 = "foo/file1.txt";
	private static final String TestPath2 = "/foo/bar/file2.txt";
	private static final String TestPath3 = "\\file3.txt"; // need to escape the \
	private static final String TestPath4 = "file4.txt";
	
	// expected files
	private static final String File1 = "file1.txt";
	private static final String File2 = "file2.txt";
	private static final String File3 = "file3.txt";
	private static final String File4 = "file4.txt";
	
	// expected PathsToFolder
	private static final String PathToFolder1 = "foo";
	private static final String PathToFolder2 = "/foo/bar";
	private static final String PathToFolder3 = "";
	private static final String PathToFolder4 = "";
	private File[] files;
	
	@BeforeTest
	public void setupTest(){
		files = new File[4];
		files[0] = new File();
		files[1] = new File();
		files[2] = new File();
		files[3] = new File();
		files[0].setAbsolutePath(TestPath1);
		files[1].setAbsolutePath(TestPath2);
		files[2].setAbsolutePath(TestPath3);
		files[3].setAbsolutePath(TestPath4);
	}
	
	@Test (groups = "unit-tests")
	public void TestGetFileName() {
		String fileName = files[0].getFileName();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File1);
		
		fileName = files[1].getFileName();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File2);
		
		fileName = files[2].getFileName();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File3);
		
		fileName = files[3].getFileName();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File4);
	}
	
	@Test (groups = "unit-tests")
	public void TestGetAbsolutePathToFileFolder() {
		String fileName = files[0].getAbsolutePathToFileFolder();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, PathToFolder1);
		
		fileName = files[1].getAbsolutePathToFileFolder();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, PathToFolder2);
		
		fileName = files[2].getAbsolutePathToFileFolder();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, PathToFolder3);
		
		fileName = files[3].getAbsolutePathToFileFolder();
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, PathToFolder4);
	}
  

}
