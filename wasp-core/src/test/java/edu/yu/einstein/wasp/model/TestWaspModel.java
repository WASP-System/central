package edu.yu.einstein.wasp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.exception.ModelCopyException;

public class TestWaspModel {
	
	public Sample sample;
	
	@Test (groups = "unit-tests")
	public void testDeepCopy() {
		Sample sampleCopy = null;
		try{
			sampleCopy = Sample.getDeepCopy(sample);
		} catch(ModelCopyException e){
			Assert.fail("Unexpected exception caught: " + e.getMessage());
		}
		
		Assert.assertNotNull(sampleCopy.getSampleId()); 
		Assert.assertEquals(sampleCopy.getSampleId().intValue(), 1);
		Assert.assertEquals(sampleCopy.getName(), "s1");
		
		Assert.assertNotNull(sampleCopy.getLab());
		Assert.assertNotSame(sampleCopy.getLab(), sample.getLab());
		Assert.assertNotNull(sampleCopy.getLab().getLabId()); 
		Assert.assertEquals(sampleCopy.getLab().getLabId().intValue(), 10); 
		Assert.assertEquals(sampleCopy.getLab().getName(), "LabName");
		
		Assert.assertNotNull(sampleCopy.getSampleMeta());
		Assert.assertNotSame(sampleCopy.getSampleMeta(), sample.getSampleMeta());
		Assert.assertEquals(sampleCopy.getSampleMeta().size(), 2);
		Assert.assertNotNull(sampleCopy.getSampleMeta().get(1).getSampleMetaId());
		Assert.assertEquals(sampleCopy.getSampleMeta().get(1).getSampleMetaId().intValue(), 21);
		Assert.assertNotNull(sampleCopy.getSampleMeta().get(1).getSampleId()); 
		Assert.assertEquals(sampleCopy.getSampleMeta().get(1).getSampleId().intValue(), 1);
		Assert.assertEquals(sampleCopy.getSampleMeta().get(0).getK(), "meta1Key");
		Assert.assertEquals(sampleCopy.getSampleMeta().get(1).getV(), "meta2Value");
		Assert.assertEquals(sampleCopy.getReceiveDts(), sample.getReceiveDts());
		
		// test recursion
		Assert.assertNotNull(sampleCopy.getLab().getUser().getUserId());
		Assert.assertNotSame(sampleCopy.getLab().getUser(), sample.getLab().getUser());
		Assert.assertEquals(sampleCopy.getLab().getUser().getUserId().intValue(), 15);
		Assert.assertEquals(sampleCopy.getLab().getUser().getFirstName(), "Andy");
	}
	
	@Test (groups = "unit-tests")
	public void testShallowCopy() {
		Sample sampleCopy = Sample.getShallowCopy(sample);
		
		Assert.assertNotNull(sampleCopy.getSampleId()); 
		Assert.assertEquals(sampleCopy.getSampleId().intValue(), 1);
		Assert.assertEquals(sampleCopy.getName(), "s1");
		
		Assert.assertNotNull(sampleCopy.getLab());
		Assert.assertSame(sampleCopy.getLab(), sample.getLab());
		Assert.assertNotNull(sampleCopy.getLab().getLabId()); 
		Assert.assertEquals(sampleCopy.getLab().getLabId().intValue(), 10); 
		Assert.assertEquals(sampleCopy.getLab().getName(), "LabName");
		
		Assert.assertNotNull(sampleCopy.getSampleMeta());
		Assert.assertSame(sampleCopy.getSampleMeta(), sample.getSampleMeta());
		Assert.assertEquals(sampleCopy.getSampleMeta().size(), 2);
		Assert.assertNotNull(sampleCopy.getSampleMeta().get(1).getSampleMetaId());
		Assert.assertEquals(sampleCopy.getSampleMeta().get(1).getSampleMetaId().intValue(), 21);
		Assert.assertNotNull(sampleCopy.getSampleMeta().get(1).getSampleId()); 
		Assert.assertEquals(sampleCopy.getSampleMeta().get(1).getSampleId().intValue(), 1);
		Assert.assertEquals(sampleCopy.getSampleMeta().get(0).getK(), "meta1Key");
		Assert.assertEquals(sampleCopy.getSampleMeta().get(1).getV(), "meta2Value");
		Assert.assertEquals(sampleCopy.getReceiveDts(), sample.getReceiveDts());
		
		// test recursion
		Assert.assertNotNull(sampleCopy.getLab().getUser().getUserId());
		Assert.assertSame(sampleCopy.getLab().getUser(), sample.getLab().getUser());
		Assert.assertEquals(sampleCopy.getLab().getUser().getUserId().intValue(), 15);
		Assert.assertEquals(sampleCopy.getLab().getUser().getFirstName(), "Andy");
	}

	@BeforeMethod
	public void beforeTest() {
		sample = new Sample();
		sample.setSampleId(1);
		Lab lab = new Lab();
		lab.setLabId(10);
		lab.setName("LabName");
		User user = new User();
		user.setUserId(15);
		user.setFirstName("Andy");
		lab.setUser(user);
		sample.setLab(lab);
		sample.setName("s1");
		List<SampleMeta> sampleMeta = new ArrayList<SampleMeta>();
		SampleMeta sm1 = new SampleMeta();
		sm1.setSampleMetaId(20);
		sm1.setSampleId(1);
		sm1.setK("meta1Key");
		sm1.setV("meta1Value");
		sampleMeta.add(sm1);
		SampleMeta sm2 = new SampleMeta();
		sm2.setSampleMetaId(21);
		sm2.setSampleId(1);
		sm2.setK("meta2Key");
		sm2.setV("meta2Value");
		sampleMeta.add(sm2);
		sample.setSampleMeta(sampleMeta);
		sample.setReceiveDts(new Date(java.lang.System.currentTimeMillis()));
	}

}
