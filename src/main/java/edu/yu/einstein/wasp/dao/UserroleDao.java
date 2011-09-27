
/**
 *
 * UserroleDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Userrole Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface UserroleDao extends WaspDao<Userrole> {

  public Userrole getUserroleByUserroleId (final int userroleId);

  public Userrole getUserroleByUserIdRoleId (final int UserId, final int roleId);


}

