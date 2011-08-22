
/**
 *
 * RunLaneService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLaneService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RunLaneDao;
import edu.yu.einstein.wasp.model.RunLane;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface RunLaneService extends WaspService<RunLane> {

  public void setRunLaneDao(RunLaneDao runLaneDao);
  public RunLaneDao getRunLaneDao();

  public RunLane getRunLaneByRunLaneId (final int runLaneId);

  public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId);

  public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId);

}

