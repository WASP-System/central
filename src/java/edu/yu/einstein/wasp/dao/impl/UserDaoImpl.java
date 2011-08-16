
/**
 *
 * UserImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the User object
 *
 *
 **/
 
package edu.yu.einstein.wasp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.User;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class UserDaoImpl extends WaspDaoImpl<User> implements edu.yu.einstein.wasp.dao.UserDao {

  public UserDaoImpl() {
    super();
    this.entityClass = User.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public User getUserByUserId (final int UserId) {
    HashMap m = new HashMap();
    m.put("UserId", UserId);
    List<User> results = (List<User>) this.findByMap((Map) m);
    if (results.size() == 0) {
      User rt = new User();
      return rt;
    }
    return (User) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public User getUserByLogin (final String login) {
    HashMap m = new HashMap();
    m.put("login", login);
    List<User> results = (List<User>) this.findByMap((Map) m);
    if (results.size() == 0) {
      User rt = new User();
      return rt;
    }
    return (User) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public User getUserByEmail (final String email) {
    HashMap m = new HashMap();
    m.put("email", email);
    List<User> results = (List<User>) this.findByMap((Map) m);
    if (results.size() == 0) {
      User rt = new User();
      return rt;
    }
    return (User) results.get(0);
  }
  
  public boolean loginExists(final String login, final Integer excludeUserId) {

	    return (Boolean)getJpaTemplate().execute(new JpaCallback() {

	      public Boolean doInJpa(EntityManager em) throws PersistenceException {
	    	  
	    	if (excludeUserId==null) {
	    		 List l = em.createNativeQuery("select 1 from user where login=:login").setParameter("login", login).getResultList();
	    		 return !l.isEmpty();
	    	}
	    	
	    	 List l = em.createNativeQuery("select 1 from user where login=:login and userId!=:userId")
	    		 .setParameter("login", login)
	    		 .setParameter("userId", excludeUserId)
	    		 .getResultList();
	    	 return !l.isEmpty();
	    	}	      
	    });
	  }


}

