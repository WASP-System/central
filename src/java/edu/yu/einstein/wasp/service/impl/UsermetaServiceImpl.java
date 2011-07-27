
/**
 *
 * UsermetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the UsermetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.UsermetaService;
import edu.yu.einstein.wasp.dao.UsermetaDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Usermeta;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsermetaServiceImpl extends WaspServiceImpl<Usermeta> implements UsermetaService {

  private UsermetaDao usermetaDao;
  @Autowired
  public void setUsermetaDao(UsermetaDao usermetaDao) {
    this.usermetaDao = usermetaDao;
    this.setWaspDao(usermetaDao);
  }
  public UsermetaDao getUsermetaDao() {
    return this.usermetaDao;
  }

  // **

  
  public Usermeta getUsermetaByUsermetaId (final int usermetaId) {
    return this.getUsermetaDao().getUsermetaByUsermetaId(usermetaId);
  }

  public Usermeta getUsermetaByKUserId (final String k, final int UserId) {
    return this.getUsermetaDao().getUsermetaByKUserId(k, UserId);
  }

  public void updateByUserId (final int UserId, final List<Usermeta> metaList) {
    this.getUsermetaDao().updateByUserId(UserId, metaList); 
  }

}

