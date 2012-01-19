
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

	public TypeSampleResourceCategory getTypeSampleResourceCategoryByTypeSampleresourcecategoryId (final Integer typeSampleresourcecategoryId);

	public TypeSampleResourceCategory getTypeSampleResourceCategoryByIName (final String iName);

	public TypeSampleResourceCategory getTypeSampleResourceCategoryByName (final String name);
	
}

