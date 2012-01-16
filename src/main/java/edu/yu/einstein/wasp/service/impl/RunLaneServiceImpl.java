
/**
 *
 * RunLaneServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLaneService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunLaneDao;
import edu.yu.einstein.wasp.model.RunLane;
import edu.yu.einstein.wasp.service.RunLaneService;

@Service
public class RunLaneServiceImpl extends WaspServiceImpl<RunLane> implements RunLaneService {

	/**
	 * runLaneDao;
	 *
	 */
	private RunLaneDao runLaneDao;

	/**
	 * setRunLaneDao(RunLaneDao runLaneDao)
	 *
	 * @param runLaneDao
	 *
	 */
	@Autowired
	public void setRunLaneDao(RunLaneDao runLaneDao) {
		this.runLaneDao = runLaneDao;
		this.setWaspDao(runLaneDao);
	}

	/**
	 * getRunLaneDao();
	 *
	 * @return runLaneDao
	 *
	 */
	public RunLaneDao getRunLaneDao() {
		return this.runLaneDao;
	}


  public RunLane getRunLaneByRunLaneId (final int runLaneId) {
    return this.getRunLaneDao().getRunLaneByRunLaneId(runLaneId);
  }

  public RunLane getRunLaneByRunIdResourcelaneId (final int runId, final int resourcelaneId) {
    return this.getRunLaneDao().getRunLaneByRunIdResourcelaneId(runId, resourcelaneId);
  }

  public RunLane getRunLaneBySampleIdRunId (final int sampleId, final int runId) {
    return this.getRunLaneDao().getRunLaneBySampleIdRunId(sampleId, runId);
  }

}

