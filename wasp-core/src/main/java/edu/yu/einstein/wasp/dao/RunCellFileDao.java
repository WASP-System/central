
/**
 *
 * RunCellfileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunCellFile Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.RunCellFile;


public interface RunCellFileDao extends WaspDao<RunCellFile> {

  public RunCellFile getRunCellFileByRunCellfileId (final int runCellfileId);

  public RunCellFile getRunCellFileByFileId (final int fileId);


}

