
/**
 *
 * UserPendingDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface UserPendingDao extends WaspDao<UserPending> {

  public UserPending getUserPendingByUserPendingId (final int userPendingId);

}

