
/**
 *
 * RunFileServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFileService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RunFileDao;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.service.RunFileService;

@Service
public class RunFileServiceImpl extends WaspServiceImpl<RunFile> implements RunFileService {

	/**
	 * runFileDao;
	 *
	 */
	private RunFileDao runFileDao;

	/**
	 * setRunFileDao(RunFileDao runFileDao)
	 *
	 * @param runFileDao
	 *
	 */
	@Override
	@Autowired
	public void setRunFileDao(RunFileDao runFileDao) {
		this.runFileDao = runFileDao;
		this.setWaspDao(runFileDao);
	}

	/**
	 * getRunFileDao();
	 *
	 * @return runFileDao
	 *
	 */
	@Override
	public RunFileDao getRunFileDao() {
		return this.runFileDao;
	}


  @Override
public RunFile getRunFileByRunlanefileId (final int runlanefileId) {
    return this.getRunFileDao().getRunFileByRunlanefileId(runlanefileId);
  }

  @Override
public RunFile getRunFileByFileId (final int fileId) {
    return this.getRunFileDao().getRunFileByFileId(fileId);
  }

}

