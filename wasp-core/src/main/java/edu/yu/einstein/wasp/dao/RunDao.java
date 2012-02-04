
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

import edu.yu.einstein.wasp.model.Run;


public interface RunDao extends WaspDao<Run> {

  public Run getRunByRunId (final int runId);


}

