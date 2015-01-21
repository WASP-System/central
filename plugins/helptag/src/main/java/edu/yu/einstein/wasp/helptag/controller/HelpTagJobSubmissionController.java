package edu.yu.einstein.wasp.helptag.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.JobSubmissionController;
import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;

@Controller
@Transactional
@RequestMapping("/jobsubmit/helptag")
public class HelpTagJobSubmissionController extends JobSubmissionController {

	@Autowired
	protected JobDraftDao jobDraftDao;

	@Autowired
	protected SampleDraftDao sampleDraftDao;
	
	@Autowired
	protected JobDraftMetaDao jobDraftMetaDao;
	
	@Autowired
	protected HelptagService helptagService;

	protected final String SAMPLE_PAIR_STR_PREFIX = "testVsControl";

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showHelpTagPairForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		List<SampleDraft> sampleDrafts=sampleDraftDao.getSampleDraftByJobId(jobDraftId);
		if (sampleDrafts.size() < 2){
			return nextPage(jobDraft);
		}
		

		Set<String> selectedSampleDraftPairStringSet = new HashSet<String>();
		Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		if (!sampleDraftPairSet.isEmpty()){
			for(Map<SampleDraft, SampleDraft> pair: sampleDraftPairSet){
				Entry<SampleDraft, SampleDraft> e = pair.entrySet().iterator().next();
				selectedSampleDraftPairStringSet.add(SAMPLE_PAIR_STR_PREFIX + "_" + e.getKey().getId() + "_" + e.getValue().getId());
			}
		}

		// helptag specific parameters needed on the JSP form
		m.put("m_samples", helptagService.getAllMspISampleDraftsFromJobDraftId(jobDraftId));
		//m.put("h_samples", helptagService.getAllHpaIISampleDraftsFromJobDraftId(jobDraftId));		
		m.put("h_samples", helptagService.getAllHpaIIAndbetaGTMspISampleDraftsFromJobDraftId(jobDraftId));
		
		m.put("samplePairStrPrefix", SAMPLE_PAIR_STR_PREFIX);
		m.put("selectedSamplePairs", selectedSampleDraftPairStringSet);

		// generic parameters needed on the JSP form
		m.put("jobDraft", jobDraft);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		boolean atLeastOneBetaGTMspISampleDraftPresent = false;
		List<SampleDraft> betaGTMspISampleDraftList = helptagService.getAllbetaGTMspISampleDraftsFromJobDraftId(jobDraftId);
		if(betaGTMspISampleDraftList.size()>0){
			atLeastOneBetaGTMspISampleDraftPresent = true;
		}
		m.put("atLeastOneBetaGTMspISampleDraftPresent",atLeastOneBetaGTMspISampleDraftPresent);
		
		return "jobsubmit/helptagform";
	}

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateHelpTagPair(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {

		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
	
	    Map<String,String[]> params = request.getParameterMap();
	
	    List<SampleDraft> samples =  jobDraft.getSampleDraft();
	
	    Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = new HashSet<Map<SampleDraft, SampleDraft>>();
	    for (SampleDraft sd1: samples) {
	    	String sd1Id = String.valueOf(sd1.getId().intValue());
	    	for (SampleDraft sd2: samples) {
	    		String sd2Id = String.valueOf(sd2.getId().intValue());
	    		if (sd1Id.equals(sd2Id))
	    			continue;
	    		String checkValue = "";
	    		try {
	    			checkValue = ((String[])params.get(SAMPLE_PAIR_STR_PREFIX + "_" + sd1Id + "_" + sd2Id))[0];
	    		} catch (Exception e) {
	    		}
		
	    		if (checkValue.equals("1")){
	    			Map<SampleDraft,SampleDraft> sampleDraftPair = new HashMap<SampleDraft,SampleDraft>();
	    			sampleDraftPair.put(sd1, sd2);
	    			sampleDraftPairSet.add(sampleDraftPair);
	    		}
	    	}
	    }
	    
	    jobDraftService.setSampleDraftPairsByJobDraft(jobDraft, sampleDraftPairSet);
	
		return nextPage(jobDraft);
	}


	@RequestMapping(value="/helptagSpecificSampleReview/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String helptagSpecificSampleReview (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		//THIS DEALS WITH SUBMISSION OF GENOMIC DNA BEING SUBMITTED TO FACILITY FOR CREATION OF HELP LIBRARIES
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		Map<SampleDraft,String> sampleDraftTypeOfHelpLibraryRequestedMap = new HashMap<SampleDraft,String>();
		for(SampleDraft sampleDraft : jobDraft.getSampleDraft()){
			
			if(sampleDraft.getSampleType().getIName().equalsIgnoreCase("library")){//here, we only want genomic DNA samples that are to be converted to help-tag libraries
				continue;
			}			
			String typeOfHelpLibraryRequested = "unexpectedly not found - please fix";
			for(SampleDraftMeta sdm : sampleDraft.getSampleDraftMeta()){
				 if(sdm.getK().endsWith("typeOfHelpLibraryRequested")){
					typeOfHelpLibraryRequested = sdm.getV();
				}
			}		
			sampleDraftTypeOfHelpLibraryRequestedMap.put(sampleDraft, typeOfHelpLibraryRequested);
			sampleDraftList.add(sampleDraft);
		}
	
		m.addAttribute("jobDraft", jobDraft);
		m.addAttribute("sampleDraftList", sampleDraftList);
		m.addAttribute("sampleDraftTypeOfHelpLibraryRequestedMap", sampleDraftTypeOfHelpLibraryRequestedMap);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		return "jobsubmit/helptagSpecificSampleReview";
	}

	@RequestMapping(value="/helptagSpecificSampleReview/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String helptagSpecificSampleReviewPost (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		//THIS DEALS WITH SUBMISSION OF GENOMIC DNA BEING SUBMITTED TO FACILITY FOR CREATION OF HELP LIBRARIES

		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		Map<SampleDraft,String> sampleDraftTypeOfHelpLibraryRequestedMap = new HashMap<SampleDraft,String>();
		boolean atLeastOneSampleConversionOccurred = false;
		for(SampleDraft sampleDraft : jobDraft.getSampleDraft()){
			
			if(sampleDraft.getSampleType().getIName().equalsIgnoreCase("library")){//here, we only want genomic DNA samples that are to be converted to help-tag libraries
				continue;
			}	
			
			List<String> typeOfHelpLibrariesRequestedList = helptagService.getTypeOfHelpLibrariesRequestedList(sampleDraft.getSampleDraftMeta());
			if(typeOfHelpLibrariesRequestedList.size()>1){//convert
				List<SampleDraft> newSampleDrafts = helptagService.createNewHelpDNASampleDrafts(sampleDraft, typeOfHelpLibrariesRequestedList);
				//newSampleDrafts.size should equal typeOfHelpLibrariesRequestedList.size
				if(typeOfHelpLibrariesRequestedList.size()!=newSampleDrafts.size()){
					waspErrorMessage("helptag.helptagSpecificSampleReview_unexpectedProblemOccurredNewRecordHasErrors.error");
					return "redirect:/dashboard.do";
				}
				for(SampleDraft newSampleDraft : newSampleDrafts){
					String typeOfHelpLibraryRequested = "unexpectedly not found - please fix";
					for(SampleDraftMeta sdm : newSampleDraft.getSampleDraftMeta()){
						 if(sdm.getK().endsWith("typeOfHelpLibraryRequested")){
							typeOfHelpLibraryRequested = sdm.getV();
						}
					}
					sampleDraftList.add(newSampleDraft);
					sampleDraftTypeOfHelpLibraryRequestedMap.put(newSampleDraft, typeOfHelpLibraryRequested.replaceAll(",", ", "));
				}
				atLeastOneSampleConversionOccurred = true;
				jobDraftService.removeSampleDraftAndAllDependencies(jobDraft, sampleDraft);
			}
			else {//no conversion required for this one
				String typeOfHelpLibraryRequested = "unexpectedly not found - please fix";
				for(SampleDraftMeta sdm : sampleDraft.getSampleDraftMeta()){
					 if(sdm.getK().endsWith("typeOfHelpLibraryRequested")){
						typeOfHelpLibraryRequested = sdm.getV();
					}
				}
				sampleDraftList.add(sampleDraft);
				sampleDraftTypeOfHelpLibraryRequestedMap.put(sampleDraft, typeOfHelpLibraryRequested.replaceAll(",", ", "));				
			}
		}
		
		if(atLeastOneSampleConversionOccurred){
			m.addAttribute("jobDraft", jobDraft);
			m.addAttribute("sampleDraftList", sampleDraftList);
			m.addAttribute("sampleDraftTypeOfHelpLibraryRequestedMap", sampleDraftTypeOfHelpLibraryRequestedMap);			
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			m.addAttribute("atLeastOneSampleConversionOccurred", atLeastOneSampleConversionOccurred);
			waspMessage("helptag.helptagSpecificSampleReview_NewRecordsCreated.label");	
			return "jobsubmit/helptagSpecificSampleReview";
		}
		
		return nextPage(jobDraft);
	}
}

