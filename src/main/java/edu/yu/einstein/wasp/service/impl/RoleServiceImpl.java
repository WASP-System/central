
/**
 *
 * RoleService.java 
 * @author echeng (table2type.pl)
 *  
 * the RoleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Role;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl extends WaspServiceImpl<Role> implements RoleService {

  private RoleDao roleDao;
  @Autowired
  public void setRoleDao(RoleDao roleDao) {
    this.roleDao = roleDao;
    this.setWaspDao(roleDao);
  }
  public RoleDao getRoleDao() {
    return this.roleDao;
  }

  // **

  
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

