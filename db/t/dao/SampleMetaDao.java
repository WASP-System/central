
/**
 *
 * SampleMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleMetaDao extends WaspDao<SampleMeta> {

  public SampleMeta getSampleMetaBySampleMetaId (final int sampleMetaId);

  public SampleMeta getSampleMetaByKSampleId (final String k, final int sampleId);

  public void updateBySampleId (final int sampleId, final List<SampleMeta> metaList);

}

