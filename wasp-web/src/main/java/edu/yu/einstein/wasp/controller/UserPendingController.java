package edu.yu.einstein.wasp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
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

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.controller.validator.PasswordValidator;
import edu.yu.einstein.wasp.controller.validator.UserPendingMetaValidatorImpl;
import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.LabPendingMetaDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.dao.UserPendingMetaDao;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabPendingMeta;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.WebAuthenticationService;
import edu.yu.einstein.wasp.util.StringHelper;

/**
 * 
 * Controller for handling request mappings for pending user and pending PI applications
 *
 */
@Controller
@Transactional
@RequestMapping("/auth")
public class UserPendingController extends WaspController {

	@Autowired
	private UserPendingDao userPendingDao;

	@Autowired
	private UserPendingMetaDao userPendingMetaDao;

	@Autowired
	private ConfirmEmailAuthDao confirmEmailAuthDao;
	
	@Autowired
	private LabDao labDao;

	@Autowired
	private LabPendingDao labPendingDao;
	
	@Autowired
	private LabPendingMetaDao labPendingMetaDao;

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordValidator passwordValidator;
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private WebAuthenticationService webAuthenticationService;
	
		
	/**
	 * get a @{link MetaHelperWebapp} instance for working with userPending metadata
	 * @return
	 */
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(UserPendingMeta.class, request.getSession());
	}
	
		
	/**
	 * Create model for a new user application form view based on the current userPending metadata in the uifield properties table
	 * @param m model
	 * @return view
	 */
	@RequestMapping(value="/newuser", method=RequestMethod.GET)
	public String showNewPendingUserForm(ModelMap m) {
		

		UserPending userPending = new UserPending();
		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();	
		
		userPending.setUserPendingMeta(metaHelperWebapp.getMasterList(UserPendingMeta.class));
		m.put("isAuthenticationExternal", webAuthenticationService.isAuthenticationSetExternal());
		m.addAttribute(metaHelperWebapp.getParentArea(), userPending);
		prepareSelectListData(m);

		return "auth/newuser/form";

	}

	/**
	 * Validate posted form with bound {@link UserPending} data
	 * @param userPendingForm
	 * @param result
	 * @param status
	 * @param m model
	 * @return view
	 * @throws MetadataException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/newuser", method=RequestMethod.POST)
	public String createNewPendingUser (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) throws MetadataException {
		
		
		MetaHelperWebapp userPendingMetaHelperWebapp = getMetaHelperWebapp();
		userPendingMetaHelperWebapp.getFromRequest(request, UserPendingMeta.class);
		userPendingMetaHelperWebapp.validate(new UserPendingMetaValidatorImpl(userDao, labDao), result);
		if (! result.hasFieldErrors("login")){
			if (webAuthenticationService.isAuthenticationSetExternal()){
				if (! webAuthenticationService.authenticate(userPendingForm.getLogin(), userPendingForm.getPassword())){
					Errors errors=new BindException(result.getTarget(), userPendingMetaHelperWebapp.getParentArea());
					errors.rejectValue("login", userPendingMetaHelperWebapp.getParentArea()+".external_authentication.error", userPendingMetaHelperWebapp.getParentArea()+".external_authentication.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				} else {
					// user is now authenticated with single role 'ag' (authenticated guest) log them out again
					webAuthenticationService.logoutUser();
				}
			}
			else {
				// using internal authentication
				if (! webAuthenticationService.isLoginNameWellFormed(userPendingForm.getLogin())){
					Errors errors=new BindException(result.getTarget(), userPendingMetaHelperWebapp.getParentArea());
					errors.rejectValue("login", userPendingMetaHelperWebapp.getParentArea()+".login_malformed.error", userPendingMetaHelperWebapp.getParentArea()+".login_malformed.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				}
				else{
					// login name correctly formed
					try {
						if (webAuthenticationService.isLoginAlreadyInUse(userPendingForm.getLogin(), userPendingForm.getEmail())){
							Errors errors=new BindException(result.getTarget(), userPendingMetaHelperWebapp.getParentArea());
							errors.rejectValue("login", userPendingMetaHelperWebapp.getParentArea()+".login_exists.error", userPendingMetaHelperWebapp.getParentArea()+".login_exists.error (no message has been defined for this property)");
							result.addAllErrors(errors);
						}
					} catch(LoginNameException e){
						Errors errors=new BindException(result.getTarget(), userPendingMetaHelperWebapp.getParentArea());
						errors.rejectValue("login", userPendingMetaHelperWebapp.getParentArea()+".login_malformed.error", userPendingMetaHelperWebapp.getParentArea()+".login_malformed.error (no message has been defined for this property)");
						result.addAllErrors(errors);
					}
				}
			}
		}
		if (!webAuthenticationService.isAuthenticationSetExternal()){
			passwordValidator.validate(result, userPendingForm.getPassword(), request.getParameter("password2"), userPendingMetaHelperWebapp.getParentArea(), "password");
		}
		if (! result.hasFieldErrors("email")){
			User user = userDao.getUserByEmail(userPendingForm.getEmail());
			//NV 12132011
			//if (user.getUserId() != null ){
			if (user.getUserId() != null ){

				Errors errors=new BindException(result.getTarget(), userPendingMetaHelperWebapp.getParentArea());
				errors.rejectValue("email", userPendingMetaHelperWebapp.getParentArea()+".email_exists.error", userPendingMetaHelperWebapp.getParentArea()+".email_exists.error (no message has been defined for this property)");
				result.addAllErrors(errors);
			}
		}
		
		// validate captcha
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		
		String captchaText = request.getParameter("captcha");
		/* NV commented for testing
		if (captcha == null || captchaText == null || captchaText.isEmpty() || (! captcha.isCorrect(captchaText)) ){
			m.put("captchaError", messageService.getMessage(userPendingMetaHelperWebapp.getParentArea()+".captcha.error"));
		}
		*/
		if (result.hasErrors() || m.containsKey("captchaError")) {
			userPendingForm.setUserPendingMeta((List<UserPendingMeta>) userPendingMetaHelperWebapp.getMetaList());
			prepareSelectListData(m);
			m.put("isAuthenticationExternal", webAuthenticationService.isAuthenticationSetExternal());
			waspErrorMessage("user.created.error");
			return "auth/newuser/form";
		}
		

		// form passes validation so finalize and persist userPending data and metadata	
		if (isInDemoMode)
			request.getSession().setAttribute("demoEmail", userPendingForm.getEmail());
		//NOTE: AS OF 02-04-2013, metadata primaryuserid on the userpending web form is requesting the email address of the PI whose lab the new user wants to join
		//String piUserLogin = userPendingMetaHelperWebapp.getMetaByName("primaryuserid").getV();	//replaced by next line, 02-04-2013
		String piUserEmail = userPendingMetaHelperWebapp.getMetaByName("primaryuserid").getV();
		
		//reset this meta primaryuserid back to the PI's login name (rather than the pi's email), so that everything downstream from here is maintained as before
		userPendingMetaHelperWebapp.getMetaByName("primaryuserid").setV(userDao.getUserByEmail(piUserEmail).getLogin());

		
		//Lab lab = labDao.getLabByPrimaryUserId(userDao.getUserByLogin(piUserLogin).getUserId()); //replaced by next line, 02-04-2013
		Lab lab = labDao.getLabByPrimaryUserId(userDao.getUserByEmail(piUserEmail).getUserId());
		userPendingForm.setLabId(lab.getLabId());
		userPendingForm.setStatus("WAIT_EMAIL"); // set to WAIT_EMAIL even if isEmailApproved == true or sendPendingUserConfRequestEmail() won't work properly
				
		userPendingForm.setPassword( webAuthenticationService.encodePassword(userPendingForm.getPassword()) ); 
				
		List<UserPendingMeta> userPendingMetaList = (List<UserPendingMeta>) userPendingMetaHelperWebapp.getMetaList();
		userPendingForm.setUserPendingMeta(userPendingMetaList);
		userPendingForm.setFirstName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getFirstName()));
		userPendingForm.setLastName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getLastName()));
		UserPending userPendingDb = userPendingDao.save(userPendingForm);
		try{
			userPendingMetaDao.setMeta(userPendingMetaList, userPendingDb.getUserPendingId());
		} catch (MetadataException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("user.created.error");
			return "auth/newuser/form";
		}
		
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		status.setComplete();
		
		if (isUserPendingEmailApproved(userPendingDb.getEmail())){ // is email address already confrimed
			sendPendingUserConfRequestEmail(userPendingDb.getEmail()); // email PI to inform of pending user
			return "redirect:/auth/newuser/emailok.do";
		} else {
			// email address not confirmed yet so request confirmation
			emailService.sendPendingUserEmailConfirm(userPendingForm, userService.getNewAuthcodeForUserPending(userPendingForm));
		}
		return "redirect:/auth/newuser/created.do";
	}
	
	/**
	 * If any {@link userPending} instances with same email are not in state 'WAIT_EMAIL' we assume the email address is already confirmed 
	 * @param email
	 * @return boolean = email address is/is not confirmed
	 */
	private boolean isUserPendingEmailApproved(String email){
		// see if pending user has already confirmed their email
		Map<String, String> confirmedEmailQueryMap = new HashMap<String, String>();
		confirmedEmailQueryMap.put("email", email);
		for (UserPending up: userPendingDao.findByMap(confirmedEmailQueryMap)){
			// if any userPending instances with same email are not in state WAIT_EMAIL we assume the email address
			// is already confirmed
			if (!up.getStatus().equals("WAIT_EMAIL")){
				return true;
			}
				
		}
		return false;
	}
	
	/**
	 * Display form to applying PI to obtain institute to be joined from GET
	 * @param m
	 * @return view
	 */
	@RequestMapping(value="/newpi/institute", method=RequestMethod.GET)
	public String selectPiInstitute(ModelMap m) {
		String internalInstituteList = messageService.getMessage("piPending.internal_institute_list.data");
		List<String> instituteList = new ArrayList<String>();
		Collections.addAll(instituteList,internalInstituteList.split(";")); 
		m.addAttribute("instituteList", instituteList);
		return "auth/newpi/institute";
	}
	
	/**
	 * Display form to applying PI to obtain institute to be joined from POST
	 * @param instituteSelect
	 * @param instituteOther
	 * @param m
	 * @return view
	 * @throws MetadataException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/newpi/institute", method=RequestMethod.POST)
	public String selectPiInstitute(
			@RequestParam(value="instituteSelect") String instituteSelect,
			@RequestParam(value="instituteOther", required = false) String instituteOther,
			ModelMap m) throws MetadataException {
		String internalInstituteList = messageService.getMessage("piPending.internal_institute_list.data");
		List<String> instituteList = new ArrayList<String>();
		Collections.addAll(instituteList,internalInstituteList.split(";")); 
		m.addAttribute("instituteList", instituteList);
		if ( (instituteSelect == null || instituteSelect.equals("other") || instituteSelect.isEmpty()) && (instituteOther == null || instituteOther.isEmpty()) ){
			waspErrorMessage("piPending.institute_not_selected.error");
			return "auth/newpi/institute";
		}
		if ( instituteSelect != null && !instituteSelect.equals("other")  && !instituteSelect.isEmpty() && instituteOther != null && !instituteOther.isEmpty() ){
			waspErrorMessage("piPending.institute_multi_select.error");
			return "auth/newpi/institute";
		}
		
		MetaHelperWebapp metaHelperWebapp=getMetaHelperWebapp();
		metaHelperWebapp.setArea("piPending");
		String instituteName = "";
		Map<String, MetaAttribute.FormVisibility> visibilityElementMap = new HashMap<String, MetaAttribute.FormVisibility>();
		visibilityElementMap.put("piPending.institution", MetaAttribute.FormVisibility.immutable);
		if (instituteSelect != null && !instituteSelect.equals("other")){
			// internal institute
			instituteName = instituteSelect;
			metaHelperWebapp.getMasterList(visibilityElementMap, UserPendingMeta.class);
		} else {
			// external institute
			instituteName = StringHelper.removeExtraSpacesAndCapFirstLetter(instituteOther);
			visibilityElementMap.put("piPending.departmentId", MetaAttribute.FormVisibility.immutable);
			metaHelperWebapp.getMasterList(visibilityElementMap, UserPendingMeta.class);
			Map<String, Integer> departmentQueryMap = new HashMap<String, Integer>();
			departmentQueryMap.put("isInternal", 0);
			List<Department> extDepartments = departmentDao.findByMap(departmentQueryMap);
			if (extDepartments.isEmpty() || extDepartments.size() >1 ){
				throw new MetadataException("Either 0 or >1 external departments defined in the database. Should only be 1");
			}
			metaHelperWebapp.setMetaValueByName("departmentId", Integer.toString(extDepartments.get(0).getDepartmentId()));
		}
		metaHelperWebapp.setMetaValueByName("institution", instituteName);
		
		UserPending userPending = new UserPending();
		userPending.setUserPendingMeta((List<UserPendingMeta>) metaHelperWebapp.getMetaList());
		m.addAttribute(metaHelperWebapp.getParentArea(), userPending);
		
		// save visibility map to session in order to use it later
		request.getSession().setAttribute("visibilityElementMap", visibilityElementMap);
		prepareSelectListData(m, metaHelperWebapp);
		m.put("isAuthenticationExternal", webAuthenticationService.isAuthenticationSetExternal());
		return "auth/newpi/form";

	}
	

	/**
	 * Validate posted form with bound {@link UserPending} data
	 * @param userPendingForm
	 * @param result
	 * @param status
	 * @param m model
	 * @return view
	 * @throws MetadataException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/newpi/form", method=RequestMethod.POST)
	public String createNewPendingPi (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) throws MetadataException {
		
		MetaHelperWebapp metaHelperWebapp=getMetaHelperWebapp();
		
		metaHelperWebapp.setArea("piPending"); 
		// get the visibilityElement Map previously saved in session
		Map<String, MetaAttribute.FormVisibility> visibilityElementMap = (Map<String, MetaAttribute.FormVisibility>) request.getSession().getAttribute("visibilityElementMap");
		metaHelperWebapp.getFromRequest(request, visibilityElementMap, UserPendingMeta.class);
		metaHelperWebapp.validate(result);
		
		if (! result.hasFieldErrors("login")){
			if (webAuthenticationService.isAuthenticationSetExternal()){
				if (! webAuthenticationService.authenticate(userPendingForm.getLogin(), userPendingForm.getPassword())){
					Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
					errors.rejectValue("login", metaHelperWebapp.getParentArea()+".external_authentication.error", metaHelperWebapp.getParentArea()+".external_authentication.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				} else {
					// user is now authenticated with single role 'ag' (authenticated guest) log them out again
					webAuthenticationService.logoutUser();
				}
			}
			else {
				// using internal authentication
				if (! webAuthenticationService.isLoginNameWellFormed(userPendingForm.getLogin())){
					Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
					errors.rejectValue("login", metaHelperWebapp.getParentArea()+".login_malformed.error", metaHelperWebapp.getParentArea()+".login_malformed.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				}
				else{
					// login name correctly formed
					try {
						if (webAuthenticationService.isLoginAlreadyInUse(userPendingForm.getLogin(), userPendingForm.getEmail())){
							Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
							errors.rejectValue("login", metaHelperWebapp.getParentArea()+".login_exists.error", metaHelperWebapp.getParentArea()+".login_exists.error (no message has been defined for this property)");
							result.addAllErrors(errors);
						}
					} catch(LoginNameException e){
						Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
						errors.rejectValue("login", metaHelperWebapp.getParentArea()+".login_malformed.error", metaHelperWebapp.getParentArea()+".login_malformed.error (no message has been defined for this property)");
						result.addAllErrors(errors);
					}
				}
			}
		}
		if (!webAuthenticationService.isAuthenticationSetExternal()){
			passwordValidator.validate(result, userPendingForm.getPassword(), request.getParameter("password2"), metaHelperWebapp.getParentArea(), "password");
		}
		
		if (! result.hasFieldErrors("email")){
			User user = userDao.getUserByEmail(userPendingForm.getEmail());

			//NV 12132011
			if (user.getUserId() != null){

				Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
				errors.rejectValue("email", metaHelperWebapp.getParentArea()+".email_exists.error", metaHelperWebapp.getParentArea()+".email_exists.error (no message has been defined for this property)");
				result.addAllErrors(errors);
			}
		}
		
		// validate captcha
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		
		String captchaText = request.getParameter("captcha");
		/* NV commented for testing
		if (captcha == null || captchaText == null || captchaText.isEmpty() || (! captcha.isCorrect(captchaText)) ){
			m.put("captchaError", messageService.getMessage(metaHelperWebapp.getParentArea()+".captcha.error"));
		}
		*/
		if (result.hasErrors() || m.containsKey("captchaError")) {
			userPendingForm.setUserPendingMeta((List<UserPendingMeta>) metaHelperWebapp.getMetaList());
			prepareSelectListData(m, metaHelperWebapp);
			m.put("isAuthenticationExternal", webAuthenticationService.isAuthenticationSetExternal());
			waspErrorMessage("user.created.error");
			
			return "auth/newpi/form";
		}
		if (isInDemoMode)
			request.getSession().setAttribute("demoEmail", userPendingForm.getEmail());
		userPendingForm.setStatus("WAIT_EMAIL");

		userPendingForm.setPassword( webAuthenticationService.encodePassword(userPendingForm.getPassword()) ); 
		userPendingForm.setFirstName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getFirstName()));
		userPendingForm.setLastName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getLastName()));
		UserPending userPendingDb = userPendingDao.save(userPendingForm);
		List<UserPendingMeta> userPendingMetaList = (List<UserPendingMeta>) metaHelperWebapp.getMetaList();
		try{
			userPendingMetaDao.setMeta(userPendingMetaList, userPendingDb.getUserPendingId());
		} catch (MetadataException e){
			logger.warn(e.getLocalizedMessage());
		}
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		request.getSession().removeAttribute("visibilityElementMap"); // remove visibilityElementMap from the session
		status.setComplete();
		
		if (isUserPendingEmailApproved(userPendingDb.getEmail())){ // is email already confirmed
			sendPendingUserConfRequestEmail(userPendingDb.getEmail()); // email DA to inform of pending lab request
			return "redirect:/auth/newpi/emailok.do";
		} else {
			// email address not confirmed yet so request confirmation
			emailService.sendPendingPIEmailConfirm(userPendingForm, userService.getNewAuthcodeForUserPending(userPendingForm));
		}
		return "redirect:/auth/newpi/created.do";
	}
	
	/**
	 * Validates a given email address and authCode.
	 * @param authCode
	 * @param email
	 * @param m model(can be null)
	 * @return is valid result (true / false)
	 */
	protected boolean userPendingEmailValid(String authCode, String email, ModelMap m) {
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
			  
		UserPending userPending = userPendingDao.getUserPendingByUserPendingId(confirmEmailAuth.getUserPendingId());

		if (! userPending.getEmail().equals(email)){
			waspErrorMessage("auth.confirmemail_wronguser.error");
			return false;
		}
		return true;
	}
		
	/**
	 * After confirmation of a pending user's email address, email PIs of any labs for which joining is request and  
	 * administrators of any departments for which a new labs are requested.  
	 * @param email
	 * @throws MetadataException
	 */
	protected void sendPendingUserConfRequestEmail(String email) throws MetadataException{
		// now find any existing userPending instances with same email and process them too
		Map<String, String> userPendingQueryMap = new HashMap<String, String>();
		userPendingQueryMap.put("email", email);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		List<UserPending> userPendingList = userPendingDao.findByMap(userPendingQueryMap);
		// consider email confirmed for ALL pending user and lab applications linked to this validated email address
		for (UserPending up: userPendingList){
			ConfirmEmailAuth auth = confirmEmailAuthDao.getConfirmEmailAuthByUserpendingId(up.getUserPendingId());
			if (auth.getConfirmEmailAuthId() != null){
				confirmEmailAuthDao.remove(auth);
			}
			up.setStatus("PENDING");
			userPendingDao.save(up);
			if (up.getLabId() == null){
				// email DA that a new pi is pending
				emailService.sendPendingPrincipalConfirmRequest(getNewLabPending(up));
			} else {
				// email PI of selected lab
				emailService.sendPendingUserConfirmRequest(up);
			}
		}
	}
	
	/**
	 * Create, persist and return a {@link LabPending} object for a given {@link UserPending} instance.
	 * @param userPending
	 * @return {@link LabPending}
	 * @throws MetadataException
	 */
	@SuppressWarnings("unchecked")
	protected LabPending getNewLabPending(UserPending userPending) throws MetadataException{
		MetaHelperWebapp userPendingMetaHelperWebapp=getMetaHelperWebapp();
		userPendingMetaHelperWebapp.setArea("piPending");		
		List<UserPendingMeta> userPendingMetaList = null;
		userPendingMetaList = userPending.getUserPendingMeta();//11-08-12 (Rob): this hibernate-type call will not work if the userPending object was just now created (second request by a PI that has confirmed email address) and not completely committed
		if(userPendingMetaList==null){
			Map<String, Integer> filterMap = new HashMap<String, Integer>();
			filterMap.put("userPendingId", userPending.getUserPendingId());
			userPendingMetaList = userPendingMetaDao.findByMap(filterMap);
			List<UserPendingMeta> toRemoveList = new ArrayList<UserPendingMeta>();
			for(UserPendingMeta upm : userPendingMetaList){
				if(!upm.getK().startsWith("piPending")){
					toRemoveList.add(upm);
				}
			}
			userPendingMetaList.removeAll(toRemoveList);
		}
		userPendingMetaHelperWebapp.syncWithMaster(userPendingMetaList);
		LabPending labPending = new LabPending();
		labPending.setStatus("PENDING");
		labPending.setUserpendingId(userPending.getUserPendingId());
		int departmentId = Integer.parseInt(userPendingMetaHelperWebapp.getMetaByName("departmentId").getV());
		labPending.setDepartmentId(departmentId);
		String labName = userPendingMetaHelperWebapp.getMetaByName("labName").getV();
		labPending.setName(labName);
		LabPending labPendingDb = labPendingDao.save(labPending);
		// copies address meta data from PI userMeta to labMeta as billing address info. 
		MetaHelperWebapp labPendingMetaHelperWebapp = new MetaHelperWebapp(LabPendingMeta.class, request.getSession());
		List<LabPendingMeta> labPendingMetaList = labPendingMetaHelperWebapp.getMasterList(LabPendingMeta.class);
				
		// fill up labpending metadata using information from userpending metadata
		for (LabPendingMeta lpm: labPendingMetaList) {
			
			// get name from prefix by removing area 
			if (lpm.getK().contains("labPending.billing_")){
				String sourceName = lpm.getK().replaceAll("labPending\\.billing_", "");
				String targetName = "billing_" + sourceName;
				try{
					if (targetName.equals("billing_contact")){
						String contactName = userPending.getFirstName() + " " + userPending.getLastName();
						labPendingMetaHelperWebapp.setMetaValueByName(targetName, contactName);
					} else{
						labPendingMetaHelperWebapp.setMetaValueByName(targetName, userPendingMetaHelperWebapp.getMetaByName(sourceName).getV());
						// see if we need to use this metadata to populate other fields
						if (sourceName.equals("phone") || sourceName.equals("building_room")){
							targetName = sourceName;
							labPendingMetaHelperWebapp.setMetaValueByName(targetName, userPendingMetaHelperWebapp.getMetaByName(sourceName).getV());
						} else if (sourceName.equals("departmentId")){
							String internalExternal = (
									departmentDao.findById( Integer.valueOf( userPendingMetaHelperWebapp.getMetaByName(sourceName).getV() ) )
											.getIsInternal().intValue() == 1) ? "internal" : "external";
							targetName = "internal_external_lab";
							labPendingMetaHelperWebapp.setMetaValueByName(targetName, internalExternal);
						}
					}
				} catch (MetadataException e){
					// no match for 'name' in labMeta
					logger.warn("No match for labPendingMeta property with name '" + targetName + "' in userMeta properties with name '" + sourceName + "'");
				}
			}
		}
		try{
			labPendingMetaDao.setMeta((List<LabPendingMeta>)labPendingMetaHelperWebapp.getMetaList(), labPendingDb.getLabPendingId() );
		} catch (MetadataException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("labPending.created_meta.error");
		}

		return labPendingDb;
	}

	
	/**
	 * Processes a pending user email validation GET request (should be from clicking a link in an email) 
	 * @param authCode
	 * @param urlEncodedEmail
	 * @param m model
	 * @return view
	 * @throws MetadataException
	 */
	  @RequestMapping(value="/confirmUserEmail", method=RequestMethod.GET)
	  public String confirmUserEmailFromEmailLink(
			  @RequestParam(value="authcode", required=false) String authCode,
			  @RequestParam(value="email", required=false) String urlEncodedEmail,
		      ModelMap m) throws MetadataException {
		if ( (authCode == null || authCode.isEmpty()) && (urlEncodedEmail == null || urlEncodedEmail.isEmpty()) ){
			// get the authcodeform view
			return "auth/confirmemail/authcodeform";
		}
		String decodedEmail;
		try{
			decodedEmail = URLDecoder.decode(urlEncodedEmail, "UTF-8");
		} catch(UnsupportedEncodingException e){
			waspErrorMessage("auth.confirmemail_corruptemail.error");
			return "redirect:/auth/confirmUserEmail.do"; // do this to clear GET parameters and forward to authcodeform view
		}
		Map<String, String> userPendingQueryMap = new HashMap<String, String>();
		userPendingQueryMap.put("email", decodedEmail);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		if (userPendingDao.findByMap(userPendingQueryMap).isEmpty()){
			// email already confirmed probably accidently re-confirming
			return "redirect:/auth/newuser/emailok.do";
		}
		if (! userPendingEmailValid(authCode, decodedEmail, null)) return "redirect:/auth/confirmUserEmail.do"; // do this to clear GET parameters and forward to authcodeform view
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		sendPendingUserConfRequestEmail(decodedEmail);
		return "redirect:/auth/newuser/emailok.do";
	}
	
	/**
	 * Processes pending user email validation POST request from a form view
	 * @param authCode
	 * @param email
	 * @param captchaText
	 * @param m model
	 * @return view
	 * @throws MetadataException
	 */
	 @RequestMapping(value="/confirmUserEmail", method=RequestMethod.POST)
	  public String confirmUserEmailFromForm(
	        @RequestParam(value="authcode") String authCode,
			@RequestParam(value="email") String email,
	        @RequestParam(value="captcha_text") String captchaText,
	        ModelMap m) throws MetadataException {
		  Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		  /* NV commented for testing
		  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			  waspErrorMessage("auth.confirmemail_captcha.error");
			  m.put("authcode", authCode);
			  m.put("email", email);
			  return "auth/confirmemail/authcodeform";
		  }
		   */
		  if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";


		  Map<String, String> userPendingQueryMap = new HashMap<String, String>();
		  userPendingQueryMap.put("email", email);
		  userPendingQueryMap.put("status", "WAIT_EMAIL");
		  if (userPendingDao.findByMap(userPendingQueryMap).isEmpty()){
			// email already confirmed probably accidently re-confirming
			return "redirect:/auth/newuser/emailok.do";
		  }
		  if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";
		  sendPendingUserConfRequestEmail(email);
		  return "redirect:/auth/newuser/emailok.do";
	  }

	 /**
	  * Processes a pending principal investigator email validation GET request (should be from clicking a link in an email) 
	  * @param authCode
	  * @param urlEncodedEmail
	  * @param m model
	  * @return view
	  * @throws MetadataException
	  */
	 @RequestMapping(value="/confirmPIEmail", method=RequestMethod.GET)
	 public String confirmPIEmailFromEmailLink(
			  @RequestParam(value="authcode", required=false) String authCode,
			  @RequestParam(value="email", required=false) String urlEncodedEmail,
		      ModelMap m) throws MetadataException {
		 if ( (authCode == null || authCode.isEmpty()) && (urlEncodedEmail == null || urlEncodedEmail.isEmpty()) ){
			// get the authcodeform view
			return "auth/confirmemail/authcodeform";
		 }
		 String decodedEmail;
		 try{
			 decodedEmail = URLDecoder.decode(urlEncodedEmail, "UTF-8");
		 } catch(UnsupportedEncodingException e){
			 waspErrorMessage("auth.confirmemail_corruptemail.error");
			 return "redirect:/auth/confirmPIEmail.do"; // do this to clear GET parameters and forward to authcodeform view
		 }
		 Map<String, String> userPendingQueryMap = new HashMap<String, String>();
		 userPendingQueryMap.put("email", decodedEmail);
		 userPendingQueryMap.put("status", "WAIT_EMAIL");
		 if (userPendingDao.findByMap(userPendingQueryMap).isEmpty()){
			 // email already confirmed probably accidently re-confirming
			 return "redirect:/auth/newpi/emailok.do";
		 }	  
		 if (! userPendingEmailValid(authCode, decodedEmail, null)) return "redirect:/auth/confirmPIEmail.do"; // do this to clear GET parameters and forward to authcodeform view
		 sendPendingUserConfRequestEmail(decodedEmail);
		 return "redirect:/auth/newpi/emailok.do";
	}
	 
	/**
	 * Processes pending principal investigator email validation POST request from a form view
	 * @param authCode
	 * @param email
	 * @param captchaText
	 * @param m model
	 * @return view
	 * @throws MetadataException
	 */
	@RequestMapping(value="/confirmPIEmail", method=RequestMethod.POST)
	public String confirmPIEmailFromForm(
			@RequestParam(value="authcode") String authCode,
			@RequestParam(value="email") String email,
			@RequestParam(value="captcha_text") String captchaText,
			ModelMap m) throws MetadataException {
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		
		/* NV commented for testing
		if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			waspErrorMessage("auth.confirmemail_captcha.error");
			m.put("authcode", authCode);
			m.put("email", email);
			return "auth/confirmemail/authcodeform";
		}
		*/
		if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";


		Map<String, String> userPendingQueryMap = new HashMap<String, String>();
		userPendingQueryMap.put("email", email);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		if (userPendingDao.findByMap(userPendingQueryMap).isEmpty()){
			// email already confirmed probably accidently re-confirming
			return "redirect:/auth/newpi/emailok.do";
		}	 
		if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";
		sendPendingUserConfRequestEmail(email);
		return "redirect:/auth/newpi/emailok.do";
	}
	
	/**
	 * Uses internal / external status of specified department so that department select list is only populated internal or external department options
	 * @param m
	 * @param departmentId
	 */
	private void prepareSelectListData(ModelMap m, final MetaHelperWebapp metaHelperWebapp) {
		int isInternal = 0;
		try{
			String institueName = metaHelperWebapp.getMetaByName("institution").getV();
			if (institueName.isEmpty()) 
				throw new MetadataException();
			if (messageService.getMessage("piPending.internal_institute_list.data").contains(institueName))
				isInternal = 1;
		} catch (MetadataException e){
			// handle MetadataException by simply logging an error and defaulting to the complete department list
			logger.warn("Unable to extract a valid departmentId from metadata for preparing department list. Defaulting to whole department list");
			isInternal = -1;
		}
		prepareSelectListData(m);
		if (isInternal != -1){
			Map<String, Integer> departmentQueryMap = new HashMap<String, Integer>();
			departmentQueryMap.put("isInternal",  isInternal );
			m.addAttribute("department", departmentDao.findByMap(departmentQueryMap));
		}
	}
	
}
