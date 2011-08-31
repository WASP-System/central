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

import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.model.*;

@Controller
@Transactional
@RequestMapping("/resource")
public class ResourceController {

  private ResourceService resourceService;
  @Autowired
  public void setResourceService(ResourceService resourceService) {
    this.resourceService = resourceService;
  }
  public ResourceService getResourceService() {
    return this.resourceService;
  }


  @RequestMapping("/list")
  public String list(ModelMap m) {
    List<Resource> resourceList = this.getResourceService().findAll();
    
    m.addAttribute("resource", resourceList);

    return "resource/list";
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

    Resource resource = this.getResourceService().getById(i.intValue());

    List<ResourceMeta> resourceMetaList = resource.getResourceMeta();
    resourceMetaList.size();

    List<ResourceLane> resourceLaneList = resource.getResourceLane();
    resourceLaneList.size();

    List<Run> runList = resource.getRun();
    runList.size();

    // List<ResourceUser> resourceUserList = resource.getResourceUser();
    // resourceUserList.size();

    m.addAttribute("now", now);
    m.addAttribute("resource", resource);
    m.addAttribute("resourcemeta", resourceMetaList);
    m.addAttribute("resourcelane", resourceLaneList);
    m.addAttribute("run", runList);
    // m.addAttribute("resourceuser", resourceUserList);

    return "resource/detail";
  }


}
