package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.DashboardController.DashboardEntityRolename;
import edu.yu.einstein.wasp.controller.validator.MetaHelper;
import edu.yu.einstein.wasp.model.Department;
import edu.yu.einstein.wasp.model.DepartmentUser;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabMeta;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.DepartmentUserService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.StringHelper;

@Controller
@Transactional
@RequestMapping("/department")
public class DepartmentController extends WaspController {

  private DepartmentService departmentService;
  
  @Autowired
  public void setDepartmentService(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }
  @Override
public DepartmentService getDepartmentService() {
    return this.departmentService;
  }

  private DepartmentUserService departmentUserService;
  
  @Autowired
  public void setDepartmentUserService(DepartmentUserService departmentUserService) {
    this.departmentUserService = departmentUserService;
  }
  public DepartmentUserService getDepartmentUserService() {
    return this.departmentUserService;
  }

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private LabService labService;
  
  //added by Dubin 9-29-11 for use in getNames
  private UserService userService;
  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }
  public UserService getUserService() {
    return this.userService;
  }
  
  
  
  @Autowired
  private LabPendingService labPendingService;
  
  //private static final MetaAttribute.Area AREA = MetaAttribute.Area.labPending;
 
	@Override
	protected void prepareSelectListData(ModelMap m) {
		Map userQueryMap = new HashMap();
		userQueryMap.put("isActive", 1);
		m.addAttribute("pusers", userService.findByMap(userQueryMap));
		super.prepareSelectListData(m);
	}
  
  @RequestMapping("/list")
  @PreAuthorize("hasRole('god') or hasRole('da-*') or hasRole('ga-*')")
  public String list(ModelMap m) {
	  
	List<Department> departmentList; 
	int departmentAdminPendingTasks = 0;
	
	if(authenticationService.isGod() || authenticationService.hasRole("ga")){  
		departmentList = this.getDepartmentService().findAllOrderBy("name", "ASC");
	}
	else{
		departmentList = new ArrayList<Department>();
		for (String role: authenticationService.getRoles()) {			
			
			String[] splitRole = role.split("-");
			if (splitRole.length != 2) { continue; }
			if (splitRole[1].equals("*")) { continue; }
		
			DashboardEntityRolename entityRolename; 
			int roleObjectId = 0;

			try { 
				entityRolename = DashboardEntityRolename.valueOf(splitRole[0]);
				roleObjectId = Integer.parseInt(splitRole[1]);
			} catch (Exception e)	{
				continue;
			}

			// adds the role object to the proper bucket
			switch (entityRolename) {
				case da: departmentList.add(departmentService.getDepartmentByDepartmentId(roleObjectId)); break;		
			}	
		}
	}
	m.addAttribute("department", departmentList);
	departmentAdminPendingTasks = departmentService.getDepartmentAdminPendingTasks();//number of da pending tasks (if god or ga, then department not considered)	
	m.addAttribute("departmentAdminPendingTasks", departmentAdminPendingTasks);
    return "department/list";
  }
  
	private final MetaHelper getLabMetaHelper() {
		return new MetaHelper("lab", LabMeta.class,
				request.getSession());
	}
	
  @RequestMapping(value="/detail/{departmentId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('ga-*') or hasRole('da-' + #departmentId)")
  public String detail(@PathVariable("departmentId") Integer departmentId, ModelMap m) {

    Department department = this.getDepartmentService().getById(departmentId.intValue());

    List<Lab> labList = department.getLab();
    labList.size();

    List<DepartmentUser> departmentUserList = department.getDepartmentUser();
    departmentUserList.size();

    Map labPendingQueryMap = new HashMap();
    labPendingQueryMap.put("status", "PENDING");
    labPendingQueryMap.put("departmentId", departmentId);

    List<LabPending> labPendingList = labPendingService.findByMap(labPendingQueryMap);

   m.addAttribute("departmentId", departmentId);
    m.addAttribute("departmentName", department.getName());
    m.addAttribute("department", department);
    m.addAttribute("departmentuser", departmentUserList);
    m.addAttribute("lab", labList);
    m.addAttribute("labpending", labPendingList);

	m.addAttribute("_metaList",
			getLabMetaHelper().getMasterList(MetaBase.class));
	m.addAttribute(JQFieldTag.AREA_ATTR, getLabMetaHelper().getArea());

	prepareSelectListData(m);

	// List<Resource> resourceList = resourceService.findAll();
	//
	// m.addAttribute("resource", resourceList);

    return "department/detail";
  }
	
	@RequestMapping(value = "/detail/{departmentId}/listLabJSON", method = RequestMethod.GET)
	@PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
	public String getLabListJSON(HttpServletResponse response, @PathVariable("departmentId") Integer departmentId) {

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		Department department = this.getDepartmentService().getById(departmentId.intValue());
		List<Lab> labList = department.getLab();

		if (request.getParameter("_search") != null
				&& StringUtils.isNotEmpty(request.getParameter("searchString"))) {

			Map<String, String> m = new HashMap<String, String>();

			m.put(request.getParameter("searchField"),
					request.getParameter("searchString"));

			List<Lab> searchLabList = labService.findByMap(m);

			if ("ne".equals(request.getParameter("searchOper"))) {
				labList.removeAll(searchLabList);
			} else {
				labList.retainAll(searchLabList);
			}
		}

		ObjectMapper mapper = new ObjectMapper();

		Map<Integer, String> allDepts = new TreeMap<Integer, String>();
		for (Department dept : (List<Department>) this.getDepartmentService().findAll()) {
			allDepts.put(dept.getDepartmentId(), dept.getName());
		}

		Map<Integer, String> allUsers = new TreeMap<Integer, String>();
		for (User user : (List<User>) userService.findAll()) {
			allUsers.put(user.getUserId(),	user.getFirstName() + " " + user.getLastName());
		}

		/***** Sort lab list by lab name OR PI name *****/
		class LabNameComparator implements Comparator<Lab> {
			public int compare(Lab arg0, Lab arg1) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			}
		}

		class LabPUNameComparator implements Comparator<Lab> {
			public int compare(Lab arg0, Lab arg1) {
				return arg0.getUser().getFirstName().compareToIgnoreCase(arg1.getUser().getFirstName());
			}
		}

		if (!sord.isEmpty() && !sidx.isEmpty()) {
			if (sidx.equals("name")) {
				Collections.sort(labList, new LabNameComparator());
			} else if (sidx.equals("primaryUserId")) {
				Collections.sort(labList, new LabPUNameComparator());
			}

			if (sord.equals("desc"))
				Collections.reverse(labList);
		}
		/***** Sorting ends here *****/
		
		try {
			// String users = mapper.writeValueAsString(userList);
			int pageId = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = labList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("page", pageId + "");
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");

			Map<String, String> labData = new HashMap<String, String>();
			//labData.put("page", "1");
			labData.put("page", pageId + "");
			labData.put("selId",
					StringUtils.isEmpty(request.getParameter("selId")) ? ""
							: request.getParameter("selId"));
			jqgrid.put("labdata", labData);

			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageId - 1);
			int toId = pageRowNum * pageId;
			toId = toId <= rowNum ? toId : rowNum;
			List<Lab> labPage = labList.subList(frId, toId);
			for (Lab lab : labPage) {
				Map cell = new HashMap();
				cell.put("id", lab.getLabId());

				List<LabMeta> labMeta = getLabMetaHelper().syncWithMaster(lab.getLabMeta());

				List<String> cellList = new ArrayList<String>(
						Arrays.asList(new String[] {
						lab.getName(),
						"<a href=/wasp/user/list.do?selId="
							+ lab.getPrimaryUserId() + ">"
							+ allUsers.get(lab.getPrimaryUserId())
							+ "</a>",
						allDepts.get(lab.getDepartmentId()),
						lab.getIsActive().intValue() == 1 ? "yes" : "no" }));

				for (LabMeta meta : labMeta) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "
					+ labList, e);
		}

	}
	
  /**
   * Create a new department and assign a department administrator
   * --Ensure that the new department has not yet been created
   * --Ensure that the new administrator already exists within the system
   * @param departmentName
   * @param adminName
   * @param m
   * @return
   */
  @RequestMapping(value="/create", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god')")
  public String createDepartment(@RequestParam("departmentName") String departmentName, 
		  						@RequestParam("adminName") String adminName, 
		  						ModelMap m) {
	    
	boolean departmentNameIsOK = false;
	boolean adminNameIsOK = false;
	String modifiedDepartmentName = "";
	Department department = new Department();
	User user = new User();
	
	//check that the department name is ok and has not yet been created
    if( "".equals(departmentName.trim()) ){
		waspMessage("department.list_missingparam.error");
	} 
    else if(departmentName.toLowerCase().indexOf("external") != -1){//prevent any department from being named %external%
			waspMessage("department.list_invalid.error");
	}
	else{
		modifiedDepartmentName = StringHelper.removeExtraSpacesAndCapOnlyFirstLetterOfEachWord(departmentName);
		
		Department existingDepartment = this.departmentService.getDepartmentByName(modifiedDepartmentName);//is this name already being used as a department name (which is prohibited) 
		//if(existingDepartment.getDepartmentId() > 0 ){//the id will be 0 if empty department [ie.: department does not already exist]
		if(existingDepartment.getDepartmentId() != null){//if true, then the deparmnet already exists, so do let allow system to create department again
			waspMessage("department.list_department_exists.error");
		}
		else{
			departmentNameIsOK = true;
		}
	}

    //adminName will be in the format: John Greally (JGreally)
	//the login is JGreally
	//so, must parse adminName to get unique login
	String trimmedAdminName = adminName.trim();
	int startIndex = trimmedAdminName.indexOf("(");
	int endIndex = trimmedAdminName.indexOf(")");
	
	if( "".equals(trimmedAdminName) ){
		waspMessage("department.detail_missingparam.error");
	}
	else if(startIndex == -1 || endIndex == -1 || startIndex > endIndex){
		//ToDo: add new message about unable to discern login
		waspMessage("department.detail_formatting.error");
	}
	else{
		String login = trimmedAdminName.substring(startIndex+1, endIndex);
		String trimmedLogin = login.trim();
		if("".equals(trimmedLogin)){
			waspMessage("department.detail_missinglogin.error");
		}
		else{
			//logger.debug("ROB trimmedLogin: " + trimmedLogin);
			user = userService.getUserByLogin(login);
			if(user.getUserId() == null){//user not found in database
				waspMessage("department.detail_usernotfound.error");
			}
			else{//since this is a new department (that we know does NOT exist, it cannot have any department administrators associated with it. so, no need here to check that this person is already associated with this department
				adminNameIsOK = true;
			}
		}	
	}
    
    if(departmentNameIsOK && adminNameIsOK){//if all is OK, create new department and add new administrator
		department.setName(modifiedDepartmentName); 
		department.setIsActive(1);
		department.setIsInternal(1);
		department = departmentService.save(department);
		
		DepartmentUser departmentUser = new DepartmentUser(); 
		departmentUser.setDepartmentId(department.getDepartmentId()); 
		departmentUser.setUserId(user.getUserId()); 
		departmentUserService.save(departmentUser);
		////waspMessage("department.detail_ok.label");
	
		// if i am the user,  reauth
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() == user.getUserId().intValue()) {
			doReauth();
		}
		
		waspMessage("department.list_ok.label");
    }
    
    return "redirect:/department/list.do";
  }

  @RequestMapping(value="/user/roleRemove/{departmentId}/{userId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
  public String departmentUserRoleRemove (
    @PathVariable("departmentId") Integer departmentId, 
    @PathVariable("userId") Integer userId, 
    ModelMap m) {

    DepartmentUser departmentUser = departmentUserService.getDepartmentUserByDepartmentIdUserId(departmentId, userId);

    departmentUserService.remove(departmentUser);

    // if i am the user,  reauth
    User me = authenticationService.getAuthenticatedUser();   
    if (me.getUserId().intValue() == userId.intValue()) {
      doReauth();
      //if a user is NOT god and the user is a da and removes him/herself from being a da, 
      //then they are NOT permitted to navigate back to /department/detail/" + departmentId + ".do";
      //Instead send them back to dashboard
      if(!request.isUserInRole("god")){
    	  return "redirect:/dashboard.do";
      }      
    }

    
    return "redirect:/department/detail/" + departmentId + ".do";
  }

  @RequestMapping(value="/user/roleAdd", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god') or hasRole('da-' + #departmentId)")
  public String departmentUserRoleAdd (
    @RequestParam("departmentId") Integer departmentId,
    @RequestParam("adminName") String adminName,
    ModelMap m) {
	  
	//first, confirm that the departmentId is valid (although it is quite unlikely that this is going to be an invalid ID)
	Department department = departmentService.getDepartmentByDepartmentId(departmentId);
	if(department.getDepartmentId() == null){//id of 0 means this department does not exist in the database; this should not really occur
		waspMessage("department.detail_invalidDept.error");
	}
	else{//next deal with the adminName
		//adminName will be in the format: John Greally (JGreally), and if it's not in this format, then terminate
		//the login is JGreally
		//so, must parse adminName to get unique login
		String trimmedAdminName = adminName.trim();
		int startIndex = trimmedAdminName.indexOf("(");
		int endIndex = trimmedAdminName.indexOf(")");
		
		if( "".equals(trimmedAdminName) ){
			waspMessage("department.detail_missingparam.error");
		}
		else if(startIndex == -1 || endIndex == -1 || startIndex > endIndex){
			waspMessage("department.detail_formatting.error");
		}
		else{
			
			String login = trimmedAdminName.substring(startIndex+1, endIndex);
			String trimmedLogin = login.trim();
			if("".equals(trimmedLogin)){
				waspMessage("department.detail_missinglogin.error");
			}
			else{
				User user = userService.getUserByLogin(login);
				if(user.getUserId() == null){//user not found in database
					waspMessage("department.detail_usernotfound.error");
				}
				else{
					
					DepartmentUser existingDepartmentUser = departmentUserService.getDepartmentUserByDepartmentIdUserId(departmentId, user.getUserId());
					//if(existingDepartmentUser.getDepartmentUserId() > 0 && existingDepartmentUser.getUser().getUserId().intValue() == user.getUserId().intValue()){//this person is already a departmentAdmin for this particular department
					if(existingDepartmentUser.getDepartmentUserId() != null && existingDepartmentUser.getUser().getUserId().intValue() == user.getUserId().intValue()){//this person is already a departmentAdmin for this particular department
						waspMessage("department.detail_adminAlreadyExists.error");
					}
					else{						
						DepartmentUser departmentUser = new DepartmentUser(); 
						departmentUser.setDepartmentId(departmentId); 
						departmentUser.setUserId(user.getUserId()); 
						departmentUserService.save(departmentUser);
						waspMessage("department.detail_ok.label");						
						// if i am the user,  reauth
						User me = authenticationService.getAuthenticatedUser();
						if (me.getUserId().intValue() == user.getUserId().intValue()) {
							doReauth();
						}
					}
				}
			}	
		}
	}
    return "redirect:/department/detail/" + departmentId + ".do";
  }

  @RequestMapping(value="/updateDepartment", method=RequestMethod.POST)
  @PreAuthorize("hasRole('god')")
  public String updateDepartment (
    @RequestParam("departmentId") Integer departmentId,
    @RequestParam("name") String name,
    @RequestParam("isActive") Integer isActive, 
    ModelMap m) {

	  if( "".equals(name) ){
			waspMessage("department.detail_update_missingparam.error");//must add to list
	  }
	  else if(name.toLowerCase().indexOf("external") != -1){//prevent any department from being re-named %external%
			waspMessage("department.list_invalid.error");
	  }
	  else{
		   String modifiedName = StringHelper.removeExtraSpacesAndCapOnlyFirstLetterOfEachWord(name);

		  Department departmentBeingModified = this.departmentService.getDepartmentByDepartmentId(departmentId);
		  //modifiedName can be the (unchanged) name of the department being modified (which is valid)
		  //or it can also be the case that the user
		  //is attempting to set one department name to another department's name (which is invalid)
		  //These can be distinguished by departmentId
		  Department otherDepartment = this.departmentService.getDepartmentByName(modifiedName);
		  if(otherDepartment.getDepartmentId() != null && departmentBeingModified.getDepartmentId().intValue() != otherDepartment.getDepartmentId().intValue()){
			  waspMessage("department.list_department_exists.error");//this name is taken
		  }
		  else{
			departmentBeingModified.setName(modifiedName);
			departmentBeingModified.setIsActive(isActive);
		  	this.departmentService.save(departmentBeingModified);
		  	waspMessage("department.detail_update_ok.label");
		  }
	  }
      return "redirect:/department/detail/" + departmentId + ".do";
  }

  @RequestMapping(value="/dapendingtasklist", method=RequestMethod.GET)
  @PreAuthorize("hasRole('god') or hasRole('da-*') or hasRole('ga-*')")
  public String departmentAdminPendingTaskList (ModelMap m) {
	  
	    List<LabPending> labsPendingDaApprovalList = new ArrayList<LabPending>();
		List<Job> jobsPendingDaApprovalList = new ArrayList<Job>();

		departmentService.getDepartmentAdminPendingTasks(labsPendingDaApprovalList,jobsPendingDaApprovalList);
		//logger.debug("ROB : total number of labs pending: " + labsPendingDaApprovalList.size());
		//logger.debug("ROB : total number of jobs pending: " + jobsPendingDaApprovalList.size());
		//logger.debug("ROB : count of tasks: " + count);
		m.addAttribute("labspendinglist", labsPendingDaApprovalList);
		m.addAttribute("jobspendinglist", jobsPendingDaApprovalList);
		m.addAttribute("sizelabspendinglist", labsPendingDaApprovalList.size());
		m.addAttribute("sizejobspendinglist", jobsPendingDaApprovalList.size());
		m.addAttribute("actingasrole", "da");
		return "department/dapendingtasks";
    
  }
    
}
