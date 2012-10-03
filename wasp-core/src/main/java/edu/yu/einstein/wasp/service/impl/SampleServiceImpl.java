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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import edu.yu.einstein.wasp.controller.PlatformUnitController.SelectOptionsMeta;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.SampleSubtypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
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
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional
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
	private SampleBarcodeDao sampleBarcodeDao;

	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	@Autowired
	private SampleSourceDao sampleSourceDao;
	
	@Autowired
	private SampleSourceMetaDao sampleSourceMetaDao;
	
	@Autowired
	private SampleTypeDao sampleTypeDao;

	@Autowired
	private SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	private StateDao stateDao;
	
	@Autowired
	  private AdaptorDao adaptorDao;
	
	@Autowired
	private BarcodeDao barcodeDao;
	
	@Autowired
	  private TaskDao taskDao;
	
	@Autowired
	  private RunDao runDao;

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
	  public Integer getNumberOfIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException{

		  Map<Integer, Sample> indexedCells = getIndexedCellsOnPlatformUnit(platformUnit);
		  return new Integer(indexedCells.size());
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
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public List<Sample> platformUnitsAwaitingLibraries(){
		  
			Map stateMap = new HashMap(); 
			Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
			if(task == null || task.getTaskId() == null){
				//waspErrorMessage("platformunit.taskNotFound.error"); maybe throw exception?????
			}
			stateMap.put("taskId", task.getTaskId()); 	
			stateMap.put("status", "CREATED"); 
			List<State> stateList = stateDao.findByMap(stateMap);
			
			Set<Sample> samples = new HashSet<Sample>();//use set to avoid duplicates
			for(State state : stateList){
				List<Statesample> statesampleList = state.getStatesample();
				for(Statesample statesample : statesampleList){
					if(statesample.getSample().getSampleType().getIName().equals("platformunit")){
						samples.add(statesample.getSample());
					}
				}
			}
			
			return new ArrayList<Sample>(samples);//return as list rather than as set
	  }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Run getCurrentRunForPlatformUnit(Sample platformUnit) {
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
	public boolean barcodeNameExists(String barcodeName){

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
		
		return sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleSubtypeId.intValue());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	 public boolean sampleSubtypeIsSpecificSampleType(SampleSubtype sampleSubtype, String sampleTypeIName) throws SampleSubtypeException, SampleTypeException{
		if(sampleSubtype==null){throw new SampleSubtypeException("SampleSubtype is null");} 
		else if(sampleSubtype.getSampleType()==null || sampleSubtype.getSampleType().getIName()==null){throw new SampleTypeException("SampleType is null or it's iname is null");} 
		return sampleTypeIName.equals(sampleSubtype.getSampleType().getIName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Sample getSampleById(Integer sampleId){
		
		return sampleDao.getSampleBySampleId(sampleId.intValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sampleIsSpecificSampleType(Sample sample, String sampleTypeIName) throws SampleException, SampleTypeException{
		if(sample==null){throw new SampleException("Sample is null");} 
		else if(sample.getSampleType()==null || sample.getSampleType().getIName()==null){throw new SampleTypeException("SampleType is null or it's iname is null");} 
		return sampleTypeIName.equals(sample.getSampleType().getIName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sampleIsInDatabase(Sample sample){
		
		if(sample == null){return false;}
		return this.sampleIdIsInDatabase(sample.getSampleId());
	}
	 
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sampleIdIsInDatabase(Integer sampleId){
		
		if(sampleId == null || sampleId.intValue() <= 0){return false;}
		Sample sample = this.getSampleById(sampleId.intValue());
		return sample.getSampleId()!=null && sample.getSampleId().intValue() > 0?true:false;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sampleIsPlatformUnit(Sample sample){
		
		if("platformunit".equals(sample.getSampleType().getIName()) && "platformunit".equals(sample.getSampleSubtype().getSampleType().getIName())){
			return true;
		}
		return false;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getNumberOfCellsListForThisTypeOfPlatformUnit(SampleSubtype sampleSubtype) throws SampleTypeException, SampleSubtypeException{
			
		if(!sampleSubtypeIsSpecificSampleType(sampleSubtype, "platformunit")){
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
	public boolean requestedReductionInCellNumberIsProhibited(Sample platformUnitInDatabase, Integer numberOfLanesRequested) throws SampleException, SampleTypeException{
		
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
	
		String action = new String("create");
		Sample pu = null;
		SampleType sampleTypeForPlatformUnit = null;
		SampleType sampleTypeForCell = null;
		Integer numberOfLanesInDatabase = null;
		boolean platformUnitNameHasBeenChanged = false;
		
		if(sampleIsInDatabase(platformUnit)){//this is an update of an existing record
			pu = platformUnit;
			if(!sampleIsPlatformUnit(platformUnit)){
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
			System.out.println("The name of the pu sample is : " + platformUnit.getName());

			action = new String("update");			
		}
		else{//request for a new platformunit record 
			numberOfLanesInDatabase = new Integer(0);
			pu = new Sample();
		}
		
		if(!this.sampleSubtypeIsSpecificSampleType(sampleSubtype, "platformunit")){
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
			if(this.requestedReductionInCellNumberIsProhibited(pu, numberOfLanesRequested)){//value of true means libraries are assigned to those lanes being asked to be removed. Prohibit this action and inform user to first remove those libraries from the lanes being requested to be removed
				throw new SampleException("Sample Exception during platform unit update: Action not permitted at this time. To reduce the number of lanes, remove libraries on the lanes that will be lost.");
			}
		}
		
		
		if(barcodeName == null || "".equalsIgnoreCase(barcodeName)){
			throw new SampleException("Barcode Name cannot be empty");
		}
		else if(this.barcodeNameExists(barcodeName)){
			if("create".equals(action) /* this is new platformUnit, and barcode name already used, so prevent */ 
					|| ( "update".equals(action) && !barcodeName.equalsIgnoreCase(pu.getSampleBarcode().get(0).getBarcode().getBarcode()) /* existing record being updated, but barcode name used is not my barcode name, so prevent */  ) ){
				throw new SampleException("Barcode Name used by another sample");
			}
		}

		if(sampleMetaList == null){
			throw new SampleException("SampleMetaList cannot be null");
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
			//if create new platformunit or update and add addeal with the lanes during update, which could include addition of new lanes or loss of existing lanes
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
					SampleSource sampleSourceDb = this.sampleSourceDao.save(sampleSource);
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
						sampleSourceDao.remove(ss);//no additional samplesourcemeta needs to be removed here
						sampleDao.remove(cell);//no additional samplemeta needs to be removed here
					}
				}
			}
			else if(numberOfLanesRequested.intValue() == numberOfLanesInDatabase.intValue()){//do nothing
				;
			}
	
		}catch (Exception e){	throw new RuntimeException(e.getMessage());	}
		
		return;
	}
		
}
