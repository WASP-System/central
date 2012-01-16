
/**
 *
 * LabPendingServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabPendingService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.service.LabPendingService;

@Service
public class LabPendingServiceImpl extends WaspServiceImpl<LabPending> implements LabPendingService {

	/**
	 * labPendingDao;
	 *
	 */
	private LabPendingDao labPendingDao;

	/**
	 * setLabPendingDao(LabPendingDao labPendingDao)
	 *
	 * @param labPendingDao
	 *
	 */
	@Override
	@Autowired
	public void setLabPendingDao(LabPendingDao labPendingDao) {
		this.labPendingDao = labPendingDao;
		this.setWaspDao(labPendingDao);
	}

	/**
	 * getLabPendingDao();
	 *
	 * @return labPendingDao
	 *
	 */
	@Override
	public LabPendingDao getLabPendingDao() {
		return this.labPendingDao;
	}


  @Override
public LabPending getLabPendingByLabPendingId (final int labPendingId) {
    return this.getLabPendingDao().getLabPendingByLabPendingId(labPendingId);
  }

}

