package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional
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
		setMeta(workflow, JOB_FLOW_BATCH_META_KEY, name);
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
		setMeta(workflow, PAGE_FLOW_ORDER_META_KEY, StringUtils.collectionToDelimitedString(pageList, DELIMITER));
	}
	
	private void setMeta(Workflow workflow, String metaKey, String metaValue){
		Assert.assertParameterNotNull(workflow, "workflow cannot be null");
		Assert.assertParameterNotNull(metaKey, "metaKey cannot be null");
		Assert.assertParameterNotNull(metaValue, "metaValue cannot be null");
		List<WorkflowMeta> workflowMetaList = workflow.getWorkflowMeta();
		if (workflowMetaList == null)
			workflowMetaList = new ArrayList<WorkflowMeta>();
		WorkflowMeta jobFlowBatchJobNameMeta = null;
		try{
			jobFlowBatchJobNameMeta = MetaHelper.getMetaObjectFromList(WORKFLOW_AREA, metaKey, workflowMetaList);
			
			if (jobFlowBatchJobNameMeta.getV().equals(metaValue)){ // no change in value
				return;
			}
		} catch(MetadataException e) {
			// doesn't exist so create
			jobFlowBatchJobNameMeta = new WorkflowMeta();
			jobFlowBatchJobNameMeta.setK(WORKFLOW_AREA + "." + metaKey);
		}
		jobFlowBatchJobNameMeta.setV(metaValue);
		workflowMetaDao.updateByWorkflowId(workflow.getWorkflowId(), jobFlowBatchJobNameMeta);
	}
	
	

}
