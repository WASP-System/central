package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.additionalClasses.Strategy;
import edu.yu.einstein.wasp.load.service.StrategyLoadService;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;

public class StrategyLoaderAndFactory implements FactoryBean<Strategy> {

	@Autowired
	private StrategyLoadService strategyLoadService;
	
	private Strategy strategy;

	private String v;
	private String k;
	
	public void setV(String v){
		this.v = v;
	}
	public void setK(String k){
		this.k = k;
	}
	public String getV(){return v;}
	public String getK(){return k;}
	
	@PostConstruct
	public void init(){
		strategy =  strategyLoadService.update(k, v);
	}

	@Override
	public Strategy getObject() throws Exception {
		return strategy;
	}

	@Override
	public Class<?> getObjectType() {
		return Strategy.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	
}
