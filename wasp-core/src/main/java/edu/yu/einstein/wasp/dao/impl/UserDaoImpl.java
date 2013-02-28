
/**
 *
 * UserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the WUser object
 *
 *
 **/
 
package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.WUser;

@Transactional
@Repository
public class UserDaoImpl extends WaspDaoImpl<WUser> implements edu.yu.einstein.wasp.dao.UserDao {

  public UserDaoImpl() {
    super();
    this.entityClass = WUser.class;
  }

  @Override
  @Transactional
  public WUser getUserByUserId (final int userId) {
    HashMap<String, Integer> m = new HashMap<String, Integer>();
    m.put("id", userId);
    List<WUser> results = this.findByMap(m);
    if (results.size() == 0) {
      WUser rt = new WUser();
      return rt;
    }
    return results.get(0);
  }


  @Override
  @Transactional
  public WUser getUserByLogin (final String login) {
    HashMap<String, String> m = new HashMap<String, String>();
    m.put("login", login);
    List<WUser> results = this.findByMap(m);
    if (results.size() == 0) {
      WUser rt = new WUser();
      return rt;
    }
    return results.get(0);
  }


  @Override
  @Transactional
  public WUser getUserByEmail (final String email) {
    HashMap<String, String> m = new HashMap<String, String>();
    m.put("email", email);
    List<WUser> results = this.findByMap(m);
    if (results.size() == 0) {
      WUser rt = new WUser();
      return rt;
    }
    return results.get(0);
  }
  
 
  @Override
  public List<WUser> getActiveUsers(){
	  Map<String, Integer> queryMap = new HashMap<String, Integer>();
	  queryMap.put("isActive", 1);
	  return this.findByMap(queryMap);
  }
}

