
/**
 *
 * SampleBarcodeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcodeDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface SampleBarcodeDao extends WaspDao<SampleBarcode> {

  public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode);

  public SampleBarcode getSampleBarcodeBySampleId (final int sampleId);

  public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId);

}

