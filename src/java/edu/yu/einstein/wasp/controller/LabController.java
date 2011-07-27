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

import org.springframework.security.access.prepost.*;

import java.util.Date; 
import java.util.List; 

import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.model.*;

import edu.yu.einstein.wasp.service.LabService;

@Controller
@Transactional
@RequestMapping("/lab")
public class LabController {

  private LabService labService;
  @Autowired
  public void setLabService(LabService labService) {
    this.labService = labService;
  }
  public LabService getLabService() {
    return this.labService;
  }


  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Lab> labList = this.getLabService().findAll();
    
    m.addAttribute("lab", labList);

    return "lab/list";
  }

  @RequestMapping(value="/{strId}/detail", method=RequestMethod.GET)
  // @PreAuthorize("\"8\".equals(\"8\")") // ok
  // @PreAuthorize("\"8\".equals(#str)") // nok - require auth
  // @PreAuthorize("\"8\".equals(\"\" + #str)") // nok - require auth
  // @PreAuthorize("hasRole(\"8\")")  // ok 
  // @PreAuthorize("hasPermission('8', 'LM')")   // ok
  // @PreAuthorize("hasPermission(#strId, 'LM')")  
  @PreAuthorize("hasRole(#str)")   // nok
  public String detail(@PathVariable("strId") String str, ModelMap m) {
    String now = (new Date()).toString();

    Integer i;
    try {
      i = new Integer(str);
    } catch (Exception e) {
      return "default";
    }

    Lab lab = this.getLabService().getById(i.intValue());
    List<Labmeta> labmetaList = lab.getLabmeta();
    labmetaList.size();

    List<LabUser> labUserList = lab.getLabUser();
    labUserList.size();

    List<Job> jobList = lab.getJob();
    jobList.size();

    m.addAttribute("now", now);
    m.addAttribute("lab", lab);
    m.addAttribute("labmeta", labmetaList);
    m.addAttribute("labuser", labUserList);
    m.addAttribute("job", jobList);

    return "lab/detail";
  }
}
