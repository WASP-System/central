package edu.yu.einstein.wasp.controller.chipseq;

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
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;

@Controller
@Transactional
@RequestMapping("/jobsubmit/chipSeq")
public class ChipSeqJobSubmissionController extends JobSubmissionController {

	@Autowired
	protected JobDraftDao jobDraftDao;

	@Autowired
	protected SampleDraftDao sampleDraftDao;
	
	@Autowired
	protected JobDraftMetaDao jobDraftMetaDao;

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipSeqPairForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";


		List<SampleDraft> sampleDrafts=sampleDraftDao.getSampleDraftByJobId(jobDraftId);
		if (sampleDrafts.size() < 2){
			return nextPage(jobDraft);
		}

		List<SampleDraft> inputSampleDrafts = new ArrayList<SampleDraft>();
		List<SampleDraft> ipSampleDrafts = new ArrayList<SampleDraft>();
		Map<SampleDraft, Integer> sampleDraftOrganismMap = new HashMap<SampleDraft, Integer>();
		
		for(SampleDraft sampleDraft : sampleDrafts){
			boolean foundInputOrIP = false;
			for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){
				if(sampleDraftMeta.getK().endsWith("organism")){
					sampleDraftOrganismMap.put(sampleDraft, new Integer(sampleDraftMeta.getV()));
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
			return nextPage(jobDraft);
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
		m.put("jobDraft", jobDraft);
		m.put("samples", sampleDrafts);
		m.put("inputSamples", inputSampleDrafts);
		m.put("ipSamples", ipSampleDrafts);
		m.put("sampleOrganismMap", sampleDraftOrganismMap);
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

	@RequestMapping(value="/replicates/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipSeqReplicatesForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if ( ! isJobDraftEditable(jobDraft) ){
			return "redirect:/dashboard.do";
		}
		List<SampleDraft> sampleDrafts=sampleDraftDao.getSampleDraftByJobId(jobDraftId);
		if (sampleDrafts.size() < 2){
			return nextPage(jobDraft);
		}
		
		List<List<SampleDraft>> replicatesListOfLists = jobDraftService.getReplicateSets(jobDraft);//from database
		for (List<SampleDraft> sdList: replicatesListOfLists) {
			for(SampleDraft sd : sdList){
				System.out.println("---:"+sd.getId()+" ---: "+sd.getName());
			}			
		}
		
		Set<SampleDraft> sampleDraftsAlreadyInReplicateSet = new HashSet<SampleDraft>();		
		for (List<SampleDraft> sdList: replicatesListOfLists) {
			for(SampleDraft sd : sdList){
				sampleDraftsAlreadyInReplicateSet.add(sd);
			}			
		}
		for(SampleDraft sd : sampleDraftsAlreadyInReplicateSet){
			System.out.println("sampleDraft already in a replicate set in jobDraftMeta: ---:"+sd.getId()+" ---: "+sd.getName());
		}
		
		
		
		List<SampleDraft> inputSampleDrafts = new ArrayList<SampleDraft>();
		List<SampleDraft> ipSampleDrafts = new ArrayList<SampleDraft>();
		Map<SampleDraft, Integer> sampleDraftOrganismMap = new HashMap<SampleDraft, Integer>();
		
		for(SampleDraft sampleDraft : sampleDrafts){			
			for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){
				if(sampleDraftMeta.getK().endsWith("organism")){
					sampleDraftOrganismMap.put(sampleDraft, new Integer(sampleDraftMeta.getV()));
				}
				if(sampleDraftMeta.getK().endsWith("inputOrIP")){
					if(sampleDraftMeta.getV().equals("ip")){
						ipSampleDrafts.add(sampleDraft);
					}
				}
			}
			if(!sampleDraftOrganismMap.containsKey(sampleDraft)){//species == OTHER
				if(ipSampleDrafts.contains(sampleDraft)){
					ipSampleDrafts.remove(sampleDraft);
				}				
			}
			if(sampleDraftsAlreadyInReplicateSet.contains(sampleDraft)){
				if(ipSampleDrafts.contains(sampleDraft)){
					ipSampleDrafts.remove(sampleDraft);
				}
			}
		}
		if (sampleDraftsAlreadyInReplicateSet.isEmpty() && (ipSampleDrafts.isEmpty() || ipSampleDrafts.size() == 1)){//first time around and either no ipSamples or one ipSample -- unable to make any ip replicates
			return nextPage(jobDraft);
		}
		
		
		m.put("replicatesListOfLists", replicatesListOfLists);//replicate sets already in db
		m.put("ipSamples", ipSampleDrafts);//for dropdown box
		m.put("jobDraft", jobDraft);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
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
		//for testing only
		for (String key : params.keySet()) {
			System.out.println("Key : " + key.toString());
			String[] stringArray = params.get(key);
			
			for(String s : stringArray){
				System.out.println("--val: " + s);
			}
		}
		
		if(params.containsKey("continueToNextPage")){
	    	return nextPage(jobDraft);
	    }

    	if(params.containsKey("ipIdForNewReplicateSet")){
    		String[] stringArray = params.get("ipIdForNewReplicateSet");
    		String ipId = stringArray[0];
    		System.out.println("--------ipIdForNewReplicateSet = " + ipId);
    		SampleDraft sampleDraftToBeSavedToNewReplicateSet = sampleDraftDao.getSampleDraftBySampleDraftId(Integer.parseInt(ipId));
    		System.out.println("--------sampleDraftToBeSavedToNewReplicateSet ID  = " + sampleDraftToBeSavedToNewReplicateSet.getId().toString());
    		jobDraftService.saveReplicateSets(jobDraft, sampleDraftToBeSavedToNewReplicateSet, null);
    		return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
    	}
    	
	    String paramPrefix = "ipIdForExistingReplicateSet_";
	    String completeKey = "";
    	for (String key : params.keySet()) {
    		if(key.startsWith(paramPrefix)){
    			completeKey = key;
    			break;
    		}
		}
    	if( !completeKey.isEmpty() ){
    		String[] stringArray = params.get(completeKey);
    		String replicateSetNumberAsString = completeKey.replaceFirst(paramPrefix, "");//so ipIdForExistingReplicateSet__1 is converted to 1
    		System.out.println("replicateSetNumberAsString: " + replicateSetNumberAsString);
    		Integer replicateSetNumberAsInteger = null;
    		try{
    			replicateSetNumberAsInteger = Integer.parseInt(replicateSetNumberAsString);
    		}catch(Exception e){
    			waspErrorMessage("wasp.unexpected_error.error");
    			return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
    		} 
    		String ipId = stringArray[0];
    		System.out.println("--------ipIdForExistingReplicateSet = " + ipId);
    		SampleDraft sampleDraftToBeSavedToExistingReplicateSet = sampleDraftDao.getSampleDraftBySampleDraftId(Integer.parseInt(ipId));
    		System.out.println("--------sampleDraftToBeSavedToExistingReplicateSet ID  = " + sampleDraftToBeSavedToExistingReplicateSet.getId().toString());
    		jobDraftService.saveReplicateSets(jobDraft, sampleDraftToBeSavedToExistingReplicateSet, replicateSetNumberAsInteger);
    		//success message??
    		System.out.println("GOT HERE SO LOOKS OK");
    		return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
    	}
    	
    	
	    waspErrorMessage("wasp.unexpected_error.error");
    	return "redirect:/jobsubmit/chipSeq/replicates/"+jobDraftId+".do";
	}
}

