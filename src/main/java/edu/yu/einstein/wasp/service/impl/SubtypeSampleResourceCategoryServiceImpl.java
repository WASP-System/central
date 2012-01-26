
/**
 *
 * SubtypeSampleResourceCategoryServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSampleResourceCategoryService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SubtypeSampleResourceCategoryDao;
import edu.yu.einstein.wasp.model.SubtypeSampleResourceCategory;
import edu.yu.einstein.wasp.service.SubtypeSampleResourceCategoryService;

@Service
public class SubtypeSampleResourceCategoryServiceImpl extends WaspServiceImpl<SubtypeSampleResourceCategory> implements SubtypeSampleResourceCategoryService {

	/**
	 * subtypeSampleResourceCategoryDao;
	 *
	 */
	private SubtypeSampleResourceCategoryDao subtypeSampleResourceCategoryDao;

	/**
	 * setTypeSampleresourcecategoryDao(SubtypeSampleResourceCategoryDao subtypeSampleResourceCategoryDao)
	 *
	 * @param subtypeSampleResourceCategoryDao
	 *
	 */
	@Override
	@Autowired
	public void setSubtypeSampleResourceCategoryDao(SubtypeSampleResourceCategoryDao subtypeSampleResourceCategoryDao) {
		this.subtypeSampleResourceCategoryDao = subtypeSampleResourceCategoryDao;
		this.setWaspDao(subtypeSampleResourceCategoryDao);
	}

	/**
	 * getTypeSampleresourcecategoryDao();
	 *
	 * @return subtypeSampleResourceCategoryDao
	 *
	 */
	@Override
	public SubtypeSampleResourceCategoryDao getSubtypeSampleResourceCategoryDao() {
		return this.subtypeSampleResourceCategoryDao;
	}


  @Override
  public SubtypeSampleResourceCategory getSubtypeSampleResourceCategoryBySubtypeSampleResourceCategoryId (final Integer subtypeSampleResourceCategoryId) {
    return this.getSubtypeSampleResourceCategoryDao().getSubtypeSampleResourceCategoryBySubtypeSampleResourceCategoryId(subtypeSampleResourceCategoryId);
  }
  
  @Override
  public SubtypeSampleResourceCategory getSubtypeSampleResourceCategoryBySubtypeSampleIdResourceCategoryId(final Integer subtypeSampleId, final Integer resourceCategoryId){
	  return this.getSubtypeSampleResourceCategoryDao().getSubtypeSampleResourceCategoryBySubtypeSampleIdResourceCategoryId(subtypeSampleId, resourceCategoryId);
  }
}

