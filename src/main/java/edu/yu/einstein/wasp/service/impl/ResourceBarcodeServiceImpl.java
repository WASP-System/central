
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.model.ResourceBarcode;
import edu.yu.einstein.wasp.service.ResourceBarcodeService;

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
	@Override
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
	@Override
	public ResourceBarcodeDao getResourceBarcodeDao() {
		return this.resourceBarcodeDao;
	}


  @Override
public ResourceBarcode getResourceBarcodeByResourceBarcodeId (final int resourceBarcodeId) {
    return this.getResourceBarcodeDao().getResourceBarcodeByResourceBarcodeId(resourceBarcodeId);
  }

  @Override
public ResourceBarcode getResourceBarcodeByResourceId (final int resourceId) {
    return this.getResourceBarcodeDao().getResourceBarcodeByResourceId(resourceId);
  }

  @Override
public ResourceBarcode getResourceBarcodeByBarcodeId (final int barcodeId) {
    return this.getResourceBarcodeDao().getResourceBarcodeByBarcodeId(barcodeId);
  }

}

