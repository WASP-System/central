package edu.yu.einstein.wasp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import nl.captcha.Captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.UserpasswordauthDao;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userpasswordauth;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.util.AuthCode;
import edu.yu.einstein.wasp.util.StringHelper;


@Controller
@Transactional
@RequestMapping("/auth")
public class AuthController extends WaspController {

  @Autowired
  private UserpasswordauthDao userpasswordauthDao;

  @Autowired
  private EmailService emailService;

  
  @Autowired
  private UserDao userDao;
  
  @Autowired
  private BeanValidator validator;
  
  @Autowired
  private AuthenticationService authenticationService;
  
  @Autowired
  private ConfirmEmailAuthDao confirmEmailAuthDao;
  
  @Override
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }
  
  @RequestMapping(value="/loginHandler", method=RequestMethod.GET)
  public String loginHandler(ModelMap m){
	  if (authenticationService.isAuthenticated()){
		  User authUser = authenticationService.getAuthenticatedUser();
		  ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthDao.getConfirmEmailAuthByUserId(authUser.getUserId());
		  if (confirmEmailAuth.getConfirmEmailAuthId() != null){
			  // email awaiting confirmation for this user
			  authenticationService.logoutUser();
			  return "redirect:/auth/confirmemail/emailchanged.do";
		  } else {
			  return "redirect:/dashboard.do";
		  }
	  } 
	  return "redirect:/auth/login.do";
  }
  
  @RequestMapping(value="/confirmemail/requestEmailChange", method=RequestMethod.GET)
  public String requestEmailChange(ModelMap m){
	  return "auth/confirmemail/requestEmailChange";
  }
  
  @RequestMapping(value="/confirmemail/requestEmailChange", method=RequestMethod.POST)
  public String requestEmailChange(
		  @RequestParam(value="loginName") String loginName,
		  @RequestParam(value="password") String password,
		  @RequestParam(value="email") String email,
	  	  @RequestParam(value="captcha_text") String captchaText, ModelMap m){
	  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
	  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
		  waspErrorMessage("auth.requestEmailChange_captcha.error");
		  m.put("loginName", loginName);
		  m.put("email", email);
		  return "auth/confirmemail/requestEmailChange";
	  }
	  if (! StringHelper.isStringAValidEmailAddress(email)){
		  waspErrorMessage("auth.requestEmailChange_bademail.error");
		  m.put("loginName", loginName);
		  m.put("captcha_text", captchaText);
		  return "auth/confirmemail/requestEmailChange";
	  }
	  if (!authenticationService.authenticates(loginName, password)){
		  m.addAttribute("email", email);
		  m.put("captcha_text", captchaText);
		  waspErrorMessage("auth.requestEmailChange_badcredentials.error");
		  return "auth/confirmemail/requestEmailChange";
	  }
	  //update user with new email address
	  User user = userDao.getUserByLogin(loginName);
	  user.setEmail(email);
	  User userDb = userDao.merge(user);
	  
	  // email user with new authcode
	  emailService.sendUserEmailConfirm(userDb, emailService.getNewAuthcodeForUser(userDb));
	  return "redirect:/auth/confirmemail/emailchanged.do";
  }

    
  @RequestMapping(value="/resetpassword/request", method=RequestMethod.GET)
  public String showForgotPasswordForm(ModelMap m) {
    return "auth/resetpassword/request";
  }

  @RequestMapping(value="/resetpassword/request", method=RequestMethod.POST)
  public String forgotPassword(@RequestParam("username") String username, @RequestParam("captcha_text") String captchaText, ModelMap m) {

	  User user=userDao.getUserByLogin(username);
	  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
	  
	  if (username == null || captchaText == null || username.equals("") || captchaText.equals(""))
	  {
		  waspErrorMessage("auth.resetpasswordRequest_missingparam.error");
		  m.put("username", username);
		  return "auth/resetpassword/request";
	  }
	  
	  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
		  waspErrorMessage("auth.resetpasswordRequest_captcha.error");
		  m.put("username", username);
		  return "auth/resetpassword/request";
	  }
	  
	  if (user==null || user.getUserId() == null)  {
		  waspErrorMessage("auth.resetpasswordRequest_username.error");
		  return "auth/resetpassword/request";
	  }

	  emailService.sendRequestNewPassword(user, getUserPasswordAuthcode(user));
	  
    return "auth/resetpassword/email";
  }
  
  
  @PreAuthorize("not hasRole('ldap')")
  @RequestMapping(value="/resetpassword/form", method=RequestMethod.GET)
  public String showResetPasswordForm(@RequestParam(required = false, value="authcode") String authCode, ModelMap m) {
	  if (authCode == null || "".equals(authCode)) {
		  return "auth/resetpassword/authcodeform";
	  }
		  
	  Userpasswordauth userpasswordauth   = userpasswordauthDao.getUserpasswordauthByAuthcode(authCode);
	  if (userpasswordauth.getUserId() == null){
		  waspErrorMessage("auth.resetpassword_badauthcode.error");
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
	    	waspErrorMessage("auth.resetpassword_noauthcode.error");
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
	    	waspErrorMessage("auth.resetpassword_missingparam.error");
	        m.put("authcode", authCode);
	        m.put("username", username);
	        return "auth/resetpassword/form";
	 }
	 
	 Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
	 
	if (captcha == null || (! captcha.isCorrect(captchaText)) ){
		waspErrorMessage("auth.resetpassword_captcha.error");
		m.put("authcode", authCode);
	    m.put("username", username);
	    return "auth/resetpassword/form";
	}

    Userpasswordauth userpasswordauth   = userpasswordauthDao.getUserpasswordauthByAuthcode(authCode);
    User user = userDao.getUserByLogin(username);
    
    if (user.getUserId() == null) {
        waspErrorMessage("auth.resetpassword_username.error");
        m.put("authcode", authCode);
        return "auth/resetpassword/form";
     }
    
    if (userpasswordauth.getUserId() == null){
    	waspErrorMessage("auth.resetpassword_badauthcode.error");
    	return "auth/resetpassword/authcodeform";
    }

    if (user.getUserId().intValue() != userpasswordauth.getUserId().intValue()) {
    	m.put("authcode", authCode);
    	waspErrorMessage("auth.resetpassword_wronguser.error");
    	return "auth/resetpassword/authcodeform";
    }
    
    if (! authenticationService.matchPassword(password1, password2)){
    	waspErrorMessage("auth.resetpassword_new_mismatch.error");
    	m.put("authcode", authCode);
    	m.put("username", username);
    	return "auth/resetpassword/form";
    }
    
    if (! authenticationService.validatePassword(password1)){
    	 waspErrorMessage("auth.resetpassword_new_invalid.error");
         m.put("authcode", authCode);
         m.put("username", username);
         return "auth/resetpassword/form";
    }

    user.setPassword( authenticationService.encodePassword(password1) ); 
    userDao.merge(user);
    userpasswordauthDao.remove(userpasswordauth); // removes auth code from future use
    request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
    m.put("user", user); 

    return "auth/resetpassword/ok";
  }
  
  /**
	 * Validates a given email address and authCode.
	 * @param authCode
	 * @param email
	 * @param m model(can be null)
	 * @return is valid result (true / false)
	 */
  protected boolean userEmailValid(String authCode, String email, ModelMap m) {
	if (authCode == null || authCode.isEmpty()) {
		waspErrorMessage("auth.confirmemail_badauthcode.error");
		if (m != null) m.put("email", email);
		return false;
	}
	ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthDao.getConfirmEmailAuthByAuthcode(authCode);
	if (email == null || email.isEmpty() || confirmEmailAuth.getConfirmEmailAuthId() == null) {
		waspErrorMessage("auth.confirmemail_bademail.error");
		if (m != null) m.put("authcode", authCode);
		return false;
	}
		  
	User user = userDao.getUserByUserId(confirmEmailAuth.getUserId());

	if (! user.getEmail().equals(email)){
		waspErrorMessage("auth.confirmemail_wronguser.error");
		return false;
	}
	return true;
  }
  
  /**
   * Makes an entry in the userpasswordauth table and sends an email to email authcode to user
   * @param user {@link User} object
   */
  protected String getUserPasswordAuthcode(final User user){
	  Userpasswordauth userpasswordauth = new Userpasswordauth();
	  userpasswordauth.setUserId(user.getUserId());
	  String authcode = AuthCode.create(20);
	  userpasswordauth.setAuthcode(authcode);
	  userpasswordauthDao.merge(userpasswordauth); // merge handles both inserts and updates. Doesn't have problem with disconnected entities like persist does
	  return authcode; 
  }
  
  /**
   * Processes a pending user email validation GET request (should be from clicking a link in an email) 
   * @param authCode
   * @param urlEncodedEmail
   * @param m model
   * @return view
   */
  @RequestMapping(value="/confirmNewUserEmail", method=RequestMethod.GET)//for new users created via the User grid
  public String confirmNewUserEmailFromEmailLink(
		  @RequestParam(value="authcode", required=true) String authCode,
		  @RequestParam(value="email", required=true) String urlEncodedEmail,
		  @RequestParam(value="isAdminCreated", required=false) Integer isAdminCreated,
	      ModelMap m) {
    if (isAdminCreated == null) isAdminCreated = 0;
	if ( (authCode==null || authCode.isEmpty()) && (urlEncodedEmail==null || urlEncodedEmail.isEmpty()) ){
		m.addAttribute("isAdminCreated", isAdminCreated);
		// return the authcodeform view
		return "auth/confirmemail/authcodeform";
	}
	
	String decodedEmail;
	try{
		decodedEmail = URLDecoder.decode(urlEncodedEmail, "UTF-8");
	} catch(UnsupportedEncodingException e){//not good place to go; should be a different error page.
		waspErrorMessage("auth.confirmemail_corruptemail.error");
		m.addAttribute("isAdminCreated", isAdminCreated);
		return "redirect:/auth/confirmNewUserEmail.do"; // do this to clear GET parameters and forward to authcodeform view
		
	}
	
	//if (isAdminCreated == 0 && ! userEmailValid(authCode, decodedEmail, null)){
	//	m.addAttribute("isAdminCreated", isAdminCreated);
	//	return "redirect:/auth/confirmNewUserEmail.do"; // do this to clear GET parameters and forward to authcodeform view
	//}
	// authcode and email match if we get here
	// remove entry for current user in email auth table
	ConfirmEmailAuth auth = confirmEmailAuthDao.getConfirmEmailAuthByAuthcode(authCode);
	if(auth != null && auth.getConfirmEmailAuthId()!=null && auth.getConfirmEmailAuthId().intValue()!=0){//first time clicked
		User user = userDao.getUserByUserId(auth.getUserId());
		if ( ! user.getEmail().equals(decodedEmail)){//mismatch error with email address and user.email; should virtually never occur			
			return "auth/confirmemail/confirmemailerror";
		}
		confirmEmailAuthDao.remove(auth);
		return "redirect:/auth/resetpassword/form.do?authcode="+getUserPasswordAuthcode(user);//sets the password authcode in database (see function above)
	}
	else if (isAdminCreated != null && isAdminCreated == 1 && !authenticationService.isAuthenticationSetExternal()){
		
		User user = userDao.getUserByEmail(decodedEmail);//user.email is unique 
		if(user == null || user.getUserId().intValue()==0){
			return "auth/confirmemail/confirmemailerror";
		}
		Userpasswordauth userpasswordauth = userpasswordauthDao.getUserpasswordauthByUserId(user.getUserId());
			
		if(userpasswordauth != null && userpasswordauth.getUserId()!=null && userpasswordauth.getUserId().intValue()==user.getUserId().intValue()){
			// request user changes their password
			return "redirect:/auth/resetpassword/form.do?authcode="+userpasswordauth.getAuthcode();
		}
		return "redirect:/auth/login.do"; 		
	}
	
	return "redirect:/auth/login.do";
  }
  
  /**
   * Processes pending principal investigator email validation POST request from a form view
   * @param authCode
   * @param email
   * @param captchaText
   * @param m model
   * @return view
   */
  	@RequestMapping(value="/confirmNewUserEmail", method=RequestMethod.POST)
	public String confirmNewUserEmailFromForm(
			@RequestParam(value="authcode") String authCode,
			@RequestParam(value="email") String email,
			@RequestParam(value="captcha_text") String captchaText,
			@RequestParam(value="isAdminCreated") Integer isAdminCreated,
			ModelMap m) {
  		if (isAdminCreated == null) isAdminCreated = 0;
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			waspErrorMessage("auth.confirmemail_captcha.error");
			m.addAttribute("authcode", authCode);
			m.addAttribute("email", email);
			m.addAttribute("isAdminCreated", isAdminCreated);
			return "auth/confirmemail/authcodeform";
		}
		if (! userEmailValid(authCode, email, m)){
			m.addAttribute("isAdminCreated", isAdminCreated);
			return "auth/confirmemail/authcodeform";
		}
		
		User user = userDao.getUserByEmail(email);
		if (user.getUserId() == null){
			waspErrorMessage("auth.confirmemail_bademail.error");
			m.addAttribute("isAdminCreated", isAdminCreated);
			return "auth/confirmemail/authcodeform"; 
		}
		// authcode and email match if we get here
		// remove entry for current user in email auth table
		ConfirmEmailAuth auth = confirmEmailAuthDao.getConfirmEmailAuthByAuthcode(authCode);
		confirmEmailAuthDao.remove(auth);
		waspMessage("user.email_change_confirmed.label");
		if (isAdminCreated != null && isAdminCreated == 1 && !authenticationService.isAuthenticationSetExternal()){
			// request user changes their password
			return "redirect:/auth/resetpassword/form.do?authcode="+getUserPasswordAuthcode(user);
		}
		return "redirect:/auth/loginHandler.do";
	}

  @RequestMapping("/reauth")
  public String reauth(ModelMap m) {
    doReauth();

    return "redirect:/dashboard.do";
  }
  
  @RequestMapping("/wasp403")
  public String wasp403(){
	  return "auth/accessdenied";
  }

}
