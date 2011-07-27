
/**
 *
 * RoleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the RoleDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface RoleDao extends WaspDao<Role> {

  public Role getRoleByRoleId (final int roleId);

  public Role getRoleByRoleName (final String roleName);

  public Role getRoleByName (final String name);

}

