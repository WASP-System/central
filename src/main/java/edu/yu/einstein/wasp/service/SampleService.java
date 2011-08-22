
/**
 *
 * SampleService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.model.Sample;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SampleService extends WaspService<Sample> {

  public void setSampleDao(SampleDao sampleDao);
  public SampleDao getSampleDao();

  public Sample getSampleBySampleId (final int sampleId);

}

