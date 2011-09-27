
/**
 *
 * RunService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.model.Run;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface RunService extends WaspService<Run> {

	/**
	 * setRunDao(RunDao runDao)
	 *
	 * @param runDao
	 *
	 */
	public void setRunDao(RunDao runDao);

	/**
	 * getRunDao();
	 *
	 * @return runDao
	 *
	 */
	public RunDao getRunDao();

  public Run getRunByRunId (final int runId);


}

