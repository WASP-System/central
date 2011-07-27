
/**
 *
 * RunMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RunMetaDao extends WaspDao<RunMeta> {

  public RunMeta getRunMetaByRunMetaId (final int runMetaId);

  public RunMeta getRunMetaByKRunId (final String k, final int runId);

  public void updateByRunId (final int runId, final List<RunMeta> metaList);

}

