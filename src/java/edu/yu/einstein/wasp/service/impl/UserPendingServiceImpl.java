
/**
 *
 * UserPendingService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.UserPending;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPendingServiceImpl extends WaspServiceImpl<UserPending> implements UserPendingService {

  private UserPendingDao userPendingDao;
  @Autowired
  public void setUserPendingDao(UserPendingDao userPendingDao) {
    this.userPendingDao = userPendingDao;
    this.setWaspDao(userPendingDao);
  }
  public UserPendingDao getUserPendingDao() {
    return this.userPendingDao;
  }

  // **

  
  public UserPending getUserPendingByUserPendingId (final int userPendingId) {
    return this.getUserPendingDao().getUserPendingByUserPendingId(userPendingId);
  }
}

