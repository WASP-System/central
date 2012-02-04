
/**
 *
 * RoleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Role Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Role;


public interface RoleDao extends WaspDao<Role> {

  public Role getRoleByRoleId (final int roleId);

  public Role getRoleByRoleName (final String roleName);

  public Role getRoleByName (final String name);


}

