package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.load.service.ControlLibraryLoadService;
import edu.yu.einstein.wasp.load.service.SampleTypeLoadService;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

/**
 * update/inserts db copy of type SampleType from bean definition
 * 
 * @author rdubin
 */

public class ControlLibraryLoaderAndFactory extends WaspLoader implements FactoryBean<Sample> {

	@Autowired
	private ControlLibraryLoadService controlLibraryLoadService;
	
	private Sample controlSample;
	
	private SampleType sampleType;
	
	private SampleSubtype sampleSubtype;
	
	private int isActive = 1;
		
	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	
	public SampleType getSampleType(){
		return this.sampleType;
	}
	public void setSampleType(SampleType sampleType){
		this.sampleType = sampleType;
	}
	
	public void setSampleSubtype(SampleSubtype sampleSubtype){
		this.sampleSubtype = sampleSubtype;
	}
	public SampleSubtype getSampleSubtype(){
		return this.sampleSubtype;
	}
	
	@PostConstruct
	public void init(){
		controlSample =  controlLibraryLoadService.update(this.name, this.sampleType, this.sampleSubtype, this.isActive);
	}

	@Override
	public Sample getObject() throws Exception {
		return controlSample;
	}

	@Override
	public Class<?> getObjectType() {
		return Sample.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}


