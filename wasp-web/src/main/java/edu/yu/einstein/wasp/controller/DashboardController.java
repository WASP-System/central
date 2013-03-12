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
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.taskMapping.TaskMappingRegistry;
import edu.yu.einstein.wasp.taskMapping.WaspTaskMapping;
import edu.yu.einstein.wasp.web.WebHyperlink;

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
	private EmailService emailService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskMappingRegistry taskMappingRegistry;
	

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
				default:
					break;
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
		
		List<WebHyperlink> taskMappingHyperlinksToDisplay = new ArrayList<WebHyperlink>();
		for (String name: taskMappingRegistry.getNames()){
			WaspTaskMapping taskMapping = taskMappingRegistry.getTaskMapping(name);
			if (taskMapping == null){
				logger.warn("Unable to retrieve a taskmapping with name '" + name + "' from the TaskMappingRegistry");
				continue;
			}
			if (taskMapping.isLinkToBeShown())
				taskMappingHyperlinksToDisplay.add(taskMapping);
		}
		
		m.addAttribute("taskHyperlinks",taskMappingHyperlinksToDisplay);
		m.addAttribute("isTasks", taskMappingHyperlinksToDisplay.size() > 0);
		/*
		emailService.sendSubmitterJobStarted(jobDao.findById(47));//for testing by dubin 3-11-13
		emailService.sendPIJobStartedConfirmRequest(jobDao.findById(47));//for testing by dubin 3-11-13
		emailService.sendLabManagerJobStartedConfirmRequest(jobDao.findById(47));//for testing by dubin 3-11-13
		emailService.sendDAJobStartedConfirmRequest(jobDao.findById(47));//for testing by dubin 3-11-13
		emailService.sendFacilityManagerJobStartedConfirmRequest(jobDao.findById(47));//for testing by dubin 3-11-13
		emailService.sendSubmitterWhoIsAlsoThePIJobStartedConfirmRequest(jobDao.findById(47));//for testing by dubin 3-11-13
		*/
		/*
		emailService.sendJobStarted(jobDao.findById(47), userDao.getUserByLogin("eberko"), "emails/inform_submitter_job_started");
		emailService.sendJobStarted(jobDao.findById(47), userDao.getUserByLogin("jgreally"), "emails/inform_pi_or_lab_manager_job_started");
		emailService.sendJobStarted(jobDao.findById(47), userDao.getUserByLogin("msuzuki"), "emails/inform_pi_or_lab_manager_job_started");
		emailService.sendJobStarted(jobDao.findById(47), userDao.getUserByLogin("smaqbool"), "emails/inform_facility_manager_job_started");
		emailService.sendJobStarted(jobDao.findById(47), userDao.getUserByLogin("smaslova"), "emails/inform_da_job_started");
		*/

		

		
		return "dashboard";
	}
}
