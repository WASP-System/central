
/**
 *
 * RunService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.model.Run;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface RunService extends WaspService<Run> {

  public void setRunDao(RunDao runDao);
  public RunDao getRunDao();

  public Run getRunByRunId (final int runId);

}

