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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
public class SampleServiceImpl extends WaspServiceImpl implements SampleService {

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
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	@Autowired
	private SampleTypeDao sampleTypeDao;

	@Autowired
	private StateDao stateDao;
	
	@Autowired
	  private AdaptorDao adaptorDao;

	public void setSampleMetaDao(SampleMetaDao sampleMetaDao) {
		this.sampleMetaDao = sampleMetaDao;
	}

	@Override
	public Sample getSampleByName(final String name) {
		return this.getSampleDao().getSampleByName(name);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Sample> findAllPlatformUnits() {
		Map queryMap = new HashMap();
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
	  @Transactional
	  public void saveSampleWithAssociatedMeta(Sample sample){
		  sampleDao.save(sample);
		  sampleMetaDao.updateBySampleId(sample.getSampleId(), sample.getSampleMeta());
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId, String[] roles, String sampleTypeIName){
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
				  if (authenticationService.hasRoleInRoleArray(includedRoles, roles) && !authenticationService.hasRoleInRoleArray(excludedRoles, roles)){
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
	  public String getReceiveSampleStatus(final Sample sample){
		  
		  String sampleReceivedStatus = "UNKNOWN";
		  List<Statesample> statesamples = sample.getStatesample();
		  for(Statesample ss : statesamples){
			if(ss.getState().getTask().getIName().equals("Receive Sample")){
				sampleReceivedStatus = ss.getState().getStatus();
			}
	  	  }
		  return sampleReceivedStatus;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortSamplesBySampleName(List<Sample> samples){
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
	  public String convertReceiveSampleStatusForWeb(String internalStatus){
		  
		  if(internalStatus.equals("CREATED")){
			  return new String("NOT ARRIVED");
			}
			else if(internalStatus.equals("RECEIVED") || internalStatus.equals("COMPLETED") || internalStatus.equals("FINALIZED")){
				return new String("RECEIVED");
			}
			else if(internalStatus.equals("WITHDRAWN") || internalStatus.equals("ABANDONED") ){
				return new String("WITHDRAWN");
			}
			else if(internalStatus.equals("UNKNOWN")){
				return new String("UNKNOWN");
			}
			else{
				return new String(internalStatus);
			}
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public String convertReceiveSampleStatusForInternalStorage(String webStatus){
		  
		  if(webStatus.equals("NOT ARRIVED")){
			  return new String("CREATED");
			}
			else if(webStatus.equals("RECEIVED")){
				return new String("COMPLETED");
			}
			else if(webStatus.equals("WITHDRAWN")){
				return new String("ABANDONED");
			}
			else if(webStatus.equals("UNKNOWN")){
				return new String("UNKNOWN");
			}
			else{
				return new String(webStatus);
			}
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<String> getReceiveSampleStatusOptionsForWeb(){
		  String [] stringList = {"CREATED", "COMPLETED", "ABANDONED", "UNKNOWN"};
		  List<String> options = new ArrayList<String>();
		  for(String str : stringList){
			  options.add(convertReceiveSampleStatusForWeb(str));
		  }
		  return options;
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean updateSampleReceiveStatus(final Sample sample, final String status){
	
		  if(sample.getSampleId()==0){
			  return false;
		  }
		  List<Statesample> statesamples = sample.getStatesample();
		  for(Statesample ss : statesamples){
			if(ss.getState().getTask().getIName().equals("Receive Sample")){
				State state = ss.getState();
				String newInternalStatus = convertReceiveSampleStatusForInternalStorage(status);
				if(!state.getStatus().equals(newInternalStatus)){					
					state.setStatus(newInternalStatus);
					stateDao.save(state);
				}
			}
	  	  }
		  return true;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public boolean submittedSampleHasBeenProcessedByFacility(final Sample sample){//should but doesn't really check that this is a user-submitted sample
		  boolean sampleHasBeenProcessed = false;
		  if( sample.getSampleSourceViaSourceSampleId().size() > 0){/* submitted sample is a user-submitted library that has been placed onto a flow cell or a user-submitted macromolecule that has been used to generate a library */
			  sampleHasBeenProcessed = true;
		  }
		  return sampleHasBeenProcessed;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getFacilityGeneratedLibraries(Sample sample){
		  List<Sample> libraryList = new ArrayList<Sample>();
		  List<SampleSource> sampleSourceList = sample.getSampleSourceViaSourceSampleId();
		  for(SampleSource sampleSource : sampleSourceList){
			  libraryList.add(sampleSource.getSample());
		  }
		  return libraryList;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Adaptor getLibraryAdaptor(Sample library){
		  Adaptor adaptor = null;
		  String adaptorId = new String("");
		  try{		
			  adaptorId = MetaHelper.getMetaValue("genericLibrary", "adaptor", library.getSampleMeta());
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
	  public List<Sample> getAvailableFlowCells(){
		  
		  List<Sample> availableFlowCellList = new ArrayList<Sample>();
		  
		  SampleType sampleType = sampleTypeDao.getSampleTypeByIName("platformunit");
		  if(sampleType==null || sampleType.getSampleTypeId()==null || sampleType.getSampleTypeId().intValue()==0){
			  //exception or something
		  }
		  Map<String, Integer> filterMap = new HashMap<String, Integer>();
		  filterMap.put("sampleTypeId", sampleType.getSampleTypeId());
		  List<Sample> allFlowCellsList = sampleDao.findByMap(filterMap);
		  
		  for(Sample sample : allFlowCellsList){
			  List<Statesample> stateSampleList = sample.getStatesample();
			  for(Statesample stateSample : stateSampleList){
				  if(stateSample.getState().getStatus().equals("CREATED")){
					  availableFlowCellList.add(sample);
				  }
			  }
		  }
		  
		  return availableFlowCellList;
	  }
	  
	  public List<Sample> getAvailableAndCompatibleFlowCells(Job job){
		  List<Sample> availableFlowCells = getAvailableFlowCells();
		  List<Sample> availableAndCompatibleFlowCells = new ArrayList<Sample>();
		  for(Sample flowCell : availableFlowCells){
			  for(SampleSubtypeResourceCategory ssrc : flowCell.getSampleSubtype().getSampleSubtypeResourceCategory()){
				  for(JobResourcecategory jrc : job.getJobResourcecategory()){
					  if(ssrc.getResourcecategoryId().intValue() == jrc.getResourcecategoryId().intValue()){
						  availableAndCompatibleFlowCells.add(flowCell);
					  }
				  }
			  }
		  }
		  return availableAndCompatibleFlowCells;
	  }
	  
}
