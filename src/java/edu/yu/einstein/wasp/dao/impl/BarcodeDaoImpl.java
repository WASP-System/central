
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
import java.util.HashMap;
import java.util.Map;

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
    HashMap m = new HashMap();
    m.put("barcodeId", barcodeId);
    List<Barcode> results = (List<Barcode>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Barcode rt = new Barcode();
      return rt;
    }
    return (Barcode) results.get(0);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public Barcode getBarcodeByBarcode (final String barcode) {
    HashMap m = new HashMap();
    m.put("barcode", barcode);
    List<Barcode> results = (List<Barcode>) this.findByMap((Map) m);
    if (results.size() == 0) {
      Barcode rt = new Barcode();
      return rt;
    }
    return (Barcode) results.get(0);
  }


}

