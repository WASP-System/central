package edu.yu.einstein.wasp.util;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringHelperTest {
	

	StringHelper stringHelper;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		stringHelper = new StringHelper(); 
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		String expected = new String("Test");
		assertEquals("",expected, stringHelper.toCapFirstLetter("test"));
		//fail("Not yet implemented");
	}
}
