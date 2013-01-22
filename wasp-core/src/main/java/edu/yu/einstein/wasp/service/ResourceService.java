package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCellDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;

public interface ResourceService extends WaspService {

	public ResourceBarcodeDao getResourceBarcodeDao();

	public ResourceDao getResourceDao();
	
	public ResourceCellDao getResourceCellDao();

	public ResourceMetaDao getResourceMetaDao();

	public ResourceCategoryDao getResourceCategoryDao();
	
	public ResourceTypeDao getResourceTypeDao();
	
}
