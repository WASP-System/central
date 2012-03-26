
/**
 *
 * SampleTypeCategoryDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleTypeCategory Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleTypeCategory;


public interface SampleTypeCategoryDao extends WaspDao<SampleTypeCategory> {

  public SampleTypeCategory getSampleTypeCategoryBySampleTypecategoryId (final Integer sampleTypecategoryId);

  public SampleTypeCategory getSampleTypeCategoryByIName (final String iName);

  public SampleTypeCategory getSampleTypeCategoryByName (final String name);


}

