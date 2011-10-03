
/**
 *
 * SampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.Sample;


public interface SampleDao extends WaspDao<Sample> {

  Sample getSampleBySampleId (final int sampleId);
  
  List<Sample> getSamplesByJobId (final int jobId);


}

