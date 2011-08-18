
/**
 *
 * UserpasswordauthDao.java 
 * @author echeng (table2type.pl)
 *  
 * the UserpasswordauthDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface UserpasswordauthDao extends WaspDao<Userpasswordauth> {

  public Userpasswordauth getUserpasswordauthByUserId (final int UserId);

  public Userpasswordauth getUserpasswordauthByAuthcode (final String authcode);

}

