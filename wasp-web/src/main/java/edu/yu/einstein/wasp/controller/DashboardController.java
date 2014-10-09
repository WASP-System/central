package edu.yu.einstein.wasp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.taskMapping.TaskMappingRegistry;
import edu.yu.einstein.wasp.taskMapping.WaspTaskMapping;

@Controller
@Transactional
public class DashboardController extends WaspController {


	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private TaskMappingRegistry taskMappingRegistry;
	
	@Autowired
	private JobService jobService;
	
	@RequestMapping("/dashboard")
	public String list(ModelMap m) {
		boolean isTasks = false;
		m.addAttribute("me", authenticationService.getAuthenticatedUser());
		for (String name: taskMappingRegistry.getNames()){
			WaspTaskMapping taskMapping = taskMappingRegistry.getTaskMapping(name);
			if (taskMapping == null){
				logger.warn("Unable to retrieve a taskmapping with name '" + name + "' from the TaskMappingRegistry");
				continue;
			}
			if (taskMapping.isLinkToBeShown(jobService.getActiveJobs())){				
				isTasks = true;
				break;
			}
		}
		
		m.addAttribute("isTasks", isTasks);
		return "dashboard";
	}
}
