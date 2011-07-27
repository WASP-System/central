
/**
 *
 * UsermetaDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UsermetaDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface UsermetaDao extends WaspDao<Usermeta> {

  public Usermeta getUsermetaByUsermetaId (final int usermetaId);

  public Usermeta getUsermetaByKUserId (final String k, final int UserId);

  public void updateByUserId (final int UserId, final List<Usermeta> metaList);

}

