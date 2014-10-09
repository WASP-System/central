package edu.yu.einstein.wasp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import nl.captcha.Captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CookieValue;
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
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.WebAuthenticationService;
import edu.yu.einstein.wasp.util.AuthCode;
import edu.yu.einstein.wasp.util.DemoEmail;
import edu.yu.einstein.wasp.util.StringHelper;


@Controller
@Transactional
@RequestMapping("/auth")
public class AuthController extends WaspController {

  @Autowired
  private UserpasswordauthDao userpasswordauthDao;

  @Autowired
  protected EmailService emailService;
  
  @Autowired
  private DemoEmail demoEmail;

  @Autowired
  protected UserDao userDao;
  
  @Autowired
  private BeanValidator validator;
  
  @Autowired
  protected WebAuthenticationService webAuthenticationService;
  
  @Autowired
  protected ConfirmEmailAuthDao confirmEmailAuthDao;
  
  private static final String TARGET_URL_KEY = "targetURL";
  
  private static final String LOGIN_EXPIRED_WARNING_KEY = "loginExpiredWarningLabel";
  
    
  @Override
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setValidator(validator);
  }
  
  @RequestMapping(value="/loginRedirect", method=RequestMethod.GET)
  public String loginRefer(HttpServletResponse response, ModelMap m){
	  logger.debug("Using external authentication = " + webAuthenticationService.isAuthenticationSetExternal());
	  String targetURL = "";
	  // store target URL in session variable. During login process the original target from which
	  // Spring Security redirected non-authenticated user will be preserved and can be retrieved from the session after successful login.
	  // target URL is ONLY set if Spring Security forces a re-direction to the login page.
	  SavedRequest savedRequest =  new HttpSessionRequestCache().getRequest(request, response);
	  if (savedRequest != null){
		  targetURL = savedRequest.getRedirectUrl(); // get the target url from which we were redirected to login (if set)
		  logger.debug("Raw target URL = " + targetURL);
		  if (!targetURL.isEmpty()){
			  // HACK ALERT: Ideally we'd have an easy URL design that makes it easy to distinguish calls to pages and ajax calls for data
			  // but we don't so need to put in some catches for handling known corner cases
			  if (targetURL.contains("/job/") && !targetURL.matches("^.+\\/job\\/(list|\\d+\\/homepage)\\.do$")){
				  Pattern p = Pattern.compile("^(.+\\/job\\/\\d+\\/).*$");
				  Matcher match = p.matcher(targetURL);
				  if (match.find()){
					  targetURL = match.group(1) + "homepage.do";
					  request.getSession().setAttribute(LOGIN_EXPIRED_WARNING_KEY, "auth.redirectDataNotSaved.label");
					  logger.debug("target URL is provided so setting session variable for target = " + targetURL);
					  request.getSession().setAttribute(TARGET_URL_KEY, targetURL); 
				  }
				  return "auth/loginReferralPage";
			  } else if (targetURL.toLowerCase().contains("json.do")){
				  // to messy to decide where to go so default to dashboard
				  request.getSession().setAttribute(LOGIN_EXPIRED_WARNING_KEY, "auth.redirectDataNotSaved.label");
				  logger.debug("target URL is not suitable for redirection");
				  return "auth/loginReferralPage";
			  }
			  logger.debug("target URL is provided so setting session variable for target = " + targetURL);
			  request.getSession().setAttribute(TARGET_URL_KEY, targetURL); 
		  }
	  }
	  return "redirect:/auth/login.do";
  }
  
  @RequestMapping(value="/login", method=RequestMethod.GET)
  public String login(ModelMap m){
	  // this is our entry point when starting up so save some session attributes here
	  logger.info("Setting 'isInDemoMode' session attribute to: " + Boolean.toString(isInDemoMode));
	  request.getSession().setAttribute("isInDemoMode", new Boolean(isInDemoMode));
	  if (isInDemoMode && (demoEmail == null || demoEmail.getDemoEmail().isEmpty()) )
		  return "redirect:/auth/getEmailForDemo.do";
	  if (webAuthenticationService.isAuthenticatedWaspUser()){
		  User authUser = webAuthenticationService.getAuthenticatedUser();
		  ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthDao.getConfirmEmailAuthByUserId(authUser.getId());
		  if (confirmEmailAuth.getId() != null){
			  // email awaiting confirmation for this user
			  webAuthenticationService.logoutUser();
			  return "redirect:/auth/confirmemail/emailchanged.do";
		  } else {
			  // login ok so proceed.
			  
			  String loginExpiredWarningLabel = (String) request.getSession().getAttribute(LOGIN_EXPIRED_WARNING_KEY);
			  if (loginExpiredWarningLabel != null && !loginExpiredWarningLabel.isEmpty())
			  	  waspErrorMessage(loginExpiredWarningLabel);
			  // if a target URL was set, get it out of the session variable and redirect to it otherwise go to dashboard. Target URL is a location 
			  // within the secure area of the system from which Spring Security re-directed to the login page because
			  // the user was not logged in (note difference from referring URL).
			  String targetURL = (String) request.getSession().getAttribute(TARGET_URL_KEY);
			  if (targetURL != null ){
				  request.getSession().removeAttribute("targetURL"); // remove used session variable
				  logger.debug("targetURL from session =" + targetURL);
				  // extract relative path for redirect destination from the target url
				  String servletPathCaptureExp = "^.*?" + request.getServerName() + "(:\\d+)?\\" + request.getContextPath() + "(/.+)?\\s*$";
				  Pattern pattern = Pattern.compile(servletPathCaptureExp);
				  Matcher matcher = pattern.matcher(targetURL);
				  if (matcher.find()){
					  String destPath = matcher.group(2);
					  logger.debug("redirecting to target destination " + destPath);
					  return "redirect:" + targetURL;
				  } else {
					  logger.warn("Unable to extract destination path from target URL");
				  }
			  }
			  logger.debug("Login success so redirecting to dashboard");
			  return "redirect:/dashboard.do";
		  }
	  } else if (webAuthenticationService.isAuthenticated()){
		  webAuthenticationService.logoutUser();
		  waspErrorMessage("auth.authenticatedButNoWaspAccount.error");
		  return "redirect:/auth/login.do";
	  }
	  m.addAttribute("isAuthenticationExternal", webAuthenticationService.isAuthenticationSetExternal());
	  return "auth/loginPage";
  }
  
  @RequestMapping(value="/getEmailForDemo", method=RequestMethod.GET)
  public String getEmailForDemoGet(@CookieValue(value="waspDemoEmail", defaultValue="", required=false) String email, ModelMap m){
	  if (!email.isEmpty()){
		  demoEmail.setDemoEmail(email);
		  return "redirect:/auth/login.do";
	  }
	  return "auth/getEmailForDemoForm";
  }
  
  @RequestMapping(value="/getEmailForDemo", method=RequestMethod.POST)
  public String getEmailForDemoPost(@RequestParam(value="email") String email, ModelMap m, HttpServletResponse response){
	  if (email.isEmpty() || !Pattern.matches("([\\w+|\\.?]+)\\w+@([\\w+|\\.?]+)\\.(\\w{2,8}\\w?)", email)){
		  waspErrorMessage("auth.demo_email.error");
		  return "auth/getEmailForDemoForm";
	  }
	  Cookie waspDemoCookie = new Cookie("waspDemoEmail", email);
	  waspDemoCookie.setMaxAge(60*60*24);
	  response.addCookie(waspDemoCookie);
	  demoEmail.setDemoEmail(email);
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
	  if (!webAuthenticationService.authenticates(loginName, password)){
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
	  
	  if (user==null || user.getId() == null)  {
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
	  if (userpasswordauth.getId() == null){
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
    
    if (user.getId() == null) {
        waspErrorMessage("auth.resetpassword_username.error");
        m.put("authcode", authCode);
        return "auth/resetpassword/form";
     }
    
    if (userpasswordauth.getId() == null){
    	waspErrorMessage("auth.resetpassword_badauthcode.error");
    	return "auth/resetpassword/authcodeform";
    }

    if (user.getId().intValue() != userpasswordauth.getId().intValue()) {
    	m.put("authcode", authCode);
    	waspErrorMessage("auth.resetpassword_wronguser.error");
    	return "auth/resetpassword/authcodeform";
    }
    
    if (! webAuthenticationService.matchPassword(password1, password2)){
    	waspErrorMessage("auth.resetpassword_new_mismatch.error");
    	m.put("authcode", authCode);
    	m.put("username", username);
    	return "auth/resetpassword/form";
    }
    
    if (! webAuthenticationService.validatePassword(password1)){
    	 waspErrorMessage("auth.resetpassword_new_invalid.error");
         m.put("authcode", authCode);
         m.put("username", username);
         return "auth/resetpassword/form";
    }

    user.setPassword( webAuthenticationService.encodePassword(password1) ); 
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
	if (email == null || email.isEmpty() || confirmEmailAuth.getId() == null) {
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
	  userpasswordauth.setId(user.getId());
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
	if (webAuthenticationService.isAuthenticatedWaspUser())
		  webAuthenticationService.logoutUser();
    if (isAdminCreated == null) isAdminCreated = 0;
	if ( (authCode==null || authCode.isEmpty()) && (urlEncodedEmail==null || urlEncodedEmail.isEmpty()) ){
		logger.warn("authcode or email empty");
		return "redirect:/auth/confirmemail/confirmemailerror.do";
	}
	
	String decodedEmail;
	try{
		decodedEmail = URLDecoder.decode(urlEncodedEmail, "UTF-8");
	} catch(UnsupportedEncodingException e){
		logger.warn("Problem decoding URL-encoded email: " + e.getLocalizedMessage());
		return "redirect:/auth/confirmemail/confirmemailerror.do";
	}
	
	ConfirmEmailAuth auth = confirmEmailAuthDao.getConfirmEmailAuthByAuthcode(authCode);
	if(auth != null && auth.getId() != null){//first time clicked
		User user = userDao.getUserByUserId(auth.getUserId());
		if ( ! user.getEmail().equals(decodedEmail)){
			logger.warn("mismatch error with email address supplied in URL and user.email");
			return "redirect:/auth/confirmemail/confirmemailerror.do";
		}
		// authcode and email match 
		confirmEmailAuthDao.remove(auth);
		if (isAdminCreated == 0 || webAuthenticationService.isAuthenticationSetExternal()){
			waspMessage("user.email_change_confirmed.label");
			return "redirect:/auth/login.do"; 	
		}
		return "redirect:/auth/resetpassword/form.do?authcode="+getUserPasswordAuthcode(user);//sets the password authcode in database (see function above)
	}
	else if (isAdminCreated == 1){
		// check if Userpasswordauth object exists for user who owns currently validated email and if so request change of password
		User user = userDao.getUserByEmail(decodedEmail); //user.email is unique 
		if(user == null){
			logger.warn("Unable to retrieve a Wasp user by email address");
			return "redirect:/auth/confirmemail/confirmemailerror.do";
		}
		Userpasswordauth userpasswordauth = userpasswordauthDao.getUserpasswordauthByUserId(user.getId());
			
		if(userpasswordauth != null && userpasswordauth.getId()!=null && userpasswordauth.getId().intValue()==user.getId().intValue()){
			// request user changes their password
			return "redirect:/auth/resetpassword/form.do?authcode="+userpasswordauth.getAuthcode();
		}
		waspMessage("user.email_change_confirmed.label");
		return "redirect:/auth/login.do";	
	}
	// get here if no ConfirmEmailAuth object for current authcode (maybe already clicked on the link or expired) 
	// and externally authenticating or not admin created

	return "redirect:/auth/confirmemail/confirmemailerror.do";
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
