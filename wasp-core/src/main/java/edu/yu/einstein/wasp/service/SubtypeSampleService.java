
/**
 *
 * SubtypeSampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SubtypeSampleDao;
import edu.yu.einstein.wasp.model.SubtypeSample;

@Service
public interface SubtypeSampleService extends WaspService<SubtypeSample> {

	/**
	 * setSubtypeSampleDao(SubtypeSampleDao subtypeSampleDao)
	 *
	 * @param subtypeSampleDao
	 *
	 */
	public void setSubtypeSampleDao(SubtypeSampleDao subtypeSampleDao);

	/**
	 * getSubtypeSampleDao();
	 *
	 * @return subtypeSampleDao
	 *
	 */
	public SubtypeSampleDao getSubtypeSampleDao();

  public SubtypeSample getSubtypeSampleBySubtypeSampleId (final int subtypeSampleId);

  public SubtypeSample getSubtypeSampleByIName (final String iName);

  public List<SubtypeSample> getActiveSubtypeSamples();
  
  /**
   * Gets a list of {@link SubtypeSample} objects associated with given workflow which are specified as viewable
   * to the currently logged in user (role dependent)
   * @param workflowId
   * @return List<{@link SubtypeSample}>
   */
  public List<SubtypeSample> getWorkflowSubtypeSamplesByLoggedInUserRoles(Integer workflowId);

  /**
   * Gets a list of {@link SubtypeSample} objects associated with given workflowId and typeSampleId which are specified as viewable
   * to the currently logged in user (role dependent)
   * @param workflowId
   * @return List<{@link SubtypeSample}>
   */
  public List<SubtypeSample> getWorkflowSubtypeSamplesByLoggedInUserRoles(Integer workflowId, String typeSampleIName);


}

