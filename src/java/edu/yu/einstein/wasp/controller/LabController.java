package edu.yu.einstein.wasp.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Labmeta;
import edu.yu.einstein.wasp.model.MetaProperty;
import edu.yu.einstein.wasp.model.MetaProperty.Country;
import edu.yu.einstein.wasp.model.MetaProperty.State;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.LabmetaService;
import edu.yu.einstein.wasp.service.UserService;

@Controller
@Transactional
@RequestMapping("/lab")
public class LabController {

  public static final String AREA="lab";
	
  @Autowired
  private LabService labService;
  
  @Autowired
  HttpServletRequest request;
  
  @Autowired
  private LabmetaService labmetaService;
  
  @Autowired
  private DepartmentService deptService;

  
  @Autowired
  private UserService userService;
  
  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <Lab> labList = this.labService.findAll();
    
    m.addAttribute("lab", labList);

    return "lab/list";
  }

  
  @RequestMapping(value="/create/form.do", method=RequestMethod.GET)
  public String showEmptyForm(ModelMap m) {
	  
    String now = (new Date()).toString();
    
    Lab lab = new Lab();
    
    lab.setLabmeta(getLabmetaList());
    
    m.addAttribute("now", now);
    m.addAttribute(AREA, lab);
    m.addAttribute("countries", Country.getList());
    m.addAttribute("states", State.getList());
    m.addAttribute("departments",deptService.findAll());
    
    
    return AREA+"/detail";
  }
  
  @RequestMapping(value="/detail/{labId}.do", method=RequestMethod.GET)
  public String detail(@PathVariable("labId") Integer labId, ModelMap m) {
	  
    String now = (new Date()).toString();

    Lab lab = this.labService.getById(labId);
    
    lab.setLabmeta(merge(lab.getLabmeta(), MetaProperty.getUniqueKeys(AREA)));
   
    List<LabUser> labUserList = lab.getLabUser();
    labUserList.size();

    List<Job> jobList = lab.getJob();
    jobList.size();
    
    
    m.addAttribute("now", now);
    m.addAttribute(AREA, lab);
    m.addAttribute("primaryuser",  userService.findById(lab.getPrimaryUserId()));
    m.addAttribute("countries", Country.getList());
    m.addAttribute("states", State.getList());
    m.addAttribute("departments",deptService.findAll());
    
    return "lab/detail";
  }
  
  
  @RequestMapping(value="/create/form.do", method=RequestMethod.POST)
  public String create(@Valid Lab labForm, BindingResult result, SessionStatus status, ModelMap m) {
	  	   
  	    //read properties from form
	  	List<Labmeta> labmetaList = getLabmetaFromForm(null);
	  	
	  	//set property attributes and sort them according to "position"
	  	MetaProperty.setAttributesAndSort(labmetaList,AREA);
	  	
		labForm.setLabmeta(labmetaList) ;
		
		//manually validate login and password
	
		List<String> validateList=new ArrayList<String>();
	
		for(Labmeta meta:labmetaList) {
			if (meta.getProperty()!=null && meta.getProperty().getConstraint()!=null) {
				validateList.add(meta.getK());validateList.add(meta.getProperty().getConstraint());
			}
		}
		
	    MetaValidator validator=new MetaValidator(validateList.toArray(new String[]{}));
		 
		validator.validate(labmetaList, result, AREA);
		   
	     if (result.hasErrors()) {        
	        return "lab/detail";
	    }
	   
	 
	    labForm.setLastUpdTs(new Date());
	    
	    Lab labDb=this.labService.save(labForm);
	    for (Labmeta um:labmetaList) {
	    	um.setLabId(labDb.getLabId());
	    };
	    
	    labmetaService.updateByLabId(labDb.getLabId(), labmetaList);
	    
	    status.setComplete();
	    
	 	return "redirect:/lab/detail/"+labDb.getLabId()+".do";
  }
  
  @RequestMapping(value="/detail/{labId}.do", method=RequestMethod.POST)
  public String updateDetail(@PathVariable("labId") Integer labId, @Valid Lab labForm, BindingResult result, SessionStatus status, ModelMap m) {
    
	 List<Labmeta> labmetaList = getLabmetaFromForm(labId);	
	 MetaProperty.setAttributesAndSort(labmetaList,AREA);
	 labForm.setLabmeta(labmetaList) ;
	 	
   	 List<String> validateList=new ArrayList<String>();
		
   	 for(Labmeta meta:labmetaList) {
		if (meta.getProperty()!=null && meta.getProperty().getConstraint()!=null) {
			validateList.add(meta.getK());validateList.add(meta.getProperty().getConstraint());
		}
	 }
	 	
	 MetaValidator validator=new MetaValidator(validateList.toArray(new String[] {}));
	 
	 validator.validate(labmetaList, result, AREA);
	   
     if (result.hasErrors()) {        
        return "lab/detail";
    }
   
    Lab labDb = this.labService.getById(labId);
    labDb.setName(labForm.getName());
       
    labDb.setLastUpdTs(new Date());
    
    this.labService.merge(labDb);
    
    labmetaService.updateByLabId(labId, labmetaList);
    
    status.setComplete();
    
 	return "redirect:"+labId+".do";
    
  }

  private List<Labmeta> merge(List<Labmeta> dbList,Set<String> keys) {
	  
	  List<Labmeta> resultList = new ArrayList<Labmeta>();
	
	  Set<String> dbKeys = new HashSet<String>();
	  
	  for(Labmeta m:dbList) {
		  
		  if (!m.getK().startsWith(AREA)) continue;
		  
		  String name=m.getK().substring(AREA.length()+1);
		  
		  if (!keys.contains(name)) continue;
		  
		  resultList.add(m);
		  dbKeys.add(m.getK());		  
	  }
	  
	  for(String key:keys) {		  
		  if (!dbKeys.contains("lab."+key)) {
			  resultList.add(new Labmeta("lab."+key,null));
		  }
	  }
	  
	  MetaProperty.setAttributesAndSort(resultList,AREA);
	  
	  return resultList;
  }
  
  private List<Labmeta> getLabmetaList() {
	  
	    List<Labmeta> list = new ArrayList<Labmeta>();   
	  
	    //get current list of meta properties to capture
	    Set<String> set=MetaProperty.getUniqueKeys(AREA);
	    
	    for(String name:set) {
	    	Labmeta meta=new Labmeta();
	    	meta.setK(AREA+"."+name);
	    	list.add(meta);
	    }
	       

	    //set property attributes and sort them according to "position"
	    MetaProperty.setAttributesAndSort(list,AREA);
	    
	    return list;
}
  
  	private List<Labmeta> getLabmetaFromForm(Integer labId) {
	  
	  List<Labmeta> labmetaList = new ArrayList<Labmeta>();
	  
	    Map parms = request.getParameterMap();
	    
	    for (Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();)  {  
	    	Map.Entry entry = (Map.Entry) iterator.next();
	    	String key=(String)entry.getKey();
	    	if (key.startsWith("labmeta_")) {
	    		Labmeta meta = new Labmeta();
	    		meta.setLabId(labId==null?0:labId);
	    		String name=key.substring("labmeta_".length());	    		
	    		meta.setK(name);
	    		meta.setV(((String[])entry.getValue())[0]);
	    		labmetaList.add(meta);	    		
	    	}    	
	    }
	    
	    return labmetaList;
  }
  
}
