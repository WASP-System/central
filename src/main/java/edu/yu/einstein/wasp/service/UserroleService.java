
/**
 *
 * UserroleService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserroleService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.model.Userrole;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UserroleService extends WaspService<Userrole> {

  public void setUserroleDao(UserroleDao userroleDao);
  public UserroleDao getUserroleDao();

  public Userrole getUserroleByUserroleId (final int userroleId);

  public Userrole getUserroleByUserIdRoleId (final int UserId, final int roleId);

}

