
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
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
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM User a WHERE "
       + "a.UserId = :UserId";
     Query query = em.createQuery(queryString);
      query.setParameter("UserId", UserId);

    return query.getResultList();
  }
  });
    List<User> results = (List<User>) res;
    if (results.size() == 0) {
      User rt = new User();
      return rt;
    }
    return (User) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public User getUserByLogin (final String login) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM User a WHERE "
       + "a.login = :login";
     Query query = em.createQuery(queryString);
      query.setParameter("login", login);

    return query.getResultList();
  }
  });
    List<User> results = (List<User>) res;
    if (results.size() == 0) {
      User rt = new User();
      return rt;
    }
    return (User) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public User getUserByEmail (final String email) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM User a WHERE "
       + "a.email = :email";
     Query query = em.createQuery(queryString);
      query.setParameter("email", email);

    return query.getResultList();
  }
  });
    List<User> results = (List<User>) res;
    if (results.size() == 0) {
      User rt = new User();
      return rt;
    }
    return (User) results.get(0);
  }


}

