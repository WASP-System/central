
/**
 *
 * RolesetServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RolesetService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RolesetDao;
import edu.yu.einstein.wasp.model.Roleset;
import edu.yu.einstein.wasp.service.RolesetService;

@Service
public class RolesetServiceImpl extends WaspServiceImpl<Roleset> implements RolesetService {

	/**
	 * rolesetDao;
	 *
	 */
	private RolesetDao rolesetDao;

	/**
	 * setRolesetDao(RolesetDao rolesetDao)
	 *
	 * @param rolesetDao
	 *
	 */
	@Autowired
	public void setRolesetDao(RolesetDao rolesetDao) {
		this.rolesetDao = rolesetDao;
		this.setWaspDao(rolesetDao);
	}

	/**
	 * getRolesetDao();
	 *
	 * @return rolesetDao
	 *
	 */
	public RolesetDao getRolesetDao() {
		return this.rolesetDao;
	}


  public Roleset getRolesetByRolesetId (final int rolesetId) {
    return this.getRolesetDao().getRolesetByRolesetId(rolesetId);
  }

  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId) {
    return this.getRolesetDao().getRolesetByParentroleIdChildroleId(parentroleId, childroleId);
  }

}

