
/**
 *
 * RunServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.service.RunService;

@Service
public class RunServiceImpl extends WaspServiceImpl<Run> implements RunService {

	/**
	 * runDao;
	 *
	 */
	private RunDao runDao;

	/**
	 * setRunDao(RunDao runDao)
	 *
	 * @param runDao
	 *
	 */
	@Autowired
	public void setRunDao(RunDao runDao) {
		this.runDao = runDao;
		this.setWaspDao(runDao);
	}

	/**
	 * getRunDao();
	 *
	 * @return runDao
	 *
	 */
	public RunDao getRunDao() {
		return this.runDao;
	}


  public Run getRunByRunId (final int runId) {
    return this.getRunDao().getRunByRunId(runId);
  }

}

