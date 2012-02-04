package edu.yu.einstein.wasp.load;

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
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
import edu.yu.einstein.wasp.model.Workflowtyperesource;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.service.WorkflowMetaService;
import edu.yu.einstein.wasp.service.WorkflowService;
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

  @Override
  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (iname == null) { return; }

    Workflow workflow = workflowService.getWorkflowByIName(iname); 

    // inserts or update workflow
    if (workflow.getWorkflowId() == null) { 
      workflow = new Workflow();

      workflow.setIName(iname);
      workflow.setName(name);
      workflow.setIsActive(0); // only set to active when configured by admin
      workflow.setCreatets(new Date());

      workflowService.save(workflow); 

      // refreshes
      workflow = workflowService.getWorkflowByIName(iname); 

    } else {
      workflow.setName(name);

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



    // update dependencies
    // TODO: insert/update instead of delete insert
    List<Workflowtyperesource> oldWorkflowtyperesources = workflow.getWorkflowtyperesource();
    if (oldWorkflowtyperesources != null) {
      for (Workflowtyperesource oldWorkflowtyperesource: oldWorkflowtyperesources) {
        workflowtyperesourceService.remove(oldWorkflowtyperesource);
        workflowtyperesourceService.flush(oldWorkflowtyperesource);
      }
    }

    for (String dependency: safeList(dependencies)) { 
      TypeResource typeResource = typeResourceService.getTypeResourceByIName(dependency);
      if (typeResource.getTypeResourceId() != null){
	      Workflowtyperesource workflowtyperesource = new Workflowtyperesource(); 
	
	      workflowtyperesource.setWorkflowId(workflow.getWorkflowId()); 
	      workflowtyperesource.setTypeResourceId(typeResource.getTypeResourceId()); 
	
	      workflowtyperesourceService.save(workflowtyperesource);
      } else {
    	// the specified resourceType does not exist!!
    	  throw new NullTypeResourceException();
      }
    }

  }
}

