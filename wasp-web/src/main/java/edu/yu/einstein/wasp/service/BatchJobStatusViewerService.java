package edu.yu.einstein.wasp.service;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

import edu.yu.einstein.wasp.controller.util.ExtGridResponse;
import edu.yu.einstein.wasp.controller.util.ExtStepInfoModel;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel;

public interface BatchJobStatusViewerService {
	
	public static final String ROOT_NODE_ID = "node-root";
	public static final String JOB_EXECUTION_ID_PREFIX = "JE";
	public static final String STEP_EXECUTION_ID_PREFIX = "SE";
	
	public static final String SHOW_ALL = "All";
	public static final String SHOW_ACTIVE = "Active";
	public static final String SHOW_COMPLETED = "Completed";
	public static final String SHOW_TERMINATED = "Terminated";
	public static final String SHOW_FAILED = "Failed";
	
	public ExtTreeModel getTreeModel(JobExecution je);
	
	public ExtTreeModel getTreeModel(StepExecution se);
	
	public List<ExtTreeModel> getJobList(String property, String direction, Long start, Long limit);
	
	public List<ExtTreeModel> getJobList(ExitStatus exitStatus, String property, String direction, Long start, Long limit);
	
	public ExtGridResponse<ExtTreeModel> getPagedModelList(String nodeId, String displayParam, Long start, Long limit);
	
	public ExtGridResponse<ExtTreeModel> getPagedModelList(String nodeId, String displayParam, String property, String direction, Long start, Long limit);

	public ExtStepInfoModel getExtStepInfoModel(Long jobExecutionId, String stepName);

}
