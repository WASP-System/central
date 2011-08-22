
/**
 *
 * UserroleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserroleDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface UserroleDao extends WaspDao<Userrole> {

  public Userrole getUserroleByUserroleId (final int userroleId);

  public Userrole getUserroleByUserIdRoleId (final int UserId, final int roleId);

}

