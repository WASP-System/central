
/**
 *
 * SampleTypeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleType Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleType;


public interface SampleTypeDao extends WaspDao<SampleType> {

  public SampleType getSampleTypeBySampleTypeId (final int sampleTypeId);

  public SampleType getSampleTypeByIName (final String iName);

  public SampleType getSampleTypeByName (final String name);


}

