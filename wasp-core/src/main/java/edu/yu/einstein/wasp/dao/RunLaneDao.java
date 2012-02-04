
/**
 *
 * RunLaneDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLane Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.RunLane;


public interface RunLaneDao extends WaspDao<RunLane> {

  public RunLane getRunLaneByRunLaneId (final int runLaneId);

  public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId);

  public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId);


}

