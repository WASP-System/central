package edu.yu.einstein.wasp.service.impl;

import java.util.List;

import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.service.WorkflowService;

public class WorkflowServiceImpl extends WaspServiceImpl implements WorkflowService{
	
	public static final String JOB_FLOW_BATCH_META_KEY = "workflow.jobFlowBatchJob";
	
	public static final String PAGE_FLOW_ORDER_META_KEY = "workflow.submitpageflow";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobFlowBatchJobName(Workflow workflow) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJobFlowBatchJobName(Workflow workflow, String name) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getPageFlowOrder(Workflow workflow) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPageFlowOrder(Workflow workflow, List<String> pageList) {
		if (workflow == null)
			throw new InvalidParameterException("workflow cannot be null");
		if (pageList == null)
			throw new InvalidParameterException("pageList cannot be null");
		if (workflow.getWorkflowId() == null || workflow.getWorkflowId() == 0)
			throw new InvalidParameterException("pageList cannot be null");
		WorkflowMeta pageFlowWorkflowMeta = new WorkflowMeta();
		pageFlowWorkflowMeta.setK("workflow.submitpageflow");
		pageFlowWorkflowMeta.setV( StringUtils.collectionToDelimitedString(pageList, ";") );
		pageFlowWorkflowMeta.setPosition(0);
		
	}
	
	

}
