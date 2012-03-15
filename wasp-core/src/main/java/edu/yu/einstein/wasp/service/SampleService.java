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
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SubtypeSample;

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
	   * Gets a list of {@link SubtypeSample} objects associated with given workflow which are specified as viewable
	   * to the currently logged in user (role dependent)
	   * @param workflowId
	   * @return List<{@link SubtypeSample}>
	   */
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByLoggedInUserRoles(Integer workflowId);

	  /**
	   * Gets a list of {@link SubtypeSample} objects associated with given workflowId and typeSampleId which are specified as viewable
	   * to the currently logged in user (role dependent)
	   * @param workflowId
	   * @return List<{@link SubtypeSample}>
	   */
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByLoggedInUserRoles(Integer workflowId, String typeSampleIName);


	  /**
	   * Gets a list of {@link SubtypeSample} objects associated with given workflowId and typeSampleId which are specified as viewable
	   * to the users with given roles (role dependent)
	   * @param workflowId
	   * @param roles
	   * @param typeSampleIName
	   * @return
	   */
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByRole(Integer workflowId,	String[] roles, String typeSampleIName);

	  /**
	   * Gets a list of {@link SubtypeSample} objects associated with given workflowId which are specified as viewable
	   * to the users with given roles (role dependent)
	   * @param workflowId
	   * @param roles
	   * @return
	   */
	  public List<SubtypeSample> getSubtypeSamplesForWorkflowByRole(Integer workflowId,	String[] roles);


}
