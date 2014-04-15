package edu.yu.einstein.wasp.variantcalling.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.controller.JobSubmissionController;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.variantcalling.service.VariantcallingService;

@Controller
@RequestMapping("/jobsubmit/variantcalling")
public class VariantcallingJobSubmissionController extends JobSubmissionController {

	@Autowired
	protected GenomeService genomeService;
	
	@Autowired
	protected StrategyService strategyService;
	
	@Autowired
	protected VariantcallingService variantcallingService;

	@RequestMapping(value="/intervals/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showIntervalFileSelectionForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		Strategy jobStrategy = strategyService.getThisJobDraftsStrategy(StrategyType.LIBRARY_STRATEGY, jobDraft);
		if (!jobStrategy.getStrategy().equals("WXS")){
			logger.debug("Job is configured for strategy '" + jobStrategy.getStrategy() + "', not 'WXS'. Skipping page.'");
			return nextPage(jobDraft);
		}
		
		Map<String, String> buildHeadings = new TreeMap<>();
		Map<String, List<String>> configuredIntervalFilesByBuild = new HashMap<>();
		Set<Build> builds = variantcallingService.getBuildsForJobDraft(jobDraft);
		if (builds.isEmpty()){
			logger.debug("No build specified. Skipping page.");
			return nextPage(jobDraft);
		}
		for (Build b : builds){
			String genomeString = genomeService.getDelimitedParameterString(b);
			buildHeadings.put(genomeString, b.getGenome().getOrganism().getName() + " (" + genomeString + ")");
			configuredIntervalFilesByBuild.put(genomeString, new ArrayList<String>());
			(configuredIntervalFilesByBuild.get(genomeString)).add("None");
			(configuredIntervalFilesByBuild.get(genomeString)).addAll(variantcallingService.getWxsIntervalFilenameFromConfiguration(b));
		}

		m.put("jobDraft", jobDraft);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		m.put("buildHeadings", buildHeadings);
		m.put("intervalFilesByBuild", configuredIntervalFilesByBuild);
		m.put("currentOptions", variantcallingService.getSavedWxsIntervalFilesByBuild(jobDraft));
		
		return "jobsubmit/variantcalling/intervals";
	}

	@RequestMapping(value="/intervals/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateIntervalFileSelection(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		
		Map<String, String> formErrors = new HashMap<>();
		for (Build b : variantcallingService.getBuildsForJobDraft(jobDraft)){
			String buildString = genomeService.getDelimitedParameterString(b);
			String filePath = request.getParameter(buildString);
			logger.debug("processing filePath '" + filePath + "' for buildstring '" + buildString + "'");
			if (filePath == null || filePath.isEmpty()){
				formErrors.put(buildString, "variantcalling.intervalSelectionForm.error");
				continue;
			}
			logger.debug("saving filePath '" + filePath + "' for buildstring '" + buildString + "'");
			variantcallingService.saveWxsIntervalFile(jobDraft, b, filePath);
		} 
	    if (!formErrors.isEmpty()){
			m.put("formErrors", formErrors);
			return showIntervalFileSelectionForm(jobDraftId, m);
	    }
		return nextPage(jobDraft);
	}

}
