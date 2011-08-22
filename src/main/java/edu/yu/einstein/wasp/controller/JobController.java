package edu.yu.einstein.wasp.controller;


import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.transaction.annotation.*; 

import java.util.Date; 
import java.util.List; 

import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.model.*;

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
  @PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
  public String jobViewerUserRoleAdd (
      @RequestParam("labId") Integer labId,
      @RequestParam("jobId") Integer jobId,
      @RequestParam("useremail") String useremail,
      ModelMap m) {
 
    // todo ensure usermail exists
    // todo jobid within labid
    // todo not already job submitter

    Role role = roleService.getRoleByRoleName("jv");

    User user = userService.getUserByEmail(useremail);
    JobUser jobUser = new JobUser();
    jobUser.setJobId(jobId);
    jobUser.setUserId(user.getUserId());
    jobUser.setRoleId(role.getRoleId());
    jobUserService.save(jobUser);

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




}
