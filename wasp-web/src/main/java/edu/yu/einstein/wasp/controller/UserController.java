package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import edu.yu.einstein.wasp.dao.ConfirmEmailAuthDao;
import edu.yu.einstein.wasp.dao.DepartmentUserDao;
import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.exception.LoginNameException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.WebAuthenticationService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.StringHelper;


/**
 * Controller to manage users
 * 
 */

@Controller
@Transactional
@RequestMapping("/user")
public class UserController extends WaspController {

	@Autowired
	private UserMetaDao userMetaDao;

	@Autowired
	private MessageServiceWebapp messageService;
	  
	@Autowired
	private WebAuthenticationService webAuthenticationService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FilterService	filterService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ConfirmEmailAuthDao confirmEmailAuthDao;
	
	@Autowired
	private DepartmentUserDao departmentUserDao;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(UserMeta.class, request.getSession());
	}
	
	/**
	 * Prepares page to display list of users
	 * 
	 * @Author Sasha Levchuk 
	 */
	@RequestMapping("/list")
	@PreAuthorize("hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm') or hasRole('ft')")
	public String list(ModelMap m) {
		
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));
		
		prepareSelectListData(m);
	
		return "user/list";
	}
	
	
	/**
	 * Returns data to render a subgrid with user info
	 * 
	 * @Author Sasha Levchuk 
	 */
	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('da-*') or hasRole('ga') or hasRole('fm') or hasRole('ft') or User.login == principal.name")
	public String subgridJSON(@RequestParam("id") Integer userId,ModelMap m, HttpServletResponse response) {
				
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		User userDb = this.userDao.getById(userId);
		
		List<LabUser> uLabs=userDb.getLabUser();
		
		try {
//			jqgrid.put("page","1");
//			jqgrid.put("records",max+"");
//			jqgrid.put("total",max+"");

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			for (LabUser uLab:uLabs) {
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", uLab.getLabId());
				
				User pi = userDao.getUserByUserId(uLab.getLab().getPrimaryUserId().intValue());
				
				List<String> cellList = new ArrayList<String>(
						Arrays.asList(
								new String[] {
										"<a href=" + getServletPath() + "/job/list.do?labId=" 
										+ uLab.getLabId().intValue() 
										+ "&UserId=" + userId + ">" + 
											//uLab.getLab().getName() + 
											pi.getFirstName() + " " + pi.getLastName() + " " + "Lab" +
											"</a>"
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
		//Parameter coming from url anchor within lab grid (not coming from the filterToolbar)
		String userIdFromURL = request.getParameter("selId");//if not passed, UserId is the empty string (interestingly, it's value is not null)
		//logger.debug("selId = " + userIdFromURL);logger.debug("sidx = " + sidx);logger.debug("sord = " + sord);logger.debug("search = " + search);logger.debug("selId = " + selId);

		//Parameters coming from grid's toolbar
		//The jobGrid's toolbar's is it's search capability. The toolbar's attribute stringResult is currently set to false, 
		//meaning that each parameter on the toolbar is sent as a key:value pair
		String login = request.getParameter("login")==null?null:request.getParameter("login").trim();//null if this parameter is not passed
		String firstName = request.getParameter("firstName")==null?null:request.getParameter("firstName").trim();//null if this parameter is not passed
		String lastName = request.getParameter("lastName")==null?null:request.getParameter("lastName").trim();//null if this parameter is not passed
		String email = request.getParameter("email")==null?null:request.getParameter("email").trim();//null if this parameter is not passed
		//logger.debug("login = " + login);logger.debug("firstName = " + firstName);logger.debug("lastName = " + lastName);logger.debug("email = " + email);
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();

		List<User> userList = new ArrayList<User>();
		
		Map<String, Object> m = new HashMap<String, Object>();
		if(userIdFromURL != null && !userIdFromURL.isEmpty()){
			Integer userIdAsInteger = StringHelper.convertStringToInteger(userIdFromURL.trim());//returns null is unable to convert
			if(userIdAsInteger == null){
				userIdAsInteger = new Integer(0);//fake it; perform search below and no user will appear in the result set
			}
			m.put("id", userIdAsInteger.intValue());
		}
		else{
			if(login != null){
				m.put("login", login);
			}
			if(firstName != null){
				m.put("firstName", firstName);
			}
			if(lastName != null){
				m.put("lastName", lastName);
			}
			if(email != null){
				m.put("email", email);
			}
		}
		List<String> orderByList = new ArrayList<String>();
		if(sidx != null && !sidx.isEmpty() && sord != null && !sord.isEmpty() ){
			orderByList.add(sidx);
		}
		else{//default orderBy will be UserId/desc (rationale: so that when a new user is created using the grid, the viewer sees a link to prompt that they should assign a role)
			orderByList.add("id");
			sord = new String("desc");
		}
		userList = this.userDao.findByMapDistinctOrderBy(m, null, orderByList, sord);

		//perform ONLY if the viewer is A DA but is NOT any other type of facility member
		if(webAuthenticationService.isOnlyDepartmentAdministrator()){//remove users not in the DA's department
			List<User> usersToKeep = filterService.filterUserListForDA(userList);
			userList.retainAll(usersToKeep);
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
			 
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			
			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
//			if(!StringUtils.isEmpty(request.getParameter("selId")))
//			{
//				int selId = Integer.parseInt(request.getParameter("selId"));
//				int selIndex = userList.indexOf(userDao.findById(selId));
//				frId = selIndex;
//				toId = frId + 1;
//
//				jqgrid.put("records", "1");
//				jqgrid.put("total", "1");
//				jqgrid.put("page", "1");
//			}				

			List<User> userPage = userList.subList(frId, toId);
			for (User user:userPage) {
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", user.getUserId());
				 
				List<UserMeta> userMeta=getMetaHelperWebapp().syncWithMaster(user.getUserMeta());
				 
				//List<String> roles = roleService.getUniqueSortedRoleList(user);//unique list without lab affiliation
				List<String> roles = roleService.getCompleteSortedRoleList(user);//complete list with lab affilation if labmember or labmanager
				StringBuffer stringBuffer = new StringBuffer();
				int counter = 1;
				for(String role : roles){
					if(counter > 1){stringBuffer.append("<br />");}
					stringBuffer.append(role);
					counter++;
				}
				String rolesAsString;				
				if(stringBuffer.length()==0){
					rolesAsString = new String("<a href=" + getServletPath() + "/sysrole/list.do>Add Role</a><br><a href=" + getServletPath() + "/department/list.do>Confer Dept Admin</a>");
				}
				else{
					rolesAsString = new String(stringBuffer);
				}
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							user.getLogin(),
							//user.getPassword(),
							user.getFirstName(),
							user.getLastName(),	
							rolesAsString,
							user.getEmail(),
							//LOCALES.get(user.getLocale()),
							this.getLocales().get(user.getLocale()),
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
			 
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+userList,e);
		}	
	}

	/**
	 * Creates/Updates user
	 * 
	 * @Author Sasha Levchuk 
	 */	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm') or User.login == principal.name")
	public String updateDetailJSON(@RequestParam("id") Integer userId,User userForm, ModelMap m, HttpServletResponse response) {
		userId = (userId == null)? 0:userId;
		boolean adding = (userId == 0);
		if (adding || !userDao.getById(userId).getLogin().equals(userForm.getLogin())){
			boolean loginExists = false;
			try {
				loginExists = webAuthenticationService.isLoginAlreadyInUse(userForm.getLogin(), userForm.getEmail());
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
		
		int emailOwnerUserId = (userDao.getUserByEmail(userForm.getEmail()).getUserId() == null)? 0:userDao.getUserByEmail(userForm.getEmail()).getUserId();
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
		//NV 12282011 - The code userForm.setUserId(UserId) below throws exception: "detached entity passed to persist" when adding a new user. 
		//				Do not set UserId for a new user here - it is configured as a generated value, therefore, Hibernate expects UserId to be null when EntityManager#persist is called.
		//userForm.setUserId(UserId);
		if (!adding) {
			userForm.setUserId(userId);
		}
		if (adding) {
			// set random password. We don't care what it is as new user will be prompted to
			// set a new one via email.
			userForm.setPassword(webAuthenticationService.encodePassword(webAuthenticationService.getRandomPassword(10)));
			userForm.setIsActive(1);
			User userDb = this.userDao.save(userForm);
			userId=userDb.getId();
			try {
				userMetaDao.setMeta(userMetaList, userId);
				emailService.informUserAccountCreatedByAdmin(userDb, userService.getNewAuthcodeForUser(userDb));
			} catch (MetadataException e){
				logger.warn(e.getLocalizedMessage());
				try{
					response.getWriter().println(messageService.getMessage("user.updated_fail.error"));
				} catch (Throwable e1) {
					throw new IllegalStateException("Cant output error "+messageService.getMessage("user.updated_fail.error"),e1);
				}
				return null;
			}
		} else {
			User userDb = this.userDao.getById(userId);
			
			if (!userDb.getEmail().equals(userForm.getEmail())){
				//myemailChanged = true;
				if (webAuthenticationService.getAuthenticatedUser().getUserId().intValue() == userId.intValue()) {
					try {
						response.getWriter().println(messageService.getMessage("user.updated_fail.error"));
						return null;
					} catch (Throwable e) {
						throw new IllegalStateException("Cant output success message ",e);
					}
				}
				emailService.sendUserEmailConfirm(userForm, userService.getNewAuthcodeForUser(userForm));

			}
			if (!userDb.getLogin().equals(userForm.getLogin())){
				if (webAuthenticationService.getAuthenticatedUser().getUserId().intValue() == userId.intValue()) {
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
			this.userDao.merge(userDb);
			try {
				userMetaDao.setMeta(userMetaList, userId);
			} catch (MetadataException e){
				logger.warn(e.getLocalizedMessage());
				try{
					response.getWriter().println(messageService.getMessage("user.updated_fail.error"));
				} catch (Throwable e1) {
					throw new IllegalStateException("Cant output error "+messageService.getMessage("user.updated_fail.error"),e1);
				}
				return null;
			}
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
		User user = webAuthenticationService.getAuthenticatedUser();		
		return this.detailRO(user.getUserId(), m);
	}
	
	@RequestMapping(value = "/me_rw", method = RequestMethod.GET)
	public String myDetail(ModelMap m) {
		User user = webAuthenticationService.getAuthenticatedUser();		
		return this.detailRW(user.getUserId(), m);
	}
	
	@RequestMapping(value = "/me_rw", method = RequestMethod.POST)
	public String updateDetail(@Valid User userForm, BindingResult result,
			SessionStatus status, ModelMap m) {
		User user = webAuthenticationService.getAuthenticatedUser();		

		return updateDetail(user.getUserId(), userForm, result, status, m);
	}
	
	/**
	 * Updates user
	 * 
	 * @Author Sasha Levchuk 
	 */
	@RequestMapping(value = "/detail_rw/{UserId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su')")
	public String updateDetail(@PathVariable("UserId") Integer userId,
			@Valid User userForm, BindingResult result, SessionStatus status,
			ModelMap m) {
		
		userId = (userId == null)? 0:userId;
		// return read only version of page if cancel button pressed
		String submitValue = request.getParameter("submit");
		if ( submitValue.equals(messageService.getMessage("userDetail.cancel.label")) ){
			if (userId.intValue() == webAuthenticationService.getAuthenticatedUser().getUserId().intValue()){
				return "redirect:/user/me_ro.do";
			}
			return "redirect:/user/detail_ro/" + userId + ".do";
		}
		MetaHelperWebapp metaHelper = getMetaHelperWebapp();
		List<UserMeta> userMetaList = metaHelper.getFromRequest(request, UserMeta.class);

		userForm.setUserMeta(userMetaList);
		
		logger.debug("userMeta: " + StringUtils.join(userMetaList, ","));
		
		metaHelper.validate(result);
		userForm.setId(userId);
		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspErrorMessage("user.updated.error");
			return "user/detail_rw";
		}
		boolean isMyEmailChanged = false;
		User userDb = this.userDao.getById(userId);
		if (!userDb.getEmail().equals(userForm.getEmail().trim())){
			// email changed
			emailService.sendUserEmailConfirm(userForm, userService.getNewAuthcodeForUser(userForm));
			if (userId.intValue() == webAuthenticationService.getAuthenticatedUser().getUserId().intValue()) isMyEmailChanged = true;
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
		
		//this.userDao.merge(userDb);

		try {
			userService.getUserMetaDao().setMeta(userMetaList, userId);
			status.setComplete();

			waspMessage("user.updated_success.label");
			
		} catch (Exception e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("user.updated.error");
		}
		if (isMyEmailChanged){
			webAuthenticationService.logoutUser();
			return "redirect:/auth/confirmemail/emailchanged.do";
		}
		if (userId.intValue() == webAuthenticationService.getAuthenticatedUser().getUserId().intValue()){
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
				waspErrorMessage("user.mypassword_missingparam.error");
				return "user/mypassword";
		}
		
		User user = webAuthenticationService.getAuthenticatedUser();
		String currentPasswordAsHash = user.getPassword();//this is from database and is hashed
		String oldPasswordAsHash = webAuthenticationService.encodePassword(oldpassword);//oldpassword is from the form, so must hash it for comparison
		  
		if(!webAuthenticationService.matchPassword(currentPasswordAsHash, oldPasswordAsHash)){
			waspErrorMessage("user.mypassword_cur_mismatch.error");
			return "user/mypassword";
		}
		else if(!webAuthenticationService.matchPassword(newpassword1, newpassword2)){
			waspErrorMessage("user.mypassword_new_mismatch.error");
		    return "user/mypassword";
		}
		else if(webAuthenticationService.matchPassword(oldpassword, newpassword1)){//make sure old and new passwords differ
			waspErrorMessage("user.mypassword_nodiff.error");
		    return "user/mypassword";
		}
		else if(!webAuthenticationService.validatePassword(newpassword1)){//at least 8 char, at least one letter; at least one number
			waspErrorMessage("user.mypassword_new_invalid.error");
		    return "user/mypassword";
		}
		  
		user.setPassword( webAuthenticationService.encodePassword(newpassword1) ); 
		userDao.merge(user);
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
			User user = userDao.getUserByLogin(userForm.getLogin());
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
			waspErrorMessage("user.created.error");
			return "user/detail_rw";
		}

		userForm.setLastUpdTs(new Date());

		//PasswordEncoder encoder = new Md5PasswordEncoder();
		PasswordEncoder encoder = new ShaPasswordEncoder();
		String hashedPass = encoder.encodePassword(userForm.getPassword(), null);
		userForm.setPassword(hashedPass);
		
		userForm.setIsActive(1);
		
		User userDb = this.userDao.save(userForm);
		
		userMetaDao.updateByUserId(userDb.getUserId(), userMetaList);

		status.setComplete();

		waspMessage("user.created.success");
		
		return "redirect:/user/detail_rw/" + userDb.getUserId() + ".do";
	}
*/
	
	@RequestMapping(value = "/detail_rw/{UserId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su')")
	public String detailRW(@PathVariable("UserId") Integer userId, ModelMap m) {		
		return detail(userId,m,true);
	}
	
	@RequestMapping(value = "/detail_ro/{UserId}.do", method = RequestMethod.GET)	
	public String detailRO(@PathVariable("UserId") Integer userId, ModelMap m) {
		return detail(userId,m,false);
	}
	
	
	
	private String detail(Integer userId, ModelMap m,boolean isRW) {

		User user = this.userDao.getById(userId);

		user.setUserMeta(getMetaHelperWebapp().syncWithMaster(user.getUserMeta()));
		
		m.addAttribute("user", user);
		m.addAttribute("isAuthenticationExternal", webAuthenticationService.isAuthenticationSetExternal());
		
		prepareSelectListData(m);
		
		return isRW?"user/detail_rw":"user/detail_ro";
	}

}
