package edu.yu.einstein.wasp.controller.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BatchJobTreeModel extends ExtTreeModel {
	
	private static final long serialVersionUID = 1483666735851913153L;

	private String name = "";
	
	private Long executionId = 0L;
	
	private String startTime = null;
	
	private String endTime = null;
	
	private String status = "";
	
	private String exitCode = "";
	
	private boolean isResultAvailable = false;

	public BatchJobTreeModel() {
	}

	public BatchJobTreeModel(String id, ExtIcon iconCls, boolean isExpanded, boolean isLeaf) {
		super(id, iconCls, isExpanded, isLeaf);
	}

	public BatchJobTreeModel(String id, ExtIcon iconCls, boolean isExpanded, boolean isLeaf, String name, Long executionId, Date startTime, Date endTime,
			String status, String exitCode, boolean isResultAvailable) {
		super(id, iconCls, isExpanded, isLeaf);
		this.name = name;
		this.executionId = executionId;
		setStartTime(startTime);
		setEndTime(endTime);
		this.status = status;
		this.exitCode = exitCode;
		this.isResultAvailable = isResultAvailable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getExecutionId() {
		return executionId;
	}

	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
	}

	public String getStartTime() {
		return startTime;
	}
	
	private String getFormattedDateString(Date date){
		if (date == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}

	public void setStartTime(Date startTime) {
		this.startTime = getFormattedDateString(startTime);
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = getFormattedDateString(endTime);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExitCode() {
		return exitCode;
	}

	public void setExitCode(String exitCode) {
		this.exitCode = exitCode;
	}

	public boolean isResultAvailable() {
		return isResultAvailable;
	}

	public void setResultAvailable(boolean isResultAvailable) {
		this.isResultAvailable = isResultAvailable;
	}

}
