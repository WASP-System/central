/**
 *
 * SampleServiceImpl.java 
 * 
 * the SampleService Implementation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.MetaAttributeNotFoundException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.ResourceException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleSubtypeException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspLibraryTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspSampleTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.LibraryStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.SampleStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;
import edu.yu.einstein.wasp.interfacing.plugin.SequencingViewProviding;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCellSelection;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;

@Service
@Transactional("entityManager")
public class SampleServiceImpl extends WaspMessageHandlingServiceImpl implements SampleService {
	
	protected static final String LOCK_META_AREA = "lock";
	
	protected static final String LOCK_META_KEY = "status";
	
	protected SampleDao	sampleDao;

	/**
	 * setSampleDao(SampleDao sampleDao)
	 * 
	 * @param sampleDao
	 * 
	 */
	@Override
	@Autowired
	public void setSampleDao(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
	}
	
	@Override
	public SampleDao getSampleDao() {
		return this.sampleDao;
	}
	
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
	
	protected AuthenticationService authenticationService;
	
	
	@Override
	@Autowired
	public void setAuthenticationService(AuthenticationService authenticationService){
		this.authenticationService = authenticationService;
	}
	
	@Autowired
	protected MetaMessageService metaMessageService;

	@Autowired
	protected WorkflowDao workflowDao;
	
	@Autowired
	protected SampleBarcodeDao sampleBarcodeDao;

	@Autowired
	protected SampleMetaDao sampleMetaDao;
	
	@Autowired
	protected JobService jobService;

	@Autowired
	protected ResourceService resourceService;
	
	protected SampleSourceDao sampleSourceDao;
	
	protected SampleSourceMetaDao sampleSourceMetaDao;
	

	
	protected SampleSubtypeDao sampleSubtypeDao;
	

	@Override
	public SampleSourceMetaDao getSampleSourceMetaDao() {
		return sampleSourceMetaDao;
	}

	@Override
	@Autowired
	public void setSampleSourceMetaDao(SampleSourceMetaDao sampleSourceMetaDao) {
		this.sampleSourceMetaDao = sampleSourceMetaDao;
	}

	@Override
	public SampleSubtypeDao getSampleSubtypeDao() {
		return sampleSubtypeDao;
	}

	@Override
	@Autowired
	public void setSampleSubtypeDao(SampleSubtypeDao sampleSubtypeDao) {
		this.sampleSubtypeDao = sampleSubtypeDao;
	}
	
	protected SampleTypeDao sampleTypeDao;

	@Override
	public SampleTypeDao getSampleTypeDao() {
		return sampleTypeDao;
	}

	@Override
	@Autowired
	public void setSampleTypeDao(SampleTypeDao sampleTypeDao) {
		this.sampleTypeDao = sampleTypeDao;
	}

	@Autowired
	protected AdaptorDao adaptorDao;
	
	@Autowired
	protected BarcodeDao barcodeDao;
	
	@Autowired
	protected RunDao runDao;
	
	@Autowired
	protected JobDao jobDao;
	
	@Autowired
	protected JobSampleDao jobSampleDao;
	
	@Autowired
	protected JobCellSelectionDao jobCellSelectionDao;
	
	@Autowired
	protected SampleJobCellSelectionDao sampleJobCellSelectionDao;

	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
	@Autowired
	protected RunService runService;
		
	@Autowired
	GenomeService genomeService;
	
	@Autowired
	protected RunMetaDao runMetaDao;
	
	@Autowired
	 protected ResourceDao resourceDao;
	
	@Autowired
	protected WaspPluginRegistry pluginRegistry;
	
	/**
	 * Setter for the sampleMetaDao
	 * @param sampleMetaDao
	 */
	@Autowired
	@Override
	public void setSampleMetaDao(SampleMetaDao sampleMetaDao) {
		this.sampleMetaDao = sampleMetaDao;
	}
	
	@Override
	public SampleMetaDao getSampleMetaDao(){
		return this.sampleMetaDao;
	}
	
	protected SampleDraftDao sampleDraftDao;
	
	@Autowired
	@Override
	public void setSampleDraftDao(SampleDraftDao sampleDraftDao) {
		this.sampleDraftDao = sampleDraftDao;
	}
	
	@Override
	public SampleDraftDao getSampleDraftDao(){
		return this.sampleDraftDao;
	}
	
	protected SampleDraftMetaDao sampleDraftMetaDao;
	
	@Autowired
	@Override
	public void setSampleDraftMetaDao(SampleDraftMetaDao sampleDraftMetaDao) {
		this.sampleDraftMetaDao = sampleDraftMetaDao;
	}
	
	@Override
	public SampleDraftMetaDao getSampleDraftMetaDao(){
		return this.sampleDraftMetaDao;
	}


	@Override
	public Sample getSampleByName(final String name) {
		return this.getSampleDao().getSampleByName(name);
	}

	@Override
	public List<Sample> getPlatformUnits() {
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("sampleType.iName", "platformunit");
		return sampleDao.findByMap(queryMap);
	}
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId){
		  return getSampleSubtypesForWorkflowByRole(workflowId, authenticationService.getRoles(), null);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId, String sampleTypeIName){
		  return getSampleSubtypesForWorkflowByRole(workflowId, authenticationService.getRoles(), sampleTypeIName);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId, String[] roles){
		  return getSampleSubtypesForWorkflowByRole(workflowId, roles, null);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  @Transactional("entityManager")
	  public void saveSampleWithAssociatedMeta(Sample sample){
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  //create new reference to sample.sampleMeta, for use in the sampleMetaDoa call, as sample.getSampleMeta() returns an empty list following the save(sample) [due to entityManager.refresh(sample) performed in SampleDao.save()]
		  List<SampleMeta> sampleMetaList = sample.getSampleMeta();		  
		  Sample sampleInDB=sampleDao.save(sample);
		  try {
			  sampleMetaDao.setMeta(sampleMetaList, sampleInDB.getId());
		  } catch (MetadataException e) {
			  logger.warn(e.getLocalizedMessage());
		  }
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId, String[] roles, String sampleTypeIName){
		  Assert.assertParameterNotNullNotZero(workflowId, "Invalid workflowId Provided");
		  Assert.assertParameterNotNull(roles, "No roles provided");
		  List<SampleSubtype> sampleSubtypes = new ArrayList<SampleSubtype>();
		  for (WorkflowSampleSubtype wfsts: workflowDao.getWorkflowByWorkflowId(workflowId).getWorkflowSampleSubtype() ){
			  SampleSubtype sts = wfsts.getSampleSubtype();
			  if (sampleTypeIName == null || sampleTypeIName.equals(sts.getSampleType().getIName())){
				  String[] includedRoles = new String[]{};
				  String[] excludedRoles = new String[]{};
				  try{
					  includedRoles = MetaHelper.getMetaValue(sts.getIName(), "includeRoles",sts.getSampleSubtypeMeta()).split(",");
				  } catch(MetadataException e){
					  // "includeRoles" meta not present
				  }
				  try{
					  excludedRoles = MetaHelper.getMetaValue(sts.getIName(), "excludeRoles",sts.getSampleSubtypeMeta()).split(",");
				  } catch(MetadataException e){
					  // "excludeRoles" meta not present
				  }
				  if ((includedRoles.length == 0 && excludedRoles.length == 0) ||
						  ( authenticationService.hasRoleInRoleArray(includedRoles, roles) && 
								  !authenticationService.hasRoleInRoleArray(excludedRoles, roles) ) ){
					  sampleSubtypes.add(sts);
				  }
			  }
		  }
		  return sampleSubtypes;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean isSampleNameUniqueWithinJob(Sample sampleIn, SampleType sampleType, Job job){
		  Assert.assertParameterNotNull(sampleIn, "Invalid sampleIn Provided");
		  Assert.assertParameterNotNull(sampleType, "Invalid sampleType Provided");
		  Assert.assertParameterNotNull(job, "Invalid sampleIn Provided");
		  List<Sample> samplesInThisJob = job.getSample();
		  for(Sample sample : samplesInThisJob){
			  if (sampleIn.getId() != null && sample.getId().intValue() == sampleIn.getId().intValue())
				  continue;
			  if( sample.getSampleType().getIName().equals(sampleType.getIName()) && sampleIn.getName().equals(sample.getName()) ){
				  return false;
			  }
		  }
		  return true;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public ExitStatus getReceiveSampleStatus(final Sample sample){
		// TODO: Write test!!
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		  ExitStatus sampleReceivedStatus = ExitStatus.UNKNOWN;
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> sampleIdStringSet = new LinkedHashSet<String>();
		  sampleIdStringSet.add(sample.getId().toString());
		  parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		  List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.sample.step.listenForSampleReceived", parameterMap, false);
		  stepExecutions.addAll(batchJobExplorer.getStepExecutions("wasp.library.step.listenForLibraryReceived", parameterMap, false));
		  if (!stepExecutions.isEmpty())
			  sampleReceivedStatus =  batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions).getExitStatus();
		  return sampleReceivedStatus;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public ExitStatus getSampleQCStatus(final Sample sample){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> sampleIdStringSet = new LinkedHashSet<String>();
		  sampleIdStringSet.add(sample.getId().toString());
		  parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		  List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.sample.step.sampleQC", parameterMap, false);
		  ExitStatus sampleQCStatus = ExitStatus.UNKNOWN;
		  if (!stepExecutions.isEmpty())
			  sampleQCStatus = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions).getExitStatus();
		  return sampleQCStatus;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public ExitStatus getLibraryQCStatus(final Sample library){
		// TODO: Write test!!
		Assert.assertParameterNotNull(library, "No library provided");
		Assert.assertParameterNotNullNotZero(library.getId(), "Invalid library Provided");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> sampleIdStringSet = new LinkedHashSet<String>();
		sampleIdStringSet.add(library.getId().toString());
		parameterMap.put(WaspJobParameters.LIBRARY_ID, sampleIdStringSet);
		List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.library.step.libraryQC", parameterMap, false);
		ExitStatus libraryQCStatus = ExitStatus.UNKNOWN;
		if (!stepExecutions.isEmpty())
			libraryQCStatus = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions).getExitStatus();
		return libraryQCStatus;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean isSamplePassQC(final Sample sample){
		// TODO: Write test!!
		Assert.assertParameterNotNull(sample, "No Sample provided");
		Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		if (getSampleQCStatus(sample).getExitCode().equals(ExitStatus.COMPLETED.getExitCode()))
			return true;
		return false;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean isLibraryPassQC(final Sample library){
		// TODO: Write test!!
		  Assert.assertParameterNotNull(library, "No library provided");
		  Assert.assertParameterNotNullNotZero(library.getId(), "Invalid library Provided");
		  if (getLibraryQCStatus(library).getExitCode().equals(ExitStatus.COMPLETED.getExitCode()))
				return true;
			return false;
	  }
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isSampleReceivedOrWithdrawn(Sample sample){
		Assert.assertParameterNotNull(sample, "No Sample provided");
		Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		if (getReceiveSampleStatus(sample).getExitCode().equals(ExitStatus.COMPLETED.getExitCode()) || 
				getReceiveSampleStatus(sample).getExitCode().equals(ExitStatus.FAILED.getExitCode()))
			return true;
		return false;
	}
	
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isSampleAwaitingLibraryCreation(Sample sample){
		// this requires the existence of a sample and no library recorded as currently being processed or successfully made 
		Assert.assertParameterNotNull(sample, "No Sample provided");
		Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		if (isLibrary(sample))
			return false;
		if (!isSamplePassQC(sample))
			return false;
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> sampleIdStringSet = new LinkedHashSet<String>();
		sampleIdStringSet.add(sample.getId().toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		List<Sample> librariesExisting = sample.getChildren();
		if (librariesExisting == null || librariesExisting.isEmpty()){
			logger.debug("No libraries currently associated with sample id=" + sample.getId() + " (" + sample.getName() + ")");
			return true; // no libraries made yet for this sample
		}
		
		
		// libraries already exist for this sample. Lets see if we need to make another 
		// (no existing libraries have a flow that is running or completed successfully)
		parameterMap = new HashMap<String, Set<String>>();
		for (Sample library: librariesExisting){
			Set<String> libraryIdStringSet = new LinkedHashSet<String>();
			libraryIdStringSet.add(library.getId().toString());
			parameterMap.put(WaspJobParameters.LIBRARY_ID, libraryIdStringSet);
			List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions("wasp.facilityLibrary.jobflow", parameterMap, false);
			
			for (JobExecution jobExecution: jobExecutions){
				ExitStatus jobExitStatus = jobExecution.getExitStatus();
				if (jobExitStatus.isRunning() || jobExitStatus.isCompleted() ){
					// a library is still active or completed so not awaiting creation.
					// to make a new library despite this requires special logic
					return false;  
				}
			}
		}
		
		return true; // no existing libraries have a flow that is running or completed successfully
	}
	
		
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortSamplesBySampleName(List<Sample> samples){
		  Assert.assertParameterNotNull(samples, "No Sample list provided");
		  // TODO: Write test!!
		  class SampleNameComparator implements Comparator<Sample> {
			    @Override
			    public int compare(Sample arg0, Sample arg1) {
			        return arg0.getName().compareToIgnoreCase(arg1.getName());
			    }
		  }
		  Collections.sort(samples, new SampleNameComparator());//sort by sample's name 
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortSamplesBySampleId(List<Sample> samples){
		  Assert.assertParameterNotNull(samples, "No Sample list provided");
		  // TODO: Write test!!
		  class SampleIdComparator implements Comparator<Sample> {
			    @Override
			    public int compare(Sample arg0, Sample arg1) {
			        return arg0.getId().compareTo(arg1.getId());
			    }
		  }
		  Collections.sort(samples, new SampleIdComparator());//sort by sample's id 
	  }
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public String convertSampleReceivedStatusForWeb(ExitStatus internalStatus){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(internalStatus, "No internalStatus provided");
		  if(internalStatus.isRunning()){
			  return "NOT ARRIVED";
			}
			else if(internalStatus.isCompleted()){
				return "RECEIVED";
			}
			else if(internalStatus.isTerminated() || internalStatus.isStopped()){
				return "WITHDRAWN";
			} 
			else {
				return "UNKNOWN";
			}
	  }
	  
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public String convertSampleQCStatusForWeb(ExitStatus internalStatus){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(internalStatus, "No internalStatus provided");
		  if(internalStatus.isRunning())
			  return "AWAITING QC";
		  if(internalStatus.isCompleted())
			  return "PASSED";
		  if(internalStatus.isFailed())
			  return "FAILED";
		  if(internalStatus.isTerminated())
			  return "ABANDONED";
		  return "UNKNOWN";
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public WaspStatus convertSampleQCStatusFromWeb(String webStatus){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(webStatus, "No webStatus provided");
		  	if(webStatus.equals(STATUS_PASSED))
				return WaspStatus.COMPLETED;
			if(webStatus.equals(STATUS_FAILED))
				return WaspStatus.FAILED;
			return WaspStatus.UNKNOWN;
	  }
	  

	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<String> getReceiveSampleStatusOptionsForWeb(){
		  List<String> options = new ArrayList<String>();
		  options.add(convertSampleReceivedStatusForWeb(ExitStatus.COMPLETED));
		  options.add(convertSampleReceivedStatusForWeb(ExitStatus.FAILED));
		  return options;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void initiateBatchJobForSample(Job job, Sample sample, String batchJobName){
		  Assert.assertParameterNotNull(job, "No Job provided");
		  Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		  Assert.assertParameterNotNull(batchJobName, "No batchJobName provided");
		  // send message to initiate job processing
		  Map<String, String> jobParameters = new HashMap<String, String>();
		  jobParameters.put(WaspJobParameters.JOB_ID, job.getId().toString());
		  jobParameters.put(WaspJobParameters.SAMPLE_ID, sample.getId().toString());
		  BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(batchJobName, jobParameters) );
		  try {
			sendOutboundMessage(batchJobLaunchMessageTemplate.build(), true);
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void initiateBatchJobForLibrary(Job job, Sample library, String batchJobName){
		  Assert.assertParameterNotNull(job, "No Job provided");
		  Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		  Assert.assertParameterNotNull(library, "No Library provided");
		  Assert.assertParameterNotNullNotZero(library.getId(), "Invalid Library Provided");
		  Assert.assertParameterNotNull(batchJobName, "No batchJobName provided");
		  // send message to initiate job processing
		  Map<String, String> jobParameters = new HashMap<String, String>();
		  jobParameters.put(WaspJobParameters.JOB_ID, job.getId().toString());
		  jobParameters.put(WaspJobParameters.LIBRARY_ID, library.getId().toString());
		  BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(batchJobName, jobParameters) );
		  try {
			sendOutboundMessage(batchJobLaunchMessageTemplate.build(), true);
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void updateSampleReceiveStatus(final Sample sample, final WaspStatus status) throws WaspMessageBuildingException{
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		  Assert.assertParameterNotNull(status, "No Status provided");
		  if (status != WaspStatus.CREATED && status != WaspStatus.ABANDONED)
			  throw new InvalidParameterException("WaspStatus is null, or not CREATED or ABANDONED");
		  
		  WaspStatusMessageTemplate messageTemplate;
		  if (isLibrary(sample)){
			  messageTemplate = new LibraryStatusMessageTemplate(sample.getId());
		  } else {
			  messageTemplate = new SampleStatusMessageTemplate(sample.getId());
		  }
		  messageTemplate.setStatus(status); // sample received (CREATED) or abandoned (ABANDONED)
		  try{
			  sendOutboundMessage(messageTemplate.build(), true);
		  } catch (MessagingException e){
			  throw new WaspMessageBuildingException(e.getLocalizedMessage());
		  }
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void updateQCStatus(final Sample sample, final WaspStatus status) throws WaspMessageBuildingException{
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		  Assert.assertParameterNotNull(status, "No Status provided");
		  if (status != WaspStatus.COMPLETED && status != WaspStatus.FAILED)
			  throw new InvalidParameterException("WaspStatus is null, or not COMPLETED or FAILED");
		  
		  WaspStatusMessageTemplate messageTemplate;
		  if (isLibrary(sample)){
			  messageTemplate = new LibraryStatusMessageTemplate(sample.getId());
			  messageTemplate.setTask(WaspLibraryTask.QC);
		  } else {
			  messageTemplate = new SampleStatusMessageTemplate(sample.getId());
			  messageTemplate.setTask(WaspSampleTask.QC);
		  }
		  messageTemplate.setStatus(status); // sample received (COMPLETED) or abandoned (FAILED)
		  try{
			  sendOutboundMessage(messageTemplate.build(), true);
		  } catch (MessagingException e){
			  throw new WaspMessageBuildingException(e.getLocalizedMessage());
		  }
	  }
	  
	  
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean isSubmittedSampleProcessedByFacility(final Sample sample){//should but doesn't really check that this is a user-submitted sample
		// TODO: Write test!!
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		  boolean sampleHasBeenProcessed = false;
		  if( sample.getSourceSample().size() > 0){/* submitted sample is a user-submitted library that has been placed onto a flow cell or a user-submitted macromolecule that has been used to generate a library */
			  sampleHasBeenProcessed = true;
		  }
		  return sampleHasBeenProcessed;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getFacilityGeneratedLibraries(Sample sample){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		  List<Sample> libraryList = new ArrayList<Sample>();
		  if (sample.getChildren() != null){
			  for (Sample childSample : sample.getChildren()){
				  if (isLibrary(childSample)){
					  libraryList.add(childSample);
				  }
			  }
		  } else {
			  logger.warn("No facility generated libraries found");
		  }
		  return libraryList;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Adaptor getLibraryAdaptor(Sample library){
		// TODO: Write test!!
		  //ACTUALLY, there is another method, AdaptorService.getAdaptor(Sample Library) that exists. We don't need two. Perhaps this should be retired.
		  Assert.assertParameterNotNull(library, "No Sample provided");
		  Adaptor adaptor = null;
		  String adaptorId = new String("");
		  SampleSubtype sampleSubtype = library.getSampleSubtype();
		  String areaList = sampleSubtype.getAreaList();
		  String area = new String("");
		  
		  String [] stringList = areaList.split("[\\s,]+");//separates on comma or whitespace
		  for(String string : stringList){
			  
			  if(string.indexOf("genericLibrary") > -1){//5-31-13 changed Library to genericLibrary
				  area = string;
			  }
		  }
		 
		  try{		
			  adaptorId = MetaHelper.getMetaValue(area, "adaptor", library.getSampleMeta());
		  }
		  catch(MetadataException me){
			  logger.warn("Unable to identify adaptor for libraryId " + library.getId());
		  }
		  if( ! adaptorId.equals("") ){
			  adaptor = adaptorDao.findById(new Integer(adaptorId));
		  }		  
		  return adaptor;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getAvailablePlatformUnits(){
		  // TODO: Write test!!
		  
		  List<Sample> availablePlatformUnits = new ArrayList<Sample>();
		  List<Sample> allPlatformUnits = getPlatformUnits(); 
		  if (allPlatformUnits == null || allPlatformUnits.isEmpty())
			  return availablePlatformUnits;
		
		  // get platform units that are not associated with currently executing or completed runs
		 		  
		  // get job executions for ALL platform units on all runs and record those associated with runs that are currently executing or have completed
		  // successfully (COMPLETED) or have failed QC or been rejected (FAILED). If run is in status STOPPED (aborted) the platform unit
		  // should be made available for adding more libraries. Might want to review this use case!!
		  
		  // 'run' batch jobs are provided with one parameter, runId
		  // we can obtain all run job executions by selecting jobs which have these parameters (regardless of the values as specified by "*")
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> runIdStringSet = new LinkedHashSet<String>();
		  runIdStringSet.add("*");
		  parameterMap.put(WaspJobParameters.RUN_ID, runIdStringSet);
		  Set<Integer> IdsForPlatformUnitsNotAvailable = new LinkedHashSet<Integer>();
		  List<JobExecution> allRelevantJobExecutions = new ArrayList<JobExecution>();
		  allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, false, ExitStatus.EXECUTING) );
		  allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, false, ExitStatus.COMPLETED) );
		  allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, false, ExitStatus.FAILED) );
		  
		  // make platform unit available again if ExitStatus is STOPPED (aborted) 
		  // so comment the following line out for now:
		  // allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.STOPPED) );
		  
		  // get sample id for all platform units associated with the batch job executions retrieved
		  for (JobExecution je: allRelevantJobExecutions){
			  try{
				  Run run = runService.getRunById(Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(je, WaspJobParameters.RUN_ID)));
				  IdsForPlatformUnitsNotAvailable.add(run.getPlatformUnit().getId());
			  } catch (ParameterValueRetrievalException e){
				  logger.warn(e.getLocalizedMessage());
				  continue;
			  } catch (NumberFormatException e){
				  logger.warn(e.getLocalizedMessage());
				  continue;
			  }
		  }
		  
		  // collect platform unit objects whose id's are not in the IdsForPlatformUnitsNotAvailable list
		  for (Sample pu: allPlatformUnits){
			  try {
				if (! IdsForPlatformUnitsNotAvailable.contains( pu.getId() ) && ! getPlatformUnitLockStatus(pu).equals(LockStatus.LOCKED))
					  availablePlatformUnits.add(pu);
				} catch (SampleTypeException e) {
					logger.warn("received unexpected exception: " + e.getLocalizedMessage()); // shouldn't get here
				}
		  }
		  
		  return availablePlatformUnits; 
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getAvailableAndCompatiblePlatformUnits(ResourceCategory resourceCategory){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(resourceCategory, "No ResourceCategory provided");
		  List<Sample> availablePlatformUnits = getAvailablePlatformUnits();
		  List<Sample> availableAndCompatibleFlowCells = new ArrayList<Sample>();
		  for(Sample pu : availablePlatformUnits){
			  for(SampleSubtypeResourceCategory ssrc : pu.getSampleSubtype().getSampleSubtypeResourceCategory()){
				  if(ssrc.getResourcecategoryId().intValue() == resourceCategory.getId().intValue()){
					  availableAndCompatibleFlowCells.add(pu);
				  }
			  }
		  }
		  return availableAndCompatibleFlowCells;
	  }
	  
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getAvailableAndCompatiblePlatformUnits(Job job){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(job, "No Job provided");
		  List<Sample> availablePlatformUnits = getAvailablePlatformUnits();
		  List<Sample> availableAndCompatibleFlowCells = new ArrayList<Sample>();
		  List<JobResourcecategory> jrcList = job.getJobResourcecategory();
		  if (jrcList == null || jrcList.isEmpty()){
			  logger.debug("No resource categories defined for job with id=" + job.getId());
			  return availableAndCompatibleFlowCells;
		  }
		  for(Sample pu : availablePlatformUnits){
			  for(SampleSubtypeResourceCategory ssrc : pu.getSampleSubtype().getSampleSubtypeResourceCategory()){
				  for(JobResourcecategory jrc : jrcList){
					  if(ssrc.getResourcecategoryId().intValue() == jrc.getResourcecategoryId().intValue()){
						  availableAndCompatibleFlowCells.add(pu);
					  }
				  }
			  }
		  }
		  class SampleCreatedComparator implements Comparator<Sample> {
				@Override
				public int compare(Sample arg0, Sample arg1) {
					return arg1.getCreated().compareTo(arg0.getCreated());
				}
			}
			Collections.sort(availableAndCompatibleFlowCells, new SampleCreatedComparator());//most recent is now first, least recent is last

		  return availableAndCompatibleFlowCells;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Integer getCellIndex(Sample cell) throws SampleTypeException, SampleParentChildException{
		  Assert.assertParameterNotNull(cell, "No Cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid Cell Provided");
		  if (!isCell(cell)){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sourceSampleId", cell.getId());
		  List<SampleSource> sampleSourceList = getSampleSourceDao().findByMap(q);
			  
		  if (sampleSourceList==null || sampleSourceList.isEmpty())
			  throw new SampleParentChildException("Cell '"+cell.getId().toString()+"' is associated with no flowcells");
		  if (sampleSourceList.size() > 1)
			  throw new SampleParentChildException("Cell '"+cell.getId().toString()+"' is associated with more than one flowcell");
		  SampleSource ss = sampleSourceList.get(0);
		  return ss.getIndex();
	  }

	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Sample getPlatformUnitForCell(Sample cell) throws SampleTypeException, SampleParentChildException{
		  Assert.assertParameterNotNull(cell, "No Cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid Cell Provided");
		  if (!isCell(cell)){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sourceSampleId", cell.getId());
		  List<SampleSource> sampleSourceList = getSampleSourceDao().findByMap(q);
			  
		  if (sampleSourceList==null || sampleSourceList.isEmpty())
			  throw new SampleParentChildException("Cell '"+cell.getId().toString()+"' is associated with no flowcells");
		  if (sampleSourceList.size() > 1)
			  throw new SampleParentChildException("Cell '"+cell.getId().toString()+"' is associated with more than one flowcell");
		  SampleSource ss = sampleSourceList.get(0);
		  return ss.getSample();
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Map<Integer, Sample> getIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException{
		  Assert.assertParameterNotNull(platformUnit, "No Platform unit provided");
		  Assert.assertParameterNotNullNotZero(platformUnit.getId(), "Invalid Platform unit provided");
		  if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			  throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		  }
		  
		  Map<Integer, Sample> indexedCells = new HashMap<Integer, Sample>();
		  if (platformUnit.getSampleSource() == null)
			  return indexedCells;
		  
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sampleId", platformUnit.getId());
		  
		  for (SampleSource ss : getSampleSourceDao().findByMap(q)){
			  Sample cell = ss.getSourceSample();
			  Integer index = ss.getIndex();
			  if (!isCell(cell)){
				  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
			  }
			  indexedCells.put(index, cell);
		  }
		  return indexedCells;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Integer getNumberOfIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException{
		  Assert.assertParameterNotNull(platformUnit, "No platform unit provided");
		  Assert.assertParameterNotNullNotZero(platformUnit.getId(), "Invalid platform unit Provided");
		  Map<Integer, Sample> indexedCells = getIndexedCellsOnPlatformUnit(platformUnit);
		  return new Integer(indexedCells.size());
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void addCellToPlatformUnit(Sample platformUnit, Sample cell, Integer index) throws SampleTypeException, SampleIndexException{
		  Assert.assertParameterNotNull(platformUnit, "No platform unit provided");
		  Assert.assertParameterNotNullNotZero(platformUnit.getId(), "Invalid platform unit Provided");
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid cell Provided");
		  Assert.assertParameterNotNull(index, "No index provided");
		  if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			  throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		  }
		  if (!isCell(cell)){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  if (index < 1)
			  throw new SampleIndexException("index must be an integer >= 1");
		  Map<String, Integer> sampleSourceQuery = new HashMap<String, Integer>();
		  sampleSourceQuery.put("sampleId", platformUnit.getId());
		  sampleSourceQuery.put("index", index);
		  if (getSampleSourceDao().findByMap(sampleSourceQuery) != null)
			  throw new SampleIndexException("index '"+index+"' already assigned to a cell associated with this platform unit");
		  SampleSource sampleSource = new SampleSource();
		  sampleSource.setSample(platformUnit);
		  sampleSource.setSourceSample(cell);
		  sampleSource.setIndex(index);
		  getSampleSourceDao().persist(sampleSource);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getLibrariesOnCell(Sample cell) throws SampleTypeException{
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid cell Provided");
		  return getLibrariesOnCell(cell, null);
	  }
	  
	  /**
	   * Index is essentially a a wrapper around an int. It is required (instead of Integer) because
	   * Integer is immutable and so it's value passed as a parameter, when 'changed' within a method
	   * cannot be used as the modified version outside that method.
	   * @author asmclellan
	   *
	   */
	  protected class Index{
		  protected int index;
		  
		  public Index(){
			  index = 0;
		  }
		  
		  public void setValue(int index){
			  this.index = index;
		  }
		  
		  public int getValue(){
			  return this.index;
		  }
		  
		  public void increment(){
			  this.index++;
		  }
	  }
	  
	  /**
	   * Returns list of Samples on a cell. If control libraries are spiked in, these are also returned.
	   * Also takes a parameter maxIndex (can be null) which returns the maximum index sampleSource index value found
	   * @param cell
	   * @param maxIndex
	   * @return
	   * @throws SampleTypeException
	   */
	  protected List<Sample> getLibrariesOnCell(Sample cell, Index maxIndex) throws SampleTypeException{
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid cell Provided");
		  if (!isCell(cell)){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  List<Sample> libraries = new ArrayList<Sample>();
		  if (cell.getSampleSource() == null)
			  return libraries;
		  
		  //no longer used
		  //Map<String,Integer> q = new HashMap<String,Integer>();
		  //q.put("sampleId", cell.getId());
		  		  
		  for (SampleSource ss : this.getCellLibrariesForCell(cell)){ //this.getSampleSourceDao().findByMap(q)){
			  Sample library = ss.getSourceSample();
			  if (!this.isLibrary(library)){//controlLibraries are of type library // WHAT IS THIS DOING HERE??? && !library.getSampleType().getIName().equals("controlLibrarySample")){
				  throw new SampleTypeException("Expected 'library' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.: cellId: " + cell.getId().intValue() + " cellName = " + cell.getName() + " problem libraryId = " + library.getId().intValue() + " problem library name = " + library.getName() );
			  }
			  if (maxIndex != null && ss.getIndex() != null && ss.getIndex() > maxIndex.getValue())
				  maxIndex.setValue(ss.getIndex());
			  libraries.add(library);
		  }
		  return libraries;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getLibrariesOnCellWithoutControls(Sample cell) throws SampleTypeException{
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  List<Sample> filteredLibraryList = new ArrayList<Sample>();
		  for (Sample library : getLibrariesOnCell(cell)){
			  if (!library.getSampleSubtype().getIName().equals("controlLibrarySample"))
				  filteredLibraryList.add(library);
		  }
		  return filteredLibraryList;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getControlLibrariesOnCell(Sample cell) throws SampleTypeException{
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid cell Provided");
		   List<Sample> filteredLibraryList = new ArrayList<Sample>();
		  for (Sample library : getLibrariesOnCell(cell)){
			  if (library.getSampleSubtype().getIName().equals("controlLibrarySample"))
				  filteredLibraryList.add(library);
		  }
		  return filteredLibraryList;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  //DO NOT USE. this method calls setJobForLibraryOnCell(cell, library); which is not a good idea, as job is derived from library.getJob (not good if a sample is on two jobs)
	  //USE addLibraryToCell(Sample cell, Sample library, Float libConcInCellPicoM, Job job in) instead (see below)
	  public void addLibraryToCell(Sample cell, Sample library, Float libConcInCellPicoM) throws SampleTypeException, SampleException, SampleMultiplexException, MetadataException{
		  // TODO: Write test!!
		//DO NOT USE
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid cell Provided");
		  Assert.assertParameterNotNull(library, "No library provided");
		  Assert.assertParameterNotNullNotZero(library.getId(), "Invalid library Provided");
		  Assert.assertParameterNotNull(libConcInCellPicoM, "No lib conc provided");
		  if (!isCell(cell)){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  if (!this.isLibrary(library)){
			  throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		  }
		  /* 
			(1) identify the barcode sequence on the library being added. If problem then terminate. 
			(2) if the library being added has a barcode that is NONE, and the cell contains ANY OTHER LIBRARY, then terminate. 
			(3) identify barcode of libraries already on cell; if problem, terminate. Should also get their jobIds.
			(4) if the cell already has a library with a barcode of NONE, then terminate
			(5) if the library being added has a bardcode that is something other than NONE (meaning a real barcode sequence) AND if a library already on the cell has that same barcode, then terminate. 
			(6) do we want to maintain only a single jobId for a cell???
		   */

		  //case 1: identify the adaptor barcode for the library being added; it's barcode is either NONE (no multiplexing) or has some more interesting barcode sequence (for multiplexing, such as AACTG)
		  Adaptor adaptorOnLibraryBeingAdded = null;
		  try{
			  adaptorOnLibraryBeingAdded = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", library.getSampleMeta())));
		  } catch(NumberFormatException e){
			  throw new MetadataException("Cannot convert genericLibrary.adaptor meta result to Integer: "+e.getMessage());
		  }
		  
		  if(adaptorOnLibraryBeingAdded==null || adaptorOnLibraryBeingAdded.getId()==null){
			  throw new SampleException("No adaptor associated with library");
		  }
		  else if( adaptorOnLibraryBeingAdded.getBarcodesequence()==null || adaptorOnLibraryBeingAdded.getBarcodesequence().equals("") ){
			  throw new SampleException("Library adaptor has no barcode");
		  }
		  Index index = new Index();
		  List<Sample> libraries = this.getLibrariesOnCell(cell, index); 
		  index.increment();
		  String barcodeOnLibBeingAdded = new String(adaptorOnLibraryBeingAdded.getBarcodesequence());

		  //case 2: dispense with this easy check 
		  if( barcodeOnLibBeingAdded.equals("NONE") && libraries != null && libraries.size() > 0  ){ //case 2: the library being added has a barcode of "NONE" AND the cell to which user wants to add this library already contains one or more libraries (such action is prohibited)
			  throw new SampleMultiplexException("Cannot add more than one sample to cell if not multiplexed. Input library has barcode 'NONE'.");
		  }
		 
		  //cases 3, 4, 5, 6 
		  if (libraries != null) {
			  for (Sample libraryAlreadyOnCell: libraries) {
				  Adaptor adaptorOnCell = null;
				  try{
					  adaptorOnCell = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryAlreadyOnCell.getSampleMeta())));
				  } catch(NumberFormatException e){
					  throw new MetadataException("Library already on cell: Cannot convert genericLibrary.adaptor meta result to Integer: "+e.getMessage());
				  }
				  
				  if(adaptorOnCell==null || adaptorOnCell.getId()==null){
					  throw new SampleException("Library already on cell : No adaptor associated with library");
				  }
				  else if( adaptorOnCell.getBarcodesequence()==null || adaptorOnCell.getBarcodesequence().equals("") ){
					  throw new SampleException("Library already on cell: adaptor has no barcode");
				  } 
				  else if( adaptorOnCell.getBarcodesequence().equals("NONE")){ 
					  throw new SampleMultiplexException("Library already on cell: Cannot add more than one sample to cell if not multiplexed. Library has barcode 'NONE'");
				  }
				  else if(adaptorOnCell.getBarcodesequence().equals(barcodeOnLibBeingAdded)){
					  throw new SampleMultiplexException("Library already on cell: has same barcode as input library");
				  }
				  else{
					  // TODO: confirm library is really part of this jobId. For now do nothing. If Einstein, then terminate (cell restricted to libraries from single job)
				  }
			  }	
		  }

		  SampleSource newSampleSource = new SampleSource(); 
		  newSampleSource.setSample(cell);
		  newSampleSource.setSourceSample(library);
		  newSampleSource.setIndex(index.getValue());
		  newSampleSource = getSampleSourceDao().save(newSampleSource);//capture the new samplesourceid
		  
		  try{
			  setJobForLibraryOnCell(cell, library);
			  setLibraryOnCellConcentration(cell, library, libConcInCellPicoM);		  
		  } catch(Exception e){
			  logger.warn("Unable to set LibraryOnCell SampleSourceMeta");
		  }
		  
	  }
	  
	  @Override
	  public int getRequestedSampleCoverage(Sample sample){
		  Assert.assertParameterNotNull(sample, "No sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid sample Provided");
		  Map<String, Integer> jobCellFilter = new HashMap<String, Integer>();
		  jobCellFilter.put("jobId", sample.getJob().getId());
		  Map<String, Integer> sampleCellFilter = new HashMap<String, Integer>();
		  sampleCellFilter.put("sampleId", sample.getId());
		  int coverage = 0;
		  for (JobCellSelection jobCellSelection: jobCellSelectionDao.findByMap(jobCellFilter)){
			  sampleCellFilter.put("jobCellSelectionId", jobCellSelection.getId());
			  coverage += sampleJobCellSelectionDao.findByMap(sampleCellFilter).size();
		  }
		  return coverage;
	  }
	  
	  
	  @Override
	  public List<Sample> getCellsForLibrary(Sample library) throws SampleTypeException{
		  Assert.assertParameterNotNull(library, "No library provided");
		  Assert.assertParameterNotNullNotZero(library.getId(), "Invalid library Provided");
		  if (!isLibrary(library)){
			  throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		  }
		  List<Sample> cells = new ArrayList<Sample>();
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sourceSampleId", library.getId());
		  for (SampleSource ss : getSampleSourceDao().findByMap(q)){
			  if (ss.getSample() == null){
				  // may be null if library added from external source e.g. via CLI
				  logger.debug("cellLibrary with id=" + ss.getId() + " has no associated cells");
				  continue;
			  }
			  if (isCell(ss.getSample())) 
				  cells.add(ss.getSample());
		  }
		  return cells;
	  }
	  
	  @Override
	  public SampleDraft cloneSampleDraft(final SampleDraft sampleDraft){
		  Assert.assertParameterNotNull(sampleDraft, "No SampleDraft provided");
		  SampleDraft clone = new SampleDraft();
		  if (sampleDraft.getFileGroup() != null)
			  clone.setFile(sampleDraft.getFileGroup());
		  if (sampleDraft.getJobDraft() != null)
			  clone.setJobDraft(sampleDraft.getJobDraft());
		  if (sampleDraft.getLab() != null)
			  clone.setLab(sampleDraft.getLab());
		  if (sampleDraft.getName() != null)
			  clone.setName(sampleDraft.getName());
		  if (sampleDraft.getSampleSubtype() != null)
			  clone.setSampleSubtype(sampleDraft.getSampleSubtype());
		  if (sampleDraft.getSampleType() != null)
			  clone.setSampleType(sampleDraft.getSampleType());
		  clone.setSourceSampleId(sampleDraft.getSourceSampleId());
		  clone.setStatus(sampleDraft.getStatus());
		  if (sampleDraft.getUser() != null)
			  clone.setUser(sampleDraft.getUser());
		  if (sampleDraft.getSampleDraftMeta() != null){
			  List<SampleDraftMeta> clonedMeta = new ArrayList<SampleDraftMeta>();
			  for (SampleDraftMeta sdm: sampleDraft.getSampleDraftMeta()){
				  SampleDraftMeta sdmClone = new SampleDraftMeta();
				  sdmClone.setK(sdm.getK());
				  sdmClone.setV(sdm.getV());
				  sdmClone.setPosition(sdm.getPosition());
				  clonedMeta.add(sdmClone);
			  }
			  clone.setSampleDraftMeta(clonedMeta);
		  }
		  return clone;
	  }
	  

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Run getCurrentRunForPlatformUnit(Sample platformUnit) throws SampleTypeException{
		Assert.assertParameterNotNull(platformUnit, "No platform unit provided");
		Assert.assertParameterNotNullNotZero(platformUnit.getId(), "Invalid platform unit Provided");
		if (!isPlatformUnit(platformUnit))
			throw new SampleTypeException("sample is not a platfrom unit");
		for (Run run : platformUnit.getRun()){
			// return run if it has been started by has no record of completion
			if (run.getStarted() != null && run.getFinished() == null)
				return run;
		}
		return null;
	}
	

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBiomolecule(Sample sample){
		Assert.assertParameterNotNull(sample, "No sample provided");
		if (sample.getSampleType().getIName().equals("dna") || 
				sample.getSampleType().getIName().equals("rna") || 
				sample.getSampleType().getIName().equals("library") || 
				sample.getSampleType().getIName().equals("facilityLibrary") || 
				sample.getSampleType().getIName().equals("protein") 
				)
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBiomolecule(SampleDraft sampleDraft){
		Assert.assertParameterNotNull(sampleDraft, "No sampleDraft provided");
		if (sampleDraft.getSampleType().getIName().equals("dna") || 
				sampleDraft.getSampleType().getIName().equals("rna") || 
				sampleDraft.getSampleType().getIName().equals("library") || 
				sampleDraft.getSampleType().getIName().equals("facilityLibrary") || 
				sampleDraft.getSampleType().getIName().equals("protein") 
				)
			return true;
		return false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLibrary(SampleDraft sampleDraft){
		Assert.assertParameterNotNull(sampleDraft, "No sampleDraft provided");
		if (sampleDraft.getSampleType().getIName().equals("library") || sampleDraft.getSampleType().getIName().equals("facilityLibrary"))
			return true;
		return false;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLibrary(Sample sample){
		Assert.assertParameterNotNull(sample, "No sample provided");
		if (sample.getSampleType().getIName().equals("library") || sample.getSampleType().getIName().equals("facilityLibrary"))
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCellLibrary(SampleSource cellLibrary){
		Assert.assertParameterNotNull(cellLibrary, "No cellLibrary provided");
		return isLibrary(getLibrary(cellLibrary)) && isCell(getCell(cellLibrary));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDnaOrRna(SampleDraft sampleDraft) {
		Assert.assertParameterNotNull(sampleDraft, "No sampleDraft provided");
		if (sampleDraft.getSampleType().getIName().equals(sampleTypeDao.getSampleTypeByIName("dna").getIName()) || sampleDraft.getSampleType().getIName().equals(sampleTypeDao.getSampleTypeByIName("rna").getIName()))
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDnaOrRna(Sample sample) {
		Assert.assertParameterNotNull(sample, "No sample provided");
		if (sample.getSampleType().getIName().equals(sampleTypeDao.getSampleTypeByIName("dna").getIName()) || sample.getSampleType().getIName().equals(sampleTypeDao.getSampleTypeByIName("rna").getIName()))
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isBarcodeNameExisting(String barcodeName){
		Assert.assertParameterNotNull(barcodeName, "No barcodeName provided");
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("barcode", barcodeName);
		List<Barcode> barcodesWithThisName = barcodeDao.findByMap(filter);
		
		if(barcodesWithThisName != null && barcodesWithThisName.size() > 0){
			return true;
		}			
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SampleSubtype> getSampleSubtypesBySampleTypeIName(String sampleTypeIName) throws SampleTypeException{
		Assert.assertParameterNotNull(sampleTypeIName, "No sampleTypeIName provided");
		SampleType sampleType = sampleTypeDao.getSampleTypeByIName(sampleTypeIName);
		if(sampleType==null||sampleType.getId()==null||sampleType.getId().intValue()==0){
			throw new SampleTypeException("SampleType not found: iname = " + sampleTypeIName);
		}
		
		Map<String,String> filterMap = new HashMap<String,String>();
		filterMap.put("sampleType.iName", sampleType.getIName());
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("name");
		return sampleSubtypeDao.findByMapDistinctOrderBy(filterMap, null, orderByColumnNames, "asc");

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	 public SampleSubtype getSampleSubtypeById(Integer sampleSubtypeId){
		Assert.assertParameterNotNullNotZero(sampleSubtypeId, "No valid sampleSubtype provided");
		return sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleSubtypeId.intValue());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	 public boolean isSampleSubtypeWithSpecificSampleType(SampleSubtype sampleSubtype, String sampleTypeIName){
		Assert.assertParameterNotNull(sampleSubtype, "No sampleSubtype provided");
		if(sampleTypeIName==null || sampleSubtype.getSampleType()==null || sampleSubtype.getSampleType().getIName()==null){return false;} 
		return sampleTypeIName.equals(sampleSubtype.getSampleType().getIName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sample getSampleById(Integer sampleId){
		Assert.assertParameterNotNullNotZero(sampleId, "No valid sampleId provided");
		return sampleDao.getSampleBySampleId(sampleId.intValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSampleOfSpecificSampleType(Sample sample, String sampleTypeIName){
		Assert.assertParameterNotNull(sample, "No sample provided");
		if(sampleTypeIName==null || sample.getSampleType()==null || sample.getSampleType().getIName()==null){return false;} 
		return sampleTypeIName.equals(sample.getSampleType().getIName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSampleOfSpecificSampleSubtype(Sample sample, String sampleSubtypeIName){
		Assert.assertParameterNotNull(sample, "No sample provided");
		if(sampleSubtypeIName==null || sample.getSampleSubtype()==null || sample.getSampleSubtype().getIName()==null){return false;} 
		return sampleSubtypeIName.equals(sample.getSampleSubtype().getIName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSampleInDatabase(Sample sample){
		Assert.assertParameterNotNull(sample, "No sample provided");
		if (sample.getId() != null && sample.getId().intValue() > 0)
			return true;
		return false;
	}
	 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSampleIdInDatabase(Integer sampleId){
		if (sampleId == null || sampleId == 0)
			return false;
		return isSampleInDatabase(getSampleById(sampleId));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSampleIdValid(Sample sample){
		
		if(sample == null || sample.getId()==null || sample.getId().intValue() <= 0){return false;}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlatformUnit(Sample sample){
		Assert.assertParameterNotNull(sample, "No platform unit provided");
		if("platformunit".equals(sample.getSampleType().getIName()) && "platformunit".equals(sample.getSampleSubtype().getSampleType().getIName())){
			return true;
		}
		return false;		
	}
	
	@Override
	public boolean isControlLibrary(Sample library) {
		Assert.assertParameterNotNull(library, "No library provided");
		if (this.isLibrary(library) && !library.getSampleSubtype().getIName().equals("controlLibrarySample")) {
			return false;
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sample getPlatformUnit(Integer sampleId)  throws NumberFormatException, SampleException, SampleTypeException, SampleSubtypeException {
		
		if(sampleId==null){throw new NumberFormatException("SampleId is null");}
		
		Sample sample = sampleDao.getSampleBySampleId(sampleId.intValue());
		if(!isSampleIdValid(sample)){throw new SampleException("Sample with sampleId of " + sampleId.intValue() + " not in database.");}
		else if(!this.isSampleOfSpecificSampleType(sample, "platformunit")){throw new SampleTypeException("Sample with sampleId of " + sampleId.intValue() + " not of sampleType platformunit.");}
		else if(!this.isSampleSubtypeWithSpecificSampleType(sample.getSampleSubtype(), "platformunit")){throw new SampleSubtypeException("Sample with sampleId of " + sampleId.intValue() + " not of sampleSubtype platformunit.");}
		//could have used this.sampleIsPlatformUnit(sample) as well for the two lines immediately above
		
		return sample;		
	}
	
	public SampleSubtype getSampleSubtypeConfirmedForPlatformunit(Integer sampleSubtypeId) throws NumberFormatException, SampleSubtypeException{
		
		if(sampleSubtypeId==null){throw new NumberFormatException("SampleSubtypeId is null");}

		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleSubtypeId.intValue());
		if(sampleSubtype==null || sampleSubtype.getId()==null || sampleSubtype.getId().intValue() <= 0){throw new SampleSubtypeException("SampleSubtype with sampleSubtypeId of " + sampleSubtypeId.intValue() + " not in database.");}
		else if(!this.isSampleSubtypeWithSpecificSampleType(sampleSubtype, "platformunit")){throw new SampleSubtypeException("SampleSubtype with sampleSubtypeId of " + sampleSubtypeId.intValue() + " not of sampletype platformunit.");}
		return sampleSubtype;		
	}

	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getNumberOfCellsListForThisTypeOfPlatformUnit(SampleSubtype sampleSubtype) throws SampleTypeException, SampleSubtypeException{
		Assert.assertParameterNotNull(sampleSubtype, "No sampleSubtype provided");
		Assert.assertParameterNotNullNotZero(sampleSubtype.getId(), "Invalid SampleSubtype Provided");
		if(!isSampleSubtypeWithSpecificSampleType(sampleSubtype, "platformunit")){
			throw new SampleSubtypeException("SampleSubtype with Id of " + sampleSubtype.getId().toString() + " is not platformunit");
		}
		
		Integer maxCellNumber = null;
		Integer multiplicationFactor = null;
		List<Integer> numberOfCellsListForPlatformUnit = new ArrayList<Integer>();
		
		List<SampleSubtypeMeta> ssMetaList = sampleSubtype.getSampleSubtypeMeta();
		for(SampleSubtypeMeta ssm : ssMetaList){
			if( ssm.getK().indexOf("maxCellNumber") > -1 ){
				try{
					maxCellNumber = new Integer(ssm.getV()); //could throw NumberFormatException
				}catch(Exception e){throw new SampleSubtypeException("maxCellNumber value invalid for SampleSubtype with Id of " + sampleSubtype.getSampleTypeId().intValue());}
			}
			if( ssm.getK().indexOf("multiplicationFactor") > -1 ){
				try{
					multiplicationFactor = new Integer(ssm.getV());  //could throw NumberFormatException
				}catch(Exception e){throw new SampleSubtypeException("multiplicationFactor value invalid for SampleSubtype with Id of " + sampleSubtype.getSampleTypeId().intValue());}
			}				 
		}
		if(maxCellNumber==null){throw new SampleSubtypeException("maxCellNumber not found for SampleSubtype with Id of " + sampleSubtype.getSampleTypeId().intValue());}
		else if(maxCellNumber.intValue()<=0){throw new SampleSubtypeException("maxCellNumber value invalid for SampleSubtype with Id of " + sampleSubtype.getSampleTypeId().intValue());}
		
		numberOfCellsListForPlatformUnit.add(maxCellNumber);	
		
		if (multiplicationFactor != null && multiplicationFactor.intValue() > 1 && multiplicationFactor.intValue() <= maxCellNumber.intValue() ) {
			Integer cellNum = new Integer(maxCellNumber.intValue());			
			while (cellNum.intValue() >= multiplicationFactor.intValue()){
				cellNum = new Integer(cellNum.intValue()/multiplicationFactor.intValue());				
				numberOfCellsListForPlatformUnit.add(cellNum);						
			}
		}
		Collections.sort(numberOfCellsListForPlatformUnit);
		
		return numberOfCellsListForPlatformUnit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequestedReductionInCellNumberProhibited(Sample platformUnitInDatabase, Integer numberOfCellsRequested) throws SampleException, SampleTypeException{
		Assert.assertParameterNotNull(platformUnitInDatabase, "No platform unit provided");
		Assert.assertParameterNotNullNotZero(platformUnitInDatabase.getId(), "Invalid platform unit Provided");
		Assert.assertParameterNotNullNotZero(numberOfCellsRequested, "Invalid numberOfcellsRequested value provided");
		Map<Integer,Sample> indexedCellMap = this.getIndexedCellsOnPlatformUnit(platformUnitInDatabase);//throws exception	
		Integer numberOfCellsInDatabase = indexedCellMap.size();
		if(numberOfCellsInDatabase.intValue() <= numberOfCellsRequested.intValue()){//no loss of cells, so return false, as action not prohibited
			return false;
		}
		
		//user asking to reduce number of cells
		//must check for presence of libraries on those cells that user seems to want to remove. If any found, return true.
		for(int i = numberOfCellsRequested.intValue() + 1; i <= numberOfCellsInDatabase.intValue(); i++){
			Integer index = new Integer(i);
			Sample cell = indexedCellMap.get(index);
			if(cell == null){
				//unexpected problem; indexes not ordered
				throw new SampleException("No cell found for platformUnitId " + platformUnitInDatabase.getId().intValue() + " and cell index " + i);
			}
			List<Sample> libraryList = null;
			libraryList = this.getLibrariesOnCell(cell);//throws exception
			if(libraryList!=null && libraryList.size()>0){//found at least one library
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createUpdatePlatformUnit(Sample platformUnit, SampleSubtype sampleSubtype, String barcodeName, Integer numberOfCellsRequested, List<SampleMeta> sampleMetaList) throws SampleException, SampleTypeException, SampleSubtypeException{
		Assert.assertParameterNotNull(platformUnit, "No platformUnit provided");
		Assert.assertParameterNotNull(sampleSubtype, "No sampleSubtype provided");
		Assert.assertParameterNotNullNotZero(sampleSubtype.getId(), "Invalid sampleSubtype Provided");
		Assert.assertParameterNotNull(barcodeName, "No barcodeName provided");
		Assert.assertParameterNotNullNotZero(numberOfCellsRequested, "Invalid numberOfCellsRequested value provided");
		Assert.assertParameterNotNull(sampleMetaList, "No sampleMetaList provided");
		String action = new String("create");
		Sample pu = null;
		SampleType sampleTypeForPlatformUnit = null;
		SampleType sampleTypeForCell = null;
		Integer numberOfCellsInDatabase = null;
		boolean platformUnitNameHasBeenChanged = false;
		
		if(isSampleInDatabase(platformUnit)){//this is an update of an existing record
			pu = platformUnit;
			if(!isPlatformUnit(platformUnit)){
				throw new SampleException("Sample with Id of " + pu.getId().toString() + " unexpectedly NOT a platformUnit either in sampletype or samplesubtype");
			}
			
			//check this first, since it could throw an exception, so no need to proceed with the update unless this is OK
			numberOfCellsInDatabase = this.getNumberOfIndexedCellsOnPlatformUnit(pu);
			if(numberOfCellsInDatabase==null || numberOfCellsInDatabase.intValue()<=0){//should never be 0 cells on a platformunit
				throw new SampleException("cellcount in database is not valid for platformunit with Id " + pu.getId().intValue());
			}	
			
			//if update, determine whether platformunit name has been changed
			if(barcodeName != null && !barcodeName.equals(platformUnit.getName())){
				platformUnitNameHasBeenChanged = true;
			}
			

			action = new String("update");			
		}
		else{//request for a new platformunit record 
			numberOfCellsInDatabase = new Integer(0);
			pu = new Sample();
		}
		
		if(!this.isSampleSubtypeWithSpecificSampleType(sampleSubtype, "platformunit")){
			throw new SampleSubtypeException("SampleSubtype with ID of " + sampleSubtype.getId().toString() + " is unexpectedly not SampleType of platformunit");								
		}
		sampleTypeForPlatformUnit = sampleTypeDao.getSampleTypeByIName("platformunit");
		if(sampleTypeForPlatformUnit==null || sampleTypeForPlatformUnit.getId()==null || sampleTypeForPlatformUnit.getId().intValue()<=0){
			throw new SampleTypeException("SampleType of type platformunit unexpectedly not found");
		}
		sampleTypeForCell = sampleTypeDao.getSampleTypeByIName("cell");
		if(sampleTypeForCell==null || sampleTypeForCell.getId()==null || sampleTypeForCell.getId().intValue()<=0){
			throw new SampleTypeException("SampleType of type cell unexpectedly not found");
		}
		
		if(numberOfCellsRequested == null || numberOfCellsRequested.intValue() <= 0){
			throw new SampleException("Number of cells requested not valid value");
		}
		else{//confirm numberOfCellsRequested is a valid value for this subtype of platformUnit
			List<Integer> numberOfCellsList = this.getNumberOfCellsListForThisTypeOfPlatformUnit(sampleSubtype);
			boolean foundIt = false;
			for(Integer numberOfCellsAllowed : numberOfCellsList){
				if(numberOfCellsAllowed.intValue()==numberOfCellsRequested.intValue()){
					foundIt = true;
					break;
				}
			}
			if(!foundIt){
				throw new SampleException("Number of cells requested is not compatible with the requested sampleSubtype");
			}
		}
		
		if(numberOfCellsRequested.intValue() >= numberOfCellsInDatabase.intValue()){//request to add cells or no change in cell number, so not a problem
			;
		}
		else if(numberOfCellsRequested.intValue() < numberOfCellsInDatabase.intValue()){//request to remove cells; a potential problem if libraries are on the cells to be removed
			// perform next test
			if(this.isRequestedReductionInCellNumberProhibited(pu, numberOfCellsRequested)){//value of true means libraries are assigned to those cells being asked to be removed. Prohibit this action and inform user to first remove those libraries from the cells being requested to be removed
				throw new SampleException("Sample Exception during platform unit update: Action not permitted at this time. To reduce the number of cells, remove libraries on the cells that will be lost.");
			}
		}
		
		
		if(barcodeName == null || "".equalsIgnoreCase(barcodeName)){
			throw new SampleException("Barcode Name cannot be empty");
		}
		else if(this.isBarcodeNameExisting(barcodeName)){
			if("create".equals(action) /* this is new platformUnit, and barcode name already used, so prevent */ 
					|| ( "update".equals(action) && !barcodeName.equalsIgnoreCase(pu.getSampleBarcode().get(0).getBarcode().getBarcode()) /* existing record being updated, but barcode name used is not my barcode name, so prevent */  ) ){
				throw new SampleException("Barcode Name used by another sample");
			}
		}
		
		try{	
			pu.setName(barcodeName);//sample.name will be set to the barcode name; as per Andy 9-28-12
	
			User me = authenticationService.getAuthenticatedUser();
			pu.setSubmitterUserId(me.getId());
					
			pu.setSampleSubtypeId(sampleSubtype.getId());//sampleSubtype is a parameter
	
			if(action.equals("create")){//new record
				pu.setSampleTypeId(sampleTypeForPlatformUnit.getId());
				pu.setSubmitterLabId(1);//Ed
				pu.setReceiverUserId(platformUnit.getSubmitterUserId());//Ed
				pu.setReceiveDts(new Date());//Ed
				pu.setIsReceived(1);//Ed
				pu.setIsActive(1);//Ed
				pu.setIsGood(1);//Ed
			}
			Sample platformUnitDb = sampleDao.save(pu);
			if(platformUnitDb==null || platformUnitDb.getId()==null || platformUnitDb.getId().intValue()<=0){
				throw new SampleException("new platform unit unexpectedly not saved");
			}
			sampleMetaDao.setMeta(sampleMetaList, platformUnitDb.getId()); // persist the metadata; no way to check as this returns void
		
			//barcode
			List<SampleBarcode> sampleBarcodeList = platformUnitDb.getSampleBarcode();//any barcodes exist for this platform unit?
			if(sampleBarcodeList != null && sampleBarcodeList.size() > 0){//this is an update
				SampleBarcode sampleBarcode = sampleBarcodeList.get(0);
				Barcode existingBarcode = sampleBarcode.getBarcode();
				existingBarcode.setBarcode(barcodeName);//update the barcodeName
				Barcode barcodeDb = this.barcodeDao.save(existingBarcode);
				if(barcodeDb==null || barcodeDb.getId()==null || barcodeDb.getId().intValue()<=0){
					throw new SampleException("updated barcode unexpectedly not saved");
				}
			}
			else{//new barcode for a new platformunit
				Barcode barcodeObject = new Barcode();		
				barcodeObject.setBarcode(barcodeName);
				barcodeObject.setBarcodefor("WASP");
				barcodeObject.setIsActive(new Integer(1));
				Barcode barcodeDb = this.barcodeDao.save(barcodeObject);//save new barcode in db
				if(barcodeDb==null || barcodeDb.getId()==null || barcodeDb.getId().intValue()<=0){
					throw new SampleException("updated barcode unexpectedly not saved");
				}
				SampleBarcode sampleBarcode = new SampleBarcode();	
				sampleBarcode.setBarcodeId(barcodeDb.getId()); // set new barcodeId in samplebarcode
				sampleBarcode.setSampleId(platformUnitDb.getId());
				SampleBarcode sampleBarcodeDb = this.sampleBarcodeDao.save(sampleBarcode);
				if(sampleBarcodeDb==null || sampleBarcodeDb.getId()==null || sampleBarcodeDb.getId().intValue()<=0){
					throw new SampleException("new samplebarcode in update area unexpectedly not saved");
				}
			}
		
			//The name of the platformunit (such as 102A4; which is the barcode name) is also part of the name of the cell (102A4/1)
			//and if the name of the platformunit is being updated, then the names of the cells also have to be updated
			//Do this only if needed: action == update [since no cells yet exist] and the name has been changed (and cells exist - which they should).
			if(action.equals("update") && platformUnitNameHasBeenChanged){
				List<SampleSource> sampleSourceList = platformUnitDb.getSampleSource();
				if(sampleSourceList != null && sampleSourceList.size() > 0){//should be true for all updates
					for (SampleSource ss : sampleSourceList){
						Sample cell = ss.getSourceSample();
						if (!isCell(cell)){
							throw new SampleTypeException("Expected 'cell' while updating cell name but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
						}
						Integer index = ss.getIndex();
						cell.setName(barcodeName +"/"+(index.intValue()));
						Sample cellDb = this.sampleDao.save(cell);
						if(cellDb==null || cellDb.getId()==null || cell.getId().intValue() <= 0){
							throw new SampleException("updated cell unexpectedly not saved during update of cell name");
						}
					}
				}
			}
			
			//cells
			//if create new platformunit, or add/remove additional cells during and update
			if(numberOfCellsRequested.intValue() > numberOfCellsInDatabase.intValue()){//add cells; can be create new record (where numberOfCellsInDatabase = 0) or an update to add some more cells 
	
				for(int i = numberOfCellsInDatabase + 1; i <= numberOfCellsRequested; i++){//add additional cells
				
					Sample cell = new Sample();
					cell.setSubmitterLabId(platformUnitDb.getSubmitterLabId());
					cell.setSubmitterUserId(platformUnitDb.getSubmitterUserId());
					cell.setName(platformUnitDb.getName()+"/"+(i));
					cell.setSampleTypeId(sampleTypeForCell.getId());
					cell.setIsGood(1);
					cell.setIsActive(1);
					cell.setIsReceived(1);
					cell.setReceiverUserId(platformUnitDb.getSubmitterUserId());
					cell.setReceiveDts(new Date());
					Sample cellDb = this.sampleDao.save(cell);
					if(cellDb==null || cellDb.getId()==null || cell.getId().intValue() <= 0){
						throw new SampleException("new cell unexpectedly not saved during create or update of platformunit");
					}
	
					SampleSource sampleSource = new SampleSource();
					sampleSource.setSampleId(platformUnitDb.getId());
					sampleSource.setSourceSampleId(cellDb.getId());
					sampleSource.setIndex(i);
					SampleSource sampleSourceDb = this.getSampleSourceDao().save(sampleSource);
					if(sampleSourceDb==null || sampleSourceDb.getId()==null || sampleSourceDb.getId().intValue() <= 0){
						throw new SampleException("new samplesource unexpectedly not saved during create or update of platformunit");
					}
				}
			}
			else if(numberOfCellsRequested.intValue() < numberOfCellsInDatabase.intValue()){//update requests to remove cells; above we already confirmed that this will NOT result in loss of info  with this call (if(this.requestedReductionInCellNumberIsProhibited(pu, numberOfCellsRequested))  
				
				//get the list 
				//Map<Integer, Sample> indexedCellMap = sampleService.getIndexedCellsOnPlatformUnit(platformUnitInDatabase);
				for (SampleSource ss : platformUnitDb.getSampleSource()){
					Sample cell = ss.getSourceSample();
					if (!isCell(cell)){
						throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
					}
					Integer index = ss.getIndex();
					if(index.intValue() >= numberOfCellsRequested.intValue() + 1 && index.intValue() <= numberOfCellsInDatabase.intValue()){
						//check for present of any libraries on these cells (just as a final fail safe mechanism, as this was tested above)
						List<Sample> libraryList = null;
						libraryList = this.getLibrariesOnCell(cell);//throws exception
						if(libraryList!=null && libraryList.size()>0){//found at least one library
							throw new SampleException("Cell " + cell.getId().intValue() + "unexpectedly has " + libraryList.size() + " libraries on it. Unable to remove this cell");
						}
						//first deletes each pu-cell link and its meta (if any) AND THEN ALSO deletes the cell and the cell's meta (if any)
						this.deleteCellFromPlatformUnit(ss);
					}
				}
			}
			else if(numberOfCellsRequested.intValue() == numberOfCellsInDatabase.intValue()){//do nothing
				;
			}
	
		}catch (Exception e){	throw new RuntimeException(e.getMessage());	}
		
		return;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlatformUnit(Integer platformUnitId) throws NumberFormatException, SampleException, SampleTypeException, SampleSubtypeException{
		Assert.assertParameterNotNullNotZero(platformUnitId, "Invalid platformUnitId provided");
		try{
			Sample platformUnit = this.getPlatformUnit(platformUnitId);//throws exceptions if not valid pu
	
			for (SampleSource puCellLink : platformUnit.getSampleSource()){//represents pu-cell link
				Sample cell = puCellLink.getSourceSample();//cell is the cell
				if (!isCell(cell)){//confirm its a cell
					throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
				}
				for(SampleSource cellLibraryLink : cell.getSampleSource()){// each cellLibraryLink represents a cell-library link
		
					//deletes each cell-lib link, along with the links metadata (if any)
					//NOTE: the library itself, of course, remains untouched
					this.removeLibraryFromCellOfPlatformUnit(cellLibraryLink);
				}
				//first deletes each pu-cell link and its meta (if any) AND THEN ALSO deletes the cell and the cell's meta (if any)
				this.deleteCellFromPlatformUnit(puCellLink);
			}
			//deletes the platformunit itself and its meta (if any) and its barcode/samplebarcode
			this.deletePlatformUnit(platformUnit);
			
		}catch (Exception e){	throw new RuntimeException(e.getMessage());	}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Resource> getAllMassivelyParallelSequencingMachines(){
		
		Map<String,String> filterMap = new HashMap<String,String>();
		filterMap.put("resourceType.iName", "mps");
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("name");
		return resourceDao.findByMapDistinctOrderBy(filterMap, null, orderByColumnNames, "asc");	
	}
	 
	/**
	 * Gets list of all massively-parallel sequencing machines compatible with platformUnit (actually compatible with the platformUnit's sampleSubtype)
	 * @param Sample platformUnit
	 * @return List<Resource>
	*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Resource> getSequencingMachinesCompatibleWithPU(Sample platformUnit) throws SampleException{
		Assert.assertParameterNotNull(platformUnit, "Invalid platformUnit provided");
		Assert.assertParameterNotNullNotZero(platformUnit.getId(), "Invalid platformUnit provided");
		if(!this.isPlatformUnit(platformUnit)){
			throw new SampleException("Expected platformUnit for SampleId " + platformUnit.getId().intValue() + " but sample failed in sampletype and/or samplesubtype");
		}
		
		List<Resource> resources = this.getAllMassivelyParallelSequencingMachines();
		Set<Resource> filteredResourceSet = new LinkedHashSet();//use set to make list distinct
		for(Resource resource : resources){
			for(SampleSubtypeResourceCategory ssrc : resource.getResourceCategory().getSampleSubtypeResourceCategory()){
				if(ssrc.getSampleSubtypeId().intValue() == platformUnit.getSampleSubtypeId().intValue()){
					filteredResourceSet.add(resource);				
				}
			}
		}
		return new ArrayList(filteredResourceSet);
	  }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource getSequencingMachineByResourceId(Integer resourceId) throws ResourceException{
		Assert.assertParameterNotNullNotZero(resourceId, "Invalid resourceId provided");
		Resource resource = resourceDao.getResourceByResourceId(resourceId);
		if(resource==null || resource.getId()==null || resource.getId().intValue() <= 0){
			throw new ResourceException("Resource of Id " + resourceId.intValue() + " does NOT exist in database");
		}
		else if( !"mps".equals(resource.getResourceType().getIName()) ){
			throw new ResourceException("Resource of Id " + resourceId.intValue() + " is not a massively parallel sequening machine through its resourcetype");
		}
		else if( !"mps".equals(resource.getResourceCategory().getResourceType().getIName()) ){
			throw new ResourceException("Resource of Id " + resourceId.intValue() + " is not a massively parallel sequening machine through its resourcecategory");
		}
		return resource;
		
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlatformUnitCompatibleWithSequencingMachine(Sample platformUnit, Resource sequencingMachineInstance){
		Assert.assertParameterNotNull(platformUnit, "Invalid platformUnit provided");
		Assert.assertParameterNotNull(sequencingMachineInstance, "Invalid sequencingMachineInstance provided");
		SampleSubtype sampleSubtypeOnPlatformUnit = platformUnit.getSampleSubtype();
		for(SampleSubtypeResourceCategory ssrc : sequencingMachineInstance.getResourceCategory().getSampleSubtypeResourceCategory()){
			if(ssrc.getSampleSubtype().getId().intValue()==sampleSubtypeOnPlatformUnit.getId().intValue()){
				return true;
			}
		}
		return false;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLibraryFromCellOfPlatformUnit(SampleSource cellLibraryLink) throws SampleTypeException{
		Assert.assertParameterNotNull(cellLibraryLink, "Invalid cellLibraryLink provided");
		Assert.assertParameterNotNullNotZero(cellLibraryLink.getId(), "Invalid cellLibraryLink provided");
		if (!isLibrary(cellLibraryLink.getSourceSample())){
			throw new SampleTypeException("Expected 'library' but got Sample of type '" + cellLibraryLink.getSourceSample().getSampleType().getIName() + "' instead.");
		}
		if (!cellLibraryLink.getSample().getSampleType().getIName().equals("cell")){
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cellLibraryLink.getSample().getSampleType().getIName() + "' instead.");
		}
		this.deleteSampleSourceAndItsMeta(cellLibraryLink);//currently the cellLibraryLink meta represents the pM applied and the jobId
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLibraryFromCellOfPlatformUnit(Sample cell, Sample library) throws SampleTypeException, SampleParentChildException {
		Assert.assertParameterNotNull(cell, "No Cell provided");
		Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid Cell Provided");
		Assert.assertParameterNotNull(library, "No Library provided");
		Assert.assertParameterNotNullNotZero(library.getId(), "Invalid Library Provided");
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		if (!isLibrary(library))
			throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		removeLibraryFromCellOfPlatformUnit(getCellLibrary(cell, library));
	}
	
	protected void deleteCellFromPlatformUnit(SampleSource puCellLink)throws SampleTypeException{
		Assert.assertParameterNotNull(puCellLink, "Invalid puCellLink provided");
		Assert.assertParameterNotNullNotZero(puCellLink.getId(), "Invalid puCellLink provided");
		Sample cell = puCellLink.getSourceSample();//cell is the cell
		if (!isCell(cell)){
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		}
		if(!puCellLink.getSample().getSampleType().getIName().equals("platformunit")){
			throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + puCellLink.getSample().getSampleType().getIName() + "' instead.");			
		}
		this.deleteSampleSourceAndItsMeta(puCellLink);//first, remove the samplesource link (currently this is no meta here, but if in the future there is, it will be taken care of automatically)
		this.deleteSampleAndItsMeta(cell);//second, remove the cell itself (currently this is no meta here, but if in the future there is, it will be taken care of automatically)
	}
	
	protected void deletePlatformUnit(Sample platformUnit)throws SampleTypeException{
		Assert.assertParameterNotNull(platformUnit, "Invalid platformUnit provided");
		Assert.assertParameterNotNullNotZero(platformUnit.getId(), "Invalid platformUnit provided");
		if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		}
		this.deleteSampleBarcode(platformUnit);
		this.deleteSampleAndItsMeta(platformUnit);//currently, meta includes readLength, readType, comments
	}
	protected void deleteSampleBarcode(Sample sample){
		Assert.assertParameterNotNull(sample, "Invalid platformUnit provided");
		List<SampleBarcode> sampleBarcodeList = sample.getSampleBarcode();
		for(SampleBarcode sampleBarcode : sampleBarcodeList){
			Barcode barcode = sampleBarcode.getBarcode();
			sampleBarcodeDao.remove(sampleBarcode);
			sampleBarcodeDao.flush(sampleBarcode);
			barcodeDao.remove(barcode);
			barcodeDao.flush(barcode);
		}
	}
	protected void deleteSampleSourceAndItsMeta(SampleSource sampleSource){
		Assert.assertParameterNotNull(sampleSource, "Invalid sampleSource provided");
		Assert.assertParameterNotNullNotZero(sampleSource.getId(), "Invalid sampleSource provided");
		for(SampleSourceMeta meta : sampleSource.getSampleSourceMeta()){
			sampleSourceMetaDao.remove(meta);
			sampleSourceMetaDao.flush(meta);
		}
		getSampleSourceDao().remove(sampleSource);
		getSampleSourceDao().flush(sampleSource);
	}
	protected void deleteSampleAndItsMeta(Sample sample){
		Assert.assertParameterNotNull(sample, "Invalid sample provided");
		Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid sample provided");
		for(SampleMeta meta : sample.getSampleMeta()){
			sampleMetaDao.remove(meta);
			sampleMetaDao.flush(meta);
		}
		sampleDao.remove(sample);
		sampleDao.flush(sample);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLibraryAwaitingPlatformUnitPlacement(Sample library) throws SampleTypeException{
		Assert.assertParameterNotNull(library, "No Sample provided");
		Assert.assertParameterNotNullNotZero(library.getId(), "Invalid Sample Provided");
		if (!isLibrary(library)){
			throw new SampleTypeException("Expected a library but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		}
		if (!isLibraryPassQC(library))
			return false;
		int sampleActualCoverage = 0;
		Set<Sample> platformUnitsToConsider = new LinkedHashSet<Sample>();
		platformUnitsToConsider.addAll(getPlatformUnitsNotYetRun());
		platformUnitsToConsider.addAll(getRunningOrSuccessfullyRunPlatformUnits());
		try{
			for (Sample cell : getCellsForLibrary(library)){
				if (platformUnitsToConsider.contains(getPlatformUnitForCell(cell)))
					sampleActualCoverage++;
			}
		} catch(Exception e){
			logger.warn(e.getLocalizedMessage());
		}
		int requestedCoverage = 0;
		if (library.getParent() != null)
			requestedCoverage = getRequestedSampleCoverage(library.getParent());
		else
			requestedCoverage = getRequestedSampleCoverage(library);
		logger.debug("Library id=" + library.getId() + ", name=" + library.getName() + " has requested coverage=" + requestedCoverage + " and actual coverage=" + sampleActualCoverage);
		if (sampleActualCoverage < requestedCoverage)
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getPlatformUnitsNotYetRun(){
		List<Sample> platformUnitsNotYetRun = new ArrayList<Sample>();
		for (Sample pu : getPlatformUnits()){
			try {
				if (getCurrentRunForPlatformUnit(pu) == null)
					platformUnitsNotYetRun.add(pu);
			} catch (SampleTypeException e) {
				logger.warn(e.getLocalizedMessage());
			}
		}
		return platformUnitsNotYetRun;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlatformUnitAwaitingSequenceRunPlacement(Sample platformUnit) throws SampleTypeException{
		Assert.assertParameterNotNull(platformUnit, "No platformUnit provided");
		Assert.assertParameterNotNullNotZero(platformUnit.getId(), "Invalid platformUnit Provided");
		if (!isPlatformUnit(platformUnit)){
			throw new SampleTypeException("Expected a platform unit but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		}
		return (getRunningOrSuccessfullyRunPlatformUnits().contains(platformUnit)) ? false : true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getRunningOrSuccessfullyRunPlatformUnits(){
		List<Sample> runningOrSuccessfullyRunPlatformUnits = new ArrayList<Sample>(); 
		for (Run run: runService.getSuccessfullyCompletedRuns())
			runningOrSuccessfullyRunPlatformUnits.add(run.getPlatformUnit());
		for (Run run: runService.getCurrentlyActiveRuns())
			runningOrSuccessfullyRunPlatformUnits.add(run.getPlatformUnit());
		return runningOrSuccessfullyRunPlatformUnits;
	}


	public enum LockStatus{LOCKED,UNLOCKED,UNKOWN}
	
	/**
	 * {@inheritDoc}
	 * @throws MetadataException 
	 */
	@Override
	public void setPlatformUnitLockStatus(Sample platformunit, LockStatus lockStatus) throws SampleTypeException, MetadataException{
		Assert.assertParameterNotNull(platformunit, "platformunit cannot be null");
		Assert.assertParameterNotNull(lockStatus, "lockStatus cannot be null");
		if (!isPlatformUnit(platformunit))
			throw new SampleTypeException("sample is not a platformunit");
		SampleMeta currentLockStatusMeta = new SampleMeta();
		currentLockStatusMeta.setK(LOCK_META_AREA + "." + LOCK_META_AREA);
		currentLockStatusMeta.setV(lockStatus.toString());
		currentLockStatusMeta.setSampleId(platformunit.getId());
		sampleMetaDao.setMeta(currentLockStatusMeta);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LockStatus getPlatformUnitLockStatus(Sample platformunit) throws SampleTypeException{
		Assert.assertParameterNotNull(platformunit, "platformunit cannot be null");
		if (!isPlatformUnit(platformunit))
			throw new SampleTypeException("sample is not a platformunit");
		LockStatus currentLockStatus = LockStatus.UNKOWN;
		List<SampleMeta> sampleMetaList = platformunit.getSampleMeta();
		if (sampleMetaList == null)
			sampleMetaList = new ArrayList<SampleMeta>();
		try{
			currentLockStatus = LockStatus.valueOf((String) MetaHelper.getMetaValue(LOCK_META_AREA, LOCK_META_KEY, sampleMetaList));
		} catch(MetadataException e) {
			// value not found
		}
		return currentLockStatus;
	}
		

	/**
	 * @return the sampleSourceDao
	 */
	@Override
	public SampleSourceDao getSampleSourceDao() {
		return sampleSourceDao;
	}

	/**
	 * @param sampleSourceDao the sampleSourceDao to set
	 */
	@Autowired
	public void setSampleSourceDao(SampleSourceDao sampleSourceDao) {
		this.sampleSourceDao = sampleSourceDao;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public void createFacilityLibraryFromMacro(Job job, SampleWrapper managedLibrary, List<SampleMeta> libraryMetaList){
		managedLibrary.updateMetaToList(libraryMetaList, sampleMetaDao);
		managedLibrary.saveAll(this);
		
		//add entry to jobsample table to link new library to job
		JobSample newJobSample = new JobSample();
		newJobSample.setJob(job);
		newJobSample.setSample(managedLibrary.getSampleObject());
		newJobSample = jobSampleDao.save(newJobSample);
		initiateBatchJobForLibrary(job, managedLibrary.getSampleObject(), "wasp.facilityLibrary.jobflow.v1");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSampleQCComment(Integer sampleId, String comment) throws Exception{
		try{
			metaMessageService.saveToGroup("sampleQCComment", "Sample QC Comment", comment, sampleId, SampleMeta.class, sampleMetaDao);
		}catch(Exception e){ throw new Exception(e.getMessage());}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MetaMessage> getSampleQCComments(Integer sampleId){
		return metaMessageService.read("sampleQCComment", sampleId, SampleMeta.class, sampleMetaDao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCellLibraryQCComment(Integer sampleSourceId, String comment) throws Exception{
		
		List<MetaMessage> existingMessages = metaMessageService.read(CellLibraryMeta.PREPROCESS_PASS_QC, "Cell Library QC Comment", sampleSourceId, SampleSourceMeta.class, sampleSourceMetaDao);
		if (existingMessages.isEmpty()){
			metaMessageService.saveToGroup(CellLibraryMeta.PREPROCESS_PASS_QC, "Cell Library QC Comment", comment, sampleSourceId, SampleSourceMeta.class, sampleSourceMetaDao);
		} else {
			metaMessageService.edit(existingMessages.get(0), comment, sampleSourceId, SampleSourceMeta.class, sampleSourceMetaDao);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MetaMessage> getCellLibraryQCComments(Integer sampleSourceId){
		return metaMessageService.read(CellLibraryMeta.PREPROCESS_PASS_QC, sampleSourceId, SampleSourceMeta.class, sampleSourceMetaDao);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateExistingSampleViaSampleWrapper(SampleWrapper sw, List<SampleMeta> sampleMetaList){
		sw.updateMetaToList(sampleMetaList, sampleMetaDao);
		sw.saveAll(this);
	}

	public boolean isCell(Sample cell){
		return cell.getSampleType().getIName().equals("cell");
	}
	

	
	
	public static final String LIBRARY_ON_CELL_AREA = "LibraryOnCell";
	public static final String LIB_CONC = "libConcInCellPicoM";
	public static final String JOB_ID = "jobId";
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void setLibraryOnCellConcentration(SampleSource cellLibrary, Float valueInPicoM) throws MetadataException{
		Assert.assertParameterNotNull(cellLibrary, "A valid cellLibrary must be provided");
		Assert.assertParameterNotNull(valueInPicoM, "a value to set must be provided");
		MetaHelper metahelper = new MetaHelper(LIBRARY_ON_CELL_AREA, SampleSourceMeta.class);
		metahelper.setMetaList(cellLibrary.getSampleSourceMeta());
		metahelper.setMetaValueByName(LIB_CONC, valueInPicoM.toString());
		List<SampleSourceMeta> meta = new ArrayList<SampleSourceMeta>();
		meta.add( (SampleSourceMeta) metahelper.getMetaByName(LIB_CONC) ); // may be new OR existing
		sampleSourceMetaDao.setMeta(meta, cellLibrary.getId());
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void setLibraryOnCellConcentration(Sample cell, Sample library, Float valueInPicoM) throws SampleTypeException, MetadataException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		if (!isLibrary(library))
			throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		setLibraryOnCellConcentration(getCellLibrary(cell, library), valueInPicoM);
		
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Float getConcentrationOfLibraryAddedToCell(Sample cell, Sample library) throws SampleException{
		SampleSource sampleSource = getCellLibrary(cell, library);
		if (sampleSource == null)
			throw new SampleException("no relationship between provided cell and library exists in the samplesource table");
		Float valueInPicoM = -0.0f; // some nonesense value
		List<SampleSourceMeta> ssMetaList = sampleSource.getSampleSourceMeta();
		if (ssMetaList == null)
			return valueInPicoM;
		try{
			valueInPicoM = Float.valueOf(MetaHelper.getMetaValue(LIBRARY_ON_CELL_AREA, LIB_CONC, ssMetaList));
		} catch(Exception e) {
			// value not found or not a sensible value
		}
		return valueInPicoM;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void setJobForLibraryOnCell(SampleSource cellLibrary) throws MetadataException{
		Job job = getLibrary(cellLibrary).getJob();//not good to use this; it assumes a sample is only on one job
		if (job == null){
			logger.debug("Not setting job for library on cell as library as no job associated with it (probably a control?)");
			return;
		}
		Assert.assertParameterNotNull(cellLibrary, "A valid cellLibrary must be provided");
		MetaHelper metahelper = new MetaHelper(LIBRARY_ON_CELL_AREA, SampleSourceMeta.class);
		metahelper.setMetaList(cellLibrary.getSampleSourceMeta());
		metahelper.setMetaValueByName(JOB_ID, job.getId().toString());
		List<SampleSourceMeta> meta = new ArrayList<SampleSourceMeta>();
		meta.add( (SampleSourceMeta) metahelper.getMetaByName(JOB_ID) ); // may be new OR existing
		sampleSourceMetaDao.setMeta(meta, cellLibrary.getId());
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void setJobForLibraryOnCell(Sample cell, Sample library) throws SampleTypeException, MetadataException{
		SampleSource cellLibrary = getCellLibrary(cell, library);//not good to use this; it assumes a sample is only on one job
		setJobForLibraryOnCell(cellLibrary);
	}
	
	/**
	 *  {@inheritDoc}
	 * @throws SampleException 
	 */
	@Override
	public Job getJobOfLibraryOnCell(Sample cell, Sample library) throws SampleException{
		SampleSource cellLibrary = getCellLibrary(cell, library);
		return getJobOfLibraryOnCell(cellLibrary);
	}
	
	/**
	 *  {@inheritDoc}
	 * @throws SampleException 
	 */
	@Override
	public Job getJobOfLibraryOnCell(SampleSource cellLibrary){
		Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be null");
		Assert.assertParameterNotNull(cellLibrary.getId(), "cellLibrary must have a valid id");
		Job job = null;
		List<SampleSourceMeta> ssMetaList = cellLibrary.getSampleSourceMeta();
		logger.debug("cellLibrary: " + cellLibrary.getId());
		if (ssMetaList == null) {
			logger.debug("sample source meta list is null");
			return job;
		}
		if (ssMetaList.size() == 0) {
			logger.debug("sample source meta list empty");
		} else {
			logger.debug(Arrays.toString(ssMetaList.toArray()));
		}
		try {
			job = jobDao.getJobByJobId(Integer.valueOf(MetaHelper.getMetaValue(LIBRARY_ON_CELL_AREA, JOB_ID, ssMetaList)));
			if (job.getId() == null) {
				logger.debug("Job has a null id");
				job = null;
			}
				
		} catch(Exception e) {
			// value not found or not a sensible value
		}
		return job;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Set<SampleSource> getCellLibrariesForJob(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		Set<SampleSource> cellLibraries = new LinkedHashSet<SampleSource>();
		Map<String, String> m = new HashMap<String, String>();
		m.put("k", LIBRARY_ON_CELL_AREA + "." + JOB_ID);
		m.put("v", job.getId().toString());
		for (SampleSourceMeta ssm: sampleSourceMetaDao.findByMap(m)){
			cellLibraries.add(ssm.getSampleSource());
		}
		return cellLibraries;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public SampleSource getCellLibrary(Sample cell, Sample library) throws SampleTypeException{
		if (!isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		if (!isLibrary(library))
			throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("sourceSampleId", library.getId());
		m.put("sampleId", cell.getId());
		List<SampleSource> ss = sampleSourceDao.findByMap(m);
		if (ss.isEmpty())
			return null;
		return ss.get(0); // should be one
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public SampleSource getCellLibraryBySampleSourceId(Integer ssid) throws SampleTypeException{
		SampleSource cellLibrary = sampleSourceDao.getById(ssid);
		Sample cell = this.getCell(cellLibrary);
		if (cell != null && !isCell(cell))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		Sample library = this.getLibrary(cellLibrary);
		if (!isLibrary(library))
			throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		return cellLibrary;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Sample getCell(SampleSource cellLibrary){
		Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be empty");
		Assert.assertParameterNotNull(cellLibrary.getId(), "cellLibrary must have a valid id");
		if (cellLibrary.getSampleId() == null)
			return null;
		return sampleDao.getSampleBySampleId(cellLibrary.getSampleId()); // get from Dao in case cellLibrary not entity managed
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Sample getLibrary(SampleSource cellLibrary){
		Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be empty");
		Assert.assertParameterNotNull(cellLibrary.getId(), "cellLibrary must have a valid id");
		return sampleDao.getSampleBySampleId(cellLibrary.getSourceSampleId()); // get from Dao in case cellLibrary not entity managed
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public List<Sample> getControlSamplesForAJobsSample(Job job, Sample sample){
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		Assert.assertParameterNotNullNotZero(sample.getId(), "Invalid Sample Provided");
		List<Sample> sampleList = new ArrayList<Sample>();
		String samplePairs = null;
		for(JobMeta jm : job.getJobMeta()){
			if(jm.getK().indexOf("samplePairs")>-1){
				samplePairs = jm.getV();
				break;
			}
		}
		if(samplePairs != null && !samplePairs.isEmpty()){
			for(String realPair : samplePairs.split(";")){
				String [] stringArray = realPair.split(":");
				if(!stringArray[0].isEmpty() && sample.getId().toString().equals(stringArray[0])){
					try{
						Sample sc = this.getSampleById(Integer.valueOf(stringArray[1]));
						if(sc.getId() != null){
							sampleList.add(sc);
						}
					}catch(Exception e){continue;}
					
				}
				else{
					continue;
				}
			}
		}
		return sampleList;
	}
	
	// statics 

	public static class CellSuccessMeta {
		public static final String AREA = "cell";
		public static final String RUN_SUCCESS = "run_success";
	}
		
	public static class CellLibraryMeta {
		public static final String AREA = "cellLibrary";
		public static final String PREPROCESS_PASS_QC = "preprocess_qc_pass";
	}
				
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public boolean isCellSequencedSuccessfully(Sample cell) throws SampleTypeException, MetaAttributeNotFoundException{
			if (!isCell(cell))
				throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
			String success = null;
			List<SampleMeta> sampleMetaList = cell.getSampleMeta();
			if (sampleMetaList == null)
				sampleMetaList = new ArrayList<SampleMeta>();
			try{
				success = (String) MetaHelper.getMetaValue(CellSuccessMeta.AREA, CellSuccessMeta.RUN_SUCCESS, sampleMetaList);
			} catch(MetadataException e) {
				throw new MetaAttributeNotFoundException("Samplesource meta attribute not found: " + CellSuccessMeta.AREA + "." + CellSuccessMeta.RUN_SUCCESS); // no value exists already
			}
			Boolean b = new Boolean(success);
			return b.booleanValue();
		}
		
		/**
		 *  {@inheritDoc}
		 * @throws MetadataException 
		 */
		@Override
		public void setCellSequencedSuccessfully(Sample cell, boolean success) throws SampleTypeException, MetadataException {
			if (!isCell(cell))
				throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
			Boolean b = new Boolean(success);
			String successString = b.toString();
			SampleMeta sampleMeta = new SampleMeta();
			sampleMeta.setK(CellSuccessMeta.AREA + "." + CellSuccessMeta.RUN_SUCCESS);
			sampleMeta.setV(successString);
			sampleMeta.setSampleId(cell.getId());
			sampleMetaDao.setMeta(sampleMeta);
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public ExitStatus getCellLibraryPreprocessingStatus(SampleSource cellLibrary) throws SampleTypeException{
			Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be null");
			Assert.assertParameterNotNull(cellLibrary.getId(), "sourceSampleId cannot be null");
			if (!jobService.getIsAnalysisSelected(getJobOfLibraryOnCell(cellLibrary)))
				return ExitStatus.NOOP;
			ExitStatus status = ExitStatus.UNKNOWN;
			Map<String, Set<String>> jobParameters = new HashMap<String, Set<String>>();
			Set<String> ssIdStringSet = new LinkedHashSet<String>();
			ssIdStringSet.add(cellLibrary.getId().toString());
			jobParameters.put(WaspJobParameters.CELL_LIBRARY_ID, ssIdStringSet);
			Set<String> jobTaskSet = new LinkedHashSet<String>();
			jobTaskSet.add(BatchJobTask.ANALYSIS_LIBRARY_PREPROCESS);
			jobParameters.put(WaspJobParameters.BATCH_JOB_TASK, jobTaskSet);
			JobExecution je = batchJobExplorer.getMostRecentlyStartedJobExecutionInList(batchJobExplorer.getJobExecutions(jobParameters, true));
			if (je != null)
				status = je.getExitStatus();
			return status;
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public ExitStatus getCellLibraryAggregationAnalysisStatus(SampleSource cellLibrary) throws SampleTypeException{
			Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be null");
			Assert.assertParameterNotNull(cellLibrary.getId(), "sourceSampleId cannot be null");
			if (!jobService.getIsAnalysisSelected(getJobOfLibraryOnCell(cellLibrary)))
				return ExitStatus.NOOP;
			ExitStatus status = ExitStatus.UNKNOWN;
			Map<String, Set<String>> jobParameters = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new LinkedHashSet<String>();
			jobIdStringSet.add(getJobOfLibraryOnCell(cellLibrary).getId().toString());
			jobParameters.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			Set<String> jobTaskSet = new LinkedHashSet<String>();
			jobTaskSet.add(BatchJobTask.ANALYSIS_AGGREGATE);
			jobParameters.put(WaspJobParameters.BATCH_JOB_TASK, jobTaskSet);
			JobExecution je = batchJobExplorer.getMostRecentlyStartedJobExecutionInList(batchJobExplorer.getJobExecutions(jobParameters, true));
			if (je != null)	
				status = je.getExitStatus();
			return status;
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public boolean isCellLibraryAwaitingQC(SampleSource cellLibrary) throws SampleTypeException{
			Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be null");
			Assert.assertParameterNotNull(cellLibrary.getId(), "sourceSampleId cannot be null");
			try{
				isCellLibraryPassedQC(cellLibrary);
			} catch (MetaAttributeNotFoundException e){
				// no value recorded yet
				if (getCellLibraryPreprocessingStatus(cellLibrary).isCompleted())
					return true;
			}
			return false;
		}
		
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public boolean isCellLibraryPassedQC(SampleSource cellLibrary) throws SampleTypeException, MetaAttributeNotFoundException{
			Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be null");
			Assert.assertParameterNotNull(cellLibrary.getId(), "sourceSampleId cannot be null");
			if (!this.isCellSequencedSuccessfully(this.getCell(cellLibrary)))
				return false;
			String isPassedQC = null;
			List<SampleSourceMeta> metaList = cellLibrary.getSampleSourceMeta();
			if (metaList == null)
				metaList = new ArrayList<SampleSourceMeta>();
			try{
				isPassedQC = (String) MetaHelper.getMetaValue(CellLibraryMeta.AREA, CellLibraryMeta.PREPROCESS_PASS_QC, metaList);
			} catch(MetadataException e) {
				throw new MetaAttributeNotFoundException("Samplesource meta attribute not found: " + CellLibraryMeta.AREA + "." + CellLibraryMeta.PREPROCESS_PASS_QC); // no value exists already
			}
			Boolean b = new Boolean(isPassedQC);
			return b.booleanValue();
		}
		
		/**
		 *  {@inheritDoc}
		 * @throws MetadataException 
		 */
		@Override
		public void setCellLibraryPassedQC(SampleSource cellLibrary, boolean isPassedQC) throws SampleTypeException, MetadataException {
			Assert.assertParameterNotNull(cellLibrary, "cellLibrary cannot be null");
			Assert.assertParameterNotNull(cellLibrary.getId(), "sourceSampleId cannot be null");
			Boolean b = new Boolean(isPassedQC);
			String isPreprocessedString = b.toString();
			SampleSourceMeta sampleSourceMeta = new SampleSourceMeta();
			sampleSourceMeta.setK(CellLibraryMeta.AREA + "." + CellLibraryMeta.PREPROCESS_PASS_QC);
			sampleSourceMeta.setV(isPreprocessedString);
			sampleSourceMeta.setSampleSourceId(cellLibrary.getId());
			sampleSourceMetaDao.setMeta(sampleSourceMeta);
		}



		/**
		 *  {@inheritDoc}
		 */
		@Override
		public List<SampleSource> getCellLibrariesThatPassedQCForJob(Job job) throws SampleTypeException{
			List<SampleSource> cellLibrariesThatPassedQC = new ArrayList<SampleSource>();
			for(SampleSource cellLibrary : this.getCellLibrariesForJob(job)){
				try{
					if(this.isCellLibraryPassedQC(cellLibrary))
						cellLibrariesThatPassedQC.add(cellLibrary);
				}catch(MetaAttributeNotFoundException e){
					logger.warn("recieved possibly anticipated MetaAttributeNotFoundException: " + e.getLocalizedMessage()); 				
				}
			}
			return cellLibrariesThatPassedQC;
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public List<SampleSource> getCellLibrariesPassQCAndNoAggregateAnalysis(Job job) throws SampleTypeException{
			List<SampleSource> cellLibrariesPassQCAndNoAggregateAnalysis = new ArrayList<SampleSource>();
			for(SampleSource cellLibrary : getCellLibrariesThatPassedQCForJob(job))
				cellLibrariesPassQCAndNoAggregateAnalysis.add(cellLibrary);
			return cellLibrariesPassQCAndNoAggregateAnalysis;			
		}



		public static final String SAMPLE_PAIR_AREA = "SamplePair";
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public void setJobByTestAndControlSamples(Sample testSample, Sample controlSample) throws SampleException, MetadataException{
			//Do not use. see warning within
			SampleSource sampleSource = getSamplePair(testSample, controlSample);
			if (sampleSource == null)
				throw new SampleException("no relationship between provided sample pair exists in the samplesource table");
			SampleSourceMeta sampleSourceMeta = new SampleSourceMeta();
			sampleSourceMeta.setK(SAMPLE_PAIR_AREA + "." + JOB_ID);
			sampleSourceMeta.setV(testSample.getJob().getId().toString());//WARNING: bad idea, since a sample can be on many jobs
			sampleSourceMeta.setSampleSourceId(sampleSource.getId());
			sampleSourceMetaDao.setMeta(sampleSourceMeta);
		}
		
		/**
		 *  {@inheritDoc}
		 * @throws SampleException 
		 */
		@Override
		public Job getJobByTestAndControlSamples(Sample testSample, Sample controlSample) throws SampleException{
			SampleSource samplePair = getSamplePair(testSample, controlSample);
			return getJobBySamplePair(samplePair);
		}
		
		
		
		/**
		 * @throws SampleException 
		 */
		@Override
		public Job getJobBySamplePair(SampleSource samplePair){
			Assert.assertParameterNotNull(samplePair, "Sample pair cannot be null");
			Assert.assertParameterNotNull(samplePair.getId(), "Sample pair must have a valid id");
			
			Job job = null;
			List<SampleSourceMeta> ssMetaList = samplePair.getSampleSourceMeta();
			if (ssMetaList == null)
				return job;
			try{
				job = jobDao.getJobByJobId(Integer.valueOf(MetaHelper.getMetaValue(SAMPLE_PAIR_AREA, JOB_ID, ssMetaList)));
				if (job.getId() == null)
					job = null;
			} catch(Exception e) {
				// value not found or not a sensible value
			}
			return job;
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public Set<SampleSource> getSamplePairsByJob(Job job){
			Assert.assertParameterNotNull(job, "job cannot be null");
			Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
			
			Set<SampleSource> samplePairs = new LinkedHashSet<SampleSource>();
			Map<String, String> m = new HashMap<String, String>();
			m.put("k", SAMPLE_PAIR_AREA + "." + JOB_ID);
			m.put("v", job.getId().toString());
			for (SampleSourceMeta ssm: sampleSourceMetaDao.findByMap(m)){
				samplePairs.add(ssm.getSampleSource());
			}
			return samplePairs;
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public SampleSource getSamplePair(Sample testSample, Sample controlSample) throws SampleTypeException{
//			if (!isCell(testSample))
//				throw new SampleTypeException("Expected 'cell' but got Sample of type '" + testSample.getSampleType().getIName() + "' instead.");
			if (!isLibrary(controlSample))
				throw new SampleTypeException("Expected 'library' but got Sample of type '" + controlSample.getSampleType().getIName() + "' instead.");

			Assert.assertParameterNotNull(testSample, "Test sample cannot be null");
			Assert.assertParameterNotNull(testSample.getId(), "Test sample id cannot be null");
			Assert.assertParameterNotNull(controlSample, "Control sample cannot be null");
			Assert.assertParameterNotNull(controlSample.getId(), "Control sample id cannot be null");
			
			Map<String, Integer> m = new HashMap<String, Integer>();
			m.put("sourceSampleId", controlSample.getId());
			m.put("sampleId", testSample.getId());
			List<SampleSource> ss = sampleSourceDao.findByMap(m);
			if (ss.isEmpty())
				return null;
			return ss.get(0); // should be one
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public Sample getTestSample(SampleSource samplePair){
			Assert.assertParameterNotNull(samplePair, "Sample pair cannot be empty");
			Assert.assertParameterNotNull(samplePair.getId(), "Sample pair must have a valid id");
			
			return sampleDao.getSampleBySampleId(samplePair.getSampleId()); // get from Dao in case sample pair not entity managed
		}
		
		/**
		 *  {@inheritDoc}
		 */
		@Override
		public Sample getControlSample(SampleSource samplePair){
			Assert.assertParameterNotNull(samplePair, "Sample pair cannot be empty");
			Assert.assertParameterNotNull(samplePair.getId(), "Sample pair must have a valid id");
			
			return sampleDao.getSampleBySampleId(samplePair.getSourceSampleId()); // get from Dao in case sample pair not entity managed
		}

		/**
		 *  {@inheritDoc}
		 */
		@Override
		public List<Sample> getControlSamplesByTestSample(Sample testSample){
			Assert.assertParameterNotNull(testSample, "Test sample cannot be empty");
			Assert.assertParameterNotNull(testSample.getId(), "Test sample must have a valid id");
			List<Sample> controlSamples = new ArrayList<Sample>();
			Map<String, Integer> m = new HashMap<String, Integer>();
			m.put("sampleId", testSample.getId());
			Sample controlSample = null;
			for (SampleSource ss : sampleSourceDao.findByMap(m)){
				controlSample = ss.getSourceSample();
				if (isDnaOrRna(controlSample) || isLibrary(controlSample))
					controlSamples.add(controlSample); 
			}
			return controlSamples;
		}

		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  public void createTestControlSamplePairsByIds(Integer testSampleId, Integer controlSampleId, Job job) throws SampleTypeException, SampleException, MetadataException {
			  Assert.assertParameterNotNull(testSampleId, "No test sample id provided");
			  Assert.assertParameterNotNull(controlSampleId, "No control sample id provided");
			  Assert.assertParameterNotNull(job, "Job cannot be null");
			  
			  Sample testSample = this.getSampleById(testSampleId);
			  Assert.assertParameterNotNull(testSample.getId(), "Test sample does not exist!");
			  Sample controlSample = this.getSampleById(controlSampleId);
			  Assert.assertParameterNotNull(controlSample.getId(), "Control sample does not exist!");

			  Integer jobId = job.getId();
			  Assert.assertParameterNotNull(jobId, "jobId cannot be null");
				 

			  /* this concept is specific for help-tag; DO NOT INCLUDE 
			  if (!this.isLibrary(controlSample)){
				  throw new SampleTypeException("Expected 'library' but got Sample of type '" + controlSample.getSampleType().getIName() + "' instead.");
			  }

			  */

			  SampleSource newSampleSource = new SampleSource(); 
			  newSampleSource.setSample(testSample);
			  newSampleSource.setSourceSample(controlSample);
			  newSampleSource.setIndex(null);
			  SampleSource newSampleSourceDB = getSampleSourceDao().save(newSampleSource);//capture the new samplesourceid
			  
			  SampleSourceMeta sampleSourceMeta = new SampleSourceMeta();
			  sampleSourceMeta.setK(SAMPLE_PAIR_AREA + "." + JOB_ID);
			  sampleSourceMeta.setV(jobId.toString());
			  sampleSourceMeta.setSampleSourceId(newSampleSourceDB.getId());
			  sampleSourceMetaDao.setMeta(sampleSourceMeta);
			  
			  /*
			  try{
				  this.setJobByTestAndControlSamples(testSample, controlSample);
			  } catch(Exception e){
				  logger.warn("Unable to set 'jobId' SampleSourceMeta for sample "+testSample.getName()+" and sample "+controlSample.getName());
			  }
			  */
		  }
		  
		  public Map<SampleSource, ExitStatus> getCellLibrariesWithPreprocessingStatus(Job job){
			  Map<SampleSource, ExitStatus> preprocessedCellLibraries = new HashMap<SampleSource, ExitStatus>();//preprocessed means sequenced and aligned
			  for (SampleSource cellLibrary: this.getCellLibrariesForJob(job)){
				  try{
					  ExitStatus preprocessingStatus = this.getCellLibraryPreprocessingStatus(cellLibrary);
					  preprocessedCellLibraries.put(cellLibrary, preprocessingStatus);
				  }
				  catch(SampleTypeException e){logger.warn("Expected sampletype of cellLibrary for SampleSource with Id of " + cellLibrary.getId()); 
				  }						  
			  }
			  return preprocessedCellLibraries;
		  }
		  
		 
		  
		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  public void saveCellLibraryQCStatusAndComment(SampleSource cellLibrary, String qcStatus, String comment){
			  try{
				  if(qcStatus.trim().equalsIgnoreCase(STATUS_PASSED)){
					  this.setCellLibraryPassedQC(cellLibrary, true);
				  }
				  else if(qcStatus.trim().equalsIgnoreCase(STATUS_FAILED)){
					  this.setCellLibraryPassedQC(cellLibrary, false);
				  }
				  
				  if( !comment.trim().isEmpty() ){
					  this.setCellLibraryQCComment(cellLibrary.getId(), comment.trim());
				  }
			  }
			  catch(Exception e){throw new RuntimeException(e.getMessage());}
		  }
		  
		  /**
		   * Gets the link to display platformunit
		   * @param platformunit
		   * @return
		   */
		  @Override
		  public String getPlatformunitViewLink(Sample platformunit) throws WaspException{
			  Assert.assertParameterNotNull(platformunit, "a platformunit must be supplied");
			  Assert.assertTrue(isPlatformUnit(platformunit), "sample is not a platformunit");
			  Set<SequencingViewProviding> plugins = new LinkedHashSet<>(); // use set so duplicates not added
			  for (ResourceCategory rc : resourceService.getAssignedResourceCategory(platformunit))
				  plugins.addAll(pluginRegistry.getPluginsHandlingArea(rc.getIName(), SequencingViewProviding.class));
			  // we expect one (and ONLY one) plugin to handle the platformunit otherwise we do not know which one to show so programming defensively:
			 if (plugins.size() > 1)
				  throw new WaspException("More than one SequencingViewProviding plugin found");
			  for (SequencingViewProviding plugin : plugins)
				  return plugin.getShowPlatformUnitViewLink(platformunit.getId()); // should only be one so this is ok
			  throw new WaspException("No SequencingViewProviding plugins found");
		  }

		@Override
		public List<SampleSource> getCellLibrariesForCell(Sample cell) {
			Assert.assertTrue(this.isCell(cell));
			return sampleSourceDao.getCellLibrariesForCell(cell);
		}
		
		@Override
		public List<SampleSource> getCellLibrariesForLibrary(Sample library) {
			Assert.assertTrue(this.isLibrary(library));
			return sampleSourceDao.getCellLibrariesForLibrary(library);
		}
		
		@Override
		public List<SampleSource> getCellLibraries() {
			List<SampleSource> cellLibraries = new ArrayList<>();
			for (Sample library : getLibraries())
				cellLibraries.addAll(getCellLibrariesForLibrary(library));
			return cellLibraries;
		}
		
		@Override
		public List<Sample> getLibraries() {
			return sampleDao.getActiveLibraries();
		}
		
		@Override
		public String getNameOfOrganism(Sample sample){
			return getNameOfOrganism(sample, "Unknown Organism");
		}
		
		@Override
		public String getNameOfOrganism(Sample sample, String defaultValue){
			final String ORGANISM_META_AREA = "genericBiomolecule";
			final String ORGANISM_META_KEY = "organism";
			Assert.assertParameterNotNull(sample, "sample cannot be null");
			Assert.assertParameterNotNull(defaultValue, "defaultValue cannot be null");
			String organismName = defaultValue; // default
			Integer genomeId = null;
			Sample tempSample = sample;
			while(true){
				try{	
					genomeId = Integer.parseInt(MetaHelper.getMetaValue(ORGANISM_META_AREA, ORGANISM_META_KEY, tempSample.getSampleMeta()));
					organismName = genomeService.getOrganismMap().get(genomeId).getName();
					return organismName;
				}
				catch(Exception me){
					if(tempSample.getParentId()!=null){
						tempSample = tempSample.getParent();
						continue;
					}
					logger.debug("Unable to identify organism for sampleId " + sample.getId() + " assuming it is of type 'Other'");
					organismName="Other";
					return organismName;
				}
			}
		}

		@Override
		public Integer getIdOfOrganism(Sample sample){
			final String ORGANISM_META_AREA = "genericBiomolecule";
			final String ORGANISM_META_KEY = "organism";
			Assert.assertParameterNotNull(sample, "sample cannot be null");
			Integer genomeId = new Integer(0);
			try{	
				genomeId = Integer.parseInt(MetaHelper.getMetaValue(ORGANISM_META_AREA, ORGANISM_META_KEY, this.getSampleMetaDao().getSamplesMetaBySampleId(sample.getId())));
			}
			catch(Exception me){
				logger.debug("Unable to identify organism for sampleId " + sample.getId() + " assuming it is of type 'Other'");
			}
			return genomeId;
		}
		
		@Override
		public Set<Organism> getOrganismsPlusOther(){
			Set<Organism> organisms = genomeService.getOrganisms();
			Organism other = new Organism(0);
			other.setCommonName("Other");
			other.setName("Other");
			other.setAlias("Other");
			organisms.add(other);
			return organisms;
		}
		
		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  public List<Sample> getCellsForLibrary(Sample library, Job job) throws SampleTypeException{
			  
			  List<Sample> cellsForLibraryAndJob = new ArrayList<Sample>();		  
			  Set<SampleSource> sampleSourceList = this.getCellLibrariesForJob(job);
			  for(SampleSource ss : sampleSourceList){
				  if (ss.getSample() == null){
					  // may be null if library added from external source e.g. via CLI
					  logger.debug("cellLibrary with id=" + ss.getId() + " has no associated cells");
					  continue;
				  }
				  if(ss.getSourceSample()==library){//here, sourcesample is library; sample is cell
					  cellsForLibraryAndJob.add(ss.getSample());//add cell to list
				  }
			  }			  
			  return cellsForLibraryAndJob;
		  }
		  
		  /**
		   * {@inheritDoc}
		   */
		  @Override
		  public void addLibraryToCell(Sample cell, Sample libraryToBeAddedToCell, Float libConcInCellPicoM, Job job) throws SampleTypeException, SampleException, SampleMultiplexException, MetadataException{
			  
			  //reworked 7-9-14 by dubin
			  
			  Assert.assertParameterNotNull(cell, "No cell provided");
			  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid cell Provided");
			  Assert.assertParameterNotNull(libraryToBeAddedToCell, "No library provided");
			  Assert.assertParameterNotNullNotZero(libraryToBeAddedToCell.getId(), "Invalid library Provided");
			  Assert.assertParameterNotNull(libConcInCellPicoM, "No lib conc provided");
			  Assert.assertParameterNotNull(job, "No job provided");
			  Assert.assertParameterNotNullNotZero(job.getId(), "Invalid job Provided");
			  if (!isCell(cell)){
				  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
			  }
			  if (!this.isLibrary(libraryToBeAddedToCell)){
				  throw new SampleTypeException("Expected 'library' but got Sample of type '" + libraryToBeAddedToCell.getSampleType().getIName() + "' instead.");
			  }
			  List<Sample> jobLibraryList = jobService.getLibraries(job);
			  if( !jobLibraryList.contains(libraryToBeAddedToCell) ){
				  throw new SampleException("Library (id = "+libraryToBeAddedToCell.getId()+") not part of specified job (id=" + job.getId()+")");
			  }
			  
			  //If the library being added has a valid barcode or if it has no barcode (empty barcode), only permit ONE library on the flowcell that has that barcode (a valid barcode or an empty (none) barcode).
			  Adaptor adaptorOnLibraryBeingAdded = null;
			  String barcodeOnLibraryBeingAdded = null;//this is the key variable being used
			  try{
				  adaptorOnLibraryBeingAdded = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryToBeAddedToCell.getSampleMeta())));
				  barcodeOnLibraryBeingAdded = adaptorOnLibraryBeingAdded.getBarcodesequence();	
				  if(barcodeOnLibraryBeingAdded==null){
					  barcodeOnLibraryBeingAdded = "";//no barcode on the control
				  }				  
			  }catch(Exception e){
				  //this is ok; no adaptor or index; some libraries may have no adaptor in the metadata, indicating no indexing
				  logger.debug("Cannot convert genericLibrary.adaptor meta result to Integer or cannot find the requested metadata: "+e.getMessage());
				  barcodeOnLibraryBeingAdded = "";//no barcode on the control
			  }
			  
			  Index index = new Index();
			  List<Sample> librariesAlreadyOnCell = this.getLibrariesOnCell(cell, index); //all Libraries, both job-related and control libraries
			  ////List<Sample> librariesAlreadyOnCell = this.getLibrariesOnCell(cell);//all Libraries, both job-related and control libraries
			  for(Sample libraryAlreadyOnCell : librariesAlreadyOnCell){
				  logger.debug("addLibraryToCell: Lib on cell: " + libraryAlreadyOnCell.getName());
				  Adaptor adaptorOnLibraryAlreadyOnCell = null;
				  String barcodeOnLibraryAlreadyOnCell = null;//this is the key variable being used
				  try{
					  adaptorOnLibraryAlreadyOnCell = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryAlreadyOnCell.getSampleMeta())));
					  barcodeOnLibraryAlreadyOnCell = adaptorOnLibraryAlreadyOnCell.getBarcodesequence();	
					  if(barcodeOnLibraryAlreadyOnCell==null){
						  barcodeOnLibraryAlreadyOnCell = "";//no barcode
					  }					  
				  }catch(Exception e){
					  //this is ok; no adaptor; some one library per lane may have no adaptor in the metadata or an adaptor with no barcode, indicating no indexing
					  logger.debug("Cannot convert genericLibrary.adaptor meta result to Integer or cannot find the requested metadata: "+e.getMessage());
					  barcodeOnLibraryAlreadyOnCell = "";//no barcode
				  }
				  if(barcodeOnLibraryBeingAdded.equalsIgnoreCase(barcodeOnLibraryAlreadyOnCell)){
					  throw new SampleMultiplexException("addLibraryToCell: You may not have two libraries with same barcode (which could be no barcode)");
				  }
			  }
			  
			  /* 
				////(1) identify the barcode sequence on the library being added. If problem then terminate. 
				////(2) if the library being added has a barcode that is NONE, and the cell contains ANY OTHER LIBRARY, then terminate. 
				////(3) identify barcode of libraries already on cell; if problem, terminate. Should also get their jobIds.
				////(4) if the cell already has a library with a barcode of NONE, then terminate
				////(5) if the library being added has a bardcode that is something other than NONE (meaning a real barcode sequence) AND if a library already on the cell has that same barcode, then terminate. 
				////(6) do we want to maintain only a single jobId for a cell???
			   

			  //case 1: identify the adaptor barcode for the library being added; it's barcode is either NONE (no multiplexing) or has some more interesting barcode sequence (for multiplexing, such as AACTG)
			  Adaptor adaptorOnLibraryBeingAdded = null;
			  try{
				  adaptorOnLibraryBeingAdded = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryToBeAddedToCell.getSampleMeta())));
			  } catch(NumberFormatException e){
				  throw new MetadataException("Cannot convert genericLibrary.adaptor meta result to Integer: "+e.getMessage());
			  }
			  
			  if(adaptorOnLibraryBeingAdded==null || adaptorOnLibraryBeingAdded.getId()==null){
				  throw new SampleException("No adaptor associated with library");
			  }
			  else if( adaptorOnLibraryBeingAdded.getBarcodesequence()==null || adaptorOnLibraryBeingAdded.getBarcodesequence().equals("") ){
				  throw new SampleException("Library adaptor has no barcode");
			  }
			  Index index = new Index();
			  List<Sample> libraries = this.getLibrariesOnCell(cell, index); 
			  index.increment();
			  String barcodeOnLibBeingAdded = new String(adaptorOnLibraryBeingAdded.getBarcodesequence());

			  //case 2: dispense with this easy check 
			  if( barcodeOnLibBeingAdded.equals("NONE") && libraries != null && libraries.size() > 0  ){ //case 2: the library being added has a barcode of "NONE" AND the cell to which user wants to add this library already contains one or more libraries (such action is prohibited)
				  throw new SampleMultiplexException("Cannot add more than one sample to cell if not multiplexed. Input library has barcode 'NONE'.");
			  }
			 
			  //cases 3, 4, 5, 6 
			  if (libraries != null) {
				  for (Sample libraryAlreadyOnCell: libraries) {
					  if(libraryAlreadyOnCell.getSampleSubtype().getIName().equals("controlLibrarySample")){
						  continue;
					  }
					  Adaptor adaptorOnCell = null;
					  try{
						  adaptorOnCell = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryAlreadyOnCell.getSampleMeta())));
					  } catch(NumberFormatException e){
						  throw new MetadataException("Library already on cell: Cannot convert genericLibrary.adaptor meta result to Integer: "+e.getMessage());
					  }
					  
					  if(adaptorOnCell==null || adaptorOnCell.getId()==null){
						  throw new SampleException("Library already on cell : No adaptor associated with library");
					  }
					  else if( adaptorOnCell.getBarcodesequence()==null || adaptorOnCell.getBarcodesequence().equals("") ){
						  throw new SampleException("Library already on cell: adaptor has no barcode");
					  } 
					  else if( adaptorOnCell.getBarcodesequence().equals("NONE")){ 
						  throw new SampleMultiplexException("Library already on cell: Cannot add more than one sample to cell if not multiplexed. Library has barcode 'NONE'");
					  }
					  else if(adaptorOnCell.getBarcodesequence().equals(barcodeOnLibBeingAdded)){
						  throw new SampleMultiplexException("Library already on cell: has same barcode as input library");
					  }
					  else{
						  // TODO: confirm library is really part of this jobId. For now do nothing. If Einstein, then terminate (cell restricted to libraries from single job)
					  }
				  }	
			  }
		*/
			  
			  
			  
			  
			  //all OK, so add new library
			  SampleSource newSampleSource = new SampleSource(); 
			  newSampleSource.setSample(cell);
			  newSampleSource.setSourceSample(libraryToBeAddedToCell);
			  newSampleSource.setIndex(index.getValue());
			  SampleSource newSampleSourceDB = getSampleSourceDao().save(newSampleSource);//capture the new samplesourceid
			  
			  try{
				  setJobForLibraryOnCell(newSampleSourceDB, job);
				  setLibraryOnCellConcentration(newSampleSourceDB, libConcInCellPicoM);		  
			  } catch(Exception e){
				  logger.warn("Unable to set LibraryOnCell SampleSourceMeta");
			  }
			  
		  }
		  
			/**
			 *  {@inheritDoc}
			 */
			@Override
			public void setJobForLibraryOnCell(SampleSource cellLibrary, Job job) throws MetadataException{
				Assert.assertParameterNotNull(job, "No job provided");
				Assert.assertParameterNotNullNotZero(job.getId(), "Invalid job Provided");
				Assert.assertParameterNotNull(cellLibrary, "A valid cellLibrary must be provided");
				Assert.assertParameterNotNullNotZero(cellLibrary.getId(), "Invalid cellLibrary Provided");

				MetaHelper metahelper = new MetaHelper(LIBRARY_ON_CELL_AREA, SampleSourceMeta.class);
				metahelper.setMetaList(cellLibrary.getSampleSourceMeta());
				metahelper.setMetaValueByName(JOB_ID, job.getId().toString());
				List<SampleSourceMeta> meta = new ArrayList<SampleSourceMeta>();
				meta.add( (SampleSourceMeta) metahelper.getMetaByName(JOB_ID) ); // may be new OR existing
				sampleSourceMetaDao.setMeta(meta, cellLibrary.getId());
			}
			
			/**
			 *  {@inheritDoc}
			 */
			@Override
			public void enumerateSamplesForMPS(List<Sample> allSamples, List<Sample> submittedMacromolecules, List<Sample> submittedLibraries, List<Sample> facilityLibraries){
				Assert.assertParameterNotNull(allSamples, "allSamples list cannot be null");
				Assert.assertParameterNotNull(submittedMacromolecules, "submittedMacromolecules list cannot be null");
				Assert.assertParameterNotNull(submittedLibraries, "submittedLibraries list cannot be null");
				Assert.assertParameterNotNull(facilityLibraries, "facilityLibraries list cannot be null");
				
				for(Sample s : allSamples){
					  if(s.getParent()==null){
						  if(s.getSampleType().getIName().toLowerCase().contains("library")){
							  submittedLibraries.add(s);
						  }
						  else if(s.getSampleType().getIName().toLowerCase().contains("dna") || s.getSampleType().getIName().toLowerCase().contains("rna")){
							  submittedMacromolecules.add(s);
						  }
					  }
					  else if(s.getParent()!=null && s.getSampleType().getIName().toLowerCase().contains("library")){
						  facilityLibraries.add(s);
					  }
				}
			}

			/**
			 *  {@inheritDoc}
			 */
			@Override
			 public void validateSampleNameUniqueWithinJob(String sampleName, Integer sampleId, Job job, BindingResult result){
				  //confirm that, if a new sample.name was supplied on the form, it is different from all other sample.name in this job
				  List<Sample> samplesInThisJob = job.getSample();
				  for(Sample eachSampleInThisJob : samplesInThisJob){
					  if(eachSampleInThisJob.getId().intValue() != sampleId.intValue()){
						  if( sampleName.equals(eachSampleInThisJob.getName()) ){
							  // adding an error to 'result object' linked to the 'name' field as the name chosen already exists
							  Errors errors=new BindException(result.getTarget(), "sample");
							  // reject value on the 'name' field with the message defined in sampleDetail.updated.nameClashError
							  // usage: errors.rejectValue(field, errorString, default errorString)
							  errors.rejectValue("name", "sampleDetail.nameClash.error", "sampleDetail.nameClash.error (no message has been defined for this property)");
							  result.addAllErrors(errors);
							  break;
						  }
					  }
				  }
			  }	
			
			
			@Override
			public String getNameOfOrganismForAlignmentRequest(Sample sample, String defaultValue){
				final String ORGANISM_META_AREA = "genome";
				final String ORGANISM_META_KEY = "genomeString";
				Assert.assertParameterNotNull(sample, "sample cannot be null");
				Assert.assertParameterNotNull(defaultValue, "defaultValue cannot be null");
				String organismName = defaultValue; // default
				try{	
					 String codedString = MetaHelper.getMetaValue(ORGANISM_META_AREA, ORGANISM_META_KEY, sample.getSampleMeta());
					 String array [] = codedString.split("::");
					 Integer genomeId = new Integer(array[0]);
					 organismName = genomeService.getOrganismMap().get(genomeId).getName();
				}
				catch(Exception me){
					logger.debug("Unable to identify organism alignment request for sampleId " + sample.getId() + " taking default");
				}
				return organismName;
			}
			
			@Override
			public String getNameOfGenomeForAlignmentRequest(Sample sample, String defaultValue){
				final String ORGANISM_META_AREA = "genome";
				final String ORGANISM_META_KEY = "genomeString";
				Assert.assertParameterNotNull(sample, "sample cannot be null");
				Assert.assertParameterNotNull(defaultValue, "defaultValue cannot be null");
				String genomeName = defaultValue; // default
				try{	
					 String codedString = MetaHelper.getMetaValue(ORGANISM_META_AREA, ORGANISM_META_KEY, sample.getSampleMeta());
					 String array [] = codedString.split("::");
					 genomeName = array[1];
				}
				catch(Exception me){
					logger.debug("Unable to identify genome alignment request for sampleId " + sample.getId() + " taking default");
				}
				return genomeName;
			}
			
			@Override
			public String getNameOfGenomeBuildForAlignmentRequest(Sample sample, String defaultValue){
				final String ORGANISM_META_AREA = "genome";
				final String ORGANISM_META_KEY = "genomeString";
				Assert.assertParameterNotNull(sample, "sample cannot be null");
				Assert.assertParameterNotNull(defaultValue, "defaultValue cannot be null");
				String genomeBuildName = defaultValue; // default
				try{	
					 String codedString = MetaHelper.getMetaValue(ORGANISM_META_AREA, ORGANISM_META_KEY, sample.getSampleMeta());
					 String array [] = codedString.split("::");
					 genomeBuildName = array[2];
				}
				catch(Exception me){
					logger.debug("Unable to identify genome build alignment request for sampleId " + sample.getId() + " taking default");
				}
				return genomeBuildName;
			}
			
			/**
			 *  {@inheritDoc}
			 */
			@Override
			public void setLibraryOnCellMeta(SampleSource cellLibrary, String area, String metaValueName, String metaValue) throws MetadataException{
				Assert.assertParameterNotNull(cellLibrary, "A valid cellLibrary must be provided");
				Assert.assertParameterNotNull(area, "an area must be provided");
				Assert.assertParameterNotNull(metaValueName, "a metaValueName must be provided");
				Assert.assertParameterNotNull(metaValue, "a metaValue must be provided");
				MetaHelper metahelper = new MetaHelper(area, SampleSourceMeta.class);
				metahelper.setMetaList(cellLibrary.getSampleSourceMeta());
				metahelper.setMetaValueByName(metaValueName, metaValue);
				List<SampleSourceMeta> meta = new ArrayList<SampleSourceMeta>();
				meta.add( (SampleSourceMeta) metahelper.getMetaByName(metaValueName) ); // may be new OR existing
				sampleSourceMetaDao.setMeta(meta, cellLibrary.getId());
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Transactional("entityManager")
			@Override
			public Map<Sample, List<SampleSource>> associateUppermostSampleWithCellLibraries(List<SampleSource> cellLibraryList){
				
				Map<Sample, List<SampleSource>> sampleCellLibraryListMap = new HashMap<Sample, List<SampleSource>>();
				
				for(SampleSource cellLibrary : cellLibraryList){
					
					Sample parentSample = this.getLibrary(cellLibrary);
					
					while(parentSample.getParentId()!=null){
						//get the uppermost sample associated with these cellLibraries
						//parentSample = parentSample.getParent();//this call could have possible lazy loading issues; 02-18-14
						parentSample = this.getSampleById(parentSample.getParentId());//avoids any possible downstream lazy loading issues
					}
					if(sampleCellLibraryListMap.containsKey(parentSample)){
						sampleCellLibraryListMap.get(parentSample).add(cellLibrary);
					}
					else{
						List<SampleSource> cellLibraryListForASample = new ArrayList<SampleSource>();
						cellLibraryListForASample.add(cellLibrary);
						sampleCellLibraryListMap.put(parentSample, cellLibraryListForASample);
					}
				}
				return sampleCellLibraryListMap;
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Transactional("entityManager")
			@Override
			public boolean confirmSamplePairIsOfSameSpecies(Sample s1, Sample s2) {
				
				Integer s1OrganismId = this.getIdOfOrganism(s1);
				Integer s2OrganismId = this.getIdOfOrganism(s2);
				if( s1OrganismId.intValue()==0 || s2OrganismId.intValue()==0 ){
					logger.debug("one or both organisms of this samplePair is Not known"); 
					return false;
				}
				else if(s1OrganismId.intValue()!=s2OrganismId.intValue()){
					logger.debug("samplePair Not of same organism"); 
					return false;
				}
				return true;
			}
			
			
			/**
			 * {@inheritDoc}
			 */
			@Transactional("entityManager")
			@Override
			public List<Integer> convertCellLibraryListToIdList(List<SampleSource> cellLibraryList){
				List<Integer> list = new ArrayList<Integer>();
				for(SampleSource ss : cellLibraryList){
					list.add(ss.getId());
				}
				return list;
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Transactional("entityManager")
			@Override
			public void addControlLibraryToCell(Sample cell, Sample controlLibraryToBeAdded, Float libConcInCellPicoM) throws SampleTypeException, SampleSubtypeException, SampleException, SampleMultiplexException, MetadataException{
			
				  //reworked by dubin, 7-9-14
				
				  Assert.assertParameterNotNull(cell, "No cell provided");
				  Assert.assertParameterNotNullNotZero(cell.getId(), "Invalid cell Provided");
				  Assert.assertParameterNotNull(controlLibraryToBeAdded, "No library provided");
				  Assert.assertParameterNotNullNotZero(controlLibraryToBeAdded.getId(), "Invalid library Provided");
				  Assert.assertParameterNotNull(libConcInCellPicoM, "No lib conc provided");
				  if (!isCell(cell)){
					  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
				  }
				  if (!this.isLibrary(controlLibraryToBeAdded)){
					  throw new SampleTypeException("Expected 'library' but got Sample of type '" + controlLibraryToBeAdded.getSampleType().getIName() + "' instead.");
				  }
				  if(!controlLibraryToBeAdded.getSampleSubtype().getIName().equals("controlLibrarySample")){
					  throw new SampleSubtypeException("Expected 'controlLibrarySample' but got Sample with SampleSubtype '" + controlLibraryToBeAdded.getSampleSubtype().getIName() + "' instead.");
				  }
		  
				  //as of 7-8-14, only permit ONE control library per flowcell lane
				  //the web page should prevent this, but just in case....
				  if(this.getControlLibrariesOnCell(cell).size()>0){
					  throw new SampleException("Only one control library permitted per lane");
				  }
				  
				  //If the control has an adaptor index (barcode), check against the libraries already on the lane and do NOT permit duplicate barcodes. 
				  //If the control has no adaptor index (barcode), permit addition of this control library except if there already is a library on the lane that lacks a barcode. 
				  //so, in other words, prohibit libraries with the same barcode and only permit ONE library on the flowcell that lacks a barcode
				  //so, in other words, if there is a barcode or barcode is not present, only permit ONE library on the flowcell that has that barcode (a valid barcode or an empty (none) barcode)
				  //so, in other word, If the control library being added has a valid barcode or if it has no barcode (empty barcode), only permit ONE library on the flowcell that has that barcode (a valid barcode or an empty (none) barcode).
				  
				  Adaptor adaptorOnControlLibraryBeingAdded = null;
				  String barcodeOnControlLibraryBeingAdded = null;//this is the key variable being used
				  try{
					//I'm not certain we should be using getMetaValue("genericLibrary" ... for a controlLibrary, but as of this minute, I have no example of a control Library with an index!)
					  adaptorOnControlLibraryBeingAdded = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", controlLibraryToBeAdded.getSampleMeta())));
					  barcodeOnControlLibraryBeingAdded = adaptorOnControlLibraryBeingAdded.getBarcodesequence();	
					  if(barcodeOnControlLibraryBeingAdded==null){
						  barcodeOnControlLibraryBeingAdded = "";//no barcode on the control
					  }				  
				  }catch(Exception e){
					  //this is ok; no adaptor; some control libraries have no adaptor in the metadata, indicating no indexing
					  logger.debug("Cannot convert genericLibrary.adaptor meta result to Integer or cannot find the requested metadata: "+e.getMessage());
					  barcodeOnControlLibraryBeingAdded = "";//no barcode on the control
				  }
				  
				  List<Sample> librariesAlreadyOnCell = this.getLibrariesOnCell(cell);//all Libraries, both job-related and control libraries
				  for(Sample libraryAlreadyOnCell : librariesAlreadyOnCell){
					  logger.debug("addControlLibraryToCell: Lib on cell: " + libraryAlreadyOnCell.getName());
					  Adaptor adaptorOnLibraryAlreadyOnCell = null;
					  String barcodeOnLibraryAlreadyOnCell = null;//this is the key variable being used
					  try{
						  adaptorOnLibraryAlreadyOnCell = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryAlreadyOnCell.getSampleMeta())));
						  barcodeOnLibraryAlreadyOnCell = adaptorOnLibraryAlreadyOnCell.getBarcodesequence();	
						  if(barcodeOnLibraryAlreadyOnCell==null){
							  barcodeOnLibraryAlreadyOnCell = "";//no barcode
						  }					  
					  }catch(Exception e){
						  //this is ok; no adaptor; some one library per lane may have no adaptor in the metadata or an adaptor with no barcode, indicating no indexing
						  logger.debug("Cannot convert genericLibrary.adaptor meta result to Integer or cannot find the requested metadata: "+e.getMessage());
						  barcodeOnLibraryAlreadyOnCell = "";//no barcode
					  }
					  if(barcodeOnControlLibraryBeingAdded.equalsIgnoreCase(barcodeOnLibraryAlreadyOnCell)){
						  throw new SampleMultiplexException("addControlLibraryToCell: You may not have two libraries with same barcode (which could be no barcode)");
					  }
				  }
				  
				  //OK; no conflicts, so add control library to the lane
				  SampleSource newSampleSource = new SampleSource(); 
				  newSampleSource.setSample(cell);
				  newSampleSource.setSourceSample(controlLibraryToBeAdded);
				  //newSampleSource.setIndex(index.getValue());
				  newSampleSource = getSampleSourceDao().save(newSampleSource);//capture the new samplesourceid
				  
				  try{
					  //setJobForLibraryOnCell(cell, library); //no job for control library
					  setLibraryOnCellConcentration(cell, controlLibraryToBeAdded, libConcInCellPicoM);		  
				  } catch(Exception e){
					  logger.debug("Unable to set LibraryOnCell SampleSourceMeta");
					  throw new MetadataException("Unable to set LibraryOnCell SampleSourceMeta");
				  }
				  logger.debug("OK, was able to add controlLibrary to Lane: " + cell.getName());				  
			}
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void setReasonForNewLibraryComment(Integer sampleId, String comment) throws Exception{
				try{
					metaMessageService.saveToGroup("reasonForNewLibraryComment", "Reason For New Library", comment, sampleId, SampleMeta.class, sampleMetaDao);
				}catch(Exception e){ throw new Exception(e.getMessage());}
			}
			
			/**
			 * {@inheritDoc}
			 */
			@Override
			public List<MetaMessage> getReasonForNewLibraryComment(Integer sampleId){
				return metaMessageService.read("reasonForNewLibraryComment", sampleId, SampleMeta.class, sampleMetaDao);
			}

}

