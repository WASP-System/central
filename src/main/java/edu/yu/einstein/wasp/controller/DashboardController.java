package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.DepartmentService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.LabPendingService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.TaskService;

@Controller
@Transactional
public class DashboardController extends WaspController {

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private LabService labService;

	@Autowired
	private JobService jobService;

	@Autowired
	private JobDraftService jobDraftService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private LabPendingService labPendingService;
	
	@Autowired
	private TaskService taskService;

	// list of baserolenames (da-department admin, lu- labuser ...)
	// see role table
	// higher level roles such as 'lm' or 'js' are used on the view
	public static enum DashboardEntityRolename {
		da, lu, jv, jd
	};

	@RequestMapping("/dashboard")
	public String list(ModelMap m) {
		//List<Department> departmentList = new ArrayList<Department>();
		List<Lab> labList = new ArrayList<Lab>();
		List<Job> jobList = new ArrayList<Job>();
		List<JobDraft> jobDraftList = new ArrayList<JobDraft>();
		HashMap labMap = new HashMap();
		
		int departmentAdminPendingTasks = 0;
		int allLabManagerPendingTasks = 0;

		
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
			

			// adds the role ojbect to the proper bucket
			switch (entityRolename) {
				////case da: /* departmentList.add(departmentService.getDepartmentByDepartmentId(roleObjectId)); break; */ 
				case lu: labList.add(labService.getLabByLabId(roleObjectId)); labMap.put(roleObjectId, labService.getLabManagerPendingTasks(roleObjectId));break;
				case jv: jobList.add(jobService.getJobByJobId(roleObjectId)); break;
				case jd: jobDraftList.add(jobDraftService.getJobDraftByJobDraftId(roleObjectId)); break;
			}
		}
		m.addAttribute("me", authenticationService.getAuthenticatedUser());
		
		//m.addAttribute("departments", departmentList);  //no longer needed
		departmentAdminPendingTasks = departmentService.getDepartmentAdminPendingTasks();//number of da pending tasks (if god or ga, then department not considered)	
		m.addAttribute("departmentAdminPendingTasks", departmentAdminPendingTasks);		
		
		m.addAttribute("labs", labList); m.addAttribute("labmap", labMap);
		m.addAttribute("jobs", jobList);
		m.addAttribute("jobdrafts", jobDraftList);	
		if(authenticationService.isGod() || authenticationService.hasRole("ga")){
			allLabManagerPendingTasks = labService.getAllLabManagerPendingTasks();
		}
		m.addAttribute("allLabManagerPendingTasks", allLabManagerPendingTasks);
		
		m.addAttribute("tasks",taskService.getTaskMappings());
		
		return "dashboard";
	}
}
