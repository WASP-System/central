
/**
 *
 * RunDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Run Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RunDao extends WaspDao<Run> {

  public Run getRunByRunId (final int runId);


}

