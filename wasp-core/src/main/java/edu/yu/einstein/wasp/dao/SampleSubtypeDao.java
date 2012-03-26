
/**
 *
 * SampleSubtypeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSubtype Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.SampleSubtype;


public interface SampleSubtypeDao extends WaspDao<SampleSubtype> {

  public SampleSubtype getSampleSubtypeBySampleSubtypeId (final int sampleSubtypeId);

  public SampleSubtype getSampleSubtypeByIName (final String iName);

  public List<SampleSubtype> getActiveSampleSubtypes();
}

