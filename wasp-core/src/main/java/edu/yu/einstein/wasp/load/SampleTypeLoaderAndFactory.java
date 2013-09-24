package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.SampleTypeLoadService;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

/**
 * update/inserts db copy of type SampleType from bean definition
 * 
 * @author asmclellan
 */

public class SampleTypeLoaderAndFactory extends WaspLoader implements FactoryBean<SampleType> {

	@Autowired
	private SampleTypeLoadService sampleTypeLoadService;
	
	private SampleType sampleType;

	private SampleTypeCategory sampleTypeCategory;

	private int isActive = 1;

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public SampleTypeCategory getSampleTypeCategory() {
		return sampleTypeCategory;
	}

	public void setSampleTypeCategory(SampleTypeCategory sampleTypeCategory) {
		this.sampleTypeCategory = sampleTypeCategory;
	}
	
	@PostConstruct
	public void init(){
		sampleType =  sampleTypeLoadService.update(iname, name, sampleTypeCategory, isActive);
	}

	@Override
	public SampleType getObject() throws Exception {
		return sampleType;
	}

	@Override
	public Class<?> getObjectType() {
		return SampleType.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
