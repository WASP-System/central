
/**
 *
 * RunMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.RunMetaService;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.RunMeta;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RunMetaServiceImpl extends WaspServiceImpl<RunMeta> implements RunMetaService {

  private RunMetaDao runMetaDao;
  @Autowired
  public void setRunMetaDao(RunMetaDao runMetaDao) {
    this.runMetaDao = runMetaDao;
    this.setWaspDao(runMetaDao);
  }
  public RunMetaDao getRunMetaDao() {
    return this.runMetaDao;
  }

  // **

  
  public RunMeta getRunMetaByRunMetaId (final int runMetaId) {
    return this.getRunMetaDao().getRunMetaByRunMetaId(runMetaId);
  }

  public RunMeta getRunMetaByKRunId (final String k, final int runId) {
    return this.getRunMetaDao().getRunMetaByKRunId(k, runId);
  }

  public void updateByRunId (final int runId, final List<RunMeta> metaList) {
    this.getRunMetaDao().updateByRunId(runId, metaList); 
  }

}

