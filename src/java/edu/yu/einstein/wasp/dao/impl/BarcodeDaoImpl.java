
/**
 *
 * BarcodeImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the Barcode object
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

import edu.yu.einstein.wasp.model.Barcode;

@SuppressWarnings("unchecked")
@Transactional
@Repository
public class BarcodeDaoImpl extends WaspDaoImpl<Barcode> implements edu.yu.einstein.wasp.dao.BarcodeDao {

  public BarcodeDaoImpl() {
    super();
    this.entityClass = Barcode.class;
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public Barcode getBarcodeByBarcodeId (final int barcodeId) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Barcode a WHERE "
       + "a.barcodeId = :barcodeId";
     Query query = em.createQuery(queryString);
      query.setParameter("barcodeId", barcodeId);

    return query.getResultList();
  }
  });
    List<Barcode> results = (List<Barcode>) res;
    if (results.size() == 0) {
      Barcode rt = new Barcode();
      return rt;
    }
    return (Barcode) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Barcode getBarcodeByBarcode (final String barcode) {
   Object res = getJpaTemplate().execute(new JpaCallback() {
   public Object doInJpa(EntityManager em) throws PersistenceException {
     String queryString = "SELECT a FROM Barcode a WHERE "
       + "a.barcode = :barcode";
     Query query = em.createQuery(queryString);
      query.setParameter("barcode", barcode);

    return query.getResultList();
  }
  });
    List<Barcode> results = (List<Barcode>) res;
    if (results.size() == 0) {
      Barcode rt = new Barcode();
      return rt;
    }
    return (Barcode) results.get(0);
  }


}

