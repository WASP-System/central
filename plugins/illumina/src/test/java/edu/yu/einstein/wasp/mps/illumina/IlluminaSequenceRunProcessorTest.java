package edu.yu.einstein.wasp.mps.illumina;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

@ContextConfiguration(locations={"classpath:illumina-test-application-context.xml"})
public class IlluminaSequenceRunProcessorTest extends AbstractTestNGSpringContextTests {

	@Autowired 
	SampleDao sampleDao;
	
	@Autowired
	SampleSourceDao sampleSourceDao;
	
	@Autowired
	SampleService sampleService;
	
	@BeforeMethod
	public void setUp() {
		
		

	}

	@AfterMethod
	public void tearDown() {

	}

	@Test
	public void f() {
		Sample platformUnit = sampleDao.getById(11);
		
		System.out.println(platformUnit.getSampleId());
		
		//System.out.println(sampleSourceDao.getParentSampleByDerivedSampleId(platformUnit.getSampleId()).getSampleId());
		
		
//		for (Sample c : sampleSourceDao.getParentSampleByDerivedSampleId(platformUnit.getSampleId())) {
//			System.out.println(c.getName() + " " + c.getSampleId() + " " + c.getSampleSubtype().getIName());
//		}

	}
}
