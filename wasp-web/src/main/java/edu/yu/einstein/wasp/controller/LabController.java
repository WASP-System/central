package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabMetaDao;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.LabPendingMetaDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.UserMetaDao;
import edu.yu.einstein.wasp.dao.UserPendingDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabMeta;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabPendingMeta;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.model.UserPendingMeta;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.StringHelper;

@Controller
@Transactional
@RequestMapping("/lab")
public class LabController extends WaspController {

	@Autowired
	private LabDao labDao;

	@Autowired
	private LabUserDao labUserDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private LabMetaDao labMetaDao;

	@Autowired
	private DepartmentDao deptDao;

	@Autowired
	private UserMetaDao userMetaDao;

	@Autowired
	private UserPendingDao userPendingDao;

	@Autowired
	private LabPendingDao labPendingDao;

	@Autowired
	private LabPendingMetaDao labPendingMetaDao;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FilterService filterService;
	
	@Autowired
	private JobService jobService;

	@Autowired
	private LabService labService;

	@Autowired
	private MessageServiceWebapp messageService; 
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;

	

	/**
	 * get a @{link MetaHelperWebapp} instance for working with LabMeta metadata
	 * 
	 * @return
	 */
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(LabMeta.class, request.getSession());
	}

	/**
	 * get a @{link MetaHelperWebapp} instance for working with labPending metadata
	 * 
	 * @return
	 */
	private final MetaHelperWebapp getLabPendingMetaHelperWebapp() {
		return new MetaHelperWebapp(LabPendingMeta.class,request.getSession());
	}

	/**
	 * Return list of labs in JGrid
	 * 
	 * @param m
	 * @return
	 */
	@RequestMapping("/list")
	@PreAuthorize("hasRole('su') or hasRole('da-*') or hasRole('ga-*')")
	public String list(ModelMap m) {

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "lab/list";
	}

	/**
	 * Returns list of labs
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	public String getListJSON(HttpServletResponse response) {

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		String search = request.getParameter("_search");//from grid (will return true or false, depending on the toolbar's parameters)
		logger.debug("sidx = " + sidx);logger.debug("sord = " + sord);logger.debug("search = " + search);

		//parameter from filterToolbar
		String piNameAndLogin = request.getParameter("primaryUser")==null?null:request.getParameter("primaryUser").trim();//if not passed, will be null; if passed will be firstname lastname (login)
		String departmentName = request.getParameter("departmentId")==null?null:request.getParameter("departmentId").trim();//if not passed, will be null; if passed will be name of department
		logger.debug("piNameAndLogin = " + piNameAndLogin);logger.debug("departmentName = " + departmentName);
		
		//deal with the parameter
		User pi = null;
		if(piNameAndLogin != null){//something was passed; expecting firstname lastname (login)
			String piLogin = StringHelper.getLoginFromFormattedNameAndLogin(piNameAndLogin.trim());//if fails, returns empty string
			if(!piLogin.isEmpty()){//likely incorrect format
				pi = userDao.getUserByLogin(piLogin);//if User not found, pi object is NOT null and pi.getUnserId()=null
				if(pi.getUserId()==null){//fake it
					pi.setUserId(new Integer(0));
				}
			}
		}
		Department department = null;
		if(departmentName != null){
			department = deptDao.getDepartmentByName(departmentName.trim());
			if(department.getDepartmentId()==null){//not found in department list
				department.setDepartmentId(new Integer(0));
			}
		}
		
		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Lab> labList = new ArrayList<Lab>();

		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		if(pi != null){
			queryMap.put("primaryUserId", pi.getUserId().intValue());
		}
		if(department != null){
			queryMap.put("departmentId", department.getDepartmentId().intValue());
		}
		
		List<String> orderByColumnNames = new ArrayList<String>();
		if(sidx!=null && !"".equals(sidx)){//sord is apparently never null; default is desc
			if(sidx.equals("name")){
				orderByColumnNames.add("name");
			}
			else if(sidx.equals("primaryUser")){
				orderByColumnNames.add("user.lastName"); orderByColumnNames.add("user.firstName");
			}
			else if(sidx.equals("departmentId")){
				orderByColumnNames.add("department.name");
			}
		}
		else if(sidx==null || "".equals(sidx)){
			orderByColumnNames.add("user.lastName"); orderByColumnNames.add("user.firstName");
			sord = new String("asc");
		}
		labList = labDao.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, sord);	
		
		//perform ONLY if the viewer is A DA but is NOT any other type of facility member
		if(authenticationService.isOnlyDepartmentAdministrator()){//remove labs not in the DA's department
			List<Lab> labsToKeep = filterService.filterLabListForDA(labList);
			labList.retainAll(labsToKeep);
		}
	
		try {
			// String labs = mapper.writeValueAsString(labList);
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = labList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");
			
			Map<String, String> userData = new HashMap<String, String>();
			userData.put("page", pageIndex + "");
			userData.put("selId", StringUtils.isEmpty(request.getParameter("selId")) ? "" : request.getParameter("selId"));
			jqgrid.put("userdata", userData);

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selLabId = Integer.parseInt(request.getParameter("selId"));
				int selLabIndex = labList.indexOf(labDao.findById(selLabId));
				frId = selLabIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				
			
			List<Lab> labPage = labList.subList(frId, toId);
			for (Lab lab : labPage) {
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", lab.getLabId());

				List<LabMeta> labMeta = getMetaHelperWebapp().syncWithMaster(
						lab.getLabMeta());

				List<String> cellList = new ArrayList<String>(
						Arrays.asList(new String[] {
								lab.getName(),
								this.userDao.getUserByUserId(lab.getPrimaryUserId()).getNameFstLst(), //the grid itself is aattaching an anchor to the PI, using selId
								lab.getPrimaryUserId().toString(),
								lab.getDepartment().getName(),

								lab.getIsActive().intValue() == 1 ? "yes" : "no",
								"<a href=" + getServletPath() + "/lab/user_manager/"+lab.getLabId()+".do>Manage</a>"
								}));

				for (LabMeta meta : labMeta) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + labList, e);
		}

	}

	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	public String subgridJSON(@RequestParam("id") Integer labId, ModelMap m, HttpServletResponse response) {

		Map<String, Object> jqgrid = new HashMap<String, Object>();

		Lab labDb = this.labDao.getById(labId);

		List<LabUser> users = labDb.getLabUser();


		int max = users.size();
		String [] mtrx = new String [max];


		String text;
		try {
			int j = 0;
			for (LabUser user : users) {

				text = user.getUserId() == null ? "No Users"
						: "<a href=" + getServletPath() + "/user/list.do?selId="
								+ user.getUserId() + ">"
								+ user.getUser().getFirstName() + " "
								+ user.getUser().getLastName() + "</a>";
				mtrx[j] = text;
				j++;
			}

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			for (j = 0; j < max; j++) {
				Map<String, Object> cell = new HashMap<String, Object>();
				rows.add(cell);
				cell.put("id", j + "");
				List<String> cellList = Arrays.asList(mtrx[j]);
				cell.put("cell", cellList);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + labDb,e);
		}
	}

	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	public String updateDetailJSON(@RequestParam("id") Integer labId,
			Lab labForm, ModelMap m, HttpServletResponse response) {

		List<LabMeta> labMetaList = getMetaHelperWebapp().getFromJsonForm(request, LabMeta.class);

		labForm.setLabMeta(labMetaList);
		
		if (labId == null || labId == 0) {

			labForm.setIsActive(1);

			Lab labDb = this.labDao.save(labForm);

			labId = labDb.getId();
		} else {
			Lab labDb = this.labDao.getById(labId);
			labDb.setName(labForm.getName());
			labDb.setIsActive(labForm.getIsActive());
			labDb.setDepartmentId(labForm.getDepartmentId());
			this.labDao.merge(labDb);
		}
		try {
			try{
				labMetaDao.setMeta(labMetaList, labId);
				response.getWriter().println(messageService.getMessage("lab.updated_success.label"));
				return null;
			} catch (MetadataException e){
				logger.warn(e.getMessage());
				response.getWriter().println(this.messageService.getMessage("lab.updated.error"));
				return null;
			}
		} catch (Throwable e1) {
			throw new IllegalStateException("Cant output message ", e1);
		}
	}

	@RequestMapping(value = "/pending/detail_rw/updateJSON.do", method = RequestMethod.POST)
	public String updatePendingDetailJSON(
			@RequestParam("id") Integer labPendingId, LabPending labPendingForm, ModelMap m, HttpServletResponse response) {

		List<LabPendingMeta> labPendingMetaList = getLabPendingMetaHelperWebapp().getFromJsonForm(request, LabPendingMeta.class);

		labPendingForm.setLabPendingMeta(labPendingMetaList);
		if (labPendingId == null || labPendingId == 0) {

			// labPendingForm.setIsActive(1);

			LabPending labPendingDb = this.labPendingDao.save(labPendingForm);

			labPendingId = labPendingDb.getId();
		} else {
			LabPending labPendingDb = this.labPendingDao.getById(labPendingId);
			labPendingDb.setName(labPendingForm.getName());
			// labPendingDb.setIsActive(labPendingForm.getIsActive());
			labPendingDb.setDepartmentId(labPendingForm.getDepartmentId());
			labPendingDb.setPrimaryUserId(labPendingForm.getPrimaryUserId());

			this.labPendingDao.merge(labPendingDb);
		}
		try{
			try {
				labPendingMetaDao.setMeta(labPendingMetaList,labPendingId);
				response.getWriter().println(messageService.getMessage("labPending.updated_success.label"));
				return null;
			} catch (MetadataException e){
				response.getWriter().println(messageService.getMessage("labPending.updated.error"));
				return null;
			}
		} catch (Throwable e1) {
			throw new IllegalStateException("Cant output success message ", e1);
		}
	}

	@RequestMapping(value = "/detail_rw/{deptId}/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId) or hasRole('lu-' + #labId)")
	public String detailRW(@PathVariable("deptId") Integer deptId, @PathVariable("labId") Integer labId, ModelMap m) {
		return detail(labId, m, true);
	}

	@RequestMapping(value = "/detail_ro/{deptId}/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId) or hasRole('lu-' + #labId)")
	public String detailRO(@PathVariable("deptId") Integer deptId, @PathVariable("labId") Integer labId, ModelMap m) {
		return detail(labId, m, false);
	}

	private String detail(Integer labId, ModelMap m, boolean isRW) {

		Lab lab = this.labDao.getById(labId);

		lab.setLabMeta(getMetaHelperWebapp().syncWithMaster(lab.getLabMeta()));

		List<LabUser> labUserList = lab.getLabUser();
		labUserList.size();

		List<Job> jobList = lab.getJob();
		jobList.size();

		m.addAttribute("lab", lab);

		prepareSelectListData(m);
		if (isRW) {
			m.addAttribute("puserFullName", getPiFullNameFromLabId(labId));
			return "lab/detail_rw";
		} else {
			m.addAttribute("puserFullName", getPiFullNameFromLabId(labId));
			return "lab/detail_ro";
		}
	}

	@RequestMapping(value = "/pending/detail_rw/{deptId}/{labPendingId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId)")
	public String pendingDetailRW(@PathVariable("deptId") Integer deptId,
			@PathVariable("labPendingId") Integer labPendingId, ModelMap m) {
		return pendingDetail(labPendingId, m, true);
	}

	@RequestMapping(value = "/pending/detail_ro/{deptId}/{labPendingId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId)")
	public String pendingDetailRO(@PathVariable("deptId") Integer deptId,
			@PathVariable("labPendingId") Integer labPendingId, ModelMap m) {
		LabPending labPending = this.labPendingDao.getLabPendingByLabPendingId(labPendingId);
		if (labPending.getLabPendingId() == null || labPending.getLabPendingId() == 0) {// labpendingId doesn't exist
			waspErrorMessage("labPending.labpendingid_notexist.error");
			return "redirect:/dashboard.do";
		}
		if (deptId.intValue() != labPending.getDepartmentId().intValue()) {
			waspErrorMessage("labPending.departmentid_mismatch.error");
			return "redirect:/dashboard.do";
		} else if (!labPending.getStatus().equalsIgnoreCase("PENDING")) {
			waspErrorMessage("labPending.status_mismatch.error");
			return "redirect:/dashboard.do";
		} else {
			return pendingDetail(labPendingId, m, false);
		}
	}

	private String getPiFullNameFromLabPendingId(int labPendingId) {
		LabPending labPending = this.labPendingDao.getById(labPendingId);
		String puserFullName = "";
		if (labPending.getUserpendingId() != null) {
			// this PI is currently a pending user.
			UserPending userPending = userPendingDao.getUserPendingByUserPendingId(labPending.getUserpendingId());
			puserFullName = userPending.getFirstName() + " " + userPending.getLastName();
		} else if (labPending.getPrimaryUserId() != null){
			// the referenced PI of this lab exists in the user table already
			User user = userDao.getUserByUserId(labPending.getPrimaryUserId());
			puserFullName = user.getFirstName() + " " + user.getLastName();
		} else {
			// shouldn't get here
		}
		return puserFullName;
	}

	private String getPiFullNameFromLabId(int labId) {
		Lab lab = this.labDao.getById(labId);
		String puserFullName = "";
		User user = userDao.getUserByUserId(lab.getPrimaryUserId());
		puserFullName = user.getFirstName() + " " + user.getLastName();
		return puserFullName;
	}

	private String pendingDetail(Integer labPendingId, ModelMap m, boolean isRW) {

		LabPending labPending = this.labPendingDao.getById(labPendingId);

		labPending.setLabPendingMeta(getLabPendingMetaHelperWebapp().syncWithMaster(
				labPending.getLabPendingMeta()));

		// List<LabUser> labUserList = labPending.getLabUser();
		// labUserList.size();

		// List<Job> jobList = labPending.getJob();
		// jobList.size();
		m.addAttribute("puserFullName",	getPiFullNameFromLabPendingId(labPendingId));
		m.addAttribute("labPending", labPending);

		prepareSelectListData(m);

		return isRW ? "lab/pending/detail_rw" : "lab/pending/detail_ro";
	}

	@RequestMapping(value = "/create/form.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su')")
	public String showEmptyForm(ModelMap m) {

		Lab lab = new Lab();
		lab.setLabMeta(getMetaHelperWebapp().getMasterList(LabMeta.class));

		m.addAttribute("lab", lab);

		prepareSelectListData(m);

		return "lab/detail_rw";
	}

	@RequestMapping(value = "/create/form.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su')")
	public String create(@Valid Lab labForm, BindingResult result,
			SessionStatus status, ModelMap m) {

		// read properties from form
		MetaHelperWebapp metaHelper = getMetaHelperWebapp();
		List<LabMeta> labMetaList = metaHelper.getFromRequest(request,	LabMeta.class);
		metaHelper.validate(result);

		labForm.setLabMeta(labMetaList);

		if (result.hasErrors()) {
			waspErrorMessage("lab.created.error");
			return "lab/detail_rw";
		}

		Lab labDb = this.labDao.save(labForm);
		try {
			labMetaDao.setMeta(labMetaList, labDb.getLabId());
			status.setComplete();
			waspMessage("lab.created_success.label");
		} catch (MetadataException e) {
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("lab.created.error");
		}

		return "redirect:/lab/detail_rw/" + labDb.getLabId() + ".do";
	}

	@RequestMapping(value = "/detail_rw/{deptId}/{labId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('da-' + #deptId) or hasRole('lm-' + #labId)")
	public String updateDetail(@PathVariable("deptId") Integer deptId,
			@PathVariable("labId") Integer labId, @Valid Lab labForm,
			BindingResult result, SessionStatus status, ModelMap m) {
		
		// return read only version of page if cancel button pressed
		String submitValue = request.getParameter("submit");
		if ( submitValue.equals(messageService.getMessage("labDetail.cancel.label")) ){
			return "redirect:/lab/detail_ro/" + deptId + "/" + labId + ".do";
		}
		MetaHelperWebapp metaHelper = getMetaHelperWebapp();
		List<LabMeta> labMetaList = metaHelper.getFromRequest(request,	LabMeta.class);
		labForm.setLabMeta(labMetaList);

		metaHelper.validate(result);

		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspErrorMessage("lab.updated.error");
			return "lab/detail_rw";
		}

		Lab labDb = this.labDao.getById(labId);
		labDb.setName(labForm.getName());
		labDb.setDepartmentId(labForm.getDepartmentId());
		labDb.setPrimaryUserId(labForm.getPrimaryUserId());

		this.labDao.merge(labDb);

		try {
			labMetaDao.setMeta(labMetaList, labId);
			status.setComplete();
			waspMessage("lab.updated_success.label");
		} catch (MetadataException e) {
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("lab.updated.error");
		}

		// return "redirect:" + labId + ".do";
		return "redirect:/lab/detail_ro/"
				+ Integer.toString(labDb.getDepartmentId()) + "/" + labId
				+ ".do";
	}

	@RequestMapping(value = "/pending/detail_rw/{deptId}/{labPendingId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('da-' + #deptId) or hasRole('lm-' + #labPendingId)")
	public String updatePendingDetail(@PathVariable("deptId") Integer deptId,
			@PathVariable("labPendingId") Integer labPendingId,
			@Valid LabPending labPendingForm, BindingResult result,
			SessionStatus status, ModelMap m) {
		
		// return read only version of page if cancel button pressed
		String submitValue = request.getParameter("submit");
		if ( submitValue.equals(messageService.getMessage("labPending.cancel.label")) ){
			return "redirect:/lab/pending/detail_ro/" + deptId + "/" + labPendingId + ".do";
		}
		MetaHelperWebapp metaHelper = getMetaHelperWebapp();
		List<LabPendingMeta> labPendingMetaList = metaHelper.getFromRequest(request, LabPendingMeta.class);

		labPendingForm.setLabPendingMeta(labPendingMetaList);

		metaHelper.validate(result);

		if (result.hasErrors()) {
			waspErrorMessage("labPending.updated.error");
			prepareSelectListData(m);
			m.addAttribute("puserFullName",	getPiFullNameFromLabPendingId(labPendingId));
			return "lab/pending/detail_rw";
		}

		LabPending labPendingDb = this.labPendingDao.getById(labPendingId);
		labPendingDb.setName(labPendingForm.getName());
		labPendingDb.setDepartmentId(labPendingForm.getDepartmentId());
		labPendingDb.setPrimaryUserId(labPendingForm.getPrimaryUserId());

		this.labPendingDao.merge(labPendingDb);

		try {
			labPendingMetaDao.setMeta(labPendingMetaList, labPendingId);
			status.setComplete();
			waspMessage("labPending.updated_success.label");
		} catch (MetadataException e) {
			waspErrorMessage("labPending.updated.error");
			logger.warn(e.getLocalizedMessage());
		}

		// return "redirect:" + labId + ".do";
		return "redirect:/lab/pending/detail_ro/"
				+ Integer.toString(labPendingDb.getDepartmentId()) + "/"
				+ labPendingId + ".do";
	}

	@RequestMapping(value = "/user_manager/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('da-*') or hasRole('lu-' + #labId)")
	public String userManager(@PathVariable("labId") Integer labId, ModelMap m) {
		Lab lab = this.labDao.getById(labId);
		if(lab==null || lab.getId()==null || lab.getId().intValue()<=0){
			waspErrorMessage("labuser.labNotFound.error");
			String referer = request.getHeader("Referer");
			return "redirect:"+ referer;
		}
		LabUser labUserPI = null;
		List<LabUser> labManagerList = new ArrayList<LabUser>();
		List<LabUser> labUserList = new ArrayList<LabUser>();
		List<LabUser> labUserInactiveList = new ArrayList<LabUser>();
		//List<LabUser> labUserPendingList = new ArrayList<LabUser>();
		List<LabUser> labUserFinalList = new ArrayList<LabUser>();
		
		for (LabUser lu: lab.getLabUser()){
			
			if(lu.getRole().getRoleName().equals("pi")){//active and pi
				labUserPI = lu;
			}
			else if(lu.getRole().getRoleName().equals("lm")){//active and lab manager
				labManagerList.add(lu);
			}
			else if(lu.getRole().getRoleName().equals("lu") ){//active regular lab member
				labUserList.add(lu);
			}
			else if(lu.getRole().getRoleName().equals("lx")){//inactive
				labUserInactiveList.add(lu);
			}
			//else if(lu.getRole().getRoleName().equals("lp")){//pending lab member
			//	labUserPendingList.add(lu);
			//}
		}
		class LabUser_UserNameComparator implements Comparator<LabUser> {
		    @Override
		    public int compare(LabUser arg0, LabUser arg1) {
		        return arg0.getUser().getLastName().concat(arg0.getUser().getFirstName()).compareToIgnoreCase(arg1.getUser().getLastName().concat(arg1.getUser().getFirstName()));
		    }
		}
		Collections.sort(labManagerList, new LabUser_UserNameComparator());//sort by labUser's lastname,firstname
		Collections.sort(labUserList, new LabUser_UserNameComparator());//sort by labUser's lastname,firstname
		Collections.sort(labUserInactiveList, new LabUser_UserNameComparator());//sort by labUser's lastname,firstname
		//NOT USED; instead used existingUsersPendingLmApprovalList below;     Collections.sort(labUserPendingList, new LabUser_UserNameComparator());//sort by labUser's lastname,firstname

		labUserFinalList.add(labUserPI);
		labUserFinalList.addAll(labManagerList);
		labUserFinalList.addAll(labUserList);
		labUserFinalList.addAll(labUserInactiveList);
		m.addAttribute("labUserFinalList", labUserFinalList);
		m.addAttribute("labId", lab.getLabId().intValue());
		m.addAttribute("piName", labUserPI.getUser().getNameFstLst());
		
		// 02/11/13 add userPending - stolen from task/piapprove
		List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
		List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
		List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();//won't be used but needed for the next call
		taskService.getLabManagerPendingTasks(newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);
		//IF the web user is a superuser, facility manager, facility tech, or DA, 
		//existingUsersPendingLmApprovalList and newUsersPendingLmApprovalList and jobsPendingLmApprovalList
		//contain ALL users needing approval and ALL jobs needing approval, not just those for this lab.
		//SO, go through these lists and make sure labId matches
		List<UserPending> removeListnewUsersPendingLmApproval = new ArrayList<UserPending>();
		List<LabUser> removeListexistingUsersPendingLmApproval  = new ArrayList<LabUser>();
		for(UserPending up : newUsersPendingLmApprovalList){
			if(up.getLabId() != labId){
				removeListnewUsersPendingLmApproval.add(up);
			}
		}
		for(LabUser lu : existingUsersPendingLmApprovalList){
			if(lu.getLabId() != labId){
				removeListexistingUsersPendingLmApproval.add(lu);
			}
		}
		newUsersPendingLmApprovalList.removeAll(removeListnewUsersPendingLmApproval);
		existingUsersPendingLmApprovalList.removeAll(removeListexistingUsersPendingLmApproval);
		m.addAttribute("newuserspendinglist", newUsersPendingLmApprovalList); 
		m.addAttribute("existinguserspendinglist", existingUsersPendingLmApprovalList); 
		//m.addAttribute("jobspendinglist", jobsPendingLmApprovalList);//not needed here 

		return "lab/user_manager";
	}
	
	@RequestMapping(value = "/user_manager/update.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('da-*') or hasRole('lu-' + #labId)")
	public String userManagerUpdate(
			@RequestParam("labId") Integer labId,
		    @RequestParam("userId") Integer userId,
		    @RequestParam("newRole") String newRole, 
		    ModelMap m) {

		String referer = request.getHeader("Referer");
		
		//CHECK VALID LAB, USER, and LabUser
		Lab lab = labDao.findById(labId.intValue());
		User user = userDao.getUserByUserId(userId.intValue());
		if(lab==null || lab.getLabId()==null || lab.getLabId().intValue()<=0){
			waspErrorMessage("labUser.userManager_labNotFound.error");
			return "redirect:"+ referer;
		}
		else if(user==null || user.getUserId()==null || user.getUserId().intValue()<=0){
			waspErrorMessage("labUser.userManager_userNotFound.error");
			return "redirect:"+ referer;
		}
		LabUser labUser = labUserDao.getLabUserByLabIdUserId(labId, userId);
		if(labUser==null || labUser.getLabUserId()==null || labUser.getLabUserId().intValue()<=0){
			waspErrorMessage("labUser.userManager_userNotInLab.error");
			return "redirect:"+ referer;
		}

		//CHECK VALID ROLE NAME
		Role role = roleDao.getRoleByRoleName(newRole);
		if(role==null || role.getRoleId()==null || role.getRoleId().intValue()<=0){
			waspErrorMessage("labUser.userManager_roleNotFound.error");
			return "redirect:"+ referer;
		}

		labUser.setRoleId(role.getRoleId());
		LabUser labUserDB = labUserDao.merge(labUser);
		if(labUserDB==null || labUserDB.getLabUserId()==null){
			waspErrorMessage("labUser.userManager_updateUnexpectedlyFailed.error");
			return "redirect:"+ referer;
		}
		waspMessage("labUser.userManager_labRoleSuccessfullyUpdated.label");

		// if i am the user, reauth
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() == userId.intValue()) {
			doReauth();
		}
			
		return "redirect:"+ referer;
	}
	
	@RequestMapping(value = "/user_list/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('lu-' + #labId)")
	public String userList(@PathVariable("labId") Integer labId, ModelMap m) {
		Lab lab = this.labDao.getById(labId);
		List<User> labManagers = new ArrayList<User>();
		List<User> labMembers= new ArrayList<User>();
		User pi = new User();
		for(LabUser labUser : lab.getLabUser() ){
			User currentUser = userDao.getUserByUserId(labUser.getUserId());
			if (currentUser.getUserId().intValue() == lab.getPrimaryUserId().intValue()){
				pi = currentUser;
			} else if (labUser.getRole().getRoleName().equals("lm")){
				labManagers.add(currentUser);
			} else {
				labMembers.add(currentUser);
			}
		}
		m.addAttribute("lab", lab);
		m.addAttribute("pi", pi);
		m.addAttribute("labManagers", labManagers);
		m.addAttribute("labMembers", labMembers);
		return "lab/user_list";
	}

	@RequestMapping(value = "/pendinguser/list/{labId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('lu-' + #labId)")
	public String pendingUserList(@PathVariable("labId") Integer labId,	ModelMap m) {
		Lab lab = this.labDao.getById(labId);

		Map<String, Object> userPendingQueryMap = new HashMap<String, Object>();
		userPendingQueryMap.put("labId", labId);
		userPendingQueryMap.put("status", "PENDING");
		List<UserPending> userPending = userPendingDao.findByMap(userPendingQueryMap);
		
		Map<String, Integer> labUserPendingQueryMap = new HashMap<String, Integer>();
		labUserPendingQueryMap.put("labId", labId);
		labUserPendingQueryMap.put("roleId", roleDao.getRoleByRoleName("lp").getRoleId());
		List<LabUser> labUserPending = labUserDao.findByMap(labUserPendingQueryMap);

		m.addAttribute("lab", lab);
		m.addAttribute("userpending", userPending);
		m.addAttribute("labuserpending", labUserPending);

		return "lab/pendinguser/list";
	}

	@RequestMapping(value = "/user/role/{labId}/{UserId}/{roleName}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('da-*') or hasRole('lm-' + #labId)")
	public String userDetail(@PathVariable("labId") Integer labId,
			@PathVariable("UserId") Integer userId,
			@PathVariable("roleName") String roleName, ModelMap m) {

		// TODO CHECK VALID LABUSER
		LabUser labUser = labUserDao.getLabUserByLabIdUserId(labId, userId);

		if (roleName.equals("xx")) {
			// TODO CONFIRM ROLE WAS "LP"
			labUserDao.remove(labUser);

			waspErrorMessage("hello.error");
			return "redirect:/lab/user_manager/" + labId + ".do";
		}

		// TODO CHECK VALID ROLE NAME
		Role role = roleDao.getRoleByRoleName(roleName);

		// TODO CHECK VALID ROLE FLOW

		labUser.setRoleId(role.getRoleId());
		labUserDao.merge(labUser);

		// TODO ADD MESSAGE

		// if i am the user, reauth
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() == userId.intValue()) {
			doReauth();
		}

		waspMessage("labuser.role_change_request.label");
		return "redirect:/lab/user_manager/" + labId + ".do";
	}

	/**
	 * Creates a new Lab from a LabPending object. If principal investigator is
	 * pending, her {@link User} account is generated first.
	 * 
	 * @param labPending
	 * @return {@link Lab} the created lab
	 * @throws MetadataException
	 */
	@SuppressWarnings("unchecked")
	public Lab createLabFromLabPending(LabPending labPending) throws MetadataException {
		Lab lab = new Lab();
		User user;

		if (labPending.getUserpendingId() != null) {
			// this PI is currently a pending user. Make them a full user before
			// creating lab
			UserPending userPending = userPendingDao.getUserPendingByUserPendingId(labPending.getUserpendingId());
			user = createUserFromUserPending(userPending);
		} else if (labPending.getPrimaryUserId() != null) {
			// the referenced PI of this lab exists in the user table already so
			// get their record
			user = userDao.getUserByUserId(labPending.getPrimaryUserId());
		} else {
			// shouldn't get here
			return lab; // return empty lab
		}

		lab.setPrimaryUserId(user.getUserId());
		lab.setName(labPending.getName());
		lab.setDepartmentId(labPending.getDepartmentId());
		lab.setIsActive(1);

		Lab labDb = labDao.save(lab);

		// copies meta data from labPendingMeta to labMeta.
		MetaHelperWebapp labMetaHelperWebapp = new MetaHelperWebapp(LabMeta.class,	request.getSession());
		labMetaHelperWebapp.getMasterList(LabMeta.class);
		MetaHelperWebapp labPendingMetaHelperWebapp = new MetaHelperWebapp(LabPendingMeta.class, request.getSession());
		List<LabPendingMeta> labPendingMetaList = labPendingMetaHelperWebapp.syncWithMaster(labPending.getLabPendingMeta());

		for (LabPendingMeta lpm : labPendingMetaList) {
			// get name from prefix by removing area
			String name = lpm.getK().replaceAll("^.*?\\.", "");
			try {
				labMetaHelperWebapp.setMetaValueByName(name, lpm.getV());
			} catch (MetadataException e) {
				// no match for 'name' in labMeta
				logger.warn("No match for labPendingMeta property with name '"	+ name + "' in labMeta properties");
			}
		}
		labMetaDao.setMeta((List<LabMeta>) labMetaHelperWebapp.getMetaList(), labDb.getLabId());
		// set pi role
		Role role = roleDao.getRoleByRoleName("pi");

		LabUser labUser = new LabUser();
		labUser.setUserId(user.getUserId());
		labUser.setLabId(lab.getLabId());
		labUser.setRoleId(role.getRoleId());
		labUserDao.save(labUser);

		// set status to 'CREATED' for any other pending labs of the same name
		// (user may have attempted to apply for their
		// lab account more than once)
		Map<String, Object> pendingLabQueryMap = new HashMap<String, Object>();
		pendingLabQueryMap.put("primaryUserId", user.getUserId());
		pendingLabQueryMap.put("name", labDb.getName());
		for (LabPending lp : labPendingDao.findByMap(pendingLabQueryMap)) {
			lp.setStatus("CREATED");
			labPendingDao.save(lp);
		}

		// if i am the p.i. reauth
		User me = authenticationService.getAuthenticatedUser();

		if (me.getUserId().intValue() == user.getUserId().intValue()) {
			doReauth();
		}

		return labDb;
	}

	/**
	 * Creates and returns a new {@link User} object from the supplied
	 * {@link UserPending} object.
	 * 
	 * @param userPending
	 *            the pending user
	 * @return {@link User} the created user
	 * @throws MetadataException
	 */
	@SuppressWarnings("unchecked")
	public User createUserFromUserPending(UserPending userPending) throws MetadataException {
		boolean isPiPending = (userPending.getLabId() == null) ? true : false;
		User user = new User();
		user.setFirstName(userPending.getFirstName());
		user.setLastName(userPending.getLastName());
		user.setEmail(userPending.getEmail());
		user.setPassword(userPending.getPassword());
		user.setLocale(userPending.getLocale());
		user.setIsActive(1);
		user.setLogin(userPending.getLogin());
		User userDb = userDao.save(user);
		int userId = userDb.getUserId();

		/*
		 * List<UserPendingMeta> userPendingMetaList =
		 * userPendingMetaDao.getUserPendingMetaByUserPendingId
		 * (userPending.getUserPendingId()); copies meta data
		 */
		MetaHelperWebapp userMetaHelperWebapp = new MetaHelperWebapp(UserMeta.class, request.getSession());
		userMetaHelperWebapp.getMasterList(UserMeta.class);
		MetaHelperWebapp userPendingMetaHelperWebapp = new MetaHelperWebapp(UserPendingMeta.class, request.getSession());
		if (isPiPending)
			userPendingMetaHelperWebapp.setArea("piPending");
		List<UserPendingMeta> userPendingMetaList = userPendingMetaHelperWebapp.syncWithMaster(userPending.getUserPendingMeta());

		for (UserPendingMeta upm : userPendingMetaList) {
			// convert prefix
			String name = upm.getK().replaceAll("^.*?\\.", "");
			try {
				userMetaHelperWebapp.setMetaValueByName(name, upm.getV());
			} catch (MetadataException e) {
				// no match for 'name' in userMeta data
				logger.warn("No match for userPendingMeta property with name '" + name + "' in userMeta properties");
			}
		}
		// if this user is not a PI, copy address information from the PI's User
		// data.
		if (!isPiPending) {
			/*
			 * not a PI application request create a metahelper object to work
			 * with metadata for PI.
			 */
			String piUserLogin = userPendingMetaHelperWebapp.getMetaByName("primaryuserid").getV();
			MetaHelperWebapp piMetaHelperWebapp = new MetaHelperWebapp(UserMeta.class, request.getSession());
			piMetaHelperWebapp.syncWithMaster(userDao.getUserByLogin(piUserLogin).getUserMeta()); // get PI meta from database and sync with
										// current properties
			try {
				userMetaHelperWebapp.setMetaValueByName("institution", piMetaHelperWebapp.getMetaByName("institution").getV());
				userMetaHelperWebapp.setMetaValueByName("departmentId", piMetaHelperWebapp.getMetaByName("departmentId").getV());
				userMetaHelperWebapp.setMetaValueByName("state", piMetaHelperWebapp.getMetaByName("state").getV());
				userMetaHelperWebapp.setMetaValueByName("city", piMetaHelperWebapp.getMetaByName("city").getV());
				userMetaHelperWebapp.setMetaValueByName("country", piMetaHelperWebapp.getMetaByName("country").getV());
				userMetaHelperWebapp.setMetaValueByName("zip", piMetaHelperWebapp.getMetaByName("zip").getV());
			} catch (MetadataException e) {
				// should never get here because of sync
				throw new MetadataException("Metadata user / pi meta name mismatch", e);
			}
		}
		userMetaDao.setMeta((List<UserMeta>) userMetaHelperWebapp.getMetaList(), userId);

		// userDb doesn't have associated metadata so add it
		userDb.setUserMeta((List<UserMeta>) userMetaHelperWebapp.getMetaList());

		/*
		 * Set status of any other applications from user with the same email
		 * address to 'CREATED' If the new user is also pending in other labs
		 * but not yet confirmed by the PI of each of those labs, add the user
		 * to that lab (via entry into the labUser table) and set their status
		 * as 'lp' (lab-pending).
		 */
		Map<String, String> userPendingQueryMap = new HashMap<String, String>();
		userPendingQueryMap.put("email", userPending.getEmail());
		userPendingQueryMap.put("status", "PENDING");
		List<UserPending> userPendingList = userPendingDao.findByMap(userPendingQueryMap);
		userPendingQueryMap.put("status", "WAIT_EMAIL");
		userPendingList.addAll(userPendingDao.findByMap(userPendingQueryMap));
		Role roleLabPending = roleDao.getRoleByRoleName("lp");

		for (UserPending userPendingCurrent : userPendingList) {
			userPendingCurrent.setStatus("CREATED");
			userPendingDao.save(userPendingCurrent);

			if (userPendingCurrent.getLabId() != null) {
				// not a PI application request
				LabUser labUserCurrent = labUserDao.getLabUserByLabIdUserId(userPendingCurrent.getLabId(),	userDb.getUserId());
				//NV 12132011
				//if (labUserCurrent.getLabUserId() > 0) {
				if (labUserCurrent.getLabUserId() != null) {
					// already registered as a user of the requested lab
					continue;
				}
				// add user to requested lab with lab-pending role
				labUserCurrent.setUserId(userId);
				labUserCurrent.setLabId(userPendingCurrent.getLabId());
				labUserCurrent.setRoleId(roleLabPending.getRoleId());
				labUserDao.save(labUserCurrent);

			}
			/*
			 * iterate through list of pending labs. If this user was previously
			 * registered as 'userPending' in a lab, remove reference to her
			 * userPendingId and insert reference to her new UserId instead
			 */
			Map<String, Integer> labPendingQueryMap = new HashMap<String, Integer>();
			labPendingQueryMap.put("userPendingId",	userPendingCurrent.getUserPendingId());

			List<LabPending> labPendingList = labPendingDao.findByMap(labPendingQueryMap);
			for (LabPending labPending : labPendingList) {
				labPending.setUserpendingId((Integer) null);
				labPending.setPrimaryUserId(userId);
				labPendingDao.save(labPending);
			}
		}

		return userDb;
	}

	/**
	 * Request-mapped function that allows a principal investigator or lab
	 * manager to accept or reject an application from a pending lab user (already
	 * an active WASP user) to join their lab.
	 * @param labId
	 * @param labUserId
	 * @param action
	 * @param m
	 * @return
	 */
	@RequestMapping(value = "/labuserpending/{action}/{labId}/{labUserId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('lm-' + #labId) or hasRole('fm-*') or hasRole('ft-*')")
	public String labUserPendingDetail(@PathVariable("labId") Integer labId,
									   @PathVariable("labUserId") Integer labUserId,
									   @PathVariable("action") String action, ModelMap m){
		if (!(action.equals("approve") || action.equals("reject"))) {
			waspErrorMessage("userPending.action.error");
			return "redirect:/dashboard.do";
		}
		LabUser labUserPending = labUserDao.getLabUserByLabUserId(labUserId);
		if (labUserPending.getLabId().intValue() != labId.intValue()){
			waspErrorMessage("labuser.labUserNotFoundInLab.error");
			return "redirect:/dashboard.do";
		}
		if (labUserPending.getRoleId().intValue() != roleDao.getRoleByRoleName("lp").getRoleId().intValue() ) {
			waspErrorMessage("userPending.status_not_pending.error");
			return "redirect:/dashboard.do";
		}

		if ("approve".equals(action)) {
			// add user to lab (labUser table) with role 'lu' (lab-user) and
			// email pending user notification of acceptance
			Role roleLabUser = roleDao.getRoleByRoleName("lu");
			// createUserFromUserPending, should have made this.
			labUserPending.setRoleId(roleLabUser.getRoleId());
			labUserDao.save(labUserPending);
			emailService.sendPendingLabUserNotifyAccepted(labUserPending.getUser(), labUserPending.getLab());
			waspMessage("userPending.approved.label");
		} else {
			labUserDao.remove(labUserPending);
			// email pending user notification of rejection
			emailService.sendPendingLabUserNotifyRejected(labUserPending.getUser(),	labUserPending.getLab());
			waspMessage("userPending.rejected.label");
		}

		String referer = request.getHeader("Referer");
		return "redirect:"+ referer;
	}
	
	/**
	 * Request-mapped function that allows a principal investigator or lab
	 * manager to accept or reject an application from a pending user (not yet
	 * an active WASP user) to join their lab.
	 * 
	 * @param labId
	 * @param userPendingId
	 * @param action
	 * @param m
	 * @return view
	 * @throws MetadataException
	 */
	@RequestMapping(value = "/userpending/{action}/{labId}/{userPendingId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('lm-' + #labId) or hasRole('fm-*') or hasRole('ft-*')")
	public String userPendingDetail(@PathVariable("labId") Integer labId,
			@PathVariable("userPendingId") Integer userPendingId,
			@PathVariable("action") String action, ModelMap m)
			throws MetadataException {

		if (!(action.equals("approve") || action.equals("reject"))) {
			waspErrorMessage("userPending.action.error");
			return "redirect:/dashboard.do";
		}

		UserPending userPending = userPendingDao.getUserPendingByUserPendingId(userPendingId);
		if (! userPending.getStatus().equals("PENDING") ) {
			waspErrorMessage("userPending.status_not_pending.error");
			return "redirect:/dashboard.do";
		}
		
		if (userPending.getLabId().intValue() != labId.intValue()) {
			waspErrorMessage("userPending.labid_mismatch.error");
			return "redirect:/dashboard.do";
		}
		User user;

		if ("approve".equals(action)) {
			// add user to lab (labUser table) with role 'lu' (lab-user) and
			// email pending user notification of acceptance
			user = createUserFromUserPending(userPending);
			Role roleLabUser = roleDao.getRoleByRoleName("lu");
			// createUserFromUserPending, should have made this.
			LabUser labUser = labUserDao.getLabUserByLabIdUserId(labId,	user.getUserId());
			labUser.setRoleId(roleLabUser.getRoleId());
			labUserDao.merge(labUser);
			emailService.sendPendingUserNotifyAccepted(user, labDao.getLabByLabId(labId));
			waspMessage("userPending.approved.label");
		} else {
			// email pending user notification of rejection
			emailService.sendPendingUserNotifyRejected(userPending,	labDao.getLabByLabId(labId));
			waspMessage("userPending.rejected.label");
		}
		userPending.setStatus(action);
		userPendingDao.save(userPending);
		String referer = request.getHeader("Referer");
		return "redirect:"+ referer;
	}

	/**NO LONGER USED
	 * Request-mapped function that allows a department administrator to accept
	 * or reject an application for a new lab (new PI) within their department.
	 * 
	 * @param departmentId
	 * @param labPendingId
	 * @param action
	 * @param m
	 * @return
	 * @throws MetadataException
	 */
/*  the code below was replaced 3/6/13 with
 * @RequestMapping(value = "/pending/{deptId}/{labPendingId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('da-' + #deptId) or hasRole('ga-*') or hasRole('fm') or hasRole('ft') ")
	public String labPendingUpdate(@PathVariable("deptId") Integer deptId,
	
	
	@RequestMapping(value = "/pending/{action}/{deptId}/{labPendingId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('da-' + #deptId) or hasRole('ga-*') or hasRole('fm') or hasRole('ft') ")
	public String labPendingDetail(@PathVariable("deptId") Integer deptId,
			@PathVariable("labPendingId") Integer labPendingId,
			@PathVariable("action") String action, ModelMap m)
			throws MetadataException {

		String referer = request.getHeader("Referer");
		
		if (!(action.equals("approve") || action.equals("reject"))) {
			waspErrorMessage("labPending.action.error");
			return "redirect:"+ referer;
		}
		LabPending labPending = labPendingDao.getLabPendingByLabPendingId(labPendingId);
		if (labPending.getLabPendingId()==null || labPending.getLabPendingId().intValue()==0 || ! labPending.getStatus().equals("PENDING") ) {
			waspErrorMessage("labPending.status_not_pending.error");
			return "redirect:"+ referer;
		}
		
		if (labPending.getDepartmentId().intValue() != deptId.intValue()) {
			waspErrorMessage("labPending.departmentid_mismatch.error");
			return "redirect:"+ referer;
		}

		if ("approve".equals(action)) {
			Lab lab = createLabFromLabPending(labPending);
			if (lab.getLabId() == null || lab.getLabId().intValue() == 0){
				waspErrorMessage("labPending.could_not_create_lab.error");
				return "redirect:"+ referer;
			}
			emailService.sendPendingLabNotifyAccepted(lab);
			waspMessage("labPending.approved.label");
		} else {
			if (labPending.getUserpendingId() != null) {
				// this PI is currently a pending user. Reject their pending
				// user application too
				UserPending userPending = userPendingDao.getUserPendingByUserPendingId(labPending.getUserpendingId());
				userPending.setStatus(action);
				userPendingDao.save(userPending);
				waspMessage("labPending.rejected.label");
			} else if (labPending.getPrimaryUserId() == null){ //if labPending.userpendingid AND labPending.primaryuserid are both null - should never get here
				waspErrorMessage("labPending.could_not_create_lab.error");
				return "redirect:"+ referer;
			}
			emailService.sendPendingLabNotifyRejected(labPending);
		}
		labPending.setStatus(action);
		labPendingDao.save(labPending);
		//return "redirect:/department/detail/" + deptId + ".do";
		//return "redirect:/department/dapendingtasklist.do";
		
		return "redirect:"+ referer;
	}
*/
	/**
	 * Request to upgrade status of a regular existing user to PI by GET
	 * (NOT AVAILABLE TO ANYONE THAT IS ALREADY A PI; Any person can only be the PI OF ONE LAB IN WASP - CONFIRMED WITH ANDY, 1/2013)
	 * Pre-populates as much of the form as possible given current user information	
	 * @param ModelMap m
	 * @return String view
	 * @throws MetadataException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/upgradeStatusToPI", method = RequestMethod.GET)
	public String requestUpgradeStatusToPI(ModelMap m) throws MetadataException  {
		//this is for an existing user!!
		MetaHelperWebapp labPendingMetaHelperWebapp = new MetaHelperWebapp(LabPendingMeta.class, request.getSession());
		labPendingMetaHelperWebapp.getMasterList(LabPendingMeta.class);
		MetaHelperWebapp userMetaHelperWebapp = new MetaHelperWebapp(UserMeta.class, request.getSession());

		// Pre-populate some metadata from user's current information
		User me = authenticationService.getAuthenticatedUser();
		userMetaHelperWebapp.syncWithMaster(me.getUserMeta()); // get user meta from database and sync with current properties
		LabPending labPending = new LabPending();
		try {
			String departmentId = userMetaHelperWebapp.getMetaByName("departmentId").getV();
			if (departmentId != null && !departmentId.isEmpty()){
				labPendingMetaHelperWebapp.setMetaValueByName("billing_departmentId",departmentId);
				labPending.setDepartmentId(Integer.valueOf(departmentId));
				String internalExternal = (deptDao.findById( Integer.valueOf(departmentId) ).getIsInternal().intValue() == 1) ? "internal" : "external";
				labPendingMetaHelperWebapp.setMetaValueByName("internal_external_lab", internalExternal);
			}
			labPendingMetaHelperWebapp.setMetaValueByName("billing_institution", userMetaHelperWebapp.getMetaByName("institution").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_state", userMetaHelperWebapp.getMetaByName("state").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_city", userMetaHelperWebapp.getMetaByName("city").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_country", userMetaHelperWebapp.getMetaByName("country").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_zip", userMetaHelperWebapp.getMetaByName("zip").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_contact", me.getFirstName() + " " + me.getLastName());
			
		} catch (MetadataException e) {
			// report meta problem
			logger.warn("Meta data mismatch when pre-populating labMeta data from userMeta (" + e.getMessage() + ")");
		}
		
		labPending.setLabPendingMeta( (List<LabPendingMeta>) labPendingMetaHelperWebapp.getMetaList());
		m.addAttribute("labPending", labPending);
		String userIsPI = authenticationService.hasRole("pi")?new String("true"):new String("false");
		m.addAttribute("userIsPI", userIsPI);
		if("true".equals(userIsPI)){//should never ever occur, but...
			waspErrorMessage("lab.upgradeStatusToPI_userIsAlreadyPI.error");
			return "redirect:/dashboard.do";
		}
		String userIsPIPending = authenticationService.isThisExistingUserPIPending()?new String("true"):new String("false");
		m.addAttribute("userIsPIPending", userIsPIPending);
		if("true".equals(userIsPIPending)){//this existing user is already PIPending, so don't let them apply again to be a pi
			waspErrorMessage("lab.upgradeStatusToPI_userIsAlreadyPIPending.error");
			return "redirect:/dashboard.do";
		}
		
		prepareSelectListData(m);

		//return "lab/newrequest";

		return "lab/upgradeStatusToPI/form";
	}
	
	/**
	 * Request to upgrade status of a regular existing 
	 * 
	 * user to PI by POST.
	 * Validates LabPending form.
	 * @param labPendingForm
	 * @param result
	 * @param status
	 * @param m model
	 * @return view
	 */
	@RequestMapping(value = "/upgradeStatusToPI", method = RequestMethod.POST)
	public String handleRequestUpgradeStatusToPI(@Valid LabPending labPendingForm,	BindingResult result, SessionStatus status, ModelMap m) {
		//this is for an existing user!!
		MetaHelperWebapp pendingMetaHelperWebapp = new MetaHelperWebapp(LabPendingMeta.class, request.getSession());

		List<LabPendingMeta> labPendingMetaList = pendingMetaHelperWebapp.getFromRequest(request, LabPendingMeta.class);
		pendingMetaHelperWebapp.validate(result);

		User me = authenticationService.getAuthenticatedUser();
		
		String userIsPI = authenticationService.hasRole("pi")?new String("true"):new String("false");
		m.addAttribute("userIsPI", userIsPI);
		if("true".equals(userIsPI)){//should never ever occur, but...
			waspErrorMessage("lab.upgradeStatusToPI_userIsAlreadyPI.error");
			return "redirect:/dashboard.do";
		}
		String userIsPIPending = authenticationService.isThisExistingUserPIPending()?new String("true"):new String("false");
		m.addAttribute("userIsPIPending", userIsPIPending);
		if("true".equals(userIsPIPending)){//this existing user is already PIPending, so don't let them apply again to be a pi
			waspErrorMessage("lab.upgradeStatusToPI_userIsAlreadyPIPending.error");
			return "redirect:/dashboard.do";
		}
		
		labPendingForm.setPrimaryUserId(me.getUserId());
		labPendingForm.setStatus("PENDING");

		if (result.hasErrors()) {
			labPendingForm.setLabPendingMeta(labPendingMetaList);
			prepareSelectListData(m);
			waspErrorMessage("lab.upgradeStatusToPI_fixErrorsInForm.error");

			return "lab/upgradeStatusToPI/form";
		}

		LabPending labPendingDb = labPendingDao.save(labPendingForm);

		try {
			labPendingMetaDao.setMeta(labPendingMetaList, labPendingDb.getLabPendingId());
			status.setComplete();
			emailService.sendExistingUserPendingPrincipalConfirmRequest(labPendingDb);
			waspMessage("lab.upgradeStatusToPI_requestSuccess.label");
		} catch (MetadataException e) {
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("lab.upgradeStatusToPI_unexpectedError.error");
		}
		return "redirect:/dashboard.do";
	}
	
	/**
	 * Request get list of webviewer's labs
	 * @param ModelMap m
	 * @return String view
	 */
	@RequestMapping(value = "/viewerLabList", method = RequestMethod.GET)
	public String getMyLabs(ModelMap m)  {
		User user = authenticationService.getAuthenticatedUser();
		List<LabUser> labUserList = user.getLabUser();
		List<Lab> labList = new ArrayList<Lab>();
		for(LabUser lu : labUserList){
			labList.add(lu.getLab());
		}
		class LabPIComparator implements Comparator<LabUser> {
		    @Override
		    public int compare(LabUser arg0, LabUser arg1) {
		        return arg0.getLab().getUser().getLastName().compareToIgnoreCase(arg1.getLab().getUser().getLastName());//sort by PI's last name
		    }
		}
		Collections.sort(labUserList, new LabPIComparator());
		
		
		if(labService.isExistingUserPIPending(user)){//has this existing user applied to become a PI and is still awaiting a response from the department admin?
			m.addAttribute("user", user);
			m.addAttribute("isExistingUserPIPending", true);
		}
		
		m.addAttribute("labUserList", labUserList);
		return "lab/viewerLabList";
	}
	
	
	/**
	 * Request to join a new lab by GET
	 * @param ModelMap m
	 * @return String view
	 */
	@RequestMapping(value = "/joinAnotherLab", method = RequestMethod.GET)
	public String requestAcessToAnotherLab(ModelMap m)  {
		return "lab/joinAnotherLab/form";
	}
	/**
	 * Handles request to join a new lab by POST
	 * @param ModelMap m
	 * @return String view
	 */
	@RequestMapping(value = "/joinAnotherLab", method = RequestMethod.POST)
	public String handleRequestAcessToAnotherLab(@RequestParam("piEmail") String piEmail, ModelMap m)  {
		
		String view = "lab/joinAnotherLab/form";
		
		if (piEmail == null || piEmail.isEmpty() || piEmail.trim().isEmpty()){
			waspErrorMessage("lab.joinAnotherLab_piEmailEmpty.error");
			return view;
		}
		
		User pi = userService.getUserByEmail(piEmail.trim());
		if (pi==null || pi.getUserId() == null || pi.getUserId() <= 0) {
			waspErrorMessage("lab.joinAnotherLab_emailNotFoundInDatabase.error");
			return view;
		}
		
		Lab lab = labService.getLabByPI(pi);
		if (lab.getLabId() == null || lab.getLabId() == null || lab.getLabId() <= 0) {
			waspErrorMessage("lab.joinAnotherLab_emailDoesNotBelongToLabPI.error");
			return view;
		}
		if(lab.getIsActive() != 1){
			waspErrorMessage("lab.joinAnotherLab_labNotActive.error");
			return view;
		}
		
		User me = authenticationService.getAuthenticatedUser();
		Role role = labService.getUserRoleInLab(lab, me);//role is empty if user is not in this lab
		
		if(role != null && role.getRoleId() != null && role.getRoleId().intValue() > 0){
			String roleAsString = role.getRoleName();
			if("lx".equals(roleAsString)){//Lab Member Inactive. Message informs user to request the PI to reactivate your account.
				//Please, DO NOT programatically re-set this role to member pending. The PI or Lab Manager can do this, as can the Facility Manager or Superuser.
				waspMessage("lab.joinAnotherLab_requestToReactivateLabMember.label");				
				return view;
			}
			else if("lp".equals(roleAsString)){//already registered as lab member pending approval
				waspErrorMessage("lab.joinAnotherLab_userIsLabMemberPending.error");
				return view;
			}
			else{//user is already pi, lab manager, or lab member
				waspErrorMessage("lab.joinAnotherLab_userIsLabMember.error");
				return view;
			}
		}
		
		LabUser labUser = labService.addExistingUserToLabAsLabMemberPending(lab, me);
		if(labUser != null && labUser.getLabUserId() != null && labUser.getLabUserId().intValue() > 0){			
			emailService.sendPendingLabUserConfirmRequest(labUser);
			waspMessage("lab.joinAnotherLab_requestSuccess.label");
		}else{waspErrorMessage("lab.joinAnotherLab_unexpectedError.error");}
		
		return view;
		
		/* Ed's old code
		 		// check existence of primaryUser/lab
		if (primaryUserLogin == null || primaryUserLogin.isEmpty()){
			waspErrorMessage("labuser.request_primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}
		
		User primaryUser = userDao.getUserByLogin(primaryUserLogin);
		if (primaryUser.getUserId() == null || primaryUser.getUserId() == 0) {
			waspErrorMessage("labuser.request_primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}

		Lab lab = labDao.getLabByPrimaryUserId(primaryUser.getUserId());
		if (lab.getLabId() == null || lab.getLabId() == 0) {
			waspErrorMessage("labuser.request_primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}

		// check role of lab user
		User me = authenticationService.getAuthenticatedUser();
		LabUser labUser = labUserDao.getLabUserByLabIdUserId(lab.getLabId(), me.getUserId());

		if (labUser.getLabUserId() != null) {
			ArrayList<String> alreadyPendingRoles = new ArrayList<String>();
			alreadyPendingRoles.add("lp");
			if (alreadyPendingRoles.contains(labUser.getRole().getRoleName())) {
				waspErrorMessage("labuser.request_alreadypending.error");
				return "redirect:/lab/newrequest.do";
			}
			
			ArrayList<String> alreadyAccessRoles = new ArrayList<String>();
			alreadyAccessRoles.add("pi");
			alreadyAccessRoles.add("lm");
			alreadyAccessRoles.add("lu");
			if (alreadyAccessRoles.contains(labUser.getRole().getRoleName())) {
				waspErrorMessage("labuser.request_alreadyaccess.error");
				return "redirect:/lab/newrequest.do";
			}
		}

		Role role = roleDao.getRoleByRoleName("lp");

		labUser.setLabId(lab.getLabId());
		labUser.setUserId(me.getUserId());
		labUser.setRoleId(role.getRoleId());
		labUser = labUserDao.save(labUser);

		emailService.sendPendingLabUserConfirmRequest(labUser);

		waspMessage("labuser.request_success.label");

		return "redirect:/dashboard.do";
		 */
	}
	/**
	 * Handles request to create new laboratory by GET. 
	 * Pre-populates as much of the form as possible given current user information.
	 * @param m model
	 * @return view
	 * @throws MetadataException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newrequest", method = RequestMethod.GET)
	public String showRequestForm(ModelMap m) throws MetadataException {
		//no longer used as of 1-30-13, replaced by updateStatusToPI above
		MetaHelperWebapp labPendingMetaHelperWebapp = new MetaHelperWebapp(LabPendingMeta.class, request.getSession());
		labPendingMetaHelperWebapp.getMasterList(LabPendingMeta.class);
		MetaHelperWebapp userMetaHelperWebapp = new MetaHelperWebapp(UserMeta.class, request.getSession());

		// Pre-populate some metadata from user's current information
		User me = authenticationService.getAuthenticatedUser();
		userMetaHelperWebapp.syncWithMaster(me.getUserMeta()); // get user meta from database and sync with current properties
		LabPending labPending = new LabPending();
		try {
			String departmentId = userMetaHelperWebapp.getMetaByName("departmentId").getV();
			if (departmentId != null && !departmentId.isEmpty()){
				labPendingMetaHelperWebapp.setMetaValueByName("billing_departmentId",departmentId);
				labPending.setDepartmentId(Integer.valueOf(departmentId));
				String internalExternal = (deptDao.findById( Integer.valueOf(departmentId) ).getIsInternal().intValue() == 1) ? "internal" : "external";
				labPendingMetaHelperWebapp.setMetaValueByName("internal_external_lab", internalExternal);
			}
			labPendingMetaHelperWebapp.setMetaValueByName("billing_institution", userMetaHelperWebapp.getMetaByName("institution").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_state", userMetaHelperWebapp.getMetaByName("state").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_city", userMetaHelperWebapp.getMetaByName("city").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_country", userMetaHelperWebapp.getMetaByName("country").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_zip", userMetaHelperWebapp.getMetaByName("zip").getV());
			labPendingMetaHelperWebapp.setMetaValueByName("billing_contact", me.getFirstName() + " " + me.getLastName());
			
		} catch (MetadataException e) {
			// report meta problem
			logger.warn("Meta data mismatch when pre-populating labMeta data from userMeta (" + e.getMessage() + ")");
		}
		
		labPending.setLabPendingMeta( (List<LabPendingMeta>) labPendingMetaHelperWebapp.getMetaList());
		m.addAttribute("labPending", labPending);
		String userIsPI = authenticationService.hasRole("pi")?new String("true"):new String("false");
		m.addAttribute("userIsPI", userIsPI);
		
		prepareSelectListData(m);

		return "lab/newrequest";
	}
	
	/**
	 * Handles request to create new laboratory by POST.
	 * Validates LabPending form.
	 * @param labPendingForm
	 * @param result
	 * @param status
	 * @param m model
	 * @return view
	 */
	@RequestMapping(value = "/newrequest", method = RequestMethod.POST)
	public String createNewLabPending(@Valid LabPending labPendingForm,	BindingResult result, SessionStatus status, ModelMap m) {
		//no longer used as of 1-30-13, replaced by updateStatusToPI above
		MetaHelperWebapp pendingMetaHelperWebapp = new MetaHelperWebapp(LabPendingMeta.class, request.getSession());

		List<LabPendingMeta> labPendingMetaList = pendingMetaHelperWebapp.getFromRequest(request, LabPendingMeta.class);
		pendingMetaHelperWebapp.validate(result);

		User me = authenticationService.getAuthenticatedUser();

		labPendingForm.setPrimaryUserId(me.getUserId());
		labPendingForm.setStatus("PENDING");

		if (result.hasErrors()) {
			labPendingForm.setLabPendingMeta(labPendingMetaList);
			prepareSelectListData(m);
			waspErrorMessage("user.created.error");

			return "lab/newrequest";
		}

		LabPending labPendingDb = labPendingDao.save(labPendingForm);

		try {
			labPendingMetaDao.setMeta(labPendingMetaList, labPendingDb.getLabPendingId());
			status.setComplete();
			emailService.sendPendingPrincipalConfirmRequest(labPendingDb);
			waspMessage("labuser.request_success.label");
		} catch (MetadataException e) {
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("user.created.error");
		}

		

		return "redirect:/dashboard.do";
	}
	
	/**
	 * Handles request to join an existing lab by logged in user.
	 * @param primaryUserLogin The login name of the PI of the lab which the user wishes to join
	 * @param m model
	 * @return view
	 */
	@RequestMapping(value = "/request.do", method = RequestMethod.POST)
	public String requestAccess(
			@RequestParam("primaryUserLogin") String primaryUserLogin,
			ModelMap m) {
		
		// check existence of primaryUser/lab
		if (primaryUserLogin == null || primaryUserLogin.isEmpty()){
			waspErrorMessage("labuser.request_primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}
		
		User primaryUser = userDao.getUserByLogin(primaryUserLogin);
		if (primaryUser.getUserId() == null || primaryUser.getUserId() == 0) {
			waspErrorMessage("labuser.request_primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}

		Lab lab = labDao.getLabByPrimaryUserId(primaryUser.getUserId());
		if (lab.getLabId() == null || lab.getLabId() == 0) {
			waspErrorMessage("labuser.request_primaryuser.error");
			return "redirect:/lab/newrequest.do";
		}

		// check role of lab user
		User me = authenticationService.getAuthenticatedUser();
		LabUser labUser = labUserDao.getLabUserByLabIdUserId(lab.getLabId(), me.getUserId());

		if (labUser.getLabUserId() != null) {
			ArrayList<String> alreadyPendingRoles = new ArrayList<String>();
			alreadyPendingRoles.add("lp");
			if (alreadyPendingRoles.contains(labUser.getRole().getRoleName())) {
				waspErrorMessage("labuser.request_alreadypending.error");
				return "redirect:/lab/newrequest.do";
			}
			
			ArrayList<String> alreadyAccessRoles = new ArrayList<String>();
			alreadyAccessRoles.add("pi");
			alreadyAccessRoles.add("lm");
			alreadyAccessRoles.add("lu");
			if (alreadyAccessRoles.contains(labUser.getRole().getRoleName())) {
				waspErrorMessage("labuser.request_alreadyaccess.error");
				return "redirect:/lab/newrequest.do";
			}
		}

		Role role = roleDao.getRoleByRoleName("lp");

		labUser.setLabId(lab.getLabId());
		labUser.setUserId(me.getUserId());
		labUser.setRoleId(role.getRoleId());
		labUser = labUserDao.save(labUser);

		emailService.sendPendingLabUserConfirmRequest(labUser);

		waspMessage("labuser.request_success.label");

		return "redirect:/dashboard.do";
	}

	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		m.addAttribute("pusers", userDao.getActiveUsers());
		//m.addAttribute("departments", deptDao.findAll());
	}

	/**NO LONGER USED
	 * Request-mapped function that allows a department administrator to accept
	 * or reject an application for a new lab (new PI) within their department.
	 * 
	 * @param departmentId
	 * @param labPendingId
	 * @param action
	 * @param m
	 * @return
	 * @throws MetadataException
	 */

	@RequestMapping(value = "/pending/{deptId}/{labPendingId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('da-' + #deptId) or hasRole('ga-*') or hasRole('fm') or hasRole('ft') ")
	public String labPendingUpdate(@PathVariable("deptId") Integer deptId,
			@PathVariable("labPendingId") Integer labPendingId,
			@RequestParam("action") String action, 
			@RequestParam("comment") String comment,
			ModelMap m)
			throws MetadataException {

		String referer = request.getHeader("Referer");
		
		if (!(action.equals("approve") || action.equals("reject"))) {
			waspErrorMessage("labPending.action.error");
			return "redirect:"+ referer;
		}
		if ( action.equals("reject")  && "".equalsIgnoreCase(comment.trim()) ) {
			waspErrorMessage("labPending.rejectedPIMustHaveComment.error");
			return "redirect:"+ referer;
		}
		LabPending labPending = labPendingDao.getLabPendingByLabPendingId(labPendingId);
		if (labPending.getLabPendingId()==null || labPending.getLabPendingId().intValue()==0 || ! labPending.getStatus().equals("PENDING") ) {
			waspErrorMessage("labPending.status_not_pending.error");
			return "redirect:"+ referer;
		}
		
		if (labPending.getDepartmentId().intValue() != deptId.intValue()) {
			waspErrorMessage("labPending.departmentid_mismatch.error");
			return "redirect:"+ referer;
		}

		if ("approve".equals(action)) {
			Lab lab = createLabFromLabPending(labPending);
			if (lab.getLabId() == null || lab.getLabId().intValue() == 0){
				waspErrorMessage("labPending.could_not_create_lab.error");
				return "redirect:"+ referer;
			}
			emailService.sendPendingLabNotifyAccepted(lab);
			waspMessage("labPending.approved.label");
		} else {
			if (labPending.getUserpendingId() != null) {
				// this PI is currently a pending user. Reject their pending
				// user application too
				UserPending userPending = userPendingDao.getUserPendingByUserPendingId(labPending.getUserpendingId());
				userPending.setStatus(action);
				userPendingDao.save(userPending);
				waspMessage("labPending.rejected.label");
			} else if (labPending.getPrimaryUserId() == null){ //if labPending.userpendingid AND labPending.primaryuserid are both null - should never get here
				waspErrorMessage("labPending.could_not_create_lab.error");
				return "redirect:"+ referer;
			}
			emailService.sendPendingLabNotifyRejected(labPending, comment.trim());
		}
		labPending.setStatus(action);
		labPendingDao.save(labPending);
		//return "redirect:/department/detail/" + deptId + ".do";
		//return "redirect:/department/dapendingtasklist.do";
		
		return "redirect:"+ referer;
	}
}
