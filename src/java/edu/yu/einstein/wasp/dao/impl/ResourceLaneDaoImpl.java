
/**
 *
 * ResourceLaneImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceLane object
 *
 *
 **/

package edu.yu.einstein.wasp.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.model.ResourceLane;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ResourceLaneDaoImpl extends WaspDaoImpl<ResourceLane> implements edu.yu.einstein.wasp.dao.ResourceLaneDao {

  public ResourceLaneDaoImpl() {
    super();
    this.entityClass = ResourceLane.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public ResourceLane getResourceLaneByResourceLaneId (final int resourceLaneId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM ResourceLane a WHERE "
       + "a.resourceLaneId = :resourceLaneId";
     Query query = em.createQuery(queryString);
      query.setParameter("resourceLaneId", resourceLaneId);

    return query.getResultList();
  }
  });
    List<ResourceLane> results = (List<ResourceLane>) res;
    if (results.size() == 0) {
      ResourceLane rt = new ResourceLane();
      return rt;
    }
    return (ResourceLane) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public ResourceLane getResourceLaneByINameResourceId (final String iName, final int resourceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM ResourceLane a WHERE "
       + "a.iName = :iName"
       + " AND "+ "a.resourceId = :resourceId";
     Query query = em.createQuery(queryString);
      query.setParameter("iName", iName);
      query.setParameter("resourceId", resourceId);

    return query.getResultList();
  }
  });
    List<ResourceLane> results = (List<ResourceLane>) res;
    if (results.size() == 0) {
      ResourceLane rt = new ResourceLane();
      return rt;
    }
    return (ResourceLane) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public ResourceLane getResourceLaneByNameResourceId (final String name, final int resourceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM ResourceLane a WHERE "
       + "a.name = :name"
       + " AND "+ "a.resourceId = :resourceId";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);
      query.setParameter("resourceId", resourceId);

    return query.getResultList();
  }
  });
    List<ResourceLane> results = (List<ResourceLane>) res;
    if (results.size() == 0) {
      ResourceLane rt = new ResourceLane();
      return rt;
    }
    return (ResourceLane) results.get(0);
  }


}

