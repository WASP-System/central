package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.additionalClasses.Strategy;
import edu.yu.einstein.wasp.load.service.StrategyLoadService;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.SampleTypeCategory;
import edu.yu.einstein.wasp.service.StrategyService;

public class StrategyLoaderAndFactory implements FactoryBean<Strategy> {

	@Autowired
	private StrategyService strategyService;
	
	private Strategy strategy;

	private String sraStrategy;
	private String displayStrategy;
	private String description;	
	private String available;

	public void setSraStrategy(String strategy){ this.sraStrategy = strategy; }
	public void setDisplayStrategy(String displayStrategy){ this.displayStrategy = displayStrategy; }
	public void setDescription(String description){ this.description = description; }
	public void setAvailable(String available){ this.available = available; }
	
	public String getSraStrategy(){ return this.sraStrategy; }
	public String getDisplayStrategy(){ return this.displayStrategy; }
	public String getDescription(){ return this.description; }
	public String getAvailable(){ return this.available; }

	StrategyLoaderAndFactory(){ this.strategy = new Strategy(); }
	
	@PostConstruct
	public void init(){
		if(strategy==null){ this.strategy = new Strategy(); }
		this.strategy.setSraStrategy(this.sraStrategy);
		this.strategy.setDisplayStrategy(this.displayStrategy);
		this.strategy.setDescription(this.description);
		this.strategy.setAvailable(this.available);
		this.strategy =  strategyService.save(strategy);
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
