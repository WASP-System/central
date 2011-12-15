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
import edu.yu.einstein.wasp.controller.validator.PasswordValidator;
import edu.yu.einstein.wasp.controller.validator.UserPendingMetaValidatorImpl;
import edu.yu.einstein.wasp.model.ConfirmEmailAuth;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabPendingMeta;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaHelper;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.ConfirmEmailAuthService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.LabPendingMetaService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.PasswordService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.util.AuthCode;
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
	private MessageService messageService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
		
	/**
	 * get a @{link MetaHelper} instance for working with userPending metadata
	 * @return
	 */
	private final MetaHelper getMetaHelper() {
		return new MetaHelper("userPending", UserPendingMeta.class, request.getSession());
	}
	
		
	/**
	 * Create model for a new user application form view based on the current userPending metadata in the uifield properties table
	 * @param m model
	 * @return view
	 */
	@RequestMapping(value="/newuser", method=RequestMethod.GET)
	public String showNewPendingUserForm(ModelMap m) {
		

		UserPending userPending = new UserPending();
		MetaHelper metaHelper = getMetaHelper();	
		
		userPending.setUserPendingMeta(metaHelper.getMasterList(UserPendingMeta.class));
		m.put("isAuthenticationExternal", authenticationService.isAuthenticationSetExternal());
		m.addAttribute(metaHelper.getParentArea(), userPending);
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
	@RequestMapping(value="/newuser", method=RequestMethod.POST)
	public String createNewPendingUser (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) throws MetadataException {
		
		
		MetaHelper userPendingMetaHelper = getMetaHelper();
		userPendingMetaHelper.getFromRequest(request, UserPendingMeta.class);
		userPendingMetaHelper.validate(new UserPendingMetaValidatorImpl(userService, labService), result);
		if (! result.hasFieldErrors("login")){
			if (authenticationService.isAuthenticationSetExternal()){
				if (! authenticationService.authenticate(userPendingForm.getLogin(), userPendingForm.getPassword())){
					Errors errors=new BindException(result.getTarget(), userPendingMetaHelper.getParentArea());
					errors.rejectValue("login", userPendingMetaHelper.getParentArea()+".external_authentication.error", userPendingMetaHelper.getParentArea()+".external_authentication.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				} else {
					// user is now authenticated with single role 'ag' (authenticated guest)
				}
			}
			else {
				// using internal authentication
				if (! authenticationService.isLoginNameWellFormed(userPendingForm.getLogin())){
					Errors errors=new BindException(result.getTarget(), userPendingMetaHelper.getParentArea());
					errors.rejectValue("login", userPendingMetaHelper.getParentArea()+".login_malformed.error", userPendingMetaHelper.getParentArea()+".login_malformed.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				}
				else{
					// login name correctly formed
					try {
						if (authenticationService.isLoginAlreadyInUse(userPendingForm.getLogin(), userPendingForm.getEmail())){
							Errors errors=new BindException(result.getTarget(), userPendingMetaHelper.getParentArea());
							errors.rejectValue("login", userPendingMetaHelper.getParentArea()+".login_exists.error", userPendingMetaHelper.getParentArea()+".login_exists.error (no message has been defined for this property)");
							result.addAllErrors(errors);
						}
					} catch(LoginNameException e){
						Errors errors=new BindException(result.getTarget(), userPendingMetaHelper.getParentArea());
						errors.rejectValue("login", userPendingMetaHelper.getParentArea()+".login_malformed.error", userPendingMetaHelper.getParentArea()+".login_malformed.error (no message has been defined for this property)");
						result.addAllErrors(errors);
					}
				}
			}
		}
		if (!authenticationService.isAuthenticationSetExternal()){
			passwordValidator.validate(result, userPendingForm.getPassword(), (String) request.getParameter("password2"), userPendingMetaHelper.getParentArea(), "password");
		}
		if (! result.hasFieldErrors("email")){
			User user = userService.getUserByEmail(userPendingForm.getEmail());
			//NV 12132011
			//if (user.getUserId() != null ){
			if (user.getUserId() != null ){

				Errors errors=new BindException(result.getTarget(), userPendingMetaHelper.getParentArea());
				errors.rejectValue("email", userPendingMetaHelper.getParentArea()+".email_exists.error", userPendingMetaHelper.getParentArea()+".email_exists.error (no message has been defined for this property)");
				result.addAllErrors(errors);
			}
		}
		
		// validate captcha
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		/* NV
		String captchaText = (String) request.getParameter("captcha");
		/* NV
		if (captcha == null || captchaText == null || captchaText.isEmpty() || (! captcha.isCorrect(captchaText)) ){
			m.put("captchaError", messageService.getMessage(userPendingMetaHelper.getParentArea()+".captcha.error"));
		}
		*/
		if (result.hasErrors() || m.containsKey("captchaError")) {
			userPendingForm.setUserPendingMeta((List<UserPendingMeta>) userPendingMetaHelper.getMetaList());
			prepareSelectListData(m);
			m.put("isAuthenticationExternal", authenticationService.isAuthenticationSetExternal());
			waspMessage("user.created.error");
			return "auth/newuser/form";
		}
		
		// form passes validation so finalize and persist userPending data and metadata		
		String piUserLogin = userPendingMetaHelper.getMetaByName("primaryuserid").getV();	
		
		Lab lab = labService.getLabByPrimaryUserId(userService.getUserByLogin(piUserLogin).getUserId());
		userPendingForm.setLabId(lab.getLabId());
		userPendingForm.setStatus("WAIT_EMAIL"); // set to WAIT_EMAIL even if isEmailApproved == true or sendPendingUserConfRequestEmail() won't work properly
				
		userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 
				
		List<UserPendingMeta> userPendingMetaList = (List<UserPendingMeta>) userPendingMetaHelper.getMetaList();
		userPendingForm.setUserPendingMeta(userPendingMetaList);
		userPendingForm.setFirstName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getFirstName()));
		userPendingForm.setLastName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getLastName()));
		UserPending userPendingDb = userPendingService.save(userPendingForm);
		userPendingMetaService.updateByUserpendingId(userPendingDb.getUserPendingId(), userPendingMetaList);
		
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		status.setComplete();
		
		if (isUserPendingEmailApproved(userPendingDb.getEmail())){ // is email address already confrimed
			sendPendingUserConfRequestEmail(userPendingDb.getEmail()); // email PI to inform of pending user
			return "redirect:/auth/newuser/emailok.do";
		} else {
			// email address not confirmed yet so request confirmation
			emailService.sendPendingUserEmailConfirm(userPendingForm, confirmEmailAuthService.getNewAuthcodeForUserPending(userPendingForm));
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
		Map confirmedEmailQueryMap = new HashMap();
		confirmedEmailQueryMap.put("email", email);
		for (UserPending up: (List<UserPending>) userPendingService.findByMap(confirmedEmailQueryMap)){
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
		List<String> instituteList = new ArrayList();
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
	@RequestMapping(value="/newpi/institute", method=RequestMethod.POST)
	public String selectPiInstitute(
			@RequestParam(value="instituteSelect") String instituteSelect,
			@RequestParam(value="instituteOther", required = false) String instituteOther,
			ModelMap m) throws MetadataException {
		String internalInstituteList = messageService.getMessage("piPending.internal_institute_list.data");
		List<String> instituteList = new ArrayList();
		Collections.addAll(instituteList,internalInstituteList.split(";")); 
		m.addAttribute("instituteList", instituteList);
		if ( (instituteSelect == null || instituteSelect.equals("other") || instituteSelect.isEmpty()) && (instituteOther == null || instituteOther.isEmpty()) ){
			waspMessage("piPending.institute_not_selected.error");
			return "auth/newpi/institute";
		}
		if ( instituteSelect != null && !instituteSelect.equals("other")  && !instituteSelect.isEmpty() && instituteOther != null && !instituteOther.isEmpty() ){
			waspMessage("piPending.institute_multi_select.error");
			return "auth/newpi/institute";
		}
		
		MetaHelper metaHelper=getMetaHelper();
		metaHelper.setArea("piPending");
		String instituteName = "";
		Map visibilityElementMap = new HashMap();
		visibilityElementMap.put("institution", MetaAttribute.FormVisibility.immutable);
		if (instituteSelect != null && !instituteSelect.equals("other")){
			// internal institute
			instituteName = instituteSelect;
			metaHelper.getMasterList(visibilityElementMap, UserPendingMeta.class);
		} else {
			// external institute
			instituteName = StringHelper.removeExtraSpacesAndCapFirstLetter(instituteOther);
			visibilityElementMap.put("departmentId", MetaAttribute.FormVisibility.immutable);
			metaHelper.getMasterList(visibilityElementMap, UserPendingMeta.class);
			Map departmentQueryMap = new HashMap();
			departmentQueryMap.put("isInternal", 0);
			List<Department> extDepartments = (List<Department>) departmentService.findByMap(departmentQueryMap);
			if (extDepartments.isEmpty() || extDepartments.size() >1 ){
				throw new MetadataException("Either 0 or >1 external departments defined in the database. Should only be 1");
			}
			metaHelper.setMetaValueByName("departmentId", Integer.toString(extDepartments.get(0).getDepartmentId()));
		}
		metaHelper.setMetaValueByName("institution", instituteName);
		
		UserPending userPending = new UserPending();
		userPending.setUserPendingMeta((List<UserPendingMeta>) metaHelper.getMetaList());
		m.addAttribute(metaHelper.getParentArea(), userPending);
		
		// save visibility map to session in order to use it later
		request.getSession().setAttribute("visibilityElementMap", visibilityElementMap);
		prepareSelectListData(m, metaHelper);
		m.put("isAuthenticationExternal", authenticationService.isAuthenticationSetExternal());
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
	@RequestMapping(value="/newpi/form", method=RequestMethod.POST)
	public String createNewPendingPi (
			 @Valid UserPending userPendingForm, 
			 BindingResult result,
			 SessionStatus status, 
			 ModelMap m) throws MetadataException {
		
		MetaHelper metaHelper=getMetaHelper();
		
		metaHelper.setArea("piPending"); 
		// get the visibilityElement Map previously saved in session
		Map visibilityElementMap = (Map<String, MetaAttribute.FormVisibility>) request.getSession().getAttribute("visibilityElementMap");
		metaHelper.getFromRequest(request, visibilityElementMap, UserPendingMeta.class);
		metaHelper.validate(result);
		
		if (! result.hasFieldErrors("login")){
			if (authenticationService.isAuthenticationSetExternal()){
				if (! authenticationService.authenticate(userPendingForm.getLogin(), userPendingForm.getPassword())){
					Errors errors=new BindException(result.getTarget(), metaHelper.getParentArea());
					errors.rejectValue("login", metaHelper.getParentArea()+".external_authentication.error", metaHelper.getParentArea()+".external_authentication.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				} else {
					// user is now authenticated with single role 'ag' (authenticated guest)
				}
			}
			else {
				// using internal authentication
				if (! authenticationService.isLoginNameWellFormed(userPendingForm.getLogin())){
					Errors errors=new BindException(result.getTarget(), metaHelper.getParentArea());
					errors.rejectValue("login", metaHelper.getParentArea()+".login_malformed.error", metaHelper.getParentArea()+".login_malformed.error (no message has been defined for this property)");
					result.addAllErrors(errors);
				}
				else{
					// login name correctly formed
					try {
						if (authenticationService.isLoginAlreadyInUse(userPendingForm.getLogin(), userPendingForm.getEmail())){
							Errors errors=new BindException(result.getTarget(), metaHelper.getParentArea());
							errors.rejectValue("login", metaHelper.getParentArea()+".login_exists.error", metaHelper.getParentArea()+".login_exists.error (no message has been defined for this property)");
							result.addAllErrors(errors);
						}
					} catch(LoginNameException e){
						Errors errors=new BindException(result.getTarget(), metaHelper.getParentArea());
						errors.rejectValue("login", metaHelper.getParentArea()+".login_malformed.error", metaHelper.getParentArea()+".login_malformed.error (no message has been defined for this property)");
						result.addAllErrors(errors);
					}
				}
			}
		}
		if (!authenticationService.isAuthenticationSetExternal()){
			passwordValidator.validate(result, userPendingForm.getPassword(), (String) request.getParameter("password2"), metaHelper.getParentArea(), "password");
		}
		
		if (! result.hasFieldErrors("email")){
			User user = userService.getUserByEmail(userPendingForm.getEmail());

			//NV 12132011
			if (user.getUserId() != null){

				Errors errors=new BindException(result.getTarget(), metaHelper.getParentArea());
				errors.rejectValue("email", metaHelper.getParentArea()+".email_exists.error", metaHelper.getParentArea()+".email_exists.error (no message has been defined for this property)");
				result.addAllErrors(errors);
			}
		}
		
		// validate captcha
		Captcha captcha = (Captcha) request.getSession().getAttribute(Captcha.NAME);
		
		/* NV
		String captchaText = (String) request.getParameter("captcha");
		/* NV
		if (captcha == null || captchaText == null || captchaText.isEmpty() || (! captcha.isCorrect(captchaText)) ){
			m.put("captchaError", messageService.getMessage(metaHelper.getParentArea()+".captcha.error"));
		}
		*/
		if (result.hasErrors() || m.containsKey("captchaError")) {
			userPendingForm.setUserPendingMeta((List<UserPendingMeta>) metaHelper.getMetaList());
			prepareSelectListData(m, metaHelper);
			m.put("isAuthenticationExternal", authenticationService.isAuthenticationSetExternal());
			waspMessage("user.created.error");
			
			return "auth/newpi/form";
		}

		userPendingForm.setStatus("WAIT_EMAIL");

		userPendingForm.setPassword( passwordService.encodePassword(userPendingForm.getPassword()) ); 
		userPendingForm.setFirstName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getFirstName()));
		userPendingForm.setLastName(StringHelper.removeExtraSpacesAndCapFirstLetter(userPendingForm.getLastName()));
		UserPending userPendingDb = userPendingService.save(userPendingForm);
		List<UserPendingMeta> userPendingMetaList = (List<UserPendingMeta>) metaHelper.getMetaList();
		userPendingMetaService.updateByUserpendingId(userPendingDb.getUserPendingId(), userPendingMetaList);
		request.getSession().removeAttribute(Captcha.NAME); // ensures fresh capcha issued if required in this session
		request.getSession().removeAttribute("visibilityElementMap"); // remove visibilityElementMap from the session
		status.setComplete();
		
		if (isUserPendingEmailApproved(userPendingDb.getEmail())){ // is email already confirmed
			sendPendingUserConfRequestEmail(userPendingDb.getEmail()); // email DA to inform of pending lab request
			return "redirect:/auth/newpi/emailok.do";
		} else {
			// email address not confirmed yet so request confirmation
			emailService.sendPendingPIEmailConfirm(userPendingForm, confirmEmailAuthService.getNewAuthcodeForUserPending(userPendingForm));
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
			waspMessage("auth.confirmemail_badauthcode.error");
			if (m != null) m.put("email", email);
			return false;
		}
		ConfirmEmailAuth confirmEmailAuth = confirmEmailAuthService.getConfirmEmailAuthByAuthcode(authCode);
		if (email == null || email.isEmpty() || confirmEmailAuth.getConfirmEmailAuthId() == null) {
			waspMessage("auth.confirmemail_bademail.error");
			if (m != null) m.put("authcode", authCode);
			return false;
		}
			  
		UserPending userPending = userPendingService.getUserPendingByUserPendingId(confirmEmailAuth.getUserpendingId());

		if (! userPending.getEmail().equals(email)){
			waspMessage("auth.confirmemail_wronguser.error");
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
		Map userPendingQueryMap = new HashMap();
		userPendingQueryMap.put("email", email);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		List<UserPending> userPendingList = userPendingService.findByMap(userPendingQueryMap);
		// consider email confirmed for ALL pending user and lab applications linked to this validated email address
		for (UserPending up: userPendingList){
			ConfirmEmailAuth auth = confirmEmailAuthService.getConfirmEmailAuthByUserpendingId(up.getUserPendingId());
			if (auth.getConfirmEmailAuthId() != null){
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
	
	/**
	 * Create, persist and return a {@link LabPending} object for a given {@link UserPending} instance.
	 * @param userPending
	 * @return {@link LabPending}
	 * @throws MetadataException
	 */
	protected LabPending getNewLabPending(UserPending userPending) throws MetadataException{
		MetaHelper userPendingMetaHelper=getMetaHelper();
		userPendingMetaHelper.setArea("piPending");
		userPendingMetaHelper.syncWithMaster(userPending.getUserPendingMeta());
		LabPending labPending = new LabPending();
		labPending.setStatus("PENDING");
		labPending.setUserpendingId(userPending.getUserPendingId());
		int departmentId = Integer.parseInt(userPendingMetaHelper.getMetaByName("departmentId").getV());
		labPending.setDepartmentId(departmentId);
		String labName = userPendingMetaHelper.getMetaByName("labName").getV();
		labPending.setName(labName);
		LabPending labPendingDb = labPendingService.save(labPending);
		// copies address meta data from PI userMeta to labMeta as billing address info. 
		MetaHelper labPendingMetaHelper = new MetaHelper("labPending", LabPendingMeta.class, request.getSession());
		List<LabPendingMeta> labPendingMetaList = labPendingMetaHelper.getMasterList(LabPendingMeta.class);
				
		// fill up labpending metadata using information from userpending metadata
		for (LabPendingMeta lpm: labPendingMetaList) {
			
			// get name from prefix by removing area 
			if (lpm.getK().contains("labPending.billing_")){
				String sourceName = lpm.getK().replaceAll("labPending\\.billing_", "");
				String targetName = "billing_" + sourceName;
				try{
					if (targetName.equals("billing_contact")){
						String contactName = userPending.getFirstName() + " " + userPending.getLastName();
						labPendingMetaHelper.setMetaValueByName(targetName, contactName);
					} else{
						labPendingMetaHelper.setMetaValueByName(targetName, userPendingMetaHelper.getMetaByName(sourceName).getV());
						// see if we need to use this metadata to populate other fields
						if (sourceName.equals("phone") || sourceName.equals("building_room")){
							targetName = sourceName;
							labPendingMetaHelper.setMetaValueByName(targetName, userPendingMetaHelper.getMetaByName(sourceName).getV());
						} else if (sourceName.equals("departmentId")){
							String internalExternal = (
									departmentService.findById( Integer.valueOf( userPendingMetaHelper.getMetaByName(sourceName).getV() ) )
											.getIsInternal().intValue() == 1) ? "internal" : "external";
							targetName = "internal_external_lab";
							labPendingMetaHelper.setMetaValueByName(targetName, internalExternal);
						}
					}
				} catch (MetadataException e){
					// no match for 'name' in labMeta
					logger.warn("No match for labPendingMeta property with name '" + targetName + "' in userMeta properties with name '" + sourceName + "'");
				}
			}
		}
		labPendingMetaService.updateByLabpendingId(labPendingDb.getLabPendingId(),  (List<LabPendingMeta>)labPendingMetaHelper.getMetaList() );

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
			waspMessage("auth.confirmemail_corruptemail.error");
			return "redirect:/auth/confirmUserEmail.do"; // do this to clear GET parameters and forward to authcodeform view
		}
		Map userPendingQueryMap = new HashMap();
		userPendingQueryMap.put("email", decodedEmail);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		if (userPendingService.findByMap(userPendingQueryMap).isEmpty()){
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
		  /* NV
		  if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			  waspMessage("auth.confirmemail_captcha.error");
			  m.put("authcode", authCode);
			  m.put("email", email);
			  return "auth/confirmemail/authcodeform";
		  }
<<<<<<< .mine
		  */

		  if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";


		  Map userPendingQueryMap = new HashMap();
		  userPendingQueryMap.put("email", email);
		  userPendingQueryMap.put("status", "WAIT_EMAIL");
		  if (userPendingService.findByMap(userPendingQueryMap).isEmpty()){
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
			 waspMessage("auth.confirmemail_corruptemail.error");
			 return "redirect:/auth/confirmPIEmail.do"; // do this to clear GET parameters and forward to authcodeform view
		 }
		 Map userPendingQueryMap = new HashMap();
		 userPendingQueryMap.put("email", decodedEmail);
		 userPendingQueryMap.put("status", "WAIT_EMAIL");
		 if (userPendingService.findByMap(userPendingQueryMap).isEmpty()){
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
		/* NV

		if (captcha == null || (! captcha.isCorrect(captchaText)) ){
			waspMessage("auth.confirmemail_captcha.error");
			m.put("authcode", authCode);
			m.put("email", email);
			return "auth/confirmemail/authcodeform";
		}
		*/
		if (! userPendingEmailValid(authCode, email, m)) return "auth/confirmemail/authcodeform";


		Map userPendingQueryMap = new HashMap();
		userPendingQueryMap.put("email", email);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		if (userPendingService.findByMap(userPendingQueryMap).isEmpty()){
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
	private void prepareSelectListData(ModelMap m, final MetaHelper metaHelper) {
		int isInternal = 0;
		try{
			String institueName = metaHelper.getMetaByName("institution").getV();
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
			Map departmentQueryMap = new HashMap();
			departmentQueryMap.put("isInternal",  isInternal );
			m.addAttribute("department", departmentService.findByMap(departmentQueryMap));
		}
	}
	
}
