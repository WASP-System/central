
/**
 *
 * SampleSourceService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleSourceService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.model.SampleSource;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SampleSourceService extends WaspService<SampleSource> {

  public void setSampleSourceDao(SampleSourceDao sampleSourceDao);
  public SampleSourceDao getSampleSourceDao();

  public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId);

  public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex);

}

