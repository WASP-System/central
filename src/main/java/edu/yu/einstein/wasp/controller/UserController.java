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
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
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
import edu.yu.einstein.wasp.service.PasswordEncoderService;
import edu.yu.einstein.wasp.service.UserMetaService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.taglib.MessageTag;

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
	private PasswordEncoderService passwordEncoderService;

	@Autowired
	private MappingJacksonHttpMessageConverter jsonnMapper;
	
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
			lab.setName("No labs");
			puLabs.add(lab);
			
			LabUser lu=new LabUser();
			lu.setLab(lab);
			uLabs.add(lu);
			
			
			Job job = new Job();
			job.setName("No jobs");
			uJobs.add(job);
			
			Sample sample = new Sample();
			sample.setName("No samples");
			uSamples.add(sample);
			
			max=1;
		}
		
		String [][] mtrx = new String[max][4]	;
		
	 	ObjectMapper mapper = new ObjectMapper();
    	
		 try {
			
			 jqgrid.put("page","1");
			 jqgrid.put("records",max+"");
			 jqgrid.put("total",max+"");
			 
			 int i=0;
			 int j=0;
			 for (Lab lab:puLabs) {		
			
				 mtrx[j][i]="<a href=/wasp/lab/list.do?selId="+lab.getLabId()+">"+lab.getName()+"</a>";
				 
				 j++;
				 
			 }

			 i=1;
			 j=0;
			 for (LabUser lab:uLabs) {		
					
				 mtrx[j][i]="<a href=/wasp/lab/list.do?selId="+lab.getLab().getLabId()+">"+lab.getLab().getName()+"</a>";
				 
				 j++;
				 
			 }
			 
			 i=2;
			 j=0;
			 for (Job job:uJobs) {		
					
				 mtrx[j][i]=job.getName();
				 
				 j++;
				 
			 }
			 
			 i=3;
			 j=0;
			 for (Sample sample:uSamples) {		
					
				 mtrx[j][i]=sample.getName();
				 
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
				
		List<UserMeta> userMetaList = MetaUtil.getMetaFromForm(request,
				AREA, UserMeta.class, getBundle());
		
		userForm.setUserMeta(userMetaList);

		if (userId==0) {
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
			MessageTag.addMessage(request.getSession(), "user.created.error");
			return "user/detail_rw";
		}
		*/

		userForm.setLastUpdTs(new Date());

		userForm.setPassword( passwordEncoderService.encodePassword(userForm.getPassword()) );
		
		//MessageTag.addMessage(request.getSession(), "user.updated.success");
		
		//emailService.sendNewPassword(userDb, "new pass");
		
		try {
			response.getWriter().println(getMessage("user.updated.success"));
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

		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(userMetaList, result, AREA);

		if (result.hasErrors()) {
			userForm.setUserId(userId);
			prepareSelectListData(m);
			MessageTag.addMessage(request.getSession(), "user.updated.error");
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

		MessageTag.addMessage(request.getSession(), "user.updated.success");
		
		//emailService.sendNewPassword(userDb, "new pass");
		
		return "redirect:" + userId + ".do";
	}


	
	@RequestMapping(value = "/mypassword.do", method = RequestMethod.GET)
	public String myPasswordForm(ModelMap m) {
		return "user/mypassword";
	}

	@RequestMapping(value = "/mypassword.do", method = RequestMethod.POST)
	public String myPassword(
			@RequestParam(value = "passwordold") String passwordold,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "password2") String password2, ModelMap m) {
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
		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(userMetaList, result, AREA);

		if (result.hasErrors()) {
			prepareSelectListData(m);
			MessageTag.addMessage(request.getSession(), "user.created.error");
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

		MessageTag.addMessage(request.getSession(), "user.created.success");
		
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
