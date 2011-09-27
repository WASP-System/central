
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

import edu.yu.einstein.wasp.service.UserroleService;
import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Userrole;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

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

