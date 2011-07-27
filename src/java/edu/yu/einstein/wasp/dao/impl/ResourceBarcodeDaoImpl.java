
/**
 *
 * ResourceBarcodeImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcode object
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

import edu.yu.einstein.wasp.model.ResourceBarcode;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class ResourceBarcodeDaoImpl extends WaspDaoImpl<ResourceBarcode> implements edu.yu.einstein.wasp.dao.ResourceBarcodeDao {

  public ResourceBarcodeDaoImpl() {
    super();
    this.entityClass = ResourceBarcode.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final int resourceBarcodeId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM ResourceBarcode a WHERE "
       + "a.resourceBarcodeId = :resourceBarcodeId";
     Query query = em.createQuery(queryString);
      query.setParameter("resourceBarcodeId", resourceBarcodeId);

    return query.getResultList();
  }
  });
    List<ResourceBarcode> results = (List<ResourceBarcode>) res;
    if (results.size() == 0) {
      ResourceBarcode rt = new ResourceBarcode();
      return rt;
    }
    return (ResourceBarcode) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public ResourceBarcode getResourceBarcodeByResourceId (final int resourceId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM ResourceBarcode a WHERE "
       + "a.resourceId = :resourceId";
     Query query = em.createQuery(queryString);
      query.setParameter("resourceId", resourceId);

    return query.getResultList();
  }
  });
    List<ResourceBarcode> results = (List<ResourceBarcode>) res;
    if (results.size() == 0) {
      ResourceBarcode rt = new ResourceBarcode();
      return rt;
    }
    return (ResourceBarcode) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public ResourceBarcode getResourceBarcodeByBarcodeId (final int barcodeId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM ResourceBarcode a WHERE "
       + "a.barcodeId = :barcodeId";
     Query query = em.createQuery(queryString);
      query.setParameter("barcodeId", barcodeId);

    return query.getResultList();
  }
  });
    List<ResourceBarcode> results = (List<ResourceBarcode>) res;
    if (results.size() == 0) {
      ResourceBarcode rt = new ResourceBarcode();
      return rt;
    }
    return (ResourceBarcode) results.get(0);
  }


}

