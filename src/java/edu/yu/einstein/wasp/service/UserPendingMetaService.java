
/**
 *
 * UserPendingMetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.UserPendingMetaDao;
import edu.yu.einstein.wasp.model.UserPendingMeta;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface UserPendingMetaService extends WaspService<UserPendingMeta> {

  public void setUserPendingMetaDao(UserPendingMetaDao userPendingMetaDao);
  public UserPendingMetaDao getUserPendingMetaDao();

  public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId);

  public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userpendingId);

  public void updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList);

}

