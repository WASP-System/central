package edu.yu.einstein.wasp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.BeforeMethod;

public class TestWaspModel {
	
	public Sample sample;

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
