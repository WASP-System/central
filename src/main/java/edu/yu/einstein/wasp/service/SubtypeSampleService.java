
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

import edu.yu.einstein.wasp.dao.SubtypeSampleDao;
import edu.yu.einstein.wasp.model.SubtypeSample;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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


}

