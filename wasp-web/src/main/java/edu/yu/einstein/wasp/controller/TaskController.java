package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

@Controller
@Transactional
@RequestMapping("/task")
public class TaskController extends WaspController {

   @Autowired
  private SampleDao sampleDao;
  
  @Autowired
  private JobDao jobDao;

  @Autowired
  private JobService jobService;

  @Autowired
  private SampleService sampleService;
  

  @RequestMapping(value = "/assignLibraries/lists", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String assignLibrariesLists(ModelMap m) {

	  List<Job> jobsWithLibrariesToGoOnFlowCell = jobService.getJobsWithLibrariesToGoOnFlowCell();
	  List<Job> jobsActive = jobService.getActiveJobs();
	    
	  List<Job> jobsActiveAndWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
	  for(Job jobActive : jobsActive){
		  for(Job jobAwaiting : jobsWithLibrariesToGoOnFlowCell){
			  if(jobActive.getJobId().intValue()==jobAwaiting.getJobId().intValue()){
	    			jobsActiveAndWithLibrariesToGoOnFlowCell.add(jobActive);
	    			break;
	    	  }
	      }
	  }
	  jobService.sortJobsByJobId(jobsActiveAndWithLibrariesToGoOnFlowCell);	  
	  m.addAttribute("jobList", jobsActiveAndWithLibrariesToGoOnFlowCell);
	  
	  List<Sample> activePlatformUnits = sampleService.platformUnitsAwaitingLibraries();
	  sampleService.sortSamplesBySampleName(activePlatformUnits);	  
	  m.addAttribute("activePlatformUnits", activePlatformUnits);
	  
	  List<String> barcodes = new ArrayList<String>();
	  List<String> lanes = new ArrayList<String>();
	  for(Sample platformUnit : activePlatformUnits){
		  String barcode = platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode();
		  if(barcode==null || barcode.equals("")){
			  barcode = new String("Unknown");
		  }
		  barcodes.add(barcode);
		  String lane = new String("");
		  List<SampleMeta> sampleMetaList = platformUnit.getSampleMeta();
		  for(SampleMeta sampleMeta : sampleMetaList){
			  if(sampleMeta.getK().indexOf("lanecount") > -1){
				  lane = sampleMeta.getV();
			  }
		  }
		  if(lane.equals("")){
			  lane = new String("Unknown");
		  }
		  lanes.add(lane);
	  }
	  
	  m.addAttribute("barcodes", barcodes);
	  m.addAttribute("lanes", lanes);
	 
	  return "task/assignLibraries/lists";
  }
  
  @RequestMapping(value = "/samplereceive/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String listSampleReceive(ModelMap m) {

    List<Job> jobsAwaitingReceivingOfSamples = jobService.getJobsAwaitingReceivingOfSamples();
    List<Job> jobsActive = jobService.getActiveJobs();
    
    List<Job> jobsActiveAndAwaitingSubmittedSamples = new ArrayList<Job>();
    for(Job jobActive : jobsActive){
    	for(Job jobAwaiting : jobsAwaitingReceivingOfSamples){
    		if(jobActive.getJobId().intValue()==jobAwaiting.getJobId().intValue()){
    			jobsActiveAndAwaitingSubmittedSamples.add(jobActive);
    			break;
    		}
    	}
    }
    jobService.sortJobsByJobId(jobsActiveAndAwaitingSubmittedSamples);

    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingSubmittedSamples){
    	List<Sample> newSampleList = jobService.getSubmittedSamplesNotYetReceived(job);
    	sampleService.sortSamplesBySampleName(newSampleList);    	
    	jobAndSampleMap.put(job, newSampleList);
    }
    
    if(jobsActiveAndAwaitingSubmittedSamples.size() != jobAndSampleMap.size()){
    	//message and get out of here
    }
    
    m.addAttribute("jobList", jobsActiveAndAwaitingSubmittedSamples);
    m.addAttribute("jobAndSamplesMap", jobAndSampleMap);
    
    return "task/samplereceive/list";
  }

  @RequestMapping(value = "/samplereceive/receive", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String payment(
      @RequestParam("sampleId") Integer sampleId,
      @RequestParam("receivedStatus") String receivedStatus,
      ModelMap m
    ) {

	  if(receivedStatus == null ||  receivedStatus.equals("")){
		  waspErrorMessage("task.samplereceive.error_receivedstatus_empty");
	  }
	  else if(!receivedStatus.equals("RECEIVED") && !receivedStatus.equals("WITHDRAWN")){
		  waspErrorMessage("task.samplereceive.error_receivedstatus_invalid");
	  }
	  Sample sample = sampleDao.getSampleBySampleId(sampleId);
	  if(sample.getSampleId().intValue()==0){
		  //message can't find sample in db
	  }
	  else{
		  sampleService.updateSampleReceiveStatus(sample, receivedStatus);
		  waspMessage("task.samplereceive.update_success");	
	  	 }
	  return "redirect:/task/samplereceive/list.do";
  }

  
  @RequestMapping(value = "/updatesamplereceive/{jobId}", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String updateSampleReceive(@PathVariable("jobId") Integer jobId, ModelMap m) {

	  Job job = jobDao.getJobByJobId(jobId);
	  if(job.getJobId()==0){
		  //message can't find job by id
		  return "redirect:/dashboard.do";
	  }
	  
	  //getSubmittedSamples(job) returns list of all samples (macromolecules and user-generated libraries) 
	  //that were submitted for a particular job and it does NOT include facility-generated libraries.
	  List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
	  //order by sample name
	  sampleService.sortSamplesBySampleName(submittedSamplesList);
	  
	  List<String> receiveSampleStatusList = new ArrayList<String>();
	  List<Boolean> sampleHasBeenProcessedList = new ArrayList<Boolean>();
	  for(Sample sample : submittedSamplesList){	
		  String status = sampleService.getReceiveSampleStatus(sample);		  
		  receiveSampleStatusList.add(sampleService.convertReceiveSampleStatusForWeb(status));	
		  
		  boolean sampleHasBeenProcessedByFacility = sampleService.submittedSampleHasBeenProcessedByFacility(sample);
		  sampleHasBeenProcessedList.add(new Boolean(sampleHasBeenProcessedByFacility));
	  }
	  
	  if(submittedSamplesList.size() != receiveSampleStatusList.size() && receiveSampleStatusList.size() != sampleHasBeenProcessedList.size()){
		  //message
		  return "redirect:/dashboard.do";
	  }
	  
	  
	  //list of options for the SELECT box on the web page
	  List<String> receiveSampleOptionsList = sampleService.getReceiveSampleStatusOptionsForWeb();
	  
	  m.addAttribute("job", job);
	  m.addAttribute("submittedSamplesList", submittedSamplesList);
	  m.addAttribute("receiveSampleStatusList", receiveSampleStatusList);
	  m.addAttribute("sampleHasBeenProcessedList", sampleHasBeenProcessedList);
	  m.addAttribute("receiveSampleOptionsList", receiveSampleOptionsList);	  
	  
	  return "task/samplereceive/updateSampleReceive";
  }

  @RequestMapping(value = "/updatesamplereceive", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String updateSampleReceive(@RequestParam("jobId") Integer jobId, 
		  @RequestParam("sampleId") List<Integer> sampleIdList,
	      @RequestParam("receivedStatus") List<String> receivedStatusList,
	      ModelMap m) {

	  Job job = jobDao.getJobByJobId(jobId);
	  if(job.getJobId()==0){
		  //message can't find job by id
		  return "redirect:/dashboard.do";
	  }
	  if(sampleIdList.size() != receivedStatusList.size()){
		  //message
		  return "redirect:/dashboard.do";
	  }
	  int index = 0;
	  for(Integer sampleId : sampleIdList){		  
		  Sample sample = sampleDao.getSampleBySampleId(sampleId);
		  if(sample.getSampleId() > 0 && ! sampleService.submittedSampleHasBeenProcessedByFacility(sample)){
			  sampleService.updateSampleReceiveStatus(sample, receivedStatusList.get(index++));
		  }
	  }
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  
  }
  
  	public class OrderStatesByJobIdComparator implements Comparator<State> {
		 
  		@Override
  		public int compare(State s1, State s2) {

  			try{
  			if(s1.getStatejob().get(0).getJobId().intValue() > s2.getStatejob().get(0).getJobId().intValue()){
  				return 1;
  			}
  			else if(s1.getStatejob().get(0).getJobId().intValue() == s2.getStatejob().get(0).getJobId().intValue()){
  				return 0;
  			}
  			else{
  				return -1;
  			}
  			}
  			catch(Exception e){
  				System.out.println("1. My Comparator Exception: ");// + s1.getStatejob().get(0).getJobId().intValue() + "  and " + s2.getStatejob().get(0).getJobId().intValue());
  				return 0;
  			}
  		}
	}
  
  
  
}

