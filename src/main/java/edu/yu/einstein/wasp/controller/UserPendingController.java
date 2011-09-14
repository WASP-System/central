package edu.yu.einstein.wasp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.validation.Valid;

import nl.captcha.Captcha;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.controller.validator.PasswordValidator;
import edu.yu.einstein.wasp.controller.validator.UserPendingMetaValidatorImpl;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.Userpasswordauth;
import edu.yu.einstein.wasp.service.AuthCodeService;
import edu.yu.einstein.wasp.service.ConfirmEmailAuthService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabPendingMetaService;

import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.PasswordService;
import edu.yu.einstein.wasp.service.impl.ConfirmEmailAuthServiceImpl;

import edu.yu.einstein.wasp.model.MetaHelper;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


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
	private DepartmentService departmentService;

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


	MetaHelper metaHelper = new MetaHelper("userPending", UserPending.class);

	@RequestMapping(value="/newuser", method=RequestMethod.GET)
	public String showNewPendingUserForm(ModelMap m) {
		metaHelper.setBundle(getBundle());

		UserPending userPending = new UserPending();

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
			 ModelMap m) {
		metaHelper.setBundle(getBundle());
		List<UserPendingMeta> userPendingMetaList = metaHelper.getFromRequest(request, UserPendingMeta.class);

		userPendingForm.setUserPendingMeta(userPendingMetaList);

		metaHelper.validate(new UserPendingMetaValidatorImpl(userService, labService), userPendingMetaList, result);
	
		passwordValidator.validate(result, userPendingForm.getPassword(), (String) request.getParameter("password2"), metaHelper.getParentArea(), "password");
		
		if (! result.hasFieldErrors("email")){
			Errors errors=new BindException(result.getTarget(), metaHelper.getParentArea());
			User user = userService.getUserByEmail(userPendingForm.getEmail());
			if (user.getUserId() != 0 ){
				errors.rejectValue("email", metaHelper.getParentArea()+".email_exists.error", metaHelper.getParentArea()+".email_exists.error (no message has been defined for this property)");
			}
			result.addAllErrors(errors);
		}
		
		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspMessage("user.created.error");
			return "auth/newuser/form";
		}
		
		
		String piUserEmail = "";
		
		for (UserPendingMeta meta : userPendingMetaList) {
			if (meta.getK().equals(metaHelper.getArea() + ".primaryuseremail") ) {
				piUserEmail = meta.getV();
				break;
			}
		} 
		Lab lab = labService.getLabByPrimaryUserId(userService.getUserByEmail(piUserEmail).getUserId());
		userPendingForm.setLabId(lab.getLabId());
		userPendingForm.setStatus("PENDING");
		userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 


		UserPending userPendingDb = userPendingService.save(userPendingForm);

		for (UserPendingMeta upm : userPendingMetaList) {
			upm.setUserpendingId(userPendingDb.getUserPendingId());
			userPendingMetaService.save(upm);
		}

		status.setComplete();

		// TODO email PI/LM that a new user is pending
		String authcode = authCodeService.createAuthCode(20);
		ConfirmEmailAuth confirmEmailAuth = new ConfirmEmailAuth();
		confirmEmailAuth.setAuthcode(authcode);
		confirmEmailAuth.setUserpendingId(userPendingDb.getUserPendingId());
		confirmEmailAuthService.merge(confirmEmailAuth);
		emailService.sendPendingUserEmailConfirm(userPendingForm, authcode);
		return "redirect:/auth/newuser/ok.do";
	}

	@RequestMapping(value="/newpi", method=RequestMethod.GET)
	public String showNewPendingPiForm(ModelMap m) {
		metaHelper.setArea("piPending"); 
		metaHelper.setBundle(getBundle());

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
		metaHelper.setBundle(getBundle());
		metaHelper.setArea("piPending");
		
		List<UserPendingMeta> userPendingMetaList = metaHelper.getFromRequest(request, UserPendingMeta.class);
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
			if (meta.getK().equals(metaHelper.getArea() + ".labName")) {
				labPendingForm.setName(meta.getV());
				continue;
			}
			if (meta.getK().equals(metaHelper.getArea() + ".departmentId")) {
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
			  waspMessage("auth.confirmemail.badauthcode.error");
			  return "auth/confirmemail/authcodeform";
		  }
		  
		  ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthService.getConfirmEmailAuthByAuthcode(authCode);
		  if (urlEncodedEmail == null || urlEncodedEmail.isEmpty() || confirmEmailAuth.getUserpendingId() == 0) {
			  waspMessage("auth.confirmemail.bademail.error");
			  return "auth/confirmemail/authcodeform";
		  }
			  
		  UserPending userPending = userPendingService.getUserPendingByUserPendingId(confirmEmailAuth.getUserpendingId());
		  try{
			  if (! userPending.getEmail().equals(URLDecoder.decode(urlEncodedEmail, "UTF-8"))){
				  waspMessage("auth.confirmemail.wronguser.error");
				  return "auth/confirmemail/authcodeform";
			  }
		  } catch(UnsupportedEncodingException e){
			  waspMessage("auth.confirmemail.corruptemail.error");
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
			 waspMessage("auth.confirmemail.badauthcode.error");
			 m.put("email", email);
			 return "auth/confirmemail/authcodeform";
		  }
		 
		  ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthService.getConfirmEmailAuthByAuthcode(authCode);
		  UserPending userPending = userPendingService.getUserPendingByUserPendingId(confirmEmailAuth.getUserpendingId());
		  
		  if (email == null || email.isEmpty() || confirmEmailAuth.getUserpendingId() == 0 ){
			  m.put("authcode", authCode);
			  waspMessage("auth.confirmemail.bademail.error");
			  return "auth/confirmemail/authcodeform";
		  }
		  if (! userPending.getEmail().equals(email) ){
				m.put("authcode", authCode);
				waspMessage("auth.confirmemail.wrongemail.error");
				return "auth/confirmemail/authcodeform";
		  }

		  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			  waspMessage("auth.confirmemail.captcha.error");
			  m.put("authcode", authCode);
			  m.put("email", email);
			  return "auth/confirmemail/authcodeform";
		  }
		  
		  confirmEmailAuthService.remove(confirmEmailAuth);
		  request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		  //TODO:  add logic to make sure email address is confirmed before activating account
		  return "auth/confirmemail/ok";
	  }

}
