package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
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
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
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

	  List<Job> jobsWithLibrariesToGoOnFlowCell = jobService.getJobsWithLibrariesToGoOnPlatformUnit();
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
	  
	  List<Sample> activePlatformUnits = sampleService.getAvailablePlatformUnits();
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

    List<Job> jobsActiveAndAwaitingSubmittedSamples = jobService.getJobsAwaitingReceivingOfSamples();
        
    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingSubmittedSamples){
    	logger.debug("processing samples for job with id='" + job.getJobId() + "'");
    	List<Sample> newSampleList = jobService.getSubmittedSamplesNotYetReceived(job);
    	for (Sample sample: newSampleList)
    		logger.debug("    .... sample: id='" + sample.getSampleId() + "'");
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
  public String recordSampleReceive(@RequestParam("sampleId") List<Integer> sampleIdList,
		  				ModelMap m) 
  {
	  List<String> receivedStatusList = new ArrayList<String>(); //Arrays.asList(receivedStatusArray);
	  for(Integer id : sampleIdList){
			String val = request.getParameter("receivedStatus" + id.toString());
			if(val == null){
				waspErrorMessage("task.samplereceive_receivedstatus_unexpected.error");
				return "redirect:/task/samplereceive/list.do";
			}
			receivedStatusList.add(val);
	  }	  
	  boolean atLeastOneSampleSelectedForUpdate = false;
	  for(String status: receivedStatusList){
		  if("RECEIVED".equals(status) || "WITHDRAWN".equals(status)){
			  atLeastOneSampleSelectedForUpdate = true;
			  break;
		  }
	  }
	  if(atLeastOneSampleSelectedForUpdate==false){
		  waspErrorMessage("task.samplereceive_receivedstatus_empty.error");
		  return "redirect:/task/samplereceive/list.do";
	  }
	  if(sampleIdList.size() != receivedStatusList.size()){
		  logger.debug(sampleIdList.size() + " != " + receivedStatusList.size());
		  waspErrorMessage("task.samplereceive_receivedstatus_unexpected.error");
		  return "redirect:/task/samplereceive/list.do";		  
	  }
	 	  
	  for(int i = 0; i < receivedStatusList.size(); i++){
		  if(!receivedStatusList.get(i).isEmpty()){
			  Sample sample = sampleDao.getSampleBySampleId(sampleIdList.get(i).intValue());
			  if(sample.getSampleId()<=0){
				  waspErrorMessage("task.samplereceive_receivedstatus_unexpected.error");
				  logger.warn("unable to find sampleId " + sampleIdList.get(i).intValue() + " in task/samplereceive/receive - POST");
				  return "redirect:/task/samplereceive/list.do";
			  }
			  try{
				  if(receivedStatusList.get(i).equals("RECEIVED")){	
					  logger.debug("Received Sample = " + sample.getName());
					  sampleService.updateSampleReceiveStatus(sample, WaspStatus.CREATED);
				  }
				  else if(receivedStatusList.get(i).equals("WITHDRAWN")){
					  logger.debug("Withdrawn Sample = " + sample.getName());
					  sampleService.updateSampleReceiveStatus(sample, WaspStatus.ABANDONED);
				  }
				  else{logger.debug("neither received nor withdrawn. very unexpected");}
			  }catch (WaspMessageBuildingException e){
				  logger.warn(e.getLocalizedMessage());
				  waspErrorMessage("task.samplereceive_message.error");
				  return "redirect:/task/samplereceive/list.do";
			  } 
		  }	
		  else{
			  logger.debug("SampleId " + sampleIdList.get(i) + " is not being updated now");
		  }
	  }
	  waspMessage("task.samplereceive_update_success.label");
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
		  receiveSampleStatusList.add(sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(sample)));	
		  
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
			  try{
				  sampleService.updateSampleReceiveStatus(sample, sampleService.convertSampleReceivedStatusFromWeb(receivedStatusList.get(index++)));
			  } catch (WaspMessageBuildingException e){
				  logger.warn(e.getLocalizedMessage());
				  waspMessage("task.samplereceive_message.error");
			  }
		  }
	  }
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  
  }
  
  @RequestMapping(value = "/sampleqc/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String listSampleQC(ModelMap m) {

    List<Job> jobsActiveAndAwaitingSampleQC = jobService.getJobsAwaitingSampleQC();
        
    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingSampleQC){
    	logger.debug("processing samples for job with id='" + job.getJobId() + "'");
    	List<Sample> newSampleList = jobService.getSubmittedSamplesNotYetQC(job);
    	for (Sample sample: newSampleList)
    		logger.debug("    .... sample: id='" + sample.getSampleId() + "'");
    	sampleService.sortSamplesBySampleName(newSampleList);    	
    	jobAndSampleMap.put(job, newSampleList);
    }
    
    if(jobsActiveAndAwaitingSampleQC.size() != jobAndSampleMap.size()){
    	//message and get out of here
    }
    
    m.addAttribute("jobList", jobsActiveAndAwaitingSampleQC);
    m.addAttribute("jobAndSamplesMap", jobAndSampleMap);
    
    return "task/sampleqc/list";
  }

  @RequestMapping(value = "/sampleqc/qc", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String updateSampleQC(
      @RequestParam("sampleId") Integer sampleId,
      @RequestParam("qcStatus") String qcStatus,
      @RequestParam("comment") String comment,
      ModelMap m) {	  
	  
	  Sample sample = sampleDao.getSampleBySampleId(sampleId);
	  if(sample.getSampleId()==null){
		  waspErrorMessage("task.sampleqc_invalid_sample.error");
		  return "redirect:/task/sampleqc/list.do";
	  }
	  if(qcStatus == null ||  qcStatus.equals("")){
		  waspErrorMessage("task.sampleqc_status_empty.error");
		  return "redirect:/task/sampleqc/list.do";
	  }
	  if( ! "FAILED".equals(qcStatus) && ! "PASSED".equals(qcStatus) ){
		  waspErrorMessage("task.sampleqc_qcStatus_invalid.error");	
		  return "redirect:/task/sampleqc/list.do";
	  }
	  if("FAILED".equals(qcStatus) && comment.trim().isEmpty() ){
		  waspErrorMessage("task.sampleqc_comment_empty.error");	
		  return "redirect:/task/sampleqc/list.do";
	  }

	  try{
		  if(qcStatus.equals("PASSED")){
			  sampleService.updateQCStatus(sample, WaspStatus.COMPLETED);
		  }
		  else if(qcStatus.equals("FAILED")){
			  sampleService.updateQCStatus(sample, WaspStatus.FAILED);
		  }
		  else{
			  waspErrorMessage("task.sampleqc_status_invalid.error");
			  return "redirect:/task/sampleqc/list.do";
		  }
	  } catch (WaspMessageBuildingException e){
		  logger.warn(e.getLocalizedMessage());
		  waspErrorMessage("task.sampleqc_message.error");
		  return "redirect:/task/sampleqc/list.do";
	  }
	  
	  //12-11-12 as per Andy, perform the updateQCstatus and the setSampleQCComment separately
	  //unfortunately, they are not easily linked within a single transaction.
	  try{
		  sampleService.setSampleQCComment(sample.getSampleId(), comment.trim());
	  }catch(Exception e){
		  logger.warn(e.getMessage());
	  }
	  
	  waspMessage("task.sampleqc_update_success.label");	
	  return "redirect:/task/sampleqc/list.do";
  }
  
  @RequestMapping(value = "/libraryqc/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String listLibraryQC(ModelMap m) {

    List<Job> jobsActiveAndAwaitingLibraryQC = jobService.getJobsAwaitingLibraryQC();
        
    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingLibraryQC){
    	logger.debug("processing libraries for job with id='" + job.getJobId() + "'");
    	List<Sample> newLibraryList = jobService.getLibrariesNotYetQC(job);
    	for (Sample sample: newLibraryList)
    		logger.debug("    .... sample: id='" + sample.getSampleId() + "'");
    	sampleService.sortSamplesBySampleName(newLibraryList);    	
    	jobAndSampleMap.put(job, newLibraryList);
    }
    
    if(jobsActiveAndAwaitingLibraryQC.size() != jobAndSampleMap.size()){
    	//message and get out of here
    }
    
    m.addAttribute("jobList", jobsActiveAndAwaitingLibraryQC);
    m.addAttribute("jobAndSamplesMap", jobAndSampleMap);
    
    return "task/libraryqc/list";
  }

  @RequestMapping(value = "/libraryqc/qc", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String updateLibraryQC(
      @RequestParam("sampleId") Integer sampleId,
      @RequestParam("qcStatus") String qcStatus,
      ModelMap m) {
	  Sample sample = sampleDao.getSampleBySampleId(sampleId);
	  if(sample.getSampleId()==null){
		  waspErrorMessage("task.libraryqc_invalid_sample.error");
		  return "redirect:/task/libraryqc/list.do";
	  }
	  if(qcStatus == null ||  qcStatus.equals("")){
		  waspErrorMessage("task.libraryqc_status_empty.error");
		  return "redirect:/task/libraryqc/list.do";
	  }
	  try{
		  if(qcStatus.equals("PASSED")){
			  sampleService.updateQCStatus(sample, WaspStatus.COMPLETED);
		  }
		  else if(qcStatus.equals("FAILED")){
			  sampleService.updateQCStatus(sample, WaspStatus.FAILED);
		  }
		  else{
			  waspErrorMessage("task.libraryqc_status_invalid.error");
			  return "redirect:/task/libraryqc/list.do";
		  }
	  } catch (WaspMessageBuildingException e){
		  logger.warn(e.getLocalizedMessage());
		  waspErrorMessage("task.libraryqc_message.error");
		  return "redirect:/task/libraryqc/list.do";
	  }
	  waspMessage("task.libraryqc_update_success.label");	
	  return "redirect:/task/libraryqc/list.do";
  }
  
 
  
  
}

