/**
 *
 * JobServiceImpl.java 
 * @author dubin
 *  
 * the JobService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.additionalClasses.Strategy;
import edu.yu.einstein.wasp.dao.MetaDao;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.model.Meta;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.service.StrategyService;

@Service
@Transactional("entityManager")
public class StrategyServiceImpl extends WaspMessageHandlingServiceImpl implements StrategyService {

	@Autowired
	private MetaDao metaDao;
	@Autowired
	private WorkflowMetaDao workflowMetaDao;
	
	public Strategy saveDuringInitialLoading(Strategy strategy){//save to table meta
			Assert.assertParameterNotNull(strategy.getType(), "type Cannot be null");
			Assert.assertParameterNotNull(strategy.getStrategy(), "strategy Cannot be null");
			Assert.assertParameterNotNull(strategy.getDisplayStrategy(), "display Cannot be null");
			Assert.assertParameterNotNull(strategy.getDescription(), "description Cannot be null");
			Assert.assertParameterNotNull(strategy.getAvailable(), "available Cannot be null");
			Assert.assertParameterNotNull(strategy.getSraCompatible(), "sraCompatible Cannot be null");
			
			Meta meta = metaDao.getMetaByK(Strategy.KEY_PREFIX+strategy.getStrategy());
			
			meta.setK(Strategy.KEY_PREFIX+strategy.getStrategy());
			
			meta.setV( strategy.getType()+Strategy.SEPARATOR+
					   strategy.getStrategy()+Strategy.SEPARATOR+
					   strategy.getDisplayStrategy()+Strategy.SEPARATOR+
					   strategy.getDescription()+Strategy.SEPARATOR+
					   strategy.getAvailable()+Strategy.SEPARATOR+
					   strategy.getSraCompatible());
			
			meta = metaDao.save(meta);
			if(meta.getId()!=null){
				strategy.setId(meta.getId());
			}
			return strategy;
	}
	
	public List<Strategy> getAllStrategies(){
		List<Strategy> strategies = new ArrayList<Strategy>();
		for(Meta meta : metaDao.findAll()){
			if(meta.getK().startsWith(Strategy.KEY_PREFIX)){
				String encodedStrategy = meta.getV();
				if(encodedStrategy==null){
					continue;
				}
				String [] stringArray = meta.getV().split(Strategy.SEPARATOR);
				if(stringArray.length != 6){
					continue;
				}
				Strategy strategy = new Strategy(meta.getId(), stringArray[0],stringArray[1],stringArray[2],stringArray[3], stringArray[4], stringArray[5]);
				strategies.add(strategy);
			}
		}
		return strategies;		
	}
	
	public List<Strategy> getAllStrategiesByStrategyType(String strategyType){
		List<Strategy> filteredStrategies = new ArrayList<Strategy>();
		for(Strategy strategyObject : getAllStrategies()){
			if(strategyObject.getType().toLowerCase().contains(strategyType.toLowerCase())){
				filteredStrategies.add(strategyObject);
			}
		}
		return filteredStrategies;		
	}

	
	public List<Strategy> getAllStrategiesOrderByStrategy(){
		List<Strategy> strategies = this.getAllStrategies();
		Collections.sort(strategies, new StrategyComparatorOrderByStrategy());
		return strategies;
	}
	
	public List<Strategy> getAllStrategiesOrderByDisplayStrategy(){
		List<Strategy> strategies = this.getAllStrategies();
		Collections.sort(strategies, new StrategyComparatorOrderByDisplayStrategy());
		return strategies;
	}
	
	public void orderStrategiesByDisplayStrategy(List<Strategy> strategies){
		Collections.sort(strategies, new StrategyComparatorOrderByDisplayStrategy());
	}
	
	public void orderStrategiesByStrategy(List<Strategy> strategies){
		Collections.sort(strategies, new StrategyComparatorOrderByStrategy());
	}
	
	class StrategyComparatorOrderByStrategy implements Comparator<Strategy> {
	    @Override
	    public int compare(Strategy arg0, Strategy arg1) {
	        return arg0.getStrategy().compareToIgnoreCase(arg1.getStrategy());
	    }
	}
	class StrategyComparatorOrderByDisplayStrategy implements Comparator<Strategy> {
	    @Override
	    public int compare(Strategy arg0, Strategy arg1) {
	        return arg0.getDisplayStrategy().compareToIgnoreCase(arg1.getDisplayStrategy());
	    }
	}
	
	public Strategy getStrategyObjectByStrategy(String strategy){
		Meta meta;
		Strategy strategyObject = new Strategy();
		if(strategy.startsWith(Strategy.KEY_PREFIX)){
			meta = metaDao.getMetaByK(strategy);
		}
		else{
			meta = metaDao.getMetaByK(Strategy.KEY_PREFIX+strategy);
		}
		if(meta.getId()!=null){
			String encodedStrategy = meta.getV();
			if(encodedStrategy!=null){
				String [] stringArray = meta.getV().split(Strategy.SEPARATOR);
				if(stringArray.length == 6){
					strategyObject = new Strategy(meta.getId(), stringArray[0],stringArray[1],stringArray[2],stringArray[3], stringArray[4], stringArray[5]);
				}
			}
		}
		return strategyObject;
	}
	

	public WorkflowMeta saveStrategyToWorkflowMeta(Workflow workflow, Strategy strategy){//save to WorkflowMeta
				
		WorkflowMeta workflowMeta = workflowMetaDao.getWorkflowMetaByKWorkflowId(Strategy.KEY_PREFIX, workflow.getId());
		workflowMeta.setK(Strategy.KEY_PREFIX);
		workflowMeta.setV( Strategy.KEY_PREFIX+strategy.getStrategy() );
		workflowMeta.setWorkflowId(workflow.getId());
		
		workflowMeta = workflowMetaDao.save(workflowMeta);		
		return workflowMeta;
	}

	public Strategy getStrategyFromWorkflowMeta(Workflow workflow){//get from WorkflowMeta
		
		WorkflowMeta workflowMeta = workflowMetaDao.getWorkflowMetaByKWorkflowId(Strategy.KEY_PREFIX, workflow.getId());
		if(workflowMeta.getId()==null){
			return new Strategy();
		}
		Strategy strategy = this.getStrategyObjectByStrategy(workflowMeta.getV());
		return strategy;
	}
}
