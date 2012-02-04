
/**
 *
 * BarcodeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the Barcode Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.Barcode;


public interface BarcodeDao extends WaspDao<Barcode> {

  public Barcode getBarcodeByBarcodeId (final int barcodeId);

  public Barcode getBarcodeByBarcode (final String barcode);


}

