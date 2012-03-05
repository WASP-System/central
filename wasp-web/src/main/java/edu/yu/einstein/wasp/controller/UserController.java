package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.ConfirmEmailAuthService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.PasswordService;
import edu.yu.einstein.wasp.service.UserMetaService;
import edu.yu.einstein.wasp.service.UserpasswordauthService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;


/**
 * Controller to manage users
 * 
 */

@Controller
@Transactional
@RequestMapping("/user")
public class UserController extends WaspController {

	@Autowired
	private UserMetaService userMetaService;
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private MessageService messageService;
	  
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ConfirmEmailAuthService confirmEmailAuthService;
	
	@Autowired
	private UserpasswordauthService userpasswordauthService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("user", UserMeta.class, request.getSession());
	}
	
	/**
	 * Prepares page to display list of users
	 * 
	 * @Author Sasha Levchuk 
	 */
	@RequestMapping("/list")
	@PreAuthorize("hasRole('su')")
	public String list(ModelMap m) {
		
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));
		
		prepareSelectListData(m);
	
		return "user/list";
	}
	
	
	/**
	 * Returns data to render a subgrid with user info
	 * 
	 * @Author Sasha Levchuk 
	 */
	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or User.login == principal.name")
	public String subgridJSON(@RequestParam("id") Integer userId,ModelMap m, HttpServletResponse response) {
				
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		User userDb = this.userService.getById(userId);
		
		List<LabUser> uLabs=userDb.getLabUser();
		
		int rowNum = uLabs.size();
		
	 	ObjectMapper mapper = new ObjectMapper();
    	
		try {
//			jqgrid.put("page","1");
//			jqgrid.put("records",max+"");
//			jqgrid.put("total",max+"");

			List<Map> rows = new ArrayList<Map>();
			for (LabUser uLab:uLabs) {
				Map cell = new HashMap();
				cell.put("id", uLab.getLabId());
				 					
				List<String> cellList = new ArrayList<String>(
						Arrays.asList(
								new String[] {
										"<a href=/wasp/job/list.do?labId=" 
										+ uLab.getLabId().intValue() 
										+ "&userId=" + userId + ">" + 
											uLab.getLab().getName() + "</a>"
								}
						)
				);
				 
				cell.put("cell", cellList);
				 
				rows.add(cell);
			}
			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+uLabs,e);
		 }
	
	}
	
	/**
	 * Prepares page to display JQGrid table with a list of users
	 * 
	 * @Author Sasha Levchuk 
	 */
	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public String getListJSON(HttpServletResponse response) {
	
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		String search = request.getParameter("_search");
		String searchField = request.getParameter("searchField");
		String searchString = request.getParameter("searchString");
		String selId = request.getParameter("selId");
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<User> userList = new ArrayList<User>();
		
		if (!StringUtils.isEmpty(selId)) {

			userList.add(this.userService.getUserByUserId(Integer.parseInt(selId)));
		
		} else if (!StringUtils.isEmpty(search) && !StringUtils.isEmpty(searchField) && !StringUtils.isEmpty(searchString) ) {
		
			Map<String, String> m = new HashMap<String, String>();

			m.put(searchField, searchString);

			if (sidx.isEmpty()) {
				userList = this.userService.findByMap(m);
			} else {
				List<String> sidxList =  new ArrayList<String>();
				sidxList.add(sidx);
				userList = this.userService.findByMapDistinctOrderBy(m, null, sidxList, sord);
			}

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<User> all = new ArrayList<User>(sidx.isEmpty() ? 
						this.userService.findAll() : this.userService.findAllOrderBy(sidx, sord));

				for (Iterator<User> it = userList.iterator(); it.hasNext();) {
					User exclude = it.next();
					all.remove(exclude);

				}
				userList = all;
			}
			
		} else {
			
			userList = sidx.isEmpty() ? this.userService.findAll() : this.userService.findAllOrderBy(sidx, sord);
		}

		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = userList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");
			 
			Map<String, String> userData=new HashMap<String, String>();
			userData.put("page", pageIndex + "");
			userData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("userdata",userData);
			 
			List<Map> rows = new ArrayList<Map>();
			
			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
//			if(!StringUtils.isEmpty(request.getParameter("selId")))
//			{
//				int selId = Integer.parseInt(request.getParameter("selId"));
//				int selIndex = userList.indexOf(userService.findById(selId));
//				frId = selIndex;
//				toId = frId + 1;
//
//				jqgrid.put("records", "1");
//				jqgrid.put("total", "1");
//				jqgrid.put("page", "1");
//			}				

			List<User> userPage = userList.subList(frId, toId);
			for (User user:userPage) {
				Map cell = new HashMap();
				cell.put("id", user.getUserId());
				 
				List<UserMeta> userMeta=getMetaHelperWebapp().syncWithMaster(user.getUserMeta());
				 					
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							user.getLogin(),
//							user.getPassword(),
							user.getFirstName(),
							user.getLastName(),						
							user.getEmail(),
							LOCALES.get(user.getLocale()),
							user.getIsActive().intValue()==1?"yes":"no"
				}));
				 
				for(UserMeta meta:userMeta) {
					cellList.add(meta.getV());
				}
				
				 
				cell.put("cell", cellList);
				 
				rows.add(cell);
			}

			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			 
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+userList,e);
		}
	
	}

	/**
	 * Creates/Updates user
	 * 
	 * @Author Sasha Levchuk 
	 */	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or User.login == principal.name")
	public String updateDetailJSON(@RequestParam("id") Integer userId,User userForm, ModelMap m, HttpServletResponse response) {
		userId = (userId == null)? 0:userId;
		boolean adding = (userId == 0);
		if (adding || !userService.getById(userId).getLogin().equals(userForm.getLogin())){
			boolean loginExists = false;
			try {
				loginExists = authenticationService.isLoginAlreadyInUse(userForm.getLogin(), userForm.getEmail());
			} catch(LoginNameException e){
				loginExists = true;
			}
			if (loginExists){
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("user.login_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("user.login_exists.error"),e);
				}
			
			}
		}
		
		int emailOwnerUserId = (userService.getUserByEmail(userForm.getEmail()).getUserId() == null)? 0:userService.getUserByEmail(userForm.getEmail()).getUserId();
		if (emailOwnerUserId != 0 && emailOwnerUserId != userId){
			try{
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println(messageService.getMessage("user.email_exists.error"));
				return null;
			} catch (Throwable e) {
				throw new IllegalStateException("Cant output validation error "+messageService.getMessage("user.email_exists.error"),e);
			}
		}
				
		List<UserMeta> userMetaList = getMetaHelperWebapp().getFromJsonForm(request, UserMeta.class);
		userForm.setUserMeta(userMetaList);
		//NV 12282011 - The code userForm.setUserId(userId) below throws exception: "detached entity passed to persist" when adding a new user. 
		//				Do not set userId for a new user here - it is configured as a generated value, therefore, Hibernate expects userId to be null when EntityManager#persist is called.
		//userForm.setUserId(userId);
		if (!adding) {
			userForm.setUserId(userId);
		}
		boolean myemailChanged = false;

		if (adding) {
			// set random password. We don't care what it is as new user will be prompted to
			// set a new one via email.
			userForm.setPassword(passwordService.encodePassword(passwordService.getRandomPassword(10))); 
			userForm.setLastUpdTs(new Date());
			userForm.setIsActive(1);
			User userDb = this.userService.save(userForm);
			userId=userDb.getUserId();
			userMetaService.updateByUserId(userId, userMetaList);
			emailService.informUserAccountCreatedByAdmin(userDb, confirmEmailAuthService.getNewAuthcodeForUser(userDb));
		} else {
			User userDb = this.userService.getById(userId);
			
			if (!userDb.getEmail().equals(userForm.getEmail())){
				//myemailChanged = true;
				if (authenticationService.getAuthenticatedUser().getUserId().intValue() == userId.intValue()) {
					try {
						response.getWriter().println(messageService.getMessage("user.updated_fail.error"));
						return null;
					} catch (Throwable e) {
						throw new IllegalStateException("Cant output success message ",e);
					}
				}
				emailService.sendUserEmailConfirm(userForm, confirmEmailAuthService.getNewAuthcodeForUser(userForm));

			}
			if (!userDb.getLogin().equals(userForm.getLogin())){
				if (authenticationService.getAuthenticatedUser().getUserId().intValue() == userId.intValue()) {
					try {
						response.getWriter().println(messageService.getMessage("user.updated_fail.error"));
						return null;
					} catch (Throwable e) {
						throw new IllegalStateException("Cant output success message ",e);
					}
				}
				emailService.informUserLoginChanged(userForm);
			}
			userDb.setLogin(userForm.getLogin());
			userDb.setFirstName(userForm.getFirstName());
			userDb.setLastName(userForm.getLastName());
			userDb.setEmail(userForm.getEmail());
			userDb.setLocale(userForm.getLocale());
			userDb.setIsActive(userForm.getIsActive());
			userDb.setLastUpdTs(new Date());
			this.userService.merge(userDb);
			userMetaService.updateByUserId(userId, userMetaList);
		}
		
		//waspMessage("user.updated.success");
		// if I'm the changed user log me out. I need to re-confirm my email and log in.
		/* do not allow 'SuperUser' to change email through user detail in JqGrid.
		if (myemailChanged){
			authenticationService.logoutUser();
			return "redirect:/auth/confirmemail/emailchanged";
		}
		*/
		try {
			response.getWriter().println(adding ? messageService.getMessage("user.created_success.label") : messageService.getMessage("user.updated_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	
	    
	}
	
	@RequestMapping(value = "/me_ro", method = RequestMethod.GET)
	public String myDetail_RO(ModelMap m) {
		User user = authenticationService.getAuthenticatedUser();		
		return this.detailRO(user.getUserId(), m);
	}
	
	@RequestMapping(value = "/me_rw", method = RequestMethod.GET)
	public String myDetail(ModelMap m) {
		User user = authenticationService.getAuthenticatedUser();		
		return this.detailRW(user.getUserId(), m);
	}
	
	@RequestMapping(value = "/me_rw", method = RequestMethod.POST)
	public String updateDetail(@Valid User userForm, BindingResult result,
			SessionStatus status, ModelMap m) {
		User user = authenticationService.getAuthenticatedUser();		

		return updateDetail(user.getUserId(), userForm, result, status, m);
	}
	
	/**
	 * Updates user
	 * 
	 * @Author Sasha Levchuk 
	 */
	@RequestMapping(value = "/detail_rw/{userId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su')")
	public String updateDetail(@PathVariable("userId") Integer userId,
			@Valid User userForm, BindingResult result, SessionStatus status,
			ModelMap m) {
		
		userId = (userId == null)? 0:userId;
		// return read only version of page if cancel button pressed
		String submitValue = request.getParameter("submit");
		if ( submitValue.equals(messageService.getMessage("userDetail.cancel.label")) ){
			if (userId.intValue() == authenticationService.getAuthenticatedUser().getUserId().intValue()){
				return "redirect:/user/me_ro.do";
			}
			return "redirect:/user/detail_ro/" + userId + ".do";
		}
		
		List<UserMeta> userMetaList = getMetaHelperWebapp().getFromRequest(request, UserMeta.class);

		for (UserMeta meta : userMetaList) {
			meta.setUserId(userId);
		}

		userForm.setUserMeta(userMetaList);
		getMetaHelperWebapp().validate(userMetaList, result);
		userForm.setUserId(userId);
		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspMessage("user.updated.error");
			return "user/detail_rw";
		}
		boolean isMyEmailChanged = false;
		User userDb = this.userService.getById(userId);
		if (!userDb.getEmail().equals(userForm.getEmail().trim())){
			// email changed
			emailService.sendUserEmailConfirm(userForm, confirmEmailAuthService.getNewAuthcodeForUser(userForm));
			if (userId.intValue() == authenticationService.getAuthenticatedUser().getUserId().intValue()) isMyEmailChanged = true;
		}
		userDb.setFirstName(userForm.getFirstName().trim());
		userDb.setLastName(userForm.getLastName().trim());
		//don't permit user to alter password from this form!
		//if (userForm.getPassword()!=null && !userForm.getPassword().trim().isEmpty()) {
		//	PasswordEncoder encoder = new ShaPasswordEncoder();
		//	String hashedPass = encoder.encodePassword(userForm.getPassword(), null);
		//	userForm.setPassword(hashedPass);
		//}
		
		userDb.setEmail(userForm.getEmail().trim());
		userDb.setLocale(userForm.getLocale());

		userDb.setLastUpdTs(new Date());
		
		//this.userService.merge(userDb);

		userMetaService.updateByUserId(userId, userMetaList);

		MimeMessageHelper a;
		
		status.setComplete();

		waspMessage("user.updated_success.label");
		if (isMyEmailChanged){
			authenticationService.logoutUser();
			return "redirect:/auth/confirmemail/emailchanged.do";
		}
		if (userId.intValue() == authenticationService.getAuthenticatedUser().getUserId().intValue()){
			return "redirect:/user/me_ro.do";
		}
		return "redirect:/user/detail_ro/" + userId + ".do";
	}


	
	@RequestMapping(value = "/mypassword.do", method = RequestMethod.GET)
	public String myPasswordForm(ModelMap m) {
		return "user/mypassword";
	}
	
	@RequestMapping(value = "/mypassword.do", method = RequestMethod.POST)   
	public String myPassword(
			@RequestParam(required=true, value = "oldpassword") String oldpassword,
			@RequestParam(required=true, value = "newpassword1") String newpassword1,
			@RequestParam(required=true, value = "newpassword2") String newpassword2, ModelMap m) {		
		
		if (oldpassword  == null || "".equals(oldpassword)  ||
			newpassword1 == null || "".equals(newpassword1) ||
			newpassword2 == null || "".equals(newpassword2)  ) 
		{
				waspMessage("user.mypassword_missingparam.error");
				return "user/mypassword";
		}
		
		User user = authenticationService.getAuthenticatedUser();
		String currentPasswordAsHash = user.getPassword();//this is from database and is hashed
		String oldPasswordAsHash = passwordService.encodePassword(oldpassword);//oldpassword is from the form, so must hash it for comparison
		  
		if(!passwordService.matchPassword(currentPasswordAsHash, oldPasswordAsHash)){
			waspMessage("user.mypassword_cur_mismatch.error");
			return "user/mypassword";
		}
		else if(!passwordService.matchPassword(newpassword1, newpassword2)){
			waspMessage("user.mypassword_new_mismatch.error");
		    return "user/mypassword";
		}
		else if(passwordService.matchPassword(oldpassword, newpassword1)){//make sure old and new passwords differ
			waspMessage("user.mypassword_nodiff.error");
		    return "user/mypassword";
		}
		else if(!passwordService.validatePassword(newpassword1)){//at least 8 char, at least one letter; at least one number
			waspMessage("user.mypassword_new_invalid.error");
		    return "user/mypassword";
		}
		  
		user.setPassword( passwordService.encodePassword(newpassword1) ); 
		userService.merge(user);
		waspMessage("user.mypassword_ok.label");	
		return "redirect:/dashboard.do";		 
	}

	@RequestMapping(value = "/create/form.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su')")
	public String showEmptyForm(ModelMap m) {
	
		User user = new User();

		user.setUserMeta(getMetaHelperWebapp().getMasterList(UserMeta.class));
		
		m.addAttribute("user", user);
		
		prepareSelectListData(m);		

		return "user/detail_rw";
	}
	
/*
 * Should not need this. Only SuperUser can create user and this is done through the JGrid List create functionality
 * 
	@RequestMapping(value = "/create/form.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su')")
	public String create(@Valid User userForm, BindingResult result,
			SessionStatus status, ModelMap m) {

		List<UserMeta> userMetaList = getMetaHelper().getFromRequest(request, UserMeta.class);
		
		userForm.setUserMeta(userMetaList);

		// manually validate login and password
		Errors errors = new BindException(result.getTarget(), getMetaHelper().getArea());
		if (userForm.getLogin() == null || userForm.getLogin().isEmpty()) {
			errors.rejectValue("login", "user.login.error");
		} else {
			User user = userService.getUserByLogin(userForm.getLogin());
			if (user != null && user.getLogin() != null) {
				errors.rejectValue("login", "user.login.exists_error");
			}
		}

		if (userForm.getPassword() == null || userForm.getPassword().isEmpty()) {
			errors.rejectValue("password", "user.password.error");
		}

		result.addAllErrors(errors);

		List<String> validateList = new ArrayList<String>();

		validateList.add("login");
		validateList.add(Constraint.NotEmpty.name());
		validateList.add("password");
		validateList.add(Constraint.NotEmpty.name());

		for (UserMeta meta : userMetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}
		getMetaHelper().validate(userMetaList, result);
		
		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspMessage("user.created.error");
			return "user/detail_rw";
		}

		userForm.setLastUpdTs(new Date());

		//PasswordEncoder encoder = new Md5PasswordEncoder();
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String hashedPass = encoder.encodePassword(userForm.getPassword(), null);
		userForm.setPassword(hashedPass);
		
		userForm.setIsActive(1);
		
		User userDb = this.userService.save(userForm);
		
		userMetaService.updateByUserId(userDb.getUserId(), userMetaList);

		status.setComplete();

		waspMessage("user.created.success");
		
		return "redirect:/user/detail_rw/" + userDb.getUserId() + ".do";
	}
*/
	
	@RequestMapping(value = "/detail_rw/{userId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su')")
	public String detailRW(@PathVariable("userId") Integer userId, ModelMap m) {		
		return detail(userId,m,true);
	}
	
	@RequestMapping(value = "/detail_ro/{userId}.do", method = RequestMethod.GET)	
	public String detailRO(@PathVariable("userId") Integer userId, ModelMap m) {
		return detail(userId,m,false);
	}
	
	
	
	private String detail(Integer userId, ModelMap m,boolean isRW) {

		User user = this.userService.getById(userId);

		user.setUserMeta(getMetaHelperWebapp().syncWithMaster(user.getUserMeta()));
		
		m.addAttribute("user", user);
		
		prepareSelectListData(m);
		
		return isRW?"user/detail_rw":"user/detail_ro";
	}

}
