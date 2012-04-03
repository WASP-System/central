package edu.yu.einstein.wasp.helptag;

import edu.yu.einstein.wasp.controller.JobSubmissionController; 

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
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;

@Controller
@Transactional
@RequestMapping("/jobsubmit/helpTag")
public class HelpTagJobSubmissionController extends JobSubmissionController {

	@Autowired
	protected JobDraftDao jobDraftDao;

	@Autowired
	protected SampleDraftDao sampleDraftDao;
	
	@Autowired
	protected JobDraftMetaDao jobDraftMetaDao;

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showHelpTagPairForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";


		List<SampleDraft> samples=sampleDraftDao.getSampleDraftByJobId(jobDraftId);
		if (samples.size() < 2){
			return nextPage(jobDraft);
		}

		Set<String> selectedSamplePairs = new HashSet<String>();
		String samplePairsKey = jobDraft.getWorkflow().getIName()+".samplePairsTvsC";
		JobDraftMeta samplePairsTvsC = jobDraftMetaDao.getJobDraftMetaByKJobdraftId(samplePairsKey, jobDraftId);
		if (samplePairsTvsC.getJobDraftMetaId() != null){
			for(String pair: samplePairsTvsC.getV().split(";")){
				String[] pairList = pair.split(":");
				selectedSamplePairs.add("testVsControl_"+pairList[0]+"_"+pairList[1]);
			}
		}

		m.put("jobDraft", jobDraft);
		m.put("samples", samples);
		m.put("selectedSamplePairs", selectedSamplePairs);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		return "jobsubmit/helptagform";
	}

	@RequestMapping(value="/pair/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateHelpTagPair(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {

		JobDraft jobDraft = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
	
	    Map<String,String[]> params = request.getParameterMap();
	
	    JobDraft jobDraftDb = jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
	    List<SampleDraft> samples =  jobDraftDb.getSampleDraft();
	
	    String pairMetaString = ""; 
	    
	    // remove old paired sample for jobdraft
	    String samplePairsKey = jobDraft.getWorkflow().getIName()+".samplePairsTvsC";
		JobDraftMeta samplePairsTvsC = jobDraftMetaDao.getJobDraftMetaByKJobdraftId(samplePairsKey, jobDraftId);
		if (samplePairsTvsC.getJobDraftMetaId() != null){
			jobDraftMetaDao.remove(samplePairsTvsC);
			jobDraftMetaDao.flush(samplePairsTvsC);
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
			jobDraftMetaDao.save(jdm);
		}
	
		return nextPage(jobDraftDb);
	}


}

