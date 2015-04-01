package edu.yu.einstein.wasp.controller.chipseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.chipseq.service.ChipSeqService;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;

@Controller
@RequestMapping("/chipseq")
public class ChipSeqController extends WaspController {

	@Autowired
	JobService jobService;
	@Autowired
	SampleService sampleService;
	@Autowired
	ChipSeqService chipseqService;
	@Autowired
	private MessageServiceWebapp messageService;

	@RequestMapping(value="/description", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		return "chipseq/description";
	}
/*	next method no longer used (as of 3-30-15; dubin)
	@RequestMapping(value="/{jobId}/{sampleId}/plugInSpecificSampleDataForDisplay", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	public void plugInSpecificSampleDataForDisplay(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("sampleId") Integer sampleId, 
			  HttpServletResponse response ) {
	      
		////* *********THIS IS AN AJAX CALL************* //* /
		
		  Job job = jobService.getJobByJobId(jobId);
		  Sample sample = null;
		  for(Sample s : job.getSample()){
			  if(s.getId().intValue()==sampleId.intValue()){
				  sample = s;
				  break;
			  }
		  }
		  if(sample != null){
			  String inputOrIPStatus = chipseqService.getInputOrIPStatus(sample);
			  if(inputOrIPStatus!=null && !inputOrIPStatus.isEmpty()){
				  Map<String,String> map = new LinkedHashMap<String,String>();
				  map.put(messageService.getMessage("chipseq.inputOrIPStatus.label"), inputOrIPStatus);
				  try{
					  outputJSON(map, response);
				  }catch(Exception e){}  
			  }
		  }
	}

	@RequestMapping(value="/{jobId}/plugInSpecificSamplePairingDataForDisplay", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	public String plugInSpecificSamplePairingDataForDisplay(
			  @PathVariable("jobId") Integer jobId, 
			  ModelMap m ) {
	      
		////* *********THIS IS AN AJAX CALL************* //* /
		
		Job job = jobService.getJobByJobId(jobId);
		List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
		List<Sample> controlList = new ArrayList<Sample>();
		//m.addAttribute("submittedSamplesList", submittedSamplesList);
		Map<Sample, List<Sample>> samplePairsMap = new HashMap<Sample, List<Sample>>();
		Set<SampleSource> sampleSourceSet = sampleService.getSamplePairsByJob(job);
		for(Sample submittedSample : submittedSamplesList){
			List<Sample> list = new ArrayList<Sample>();
			for(SampleSource ss : sampleSourceSet){
				Sample test = ss.getSample();//test is an IP sample
				Sample control = ss.getSourceSample();
				//logger.debug("----control = " + control.getName() + " AND test = " + test.getName());
				if(submittedSample == control){
					list.add(test);
				}
			}
			if(!list.isEmpty()){
				samplePairsMap.put(submittedSample, list);
				controlList.add(submittedSample);
			}
		}
		m.addAttribute("samplePairsMap", samplePairsMap);
		m.addAttribute("controlList", controlList);
		
		return "chipseq/chipseqSpecificSamplePairingPostSubmission";
	}
*/
}
