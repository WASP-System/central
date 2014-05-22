package edu.yu.einstein.wasp.model;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class TestFile{
	
	private static final Logger logger = LoggerFactory.getLogger(TestFile.class);
  
	// paths to test
	private static final String TestPath1 = "foo/file1.txt";
	private static final String TestPath2 = "/foo/bar/file2.txt";
	private static final String TestPath3 = "/file3.txt";
	private static final String TestPath4 = "file4.txt";
	private static final String TestPath5 = "/foo/bar/file5.txt.345543";
	
	// expected files
	private static final String File1 = "file1.txt";
	private static final String File2 = "file2.txt";
	private static final String File3 = "file3.txt";
	private static final String File4 = "file4.txt";
	private static final String File5 = "file5.txt.345543";
	
	private FileHandle[] files;
	
	@BeforeTest
	public void setupTest() throws Exception{
		files = new FileHandle[5];
		files[0] = new FileHandle();
		files[1] = new FileHandle();
		files[2] = new FileHandle();
		files[3] = new FileHandle();
		files[4] = new FileHandle();
		files[0].setFileURI(new URI(TestPath1));
		files[1].setFileURI(new URI(TestPath2));
		files[2].setFileURI(new URI(TestPath3));
		files[3].setFileURI(new URI(TestPath4));
		files[4].setFileURI(new URI(TestPath5));
	}
	
	@Test (groups = "unit-tests")
	public void TestGetFileName() {
		String fileName = files[0].getFileURI().getPath();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File1);
		
		fileName = files[1].getFileURI().getPath();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File2);
		
		fileName = files[2].getFileURI().getPath();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File3);
		
		fileName = files[3].getFileURI().getPath();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File4);
		
		fileName = files[4].getFileURI().getPath();
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		Assert.assertNotNull(fileName);
		Assert.assertEquals(fileName, File5);
	}
  

}
