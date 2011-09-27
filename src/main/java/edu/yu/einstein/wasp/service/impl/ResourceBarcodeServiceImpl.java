
/**
 *
 * ResourceBarcodeServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the ResourceBarcodeService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ResourceBarcodeService;
import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.dao.WaspDao;
import edu.yu.einstein.wasp.model.ResourceBarcode;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResourceBarcodeServiceImpl extends WaspServiceImpl<ResourceBarcode> implements ResourceBarcodeService {

	/**
	 * resourceBarcodeDao;
	 *
	 */
	private ResourceBarcodeDao resourceBarcodeDao;

	/**
	 * setResourceBarcodeDao(ResourceBarcodeDao resourceBarcodeDao)
	 *
	 * @param resourceBarcodeDao
	 *
	 */
	@Autowired
	public void setResourceBarcodeDao(ResourceBarcodeDao resourceBarcodeDao) {
		this.resourceBarcodeDao = resourceBarcodeDao;
		this.setWaspDao(resourceBarcodeDao);
	}

	/**
	 * getResourceBarcodeDao();
	 *
	 * @return resourceBarcodeDao
	 *
	 */
	public ResourceBarcodeDao getResourceBarcodeDao() {
		return this.resourceBarcodeDao;
	}


  public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final int resourceBarcodeId) {
    return this.getResourceBarcodeDao().getResourceBarcodeByResourceBarcodeId(resourceBarcodeId);
  }

  public ResourceBarcode getResourceBarcodeByResourceId (final int resourceId) {
    return this.getResourceBarcodeDao().getResourceBarcodeByResourceId(resourceId);
  }

  public ResourceBarcode getResourceBarcodeByBarcodeId (final int barcodeId) {
    return this.getResourceBarcodeDao().getResourceBarcodeByBarcodeId(barcodeId);
  }

}

