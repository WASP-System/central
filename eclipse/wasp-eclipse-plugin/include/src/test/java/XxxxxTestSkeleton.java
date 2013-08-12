/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package ___package___;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

// The test context is created using the configuration files provided in the @ContextConfiguration locations list
@ContextConfiguration(locations={"/___pluginname___-test-launch-context.xml"})

/**
 * Test Skeleton
 * @author 
 * 
 */
public class ___Pluginname___TestSkeleton extends AbstractTestNGSpringContextTests{
	
	private final Logger logger = LoggerFactory.getLogger(___Pluginname___TestSkeleton.class);
	
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
	@Test (groups = "___pluginname___-tests")
	public void test___Pluginname___TestSkeleton() throws Exception{
		try{
			// TODO: Test logic here. 
			Assert.assertTrue(true); // pass  default
		} catch (Exception e){
			 // caught an unexpected exception
			Assert.fail("testSuccessfulJobLaunch(): Caught Exception: "+e.getMessage());
		}
	}
	
}
