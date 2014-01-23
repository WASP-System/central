package edu.yu.einstein.wasp.controller.util;

import java.util.Date;

public class BatchJobTreeModel extends ExtTreeModel {
	
	private static final long serialVersionUID = 1483666735851913153L;

	private String jobName = "";
	
	private Long jobExecutionId = 0L;
	
	private Date startTime = null;
	
	private Date endTime = null;
	
	private String Status = "";
	
	private String ExitCode = "";
	
	private String ExitMessage = "";

	public BatchJobTreeModel() {
	}

	public BatchJobTreeModel(String id, ExtIcon iconCls, boolean isExpanded, boolean isLeaf) {
		super(id, iconCls, isExpanded, isLeaf);
	}

	public BatchJobTreeModel(String id, ExtIcon iconCls, boolean isExpanded, boolean isLeaf, String jobName, Long jobExecutionId, Date startTime, Date endTime,
			String status, String exitCode, String exitMessage) {
		super(id, iconCls, isExpanded, isLeaf);
		this.jobName = jobName;
		this.jobExecutionId = jobExecutionId;
		this.startTime = startTime;
		this.endTime = endTime;
		Status = status;
		ExitCode = exitCode;
		ExitMessage = exitMessage;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getExitCode() {
		return ExitCode;
	}

	public void setExitCode(String exitCode) {
		ExitCode = exitCode;
	}

	public String getExitMessage() {
		return ExitMessage;
	}

	public void setExitMessage(String exitMessage) {
		ExitMessage = exitMessage;
	}

}
