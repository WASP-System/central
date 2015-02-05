/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.rnaseq.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.plugin.rnaseq.service.RnaseqService;


@Controller
@RequestMapping("/rnaseq")
public class RnaseqController extends WaspController {
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private RnaseqService rnaseqService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;

	@RequestMapping(value="/displayDescription", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + rnaseqService.performAction());
		return "rnaseq/description";
	}

	@RequestMapping(value="/{jobId}/{sampleId}/plugInSpecificSampleDataForDisplay", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	public void plugInSpecificSampleDataForDisplay(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("sampleId") Integer sampleId, 
			  HttpServletResponse response ) {
	      
		/* *********THIS IS AN AJAX CALL************* */
		
		  Job job = jobService.getJobByJobId(jobId);
		  Sample sample = null;
		  for(Sample s : job.getSample()){
			  if(s.getId().intValue()==sampleId.intValue()){
				  sample = s;
				  break;
			  }
		  }
		  if(sample != null){
			  Map<String,String> map = new LinkedHashMap<String,String>();
			  if(sample.getSampleType().getIName().toLowerCase().equals("dna")){//HELP macromolecule
				  //String typeOfHelpLibraryRequested = rnaseqService.getTypeOfHelpLibraryRequestedForMacromolecule(sample);
				  //if(typeOfHelpLibraryRequested!=null && !typeOfHelpLibraryRequested.isEmpty()){
				//	  map.put(messageService.getMessage("helptag.typeOfHelpLibraryRequested.label"), typeOfHelpLibraryRequested);					  
				 // } 
			  }
			  
			  if(!map.isEmpty()){
				  try{
					  outputJSON(map, response);
				  }catch(Exception e){}
			  }
		  }
	}
}
