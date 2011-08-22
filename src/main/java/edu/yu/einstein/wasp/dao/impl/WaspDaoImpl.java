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

package edu.yu.einstein.wasp.dao.impl;

import edu.yu.einstein.wasp.model.WaspModel;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// @ Transactional
@Repository
public abstract class WaspDaoImpl<E extends Serializable> extends JpaDaoSupport implements edu.yu.einstein.wasp.dao.WaspDao<E> {
 protected Class<E> entityClass;

 private static Logger log = Logger.getLogger(WaspDaoImpl.class);


 @SuppressWarnings("unchecked")
 public WaspDaoImpl() {
   // ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
   // this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
 }

 // @ Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
 public void persist(E entity) {
  getJpaTemplate().persist(entity);
 }

 // @ Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
 public E save(E entity) {
   if (getJpaTemplate().contains(entity)) {
     getJpaTemplate().merge(entity);
   } else {
     getJpaTemplate().persist(entity);
   }
   getJpaTemplate().flush();
   return entity;
 }

 // @ Transactional
 public void remove(E entity) {
  getJpaTemplate().remove(entity);
 }

 // @ Transactional
 public E merge(E entity) {
  return getJpaTemplate().merge(entity);
 }

 // @ Transactional
 public void refresh(E entity) {
  getJpaTemplate().refresh(entity);
 }
 

 // @ Transactional
 public E findById(int id) {
  return (E) getJpaTemplate().find(this.entityClass, id);
 }
 public E getById(int id) {
  return (E) getJpaTemplate().find(this.entityClass, id);
 }

 // @ Transactional
 public E flush(E entity) {
  getJpaTemplate().flush();
  return entity;
 }

 @SuppressWarnings("unchecked")
 // @ Transactional
 public List findAll() {
  Object res = getJpaTemplate().execute(new JpaCallback() {

   public Object doInJpa(EntityManager em) throws PersistenceException {
    Query q = em.createQuery("SELECT h FROM "
      + entityClass.getName() + " h");
    return q.getResultList();
   }

  });

  return (List) res;
 }

 @SuppressWarnings("unchecked")
 // @ Transactional
 public Integer removeAll() {
  return (Integer) getJpaTemplate().execute(new JpaCallback() {

   public Object doInJpa(EntityManager em) throws PersistenceException {
    Query q = em.createQuery("DELETE FROM " + entityClass.getName()
      + " h");
    return q.executeUpdate();
   }

  });
 }

  @SuppressWarnings("unchecked")
  // @ Transactional
  public List findByMap(final Map m) {
    Object res = getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        boolean first = true;


        String qString = "SELECT h FROM " + entityClass.getName() + " h";
        for (Object key: m.keySet()){
          if (! first) {
             qString += " and ";
          } else { 
             qString += " WHERE ";
          }

          qString += "h." + key.toString() + " = :" + key.toString() + "\n";
          first = false;
        }

        Query q = em.createQuery(qString);

        for (Object key: m.keySet()){
          q.setParameter(key.toString(), m.get(key));
        }

        return q.getResultList();
      }
    });

    return (List) res;
  }
}

