/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

// The test context is created using the configuration files provided in the @ContextConfiguration locations list
@ContextConfiguration(locations={"/helptag-test-launch-context.xml"})

/**
 * Test Skeleton
 * @author 
 * 
 */
public class HelptagTestSkeleton extends AbstractTestNGSpringContextTests{
	
	private final Logger logger = LoggerFactory.getLogger(HelptagTestSkeleton.class);
	
	/**
	 * Code to execute before running any tests
	 */
	@BeforeClass 
	private void setup(){

	}
	
	/**
	 * Code to execute after running all tests
	 */
	@AfterClass 
	public void teardown(){
		
	}

		
	/**
	 * Simple Test skeleton
	 * @throws Exception
	 */
	@Test (groups = "helptag-tests")
	public void testHelptagTestSkeleton() throws Exception{
		try{
			// TODO: Test logic here. 
			Assert.assertTrue(true); // pass  default
		} catch (Exception e){
			logger.error("Caught unexpected exception: " + e.getLocalizedMessage());
			throw e; // re-throw the exception
		}
	}
	
}
