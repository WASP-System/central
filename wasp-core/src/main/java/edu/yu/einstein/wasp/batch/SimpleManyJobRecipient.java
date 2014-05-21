package edu.yu.einstein.wasp.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import edu.yu.einstein.wasp.interfacing.batch.ManyJobRecipient;

public class SimpleManyJobRecipient implements ManyJobRecipient {
	
	private Long jobExecutionId;
	
	private String stepName;
	
	private UUID parentID;
	
	private List<String> childIDs;
	
	public SimpleManyJobRecipient(final ManyJobRecipient sourceJobRecipient){
		setJobExecutionId(sourceJobRecipient.getJobExecutionId());
		setStepName(sourceJobRecipient.getStepName());
    	setParentID(sourceJobRecipient.getParentID());
    	setChildIDs(new ArrayList<String>(sourceJobRecipient.getChildIDs()));
	}

	@Override
	public Long getJobExecutionId() {
		return jobExecutionId;
	}

	@Override
	public String getStepName() {
		return stepName;
	}

	@Override
	public UUID getParentID() {
		return parentID;
	}

	@Override
	public List<String> getChildIDs() {
		return childIDs;
	}

	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public void setParentID(UUID parentID) {
		this.parentID = parentID;
	}

	public void setChildIDs(List<String> childIDs) {
		this.childIDs = childIDs;
	}

}
