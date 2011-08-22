/**
 *
 * WaspDao.java
 * @author echeng
 * 
 * this is the base class,
 * every class in the system should extend this.
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.WaspModel;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface WaspDao<E> {

 // from JPADaoSupport
 // public javax.persistence.EntityManager getEntityManager();
 public void setEntityManager(javax.persistence.EntityManager entityManager);

 // public javax.persistence.EntityManagerFactory getEntityManagerFactory();
 public void setEntityManagerFactory(javax.persistence.EntityManagerFactory entityManagerFactory);

 // public JpaTemplate getJpaTemplate(); 
 public void setJpaTemplate(JpaTemplate jpaTemplate); 

 public void persist(E entity);

 public E save(E entity);

 public void remove(E entity);

 public E merge(E entity);

 public void refresh(E entity);

 public E findById(int id);
 public E getById(int id);
 public E flush(E entity);
 public List findAll();
 public Integer removeAll();

 public List findByMap(Map m);

}

