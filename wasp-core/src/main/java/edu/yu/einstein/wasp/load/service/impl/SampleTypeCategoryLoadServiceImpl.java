package edu.yu.einstein.wasp.load.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.load.service.SampleTypeCategoryLoadService;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

@Service
@Transactional("entityManager")
public class SampleTypeCategoryLoadServiceImpl extends WaspLoadServiceImpl implements SampleTypeCategoryLoadService {

	@Autowired
	private SampleTypeCategoryDao sampleTypeCategoryDao;

	@Override
	public SampleTypeCategory update(String iname, String name, int isActive) {
		Assert.assertParameterNotNull(iname, "iname Cannot be null");
		Assert.assertParameterNotNull(name, "name Cannot be null");
		SampleTypeCategory sampleTypeCategory = sampleTypeCategoryDao.getSampleTypeCategoryByIName(iname);
		// inserts or update sampleSubtype
		if (sampleTypeCategory.getSampleTypeCategoryId() == null) {
			sampleTypeCategory.setIName(iname);
			sampleTypeCategory.setName(name);
			sampleTypeCategory.setIsActive(isActive);
			sampleTypeCategory = sampleTypeCategoryDao.save(sampleTypeCategory);
		} else {
			if (!sampleTypeCategory.getName().equals(name)) {
				sampleTypeCategory.setName(name);
			}
			if (sampleTypeCategory.getIsActive().intValue() != isActive) {
				sampleTypeCategory.setIsActive(isActive);
			}
		}
		return sampleTypeCategory;
	}

}
