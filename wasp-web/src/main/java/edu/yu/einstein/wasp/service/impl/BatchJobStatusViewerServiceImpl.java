package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.batch.core.repository.dao.wasp.BatchJobSortAttribute;
import org.springframework.batch.core.repository.dao.wasp.BatchJobSortAttribute.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.controller.util.BatchJobTreeModel;
import edu.yu.einstein.wasp.controller.util.ExtTreeGridResponse;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel.ExtIcon;
import edu.yu.einstein.wasp.service.BatchJobStatusViewerService;

@Service
@Transactional // batch
public class BatchJobStatusViewerServiceImpl implements BatchJobStatusViewerService{
	
	private JobExplorerWasp jobExplorer;
	
	@Autowired
	public void setJobExplorer(JobExplorer jobExplorer){
		this.jobExplorer = (JobExplorerWasp) jobExplorer;
	}

	public BatchJobStatusViewerServiceImpl() {
		
	}
	
	
	@Override
	public ExtTreeModel getTreeModel(JobExecution je){
		BatchJobTreeModel model = new BatchJobTreeModel();
		model.setId(JOB_EXECUTION_ID_PREFIX + je.getId().toString());
		model.setExecutionId(je.getId());
		model.setName(je.getJobInstance().getJobName());
		model.setStartTime(je.getStartTime());
		if (!je.getExitStatus().getExitCode().equals(ExitStatus.HIBERNATING.getExitCode()))
			model.setEndTime(je.getEndTime()); // only set if not hibernating as a hibernating job is unfinished
		model.setStatus(je.getStatus().toString());
		model.setExitCode(je.getExitStatus().getExitCode());
		model.setExitMessage(je.getExitStatus().getExitDescription());
		model.setIconCls(ExtIcon.TASK_FOLDER);
		model.setExpanded(false);
		model.setLeaf(false);
		return model;
	}

	@Override
	public ExtTreeModel getTreeModel(StepExecution se){
		BatchJobTreeModel model = new BatchJobTreeModel();
		model.setId(JOB_EXECUTION_ID_PREFIX + se.getJobExecutionId().toString() + STEP_EXECUTION_ID_PREFIX + se.getId().toString());
		model.setExecutionId(se.getId());
		model.setName(se.getStepName());
		model.setStartTime(se.getStartTime());
		if (!se.getExitStatus().getExitCode().equals(ExitStatus.HIBERNATING.getExitCode()))
			model.setEndTime(se.getEndTime()); // only set if not hibernating as a hibernating job is unfinished
		model.setStatus(se.getStatus().toString());
		model.setExitCode(se.getExitStatus().getExitCode());
		model.setExitMessage(se.getExitStatus().getExitDescription());
		model.setIconCls(ExtIcon.TASK);
		model.setExpanded(false);
		model.setLeaf(true);
		return model;
	}

	@Override
	public List<ExtTreeModel> getJobList(String property, String direction, Long start, Long limit){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions(getJobSortProperty(property), getSortDirection(direction), start, limit))
			modelList.add(getTreeModel(je));
		return modelList;
	}

	@Override
	public List<ExtTreeModel> getJobList(ExitStatus exitStatus, String property, String direction, Long start, Long limit){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions(exitStatus, getJobSortProperty(property), getSortDirection(direction), start, limit))
			modelList.add(getTreeModel(je));
		return modelList;
	}
	
	private List<ExtTreeModel> getSteps(String nodeId, String property, String direction, Long start, Long limit){
		List<ExtTreeModel> modelList = new ArrayList<>();
		if (nodeId.startsWith(JOB_EXECUTION_ID_PREFIX)){
			Long jobExecutionId = Long.valueOf(nodeId.replace(JOB_EXECUTION_ID_PREFIX, ""));
			for (StepExecution se : jobExplorer.getStepExecutions(jobExecutionId, getStepSortProperty(property), getSortDirection(direction), start, limit)){
				StepExecution seInflated = jobExplorer.getStepExecution(jobExecutionId, se.getId());
				modelList.add(getTreeModel(seInflated));
			}
		}
		return modelList;
	}
	
	@Override
	public ExtTreeGridResponse getPagedModelList(String nodeId, String displayParam, Long start, Long limit){
		return getPagedModelList(nodeId, displayParam, null, null, start, limit);
	}
	
	@Override
	public ExtTreeGridResponse getPagedModelList(String nodeId, String displayParam, String property, String direction, Long start, Long limit){
		if (nodeId.equals(ROOT_NODE_ID)){
			if (limit == null || start == null)
				return new ExtTreeGridResponse(new ArrayList<ExtTreeModel>(), jobExplorer.getJobExecutionCount());
			if (displayParam.equals(SHOW_ALL))
				return new ExtTreeGridResponse(getJobList(property, direction, start, limit), jobExplorer.getJobExecutionCount());
			if (displayParam.equals(SHOW_ACTIVE))
				return new ExtTreeGridResponse(getJobList(ExitStatus.RUNNING, property, direction, start, limit), 
						jobExplorer.getJobExecutionCount(ExitStatus.EXECUTING) + jobExplorer.getJobExecutionCount(ExitStatus.HIBERNATING));
			if (displayParam.equals(SHOW_COMPLETED))
				return new ExtTreeGridResponse(getJobList(ExitStatus.COMPLETED, property, direction, start, limit), jobExplorer.getJobExecutionCount(ExitStatus.COMPLETED));
			if (displayParam.equals(SHOW_FAILED))
				return new ExtTreeGridResponse(getJobList(ExitStatus.FAILED, property, direction, start, limit), jobExplorer.getJobExecutionCount(ExitStatus.FAILED));
			if (displayParam.equals(SHOW_TERMINATED))
				return new ExtTreeGridResponse(getJobList(ExitStatus.TERMINATED, property, direction, start, limit), jobExplorer.getJobExecutionCount(ExitStatus.TERMINATED));
			
		}
		if (nodeId.startsWith(JOB_EXECUTION_ID_PREFIX)){
			Long totalCount = jobExplorer.getJobExecutionCount();
			if (displayParam.equals(SHOW_ACTIVE))
				totalCount = jobExplorer.getJobExecutionCount(ExitStatus.HIBERNATING);
			else if (displayParam.equals(SHOW_COMPLETED))
				totalCount = jobExplorer.getJobExecutionCount(ExitStatus.COMPLETED);
			else if (displayParam.equals(SHOW_FAILED))
				totalCount = jobExplorer.getJobExecutionCount(ExitStatus.FAILED);
			else if (displayParam.equals(SHOW_TERMINATED))
				totalCount = jobExplorer.getJobExecutionCount(ExitStatus.TERMINATED);
			return new ExtTreeGridResponse(getSteps(nodeId, property, direction, start, limit), totalCount);
		}
		return null;
	}
	
	private BatchJobSortAttribute getJobSortProperty(String property){
		if (property == null)
			return null;
		if (property.equals("name"))
			return BatchJobSortAttribute.JOB_NAME;
		if (property.equals("executionId"))
			return BatchJobSortAttribute.JOB_EXECUTION_ID;
		if (property.equals("startTime"))
			return BatchJobSortAttribute.START_TIME;
		if (property.equals("endTime"))
			return BatchJobSortAttribute.END_TIME;
		if (property.equals("status"))
			return BatchJobSortAttribute.STATUS;
		if (property.equals("exitCode"))
			return BatchJobSortAttribute.EXIT_CODE;
		return null;
	}
	
	private BatchJobSortAttribute getStepSortProperty(String property){
		if (property == null)
			return null;
		if (property.equals("name"))
			return BatchJobSortAttribute.STEP_NAME;
		if (property.equals("executionId"))
			return BatchJobSortAttribute.STEP_EXECUTION_ID;
		if (property.equals("startTime"))
			return BatchJobSortAttribute.START_TIME;
		if (property.equals("endTime"))
			return BatchJobSortAttribute.END_TIME;
		if (property.equals("status"))
			return BatchJobSortAttribute.STATUS;
		if (property.equals("exitCode"))
			return BatchJobSortAttribute.EXIT_CODE;
		return null;
	}
	
	private SortDirection getSortDirection(String direction){
		if (direction == null)
			return null;
		if (direction.equals("ASC"))
			return SortDirection.ASC;
		if (direction.equals("DESC"))
			return SortDirection.DESC;
		return null;
	}

}
