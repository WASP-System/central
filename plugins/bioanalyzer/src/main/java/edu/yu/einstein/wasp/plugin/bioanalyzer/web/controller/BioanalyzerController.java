/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.BioanalyzerService;


@Controller
@RequestMapping("/bioanalyzer")
public class BioanalyzerController extends WaspController {
	@Autowired
	protected AuthenticationService authenticationService;
	@Autowired
	private BioanalyzerService bioanalyzerService;
	@Autowired
	private LabService labService;
	@Autowired
	private MessageServiceWebapp messageService;
	@Autowired
	private StrategyService strategyService;
	@Autowired
	private WorkflowService workflowService;
	
	@RequestMapping(value="/displayDescription", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + bioanalyzerService.performAction());
		return "bioanalyzer/description";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createNewBioanalyzerJobGet(ModelMap m){
		
		User me = authenticationService.getAuthenticatedUser();

		List <LabUser> labUserAllRoleList = me.getLabUser();
		List <Lab> labList = new ArrayList<Lab>();
		for (LabUser lu: labUserAllRoleList) {
			String roleName =	lu.getRole().getRoleName();
			if (roleName.equals("lu") || roleName.equals("lm") || roleName.equals("pi")) {
				labList.add(lu.getLab());
			}
		}
		if (labList.isEmpty()){
			waspErrorMessage("bioanalyzer.create_labList.error");
			return "redirect:/dashboard.do";
		}
		m.put("labList", labList);
		Map<Lab, String> labPiInstitutionMap = new HashMap<Lab, String>();
		for(Lab lab : labList){
			String institution = labService.getInstitutionOfLabPI(lab);
			if(!institution.isEmpty()){
				labPiInstitutionMap.put(lab, institution);
			}
		}
		m.put("labPiInstitutionMap", labPiInstitutionMap);
		
		List<String> availableBioanalyzerChipList = new ArrayList<String>();
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChipHighSensitivity.label"));
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip7500.label"));
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip1000.label"));
		m.put("availableBioanalyzerChipList", availableBioanalyzerChipList);
		
		//get workflows with stratagies, meaning its a sequencing workflow (so, the bioanalyzer workflow is not a sequencing workflow)
		List<Workflow> workflowList = new ArrayList<Workflow>();
		for(Workflow workflow : workflowService.getWorkflowDao().findAll()){
			//if(!strategyService.getThisWorkflowsStrategies(StrategyType.LIBRARY_STRATEGY, workflow).isEmpty()){
			//	workflowList.add(workflow);
			//	logger.debug("the workflowname: " + workflow.getName());
			//}
			if(!workflow.getIName().equalsIgnoreCase("bioanalyzer")){
				workflowList.add(workflow);
			}
		}
		class WorkflowNameComparator implements Comparator<Workflow> {
		    @Override
		    public int compare(Workflow arg0, Workflow arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		Collections.sort(workflowList, new WorkflowNameComparator());
		m.put("workflowList", workflowList);
		for(Workflow wf : workflowList){
			logger.debug("the workflowname again: " + wf.getName());
		}
		return "bioanalyzer/create";
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	
	public String createNewBioanalyzerJobPost( @RequestParam(value="labId") Integer labId,
			@RequestParam(value="grantId", required=false) Integer grantId,
			 @RequestParam(value="bioanalyzerChip") String bioanalyzerChip,
			 @RequestParam(value="workflowId") Integer workflowId,
			 @RequestParam(value="jobName") String jobName,
			 
			@RequestParam(value="libraryName[]") String[] libraryNameArray,
			@RequestParam(value="librarySize[]") String[] librarySizeArray,
			@RequestParam(value="libraryConcentration[]") String[] libraryConcentration,
			@RequestParam(value="libraryConcDeterminedByFluorometry[]") String[] libraryConcDeterminedByFluorometry,
			ModelMap m){
		
		
		//********************MAKE GrantId required
		
		return "bioanalyzer/description";
	}

}
