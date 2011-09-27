
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

import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.LabPending;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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
	public LabPendingDao getLabPendingDao() {
		return this.labPendingDao;
	}


  public LabPending getLabPendingByLabPendingId (final int labPendingId) {
    return this.getLabPendingDao().getLabPendingByLabPendingId(labPendingId);
  }

}

