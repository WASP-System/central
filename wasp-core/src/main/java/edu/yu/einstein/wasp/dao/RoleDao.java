
/**
 *
 * RoleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the WRole Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WRole;


public interface RoleDao extends WaspDao<WRole> {

  public WRole getRoleByRoleId (final int roleId);

  public WRole getRoleByRoleName (final String roleName);

  public WRole getRoleByName (final String name);


}

