package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.WaspService;
import edu.yu.einstein.wasp.dao.WaspDao;

import java.util.List;
import java.util.Map;


public abstract class WaspServiceImpl<E> implements WaspService<E> {

  private WaspDao<E> waspDao;
  public void setWaspDao(WaspDao<E> waspDao) {
    this.waspDao = waspDao;
  } 
  public WaspDao<E> getWaspDao() {
    return this.waspDao;
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

  public List findByMap(Map m) {
    return this.getWaspDao().findByMap(m);
  }

  public List findDistinctOrderBy(final String distinctColumnName, final String orderByColumnName, final String direction){
	  return this.getWaspDao().findDistinctOrderBy(distinctColumnName, orderByColumnName, direction);  
  }
  
  public List findDistinctOrderBy(final String distinctColumnName, final String direction){
	  return this.getWaspDao().findDistinctOrderBy(distinctColumnName, distinctColumnName, direction);  
  }
  
  public List findAllOrderBy(final String orderByColumnName, final String direction){
	  return this.getWaspDao().findAllOrderBy(orderByColumnName, direction);
  }
  
  public List findByMapDistinctOrderBy(final Map m, final List<String> distinctColumnNames, final List<String> orderByColumnNames, final String direction) {
	  return this.getWaspDao().findByMapDistinctOrderBy(m, distinctColumnNames, orderByColumnNames, direction);
  }	  
}

