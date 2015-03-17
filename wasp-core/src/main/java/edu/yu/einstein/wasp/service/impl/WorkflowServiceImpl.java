package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Control.Option;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
@Transactional("entityManager")
public class WorkflowServiceImpl extends WaspServiceImpl implements WorkflowService{
	
	private static final String DELIMITER = ";";
	
	private WorkflowMetaDao workflowMetaDao;
	
	private WorkflowDao workflowDao;
	
	private WorkflowresourcecategoryDao workflowresourcecategoryDao;
	
	private WorkflowSoftwareDao workflowSoftwareDao;

	@Autowired
	private AdaptorService adaptorService;

	@Override
	public WorkflowSoftwareDao getWorkflowSoftwareDao() {
		return workflowSoftwareDao;
	}

	@Override
	@Autowired
	public void setWorkflowSoftwareDao(WorkflowSoftwareDao workflowSoftwareDao) {
		this.workflowSoftwareDao = workflowSoftwareDao;
	}

	@Override
	@Autowired
	public void setWorkflowMetaDao(WorkflowMetaDao workflowMetaDao) {
		this.workflowMetaDao = workflowMetaDao;
	}
	
	@Override
	public WorkflowMetaDao getWorkflowMetaDao() {
		return workflowMetaDao;
	}
	
	@Override
	@Autowired
	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
	
	@Override
	public WorkflowDao getWorkflowDao() {
		return workflowDao;
	}
	
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Workflow> getWorkflows(){
		return workflowDao.findAll();
	}

	@Override
	public WorkflowresourcecategoryDao getWorkflowresourcecategoryDao() {
		return workflowresourcecategoryDao;
	}

	@Override
	@Autowired
	public void setWorkflowresourcecategoryDao(WorkflowresourcecategoryDao workflowresourcecategoryDao) {
		this.workflowresourcecategoryDao = workflowresourcecategoryDao;
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
	 * @throws MetadataException 
	 */
	@Override
	public void setJobFlowBatchJobName(Workflow workflow, String name) throws MetadataException {
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
	 * @throws MetadataException 
	 */
	@Override
	public void setPageFlowOrder(Workflow workflow, List<String> pageList) throws MetadataException {
		Assert.assertParameterNotNull(workflow, "workflow cannot be null");
		Assert.assertParameterNotNull(pageList, "pageList cannot be null");
		setMeta(workflow, PAGE_FLOW_ORDER_META_KEY, StringUtils.collectionToDelimitedString(pageList, DELIMITER));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, List<Option>> getConfiguredOptions(Workflow workflow, ResourceCategory resourceCategory) throws MetadataException{
		// Resource Options loading
		Map<String, List<Option>> resourceOptions = new HashMap<String, List<Option>>();
		Workflowresourcecategory workflowresourcecategory = workflowresourcecategoryDao.getWorkflowresourcecategoryByWorkflowIdResourcecategoryId(workflow.getId(), resourceCategory.getId());
		for (WorkflowresourcecategoryMeta wrm: workflowresourcecategory.getWorkflowresourcecategoryMeta()) {
			String key = wrm.getK(); 
			key = key.replaceAll("^.*allowableUiField\\.", "");
			List<MetaAttribute.Control.Option> options=new ArrayList<MetaAttribute.Control.Option>();
			for(String el: org.springframework.util.StringUtils.tokenizeToStringArray(wrm.getV(),";")) {
				String [] pair=StringUtils.split(el,":");
				MetaAttribute.Control.Option option = new MetaAttribute.Control.Option();
				option.setValue(pair[0]);
				option.setLabel(pair[1]);
				options.add(option);
			}
			if (options.isEmpty())
				throw new MetadataException("No options obtained from metadata");
			resourceOptions.put(key, options);
		}
		return resourceOptions;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, List<Option>> getConfiguredOptions(Workflow workflow, Software software) throws MetadataException{
		// Resource Options loading
		Map<String, List<Option>> resourceOptions = new HashMap<String, List<Option>>();
		WorkflowSoftware workflowSoftware = workflowSoftwareDao.getWorkflowSoftwareByWorkflowIdSoftwareId(workflow.getId(), software.getId());
		for (WorkflowsoftwareMeta wrm: workflowSoftware.getWorkflowsoftwareMeta()) {
			String key = wrm.getK(); 
			key = key.replaceAll("^.*allowableUiField\\.", "");
			List<MetaAttribute.Control.Option> options=new ArrayList<MetaAttribute.Control.Option>();
			for(String el: org.springframework.util.StringUtils.tokenizeToStringArray(wrm.getV(),";")) {
				String [] pair=StringUtils.split(el,":");
				MetaAttribute.Control.Option option = new MetaAttribute.Control.Option();
				option.setValue(pair[0]);
				option.setLabel(pair[1]);
				options.add(option);
			}
			if (options.isEmpty())
				throw new MetadataException("No options obtained from metadata");
			resourceOptions.put(key, options);
		}
		return resourceOptions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setIsWorkflowDefault(Workflow workflow, boolean isDefault) throws MetadataException{
		setMeta(workflow, IS_WORKFLOW_DEFAULT_META_KEY, Boolean.toString(isDefault));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getIsWorkflowDefault(Workflow workflow){
		WorkflowMeta wfm = workflowMetaDao.getWorkflowMetaByKWorkflowId(WORKFLOW_AREA + "." + IS_WORKFLOW_DEFAULT_META_KEY, workflow.getId());
		logger.debug(WORKFLOW_AREA + "." + IS_WORKFLOW_DEFAULT_META_KEY + "=" + wfm);
		if (wfm != null && wfm.getV() != null)
			return Boolean.valueOf(wfm.getV());
		return false;
	}
	
	private void setMeta(Workflow workflow, String metaKey, String metaValue) throws MetadataException{
		Assert.assertParameterNotNull(workflow, "workflow cannot be null");
		Assert.assertParameterNotNull(metaKey, "metaKey cannot be null");
		Assert.assertParameterNotNull(metaValue, "metaValue cannot be null");
		WorkflowMeta jobFlowBatchJobNameMeta = new WorkflowMeta();
		jobFlowBatchJobNameMeta.setK(WORKFLOW_AREA + "." + metaKey);
		jobFlowBatchJobNameMeta.setV(metaValue);
		jobFlowBatchJobNameMeta.setWorkflowId(workflow.getId());
		workflowMetaDao.setMeta(jobFlowBatchJobNameMeta);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Adaptorset> getAdaptorsetsForWorkflow(Workflow workflow){
		List<Adaptorset> adaptorsetList = new ArrayList<Adaptorset>();
		WorkflowMeta wfm = workflowMetaDao.getWorkflowMetaByKWorkflowId(WORKFLOW_AREA+"."+ADAPTORSETS_META_KEY, workflow.getId());
		if(wfm!=null && wfm.getV()!=null){//will be delimited string of id(s) like "1;2;4;7"
			for(String adaptorsetIdAsString : wfm.getV().split(ADAPTORSET_SEPERATOR)){
				try{
					Integer id = Integer.parseInt(adaptorsetIdAsString);
					Adaptorset adaptorset = adaptorService.getAdaptorsetDao().findById(id.intValue());
					adaptorsetList.add(adaptorset);
				}catch(Exception e){
					logger.debug("unable to obtain adaptorset for workflow iname " + workflow.getIName());
				}
			}
		}
		return adaptorsetList;			
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAdaptorsetsForWorkflow(Workflow workflow, List<Adaptorset> adaptorsetListForThisWorkflow){
		
		if(adaptorsetListForThisWorkflow.size()>1){
			class AdaptorsetIdComparator implements Comparator<Adaptorset> {
				@Override
				public int compare(Adaptorset arg0, Adaptorset arg1) {
					return arg0.getId().intValue() - arg1.getId().intValue();
				}
			}
			Collections.sort(adaptorsetListForThisWorkflow, new AdaptorsetIdComparator());//sort by Adaptorset id
		}
		
		StringBuffer stringBuffer = new StringBuffer("");
		for(Adaptorset adaptorset : adaptorsetListForThisWorkflow){
			if(!stringBuffer.toString().isEmpty()){
				stringBuffer.append(ADAPTORSET_SEPERATOR);
			}
			stringBuffer.append(adaptorset.getId().toString());
		}
		String metaV = new String(stringBuffer);
		try{			
			this.setMeta(workflow, ADAPTORSETS_META_KEY, metaV);
		}catch(Exception e){
			logger.debug("unable to save adaptorset metadata for workflow iname: " + workflow.getIName());
		}
	}
}
