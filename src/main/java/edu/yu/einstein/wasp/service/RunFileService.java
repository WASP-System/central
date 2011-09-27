
/**
 *
 * RunFileService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFileService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RunFileDao;
import edu.yu.einstein.wasp.model.RunFile;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface RunFileService extends WaspService<RunFile> {

	/**
	 * setRunFileDao(RunFileDao runFileDao)
	 *
	 * @param runFileDao
	 *
	 */
	public void setRunFileDao(RunFileDao runFileDao);

	/**
	 * getRunFileDao();
	 *
	 * @return runFileDao
	 *
	 */
	public RunFileDao getRunFileDao();

  public RunFile getRunFileByRunlanefileId (final int runlanefileId);

  public RunFile getRunFileByFileId (final int fileId);


}

