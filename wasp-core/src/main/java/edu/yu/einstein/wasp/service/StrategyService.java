package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.additionalClasses.Strategy;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.MetaAttributeNotFoundException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.ResourceException;
import edu.yu.einstein.wasp.exception.RunException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleSubtypeException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl.LockStatus;
import edu.yu.einstein.wasp.util.SampleWrapper;


@Service
public interface StrategyService extends WaspMessageHandlingService{
		
	static final String WORKFLOW_KEY = "workflow.strategy";

	public Strategy saveDuringInitialLoading(Strategy strategy);//save to table Meta
	//public List<Strategy> getAllStrategies();//from table Meta
	public List<Strategy> getStrategiesByStrategyType(String strategyType);//from table Meta
		
	//public List<Strategy> getAllStrategiesOrderByStrategy();//from table Meta
	//public List<Strategy> getAllStrategiesOrderByDisplayStrategy();//from table Meta
	
	public void orderStrategiesByDisplayStrategy(List<Strategy> strategies);//from table Meta
	public void orderStrategiesByStrategy(List<Strategy> strategies);//from table Meta

	
	//public Strategy getStrategyObjectByStrategy(String strategy);//from table Meta
	public Strategy getStrategyByKey(String key); //from table Meta
	
	public WorkflowMeta saveStrategiesToWorkflowMeta(Workflow workflow, List<String> strategyKeyList, String strategyType) throws Exception;//save to WorkflowMeta
	public WorkflowMeta saveStrategyToWorkflowMeta(Workflow workflow, Strategy strategy);//save to WorkflowMeta
	//public Strategy getStrategyFromWorkflowMeta(Workflow workflow);//get from WorkflowMeta
	public List<Strategy> getThisWorkflowsStrategies(String strategyType,  Workflow workflow);	//get from WorkflowMeta
}
