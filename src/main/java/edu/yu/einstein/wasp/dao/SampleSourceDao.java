
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleSourceDao extends WaspDao<SampleSource> {

  public SampleSource getSampleSourceBySampleSourceId (final int sampleSourceId);

  public SampleSource getSampleSourceBySampleIdMultiplexindex (final int sampleId, final int multiplexindex);


}

