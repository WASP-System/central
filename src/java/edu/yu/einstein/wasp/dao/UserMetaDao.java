
/**
 *
 * UserMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserMetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface UserMetaDao extends WaspDao<UserMeta> {

  public UserMeta getUserMetaByUserMetaId (final int userMetaId);

  public UserMeta getUserMetaByKUserId (final String k, final int UserId);

  public void updateByUserId (final int UserId, final List<UserMeta> metaList);

}

