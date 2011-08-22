
/**
 *
 * BarcodeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the BarcodeDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface BarcodeDao extends WaspDao<Barcode> {

  public Barcode getBarcodeByBarcodeId (final int barcodeId);

  public Barcode getBarcodeByBarcode (final String barcode);

}

