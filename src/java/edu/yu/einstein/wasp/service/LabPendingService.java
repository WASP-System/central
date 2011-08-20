
/**
 *
 * LabPendingService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.model.LabPending;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface LabPendingService extends WaspService<LabPending> {

  public void setLabPendingDao(LabPendingDao labPendingDao);
  public LabPendingDao getLabPendingDao();

  public LabPending getLabPendingByLabPendingId (final int labPendingId);

}

