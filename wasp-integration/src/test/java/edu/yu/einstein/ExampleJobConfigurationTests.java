package edu.yu.einstein;

import org.testng.Assert;
import org.testng.annotations.Test;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;


@ContextConfiguration(locations={"classpath:launch-context.xml"})
public class ExampleJobConfigurationTests extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;
	
	@Test (groups = "unit-tests")
	public void testSimpleProperties() throws Exception {
		Assert.assertNotNull(jobLauncher);
	}
	
	@Test (groups = "unit-tests", dependsOnMethods = "testSimpleProperties")
	public void testLaunchJob() throws Exception {
		jobLauncher.run(job, new JobParameters());
	}
	
}
