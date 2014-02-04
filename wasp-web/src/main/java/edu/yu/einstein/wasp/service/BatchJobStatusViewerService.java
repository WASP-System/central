package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import edu.yu.einstein.wasp.controller.util.ExtTreeGridResponse;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel;

public interface BatchJobStatusViewerService {
	
	public static final String ROOT_NODE_ID = "node-root";
	public static final String JOB_EXECUTION_ID_PREFIX = "JE";
	public static final String STEP_EXECUTION_ID_PREFIX = "SE";
	
	public ExtTreeModel getTreeModel(JobExecution je);
	
	public ExtTreeModel getTreeModel(StepExecution se);
	
	public List<ExtTreeModel> getJobListAll(String property, String direction, Long start, Long limit);
	
	public List<ExtTreeModel> getJobListActive();
	
	public List<ExtTreeModel> getJobListCompleted();
	
	public List<ExtTreeModel> getJobListTerminated();
	
	public List<ExtTreeModel> getJobListFailed();
	
	public ExtTreeGridResponse getPagedModelList(String nodeId, Long start, Long limit);
	
	public ExtTreeGridResponse getPagedModelList(String nodeId, String property, String direction, Long start, Long limit);

}
