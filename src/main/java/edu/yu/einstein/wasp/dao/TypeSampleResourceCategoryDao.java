
/**
 *
 * TypeSampleResourceCategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleResourceCategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.TypeSampleResourceCategory;


public interface TypeSampleResourceCategoryDao extends WaspDao<TypeSampleResourceCategory> {

	public TypeSampleResourceCategory getTypeSampleResourceCategoryByTypeSampleResourceCategoryId (final Integer typeSampleResourceCategoryId);

	public TypeSampleResourceCategory getTypeSampleResourceCategoryByIName (final String iName);

	public TypeSampleResourceCategory getTypeSampleResourceCategoryByName (final String name);
	
}

