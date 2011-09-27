
/**
 *
 * SampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Sample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleDao extends WaspDao<Sample> {

  public Sample getSampleBySampleId (final int sampleId);


}

