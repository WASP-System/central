package edu.yu.einstein.wasp.controller;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.access.prepost.*;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.DepartmentService;




@Controller
@RequestMapping("/auth")
public class AuthController {

  private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

  @Autowired
  private UserService userService;

  @Autowired
  private DepartmentService departmentService;

  UserCache userCache = new NullUserCache();
  @Autowired
  public void setUserCache(UserCache userCache) {
    this.userCache = userCache;
  }
  public UserCache getUserCache() {
    return this.userCache;
  }

  UserDetailsService userDetailsService; 
  @Autowired
  public void setUserDetailsSerivice(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }
  public UserDetailsService getUserDetailsSerivice() {
    return this.userDetailsService;
  }

  @RequestMapping(value="/forgotpassword", method=RequestMethod.GET)
  public String showForgotPasswordForm(ModelMap m) {
    return "auth/forgotpassword/form";
  }

  @RequestMapping(value="/forgotpassword", method=RequestMethod.POST)
  public String forgotPassword(@RequestParam("j_username") String username, ModelMap m) {

    // - check if user exists
    // - email user 

    return "auth/forgotpassword/email";
  }

  @RequestMapping(value="/recoverpassword", method=RequestMethod.GET)
  public String showRecoverPasswordForm(
        @RequestParam(required = false, value="j_username") String username, 
        @RequestParam(required = false, value="authcode") String authCode, 
        ModelMap m) {
    if (authCode == null || "".equals(authCode)) {
      return "auth/recoverpassword/authcodeform";
    }
    return "auth/recoverpassword/form";
  }

  @RequestMapping(value="/recoverpassword", method=RequestMethod.POST)
  public String resetPassword(
        @RequestParam("j_username") String username, 
        @RequestParam("authcode") String authCode, 
        @RequestParam("password1") String password1, 
        @RequestParam("password2") String password2, 
        ModelMap m) {
    if (authCode == null || "".equals(authCode)) {
      return "auth/recoverpassword/authcodeform";
    }
    return "auth/recoverpassword/ok";
  }


  @RequestMapping(value="/newuser", method=RequestMethod.GET)
  public String showNewUserForm(ModelMap m) {
    User user = new User();

    List<Department> departments = departmentService.findAll();

    m.addAttribute("user", user);
    m.addAttribute("departments", departments);

    return "auth/newuser/form";
  }

  @RequestMapping(value="/newuser", method=RequestMethod.POST)
  public String showNewUser(ModelMap m) {
    return "auth/newuser/ok";
  }

  // *********************************************************

  @RequestMapping("/reauth")
  public String reauth(ModelMap m) {

    Authentication authentication = SecurityContextHolder.getContext()
           .getAuthentication();

    /*
    UserDetails oldUserDetails = userCache.getUserFromCache(authentication.getName()); 
    userCache.removeUserFromCache(authentication.getName()); 

    UserDetails newUserDetails = userDetailsService.loadUserByUsername(authentication.getName());

    userCache.putUserInCache(newUserDetails);

    if (!("anonymous".equalsIgnoreCase(authentication.getName()))
           && authentication.isAuthenticated()
           && userCache.getUserFromCache(authentication.getName()) == null
       ) {
       authentication.setAuthenticated(false);
    }
    */
    
       authentication.setAuthenticated(false);

    return "redirect:/dashboard.do";
  }

}
