/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.helptag.controller;

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
	  public @ResponseBody String plugInSpecificSampleDataForDisplay(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId ) {
	      
		  Job job = jobService.getJobByJobId(jobId);
		  Sample sample = null;
		  for(Sample s : job.getSample()){
			  if(s.getId().intValue()==sampleId.intValue()){
				  sample = s;
				  break;
			  }
		  }
		  if(sample != null){
			  String glycosylationStatusBeforeSubmission = helptagService.getGlycosylationStatusBeforeSubmission(sample);
			  String restrictionStatusBeforeSubmission = helptagService.getRestrictionStatusBeforeSubmission(sample);
			  String helpLibraryToMakeFromMacromolecule = helptagService.getHelpLibraryToMakeFromMacromolecule(sample);
			  
			  StringBuilder sb = new StringBuilder();			
			  sb.append("Initial Glycosylation Status: " + glycosylationStatusBeforeSubmission + "<br />");
			  sb.append("Initial Restriction Status: " + restrictionStatusBeforeSubmission + "<br />");
			  sb.append("Library To Create: " + helpLibraryToMakeFromMacromolecule + "<br />");
			  return new String(sb);
		  }
		  return "";
		  /*
		  Map<String, String> queryMap = new HashMap<String, String>();
		  queryMap.put("sampleTypeCategory.iName", "biomaterial");
		  List<String> orderByColumnNames = new ArrayList<String>();
		  orderByColumnNames.add("name");
		  String direction = "asc";
		  List<SampleType> sampleTypeList = sampleTypeDao.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, direction);
		 
	      StringBuilder sb = new StringBuilder();
	      sb.append("{\"source\": [");
	      int counter = 0;
	      for (SampleType st : sampleTypeList){
	    	  if(counter++ > 0){
	    		  sb.append(",");
	    	  }
	    	  sb.append("{\"label\": \""+st.getName()+"\", \"value\":\""+st.getIName()+"\"}");
	      }
	      sb.append("]}");
	      
	      String jsonOutput = new String(sb);
	      logger.debug("jsonOutput: " + jsonOutput);
	      
	      return jsonOutput; 
	      */
		  //return "<h2>helpSpecific info test</h2>";
		  
	  }
	  
	  
}
