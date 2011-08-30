package edu.yu.einstein.wasp.controller;

import nl.captcha.Captcha;

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

    
  @RequestMapping(value="/forgotpassword", method=RequestMethod.GET)
  public String showForgotPasswordForm(ModelMap m) {
    return "auth/forgotpassword/form";
  }

  @RequestMapping(value="/forgotpassword", method=RequestMethod.POST)
  public String forgotPassword(@RequestParam("username") String username, @RequestParam("captcha_text") String captchaText, ModelMap m) {

	  User user=userService.getUserByLogin(username);
	  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
	  
	  if (username == null || captchaText == null || username.equals("") || captchaText.equals(""))
	  {
		  waspMessage("auth.forgotpassword.missingparam.error");
		  m.put("username", username);
		  return "auth/forgotpassword/form";
	  }
	  
	  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
		  waspMessage("auth.forgotpassword.captcha.error");
		  m.put("username", username);
		  return "auth/forgotpassword/form";
	  }
	  
	  if (user==null || user.getUserId()==0)  {
		  waspMessage("auth.forgotpassword.username.error");
		  return "auth/forgotpassword/form";
	  }

	Userpasswordauth userpasswordauth = new Userpasswordauth();
	userpasswordauth.setUserId(user.getUserId());
	String authcode = userpasswordauthService.createAuthCode(20);
	userpasswordauth.setAuthcode(authcode);
	//userpasswordauth.setLastUpdTs(new Date());
	//userpasswordauth.setLastUpdUser(user.getUserId());
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
		  waspMessage("auth.resetpassword.badauthcode.error");
		  return "auth/resetpassword/authcodeform";
	  }

	  m.put("authcode", authCode);
	  request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
	  return "auth/resetpassword/form";
  }
  
  @RequestMapping(value="/resetpassword", method=RequestMethod.POST)
  public String resetPassword(
        @RequestParam("username") String username, 
        @RequestParam("authcode") String authCode, 
        @RequestParam("password1") String password1, 
        @RequestParam("password2") String password2, 
        @RequestParam("captcha_text") String captchaText,
        ModelMap m) {
	
	  if (authCode == null || authCode.equals("")) {
	    	waspMessage("auth.resetpassword.noauthcode.error");
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
	    	waspMessage("auth.resetpassword.missingparam.error");
	        m.put("authcode", authCode);
	        m.put("username", username);
	        return "auth/resetpassword/form";
	 }
	 
	 Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
	 
	if (captcha == null || (! captcha.isCorrect(captchaText)) ){
		waspMessage("auth.resetpassword.captcha.error");
		m.put("authcode", authCode);
	    m.put("username", username);
	    return "auth/resetpassword/form";
	}

    Userpasswordauth userpasswordauth   = userpasswordauthService.getUserpasswordauthByAuthcode(authCode);
    User user = userService.getUserByLogin(username);
    
    if (user.getUserId() == 0) {
        waspMessage("auth.resetpassword.username.error");
        m.put("authcode", authCode);
        return "auth/resetpassword/form";
     }
    
    if (userpasswordauth.getUserId() == 0){
    	waspMessage("auth.resetpassword.badauthcode.error");
    	return "auth/resetpassword/authcodeform";
    }

    if (user.getUserId() != userpasswordauth.getUserId()) {
    	m.put("authcode", authCode);
    	waspMessage("auth.resetpassword.wronguser.error");
    	return "auth/resetpassword/authcodeform";
    }
    
    if (! passwordService.matchPassword(password1, password2)){
    	waspMessage("auth.resetpassword.new_mismatch.error");
    	m.put("authcode", authCode);
    	m.put("username", username);
    	return "auth/resetpassword/form";
    }
    
    if (! passwordService.validatePassword(password1)){
    	 waspMessage("auth.resetpassword.new_invalid.error");
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
