package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.yu.einstein.wasp.dao.DepartmentDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabPendingDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.TaskMapping;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.TaskService;

@Controller
@Transactional
public class DashboardController extends WaspController {

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private LabDao labDao;

	@Autowired
	private JobDao jobDao;

	@Autowired
	private JobDraftDao jobDraftDao;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private LabPendingDao labPendingDao;
	
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private TaskService taskService;

	// list of baserolenames (da-department admin, lu- labuser ...)
	// see role table
	// higher level roles such as 'lm' or 'js' are used on the view
	public static enum DashboardEntityRolename {
		da, lu, jv, jd, su, ga
	};

	@RequestMapping("/dashboard")
	public String list(ModelMap m) {
		//List<Department> departmentList = new ArrayList<Department>();
		List<Lab> labList = new ArrayList<Lab>();
		int jobViewableCount = 0;
		int jobsAllCount = 0;
		int jobDraftCount = 0;
		HashMap labMap = new HashMap();
		
		int departmentAdminPendingTasks = 0;
		int allLabManagerPendingTasks = 0;

		
		
		//List<State> states=taskDao.getStatesByTaskMappingRule(taskDao.findById(1), "CREATED");
		
		
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
				////case da: /* departmentList.add(departmentDao.getDepartmentByDepartmentId(roleObjectId)); break; */ 
				case lu: labList.add(labDao.getLabByLabId(roleObjectId)); labMap.put(roleObjectId, taskService.getLabManagerPendingTasks(roleObjectId));break;
				case jv: jobViewableCount++; break;
				case jd: jobDraftCount++; break;
			}
		}
		jobsAllCount = jobDao.findAll().size();
		m.addAttribute("me", authenticationService.getAuthenticatedUser());
		
		//m.addAttribute("departments", departmentList);  //no longer needed
		departmentAdminPendingTasks = taskService.getDepartmentAdminPendingTasks();//number of da pending tasks (if su or ga, then department not considered)	
		m.addAttribute("departmentAdminPendingTasks", departmentAdminPendingTasks);		
		
		m.addAttribute("labs", labList);
		m.addAttribute("labmap", labMap);
		m.addAttribute("jobViewableCount", jobViewableCount);
		m.addAttribute("jobsAllCount", jobsAllCount);
		m.addAttribute("jobDraftCount", jobDraftCount);	
		if(authenticationService.isSuperUser() || authenticationService.hasRole("ga")){
			allLabManagerPendingTasks = taskService.getAllLabManagerPendingTasks();
		}
		m.addAttribute("allLabManagerPendingTasks", allLabManagerPendingTasks);
		
		List<TaskMapping> taskMappings= new ArrayList<TaskMapping>();
	
		List<TaskMapping> taskMappingsAll=taskDao.getTaskMappings();
		for(TaskMapping tm:taskMappingsAll) {
			List<State> states=taskService.filterStatesByStatusAndPermission(tm.getTask().getState(),tm.getStatus(), tm.getPermission());
		
			if (states!=null && !states.isEmpty()) {
				tm.setStateCount(states.size());
				taskMappings.add(tm);
			}
		}
		
		m.addAttribute("tasks",taskMappings);
		
		return "dashboard";
	}
}
