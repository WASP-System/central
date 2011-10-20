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
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import edu.yu.einstein.wasp.model.Department;

// @ Transactional
@Repository
public abstract class WaspDaoImpl<E extends Serializable> extends JpaDaoSupport implements edu.yu.einstein.wasp.dao.WaspDao<E> {
 protected Class<E> entityClass;

 private static final Logger log = Logger.getLogger(WaspDaoImpl.class);


 @SuppressWarnings("unchecked")
 public WaspDaoImpl() {
   // ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
   // this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
 }

 // @ Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
 public void persist(E entity) {
	 
  setUpdateTs(entity);
  setEditorId(entity);
  
  getJpaTemplate().persist(entity);
 }

 
 // @ Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
 public E save(E entity) {
	 
    setEditorId(entity);
    setUpdateTs(entity); 
    
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
	 
  setUpdateTs(entity);
  setEditorId(entity);
	 
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
 
  @SuppressWarnings("unchecked")
  // @ Transactional
  public List findByMapDistinctOrderBy(final Map m, final List<String> distinctColumnNames, final List<String> orderByColumnNames, final String direction) {
    Object res = getJpaTemplate().execute(new JpaCallback() {

      public Object doInJpa(EntityManager em) throws PersistenceException {
        
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
        
        Query q = em.createQuery(qString);

        for (Object key: m.keySet()){
          q.setParameter(key.toString(), m.get(key));
        }

        return q.getResultList();
      }
    });

    return (List) res;
  }
  
  
  @SuppressWarnings("unchecked")
  // @ Transactional
  public List findDistinctOrderBy(final String distinctColumnName, final String orderByColumnName, final String direction) {
   Object res = getJpaTemplate().execute(new JpaCallback() {

    public Object doInJpa(EntityManager em) throws PersistenceException {
     //Query q = em.createQuery("SELECT h FROM " + entityClass.getName() + " h WHERE h." + distinctColumnName + " IN " +
    //   		"(SELECT DISTINCT j." + distinctColumnName + " FROM " + entityClass.getName() + " j ) ORDER BY h." + orderByColumnName + " " + direction + "");
    // return q.getResultList();
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
        Query q = em.createQuery(qString);
        return q.getResultList();    	
    }

   });

   return (List) res;
  }
  
  @SuppressWarnings("unchecked")
  // @ Transactional
  public List findAllOrderBy(final String orderByColumnName, final String direction) {
   Object res = getJpaTemplate().execute(new JpaCallback() {

    public Object doInJpa(EntityManager em) throws PersistenceException {
     //Query q = em.createQuery("SELECT h FROM " + entityClass.getName() + " h ORDER BY h." + orderByColumnName + " " + direction + "");
     String qString = "SELECT h FROM " + entityClass.getName() + " h";
     if( ! "".equals(orderByColumnName) &&  "".equals(direction) ){
    	 qString += " ORDER BY h." + orderByColumnName;  
     }
     else if( ! "".equals(orderByColumnName) && ! "".equals(direction) ){
    	 qString += " ORDER BY h." + orderByColumnName + " " + direction;
     }
     //logger.debug("ROBERT: " + qString);
     Query q = em.createQuery(qString);
     return q.getResultList();
    }

   });

   return (List) res;
  }
  
  private void setEditorId(E entity) {
		 try {
			 Method method = entity.getClass().getMethod("setLastUpdUser", new Class[] {Integer.TYPE});
			 
			 if (method!=null) {
				 
				 org.springframework.security.core.userdetails.User u=
					 (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				   
				 final String login = u.getUsername();
				 
				 Integer userId = (Integer)getJpaTemplate().execute(new JpaCallback() {

					   public Object doInJpa(EntityManager em) throws PersistenceException {
						   
					    Query q = em.createNativeQuery("select userId from user where login=:login").setParameter("login",login);
					    
					    return (Integer)q.getSingleResult();
					   }

					  });
				 
				 
				  method.invoke(entity, new Object[]{userId});		
			 }
		 } catch (Throwable e) {
			 
		 }
	 }
	 
	 private void setUpdateTs(E entity) {
		 try {
			 Method method = entity.getClass().getMethod("setLastUpdTs", new Class[] {Date.class});
			 
			 if (method!=null) {
			   method.invoke(entity, new Object[]{new Date()});		
			 }
		 } catch (Throwable e) {
			
		 }
	 }
	 
}

