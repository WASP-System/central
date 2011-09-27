
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

import edu.yu.einstein.wasp.service.RunLaneService;
import edu.yu.einstein.wasp.dao.RunLaneDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.RunLane;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

