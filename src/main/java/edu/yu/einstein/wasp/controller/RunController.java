package edu.yu.einstein.wasp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.model.RunLane;
import edu.yu.einstein.wasp.model.RunLanefile;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.service.RunLaneService;
import edu.yu.einstein.wasp.service.RunService;

@Controller
@Transactional
@RequestMapping("/run")
public class RunController {

  private RunService runService;
  @Autowired
  public void setRunService(RunService runService) {
    this.runService = runService;
  }
  public RunService getRunService() {
    return this.runService;
  }

  private RunLaneService runLaneService;
  @Autowired
  public void setRunLaneService(RunLaneService runLaneService) {
    this.runLaneService = runLaneService;
  }
  public RunLaneService getRunLaneService() {
    return this.runLaneService;
  }


  @RequestMapping("/list")
  public String list(ModelMap m) {
    List<Run> runList = this.getRunService().findAll();
    
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

    Run run = this.getRunService().getById(i.intValue());

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

    RunLane runLane = this.getRunLaneService().getById(i.intValue());

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
