/**
 *
 * SampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;

@Service
public interface SampleService extends WaspService {

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

	List<Sample> findAllPlatformUnits();

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
	   * Returns string containing the status of a sample's Receive Sample state
	   * @param Sample
	   * @return String
	   */
	  public String getReceiveSampleStatus(final Sample sample);
	  
	  /**
	   * Accepts and (in-situ) sorts list of samples by sample name 
	   * @param List<Sample>
	   * @return void
	   */
	  public void sortSamplesBySampleName(List<Sample> samples);
	  
	  /**
	   * Converts sample's Receive Sample status from database storage meaning to human-comprehensible meaning for viewing on the web
	   * @param String status
	   * @return String 
	   */
	  public String convertReceiveSampleStatusForWeb(String internalStatus);

	  /**
	   * Converts sample's Receive Sample status from web meaning (human-comprehensible meaning) to enum consistent value for internal storage
	   * @param String webStatus
	   * @return String 
	   */
	  public String convertReceiveSampleStatusForInternalStorage(String webStatus);

	  /**
	   * Gets list of Receive Sample options for web display
	   * @param none
	   * @return List<String> containing the list of Receive Sample Options for web display 
	   */
	  public List<String> getReceiveSampleStatusOptionsForWeb();
	  
	  /**
	   * Updates sample's Receive Sample status
	   * @param Sample sample
	   * @param String status (from web)
	   * @return boolean
	   */
	  public boolean updateSampleReceiveStatus(final Sample sample, final String status);
	  
	  /**
	   * Returns boolean informing whether a sample has been processed by the facility
	   * If a macromolecule was submitted to the facility and a library was created from it, then return true
	   * If a library was submitted to the facility and it has been added to a flowcell, then return true
	   * Else return false
	   * @param Sample sample
	   * @return boolean
	   */
	  public boolean submittedSampleHasBeenProcessedByFacility(final Sample sample);
	  
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
	   * Returns list of samples that are flow cells to which libraries can be added [meaning whose task-status is CREATED])
	   * @param void
	   * @return List<Sample>
	   */
	  public List<Sample> getAvailableFlowCells();
	  
	  /**
	   * Returns list of samples that are flow cells to which libraries can be added AND are compatible with the parameter job
	   * @param Job job
	   * @return List<Sample>
	   * 
	   */
	  public List<Sample> getAvailableAndCompatibleFlowCells(Job job);

	  
	  /**
	   * Returns Map of Cells according to their index for a platform unit
	   * @param platformUnit
	   * @return Map<Integer, Sample> indexedCells
	   * @throws SampleTypeException 
	   */
	  public Map<Integer, Sample> getIndexedCellsOnPlatformUnit(Sample platformUnit) throws SampleTypeException;
	  
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
	   * @param libConcInLanePicoM
	   * @throws SampleTypeException
	   * @throws SampleException
	   * @throws SampleMultiplexException
	   * @throws MetadataException
	   */
	  public void addLibraryToCell(Sample cell, Sample library,	Float libConcInLanePicoM) throws SampleTypeException, SampleException, SampleMultiplexException, MetadataException;

	  /**
	   * Clones a sampleDraft object
	   * @param sampleDraft
	   * @return
	   */
	  public SampleDraft cloneSampleDraft(SampleDraft sampleDraft);

	  /**
	   * Returns list of samples that are platformUnits with task assignLibraryToPlatformUnit and status = CREATED (so, it's not yet part of a sequence run)
	   * @param none
	   * @return List<Sample>
	   */
	  public List<Sample> platformUnitsAwaitingLibraries();
	  
	  /**
	   * Get the run on which a given platform unit has been placed. If the platform unit is not currently associated with an active run or is not associated with a run 
	   * this method returns null
	   * @param platformUnit
	   * @return
	   */
	  public Run getCurrentRunForPlatformUnit(Sample platformUnit);
	  
	  /**
	   * Determine whether a platform unit name is already in the database. If this is a new platformunit, the id will be null so compare the name to all platformunits. 
	   * If id is not null, then make sure that the name is associated with that pu, and if not return true.
	   * @param Sample platformUnit
	   * @param String platformUnitName 
	   * @return boolean
	   */
	  public boolean platformUnitNameUsedByAnother(Sample platformUnit, String name) throws SampleTypeException, SampleException;
	  
	  /**
	   * Determine whether a platform unit barcodeName is already in the database. 
	   * @param Sample platformUnit
	   * @return boolean
	   */
	  public boolean platformUnitBarcodeUsedByAnother(Sample platformUnit, String barcodeName) throws SampleTypeException, SampleException;
	  
}
