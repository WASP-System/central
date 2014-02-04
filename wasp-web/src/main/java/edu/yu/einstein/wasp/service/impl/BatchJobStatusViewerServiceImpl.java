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
	public List<ExtTreeModel> getJobListAll(String property, String direction, Long start, Long limit){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions(getJobSortProperty(property), getSortDirection(direction), start, limit))
			modelList.add(getTreeModel(je));
		return modelList;
	}

	@Override
	public List<ExtTreeModel> getJobListActive(){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions(ExitStatus.RUNNING))
			modelList.add(getTreeModel(je));
		return modelList;
	}

	@Override
	public List<ExtTreeModel> getJobListCompleted(){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions(ExitStatus.COMPLETED))
			modelList.add(getTreeModel(je));
		return modelList;
	}

	@Override
	public List<ExtTreeModel> getJobListTerminated(){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions(ExitStatus.TERMINATED))
			modelList.add(getTreeModel(je));
		return modelList;
	}

	@Override
	public List<ExtTreeModel> getJobListFailed(){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions(ExitStatus.FAILED))
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
	public ExtTreeGridResponse getPagedModelList(String nodeId, Long start, Long limit){
		return getPagedModelList(nodeId, null, null, start, limit);
	}
	
	@Override
	public ExtTreeGridResponse getPagedModelList(String nodeId, String property, String direction, Long start, Long limit){
		if (nodeId.equals(ROOT_NODE_ID)){
			Long totalCount = jobExplorer.getJobExecutionCount();
			if (limit == null || start == null)
				return new ExtTreeGridResponse(new ArrayList<ExtTreeModel>(), totalCount);
			return new ExtTreeGridResponse(getJobListAll(property, direction, start, limit), totalCount);
		}
		if (nodeId.contains(STEP_EXECUTION_ID_PREFIX)){
		/*	String jobExecutionIdString = nodeId.replace(STEP_EXECUTION_ID_PREFIX + "[0-9]+", "");
			String stepExecutionIdString = nodeId.replace(JOB_EXECUTION_ID_PREFIX + "[0-9]+", "");
			Long jobExecutionId = Long.valueOf(jobExecutionIdString.replace(JOB_EXECUTION_ID_PREFIX, ""));
			Long stepExecutionId = Long.valueOf(stepExecutionIdString.replace(STEP_EXECUTION_ID_PREFIX, ""));
			StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
			return getTreeModel(se);*/
			return new ExtTreeGridResponse(null, null);
		} 
		if (nodeId.startsWith(JOB_EXECUTION_ID_PREFIX)){
			Long totalCount = jobExplorer.getJobExecutionCount();
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
