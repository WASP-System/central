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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import edu.yu.einstein.wasp.model.Department;

@Repository
public abstract class WaspDaoImpl<E extends Serializable> implements edu.yu.einstein.wasp.dao.WaspDao<E> {
 protected Class<E> entityClass;

 private static final Logger log = Logger.getLogger(WaspDaoImpl.class);

 @PersistenceContext
 protected EntityManager entityManager;

 public void persist(final E entity) {
	 
  setUpdateTs(entity);
  setEditorId(entity);
  entityManager.persist(entity);
 }

 
 public E save(E entity) {
	 
    setEditorId(entity);
    setUpdateTs(entity); 
    
   if (entityManager.contains(entity)) {
     entityManager.merge(entity);
   } else {
     entityManager.persist(entity);
   }
   
   
   entityManager.flush();
   return entity;
 }

 public void remove(E entity) {
  entityManager.remove(entity);
 }

 public E merge(E entity) {
	 
  setUpdateTs(entity);
  setEditorId(entity);
	 
  return entityManager.merge(entity);
 }

 public void refresh(E entity) {
  entityManager.refresh(entity);
 }
 

 public E findById(int id) {
  return (E) entityManager.find(this.entityClass, id);
 }
 public E getById(int id) {
  return (E) entityManager.find(this.entityClass, id);
 }

 public E flush(E entity) {
  entityManager.flush();
  return entity;
 }

 @SuppressWarnings("unchecked")
 public List<E> findAll() {
	 return this.entityManager.createQuery( "FROM " + entityClass.getName() ).getResultList();
 }

 public Integer removeAll() {
	 return entityManager.createQuery("DELETE FROM " + entityClass.getName() + " h").executeUpdate();
 }

  @SuppressWarnings("unchecked")
   public List<E> findByMap(final Map m) {
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

        Query q = entityManager.createQuery(qString);

        for (Object key: m.keySet()){
          q.setParameter(key.toString(), m.get(key));
        }

        return q.getResultList();
  }
 
  @SuppressWarnings("unchecked")
   public List<E> findByMapDistinctOrderBy(final Map m, final List<String> distinctColumnNames, final List<String> orderByColumnNames, final String direction) {
    	boolean where = false;
    	boolean firstMap = true;
        boolean firstDistinct = true;
        boolean firstOrderBy = true;

        String qString = "SELECT h FROM " + entityClass.getName() + " h";
               
        for (Object key: m.keySet()){
          if(where == false){
        	  qString += " WHERE ";
        	  where = true;
          }
          else if (firstMap == false) {
             qString += " and ";
          } 
          qString += "h." + key.toString() + " = :" + key.toString();
          firstMap = false;
        }
        if (distinctColumnNames != null && ! "".equals(distinctColumnNames)){
	        for(String distinctColumnName : distinctColumnNames){
	        	if(where == false){
	          	  qString += " WHERE (";
	          	  where = true;
	            }
	        	else if(firstDistinct == true){
	        		qString += " AND (";
	        	}
	        	else if(firstDistinct == false){ 
	                qString += ", ";
	             }
	        	
	        	qString += "h." + distinctColumnName;
	        	firstDistinct = false;
	        }
	        if(firstDistinct == false){ 
	            qString += ") IN (SELECT DISTINCT";
	            firstDistinct = true;
	            
	            for(String distinctColumnName : distinctColumnNames){
	            	
	            	if(firstDistinct == false){
	                	qString += ", ";
	                }
	            	qString += " j." + distinctColumnName;
	            	firstDistinct = false;
	            }
	         }
	        if(firstDistinct == false){
	        	qString += " FROM " + entityClass.getName() + " j) ";
	        }
        }
        if (orderByColumnNames != null && !orderByColumnNames.isEmpty() ){  
	        for(String orderByColumnName : orderByColumnNames){
	        	if(firstOrderBy == true){
	        		qString += " ORDER BY ";
	        	}
	        	else if(firstOrderBy == false){
	        		qString += ", ";
	        	}
	        	qString += "h." + orderByColumnName;
	        	firstOrderBy = false;
	        }
	        if( firstOrderBy == false && direction != null && ! "".equals(direction) ){
	        	qString += " " + direction;
	        }
        }
        //logger.debug("ROBERT: " + qString);
        
        Query q = entityManager.createQuery(qString);

        for (Object key: m.keySet()){
          q.setParameter(key.toString(), m.get(key));
        }

        return q.getResultList();
}

  
  
  @SuppressWarnings("unchecked")
  public List<E> findDistinctOrderBy(final String distinctColumnName, final String orderByColumnName, final String direction) {
    
        String qString = "SELECT h FROM " + entityClass.getName() + " h";
        
        if(distinctColumnName != null && ! "".equals(distinctColumnName)){
        	qString += " WHERE h." + distinctColumnName + " IN (SELECT DISTINCT j." + distinctColumnName + " FROM " + entityClass.getName() + " j)";
        }
        
        if( orderByColumnName != null && ! "".equals(orderByColumnName) && direction != null && "".equals(direction) ){
       	 qString += " ORDER BY h." + orderByColumnName;  
        }
        else if(orderByColumnName != null &&  ! "".equals(orderByColumnName) && direction != null && ! "".equals(direction) ){
       	 qString += " ORDER BY h." + orderByColumnName + " " + direction;
        }
        //logger.debug("ROBERT: " + qString);
        Query q = entityManager.createQuery(qString);
        return q.getResultList();    	

  }
  
  @SuppressWarnings("unchecked")
  public List findAllOrderBy(final String orderByColumnName, final String direction) {
     String qString = "SELECT h FROM " + entityClass.getName() + " h";
     if( ! "".equals(orderByColumnName) &&  "".equals(direction) ){
    	 qString += " ORDER BY h." + orderByColumnName;  
     }
     else if( ! "".equals(orderByColumnName) && ! "".equals(direction) ){
    	 qString += " ORDER BY h." + orderByColumnName + " " + direction;
     }
     Query q = entityManager.createQuery(qString);
     return q.getResultList();
  }
  
  private void setEditorId(E entity) {
		 try {
			 Method method = entity.getClass().getMethod("setLastUpdUser", new Class[] {Integer.TYPE});
			 
			 if (method!=null) {
				 
				 org.springframework.security.core.userdetails.User u=
					 (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				   
				 final String login = u.getUsername();
				 
				 Integer userId = (Integer)entityManager.createNativeQuery("select userId from user where login=:login").setParameter("login",login).getSingleResult();
	
				 method.invoke(entity, new Object[]{userId});		
			 }
		 } catch (Throwable e) {
			 log.warn("setEditorId() threw exeption: " + e.getMessage()  );
		 }
	 }
	 
	 private void setUpdateTs(E entity) {
		 try {
			 Method method = entity.getClass().getMethod("setLastUpdTs", new Class[] {Date.class});
			 
			 if (method!=null) {
			   method.invoke(entity, new Object[]{new Date()});		
			 }
		 } catch (Throwable e) {
			 log.warn("setUpdateTs() threw exeption: " + e.getMessage()  );
		 }
	 }
	 
}

