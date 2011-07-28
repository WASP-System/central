package edu.yu.einstein.wasp.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
  
  @Autowired
  HttpServletRequest request;
  
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
  
  private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);
  
  
  @RequestMapping(value="/create/form.do", method=RequestMethod.GET)
  public String showEmptyForm(ModelMap m) {
	  
    String now = (new Date()).toString();
    
    User user = new User();
    
    user.setUsermeta(getUsermetaList("user"));
    
    m.addAttribute("now", now);
    m.addAttribute("user", user);
  
    return "user/detail";
  }
  
  
  private List<Usermeta> getUsermetaList(String prefix) {
	  
	    List<Usermeta> list = new ArrayList<Usermeta>();   
	  
	  //get current list of meta properties to capture
	    Set<String> set=getUniqueKeys("user");
	    
	    for(String name:set) {
	    	Usermeta meta=new Usermeta();
	    	meta.setK("user."+name);
	    	list.add(meta);
	    }
	       

	    //set property attributes and sort them according to "position"
	    setAttributesAndSort(list);
	    
	    return list;
  }
  
  @RequestMapping(value="/create/form.do", method=RequestMethod.POST)
  public String create(@Valid User userForm, BindingResult result, SessionStatus status, ModelMap m) {
	  	   
  	    //read properties from form
	  	List<Usermeta> usermetaList = getUsermetaFromForm(null);
	  	
	  	//set property attributes and sort them according to "position"
	  	setAttributesAndSort(usermetaList);
	  	
		userForm.setUsermeta(usermetaList) ;
		
		//manually val;idate login and password
		Errors errors=new BindException(result.getTarget(), "user"); 
		if (userForm.getLogin()==null || userForm.getLogin().isEmpty()) {
			errors.rejectValue("login", "user.error.login");
		} else {
			User user=userService.getUserByLogin(userForm.getLogin());
			if (user!=null && user.getLogin()!=null) {
				errors.rejectValue("login", "user.error.login_exists");
			}
		}
		
		if (userForm.getPassword()==null || userForm.getPassword().isEmpty()) {
			errors.rejectValue("password", "user.error.password");
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
  
 
  @RequestMapping(value="/detail/{userId}.do", method=RequestMethod.GET)
  public String detail(@PathVariable("userId") Integer userId, ModelMap m) {
	  
    String now = (new Date()).toString();

    User user = this.userService.getById(userId);
    
    //check if there are new metafields defined in property files
    //and add them to the form according to "postion" attribute
    user.setUsermeta(merge(user.getUsermeta(), getUniqueKeys("user")));
    
    //TODO: remove. just for testing. move to "login" controller when it's implemented
    String lang=user.getLocale().substring(0,2);
    String cntry=user.getLocale().substring(3);
    Locale locale=new Locale(lang,cntry);
    request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);

    m.addAttribute("now", now);
    m.addAttribute("user", user);
  
    return "user/detail";
  }
  

	// returns list of unique "k" values for the given "prefix"
	public static Set<String> getUniqueKeys(String prefix) {
	     
	     Set<String> set = new HashSet<String>();
	     
	     Enumeration<String> en=BASE_BUNDLE.getKeys();
	     while(en.hasMoreElements()) {
	    	String k = en.nextElement();
	    	
	    	if (!k.startsWith(prefix+".metaposition.")) continue;
	    	
	    	String name=k.substring(k.lastIndexOf(".")+1);
	    	
	    	set.add(name);

	    }
	     
	     return set;
	  }
  
  @RequestMapping(value="/detail/{userId}.do", method=RequestMethod.POST)
  public String updateDetail(@PathVariable("userId") Integer userId, @Valid User userForm, BindingResult result, SessionStatus status, ModelMap m) {
    
	 List<Usermeta> usermetaList = getUsermetaFromForm(userId);	
	 setAttributesAndSort(usermetaList);
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
  
  /* 1. populates "control" and "position" property of each object in the list
  * with values from the messages_en.properties file
  *
  * 2. Sorts list on meta.position field 
  */
  
  private void setAttributesAndSort(List<Usermeta> list) {
	  
	 for(Usermeta m:list) {
		String name=m.getK();
		String basename=name.substring(name.lastIndexOf(".")+1);
		
		//some old key we dont know about 
		if (!BASE_BUNDLE.containsKey("user.metaposition."+basename)) continue;
		
		Usermeta.Property p=new Usermeta.Property();
		m.setProperty(p);
		
		int pos=-1;
		if (BASE_BUNDLE.containsKey("user.metaposition."+basename)) {
				    		
			try {	    			
				pos=Integer.parseInt(BASE_BUNDLE.getString("user.metaposition."+basename));
			} catch (Throwable e) {}
 		}
		p.setMetaposition(pos);
		
 		if (BASE_BUNDLE.containsKey("user.control."+basename)) {
 			String controlStr=BASE_BUNDLE.getString("user.control."+basename);
 			String typeStr=controlStr.substring(0,controlStr.indexOf(":"));
 			Usermeta.Property.Control.Type type=Usermeta.Property.Control.Type.valueOf(typeStr);
 			if (type==Usermeta.Property.Control.Type.select) {
 				String[] pairs=StringUtils.tokenizeToStringArray(controlStr.substring(controlStr.indexOf(":")+1),";");
 				List<Usermeta.Property.Control.Option> options=new ArrayList<Usermeta.Property.Control.Option>();
 				
 				for(String el:pairs) {
 					String [] pair=StringUtils.split(el,":");
 					Usermeta.Property.Control.Option option = new Usermeta.Property.Control.Option();
 					option.setValue(pair[0]);
 					option.setLabel(pair[1]);
 					options.add(option);
 				}
 				
 				Usermeta.Property.Control control=new Usermeta.Property.Control();
 				control.setType(Usermeta.Property.Control.Type.select);
 				control.setOptions(options);	    				
 				p.setControl(control);
 			}	
 		}
 		
 		if (BASE_BUNDLE.containsKey("user.label."+basename)) {
 			p.setLabel(BASE_BUNDLE.getString("user.label."+basename));
 		}
 		
 		if (BASE_BUNDLE.containsKey("user.constraint."+basename)) {
 			p.setLabel(BASE_BUNDLE.getString("user.constraint."+basename));
 		}

 		if (BASE_BUNDLE.containsKey("user.error."+basename)) {
 			p.setLabel(BASE_BUNDLE.getString("user.error."+basename));
 		}
 		
	 }
	 
	 Collections.sort( list, META_POSITION_COMPARATOR);
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
	  
	  setAttributesAndSort(resultList);
	  
	  return resultList;
  }
    
  private final static Comparator<Usermeta> META_POSITION_COMPARATOR =  new Comparator<Usermeta>() {
		public int compare(Usermeta f1, Usermeta f2) {
   		 
 			if (f1==null) return -1;
 			if (f2==null) return 1;
 			
 			Integer p1=f1.getPosition();
 			Integer p2=f2.getPosition();	    		 

 			if (p1==null) return -1;
 			if (p2==null) return 1;

 			
 			return p1.compareTo(p2);
	 
 }
};
  
}
