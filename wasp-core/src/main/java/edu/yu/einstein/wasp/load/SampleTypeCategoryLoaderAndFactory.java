package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

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
	
	private SampleTypeCategory sampleTypeCategory;

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
	
	@PostConstruct
	public void init(){
		sampleTypeCategory =  sampleTypeCategoryLoadService.update(area, name, isActive);
	}

	@Override
	public SampleTypeCategory getObject() throws Exception {
		return sampleTypeCategory;
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
