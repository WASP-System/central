package edu.yu.einstein.wasp.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import org.springframework.security.access.prepost.*;

import edu.yu.einstein.wasp.model.MetaProperty;
import edu.yu.einstein.wasp.model.MetaProperty.Country;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Usermeta;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.UsermetaService;



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
  
  @Autowired
  HttpServletRequest request;
  
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
      binder.setValidator(validator);
  }

  public static final String AREA="user";
  
  @RequestMapping("/list")
  @PreAuthorize("hasRole('god')")
  public String list(ModelMap m) {
    List <User> userList = this.userService.findAll();
    
    m.addAttribute(AREA, userList);

    return "user/list";
  }
  
  @RequestMapping(value="/create/form.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god')")
  public String showEmptyForm(ModelMap m) {
	  
    String now = (new Date()).toString();
    
    User user = new User();
    
    user.setUsermeta(getUsermetaList(AREA));
    
    m.addAttribute("now", now);
    m.addAttribute(AREA, user);
    m.addAttribute("countries", Country.getList());
  
    return "user/detail";
  }
  
  
  private List<Usermeta> getUsermetaList(String prefix) {
	  
	    List<Usermeta> list = new ArrayList<Usermeta>();   
	  
	  //get current list of meta properties to capture
	    Set<String> set=MetaProperty.getUniqueKeys(AREA);
	    
	    for(String name:set) {
	    	Usermeta meta=new Usermeta();
	    	meta.setK("user."+name);
	    	list.add(meta);
	    }
	       

	    //set property attributes and sort them according to "position"
	    MetaProperty.setAttributesAndSort(list,AREA);
	    
	    return list;
  }
  
  @RequestMapping(value="/create/form.do", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god')")
  public String create(@Valid User userForm, BindingResult result, SessionStatus status, ModelMap m) {
	  	   
  	    //read properties from form
	  	List<Usermeta> usermetaList = getUsermetaFromForm(null);
	  	
	  	//set property attributes and sort them according to "position"
	  	MetaProperty.setAttributesAndSort(usermetaList,AREA);
	  	
		userForm.setUsermeta(usermetaList) ;
		
		//manually validate login and password
		Errors errors=new BindException(result.getTarget(), AREA); 
		if (userForm.getLogin()==null || userForm.getLogin().isEmpty()) {
			errors.rejectValue("login", "user.login.error");
		} else {
			User user=userService.getUserByLogin(userForm.getLogin());
			if (user!=null && user.getLogin()!=null) {
				errors.rejectValue("login", "user.login.exists_error");
			}
		}
		
		if (userForm.getPassword()==null || userForm.getPassword().isEmpty()) {
			errors.rejectValue("password", "user.password.error");
		}
		
		result.addAllErrors(errors);
	
		List<String> validateList=new ArrayList<String>();
		
		validateList.add("login");validateList.add(UsermetaValidator.Constraint.NotEmpty.name());
		validateList.add("password");validateList.add(UsermetaValidator.Constraint.NotEmpty.name());
		
		for(Usermeta meta:usermetaList) {
			if (meta.getProperty()!=null && meta.getProperty().getConstraint()!=null) {
				validateList.add(meta.getK());validateList.add(meta.getProperty().getConstraint());
			}
		}
	    UsermetaValidator validator=new UsermetaValidator(validateList.toArray(new String[]{}));
		 
		validator.validate(usermetaList, result);
		   
	     if (result.hasErrors()) {        
	        return "user/detail";
	    }
	   
	 
	    userForm.setLastUpdTs(new Date());
	    
	    User userDb=this.userService.save(userForm);
	    for (Usermeta um:usermetaList) {
	    	um.setUserId(userDb.getUserId());
	    };
	    
	    usermetaService.updateByUserId(userDb.getUserId(), usermetaList);
	    
	    status.setComplete();
	    
	 	return "redirect:/user/detail/"+userDb.getUserId()+".do";
  }
  
  @RequestMapping(value="/me.do", method=RequestMethod.GET)
  public String detailSelf(ModelMap m) {
    Authentication authentication = SecurityContextHolder.getContext()
           .getAuthentication();

    User user = this.userService.getUserByLogin(authentication.getName());

    return detail(user.getUserId(), m);
  }

 
  @RequestMapping(value="/detail/{userId}.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god')")
  public String detailById(@PathVariable("userId") Integer userId, ModelMap m) {
    return detail(userId, m);
  }
	  
  public String detail(Integer userId, ModelMap m) {
    String now = (new Date()).toString();

    User user = this.userService.getById(userId);
    
    //check if there are new metafields defined in property files
    //and add them to the form according to "postion" attribute
    user.setUsermeta(merge(user.getUsermeta(), MetaProperty.getUniqueKeys(AREA)));
    
    //TODO: remove. just for testing. move to "login" controller when it's implemented
    String lang=user.getLocale().substring(0,2);
    String cntry=user.getLocale().substring(3);
    Locale locale=new Locale(lang,cntry);
    request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

    m.addAttribute("now", now);
    m.addAttribute(AREA, user);
    m.addAttribute("countries", Country.getList());
  
    return "user/detail";
  }
  

  	
  @RequestMapping(value="/me.do", method=RequestMethod.POST)
  public String updateDetail(@Valid User userForm, BindingResult result, SessionStatus status, ModelMap m) {
    Authentication authentication = SecurityContextHolder.getContext()
           .getAuthentication();

    User user = this.userService.getUserByLogin(authentication.getName());

    updateDetail(user.getUserId(), userForm, result, status, m);

 	  return "redirect:" + "me.do";
  }
  
  @RequestMapping(value="/detail/{userId}.do", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god') or User.login == principal.name")
  public String updateDetail(@PathVariable("userId") Integer userId, @Valid User userForm, BindingResult result, SessionStatus status, ModelMap m) {
    
	 List<Usermeta> usermetaList = getUsermetaFromForm(userId);	
	 MetaProperty.setAttributesAndSort(usermetaList,AREA);
	 userForm.setUsermeta(usermetaList) ;
	 	
   	 List<String> validateList=new ArrayList<String>();
		
   	 for(Usermeta meta:usermetaList) {
		if (meta.getProperty()!=null && meta.getProperty().getConstraint()!=null) {
			validateList.add(meta.getK());validateList.add(meta.getProperty().getConstraint());
		}
	 }
	 	
	 UsermetaValidator validator=new UsermetaValidator(validateList.toArray(new String[] {}));
	 
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

  @RequestMapping(value="/mypassword.do", method=RequestMethod.GET)
  public String myPasswordForm (ModelMap m) {
    return "user/mypassword";
  }

  @RequestMapping(value="/mypassword.do", method=RequestMethod.POST)
  public String myPassword (
        @RequestParam(value="passwordold") String passwordold,
        @RequestParam(value="password") String password,
        @RequestParam(value="password2") String password2,
      ModelMap m) {
    return "redirect:/dashboard.do";
  }

  
  
  
  private List<Usermeta> getUsermetaFromForm(Integer userId) {
	  
	  List<Usermeta> usermetaList = new ArrayList<Usermeta>();
	  
	    Map parms = request.getParameterMap();
	    
	    for (Iterator iterator = parms.entrySet().iterator(); iterator.hasNext();)  {  
	    	Map.Entry entry = (Map.Entry) iterator.next();
	    	String key=(String)entry.getKey();
	    	if (key.startsWith("usermeta_")) {
	    		Usermeta meta = new Usermeta();
	    		meta.setUserId(userId==null?0:userId);
	    		String name=key.substring("usermeta_".length());	    		
	    		meta.setK(name);
	    		meta.setV(((String[])entry.getValue())[0]);
	    		usermetaList.add(meta);	    		
	    	}    	
	    }
	    
	    return usermetaList;
  }
  
  private List<Usermeta> merge(List<Usermeta> dbList,Set<String> keys) {
	  
	  List<Usermeta> resultList = new ArrayList<Usermeta>();
	
	  Set<String> dbKeys = new HashSet<String>();
	  
	  for(Usermeta m:dbList) {
		  resultList.add(m);
		  dbKeys.add(m.getK());		  
	  }
	  
	  for(String key:keys) {		  
		  if (!dbKeys.contains("user."+key)) {
			  resultList.add(new Usermeta("user."+key,null));
		  }
	  }
	  
	  MetaProperty.setAttributesAndSort(resultList,AREA);
	  
	  return resultList;
  }
    
}
