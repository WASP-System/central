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
import edu.yu.einstein.wasp.exception.NullSubtypeSampleException;
import edu.yu.einstein.wasp.exception.NullTypeResourceException;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
import edu.yu.einstein.wasp.model.Workflowtyperesource;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.service.WorkflowMetaService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.service.WorkflowSoftwareService;
import edu.yu.einstein.wasp.service.WorkflowresourcecategoryService;
import edu.yu.einstein.wasp.service.WorkflowsubtypesampleService;
import edu.yu.einstein.wasp.service.WorkflowtyperesourceService;


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
  private WorkflowService workflowService;

  @Autowired
  private WorkflowMetaService workflowMetaService;

  @Autowired
  private SubtypeSampleService subtypeSampleService;
  
  @Autowired
  private SoftwareService softwareService;
  
  @Autowired
  private ResourceCategoryService resourceCategoryService;
  
  @Autowired
  private WorkflowresourcecategoryService workflowResourceCategoryService;
  
  @Autowired
  private WorkflowSoftwareService workflowSoftwareService;

  @Autowired
  private WorkflowsubtypesampleService workflowsubtypesampleService;

  @Autowired
  private WorkflowtyperesourceService workflowtyperesourceService;

  @Autowired
  TypeResourceService typeResourceService;

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
    Workflow workflow = workflowService.getWorkflowByIName(iname); 
    
    // update dependencies
    Map<String,Workflowtyperesource> oldWorkflowTypeResources = new HashMap<String, Workflowtyperesource>();
    for (Workflowtyperesource old : safeList(workflow.getWorkflowtyperesource()) ){
	   oldWorkflowTypeResources.put(old.getWorkflowtyperesourceId().toString(), old);
    }
    List<Integer> typeResourceIdList = new ArrayList<Integer>();
    for (String dependency: safeList(dependencies)) { 
      Integer typeResourceId = typeResourceService.getTypeResourceByIName(dependency).getTypeResourceId();
      if (typeResourceId == null){
    	// the specified resourceType does not exist!!
    	  throw new NullTypeResourceException();
      }
      
      Workflowtyperesource workflowtyperesource = workflowtyperesourceService.getWorkflowtyperesourceByWorkflowIdTypeResourceId(workflow.getWorkflowId(), typeResourceId);
	  if (workflowtyperesource.getWorkflowtyperesourceId() == null){
		  // doesn't exist so create and save
	      workflowtyperesource.setWorkflowId(workflow.getWorkflowId()); 
	      workflowtyperesource.setTypeResourceId(typeResourceId); 
	      workflowtyperesourceService.save(workflowtyperesource);
	  } else {
		  // already exists
		  oldWorkflowTypeResources.remove(workflowtyperesource.getWorkflowtyperesourceId().toString());
	  } 
    }
    // remove old no longer used dependencies
    for (String key : oldWorkflowTypeResources.keySet()){
  	  workflowtyperesourceService.remove(oldWorkflowTypeResources.get(key));
  	  workflowtyperesourceService.flush(oldWorkflowTypeResources.get(key));
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
        for (Software dependency : (List<Software>) softwareService.findByMap(dependencyQueryMap)){
      	  if (workflowSoftwareService.getWorkflowSoftwareByWorkflowIdSoftwareId(workflow.getWorkflowId(), dependency.getSoftwareId()).getWorkflowSoftwareId() != null){
      		isActiveDependencyMatch = true;
      		break;
      	  }
        }
        if (isActiveDependencyMatch)
        	continue;
        for (ResourceCategory dependency : (List<ResourceCategory>) resourceCategoryService.findByMap(dependencyQueryMap)){
        	  if (workflowResourceCategoryService
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

      workflowService.save(workflow); 

      // refreshes
      workflow = workflowService.getWorkflowByIName(iname); 

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
      	  workflowService.save(workflow); 
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
        	workflowMetaService.save(old);

        oldWorkflowMetas.remove(old.getK()); // remove this key from the old meta list as we're done with it
        continue;
      }
      // current meta key does not exist in db already. Save this new metadata
      workflowMeta.setWorkflowId(workflow.getWorkflowId());
      workflowMeta.setPosition(1);
      workflowMetaService.save(workflowMeta);
    }

    // delete the left overs
    for (String workflowMetaKey : oldWorkflowMetas.keySet()) {
      WorkflowMeta workflowMeta = oldWorkflowMetas.get(workflowMetaKey);
      workflowMetaService.remove(workflowMeta);
      workflowMetaService.flush(workflowMeta);
    }



    // TODO update instead of delete/insert

    String pageFlowString = StringUtils.collectionToDelimitedString(pageFlowOrder, ";");

    // Inserts new UiPageflow fields
    WorkflowMeta pageFlowWorkflowMeta = new WorkflowMeta();
    pageFlowWorkflowMeta.setWorkflowId(workflow.getWorkflowId());
    pageFlowWorkflowMeta.setK("workflow.submitpageflow");
    pageFlowWorkflowMeta.setV(pageFlowString);
    pageFlowWorkflowMeta.setPosition(0);

    workflowMetaService.save(pageFlowWorkflowMeta);

     
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
	      workflowsubtypesampleService.remove(Workflowsubtypesample);
	      workflowsubtypesampleService.flush(Workflowsubtypesample);
	    }
    }

    // the leftovers were not in the db so create
    for (String subtypeSampleIName: safeSet(subtypeSamples)) {
      SubtypeSample subtypeSample = subtypeSampleService.getSubtypeSampleByIName(subtypeSampleIName);
      if (subtypeSample.getSubtypeSampleId() != null){
	      Workflowsubtypesample newWorkflowsubtypesample = new Workflowsubtypesample();
	      newWorkflowsubtypesample.setWorkflowId(workflow.getWorkflowId()); 
	      newWorkflowsubtypesample.setSubtypeSampleId(subtypeSample.getSubtypeSampleId());
	
	      workflowsubtypesampleService.save(newWorkflowsubtypesample);
      } else {
    	  // the specified subtypesample does not exist!!
    	  throw new NullSubtypeSampleException();
      }
    }

  }
}

