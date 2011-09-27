
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface BarcodeDao extends WaspDao<Barcode> {

  public Barcode getBarcodeByBarcodeId (final int barcodeId);

  public Barcode getBarcodeByBarcode (final String barcode);


}

