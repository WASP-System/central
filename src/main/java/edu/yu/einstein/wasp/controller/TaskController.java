package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.StateMeta;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StateMetaService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatesampleService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

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

  @Autowired
  private MessageService messageService;
  
  @Autowired
  private StateMetaService stateMetaService;

  @Autowired
  private StatesampleService stateSampleService;
  
  @Autowired
  private SampleService sampleService;
  
  @Autowired
  private AuthenticationService authenticationService;
  
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

  @RequestMapping(value = "/lmapproval/list/{labId}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('lm-' + #labId)")
  public String listLabManagerApproval(@PathVariable("labId") Integer labId, ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("PI Approval");

    HashMap map = new HashMap();
    map.put("taskId", task.getTaskId()); 
    map.put("status", "RUNNING");
    List<State> rawStates = stateService.findByMap(map);

    ArrayList<State> states = new ArrayList();
    for (State state:rawStates) {
      List<Statejob> stateJob = state.getStatejob();
      int stateLabId = stateJob.get(0).getJob().getLabId(); 

      if (stateLabId == labId.intValue()) {
        states.add(state);
      }
    }

    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/lmapproval/list";
  }

  @RequestMapping(value = "/lmapproval/{labId}/{stateId}/{newStatus}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('lm-' + #labId)")
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
  @PreAuthorize("hasRole('su') or hasRole('da-' + #departmentId)")
  public String listDepartementAdminApproval(@PathVariable("departmentId") Integer departmentId, ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("DA Approval");

    HashMap map = new HashMap();
    map.put("taskId", task.getTaskId()); 
    map.put("status", "RUNNING");
    List<State> rawStates = stateService.findByMap(map);

    ArrayList<State> states = new ArrayList();
    for (State state:rawStates) {
      List<Statejob> stateJob = state.getStatejob();
      int stateDeptId = stateJob.get(0).getJob().getLab().getDepartmentId(); 

      if (stateDeptId == departmentId.intValue()) {
        states.add(state);
      }
    }

    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/daapproval/list";
  }

  @RequestMapping(value = "/daapproval/{departmentId}/{stateId}/{newStatus}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('da-' + #departmentId)")
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
  @PreAuthorize("hasRole('su') or hasRole('fm')")
  public String listRequote(ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("Requote");
    List<State> states = task.getState();

    // TODO filter by status
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/fmrequote/list";
  }

  @RequestMapping(value = "/fmrequote/requote", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm')")
  public String requote(
      @RequestParam("stateId") Integer stateId,
      @RequestParam("jobId") Integer jobId,
      @RequestParam("amount") Integer amount,
      ModelMap m
    ) {

    State state = stateService.getStateByStateId(stateId);

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
  @PreAuthorize("hasRole('su') or hasRole('fm')")
  public String showJqPaymentListShell(ModelMap m) {
	
		MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp("fmpayment", "state", StateMeta.class, request.getSession());

    m.addAttribute("_metaList", metaHelperWebapp.getMasterList(StateMeta.class));
    m.addAttribute(JQFieldTag.AREA_ATTR, "fmpayment");

    return "task/fmpayment/list";
  }

	@RequestMapping(value="/fmpayment/listJson.do", method=RequestMethod.GET)
	public @ResponseBody String getListJson() {

		MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp("fmpayment", "state", StateMeta.class,request.getSession());

		List<State> stateList;

    Task task = this.getTaskService().getTaskByIName("Receive Payment");

		Map stateListBaseQueryMap = new HashMap();
		stateListBaseQueryMap.put("taskId", task.getTaskId()); 
		stateListBaseQueryMap.put("status", "WAITING");

		stateList = stateService.findByMap(stateListBaseQueryMap);

		Map <String, Object> jqgrid = new HashMap<String, Object>();
		jqgrid.put("page","1");
		jqgrid.put("records", stateList.size()+"");
		jqgrid.put("total", stateList.size()+"");

		Map<String, String> stateData=new HashMap<String, String>();
		stateData.put("page","1");
		stateData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));

 		jqgrid.put("statedata",stateData);

		List<Map> rows = new ArrayList<Map>();

		for (State state: stateList) {
			Map cell = new HashMap();
			cell.put("id", state.getStateId());

			List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
				state.getName(),
				state.getStatus()
			}));

			List<StateMeta> stateMetaList=metaHelperWebapp.syncWithMaster(state.getStateMeta());
      for(StateMeta meta:stateMetaList) {
				cellList.add(meta.getV());
			}

			cell.put("cell", cellList);

      rows.add(cell);
		}

		jqgrid.put("rows",rows);


		ObjectMapper mapper = new ObjectMapper();
		try {
			String json=mapper.writeValueAsString(jqgrid);
			return json;
		} catch (Exception e) {
			throw new IllegalStateException("Can't marshall to JSON "+stateList,e);
		}

	}

        @RequestMapping(value="/fmpayment/updateJson.do", method=RequestMethod.POST)
        public String updateJson(
                        @RequestParam("id") Integer stateId,
                        @Valid State stateForm,
                        ModelMap m,
                        HttpServletResponse response) {
		
MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp("fmpayment", "state", StateMeta.class,request.getSession());

								State stateDb = stateService.getStateByStateId(stateId);

                List<StateMeta> stateMetaList = metaHelperWebapp.getFromJsonForm(request, StateMeta.class);
                stateForm.setStateMeta(stateMetaList);
                stateForm.setStateId(stateId);
                stateForm.setTaskId(stateDb.getTaskId());
                stateForm.setStatus("PAID?");

								stateDb = stateService.merge(stateForm);
                stateMetaService.updateByStateId(stateDb.getStateId(), stateForm.getStateMeta());

    // TODO jobId belongs to stateId
    // TODO check valid state
    // TODO insert payment
    // TODO email LM/PI/DA/submitter
    //   


                try {
                        response.getWriter().println(messageService.getMessage("hello.error"));
                        return null;
                } catch (Throwable e) {
                        throw new IllegalStateException("Cant output success message ",e);
                }
	}



  @RequestMapping(value = "/fmpayment/oldlist", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm')")
  public String listPayment(ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("Receive Payment");

    HashMap map = new HashMap();
    map.put("taskId", task.getTaskId()); 
    map.put("status", "WAITING");
    List<State> states = stateService.findByMap(map);
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/fmpayment/list";
  }

  @RequestMapping(value = "/fmpayment/payment", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm')")
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
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String listSampleReceive(ModelMap m) {

    Task task = this.getTaskService().getTaskByIName("Receive Sample");
    List<State> states_temp = task.getState();

    List<State> states = new ArrayList<State>();
    // TODO filter by status
    //TODO should really filter to restrict to jobs that are NOT completed (if some samples are never going to arrive, they'll always be listed)
    for(State state : states_temp){
    	if(state.getStatus().equals("WAITING")){
    		states.add(state);
    	}
    }
    //sort list states by jobId
    Comparator<State> stateComparator = new OrderStatesByJobIdComparator();
    Collections.sort(states, stateComparator);
    
    m.addAttribute("task", task);
    m.addAttribute("states", states);

    return "task/samplereceive/list";
  }

  @RequestMapping(value = "/samplereceive/receive", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String payment(
      @RequestParam("stateId") Integer stateId,
      @RequestParam("sampleId") Integer sampleId,
      @RequestParam("receivedStatus") String receivedStatus,
      ModelMap m
    ) {

	  if(receivedStatus == null ||  receivedStatus.equals("")){
		  waspMessage("task.samplereceive.error_receivedstatus_empty");
	  }
	  else if(!receivedStatus.equals("RECEIVED") && !receivedStatus.equals("NEVER COMING")){
		  waspMessage("task.samplereceive.error_receivedstatus_invalid");
	  }
	  else{
		  Map map = new HashMap();
	  	  map.put("sampleId", sampleId);
	  	  List<Statesample> statesamples = this.stateSampleService.findByMap(map);
	  	  boolean valid = false;
	  	  for(Statesample ss : statesamples){
	  		  if(ss.getStateId().intValue() == stateId.intValue()){
	  			  valid = true;
	  			  break;
	  		  }
	  	  }
	  	  if(!valid){
	  		  waspMessage("task.samplereceive.error_state_sample_conflict");
	  	  }
	  	  else{
	  		  State state = this.getStateService().getStateByStateId(stateId);
	  		  Task task = this.getTaskService().getTaskByIName("Receive Sample");
	  		  if(state.getTaskId().intValue() != task.getTaskId().intValue()){
	  			  waspMessage("task.samplereceive.error_state_task_conflict");
	  		  }
	  		  else{
	  			  state.setStatus(receivedStatus);  
	  			  stateService.save(state);
	  			  Sample sample = this.sampleService.getSampleBySampleId(sampleId);
	  			  sample.setIsReceived(1);
	  			  sample.setReceiverUserId(authenticationService.getAuthenticatedUser().getUserId());	  			  
	  			  sample.setReceiveDts(new Date());
	  			  sampleService.save(sample);
	  			  waspMessage("task.samplereceive.update_success");
	  			  //email LM/PI/DA/submitter  //Not really necessary
	  		  }
	  	  }
	  }
	  return "redirect:/task/samplereceive/list.do";
  }

  	public class OrderStatesByJobIdComparator implements Comparator<State> {
		 
  		@Override
  		public int compare(State s1, State s2) {

  			try{
  			if(s1.getStatejob().get(0).getJobId().intValue() > s2.getStatejob().get(0).getJobId().intValue()){
  				return 1;
  			}
  			else if(s1.getStatejob().get(0).getJobId().intValue() == s2.getStatejob().get(0).getJobId().intValue()){
  				return 0;
  			}
  			else{
  				return -1;
  			}
  			}
  			catch(Exception e){
  				System.out.println("1. My Comparator Exception: ");// + s1.getStatejob().get(0).getJobId().intValue() + "  and " + s2.getStatejob().get(0).getJobId().intValue());
  				return 0;
  			}
  		}
	}
  
  
  
}

