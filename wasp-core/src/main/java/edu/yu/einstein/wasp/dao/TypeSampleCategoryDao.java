
/**
 *
 * TypeSampleCategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeSampleCategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.TypeSampleCategory;


public interface TypeSampleCategoryDao extends WaspDao<TypeSampleCategory> {

  public TypeSampleCategory getTypeSampleCategoryByTypeSamplecategoryId (final Integer typeSamplecategoryId);

  public TypeSampleCategory getTypeSampleCategoryByIName (final String iName);

  public TypeSampleCategory getTypeSampleCategoryByName (final String name);


}

