package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.dao.WaspDao;

import java.util.List;


public abstract class WaspServiceImpl<E> implements WaspService<E> {

  private WaspDao<E> waspDao;
  public void setWaspDao(WaspDao<E> waspDao) {
    this.waspDao = waspDao;
  } 
  public WaspDao<E> getWaspDao() {
    return this.waspDao;
  } 

  public void setEntityManager(javax.persistence.EntityManager entityManager) {
    this.getWaspDao().setEntityManager(entityManager);
  }

  public void setEntityManagerFactory(javax.persistence.EntityManagerFactory entityManagerFactory) {
    this.getWaspDao().setEntityManagerFactory(entityManagerFactory);
  }

  public void setJpaTemplate(org.springframework.orm.jpa.JpaTemplate jpaTemplate) {
    this.getWaspDao().setJpaTemplate(jpaTemplate);
  }
 
 
  public void persist(E entity) {
    this.getWaspDao().persist(entity);
  }
 
  public E save(E entity) {
    return this.getWaspDao().save(entity);
  }
 
  public void remove(E entity) {
    this.getWaspDao().remove(entity);
  }
 
  public E merge(E entity) {
    return this.getWaspDao().merge(entity);
  }
 
  public void refresh(E entity) {
    this.getWaspDao().refresh(entity);
  }
 
  public E findById(int id) {
    return this.getWaspDao().findById(id);
  }

  public E getById(int id) {
    return this.getWaspDao().getById(id);
  }

  public E flush(E entity) {
    return this.getWaspDao().flush(entity);
  }

  public List findAll() {
    return this.getWaspDao().findAll();
  }

  public Integer removeAll() {
    return this.getWaspDao().removeAll();
  }

}

