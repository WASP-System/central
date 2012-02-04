
/**
 *
 * BarcodeServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the BarcodeService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.service.BarcodeService;

@Service
public class BarcodeServiceImpl extends WaspServiceImpl<Barcode> implements BarcodeService {

	/**
	 * barcodeDao;
	 *
	 */
	private BarcodeDao barcodeDao;

	/**
	 * setBarcodeDao(BarcodeDao barcodeDao)
	 *
	 * @param barcodeDao
	 *
	 */
	@Override
	@Autowired
	public void setBarcodeDao(BarcodeDao barcodeDao) {
		this.barcodeDao = barcodeDao;
		this.setWaspDao(barcodeDao);
	}

	/**
	 * getBarcodeDao();
	 *
	 * @return barcodeDao
	 *
	 */
	@Override
	public BarcodeDao getBarcodeDao() {
		return this.barcodeDao;
	}


  @Override
public Barcode getBarcodeByBarcodeId (final int barcodeId) {
    return this.getBarcodeDao().getBarcodeByBarcodeId(barcodeId);
  }

  @Override
public Barcode getBarcodeByBarcode (final String barcode) {
    return this.getBarcodeDao().getBarcodeByBarcode(barcode);
  }

}

