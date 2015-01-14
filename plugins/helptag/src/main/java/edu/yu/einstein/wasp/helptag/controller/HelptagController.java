/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.controller;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.helptag.service.HelptagService;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;


@Controller
@RequestMapping("/helptag")
public class HelptagController extends WaspController {
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private HelptagService helptagService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private SampleService sampleService;
	
	@RequestMapping(value="/displayDescription", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + helptagService.performAction());
		return "helptag/description";
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
			  if(!sample.getSampleType().getIName().equalsIgnoreCase("library")){//macromolecule
				  String glycosylationStatusBeforeSubmission = helptagService.getGlycosylationStatusBeforeSubmission(sample);
				  if(glycosylationStatusBeforeSubmission==null) glycosylationStatusBeforeSubmission = "not found";
				  String restrictionStatusBeforeSubmission = helptagService.getRestrictionStatusBeforeSubmission(sample);
				  if(restrictionStatusBeforeSubmission==null) restrictionStatusBeforeSubmission = "not found";
				  String helpLibraryToMakeFromMacromolecule = helptagService.getHelpLibraryToMakeFromMacromolecule(sample);
				  if(helpLibraryToMakeFromMacromolecule==null) helpLibraryToMakeFromMacromolecule = "not found";
				  Map<String,String> map = new LinkedHashMap<String,String>();
				  
				  map.put("Initial Glycosylation Status", glycosylationStatusBeforeSubmission);
				  map.put("Initial Restriction Status", restrictionStatusBeforeSubmission);
				  map.put("Library To Create" , helpLibraryToMakeFromMacromolecule);
				  try{
						outputJSON(map, response);
					}catch(Exception e){}
				  }  
		  }
	}
	  
	  
}
