package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.SampleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCellDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;

@Service
@Transactional
public class ResourceServiceImpl extends WaspServiceImpl implements ResourceService{

	public ResourceServiceImpl() {}
	
	@Autowired
	private ResourceBarcodeDao resourceBarcodeDao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private ResourceCellDao resourceCellDao;
	
	@Autowired
	private ResourceMetaDao resourceMetaDao;
	
	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	
	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Autowired
	private SampleService sampleService;

	@Override
	public ResourceBarcodeDao getResourceBarcodeDao() {
		return resourceBarcodeDao;
	}

	@Override
	public ResourceDao getResourceDao() {
		return resourceDao;
	}

	@Override
	public ResourceCellDao getResourceCellDao() {
		return resourceCellDao;
	}

	@Override
	public ResourceMetaDao getResourceMetaDao() {
		return resourceMetaDao;
	}

	@Override
	public ResourceCategoryDao getResourceCategoryDao() {
		return resourceCategoryDao;
	}

	@Override
	public ResourceTypeDao getResourceTypeDao() {
		return resourceTypeDao;
	} 
	
	@Override
	public ResourceCategory getAssignedResourceCategory(Job job){
		Assert.assertParameterNotNull(job, "a job must be provided");
		Assert.assertParameterNotNull(job.getId(), "a valid job must be provided");
		List<JobResourcecategory> jrc = job.getJobResourcecategory();
		if (jrc == null || jrc.size() == 0 || jrc.size() > 1){
			logger.warn("Unable to resolve a unique resource category for job with id=" + job.getId());
			return null;
		}
		return jrc.get(0).getResourceCategory();
	}
	
	@Override
	public ResourceCategory getAssignedResourceCategory(Sample platformUnit){
		Assert.assertParameterNotNull(platformUnit, "a sample must be provided");
		Assert.assertParameterNotNull(platformUnit.getId(), "a valid sample must be provided");
		Assert.assertTrue(sampleService.isPlatformUnit(platformUnit), "sample must be of type platformUnit");
		List<SampleSubtypeResourceCategory> src = platformUnit.getSampleSubtype().getSampleSubtypeResourceCategory();
		if (src == null || src.size() == 0 || src.size() > 1){
			logger.warn("Unable to resolve a unique resource category for platformUnit with id=" + platformUnit.getId());
			return null;
		}
		return src.get(0).getResourceCategory();
	}

}
