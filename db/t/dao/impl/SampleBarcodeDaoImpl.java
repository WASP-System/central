
/**
 *
 * SampleBarcodeImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcode object
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

import edu.yu.einstein.wasp.model.SampleBarcode;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class SampleBarcodeDaoImpl extends WaspDaoImpl<SampleBarcode> implements edu.yu.einstein.wasp.dao.SampleBarcodeDao {

  public SampleBarcodeDaoImpl() {
    super();
    this.entityClass = SampleBarcode.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleBarcode a WHERE "
       + "a.sampleBarcode = :sampleBarcode";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleBarcode", sampleBarcode);

    return query.getResultList();
  }
  });
    List<SampleBarcode> results = (List<SampleBarcode>) res;
    if (results.size() == 0) {
      SampleBarcode rt = new SampleBarcode();
      return rt;
    }
    return (SampleBarcode) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public SampleBarcode getSampleBarcodeBySampleId (final int sampleId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleBarcode a WHERE "
       + "a.sampleId = :sampleId";
     Query query = em.createQuery(queryString);
      query.setParameter("sampleId", sampleId);

    return query.getResultList();
  }
  });
    List<SampleBarcode> results = (List<SampleBarcode>) res;
    if (results.size() == 0) {
      SampleBarcode rt = new SampleBarcode();
      return rt;
    }
    return (SampleBarcode) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM SampleBarcode a WHERE "
       + "a.barcodeId = :barcodeId";
     Query query = em.createQuery(queryString);
      query.setParameter("barcodeId", barcodeId);

    return query.getResultList();
  }
  });
    List<SampleBarcode> results = (List<SampleBarcode>) res;
    if (results.size() == 0) {
      SampleBarcode rt = new SampleBarcode();
      return rt;
    }
    return (SampleBarcode) results.get(0);
  }


}

