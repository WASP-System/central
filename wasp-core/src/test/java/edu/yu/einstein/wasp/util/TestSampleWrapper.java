package edu.yu.einstein.wasp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.isA;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.impl.SampleMetaDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleSourceDaoImpl;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl;
import edu.yu.einstein.wasp.util.SampleWrapper;

public class TestSampleWrapper {

	private SampleSourceDao mockSampleSourceDao;
	private Sample sample;
	private Sample parentSample;
	private Sample grandparentSample;
	private SampleMeta sampleMeta1;
	private SampleMeta sampleMeta2;
	private SampleMeta sampleMeta3;
	private SampleMeta sampleMeta4;
	private static final Logger logger = Logger.getLogger(TestSampleWrapper.class);

	@BeforeTest
	public void beforeTest() {
        
        // configure mockSampleSourceDao
		// mockSampleSourceDao = createMock(SampleSourceDaoImpl.class);
		mockSampleSourceDao = createMockBuilder(SampleSourceDaoImpl.class).addMockedMethods(SampleSourceDaoImpl.class.getMethods()).createMock();
		Assert.assertNotNull(mockSampleSourceDao);
		
		
		
	}
	
	@BeforeMethod
	public void beforeMethod() {
		// set up samples
		sample = new Sample();
		sample.setSampleId(1);
		grandparentSample = new Sample(); // our parent object has no parent so empty Sample object should be returned
		parentSample = new Sample();
		parentSample.setSampleId(2);
		
		List<SampleMeta> sampleMetaList1 = new ArrayList<SampleMeta>();
		sampleMeta1 = new SampleMeta();
		sampleMeta1.setSampleMetaId(1);
		sampleMeta1.setSampleId(1);
		sampleMeta1.setK("sampleMeta1.key1");
		sampleMeta1.setV("1");
		sampleMetaList1.add(sampleMeta1);

		sampleMeta2 = new SampleMeta();
		sampleMeta2.setK("sampleMeta1.key2");
		sampleMeta2.setV("2");
		sampleMetaList1.add(sampleMeta2);
		sample.setSampleMeta(sampleMetaList1);

		List<SampleMeta> sampleMetaList2 = new ArrayList<SampleMeta>();
		sampleMeta3 = new SampleMeta();
		sampleMeta3.setSampleMetaId(3);
		sampleMeta3.setSampleId(2);
		sampleMeta3.setK("parentSampleMeta2.key3");
		sampleMeta3.setV("3");
		sampleMetaList2.add(sampleMeta3);

		sampleMeta4 = new SampleMeta();
		sampleMeta4.setK("parentSampleMeta2.key4");
		sampleMeta4.setV("4");
		sampleMetaList2.add(sampleMeta4);
		parentSample.setSampleMeta(sampleMetaList2);
		
	}
	
	@AfterMethod
	public void afterMethod() {
		// after each test method remove the expectations from the mock
		EasyMock.reset(mockSampleSourceDao);
	}


	@Test (groups = "unit-tests")
	public void testSampleWrapperConstruct() {
		// set up mockSampleSourceDao
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(parentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(2)).andReturn(grandparentSample);
		replay(mockSampleSourceDao);
		
		SampleWrapper managedSample = new SampleWrapper(sample, mockSampleSourceDao);
		Assert.assertNotNull(managedSample.getSampleObject());
		Assert.assertEquals(managedSample.getSampleObject().getSampleId().intValue(), 1);
		Assert.assertNotNull(managedSample.getParentWrapper());
		Assert.assertNotNull(managedSample.getParentWrapper().getSampleObject().getSampleId());
		Assert.assertEquals(managedSample.getParentWrapper().getSampleObject().getSampleId().intValue(), 2);
		Assert.assertNull(managedSample.getParentWrapper().getParentWrapper());

		//Verifies the use of mockSampleSourceDao is as specified (only need to do this once, i.e in this test)
		verify(mockSampleSourceDao);
	}

	@Test (groups = "unit-tests", dependsOnMethods = {"testSampleWrapperConstruct"})
	public void testSampleWrapperGetAllSampleMeta() {
		// set up mockSampleSourceDao
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(parentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(2)).andReturn(grandparentSample);
		replay(mockSampleSourceDao);
		
		SampleWrapper managedSample = new SampleWrapper(sample, mockSampleSourceDao);
		List<SampleMeta> allMeta = managedSample.getAllSampleMeta();
		Assert.assertEquals(allMeta.size(), 4);
		Map<String, Integer> results = new HashMap<String, Integer>();
		for (SampleMeta m: allMeta){
			logger.debug(m.toString());
			try{
				results.put(m.getK(), Integer.valueOf(m.getV()));
			} catch(NumberFormatException e){
				Assert.fail("couldn't get integer value of " +m.getV()+ " for key "+m.getK() );
			}
		}
		// check for the expected data
		Assert.assertTrue(results.containsKey("sampleMeta1.key1"));
		Assert.assertTrue(results.containsKey("sampleMeta1.key2"));
		Assert.assertTrue(results.containsKey("parentSampleMeta2.key3"));
		Assert.assertTrue(results.containsKey("parentSampleMeta2.key4"));
		Assert.assertTrue(results.get("sampleMeta1.key1").intValue() == 1);
		Assert.assertTrue(results.get("sampleMeta1.key2").intValue() == 2);
		Assert.assertTrue(results.get("parentSampleMeta2.key3").intValue() == 3);
		Assert.assertTrue(results.get("parentSampleMeta2.key4").intValue() == 4);
	}
	
	@Test (groups = "unit-tests", dependsOnMethods = {"testSampleWrapperConstruct"})
	public void testGetAllSampleMetaAreas(){
		// set up mockSampleSourceDao
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(parentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(2)).andReturn(grandparentSample);
		replay(mockSampleSourceDao);
				
		SampleWrapper managedSample = new SampleWrapper(sample, mockSampleSourceDao);
		List<String> areas = new ArrayList<String>();
		areas.addAll(managedSample.getAllSampleMetaAreas());
		Assert.assertEquals(areas.size(), 2);
		Assert.assertTrue(areas.contains("sampleMeta1"));
		Assert.assertTrue(areas.contains("parentSampleMeta2"));
	}
	
	@Test (groups = "unit-tests", dependsOnMethods = {"testSampleWrapperConstruct"})
	public void testGetMetaTemplatedToSampleSybtype(){
		// set up mockSampleSourceDao
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(parentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(2)).andReturn(grandparentSample);
		replay(mockSampleSourceDao);
				
		SampleWrapper managedSample = new SampleWrapper(sample, mockSampleSourceDao);
		SampleSubtype testSampleSubtype1 = new SampleSubtype();
		SampleSubtype testSampleSubtype2 = new SampleSubtype();
		SampleSubtype testSampleSubtype3 = new SampleSubtype();
		testSampleSubtype1.setAreaList("sampleMeta1");
		testSampleSubtype2.setAreaList("sampleMeta1,parentSampleMeta2");
		testSampleSubtype3.setAreaList("sampleMeta1,parentSampleMeta2,anotherArea");
		List<SampleMeta> result1 = managedSample.getMetaTemplatedToSampleSybtype(testSampleSubtype1);
		List<SampleMeta> result2 = managedSample.getMetaTemplatedToSampleSybtype(testSampleSubtype2);
		List<SampleMeta> result3 = managedSample.getMetaTemplatedToSampleSybtype(testSampleSubtype3);
		Assert.assertEquals(result1.size(),2);
		Assert.assertEquals(result2.size(),4);
		Assert.assertEquals(result3.size(),4);
	}
	
	/**
	 * Tests situation in which the managed sample has a sampleId
	 */
	@Test (groups = "unit-tests", dependsOnMethods = {"testSampleWrapperConstruct"})
	public void testUpdateMetaToList1(){
		// set up mockSampleSourceDao
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(parentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(2)).andReturn(grandparentSample);
		replay(mockSampleSourceDao);
				
		SampleWrapper managedSample = new SampleWrapper(sample, mockSampleSourceDao);
		
		// set up test input meta list
		List<SampleMeta> inputMetaList = new ArrayList<SampleMeta>();
		
		SampleMeta sm1 = new SampleMeta(); // same value, sampleId = 1
		sm1.setSampleId(1);
		sm1.setK("sampleMeta1.key1");
		sm1.setV("1");
		inputMetaList.add(sm1);
		
		SampleMeta sm2 = new SampleMeta(); // changed value, sampleId = 2
		sm2.setSampleId(2);
		sm2.setK("parentSampleMeta2.key3");
		sm2.setV("10");
		inputMetaList.add(sm2);
		
		SampleMeta sm3 = new SampleMeta(); // same value, sampleId = null
		sm3.setK("sampleMeta1.key2");
		sm3.setV("2");
		inputMetaList.add(sm3);
		
		SampleMeta sm5 = new SampleMeta(); // new key, sampleId = null
		sm5.setK("parentSampleMeta2.key5");
		sm5.setV("11");
		inputMetaList.add(sm5);
		
		SampleMeta sm6 = new SampleMeta(); // new area, sampleId = 2
		sm6.setSampleId(2);
		sm6.setK("anotherarea.key1");
		sm6.setV("12");
		inputMetaList.add(sm6);
		
		SampleMeta sm7 = new SampleMeta(); // new area, sampleId = null
		sm7.setK("anotherarea.key2");
		sm7.setV("13");
		inputMetaList.add(sm7);
		
		SampleMeta sm8 = new SampleMeta(); // new key, sampleId = 3 (new sample)
		sm8.setSampleId(3);
		sm8.setK("sampleMeta3.key1");
		sm8.setV("14");
		inputMetaList.add(sm8);
		
		// set up mockSamplemetaDao
		SampleMetaDao mockSampleMetaDao = createMockBuilder(SampleMetaDaoImpl.class).addMockedMethods(SampleMetaDaoImpl.class.getMethods()).createMock();
		Assert.assertNotNull(mockSampleMetaDao);
		
		expect(mockSampleMetaDao.save(sampleMeta2)).andReturn(sampleMeta2);
		expect(mockSampleMetaDao.save(sampleMeta4)).andReturn(sampleMeta4);
		expect(mockSampleMetaDao.save(sm2)).andReturn(sm2);
		expect(mockSampleMetaDao.save(sm6)).andReturn(sm6); 
		expect(mockSampleMetaDao.save(sm5)).andReturn(sm5); 
		expect(mockSampleMetaDao.save(sm7)).andReturn(sm7);
		replay(mockSampleMetaDao);
		
		List<SampleMeta> newSampleMetaList = managedSample.updateMetaToList(inputMetaList, mockSampleMetaDao);
		
		Map<String, SampleMeta> results = new HashMap<String, SampleMeta>();
		for (SampleMeta m: newSampleMetaList){
			logger.debug(m.toString()+": sampleId: "+m.getSampleId());
			results.put(m.getK(), m);			
		}
		Assert.assertEquals(newSampleMetaList.size(), 7); // the 4 original sample Meta objects (sampleMeta1 - sampleMeta4) plus three new ones
		Assert.assertEquals(results.get("sampleMeta1.key1").getV(), "1"); // unchanged from sampleMeta1
		Assert.assertEquals(results.get("parentSampleMeta2.key3").getV(), "10"); // changed from original value of 2 in sampleMeta2 by merging in sm2
		Assert.assertEquals(results.get("parentSampleMeta2.key3").getSampleId().intValue(), 2);
		Assert.assertEquals(results.get("sampleMeta1.key2").getV(), "2"); // unchanged from sampleMeta2
		Assert.assertEquals(results.get("sampleMeta1.key2").getSampleId().intValue(), 1);
		
		Assert.assertTrue(results.containsKey("parentSampleMeta2.key5"));
		Assert.assertEquals(results.get("parentSampleMeta2.key5").getV(), "11");
		Assert.assertEquals(results.get("parentSampleMeta2.key5").getSampleId().intValue(), 1); // was null in sm5 before adding
		
		Assert.assertTrue(results.containsKey("anotherarea.key1"));
		Assert.assertEquals(results.get("anotherarea.key1").getV(), "12");
		Assert.assertEquals(results.get("anotherarea.key1").getSampleId().intValue(), 2); // set to 2 in sm6
		
		Assert.assertTrue(results.containsKey("anotherarea.key2"));
		Assert.assertEquals(results.get("anotherarea.key2").getV(), "13");
		Assert.assertEquals(results.get("anotherarea.key2").getSampleId().intValue(), 1); // was null in sm7 before adding
		
		Assert.assertFalse(results.containsKey("sampleMeta3.key1"));
		
		// Verifies the use of mockSampleMetaDao is as specified 
		verify(mockSampleMetaDao);
	}
	
	
	/**
	 * Tests situation in which the sample does NOT have an id set (sampleId=null)
	 */
	@Test (groups = "unit-tests", dependsOnMethods = {"testSampleWrapperConstruct"})
	public void testUpdateMetaToList2(){
		// set up mockSampleSourceDao
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(parentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(2)).andReturn(grandparentSample);
		replay(mockSampleSourceDao);
				
		SampleWrapper managedSample = new SampleWrapper(sample, mockSampleSourceDao);
		sample.setSampleId(null);
		
		// set up test input meta list
		List<SampleMeta> inputMetaList = new ArrayList<SampleMeta>();
		
		SampleMeta sm1 = new SampleMeta(); 
		sm1.setK("sampleMeta1.key1");
		sm1.setV("1");
		inputMetaList.add(sm1);
		
		SampleMeta sm2 = new SampleMeta();
		sm2.setK("anotherarea.key1");
		sm2.setV("12");
		inputMetaList.add(sm2);
		
		SampleMeta sm3 = new SampleMeta(); // new key, sampleId = 3 (new sample)
		sm3.setSampleId(3);
		sm3.setK("sampleMeta3.key1");
		sm3.setV("14");
		inputMetaList.add(sm3);
		
		SampleMeta sm4 = new SampleMeta(); 
		sm4.setK("parentSampleMeta2.key3");
		sm4.setSampleId(2);
		sm4.setV("10");
		inputMetaList.add(sm4);
		
		
		// set up mockSamplemetaDao
		SampleMetaDao mockSampleMetaDao = createMockBuilder(SampleMetaDaoImpl.class).addMockedMethods(SampleMetaDaoImpl.class.getMethods()).createMock();
		Assert.assertNotNull(mockSampleMetaDao);
		expect(mockSampleMetaDao.save(sm4)).andReturn(sm4);
		expect(mockSampleMetaDao.save(sampleMeta4)).andReturn(sampleMeta4);
		replay(mockSampleMetaDao);
		
		List<SampleMeta> newSampleMetaList = managedSample.updateMetaToList(inputMetaList, mockSampleMetaDao);
		Map<String, SampleMeta> results = new HashMap<String, SampleMeta>();
		for (SampleMeta m: newSampleMetaList){
			logger.debug(m.toString()+": sampleId: "+m.getSampleId());
			results.put(m.getK(), m);
		}
		Assert.assertEquals(results.get("sampleMeta1.key1").getV(), "1");
		Assert.assertEquals(results.get("anotherarea.key1").getV(), "12");
		Assert.assertNull(results.get("anotherarea.key1").getSampleId());
		Assert.assertEquals(results.get("parentSampleMeta2.key3").getV(), "10");
		Assert.assertEquals(results.get("parentSampleMeta2.key3").getSampleId().intValue(), 2);
		Assert.assertFalse(results.containsKey("sampleMeta3.key1"));
		
		// Verifies the use of mockSampleMetaDao is as specified 
		verify(mockSampleMetaDao);
	}
	
	@Test (groups = "unit-tests", dependsOnMethods = {"testSampleWrapperConstruct"})
	public void testSaveAll(){
		
		Sample otherParent = new Sample();
		otherParent.setSampleId(3);
				
		// set up mockSampleSourceDao
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(parentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(2)).andReturn(grandparentSample);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(otherParent);
		expect(mockSampleSourceDao.getParentSampleByDerivedSampleId(1)).andReturn(otherParent);
		expect(mockSampleSourceDao.save(isA(SampleSource.class))).andReturn(new SampleSource());
		replay(mockSampleSourceDao);
		
		// set up mockSamplemetaDao
		SampleService mockSampleService = createMockBuilder(SampleServiceImpl.class).addMockedMethods(SampleServiceImpl.class.getMethods()).createMock();
		Assert.assertNotNull(mockSampleService);
		replay(mockSampleService);
		
		SampleWrapper managedSample = new SampleWrapper(sample, mockSampleSourceDao);
		
		// Exercise
		managedSample.saveAll(mockSampleService, mockSampleSourceDao);
		verify(mockSampleService);
		verify(mockSampleSourceDao);
	}

}
