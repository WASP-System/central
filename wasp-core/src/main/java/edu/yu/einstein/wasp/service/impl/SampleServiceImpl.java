/**
 *
 * SampleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.exception.ResourceException;
import edu.yu.einstein.wasp.exception.RunException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleSubtypeException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.LibraryStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.SampleStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspLibraryTask;
import edu.yu.einstein.wasp.integration.messages.WaspSampleTask;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.WaspStatusMessageTemplate;
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
import edu.yu.einstein.wasp.model.RunMeta;
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
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;

@Service
@Transactional("entityManager")
public class SampleServiceImpl extends WaspMessageHandlingServiceImpl implements SampleService {
	
	private static final String LOCK_META_AREA = "lock";
	
	private static final String LOCK_META_KEY = "status";

	private SampleDao	sampleDao;

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
	
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}

	/**
	 * getSampleDao();
	 * 
	 * @return sampleDao
	 * 
	 */
	@Override
	public SampleDao getSampleDao() {
		return this.sampleDao;
	}
	
	private AuthenticationService authenticationService;
	
	
	@Override
	@Autowired
	public void setAuthenticationService(AuthenticationService authenticationService){
		this.authenticationService = authenticationService;
	}
	
	@Autowired
	private MetaMessageService metaMessageService;

	@Autowired
	private WorkflowDao workflowDao;
	
	@Autowired
	private SampleBarcodeDao sampleBarcodeDao;

	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	private SampleSourceDao sampleSourceDao;
	
	@Autowired
	private SampleSourceMetaDao sampleSourceMetaDao;
	

	@Autowired
	private SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	private AdaptorDao adaptorDao;
	
	@Autowired
	private BarcodeDao barcodeDao;
	
	@Autowired
	private RunDao runDao;
	
	@Autowired
	private JobSampleDao jobSampleDao;
	
	@Autowired
	private JobCellSelectionDao jobCellSelectionDao;
	
	@Autowired
	private SampleJobCellSelectionDao sampleJobCellSelectionDao;

	private JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
	@Autowired
	private RunService runService;
		
	@Autowired
	private SampleTypeDao sampleTypeDao;
	
	@Autowired
	private RunMetaDao runMetaDao;
	
	@Autowired
	 private ResourceDao resourceDao;

	/**
	 * Setter for the sampleMetaDao
	 * @param sampleMetaDao
	 */
	@Autowired
	public void setSampleMetaDao(SampleMetaDao sampleMetaDao) {
		this.sampleMetaDao = sampleMetaDao;
	}


	@Override
	public Sample getSampleByName(final String name) {
		return this.getSampleDao().getSampleByName(name);
	}

	@Override
	public List<Sample> findAllPlatformUnits() {
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
		  sampleDao.save(sample);
		  sampleMetaDao.updateBySampleId(sample.getSampleId(), sample.getSampleMeta());
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
			  if (sampleIn.getSampleId() != null && sample.getSampleId().intValue() == sampleIn.getSampleId().intValue())
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
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
		  ExitStatus sampleReceivedStatus = ExitStatus.UNKNOWN;
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> sampleIdStringSet = new HashSet<String>();
		  sampleIdStringSet.add(sample.getSampleId().toString());
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
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> sampleIdStringSet = new HashSet<String>();
		  sampleIdStringSet.add(sample.getSampleId().toString());
		  parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		  List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.sample.step.sampleQC", parameterMap, false);
		  ExitStatus sampleQCStatus = ExitStatus.UNKNOWN;
		  if (!stepExecutions.isEmpty())
			  sampleQCStatus =  batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions).getExitStatus();
		  return sampleQCStatus;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public ExitStatus getLibraryQCStatus(final Sample library){
		// TODO: Write test!!
		Assert.assertParameterNotNull(library, "No library provided");
		Assert.assertParameterNotNullNotZero(library.getSampleId(), "Invalid library Provided");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(library.getSampleId().toString());
		parameterMap.put(WaspJobParameters.LIBRARY_ID, sampleIdStringSet);
		List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.library.step.libraryQC", parameterMap, false);
		ExitStatus libraryQCStatus = ExitStatus.UNKNOWN;
		if (!stepExecutions.isEmpty())
			libraryQCStatus =  batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions).getExitStatus();
		return libraryQCStatus;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean isSamplePassQC(final Sample sample){
		// TODO: Write test!!
		Assert.assertParameterNotNull(sample, "No Sample provided");
		Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
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
		  Assert.assertParameterNotNullNotZero(library.getSampleId(), "Invalid library Provided");
		  if (getLibraryQCStatus(library).getExitCode().equals(ExitStatus.COMPLETED.getExitCode()))
				return true;
			return false;
	  }
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean isSampleReceived(Sample sample){
		Assert.assertParameterNotNull(sample, "No Sample provided");
		Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
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
		Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
		if (isLibrary(sample))
			return false;
		if (!isSamplePassQC(sample))
			return false;
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> sampleIdStringSet = new HashSet<String>();
		sampleIdStringSet.add(sample.getSampleId().toString());
		parameterMap.put(WaspJobParameters.SAMPLE_ID, sampleIdStringSet);
		
		List<Sample> librariesExisting = sample.getChildren();
		if (librariesExisting == null || librariesExisting.isEmpty()){
			logger.debug("No libraries currently associated with sample id=" + sample.getSampleId() + " (" + sample.getName() + ")");
			return true; // no libraries made yet for this sample
		}
		
		
		// libraries already exist for this sample. Lets see if we need to make another 
		// (no existing libraries have a flow that is running or completed successfully)
		parameterMap = new HashMap<String, Set<String>>();
		for (Sample library: librariesExisting){
			Set<String> libraryIdStringSet = new HashSet<String>();
			libraryIdStringSet.add(library.getSampleId().toString());
			parameterMap.put(WaspJobParameters.LIBRARY_ID, libraryIdStringSet);
			List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions("wasp.facilityLibrary.jobflow", parameterMap, false);
			
			for (JobExecution jobExecution: jobExecutions){
				if (jobExecution.getStatus().equals(BatchStatus.STARTING) || 
						jobExecution.getStatus().equals(BatchStatus.STARTED) ||
						jobExecution.getStatus().equals(BatchStatus.COMPLETED) ){
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
			        return arg0.getSampleId().compareTo(arg1.getSampleId());
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
		  if(internalStatus.getExitCode().equals(ExitStatus.EXECUTING.getExitCode())){
			  return "NOT ARRIVED";
			}
			else if(internalStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
				return "RECEIVED";
			}
			else if(internalStatus.getExitCode().equals(ExitStatus.FAILED.getExitCode())){
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
	  public WaspStatus convertSampleReceivedStatusFromWeb(String webStatus){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(webStatus, "No webStatus provided");
		  	if(webStatus.equals("RECEIVED")){
				return WaspStatus.CREATED;
			}
			else if(webStatus.equals("WITHDRAWN")){
				return WaspStatus.ABANDONED;
			}
			else {
				return WaspStatus.UNKNOWN;
			}
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public String convertSampleQCStatusForWeb(ExitStatus internalStatus){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(internalStatus, "No internalStatus provided");
		  	if( internalStatus.getExitCode().equals(ExitStatus.UNKNOWN.getExitCode()) ){
			  return "NONEXISTENT";
		  	}
		  	else if(internalStatus.getExitCode().equals(ExitStatus.EXECUTING.getExitCode())){
			  return "AWAITING QC";
		  	}
			else if(internalStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
				return "PASSED";
			}
			else if(internalStatus.getExitCode().equals(ExitStatus.FAILED.getExitCode())){
				return "FAILED";
			}
			else {
				return "UNKNOWN";
			}
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public WaspStatus convertSampleQCStatusFromWeb(String webStatus){
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(webStatus, "No webStatus provided");
		  	if(webStatus.equals("PASSED")){
				return WaspStatus.COMPLETED;
			}
			else if(webStatus.equals("FAILED")){
				return WaspStatus.FAILED;
			}
			else {
				return WaspStatus.UNKNOWN;
			}
	  }
	  

	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<String> getReceiveSampleStatusOptionsForWeb(){
		  // TODO: Write test!!
		  ExitStatus [] statusList = {ExitStatus.COMPLETED, ExitStatus.FAILED, ExitStatus.STOPPED};
		  List<String> options = new ArrayList<String>();
		  for(ExitStatus status : statusList){
			  options.add(convertSampleReceivedStatusForWeb(status));
		  }
		  return options;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void initiateBatchJobForSample(Job job, Sample sample, String batchJobName){
		  Assert.assertParameterNotNull(job, "No Job provided");
		  Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		  Assert.assertParameterNotNull(sample, "No Sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
		  Assert.assertParameterNotNull(batchJobName, "No batchJobName provided");
		  // send message to initiate job processing
		  Map<String, String> jobParameters = new HashMap<String, String>();
		  jobParameters.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
		  jobParameters.put(WaspJobParameters.SAMPLE_ID, sample.getSampleId().toString());
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
		  Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		  Assert.assertParameterNotNull(library, "No Library provided");
		  Assert.assertParameterNotNullNotZero(library.getSampleId(), "Invalid Library Provided");
		  Assert.assertParameterNotNull(batchJobName, "No batchJobName provided");
		  // send message to initiate job processing
		  Map<String, String> jobParameters = new HashMap<String, String>();
		  jobParameters.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
		  jobParameters.put(WaspJobParameters.LIBRARY_ID, library.getSampleId().toString());
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
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
		  Assert.assertParameterNotNull(status, "No Status provided");
		  if (status != WaspStatus.CREATED && status != WaspStatus.ABANDONED)
			  throw new InvalidParameterException("WaspStatus is null, or not CREATED or ABANDONED");
		  
		  WaspStatusMessageTemplate messageTemplate;
		  if (isLibrary(sample)){
			  messageTemplate = new LibraryStatusMessageTemplate(sample.getSampleId());
		  } else {
			  messageTemplate = new SampleStatusMessageTemplate(sample.getSampleId());
		  }
		  messageTemplate.setStatus(status); // sample received (CREATED) or abandoned (ABANDONED)
		  try{
			  sendOutboundMessage(messageTemplate.build(), false);
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
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
		  Assert.assertParameterNotNull(status, "No Status provided");
		  if (status != WaspStatus.COMPLETED && status != WaspStatus.FAILED)
			  throw new InvalidParameterException("WaspStatus is null, or not COMPLETED or FAILED");
		  
		  WaspStatusMessageTemplate messageTemplate;
		  if (isLibrary(sample)){
			  messageTemplate = new LibraryStatusMessageTemplate(sample.getSampleId());
			  messageTemplate.setTask(WaspLibraryTask.QC);
		  } else {
			  messageTemplate = new SampleStatusMessageTemplate(sample.getSampleId());
			  messageTemplate.setTask(WaspSampleTask.QC);
		  }
		  messageTemplate.setStatus(status); // sample received (COMPLETED) or abandoned (FAILED)
		  try{
			  sendOutboundMessage(messageTemplate.build(), false);
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
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
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
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid Sample Provided");
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
		  Assert.assertParameterNotNull(library, "No Sample provided");
		  Adaptor adaptor = null;
		  String adaptorId = new String("");
		  SampleSubtype sampleSubtype = library.getSampleSubtype();
		  String areaList = sampleSubtype.getAreaList();
		  String area = new String("");
		  
		  String [] stringList = areaList.split("[\\s,]+");//separates on comma or whitespace
		  for(String string : stringList){
			  
			  if(string.indexOf("Library") > -1){
				  area = string;
			  }
		  }
		 
		  try{		
			  adaptorId = MetaHelper.getMetaValue(area, "adaptor", library.getSampleMeta());
		  }
		  catch(MetadataException me){
			  logger.warn("Unable to identify adaptor for libraryId " + library.getSampleId());
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
		  List<Sample> allPlatformUnits = findAllPlatformUnits(); 
		  if (allPlatformUnits == null || allPlatformUnits.isEmpty())
			  return availablePlatformUnits;
		
		  // get platform units that are not associated with currently executing or completed runs
		 		  
		  // get job executions for ALL platform units on all runs and record those associated with runs that are currently executing or have completed
		  // successfully (COMPLETED) or have failed QC or been rejected (FAILED). If run is in status STOPPED (aborted) the platform unit
		  // should be made available for adding more libraries. Might want to review this use case!!
		  
		  // 'run' batch jobs are provided with one parameter, runId
		  // we can obtain all run job executions by selecting jobs which have these parameters (regardless of the values as specified by "*")
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> runIdStringSet = new HashSet<String>();
		  runIdStringSet.add("*");
		  parameterMap.put(WaspJobParameters.RUN_ID, runIdStringSet);
		  Set<Integer> IdsForPlatformUnitsNotAvailable = new HashSet<Integer>();
		  List<JobExecution> allRelevantJobExecutions = new ArrayList<JobExecution>();
		  allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.EXECUTING) );
		  allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.COMPLETED) );
		  allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.FAILED) );
		  
		  // make platform unit available again if ExitStatus is STOPPED (aborted) 
		  // so comment the following line out for now:
		  // allRelevantJobExecutions.addAll( batchJobExplorer.getJobExecutions(parameterMap, true, ExitStatus.STOPPED) );
		  
		  // get sample id for all platform units associated with the batch job executions retrieved
		  for (JobExecution je: allRelevantJobExecutions){
			  try{
				  String puIdStr = batchJobExplorer.getJobParameterValueByKey(je, WaspJobParameters.PLATFORM_UNIT_ID);
				  IdsForPlatformUnitsNotAvailable.add(Integer.valueOf(puIdStr));
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
				if (! IdsForPlatformUnitsNotAvailable.contains( pu.getSampleId() ) && ! getPlatformUnitLockStatus(pu).equals(LockStatus.LOCKED))
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
				  if(ssrc.getResourcecategoryId().intValue() == resourceCategory.getResourceCategoryId().intValue()){
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
		  for(Sample pu : availablePlatformUnits){
			  for(SampleSubtypeResourceCategory ssrc : pu.getSampleSubtype().getSampleSubtypeResourceCategory()){
				  for(JobResourcecategory jrc : job.getJobResourcecategory()){
					  if(ssrc.getResourcecategoryId().intValue() == jrc.getResourcecategoryId().intValue()){
						  availableAndCompatibleFlowCells.add(pu);
					  }
				  }
			  }
		  }
		  return availableAndCompatibleFlowCells;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Sample getPlatformUnitForCell(Sample cell) throws SampleTypeException, SampleParentChildException{
		  Assert.assertParameterNotNull(cell, "No Cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getSampleId(), "Invalid Cell Provided");
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sourceSampleId", cell.getSampleId());
		  List<SampleSource> sampleSourceList = getSampleSourceDao().findByMap(q);
			  
		  if (sampleSourceList==null || sampleSourceList.isEmpty())
			  throw new SampleParentChildException("Cell '"+cell.getSampleId().toString()+"' is associated with no flowcells");
		  if (sampleSourceList.size() > 1)
			  throw new SampleParentChildException("Cell '"+cell.getSampleId().toString()+"' is associated with more than one flowcell");
		  SampleSource ss = sampleSourceList.get(0);
		  logger.debug("Returning platform unit id=" + ss.getSample().getSampleId() + " for cell id=" + cell.getSampleId() + " (SampleSource id=" + ss.getSampleSourceId() + ")");
		  return ss.getSample();
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Map<Integer, Sample> getIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException{
		  Assert.assertParameterNotNull(platformUnit, "No Platform unit provided");
		  Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "Invalid Platform unit provided");
		  if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			  throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		  }
		  
		  Map<Integer, Sample> indexedCells = new HashMap<Integer, Sample>();
		  if (platformUnit.getSampleSource() == null)
			  return indexedCells;
		  
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sampleId", platformUnit.getSampleId());
		  
		  for (SampleSource ss : getSampleSourceDao().findByMap(q)){
			  Sample cell = ss.getSourceSample();
			  Integer index = ss.getIndex();
			  if (!cell.getSampleType().getIName().equals("cell")){
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
		  Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "Invalid platform unit Provided");
		  Map<Integer, Sample> indexedCells = getIndexedCellsOnPlatformUnit(platformUnit);
		  return new Integer(indexedCells.size());
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void addCellToPlatformUnit(Sample platformUnit, Sample cell, Integer index) throws SampleTypeException, SampleIndexException{
		  Assert.assertParameterNotNull(platformUnit, "No platform unit provided");
		  Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "Invalid platform unit Provided");
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getSampleId(), "Invalid cell Provided");
		  Assert.assertParameterNotNull(index, "No index provided");
		  if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			  throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		  }
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  if (index < 1)
			  throw new SampleIndexException("index must be an integer >= 1");
		  Map<String, Integer> sampleSourceQuery = new HashMap<String, Integer>();
		  sampleSourceQuery.put("sampleId", platformUnit.getSampleId());
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
		  Assert.assertParameterNotNullNotZero(cell.getSampleId(), "Invalid cell Provided");
		  return getLibrariesOnCell(cell, null);
	  }
	  
	  /**
	   * Index is essentially a a wrapper around an int. It is required (instead of Integer) because
	   * Integer is immutable and so it's value passed as a parameter, when 'changed' within a method
	   * cannot be used as the modified version outside that method.
	   * @author asmclellan
	   *
	   */
	  private class Index{
		  private int index;
		  
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
	  private List<Sample> getLibrariesOnCell(Sample cell, Index maxIndex) throws SampleTypeException{
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getSampleId(), "Invalid cell Provided");
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  List<Sample> libraries = new ArrayList<Sample>();
		  if (cell.getSampleSource() == null)
			  return libraries;
		  
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sampleId", cell.getSampleId());
		  		  
		  for (SampleSource ss : getSampleSourceDao().findByMap(q)){
			  Sample library = ss.getSourceSample();
			  if (!this.isLibrary(library) && !library.getSampleType().getIName().equals("controlLibrarySample")){
				  throw new SampleTypeException("Expected 'library' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.: cellId: " + cell.getSampleId().intValue() + " cellName = " + cell.getName() + " problem libraryId = " + library.getSampleId().intValue() + " problem library name = " + library.getName() );
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
		  Assert.assertParameterNotNullNotZero(cell.getSampleId(), "Invalid cell Provided");
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
	  public void addLibraryToCell(Sample cell, Sample library, Float libConcInLanePicoM) throws SampleTypeException, SampleException, SampleMultiplexException, MetadataException{
		  // TODO: Write test!!
		  Assert.assertParameterNotNull(cell, "No cell provided");
		  Assert.assertParameterNotNullNotZero(cell.getSampleId(), "Invalid cell Provided");
		  Assert.assertParameterNotNull(library, "No library provided");
		  Assert.assertParameterNotNullNotZero(library.getSampleId(), "Invalid library Provided");
		  Assert.assertParameterNotNull(libConcInLanePicoM, "No lib conc provided");
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  if (!this.isLibrary(library)){
			  throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		  }
		  /* 
			(1) identify the barcode sequence on the library being added. If problem then terminate. 
			(2) if the library being added has a barcode that is NONE, and the lane contains ANY OTHER LIBRARY, then terminate. 
			(3) identify barcode of libraries already on lane; if problem, terminate. Should also get their jobIds.
			(4) if the lane already has a library with a barcode of NONE, then terminate
			(5) if the library being added has a bardcode that is something other than NONE (meaning a real barcode sequence) AND if a library already on the lane has that same barcode, then terminate. 
			(6) do we want to maintain only a single jobId for a lane???
		   */

		  //case 1: identify the adaptor barcode for the library being added; it's barcode is either NONE (no multiplexing) or has some more interesting barcode sequence (for multiplexing, such as AACTG)
		  Adaptor adaptorOnLibraryBeingAdded = null;
		  try{
			  adaptorOnLibraryBeingAdded = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", library.getSampleMeta())));
		  } catch(NumberFormatException e){
			  throw new MetadataException("Cannot convert genericLibrary.adaptor meta result to Integer: "+e.getMessage());
		  }
		  
		  if(adaptorOnLibraryBeingAdded==null || adaptorOnLibraryBeingAdded.getAdaptorId()==null){
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
		  if( barcodeOnLibBeingAdded.equals("NONE") && libraries != null && libraries.size() > 0  ){ //case 2: the library being added has a barcode of "NONE" AND the lane to which user wants to add this library already contains one or more libraries (such action is prohibited)
			  throw new SampleMultiplexException("Cannot add more than one sample to cell if not multiplexed. Input library has barcode 'NONE'.");
		  }
		 
		  //cases 3, 4, 5, 6 
		  if (libraries != null) {
			  for (Sample libraryAlreadyOnLane: libraries) {
				  Adaptor adaptorOnLane = null;
				  try{
					  adaptorOnLane = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf(MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryAlreadyOnLane.getSampleMeta())));
				  } catch(NumberFormatException e){
					  throw new MetadataException("Library already on lane: Cannot convert genericLibrary.adaptor meta result to Integer: "+e.getMessage());
				  }
				  
				  if(adaptorOnLane==null || adaptorOnLane.getAdaptorId()==null){
					  throw new SampleException("Library already on lane : No adaptor associated with library");
				  }
				  else if( adaptorOnLane.getBarcodesequence()==null || adaptorOnLane.getBarcodesequence().equals("") ){
					  throw new SampleException("Library already on lane: adaptor has no barcode");
				  } 
				  else if( adaptorOnLane.getBarcodesequence().equals("NONE")){ 
					  throw new SampleMultiplexException("Library already on lane: Cannot add more than one sample to cell if not multiplexed. Library has barcode 'NONE'");
				  }
				  else if(adaptorOnLane.getBarcodesequence().equals(barcodeOnLibBeingAdded)){
					  throw new SampleMultiplexException("Library already on lane: has same barcode as input library");
				  }
				  else{
					  // TODO: confirm library is really part of this jobId. For now do nothing. If Einstein, then terminate (lane restricted to libraries from single job)
				  }
			  }	
		  }

		  SampleSource newSampleSource = new SampleSource(); 
		  newSampleSource.setSample(cell);
		  newSampleSource.setSourceSample(library);
		  newSampleSource.setIndex(index.getValue());
		  newSampleSource = getSampleSourceDao().save(newSampleSource);//capture the new samplesourceid
		  
		  try{
			  MetaHelper metaHelper = new MetaHelper("LibraryOnCell", SampleSourceMeta.class);
			  metaHelper.setMetaValueByName("libConcInLanePicoM", libConcInLanePicoM.toString());
			  metaHelper.setMetaValueByName("jobId", library.getJob().getJobId().toString());
			  sampleSourceMetaDao.updateBySampleSourceId(newSampleSource.getSampleSourceId(), (List<SampleSourceMeta>) metaHelper.getMetaList());
		  } catch(MetadataException e){
			  logger.warn("Unable to set LibraryOnCell SampleSourceMeta");
		  }
		  
	  }
	  
	  @Override
	  public int getRequestedSampleCoverage(Sample sample){
		  Assert.assertParameterNotNull(sample, "No sample provided");
		  Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid sample Provided");
		  Map<String, Integer> jobCellFilter = new HashMap<String, Integer>();
		  jobCellFilter.put("jobId", sample.getJob().getJobId());
		  Map<String, Integer> sampleCellFilter = new HashMap<String, Integer>();
		  sampleCellFilter.put("sampleId", sample.getSampleId());
		  int coverage = 0;
		  for (JobCellSelection jobCellSelection: jobCellSelectionDao.findByMap(jobCellFilter)){
			  sampleCellFilter.put("jobCellSelectionId", jobCellSelection.getJobCellSelectionId());
			  coverage += sampleJobCellSelectionDao.findByMap(sampleCellFilter).size();
		  }
		  return coverage;
	  }
	  
	  
	  @Override
	  public List<Sample> getCellsForLibrary(Sample library) throws SampleTypeException{
		  Assert.assertParameterNotNull(library, "No library provided");
		  Assert.assertParameterNotNullNotZero(library.getSampleId(), "Invalid library Provided");
		  if (!isLibrary(library)){
			  throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		  }
		  List<Sample> cells = new ArrayList<Sample>();
		  Map<String,Integer> q = new HashMap<String,Integer>();
		  q.put("sourceSampleId", library.getSampleId());
		  for (SampleSource ss : getSampleSourceDao().findByMap(q)){
			  cells.add(ss.getSample());
		  }
		  return cells;
	  }
	  
	  @Override
	  public SampleDraft cloneSampleDraft(final SampleDraft sampleDraft){
		  Assert.assertParameterNotNull(sampleDraft, "No SampleDraft provided");
		  SampleDraft clone = new SampleDraft();
		  if (sampleDraft.getFile() != null)
			  clone.setFile(sampleDraft.getFile());
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
		Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "Invalid platform unit Provided");
		if (!isPlatformUnit(platformUnit))
			throw new SampleTypeException("sample is not a platfrom unit");
		for (Run run : platformUnit.getRun()){
			// return run if it has been started by has no record of completion
			if (run.getStartts() != null && run.getEnDts() == null)
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
		if(sampleType==null||sampleType.getSampleTypeId()==null||sampleType.getSampleTypeId().intValue()==0){
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
		if (sample.getSampleId() != null && sample.getSampleId().intValue() > 0)
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
		
		if(sample == null || sample.getSampleId()==null || sample.getSampleId().intValue() <= 0){return false;}
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
		if(sampleSubtype==null || sampleSubtype.getSampleSubtypeId()==null || sampleSubtype.getSampleSubtypeId().intValue() <= 0){throw new SampleSubtypeException("SampleSubtype with sampleSubtypeId of " + sampleSubtypeId.intValue() + " not in database.");}
		else if(!this.isSampleSubtypeWithSpecificSampleType(sampleSubtype, "platformunit")){throw new SampleSubtypeException("SampleSubtype with sampleSubtypeId of " + sampleSubtypeId.intValue() + " not of sampletype platformunit.");}
		return sampleSubtype;		
	}

	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getNumberOfCellsListForThisTypeOfPlatformUnit(SampleSubtype sampleSubtype) throws SampleTypeException, SampleSubtypeException{
		Assert.assertParameterNotNull(sampleSubtype, "No sampleSubtype provided");
		Assert.assertParameterNotNullNotZero(sampleSubtype.getSampleSubtypeId(), "Invalid SampleSubtype Provided");
		if(!isSampleSubtypeWithSpecificSampleType(sampleSubtype, "platformunit")){
			throw new SampleSubtypeException("SampleSubtype with Id of " + sampleSubtype.getSampleSubtypeId().toString() + " is not platformunit");
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
	public boolean isRequestedReductionInCellNumberProhibited(Sample platformUnitInDatabase, Integer numberOfLanesRequested) throws SampleException, SampleTypeException{
		Assert.assertParameterNotNull(platformUnitInDatabase, "No platform unit provided");
		Assert.assertParameterNotNullNotZero(platformUnitInDatabase.getSampleId(), "Invalid platform unit Provided");
		Assert.assertParameterNotNullNotZero(numberOfLanesRequested, "Invalid numberOflanesRequested value provided");
		Map<Integer,Sample> indexedCellMap = this.getIndexedCellsOnPlatformUnit(platformUnitInDatabase);//throws exception	
		Integer numberOfLanesInDatabase = indexedCellMap.size();
		if(numberOfLanesInDatabase.intValue() <= numberOfLanesRequested.intValue()){//no loss of lanes, so return false, as action not prohibited
			return false;
		}
		
		//user asking to reduce number of lanes
		//must check for presence of libraries on those lanes that user seems to want to remove. If any found, return true.
		for(int i = numberOfLanesRequested.intValue() + 1; i <= numberOfLanesInDatabase.intValue(); i++){
			Integer index = new Integer(i);
			Sample cell = indexedCellMap.get(index);
			if(cell == null){
				//unexpected problem; indexes not ordered
				throw new SampleException("No cell found for platformUnitId " + platformUnitInDatabase.getSampleId().intValue() + " and cell index " + i);
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
	public void createUpdatePlatformUnit(Sample platformUnit, SampleSubtype sampleSubtype, String barcodeName, Integer numberOfLanesRequested, List<SampleMeta> sampleMetaList) throws SampleException, SampleTypeException, SampleSubtypeException{
		Assert.assertParameterNotNull(platformUnit, "No platformUnit provided");
		Assert.assertParameterNotNull(sampleSubtype, "No sampleSubtype provided");
		Assert.assertParameterNotNullNotZero(sampleSubtype.getSampleSubtypeId(), "Invalid sampleSubtype Provided");
		Assert.assertParameterNotNull(barcodeName, "No barcodeName provided");
		Assert.assertParameterNotNullNotZero(numberOfLanesRequested, "Invalid numberOfLanesRequested value provided");
		Assert.assertParameterNotNull(sampleMetaList, "No sampleMetaList provided");
		String action = new String("create");
		Sample pu = null;
		SampleType sampleTypeForPlatformUnit = null;
		SampleType sampleTypeForCell = null;
		Integer numberOfLanesInDatabase = null;
		boolean platformUnitNameHasBeenChanged = false;
		
		if(isSampleInDatabase(platformUnit)){//this is an update of an existing record
			pu = platformUnit;
			if(!isPlatformUnit(platformUnit)){
				throw new SampleException("Sample with Id of " + pu.getSampleId().toString() + " unexpectedly NOT a platformUnit either in sampletype or samplesubtype");
			}
			
			//check this first, since it could throw an exception, so no need to proceed with the update unless this is OK
			numberOfLanesInDatabase = this.getNumberOfIndexedCellsOnPlatformUnit(pu);
			if(numberOfLanesInDatabase==null || numberOfLanesInDatabase.intValue()<=0){//should never be 0 lanes on a platformunit
				throw new SampleException("lanecount in database is not valid for platformunit with Id " + pu.getSampleId().intValue());
			}	
			
			//if update, determine whether platformunit name has been changed
			if(barcodeName != null && !barcodeName.equals(platformUnit.getName())){
				platformUnitNameHasBeenChanged = true;
			}
			

			action = new String("update");			
		}
		else{//request for a new platformunit record 
			numberOfLanesInDatabase = new Integer(0);
			pu = new Sample();
		}
		
		if(!this.isSampleSubtypeWithSpecificSampleType(sampleSubtype, "platformunit")){
			throw new SampleSubtypeException("SampleSubtype with ID of " + sampleSubtype.getSampleSubtypeId().toString() + " is unexpectedly not SampleType of platformunit");								
		}
		sampleTypeForPlatformUnit = sampleTypeDao.getSampleTypeByIName("platformunit");
		if(sampleTypeForPlatformUnit==null || sampleTypeForPlatformUnit.getSampleTypeId()==null || sampleTypeForPlatformUnit.getSampleTypeId().intValue()<=0){
			throw new SampleTypeException("SampleType of type platformunit unexpectedly not found");
		}
		sampleTypeForCell = sampleTypeDao.getSampleTypeByIName("cell");
		if(sampleTypeForCell==null || sampleTypeForCell.getSampleTypeId()==null || sampleTypeForCell.getSampleTypeId().intValue()<=0){
			throw new SampleTypeException("SampleType of type cell unexpectedly not found");
		}
		
		if(numberOfLanesRequested == null || numberOfLanesRequested.intValue() <= 0){
			throw new SampleException("Number of lanes requested not valid value");
		}
		else{//confirm numberOfLanesRequested is a valid value for this subtype of platformUnit
			List<Integer> numberOfCellsList = this.getNumberOfCellsListForThisTypeOfPlatformUnit(sampleSubtype);
			boolean foundIt = false;
			for(Integer numberOfCellsAllowed : numberOfCellsList){
				if(numberOfCellsAllowed.intValue()==numberOfLanesRequested.intValue()){
					foundIt = true;
					break;
				}
			}
			if(!foundIt){
				throw new SampleException("Number of lanes requested is not compatible with the requested sampleSubtype");
			}
		}
		
		if(numberOfLanesRequested.intValue() >= numberOfLanesInDatabase.intValue()){//request to add lanes or no change in lane number, so not a problem
			;
		}
		else if(numberOfLanesRequested.intValue() < numberOfLanesInDatabase.intValue()){//request to remove lanes; a potential problem if libraries are on the lanes to be removed
			// perform next test
			if(this.isRequestedReductionInCellNumberProhibited(pu, numberOfLanesRequested)){//value of true means libraries are assigned to those lanes being asked to be removed. Prohibit this action and inform user to first remove those libraries from the lanes being requested to be removed
				throw new SampleException("Sample Exception during platform unit update: Action not permitted at this time. To reduce the number of lanes, remove libraries on the lanes that will be lost.");
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
			pu.setSubmitterUserId(me.getUserId());
					
			pu.setSampleSubtypeId(sampleSubtype.getSampleSubtypeId());//sampleSubtype is a parameter
	
			if(action.equals("create")){//new record
				pu.setSampleTypeId(sampleTypeForPlatformUnit.getSampleTypeId());
				pu.setSubmitterLabId(1);//Ed
				pu.setReceiverUserId(platformUnit.getSubmitterUserId());//Ed
				pu.setReceiveDts(new Date());//Ed
				pu.setIsReceived(1);//Ed
				pu.setIsActive(1);//Ed
				pu.setIsGood(1);//Ed
			}
			Sample platformUnitDb = sampleDao.save(pu);
			if(platformUnitDb==null || platformUnitDb.getSampleId()==null || platformUnitDb.getSampleId().intValue()<=0){
				throw new SampleException("new platform unit unexpectedly not saved");
			}
			sampleMetaDao.updateBySampleId(platformUnitDb.getSampleId(), sampleMetaList); // persist the metadata; no way to check as this returns void
		
			//barcode
			List<SampleBarcode> sampleBarcodeList = platformUnitDb.getSampleBarcode();//any barcodes exist for this platform unit?
			if(sampleBarcodeList != null && sampleBarcodeList.size() > 0){//this is an update
				SampleBarcode sampleBarcode = sampleBarcodeList.get(0);
				Barcode existingBarcode = sampleBarcode.getBarcode();
				existingBarcode.setBarcode(barcodeName);//update the barcodeName
				Barcode barcodeDb = this.barcodeDao.save(existingBarcode);
				if(barcodeDb==null || barcodeDb.getBarcodeId()==null || barcodeDb.getBarcodeId().intValue()<=0){
					throw new SampleException("updated barcode unexpectedly not saved");
				}
			}
			else{//new barcode for a new platformunit
				Barcode barcodeObject = new Barcode();		
				barcodeObject.setBarcode(barcodeName);
				barcodeObject.setBarcodefor("WASP");
				barcodeObject.setIsActive(new Integer(1));
				Barcode barcodeDb = this.barcodeDao.save(barcodeObject);//save new barcode in db
				if(barcodeDb==null || barcodeDb.getBarcodeId()==null || barcodeDb.getBarcodeId().intValue()<=0){
					throw new SampleException("updated barcode unexpectedly not saved");
				}
				SampleBarcode sampleBarcode = new SampleBarcode();	
				sampleBarcode.setBarcodeId(barcodeDb.getBarcodeId()); // set new barcodeId in samplebarcode
				sampleBarcode.setSampleId(platformUnitDb.getSampleId());
				SampleBarcode sampleBarcodeDb = this.sampleBarcodeDao.save(sampleBarcode);
				if(sampleBarcodeDb==null || sampleBarcodeDb.getSampleBarcode()==null || sampleBarcodeDb.getSampleBarcode().intValue()<=0){
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
						if (!cell.getSampleType().getIName().equals("cell")){
							throw new SampleTypeException("Expected 'cell' while updating cell name but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
						}
						Integer index = ss.getIndex();
						cell.setName(barcodeName +"/"+(index.intValue()));
						Sample cellDb = this.sampleDao.save(cell);
						if(cellDb==null || cellDb.getSampleId()==null || cell.getSampleId().intValue() <= 0){
							throw new SampleException("updated cell unexpectedly not saved during update of cell name");
						}
					}
				}
			}
			
			//lanes
			//if create new platformunit, or add/remove additional lanes during and update
			if(numberOfLanesRequested.intValue() > numberOfLanesInDatabase.intValue()){//add lanes; can be create new record (where numberOfLanesInDatabase = 0) or an update to add some more lanes 
	
				for(int i = numberOfLanesInDatabase + 1; i <= numberOfLanesRequested; i++){//add additional lanes
				
					Sample cell = new Sample();
					cell.setSubmitterLabId(platformUnitDb.getSubmitterLabId());
					cell.setSubmitterUserId(platformUnitDb.getSubmitterUserId());
					cell.setName(platformUnitDb.getName()+"/"+(i));
					cell.setSampleTypeId(sampleTypeForCell.getSampleTypeId());
					cell.setIsGood(1);
					cell.setIsActive(1);
					cell.setIsReceived(1);
					cell.setReceiverUserId(platformUnitDb.getSubmitterUserId());
					cell.setReceiveDts(new Date());
					Sample cellDb = this.sampleDao.save(cell);
					if(cellDb==null || cellDb.getSampleId()==null || cell.getSampleId().intValue() <= 0){
						throw new SampleException("new cell unexpectedly not saved during create or update of platformunit");
					}
	
					SampleSource sampleSource = new SampleSource();
					sampleSource.setSampleId(platformUnitDb.getSampleId());
					sampleSource.setSourceSampleId(cellDb.getSampleId());
					sampleSource.setIndex(i);
					SampleSource sampleSourceDb = this.getSampleSourceDao().save(sampleSource);
					if(sampleSourceDb==null || sampleSourceDb.getSampleId()==null || sampleSourceDb.getSampleId().intValue() <= 0){
						throw new SampleException("new samplesource unexpectedly not saved during create or update of platformunit");
					}
				}
			}
			else if(numberOfLanesRequested.intValue() < numberOfLanesInDatabase.intValue()){//update requests to remove lanes; above we already confirmed that this will NOT result in loss of info  with this call (if(this.requestedReductionInCellNumberIsProhibited(pu, numberOfLanesRequested))  
				
				//get the list 
				//Map<Integer, Sample> indexedCellMap = sampleService.getIndexedCellsOnPlatformUnit(platformUnitInDatabase);
				for (SampleSource ss : platformUnitDb.getSampleSource()){
					Sample cell = ss.getSourceSample();
					if (!cell.getSampleType().getIName().equals("cell")){
						throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
					}
					Integer index = ss.getIndex();
					if(index.intValue() >= numberOfLanesRequested.intValue() + 1 && index.intValue() <= numberOfLanesInDatabase.intValue()){
						//check for present of any libraries on these cells (just as a final fail safe mechanism, as this was tested above)
						List<Sample> libraryList = null;
						libraryList = this.getLibrariesOnCell(cell);//throws exception
						if(libraryList!=null && libraryList.size()>0){//found at least one library
							throw new SampleException("Cell " + cell.getSampleId().intValue() + "unexpectedly has " + libraryList.size() + " libraries on it. Unable to remove this lane");
						}
						//first deletes each pu-cell link and its meta (if any) AND THEN ALSO deletes the cell and the cell's meta (if any)
						this.deleteCellFromPlatformUnit(ss);
					}
				}
			}
			else if(numberOfLanesRequested.intValue() == numberOfLanesInDatabase.intValue()){//do nothing
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
				Sample cell = puCellLink.getSourceSample();//cell is the lane
				if (!cell.getSampleType().getIName().equals("cell")){//confirm its a cell
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
	  public List<Resource> getSequencingMachinesCompatibleWithPU(Sample platformUnit) throws SampleException{
		Assert.assertParameterNotNull(platformUnit, "Invalid platformUnit provided");
		Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "Invalid platformUnit provided");
		if(!this.isPlatformUnit(platformUnit)){
			throw new SampleException("Expected platformUnit for SampleId " + platformUnit.getSampleId().intValue() + " but sample failed in sampletype and/or samplesubtype");
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
		if(resource==null || resource.getResourceId()==null || resource.getResourceId().intValue() <= 0){
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
	public Run getSequenceRun(Integer runId) throws RunException{
		Assert.assertParameterNotNullNotZero(runId, "Invalid runId provided");
		Run run = runDao.getRunByRunId(runId.intValue());
		if(run==null||run.getRunId()==null||run.getRunId().intValue()<=0){
			throw new RunException("Run with runId of " + runId.intValue() + " not found in database");
		}
		else if(!run.getResourceCategory().getResourceType().getIName().equals("mps")){
			throw new RunException("Run with runId of " + runId.intValue() + " does not have resourcecategory of mps");
		}
		else if(!run.getResource().getResourceType().getIName().equals("mps")){
			throw new RunException("Run with runId of " + runId.intValue() + " does not have resource whose resourcetype is mps");
		}
		else if(!run.getResource().getResourceCategory().getResourceType().getIName().equals("mps")){
			throw new RunException("Run with runId of " + runId.intValue() + " does not have resource whose resourcecategory is mps");
		}
		return run;
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
			if(ssrc.getSampleSubtype().getSampleSubtypeId().intValue()==sampleSubtypeOnPlatformUnit.getSampleSubtypeId().intValue()){
				return true;
			}
		}
		return false;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void createUpdateSequenceRun(Run runInstance, List<RunMeta> runMetaList, Integer platformUnitId, Integer resourceId)throws Exception{
		
		//first check compatibility, then perform create or update
		
		//check paramaters and parameter compatibility
		String action = new String("create");
		Sample platformUnit = null;
		Resource sequencingMachineInstance = null;
		ResourceCategory resourceCategory = null;
		Run run = null;
		
		if(runInstance==null || runMetaList==null || platformUnitId == null || platformUnitId.intValue()<=0 || resourceId==null || resourceId.intValue()<=0){
			throw new Exception("parameter error in sampleservice.createUpdateSequenceRun");
		}
		
		//database create or update
		try{//regular (rather than runtime) exceptions
			if(runInstance.getRunId()!=null && runInstance.getRunId().intValue()>0){
				run = this.getSequenceRun(runInstance.getRunId());//throws an exception if problem
				action = new String("update");
			}
			else{
				run = new Run();
			}
			platformUnit = this.getPlatformUnit(platformUnitId);//throws exception if not found in db or if not a platformUnit
			sequencingMachineInstance = this.getSequencingMachineByResourceId(resourceId);//throws exception if not found in db or if not for massively-parallel seq.
			resourceCategory = sequencingMachineInstance.getResourceCategory();
			if(resourceCategory==null || resourceCategory.getResourceCategoryId()==null || resourceCategory.getResourceCategoryId().intValue()<=0){
				throw new Exception("Problem with resourcecategory in sampleservice.createUpdateSequenceRun");
			}
			if(!this.isPlatformUnitCompatibleWithSequencingMachine(platformUnit, sequencingMachineInstance)){
				throw new Exception("platformUnit (ID: " + platformUnit.getSampleId().toString() + ") is not compatible with sequencing machine (ID: " + sequencingMachineInstance.getResourceId().toString()+").");
			}			
		}catch (Exception e){ throw e; }
		
		try{//runtime exceptions
			run.setName(runInstance.getName());//set by system
			run.setUserId(runInstance.getUserId());
			//run.setStartts(new Date());//THIS MUST CHANGE so that it's gotten from param or the runInstance object
			run.setStartts(runInstance.getStartts());
			
			run.setResourceId(sequencingMachineInstance.getResourceId());
			run.setResourceCategoryId(resourceCategory.getResourceCategoryId());
			run.setSampleId(platformUnitId);
			
			Run runDB = runDao.save(run);
			if(runDB==null || runDB.getRunId()==null || runDB.getRunId().intValue()<=0){
				throw new SampleException("new run unexpectedly not saved");
			}
			runMetaDao.updateByRunId(runDB.getRunId(), runMetaList); // persist the metadata; no way to check as this returns void
			
		}catch (Exception e){	throw new RuntimeException(e.getMessage());	}
		return;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteSequenceRun(Run run)throws Exception{
		try{
		deleteSequenceRunAndItsMeta(run);
		}catch (Exception e){	throw new RuntimeException(e.getMessage());	}
		return;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLibraryFromCellOfPlatformUnit(SampleSource cellLibraryLink) throws SampleTypeException{
		Assert.assertParameterNotNull(cellLibraryLink, "Invalid cellLibraryLink provided");
		Assert.assertParameterNotNullNotZero(cellLibraryLink.getSampleSourceId(), "Invalid cellLibraryLink provided");
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
		Assert.assertParameterNotNullNotZero(cell.getSampleId(), "Invalid Cell Provided");
		Assert.assertParameterNotNull(library, "No Library provided");
		Assert.assertParameterNotNullNotZero(library.getSampleId(), "Invalid Library Provided");
		if (!cell.getSampleType().getIName().equals("cell"))
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		if (!isLibrary(library))
			throw new SampleTypeException("Expected 'library' but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		Map<String,Integer> q = new HashMap<String,Integer>();
		q.put("sourceSampleId", library.getSampleId());
		q.put("sampleId", cell.getSampleId());
		List<SampleSource> cellLibraryLinks = sampleSourceDao.findByMap(q);
		if (cellLibraryLinks == null || cellLibraryLinks.isEmpty())
			throw new SampleParentChildException("Cell is=" + cell.getSampleId() + " and library id=" + library.getSampleId() + " are not linked");
		removeLibraryFromCellOfPlatformUnit(cellLibraryLinks.get(0));
	}
	
	private void deleteCellFromPlatformUnit(SampleSource puCellLink)throws SampleTypeException{
		Assert.assertParameterNotNull(puCellLink, "Invalid puCellLink provided");
		Assert.assertParameterNotNullNotZero(puCellLink.getSampleSourceId(), "Invalid puCellLink provided");
		Sample cell = puCellLink.getSourceSample();//cell is the lane
		if (!cell.getSampleType().getIName().equals("cell")){
			throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		}
		if(!puCellLink.getSample().getSampleType().getIName().equals("platformunit")){
			throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + puCellLink.getSample().getSampleType().getIName() + "' instead.");			
		}
		this.deleteSampleSourceAndItsMeta(puCellLink);//first, remove the samplesource link (currently this is no meta here, but if in the future there is, it will be taken care of automatically)
		this.deleteSampleAndItsMeta(cell);//second, remove the cell itself (currently this is no meta here, but if in the future there is, it will be taken care of automatically)
	}
	
	private void deletePlatformUnit(Sample platformUnit)throws SampleTypeException{
		Assert.assertParameterNotNull(platformUnit, "Invalid platformUnit provided");
		Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "Invalid platformUnit provided");
		if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		}
		this.deleteSampleBarcode(platformUnit);
		this.deleteSampleAndItsMeta(platformUnit);//currently, meta includes readlength, readType, comments
	}
	private void deleteSampleBarcode(Sample sample){
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
	private void deleteSampleSourceAndItsMeta(SampleSource sampleSource){
		Assert.assertParameterNotNull(sampleSource, "Invalid sampleSource provided");
		Assert.assertParameterNotNullNotZero(sampleSource.getSampleSourceId(), "Invalid sampleSource provided");
		for(SampleSourceMeta meta : sampleSource.getSampleSourceMeta()){
			sampleSourceMetaDao.remove(meta);
			sampleSourceMetaDao.flush(meta);
		}
		getSampleSourceDao().remove(sampleSource);
		getSampleSourceDao().flush(sampleSource);
	}
	private void deleteSampleAndItsMeta(Sample sample){
		Assert.assertParameterNotNull(sample, "Invalid sample provided");
		Assert.assertParameterNotNullNotZero(sample.getSampleId(), "Invalid sample provided");
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
		Assert.assertParameterNotNullNotZero(library.getSampleId(), "Invalid Sample Provided");
		if (!isLibrary(library)){
			throw new SampleTypeException("Expected a library but got Sample of type '" + library.getSampleType().getIName() + "' instead.");
		}
		if (!isLibraryPassQC(library))
			return false;
		int sampleActualCoverage = 0;
		Set<Sample> platformUnitsToConsider = new HashSet<Sample>();
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
		logger.debug("Library id=" + library.getSampleId() + ", name=" + library.getName() + " has requested coverage=" + requestedCoverage + " and actual coverage=" + sampleActualCoverage);
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
		for (Sample pu : findAllPlatformUnits()){
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
		Assert.assertParameterNotNullNotZero(platformUnit.getSampleId(), "Invalid platformUnit Provided");
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

	
	
	private void deleteSequenceRunAndItsMeta(Run run){
		Assert.assertParameterNotNull(run, "Invalid run provided");
		Assert.assertParameterNotNullNotZero(run.getRunId(), "Invalid run provided");
		for(RunMeta runMeta : run.getRunMeta()){
			runMetaDao.remove(runMeta);
			runMetaDao.flush(runMeta);
		}
		runDao.remove(run);
		runDao.flush(run);
		return;
	}
	
	public enum LockStatus{LOCKED,UNLOCKED,UNKOWN}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlatformUnitLockStatus(Sample platformunit, LockStatus lockStatus) throws SampleTypeException{
		Assert.assertParameterNotNull(platformunit, "platformunit cannot be null");
		Assert.assertParameterNotNull(lockStatus, "lockStatus cannot be null");
		if (!isPlatformUnit(platformunit))
			throw new SampleTypeException("sample is not a platformunit");
		SampleMeta currentLockStatusMeta = null;
		List<SampleMeta> sampleMetaList = platformunit.getSampleMeta();
		if (sampleMetaList == null)
			sampleMetaList = new ArrayList<SampleMeta>();
		try{
			currentLockStatusMeta = MetaHelper.getMetaObjectFromList(LOCK_META_AREA, LOCK_META_KEY, sampleMetaList);
			if (currentLockStatusMeta.getV().equals(lockStatus.toString())){ // no change in value
				return;
			}
		} catch(MetadataException e) {
			// doesn't exist so create
			currentLockStatusMeta = new SampleMeta();
			currentLockStatusMeta.setK(LOCK_META_AREA + "." + LOCK_META_AREA);
		}
		currentLockStatusMeta.setV(lockStatus.toString());
		sampleMetaDao.updateBySampleId(platformunit.getSampleId(), currentLockStatusMeta);
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
}