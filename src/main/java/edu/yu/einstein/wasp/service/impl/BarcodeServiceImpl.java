
/**
 *
 * BarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the BarcodeService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.BarcodeService;
import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.Barcode;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BarcodeServiceImpl extends WaspServiceImpl<Barcode> implements BarcodeService {

  private BarcodeDao barcodeDao;
  @Autowired
  public void setBarcodeDao(BarcodeDao barcodeDao) {
    this.barcodeDao = barcodeDao;
    this.setWaspDao(barcodeDao);
  }
  public BarcodeDao getBarcodeDao() {
    return this.barcodeDao;
  }

  // **

  
  public Barcode getBarcodeByBarcodeId (final int barcodeId) {
    return this.getBarcodeDao().getBarcodeByBarcodeId(barcodeId);
  }

  public Barcode getBarcodeByBarcode (final String barcode) {
    return this.getBarcodeDao().getBarcodeByBarcode(barcode);
  }
}

