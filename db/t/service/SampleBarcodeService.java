
/**
 *
 * SampleBarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcodeService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.model.SampleBarcode;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface SampleBarcodeService extends WaspService<SampleBarcode> {

  public void setSampleBarcodeDao(SampleBarcodeDao sampleBarcodeDao);
  public SampleBarcodeDao getSampleBarcodeDao();

  public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode);

  public SampleBarcode getSampleBarcodeBySampleId (final int sampleId);

  public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId);

}

