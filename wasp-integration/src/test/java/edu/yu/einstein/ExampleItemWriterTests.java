package edu.yu.einstein;

import edu.yu.einstein.ExampleItemWriter;


import org.testng.annotations.Test;

public class ExampleItemWriterTests{

	private ExampleItemWriter writer = new ExampleItemWriter();
	
	@Test (groups = "unit-tests")
	public void testWrite() throws Exception {
		writer.write(null); // nothing bad happens
	}

}
