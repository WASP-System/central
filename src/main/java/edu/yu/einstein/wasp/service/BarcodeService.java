
/**
 *
 * BarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the BarcodeService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.model.Barcode;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface BarcodeService extends WaspService<Barcode> {

  public void setBarcodeDao(BarcodeDao barcodeDao);
  public BarcodeDao getBarcodeDao();

  public Barcode getBarcodeByBarcodeId (final int barcodeId);

  public Barcode getBarcodeByBarcode (final String barcode);

}

