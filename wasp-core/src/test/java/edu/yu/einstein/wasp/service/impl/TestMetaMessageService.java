package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.test.stubs.SampleMetaDaoStub;

public class TestMetaMessageService {
	
	protected static final Logger logger = LoggerFactory.getLogger(TestMetaMessageService.class);

	private SampleMetaDaoStub sampleMetaDaoStub = new SampleMetaDaoStub(); 
	
	private MetaMessageService metaMessageService = new MetaMessageServiceImpl();
	
	private final Integer SAMPLE_META_ID = 1;
	private final Integer SAMPLE_ID1 = 1;
	private final String KEY_1 = "fmComments";
	private final String NAME_1 = "reject reason";
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
		sampleMeta1.setK(STATUS_KEY_PREFIX + KEY_1 + "::0");
		sampleMeta1.setV(VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);

		MetaMessage metaMessage = null;
		
		try {
			metaMessage = metaMessageService.saveWithKey(KEY_1, NAME_1, VALUE_1B, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(metaMessage.getKey(), KEY_1);
		Assert.assertEquals(metaMessage.getName(), NAME_1);
		Assert.assertEquals(metaMessage.getValue(), VALUE_1B);
		Assert.assertEquals(sampleMetaDaoStub.stubSampleMetaList.get(1).getK(), STATUS_KEY_PREFIX + KEY_1 + "::1");
	}
	
	@Test(groups = "unit-tests")
	public void testSaveMessageKeyNew(){
		MetaMessage metaMessage = null;
		try {
			metaMessage = metaMessageService.saveWithKey(KEY_3, VALUE_3, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		SampleMeta meta = sampleMetaDaoStub.stubSampleMetaList.get(0);
		Assert.assertNull(meta.getSampleMetaId());
		Assert.assertEquals(meta.getK(), STATUS_KEY_PREFIX + KEY_3 + "::0");
		Assert.assertEquals(meta.getV(), VALUE_3);
		Assert.assertEquals(meta.getSampleId(), SAMPLE_ID1);
	}
	
	@Test(groups = "unit-tests")
	public void testReadAll(){

		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_KEY_PREFIX + KEY_1 + "::0");
		sampleMeta1.setV(VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);
		
		SampleMeta sampleMeta2 = new SampleMeta();
		sampleMeta2.setSampleMetaId(SAMPLE_META_ID + 1);
		sampleMeta2.setSampleId(SAMPLE_ID1);
		sampleMeta2.setK(STATUS_KEY_PREFIX + KEY_2 + "::1");
		sampleMeta2.setV(VALUE_2);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta2);
		
		SampleMeta sampleMeta3 = new SampleMeta();
		sampleMeta3.setSampleMetaId(SAMPLE_META_ID + 2);
		sampleMeta3.setSampleId(SAMPLE_ID1);
		sampleMeta3.setK("sampleMeta.misc");
		sampleMeta3.setV("something");
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta3);
		
		List<MetaMessage> messages = metaMessageService.readAll(SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		Assert.assertEquals(messages.size(), 2);
		Assert.assertEquals(messages.get(0).getKey(), KEY_1);
		Assert.assertEquals(messages.get(1).getKey(), KEY_2);
	}
	
	@Test(groups = "unit-tests")
	public void testReadExisting(){
		
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_KEY_PREFIX + KEY_1 + "::0");
		sampleMeta1.setV(NAME_1 + "::" + VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);
		
		SampleMeta sampleMeta2 = new SampleMeta();
		sampleMeta2.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta2.setSampleId(SAMPLE_ID1);
		sampleMeta2.setK(STATUS_KEY_PREFIX + KEY_1 + "::1");
		sampleMeta2.setV(NAME_1 + "::" + VALUE_1B);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta2);
		
		List<MetaMessage> messages = metaMessageService.read(KEY_1, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		Assert.assertEquals(messages.size(), 2);
		Assert.assertEquals(messages.get(0).getName(), NAME_1);
		Assert.assertEquals(messages.get(0).getValue(), VALUE_1A);
		Assert.assertEquals(messages.get(1).getName(), NAME_1);
		Assert.assertEquals(messages.get(1).getValue(), VALUE_1B);
	}
	
}
