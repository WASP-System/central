
/**
 *
 * TypeSampleCategoryResourceCategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategoryResourceCategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.TypeSampleCategoryResourceCategory;


public interface TypeSampleCategoryResourceCategoryDao extends WaspDao<TypeSampleCategoryResourceCategory> {

  public TypeSampleCategoryResourceCategory getTypeSampleCategoryResourceCategoryByTypeSamplecategoryresourcecategoryId (final Integer typeSamplecategoryresourcecategoryId);


}

