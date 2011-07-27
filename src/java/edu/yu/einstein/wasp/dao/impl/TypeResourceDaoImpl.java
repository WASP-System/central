
/**
 *
 * TypeResourceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the TypeResource object
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

import edu.yu.einstein.wasp.model.TypeResource;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class TypeResourceDaoImpl extends WaspDaoImpl<TypeResource> implements edu.yu.einstein.wasp.dao.TypeResourceDao {

  public TypeResourceDaoImpl() {
    super();
    this.entityClass = TypeResource.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public TypeResource getTypeResourceByTypeResourceId (final int typeResourceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM TypeResource a WHERE "
       + "a.typeResourceId = :typeResourceId";
     Query query = em.createQuery(queryString);
      query.setParameter("typeResourceId", typeResourceId);

    return query.getResultList();
  }
  });
    List<TypeResource> results = (List<TypeResource>) res;
    if (results.size() == 0) {
      TypeResource rt = new TypeResource();
      return rt;
    }
    return (TypeResource) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public TypeResource getTypeResourceByIName (final String iName) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM TypeResource a WHERE "
       + "a.iName = :iName";
     Query query = em.createQuery(queryString);
      query.setParameter("iName", iName);

    return query.getResultList();
  }
  });
    List<TypeResource> results = (List<TypeResource>) res;
    if (results.size() == 0) {
      TypeResource rt = new TypeResource();
      return rt;
    }
    return (TypeResource) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public TypeResource getTypeResourceByName (final String name) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM TypeResource a WHERE "
       + "a.name = :name";
     Query query = em.createQuery(queryString);
      query.setParameter("name", name);

    return query.getResultList();
  }
  });
    List<TypeResource> results = (List<TypeResource>) res;
    if (results.size() == 0) {
      TypeResource rt = new TypeResource();
      return rt;
    }
    return (TypeResource) results.get(0);
  }


}

