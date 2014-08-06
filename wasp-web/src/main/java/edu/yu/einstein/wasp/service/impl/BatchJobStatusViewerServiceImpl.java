package edu.yu.einstein.wasp.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.wasp.BatchJobSortAttribute;
import org.springframework.batch.core.repository.dao.wasp.BatchJobSortAttribute.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.controller.util.BatchJobTreeModel;
import edu.yu.einstein.wasp.controller.util.ExtGridResponse;
import edu.yu.einstein.wasp.controller.util.ExtStepInfoModel;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel;
import edu.yu.einstein.wasp.controller.util.ExtTreeModel.ExtIcon;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.SgeWorkService;
import edu.yu.einstein.wasp.service.BatchJobStatusViewerService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;

@Service
@Transactional // batch
public class BatchJobStatusViewerServiceImpl extends WaspServiceImpl implements BatchJobStatusViewerService{
	
	private static Logger logger = Logger.getLogger(BatchJobStatusViewerServiceImpl.class);
	
	private JobExplorerWasp jobExplorer;
	
	@Autowired
	private GridWorkService gws;
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	protected MessageServiceWebapp messageService;
	
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
		if (!je.getExitStatus().isRunning() && !je.getExitStatus().getExitCode().equals(ExitStatus.UNKNOWN.getExitCode()))
			model.setEndTime(je.getEndTime()); // only set if not running or unknown
		model.setStatus(je.getStatus().toString());
		model.setExitCode(je.getExitStatus().getExitCode());
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
		if (!se.getExitStatus().isRunning() && !se.getExitStatus().getExitCode().equals(ExitStatus.UNKNOWN.getExitCode()))
			model.setEndTime(se.getEndTime()); // only set if not running or unknown
		model.setStatus(se.getStatus().toString());
		model.setExitCode(se.getExitStatus().getExitCode());
		model.setIconCls(ExtIcon.TASK);
		model.setExpanded(false);
		model.setLeaf(true);
		model.setResultAvailable(se.getExecutionContext().containsKey(GridResult.GRID_RESULT_KEY));
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
	public ExtGridResponse<ExtTreeModel> getPagedModelList(String nodeId, String displayParam, Long start, Long limit){
		return getPagedModelList(nodeId, displayParam, null, null, start, limit);
	}
	
	@Override
	public ExtGridResponse<ExtTreeModel> getPagedModelList(String nodeId, String displayParam, String property, String direction, Long start, Long limit){
		if (nodeId.equals(ROOT_NODE_ID)){
			if (limit == null || start == null)
				return new ExtGridResponse<ExtTreeModel>(new ArrayList<ExtTreeModel>(), jobExplorer.getJobExecutionCount());
			if (displayParam.equals(SHOW_ALL))
				return new ExtGridResponse<ExtTreeModel>(getJobList(property, direction, start, limit), jobExplorer.getJobExecutionCount());
			if (displayParam.equals(SHOW_ACTIVE))
				return new ExtGridResponse<ExtTreeModel>(getJobList(ExitStatus.RUNNING, property, direction, start, limit), 
						jobExplorer.getJobExecutionCount(ExitStatus.EXECUTING) + jobExplorer.getJobExecutionCount(ExitStatus.HIBERNATING));
			if (displayParam.equals(SHOW_COMPLETED))
				return new ExtGridResponse<ExtTreeModel>(getJobList(ExitStatus.COMPLETED, property, direction, start, limit), jobExplorer.getJobExecutionCount(ExitStatus.COMPLETED));
			if (displayParam.equals(SHOW_FAILED))
				return new ExtGridResponse<ExtTreeModel>(getJobList(ExitStatus.FAILED, property, direction, start, limit), jobExplorer.getJobExecutionCount(ExitStatus.FAILED));
			if (displayParam.equals(SHOW_TERMINATED))
				return new ExtGridResponse<ExtTreeModel>(getJobList(ExitStatus.TERMINATED, property, direction, start, limit), jobExplorer.getJobExecutionCount(ExitStatus.TERMINATED));
			
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
			return new ExtGridResponse<ExtTreeModel>(getSteps(nodeId, property, direction, start, limit), totalCount);
		}
		return null;
	}
	
	
	@Override
	public ExtStepInfoModel getExtStepInfoModel(Long jobExecutionId, String stepName){
		ExtStepInfoModel m = new ExtStepInfoModel();
		// StepExecution with id provided may have been expired since displaying tree-grid so make sure we get the 
		// latest StepExecution of the same name for the given JobInstance
		JobInstance ji = jobExplorer.getJobExecution(jobExecutionId).getJobInstance();
		StepExecution se = jobRepository.getLastStepExecution(ji, stepName);
		if (se == null){
			logger.warn("Unable to retrieve Step Execution with jobExecutionId=" + jobExecutionId + " and stepName=" + stepName);
			return m;
		}
		GridResult r = null;
		if (se.getExecutionContext().containsKey(GridResult.GRID_RESULT_KEY)){
			r = (GridResult) se.getExecutionContext().get(GridResult.GRID_RESULT_KEY);
		} else {
			logger.info("Unable to retrieve a GridResult for stepName=" + stepName);
			return m;
		}
		try{
			Map<String, String> jobInfo = gws.getParsedJobSubmissionInfo(r);
			if (!se.getExitStatus().isRunning() && !se.getExitStatus().getExitCode().equals(ExitStatus.UNKNOWN.getExitCode()))
				jobInfo.put(messageService.getMessage("batchViewer.runTime.label"), getElapsedTime(se.getStartTime(), se.getEndTime()));
			else
				jobInfo.put(messageService.getMessage("batchViewer.executingTime.label"), getElapsedTime(se.getStartTime(), new Date()));
			jobInfo.put(messageService.getMessage("batchViewer.clusterJobStatus.label"), r.getJobStatus().toString().toLowerCase());
			if (r.getExitStatus() != -1)
				jobInfo.put(messageService.getMessage("batchViewer.clusterJobExitStatus.label"), parseExitStatus(r.getExitStatus()));
			jobInfo.put(messageService.getMessage("batchViewer.batchJobStatus.label"), se.getExitStatus().getExitCode().toString().toLowerCase());
			if (!se.getExitStatus().getExitDescription().isEmpty())
				jobInfo.put(messageService.getMessage("batchViewer.batchJobExitDesc.label"), se.getExitStatus().getExitDescription());
			if (!jobInfo.isEmpty())
				m.setInfo(renderMapToHtmlTable(jobInfo));
			else 
				m.setInfo(renderMessageToHtml(messageService.getMessage("batchViewer.renderJobInfoFail.label")));
		} catch (IOException e){
			m.setInfo(renderMessageToHtml(messageService.getMessage("batchViewer.renderJobInfoFail.label")));
			logger.info("No grid job information returned for GridResult id=" + r.getId());
		}
		try{
			m.setScript(renderScriptData(gws.getJobScript(r)));
		} catch (IOException e){
			m.setScript(renderMessageToHtml(messageService.getMessage("batchViewer.renderScriptFail.label")));
			logger.info("No execution script returned for GridResult id=" + r.getId());
		}
		try{
			m.setStdout(getPreformattedHtml(gws.getResultStdOut(r, SgeWorkService.MAX_FILE_SIZE)));
		} catch (IOException e){
			m.setStdout(renderMessageToHtml(messageService.getMessage("batchViewer.renderStdoutFail.label")));
			logger.info("No stdout returned for GridResult id=" + r.getId());
		}
		try{
			m.setStderr(getPreformattedHtml(gws.getResultStdErr(r, SgeWorkService.MAX_FILE_SIZE)));
		} catch (IOException e){
			m.setStderr(renderMessageToHtml(messageService.getMessage("batchViewer.renderStderrFail.label")));
			logger.info("No stderr returned for GridResult id=" + r.getId());
		}
		try{
			Map<String, String> clusterStats = gws.getParsedFinalJobClusterStats(r);
			if (!clusterStats.isEmpty())
				m.setClusterReport(renderMapToHtmlTable(clusterStats));
			else
				m.setClusterReport(renderMessageToHtml(messageService.getMessage("batchViewer.renderJobReportFail.label")));
		} catch (IOException e){
			logger.info("No grid execution final report returned for GridResult id=" + r.getId());
			m.setClusterReport(renderMessageToHtml(messageService.getMessage("batchViewer.renderJobReportFail.label")));
		}
		try{
			Map<String, String> env = gws.getParsedEnvironment(r);
			if (!env.isEmpty())
				m.setEnvVars(renderMapToHtmlTable(env));
			else
				m.setEnvVars(renderMessageToHtml(messageService.getMessage("batchViewer.renderEnvFail.label")));
		} catch (IOException e){
			m.setEnvVars(renderMessageToHtml(messageService.getMessage("batchViewer.renderEnvFail.label")));
			logger.info("No grid environment data returned for GridResult id=" + r.getId());
		}
		try{
			Set<String> sw = gws.getParsedSoftware(r);
			if (!sw.isEmpty())
				m.setSoftwareList(renderSetToHtmlTable(sw));
			else
				m.setSoftwareList(renderMessageToHtml(messageService.getMessage("batchViewer.renderSwFail.label")));
		} catch (IOException e){
			m.setSoftwareList(renderMessageToHtml(messageService.getMessage("batchViewer.renderSwFail.label")));
			logger.info("No grid software data returned for GridResult id=" + r.getId());
		}
		return m;
	}
	
	private String getElapsedTime(Date t1, Date t2){
		Period period = new Period(new DateTime(t1.getTime()), new DateTime(t2.getTime()));
	  	PeriodFormatter pf = new PeriodFormatterBuilder()
	  		.appendYears().appendSuffix("Y, ")
	  		.appendMonths().appendSuffix("M, ")
	  		.appendDays().appendSuffix("D, ")
	  		.appendHours().appendSuffix("h, ")
	  		.appendMinutes().appendSuffix("m, ")
	  		.appendSeconds().appendSuffix("s")
	  		.printZeroNever()
	  		.toFormatter();
	  	return StringUtils.removeEnd(pf.print(period), ", ");
	}
	
	private String renderScriptData(String data) {
		StringBuilder sb = new StringBuilder("<div style=\"padding: 10px;white-space: nowrap;\">");
		for (String line : data.split("\n")){
			if (line.startsWith("#$"))
				sb.append("<span style=\"color: orange\">").append(line).append("</span>");
			else if (line.startsWith("##### "))
				sb.append("<span style=\"color: blue\">").append(line).append("</span>");
			else if (line.startsWith("#"))
				sb.append("<span style=\"color: green\">").append(line).append("</span>");
			else
				sb.append(line);
			sb.append("<br />");
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	private String renderMessageToHtml(String message){
		return "<h2 style=\"padding-top: 25px;text-align:center\">" + message.replace("\n", "<br />") + "</h2>";
	}
	
	private String renderMapToHtmlTable(Map<String, String> data){
		StringBuilder info = new StringBuilder("<div style=\"margin:10px\"><table class=\"keyValue\">");
		for (String key: data.keySet())
			info.append("<tr><th>").append(key).append("</th><td>").append(data.get(key).replace("\n", "<br />")).append("</td></tr>");
		info.append("</table></div>");
		return info.toString();
	}
	
	private String renderSetToHtmlTable(Set<String> data){
		int index = 1;
		StringBuilder info = new StringBuilder("<div style=\"margin:10px\"><table class=\"keyValue\">");
		for (String value: data)
			info.append("<tr><th>").append(index++).append("</th><td>").append(value.replace("\n", "<br />")).append("</td></tr>");
		info.append("</table></div>");
		return info.toString();
	}
	
	private String getPreformattedHtml(String text){
		StringBuilder sb = new StringBuilder();
		return sb.append("<pre style=\"padding: 10px\">").append(text).append("</pre>").toString();
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
	
	private String parseExitStatus(int exitStatus){
		String exitStatusStr = Integer.toString(exitStatus);
		if (exitStatus == 1)
			exitStatusStr += messageService.getMessage("batchViewer.errorCode_1.label");
		else if (exitStatus == 2)
			exitStatusStr += messageService.getMessage("batchViewer.errorCode_2.label");
		else if (exitStatus == 126)
			exitStatusStr += messageService.getMessage("batchViewer.errorCode_126.label");
		else if (exitStatus == 127)
			exitStatusStr += messageService.getMessage("batchViewer.errorCode_127.label");
		else if (exitStatus == 128)
			exitStatusStr += messageService.getMessage("batchViewer.errorCode_128.label");
		else if (exitStatus == 137)
			exitStatusStr += messageService.getMessage("batchViewer.errorCode_137.label");
		return exitStatusStr;
	}

}
