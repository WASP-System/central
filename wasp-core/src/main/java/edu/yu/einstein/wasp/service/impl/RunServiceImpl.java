
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.model.Job;
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
	@Override
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
	@Override
	public RunDao getRunDao() {
		return this.runDao;
	}


  @Override
  public Run getRunByRunId (final int runId) {
    return this.getRunDao().getRunByRunId(runId);
  }
  
  @Override
  public List<Run> getActiveRuns(){
	  Map queryMap = new HashMap();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
  }

}

