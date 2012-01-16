
/**
 *
 * RunLaneService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLaneService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunLaneDao;
import edu.yu.einstein.wasp.model.RunLane;

@Service
public interface RunLaneService extends WaspService<RunLane> {

	/**
	 * setRunLaneDao(RunLaneDao runLaneDao)
	 *
	 * @param runLaneDao
	 *
	 */
	public void setRunLaneDao(RunLaneDao runLaneDao);

	/**
	 * getRunLaneDao();
	 *
	 * @return runLaneDao
	 *
	 */
	public RunLaneDao getRunLaneDao();

  public RunLane getRunLaneByRunLaneId (final int runLaneId);

  public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId);

  public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId);


}

