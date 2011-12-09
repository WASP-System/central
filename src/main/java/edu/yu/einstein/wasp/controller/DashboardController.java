package edu.yu.einstein.wasp.controller;

import edu.yu.einstein.wasp.service.*;
import edu.yu.einstein.wasp.model.*;

import java.util.List; 
import java.util.ArrayList; 
import java.util.Map; 
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.transaction.annotation.*; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;


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

	// list of baserolenames (da-department admin, lu- labuser ...)
	//   see role table
	// higher level roles such as 'lm' or 'js' are used on the view
	public static enum DashboardEntityRolename {da, lu, jv, jd};

	@RequestMapping("/dashboard")
	public String list(ModelMap m) {
		//List<Department> departmentList = new ArrayList<Department>();
		List<Lab> labList = new ArrayList<Lab>();
		List<Job> jobList = new ArrayList<Job>();
		List<JobDraft> jobDraftList = new ArrayList<JobDraft>();
		
		int departmentAdminPendingTasks = 0;
		
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
				case lu: labList.add(labService.getLabByLabId(roleObjectId)); break;
				case jv: jobList.add(jobService.getJobByJobId(roleObjectId)); break;
				case jd: jobDraftList.add(jobDraftService.getJobDraftByJobDraftId(roleObjectId)); break;
			}
		}
		m.addAttribute("me", authenticationService.getAuthenticatedUser());
		
		//m.addAttribute("departments", departmentList);  //no longer needed
		departmentAdminPendingTasks = departmentService.getDepartmentAdminPendingTasks();//number of da pending tasks (if god or ga, then department not considered)	
		m.addAttribute("departmentAdminPendingTasks", departmentAdminPendingTasks);		
		
		m.addAttribute("labs", labList);
		m.addAttribute("jobs", jobList);
		m.addAttribute("jobdrafts", jobDraftList);			

		return "dashboard";
	}
}
