package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.LabMeta;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.LabUserService;
import edu.yu.einstein.wasp.service.LabMetaService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.UserService;
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
		
		/*ObjectMapper mapper = new ObjectMapper();
		
		 try {
			 String json=mapper.writeValueAsString(FIELD_LIST);
			 m.addAttribute("fieldsArr", json);
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+FIELD_LIST,e);
		 }*/
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
							allUsers.get(lab.getPrimaryUserId()),
							allDepts.get(lab.getDepartmentId()),
							lab.getIsActive()==1?"yes":"no"
				}));
				 
				for(LabMeta meta:labMeta) {
					cellList.add(meta.getV());
				}				
				 
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
	
	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	public String updateDetailJSON(@RequestParam("id") Integer labId,Lab labForm, ModelMap m, HttpServletResponse response) {
				
		List<LabMeta> labMetaList = MetaUtil.getMetaFromForm(request,
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
	
	@RequestMapping(value = "/create/form.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String showEmptyForm(ModelMap m) {

		Lab lab = new Lab();

		lab.setLabMeta(MetaUtil.getMasterList(LabMeta.class, AREA,getBundle()));

		m.addAttribute(AREA.name(), lab);
		
		prepareSelectListData(m);
		
		return AREA + "/detail_rw";
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
	

	@RequestMapping(value = "/create/form.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god')")
	public String create(@Valid Lab labForm, BindingResult result,
			SessionStatus status, ModelMap m) {

		// read properties from form

		List<LabMeta> labMetaList = MetaUtil.getMetaFromForm(request, AREA,
				LabMeta.class, getBundle());

		labForm.setLabMeta(labMetaList);

		// manually validate login and password

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
		;

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

		m.addAttribute("lab", lab);
		m.addAttribute("labuser", labUser);

		return "lab/user";
	}

	@RequestMapping(value = "/user/role/{labId}/{userId}/{roleName}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String userDetail ( @PathVariable("labId") Integer labId, @PathVariable("userId") Integer userId, @PathVariable("roleName") String roleName, ModelMap m) {

		LabUser labUser = labUserService.getLabUserByLabIdUserId(labId, userId); 
		Role role = roleService.getRoleByRoleName(roleName);

		labUser.setRoleId(role.getRoleId());
		labUserService.merge(labUser);

		return "redirect:/lab/user/" + labId + ".do";
	}

	@RequestMapping(value = "/request.do", method = RequestMethod.GET)
	public String showRequestAccessForm(ModelMap m) {
		return "lab/request";
	}

	@RequestMapping(value = "/request.do", method = RequestMethod.POST)
	public String requestAccess( @RequestParam("primaryuseremail") String primaryuseremail, ModelMap m) {
		// check existance of primaryUser/lab
		User primaryUser = userService.getUserByEmail(primaryuseremail);
		if (primaryUser.getUserId() == 0) {
			MessageTag.addMessage(request.getSession(), "labuser.request.primaryuser.error");
			return "redirect:/lab/request.do";
		}

		Lab lab = labService.getLabByPrimaryUserId(primaryUser.getUserId());
		if (lab.getLabId() == 0) {
			MessageTag.addMessage(request.getSession(), "labuser.request.primaryuser.error");
			return "redirect:/lab/request.do";
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
		  		return "redirect:/lab/request.do";
 	  		}

			if (alreadyAccessRoles.contains(labUser.getRole().getRoleName())) {
				MessageTag.addMessage(request.getSession(), "labuser.request.alreadyaccess.error");
				  return "redirect:/lab/request.do";
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

		return "redirect:/lab/request.do";

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

	@RequestMapping(value = "/pendinguser/list/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String pendingLabUserList (
			@PathVariable("labId") Integer labId, ModelMap m) {

		return "redirect:/lab/pendinguser/list";
	}

	@RequestMapping(value = "/pendinguser/detail/{labId}/{userId}/{roleName}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String pendingLabUserDetail (
			@PathVariable("labId") Integer labId, @PathVariable("userId") Integer userId, @PathVariable("roleName") String roleName, ModelMap m) {

		LabUser labUser = labUserService.getLabUserByLabIdUserId(labId, userId); 
		Role role = roleService.getRoleByRoleName(roleName);

		labUser.setRoleId(role.getRoleId());
		labUserService.merge(labUser);

		return "redirect:/lab/user/" + labId + ".do";
	}

/* moved to Wasp Controller?
	private void prepareSelectListData(ModelMap m) {
		List<User> users=userService.findAll();
		List<User> usersLight=new ArrayList<User>();
		for(User user: users) {
			User u = new User();
			u.setUserId(user.getUserId());
			u.setFirstName(user.getFirstName()+" "+user.getLastName());
			
			usersLight.add(u);
		}
		
		m.addAttribute("pusers", usersLight);
		m.addAttribute("countries", Country.getList());
		m.addAttribute("states", State.getList());
		m.addAttribute("departments", deptService.findAll());
	}
*/
}
