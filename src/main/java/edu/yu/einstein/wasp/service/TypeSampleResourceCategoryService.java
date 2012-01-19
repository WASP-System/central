
/**
 *
 * TypeSampleResourceCategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleResourceCategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeSampleResourceCategoryDao;
import edu.yu.einstein.wasp.model.TypeSampleResourceCategory;

@Service
public interface TypeSampleResourceCategoryService extends WaspService<TypeSampleResourceCategory> {

	/**
	 * setTypeSampleresourcecategoryDao(TypeSampleResourceCategoryDao typeSampleresourcecategoryDao)
	 *
	 * @param typeSampleResourceCategoryDao
	 *
	 */
	public void setTypeSampleResourceCategoryDao(TypeSampleResourceCategoryDao typeSampleResourceCategoryDao);

	/**
	 * getTypeSampleresourcecategoryDao();
	 *
	 * @return typeSampleresourcecategoryDao
	 *
	 */
	public TypeSampleResourceCategoryDao getTypeSampleResourceCategoryDao();

  public TypeSampleResourceCategory getTypeSampleResourceCategoryByTypeSampleresourcecategoryId (final Integer typeSampleresourcecategoryId);


}

