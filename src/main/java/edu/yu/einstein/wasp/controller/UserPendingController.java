package edu.yu.einstein.wasp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import nl.captcha.Captcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import edu.yu.einstein.wasp.controller.validator.PasswordValidator;
import edu.yu.einstein.wasp.controller.validator.UserPendingMetaValidatorImpl;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaHelper;
import edu.yu.einstein.wasp.model.MetaHelper.WaspMetadataException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.AuthCodeService;
import edu.yu.einstein.wasp.service.ConfirmEmailAuthService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.LabPendingMetaService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.PasswordService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.service.UserPendingService;


@Controller
@Transactional
@RequestMapping("/auth")
public class UserPendingController extends WaspController {

	@Autowired
	private UserPendingService userPendingService;

	@Autowired
	private UserPendingMetaService userPendingMetaService;

	@Autowired
	private ConfirmEmailAuthService confirmEmailAuthService;
	
	@Autowired
	private LabService labService;

	@Autowired
	private LabPendingService labPendingService;

	@Autowired
	private LabPendingMetaService labPendingMetaService;

	

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordService passwordService;

	
	@Autowired
	private PasswordValidator passwordValidator;
	
	@Autowired
	private AuthCodeService authCodeService;
	
	/**
	 *
	 */

	private final MetaHelper getMetaHelper() {
		return new MetaHelper("userPending", UserPendingMeta.class, request.getSession());
	}
	
		
	
	@RequestMapping(value="/newuser", method=RequestMethod.GET)
	public String showNewPendingUserForm(ModelMap m) {
		

		UserPending userPending = new UserPending();
		MetaHelper metaHelper = getMetaHelper();
		
		//String msg= DBResourceBundle.MESSAGE_SOURCE.getMessage("userPending.lastName.error", null, Locale.US);
		
		// We wish to get some data from the principal investigator User so hide on the form
		
		
		userPending.setUserPendingMeta(metaHelper.getMasterList(UserPendingMeta.class));
		
		m.addAttribute(metaHelper.getParentArea(), userPending);
		prepareSelectListData(m);

		return "auth/newuser/form";

	}

	@RequestMapping(value="/newuser", method=RequestMethod.POST)
	public String createNewPendingUser (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) throws WaspMetadataException {
		
		
		MetaHelper userPendingMetaHelper = getMetaHelper();
		userPendingMetaHelper.getFromRequest(request, UserPendingMeta.class);
		userPendingMetaHelper.validate(new UserPendingMetaValidatorImpl(userService, labService), result);

		passwordValidator.validate(result, userPendingForm.getPassword(), (String) request.getParameter("password2"), userPendingMetaHelper.getParentArea(), "password");
		
		if (! result.hasFieldErrors("email")){
			User user = userService.getUserByEmail(userPendingForm.getEmail());
			if (user.getUserId() != 0 ){
				Errors errors=new BindException(result.getTarget(), userPendingMetaHelper.getParentArea());
				errors.rejectValue("email", userPendingMetaHelper.getParentArea()+".email_exists.error", userPendingMetaHelper.getParentArea()+".email_exists.error (no message has been defined for this property)");
				result.addAllErrors(errors);
			}
		}
		
		// validate captcha
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		String captchaText = (String) request.getParameter("captcha");
		if (captcha == null || captchaText == null || captchaText.isEmpty() || (! captcha.isCorrect(captchaText)) ){
			m.put("captchaError", getMessage(userPendingMetaHelper.getParentArea()+".captcha.error"));
		}
		
		if (result.hasErrors() || m.containsKey("captchaError")) {
			userPendingForm.setUserPendingMeta((List<UserPendingMeta>) userPendingMetaHelper.getMetaList());
			prepareSelectListData(m);
			
			waspMessage("user.created.error");
			return "auth/newuser/form";
		}
		
		// form completed properly so continue		
		String piUserLogin = userPendingMetaHelper.getMetaByName("primaryuserid").getV();
				
		Lab lab = labService.getLabByPrimaryUserId(userService.getUserByLogin(piUserLogin).getUserId());
		userPendingForm.setLabId(lab.getLabId());
		userPendingForm.setStatus("WAIT_EMAIL"); // await email confirmation
		userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 
		
		
		
		List<UserPendingMeta> userPendingMetaList = (List<UserPendingMeta>) userPendingMetaHelper.getMetaList();
		userPendingForm.setUserPendingMeta(userPendingMetaList);

		UserPending userPendingDb = userPendingService.save(userPendingForm);

		for (UserPendingMeta upm : userPendingMetaList) {
			upm.setUserpendingId(userPendingDb.getUserPendingId());
			userPendingMetaService.save(upm);
		}
		
		String authcode = authCodeService.createAuthCode(20);
		ConfirmEmailAuth confirmEmailAuth = new ConfirmEmailAuth();
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserpendingId(userPendingDb.getUserPendingId());
		confirmEmailAuthService.merge(confirmEmailAuth);
		emailService.sendPendingUserEmailConfirm(userPendingForm, authcode);
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		status.setComplete();
		return "redirect:/auth/newuser/created.do";
	}

	@RequestMapping(value="/newpi", method=RequestMethod.GET)
	public String showNewPendingPiForm(ModelMap m) {
		MetaHelper metaHelper=getMetaHelper();
		metaHelper.setArea("piPending");
		
		UserPending userPending = new UserPending();
		userPending.setUserPendingMeta(metaHelper.getMasterList(UserPendingMeta.class));

		m.addAttribute(metaHelper.getParentArea(), userPending);
		prepareSelectListData(m);

		return "auth/newpi/form";

	}

	@RequestMapping(value="/newpi", method=RequestMethod.POST)
	public String createNewPendingPi (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) {
		
		MetaHelper metaHelper=getMetaHelper();
		
		metaHelper.setArea("piPending"); 
		metaHelper.getFromRequest(request, UserPendingMeta.class);
		metaHelper.validate(result);
		passwordValidator.validate(result, userPendingForm.getPassword(), (String) request.getParameter("password2"), metaHelper.getParentArea(), "password");

		// validate captcha
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		String captchaText = (String) request.getParameter("captcha");
		if (captcha == null || captchaText == null || captchaText.isEmpty() || (! captcha.isCorrect(captchaText)) ){
			m.put("captchaError", getMessage(metaHelper.getParentArea()+".captcha.error"));
		}
		
		if (result.hasErrors() || m.containsKey("captchaError")) {
			userPendingForm.setUserPendingMeta((List<UserPendingMeta>) metaHelper.getMetaList());
			prepareSelectListData(m);
			waspMessage("user.created.error");

			return "auth/newpi/form";
		}

		userPendingForm.setStatus("WAIT_EMAIL");

		userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 

		UserPending userPendingDb = userPendingService.save(userPendingForm);
		List<UserPendingMeta> userPendingMetaList = (List<UserPendingMeta>) metaHelper.getMetaList();
		for (UserPendingMeta upm : userPendingMetaList) {
			upm.setUserpendingId(userPendingDb.getUserPendingId());
		}
		userPendingMetaService.updateByUserpendingId(userPendingDb.getUserPendingId(), userPendingMetaList);
		String authcode = authCodeService.createAuthCode(20);
		ConfirmEmailAuth confirmEmailAuth = new ConfirmEmailAuth();
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserpendingId(userPendingDb.getUserPendingId());
		confirmEmailAuthService.merge(confirmEmailAuth);
		emailService.sendPendingPIEmailConfirm(userPendingForm, authcode);
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		status.setComplete();
		return "redirect:/auth/newpi/created.do";
	}
	
	protected boolean userPendingEmailValid(String authCode, String email, ModelMap m) {
		if (authCode == null || authCode.isEmpty()) {
			waspMessage("auth.confirmemail_badauthcode.error");
			m.put("email", email);
			return false;
		}
		ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthService.getConfirmEmailAuthByAuthcode(authCode);
		if (email == null || email.isEmpty() || confirmEmailAuth.getUserpendingId() == 0) {
			waspMessage("auth.confirmemail_bademail.error");
			m.put("authcode", authCode);
			return false;
		}
			  
		UserPending userPending = userPendingService.getUserPendingByUserPendingId(confirmEmailAuth.getUserpendingId());

		if (! userPending.getEmail().equals(email)){
			waspMessage("auth.confirmemail_wronguser.error");
			return false;
		}
		return true;
	}
		
	protected void sendPendingUserConfRequestEmail(String email) throws WaspMetadataException{
		// now find any existing userPending instances with same email and process them too
		Map userPendingQueryMap = new HashMap();
		userPendingQueryMap.put("email", email);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		List<UserPending> userPendingList = userPendingService.findByMap(userPendingQueryMap);
		for (UserPending up: userPendingList){
			ConfirmEmailAuth auth = confirmEmailAuthService.getConfirmEmailAuthByUserpendingId(up.getUserPendingId());
			if (auth.getUserpendingId() != 0){
				confirmEmailAuthService.remove(auth);
			}
			up.setStatus("PENDING");
			userPendingService.save(up);
			if (up.getLabId() == null){
				// email DA that a new pi is pending
				emailService.sendPendingPrincipalConfirmRequest(getNewLabPending(up));
			} else {
				// email PI of selected lab
				emailService.sendPendingUserConfirmRequest(up);
			}
		}
	}
	
	protected LabPending getNewLabPending(UserPending userPending) throws WaspMetadataException{
		MetaHelper userPendingMetaHelper=getMetaHelper();
		userPendingMetaHelper.setArea("piPending");
		userPendingMetaHelper.syncWithMaster(userPending.getUserPendingMeta());
		LabPending labPending = new LabPending();
		labPending.setStatus("PENDING");
		labPending.setUserpendingId(userPending.getUserPendingId());
		int departmentId = Integer.parseInt(userPendingMetaHelper.getMetaByName("department").getV());
		labPending.setDepartmentId(departmentId);
		String labName = userPendingMetaHelper.getMetaByName("labName").getV();
		labPending.setName(labName);
		LabPending labPendingDb = labPendingService.save(labPending);
		return labPendingDb;
	}

	@RequestMapping(value="/confirmUserEmail", method=RequestMethod.GET)
	  public String confirmUserEmailFromEmailLink(
			  @RequestParam(value="authcode") String authCode,
			  @RequestParam(value="email") String urlEncodedEmail,
		      ModelMap m) throws WaspMetadataException {
		String decodedEmail;
		try{
			decodedEmail = URLDecoder.decode(urlEncodedEmail, "UTF-8");
		} catch(UnsupportedEncodingException e){
			waspMessage("auth.confirmemail_corruptemail.error");
			return "auth/confirmemail/authcodeform"; 
		}
		Map userPendingQueryMap = new HashMap();
		userPendingQueryMap.put("email", decodedEmail);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		if (userPendingService.findByMap(userPendingQueryMap).isEmpty()){
			// email already confirmed probably accidently re-confirming
			return "redirect:/auth/newuser/emailok.do";
		}
		if (! userPendingEmailValid(authCode, decodedEmail, m)) return "auth/confirmemail/authcodeform";
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		sendPendingUserConfRequestEmail(decodedEmail);
		return "redirect:/auth/newuser/emailok.do";
	}
	
	 @RequestMapping(value="/confirmUserEmail", method=RequestMethod.POST)
	  public String confirmUserEmailFromForm(
	        @RequestParam(value="authcode") String authCode,
			@RequestParam(value="email") String email,
	        @RequestParam(value="captcha_text") String captchaText,
	        ModelMap m) throws WaspMetadataException {
		 
		  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			  waspMessage("auth.confirmemail_captcha.error");
			  m.put("authcode", authCode);
			  m.put("email", email);
			  return "auth/confirmemail/authcodeform";
		  }
		  if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";
		  sendPendingUserConfRequestEmail(email);
		  return "redirect:/auth/newuser/emailok.do";
	  }

	 @RequestMapping(value="/confirmPIEmail", method=RequestMethod.GET)
	 public String confirmPIEmailFromEmailLink(
			  @RequestParam(value="authcode") String authCode,
			  @RequestParam(value="email") String urlEncodedEmail,
		      ModelMap m) throws WaspMetadataException {
		 String decodedEmail;
		 try{
			 decodedEmail = URLDecoder.decode(urlEncodedEmail, "UTF-8");
		 } catch(UnsupportedEncodingException e){
			 waspMessage("auth.confirmemail_corruptemail.error");
			 return "auth/confirmemail/authcodeform"; 
		 }
		 Map userPendingQueryMap = new HashMap();
		 userPendingQueryMap.put("email", decodedEmail);
		 userPendingQueryMap.put("status", "WAIT_EMAIL");
		 if (userPendingService.findByMap(userPendingQueryMap).isEmpty()){
			 // email already confirmed probably accidently re-confirming
			 return "redirect:/auth/newuser/emailok.do";
		 }	  
		 if (! userPendingEmailValid(authCode, decodedEmail, m)) return "auth/confirmemail/authcodeform";
		 sendPendingUserConfRequestEmail(decodedEmail);
		 return "redirect:/auth/newpi/emailok.do";
	}
	 
	@RequestMapping(value="/confirmPIEmail", method=RequestMethod.POST)
	public String confirmPIEmailFromForm(
			@RequestParam(value="authcode") String authCode,
			@RequestParam(value="email") String email,
			@RequestParam(value="captcha_text") String captchaText,
			ModelMap m) throws WaspMetadataException {
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			waspMessage("auth.confirmemail_captcha.error");
			m.put("authcode", authCode);
			m.put("email", email);
			return "auth/confirmemail/authcodeform";
		}

		if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";
		sendPendingUserConfRequestEmail(email);
		return "redirect:/auth/newpi/emailok.do";
	}
}
