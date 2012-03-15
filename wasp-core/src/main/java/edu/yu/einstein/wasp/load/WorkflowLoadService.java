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
import edu.yu.einstein.wasp.dao.SubtypeSampleDao;
import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.dao.WorkflowsubtypesampleDao;
import edu.yu.einstein.wasp.dao.WorkflowtyperesourceDao;
import edu.yu.einstein.wasp.exception.NullSubtypeSampleException;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
import edu.yu.einstein.wasp.model.Workflowtyperesource;


/**
 * update/inserts db copy of subtype sample from bean definition
 * takes in  properties
 *   - iname / internalname
 *   - name / label
 *   - uifields (List<UiFields>)
 *   - subtypesample (Set<SubtypeSample>)  allowable samples for workflow
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
  private SubtypeSampleDao subtypeSampleDao;
  
  @Autowired
  private SoftwareDao softwareDao;
  
  @Autowired
  private ResourceCategoryDao resourceCategoryDao;
  
  @Autowired
  private WorkflowresourcecategoryDao workflowResourceCategoryDao;
  
  @Autowired
  private WorkflowSoftwareDao workflowSoftwareDao;

  @Autowired
  private WorkflowsubtypesampleDao workflowsubtypesampleDao;

  @Autowired
  private WorkflowtyperesourceDao workflowtyperesourceDao;

  @Autowired
  TypeResourceDao typeResourceDao;

  private List<String> pageFlowOrder; 
  public void setPageFlowOrder(List<String> pageFlowOrder) {this.pageFlowOrder = pageFlowOrder; }

  private List<String> dependencies; 
  public void setDependencies(List<String> dependencies) {this.dependencies = dependencies; }

  private Set<String> subtypeSamples;
  public void setSubtypeSamples(Set<String> subtypeSamples) {this.subtypeSamples = subtypeSamples; }

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
    
    // update dependencies
    Map<String,Workflowtyperesource> oldWorkflowTypeResources = new HashMap<String, Workflowtyperesource>();
    for (Workflowtyperesource old : safeList(workflow.getWorkflowtyperesource()) ){
	   oldWorkflowTypeResources.put(old.getWorkflowtyperesourceId().toString(), old);
    }
    List<Integer> typeResourceIdList = new ArrayList<Integer>();
    for (String dependency: safeList(dependencies)) { 
      Integer typeResourceId = typeResourceDao.getTypeResourceByIName(dependency).getTypeResourceId();
      if (typeResourceId == null){
    	// the specified resourceType does not exist!!
    	  throw new NullTypeResourceException();
      }
      
      Workflowtyperesource workflowtyperesource = workflowtyperesourceDao.getWorkflowtyperesourceByWorkflowIdTypeResourceId(workflow.getWorkflowId(), typeResourceId);
	  if (workflowtyperesource.getWorkflowtyperesourceId() == null){
		  // doesn't exist so create and save
	      workflowtyperesource.setWorkflowId(workflow.getWorkflowId()); 
	      workflowtyperesource.setTypeResourceId(typeResourceId); 
	      workflowtyperesourceDao.save(workflowtyperesource);
	  } else {
		  // already exists
		  oldWorkflowTypeResources.remove(workflowtyperesource.getWorkflowtyperesourceId().toString());
	  } 
    }
    // remove old no longer used dependencies
    for (String key : oldWorkflowTypeResources.keySet()){
  	  workflowtyperesourceDao.remove(oldWorkflowTypeResources.get(key));
  	  workflowtyperesourceDao.flush(oldWorkflowTypeResources.get(key));
    } 
    
    for (Integer typeResourceId : typeResourceIdList){
    	// loop through all the dependencies and see if an active version (software or resourceCategory) is defined for each one.
    	// If any are not defined or defined but not active then we must make the workflow inactive until these associations have
    	// been made using the workflow resource/software configuration form
    	typeResourceIdList.add(typeResourceId);
        Map<String, Integer> dependencyQueryMap = new HashMap<String, Integer>();
        dependencyQueryMap.put("typeresourceid", typeResourceId);
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
    

    // inserts or update workflow
    if (workflow.getWorkflowId() == null) { 
      workflow = new Workflow();

      workflow.setIName(iname);
      workflow.setName(name);
      workflow.setIsActive(isActive); // only set to active when all dependencies configured by admin
      workflow.setCreatets(new Date());

      workflowDao.save(workflow); 

      // refreshes
      workflow = workflowDao.getWorkflowByIName(iname); 

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
        if (changed)
      	  workflowDao.save(workflow); 
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
    List<Workflowsubtypesample> oldWorkflowSubtypeSamples = workflow.getWorkflowsubtypesample();

    // todo better logic so updates.
    if (oldWorkflowSubtypeSamples != null) {
	    for (Workflowsubtypesample Workflowsubtypesample: oldWorkflowSubtypeSamples) {
	      String subtypeIName = Workflowsubtypesample.getSubtypeSample().getIName();
	
	      // already exists in set... 
	      // remove from set
	      if (subtypeSamples.contains(subtypeIName)) {
	        subtypeSamples.remove(subtypeIName);
	        continue;
	      }
	
	      // else remove from db
	      workflowsubtypesampleDao.remove(Workflowsubtypesample);
	      workflowsubtypesampleDao.flush(Workflowsubtypesample);
	    }
    }

    // the leftovers were not in the db so create
    for (String subtypeSampleIName: safeSet(subtypeSamples)) {
      SubtypeSample subtypeSample = subtypeSampleDao.getSubtypeSampleByIName(subtypeSampleIName);
      if (subtypeSample.getSubtypeSampleId() != null){
	      Workflowsubtypesample newWorkflowsubtypesample = new Workflowsubtypesample();
	      newWorkflowsubtypesample.setWorkflowId(workflow.getWorkflowId()); 
	      newWorkflowsubtypesample.setSubtypeSampleId(subtypeSample.getSubtypeSampleId());
	
	      workflowsubtypesampleDao.save(newWorkflowsubtypesample);
      } else {
    	  // the specified subtypesample does not exist!!
    	  throw new NullSubtypeSampleException();
      }
    }

  }
}

