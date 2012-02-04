
/**
 *
 * RunLanefileDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefile Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.RunLanefile;


public interface RunLanefileDao extends WaspDao<RunLanefile> {

  public RunLanefile getRunLanefileByRunLanefileId (final int runLanefileId);

  public RunLanefile getRunLanefileByFileId (final int fileId);


}

