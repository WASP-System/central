
/**
 *
 * RoleService.java 
 * @author echeng (table2type.pl)
 *  
 * the RoleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.model.Role;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface RoleService extends WaspService<Role> {

  public void setRoleDao(RoleDao roleDao);
  public RoleDao getRoleDao();

  public Role getRoleByRoleId (final int roleId);

  public Role getRoleByRoleName (final String roleName);

  public Role getRoleByName (final String name);

}

