package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;

@Service
public interface StrategyService extends WaspMessageHandlingService{
		
	public Strategy saveDuringInitialLoading(Strategy strategy);//save to table Meta
	public List<Strategy> getStrategiesByStrategyType(String strategyType);//from table Meta
		
	public void orderStrategiesByDisplayStrategy(List<Strategy> strategies);//from table Meta
	public void orderStrategiesByStrategy(List<Strategy> strategies);//from table Meta

	public Strategy getStrategyByKey(String key); //from table Meta
	
	public WorkflowMeta saveStrategiesToWorkflowMeta(Workflow workflow, List<String> strategyKeyList, String strategyType) throws Exception;//save to WorkflowMeta
	public WorkflowMeta saveStrategyToWorkflowMeta(Workflow workflow, Strategy strategy);//save to WorkflowMeta
	public List<Strategy> getThisWorkflowsStrategies(String strategyType,  Workflow workflow);	//get from WorkflowMeta
	
	public JobDraftMeta saveStrategy(JobDraft jobDraft, Strategy strategy); //save to JobdraftMeta
	public Strategy getThisJobDraftsStrategy(String strategyType,  JobDraft jobDraft);	//get from JobdraftMeta

	/**
	 * Get all active workflows configured for the chosen strategy
	 * @param requestedStrategy
	 * @return
	 */
	public List<Workflow> getActiveWorkflowsForStrategyOrderByWorkflowName(Strategy requestedStrategy);
	
	/**
	 * Get all active workflows configured for the chosen strategy, or if none, get any workflows configured as defaults
	 * @param requestedStrategy
	 * @return
	 */
	public List<Workflow> getActiveWorkflowsForStrategyOrDefaultsOrderByWorkflowName(Strategy requestedStrategy);
	
	public Strategy getThisJobsStrategy(String strategyType,  Job job);	//get from JobMeta

}
