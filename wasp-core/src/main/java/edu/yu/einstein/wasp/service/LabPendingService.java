
/**
 *
 * LabPendingService.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.model.LabPending;

@Service
public interface LabPendingService extends WaspService<LabPending> {

	/**
	 * setLabPendingDao(LabPendingDao labPendingDao)
	 *
	 * @param labPendingDao
	 *
	 */
	public void setLabPendingDao(LabPendingDao labPendingDao);

	/**
	 * getLabPendingDao();
	 *
	 * @return labPendingDao
	 *
	 */
	public LabPendingDao getLabPendingDao();

  public LabPending getLabPendingByLabPendingId (final int labPendingId);


}

