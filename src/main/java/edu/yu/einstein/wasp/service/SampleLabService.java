
/**
 *
 * SampleLabService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLabService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleLabDao;
import edu.yu.einstein.wasp.model.SampleLab;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SampleLabService extends WaspService<SampleLab> {

  public void setSampleLabDao(SampleLabDao sampleLabDao);
  public SampleLabDao getSampleLabDao();

  public SampleLab getSampleLabBySampleLabId (final int sampleLabId);

  public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId);

}

