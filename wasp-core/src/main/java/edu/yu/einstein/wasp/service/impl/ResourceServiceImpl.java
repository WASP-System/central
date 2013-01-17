package edu.yu.einstein.wasp.service.impl;

import edu.yu.einstein.wasp.service.ResourceService;

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

}
