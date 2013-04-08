
/**
 *
 * SampleSourceDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSource Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;
import java.util.Set;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;


public interface SampleSourceDao extends WaspDao<SampleSource> {

  public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId);

  public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex);
  
  public List<SampleSource> getCellLibraries(Sample cell);

}

