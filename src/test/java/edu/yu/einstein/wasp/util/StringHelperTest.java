package edu.yu.einstein.wasp.util;


import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.*;

public class StringHelperTest {
	
	@BeforeClass
	public void setUp() throws Exception {
	
	}

	@AfterClass
	public void tearDown() throws Exception {
	}

	@Test (groups = "unit-tests")
	public void testGetLoginFromFormattedNameAndLogin() {
		String expected = new String("Test");
		Assert.assertEquals(expected, StringHelper.getLoginFromFormattedNameAndLogin("This is a (Test)"),"Failed in StringHelper.getLoginFromFormattedNameAndLogin()");
		//fail("Not yet implemented");
	}
}
