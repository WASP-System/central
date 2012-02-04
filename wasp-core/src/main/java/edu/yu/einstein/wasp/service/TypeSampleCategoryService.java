
/**
 *
 * TypeSampleCategoryService.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategoryService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.TypeSampleCategoryDao;
import edu.yu.einstein.wasp.model.TypeSampleCategory;

@Service
public interface TypeSampleCategoryService extends WaspService<TypeSampleCategory> {

	/**
	 * setTypeSamplecategoryDao(TypeSampleCategoryDao typeSamplecategoryDao)
	 *
	 * @param typeSampleCategoryDao
	 *
	 */
	public void setTypeSampleCategoryDao(TypeSampleCategoryDao typeSampleCategoryDao);

	/**
	 * getTypeSamplecategoryDao();
	 *
	 * @return typeSamplecategoryDao
	 *
	 */
	public TypeSampleCategoryDao getTypeSampleCategoryDao();

	public TypeSampleCategory getTypeSampleCategoryByTypeSampleCategoryId (final Integer typeSamplecategoryId);

	public TypeSampleCategory getTypeSampleCategoryByIName (final String iName);

	public TypeSampleCategory getTypeSampleCategoryByName (final String name);


}

