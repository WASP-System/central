
/**
 *
 * ResourceBarcodeService.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcodeService object
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.model.ResourceBarcode;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ResourceBarcodeService extends WaspService<ResourceBarcode> {

  public void setResourceBarcodeDao(ResourceBarcodeDao resourceBarcodeDao);
  public ResourceBarcodeDao getResourceBarcodeDao();

  public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final int resourceBarcodeId);

  public ResourceBarcode getResourceBarcodeByResourceId (final int resourceId);

  public ResourceBarcode getResourceBarcodeByBarcodeId (final int barcodeId);

}

