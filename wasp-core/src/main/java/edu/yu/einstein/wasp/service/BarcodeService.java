
/**
 *
 * BarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the BarcodeService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.model.Barcode;

@Service
public interface BarcodeService extends WaspService<Barcode> {

	/**
	 * setBarcodeDao(BarcodeDao barcodeDao)
	 *
	 * @param barcodeDao
	 *
	 */
	public void setBarcodeDao(BarcodeDao barcodeDao);

	/**
	 * getBarcodeDao();
	 *
	 * @return barcodeDao
	 *
	 */
	public BarcodeDao getBarcodeDao();

  public Barcode getBarcodeByBarcodeId (final int barcodeId);

  public Barcode getBarcodeByBarcode (final String barcode);


}

