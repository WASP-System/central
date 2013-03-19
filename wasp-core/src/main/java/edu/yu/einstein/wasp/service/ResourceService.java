package edu.yu.einstein.wasp.service;

import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCellDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;

public interface ResourceService extends WaspService {

	public ResourceBarcodeDao getResourceBarcodeDao();

	public ResourceDao getResourceDao();
	
	public ResourceCellDao getResourceCellDao();

	public ResourceMetaDao getResourceMetaDao();

	public ResourceCategoryDao getResourceCategoryDao();
	
	public ResourceTypeDao getResourceTypeDao();

	/**
	 * Get a unique resource category associated with the given job. If none resolved, null is returned.
	 * @param job
	 * @return
	 */
	public ResourceCategory getAssignedResourceCategory(Job job);

	/**
	 * Get a unique resource category associated with the given platform unit. If none resolved, null is returned.
	 * @param platformUnit
	 * @return
	 */
	public ResourceCategory getAssignedResourceCategory(Sample platformUnit);
	
}
