
/**
 *
 * SampleMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.model.SampleMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface SampleMetaService extends WaspService<SampleMeta> {

  public void setSampleMetaDao(SampleMetaDao sampleMetaDao);
  public SampleMetaDao getSampleMetaDao();

  public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId);

  public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId);

  public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList);

}

