
/**
 *
 * UserPendingMetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserPendingMeta Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface UserPendingMetaDao extends WaspDao<UserPendingMeta> {

  public UserPendingMeta getUserPendingMetaByUserPendingMetaId (final int userPendingMetaId);

  public UserPendingMeta getUserPendingMetaByKUserpendingId (final String k, final int userpendingId);


  public void updateByUserpendingId (final String area, final int userpendingId, final List<UserPendingMeta> metaList);

  public void updateByUserpendingId (final int userpendingId, final List<UserPendingMeta> metaList);




}

