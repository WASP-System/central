package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StatusMessageService;
import edu.yu.einstein.wasp.test.stubs.SampleMetaDaoStub;

public class TestStatusMessageService {

	private SampleMetaDaoStub sampleMetaDaoStub = new SampleMetaDaoStub(); 
	
	private StatusMessageService statusMessageService = new StatusMessageServiceImpl();
	
	private final Integer SAMPLE_META_ID = 1;
	private final Integer SAMPLE_ID1 = 1;
	private final Integer SAMPLE_ID2 = 2;
	private final String KEY_1 = "reject reason";
	private final String VALUE_1A = "Not Set";
	private final String VALUE_1B = "Failed QC";
	private final String KEY_2 = "received";
	private final String VALUE_2 = "yes";
	private final String KEY_3 = "analyzing";
	private final String VALUE_3 = "no";
	private final String STATUS_KEY_PREFIX = "statusMessage."; 
	
	
	@BeforeClass
	public void init(){
		sampleMetaDaoStub.stubSampleMetaList = new ArrayList<SampleMeta>(); // init list
		
		// meta for SAMPLE_ID1 (two status messages, one other)
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_KEY_PREFIX + KEY_1);
		sampleMeta1.setV(VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);
		
		SampleMeta sampleMeta2 = new SampleMeta();
		sampleMeta2.setSampleMetaId(SAMPLE_META_ID + 1);
		sampleMeta2.setSampleId(SAMPLE_ID1);
		sampleMeta2.setK(STATUS_KEY_PREFIX + KEY_2);
		sampleMeta2.setV(VALUE_2);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta2);
		
		SampleMeta sampleMeta3 = new SampleMeta();
		sampleMeta3.setSampleMetaId(SAMPLE_META_ID + 2);
		sampleMeta3.setSampleId(SAMPLE_ID1);
		sampleMeta3.setK("sampleMeta.misc");
		sampleMeta3.setV("something");
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta3);
		
		// meta for SAMPLE_ID2 (1 status message one other)
		SampleMeta sampleMeta4 = new SampleMeta();
		sampleMeta4.setSampleMetaId(SAMPLE_META_ID + 3);
		sampleMeta4.setSampleId(SAMPLE_ID2);
		sampleMeta4.setK("sampleMeta.misc");
		sampleMeta4.setV("something");
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta4);
		
		SampleMeta sampleMeta5 = new SampleMeta();
		sampleMeta5.setSampleMetaId(SAMPLE_META_ID + 4);
		sampleMeta5.setSampleId(SAMPLE_ID2);
		sampleMeta5.setK(STATUS_KEY_PREFIX + "foo");
		sampleMeta5.setV("bar");
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta5);
	}
	
	
	
	@Test(groups = "unit-tests")
	public void testSaveMessageKeyAlreadyExists(){
		SampleMeta meta = null;
		try {
			meta = statusMessageService.save(KEY_1, VALUE_1B, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(meta.getSampleMetaId(), SAMPLE_META_ID);
		Assert.assertEquals(meta.getK(), KEY_1);
		Assert.assertEquals(meta.getV(), VALUE_1B);
		Assert.assertEquals(meta.getSampleId(), SAMPLE_ID1);	
	}
	
	@Test(groups = "unit-tests")
	public void testSaveMessageKeyNew(){
		SampleMeta meta = null;
		try {
			meta = statusMessageService.save(KEY_3, VALUE_3, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertNull(meta.getSampleMetaId());
		Assert.assertEquals(meta.getK(), KEY_3);
		Assert.assertEquals(meta.getV(), VALUE_3);
		Assert.assertEquals(meta.getSampleId(), SAMPLE_ID1);
	}
	
	@Test(groups = "unit-tests")
	public void testReadAll(){
		Map<String, String> messages = statusMessageService.readAll(SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		Assert.assertEquals(messages.size(), 2);
		Assert.assertTrue(messages.containsKey(KEY_1));
		Assert.assertEquals(messages.get(KEY_1), VALUE_1A);
		Assert.assertTrue(messages.containsKey(KEY_2));
		Assert.assertEquals(messages.get(KEY_2), VALUE_2);
	}
	
	@Test(groups = "unit-tests")
	public void testReadExisting(){
		String message = statusMessageService.read(KEY_1, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		Assert.assertNotNull(message);
		Assert.assertEquals(KEY_1, VALUE_1A);
	}
	
}
