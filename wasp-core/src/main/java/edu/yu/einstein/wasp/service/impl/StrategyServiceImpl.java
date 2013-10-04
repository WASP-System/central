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
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowResourceType;
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
			
			String key = strategy.getType()+"."+strategy.getStrategy();
			Meta meta = metaDao.getMetaByK(key);
			
			meta.setK(strategy.getType()+"."+strategy.getStrategy());
			
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
	/*
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
	*/
	public List<Strategy> getStrategiesByStrategyType(String strategyType){
		List<Strategy> filteredStrategies = new ArrayList<Strategy>();
		for(Meta meta : metaDao.findAll()){
			if(meta.getK().toLowerCase().startsWith(strategyType.toLowerCase())){
				String encodedStrategy = meta.getV();
				if(encodedStrategy==null){
					continue;
				}
				String [] stringArray = meta.getV().split(Strategy.SEPARATOR);
				if(stringArray.length != 6){
					continue;
				}
				Strategy strategy = new Strategy(meta.getId(), stringArray[0],stringArray[1],stringArray[2],stringArray[3], stringArray[4], stringArray[5]);
				filteredStrategies.add(strategy);
			}
		}
		return filteredStrategies;	
	}

	/*
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
	*/
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
	
	public Strategy getStrategyByKey(String key){
		Meta meta;
		Strategy strategy = new Strategy();
		meta = metaDao.getMetaByK(key);
		
		if(meta.getId()!=null){
			String encodedStrategy = meta.getV();
			if(encodedStrategy!=null){
				String [] stringArray = meta.getV().split(Strategy.SEPARATOR);
				if(stringArray.length == 6){
					strategy = new Strategy(meta.getId(), stringArray[0],stringArray[1],stringArray[2],stringArray[3], stringArray[4], stringArray[5]);
				}
			}
		}
		return strategy;
	}
	
	public WorkflowMeta saveStrategiesToWorkflowMeta(Workflow workflow, List<String> strategyKeyList, String strategyType)throws Exception{//save to WorkflowMeta
	
		String workflowMetaValue = "";
		
		for(String strategyKey : strategyKeyList){
			Strategy strategyObject = this.getStrategyByKey(strategyKey);
			if(strategyObject.getId()==null){
				String message = "unable to find strategy "+strategyKey+" in table meta";
				logger.warn(message);
				throw new Exception(message);
			}
			if(!strategyObject.getType().toLowerCase().equalsIgnoreCase(strategyType)){
				String message = "all strategies must be of same strategy type";
				logger.warn(message);
				throw new Exception(message);
			}
			if(workflowMetaValue.equals("")){
				
				workflowMetaValue = strategyObject.getType()+"."+strategyObject.getStrategy();
			}
			else{
				workflowMetaValue += Strategy.SEPARATOR+strategyObject.getType()+"."+strategyObject.getStrategy();
			}			
		}
		WorkflowMeta workflowMeta = workflowMetaDao.getWorkflowMetaByKWorkflowId(strategyType, workflow.getId());
		workflowMeta.setK(strategyType);
		workflowMeta.setV(workflowMetaValue);
		workflowMeta.setWorkflowId(workflow.getId());
		workflowMeta = workflowMetaDao.save(workflowMeta);
		return workflowMeta;				
	}
	
	public WorkflowMeta saveStrategyToWorkflowMeta(Workflow workflow, Strategy strategy){//save to WorkflowMeta
				
		WorkflowMeta workflowMeta = workflowMetaDao.getWorkflowMetaByKWorkflowId(strategy.getType(), workflow.getId());
		workflowMeta.setK(strategy.getType());
		if(workflowMeta.getV().isEmpty()){
			workflowMeta.setV( strategy.getType()+"."+strategy.getStrategy() );
		}
		else{
			workflowMeta.setV(workflowMeta.getV()+Strategy.SEPARATOR+strategy.getType()+"."+strategy.getStrategy() );
		}
		workflowMeta.setWorkflowId(workflow.getId());
		
		workflowMeta = workflowMetaDao.save(workflowMeta);		
		return workflowMeta;
	}

	/*
	public Strategy getStrategyFromWorkflowMeta(Workflow workflow){//get from WorkflowMeta
		
		WorkflowMeta workflowMeta = workflowMetaDao.getWorkflowMetaByKWorkflowId("abc", workflow.getId());
		if(workflowMeta.getId()==null){
			return new Strategy();
		}
		Strategy strategy = this.getStrategyObjectByStrategy(workflowMeta.getV());
		return strategy;
	}
	*/
	public List<Strategy> getThisWorkflowsStrategies(String strategyType, Workflow workflow){
		List<Strategy> strategies = new ArrayList<Strategy>();
		WorkflowMeta workflowMeta = workflowMetaDao.getWorkflowMetaByKWorkflowId(strategyType, workflow.getId());
		if(workflowMeta.getId()!=null){
			String [] stringArray = workflowMeta.getV().split(Strategy.SEPARATOR);
			for(String s : stringArray){
				Strategy strategy = this.getStrategyByKey(s);
				if(strategy.getId()!=null){
					if(strategyType.equalsIgnoreCase(strategy.getType())){
						strategies.add(strategy);
					}
				}
			}
		}
		return strategies;
	}
}
