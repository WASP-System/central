
/**
 *
 * RunLanefileServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefileService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunLanefileDao;
import edu.yu.einstein.wasp.model.RunLanefile;
import edu.yu.einstein.wasp.service.RunLanefileService;

@Service
public class RunLanefileServiceImpl extends WaspServiceImpl<RunLanefile> implements RunLanefileService {

	/**
	 * runLanefileDao;
	 *
	 */
	private RunLanefileDao runLanefileDao;

	/**
	 * setRunLanefileDao(RunLanefileDao runLanefileDao)
	 *
	 * @param runLanefileDao
	 *
	 */
	@Autowired
	public void setRunLanefileDao(RunLanefileDao runLanefileDao) {
		this.runLanefileDao = runLanefileDao;
		this.setWaspDao(runLanefileDao);
	}

	/**
	 * getRunLanefileDao();
	 *
	 * @return runLanefileDao
	 *
	 */
	public RunLanefileDao getRunLanefileDao() {
		return this.runLanefileDao;
	}


  public RunLanefile getRunLanefileByRunLanefileId (final int runLanefileId) {
    return this.getRunLanefileDao().getRunLanefileByRunLanefileId(runLanefileId);
  }

  public RunLanefile getRunLanefileByFileId (final int fileId) {
    return this.getRunLanefileDao().getRunLanefileByFileId(fileId);
  }

}

