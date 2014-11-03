/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugins.star;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

// The test context is created using the configuration files provided in the @ContextConfiguration locations list
@ContextConfiguration(locations={"/star-test-launch-context.xml"})

/**
 * Test Skeleton
 * @author 
 * 
 */
public class StarTestSkeleton extends AbstractTestNGSpringContextTests{
	
	private final Logger logger = LoggerFactory.getLogger(StarTestSkeleton.class);
	
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
	// Test (groups = "star-tests")
	public void testStarTestSkeleton() throws Exception{
		try{
			// TODO: Test logic here. 
			Assert.assertTrue(true); // pass  default
		} catch (Exception e){
			logger.error("Caught unexpected exception: " + e.getLocalizedMessage());
			throw e; // re-throw the exception
		}
	}
	
}
