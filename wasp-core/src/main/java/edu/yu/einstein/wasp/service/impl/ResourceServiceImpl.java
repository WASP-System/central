package edu.yu.einstein.wasp.service.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.ResourceBarcodeDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceCellDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceMetaDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.MetaAttribute.Control.Option;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.SampleService;

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
	public Set<ResourceCategory> getAssignedResourceCategory(Job job){
		Assert.assertParameterNotNull(job, "a job must be provided");
		Assert.assertParameterNotNull(job.getId(), "a valid job must be provided");
		List<JobResourcecategory> jrcs = job.getJobResourcecategory();
		Set<ResourceCategory> rcs = new HashSet<>();
		if (jrcs == null || jrcs.size() == 0){
			logger.warn("Unable to resolve a unique resource category for job with id=" + job.getId());
			return rcs;
		}
		for (JobResourcecategory jrc: jrcs)
			rcs.add(jrc.getResourceCategory());
		return rcs;
	}
	
	@Override
	public Set<ResourceCategory> getAssignedResourceCategory(Sample platformUnit){
		Assert.assertParameterNotNull(platformUnit, "a sample must be provided");
		Assert.assertParameterNotNull(platformUnit.getId(), "a valid sample must be provided");
		Assert.assertTrue(sampleService.isPlatformUnit(platformUnit), "sample must be of type platformUnit");
		List<SampleSubtypeResourceCategory> srcs = platformUnit.getSampleSubtype().getSampleSubtypeResourceCategory();
		Set<ResourceCategory> rcs = new HashSet<>();
		if (srcs == null || srcs.size() == 0){
			logger.warn("Unable to resolve a unique resource category for platformUnit with id=" + platformUnit.getId());
			return rcs;
		}
		for (SampleSubtypeResourceCategory src: srcs)
			rcs.add(src.getResourceCategory());
		return rcs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Option> getAllAvailableResourceCategoryOptions(ResourceCategory resourceCategory, String metaKey) {
		Set<Option> set = new LinkedHashSet<Option>();
		for(ResourceCategoryMeta rcm : resourceCategory.getResourceCategoryMeta()){
			if( rcm.getK().indexOf(metaKey) > -1 ){//such as readLength
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
				for(String token : tokens){//token could be single:single
					String[] colonTokens = token.split(":");
					set.add(new Option(colonTokens[0], colonTokens[1]));							
				}
			}		
		}	
		return set;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Option> getAllAvailableResourceCategoryOptions(SampleSubtype sampleSubtype, String metaKey) {
		Set<Option> set = new LinkedHashSet<Option>();
		for(SampleSubtypeResourceCategory ssrc : sampleSubtype.getSampleSubtypeResourceCategory()){
			set.addAll(getAllAvailableResourceCategoryOptions(ssrc.getResourceCategory(), metaKey));
		}	
		return set;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Option> getAllAvailableResourceCategoryOptions(Sample sample, String metaKey) {
		Set<Option> set = new LinkedHashSet<Option>();
		for(SampleSubtypeResourceCategory ssrc : sample.getSampleSubtype().getSampleSubtypeResourceCategory()){			
			set.addAll(getAllAvailableResourceCategoryOptions(ssrc.getResourceCategory(), metaKey));
		}	
		return set;
	}

}
