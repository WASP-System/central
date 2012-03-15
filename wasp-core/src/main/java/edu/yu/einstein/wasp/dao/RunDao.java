
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

import java.util.List;

import edu.yu.einstein.wasp.model.Run;


public interface RunDao extends WaspDao<Run> {

  public Run getRunByRunId (final int runId);

  public List<Run> getActiveRuns();


}

