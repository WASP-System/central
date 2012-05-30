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
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleDuplicationException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
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
	
	private AuthenticationService authenticationService;
	
	
	@Override
	@Autowired
	public void setAuthenticationService(AuthenticationService authenticationService){
		this.authenticationService = authenticationService;
	}
	
	@Autowired
	private WorkflowDao workflowDao;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	@Autowired
	private SampleSourceDao sampleSourceDao;
	
	@Autowired
	private SampleSourceMetaDao sampleSourceMetaDao;
	
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
		// TODO: Write test!!
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
	  public String convertReceiveSampleStatusForWeb(String internalStatus){
		  // TODO: Write test!!
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
		  // TODO: Write test!!
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
		  // TODO: Write test!!
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
		  // TODO: Write test!!
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
					break;
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
		// TODO: Write test!!
		  boolean sampleHasBeenProcessed = false;
		  if( sample.getSourceSampleId().size() > 0){/* submitted sample is a user-submitted library that has been placed onto a flow cell or a user-submitted macromolecule that has been used to generate a library */
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
		  SampleType libraryType = sampleTypeDao.getSampleTypeByIName("library");
		  List<Sample> libraryList = new ArrayList<Sample>();
		  if (sample.getChildren() != null){
			  for (Sample childSample : sample.getChildren()){
				  if (childSample.getSampleType().equals(libraryType)){
					  libraryList.add(childSample);
				  }
			  }
		  } else {
			  logger.debug("No facility generated libraries found");
		  }
		  return libraryList;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Adaptor getLibraryAdaptor(Sample library){
		// TODO: Write test!!
		  Adaptor adaptor = null;
		  String adaptorId = new String("");
		  SampleSubtype sampleSubtype = library.getSampleSubtype();
		  String areaList = sampleSubtype.getAreaList();
		  String area = new String("");
		  
		  String [] stringList = areaList.split("[\\s,]+");//separates on comma or whitespace
		  for(String string : stringList){
			  //System.out.println("The string is: " + string);
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
	  public List<Sample> getAvailableFlowCells(){
		// TODO: Write test!!
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
		  // TODO: Write test!!
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
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Sample getPlatformUnitForCell(Sample cell) throws SampleTypeException, SampleParentChildException{
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  if (cell.getSampleSource()==null || cell.getSampleSource().isEmpty())
			  throw new SampleParentChildException("Cell '"+cell.getSampleId().toString()+"' is associated with no flowcells");
		  if (cell.getSampleSource().size() > 1)
			  throw new SampleParentChildException("Cell '"+cell.getSampleId().toString()+"' is associated with more than one flowcell");
		  SampleSource ss = cell.getSampleSource().get(0);
		  return ss.getSample();
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Map<Integer, Sample> getIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException{
		  if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			  throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		  }
		  
		  Map<Integer, Sample> indexedCells = new HashMap<Integer, Sample>();
		  if (platformUnit.getSampleSource() == null)
			  return indexedCells;
		  
		  for (SampleSource ss : platformUnit.getSampleSource()){
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
	  public void addCellToPlatformUnit(Sample platformUnit, Sample cell, Integer index) throws SampleTypeException, SampleIndexException{
		  if (!platformUnit.getSampleType().getIName().equals("platformunit")){
			  throw new SampleTypeException("Expected 'platformunit' but got Sample of type '" + platformUnit.getSampleType().getIName() + "' instead.");
		  }
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  if (index == null || index < 1)
			  throw new SampleIndexException("index must be an integer >= 1");
		  Map<String, Integer> sampleSourceQuery = new HashMap<String, Integer>();
		  sampleSourceQuery.put("sampleId", platformUnit.getSampleId());
		  sampleSourceQuery.put("index", index);
		  if (sampleSourceDao.findByMap(sampleSourceQuery) != null)
			  throw new SampleIndexException("index '"+index+"' already assigned to a cell associated with this platform unit");
		  SampleSource sampleSource = new SampleSource();
		  sampleSource.setSample(platformUnit);
		  sampleSource.setSourceSample(cell);
		  sampleSource.setIndex(index);
		  sampleSourceDao.persist(sampleSource);
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> getLibrariesOnCell(Sample cell) throws SampleTypeException{
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
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  List<Sample> libraries = new ArrayList<Sample>();
		  if (cell.getSampleSource() == null)
			  return libraries;
		  
		  for (SampleSource ss : cell.getSampleSource()){
			  Sample library = ss.getSourceSample();
			  if (!library.getSampleType().getIName().equals("library") && !library.getSampleType().getIName().equals("controlLibrarySample")){
				  throw new SampleTypeException("Expected 'library' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
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
		  if (!cell.getSampleType().getIName().equals("cell")){
			  throw new SampleTypeException("Expected 'cell' but got Sample of type '" + cell.getSampleType().getIName() + "' instead.");
		  }
		  if (!library.getSampleType().getIName().equals("library")){
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
		  newSampleSource = sampleSourceDao.save(newSampleSource);//capture the new samplesourceid
		  
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
	  public SampleDraft cloneSampleDraft(final SampleDraft sampleDraft){
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
	  
	  
	  
}
