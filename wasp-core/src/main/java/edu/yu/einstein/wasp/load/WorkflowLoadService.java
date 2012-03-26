package edu.yu.einstein.wasp.load;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import util.spring.PostInitialize;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.dao.WorkflowSampleSubtypeDao;
import edu.yu.einstein.wasp.dao.WorkflowResourceTypeDao;
import edu.yu.einstein.wasp.exception.NullSampleSubtypeException;
import edu.yu.einstein.wasp.exception.NullResourceTypeException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.model.WorkflowResourceType;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - uifields (List<UiFields>)
 *   - samplesubtype (Set<SampleSubtype>)  allowable samples for workflow
 *   - pageFlow (List<String>)
 *   - meta (List<WorkflowMeta>)
 *
 */

@Transactional
public class WorkflowLoadService extends WaspLoadService {

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

  private List<String> pageFlowOrder; 
  public void setPageFlowOrder(List<String> pageFlowOrder) {this.pageFlowOrder = pageFlowOrder; }

  private List<String> dependencies; 
  public void setDependencies(List<String> dependencies) {this.dependencies = dependencies; }

  private Set<String> sampleSubtypes;
  public void setSampleSubtypes(Set<String> sampleSubtypes) {this.sampleSubtypes = sampleSubtypes; }

  private List<WorkflowMeta> meta; 
  public void setMeta(List<WorkflowMeta> workflowMeta) {this.meta = workflowMeta; }
  
  private Integer isActive;
  
  public Integer getIsActive() {
	return isActive;
  }
	
  public void setIsActive(Integer isActive) {
	this.isActive = isActive;
  }

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (iname == null) { return; }
    if (isActive == null)
  	  isActive = 1;
    
    Workflow workflow = workflowDao.getWorkflowByIName(iname);
    // inserts or update workflow
    if (workflow.getWorkflowId() == null) { 
      workflow = new Workflow();

      workflow.setIName(iname);
      workflow.setName(name);
      workflow.setIsActive(isActive); // only set to active when all dependencies configured by admin
      workflow.setCreatets(new Date());

      workflowDao.persist(workflow); 

      // refreshes
      workflowDao.refresh(workflow); 

    } else {
    	boolean changed = false;	
        if (!workflow.getName().equals(name)){
        	workflow.setName(name);
      	  	changed = true;
        }
        if (workflow.getIsActive().intValue() != isActive.intValue()){
        	workflow.setIsActive(isActive.intValue());
      	  	changed = true;
        }
        if (changed) workflowDao.merge(workflow); 
        // refreshes
        workflow = workflowDao.getWorkflowByIName(iname); 
        workflowDao.refresh(workflow); 
    }
     
    logger.debug("ANDY: workflow id for iname="+iname+" is :"+workflow.getWorkflowId());
    
    // update dependencies
    Map<String,WorkflowResourceType> oldWorkflowResourceTypes = new HashMap<String, WorkflowResourceType>();
    for (WorkflowResourceType old : safeList(workflow.getWorkflowResourceType()) ){
	   oldWorkflowResourceTypes.put(old.getWorkflowresourcetypeId().toString(), old);
    }
    List<Integer> resourceTypeIdList = new ArrayList<Integer>();
    for (String dependency: safeList(dependencies)) { 
      Integer resourceTypeId = resourceTypeDao.getResourceTypeByIName(dependency).getResourceTypeId();
      if (resourceTypeId == null){
    	// the specified resourceType does not exist!!
    	  throw new NullResourceTypeException();
      }
      
      WorkflowResourceType workflowResourceType = workflowresourcetypeDao.getWorkflowResourceTypeByWorkflowIdResourceTypeId(workflow.getWorkflowId(), resourceTypeId);
	  if (workflowResourceType.getWorkflowresourcetypeId() == null){
		  // doesn't exist so create and save
	      workflowResourceType.setWorkflowId(workflow.getWorkflowId()); 
	      workflowResourceType.setResourceTypeId(resourceTypeId); 
	      workflowresourcetypeDao.save(workflowResourceType);
	  } else {
		  // already exists
		  oldWorkflowResourceTypes.remove(workflowResourceType.getWorkflowresourcetypeId().toString());
	  } 
    }
    // remove old no longer used dependencies
    for (String key : oldWorkflowResourceTypes.keySet()){
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
    

    

    // updates uiFields
    updateUiFields();


    // sync metas
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

    // delete the left overs
    for (String workflowMetaKey : oldWorkflowMetas.keySet()) {
      WorkflowMeta workflowMeta = oldWorkflowMetas.get(workflowMetaKey);
      workflowMetaDao.remove(workflowMeta);
      workflowMetaDao.flush(workflowMeta);
    }



    // TODO update instead of delete/insert

    String pageFlowString = StringUtils.collectionToDelimitedString(pageFlowOrder, ";");

    // Inserts new UiPageflow fields
    WorkflowMeta pageFlowWorkflowMeta = new WorkflowMeta();
    pageFlowWorkflowMeta.setWorkflowId(workflow.getWorkflowId());
    pageFlowWorkflowMeta.setK("workflow.submitpageflow");
    pageFlowWorkflowMeta.setV(pageFlowString);
    pageFlowWorkflowMeta.setPosition(0);

    workflowMetaDao.save(pageFlowWorkflowMeta);

     
    // allowable subtype samples
    Map m = new HashMap();
    m.put("workflowId", workflow.getWorkflowId());
    List<WorkflowSampleSubtype> oldWorkflowSampleSubtypes = workflow.getWorkflowSampleSubtype();

    // todo better logic so updates.
    if (oldWorkflowSampleSubtypes != null) {
	    for (WorkflowSampleSubtype WorkflowSampleSubtype: oldWorkflowSampleSubtypes) {
	      String subtypeIName = WorkflowSampleSubtype.getSampleSubtype().getIName();
	
	      // already exists in set... 
	      // remove from set
	      if (sampleSubtypes.contains(subtypeIName)) {
	        sampleSubtypes.remove(subtypeIName);
	        continue;
	      }
	
	      // else remove from db
	      workflowsamplesubtypeDao.remove(WorkflowSampleSubtype);
	      workflowsamplesubtypeDao.flush(WorkflowSampleSubtype);
	    }
    }

    // the leftovers were not in the db so create
    for (String sampleSubtypeIName: safeSet(sampleSubtypes)) {
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
}

