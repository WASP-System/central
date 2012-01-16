package edu.yu.einstein.wasp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.util.StringHelper;

@Controller
@Transactional
@RequestMapping("/job")
public class JobController extends WaspController {

  private JobService jobService;
  @Autowired
  public void setJobService(JobService jobService) {
    this.jobService = jobService;
  }
  public JobService getJobService() {
    return this.jobService;
  }

  private JobUserService jobUserService;
  @Autowired
  public void setJobUserService(JobUserService jobUserService) {
    this.jobUserService = jobUserService;
  }
  public JobUserService getJobUserService() {
    return this.jobUserService;
  }

  private RoleService roleService;
  @Autowired
  public void setJobUserService(RoleService roleService) {
    this.roleService = roleService;
  }
  public RoleService getRoleUserService() {
    return this.roleService;
  }

  @Autowired
  private TaskService taskService;
  @Autowired
  private StateService stateService;
  
  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Job> jobList = this.getJobService().findAll();
    
    m.addAttribute("job", jobList);

    return "job/list";
  }

  @RequestMapping(value="/detail/{jobId}", method=RequestMethod.GET)
  public String detail(@PathVariable("jobId") Integer jobId, ModelMap m) {
    String now = (new Date()).toString();


    Job job = this.getJobService().getById(jobId);

    List<JobMeta> jobMetaList = job.getJobMeta();
    jobMetaList.size();

    List<JobSample> jobSampleList = job.getJobSample();
    jobSampleList.size();

    List<JobFile> jobFileList = job.getJobFile();
    jobFileList.size();

    List<JobUser> jobUserList = job.getJobUser();
    jobUserList.size();

    List<Statejob> stateJobList = job.getStatejob();
    stateJobList.size();

    m.addAttribute("now", now);
    m.addAttribute("job", job);
    m.addAttribute("jobmeta", jobMetaList);
    m.addAttribute("jobsample", jobSampleList);
    m.addAttribute("jobfile", jobFileList);
    m.addAttribute("jobuser", jobUserList);
    m.addAttribute("statejob", stateJobList);
 
    return "job/detail";
  }


  @RequestMapping(value="/user/roleAdd", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god') or hasRole('lm-' + #labId) or hasRole('js-' + #jobId)")
  public String jobViewerUserRoleAdd (
      @RequestParam("labId") Integer labId,
      @RequestParam("jobId") Integer jobId,
      @RequestParam("login") String login, //10-11-11 changed from useremail to login, AND 10-20-11 changed login format from jgreally to the AJAX-generated and formatted login of John Greally (jgreally), so must now extract the login from the formatted string 
      ModelMap m) {
 
	Job job = this.jobService.findById(jobId);
	if(job.getJobId() == null || job.getLabId().intValue() != labId.intValue()){
		waspMessage("job.jobViewerUserRoleAdd.error1");//this job not found in database or the labId does not belong to this job
	}
	else{   
		String extractedLogin = StringHelper.getLoginFromFormattedNameAndLogin(login);
		User user = userService.getUserByLogin(extractedLogin);
		if(user.getUserId() == null){
			waspMessage("job.jobViewerUserRoleAdd.error2");//user login name does not exist
		}
		else{
			//check that login does not belong to the job submitter (or is not already a job-viewer)
			JobUser jobUser = this.jobUserService.getJobUserByJobIdUserId(jobId, user.getUserId());
			if(jobUser.getJobUserId() != null){
				if( "js".equals( jobUser.getRole().getRoleName() ) ){
					waspMessage("job.jobViewerUserRoleAdd.error3");//user is submitter (and thus is, by default, a job-viewer)
				}
				else if( "jv".equals( jobUser.getRole().getRoleName() ) ){
					waspMessage("job.jobViewerUserRoleAdd.error4");//user is already a job-viewer
				}
			}
			else{
				Role role = roleService.getRoleByRoleName("jv");
			    JobUser jobUser2 = new JobUser();
			    jobUser2.setJobId(jobId);
			    jobUser2.setUserId(user.getUserId());
			    jobUser2.setRoleId(role.getRoleId());
			    jobUserService.save(jobUser2);
			}
		}
	}

    return "redirect:/job/detail/" + jobId + ".do";
  }


  @RequestMapping(value="/user/roleRemove/{labId}/{jobId}/{userId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
  public String departmentUserRoleRemove (
      @PathVariable("labId") Integer labId,
      @PathVariable("jobId") Integer jobId,
      @PathVariable("userId") Integer userId,
      ModelMap m) {
  
    JobUser jobUser = jobUserService.getJobUserByJobIdUserId(jobId, userId);

    // todo check job within lab
    // check user is just a job viewer

    jobUserService.remove(jobUser);

    return "redirect:/job/detail/" + jobId + ".do";
  }

  @RequestMapping(value = "/pending/detail_ro/{deptId}/{labId}/{jobId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId) or hasRole('lm-' + #labId) or hasRole('pi-' + #labId)")
	public String pendingDetailRO(@PathVariable("deptId") Integer deptId,@PathVariable("labId") Integer labId,
			@PathVariable("jobId") Integer jobId, ModelMap m) {
	  
	  String now = (new Date()).toString();


	    Job job = this.getJobService().getById(jobId);

	    List<JobMeta> jobMetaList = job.getJobMeta();
	    jobMetaList.size();

	    List<JobSample> jobSampleList = job.getJobSample();
	    jobSampleList.size();

	    List<JobFile> jobFileList = job.getJobFile();
	    jobFileList.size();

	    List<JobUser> jobUserList = job.getJobUser();
	    jobUserList.size();

	    List<Statejob> stateJobList = job.getStatejob();
	    stateJobList.size();

	    m.addAttribute("now", now);
	    m.addAttribute("job", job);
	    m.addAttribute("jobmeta", jobMetaList);
	    m.addAttribute("jobsample", jobSampleList);
	    m.addAttribute("jobfile", jobFileList);
	    m.addAttribute("jobuser", jobUserList);
	    m.addAttribute("statejob", stateJobList);
	   // m.addAttribute("actingasrole", actingAsRole);
	    
	    return "job/pendingjob/detail_ro";
  }
  
  @RequestMapping(value = "/pendinglmapproval/{action}/{labId}/{jobId}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('lm-' + #labId) or hasRole('pi-' + #labId)")
	public String pendingLmApproval(@PathVariable("action") String action, @PathVariable("labId") Integer labId, @PathVariable("jobId") Integer jobId, ModelMap m) {
	  
	  pendingJobApproval(action, jobId, "LM");//could use PI instead of LM
	  
	  return "redirect:/lab/pendinglmapproval/list/" + labId + ".do";	
	}
  
  @RequestMapping(value = "/pendingdaapproval/{action}/{deptId}/{jobId}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId)")
	public String pendingDaApproval(@PathVariable("action") String action, @PathVariable("deptId") Integer deptId, @PathVariable("jobId") Integer jobId, ModelMap m) {
	  
	  pendingJobApproval(action, jobId, "DA");//private method below
	  return "redirect:/department/dapendingtasklist.do";		  
	}
 
  private void pendingJobApproval(String action, Integer jobId, String approver){
	  //logger.debug("ROBERT DUBIN : ACTION: " + action);
	  //logger.debug("ROBERT DUBIN : APPROVER: " + approver);
	 // logger.debug("ROBERT DUBIN : Job ID: " + jobId);
	  Task taskOfInterest;
	  boolean updated = false;
	  //confirm action is either approve or reject
	  Job job = jobService.getJobByJobId(jobId);//confirm id > 0
	  if("DA".equals(approver)){
		  taskOfInterest = taskService.getTaskByIName("DA Approval");//confirm id > 0
	  }
	  else if("LM".equals(approver) || "PI".equals(approver)){
		 taskOfInterest = taskService.getTaskByIName("PI Approval");//confirm id > 0
	  }
	  else{
		  taskOfInterest = taskService.getTaskByIName("");//should never get here
		  waspMessage("job.approval.error"); 
		  return;
	  }
	  List<Statejob> statejobList = job.getStatejob();
	  for(Statejob statejob : statejobList){
		  State state = statejob.getState();
		  if(taskOfInterest.getTaskId()==state.getTaskId()){
			  if("approve".equals(action)){
				  state.setStatus("APPROVED");
				  stateService.save(state);
				  //logger.debug("ROBERT DUBIN : State saved to approved for stateId: " + state.getStateId());
				  updated = true; waspMessage("job.approval.approved"); break;
			  }
			  else if("reject".equals(action)){
				  state.setStatus("REJECTED");
				  stateService.save(state); 
				 // logger.debug("ROBERT DUBIN : State saved to rejected for stateId: " + state.getStateId());
				  updated = true; waspMessage("job.approval.rejected"); break;
			  }			 
		  }
	  }
	  
	 // flow returns to calling method, either pendingDaApproval or pendingLmApproval
  }

}
