package edu.yu.einstein.wasp.controller;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.transaction.annotation.*; 

import org.springframework.security.access.prepost.*;

import java.util.Date; 
import java.util.List; 

import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/task")
public class TaskController extends WaspController {

  private TaskService taskService;
  @Autowired
  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }
  public TaskService getTaskService() {
    return this.taskService;
  }

  private StateService stateService;
  @Autowired
  public void setStateService(StateService stateService) {
    this.stateService = stateService;
  }
  public StateService getStateService() {
    return this.stateService;
  }



  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Task> taskList = this.getTaskService().findAll();
    
    m.addAttribute("task", taskList);

    return "task/list";
  }

  @RequestMapping(value="/detail/{strId}", method=RequestMethod.GET)
  public String detail(@PathVariable("strId") String strId, ModelMap m) {
    String now = (new Date()).toString();

    Integer i;
    try {
      i = new Integer(strId);
    } catch (Exception e) {
      return "default";
    }

    Task task = this.getTaskService().getById(i.intValue());

    List<State> stateList = task.getState();
    stateList.size();

    m.addAttribute("now", now);
    m.addAttribute("task", task);
    m.addAttribute("state", stateList);

    return "task/detail";
  }

  @RequestMapping("/lmapproval/list/{labId}")
  @PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
  public String listLabManagerApproval(@PathVariable("labId") Integer labId, ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("PI Approval");
    List<State> states = task.getState();

    // TODO LIMIT BY LABID
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/lmapproval/list";
  }

  @RequestMapping(value = "/lmapproval/{labId}/{stateId}/{newStatus}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
  public String changeLabStateStatus ( @PathVariable("labId") Integer labId, @PathVariable("stateId") Integer stateId, @PathVariable("newStatus") String newStatus, ModelMap m) {

    // TODO CHECK STATUS
    // TODO CHECK STATE IN LAB
    // TODO ADD MESSAGE

    State state = this.getStateService().getStateByStateId(stateId);
    state.setStatus(newStatus);
    this.getStateService().merge(state);

    return "redirect:/task/lmapproval/list/" + labId + ".do";
  }

  @RequestMapping("/daapproval/list/{departmentId}")
  @PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
  public String listDepartementAdminApproval(@PathVariable("departmentId") Integer departmentId, ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("DA Approval");
    List<State> states = task.getState();

    // TODO filter by departmentId
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/daapproval/list";
  }

  @RequestMapping(value = "/daapproval/{departmentId}/{stateId}/{newStatus}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
  public String changeDepartmentStateStatus ( @PathVariable("departmentId") Integer departmentId, @PathVariable("stateId") Integer stateId, @PathVariable("newStatus") String newStatus, ModelMap m) {

    // TODO CHECK STATUS
    // TODO CHECK STATE IN LAB
    // TODO ADD MESSAGE

    State state = this.getStateService().getStateByStateId(stateId);
    state.setStatus(newStatus);
    this.getStateService().merge(state);

    return "redirect:/task/daapproval/list/" + departmentId + ".do";
  }

  @RequestMapping(value = "/fmrequote/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('fm')")
  public String listRequote(ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("Requote");
    List<State> states = task.getState();

    // TODO filter by status
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/fmrequote/list";
  }

  @RequestMapping(value = "/fmrequote/requote", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('fm')")
  public String requote(
      @RequestParam("stateId") Integer stateId,
      @RequestParam("jobId") Integer jobId,
      @RequestParam("amount") Integer amount,
      ModelMap m
    ) {

    // TODO jobId belongs to stateId
    // TODO check valid state
    // TODO invalidate old quote
    // TODO insert new quote
    // TODO invalidate old PI/DA approvals
    // TODO email LM/PI/DA/submitter


    // TODO add status message

    return "redirect:/task/fmrequote/list.do";
  }

  @RequestMapping(value = "/fmpayment/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('fm')")
  public String listPayment(ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("Receive Payment");
    List<State> states = task.getState();

    // TODO filter by status
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/fmpayment/list";
  }

  @RequestMapping(value = "/fmpayment/payment", method = RequestMethod.POST)
  @PreAuthorize("hasRole('god') or hasRole('fm')")
  public String payment(
      @RequestParam("stateId") Integer stateId,
      @RequestParam("jobId") Integer jobId,
      @RequestParam("amount") Integer amount,
      ModelMap m
    ) {

    // TODO jobId belongs to stateId
    // TODO check valid state
    // TODO insert payment
    // TODO email LM/PI/DA/submitter
    //   


    // TODO add status message

    return "redirect:/task/fmpayment/list.do";
  }

  @RequestMapping(value = "/samplereceive/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('fm')")
  public String listSampleReceive(ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("Receive Sample");
    List<State> states = task.getState();

    // TODO filter by status
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/samplereceive/list";
  }

  @RequestMapping(value = "/samplereceive/receive", method = RequestMethod.POST)
  @PreAuthorize("hasRole('god') or hasRole('fm')")
  public String payment(
      @RequestParam("stateId") Integer stateId,
      @RequestParam("sampleId") Integer sampleId,
      ModelMap m
    ) {

    // TODO jobId belongs to stateId
    // TODO check valid state
    // TODO email LM/PI/DA/submitter
    //   


    // TODO add status message

    return "redirect:/task/samplereceive/list.do";
  }

}
