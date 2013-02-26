package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.exception.StatusMetaMessagingException;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.MetaMessageService;

public class TestMetaMessageService {
	
	protected static final Logger logger = LoggerFactory.getLogger(TestMetaMessageService.class);

	@Mock private SampleMetaDao mockSampleMetaDao; 
	
	private MetaMessageService metaMessageService = new MetaMessageServiceImpl();
	
	private final Integer SAMPLE_META_ID = 1;
	private final Integer SAMPLE_ID1 = 1;
	private final String GROUP_1 = "fmComments";
	private final String NAME_1 = "reject reason";
	private final String VALUE_1A = "Not Set";
	private final String VALUE_1B = "Failed QC";
	
	private final String GROUP_2 = "received";
	private final String VALUE_2 = "yes";
	
	private final String STATUS_GROUP_PREFIX = "statusMessage."; 
	private List<SampleMeta> sampleMetaList;
	
	@BeforeTest
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@BeforeMethod
	public void init(){
		sampleMetaList = new ArrayList<SampleMeta>(); // init list
	}
	
	
	
	@Test(groups = "unit-tests")
	public void testSaveMessageKeyAlreadyExists(){
		SampleMeta sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta1.setSampleId(SAMPLE_ID1);
		sampleMeta1.setK(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00");
		sampleMeta1.setV(VALUE_1A);
		sampleMeta1.setLastUpdTs(new Date());
		sampleMetaList.add(sampleMeta1);
		
		// return the argument from mocked method
		PowerMockito.when(mockSampleMetaDao.save(Mockito.any(SampleMeta.class))).thenAnswer(new Answer<SampleMeta>() {
		    @Override
		    public SampleMeta answer(InvocationOnMock invocation) throws Throwable {
		    	return (SampleMeta) invocation.getArguments()[0];
		    }
		  });

		MetaMessage metaMessage = null;
		
		try {
			metaMessage = metaMessageService.saveToGroup(GROUP_1, NAME_1, VALUE_1B, SAMPLE_ID1, SampleMeta.class, mockSampleMetaDao);
		} catch (StatusMetaMessagingException e) {
			Assert.fail(e.getMessage());
		}
		logger.debug(metaMessage.toString());
		Assert.assertEquals(metaMessage.getGroup(), GROUP_1);
		Assert.assertEquals(metaMessage.getName(), NAME_1);
		Assert.assertEquals(metaMessage.getValue(), VALUE_1B);
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
		sampleMetaList.add(sampleMeta1);
		SampleMeta sampleMeta2 = new SampleMeta();
		sampleMeta2.setSampleMetaId(SAMPLE_META_ID + 1);
		sampleMeta2.setSampleId(SAMPLE_ID1);
		sampleMeta2.setK(STATUS_GROUP_PREFIX + GROUP_2 + "::54947df8-0e9e-4471-a2f9-9af509fb5889");
		sampleMeta2.setV(VALUE_2);
		sampleMetaList.add(sampleMeta2);
		
		SampleMeta sampleMeta3 = new SampleMeta();
		sampleMeta3.setSampleMetaId(SAMPLE_META_ID + 2);
		sampleMeta3.setSampleId(SAMPLE_ID1);
		sampleMeta3.setK("sampleMeta.misc");
		sampleMeta3.setV("something");
		sampleMetaList.add(sampleMeta3);
		
		PowerMockito.when(mockSampleMetaDao.findByMapOrderBy(Mockito.anyMap(), Mockito.anyListOf(String.class), Mockito.anyString())).thenReturn(sampleMetaList);
		
		List<MetaMessage> messages = metaMessageService.readAll(SAMPLE_ID1, SampleMeta.class, mockSampleMetaDao);
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
		sampleMetaList.add(sampleMeta1);
		
		SampleMeta sampleMeta2 = new SampleMeta();
		sampleMeta2.setSampleMetaId(SAMPLE_META_ID);
		sampleMeta2.setSampleId(SAMPLE_ID1);
		sampleMeta2.setK(STATUS_GROUP_PREFIX + GROUP_1 + "::54947df8-0e9e-4471-a2f9-9af509fb5889");
		sampleMeta2.setV(NAME_1 + "::" + VALUE_1B);
		sampleMetaList.add(sampleMeta2);
		
		SampleMeta sampleMeta3 = new SampleMeta();
		sampleMeta3.setSampleMetaId(SAMPLE_META_ID + 1);
		sampleMeta3.setSampleId(SAMPLE_ID1);
		sampleMeta3.setK(STATUS_GROUP_PREFIX + GROUP_2 + "::c6ac52a5-747b-452d-a1a1-48a6b9e854ed");
		sampleMeta3.setV(VALUE_2);
		sampleMetaList.add(sampleMeta3);
		PowerMockito.when(mockSampleMetaDao.findByMapOrderBy(Mockito.anyMap(), Mockito.anyListOf(String.class), Mockito.anyString())).thenReturn(sampleMetaList);
		List<MetaMessage> messages = metaMessageService.read(GROUP_1, SAMPLE_ID1, SampleMeta.class, mockSampleMetaDao);
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
		sampleMetaList.add(sampleMeta1);
		PowerMockito.when(mockSampleMetaDao.findByMap(Mockito.anyMap())).thenReturn(sampleMetaList);
		MetaMessage message = new MetaMessage(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00", GROUP_1, NAME_1, VALUE_1A);
		MetaMessage editedMessage = null;
		try {
			editedMessage = metaMessageService.edit(message, VALUE_1B, SAMPLE_ID1, SampleMeta.class, mockSampleMetaDao);
		} catch (StatusMetaMessagingException e) {
			Assert.fail("caught unexpected Exception", e);
		}
		Assert.assertEquals(editedMessage.getValue(), VALUE_1B);
	}
	
	@Test
	public void testEditFail(){
		MetaMessage message = new MetaMessage(STATUS_GROUP_PREFIX + GROUP_1 + "::067e6162-3b6f-4ae2-a171-2470b63dff00", GROUP_1, NAME_1, VALUE_1A);
		MetaMessage editedMessage = null;
		String exception = "";
		PowerMockito.when(mockSampleMetaDao.findByMap(Mockito.anyMap())).thenReturn(sampleMetaList);
		try {
			editedMessage = metaMessageService.edit(message, VALUE_1B, SAMPLE_ID1, SampleMeta.class, mockSampleMetaDao);
		} catch (StatusMetaMessagingException e) {
			logger.debug("Caught expected WaspException: " + e.getMessage());
			exception = e.getMessage();
		}
		Assert.assertEquals(exception, "edu.yu.einstein.wasp.exception.WaspException: Unable to retrieve MetaMessage");
	}
	
}
