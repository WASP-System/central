
/**
 *
 * SampleBarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcodeService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.model.SampleBarcode;

@Service
public interface SampleBarcodeService extends WaspService<SampleBarcode> {

	/**
	 * setSampleBarcodeDao(SampleBarcodeDao sampleBarcodeDao)
	 *
	 * @param sampleBarcodeDao
	 *
	 */
	public void setSampleBarcodeDao(SampleBarcodeDao sampleBarcodeDao);

	/**
	 * getSampleBarcodeDao();
	 *
	 * @return sampleBarcodeDao
	 *
	 */
	public SampleBarcodeDao getSampleBarcodeDao();

  public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode);

  public SampleBarcode getSampleBarcodeBySampleId (final int sampleId);

  public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId);


}

