
/**
 *
 * RunLanefileService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefileService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RunLanefileDao;
import edu.yu.einstein.wasp.model.RunLanefile;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface RunLanefileService extends WaspService<RunLanefile> {

	/**
	 * setRunLanefileDao(RunLanefileDao runLanefileDao)
	 *
	 * @param runLanefileDao
	 *
	 */
	public void setRunLanefileDao(RunLanefileDao runLanefileDao);

	/**
	 * getRunLanefileDao();
	 *
	 * @return runLanefileDao
	 *
	 */
	public RunLanefileDao getRunLanefileDao();

  public RunLanefile getRunLanefileByRunLanefileId (final int runLanefileId);

  public RunLanefile getRunLanefileByFileId (final int fileId);


}

