package edu.yu.einstein.wasp.util;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StringHelperTest {
	
	@BeforeClass
	public void setUp() throws Exception {
	
	}

	@AfterClass
	public void tearDown() throws Exception {
	}

	@Test (groups = "unit-tests")
	public void testGetLoginFromFormattedNameAndLogin() {
		String expected = "Test";
		Assert.assertEquals(StringHelper.getLoginFromFormattedNameAndLogin("This is a (Test)"), expected);
	}
	
	@Test (groups = "unit-tests")
	public void testToCamelCase() {
		String expected = new String("fooBarBazBob");
		Assert.assertEquals(StringHelper.toCamelCase("Foo bar Baz bob"),expected);
	}
	
	@Test (groups = "unit-tests")
	public void testDeCamelCase() {
		String expected = new String("Foo bar baz bob");
		Assert.assertEquals(StringHelper.deCamelCase("fooBarBazBob"), expected);
	}
}
