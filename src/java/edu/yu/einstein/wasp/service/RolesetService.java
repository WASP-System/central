
/**
 *
 * RolesetService.java 
 * @author echeng (table2type.pl)
 *  
 * the RolesetService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.RolesetDao;
import edu.yu.einstein.wasp.model.Roleset;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface RolesetService extends WaspService<Roleset> {

  public void setRolesetDao(RolesetDao rolesetDao);
  public RolesetDao getRolesetDao();

  public Roleset getRolesetByRolesetId (final int rolesetId);

  public Roleset getRolesetByParentroleIdChildroleId (final int parentroleId, final int childroleId);

}

