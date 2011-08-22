
/**
 *
 * RunLanefileService.java 
 * @author echeng (table2type.pl)
 *  
 * the RunLanefileService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.RunLanefileService;
import edu.yu.einstein.wasp.dao.RunLanefileDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.RunLanefile;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RunLanefileServiceImpl extends WaspServiceImpl<RunLanefile> implements RunLanefileService {

  private RunLanefileDao runLanefileDao;
  @Autowired
  public void setRunLanefileDao(RunLanefileDao runLanefileDao) {
    this.runLanefileDao = runLanefileDao;
    this.setWaspDao(runLanefileDao);
  }
  public RunLanefileDao getRunLanefileDao() {
    return this.runLanefileDao;
  }

  // **

  
  public RunLanefile getRunLanefileByRunLanefileId (final int runLanefileId) {
    return this.getRunLanefileDao().getRunLanefileByRunLanefileId(runLanefileId);
  }

  public RunLanefile getRunLanefileByFileId (final int fileId) {
    return this.getRunLanefileDao().getRunLanefileByFileId(fileId);
  }
}

