package edu.yu.einstein.wasp.util;


import java.io.UnsupportedEncodingException;

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
	
	@Test (groups = "unit-tests")
	public void testByteArrayToUTF8() {
		String expected = new String("Foo bar baz b®b");
		try {
			byte[] utf8bytes = expected.getBytes("UTF8");
			byte[] trunc = new byte[8];
			for (int i = utf8bytes.length-1; i >= utf8bytes.length - trunc.length; i--){
				int j = i - (utf8bytes.length - trunc.length);
				System.out.println(i + "," + j);
				trunc[j] = utf8bytes[i];
			}
			String truncStr = new String(trunc, "UTF8");
			System.out.println(truncStr);
			Assert.assertEquals("baz b®b", truncStr);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
