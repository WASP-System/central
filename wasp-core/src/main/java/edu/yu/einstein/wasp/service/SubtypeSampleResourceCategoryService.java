
/**
 *
 * SubtypeSampleResourceCategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleResourceCategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SubtypeSampleResourceCategoryDao;
import edu.yu.einstein.wasp.model.SubtypeSampleResourceCategory;

@Service
public interface SubtypeSampleResourceCategoryService extends WaspService<SubtypeSampleResourceCategory> {

	/**
	 * setTypeSampleresourcecategoryDao(SubtypeSampleResourceCategoryDao typeSampleresourcecategoryDao)
	 *
	 * @param subtypeSampleResourceCategoryDao
	 *
	 */
	public void setSubtypeSampleResourceCategoryDao(SubtypeSampleResourceCategoryDao subtypeSampleResourceCategoryDao);

	/**
	 * getTypeSampleresourcecategoryDao();
	 *
	 * @return typeSampleresourcecategoryDao
	 *
	 */
	public SubtypeSampleResourceCategoryDao getSubtypeSampleResourceCategoryDao();

	public SubtypeSampleResourceCategory getSubtypeSampleResourceCategoryBySubtypeSampleResourceCategoryId (final Integer subtypeSampleResourceCategoryId);
  
	public SubtypeSampleResourceCategory getSubtypeSampleResourceCategoryBySubtypeSampleIdResourceCategoryId(final Integer subtypeSampleId, final Integer resourceCategoryId);


}

