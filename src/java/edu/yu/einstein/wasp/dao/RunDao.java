
/**
 *
 * RunDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RunDao extends WaspDao<Run> {

  public Run getRunByRunId (final int runId);

}

