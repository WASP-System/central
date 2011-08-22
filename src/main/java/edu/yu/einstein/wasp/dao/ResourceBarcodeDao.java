
/**
 *
 * ResourceBarcodeDao.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcodeDao object
 *
 *
 **/

package edu.yu.einstein.wasp.dao;

import javax.persistence.*;
import java.util.List;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceBarcodeDao extends WaspDao<ResourceBarcode> {

  public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final int resourceBarcodeId);

  public ResourceBarcode getResourceBarcodeByResourceId (final int resourceId);

  public ResourceBarcode getResourceBarcodeByBarcodeId (final int barcodeId);

}

