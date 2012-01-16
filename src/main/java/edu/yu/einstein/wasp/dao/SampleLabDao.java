
/**
 *
 * SampleLabDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLab Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleLab;


public interface SampleLabDao extends WaspDao<SampleLab> {

  public SampleLab getSampleLabBySampleLabId (final int sampleLabId);

  public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId);


}

