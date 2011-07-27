
/**
 *
 * SampleBarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the SampleBarcodeService object
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.SampleBarcodeService;
import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.SampleBarcode;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleBarcodeServiceImpl extends WaspServiceImpl<SampleBarcode> implements SampleBarcodeService {

  private SampleBarcodeDao sampleBarcodeDao;
  @Autowired
  public void setSampleBarcodeDao(SampleBarcodeDao sampleBarcodeDao) {
    this.sampleBarcodeDao = sampleBarcodeDao;
    this.setWaspDao(sampleBarcodeDao);
  }
  public SampleBarcodeDao getSampleBarcodeDao() {
    return this.sampleBarcodeDao;
  }

  // **

  
  public SampleBarcode getSampleBarcodeBySampleBarcode (final int sampleBarcode) {
    return this.getSampleBarcodeDao().getSampleBarcodeBySampleBarcode(sampleBarcode);
  }

  public SampleBarcode getSampleBarcodeBySampleId (final int sampleId) {
    return this.getSampleBarcodeDao().getSampleBarcodeBySampleId(sampleId);
  }

  public SampleBarcode getSampleBarcodeByBarcodeId (final int barcodeId) {
    return this.getSampleBarcodeDao().getSampleBarcodeByBarcodeId(barcodeId);
  }
}

