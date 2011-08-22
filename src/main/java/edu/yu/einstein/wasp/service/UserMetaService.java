
/**
 *
 * UserMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.model.UserMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserMetaService extends WaspService<UserMeta> {

  public void setUserMetaDao(UserMetaDao userMetaDao);
  public UserMetaDao getUserMetaDao();

  public UserMeta getUserMetaByUserMetaId (final int userMetaId);

  public UserMeta getUserMetaByKUserId (final String k, final int UserId);

  public void updateByUserId (final int UserId, final List<UserMeta> metaList);

}

