package edu.yu.einstein.wasp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.service.JobDraftMetaService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.SampleDraftService;

@Controller
@Transactional
@RequestMapping("/jobsubmit/chipSeq")
public class ChipSeqJobSubmissionController extends JobSubmissionController {

	@Autowired
	protected JobDraftService jobDraftService;

	@Autowired
	protected SampleDraftService sampleDraftService;
	
	@Autowired
	protected JobDraftMetaService jobDraftMetaService;

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipSeqPairForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";


		List<SampleDraft> samples=sampleDraftService.getSampleDraftByJobId(jobDraftId);
		Set<String> selectedSamplePairs = new HashSet<String>();
		String samplePairsKey = jobDraft.getWorkflow().getIName()+".samplePairsTvsC";
		JobDraftMeta samplePairsTvsC = jobDraftMetaService.getJobDraftMetaByKJobdraftId(samplePairsKey, jobDraftId);
		if (samplePairsTvsC.getJobDraftMetaId() != null){
			for(String pair: samplePairsTvsC.getV().split(";")){
				String[] pairList = pair.split(":");
				selectedSamplePairs.add("testVsControl_"+pairList[0]+"_"+pairList[1]);
			}
		}

		m.put("jobDraft", jobDraft);
		m.put("samples", samples);
		m.put("selectedSamplePairs", selectedSamplePairs);

		return "jobsubmit/chipseqform";
	}

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateChipSeqPair(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {

		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
	
	    Map<String,Object> params = request.getParameterMap();
	
	    JobDraft jobDraftDb = jobDraftService.getJobDraftByJobDraftId(jobDraftId);
	    List<SampleDraft> samples =  jobDraftDb.getSampleDraft();
	
	    String pairMetaString = ""; 
	    
	    // remove old paired sample for jobdraft
	    String samplePairsKey = jobDraft.getWorkflow().getIName()+".samplePairsTvsC";
		JobDraftMeta samplePairsTvsC = jobDraftMetaService.getJobDraftMetaByKJobdraftId(samplePairsKey, jobDraftId);
		if (samplePairsTvsC.getJobDraftMetaId() != null){
			jobDraftMetaService.remove(samplePairsTvsC);
			jobDraftMetaService.flush(samplePairsTvsC);
		}
		
	    for (SampleDraft sd1: samples) {
	    	String sd1Id = String.valueOf(sd1.getSampleDraftId().intValue());
	    	for (SampleDraft sd2: samples) {
	    		String sd2Id = String.valueOf(sd2.getSampleDraftId().intValue());
	    		if (sd1Id.equals(sd2Id))
	    			continue;
	    		String checkValue = "";
	    		try {
	    			checkValue = ((String[])params.get("testVsControl_" + sd1Id + "_" + sd2Id))[0];
	    		} catch (Exception e) {
	    		}
		
	    		if (checkValue.equals("1")){
	    			pairMetaString += sd1Id + ":" + sd2Id + ";";
	    		}
	    	}
	    }
	
	    if (!pairMetaString.isEmpty()){
		    // persist pair meta string
		    JobDraftMeta jdm = new JobDraftMeta(); 
		    jdm.setJobdraftId(jobDraftDb.getJobDraftId());
		    jdm.setK(samplePairsKey);
		    jdm.setV(pairMetaString); 
		    jobDraftMetaService.save(jdm);
	    }
	
	    return nextPage(jobDraftDb);
  }


}

