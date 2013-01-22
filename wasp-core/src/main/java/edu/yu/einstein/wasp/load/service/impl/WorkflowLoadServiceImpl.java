package edu.yu.einstein.wasp.load.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowResourceTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowSampleSubtypeDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.NullResourceTypeException;
import edu.yu.einstein.wasp.exception.NullSampleSubtypeException;
import edu.yu.einstein.wasp.load.service.WorkflowLoadService;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowResourceType;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.service.impl.WorkflowServiceImpl;

@Service
@Transactional("entityManager")
public class WorkflowLoadServiceImpl extends WaspLoadServiceImpl implements	WorkflowLoadService {
	
	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private WorkflowMetaDao workflowMetaDao;

	@Autowired
	private SampleSubtypeDao sampleSubtypeDao;
	  
	@Autowired
	private SoftwareDao softwareDao;
	  
	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	  
	@Autowired
	private WorkflowresourcecategoryDao workflowResourceCategoryDao;
	  
	@Autowired
	private WorkflowSoftwareDao workflowSoftwareDao;

	@Autowired
	private WorkflowSampleSubtypeDao workflowsamplesubtypeDao;

	@Autowired
	private WorkflowResourceTypeDao workflowresourcetypeDao;

	@Autowired
	ResourceTypeDao resourceTypeDao;
	
	@Autowired
	private WorkflowService workflowService;
	
	private Workflow addOrUpdateWorkflow(String iname, String name, int isActive, List<ResourceType> dependencies){
		Workflow workflow = workflowDao.getWorkflowByIName(iname);
	    // inserts or update workflow
	    if (workflow.getWorkflowId() == null) { 
	      workflow = new Workflow();

	      workflow.setIName(iname);
	      workflow.setName(name);
	      workflow.setIsActive(isActive); // only set to active when all dependencies configured by admin
	      workflow.setCreatets(new Date());

	      workflow = workflowDao.save(workflow); 

	    } else {
	    	if (!workflow.getName().equals(name)){
	        	workflow.setName(name);
	        }
	        if (workflow.getIsActive().intValue() != isActive){
	        	workflow.setIsActive(isActive);
	        }
	    }
	   
	    // update dependencies
	    Map<Integer,WorkflowResourceType> oldWorkflowResourceTypes = new HashMap<Integer, WorkflowResourceType>();
	    for (WorkflowResourceType old : safeList(workflow.getWorkflowResourceType()) ){
		   oldWorkflowResourceTypes.put(old.getWorkflowresourcetypeId(), old);
	    }
	    List<Integer> resourceTypeIdList = new ArrayList<Integer>();
	    for (ResourceType dependency: safeList(dependencies)) { 
	      Integer resourceTypeId = dependency.getResourceTypeId();
	      if (resourceTypeId == null){
	    	// the specified resourceType does not exist!!
	    	  throw new NullResourceTypeException("No resource type defined with iname='"+dependency+"'");
	      }
	      
	      WorkflowResourceType workflowResourceType = workflowresourcetypeDao.getWorkflowResourceTypeByWorkflowIdResourceTypeId(workflow.getWorkflowId(), resourceTypeId);
		  if (workflowResourceType.getWorkflowresourcetypeId() == null){
			  // doesn't exist so create and save
		      workflowResourceType.setWorkflowId(workflow.getWorkflowId()); 
		      workflowResourceType.setResourceTypeId(resourceTypeId); 
		      workflowresourcetypeDao.save(workflowResourceType);
		  } else {
			  // already exists
			  oldWorkflowResourceTypes.remove(workflowResourceType.getWorkflowresourcetypeId());
		  } 
	    }
	    // remove old no longer used dependencies
	    for (Integer key : oldWorkflowResourceTypes.keySet()){
	  	  workflowresourcetypeDao.remove(oldWorkflowResourceTypes.get(key));
	  	  workflowresourcetypeDao.flush(oldWorkflowResourceTypes.get(key));
	    } 
	    
	    for (Integer resourceTypeId : resourceTypeIdList){
	    	// loop through all the dependencies and see if an active version (software or resourceCategory) is defined for each one.
	    	// If any are not defined or defined but not active then we must make the workflow inactive until these associations have
	    	// been made using the workflow resource/software configuration form
	    	resourceTypeIdList.add(resourceTypeId);
	        Map<String, Integer> dependencyQueryMap = new HashMap<String, Integer>();
	        dependencyQueryMap.put("resourcetypeid", resourceTypeId);
	        dependencyQueryMap.put("isactive", 1);
	        Boolean isActiveDependencyMatch = false; 
	        for (Software dependency : softwareDao.findByMap(dependencyQueryMap)){
	      	  if (workflowSoftwareDao.getWorkflowSoftwareByWorkflowIdSoftwareId(workflow.getWorkflowId(), dependency.getSoftwareId()).getWorkflowSoftwareId() != null){
	      		isActiveDependencyMatch = true;
	      		break;
	      	  }
	        }
	        if (isActiveDependencyMatch)
	        	continue;
	        for (ResourceCategory dependency : resourceCategoryDao.findByMap(dependencyQueryMap)){
	        	  if (workflowResourceCategoryDao
	        			  .getWorkflowresourcecategoryByWorkflowIdResourcecategoryId(workflow.getWorkflowId(), dependency.getResourceCategoryId())
	        			  .getResourcecategoryId() != null){
	        		  isActiveDependencyMatch = true;
	        		  break;
	        	  }
	        }
	        if (!isActiveDependencyMatch){
	        	isActive = 0;
	        	break;
	        }
	    }
	    return workflow;
	}
	
	private void syncWorkflowMeta(Workflow workflow, List<WorkflowMeta> meta, Set<String> doNotDeleteKeyList){
	    int lastPosition = 0;
	    Map<String, WorkflowMeta> oldWorkflowMetas  = new HashMap<String, WorkflowMeta>();

	    if (workflow != null && workflow.getWorkflowMeta() != null) {
	      // workflow and associated meta already exists for provided iname
	      for (WorkflowMeta workflowMeta: safeList(workflow.getWorkflowMeta())) {
	        oldWorkflowMetas.put(workflowMeta.getK(), workflowMeta);
	      }
	    }

	    for (WorkflowMeta workflowMeta: safeList(meta)) {
	      // loop through newly provided metadata
	    	
	      // incremental position numbers.
	      if ( workflowMeta.getPosition() == null ||
	           workflowMeta.getPosition().intValue() <= lastPosition
	        )  {
	        workflowMeta.setPosition(lastPosition +1);
	      }
	      lastPosition = workflowMeta.getPosition().intValue();

	      if (oldWorkflowMetas.containsKey(workflowMeta.getK())) {
	    	// current meta key exists in db already
	        WorkflowMeta old = oldWorkflowMetas.get(workflowMeta.getK());
	        boolean changed = false;
	        if (!old.getV().equals(workflowMeta.getV())){
	        	old.setV(workflowMeta.getV());
	        	changed = true;
	        }
	        if (old.getPosition().intValue() != workflowMeta.getPosition()){
	        	old.setPosition(workflowMeta.getPosition());
	        	changed = true;
	        }
	        if (changed)
	        	workflowMetaDao.save(old);

	        oldWorkflowMetas.remove(old.getK()); // remove this key from the old meta list as we're done with it
	        continue;
	      }
	      // current meta key does not exist in db already. Save this new metadata
	      workflowMeta.setWorkflowId(workflow.getWorkflowId());
	      workflowMeta.setPosition(1);
	      workflowMetaDao.save(workflowMeta);
	    }

	    // delete the left overs if not in the do not delete list
	    for (String workflowMetaKey : oldWorkflowMetas.keySet()) {
	      WorkflowMeta workflowMeta = oldWorkflowMetas.get(workflowMetaKey);
	      if (doNotDeleteKeyList.contains(workflowMeta.getK()))
	    	  continue;
	      workflowMetaDao.remove(workflowMeta);
	      workflowMetaDao.flush(workflowMeta);
	    }
	    

	}
	
	private void syncWorkflowSampleSubtypes(Workflow workflow, Set<String> sampleSubtypeInames){
	    Map<String, Integer> m = new HashMap<String, Integer>();
	    m.put("workflowId", workflow.getWorkflowId());
	    List<WorkflowSampleSubtype> oldWorkflowSampleSubtypes = workflow.getWorkflowSampleSubtype();

	    // todo better logic so updates.
	    if (oldWorkflowSampleSubtypes != null) {
		    for (WorkflowSampleSubtype WorkflowSampleSubtype: oldWorkflowSampleSubtypes) {
		      String subtypeIName = WorkflowSampleSubtype.getSampleSubtype().getIName();
		
		      // already exists in set... 
		      // remove from set
		      if (sampleSubtypeInames.contains(subtypeIName)) {
		    	  sampleSubtypeInames.remove(subtypeIName);
		        continue;
		      }
		
		      // else remove from db
		      workflowsamplesubtypeDao.remove(WorkflowSampleSubtype);
		      workflowsamplesubtypeDao.flush(WorkflowSampleSubtype);
		    }
	    }

	    // the leftovers were not in the db so create
	    for (String sampleSubtypeIName: safeSet(sampleSubtypeInames)) {
	      SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName(sampleSubtypeIName);
	      if (sampleSubtype.getSampleSubtypeId() != null){
		      WorkflowSampleSubtype newWorkflowSampleSubtype = new WorkflowSampleSubtype();
		      newWorkflowSampleSubtype.setWorkflowId(workflow.getWorkflowId()); 
		      newWorkflowSampleSubtype.setSampleSubtypeId(sampleSubtype.getSampleSubtypeId());
		
		      workflowsamplesubtypeDao.save(newWorkflowSampleSubtype);
	      } else {
	    	  // the specified samplesubtype does not exist!!
	    	  throw new NullSampleSubtypeException();
	      }
	    }
	}
	
	
	
	@Override
	public Workflow update(String iname, String name, int isActive, List<WorkflowMeta> meta, List<ResourceType> dependencies, 
			List<SampleSubtype> sampleSubtypes, List<String> pageFlowOrder, String jobFlowBatchJobName){
		Assert.assertParameterNotNull(iname, "iname Cannot be null");
		Assert.assertParameterNotNull(name, "name Cannot be null");
		Workflow workflow = addOrUpdateWorkflow(iname, name, isActive, dependencies);
	    
	    Set<String> doNotDeleteKeyList = new HashSet<String>();
	    doNotDeleteKeyList.add(WorkflowServiceImpl.WORKFLOW_AREA + "." + WorkflowServiceImpl.JOB_FLOW_BATCH_META_KEY);
	    doNotDeleteKeyList.add(WorkflowServiceImpl.WORKFLOW_AREA + "." + WorkflowServiceImpl.PAGE_FLOW_ORDER_META_KEY);

	    syncWorkflowMeta(workflow, meta, doNotDeleteKeyList);
	    Set<String> sampleSubtypeInames = new HashSet<String>();
	    for (SampleSubtype sampleSubtype: safeList(sampleSubtypes))
	    	sampleSubtypeInames.add(sampleSubtype.getIName());
	    syncWorkflowSampleSubtypes(workflow, sampleSubtypeInames);
	    try{
		    workflowService.setJobFlowBatchJobName(workflow, jobFlowBatchJobName);
		    workflowService.setPageFlowOrder(workflow, pageFlowOrder);
	    } catch (MetadataException e){
	    	throw new RuntimeException(e);
	    }
	    return workflow;
	}

}
