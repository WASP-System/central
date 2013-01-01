package edu.yu.einstein.wasp.load;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.SampleTypeCategoryLoadService;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

/**
 * update/inserts db copy of type SampleType from bean definition
 */

public class SampleTypeCategoryLoaderAndFactory extends WaspLoader implements FactoryBean<SampleTypeCategory> {

	@Autowired
	private SampleTypeCategoryLoadService sampleTypeCategoryLoadService;

	private int isActive = 1;

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public static SampleTypeCategory getSampleTypeCategoryInstance(String name,	String area, int isActive, SampleTypeCategoryLoadService sampleTypeCategoryLoadService)
			throws Exception {
		return sampleTypeCategoryLoadService.update(area, name, isActive);
	}

	public static SampleTypeCategory getSampleTypeCategoryInstance(String name,	String area, SampleTypeCategoryLoadService sampleTypeCategoryLoadService)
			throws Exception {
		return sampleTypeCategoryLoadService.update(area, name, 1);
	}

	@Override
	public SampleTypeCategory getObject() throws Exception {
		return sampleTypeCategoryLoadService.update(area, name, isActive);
	}

	@Override
	public Class<?> getObjectType() {
		return SampleTypeCategory.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
