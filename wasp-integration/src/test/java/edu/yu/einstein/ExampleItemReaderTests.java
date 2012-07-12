package edu.yu.einstein; 

import org.testng.Assert;
import org.testng.annotations.Test;

public class ExampleItemReaderTests{

	private ExampleItemReader reader = new ExampleItemReader();
	
	@Test (groups = "unit-tests")
	public void testReadOnce() throws Exception {
		Assert.assertEquals("Hello world!", reader.read());
	}

	@Test (groups = "unit-tests")
	public void testReadTwice() throws Exception {
		reader.read();
		Assert.assertEquals(null, reader.read());
	}

}
