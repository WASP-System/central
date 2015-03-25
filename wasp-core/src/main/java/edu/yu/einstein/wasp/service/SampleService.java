/**
 *
 * SampleService.java 
 * 
 * the SampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
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
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl.LockStatus;
import edu.yu.einstein.wasp.util.SampleWrapper;


@Service
public interface SampleService extends WaspMessageHandlingService {
	
	public static final String STATUS_PASSED = "PASSED";
	public static final String STATUS_FAILED = "FAILED";

	/**
	 * setSampleDao(SampleDao sampleDao)
	 * 
	 * @param sampleDao
	 * 
	 */
	public void setSampleDao(SampleDao sampleDao);

	/**
	 * getSampleDao();
	 * 
	 * @return sampleDao
	 * 
	 */
	public SampleDao getSampleDao();

	public Sample getSampleByName(final String name);

	List<Sample> getPlatformUnits();

	  /**
	   * Gets a list of {@link SampleSubtype} objects associated with given workflow which are specified as viewable
	   * to the currently logged in user (role dependent)
	   * If no role annotation is provided for the SampleSubtype it is also returned
	   * @param workflowId
	   * @return List<{@link SampleSubtype}>
	   */
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId);

	  /**
	   * Gets a list of {@link SampleSubtype} objects associated with given workflowId and sampleTypeId which are specified as viewable
	   * to the currently logged in user (role dependent)
	   * If no role annotation is provided for the SampleSubtype it is also returned
	   * @param workflowId
	   * @return List<{@link SampleSubtype}>
	   */
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId, String sampleTypeIName);


	  /**
	   * Gets a list of {@link SampleSubtype} objects associated with given workflowId and sampleTypeId which are specified as viewable
	   * to the users with given roles (role dependent). 
	   * If no role annotation is provided for the SampleSubtype it is also returned
	   * @param workflowId
	   * @param roles
	   * @param sampleTypeIName
	   * @return
	   */
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId,	String[] roles, String sampleTypeIName);

	  /**
	   * Gets a list of {@link SampleSubtype} objects associated with given workflowId which are specified as viewable
	   * to the users with given roles (role dependent)
	   * If no role annotation is provided for the SampleSubtype it is also returned
	   * @param workflowId
	   * @param roles
	   * @return
	   */
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId,	String[] roles);

	  /**
	   * Returns true unless a sample exists in job of the same sampleType with the same name as the provided Sample
	   * @param Sample
	   * @param sampleType
	   * @param job
	   * @return boolean
	   */
	  public boolean isSampleNameUniqueWithinJob(Sample sampleIn, SampleType sampleType, Job job);

	  /**
	   * Saves a sample and all the metadata associated with it
	   * @param sample
	   */
	  public void saveSampleWithAssociatedMeta(Sample sample);

	  /**
	   * Returns status of a sample's Receive Sample step
	   * @param Sample
	   * @return String
	   */
	  public ExitStatus getReceiveSampleStatus(final Sample sample);
	  
	  /**
	   * Returns status of a sample's QC Sample step
	   * @param sample
	   * @return
	   */
	  public ExitStatus getSampleQCStatus(Sample sample);
	  
	  /**
	   * Returns status of a library's QC Sample step
	   * @param library
	   * @return
	   */
	  public ExitStatus getLibraryQCStatus(Sample library);
	  
	  /**
		 * Returns true if provided sample is received, otherwise returns false
		 * @param sample
		 * @return
		 */
		public Boolean isSampleReceivedOrWithdrawn(Sample sample);
		
		/**
		 * Returns true if sample and no library recorded as currently being processed or successfully made 
		 * @param sample
		 * @return
		 */
		public Boolean isSampleAwaitingLibraryCreation(Sample sample);
	  
	  /**
	   * Accepts and (in-situ) sorts list of samples by sample name 
	   * @param List<Sample>
	   * @return void
	   */
	  public void sortSamplesBySampleName(List<Sample> samples);

	  /**
	   * Accepts and (in-situ) sorts list of samples by sample id 
	   * @param List<Sample>
	   * @return void
	   */
	  public void sortSamplesBySampleId(List<Sample> samples);

	  /**
	   * Converts sample's Receive Sample status from database storage meaning to human-comprehensible meaning for viewing on the web
	   * @param String status
	   * @return String 
	   */
	  public String convertSampleReceivedStatusForWeb(ExitStatus internalStatus);
	  
	  /**
	   * Gets list of Receive Sample options for web display
	   * @param none
	   * @return List<String> containing the list of Receive Sample Options for web display 
	   */
	  public List<String> getReceiveSampleStatusOptionsForWeb();
	  
	  /**
	   * Converts sample's Receive Sample status from database storage meaning to human-comprehensible meaning for viewing on the web
	   * @param String status
	   * @return String 
	   */
	  public String convertSampleQCStatusForWeb(ExitStatus internalStatus);
	  
	  /**
	   * Converts sample's QC Sample status from human-comprehensible meaning for viewing on the web to a WaspStatus
	   * @param webStatus
	   * @return
	   */
	  public WaspStatus convertSampleQCStatusFromWeb(String webStatus);
	  
	  /**
	   * Updates sample's Receive Sample status. Sends message via Spring Integration
	   * Status must be either CREATED or ABANDONED
	   * @param Sample sample
	   * @param String status (from web)
	   * @return boolean
	   */
	  public void updateSampleReceiveStatus(final Sample sample, final WaspStatus status) throws WaspMessageBuildingException;
	    
	  
	  /**
	   * Returns boolean informing whether a sample has been processed by the facility
	   * If a macromolecule was submitted to the facility and a library was created from it, then return true
	   * If a library was submitted to the facility and it has been added to a flowcell, then return true
	   * Else return false
	   * @param Sample sample
	   * @return boolean
	   */
	  public boolean isSubmittedSampleProcessedByFacility(final Sample sample);
	  
	  /**
	   * Returns a list of facility-generated libraries created from a user-submitted macromolecule
	   * @param Sample
	   * @return List<Sample>
	   */
	  public List<Sample> getFacilityGeneratedLibraries(Sample sample);
	 
	  /**
	   * Returns Adaptor for any library
	   * @param Sample
	   * @return Adaptor
	   */
	  public Adaptor getLibraryAdaptor(Sample library);
	  
	  /**
	   * Returns list of samples that are flow cells to which libraries can be added (no run flow exists, or run flow in 'STOPPED' state and unlocked)
	   * @param void
	   * @return List<Sample>
	   */
	  public List<Sample> getAvailablePlatformUnits();
	  
	  /**
	   * Returns list of samples that are flow cells to which libraries can be added AND are compatible with the supplied {@link ResourceCategory}
	   * @param ResourceCategory resourceCategory
	   * @return List<Sample>
	   * 
	   */
	  public List<Sample> getAvailableAndCompatiblePlatformUnits(ResourceCategory resourceCategory);
	  
	  /**
	   * Returns list of samples that are flow cells to which libraries can be added AND are compatible with the supplied job 
	   * with respect to having a common {@link ResourceCategory}
	   * @param Job job
	   * @return List<Sample>
	   * 
	   */
	  public List<Sample> getAvailableAndCompatiblePlatformUnits(Job job);

	  
	  /**
	   * Returns Map of Cells according to their index for a platform unit
	   * @param platformUnit
	   * @return Map<Integer, Sample> indexedCells
	   * @throws SampleTypeException 
	   */
	  public Map<Integer, Sample> getIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException;

	  /**
	   * Returns Number Of Indexed Cells (cells) on a platform unit
	   * @param platformUnit
	   * @return Integer numberOfIndexedCells
	   * @throws SampleTypeException 
	   */
	  public Integer getNumberOfIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException;

	  /**
	   * Adds a cell to a platform unit. Ensures that the index is unique.
	   * @param platformUnit
	   * @param cell
	   * @param Index
	   * @throws SampleTypeException
	   * @throws SampleIndexException
	   */
	  public void addCellToPlatformUnit(Sample platformUnit, Sample cell, Integer Index) throws SampleTypeException, SampleIndexException;

	  /**
	   * Returns list of Samples on a cell. If control libraries are spiked in, these are also returned.
	   * @param cell
	   * @return list of libraries (including spike-in controls)
	   * @throws SampleTypeException
	   */
	  public List<Sample> getLibrariesOnCell(Sample cell) throws SampleTypeException;

	  /**
	   * Returns list of Samples on a cell. If control libraries are spiked in, these are ignored and NOT returned.
	   * @param cell
	   * @return list of libraries (without spike-in controls)
	   * @throws SampleTypeException
	   */
	  public List<Sample> getLibrariesOnCellWithoutControls(Sample cell) throws SampleTypeException;

	  /**
	   * Returns a list of spiked-in libraries on a cell
	   * @param cell
	   * @return list of control libraries
	   * @throws SampleTypeException
	   */
	  public List<Sample> getControlLibrariesOnCell(Sample cell) throws SampleTypeException;

	  /**
	   * Set the authentication service
	   * @param authenticationService
	   */
	  public void setAuthenticationService(AuthenticationService authenticationService);

	  /**
	   * Get a cell for a given platform unit
	   * @param cell
	   * @return
	   * @throws SampleParentChildException 
	   * @throws SampleTypeException 
	   */
	  public Sample getPlatformUnitForCell(Sample cell) throws SampleTypeException, SampleParentChildException;

	  /**
	   * Adds a given library to the given cell
	   * @param cell
	   * @param library
	   * @param libConcInCellPicoM
	   * @throws SampleTypeException
	   * @throws SampleException
	   * @throws SampleMultiplexException
	   * @throws MetadataException
	   */
	  public void addLibraryToCell(Sample cell, Sample library,	Float libConcInCellPicoM) throws SampleTypeException, SampleException, SampleMultiplexException, MetadataException;

	  /**
	   * Clones a sampleDraft object
	   * @param sampleDraft
	   * @return
	   */
	  public SampleDraft cloneSampleDraft(SampleDraft sampleDraft);

	  
	  /**
	   * Get the run on which a given platform unit has been placed. If the platform unit is not currently associated with an active run or is not associated with a run 
	   * this method returns null
	   * @param platformUnit
	   * @return
	   * @throws SampleTypeException
	   */
	  public Run getCurrentRunForPlatformUnit(Sample platformUnit) throws SampleTypeException;
	  

	  /**
	   * returns true if sample is DNA, RNA, Protein, a library (or facilityLibrary)
	   * @param sample
	   * @return
	   */
	  public boolean isBiomolecule(Sample sample);
	  
	  /**
	   * returns true if sampleDraft is DNA, RNA, Protein, a library (or facilityLibrary)
	   * @param sample
	   * @return
	   */
	  public boolean isBiomolecule(SampleDraft sampleDraft);
		
	  
	  /**
	   * returns true if sample is a library (or facilityLibrary)
	   * @param sample
	   * @return
	   */
	  public boolean isLibrary(Sample sample);
	  
	  /**
	   * returns true if sampleDraft is a library (or facilityLibrary)
	   * @param sample
	   * @return
	   */
	  public boolean isLibrary(SampleDraft sampleDraft);
	  
	  /**
	   * Determine whether a barcodeName is already in the database. 
	   * @param String barcodeName
	   * @return boolean
	   */
	  public boolean isBarcodeNameExisting(String barcodeName);

	  /**
	   * Returns List of SampleSubtypes where SampleType.iname = parameter sampleTypeIName. List ordered ascending by samplesubtype.name. 
	   * If the SampleType is not found, throw SampleTypeException. If SampleType is valid but not entries in SampleSubtype, then
	   * return is an empty list (list.size()=0)
	   * @param String sampleTypeIName
	   * @return List<SampleSubtype>
	   */
	  public List<SampleSubtype> getSampleSubtypesBySampleTypeIName(String sampleTypeIName) throws SampleTypeException;

	  /**
	   * Returns a SampleSubtype with id of sampleSubtypeId. 
	   * If the id not in database, return empty object (sampleSubtype.getSampleSubtypeId is null).
	   * @param Integer sampleSubtypeId
	   * @return SampleSubtype
	   */
	  public SampleSubtype getSampleSubtypeById(Integer sampleSubtypeId);

	  /**
	   * Returns true if SampleSubtype's SampleType has iname of sampleTypeIName, else return false. 
	   * @param SampleSubtype sampleSubtype
	   * @param String sampleTypeIName
	   * @return boolean
	   */
	  public boolean isSampleSubtypeWithSpecificSampleType(SampleSubtype sampleSubtype, String sampleTypeIName);
	  
	  /**
	   * Returns a Sample with id of sampleId. 
	   * If the id is not in the database, return an empty object (sample.getSampleId is null). 
	   * @param Integer sampleId
	   * @return Sample
	   */
	  public Sample getSampleById(Integer sampleId);

	  /**
	   * Returns true if sample.getSampleType().getIName.equals(sampleTypeIName). 
	   * If sampleTypeIName==null or sample==null or sample.getSampleType()==null or sample.getSampleType().getIName == null, return false.
	   * @param Sample sample
	   * @param String sampleTypeIName (such as "platformunit")
	   * @return boolean
	   */
	  public boolean isSampleOfSpecificSampleType(Sample sample, String sampleTypeIName);

	  /**
	   * Returns true if sample.getSampleSubtype().getIName.equals(sampleSubtypeIName). 
	   * If sampleSubtypeIName==null or sample==null or sample.getSampleSubtype()==null or sample.getSampleSubtype().getIName == null, return false.
	   * @param Sample sample
	   * @param String sampleSubtypeIName (such as "platformunit")
	   * @return boolean
	   */
	  public boolean isSampleOfSpecificSampleSubtype(Sample sample, String sampleSubtypeIName);

	  /**
	   * Returns true if Sample is in database, else returns false
	   * True defined as sample != null && sample.getSampleId() != null && sample.getSampleId().intVal() > 0
	   * then use sample.getSampleId().intVal() to pull a sample from the database and the 
	   * sampleReturnedFromDatabase.getSampleId() != null && sampleReturnedFromDatabase.getSampleId().intVal > 0 
	   * @param Sample sample
	   * @return boolean
	   */
	  public boolean isSampleInDatabase(Sample sample);

	  /**
	   * Returns true if SampleId represents a sample in database, else returns false
	   * True defined as sampleId != null && sampleId.intVal() > 0
	   * then use sampleId().intVal() to pull a sample from the database and the 
	   * sampleReturnedFromDatabase.getSampleId() != null && sampleReturnedFromDatabase.getSampleId().intVal > 0 
	   * @param Integer sampleId
	   * @return boolean
	   */
	  public boolean isSampleIdInDatabase(Integer sampleId);

	  /**
	   * Returns true if sample != null && sample.getSampleId() != null && sample.getSampleId().intVal() > 0, else false
	   * @param Sample sample
	   * @return boolean
	   */
	  public boolean isSampleIdValid(Sample sample);

	  /**
	   * Returns true if Sample is a platform unit (checking both SampleType and SampleSubtype) 
	   * @param Sample sample
	   * @return boolean
	   */
	  public boolean isPlatformUnit(Sample sample);

	  /**
	   * Returns Sample if it exists in database and if it is a platform unit (its sampletype and samplesubtype both have inames of platformunit) 
	   * If sample not found in database or it is not a platformunit in sampletype and samplesubtype, throw NumberFormatException, SampleException, SampleTypeException, or SampleSubtype Exception. 
	   * @param Sample sample
	   * @return Sample
	   */
	  public Sample getPlatformUnit(Integer sampleId) throws NumberFormatException, SampleException, SampleTypeException, SampleSubtypeException;

	  /**
	   * Returns SampleSubtype if it exists in database and if its sampletype is a platform unit 
	   * If sampleSubtype not found in database or if it is not of sampletype platformunit, throws NumberFormatException or SampleSubtype Exception. 
	   * @param SampleSubtype sampleSubtype
	   * @return SampleSubtype
	   */
	  public SampleSubtype getSampleSubtypeConfirmedForPlatformunit(Integer sampleSubtypeId) throws NumberFormatException, SampleSubtypeException;

	  
	  /**
	   * Returns an ordered (ascending) List Of Integers of the number of cells that are available on a particular type of platformunit (flowcell). 
	   * If the SampleSubtype is not in the database or is not of type platformunit, throw SampleSubtypeException or SampleTypeException, respectively.
	   * If the SampleSubtypeMetadata for maxCellNumber is not found throw a SampleSubtypeException.
	   * If the values for maxCellNumber, and if found multiplicationFactor, are not convertable to numbers, throw NumberFormatException.
	   * If the values for maxCellNumber and if found multiplicationFactor, are unable to generate a list throw a SampleSubtypeException
	   * @param SampleSubtype sampleSubtype
	   * @return List<Integer>
	   */
	  public List<Integer> getNumberOfCellsListForThisTypeOfPlatformUnit(SampleSubtype sampleSubtype) throws SampleTypeException, SampleSubtypeException;

	  /**
	   * Returns true if requested reduction in number of cells of a platformunit will lead to loss of cells containing libraries 
	   * @param Sample sample
	   * @param Integer numberOfCellsRequested
	   * @return boolean
	   */
	  public boolean isRequestedReductionInCellNumberProhibited(Sample platformUnitInDatabase, Integer numberOfCellsRequested) throws SampleException, SampleTypeException;
	 
	  /**
	   * Create or update platform unit. If platformUnitId==null or platformUnitId.intVal()<=0, create new platformunit, otherwise update.
	   * If create/update is unsuccessful, throw exception, else return void. Under transactional control. 
	   * If this is an update and numberOfCellsRequested > numberOfCellsInDatabase, then add additional cells.
	   * If this is an update and  numberOfCellsRequested < numberOfCellsInDatabase, then remove extra cells ONLY IF THE LANES TO BE REMOVED DO NOT CONTAIN LIBRARIES.
	   * @param Sample platformUnit
	   * @param Sample barcodeName
	   * @param Integer numberOfCellsRequested
	   * @param List<SampleMeta> sampleMetaList
	   * @param SampleSubtype sampleSubtype
	   * @return void
	   */
	  public void createUpdatePlatformUnit(Sample platformUnit, SampleSubtype sampleSubtype, String barcodeName, Integer numberOfCellsRequested, List<SampleMeta> sampleMetaList) throws SampleException, SampleTypeException, SampleSubtypeException;
		
	  /**
	   * Deletes platform unit 
	   * @param Sample sample
	   * @return void
	   */
	  public void deletePlatformUnit(Integer platformUnitId) throws NumberFormatException, SampleException, SampleTypeException, SampleSubtypeException;
	  
	  /**
		 * Returns true if the actual coverage of provided library sample on currently running or successfully completed flowcells is less than the requested coverage. 
		 * @param sample
		 * @return
		 */
		public boolean isLibraryAwaitingPlatformUnitPlacement(Sample library) throws SampleTypeException;
		
		/**
		 * Returns true if provided platform unit sample is in a state of awaiting placement on a sequencing run, otherwise returns false
		 * @param sample
		 * @return
		 */
		public boolean isPlatformUnitAwaitingSequenceRunPlacement(Sample platformUnit) throws SampleTypeException;

	  /**
	   * Gets list of all massively-parallel sequencing machines 
	   * @return List<Resource>
	   */
	  public List<Resource> getAllMassivelyParallelSequencingMachines();
	  
	  /**
	   * Gets list of all massively-parallel sequencing machines compatible with platformUnit (actually compatible with the platformUnit's sampleSubtype)
	   * @param Sample platformUnit
	   * @return List<Resource>
	   */
	  public List<Resource> getSequencingMachinesCompatibleWithPU(Sample platformUnit) throws SampleException;

	  /**
	   * Gets sequencing machine (resource) given resourceId
	   * @param Integer resourceId
	   * @return Resource
	   */
	  public Resource getSequencingMachineByResourceId(Integer resourceId) throws ResourceException;

	    
	 
	  
	  /**
	   * Determine whether the samplesubtype of a platformunit (ie.: the type of flowcell) is compatible with a mps sequencing machine
	   * @param Sample platformUnit
	   * @param Resource sequencingMachineInstance
	   * @return boolean
	   */
	  public boolean isPlatformUnitCompatibleWithSequencingMachine(Sample platformUnit, Resource sequencingMachineInstance);
	  
	  
	  /**
	   * is sampleDraft a DNA or RNA molecule
	   * @param sampleDraft
	   * @return
	   */
	  public boolean isDnaOrRna(SampleDraft sampleDraft);
	  
	  /**
	   * is sample a DNA or RNA molecule
	   * @param sampleDraft
	   * @return
	   */
	  public boolean isDnaOrRna(Sample sample);
	  
	  /**
	   * set the lock status of a platform unit
	   * @param platformunit
	   * @param lockStatus
	   * @throws SampleTypeException
	 * @throws MetadataException 
	   */
	  public void setPlatformUnitLockStatus(Sample platformunit, LockStatus lockStatus) throws SampleTypeException, MetadataException;
	  
	  /**
	   * Get the current lock status for a platform unit or LockStatus.UNKNOWN if not set
	   * @param platformunit
	   * @return
	   * @throws sampleTypeException
	   */
	  public LockStatus getPlatformUnitLockStatus(Sample platformunit) throws SampleTypeException;

	  /**
	   * kick of batch job for sample
	   * @param job
	   * @param sample
	   * @param batchJobName
	   */
	  void initiateBatchJobForSample(Job job, Sample sample, String batchJobName);
	  
	  /**
	   * Kick off batch job for library
	   * @param job
	   * @param library
	   * @param batchJobName
	   */
	  public void initiateBatchJobForLibrary(Job job, Sample library, String batchJobName);

	  /**
	   * Get cells onto which the current library is placed
	   * @param library
	   * @return
	   * @throws SampleTypeException 
	   */
	  public List<Sample> getCellsForLibrary(Sample library) throws SampleTypeException;

	  /**
	   * Gets the requested coverage for the specified sample i.e. how many cells is the sample to be run on
	   * @param sample
	   * @return
	   */
	  public int getRequestedSampleCoverage(Sample sample);

	  /**
	   * returns a list of currently running or successfully run platform units
	   * @return
	   */
	  public List<Sample> getRunningOrSuccessfullyRunPlatformUnits();

	  /**
	   * updates QC status of a sample / library
	   * @param sample
	   * @param status
	   * @throws WaspMessageBuildingException
	   */
	  public void updateQCStatus(Sample sample, WaspStatus status) throws WaspMessageBuildingException;

	  /**
	   * Adds facility library to the database and starts a library flow for it in the wasp-daemon
	   * @param job
	   * @param managedLibrary
	   * @param libraryMetaList
	   */
	  public void createFacilityLibraryFromMacro(Job job, SampleWrapper managedLibrary,	List<SampleMeta> libraryMetaList);

	  /**
	   * Returns true if there is a sample QC task for the given sample and it is batch state 'completed'
	   * @param sample
	   * @return
	   */
	  public boolean isSamplePassQC(Sample sample);

	  /**
	   * Returns true if there is a library QC task for the given sample and it is batch state 'completed'
	   * @param library
	   * @return
	   */
	  public boolean isLibraryPassQC(Sample library);

	  /**
	   * Returns a list of platform units that are not currently put on a run
	   * @return
	   */
	  public List<Sample> getPlatformUnitsNotYetRun();

	  /**
	   * Removes library from cell of a platform unit
	   * @param cellLibraryLink
	   * @throws SampleTypeException
	   */
	  void removeLibraryFromCellOfPlatformUnit(SampleSource cellLibraryLink) throws SampleTypeException;
	  
	  /**
	   * Removes library from cell of a platform unit
	   * @param cellLibraryLink
	   * @throws SampleTypeException
	   * @throws SampleParentChildException 
	   */
	  void removeLibraryFromCellOfPlatformUnit(Sample cell, Sample library) throws SampleTypeException, SampleParentChildException;

	  /**
		 * save a Sample QC Comment
		 * @param Integer sampleId
		 * @param String comment
		 * @return void
		 * @throws Exception
		 */
	  public void setSampleQCComment(Integer sampleId, String comment)throws Exception;

	  public List<MetaMessage> getCellLibraryQCComments(Integer sampleSourceId);
	  
	  /**
		 * save an InAggregateAnalysis comment
		 * @param Integer sampleSourceId
		 * @param String comment
		 * @return void
		 * @throws Exception
		 */
	  public void setCellLibraryQCComment(Integer sampleSourceId, String comment)throws Exception;

		/**
		 * get all sampleQC comments for a particular sample (supposedly chronologically ordered)
		 * @param Integer sampleId
		 * @return List<MetaMessage>
		 */
	  public List<MetaMessage> getSampleQCComments(Integer sampleId);


	  /**
	   * Returns true if the sample is a cell
	   * @param cell
	   * @return
	   */
	  public boolean isCell(Sample cell);



	  public boolean isControlLibrary(Sample library);


	  /**
		 * updateExistingSampleViaSampleWrapperWebapp
		 * @param List<MetaMessage> sampleMetaList
		 * @return void
		 */
	  public void updateExistingSampleViaSampleWrapper(SampleWrapper sw, List<SampleMeta> sampleMetaList);
	  
	  
	  /**
	   * Set the concentration of library added to a cell (as SampleSource metadata)
	   * @param cellLibrary
	   *  @param valueInPicoM
	   * @throws MetadataException
	   */
	  public void setLibraryOnCellConcentration(SampleSource cellLibrary, Float valueInPicoM) throws MetadataException;

	  /**
	   * Set the concentration of library added to a cell (as SampleSource metadata)
	   * @param cell
	   * @param library
	   * @param valueInPicoM
	   * @throws SampleTypeException
	   * @throws MetadataException
	   */
	  public void setLibraryOnCellConcentration(Sample cell, Sample library, Float valueInPicoM) throws SampleException, MetadataException;
	  
	  /**
	   * get the concentration of library added to the cell
	   * @param cell
	   * @param library
	   * @return
	   * @throws SampleException
	   */
	  public Float getConcentrationOfLibraryAddedToCell(Sample cell, Sample library) throws SampleException;
	  
	  /**
	   * Record the job associated with the library added to the cell (as SampleSource metadata)
	   * @param cellLibrary
	   * @throws SampleTypeException
	   * @throws MetadataException
	   */
	  public void setJobForLibraryOnCell(SampleSource cellLibrary) throws MetadataException;
	  
	  /**
	   * Record the job associated with the library added to the cell (as SampleSource metadata)
	   * @param cell
	   * @param library
	   * @throws SampleTypeException
	   * @throws MetadataException
	   */
	  public void setJobForLibraryOnCell(Sample cell, Sample library) throws SampleTypeException, MetadataException;
	  
	  /**
	   * get the job of the library on a cell
	   * @param cell
	   * @param library
	   * @return
	   * @throws SampleException
	   */
	  public Job getJobOfLibraryOnCell(Sample cell, Sample library) throws SampleException;
	  
	  /**
	   * get the job of the library on a cell
	   * @param cellLibrary
	   * @return
	   */
	  Job getJobOfLibraryOnCell(SampleSource cellLibrary);

	  
	  /**
	   * Gets a SampleSource object representing cell / library relationships associated with given job
	   * @param job
	   * @return
	   */
	  public Set<SampleSource> getCellLibrariesForJob(Job job);
	  
	  /**
	   * Retrieve a SampleSource object which contains the relationship between and Library and Cell
	   * @param cell
	   * @param library
	   * @return
	   * @throws SampleTypeException
	   */
	  public SampleSource getCellLibrary(Sample cell, Sample library) throws SampleTypeException;

	  /**
	   * Retrieve the Cell object 
	   * @param cellLibrary
	   * @return
	   */
	  public Sample getCell(SampleSource cellLibrary);

	  /**
	   * Retrieve the library object
	   * @param cellLibrary
	   * @return
	   */
	  public Sample getLibrary(SampleSource cellLibrary);

	  /**
	   * Retrieve a list of samples that are designed as controls (encoded in a job's SamplePair Meta) for a specific sample in a specific job
	   * Example: samplePairs = 273:272;274:272;276:275;277:275; for job 42
	   * So getControlSamplesForAJobsSample(Job job 42, Sample sample 273) will return a list containing Sample 273
	   * @param Job job
	   * @param Sample sample
	   * @return List<Sample>
	   */
	  public List<Sample> getControlSamplesForAJobsSample(Job job, Sample sample);

	  /**
	   * Returns true if cell marked as being sequenced successfully otherwise false is returned. 
	   * Throws a MetaAttributeNotFoundException if the value is not set
	   * @param cell
	   * @return
	   * @throws SampleTypeException
	 * @throws MetaAttributeNotFoundException 
	   */
	  public boolean isCellSequencedSuccessfully(Sample cell) throws SampleTypeException, MetaAttributeNotFoundException;

	  /**
	   * Sets a cell to have been sequenced successfully or not. This value should be set by the facility manager on 
	   * assessment of a run
	   * @param cell
	   * @param success
	   * @throws SampleTypeException
	   * @throws MetadataException 
	   */
	  public void setCellSequencedSuccessfully(Sample cell, boolean success) throws SampleTypeException, MetadataException;
	  
	  /**
	   * get cellLibrary pre-processing status (returns NOOP if analysis was not selected)
	   * @param cellLibrary
	   * @return boolean
	   * @throws SampleTypeException
	   */
	  public ExitStatus getCellLibraryPreprocessingStatus(SampleSource cellLibrary) throws SampleTypeException;
			
	  /**
		 * has cellLibrary passed QC?
		 * @param SampleSource
		 * @throws SampleTypeException
		 * @throws MetaAttributeNotFoundException
		 */
	  public boolean isCellLibraryPassedQC(SampleSource cellLibrary) throws SampleTypeException, MetaAttributeNotFoundException;
		
	  /**
	   * Creates samplesource metadata to record whether cellLibrary has passed QC
	   * @param cellLibrary
	   * @param isPassedQC
	   * @return void
	   * @throws SampleTypeException
	   * @throws MetadataException
	   */
	  public void setCellLibraryPassedQC(SampleSource cellLibrary, boolean isPassedQC) throws SampleTypeException, MetadataException;

	  public SampleSourceDao getSampleSourceDao();


	  public List<SampleSource> getCellLibrariesThatPassedQCForJob(Job job) throws SampleTypeException;
	  public List<SampleSource> getCellLibrariesPassQCAndNoAggregateAnalysis(Job job) throws SampleTypeException;
	  
	  /**
		 * has cellLibrary passed QC?
		 * @param SampleSource
		 * @throws SampleTypeException
		 * @throws MetaAttributeNotFoundException
		 */
	 public void setJobByTestAndControlSamples(Sample testSample, Sample controlSample) throws SampleException, MetadataException;

	public Job getJobByTestAndControlSamples(Sample testSample, Sample controlSample) throws SampleException;

	public Job getJobBySamplePair(SampleSource samplePair);

	public Set<SampleSource> getSamplePairsByJob(Job job);

	public SampleSource getSamplePair(Sample testSample, Sample controlSample) throws SampleTypeException;

	public Sample getTestSample(SampleSource samplePair);

	public Sample getControlSample(SampleSource samplePair);

	public List<Sample> getControlSamplesByTestSample(Sample testSample);

	public void createTestControlSamplePairsByIds(Integer testSampleId, Integer controlSampleId, Job job) throws SampleTypeException, SampleException, MetadataException;

	/**
	 * Returns a map containing all cell-libraries associated with a job and current pre-processing status
	 * @param job
	 * @return
	 */
	public Map<SampleSource, ExitStatus> getCellLibrariesWithPreprocessingStatus(Job job);
	
	/**
	 * has save both the in_aggregate_analysis meta and a comment meta
	 * @param SampleSource cell library
	 * @param String qcStatus (values of PASS OR FAIL only)
	 * @param String comment
	 * @throws Runtime exception
	 */
	public void saveCellLibraryQCStatusAndComment(SampleSource cellLibrary, String qcStatus, String comment);

	/** 
	 * Get the index of the cell (the position on it's associated platform unit)
	 * @param cell
	 * @return
	 * @throws SampleTypeException
	 * @throws SampleParentChildException
	 */
	public Integer getCellIndex(Sample cell) throws SampleTypeException, SampleParentChildException;

	/**
	 * gets the view link for displaying a platformunit view based on resource category.
	 * @param platformunit
	 * @param area
	 * @throws WaspException
	 * @return
	 */
	public String getPlatformunitViewLink(Sample platformunit)  throws WaspException;
	
	public SampleSubtypeDao getSampleSubtypeDao();

	public void setSampleSubtypeDao(SampleSubtypeDao sampleSubtypeDao);

	public SampleTypeDao getSampleTypeDao();

	public void setSampleTypeDao(SampleTypeDao sampleTypeDao);
	
	public SampleSourceMetaDao getSampleSourceMetaDao();
	
	public void setSampleSourceMetaDao(SampleSourceMetaDao sampleSourceMetaDao);


	public SampleSource getCellLibraryBySampleSourceId(Integer ssid)
			throws SampleTypeException;
	  
	/**
	 * Find the cell-libraries (type SampleSource) for a given cell instance.  A cell library is the 
	 * most atomic entity, representing an instance of a library assayed on a given cell.
	 * 
	 * @param cell (of type Sample)
	 * @return List of Cell-libraries
	 */
	public List<SampleSource> getCellLibrariesForCell(Sample cell);

	public void setSampleMetaDao(SampleMetaDao sampleMetaDao); 
	
	public SampleMetaDao getSampleMetaDao();
	
	public void setSampleDraftDao(SampleDraftDao sampleDraftDao);
	
	public SampleDraftDao getSampleDraftDao();
	
	public void setSampleDraftMetaDao(SampleDraftMetaDao sampleDraftMetaDao);
	
	public SampleDraftMetaDao getSampleDraftMetaDao();

	public String getNameOfOrganism(Sample sample, String defaultValue);

	public String getNameOfOrganism(Sample sample);
	
	public Integer getIdOfOrganism(Sample sample);
	
	public Set<Organism> getOrganismsPlusOther();

	  /**
	   * Get cells onto which the current library is placed, restricted by job
	   * @param Sample library
	   * @param Job job
	   * @return List<Sample>
	   * @throws SampleTypeException 
	   */
	  public List<Sample> getCellsForLibrary(Sample library, Job job) throws SampleTypeException;

	  /**
	   * Returns cell library aggregation analysis status (returns NOOP if analysis was not selected)
	   * @param cellLibrary
	   * @return
	   * @throws SampleTypeException
	   */
	public ExitStatus getCellLibraryAggregationAnalysisStatus(SampleSource cellLibrary) throws SampleTypeException;
	
	/**
	 * Returns true if no QC status recorded against cell-library and primary analysis has been performed successfully
	 * @param cellLibrary
	 * @return
	 * @throws SampleTypeException
	 */
	public boolean isCellLibraryAwaitingQC(SampleSource cellLibrary) throws SampleTypeException;


	  /**
	   * Adds a given library to the given cell
	   * @param cell
	   * @param library
	   * @param libConcInCellPicoM
	   * @param job
	   * @throws SampleTypeException
	   * @throws SampleException
	   * @throws SampleMultiplexException
	   * @throws MetadataException
	   */
	  public void addLibraryToCell(Sample cell, Sample library,	Float libConcInCellPicoM, Job job) throws SampleTypeException, SampleException, SampleMultiplexException, MetadataException;

	  public void setJobForLibraryOnCell(SampleSource cellLibrary, Job job) throws MetadataException;
	  
	  public void enumerateSamplesForMPS(List<Sample> allSamples, List<Sample> submittedMacromolecules, List<Sample> submittedLibraries, List<Sample> facilityLibraries);
	  
	  /**
	   * See if Sample name has changed between sample objects and if so check if the new name is unique within the job.
	   * @param formSample
	   * @param originalSample
	   * @param job
	   * @param result
	   */
	  public void validateSampleNameUniqueWithinJob(String sampleName, Integer sampleId, Job job, BindingResult result);
	
	  public String getNameOfOrganismForAlignmentRequest(Sample sample, String defaultValue);
	  public String getNameOfGenomeForAlignmentRequest(Sample sample, String defaultValue);
	  public String getNameOfGenomeBuildForAlignmentRequest(Sample sample, String defaultValue);

	  public List<SampleSource> getCellLibrariesForLibrary(Sample library);

	  public List<SampleSource> getCellLibraries();

	  public List<Sample> getLibraries();

	  public boolean isCellLibrary(SampleSource cellLibrary);
	  
	  public void setLibraryOnCellMeta(SampleSource cellLibrary, String area, String metaValueName, String metaValue) throws MetadataException;
	  
	  public Map<Sample, List<SampleSource>> associateUppermostSampleWithCellLibraries(List<SampleSource> cellLibraryList);
	  public boolean confirmSamplePairIsOfSameSpecies(Sample s1, Sample s2);
	  
	  public List<Integer> convertCellLibraryListToIdList(List<SampleSource> cellLibraryList);
	  
	  public void addControlLibraryToCell(Sample cell, Sample library,	Float libConcInCellPicoM) throws SampleTypeException, SampleException, SampleMultiplexException, MetadataException;
	  public void setReasonForNewLibraryComment(Integer sampleId, String comment) throws Exception;
	  public List<MetaMessage> getReasonForNewLibraryComment(Integer sampleId);
	  public List<Sample> getCompatibleAndAvailablePlatformUnits(Job job);	
}
