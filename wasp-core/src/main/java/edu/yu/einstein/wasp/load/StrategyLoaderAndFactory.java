package edu.yu.einstein.wasp.load;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.service.StrategyService;

public class StrategyLoaderAndFactory implements FactoryBean<Strategy> {

	@Autowired
	private StrategyService strategyService;
	
	private Strategy strategyObject;

	private String type;
	private String strategy;
	private String displayStrategy;
	private String description;	
	private String available;
	private String sraCompatible; 

	public void setType(String type){ this.type = type; }
	public void setStrategy(String strategy){ this.strategy = strategy; }
	public void setDisplayStrategy(String displayStrategy){ this.displayStrategy = displayStrategy; }
	public void setDescription(String description){ this.description = description; }
	public void setAvailable(String available){ this.available = available; }
	public void setSraCompatible(String sraCompatible){ this.sraCompatible = sraCompatible; }
	
	public String getType(){ return this.type; }
	public String getStrategy(){ return this.strategy; }
	public String getDisplayStrategy(){ return this.displayStrategy; }
	public String getDescription(){ return this.description; }
	public String getAvailable(){ return this.available; }
	public String getSraCompatible(){ return this.sraCompatible; }

	StrategyLoaderAndFactory(){ this.strategyObject = new Strategy(); }
	
	@PostConstruct
	public void init(){
		if(strategyObject==null){ 
			this.strategyObject = new Strategy();
		}
		this.strategyObject.setType(this.type);
		this.strategyObject.setStrategy(this.strategy);
		this.strategyObject.setDisplayStrategy(this.displayStrategy);
		this.strategyObject.setDescription(this.description);
		this.strategyObject.setAvailable(this.available);
		this.strategyObject.setSraCompatible(this.sraCompatible);
		this.strategyObject =  strategyService.saveDuringInitialLoading(this.strategyObject);
	}

	@Override
	public Strategy getObject() throws Exception {
		return strategyObject;
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
