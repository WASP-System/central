
/**
 *
 * SampleBarcodeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcode Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.SampleBarcode;


public interface SampleBarcodeDao extends WaspDao<SampleBarcode> {

  public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode);

  public SampleBarcode getSampleBarcodeBySampleId (final int sampleId);

  public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId);


}

