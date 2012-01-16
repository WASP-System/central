
/**
 *
 * ResourceBarcodeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcode Dao 
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import edu.yu.einstein.wasp.model.ResourceBarcode;


public interface ResourceBarcodeDao extends WaspDao<ResourceBarcode> {

  public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final int resourceBarcodeId);

  public ResourceBarcode getResourceBarcodeByResourceId (final int resourceId);

  public ResourceBarcode getResourceBarcodeByBarcodeId (final int barcodeId);


}

