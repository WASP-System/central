package edu.yu.einstein.wasp.controller.chipseq;

import java.util.ArrayList;
import java.util.Enumeration;
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
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/jobsubmit/chipSeq")
public class ChipSeqJobSubmissionController extends JobSubmissionController {

	@Autowired
	protected JobDraftDao jobDraftDao;
	@Autowired
	protected SampleDraftDao sampleDraftDao;
	@Autowired
	protected SampleDraftMetaDao sampleDraftMetaDao;	
	@Autowired
	protected JobDraftMetaDao jobDraftMetaDao;

	private void confirmContinuedValidityOfSamplePairsAndAdjustAsNeeded(JobDraft jobDraft){
		
		//clean up any samplPairs entries that arise from changes made to of sampleDraft metadata inputOrIP
		//in short, if the user altered an ip to a control or visa versa, then there is the possibility that the samplePairs might no longer be valid
		//so confirm the validity of IP and input/control status and if a problem, then correct it by deleting that specific portion of the samplePair entry

		List<SampleDraft> ipSampleDrafts = new ArrayList<SampleDraft>();
		List<SampleDraft> inputSampleDrafts = new ArrayList<SampleDraft>();
		
		List<SampleDraft> sampleDrafts=jobDraft.getSampleDraft();
		for(SampleDraft sampleDraft : sampleDrafts){			
			for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){				
				if(sampleDraftMeta.getK().endsWith("inputOrIP")){
					if(sampleDraftMeta.getV().equals("ip")){
						ipSampleDrafts.add(sampleDraft);
						break;//from inner for
					}
					else if(sampleDraftMeta.getV().equals("input")){
						inputSampleDrafts.add(sampleDraft);
						break;//from inner for
					}
				}
				
			}
		}
		
		Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		Set<Map<SampleDraft, SampleDraft>> mapsContainingTheSampleDraftToBeRemoved = new HashSet<Map<SampleDraft, SampleDraft>>();
		
		for(Map<SampleDraft, SampleDraft> pair: sampleDraftPairSet){
			for(SampleDraft key : sampleDrafts){
				if(pair.containsKey(key)){
					SampleDraft ipInSamplePair = key;
					SampleDraft inputInSamplePair = pair.get(ipInSamplePair);
					if(!ipSampleDrafts.contains(ipInSamplePair) || !inputSampleDrafts.contains(inputInSamplePair) ){
						mapsContainingTheSampleDraftToBeRemoved.add(pair);						
					}
				}
			}
		}
		sampleDraftPairSet.removeAll(mapsContainingTheSampleDraftToBeRemoved);
		jobDraftService.setSampleDraftPairsByJobDraft(jobDraft, sampleDraftPairSet);
	}
	
	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipSeqPairForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		this.confirmContinuedValidityOfSamplePairsAndAdjustAsNeeded(jobDraft);
		
		List<SampleDraft> sampleDrafts=sampleDraftDao.getSampleDraftByJobId(jobDraftId);
		if (sampleDrafts.size() < 2){
			m.put("noPairingPossible", "true");
			m.put("jobDraft", jobDraft);
			m.put("nextPage", nextPage(jobDraft).replaceFirst("redirect:/", ""));
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			return "jobsubmit/chipseqform";
		}

		List<SampleDraft> inputSampleDrafts = new ArrayList<SampleDraft>();
		List<SampleDraft> ipSampleDrafts = new ArrayList<SampleDraft>();
		Map<SampleDraft, Integer> sampleDraftOrganismMap = new HashMap<SampleDraft, Integer>();
		Map<SampleDraft, String> sampleDraftSpeciesNameMap = new HashMap<SampleDraft, String>();
		
		for(SampleDraft sampleDraft : sampleDrafts){
			boolean foundInputOrIP = false;
			for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){
				if(sampleDraftMeta.getK().endsWith("organism")){
					if(!sampleDraftMeta.getV().equals("0")){//if 0, then species = OTHER
						Integer genomeId = Integer.valueOf(sampleDraftMeta.getV());
						sampleDraftOrganismMap.put(sampleDraft, genomeId);
						String speciesName = genomeService.getOrganismMap().get(genomeId).getName();
						sampleDraftSpeciesNameMap.put(sampleDraft, speciesName);
					}
				}
				if(sampleDraftMeta.getK().endsWith("inputOrIP")){
					if(sampleDraftMeta.getV().equals("input")){
						inputSampleDrafts.add(sampleDraft);
						foundInputOrIP = true;
					}
					else if(sampleDraftMeta.getV().equals("ip")){
						ipSampleDrafts.add(sampleDraft);
						foundInputOrIP = true;
					}
				}
			}
			if(!sampleDraftOrganismMap.containsKey(sampleDraft)){//species == OTHER
				if(inputSampleDrafts.contains(sampleDraft)){
					inputSampleDrafts.remove(sampleDraft);
				}
				if(ipSampleDrafts.contains(sampleDraft)){
					ipSampleDrafts.remove(sampleDraft);
				}
				
			}
			//if(foundInputOrIP == false){//unexpected, but....., then put on both lists (for consistency with previous method of pairing all to all)
			//	inputSampleDrafts.add(sampleDraft);
			//	ipSampleDrafts.add(sampleDraft);
			//}
		}
		if (ipSampleDrafts.isEmpty() || inputSampleDrafts.isEmpty()){//no pairing is possible
			m.put("noPairingPossible", "true");	
			m.put("jobDraft", jobDraft);
			m.put("nextPage", nextPage(jobDraft).replaceFirst("redirect:/", ""));
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			return "jobsubmit/chipseqform";
		}
		
		Set<String> selectedSampleDraftPairStringSet = new HashSet<String>();
		Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		if (!sampleDraftPairSet.isEmpty()){
			for(Map<SampleDraft, SampleDraft> pair: sampleDraftPairSet){
				Entry<SampleDraft, SampleDraft> e = pair.entrySet().iterator().next();
				selectedSampleDraftPairStringSet.add("testVsControl_"+e.getKey().getId()+"_"+e.getValue().getId());
			}
		}

		Map<SampleDraft,SampleDraft> testControlMap = new HashMap<SampleDraft,SampleDraft>();
		for(Map<SampleDraft, SampleDraft> pair: sampleDraftPairSet){
			for (SampleDraft key : pair.keySet()) {
				testControlMap.put(key, pair.get(key));//OK, since for each key, only one value for chipseq
			}
		}
		System.out.println("testControlMap size = " + testControlMap.size());
		for (SampleDraft sampleDraft : testControlMap.keySet()) {
			System.out.println("Test : " + sampleDraft.getName() + " and Control : "
				+ testControlMap.get(sampleDraft).getName());
		}
		m.put("noPairingPossible", "false");
		m.put("jobDraft", jobDraft);
		m.put("samples", sampleDrafts);
		m.put("inputSamples", inputSampleDrafts);
		m.put("ipSamples", ipSampleDrafts);
		m.put("sampleOrganismMap", sampleDraftOrganismMap);
		m.put("sampleSpeciesNameMap", sampleDraftSpeciesNameMap);
		m.put("selectedTestControlMap", testControlMap);
		m.put("selectedSamplePairs", selectedSampleDraftPairStringSet);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		return "jobsubmit/chipseqform";
	}

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateChipSeqPair(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {

		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
	
	    Map<String,String[]> params = request.getParameterMap();
	    for (String key : params.keySet()) {
			System.out.println("Key : " + key.toString());
			String[] stringArray = params.get(key);
			
			for(String s : stringArray){
				System.out.println("--val: " + s);
			}
		}
	
	    List<SampleDraft> allDraftSamples =  jobDraft.getSampleDraft();	
	    Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = new HashSet<Map<SampleDraft, SampleDraft>>();
	    
	    for (SampleDraft draftSample : allDraftSamples) {
	    	String draftSampleId = String.valueOf(draftSample.getId().intValue());
	    	String[] stringArray = params.get("controlIdForIP_" + draftSampleId);
	    	if(stringArray==null){//this draftSample is not an IP sample	    		
	    		continue;
	    	}
	    	else{
	    		SampleDraft ipSampleDraft = draftSample;
	    		String controlId = stringArray[0];
	    		if(controlId.equals("0")){//no control for this IP draftSample
	    			continue;
	    		}
	    		else{
	    			SampleDraft controlSampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(Integer.parseInt(controlId));
	    			Map<SampleDraft,SampleDraft> sampleDraftPair = new HashMap<SampleDraft,SampleDraft>();
	    			sampleDraftPair.put(ipSampleDraft, controlSampleDraft);
	    			sampleDraftPairSet.add(sampleDraftPair);	
	    			System.out.println("IP:Control =  " + ipSampleDraft.getName() + ":" + controlSampleDraft.getName());
	    		}
	    	}	    	
	    }
	    jobDraftService.setSampleDraftPairsByJobDraft(jobDraft, sampleDraftPairSet);
	   
	   	return nextPage(jobDraft);
	    
	   	/*	
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
	    			checkValue = ((String[])params.get("testVsControl_" + sd1Id + "_" + sd2Id))[0];
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
	   	*/
	}

	void confirmContinuedValidityOfReplicatesAndAdjustAsNeeded(JobDraft jobDraft){
		//clean up any replicate entries that arise from changes made to of sampleDraft metadata inputOrIP
		//in short, if the user altered an ip to a control or visa versa, then there is the possibility that the replicates might no longer be valid
		//so confirm the validity of IP and input/control status AND whether an IP has a control; if a problem, then correct it by deleting that replicate
		//FINALLY, recall that if a control sampleDraft was Removed (deleted), this fact won't yet be reflected in the replicates list, since the list is only of IPs. So we need to deal with that deletion here (by examining the samplePair data)

		List<SampleDraft> ipSampleDrafts = new ArrayList<SampleDraft>();
		List<SampleDraft> inputSampleDrafts = new ArrayList<SampleDraft>();
		
		List<SampleDraft> sampleDrafts=jobDraft.getSampleDraft();
		for(SampleDraft sampleDraft : sampleDrafts){			
			for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){				
				if(sampleDraftMeta.getK().endsWith("inputOrIP")){
					if(sampleDraftMeta.getV().equals("ip")){
						ipSampleDrafts.add(sampleDraft);
						break;//from inner for
					}
					else if(sampleDraftMeta.getV().equals("input")){
						inputSampleDrafts.add(sampleDraft);
						break;//from inner for
					}
				}
				
			}
		}
		
		Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		Map<SampleDraft,SampleDraft> testControlMap = new HashMap<SampleDraft,SampleDraft>();
		for(Map<SampleDraft, SampleDraft> pair: sampleDraftPairSet){
			for (SampleDraft key : pair.keySet()) {
				testControlMap.put(key, pair.get(key));//OK, since for each key, only one value for chipseq
			}
		}
		
		List<List<SampleDraft>> replicatesListOfLists = jobDraftService.getReplicateSets(jobDraft);//from database		
		for (List<SampleDraft> sdList: replicatesListOfLists) {
			for(SampleDraft ip : sdList){
				if( !ipSampleDrafts.contains(ip) || !inputSampleDrafts.contains(testControlMap.get(ip)) || !testControlMap.containsKey(ip) ){
					// !ipSampleDrafts.contains(ip)  --ip was previously edited to control
					// !inputSampleDrafts.contains(testControlMap.get(ip))  --input was previously edited to ip
					// !testControlMap.containsKey(ip) --a control sampleDraft was deleted (we have to catch this problem here)					
					jobDraftService.removeSampleDraftFromReplicates(jobDraft, ip);
				}
			}			
		}		
	}
	
	@RequestMapping(value="/replicates/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipSeqReplicatesForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if ( ! isJobDraftEditable(jobDraft) ){
			return "redirect:/dashboard.do";
		}
		
		this.confirmContinuedValidityOfSamplePairsAndAdjustAsNeeded(jobDraft);
		this.confirmContinuedValidityOfReplicatesAndAdjustAsNeeded(jobDraft);
		
		String noReplicatesPossible = "false";
		
		List<SampleDraft> sampleDrafts=jobDraft.getSampleDraft();//sampleDraftDao.getSampleDraftByJobId(jobDraftId);
		if (sampleDrafts.size() < 2){//require at least two samples in jobDraft to create any possible replicate set
			noReplicatesPossible = "true";
		}
				
		//added 6-13-14; get samplePairs list, since for chipSeq's use of IDR for integrative analysis, IP replicates must be paired with an input/control
		Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		Map<SampleDraft,SampleDraft> testControlMap = new HashMap<SampleDraft,SampleDraft>();
		for(Map<SampleDraft, SampleDraft> pair: sampleDraftPairSet){
			for (SampleDraft key : pair.keySet()) {
				testControlMap.put(key, pair.get(key));//OK, since for each key, only one value for chipseq
			}
		}
		
		List<List<SampleDraft>> replicatesListOfLists = jobDraftService.getReplicateSets(jobDraft);//from database		
		Set<SampleDraft> testSampleDraftsAlreadyInReplicateSet = new HashSet<SampleDraft>();//data from List<List<SampleDraft>> replicatesListOfLists converted to a single list for ease of use below
		for (List<SampleDraft> sdList: replicatesListOfLists) {
			for(SampleDraft sd : sdList){
				testSampleDraftsAlreadyInReplicateSet.add(sd);
			}			
		}
				 
		List<SampleDraft> testSampleDraftsAvailableForReplicateSelection = new ArrayList<SampleDraft>();//must be IP; must have a species (species cannot be OTHER)
		
		Map<SampleDraft, String> sampleDraftSpeciesNameMap = new HashMap<SampleDraft, String>();
		
		for(SampleDraft sampleDraft : sampleDrafts){			
			for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){
				if(sampleDraftMeta.getK().endsWith("organism")){
					Integer genomeId = Integer.valueOf(sampleDraftMeta.getV());
					if(genomeId != 0){//0 indicates species Other
						String speciesName = genomeService.getOrganismMap().get(genomeId).getName();
					//System.out.println("want species------------------------sampleDraft: " + sampleDraft.getName() + "   " + " species: " + speciesName);
						sampleDraftSpeciesNameMap.put(sampleDraft, speciesName);
					}
				}
				if(sampleDraftMeta.getK().endsWith("inputOrIP")){
					if(sampleDraftMeta.getV().equals("ip")){
						if(!testSampleDraftsAlreadyInReplicateSet.contains(sampleDraft)){//if not here, then add
							testSampleDraftsAvailableForReplicateSelection.add(sampleDraft);
						}
					}
				}
			}
			//this must be performed following completion of above inner for loop: for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){ 
			if(!sampleDraftSpeciesNameMap.containsKey(sampleDraft)){//species == OTHER
				testSampleDraftsAvailableForReplicateSelection.remove(sampleDraft);		
			}
			if(!testControlMap.containsKey(sampleDraft)){//no control/input paired with this IP, so remove it
				testSampleDraftsAvailableForReplicateSelection.remove(sampleDraft);
			}
		}
		if (testSampleDraftsAlreadyInReplicateSet.isEmpty() && (testSampleDraftsAvailableForReplicateSelection.isEmpty() || testSampleDraftsAvailableForReplicateSelection.size() == 1)){//first time around and either no ipSamples or one ipSample -- unable to make any ip replicates
			noReplicatesPossible = "true";
		}
		
		//In order to determine the SampleDrafts for the createNew select box (which will be stored in testSampleDraftsForCreateNew)
		//first, determine number of times a species appears in the set of available sampleDrafts (testSampleDraftsAvailableForReplicateSelection)
		Map<String,Integer> speciesNameSampleDraftCountMap = new HashMap<String,Integer>();
		List<SampleDraft> testSampleDraftsForCreateNew = new ArrayList<SampleDraft>();

		for(SampleDraft sd : testSampleDraftsAvailableForReplicateSelection){
			String speciesName = sampleDraftSpeciesNameMap.get(sd);
			if(!speciesNameSampleDraftCountMap.containsKey(speciesName)){
				speciesNameSampleDraftCountMap.put(speciesName, new Integer(1));				
			}
			else{
				Integer temp = speciesNameSampleDraftCountMap.get(speciesName);
				speciesNameSampleDraftCountMap.put(speciesName, new Integer(temp.intValue()+1));
			}
		}	
		//add sampleDraft to testSampleDraftsForCreateNew if its species appears at least twice in speciesNameSampleDraftCountMap
		//testSampleDraftsForCreateNew differs from testSampleDraftsAvailableForReplicateSelection as testSampleDraftsForCreateNew MUST contain at least two samples of same species
		for(SampleDraft sd : testSampleDraftsAvailableForReplicateSelection){
			String speciesName = sampleDraftSpeciesNameMap.get(sd);
			if(speciesNameSampleDraftCountMap.containsKey(speciesName)){
				Integer temp = speciesNameSampleDraftCountMap.get(speciesName);
				if(temp.intValue() > 1){
					testSampleDraftsForCreateNew.add(sd);
				}
			}
		}
		
		m.put("testControlMap", testControlMap);
		m.put("replicatesListOfLists", replicatesListOfLists);//replicate sets already in db
		m.put("testSampleDraftsAvailableForReplicateSelection", testSampleDraftsAvailableForReplicateSelection);//for dropdown box  /* NAME WAS CHANGED - should be altered on jsp */
		m.put("testSampleDraftsForCreateNew", testSampleDraftsForCreateNew);//for dropdown box for the create new replicate (MUST contain at least two samples of same species)
		m.put("sampleDraftSpeciesNameMap", sampleDraftSpeciesNameMap);
		m.put("jobDraft", jobDraft);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		m.put("noReplicatesPossible", noReplicatesPossible);
		return "jobsubmit/replicates";
		
	}
	
	@RequestMapping(value="/replicates/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateChipSeqReplicates(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {

		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		
		Map<String,String[]> params = request.getParameterMap();
				
		if(params.containsKey("continueToNextPage")){
	    	return nextPage(jobDraft);
	    }
		
    	if(params.containsKey("testSampleDraftIdForNewReplicateSet")){
    		String[] stringArray = params.get("testSampleDraftIdForNewReplicateSet");
    		String ipId = stringArray[0];
    		SampleDraft sampleDraftToBeSavedToNewReplicateSet = sampleDraftDao.getSampleDraftBySampleDraftId(Integer.parseInt(ipId));
    		jobDraftService.saveReplicateSets(jobDraft, sampleDraftToBeSavedToNewReplicateSet, null);
    		return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
    	}
    	
	    String paramPrefix = "testSampleDraftIdForExistingReplicateSet_";
	    String completeKey = "";
    	for (String key : params.keySet()) {
    		if(key.startsWith(paramPrefix)){
    			completeKey = key;
    			break;
    		}
		}
    	if( !completeKey.isEmpty() ){
    		String[] stringArray = params.get(completeKey);
    		String replicateSetNumberAsString = completeKey.replaceFirst(paramPrefix, "");//so testSampleDraftIdForExistingReplicateSet__1 is converted to 1
    		Integer replicateSetNumberAsInteger = null;
    		try{
    			replicateSetNumberAsInteger = Integer.valueOf(replicateSetNumberAsString);
    		}catch(Exception e){
    			waspErrorMessage("wasp.unexpected_error.error");
    			return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
    		} 
    		String testSampleDraftId = stringArray[0];
    		SampleDraft sampleDraftToBeSavedToExistingReplicateSet = sampleDraftDao.getSampleDraftBySampleDraftId(Integer.parseInt(testSampleDraftId));
    		jobDraftService.saveReplicateSets(jobDraft, sampleDraftToBeSavedToExistingReplicateSet, replicateSetNumberAsInteger);
    		
    		return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
    	}
	    waspErrorMessage("wasp.unexpected_error.error");
    	return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
	}
	
	@RequestMapping(value="/replicates/{jobDraftId}/remove/{sampleDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String removeSampleDraftFromReplicates (@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("sampleDraftId") Integer sampleDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		SampleDraft sampleDraftToBeRemovedFromExistingReplicateSet = sampleDraftDao.getSampleDraftBySampleDraftId(sampleDraftId);
		jobDraftService.removeSampleDraftFromReplicates(jobDraft, sampleDraftToBeRemovedFromExistingReplicateSet);
		return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
	}
	
	@RequestMapping(value="/chipSeqSpecificSampleReview/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String chipSeqSpecificSampleReview (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		Map<SampleDraft, List<String>> sampleDraftErrorListMap = new HashMap<SampleDraft,List<String>>();
		boolean errorsExist = false;
		for(SampleDraft sampleDraft : jobDraft.getSampleDraft()){
			
				List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
				try {			
					normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleDraft.getSampleSubtype(), sampleDraft.getSampleDraftMeta(), SampleDraftMeta.class));
				} catch (MetadataTypeException e) {
					logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
				}
				sampleDraft.setSampleDraftMeta(normalizedMeta);
				sampleDraftList.add(sampleDraft);
			
				List<String> errorList = this.checkForInputOrIPOrOrganismError(normalizedMeta);
				if(!errorList.isEmpty()){
					errorsExist = true;
				}
				sampleDraftErrorListMap.put(sampleDraft, errorList);			
		}
		
		m.addAttribute("jobDraft", jobDraft);
		m.addAttribute("sampleDraftList", sampleDraftList);
		m.addAttribute("sampleDraftErrorListMap", sampleDraftErrorListMap);
		m.addAttribute("errorsExist", errorsExist);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
		if(errorsExist){waspErrorMessage("chipSeq.chipSeqSpecificSampleReview.error");}
		return "jobsubmit/chipSeqSpecificSampleReview";
	}
	
	private List<String> checkForInputOrIPOrOrganismError(List<SampleDraftMeta> sampleDraftMetaList){
		List<String> errorList = new ArrayList<String>();
		
		String inputOrIP = "";//should be either ip or input
		String antibodyTarget = "";//if IP, should not be empty. For input/control, the value is NOT used, so let user do whatever they like
		String peakType = "";//punctate, broad, mixed, none    (should be "punctate", "broad", or "mixed" if IP; should be "none" if sample is input)
		String organism = "";//cannot be empty
		
		for(SampleDraftMeta sdm : sampleDraftMetaList){
			if(sdm.getK().contains("inputOrIP")){
				inputOrIP = sdm.getV();
			}
			else if(sdm.getK().contains("antibody")){
				antibodyTarget = sdm.getV();
			}
			else if(sdm.getK().contains("peakType")){
				peakType = sdm.getV();
			}
			else if(sdm.getK().contains("organism")){
				organism = sdm.getV();
			}
		}
		
		if(inputOrIP.isEmpty()){
			errorList.add(messageService.getMessage("chipSeq.ip_input_empty.error"));//IP or Input/Control cannot be empty
		}
		if(peakType.isEmpty()){
			errorList.add(messageService.getMessage("chipSeq.peakType_empty.error"));//Peak Type cannot be empty
		}
		if(organism.isEmpty()){
			errorList.add(messageService.getMessage("chipSeq.organism_empty.error"));//Organism cannot be empty
		}
		
		if(inputOrIP.equals("ip")){
			if(antibodyTarget.isEmpty()){
				errorList.add(messageService.getMessage("chipSeq.ip_target_empty.error"));	//Provide CHiP Target for this IP sample			
			}
			if(antibodyTarget.trim().toLowerCase().equals("none")){
				errorList.add(messageService.getMessage("chipSeq.ip_target_none.error"));	//CHiP Target for any IP sample cannot be none
			}
			if(peakType.toLowerCase().equals("none")){
				errorList.add(messageService.getMessage("chipSeq.ip_peakType_none.error"));//Peak Type for any IP sample cannot be none
			}
		}
		else if(inputOrIP.equals("input")){
			if(!antibodyTarget.isEmpty() && !antibodyTarget.trim().toLowerCase().equals("none")){
				//decided in the end not to worry about antibodyTarget for an input/control, as the information is, so far, never used
				//and a user might care to put none, or pre-immune, or whatever, so let them put what they like.
				///////errorList.add(messageService.getMessage("chipSeq.input_target_not_empty_and_not_none.error"));//CHiP Target for any Input/Control must be blank or none
			}
			if(!peakType.toLowerCase().equals("none")){
				errorList.add(messageService.getMessage("chipSeq.input_peakType_must_be_none.error"));//Peak Type for any Input/Control sample must be none
			}
		}
		
		return errorList;
	}
	
	@RequestMapping(value="/chipSeqSpecificSampleReview/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String chipSeqSpecificSampleReviewPost (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		
		String[] sampleIdsAsStringArray = request.getParameterValues("sampleId");
		int numberOfIncomingRows = sampleIdsAsStringArray.length;

		String[] inputOrIPValues = null;
		String[] antibodyTargetValues = null;
		String[] peakTypeValues = null;	
		String[] organismValues = null;
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (String key : parameterMap.keySet()) {
			if(key.endsWith("inputOrIP")){
				inputOrIPValues = parameterMap.get(key);
			}
			else if(key.endsWith("antibody")){
				antibodyTargetValues = parameterMap.get(key);
			}
			else if(key.endsWith("peakType")){
				peakTypeValues = parameterMap.get(key);
			}
			else if(key.endsWith("organism")){
				organismValues = parameterMap.get(key);
			}
		}
		
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		Map<SampleDraft, List<String>> sampleDraftErrorListMap = new HashMap<SampleDraft,List<String>>();
		boolean errorsExist = false;
		
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
				if(sdm.getK().endsWith("inputOrIP")){
					sdm.setV(inputOrIPValues[counter]);
				}
				else if(sdm.getK().endsWith("antibody")){
					sdm.setV(antibodyTargetValues[counter]);
				}
				else if(sdm.getK().endsWith("peakType")){
					sdm.setV(peakTypeValues[counter]);
				}
				else if(sdm.getK().endsWith("organism")){
					sdm.setV(organismValues[counter]);
				}
			}
			//conscious decision by Rob: save the new meta, even if it has problems. will check below (if errorsExist==true) and return to jobsubmit/chipSeqSpecificSampleReview if errors
			try{
				sampleDraftMetaDao.setMeta(normalizedMeta, sampleDraft.getId());//THIS IS A SAVE COMMAND!
			}
			catch(Exception e){logger.debug("unable to save sampleMetaList in chipSeqSpecificSampleReviewPost() for sampleDraft: " + sampleDraft.getName() + ". message: " + e.getMessage());}
			
			sampleDraft.setSampleDraftMeta(normalizedMeta);			
			sampleDraftList.add(sampleDraft);
			
			List<String> errorList = this.checkForInputOrIPOrOrganismError(normalizedMeta);
			if(!errorList.isEmpty()){
				errorsExist = true;
			}
			sampleDraftErrorListMap.put(sampleDraft, errorList);
			
			counter++;
		}
		 
		if(errorsExist == true){			
			m.addAttribute("jobDraft", jobDraft);
			m.addAttribute("sampleDraftList", sampleDraftList);
			m.addAttribute("sampleDraftErrorListMap", sampleDraftErrorListMap);
			m.addAttribute("errorsExist", errorsExist);
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
			waspErrorMessage("chipSeq.chipSeqSpecificSampleReview.error");
			return "jobsubmit/chipSeqSpecificSampleReview";
		}
		
		return nextPage(jobDraft);		
	}
}

