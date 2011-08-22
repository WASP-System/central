
/**
 *
 * SampleLabService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLabService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.SampleLabService;
import edu.yu.einstein.wasp.dao.SampleLabDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleLab;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleLabServiceImpl extends WaspServiceImpl<SampleLab> implements SampleLabService {

  private SampleLabDao sampleLabDao;
  @Autowired
  public void setSampleLabDao(SampleLabDao sampleLabDao) {
    this.sampleLabDao = sampleLabDao;
    this.setWaspDao(sampleLabDao);
  }
  public SampleLabDao getSampleLabDao() {
    return this.sampleLabDao;
  }

  // **

  
  public SampleLab getSampleLabBySampleLabId (final int sampleLabId) {
    return this.getSampleLabDao().getSampleLabBySampleLabId(sampleLabId);
  }

  public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId) {
    return this.getSampleLabDao().getSampleLabBySampleIdLabId(sampleId, labId);
  }
}

