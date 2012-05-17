package edu.yu.einstein.wasp.service.impl;


import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.impl.SampleDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleMetaDaoImpl;
import edu.yu.einstein.wasp.dao.impl.WorkflowDaoImpl;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.AuthenticationService;

public class TestSampleServiceImpl {
	
	HibernateTemplate hibernateTemplateMock;
	private SampleServiceImpl sampleServiceImpl = new SampleServiceImpl();
	SampleDao mockSampleDao;
	WorkflowDao mockWorkflowDao;
	SampleMetaDao mockSampleMetaDao;


  @Test (groups = "unit-tests-service-objects")
  public void getSampleSubtypesForWorkflowByRole() {
	  
	  sampleServiceImpl.setSampleDao(mockSampleDao);
	  sampleServiceImpl.setWorkflowDao(mockWorkflowDao);
	  sampleServiceImpl.setAuthenticationService(new AuthenticationServiceImpl());
	  
	  Integer workflowId = new Integer(1);
	  Workflow wf = new Workflow();
	  wf.setWorkflowId(workflowId);
	  WorkflowSampleSubtype wfss = new WorkflowSampleSubtype();
	  SampleSubtype sampleSubtype = new SampleSubtype();
	  sampleSubtype.setIName("subtype1");
	  sampleSubtype.setSampleSubtypeId(1);
	  
	  List<SampleSubtypeMeta> sampleSubtypeMeta = new ArrayList<SampleSubtypeMeta>();
	  SampleSubtypeMeta meta1 = new SampleSubtypeMeta();
	  meta1.setSampleSubtypeId(1);
	  meta1.setK("subtype1.includeRoles");
	  meta1.setV("7");
	  sampleSubtypeMeta.add(meta1);
	  SampleSubtypeMeta meta2 = new SampleSubtypeMeta();
	  meta2.setSampleSubtypeId(1);
	  meta2.setK("subtype1.excludeRoles");
	  meta2.setV("8,9");
	  sampleSubtypeMeta.add(meta2);
	  
	  sampleSubtype.setSampleSubtypeMeta(sampleSubtypeMeta);
	  wfss.setSampleSubtype(sampleSubtype);

	  List<WorkflowSampleSubtype> wfssList = new ArrayList <WorkflowSampleSubtype>();
	  wfssList.add(wfss);
	  wf.setWorkflowSampleSubtype(wfssList);
	  
	  List<SampleSubtype> sampleSubtypesEmpty = new ArrayList<SampleSubtype>();
	  List<SampleSubtype> sampleSubtypesPopulated = new ArrayList<SampleSubtype>();
	  sampleSubtypesPopulated.add(sampleSubtype);
	  
	  
	  expect(mockWorkflowDao.getWorkflowByWorkflowId(workflowId)).andReturn(wf);
	  expect(mockWorkflowDao.getWorkflowByWorkflowId(workflowId)).andReturn(wf);
	  expect(mockWorkflowDao.getWorkflowByWorkflowId(workflowId)).andReturn(wf);

	  replay(mockWorkflowDao);

	  String [] roles = new String[]{"6"};
      Assert.assertEquals(sampleServiceImpl.getSampleSubtypesForWorkflowByRole(workflowId, roles, null), sampleSubtypesEmpty);
      
      roles = new String[]{"7"};
      Assert.assertEquals(sampleServiceImpl.getSampleSubtypesForWorkflowByRole(workflowId, roles, null), sampleSubtypesPopulated);
      
      roles = new String[]{"7","9"};
      Assert.assertEquals(sampleServiceImpl.getSampleSubtypesForWorkflowByRole(workflowId, roles, null), sampleSubtypesEmpty);
      
	  verify(mockWorkflowDao);
	  
  }
  @Test (groups = "unit-tests-service-objects")
  public void saveSampleWithAssociatedMeta(){
	  sampleServiceImpl.setSampleDao(mockSampleDao);
	  sampleServiceImpl.setSampleMetaDao(mockSampleMetaDao);


	  Sample newsample = new Sample();
	  newsample.setSampleId(1);
	  newsample.setName("0001");
	  List<SampleMeta> sampleMetaList = new ArrayList<SampleMeta>();
	  
	  SampleMeta sampleMeta = new SampleMeta();
	  sampleMeta.setSample(newsample);
	  sampleMeta.setK("sample.sample_type");
	  sampleMeta.setV("ChIP");
	  sampleMetaList.add(sampleMeta);
	  newsample.setSampleMeta(sampleMetaList);
	  
	  expect(mockSampleDao.save(newsample)).andReturn(newsample);
	  replay(mockSampleDao);
	  mockSampleMetaDao.updateBySampleId(1, sampleMetaList);
	  EasyMock.expectLastCall();
	  sampleServiceImpl.saveSampleWithAssociatedMeta(newsample);
	  verify(mockSampleDao);
  }
  
  @Test (groups = "unit-tests-service-objects")
  public void isSampleNameUniqueWithinJob() {
	  SampleType sampleType = new SampleType();
	  sampleType.setIName("sample1");
	  Job job = new Job();
	  Sample sample = new Sample();
	  sample.setName("sample1");
	  sample.setSampleType(sampleType);
	  List<Sample> sampleList =  new ArrayList<Sample>();
	  sampleList.add(sample);
	  job.setSample(sampleList);
	  
	  Assert.assertEquals(sampleServiceImpl.isSampleNameUniqueWithinJob(sample, sampleType, job), false);

	  
  }
  
  @BeforeTest
  public void beforeTest() {
	  mockWorkflowDao = createMockBuilder(WorkflowDaoImpl.class).addMockedMethods(WorkflowDaoImpl.class.getMethods()).createMock();
	  hibernateTemplateMock = createMockBuilder(HibernateTemplate.class).addMockedMethods(HibernateTemplate.class.getMethods()).createMock();
	  mockSampleMetaDao = createMockBuilder(SampleMetaDaoImpl.class).addMockedMethods(SampleMetaDaoImpl.class.getMethods()).createMock();
	  mockSampleDao = createMockBuilder(SampleDaoImpl.class).addMockedMethods(SampleDaoImpl.class.getMethods()).createMock();
	  
	  Assert.assertNotNull(mockWorkflowDao);
	  Assert.assertNotNull(hibernateTemplateMock);
	  Assert.assertNotNull(mockSampleMetaDao);
	  Assert.assertNotNull(mockSampleDao);

  }

  @AfterTest
  public void afterTest() {

  }

  @BeforeMethod
  public void setUp() throws Exception{
	  
 }

  @AfterMethod
  public void afterMethod() {
	  EasyMock.reset(mockWorkflowDao);
	  EasyMock.reset(hibernateTemplateMock);
	  EasyMock.reset(mockSampleMetaDao);
	  EasyMock.reset(mockSampleDao);

	  
  }

  @BeforeClass
  public void beforeClass() {
  }
   
  @BeforeSuite
  public void beforeSuite() {
	  
  }

  @AfterSuite
  public void afterSuite() {
  }

}
