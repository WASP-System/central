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
		Map<SampleDraft, List<String>> sampleDraftErrorListMap = new HashMap<SampleDraft,List<String>>();
		boolean errorsExist = false;
		for(SampleDraft sampleDraft : jobDraft.getSampleDraft()){
			
			if(sampleDraft.getSampleType().getIName().equalsIgnoreCase("library")){//here, we only want genomic DNA samples that are to be converted to help-tag libraries
				continue;
			}
			
			List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
			try {			
				normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleDraft.getSampleSubtype(), sampleDraft.getSampleDraftMeta(), SampleDraftMeta.class));
			} catch (MetadataTypeException e) {
				logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
			}
			sampleDraft.setSampleDraftMeta(normalizedMeta);
			
			List<String> errorList = this.checkForGlycosylatedRestrictedLibrariesToCreateError(normalizedMeta);
			if(!errorList.isEmpty()){
				errorsExist = true;
			}
			sampleDraftErrorListMap.put(sampleDraft, errorList);
			sampleDraftList.add(sampleDraft);					
		}
	
		m.addAttribute("jobDraft", jobDraft);
		m.addAttribute("sampleDraftList", sampleDraftList);
		m.addAttribute("sampleDraftErrorListMap", sampleDraftErrorListMap);
		m.addAttribute("errorsExist", errorsExist);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		if(errorsExist){waspErrorMessage("helptag.helptagSpecificSampleReview.error");}
		return "jobsubmit/helptagSpecificSampleReview";
	}
	private List<String> checkForGlycosylatedRestrictedLibrariesToCreateError(List<SampleDraftMeta> sampleDraftMetaList){
		
		//THIS DEALS WITH SUBMISSION OF GENOMIC DNA BEING SUBMITTED TO FACILITY FOR CREATION OF HELP LIBRARIES

		List<String> errorList = new ArrayList<String>();
		
		String glycosylatedBeforeSubmission = "";//should be either glycosylated or unglycosylated
		String restrictedBeforeSubmission = "";//should be unrestricted or restricted
		String libraryToCreate = "";//MspI or HpaII or beta-GT-MspI

		for(SampleDraftMeta sdm : sampleDraftMetaList){
			if(sdm.getK().endsWith("glycosylatedBeforeSubmission")){
				glycosylatedBeforeSubmission = sdm.getV();
			}
			else if(sdm.getK().endsWith("restrictedBeforeSubmission")){
				restrictedBeforeSubmission = sdm.getV();
			}
			else if(sdm.getK().endsWith("libraryToCreate")){
				libraryToCreate = sdm.getV();
			}
		}
		
		if(glycosylatedBeforeSubmission.isEmpty()){
			errorList.add(messageService.getMessage("helptag.helptagSpecificSampleReview_glycosylatedEmpty.error"));
			//errorList.add("Sample glycosylation status cannot be empty");
		}
		if(restrictedBeforeSubmission.isEmpty()){
			errorList.add(messageService.getMessage("helptag.helptagSpecificSampleReview_restrictionEmpty.error"));
			//errorList.add("Sample restriction status cannot be empty");
		}
		if(libraryToCreate.isEmpty()){
			errorList.add(messageService.getMessage("helptag.helptagSpecificSampleReview_libraryToCreateEmpty.error"));
			//errorList.add("Library to create cannot be empty");
		}
		
		if(glycosylatedBeforeSubmission.equals("unglycosylated") && restrictedBeforeSubmission.equals("unrestricted")){
			;//NOT AN ERROR, since any library request is fine
		}
		else if(glycosylatedBeforeSubmission.equals("unglycosylated") && restrictedBeforeSubmission.equals("MspI") && !libraryToCreate.equals("MspI")){
			errorList.add(messageService.getMessage("helptag.helptagSpecificSampleReview_unglycosylatedRestrictedMsp.error"));
			//errorList.add("Unglycosylated/MspI-restricted DNA compatible only with MspI library");//since glycosylation must precede restriction
		}
		else if(glycosylatedBeforeSubmission.equals("unglycosylated") && restrictedBeforeSubmission.equals("HpaII") && !libraryToCreate.equals("HpaII")){
			errorList.add(messageService.getMessage("helptag.helptagSpecificSampleReview_unglycosylatedRestrictedHpa.error"));
			//errorList.add("Unglycosylated/HpaII-restricted DNA compatible only with HpaII library");//since glycosylation must precede restriction
		}
		else if(glycosylatedBeforeSubmission.equals("beta-GT") && (restrictedBeforeSubmission.equals("unrestricted") || restrictedBeforeSubmission.equals("MspI")) && ! libraryToCreate.equals("beta-GT-MspI")){
			errorList.add(messageService.getMessage("helptag.helptagSpecificSampleReview_glycosylatedLibrary.error"));
			//errorList.add("Glycosylated DNA compatible only with beta-GT-MspI library");
		}
		else if(glycosylatedBeforeSubmission.equals("beta-GT") && restrictedBeforeSubmission.equals("HpaII")){
			errorList.add(messageService.getMessage("helptag.helptagSpecificSampleReview_glycosylatedHpa.error"));
			//errorList.add("Glycosylated DNA incompatible with HpaII-digested DNA");
		}
		return errorList;
	}
	@RequestMapping(value="/helptagSpecificSampleReview/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String helptagSpecificSampleReviewPost (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		//THIS DEALS WITH SUBMISSION OF GENOMIC DNA BEING SUBMITTED TO FACILITY FOR CREATION OF HELP LIBRARIES

		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		
		String[] sampleIdsAsStringArray = request.getParameterValues("sampleId");
		if(sampleIdsAsStringArray==null){//there could be no DNA samples to deal with
			return nextPage(jobDraft);
		}
		
		String[] glycosylatedBeforeSubmissionValues = null;
		String[] restrictedBeforeSubmissionValues = null;
		String[] libraryToCreateValues = null;	
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (String key : parameterMap.keySet()) {
			if(key.endsWith("glycosylatedBeforeSubmission")){
				glycosylatedBeforeSubmissionValues = parameterMap.get(key);
			}
			else if(key.endsWith("restrictedBeforeSubmission")){
				restrictedBeforeSubmissionValues = parameterMap.get(key);
			}
			else if(key.endsWith("libraryToCreate")){
				libraryToCreateValues = parameterMap.get(key);
			}
		}
	
		List<SampleDraft> tempSampleDraftList = new ArrayList<SampleDraft>();
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		Map<SampleDraft,List<SampleDraftMeta>> sampleDraftSampleDraftMetaListMap = new HashMap<SampleDraft,List<SampleDraftMeta>>();
		Map<SampleDraft, List<String>> sampleDraftErrorListMap = new HashMap<SampleDraft,List<String>>();
		boolean errorsExist = false;
		boolean atLeastOneSampleConversionOccurred = false;
		
		int counter = 0;
		for(String idAsString: sampleIdsAsStringArray){
			SampleDraft sampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(Integer.parseInt(idAsString));
			
			List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
			try {			
				normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleDraft.getSampleSubtype(), sampleDraft.getSampleDraftMeta(), SampleDraftMeta.class));
			} catch (MetadataTypeException e) {
				logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
			}
			for(SampleDraftMeta sdm : normalizedMeta){
				if(sdm.getK().endsWith("glycosylatedBeforeSubmission")){
					sdm.setV(glycosylatedBeforeSubmissionValues[counter]);
				}
				else if(sdm.getK().endsWith("restrictedBeforeSubmission")){
					sdm.setV(restrictedBeforeSubmissionValues[counter]);
				}
				else if(sdm.getK().endsWith("libraryToCreate")){
					sdm.setV(libraryToCreateValues[counter]);
				}
			}
			//conscious decision by Rob: save the new meta, even if it has problems. will check below (if errorsExist==true) 
			try{
				sampleDraftMetaDao.setMeta(normalizedMeta, sampleDraft.getId());//THIS IS A SAVE COMMAND!
			}
			catch(Exception e){logger.debug("unable to save sampleMetaList in helptagSpecificSampleReviewPost() for sampleDraft: " + sampleDraft.getName() + ". message: " + e.getMessage());}
			
			sampleDraft.setSampleDraftMeta(normalizedMeta);
			sampleDraftSampleDraftMetaListMap.put(sampleDraft, normalizedMeta);
			tempSampleDraftList.add(sampleDraft);
			
			List<String> errorList = this.checkForGlycosylatedRestrictedLibrariesToCreateError(normalizedMeta);
			if(!errorList.isEmpty()){
				errorsExist = true;
			}
			sampleDraftErrorListMap.put(sampleDraft, errorList);
			
			counter++;
		}
		
		if(errorsExist){
			m.addAttribute("jobDraft", jobDraft);
			m.addAttribute("sampleDraftList", tempSampleDraftList);//Note use of tempSampleDraftList !!!!!!!!!!!!!!!!**************!!!!!!!!!!
			m.addAttribute("sampleDraftErrorListMap", sampleDraftErrorListMap);
			m.addAttribute("errorsExist", errorsExist);
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			waspErrorMessage("helptag.helptagSpecificSampleReview.error");
			return "jobsubmit/helptagSpecificSampleReview";
		}
	
		//no errors - so next check if we need to convert any sampleDraft to two or three samples because user asked for multiple libraries to be made; if so, do the conversion
		for(SampleDraft sampleDraft : tempSampleDraftList){
			List<String> librariesToCreateList = helptagService.getLibrariesToCreateList(sampleDraftSampleDraftMetaListMap.get(sampleDraft));//this will be the normalized meta from above
			if(librariesToCreateList.size()>1){
				List<SampleDraft> newSampleDrafts = helptagService.createNewHelpDNASampleDrafts(sampleDraft, librariesToCreateList);
				for(SampleDraft newSampleDraft : newSampleDrafts){
					List<SampleDraftMeta> normalizedMeta2 = new ArrayList<SampleDraftMeta>();
					try {	
						normalizedMeta2.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(newSampleDraft.getSampleSubtype(), newSampleDraft.getSampleDraftMeta(), SampleDraftMeta.class));
					} catch (MetadataTypeException e) {
						logger.warn("Could not get meta into normalizedMeta2 for class 'SampleDraftMeta':" + e.getMessage());
					}
					newSampleDraft.setSampleDraftMeta(normalizedMeta2);
					sampleDraftList.add(newSampleDraft);
					
					//acts as an assert test, to make sure the system did not screw up with the conversions.
					List<String> errorListAssert = this.checkForGlycosylatedRestrictedLibrariesToCreateError(normalizedMeta2);
					if(!errorListAssert.isEmpty()){
						waspErrorMessage("helptag.helptagSpecificSampleReview_unexpectedProblemOccurredNewRecordHasErrors.error");						
						return "redirect:/dashboard.do";
					}
				}
				atLeastOneSampleConversionOccurred = true;
				jobDraftService.removeSampleDraftAndAllDependencies(jobDraft, sampleDraft);
			}
			else if(librariesToCreateList.size()==1){
				sampleDraftList.add(sampleDraft);
			}
		}
		
		if(atLeastOneSampleConversionOccurred){
			m.addAttribute("jobDraft", jobDraft);
			m.addAttribute("sampleDraftList", sampleDraftList);//Note use of sampleDraftList !*!
			m.addAttribute("sampleDraftErrorListMap", sampleDraftErrorListMap);//there are no errors
			m.addAttribute("errorsExist", errorsExist);//will be false
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			m.addAttribute("atLeastOneSampleConversionOccurred", atLeastOneSampleConversionOccurred);
			waspMessage("helptag.helptagSpecificSampleReview_NewRecordsCreated.label");	
			return "jobsubmit/helptagSpecificSampleReview";
		}
		
		return nextPage(jobDraft);
		
	}
}

