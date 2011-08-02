
/**
 *
 * RolesetService.java 
 * @author echeng (table2type.pl)
 *  
 * the RolesetService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.RolesetService;
import edu.yu.einstein.wasp.dao.RolesetDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Roleset;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolesetServiceImpl extends WaspServiceImpl<Roleset> implements RolesetService {

  private RolesetDao rolesetDao;
  @Autowired
  public void setRolesetDao(RolesetDao rolesetDao) {
    this.rolesetDao = rolesetDao;
    this.setWaspDao(rolesetDao);
  }
  public RolesetDao getRolesetDao() {
    return this.rolesetDao;
  }

  // **

  
  public Roleset getRolesetByRolesetId (final int rolesetId) {
    return this.getRolesetDao().getRolesetByRolesetId(rolesetId);
  }

  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId) {
    return this.getRolesetDao().getRolesetByParentroleIdChildroleId(parentroleId, childroleId);
  }
}

