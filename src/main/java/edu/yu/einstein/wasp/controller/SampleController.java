package edu.yu.einstein.wasp.controller;

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

import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/sample")
public class SampleController extends WaspController {

  private SampleService sampleService;
  @Autowired
  public void setSampleService(SampleService sampleService) {
    this.sampleService = sampleService;
  }
  public SampleService getSampleService() {
    return this.sampleService;
  }


  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Sample> sampleList = this.getSampleService().findAll();
    
    m.addAttribute("sample", sampleList);

    return "sample/list";
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

    Sample sample = this.getSampleService().getById(i.intValue());

    List<SampleMeta> sampleMetaList = sample.getSampleMeta();
    sampleMetaList.size();

    List<JobSample> jobSampleList = sample.getJobSample();
    jobSampleList.size();

    List<SampleFile> sampleFileList = sample.getSampleFile();
    sampleFileList.size();

    List<SampleSource> parentSampleList = sample.getSampleSource();
    parentSampleList.size();

    List<SampleSource> childSampleList = sample.getSampleSourceViaSourceSampleId();
    childSampleList.size();

    m.addAttribute("now", now);
    m.addAttribute("sample", sample);
    m.addAttribute("samplemeta", sampleMetaList);
    m.addAttribute("jobsample", jobSampleList);
    m.addAttribute("samplefile", sampleFileList);
    m.addAttribute("parentsample", parentSampleList);
    m.addAttribute("childsample", childSampleList);

    return "sample/detail";
  }


}
