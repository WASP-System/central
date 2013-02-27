
/**
 *
 * UserpasswordauthDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Userpasswordauth Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Userpasswordauth;


public interface UserpasswordauthDao extends WaspDao<Userpasswordauth> {

  public Userpasswordauth getUserpasswordauthByUserId (final int userId);

  public Userpasswordauth getUserpasswordauthByAuthcode (final String authcode);


}

