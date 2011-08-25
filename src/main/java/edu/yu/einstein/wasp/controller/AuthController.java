package edu.yu.einstein.wasp.controller;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import javax.validation.Valid;

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

import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import org.springframework.security.access.prepost.*;

import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userpasswordauth;

import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.UserpasswordauthService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.PasswordService;

import edu.yu.einstein.wasp.taglib.MessageTag;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Controller
@Transactional
@RequestMapping("/auth")
public class AuthController extends WaspController {

  private static final ResourceBundle BASE_BUNDLE=ResourceBundle.getBundle("messages", Locale.ENGLISH);

  @Autowired
  private UserpasswordauthService userpasswordauthService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private PasswordService passwordService;
  
  @Autowired
  private BeanValidator validator;

  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }

    
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

	Userpasswordauth userpasswordauth = new Userpasswordauth();
	userpasswordauth.setUserId(user.getUserId());
	String authcode = userpasswordauthService.createAuthCode(20);
	userpasswordauth.setAuthcode(authcode);
	userpasswordauth.setLastUpdTs(new Date());
	userpasswordauth.setLastUpdUser(user.getUserId());
	userpasswordauthService.merge(userpasswordauth); // merge handles both inserts and updates. Doesn't have problem with disconnected entities like persist does
	emailService.sendForgotPassword(user, authcode);
	  
    return "auth/forgotpassword/email";
  }
  
  

  @RequestMapping(value="/resetpassword", method=RequestMethod.GET)
  public String showResetPasswordForm(@RequestParam(required = false, value="authcode") String authCode, ModelMap m) {
	  if (authCode == null || "".equals(authCode)) {
		  return "auth/resetpassword/authcodeform";
	  }
		  
	  Userpasswordauth userpasswordauth   = userpasswordauthService.getUserpasswordauthByAuthcode(authCode);
	  if (userpasswordauth.getUserId() == 0){
		  MessageTag.addMessage(request.getSession(), "auth.resetpassword.badauthcode.error");
		  return "auth/resetpassword/authcodeform";
	  }

	  m.put("authcode", authCode);
	  return "auth/resetpassword/form";
  }
  
  @RequestMapping(value="/resetpassword", method=RequestMethod.POST)
  public String resetPassword(
        @RequestParam("j_username") String username, 
        @RequestParam("authcode") String authCode, 
        @RequestParam("password1") String password1, 
        @RequestParam("password2") String password2, 
        ModelMap m) {
    if (authCode == null || authCode.equals("")) {
    	MessageTag.addMessage(request.getSession(), "auth.resetpassword.noauthcode.error");
    	return "auth/resetpassword/authcodeform";
    }

    if (username == null || username.equals("")) {
       MessageTag.addMessage(request.getSession(), "auth.resetpassword.missingparam.error");
       m.put("authcode", authCode);
       return "auth/resetpassword/form";
    }
    if (password1 == null || password2 == null || password1.equals("") || password2.equals("") ){
    	MessageTag.addMessage(request.getSession(), "auth.resetpassword.missingparam.error");
        m.put("authcode", authCode);
        m.put("username", username);
        return "auth/resetpassword/form";
    }
    
    if (password1.equals("") || password2.equals("") || ! password1.equals(password2)) {
       MessageTag.addMessage(request.getSession(), "auth.resetpassword.new_mismatch.error");
       m.put("authcode", authCode);
       m.put("username", username);
       return "auth/resetpassword/form";
    }
    
    if (! passwordService.validatePassword(password1)){
    	 MessageTag.addMessage(request.getSession(), "auth.resetpassword.new_invalid.error");
         m.put("authcode", authCode);
         m.put("username", username);
         return "auth/resetpassword/form";
    }

    Userpasswordauth userpasswordauth   = userpasswordauthService.getUserpasswordauthByAuthcode(authCode);
    User user = userService.getUserByLogin(username);
    
    if (user.getUserId() == 0) {
        MessageTag.addMessage(request.getSession(), "auth.resetpassword.error.username");
        m.put("authcode", authCode);
        return "auth/resetpassword/form";
     }
    
    if (userpasswordauth.getUserId() == 0){
    	MessageTag.addMessage(request.getSession(), "auth.resetpassword.badauthcode.error");
    	return "auth/resetpassword/authcodeform";
    }

    if (user.getUserId() != userpasswordauth.getUserId()) {
    	m.put("authcode", authCode);
    	MessageTag.addMessage(request.getSession(), "auth.resetpassword.wronguser.error");
    	return "auth/resetpassword/authcodeform";
    }

    user.setPassword( passwordService.encodePassword(password1) ); 
    userService.merge(user);

    // removes auth code from future use
    userpasswordauthService.remove(userpasswordauth);

    m.put("user", user); 

    return "auth/resetpassword/ok";
  }

  @RequestMapping("/reauth")
  public String reauth(ModelMap m) {
    doReauth();

    return "redirect:/dashboard.do";
  }

}
