
/**
 *
 * RunFileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunFile Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.RunFile;


public interface RunFileDao extends WaspDao<RunFile> {

  public RunFile getRunFileByRunlanefileId (final int runlanefileId);

  public RunFile getRunFileByFileId (final int fileId);


}

