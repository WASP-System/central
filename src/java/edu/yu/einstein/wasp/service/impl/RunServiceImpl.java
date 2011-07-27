
/**
 *
 * RunService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Run;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RunServiceImpl extends WaspServiceImpl<Run> implements RunService {

  private RunDao runDao;
  @Autowired
  public void setRunDao(RunDao runDao) {
    this.runDao = runDao;
    this.setWaspDao(runDao);
  }
  public RunDao getRunDao() {
    return this.runDao;
  }

  // **

  
  public Run getRunByRunId (final int runId) {
    return this.getRunDao().getRunByRunId(runId);
  }
}

