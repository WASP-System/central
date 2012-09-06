package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.StatusMessageService;
import edu.yu.einstein.wasp.test.stubs.SampleMetaDaoStub;

public class TestStatusMessageService {
	
	protected static final Logger logger = Logger.getLogger(TestStatusMessageService.class);

	private SampleMetaDaoStub sampleMetaDaoStub = new SampleMetaDaoStub(); 
	
	private StatusMessageService statusMessageService = new StatusMessageServiceImpl();
	
	private final Integer SAMPLE_META_ID = 1;
	private final Integer SAMPLE_ID1 = 1;
	private final String KEY_1 = "reject reason";
	private final String VALUE_1A = "Not Set";
	private final String VALUE_1B = "Failed QC";
	private final String KEY_2 = "received";
	private final String VALUE_2 = "yes";
	private final String KEY_3 = "analyzing";
	private final String VALUE_3 = "no";
	private final String STATUS_KEY_PREFIX = "statusMessage."; 
	
	
	@BeforeMethod
	public void init(){
		sampleMetaDaoStub.stubSampleMetaList = new ArrayList<SampleMeta>(); // init list
	}
	
	
	
	@Test(groups = "unit-tests")
	public void testSaveMessageKeyAlreadyExists(){
		
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_KEY_PREFIX + KEY_1);
		sampleMeta1.setV(VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);

		SampleMeta meta = null;
		
		try {
			meta = statusMessageService.save(KEY_1, VALUE_1B, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(meta.getSampleMetaId(), SAMPLE_META_ID);
		Assert.assertEquals(meta.getK(), STATUS_KEY_PREFIX + KEY_1);
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
		
		Map<String, String> messages = statusMessageService.readAll(SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		Assert.assertEquals(messages.size(), 2);
		Assert.assertTrue(messages.containsKey(KEY_1));
		Assert.assertEquals(messages.get(KEY_1), VALUE_1A);
		Assert.assertTrue(messages.containsKey(KEY_2));
		Assert.assertEquals(messages.get(KEY_2), VALUE_2);
	}
	
	@Test(groups = "unit-tests")
	public void testReadExisting(){
		
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_KEY_PREFIX + KEY_1);
		sampleMeta1.setV(VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);
		
		String message = statusMessageService.read(KEY_1, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		Assert.assertNotNull(message);
		Assert.assertEquals(message, VALUE_1A);
	}
	
}
