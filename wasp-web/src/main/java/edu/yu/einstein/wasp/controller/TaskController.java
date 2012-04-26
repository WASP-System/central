package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

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
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StateMetaDao;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.StateMeta;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/task")
public class TaskController extends WaspController {

  private TaskDao taskDao;
  @Autowired
  public void setTaskDao(TaskDao taskDao) {
    this.taskDao = taskDao;
  }
  public TaskDao getTaskDao() {
    return this.taskDao;
  }

  private StateDao stateDao;
  @Autowired
  public void setStateDao(StateDao stateDao) {
    this.stateDao = stateDao;
  }
  public StateDao getStateDao() {
    return this.stateDao;
  }

  @Autowired
  private MessageService messageService;
  
  @Autowired
  private StateMetaDao stateMetaDao;

  @Autowired
  private StatesampleDao stateSampleDao;
  
  @Autowired
  private SampleDao sampleDao;
  
  @Autowired
  private JobDao jobDao;
  
  @Autowired
  private JobSampleDao jobSampleDao;
  
  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private JobService jobService;

  @Autowired
  private SampleService sampleService;
  
  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Task> taskList = this.getTaskDao().findAll();
    
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

    Task task = this.getTaskDao().getById(i.intValue());

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

    Task task = this.getTaskDao().getTaskByIName("PI Approval");

    HashMap map = new HashMap();
    map.put("taskId", task.getTaskId()); 
    map.put("status", "RUNNING");
    List<State> rawStates = stateDao.findByMap(map);

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

    State state = this.getStateDao().getStateByStateId(stateId);
    state.setStatus(newStatus);
    this.getStateDao().merge(state);

    return "redirect:/task/lmapproval/list/" + labId + ".do";
  }

  @RequestMapping("/daapproval/list/{departmentId}")
  @PreAuthorize("hasRole('su') or hasRole('da-' + #departmentId)")
  public String listDepartementAdminApproval(@PathVariable("departmentId") Integer departmentId, ModelMap m) {

    Task task = this.getTaskDao().getTaskByIName("DA Approval");

    HashMap map = new HashMap();
    map.put("taskId", task.getTaskId()); 
    map.put("status", "RUNNING");
    List<State> rawStates = stateDao.findByMap(map);

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

    State state = this.getStateDao().getStateByStateId(stateId);
    state.setStatus(newStatus);
    this.getStateDao().merge(state);

    return "redirect:/task/daapproval/list/" + departmentId + ".do";
  }

  @RequestMapping(value = "/fmrequote/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm')")
  public String listRequote(ModelMap m) {

    Task task = this.getTaskDao().getTaskByIName("Requote");
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

    State state = stateDao.getStateByStateId(stateId);

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

    Task task = this.getTaskDao().getTaskByIName("Receive Payment");

		Map stateListBaseQueryMap = new HashMap();
		stateListBaseQueryMap.put("taskId", task.getTaskId()); 
		stateListBaseQueryMap.put("status", "WAITING");

		stateList = stateDao.findByMap(stateListBaseQueryMap);

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

								State stateDb = stateDao.getStateByStateId(stateId);

                List<StateMeta> stateMetaList = metaHelperWebapp.getFromJsonForm(request, StateMeta.class);
                stateForm.setStateMeta(stateMetaList);
                stateForm.setStateId(stateId);
                stateForm.setTaskId(stateDb.getTaskId());
                stateForm.setStatus("PAID?");

								stateDb = stateDao.merge(stateForm);
                stateMetaDao.updateByStateId(stateDb.getStateId(), stateForm.getStateMeta());

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

    Task task = this.getTaskDao().getTaskByIName("Receive Payment");

    HashMap map = new HashMap();
    map.put("taskId", task.getTaskId()); 
    map.put("status", "WAITING");
    List<State> states = stateDao.findByMap(map);
    
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

    List<Job> jobsAwaitingSubmittedSamples = jobService.getJobsAwaitingSubmittedSamples();
    List<Job> jobsActive = jobService.getActiveJobs();
    
    List<Job> jobsActiveAndAwaitingSubmittedSamples = new ArrayList<Job>();
    for(Job jobActive : jobsActive){
    	for(Job jobAwaiting : jobsAwaitingSubmittedSamples){
    		if(jobActive.getJobId().intValue()==jobAwaiting.getJobId().intValue()){
    			jobsActiveAndAwaitingSubmittedSamples.add(jobActive);
    			break;
    		}
    	}
    }
    jobService.sortJobsByJobId(jobsActiveAndAwaitingSubmittedSamples);

    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingSubmittedSamples){
    	List<Sample> newSampleList = jobService.getSubmittedSamplesAwaitingSubmission(job);
    	sampleService.sortSamplesBySampleName(newSampleList);    	
    	jobAndSampleMap.put(job, newSampleList);
    }
    
    if(jobsActiveAndAwaitingSubmittedSamples.size() != jobAndSampleMap.size()){
    	//message and get out of here
    }
    
    m.addAttribute("jobList", jobsActiveAndAwaitingSubmittedSamples);
    m.addAttribute("jobAndSamplesMap", jobAndSampleMap);
    
    return "task/samplereceive/list";
  }

  @RequestMapping(value = "/samplereceive/receive", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String payment(
      @RequestParam("sampleId") Integer sampleId,
      @RequestParam("receivedStatus") String receivedStatus,
      ModelMap m
    ) {

	  if(receivedStatus == null ||  receivedStatus.equals("")){
		  waspErrorMessage("task.samplereceive.error_receivedstatus_empty");
	  }
	  else if(!receivedStatus.equals("RECEIVED") && !receivedStatus.equals("WITHDRAWN")){
		  waspErrorMessage("task.samplereceive.error_receivedstatus_invalid");
	  }
	  Sample sample = sampleDao.getSampleBySampleId(sampleId);
	  if(sample.getSampleId().intValue()==0){
		  //message can't find sample in db
	  }
	  else{
		  sampleService.updateSampleReceiveStatus(sample, receivedStatus);
		  waspMessage("task.samplereceive.update_success");	
	  	 }
	  return "redirect:/task/samplereceive/list.do";
  }

  
  @RequestMapping(value = "/updatesamplereceive/{jobId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String updateSampleReceive(@PathVariable("jobId") Integer jobId, ModelMap m) {

	  Job job = jobDao.getJobByJobId(jobId);
	  if(job.getJobId()==0){
		  //message can't find job by id
		  return "redirect:/dashboard.do";
	  }
	  
	  //getSubmittedSamples(job) returns list of all samples (macromolecules and user-generated libraries) 
	  //that were submitted for a particular job and it does NOT include facility-generated libraries.
	  List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
	  //order by sample name
	  sampleService.sortSamplesBySampleName(submittedSamplesList);
	  
	  List<String> receiveSampleStatusList = new ArrayList<String>();
	  List<Boolean> sampleHasBeenProcessedList = new ArrayList<Boolean>();
	  for(Sample sample : submittedSamplesList){	
		  String status = sampleService.getReceiveSampleStatus(sample);		  
		  receiveSampleStatusList.add(sampleService.convertReceiveSampleStatusForWeb(status));	
		  
		  boolean sampleHasBeenProcessedByFacility = sampleService.submittedSampleHasBeenProcessedByFacility(sample);
		  sampleHasBeenProcessedList.add(new Boolean(sampleHasBeenProcessedByFacility));
	  }
	  
	  if(submittedSamplesList.size() != receiveSampleStatusList.size() && receiveSampleStatusList.size() != sampleHasBeenProcessedList.size()){
		  //message
		  return "redirect:/dashboard.do";
	  }
	  
	  
	  //list of options for the SELECT box on the web page
	  List<String> receiveSampleOptionsList = sampleService.getReceiveSampleStatusOptionsForWeb();
	  
	  m.addAttribute("job", job);
	  m.addAttribute("submittedSamplesList", submittedSamplesList);
	  m.addAttribute("receiveSampleStatusList", receiveSampleStatusList);
	  m.addAttribute("sampleHasBeenProcessedList", sampleHasBeenProcessedList);
	  m.addAttribute("receiveSampleOptionsList", receiveSampleOptionsList);	  
	  
	  return "task/samplereceive/updateSampleReceive";
  }

  @RequestMapping(value = "/updatesamplereceive", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String updateSampleReceive(@RequestParam("jobId") Integer jobId, 
		  @RequestParam("sampleId") List<Integer> sampleIdList,
	      @RequestParam("receivedStatus") List<String> receivedStatusList,
	      ModelMap m) {

	  Job job = jobDao.getJobByJobId(jobId);
	  if(job.getJobId()==0){
		  //message can't find job by id
		  return "redirect:/dashboard.do";
	  }
	  if(sampleIdList.size() != receivedStatusList.size()){
		  //message
		  return "redirect:/dashboard.do";
	  }
	  int index = 0;
	  for(Integer sampleId : sampleIdList){		  
		  Sample sample = sampleDao.getSampleBySampleId(sampleId);
		  if(sample.getSampleId() > 0 && ! sampleService.submittedSampleHasBeenProcessedByFacility(sample)){
			  sampleService.updateSampleReceiveStatus(sample, receivedStatusList.get(index++));
		  }
	  }
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  
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

