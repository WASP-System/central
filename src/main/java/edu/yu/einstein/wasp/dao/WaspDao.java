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

import java.util.List;
import java.util.Map;


public interface WaspDao<E> {

 public void persist(E entity);

 public E save(E entity);

 public void remove(E entity);

 public E merge(E entity);

 public void refresh(E entity);

 public E findById(int id);
 public E getById(int id);
 public E flush(E entity);
 public List<E> findAll();
 public Integer removeAll();

 public List<E> findByMap(Map m);
 
 public List<E> findByMapExcept(Map m);
 
 
 
 /**
  * Generates and executes simple SQL statement that includes
  * 	1. single (optional) DISTINCT   
  * 	2. single (optional) ORDER BY 
  * 	3. single (optional) direction for ORDER BY (used only if an ORDER BY paramater is provided) 
  * 
  * 	@param final String distinctColumnName for the single (may be null) DISTINCT attribute
  * 	@param final String orderByColumnName for the single (may be null) ORDER BY attribute
  * 	@param final String direction used in conjunction with the ORDER BY statement (if orderByColumnName is not empty)
  * 	@return List of objects returned by the database that fulfill the query 
  * 
  * 	example use: List<Department> departmentList = this.getDepartmentService().findDistinctOrderBy("name", "name", "ASC");
  */
 public List<E> findDistinctOrderBy(final String distinctColumnName, final String orderByColumnName, final String direction);
 
 /**
  * Generates and executes simple SQL statement that includes
  *		1. single (optional) ORDER BY 
  * 	2. single (optional) direction for ORDER BY (used only if an ORDER BY paramater is provided) 
  * 
  * 	@param final String orderByColumnName for the single (may be null) ORDER BY attribute
  * 	@param final String direction used in conjunction with the ORDER BY statement (if orderByColumnName is not null)
  * 	@return List of objects returned by the database that fulfill the query 
  * 
  *    example use: List<Department> departmentList = this.getDepartmentService().findAllOrderBy("name", "ASC");
  */
 public List<E> findAllOrderBy(final String orderByColumnName, final String direction);
 
 
 /**
  * Generates and executes SQL statement that includes
  * 	1. multiple (optional) WHERE attributes 
  * 	2. multiple (optional) DISTINCT attributes 
  * 	3. multiple (optional) ORDER BY attributes 
  * 	4. single (optional) direction (DESC or ASC) for ORDER BY  (if there is at least one ORDER BY attribute)
  * 
  * 	@param final Map m for the multiple (may be null) WHERE attributes
  * 	@param final List<String> distinctColumnNames for the multiple (may be null) DISTINCT attributes
  * 	@param final List<String> orderByColumnNames for the multiple (may be null) ORDER BY attributes
  * 	@param final String direction (may be null) used in conjunction with the ORDER BY statement (if at least one ORDER BY attribute provided) 
  * 	@return List of objects returned by the database that fulfill the query 
  * 
  * example use:
  * 	Map whereConstraints = new HashMap();
  *	whereConstraints.put("isInternal", 1);
  *	whereConstraints.put("isActive", 1);
  *	List<String> distinctConstraints = new ArrayList<String>();
  *	distinctConstraints.add("name");	  
  *	List<String> orderConstraints = new ArrayList<String>();
  *	orderConstraints.add("name");	  
  *	String direction = "ASC";
  *	List<Department> departmentList = this.getDepartmentService().findByMapDistinctOrderBy(whereConstraints, distinctConstraints, orderConstraints, direction);
  */
 public List<E> findByMapDistinctOrderBy(final Map m, final List<String> distinctColumnNames, final List<String> orderByColumnNames, final String direction);

}

