package edu.yu.einstein.wasp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
	
	private final Map<String, MetaAttribute.FormVisibility> getUserPendingMetaVisibility(){
		Map<String, MetaAttribute.FormVisibility> userMetaVisibility = new HashMap();
		userMetaVisibility.put("userPending.institution", MetaAttribute.FormVisibility.ignore);
		userMetaVisibility.put("userPending.department", MetaAttribute.FormVisibility.ignore);
		userMetaVisibility.put("userPending.state", MetaAttribute.FormVisibility.ignore);
		userMetaVisibility.put("userPending.city", MetaAttribute.FormVisibility.ignore);
		userMetaVisibility.put("userPending.country", MetaAttribute.FormVisibility.ignore);
		userMetaVisibility.put("userPending.zip", MetaAttribute.FormVisibility.ignore);
		return userMetaVisibility;
	}
	
	@RequestMapping(value="/newuser", method=RequestMethod.GET)
	public String showNewPendingUserForm(ModelMap m) {
		

		UserPending userPending = new UserPending();
		MetaHelper metaHelper = getMetaHelper();
		
		//String msg= DBResourceBundle.MESSAGE_SOURCE.getMessage("userPending.lastName.error", null, Locale.US);
		
		// We wish to get some data from the principle investigator User so hide on the form
		
		
		userPending.setUserPendingMeta(metaHelper.getMasterList(getUserPendingMetaVisibility(), UserPendingMeta.class));
		
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
		userPendingMetaHelper.getFromRequest(request, getUserPendingMetaVisibility(), UserPendingMeta.class);

		

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
			m.put("captchaError", getMessage(getMetaHelper().getParentArea()+".captcha.error"));
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
		
		// create a metahelper object to work with metadata for pi.
		MetaHelper piMetaHelper = new MetaHelper("user", UserMeta.class, request.getSession());
		piMetaHelper.syncWithMaster(userService.getUserByLogin(piUserLogin).getUserMeta()); // get PI meta from database and sync with current properties
		userPendingMetaHelper.setMetaValueByName("institution", piMetaHelper.getMetaByName("institution").getV());
		userPendingMetaHelper.setMetaValueByName("department", piMetaHelper.getMetaByName("department").getV());
		userPendingMetaHelper.setMetaValueByName("state", piMetaHelper.getMetaByName("state").getV());
		userPendingMetaHelper.setMetaValueByName("city", piMetaHelper.getMetaByName("city").getV());
		userPendingMetaHelper.setMetaValueByName("country", piMetaHelper.getMetaByName("country").getV());
		userPendingMetaHelper.setMetaValueByName("zip", piMetaHelper.getMetaByName("zip").getV());
		
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
		return "redirect:/auth/newuser/ok.do";
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
		
		List<UserPendingMeta> userPendingMetaList =  metaHelper.getFromRequest(request, UserPendingMeta.class);
		userPendingForm.setUserPendingMeta(userPendingMetaList);

		 metaHelper.validate(userPendingMetaList, result);

		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspMessage("user.created.error");

			return "auth/newpi/form";
		}

		userPendingForm.setStatus("PENDING");

		userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 

		UserPending userPendingDb = userPendingService.save(userPendingForm);

		for (UserPendingMeta upm : userPendingMetaList) {
			upm.setUserpendingId(userPendingDb.getUserPendingId());
		}
		userPendingMetaService.updateByUserpendingId(userPendingDb.getUserPendingId(), userPendingMetaList);

		// insert into labpending table
		LabPending labPendingForm = new LabPending();
		labPendingForm.setStatus("PENDING");
		labPendingForm.setUserpendingId(userPendingDb.getUserPendingId());

		// TODO DEPARTMENT ID
		labPendingForm.setDepartmentId(1);
		for (UserPendingMeta meta : userPendingMetaList) {
			if (meta.getK().equals( metaHelper.getArea() + ".labName")) {
				labPendingForm.setName(meta.getV());
				continue;
			}
			if (meta.getK().equals( metaHelper.getArea() + ".departmentId")) {
				try{
					labPendingForm.setDepartmentId(Integer.parseInt(meta.getV()));
				} catch (Exception e) {
				}
				continue;
			}
		} 
		LabPending labPendingDb = labPendingService.save(labPendingForm);

		status.setComplete();

		// TODO email DA that a new pi is pending

		waspMessage("hello.error");
		return "redirect:/auth/newpi/ok.do";
	}

	@RequestMapping(value="/confirmemail", method=RequestMethod.GET)
	  public String confirmEmailFromEmailLink(
			  @RequestParam(value="authcode") String authCode,
			  @RequestParam(value="email") String urlEncodedEmail,
		      ModelMap m) {
		  if (authCode == null || authCode.isEmpty()) {
			  waspMessage("auth.confirmemail_badauthcode.error");
			  return "auth/confirmemail/authcodeform";
		  }
		  ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthService.getConfirmEmailAuthByAuthcode(authCode);
		  if (urlEncodedEmail == null || urlEncodedEmail.isEmpty() || confirmEmailAuth.getUserpendingId() == 0) {
			  waspMessage("auth.confirmemail_bademail.error");
			  return "auth/confirmemail/authcodeform";
		  }
			  
		  UserPending userPending = userPendingService.getUserPendingByUserPendingId(confirmEmailAuth.getUserpendingId());
		  try{
			  if (! userPending.getEmail().equals(URLDecoder.decode(urlEncodedEmail, "UTF-8"))){
				  waspMessage("auth.confirmemail_wronguser.error");
				  return "auth/confirmemail/authcodeform";
			  }
		  } catch(UnsupportedEncodingException e){
			  waspMessage("auth.confirmemail_corruptemail.error");
			  return "auth/confirmemail/authcodeform"; 
		  }
		  
		  confirmEmailAuthService.remove(confirmEmailAuth);
		  request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		  return "auth/confirmemail/ok";
	  }
	
	 @RequestMapping(value="/confirmemail", method=RequestMethod.POST)
	  public String confirmEmailFromForm(
	        @RequestParam(value="authcode") String authCode,
			@RequestParam(value="email") String email,
	        @RequestParam(value="captcha_text") String captchaText,
	        ModelMap m) {
		 if (authCode == null || authCode.isEmpty()) {
			 waspMessage("auth.confirmemail_badauthcode.error");
			 m.put("email", email);
			 return "auth/confirmemail/authcodeform";
		  }
		 
		  ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthService.getConfirmEmailAuthByAuthcode(authCode);
		  UserPending userPending = userPendingService.getUserPendingByUserPendingId(confirmEmailAuth.getUserpendingId());
		  
		  if (email == null || email.isEmpty() || confirmEmailAuth.getUserpendingId() == 0 ){
			  m.put("authcode", authCode);
			  waspMessage("auth.confirmemail_bademail.error");
			  return "auth/confirmemail/authcodeform";
		  }
		  if (! userPending.getEmail().equals(email) ){
				m.put("authcode", authCode);
				waspMessage("auth.confirmemail_wronguser.error");
				return "auth/confirmemail/authcodeform";
		  }

		  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			  waspMessage("auth.confirmemail_captcha.error");
			  m.put("authcode", authCode);
			  m.put("email", email);
			  return "auth/confirmemail/authcodeform";
		  }
		  userPending.setStatus("PENDING");
		  userPendingService.save(userPending);
		  confirmEmailAuthService.remove(confirmEmailAuth);
		  request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		  //TODO:  add logic to make sure email address is confirmed before activating account
		  return "auth/confirmemail/ok";
	  }

}
