
/**
 *
 * RoleService.java 
 * @author echeng (table2type.pl)
 *  
 * the RoleService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.model.Role;

@Service
public interface RoleService extends WaspService<Role> {

	/**
	 * setRoleDao(RoleDao roleDao)
	 *
	 * @param roleDao
	 *
	 */
	public void setRoleDao(RoleDao roleDao);

	/**
	 * getRoleDao();
	 *
	 * @return roleDao
	 *
	 */
	public RoleDao getRoleDao();

  public Role getRoleByRoleId (final int roleId);

  public Role getRoleByRoleName (final String roleName);

  public Role getRoleByName (final String name);


}
