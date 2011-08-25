package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabMeta;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.Project;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleLab;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabPendingMeta;

import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.LabMetaService;
import edu.yu.einstein.wasp.service.LabUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.UserMetaService;
import edu.yu.einstein.wasp.service.UserPendingService;
import edu.yu.einstein.wasp.service.UserPendingMetaService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabPendingMetaService;
import edu.yu.einstein.wasp.service.DepartmentService;

import edu.yu.einstein.wasp.service.EmailService;

import edu.yu.einstein.wasp.taglib.MessageTag;

@Controller
@Transactional
@RequestMapping("/lab")
public class LabController extends WaspController {

	public static final MetaAttribute.Area AREA = MetaAttribute.Area.lab;

	@Autowired
	private LabService labService;

	@Autowired
	private LabUserService labUserService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private LabMetaService labMetaService;

	@Autowired
	private DepartmentService deptService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserMetaService userMetaService;

	@Autowired
	private UserPendingService userPendingService;

	@Autowired
	private UserPendingMetaService userPendingMetaService;

	@Autowired
	private LabPendingService labPendingService;

	@Autowired
	private LabPendingMetaService labPendingMetaService;

	@Autowired
	private BeanValidator validator;

	@Autowired
	private EmailService emailService;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	@RequestMapping("/list")
	@PreAuthorize("hasRole('god')")
	public String list(ModelMap m) {
		
		m.addAttribute("_metaList", MetaUtil.getMasterList(MetaBase.class, AREA, getBundle()));
		m.addAttribute("_area", AREA.name());

		prepareSelectListData(m);

		return "lab-list";
	}


	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public @ResponseBody String getListJSON() {
	
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<Lab> labList;
		
		if (request.getParameter("_search")==null || StringUtils.isEmpty(request.getParameter("searchString"))) {
			labList = this.labService.findAll();
		} else {
			
			Map<String, String> m = new HashMap<String, String>();
			
			m.put(request.getParameter("searchField"), request.getParameter("searchString"));

			labList = this.labService.findByMap(m);
			
			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Lab> allLabs=new ArrayList<Lab>(this.labService.findAll());
				for(Iterator<Lab> it=labList.iterator();it.hasNext();)  {
					Lab excludeLab=it.next();
					allLabs.remove(excludeLab);
				}
				labList=allLabs;
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		
		try {
			//String labs = mapper.writeValueAsString(labList);
			jqgrid.put("page","1");
			jqgrid.put("records",labList.size()+"");
			jqgrid.put("total",labList.size()+"");
			
			
			Map<String, String> userData=new HashMap<String, String>();
			userData.put("page","1");
			userData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("userdata",userData);
			
			List<Map> rows = new ArrayList<Map>();
			
			Map<Integer, String> allDepts=new TreeMap<Integer, String>();
			for(Department dept:(List<Department>)deptService.findAll()) {
				allDepts.put(dept.getDepartmentId(),dept.getName());
			}
			
			Map<Integer, String> allUsers=new TreeMap<Integer, String>();
			for(User user:(List<User>)userService.findAll()) {
				allUsers.put(user.getUserId(),user.getFirstName()+" "+user.getLastName());
			}
			
			
			for (Lab lab:labList) {
				
				Map cell = new HashMap();
				cell.put("id", lab.getLabId());
				
				List<LabMeta> labMeta=MetaUtil.syncWithMaster(lab.getLabMeta(), AREA, LabMeta.class);
									
				MetaUtil.setAttributesAndSort(labMeta, AREA,getBundle());
				
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						lab.getName(),
						"<a href=/wasp/user/list.do?selId="+lab.getPrimaryUserId()+">"+allUsers.get(lab.getPrimaryUserId()) +"</a>",							
							allDepts.get(lab.getDepartmentId()),
						
							lab.getIsActive()==1?"yes":"no"
				}));
				 
				for(LabMeta meta:labMeta) {
					cellList.add(meta.getV());
				}				
				 
				int l= cellList.size();
				 cell.put("cell", cellList);
				 
				 rows.add(cell);
			 }

			 
			 jqgrid.put("rows",rows);
			 
			 String json=mapper.writeValueAsString(jqgrid);
			 
			 return json;
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+labList,e);
		 }
	
	}
	
	
	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)	
	public @ResponseBody String subgridJSON(@RequestParam("id") Integer labId,ModelMap m, HttpServletResponse response) {
			
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		Lab labDb = this.labService.getById(labId);
		
		
		List<LabUser> users=labDb.getLabUser();
		
		List<Project> projects=labDb.getProject();
		
		List<Sample> samples=labDb.getSample();
		
		List<AcctGrant> accGrants=labDb.getAcctGrant();
		
		List<SampleLab> sampleLabs=labDb.getSampleLab();
		
		List<Job> jobs=labDb.getJob();
		
		
		//get max lenth of the previous 4 lists
		int max=Math.max(Math.max(users.size(), projects.size()),Math.max(samples.size(),accGrants.size()));
		
		max=Math.max(max,Math.max(sampleLabs.size(), jobs.size()));
		
		
		if (max==0) {
			LabUser lUser = new LabUser();			
			lUser.setUser(new User());			
			users.add(lUser);
			
			projects.add(new Project());
			
			samples.add(new Sample());
			
			accGrants.add(new AcctGrant());
			
			SampleLab sampleLab=new SampleLab();
			sampleLab.setLab(new Lab());
			sampleLabs.add(sampleLab);
			
			jobs.add(new Job());
						
			max=1;
		}
		
		String [][] mtrx = new String[max][6]	;
		
		ObjectMapper mapper = new ObjectMapper();

		String text;
		try {
			//String labs = mapper.writeValueAsString(labList);
			jqgrid.put("page","1");
			jqgrid.put("records",max+"");
			jqgrid.put("total",max+"");
			
			
			int i=0;
			int j=0;
			for (LabUser user:users) {		
				
				text=user.getUserId()==0?"No Users":"<a href=/wasp/user/list.do?selId="+user.getUserId()+">"+user.getUser().getFirstName() + " "+user.getUser().getLastName()+"</a>";
				mtrx[j][i]=text;
				
				j++;
				
			}

			i++;
			j=0;
			for (Project project:projects) {		
					
				text=project.getProjectId()==0?"No Projects":project.getName();
				mtrx[j][i]=text;
				
				j++;
				
			}
			
			i++;
			j=0;
			for (Sample sample:samples) {		
					
				text=sample.getSampleId()==0?"No Samples":sample.getName();
				mtrx[j][i]=text;
				
				j++;
				
			}
			
			i++;
			j=0;
			for (AcctGrant acc:accGrants) {		
					
				text=acc.getGrantId()==0?"No Acc Grants":acc.getName();
				mtrx[j][i]=text;
				
				j++;
				
			}
			
			i++;
			j=0;
			for (SampleLab sampleLab:sampleLabs) {		
					
				text=sampleLab.getLab().getLabId()==0?"No Sample Labs":sampleLab.getLab().getName();
				mtrx[j][i]=text;
				
				j++;
				
			}
			
			i++;
			j=0;
			for (Job job:jobs) {		
					
				text=job.getJobId()==0?"No Jobs":job.getName();
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
			throw new IllegalStateException("Can't marshall to JSON "+labDb,e);
		}
	}
	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	public String updateDetailJSON(@RequestParam("id") Integer labId,Lab labForm, ModelMap m, HttpServletResponse response) {
				
		List<LabMeta> labMetaList = MetaUtil.getMetaFromJSONForm(request,
				AREA, LabMeta.class, getBundle());

		labForm.setLabMeta(labMetaList);

		if (labId==0) {
			
			labForm.setLastUpdTs(new Date());
			labForm.setIsActive(1);
			
			Lab labDb = this.labService.save(labForm);
			
			labId=labDb.getLabId();
		} else {
			Lab labDb = this.labService.getById(labId);
			labDb.setName(labForm.getName());
			labDb.setIsActive(labForm.getIsActive());
			labDb.setDepartmentId(labForm.getDepartmentId());
			labDb.setPrimaryUserId(labForm.getPrimaryUserId());

			this.labService.merge(labDb);
		}


		for (LabMeta meta : labMetaList) {
			meta.setLabId(labId);
		}

		labMetaService.updateByLabId(labId, labMetaList);

		MimeMessageHelper a;
		
		//MessageTag.addMessage(request.getSession(), "lab.updated.success");
		
		//emailService.sendNewPassword(labDb, "new pass");
		
		try {
			response.getWriter().println(getMessage("lab.updated.success"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	
	    
	}
	

	@RequestMapping(value = "/detail_rw/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lu-' + #labId)")
	public String detailRW(@PathVariable("labId") Integer labId, ModelMap m) {
		return detail(labId,m,true);
	}
	
	@RequestMapping(value = "/detail_ro/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lu-' + #labId)")
	public String detailRO(@PathVariable("labId") Integer labId, ModelMap m) {
		return detail(labId,m,false);
	}
	
	private String detail(Integer labId, ModelMap m, boolean isRW) {

		Lab lab = this.labService.getById(labId);

		lab.setLabMeta(MetaUtil.syncWithMaster(lab.getLabMeta(), AREA, LabMeta.class));

		MetaUtil.setAttributesAndSort(lab.getLabMeta(), AREA,getBundle());

		List<LabUser> labUserList = lab.getLabUser();
		labUserList.size();

		List<Job> jobList = lab.getJob();
		jobList.size();
		
		m.addAttribute(AREA.name(), lab);

		prepareSelectListData(m);
		
		return isRW?"lab/detail_rw":"lab/detail_ro";
	}
	

	@RequestMapping(value = "/create/form.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String showEmptyForm(ModelMap m) {

		Lab lab = new Lab();

		lab.setLabMeta(MetaUtil.getMasterList(LabMeta.class, AREA,getBundle()));

		m.addAttribute(AREA.name(), lab);
		
		prepareSelectListData(m);
		
		return AREA + "/detail_rw";
	}


	@RequestMapping(value = "/create/form.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god')")
	public String create(@Valid Lab labForm, BindingResult result,
			SessionStatus status, ModelMap m) {

		// read properties from form

		List<LabMeta> labMetaList = MetaUtil.getMetaFromForm(request, AREA,
				LabMeta.class, getBundle());

		labForm.setLabMeta(labMetaList);

		List<String> validateList = new ArrayList<String>();

		for (LabMeta meta : labMetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}

		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(labMetaList, result, AREA);

		if (result.hasErrors()) {
			prepareSelectListData(m);
			MessageTag.addMessage(request.getSession(), "lab.created.error");
			return "lab/detail_rw";
		}

		labForm.setLastUpdTs(new Date());

		Lab labDb = this.labService.save(labForm);
		for (LabMeta um : labMetaList) {
			um.setLabId(labDb.getLabId());
		}

		labMetaService.updateByLabId(labDb.getLabId(), labMetaList);

		status.setComplete();

		MessageTag.addMessage(request.getSession(), "lab.created.success");
		
		return "redirect:/lab/detail_rw/" + labDb.getLabId() + ".do";
	}

	@RequestMapping(value = "/detail_rw/{labId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String updateDetail(@PathVariable("labId") Integer labId,
			@Valid Lab labForm, BindingResult result, SessionStatus status,
			ModelMap m) {

		List<LabMeta> labMetaList = MetaUtil.getMetaFromForm(request, AREA,
				LabMeta.class, getBundle());

		for (LabMeta meta : labMetaList) {
			meta.setLabId(labId);
		}

		labForm.setLabMeta(labMetaList);

		List<String> validateList = new ArrayList<String>();

		for (LabMeta meta : labMetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}

		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(labMetaList, result, AREA);

		if (result.hasErrors()) {
			prepareSelectListData(m);
			MessageTag.addMessage(request.getSession(), "lab.updated.error");
			return "lab/detail_rw";
		}

		Lab labDb = this.labService.getById(labId);
		labDb.setName(labForm.getName());
		labDb.setDepartmentId(labForm.getDepartmentId());
		labDb.setPrimaryUserId(labForm.getPrimaryUserId());

		labDb.setLastUpdTs(new Date());

		this.labService.merge(labDb);

		labMetaService.updateByLabId(labId, labMetaList);

		status.setComplete();
		
		MessageTag.addMessage(request.getSession(), "lab.updated.success");
		
		return "redirect:" + labId + ".do";

	}

	@RequestMapping(value = "/user/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lu-' + #labId)")
	public String userList(@PathVariable("labId") Integer labId, ModelMap m) {
		Lab lab = this.labService.getById(labId);
		List<LabUser> labUser = lab.getLabUser();

		Map userPendingQueryMap = new HashMap();
		userPendingQueryMap.put("labId", labId);
		userPendingQueryMap.put("status", "PENDING");

		List<UserPending> userPending = userPendingService.findByMap(userPendingQueryMap);

		m.addAttribute("lab", lab);
		m.addAttribute("labuser", labUser);
		m.addAttribute("labuserpending", userPending);

		return "lab/user";
	}

	@RequestMapping(value = "/user/role/{labId}/{userId}/{roleName}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String userDetail ( @PathVariable("labId") Integer labId, @PathVariable("userId") Integer userId, @PathVariable("roleName") String roleName, ModelMap m) {

    // TODO CHECK VALID LABUSER
		LabUser labUser = labUserService.getLabUserByLabIdUserId(labId, userId); 

		if (roleName.equals("xx")) {
			// TODO CONFIRM ROLE WAS "LP"
			labUserService.remove(labUser);

			MessageTag.addMessage(request.getSession(), "hello.error");
			return "redirect:/lab/user/" + labId + ".do";
		}

    // TODO CHECK VALID ROLE NAME
		Role role = roleService.getRoleByRoleName(roleName);

    // TODO CHECK VALID ROLE FLOW

		labUser.setRoleId(role.getRoleId());
		labUserService.merge(labUser);

		// TODO ADD MESSAGE

		MessageTag.addMessage(request.getSession(), "hello.error");
		return "redirect:/lab/user/" + labId + ".do";
	}

	public Lab createLabFromLabPending(LabPending labPending) {
		Lab lab = new Lab();
		User user;

		if (labPending.getUserpendingId() != null ) {
			UserPending userPending = userPendingService.getUserPendingByUserPendingId(labPending.getUserpendingId());
			user = createUserFromUserPending(userPending);
		} else {
			user = userService.getUserByUserId(labPending.getPrimaryUserId());
		}

		lab.setPrimaryUserId(user.getUserId());
		lab.setName(labPending.getName());
		lab.setDepartmentId(labPending.getDepartmentId());
		lab.setIsActive(1);

		Lab labDb = labService.save(lab);

		// copies meta data
		Map labPendingMetaQueryMap = new HashMap();
		labPendingMetaQueryMap.put("labpendingId", labPending.getLabPendingId());
		List<LabPendingMeta> labPendingMetaList = labPendingMetaService.findByMap(labPendingMetaQueryMap); 
		for (LabPendingMeta lpm: labPendingMetaList) {
			LabMeta labMeta = new LabMeta();
			labMeta.setLabId(labDb.getLabId());

			// convert prefix
			String newK = lpm.getK().replaceAll("^.*?\\.", "lab" + ".");

			labMeta.setK(newK);
			labMeta.setV(lpm.getV());
			labMetaService.save(labMeta);
		}

		return labDb;
	}

	public User createUserFromUserPending(UserPending userPending) {
		User user = new User();

		user.setFirstName(userPending.getFirstName());
		user.setLastName(userPending.getLastName());
		user.setEmail(userPending.getEmail());
		user.setPassword(userPending.getPassword());
		user.setLocale(userPending.getLocale());

		// find me a unique login name
		String loginBase = userPending.getFirstName().substring(0, 1) + 
				userPending.getLastName();
		String login = loginBase;
		int c = 1; 
		while (userService.getUserByLogin(login).getUserId() > 0 )	{
			login = loginBase + c;
			c++;				
		}
		user.setLogin(login);
		User userDb = userService.save(user);

		//List<UserPendingMeta> userPendingMetaList = userPendingMetaService.getUserPendingMetaByUserPendingId(userPending.getUserPendingId());

		// copies meta data
		Map userPendingMetaQueryMap = new HashMap();
		userPendingMetaQueryMap.put("userpendingId", userPending.getUserPendingId());
		List<UserPendingMeta> userPendingMetaList = userPendingMetaService.findByMap(userPendingMetaQueryMap); 
		for (UserPendingMeta upm: userPendingMetaList) {
			UserMeta userMeta = new UserMeta();
			userMeta.setUserId(userDb.getUserId());

			// convert prefix
			String newK = upm.getK().replaceAll("^.*?\\.", "user" + ".");

			userMeta.setK(newK);
			userMeta.setV(upm.getV());
			userMetaService.save(userMeta);
		}

		Map userPendingQueryMap = new HashMap();
		userPendingQueryMap.put("email", userPending.getEmail());
		userPendingQueryMap.put("status", "PENDING");

		Role roleLabPending = roleService.getRoleByRoleName("lp");

		List<UserPending> userPendingList = userPendingService.findByMap(userPendingQueryMap);
		for (UserPending userPendingOther: userPendingList) {

			userPendingOther.setStatus("CREATED");
			userPendingService.save(userPendingOther);

			if (userPendingOther.getLabId() != null) {
				LabUser labUserOther = labUserService.getLabUserByLabIdUserId(userPendingOther.getLabId(), userDb.getUserId());
				if (labUserOther.getLabUserId() > 0) {
					// already created
					continue;
				}

				labUserOther.setUserId(userDb.getUserId());
				labUserOther.setLabId(userPendingOther.getLabId());
				labUserOther.setRoleId(roleLabPending.getRoleId());
				labUserService.save(labUserOther);

			}	else {

				// requesting to be a PI
				Map labPendingQueryMap = new HashMap();
				userPendingQueryMap.put("userPendingId", userPendingOther.getUserPendingId());

				List<LabPending> labPendingList = labPendingService.findByMap(labPendingQueryMap);
				for (LabPending labPending: labPendingList) {
					labPending.setUserpendingId((Integer) null);
					labPending.setPrimaryUserId(userDb.getUserId());
					labPendingService.save(labPending);
				}

			}
		}

		return userDb; 
	}


	@RequestMapping(value = "/userpending/{action}/{labId}/{userPendingId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String userPendingDetail ( @PathVariable("labId") Integer labId, @PathVariable("userPendingId") Integer userPendingId, @PathVariable("action") String action, ModelMap m) {
		// TODO CHECK ACTION IS "approve" or "reject"

		UserPending userPending = userPendingService.getUserPendingByUserPendingId(userPendingId);

		// TODO ERROR IF LABID DOESNT BELONG
		if (userPending.getLabId() != labId) {
			MessageTag.addMessage(request.getSession(), "hello.error");
		}

		if ("approve".equals(action)) {
			User user = createUserFromUserPending(userPending);

			Role roleLabUser = roleService.getRoleByRoleName("lu");

			// createUserFromUserPending, should have made this.
			LabUser labUser = labUserService.getLabUserByLabIdUserId(labId, user.getUserId());
			labUser.setRoleId(roleLabUser.getRoleId());
			labUserService.merge(labUser);
		}

		userPending.setStatus(action);
		userPendingService.save(userPending);

		// TODO SEND ACTION BASED EMAIL TO PENDING USER

		MessageTag.addMessage(request.getSession(), "hello.error");

		return "redirect:/lab/user/" + labId + ".do";
	}

	@RequestMapping(value = "/labpending/{action}/{departmentId}/{labPendingId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('la-' + #departmentId)")
	public String labPendingDetail ( @PathVariable("departmentId") Integer departmentId, @PathVariable("labPendingId") Integer labPendingId, @PathVariable("action") String action, ModelMap m) {
		// TODO CHECK ACTION IS "approve" or "reject"

		LabPending labPending = labPendingService.getLabPendingByLabPendingId(labPendingId);

		// TODO ERROR IF LABID DOESNT BELONG
		if (labPending.getDepartmentId() != departmentId) {
			MessageTag.addMessage(request.getSession(), "hello.error");
		}

		if ("approve".equals(action)) {
			Lab lab = createLabFromLabPending(labPending);
		}

		labPending.setStatus(action);
		labPendingService.save(labPending);

		// TODO SEND ACTION BASED EMAIL TO PENDING USER

		MessageTag.addMessage(request.getSession(), "hello.error");

		return "redirect:/department/detail/" + departmentId + ".do";
	}

	@RequestMapping(value = "/newrequest", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String showRequestForm (ModelMap m) {

		final MetaAttribute.Area area = MetaAttribute.Area.labPending;

    LabPending labPending = new LabPending();

    labPending.setLabPendingMeta(MetaUtil.getMasterList(LabPendingMeta.class, area,getBundle()));

    m.addAttribute(area.name(), labPending);

    prepareSelectListData(m);

		return "lab/newrequest";
	}


	@RequestMapping(value = "/newrequest", method = RequestMethod.POST)
	public String createNewLabPending (
			@Valid LabPending labPendingForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		final MetaAttribute.Area area = MetaAttribute.Area.labPending;

		List<LabPendingMeta> labPendingMetaList = MetaUtil.getMetaFromForm(request,
				area, LabPendingMeta.class, getBundle());

		labPendingForm.setLabPendingMeta(labPendingMetaList);

		Errors errors = new BindException(result.getTarget(), "labPending");

		List<String> validateList = new ArrayList<String>();
		//validateList.add("password");
		//validateList.add(MetaValidator.Constraint.NotEmpty.name());

		// ADD VALIDATION
		//   - uniq name
		//   - departmentid

		for (LabPendingMeta meta : labPendingMetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}

		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(labPendingMetaList, result, area);


		User me = getAuthenticatedUser();

		labPendingForm.setPrimaryUserId(me.getUserId());
		labPendingForm.setStatus("PENDING");

		if (result.hasErrors()) {
			prepareSelectListData(m);
			MessageTag.addMessage(request.getSession(), "user.created.error");

			return "lab/newrequest";
		}

		LabPending labPendingDb = labPendingService.save(labPendingForm);

		for (LabPendingMeta lpm : labPendingMetaList) {
			lpm.setLabpendingId(labPendingDb.getLabPendingId());
			labPendingMetaService.save(lpm);
		}

		status.setComplete();

		// TODO email DA that a new pi is pending

		MessageTag.addMessage(request.getSession(), "hello.error");

		return "redirect:/lab/newrequest.do";
	}


	@RequestMapping(value = "/request.do", method = RequestMethod.POST)
	public String requestAccess( @RequestParam("primaryuseremail") String primaryuseremail, ModelMap m) {
		// check existance of primaryUser/lab

		User primaryUser = userService.getUserByEmail(primaryuseremail);
		if (primaryUser.getUserId() == 0) {
			MessageTag.addMessage(request.getSession(), "labuser.request.primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}


		Lab lab = labService.getLabByPrimaryUserId(primaryUser.getUserId());
		if (lab.getLabId() == 0) {
			MessageTag.addMessage(request.getSession(), "labuser.request.primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}

		// check role of lab user
		User user = this.getAuthenticatedUser();
		LabUser labUser = labUserService.getLabUserByLabIdUserId(lab.getLabId(), user.getUserId());

		if (labUser.getLabUserId() != 0) {
			ArrayList<String> alreadyPendingRoles = new ArrayList();
			alreadyPendingRoles.add("lp");
			ArrayList<String> alreadyAccessRoles = new ArrayList();
			alreadyPendingRoles.add("pi");
			alreadyPendingRoles.add("lm");
			alreadyPendingRoles.add("lu");

			if (alreadyPendingRoles.contains(labUser.getRole().getRoleName())) {
				MessageTag.addMessage(request.getSession(), "labuser.request.alreadypending.error");
				return "redirect:/lab/newrequest.do";
			}

			if (alreadyAccessRoles.contains(labUser.getRole().getRoleName())) {
				MessageTag.addMessage(request.getSession(), "labuser.request.alreadyaccess.error");
				  return "redirect:/lab/newrequest.do";
			}
		}

		Role role = roleService.getRoleByRoleName("lp");

		labUser.setLabId(lab.getLabId());
		labUser.setUserId(user.getUserId());
		labUser.setRoleId(role.getRoleId());
		labUserService.save(labUser);

		labUserService.refresh(labUser);

		emailService.sendPendingLabUser(labUser);

		MessageTag.addMessage(request.getSession(), "labuser.request.success");

		return "redirect:/lab/newrequest.do";

		// TODO RESET TO DASHBOARD!
		// return "redirect:/dashboard.do";
	}


	@RequestMapping(value = "/pendinglab/list/{departmentId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('da-' + $departmentId)")
	public String pendingLabList(@PathVariable("departmentId") Integer departmentId, ModelMap m) {
		return "lab/pendinglab/list";
	}

	@RequestMapping(value = "/pendinglab/{departmentId}/{labId}/{newStatus}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
	public String pendingLab (
			@PathVariable("departmentId") Integer departmentId, 
			@PathVariable("labId") Integer labId, 
			@PathVariable("newStatus") String newStatus, ModelMap m) {

		return "redirect:/lab/pendinglab/list";
	}


	protected void prepareSelectListData(ModelMap m) {
		
		super.prepareSelectListData(m);
		
		List<User> users=userService.findAll();
		List<User> usersLight=new ArrayList<User>();
		for(User user: users) {
			User u = new User();
			u.setUserId(user.getUserId());
			u.setFirstName(user.getFirstName()+" "+user.getLastName());
			
			usersLight.add(u);
		}
		
		m.addAttribute("pusers", usersLight);
		m.addAttribute("departments", deptService.findAll());
	}

}
