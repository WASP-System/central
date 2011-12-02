package edu.yu.einstein.wasp.controller;

import nl.captcha.Captcha;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userpasswordauth;

import edu.yu.einstein.wasp.service.UserpasswordauthService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.PasswordService;
import edu.yu.einstein.wasp.util.AuthCode;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


@Controller
@Transactional
@RequestMapping("/auth")
public class AuthController extends WaspController {

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

    
  @RequestMapping(value="/resetpassword/request", method=RequestMethod.GET)
  public String showForgotPasswordForm(ModelMap m) {
    return "auth/resetpassword/request";
  }

  @RequestMapping(value="/resetpassword/request", method=RequestMethod.POST)
  public String forgotPassword(@RequestParam("username") String username, @RequestParam("captcha_text") String captchaText, ModelMap m) {

	  User user=userService.getUserByLogin(username);
	  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
	  
	  if (username == null || captchaText == null || username.equals("") || captchaText.equals(""))
	  {
		  waspMessage("auth.resetpasswordRequest_missingparam.error");
		  m.put("username", username);
		  return "auth/resetpassword/request";
	  }
	  
	  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
		  waspMessage("auth.resetpasswordRequest_captcha.error");
		  m.put("username", username);
		  return "auth/resetpassword/request";
	  }
	  
	  if (user==null || user.getUserId()==0)  {
		  waspMessage("auth.resetpasswordRequest_username.error");
		  return "auth/resetpassword/request";
	  }

	Userpasswordauth userpasswordauth = new Userpasswordauth();
	userpasswordauth.setUserId(user.getUserId());
	String authcode = AuthCode.create(20);
	userpasswordauth.setAuthcode(authcode);
	//userpasswordauth.setLastUpdTs(new Date());
	//userpasswordauth.setLastUpdUser(user.getUserId());
	userpasswordauthService.merge(userpasswordauth); // merge handles both inserts and updates. Doesn't have problem with disconnected entities like persist does
	emailService.sendForgotPassword(user, authcode);
	  
    return "auth/resetpassword/email";
  }
  
  
  @PreAuthorize("not hasRole('ldap')")
  @RequestMapping(value="/resetpassword/form", method=RequestMethod.GET)
  public String showResetPasswordForm(@RequestParam(required = false, value="authcode") String authCode, ModelMap m) {
	  if (authCode == null || "".equals(authCode)) {
		  return "auth/resetpassword/authcodeform";
	  }
		  
	  Userpasswordauth userpasswordauth   = userpasswordauthService.getUserpasswordauthByAuthcode(authCode);
	  if (userpasswordauth.getUserId() == 0){
		  waspMessage("auth.resetpassword_badauthcode.error");
		  return "auth/resetpassword/authcodeform";
	  }

	  m.put("authcode", authCode);
	  request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
	  return "auth/resetpassword/form";
  }
  
  @PreAuthorize("not hasRole('ldap')")
  @RequestMapping(value="/resetpassword/form", method=RequestMethod.POST)
  public String resetPassword(
        @RequestParam("username") String username, 
        @RequestParam("authcode") String authCode, 
        @RequestParam("password1") String password1, 
        @RequestParam("password2") String password2, 
        @RequestParam("captcha_text") String captchaText,
        ModelMap m) {
	
	  if (authCode == null || authCode.equals("")) {
	    	waspMessage("auth.resetpassword_noauthcode.error");
	    	return "auth/resetpassword/authcodeform";
	  }
	  
	 if (username == null || 
			 captchaText == null || 
			 password1 == null || 
			 password2 == null || 
			 username.equals("") || 
			 captchaText.equals("") || 
			 password1.equals("") || 
			 password2.equals("") ){
	    	waspMessage("auth.resetpassword_missingparam.error");
	        m.put("authcode", authCode);
	        m.put("username", username);
	        return "auth/resetpassword/form";
	 }
	 
	 Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
	 
	if (captcha == null || (! captcha.isCorrect(captchaText)) ){
		waspMessage("auth.resetpassword_captcha.error");
		m.put("authcode", authCode);
	    m.put("username", username);
	    return "auth/resetpassword/form";
	}

    Userpasswordauth userpasswordauth   = userpasswordauthService.getUserpasswordauthByAuthcode(authCode);
    User user = userService.getUserByLogin(username);
    
    if (user.getUserId() == 0) {
        waspMessage("auth.resetpassword_username.error");
        m.put("authcode", authCode);
        return "auth/resetpassword/form";
     }
    
    if (userpasswordauth.getUserId() == 0){
    	waspMessage("auth.resetpassword_badauthcode.error");
    	return "auth/resetpassword/authcodeform";
    }

    if (user.getUserId() != userpasswordauth.getUserId()) {
    	m.put("authcode", authCode);
    	waspMessage("auth.resetpassword_wronguser.error");
    	return "auth/resetpassword/authcodeform";
    }
    
    if (! passwordService.matchPassword(password1, password2)){
    	waspMessage("auth.resetpassword_new_mismatch.error");
    	m.put("authcode", authCode);
    	m.put("username", username);
    	return "auth/resetpassword/form";
    }
    
    if (! passwordService.validatePassword(password1)){
    	 waspMessage("auth.resetpassword_new_invalid.error");
         m.put("authcode", authCode);
         m.put("username", username);
         return "auth/resetpassword/form";
    }

    user.setPassword( passwordService.encodePassword(password1) ); 
    userService.merge(user);
    userpasswordauthService.remove(userpasswordauth); // removes auth code from future use
    request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
    m.put("user", user); 

    return "auth/resetpassword/ok";
  }

  @RequestMapping("/reauth")
  public String reauth(ModelMap m) {
    doReauth();

    return "redirect:/dashboard.do";
  }

}
