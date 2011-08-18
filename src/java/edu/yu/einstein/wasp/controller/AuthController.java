package edu.yu.einstein.wasp.controller;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;


import org.springframework.security.access.prepost.*;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userpasswordauth;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.UserpasswordauthService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.taglib.MessageTag;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

@Controller
@Transactional
@RequestMapping("/auth")
public class AuthController extends WaspController {

  private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserpasswordauthService userpasswordauthService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private EmailService emailService;

  @Autowired
  HttpServletRequest request;


  @RequestMapping(value="/forgotpassword", method=RequestMethod.GET)
  public String showForgotPasswordForm(ModelMap m) {
    return "auth/forgotpassword/form";
  }

  @RequestMapping(value="/forgotpassword", method=RequestMethod.POST)
  public String forgotPassword(@RequestParam("j_username") String username, ModelMap m) {

	  User user=userService.getUserByLogin(username);
	  
	  if (user==null || user.getUserId()==0)  {
		  MessageTag.addMessage(request.getSession(), "auth.forgotpassword.error.username");
		  return "auth/forgotpassword/form";
	  }

// TODO generate table w/ good authcode	
	String authcode = user.getEmail();

	Userpasswordauth oldUserpasswordauth = userpasswordauthService.getUserpasswordauthByUserId(user.getUserId());
	if (oldUserpasswordauth.getUserId() != 0) {
		// userpasswordauthService.remove(oldUserpasswordauth);
	}

	// Userpasswordauth userpasswordauth = new Userpasswordauth();
	Userpasswordauth userpasswordauth = userpasswordauthService.getUserpasswordauthByUserId(user.getUserId());

	userpasswordauth.setUserId(user.getUserId());
	userpasswordauth.setAuthcode(authcode);

	userpasswordauthService.persist(userpasswordauth);

	emailService.sendForgotPassword(user, authcode);
	  
    return "auth/forgotpassword/email";
  }

  @RequestMapping(value="/recoverpassword", method=RequestMethod.GET)
  public String showRecoverPasswordForm(
        @RequestParam(required = false, value="authcode") String authCode, 
        ModelMap m) {
    if (authCode == null || "".equals(authCode)) {
      return "auth/recoverpassword/authcodeform";
    }

    m.put("authcode", authCode);
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

// TODO real error
    if (username.equals("")) {
       MessageTag.addMessage(request.getSession(), "hello.error");
       m.put("authcode", authCode);
       return "auth/recoverpassword/form";
    }
    if (password1.equals("") || password2.equals("") || ! password1.equals(password2)) {
       MessageTag.addMessage(request.getSession(), "hello.error");
       m.put("authcode", authCode);
       return "auth/recoverpassword/form";
    }

    Userpasswordauth userpasswordauth   = userpasswordauthService.getUserpasswordauthByAuthcode(authCode);
    User user = userService.getUserByLogin(username);

    if (userpasswordauth.getUserId() == 0) {
       MessageTag.addMessage(request.getSession(), "auth.recoverpassword.error.badauthcode");
       m.put("authcode", authCode);
       return "auth/recoverpassword/form";
    }

    // cannot find user
    if (user.getUserId() == 0) {
       m.put("authcode", authCode);

       MessageTag.addMessage(request.getSession(), "hello.error");
       return "auth/recoverpassword/form";
    }

    if (user.getUserId() != userpasswordauth.getUserId()) {
      m.put("authcode", authCode);

       MessageTag.addMessage(request.getSession(), "auth.recoverpassword.error.wronguser");
       return "auth/recoverpassword/form";
    }


    PasswordEncoder encoder = new ShaPasswordEncoder();
    String hashedPassword = encoder.encodePassword(password1, null);

    user.setPassword(hashedPassword);
    userService.merge(user);

    // removes auth code from future use
    userpasswordauthService.remove(userpasswordauth);

    m.put("user", user); 

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

    SecurityContext securityContext= SecurityContextHolder.getContext();
    Authentication currentUser = securityContext.getAuthentication();
    UserDetails currentUserDetails = (UserDetails) currentUser.getPrincipal();

    UserDetails u = userDetailsService.loadUserByUsername(currentUserDetails.getUsername());

    UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword());

    SecurityContextHolder.getContext().setAuthentication(newToken);

    return "redirect:/dashboard.do";
  }

}
