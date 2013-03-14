
/**
 *
 * RunCellDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunCell Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.RunCell;


public interface RunCellDao extends WaspDao<RunCell> {

  public RunCell getRunCellByRunCellId (final int runCellId);

  public RunCell getRunCellByRunIdResourcecellId (final int runId, final int resourcecellId);

  public RunCell getRunCellBySampleIdRunId (final int sampleId, final int runId);


}

