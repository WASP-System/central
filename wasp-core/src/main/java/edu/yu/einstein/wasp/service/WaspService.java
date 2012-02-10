package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.dao.WaspDao;


public interface WaspService<E> {

 public WaspDao getWaspDao();

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

 public List findByMapExcept(Map m);

 public List findDistinctOrderBy(final String distinctColumnName, final String direction);
 public List findDistinctOrderBy(final String distinctColumnName, final String orderByColumnName, final String direction);
 public List findAllOrderBy(final String orderByColumnName, final String direction);
 public List findByMapDistinctOrderBy(final Map m, final List<String> distinctColumnNames, final List<String> orderByColumnNames, final String direction);

} 
