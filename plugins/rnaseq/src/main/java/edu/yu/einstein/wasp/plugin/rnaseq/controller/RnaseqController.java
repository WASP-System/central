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
	/*	next  method no longer used (as of 3-30-15; dubin)
	@RequestMapping(value="/{jobId}/{sampleId}/plugInSpecificSampleDataForDisplay", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	public void plugInSpecificSampleDataForDisplay(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("sampleId") Integer sampleId, 
			  HttpServletResponse response ) {
	      
		///* *********THIS IS AN AJAX CALL************* * /
		
		  Job job = jobService.getJobByJobId(jobId);
		  Sample sample = null;
		  for(Sample s : job.getSample()){
			  if(s.getId().intValue()==sampleId.intValue()){
				  sample = s;
				  break;
			  }
		  }
		  if(sample != null){
			  try{
				  logger.debug("dubin - 2-9-15 sample " + sample.getName());
				  logger.debug("dubin - 2-9-15 and isRNALibraryDirectional returned: " + rnaseqService.isRNALibraryDirectional(sample));
			  }
			  catch(Exception e){logger.debug("dubin - 2-9-15 and excpetion: " + e.getMessage());}
			  
			  Map<String,String> map = new LinkedHashMap<String,String>();
			  if(sample.getSampleType().getIName().toLowerCase().equals("rna")){
				  String rnaFraction = rnaseqService.getRNAFraction(sample);
				  if(rnaFraction!=null && !rnaFraction.isEmpty()){
					  map.put(messageService.getMessage("rnaseq.fraction.label"), rnaFraction);	
				  }
				  String rnaDirectionality = rnaseqService.getDirectionality(sample);
				  if(rnaDirectionality!=null && !rnaDirectionality.isEmpty()){
					  map.put(messageService.getMessage("rnaseq.requestedDirectionality.label"), rnaDirectionality);
				  }
			  }
			  if(sample.getSampleType().getIName().toLowerCase().equals("cdna")){
				  String ribosomeDepleteionMethod = rnaseqService.getRibosomeDepletionMethod(sample);
				  if(ribosomeDepleteionMethod!=null && !ribosomeDepleteionMethod.isEmpty()){
					  map.put(messageService.getMessage("rnaseq.ribosomeDepletion.label"), ribosomeDepleteionMethod);	
				  }
				  String rnaDirectionality = rnaseqService.getDirectionality(sample);
				  if(rnaDirectionality!=null && !rnaDirectionality.isEmpty()){
					  map.put(messageService.getMessage("rnaseq.directionality2.label"), rnaDirectionality);
				  }
			  }
			  if(sample.getSampleType().getIName().toLowerCase().endsWith("library")){//user-submitted and facility library  
				  String ribosomeDepleteionMethod = rnaseqService.getRibosomeDepletionMethod(sample);
				  if(ribosomeDepleteionMethod!=null && !ribosomeDepleteionMethod.isEmpty()){
					  map.put(messageService.getMessage("rnaseq.ribosomeDepletion.label"), ribosomeDepleteionMethod);	
				  }
				  String rnaDirectionality = rnaseqService.getDirectionality(sample);
				  if(rnaDirectionality!=null && !rnaDirectionality.isEmpty()){
					  map.put(messageService.getMessage("rnaseq.directionality2.label"), rnaDirectionality);	
				  }
			  }
			  if(!map.isEmpty()){
				  try{
					  outputJSON(map, response);
				  }catch(Exception e){}
			  }
		  }
	}
*/
}
