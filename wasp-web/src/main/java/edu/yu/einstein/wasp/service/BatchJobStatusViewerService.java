package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import edu.yu.einstein.wasp.controller.util.ExtTreeModel;

public interface BatchJobStatusViewerService {
	
	public static final String ROOT_NODE_ID = "root_node";
	public static final String JOB_EXECUTION_ID_PREFIX = "JE_";
	public static final String STEP_EXECUTION_ID_PREFIX = "SE_";
	
	public ExtTreeModel getTreeModel(JobExecution je);
	
	public ExtTreeModel getTreeModel(StepExecution se);
	
	public List<ExtTreeModel> getJobListAll();
	
	public List<ExtTreeModel> getJobListActive();
	
	public List<ExtTreeModel> getJobListCompleted();
	
	public List<ExtTreeModel> getJobListTerminated();
	
	public List<ExtTreeModel> getJobListFailed();
	
	public List<ExtTreeModel> getSteps(String nodeId);
	
	public ExtTreeModel getModel(String nodeId);

}
