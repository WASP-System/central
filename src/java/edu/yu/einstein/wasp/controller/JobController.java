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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.transaction.annotation.*; 

import java.util.Date; 
import java.util.List; 

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/job")
public class JobController {

  private JobDao jobDao;
  @Autowired
  public void setJobDao(JobDao jobDao) {
    this.jobDao = jobDao;
  }
  public JobDao getJobDao() {
    return this.jobDao;
  }

  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Job> jobList = this.getJobDao().findAll();
    
    m.addAttribute("job", jobList);

    return "job/list";
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

    Job job = this.getJobDao().getById(i.intValue());

    List<JobMeta> jobMetaList = job.getJobMeta();
    jobMetaList.size();

    List<JobSample> jobSampleList = job.getJobSample();
    jobSampleList.size();

    List<JobFile> jobFileList = job.getJobFile();
    jobFileList.size();

    List<Statejob> stateJobList = job.getStatejob();
    stateJobList.size();

    m.addAttribute("now", now);
    m.addAttribute("job", job);
    m.addAttribute("jobmeta", jobMetaList);
    m.addAttribute("jobsample", jobSampleList);
    m.addAttribute("jobfile", jobFileList);
    m.addAttribute("statejob", stateJobList);

    return "job/detail";
  }


}
