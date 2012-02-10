package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.service.WaspService;


public abstract class WaspServiceImpl<E> implements WaspService<E> {

  private WaspDao<E> waspDao;
  public void setWaspDao(WaspDao<E> waspDao) {
    this.waspDao = waspDao;
  } 
  @Override
public WaspDao<E> getWaspDao() {
    return this.waspDao;
  } 

  
  @Override
public void persist(E entity) {
    this.getWaspDao().persist(entity);
  }
 
  @Override
public E save(E entity) {
    return this.getWaspDao().save(entity);
  }
 
  @Override
public void remove(E entity) {
    this.getWaspDao().remove(entity);
  }
 
  @Override
public E merge(E entity) {
    return this.getWaspDao().merge(entity);
  }
 
  @Override
public void refresh(E entity) {
    this.getWaspDao().refresh(entity);
  }
 
  @Override
public E findById(int id) {
    return this.getWaspDao().findById(id);
  }

  @Override
public E getById(int id) {
    return this.getWaspDao().getById(id);
  }

  @Override
public E flush(E entity) {
    return this.getWaspDao().flush(entity);
  }

  @Override
public List findAll() {
    return this.getWaspDao().findAll();
  }

  @Override
public Integer removeAll() {
    return this.getWaspDao().removeAll();
  }

  @Override
public List findByMap(Map m) {
    return this.getWaspDao().findByMap(m);
  }
  
  @Override
  public List findByMapExcept(Map m) {
      return this.getWaspDao().findByMapExcept(m);
    }

  @Override
public List findDistinctOrderBy(final String distinctColumnName, final String orderByColumnName, final String direction){
	  return this.getWaspDao().findDistinctOrderBy(distinctColumnName, orderByColumnName, direction);  
  }
  
  @Override
public List findDistinctOrderBy(final String distinctColumnName, final String direction){
	  return this.getWaspDao().findDistinctOrderBy(distinctColumnName, distinctColumnName, direction);  
  }
  
  @Override
public List findAllOrderBy(final String orderByColumnName, final String direction){
	  return this.getWaspDao().findAllOrderBy(orderByColumnName, direction);
  }
  
  @Override
public List findByMapDistinctOrderBy(final Map m, final List<String> distinctColumnNames, final List<String> orderByColumnNames, final String direction) {
	  return this.getWaspDao().findByMapDistinctOrderBy(m, distinctColumnNames, orderByColumnNames, direction);
  }	
  
}

