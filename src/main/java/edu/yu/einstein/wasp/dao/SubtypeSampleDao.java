
/**
 *
 * SubtypeSampleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SubtypeSample Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SubtypeSampleDao extends WaspDao<SubtypeSample> {

  public SubtypeSample getSubtypeSampleBySubtypeSampleId (final int subtypeSampleId);

  public SubtypeSample getSubtypeSampleByIName (final String iName);


}

