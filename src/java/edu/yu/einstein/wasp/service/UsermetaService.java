
/**
 *
 * UsermetaService.java 
 * @author echeng (table2type.pl)
 *  
 * the UsermetaService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.UsermetaDao;
import edu.yu.einstein.wasp.model.Usermeta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface UsermetaService extends WaspService<Usermeta> {

  public void setUsermetaDao(UsermetaDao usermetaDao);
  public UsermetaDao getUsermetaDao();

  public Usermeta getUsermetaByUsermetaId (final int usermetaId);

  public Usermeta getUsermetaByKUserId (final String k, final int UserId);

  public void updateByUserId (final int UserId, final List<Usermeta> metaList);

}

