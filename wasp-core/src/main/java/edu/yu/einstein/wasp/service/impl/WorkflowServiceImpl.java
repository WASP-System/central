package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.sun.jna.platform.win32.W32FileMonitor;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.util.MetaHelper;

public class WorkflowServiceImpl extends WaspServiceImpl implements WorkflowService{
	
	public static final String JOB_FLOW_BATCH_META_KEY = "jobFlowBatchJob";
	
	public static final String PAGE_FLOW_ORDER_META_KEY = "submitpageflow";
	
	public static final String WORKFLOW_AREA = "workflow";
	
	private static final String DELIMITER = ";";
	
	private WorkflowMetaDao workflowMetaDao;
	
	@Autowired
	public void setWorkflowMetaDao(WorkflowMetaDao workflowMetaDao) {
		this.workflowMetaDao = workflowMetaDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobFlowBatchJobName(Workflow workflow) {
		Assert.assertParameterNotNull(workflow, "workflow cannot be null");
		String jobFlowBatchJobName = null;
		List<WorkflowMeta> workflowMetaList = workflow.getWorkflowMeta();
		if (workflowMetaList == null)
			workflowMetaList = new ArrayList<WorkflowMeta>();
		try{
			jobFlowBatchJobName = (String) MetaHelper.getMetaValue(WORKFLOW_AREA, JOB_FLOW_BATCH_META_KEY, workflowMetaList);
		} catch(MetadataException e) {
			// value not found
		}
		return jobFlowBatchJobName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJobFlowBatchJobName(Workflow workflow, String name) {
		Assert.assertParameterNotNull(workflow, "workflow cannot be null");
		Assert.assertParameterNotNull(name, "name cannot be null");
		List<WorkflowMeta> workflowMetaList = workflow.getWorkflowMeta();
		if (workflowMetaList == null)
			workflowMetaList = new ArrayList<WorkflowMeta>();
		WorkflowMeta jobFlowBatchJobNameMeta = null;
		try{
			jobFlowBatchJobNameMeta = MetaHelper.getMetaObjectFromList(WORKFLOW_AREA, JOB_FLOW_BATCH_META_KEY, workflowMetaList);
		} catch(MetadataException e) {
			// doesn't exist so create
			jobFlowBatchJobNameMeta = new WorkflowMeta();
			jobFlowBatchJobNameMeta.setK(WORKFLOW_AREA + "." + JOB_FLOW_BATCH_META_KEY);
			jobFlowBatchJobNameMeta.setPosition(0);
			jobFlowBatchJobNameMeta.setWorkflowId(workflow.getWorkflowId());
		}
		jobFlowBatchJobNameMeta.setV(name);
		workflowMetaDao.save(jobFlowBatchJobNameMeta);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getPageFlowOrder(Workflow workflow) {
		Assert.assertParameterNotNull(workflow, "workflow cannot be null");
		String[] pageList = new String[0];
		List<WorkflowMeta> workflowMetaList = workflow.getWorkflowMeta();
		if (workflowMetaList == null)
			workflowMetaList = new ArrayList<WorkflowMeta>();
		String pageFlowWorkflowString = null;
		try{
			pageFlowWorkflowString = (String) MetaHelper.getMetaValue(WORKFLOW_AREA, PAGE_FLOW_ORDER_META_KEY, workflowMetaList);
		} catch(MetadataException e) {
			// value not found
			return pageList;
		}
		pageList = StringUtils.delimitedListToStringArray(pageFlowWorkflowString, DELIMITER);
		return pageList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPageFlowOrder(Workflow workflow, List<String> pageList) {
		Assert.assertParameterNotNull(workflow, "workflow cannot be null");
		Assert.assertParameterNotNull(pageList, "pageList cannot be null");
		List<WorkflowMeta> workflowMetaList = workflow.getWorkflowMeta();
		if (workflowMetaList == null)
			workflowMetaList = new ArrayList<WorkflowMeta>();
		WorkflowMeta pageFlowWorkflowMeta = null;
		try{
			pageFlowWorkflowMeta = MetaHelper.getMetaObjectFromList(WORKFLOW_AREA, PAGE_FLOW_ORDER_META_KEY, workflowMetaList);
		} catch(MetadataException e) {
			// doesn't exist so create
			pageFlowWorkflowMeta = new WorkflowMeta();
			pageFlowWorkflowMeta.setK(WORKFLOW_AREA + "." + PAGE_FLOW_ORDER_META_KEY);
			pageFlowWorkflowMeta.setPosition(0);
			pageFlowWorkflowMeta.setWorkflowId(workflow.getWorkflowId());
		}
		pageFlowWorkflowMeta.setV( StringUtils.collectionToDelimitedString(pageList, DELIMITER) );
		workflowMetaDao.save(pageFlowWorkflowMeta);
	}
	
	

}
