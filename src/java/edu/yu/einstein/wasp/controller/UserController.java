package edu.yu.einstein.wasp.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.UsermetaService;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Usermeta;


@Controller
@Transactional
@RequestMapping("/user")
public class UserController {
	
  @Autowired
  private UserService userService;
  
  @Autowired
  private UsermetaService usermetaService;

  @Autowired 	
  private BeanValidator validator;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
      binder.setValidator(validator);
  }

  
  @RequestMapping("/list")
  public String list(ModelMap m) {
    List <User> userList = this.userService.findAll();
    
    m.addAttribute("user", userList);

    return "user/list";
  }
  
  @Autowired
  HttpServletRequest request;
  

  
  @RequestMapping(value="/detail/{userId}.do", method=RequestMethod.GET)
  public String detail(@PathVariable("userId") Integer userId, ModelMap m) {
	  

	//Object user = request.getUserPrincipal() ; 
	  
    String now = (new Date()).toString();

    User user = this.userService.getById(userId);

    //TODO: remove. just for testing. move to "login" controller when it's implemented
    String lang=user.getLocale().substring(0,2);
    String cntry=user.getLocale().substring(3);
    Locale locale=new Locale(lang,cntry);
    request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

    Object o = Arrays.asList(Locale.getAvailableLocales());
    
    m.addAttribute("now", now);
    m.addAttribute("user", user);
  
    return "user/detail";
  }
  
  @RequestMapping(value="/detail/{userId}.do", method=RequestMethod.POST)
  public String updateDetail(@PathVariable("userId") Integer userId, @Valid User userForm, BindingResult result, SessionStatus status, ModelMap m) {
    
	 List<Usermeta> usermetaList = getUsermeta(userId);	
	 userForm.setUsermeta(usermetaList) ;
	 
	 UsermetaValidator validator=new UsermetaValidator(
			 "user.title",UsermetaValidator.Constraint.NotEmpty.name(),
			 "user.institution",UsermetaValidator.Constraint.NotEmpty.name(),
			 "user.address",UsermetaValidator.Constraint.NotEmpty.name(),
			 "user.city",UsermetaValidator.Constraint.NotEmpty.name(),
			 "user.country",UsermetaValidator.Constraint.NotEmpty.name(),
			 "user.zip",UsermetaValidator.Constraint.NotEmpty.name(),
			 "user.phone",UsermetaValidator.Constraint.NotEmpty.name()			 
	 );
	 
	 validator.validate(usermetaList, result);
	   
     if (result.hasErrors()) {        
        return "user/detail";
    }
   
    User userDb = this.userService.getById(userId);
    userDb.setFirstName(userForm.getFirstName());
    userDb.setLastName(userForm.getLastName());
    
    userDb.setEmail(userForm.getEmail());
    userDb.setLocale(userForm.getLocale());
       
    userDb.setLastUpdTs(new Date());
    
    this.userService.merge(userDb);
    
    usermetaService.updateByUserId(userId, usermetaList);
    
    status.setComplete();
    
 	return "redirect:"+userId+".do";
    
  }
  
  
  private List<Usermeta> getUsermeta(Integer userId) {
	  List<Usermeta> usermetaList = new ArrayList<Usermeta>();
	    
	    Map parms = request.getParameterMap();
	    int pos=0;
	    for (Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();)  {  
	    	Map.Entry entry = (Map.Entry) iterator.next();
	    	String key=(String)entry.getKey();
	    	if (key.startsWith("usermeta_")) {
	    		Usermeta meta = new Usermeta();
	    		meta.setUserId(userId);
	    		meta.setPosition(pos);
	    		meta.setK(key.substring("usermeta_".length()));
	    		meta.setV(((String[])entry.getValue())[0]);
	    		usermetaList.add(meta);
	    		pos++;
	    	}    	
	    }
	    
	    return usermetaList;
  }
  

}
