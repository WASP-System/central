
/**
 *
 * SampleLabDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleLabDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleLabDao extends WaspDao<SampleLab> {

  public SampleLab getSampleLabBySampleLabId (final int sampleLabId);

  public SampleLab getSampleLabBySampleIdLabId (final int sampleId, final int labId);

}

