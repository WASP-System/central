
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

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import edu.yu.einstein.wasp.model.*;

import org.springframework.stereotype.Repository;


public interface ResourceBarcodeDao extends WaspDao<ResourceBarcode> {

  public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final Integer resourceBarcodeId);

  public ResourceBarcode getResourceBarcodeByResourceId (final Integer resourceId);

  public ResourceBarcode getResourceBarcodeByBarcodeId (final Integer barcodeId);


}

