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

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
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
	   * @param workflowId
	   * @return List<{@link SampleSubtype}>
	   */
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId);

	  /**
	   * Gets a list of {@link SampleSubtype} objects associated with given workflowId and sampleTypeId which are specified as viewable
	   * to the currently logged in user (role dependent)
	   * @param workflowId
	   * @return List<{@link SampleSubtype}>
	   */
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByLoggedInUserRoles(Integer workflowId, String sampleTypeIName);


	  /**
	   * Gets a list of {@link SampleSubtype} objects associated with given workflowId and sampleTypeId which are specified as viewable
	   * to the users with given roles (role dependent)
	   * @param workflowId
	   * @param roles
	   * @param sampleTypeIName
	   * @return
	   */
	  public List<SampleSubtype> getSampleSubtypesForWorkflowByRole(Integer workflowId,	String[] roles, String sampleTypeIName);

	  /**
	   * Gets a list of {@link SampleSubtype} objects associated with given workflowId which are specified as viewable
	   * to the users with given roles (role dependent)
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
}
