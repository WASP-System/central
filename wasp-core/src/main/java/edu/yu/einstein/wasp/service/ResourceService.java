package edu.yu.einstein.wasp.service;

import java.util.Set;

import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCellDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.MetaAttribute.Control.Option;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;

public interface ResourceService extends WaspService {

	public ResourceBarcodeDao getResourceBarcodeDao();

	public ResourceDao getResourceDao();
	
	public ResourceCellDao getResourceCellDao();

	public ResourceMetaDao getResourceMetaDao();

	public ResourceCategoryDao getResourceCategoryDao();
	
	public ResourceTypeDao getResourceTypeDao();

	/**
	 * Get a resource categories associated with the given job. If none resolved, null is returned.
	 * @param job
	 * @return
	 */
	public  Set<ResourceCategory> getAssignedResourceCategory(Job job);

	/**
	 * Get a resource categories associated with the given platform unit. If none resolved, null is returned.
	 * @param platformUnit
	 * @return
	 */
	public  Set<ResourceCategory> getAssignedResourceCategory(Sample platformUnit);
	
	/**
	 * Get select options for resource category
	 * @param resourceCategory
	 * @param metaKey
	 * @return
	 */
	public Set<Option> getAllAvailableResourceCategoryOptions(ResourceCategory resourceCategory, String metaKey);
	
	/**
	 * Get select options for all resource categories associated with sampleSubtype
	 * @param sampleSubtype
	 * @param metaKey
	 * @return
	 */
	public Set<Option> getAllAvailableResourceCategoryOptions(SampleSubtype sampleSubtype, String metaKey);
	
	/**
	 * Get select options for all resource categories associated with sample
	 * @param sample
	 * @param metaKey
	 * @return
	 */
	public Set<Option> getAllAvailableResourceCategoryOptions(Sample sample, String metaKey);
	
}
