
/**
 *
 * ResourceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Resource object
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

import edu.yu.einstein.wasp.model.Resource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ResourceDaoImpl extends WaspDaoImpl<Resource> implements edu.yu.einstein.wasp.dao.ResourceDao {

  public ResourceDaoImpl() {
    super();
    this.entityClass = Resource.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Resource getResourceByResourceId (final int resourceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Resource a WHERE "
       + "a.resourceId = :resourceId";
     Query query = em.createQuery(queryString);
      query.setParameter("resourceId", resourceId);

    return query.getResultList();
  }
  });
    List<Resource> results = (List<Resource>) res;
    if (results.size() == 0) {
      Resource rt = new Resource();
      return rt;
    }
    return (Resource) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Resource getResourceByName (final String name) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Resource a WHERE "
       + "a.name = :name";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);

    return query.getResultList();
  }
  });
    List<Resource> results = (List<Resource>) res;
    if (results.size() == 0) {
      Resource rt = new Resource();
      return rt;
    }
    return (Resource) results.get(0);
  }


}

