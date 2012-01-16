
/**
 *
 * UserroleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the UserroleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.UserroleService;

@Service
public class UserroleServiceImpl extends WaspServiceImpl<Userrole> implements UserroleService {

	/**
	 * userroleDao;
	 *
	 */
	private UserroleDao userroleDao;

	/**
	 * setUserroleDao(UserroleDao userroleDao)
	 *
	 * @param userroleDao
	 *
	 */
	@Autowired
	public void setUserroleDao(UserroleDao userroleDao) {
		this.userroleDao = userroleDao;
		this.setWaspDao(userroleDao);
	}

	/**
	 * getUserroleDao();
	 *
	 * @return userroleDao
	 *
	 */
	public UserroleDao getUserroleDao() {
		return this.userroleDao;
	}


  public Userrole getUserroleByUserroleId (final int userroleId) {
    return this.getUserroleDao().getUserroleByUserroleId(userroleId);
  }

  public Userrole getUserroleByUserIdRoleId (final int UserId, final int roleId) {
    return this.getUserroleDao().getUserroleByUserIdRoleId(UserId, roleId);
  }

}

