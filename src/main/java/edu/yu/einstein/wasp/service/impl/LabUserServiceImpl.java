
/**
 *
 * LabUserServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the LabUserService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.service.LabUserService;

@Service
public class LabUserServiceImpl extends WaspServiceImpl<LabUser> implements LabUserService {

	/**
	 * labUserDao;
	 *
	 */
	private LabUserDao labUserDao;

	/**
	 * setLabUserDao(LabUserDao labUserDao)
	 *
	 * @param labUserDao
	 *
	 */
	@Override
	@Autowired
	public void setLabUserDao(LabUserDao labUserDao) {
		this.labUserDao = labUserDao;
		this.setWaspDao(labUserDao);
	}

	/**
	 * getLabUserDao();
	 *
	 * @return labUserDao
	 *
	 */
	@Override
	public LabUserDao getLabUserDao() {
		return this.labUserDao;
	}


  @Override
public LabUser getLabUserByLabUserId (final int labUserId) {
    return this.getLabUserDao().getLabUserByLabUserId(labUserId);
  }

  @Override
public LabUser getLabUserByLabIdUserId (final int labId, final int UserId) {
    return this.getLabUserDao().getLabUserByLabIdUserId(labId, UserId);
  }

}

