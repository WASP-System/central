
/**
 *
 * UserPendingService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserPendingService extends WaspService<UserPending> {

  public void setUserPendingDao(UserPendingDao userPendingDao);
  public UserPendingDao getUserPendingDao();

  public UserPending getUserPendingByUserPendingId (final int userPendingId);
 

}

