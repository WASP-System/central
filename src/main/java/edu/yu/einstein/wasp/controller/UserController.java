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
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.PasswordService;
import edu.yu.einstein.wasp.service.UserMetaService;
import edu.yu.einstein.wasp.service.UserService;

@Controller
@Transactional
@RequestMapping("/user")
public class UserController extends WaspController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMetaService userMetaService;

	@Autowired
	private BeanValidator validator;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordService passwordService;
	  
	@Autowired
	private MetaValidator metaValidator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	private static final MetaAttribute.Area AREA = MetaAttribute.Area.user;
	
	@RequestMapping("/list")
	@PreAuthorize("hasRole('god')")
	public String list(ModelMap m) {
		
		m.addAttribute("_metaList", MetaUtil.getMasterList(MetaBase.class, AREA, getBundle()));
		m.addAttribute("_area", AREA.name());		
		
		
		
		/*
		ObjectMapper mapper = new ObjectMapper();
		
		 try {
			 String json=mapper.writeValueAsString(FIELD_LIST);
			 m.addAttribute("fieldsArr", json);
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+FIELD_LIST,e);
		 }*/
		 prepareSelectListData(m);
		 return "user-list";
	}
	
	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or User.login == principal.name")
	public @ResponseBody String subgridJSON(@RequestParam("id") Integer userId,ModelMap m, HttpServletResponse response) {
				
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		User userDb = this.userService.getById(userId);
		
		List<Lab> puLabs=userDb.getLab();
		
		List<LabUser> uLabs=userDb.getLabUser();
		
		List <Job> uJobs=userDb.getJob();
		
		List <Sample> uSamples=userDb.getSample();
		
		//get max lenth of the previous 4 lists
		int max=Math.max(Math.max(puLabs.size(), uLabs.size()),Math.max(uJobs.size(),uSamples.size()));
		
		if (max==0) {
			Lab lab=new Lab();
		
			puLabs.add(lab);
			
			LabUser lu=new LabUser();
			lu.setLab(lab);
			uLabs.add(lu);
		
			uJobs.add(new Job());
			
			uSamples.add(new Sample());
			
			max=1;
		}
		
		String [][] mtrx = new String[max][4]	;
		
	 	ObjectMapper mapper = new ObjectMapper();
    	
		 try {
			
			 jqgrid.put("page","1");
			 jqgrid.put("records",max+"");
			 jqgrid.put("total",max+"");
			 
			 String text;
			 
			 int i=0;
			 int j=0;
			 for (Lab lab:puLabs) {
				 
				 text=lab.getLabId()==0?"No Labs":"<a href=/wasp/lab/list.do?selId="+lab.getLabId()+">"+lab.getName()+"</a>";
				 mtrx[j][i]=text;
				 
				 j++;
				 
			 }

			 i=1;
			 j=0;
			 for (LabUser lab:uLabs) {		
				 text=lab.getLab().getLabId()==0?"No Labs":"<a href=/wasp/lab/list.do?selId="+lab.getLab().getLabId()+">"+lab.getLab().getName()+"</a>";	
				 mtrx[j][i]=text;
				 
				 j++;
				 
			 }
			 
			 i=2;
			 j=0;
			 for (Job job:uJobs) {		
				
				 text=job.getJobId()==0?"No Jobs":job.getName();
				 mtrx[j][i]=text;
				 
				 j++;
				 
			 }
			 
			 i=3;
			 j=0;
			 for (Sample sample:uSamples) {		
					
				 text=sample.getSampleId()==0?"No Samples":sample.getName();
				 mtrx[j][i]=text;
				 
				 j++;
				 
			 }

			 List<Map> rows = new ArrayList<Map>();

			 for(j=0;j<max;j++) {
				 
				 Map cell = new HashMap();
				 rows.add(cell);
				 
				 cell.put("id", j+"");
				 List<String> cellList=Arrays.asList(mtrx[j]);
				 cell.put("cell", cellList);		
			 }
			 
			jqgrid.put("rows",rows);
			 
		    String json=mapper.writeValueAsString(jqgrid);
			 
			 return json;
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+puLabs,e);
		 }
	
	}
	
	
	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public @ResponseBody String getListJSON() {
	
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		
		List<User> userList;
		
		if (request.getParameter("_search")==null || StringUtils.isEmpty(request.getParameter("searchString"))) {
			userList = this.userService.findAll();
		} else {
			
			  Map<String, String> m = new HashMap<String, String>();
			  
			  m.put(request.getParameter("searchField"), request.getParameter("searchString"));
			  				  
			  userList = this.userService.findByMap(m);
			  
			  if ("ne".equals(request.getParameter("searchOper"))) {
				  List<User> allUsers=new ArrayList<User>(this.userService.findAll());
				  for(Iterator<User> it=userList.iterator();it.hasNext();)  {
					  User excludeUser=it.next();
					  allUsers.remove(excludeUser);
				  }
				  userList=allUsers;
			  }
		}

    	ObjectMapper mapper = new ObjectMapper();
    	
		 try {
			 //String users = mapper.writeValueAsString(userList);
			 jqgrid.put("page","1");
			 jqgrid.put("records",userList.size()+"");
			 jqgrid.put("total",userList.size()+"");
			 
			 
			 Map<String, String> userData=new HashMap<String, String>();
			 userData.put("page","1");
			 userData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			 jqgrid.put("userdata",userData);
			 
			 List<Map> rows = new ArrayList<Map>();
			 
			 for (User user:userList) {
				 Map cell = new HashMap();
				 cell.put("id", user.getUserId());
				 
				 List<UserMeta> userMeta=MetaUtil.syncWithMaster(user.getUserMeta(), AREA, UserMeta.class);
				 					
				 MetaUtil.setAttributesAndSort(userMeta, AREA,getBundle());
				 
				 List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							user.getLogin(),
							"",//password - always empty
							user.getFirstName(),
							user.getLastName(),						
							user.getEmail(),
							LOCALES.get(user.getLocale()),
							user.getIsActive()==1?"yes":"no"
				}));
				 
				for(UserMeta meta:userMeta) {
					cellList.add(meta.getV());
				}
				
				 
				 cell.put("cell", cellList);
				 
				 rows.add(cell);
			 }

			 
			 jqgrid.put("rows",rows);
			 
			 String json=mapper.writeValueAsString(jqgrid);
			 
			 return json;
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+userList,e);
		 }
	
	}

	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god') or User.login == principal.name")
	public String updateDetailJSON(@RequestParam("id") Integer userId,User userForm, ModelMap m, HttpServletResponse response) {

		if ( userService.loginExists(userForm.getLogin(), userId)) {
						
			try {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println(getMessage("user.login.exists_error"));
				return null;
			} catch (Throwable e) {
				throw new IllegalStateException("Cant output validation error "+getMessage("user.login.exists_error"),e);
			}
		
		}
				
		List<UserMeta> userMetaList = MetaUtil.getMetaFromJSONForm(request,AREA, UserMeta.class, getBundle());
		
		userForm.setUserMeta(userMetaList);

		boolean adding=userId==0;
		
		if (adding) {
			PasswordEncoder encoder = new ShaPasswordEncoder();
			String hashedPass = encoder.encodePassword(userForm.getPassword(), null);
			userForm.setPassword(hashedPass);
			userForm.setLastUpdTs(new Date());
			userForm.setIsActive(1);
			
			User userDb = this.userService.save(userForm);
			
			userId=userDb.getUserId();
		} else {
			User userDb = this.userService.getById(userId);
			userDb.setFirstName(userForm.getFirstName());
			userDb.setLastName(userForm.getLastName());
			if (userForm.getPassword()!=null && !userForm.getPassword().trim().isEmpty()) {
				PasswordEncoder encoder = new ShaPasswordEncoder();
				String hashedPass = encoder.encodePassword(userForm.getPassword(), null);
				userForm.setPassword(hashedPass);
			} else {
				userForm.setPassword(userDb.getPassword());
			}
			userDb.setEmail(userForm.getEmail());
			userDb.setLocale(userForm.getLocale());
			userDb.setIsActive(userForm.getIsActive());
			userDb.setLogin(userForm.getLogin());
			userDb.setLastUpdTs(new Date());
			userDb.setPassword(userForm.getPassword());

			this.userService.merge(userDb);
		}


		for (UserMeta meta : userMetaList) {
			meta.setUserId(userId);
		}

		userMetaService.updateByUserId(userId, userMetaList);

		/*
		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspMessage("user.created.error");
			return "user/detail_rw";
		}
		*/

		userForm.setLastUpdTs(new Date());

		userForm.setPassword( passwordService.encodePassword(userForm.getPassword()) );
		
		//waspMessage("user.updated.success");
		
		//emailService.sendNewPassword(userDb, "new pass");
		
		try {
			response.getWriter().println(adding?getMessage("user.created.success"):getMessage("user.updated.success"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	
	    
	}
	
	
	@RequestMapping(value = "/me.do", method = RequestMethod.GET)
	public String myDetail(ModelMap m) {
		User user = this.getAuthenticatedUser();		
		return this.detailRW(user.getUserId(), m);
	}

	@RequestMapping(value = "/me.do", method = RequestMethod.POST)
	public String updateDetail(@Valid User userForm, BindingResult result,
			SessionStatus status, ModelMap m) {
		User user = this.getAuthenticatedUser();		

		updateDetail(user.getUserId(), userForm, result, status, m);

		return "redirect:" + "me.do";
	}
	
	@RequestMapping(value = "/detail_rw/{userId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god') or User.login == principal.name")
	public String updateDetail(@PathVariable("userId") Integer userId,
			@Valid User userForm, BindingResult result, SessionStatus status,
			ModelMap m) {

		List<UserMeta> userMetaList = MetaUtil.getMetaFromForm(request,
				AREA, UserMeta.class, getBundle());

		for (UserMeta meta : userMetaList) {
			meta.setUserId(userId);
		}

		userForm.setUserMeta(userMetaList);

		List<String> validateList = new ArrayList<String>();

		for (UserMeta meta : userMetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}

		metaValidator.validate(validateList, userMetaList, result, AREA);

		if (result.hasErrors()) {
			userForm.setUserId(userId);
			prepareSelectListData(m);
			waspMessage("user.updated.error");
			return "user/detail_rw";
		}

		User userDb = this.userService.getById(userId);
		userDb.setFirstName(userForm.getFirstName());
		userDb.setLastName(userForm.getLastName());
		if (userForm.getPassword()!=null && !userForm.getPassword().trim().isEmpty()) {
			PasswordEncoder encoder = new ShaPasswordEncoder();
			String hashedPass = encoder.encodePassword(userForm.getPassword(), null);
			userForm.setPassword(hashedPass);
		}
		userDb.setEmail(userForm.getEmail());
		userDb.setLocale(userForm.getLocale());

		userDb.setLastUpdTs(new Date());

		this.userService.merge(userDb);

		userMetaService.updateByUserId(userId, userMetaList);

		MimeMessageHelper a;
		
		status.setComplete();

		waspMessage("user.updated.success");
		
		//emailService.sendNewPassword(userDb, "new pass");
		
		return "redirect:" + userId + ".do";
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
				waspMessage("user.mypassword.missingparam.error");
				return "user/mypassword";
		}
		
		User user = this.getAuthenticatedUser();
		String currentPasswordAsHash = user.getPassword();//this is from database and is hashed
		String oldPasswordAsHash = passwordService.encodePassword(oldpassword);//oldpassword is from the form, so must hash it for comparison
		  
		  logger.debug("one");logger.debug(currentPasswordAsHash);
		  logger.debug("two");logger.debug(oldPasswordAsHash);
		  logger.debug("three");
		  
		//if(!currentPasswordAsHash.equals(oldPasswordAsHash)){//current from db; old is from form as hash
		if(!passwordService.matchPassword(currentPasswordAsHash, oldPasswordAsHash)){
			waspMessage("user.mypassword.cur_mismatch.error");
			return "user/mypassword";
		}
		//else if(!newpassword1.equals(newpassword2)){//both from form
		else if(!passwordService.matchPassword(newpassword1, newpassword2)){
			waspMessage("user.mypassword.new_mismatch.error");
		    return "user/mypassword";
		}
		//else if(oldpassword.equals(newpassword1)){
		else if(passwordService.matchPassword(oldpassword, newpassword1)){//make sure old and new passwords differ
			waspMessage("user.mypassword.nodiff.error");
		    return "user/mypassword";
		}
		else if(!passwordService.validatePassword(newpassword1)){//at least 8 char, at least one letter; at least one number
			waspMessage("user.mypassword.new_invalid.error");
		    return "user/mypassword";
		}
		  
		user.setPassword( passwordService.encodePassword(newpassword1) ); 
		userService.merge(user);
		waspMessage("user.mypassword.ok");	
		return "redirect:/dashboard.do";		 
	}

	@RequestMapping(value = "/create/form.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String showEmptyForm(ModelMap m) {
	
		User user = new User();

		user.setUserMeta(MetaUtil.getMasterList(UserMeta.class, AREA,getBundle()));
		
		m.addAttribute(AREA.name(), user);
		
		prepareSelectListData(m);		

		return AREA + "/detail_rw";
	}
	
	
	@RequestMapping(value = "/create/form.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god')")
	public String create(@Valid User userForm, BindingResult result,
			SessionStatus status, ModelMap m) {

		List<UserMeta> userMetaList = MetaUtil.getMetaFromForm(request,
				AREA, UserMeta.class, getBundle());
		
		userForm.setUserMeta(userMetaList);

		// manually validate login and password
		Errors errors = new BindException(result.getTarget(), AREA.name());
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
		validateList.add(MetaValidator.Constraint.NotEmpty.name());
		validateList.add("password");
		validateList.add(MetaValidator.Constraint.NotEmpty.name());

		for (UserMeta meta : userMetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}
		metaValidator.validate(validateList, userMetaList, result, AREA);
		
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
		
		for (UserMeta um : userMetaList) {
			um.setUserId(userDb.getUserId());
		}
		;

		userMetaService.updateByUserId(userDb.getUserId(), userMetaList);

		status.setComplete();

		waspMessage("user.created.success");
		
		return "redirect:/user/detail_rw/" + userDb.getUserId() + ".do";
	}

	@RequestMapping(value = "/detail_rw/{userId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String detailRW(@PathVariable("userId") Integer userId, ModelMap m) {		
		return detail(userId,m,true);
	}
	
	@RequestMapping(value = "/detail_ro/{userId}.do", method = RequestMethod.GET)	
	public String detailRO(@PathVariable("userId") Integer userId, ModelMap m) {
		return detail(userId,m,false);
	}
	
	private String detail(Integer userId, ModelMap m,boolean isRW) {

		User user = this.userService.getById(userId);

		user.setUserMeta(MetaUtil.syncWithMaster(user.getUserMeta(), AREA, UserMeta.class));
		
		MetaUtil.setAttributesAndSort(user.getUserMeta(), AREA,getBundle());

		m.addAttribute(AREA.name(), user);
		
		prepareSelectListData(m);
		
		return isRW?"user/detail_rw":"user/detail_ro";
	}

}
