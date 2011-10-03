package edu.yu.einstein.wasp.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import edu.yu.einstein.wasp.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import edu.yu.einstein.wasp.model.MetaAttribute.Country;
import edu.yu.einstein.wasp.model.MetaAttribute.State;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.taglib.MessageTag;

@Controller
public class WaspController {

  protected final Logger logger = Logger.getLogger(getClass());

  @Autowired
  private MessageSource messageSource;
  
 public  static final Map<String, String> LOCALES=new LinkedHashMap<String, String>();
  static { 
	  LOCALES.put("en_US","English");
      LOCALES.put("iw_IL","Hebrew");
      LOCALES.put("ru_RU","Russian");
      LOCALES.put("ja_JA","Japanese");
  }

  //protected static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

  @Autowired
  private DepartmentService departmentService;
  
  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  protected UserService userService;

  @Autowired
  HttpServletRequest request;

  @Autowired
  private BeanValidator validator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }


  public User getAuthenticatedUser() { 
    Authentication authentication = SecurityContextHolder.getContext()
                                .getAuthentication();

    User user = this.userService.getUserByLogin(authentication.getName());

    return user;
  }

  public String[] getRoles() {
    Authentication authentication = SecurityContextHolder.getContext()
                                .getAuthentication();

    // java.util.Collection<GrantedAuthority> col = authentication.getAuthorities();
    java.util.Collection col = authentication.getAuthorities();
    String[] roles = new String[col.size()];
    int i = 0;
    for (Iterator<GrantedAuthority> it = col.iterator(); it.hasNext();) {

      GrantedAuthority grantedAuthority = it.next();
      roles[i] = grantedAuthority.getAuthority();
      i++;
    }
    return roles;
  }


  protected void prepareSelectListData(ModelMap m) {
    m.addAttribute("countries", Country.getList());
    m.addAttribute("states", State.getList());
    m.addAttribute("locales", LOCALES);
    m.addAttribute("department", departmentService.findAll());
  }

  protected String getMessage(String key) {
	  String message=null;
	  try {
		  message=(String)getBundle().getObject(key);		  
	  } catch (Throwable e) {
		
	  }
	  
	  if (message==null)   return messageSource.getMessage(key, null, Locale.US);//fallback to US locale
	  return message;
  }

  protected ResourceBundle getBundle() {
	  
	LocalizationContext nb=(LocalizationContext)Config.get(request.getSession(), Config.FMT_LOCALIZATION_CONTEXT);
	
	return nb.getResourceBundle(); 
	
  }


  public void doReauth() {
    SecurityContext securityContext= SecurityContextHolder.getContext();
    Authentication currentUser = securityContext.getAuthentication();
    UserDetails currentUserDetails = (UserDetails) currentUser.getPrincipal();

    UserDetails u = userDetailsService.loadUserByUsername(currentUserDetails.getUsername());

    UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword());

    SecurityContextHolder.getContext().setAuthentication(newToken);

  }

  public void waspMessage(String propertyString)   {
    MessageTag.addMessage(request.getSession(), propertyString);
  }

  protected String outputJSON(Map jqgridMap, HttpServletResponse response) throws JsonMappingException, IOException {
	  
	  	ObjectMapper mapper = new ObjectMapper();
	  
	  	String json=mapper.writeValueAsString(jqgridMap);
		 
		 response.setContentType("application/json;charset=UTF-8");
		
		 response.getWriter().print(json);
		 
		 return null;		
  }

}
