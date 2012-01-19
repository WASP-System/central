
/**
 *
 * TypeSampleCategoryResourceCategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategoryResourceCategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeSampleCategoryResourceCategoryDao;
import edu.yu.einstein.wasp.model.TypeSampleCategoryResourceCategory;

@Service
public interface TypeSampleCategoryResourceCategoryService extends WaspService<TypeSampleCategoryResourceCategory> {

	/**
	 * setTypeSamplecategoryresourcecategoryDao(TypeSampleCategoryResourceCategoryDao typeSamplecategoryresourcecategoryDao)
	 *
	 * @param typeSampleCategoryResourceCategoryDao
	 *
	 */
	public void setTypeSampleCategoryResourceCategoryDao(TypeSampleCategoryResourceCategoryDao typeSampleCategoryResourceCategoryDao);

	/**
	 * getTypeSamplecategoryresourcecategoryDao();
	 *
	 * @return typeSamplecategoryresourcecategoryDao
	 *
	 */
	public TypeSampleCategoryResourceCategoryDao getTypeSampleCategoryResourceCategoryDao();

  public TypeSampleCategoryResourceCategory getTypeSampleCategoryResourceCategoryByTypeSamplecategoryresourcecategoryId (final Integer typeSamplecategoryresourcecategoryId);


}

