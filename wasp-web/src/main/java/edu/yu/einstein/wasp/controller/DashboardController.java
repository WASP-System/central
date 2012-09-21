package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
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
		List<Lab> labList = new ArrayList<Lab>();
		int labCount = 0;
		int jobViewableCount = 0;
		int jobsAllCount = 0;
		int jobDraftCount = 0;
		
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
				case lu: labList.add(labDao.getLabByLabId(roleObjectId)); break; 
				case jv: jobViewableCount++; break;
				case jd: jobDraftCount++; break;
			}
		}
		jobsAllCount = jobDao.findAll().size();
		labCount = labList.size();
		m.addAttribute("me", authenticationService.getAuthenticatedUser());
				
		m.addAttribute("labs", labList);
		m.addAttribute("labCount", labCount);
		m.addAttribute("jobViewableCount", jobViewableCount);
		m.addAttribute("jobsAllCount", jobsAllCount);
		m.addAttribute("jobDraftCount", jobDraftCount);	
		
		List<TaskMapping> taskMappings = taskService.getMappedTasksForCurrentUser();
		
		m.addAttribute("tasks",taskMappings);
		
		int numberOfLabManagerPendingTasks = taskService.getLabManagerPendingTasks();//if pi or lm, then number is dependent on labId(s), otherwise all such pi/lm tasks
		m.addAttribute("numberOfLabManagerPendingTasks", numberOfLabManagerPendingTasks);
		int numberOfDepartmentAdminPendingTasks = taskService.getDepartmentAdminPendingTasks();//if da, then number is dependent on the department(s) the da covers, otherwise all such da tasks
		m.addAttribute("numberOfDepartmentAdminPendingTasks", numberOfDepartmentAdminPendingTasks);

		int totalNumberOfTypesOfTasks = taskMappings.size() + (numberOfLabManagerPendingTasks>0?1:0) + (numberOfDepartmentAdminPendingTasks>0?1:0);
		m.addAttribute("totalNumberOfTypesOfTasks", totalNumberOfTypesOfTasks);
		
		return "dashboard";
	}
}
