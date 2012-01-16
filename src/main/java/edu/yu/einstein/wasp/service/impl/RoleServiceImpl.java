
/**
 *
 * RoleServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the RoleService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.service.RoleService;

@Service
public class RoleServiceImpl extends WaspServiceImpl<Role> implements RoleService {

	/**
	 * roleDao;
	 *
	 */
	private RoleDao roleDao;

	/**
	 * setRoleDao(RoleDao roleDao)
	 *
	 * @param roleDao
	 *
	 */
	@Autowired
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
		this.setWaspDao(roleDao);
	}

	/**
	 * getRoleDao();
	 *
	 * @return roleDao
	 *
	 */
	public RoleDao getRoleDao() {
		return this.roleDao;
	}


  public Role getRoleByRoleId (final int roleId) {
    return this.getRoleDao().getRoleByRoleId(roleId);
  }

  public Role getRoleByRoleName (final String roleName) {
    return this.getRoleDao().getRoleByRoleName(roleName);
  }

  public Role getRoleByName (final String name) {
    return this.getRoleDao().getRoleByName(name);
  }

}

