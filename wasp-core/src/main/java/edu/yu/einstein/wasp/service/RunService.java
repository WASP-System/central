/**
 * 
 */
package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.model.Run;

/**
 * @author calder
 *
 */
@Service
public interface RunService extends WaspService {
	
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
	
	
	/**
	 * Get a run object by its name
	 * 
	 * @param name
	 * @return
	 */
	public Run getRunByName(String name);

}
