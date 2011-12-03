package edu.yu.einstein.wasp.load;

import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.service.UiFieldService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.WorkflowsubtypesampleService;

import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.model.*;

import java.util.Map; 
import java.util.HashMap; 
import java.util.Set; 
import java.util.List; 
import java.util.Date; 
import java.util.ArrayList; 

import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.*;

import org.springframework.util.StringUtils;

import util.spring.PostInitialize;

@Transactional
public class WorkflowLoadService {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  private UiFieldService uiFieldService;

  @Autowired
  private SubtypeSampleService subtypeSampleService;

  @Autowired
  private WorkflowsubtypesampleService workflowsubtypesampleService;

  @Autowired
  StateService stateService;

  private String iname; 
  public void setIname(String iname) { this.iname = iname; }

  private String name; 
  public void setName(String name) {this.name = name; }

  private List<String> pageFlowOrder; 
  public void setPageFlowOrder(List<String> pageFlowOrder) {this.pageFlowOrder = pageFlowOrder; }

  private List<String> dependencies; 
  public void setDependencies(List<String> dependencies) {this.dependencies = dependencies; }

  private List<UiField> uiFields; 
  public void setUiFields(List<UiField> uiFields) {this.uiFields = uiFields; }

  private Set<String> subtypeSamples;
  public void setSubtypeSamples(Set<String> subtypeSamples) {this.subtypeSamples = subtypeSamples; }

  Workflow workflow;
  public void setWorkflow(Workflow workflow) {this.workflow = workflow; }

  public WorkflowLoadService (){};

  @Transactional
  @PostInitialize 
  public void postInitialize() {
    // skips component scanned  (if scanned in)
    if (iname == null) { return; }



    workflow = workflowService.getWorkflowByIName(iname); 

    // inserts or update workflow
    if (workflow.getWorkflowId() == 0) { 
      workflow = new Workflow();

      workflow.setIName(iname);
      workflow.setName(name);
      workflow.setCreatets(new Date());

      workflowService.save(workflow); 

      // refreshes
      workflow = workflowService.getWorkflowByIName(iname); 

    } else {
      workflow.setName(name);

      workflowService.save(workflow); 
    }


    // UI fields
    // this assumes truncate to start with, so clear everything out
    // and use this.uiFields
    // TODO: logic to do CRUD compares instead 
    Map m = new HashMap();
    m.put("locale", "en_US");
    m.put("area", iname);
    List<UiField> oldUiFields = uiFieldService.findByMap(m);
// System.out.println("*******************************\n");
    for (UiField uiField: oldUiFields) {
// System.out.println("----------- " + uiField.getUiFieldId() +" \n");
      uiFieldService.remove(uiField); 
      uiFieldService.flush(uiField); 
    }

    // sets up the new
    for (UiField uiField: uiFields) {
      // skips pageflow
      if (uiField.getName().equals("pageflow")) { continue; }
      uiFieldService.save(uiField); 
    }


    String pageFlowString = StringUtils.collectionToDelimitedString(pageFlowOrder, ";");

    /*
    // old UiPageflow fields
    m = new HashMap();
    m.put("locale", "en_US");
    m.put("area", iname);
    m.put("name", "pageflow");
    List<UiField> uiFields = uiFieldService.findByMap(m);

    boolean pageFlowFound = false;
    for (UiField uiField: uiFields) {
      if (uiField.getAttrName().equals("")) {
        pageFlowFound = true; 
        if (! uiField.getAttrValue().equals(pageFlowString)) {
          // System.out.println("\n\n\nFOUND and changing\n\n\n");
          uiField.setAttrValue(pageFlowString); 
          uiFieldService.save(uiField);
        }
        continue;
      }
      uiFieldService.remove(uiField);
    }

    if (! pageFlowFound) {
    */

      // Inserts new UiPageflow fields
      UiField pageFlowUiField = new UiField();
      pageFlowUiField.setLocale("en_Us");
      pageFlowUiField.setArea(iname);
      pageFlowUiField.setName("pageflow");
      pageFlowUiField.setAttrName("");
      pageFlowUiField.setAttrValue(pageFlowString);

      uiFieldService.save(pageFlowUiField);

    /*
    }
    */
     
    // allowable subtype samples
    // - pickup existing
    m = new HashMap();
    m.put("workflowId", workflow.getWorkflowId());
    List<Workflowsubtypesample> oldWorkflowSubtypeSamples = workflow.getWorkflowsubtypesample();

    // todo better logic so updates.
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
    }

    // the leftovers were not in the db so create
    for (String subtypeSampleIName: subtypeSamples) {
      SubtypeSample subtypeSample = subtypeSampleService.getSubtypeSampleByIName(subtypeSampleIName);     
      Workflowsubtypesample newWorkflowsubtypesample = new Workflowsubtypesample();
      newWorkflowsubtypesample.setWorkflowId(workflow.getWorkflowId()); 
      newWorkflowsubtypesample.setSubtypeSampleId(subtypeSample.getSubtypeSampleId());

      workflowsubtypesampleService.save(newWorkflowsubtypesample);
    }



     



    System.out.println("\n\n\n\n\nZZZ hello worl "  + pageFlowString +  " \n\n\n\n\n");
  }

}

