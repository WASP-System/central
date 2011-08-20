
/**
 *
 * LabPendingDao.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface LabPendingDao extends WaspDao<LabPending> {

  public LabPending getLabPendingByLabPendingId (final int labPendingId);

}

