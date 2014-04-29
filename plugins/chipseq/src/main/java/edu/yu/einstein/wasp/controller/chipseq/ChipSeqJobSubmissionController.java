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
			if(foundInputOrIP == false){//unexpected, but....., then put on both lists (for consistency with previous method of pairing all to all)
				inputSampleDrafts.add(sampleDraft);
				ipSampleDrafts.add(sampleDraft);
			}
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

		m.put("jobDraft", jobDraft);
		m.put("samples", sampleDrafts);
		m.put("inputSamples", inputSampleDrafts);
		m.put("ipSamples", ipSampleDrafts);
		m.put("sampleOrganismMap", sampleDraftOrganismMap);
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
	}


}

