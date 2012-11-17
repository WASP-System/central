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
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.test.stubs.SampleMetaDaoStub;

public class TestMetaMessageService {
	
	protected static final Logger logger = LoggerFactory.getLogger(TestMetaMessageService.class);

	private SampleMetaDaoStub sampleMetaDaoStub = new SampleMetaDaoStub(); 
	
	private MetaMessageService metaMessageService = new MetaMessageServiceImpl();
	
	private final Integer SAMPLE_META_ID = 1;
	private final Integer SAMPLE_ID1 = 1;
	private final String GROUP_1 = "fmComments";
	private final String NAME_1 = "reject reason";
	private final String VALUE_1A = "Not Set";
	private final String VALUE_1B = "Failed QC";
	
	private final String GROUP_2 = "received";
	private final String VALUE_2 = "yes";
	
	private final String GROUP_3 = "analyzing";
	private final String VALUE_3 = "no";
	private final String STATUS_GROUP_PREFIX = "statusMessage."; 
	
	
	@BeforeMethod
	public void init(){
		sampleMetaDaoStub.stubSampleMetaList = new ArrayList<SampleMeta>(); // init list
	}
	
	
	
	@Test(groups = "unit-tests")
	public void testSaveMessageKeyAlreadyExists(){
		
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00");
		sampleMeta1.setV(VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);

		MetaMessage metaMessage = null;
		
		try {
			metaMessage = metaMessageService.saveToGroup(GROUP_1, NAME_1, VALUE_1B, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		logger.debug(metaMessage.toString());
		Assert.assertEquals(metaMessage.getGroup(), GROUP_1);
		Assert.assertEquals(metaMessage.getName(), NAME_1);
		Assert.assertEquals(metaMessage.getValue(), VALUE_1B);
	}
	
	/**
	 * 
	 */
	@Test(groups = "unit-tests")
	public void testSaveMessageKeyNew(){
		MetaMessage metaMessage = null;
		try {
			metaMessage = metaMessageService.saveToGroup(GROUP_3, VALUE_3, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		logger.debug(metaMessage.toString());
		SampleMeta meta = sampleMetaDaoStub.stubSampleMetaList.get(0);
		Assert.assertNull(meta.getSampleMetaId());
		Assert.assertEquals(meta.getV(), VALUE_3);
		Assert.assertEquals(meta.getSampleId(), SAMPLE_ID1);
	}
	
	/**
	 * Expect to retrieve all messages in all groups but NOT other meta
	 */
	@Test(groups = "unit-tests")
	public void testReadAll(){

		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00");
		sampleMeta1.setV(VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);
		
		SampleMeta sampleMeta2 = new SampleMeta();
		sampleMeta2.setSampleMetaId(SAMPLE_META_ID + 1);
		sampleMeta2.setSampleId(SAMPLE_ID1);
		sampleMeta2.setK(STATUS_GROUP_PREFIX + GROUP_2 + "::54947df8-0e9e-4471-a2f9-9af509fb5889");
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
	}
	
	/**
	 * Expect only to retrieve messages in GROUP_1
	 */
	@Test(groups = "unit-tests")
	public void testReadGroup(){
		
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00");
		sampleMeta1.setV(NAME_1 + "::" + VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);
		
		SampleMeta sampleMeta2 = new SampleMeta();
		sampleMeta2.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta2.setSampleId(SAMPLE_ID1);
		sampleMeta2.setK(STATUS_GROUP_PREFIX + GROUP_1 + "::54947df8-0e9e-4471-a2f9-9af509fb5889");
		sampleMeta2.setV(NAME_1 + "::" + VALUE_1B);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta2);
		
		SampleMeta sampleMeta3 = new SampleMeta();
		sampleMeta3.setSampleMetaId(SAMPLE_META_ID + 1);
		sampleMeta3.setSampleId(SAMPLE_ID1);
		sampleMeta3.setK(STATUS_GROUP_PREFIX + GROUP_2 + "::c6ac52a5-747b-452d-a1a1-48a6b9e854ed");
		sampleMeta3.setV(VALUE_2);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta3);
		
		List<MetaMessage> messages = metaMessageService.read(GROUP_1, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		Assert.assertEquals(messages.size(), 2);
		Assert.assertEquals(messages.get(0).getName(), NAME_1);
		Assert.assertEquals(messages.get(0).getValue(), VALUE_1A);
		Assert.assertEquals(messages.get(1).getName(), NAME_1);
		Assert.assertEquals(messages.get(1).getValue(), VALUE_1B);
	}
	
	@Test
	public void testEdit(){
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00");
		sampleMeta1.setV(NAME_1 + "::" + VALUE_1A);
		sampleMetaDaoStub.stubSampleMetaList.add(sampleMeta1);
		MetaMessage message = new MetaMessage(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00", GROUP_1, NAME_1, VALUE_1A);
		MetaMessage editedMessage = null;
		try {
			editedMessage = metaMessageService.edit(message, VALUE_1B, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (WaspException e) {
			Assert.fail("caught unexpected Exception", e);
		}
		Assert.assertEquals(editedMessage.getValue(), VALUE_1B);
	}
	
	@Test
	public void testEditFail(){
		MetaMessage message = new MetaMessage(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00", GROUP_1, NAME_1, VALUE_1A);
		MetaMessage editedMessage = null;
		String exception = "";
		try {
			editedMessage = metaMessageService.edit(message, VALUE_1B, SAMPLE_ID1, SampleMeta.class, sampleMetaDaoStub);
		} catch (WaspException e) {
			logger.debug("Caught expected WaspException: " + e.getMessage());
			exception = e.getMessage();
		}
		Assert.assertEquals(exception, "Unable to retrieve MetaMessage");
	}
	
}
