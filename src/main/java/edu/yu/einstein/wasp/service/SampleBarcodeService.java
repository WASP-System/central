
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

import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.model.SampleBarcode;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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

