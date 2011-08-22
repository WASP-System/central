
/**
 *
 * RunLaneDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLaneDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RunLaneDao extends WaspDao<RunLane> {

  public RunLane getRunLaneByRunLaneId (final int runLaneId);

  public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId);

  public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId);

}

