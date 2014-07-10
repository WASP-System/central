package edu.yu.einstein.wasp.service.impl;


import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.impl.SampleDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleMetaDaoImpl;
import edu.yu.einstein.wasp.dao.impl.SampleSourceDaoImpl;
import edu.yu.einstein.wasp.dao.impl.WorkflowDaoImpl;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;

public class TestSampleServiceImpl {
	
	HibernateTemplate hibernateTemplateMock;
	private SampleServiceImpl sampleServiceImpl = new SampleServiceImpl();
	SampleDao mockSampleDao;
	SampleSourceDao mockSampleSourceDao;
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
	  try {
		expect(mockSampleMetaDao.setMeta(sampleMetaList, 1)).andReturn(null);
		replay(mockSampleMetaDao);
		} catch (MetadataException e) {
			Assert.fail(e.getLocalizedMessage());
		}
	  sampleServiceImpl.saveSampleWithAssociatedMeta(newsample);
	  verify(mockSampleDao);
  }
  
  @Test (groups = "unit-tests-service-objects")
  public void isSampleNameUniqueWithinJob() {
	  SampleType sampleType = new SampleType();
	  sampleType.setIName("sample1");
	  Job job = new Job();
	  job.setJobId(1);
	  Sample sample1 = new Sample();
	  sample1.setSampleId(1);
	  sample1.setName("sample1");
	  sample1.setSampleType(sampleType);
	  JobSample js1 = new JobSample();
	  js1.setJobSampleId(1);
	  js1.setJob(job);
	  js1.setSample(sample1);
	  List<JobSample> jobSamples = new ArrayList<JobSample>();
	  jobSamples.add(js1);
	  Sample sample2 = new Sample();
	  sample2.setSampleId(2);
	  sample2.setName("sample1"); // same name as sample1
	  sample2.setSampleType(sampleType);
	  JobSample js2 = new JobSample();
	  js2.setJobSampleId(2);
	  js2.setJob(job);
	  js2.setSample(sample2);
	  jobSamples.add(js2);
	  job.setJobSample(jobSamples);
	  
	  Assert.assertEquals(sampleServiceImpl.isSampleNameUniqueWithinJob(sample1, sampleType, job), false);

	  
  }
  
  @Test (groups = "unit-tests-service-objects")
  public void getPlatformUnitForCell(){
	  sampleServiceImpl.setSampleSourceDao(mockSampleSourceDao);
	  SampleType puSampleType = new SampleType();
	  puSampleType.setIName("platformunit");
	  
	  SampleType cellSampleType = new SampleType();
	  cellSampleType.setIName("cell");
	  
	  Sample pu = new Sample();
	  pu.setSampleId(1);
	  pu.setSampleType(puSampleType);
	  
	  Sample cell1 = new Sample();
	  cell1.setSampleId(2);
	  cell1.setSampleType(cellSampleType);
	  
	  Sample pu2 = new Sample();
	  pu2.setSampleId(3);
	  pu2.setSampleType(puSampleType);
	  
	  List<SampleSource> puCells1 = new ArrayList<SampleSource>();
	  List<SampleSource> puCells2 = new ArrayList<SampleSource>();
	  List<SampleSource> puCells3 = new ArrayList<SampleSource>();
	  SampleSource puCell1 = new SampleSource();
	  puCell1.setSampleSourceId(1);
	  puCell1.setSample(pu);
	  puCell1.setIndex(1);
	  puCell1.setSourceSample(cell1);
	  
	  SampleSource pu2Cell1 = new SampleSource();
	  pu2Cell1.setSampleSourceId(2);
	  pu2Cell1.setSample(pu2);
	  pu2Cell1.setIndex(1);
	  pu2Cell1.setSourceSample(cell1);
	  
	  
	  puCells1.add(puCell1);
	  
	  puCells2.add(puCell1);
	  puCells2.add(pu2Cell1);
	  
	  Map<String,Integer> q = new HashMap<String,Integer>();
	  q.put("sourceSampleId", cell1.getSampleId());
	  expect(mockSampleSourceDao.findByMap(q)).andReturn(puCells1);
	  expect(mockSampleSourceDao.findByMap(q)).andReturn(puCells2);
	  expect(mockSampleSourceDao.findByMap(q)).andReturn(puCells3);
	  replay(mockSampleSourceDao);
	  
	  try {
		Assert.assertEquals(sampleServiceImpl.getPlatformUnitForCell(cell1), pu);
	  } catch (SampleTypeException e) {
		  Assert.fail("Caught unexpected SampleTypeException: "+ e.getMessage());
	  } catch (SampleParentChildException e) {
		  Assert.fail("Caught unexpected SampleParentChildException: "+ e.getMessage());
	  }
	  
	  String exceptionMessage="";
	  try {
			sampleServiceImpl.getPlatformUnitForCell(pu);
	  } catch (SampleTypeException e) {
		  exceptionMessage = e.getMessage();
	  } catch (SampleParentChildException e) {
		  Assert.fail("Caught unexpected SampleParentChildException: "+ e.getMessage());
	  }
	  Assert.assertEquals(exceptionMessage, "Expected 'cell' but got Sample of type 'platformunit' instead.");
	  
	  
	  
	  exceptionMessage="";
	  try {
			sampleServiceImpl.getPlatformUnitForCell(cell1);
	  } catch (SampleParentChildException e) {
		  exceptionMessage = e.getMessage();
	  } catch (SampleTypeException e) {
		  Assert.fail("Caught unexpected SampleTypeException: "+ e.getMessage());
	  }
	  Assert.assertEquals(exceptionMessage, "Cell '2' is associated with more than one flowcell");
	  
	  cell1.setSampleSource(null);
	  exceptionMessage="";
	  try {
			sampleServiceImpl.getPlatformUnitForCell(cell1);
	  } catch (SampleParentChildException e) {
		  exceptionMessage = e.getMessage();
	  } catch (SampleTypeException e) {
		  Assert.fail("Caught unexpected SampleTypeException: "+ e.getMessage());
	  }
	  Assert.assertEquals(exceptionMessage, "Cell '2' is associated with no flowcells");
	  verify(mockSampleSourceDao);
  }
  
  @Test (groups = "unit-tests-service-object")
  public void getIndexedCellsOnPlatformUnit(){
	  
	  sampleServiceImpl.setSampleSourceDao(mockSampleSourceDao);
	  
	  SampleType puSampleType = new SampleType();
	  puSampleType.setIName("platformunit");
	  
	  SampleType cellSampleType = new SampleType();
	  cellSampleType.setIName("cell");
	  
	  SampleType librarySampleType = new SampleType();
	  librarySampleType.setIName("library");
	  
	  Sample pu = new Sample();
	  pu.setSampleId(1);
	  pu.setSampleType(puSampleType);
	  
	  Sample cell1 = new Sample();
	  cell1.setSampleId(2);
	  cell1.setSampleType(cellSampleType);
	  
	  Sample cell2 = new Sample();
	  cell2.setSampleId(3);
	  cell2.setSampleType(cellSampleType);
	  
	  Sample library = new Sample();
	  library.setSampleId(4);
	  library.setSampleType(librarySampleType);
	  
	  List<SampleSource> puCells = new ArrayList<SampleSource>();
	  SampleSource puCell1 = new SampleSource();
	  puCell1.setSampleSourceId(1);
	  puCell1.setSample(pu);
	  puCell1.setIndex(1);
	  puCell1.setSourceSample(cell1);
	  puCells.add(puCell1);
	  
	  SampleSource puCell2 = new SampleSource();
	  puCell2.setSampleSourceId(2);
	  puCell2.setSample(pu);
	  puCell2.setIndex(2);
	  puCell2.setSourceSample(cell2);
	  puCells.add(puCell2);

	  pu.setSampleSource(puCells);
	  
	  Map<Integer, Sample> expectedResult = new HashMap<Integer, Sample>();
	  expectedResult.put(1, cell1);
	  expectedResult.put(2, cell2);
	  
	  Map<String, Integer> qm = new HashMap<String, Integer>();
	  qm.put("sampleId", pu.getSampleId());
	  List<SampleSource> qr1 = new ArrayList<SampleSource>();
	  qr1.add(puCell1);
	  qr1.add(puCell2);
	  
	  Map<String, Integer> qm2 = new HashMap<String, Integer>();
	  qm2.put("sampleId", cell1.getSampleId());
	  	  
	  SampleSource libFail = new SampleSource();
	  libFail.setSample(pu);
	  libFail.setIndex(3);
	  libFail.setSourceSample(library);
	  
	  List<SampleSource> failList = new ArrayList<SampleSource>();
	  failList.add(puCell1);
	  failList.add(puCell2);
	  failList.add(libFail);
	  
	  expect(mockSampleSourceDao.findByMap(qm)).andReturn(qr1);
	  expect(mockSampleSourceDao.findByMap(qm)).andReturn(failList);
	  
	  replay(mockSampleSourceDao);
	  
	  try {
		Assert.assertEquals(sampleServiceImpl.getIndexedCellsOnPlatformUnit(pu), expectedResult);
	  } catch (SampleTypeException e) {
		  Assert.fail("Caught unexpected SampleTypeException: "+ e.getMessage());
	  }
	  
	  String exceptionMessage="";
	  try {
			sampleServiceImpl.getIndexedCellsOnPlatformUnit(cell1);
	  } catch (SampleTypeException e) {
		  exceptionMessage = e.getMessage();
	  }
	  Assert.assertEquals(exceptionMessage, "Expected 'platformunit' but got Sample of type 'cell' instead.");
	  
	  exceptionMessage="";
	  try {
		  	SampleSource puLibrary = new SampleSource();
		  	puLibrary.setSampleSourceId(2);
		  	puLibrary.setSample(pu);
		  	puLibrary.setIndex(3);
		  	puLibrary.setSourceSample(library);
		  	puCells.add(puLibrary);
			sampleServiceImpl.getIndexedCellsOnPlatformUnit(pu);
	  } catch (SampleTypeException e) {
		  exceptionMessage = e.getMessage();
	  }
	  Assert.assertEquals(exceptionMessage, "Expected 'cell' but got Sample of type 'library' instead.");

	  verify(mockSampleSourceDao);
	  
  }
  
  @Test (groups = "unit-tests-service-objects")
  public void getLibrariesOnCellAllMethods(){
	  
	  sampleServiceImpl.setSampleSourceDao(mockSampleSourceDao);
	  
	  List<Sample> samplesNoControl = new ArrayList<Sample>(); 
	  List<Sample> samplesWithControl = new ArrayList<Sample>(); 
	  List<Sample> controlSamples = new ArrayList<Sample>(); 
	  
	  SampleType cellSampleType = new SampleType();
	  cellSampleType.setIName("cell");
	  
	  SampleType librarySampleType = new SampleType();
	  librarySampleType.setIName("library");
	  
	  SampleSubtype librarySampleSubtype = new SampleSubtype();
	  librarySampleSubtype.setIName("ChipSeqLibrary");
	  
	  SampleSubtype controlSampleSubtype = new SampleSubtype();
	  controlSampleSubtype.setIName("controlLibrarySample");
	  
	  Sample cell1 = new Sample();
	  cell1.setSampleId(1);
	  cell1.setSampleType(cellSampleType);
	  
	  Sample cellWrongType = new Sample();
	  cellWrongType.setSampleId(2);
	  cellWrongType.setSampleType(librarySampleType);
	  
	  Sample library1 = new Sample();
	  library1.setSampleId(3);
	  library1.setSampleType(librarySampleType);
	  library1.setSampleSubtype(librarySampleSubtype);
	  samplesNoControl.add(library1);

	  Sample library2 = new Sample();
	  library2.setSampleId(4);
	  library2.setSampleType(librarySampleType);
	  library2.setSampleSubtype(librarySampleSubtype);
	  samplesNoControl.add(library2);
	  
	  samplesWithControl.addAll(samplesNoControl);
	  
	  Sample control = new Sample();
	  control.setSampleId(5);
	  control.setSampleType(librarySampleType);
	  control.setSampleSubtype(controlSampleSubtype);
	  samplesWithControl.add(control);
	  controlSamples.add(control);

	  Sample libraryWrongType = new Sample();
	  libraryWrongType.setSampleId(6);
	  libraryWrongType.setSampleType(cellSampleType);

	  List<SampleSource> cell1Libraries = new ArrayList<SampleSource>();
	  SampleSource ssCell1Lib1 = new SampleSource();
	  ssCell1Lib1.setSampleSourceId(1);
	  ssCell1Lib1.setSample(cell1);
	  ssCell1Lib1.setSourceSample(library1);
	  cell1Libraries.add(ssCell1Lib1);

	  SampleSource ssCell1Lib2 = new SampleSource();
	  ssCell1Lib2.setSampleSourceId(2);
	  ssCell1Lib2.setSample(cell1);
	  ssCell1Lib2.setSourceSample(library2);
	  cell1Libraries.add(ssCell1Lib2);
	  
	  SampleSource ssCell1Lib3 = new SampleSource();
	  ssCell1Lib3.setSampleSourceId(3);
	  ssCell1Lib3.setSample(cell1);
	  ssCell1Lib3.setSourceSample(control);
	  cell1Libraries.add(ssCell1Lib3);
	  
	  List<SampleSource> cell1LibrariesWithNonLibrary = new ArrayList<SampleSource>();
	  cell1LibrariesWithNonLibrary.addAll(cell1Libraries);
	  
	  SampleSource ssCell1Lib3WrongType = new SampleSource();
	  ssCell1Lib3WrongType.setSampleSourceId(4);
	  ssCell1Lib3WrongType.setSample(cell1);
	  ssCell1Lib3WrongType.setSourceSample(libraryWrongType);
	  cell1LibrariesWithNonLibrary.add(ssCell1Lib3WrongType);
	  
	  cell1.setSampleSource(cell1Libraries);
	  
	  Map<String, Integer> qu = new HashMap<String, Integer>();
	  qu.put("sampleId", cell1.getSampleId());
	  
	  List<SampleSource> cell1LibrariesNoControl = new ArrayList<SampleSource>();
	  for (SampleSource ss : cell1Libraries) {
		  SampleSubtype sss = ss.getSourceSample().getSampleSubtype();
		  if ((sss == null) || (!sss.getIName().equals(controlSampleSubtype.getIName()))) {
			  cell1LibrariesNoControl.add(ss);
		  }
	  }
	  
	  List<SampleSource> ssc = new ArrayList<SampleSource>();
	  ssc.add(ssCell1Lib3);	  
	  
	  expect(mockSampleSourceDao.getCellLibrariesForCell(cell1)).andReturn(cell1Libraries);
	  expect(mockSampleSourceDao.getCellLibrariesForCell(cell1)).andReturn(cell1LibrariesNoControl);
	  expect(mockSampleSourceDao.getCellLibrariesForCell(cell1)).andReturn(ssc);
	  expect(mockSampleSourceDao.getCellLibrariesForCell(cell1)).andReturn(cell1LibrariesWithNonLibrary);
	  
	  replay(mockSampleSourceDao);
	  	  
	  try {
		Assert.assertEquals(sampleServiceImpl.getLibrariesOnCell(cell1), samplesWithControl);
	  } catch (SampleTypeException e) {
		Assert.fail("Caught unexpected SampleTypeException: "+ e.getMessage());
	  }
	  
	  try {
		Assert.assertEquals(sampleServiceImpl.getLibrariesOnCellWithoutControls(cell1), samplesNoControl);
	  } catch (SampleTypeException e) {
		Assert.fail("Caught unexpected SampleTypeException: "+ e.getMessage());
	  }
	  
	  try {
		Assert.assertEquals(sampleServiceImpl.getControlLibrariesOnCell(cell1), controlSamples);
	  } catch (SampleTypeException e) {
		Assert.fail("Caught unexpected SampleTypeException: "+ e.getMessage());
	  }
	  
	  String exceptionMessage="";
	  try {
			sampleServiceImpl.getLibrariesOnCell(cellWrongType);
	  } catch (SampleTypeException e) {
		  exceptionMessage = e.getMessage();
	  }
	  Assert.assertEquals(exceptionMessage, "Expected 'cell' but got Sample of type 'library' instead.");
	  
	  cell1.setSampleSource(cell1LibrariesWithNonLibrary);
	  
	  exceptionMessage="";
	  try {
			sampleServiceImpl.getLibrariesOnCell(cell1);
	  } catch (SampleTypeException e) {
		  exceptionMessage = e.getMessage();
	  }
	  Assert.assertEquals(exceptionMessage, "Expected 'library' but got Sample of type 'cell' instead.: cellId: 1 cellName = null problem libraryId = 6 problem library name = null");
	  
	  verify(mockSampleSourceDao);
	  
  }
  
  @BeforeTest
  public void beforeTest() {
	  mockWorkflowDao = createMockBuilder(WorkflowDaoImpl.class).addMockedMethods(WorkflowDaoImpl.class.getMethods()).createMock();
	  hibernateTemplateMock = createMockBuilder(HibernateTemplate.class).addMockedMethods(HibernateTemplate.class.getMethods()).createMock();
	  mockSampleMetaDao = createMockBuilder(SampleMetaDaoImpl.class).addMockedMethods(SampleMetaDaoImpl.class.getMethods()).createMock();
	  mockSampleDao = createMockBuilder(SampleDaoImpl.class).addMockedMethods(SampleDaoImpl.class.getMethods()).createMock();
	  mockSampleSourceDao = createMockBuilder(SampleSourceDaoImpl.class).addMockedMethods(SampleSourceDaoImpl.class.getMethods()).createMock();
	  
	  Assert.assertNotNull(mockWorkflowDao);
	  Assert.assertNotNull(hibernateTemplateMock);
	  Assert.assertNotNull(mockSampleMetaDao);
	  Assert.assertNotNull(mockSampleDao);
	  Assert.assertNotNull(mockSampleSourceDao);

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
	  EasyMock.reset(mockSampleSourceDao);

	  
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
