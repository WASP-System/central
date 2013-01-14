
/**
 *
 * RunMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import java.util.List;

import edu.yu.einstein.wasp.model.RunMeta;


public interface RunMetaDao extends WaspMetaDao<RunMeta> {

  public RunMeta getRunMetaByRunMetaId (final int runMetaId);

  public RunMeta getRunMetaByKRunId (final String k, final int runId);





}

