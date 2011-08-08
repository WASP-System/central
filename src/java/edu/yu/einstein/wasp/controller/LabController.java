package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Labmeta;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Country;
import edu.yu.einstein.wasp.model.MetaAttribute.State;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.LabUserService;
import edu.yu.einstein.wasp.service.LabmetaService;
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
	HttpServletRequest request;

	@Autowired
	private LabmetaService labmetaService;

	@Autowired
	private DepartmentService deptService;

	@Autowired
	private UserService userService;

	@Autowired
	private BeanValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}
	
	@RequestMapping("/list")
	@PreAuthorize("hasRole('god')")
	public String list(ModelMap m) {
		List<Lab> labList = this.labService.findAll();

		m.addAttribute("lab", labList);

		return "lab/list";
	}

	@RequestMapping(value = "/create/form.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god')")
	public String showEmptyForm(ModelMap m) {

		Lab lab = new Lab();

		lab.setLabmeta(MetaUtil.getMasterList(Labmeta.class, AREA));

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

		lab.setLabmeta(MetaUtil.syncWithMaster(lab.getLabmeta(), AREA, Labmeta.class));

		MetaUtil.setAttributesAndSort(lab.getLabmeta(), AREA);

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

		List<Labmeta> labmetaList = MetaUtil.getMetaFromForm(request, AREA,
				Labmeta.class);

		// set property attributes and sort them according to "position"
		MetaUtil.setAttributesAndSort(labmetaList, AREA);

		labForm.setLabmeta(labmetaList);

		// manually validate login and password

		List<String> validateList = new ArrayList<String>();

		for (Labmeta meta : labmetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}

		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(labmetaList, result, AREA);

		if (result.hasErrors()) {
			prepareSelectListData(m);
			MessageTag.addMessage(request.getSession(), "lab.created.error");
			return "lab/detail_rw";
		}

		labForm.setLastUpdTs(new Date());
		

		Lab labDb = this.labService.save(labForm);
		for (Labmeta um : labmetaList) {
			um.setLabId(labDb.getLabId());
		}
		;

		labmetaService.updateByLabId(labDb.getLabId(), labmetaList);

		status.setComplete();

		MessageTag.addMessage(request.getSession(), "lab.created.success");
		
		return "redirect:/lab/detail_rw/" + labDb.getLabId() + ".do";
	}

	@RequestMapping(value = "/detail_rw/{labId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('god') or hasRole('lm-' + #labId)")
	public String updateDetail(@PathVariable("labId") Integer labId,
			@Valid Lab labForm, BindingResult result, SessionStatus status,
			ModelMap m) {

		List<Labmeta> labmetaList = MetaUtil.getMetaFromForm(request, AREA,
				Labmeta.class);

		for (Labmeta meta : labmetaList) {
			meta.setLabId(labId);
		}

		MetaUtil.setAttributesAndSort(labmetaList, AREA);

		labForm.setLabmeta(labmetaList);

		List<String> validateList = new ArrayList<String>();

		for (Labmeta meta : labmetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}

		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(labmetaList, result, AREA);

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

		labmetaService.updateByLabId(labId, labmetaList);

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
	    User primaryUser = userService.getUserByEmail(primaryuseremail);
	    Lab lab = labService.getLabByPrimaryUserId(primaryUser.getUserId());

	    // check existance of primaryUser/lab

	    User user = this.getAuthenticatedUser();

	    // check user does not already have access

	    // create pending labuser record


	    return "redirect:/dashboard.do";
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
	  
	private void prepareSelectListData(ModelMap m) {	
		m.addAttribute("pusers", userService.findAll());
		m.addAttribute("countries", Country.getList());
		m.addAttribute("states", State.getList());
		m.addAttribute("departments", deptService.findAll());
	}
}
