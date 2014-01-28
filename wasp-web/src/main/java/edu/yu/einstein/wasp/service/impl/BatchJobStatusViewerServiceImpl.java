package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.controller.util.BatchJobTreeModel;
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
		model.setEndTime(je.getEndTime());
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
		model.setEndTime(se.getEndTime());
		model.setStatus(se.getStatus().toString());
		model.setExitCode(se.getExitStatus().getExitCode());
		model.setExitMessage(se.getExitStatus().getExitDescription());
		model.setIconCls(ExtIcon.TASK);
		model.setExpanded(false);
		model.setLeaf(true);
		return model;
	}

	@Override
	public List<ExtTreeModel> getJobListAll(){
		List<ExtTreeModel> modelList = new ArrayList<>();
		for (JobExecution je : jobExplorer.getJobExecutions())
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
	
	@Override
	public ExtTreeModel getModel(String nodeId){
		if (nodeId.equals(ROOT_NODE_ID)){
			return new BatchJobTreeModel(ROOT_NODE_ID, ExtIcon.TASK_FOLDER, true, false);
		}
		if (nodeId.contains(STEP_EXECUTION_ID_PREFIX)){
			String jobExecutionIdString = nodeId.replace(STEP_EXECUTION_ID_PREFIX + "[0-9]+", "");
			String stepExecutionIdString = nodeId.replace(JOB_EXECUTION_ID_PREFIX + "[0-9]+", "");
			Long jobExecutionId = Long.valueOf(jobExecutionIdString.replace(JOB_EXECUTION_ID_PREFIX, ""));
			Long stepExecutionId = Long.valueOf(stepExecutionIdString.replace(STEP_EXECUTION_ID_PREFIX, ""));
			StepExecution se = jobExplorer.getStepExecution(jobExecutionId, stepExecutionId);
			return getTreeModel(se);
		} 
		if (nodeId.startsWith(JOB_EXECUTION_ID_PREFIX)){
			// create jobModel and populate with steps as children
			Long jobExecutionId = Long.valueOf(nodeId.replace(JOB_EXECUTION_ID_PREFIX, ""));
			JobExecution je = jobExplorer.getJobExecution(jobExecutionId);
			ExtTreeModel jobModel = getTreeModel(je);
			for (StepExecution se : jobExplorer.getJobExecution(jobExecutionId).getStepExecutions()){
				StepExecution seInflated = jobExplorer.getStepExecution(jobExecutionId, se.getId());
				jobModel.addChild(getTreeModel(seInflated));
			}
			return jobModel;
		}
		return null;
	}
	
	@Override
	public List<ExtTreeModel> getSteps(String nodeId){
		List<ExtTreeModel> modelList = new ArrayList<>();
		if (nodeId.startsWith(JOB_EXECUTION_ID_PREFIX)){
			Long jobExecutionId = Long.valueOf(nodeId.replace(JOB_EXECUTION_ID_PREFIX, ""));
			for (StepExecution se : jobExplorer.getJobExecution(jobExecutionId).getStepExecutions()){
				StepExecution seInflated = jobExplorer.getStepExecution(jobExecutionId, se.getId());
				modelList.add(getTreeModel(seInflated));
			}
		}
		return modelList;
	}

}
