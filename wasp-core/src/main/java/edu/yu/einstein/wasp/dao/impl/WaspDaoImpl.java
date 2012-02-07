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
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

@Repository
public abstract class WaspDaoImpl<E extends Serializable> implements edu.yu.einstein.wasp.dao.WaspDao<E> {
 protected Class<E> entityClass;

 private static final Logger log = Logger.getLogger(WaspDaoImpl.class);

 @PersistenceContext
 protected EntityManager entityManager;

 @Override
public void persist(final E entity) {
	 
  setUpdateTs(entity);
  setEditorId(entity);
  entityManager.persist(entity);
 }

 
 @Override
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

 @Override
public void remove(E entity) {
  entityManager.remove(entity);
 }

 @Override
public E merge(E entity) {
	 
  setUpdateTs(entity);
  setEditorId(entity);
	 
  return entityManager.merge(entity);
 }

 @Override
public void refresh(E entity) {
  entityManager.refresh(entity);
 }
 

 @Override
public E findById(int id) {
  return entityManager.find(this.entityClass, id);
 }
 @Override
public E getById(int id) {
  return entityManager.find(this.entityClass, id);
 }

 @Override
public E flush(E entity) {
  entityManager.flush();
  return entity;
 }

 @Override
@SuppressWarnings("unchecked")
 public List<E> findAll() {
	 return this.entityManager.createQuery( "FROM " + entityClass.getName() ).getResultList();
 }

 @Override
public Integer removeAll() {
	 return entityManager.createQuery("DELETE FROM " + entityClass.getName() + " h").executeUpdate();
 }

  @Override
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
  
@Override
@SuppressWarnings("unchecked")
   public List<E> findByMapExcept(final Map m) {
	  boolean first = true;
	  	String qString = "SELECT h FROM " + entityClass.getName() + " h";
        for (Object key: m.keySet()){
          if (! first) {
             qString += " and ";
          } else { 
             qString += " WHERE ";
          }

          qString += "h." + key.toString() + " != :" + key.toString() + "\n";
          first = false;
        }

        Query q = entityManager.createQuery(qString);

        for (Object key: m.keySet()){
          q.setParameter(key.toString(), m.get(key));
        }
        System.out.println("qString="+q.toString());

        return q.getResultList();
  }
 
  @Override
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

  
  
  @Override
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
  
  @Override
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
			 Method method = entity.getClass().getMethod("setLastUpdUser", new Class[] {Integer.class});
			 
			 if (method!=null) {

				 
				 //org.springframework.security.core.userdetails.User u=
				 //	 (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				   
				Integer userId = new Integer(0);
				try {
					final String login = SecurityContextHolder.getContext().getAuthentication().getName();
					if (!login.equals("anonymousUser")) {
						Integer newUserId = (Integer)entityManager.createNativeQuery("select userId from user where login=:login").setParameter("login",login).getSingleResult();
						if (newUserId != null) {
							userId = newUserId;
						}
				 	}
				} catch (Exception e) {
					// empty catch in case login or userId can't be found. 
				}

				 method.invoke(entity, new Object[]{userId});		
			 }
		 } catch (Throwable e) {
			 log.warn("setEditorId() threw exception: " + e.getMessage()  );

		 }
	 }
	 
	 private void setUpdateTs(E entity) {
		 try {
			 Method method = entity.getClass().getMethod("setLastUpdTs", new Class[] {Date.class});
			 if (method!=null) {
			   method.invoke(entity, new Object[]{new Date()});		
			 }
		 } catch (Throwable e) {
			 log.warn("setUpdateTs() threw exception: " + e.getMessage()  );
		 }
	 }
	 
}
