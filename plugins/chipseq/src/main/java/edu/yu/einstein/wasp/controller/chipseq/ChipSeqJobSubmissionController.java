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
import org.springframework.web.bind.annotation.RequestParam;

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
		//10-29-14 added additional check to make certain species match for each pair (and no species in a pair is species "Other").
		
		List<SampleDraft> ipSampleDrafts = new ArrayList<SampleDraft>();
		List<SampleDraft> inputSampleDrafts = new ArrayList<SampleDraft>();
		Map<SampleDraft,Integer> sampleDraftOrganismIdMap = new HashMap<SampleDraft, Integer>();
		
		List<SampleDraft> sampleDrafts=jobDraft.getSampleDraft();
		for(SampleDraft sampleDraft : sampleDrafts){			
			for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){				
				if(sampleDraftMeta.getK().endsWith("inputOrIP")){
					if(sampleDraftMeta.getV().equals("ip")){
						ipSampleDrafts.add(sampleDraft);	
					}
					else if(sampleDraftMeta.getV().equals("input")){
						inputSampleDrafts.add(sampleDraft);
					}
				}
				if(sampleDraftMeta.getK().endsWith("organism")){
					Integer genomeId = Integer.valueOf(sampleDraftMeta.getV());
					sampleDraftOrganismIdMap.put(sampleDraft, genomeId);
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
						logger.debug("--type mismatch");
						mapsContainingTheSampleDraftToBeRemoved.add(pair);						
					}
					else if(sampleDraftOrganismIdMap.get(ipInSamplePair)==null || sampleDraftOrganismIdMap.get(inputInSamplePair)==null 
						|| sampleDraftOrganismIdMap.get(ipInSamplePair).intValue()==0 || sampleDraftOrganismIdMap.get(inputInSamplePair).intValue()==0
						|| sampleDraftOrganismIdMap.get(ipInSamplePair).intValue() != sampleDraftOrganismIdMap.get(inputInSamplePair).intValue()){
						//no species recorded for at least one of the pair
						//species 'other' recorded for at least one of the pair
						//species mismatch
						logger.debug("--species mismatch");
						mapsContainingTheSampleDraftToBeRemoved.add(pair);
					}
				}
			}
		}
		sampleDraftPairSet.removeAll(mapsContainingTheSampleDraftToBeRemoved);
		jobDraftService.setSampleDraftPairsByJobDraft(jobDraft, sampleDraftPairSet);//reset
	}
	
	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipSeqPairForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		this.confirmContinuedValidityOfSamplePairsAndAdjustAsNeeded(jobDraft);

		List<SampleDraft> inputSampleDrafts = new ArrayList<SampleDraft>();
		List<SampleDraft> ipSampleDrafts = new ArrayList<SampleDraft>();
		Map<SampleDraft, Integer> sampleDraftOrganismMap = new HashMap<SampleDraft, Integer>();
		Map<SampleDraft, String> sampleDraftSpeciesNameMap = new HashMap<SampleDraft, String>();
		
		List<SampleDraft> sampleDrafts=sampleDraftDao.getSampleDraftByJobId(jobDraftId);
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
			//not used, but could be
			//if(foundInputOrIP == false){//unexpected, but....., then put on both lists (for consistency with previous method of pairing all to all)
			//	inputSampleDrafts.add(sampleDraft);
			//	ipSampleDrafts.add(sampleDraft);
			//}
		}		
		
		//10-28-14
		Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		List<Map<SampleDraft, SampleDraft>> existingSampleDraftPairList = new ArrayList<Map<SampleDraft, SampleDraft>>(sampleDraftPairSet);
		Map<SampleDraft, List<SampleDraft>> alreadyRecordedIPInputListMap = new HashMap<SampleDraft, List<SampleDraft>>(); 
		for(Map<SampleDraft, SampleDraft> existingSamplePairMap : existingSampleDraftPairList){
			Entry<SampleDraft, SampleDraft> e = existingSamplePairMap.entrySet().iterator().next();
			SampleDraft ip = e.getKey();
			SampleDraft input = e.getValue();
			if(alreadyRecordedIPInputListMap.containsKey(ip)){
				alreadyRecordedIPInputListMap.get(ip).add(input);
			}
			else{
				List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
				sampleDraftList.add(input);
				alreadyRecordedIPInputListMap.put(ip, sampleDraftList);
			}
		}
		
		m.put("jobDraft", jobDraft);
		m.put("inputSamples", inputSampleDrafts);
		m.put("ipSamples", ipSampleDrafts);
		m.put("sampleSpeciesNameMap", sampleDraftSpeciesNameMap);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));//not really used
		m.put("nextPage", nextPage(jobDraft).replaceFirst("redirect:/", ""));
		m.put("alreadyRecordedIPInputListMap", alreadyRecordedIPInputListMap);
		
		return "jobsubmit/chipseqform";
	}

	@RequestMapping(value="/pair/{jobDraftId}/{ipSampleDraftId}/{inputSampleDraftId}/remove.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String removeChipSeqPair(@PathVariable("jobDraftId") Integer jobDraftId, 
			@PathVariable("ipSampleDraftId") Integer ipSampleDraftId,
			@PathVariable("inputSampleDraftId") Integer inputSampleDraftId,
			ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		SampleDraft ipSampleDraft = sampleService.getSampleDraftDao().getSampleDraftBySampleDraftId(ipSampleDraftId);
		SampleDraft inputSampleDraft = sampleService.getSampleDraftDao().getSampleDraftBySampleDraftId(inputSampleDraftId);
		if(ipSampleDraft.getId()==null || ipSampleDraft.getId()==0 || inputSampleDraft.getId()==null || inputSampleDraft.getId()==0){
			//error Message
			logger.debug("dubin - unexpectedly unable to find ip and/or input sample in database");
			waspErrorMessage("chipSeq.pair_unexpectedly_not_recorded_in_database.error");
			return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
		}
		Set<Map<SampleDraft, SampleDraft>> existingSampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		List<Map<SampleDraft, SampleDraft>> existingSampleDraftPairList = new ArrayList<Map<SampleDraft, SampleDraft>>(existingSampleDraftPairSet);
		//appears that test is first (key), control is second (value) in existingSampleDraftPairSet		
		boolean sampleDraftPairAlreadyRecordedInDatabase = false;
		for(Map<SampleDraft, SampleDraft> map : existingSampleDraftPairList){
			if(map.containsKey(ipSampleDraft)){
				SampleDraft inputInDB = map.get(ipSampleDraft);
				if(inputInDB.getId().intValue()==inputSampleDraft.getId().intValue()){//already recorded
					logger.debug("dubin - pair found _in_database");
					sampleDraftPairAlreadyRecordedInDatabase=true;
					break;
				}
			}
		}
		if(sampleDraftPairAlreadyRecordedInDatabase==false){
			logger.debug("dubin - pair NOT found _in_database; cannot remove");
			waspErrorMessage("chipSeq.unexpectedly_pair_not_recorded_in_database.error");
		}
		else{
			Map<SampleDraft, SampleDraft> existingSampleDraftPairToBeRemoved = new HashMap<SampleDraft, SampleDraft>();
			existingSampleDraftPairToBeRemoved.put(ipSampleDraft, inputSampleDraft);
			existingSampleDraftPairSet.remove(existingSampleDraftPairToBeRemoved);
			jobDraftService.setSampleDraftPairsByJobDraft(jobDraft, existingSampleDraftPairSet);
			waspMessage("chipSeq.pair_removed_from_database.label");
		}
		return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
	}
	
	@RequestMapping(value="/pair/{jobDraftId}/removeAll.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String removeAllChipSeqPair(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		Set<Map<SampleDraft, SampleDraft>> emptySampleDraftPairSet = new HashSet<Map<SampleDraft, SampleDraft>>();			
		jobDraftService.setSampleDraftPairsByJobDraft(jobDraft, emptySampleDraftPairSet);
		waspMessage("chipSeq.pair_all_removed_from_database.label");
		return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
	}
	
	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateChipSeqPair(@PathVariable("jobDraftId") Integer jobDraftId, 
			@RequestParam(value="ipSampleDraftId[]", required=false) String[] ipSampleDraftIdArray,
			@RequestParam(value="inputSampleDraftId[]", required=false) String[] inputSampleDraftIdArray,
			@RequestParam(value="theContinueButton", required=false) String theContinueButton,
			ModelMap m) {

		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		
		if( (theContinueButton!=null && ipSampleDraftIdArray!=null && ipSampleDraftIdArray.length!=0 && ipSampleDraftIdArray[0].equals("0") && inputSampleDraftIdArray==null)
			||
			(theContinueButton!=null && ipSampleDraftIdArray==null && inputSampleDraftIdArray!=null && inputSampleDraftIdArray.length!=0 && inputSampleDraftIdArray[0].equals("0")) ){
			
			//user hit continue button AND no selections were made in both the ip and input select boxes, so go to next page
			return nextPage(jobDraft);
		}
		else if(ipSampleDraftIdArray==null || ipSampleDraftIdArray.length==0 || ipSampleDraftIdArray[0].equals("0") 
			|| inputSampleDraftIdArray==null || inputSampleDraftIdArray.length==0 || inputSampleDraftIdArray[0].equals("0")){
			//if user hit continue button, since it's not absence of selection of both select boxes (which is dealt with in above if statement), 
			//it must be absence of selection of just one select box (and a selection in the other select), 
			//so assume they are trying to generate a samplepair, and thus give error message to make selection(s) and return to pair page 
			//OR ELSE 
			//the user hit the ADD button and there is no selection in one or both select boxes, so give error message to make selection(s) and return to pair page 
			waspErrorMessage("chipSeq.pair_missing_ip_or_control.error");
			return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
		}
		
		List<SampleDraft> ipSampleDraftList = new ArrayList<SampleDraft>();
		List<SampleDraft> inputSampleDraftList = new ArrayList<SampleDraft>();
		Map<SampleDraft,String> sampleDraftSpeciesIdAsStringMap = new HashMap<SampleDraft,String>();		
		
		if( ipSampleDraftIdArray.length==1 && ipSampleDraftIdArray[0].equals("-1") ){//all IPs requested			
			List<SampleDraft> allIpSampleDrafts = new ArrayList<SampleDraft>();
			for(SampleDraft sampleDraft : sampleDraftDao.getSampleDraftByJobId(jobDraftId)){
				for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){
					if(sampleDraftMeta.getK().endsWith("inputOrIP")){
						if(sampleDraftMeta.getV().equals("ip")){
							allIpSampleDrafts.add(sampleDraft);
						}
					}
				}
			}
			ipSampleDraftList.addAll(allIpSampleDrafts);
		}
		else{
			for(String ipIdAsString : ipSampleDraftIdArray){
				if(ipIdAsString.isEmpty() || "0".equals(ipIdAsString)){
					logger.debug("unexpected ipIdAsString entry");
					waspErrorMessage("wasp.unexpected_error.error");
					return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
				}
				try{ 
					Integer ipIdAsInteger = Integer.valueOf(ipIdAsString);
					ipSampleDraftList.add(sampleService.getSampleDraftDao().getSampleDraftBySampleDraftId(ipIdAsInteger));				
				}catch(NumberFormatException e){
					logger.debug("unexpected string to integer conversion error with an ip");
					waspErrorMessage("wasp.unexpected_error.error");
					return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
				}catch(Exception e){
					logger.debug("unexpectedly unable to find ip sample in database");
					waspErrorMessage("chipSeq.pair_unexpectedly_unable_to_locate_ip_or_input_in_database.error");
					return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
				}
			}
		}
		
		if( inputSampleDraftIdArray.length==1 && inputSampleDraftIdArray[0].equals("-1") ){//all Inputs requested			
			List<SampleDraft> allInputSampleDrafts = new ArrayList<SampleDraft>();
			for(SampleDraft sampleDraft : sampleDraftDao.getSampleDraftByJobId(jobDraftId)){
				for(SampleDraftMeta sampleDraftMeta : sampleDraft.getSampleDraftMeta()){
					if(sampleDraftMeta.getK().endsWith("inputOrIP")){
						if(sampleDraftMeta.getV().equals("input")){
							allInputSampleDrafts.add(sampleDraft);
						}
					}
				}
			}
			inputSampleDraftList.addAll(allInputSampleDrafts);
		}
		else{
			for(String inputIdAsString : inputSampleDraftIdArray){
				if(inputIdAsString.isEmpty() || "0".equals(inputIdAsString)){
					logger.debug("unexpected inputIdAsString entry");
					waspErrorMessage("wasp.unexpected_error.error");
					return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
				}
				try{ 
					Integer inputIdAsInteger = Integer.valueOf(inputIdAsString);
					inputSampleDraftList.add(sampleService.getSampleDraftDao().getSampleDraftBySampleDraftId(inputIdAsInteger));
				}catch(NumberFormatException e){
					logger.debug("unexpected string to integer conversion error with an input");
					waspErrorMessage("wasp.unexpected_error.error");
					return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
				}catch(Exception e){
					logger.debug("unexpectedly unable to find input sample in database");
					waspErrorMessage("chipSeq.pair_unexpectedly_unable_to_locate_ip_or_input_in_database.error");
					return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
				}
			}
		}
		
		List<SampleDraft> allSampleDraftsForThisRequestList = new ArrayList<SampleDraft>();
		allSampleDraftsForThisRequestList.addAll(ipSampleDraftList);
		allSampleDraftsForThisRequestList.addAll(inputSampleDraftList);
		
		for(SampleDraft sd : allSampleDraftsForThisRequestList){
			String speciesIdAsString = "";
			for(SampleDraftMeta sdm : sd.getSampleDraftMeta()){
				if(sdm.getK().endsWith("organism")){
					speciesIdAsString = sdm.getV();
				}
			}
			if(speciesIdAsString.isEmpty()){//unexpectedly, no species to check
				logger.debug("dubin - species not found");
				waspErrorMessage("chipSeq.pair_species_not_found.error");
				return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
			}
			else if(speciesIdAsString.equals("0")){//unexpectedly, species other (these should never appear on the web page select list)
				logger.debug("dubin - species other not allowed");
				waspErrorMessage("chipSeq.pair_species_other.error");
				return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
			}
			else{
				sampleDraftSpeciesIdAsStringMap.put(sd, speciesIdAsString);
			}
		}
		
		Set<Map<SampleDraft, SampleDraft>> existingSampleDraftPairSet = jobDraftService.getSampleDraftPairsByJobDraft(jobDraft);
		//appears that test is first (key), control is second (value) in existingSampleDraftPairSet		
		Set<Map<SampleDraft, SampleDraft>> newSampleDraftPairSetToBeAdded = new HashSet<Map<SampleDraft, SampleDraft>>();
		for(SampleDraft ipSampleDraft : ipSampleDraftList){
			for(SampleDraft inputSampleDraft : inputSampleDraftList){
				if(sampleDraftSpeciesIdAsStringMap.get(ipSampleDraft).equals(sampleDraftSpeciesIdAsStringMap.get(inputSampleDraft))){
					Map<SampleDraft, SampleDraft> newEntry = new HashMap<SampleDraft, SampleDraft>();
					newEntry.put(ipSampleDraft, inputSampleDraft);
					if(!existingSampleDraftPairSet.contains(newEntry)){						
						newSampleDraftPairSetToBeAdded.add(newEntry);
					}
					else{
						if(ipSampleDraftList.size()==1 && inputSampleDraftList.size()==1){
							waspErrorMessage("chipSeq.pair_already_recorded_in_database.error");
						}
					}
				}
				else{
					if(ipSampleDraftList.size()==1 && inputSampleDraftList.size()==1){
						waspErrorMessage("chipSeq.pair_species_mismatch.error");
					}
				}
			}
		}
		if(newSampleDraftPairSetToBeAdded.isEmpty()){
			//nothing to add
			if(ipSampleDraftList.size()!=1 || inputSampleDraftList.size()!=1){
				waspErrorMessage("chipSeq.pair_no_new_pairs_recorded.label");
			}
		}
		else{
			existingSampleDraftPairSet.addAll(newSampleDraftPairSetToBeAdded);
			jobDraftService.setSampleDraftPairsByJobDraft(jobDraft, existingSampleDraftPairSet);
			if(ipSampleDraftList.size()==1 && inputSampleDraftList.size()==1){
				waspMessage("chipSeq.pair_ip_input_pair_recorded.label");//one pair
			}
			else{
				waspMessage("chipSeq.pair_ip_input_pairs_recorded.label");//pairs
			}
		}
		return "redirect:/jobsubmit/chipSeq/pair/" + jobDraftId + ".do";
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
					    //logger.debug("want species------------------------sampleDraft: " + sampleDraft.getName() + "   " + " species: " + speciesName);
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
		String peakType = "";//punctate, broad, mixed [as of 12-30-14, mixed is no longer an available option], none    (should be "punctate", or "broad" if IP; should be "none" if sample is input; [note that mixed is no longer an option if IP])
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

