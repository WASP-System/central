
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

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;


public interface SampleSourceDao extends WaspDao<SampleSource> {

  public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId);

  public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex);

  /**
   * Get the parent sample for a derived sample with supplied Id
   * @param source_sampleId
   * @return{@link Sample} parent sample
   */
  public Sample getParentSampleByDerivedSampleId(Integer derivedSampleId);

  /**
   * Get the samples derived from a parent sample with supplied Id
   * @param source_sampleId
   * @return List<{@link Sample}> derived sample list
   */
  public List<Sample> getDerivedSamplesByParentSampleId(Integer parentSampleId);
}

