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

import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunLaneDao;
import edu.yu.einstein.wasp.dao.impl.RunDaoImpl;
import edu.yu.einstein.wasp.dao.impl.RunLaneDaoImpl;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/run")
public class RunController {

  private RunDao runDao;
  @Autowired
  public void setRunDao(RunDao runDao) {
    this.runDao = runDao;
  }
  public RunDao getRunDao() {
    return this.runDao;
  }

  private RunLaneDao runLaneDao;
  @Autowired
  public void setRunLaneDao(RunLaneDao runLaneDao) {
    this.runLaneDao = runLaneDao;
  }
  public RunLaneDao getRunLaneDao() {
    return this.runLaneDao;
  }


  @RequestMapping("/list")
  public String list(ModelMap m) {
    List<Run> runList = this.getRunDao().findAll();
    
    m.addAttribute("run", runList);

    return "run/list";
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

    Run run = this.getRunDao().getById(i.intValue());

    List<RunMeta> runMetaList = run.getRunMeta();
    runMetaList.size();

    List<RunLane> runLaneList = run.getRunLane();
    runLaneList.size();

    List<RunFile> runFileList = run.getRunFile();
    runFileList.size();

    m.addAttribute("now", now);
    m.addAttribute("run", run);
    m.addAttribute("runmeta", runMetaList);
    m.addAttribute("runlane", runLaneList);
    m.addAttribute("runfile", runFileList);

    return "run/detail";
  }

  @RequestMapping(value="/lane/detail/{strRunId}/{strId}", method=RequestMethod.GET)
  public String laneDetail(@PathVariable("strRunId") String strRunId, @PathVariable("strId") String strId, ModelMap m) {
    String now = (new Date()).toString();

    Integer i;
    try {
      i = new Integer(strId);
    } catch (Exception e) {
      return "default";
    }

    Integer runId;
    try {
      runId = new Integer(strRunId);
    } catch (Exception e) {
      return "default";
    }

    RunLane runLane = this.getRunLaneDao().getById(i.intValue());

    //
    // TODO THROW EXCEPTION IF RUNID != RUNLANE.RUNID
    //

    List<RunLanefile> runLaneFileList = runLane.getRunLanefile();
    runLaneFileList.size();

    m.addAttribute("now", now);
    m.addAttribute("runlane", runLane);
    m.addAttribute("runlanefile", runLaneFileList);

    return "run/lanedetail";
  }


}
