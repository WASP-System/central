package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TaskService;

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
  private MessageServiceWebapp messageService;

  @Autowired
  private SampleService sampleService;
  
  @Autowired
  private TaskService taskService;

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
    	sampleService.sortSamplesBySampleId(newSampleList);    	
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
	  //order by sample id
	  sampleService.sortSamplesBySampleId(submittedSamplesList);
	  
	  List<String> receiveSampleStatusList = new ArrayList<String>();
	  List<Boolean> sampleHasBeenProcessedList = new ArrayList<Boolean>();
	  for(Sample sample : submittedSamplesList){	
		  receiveSampleStatusList.add(sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(sample)));	
		  
		  boolean sampleHasBeenProcessedByFacility = sampleService.isSubmittedSampleProcessedByFacility(sample);
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
		  if(sample.getSampleId() > 0 && ! sampleService.isSubmittedSampleProcessedByFacility(sample)){
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
    	sampleService.sortSamplesBySampleId(newSampleList);    	
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
		  waspErrorMessage("task.sampleqc_qcStatus_invalid.error");
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
		  if(!comment.trim().isEmpty()){
			  sampleService.setSampleQCComment(sample.getSampleId(), comment.trim());
		  }
	  }
	  catch(Exception e){
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
    	sampleService.sortSamplesBySampleId(newLibraryList);    	
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
      @RequestParam("comment") String comment,
      ModelMap m) {
	  
	  Sample sample = sampleDao.getSampleBySampleId(sampleId);
	  if(sample.getSampleId()==null){
		  waspErrorMessage("task.libraryqc_invalid_sample.error");
		  return "redirect:/task/libraryqc/list.do";
	  }
	  if(qcStatus == null ||  qcStatus.equals("")){
		  waspErrorMessage("task.libraryqc_qcStatus_invalid.error");
		  return "redirect:/task/libraryqc/list.do";
	  }
	  if( ! "FAILED".equals(qcStatus) && ! "PASSED".equals(qcStatus) ){
		  waspErrorMessage("task.libraryqc_qcStatus_invalid.error");	
		  return "redirect:/task/libraryqc/list.do";
	  }
	  if("FAILED".equals(qcStatus) && comment.trim().isEmpty() ){
		  waspErrorMessage("task.libraryqc_comment_empty.error");	
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
	  
	  //12-11-12 as per Andy, perform the updateQCstatus and the setSampleQCComment separately
	  //unfortunately, they are not easily linked within a single transaction.
	  try{
		  if(!comment.trim().isEmpty()){
			  sampleService.setSampleQCComment(sample.getSampleId(), comment.trim());
		  }
	  }
	  catch(Exception e){
		  logger.warn(e.getMessage());
	  }
	  
	  waspMessage("task.libraryqc_update_success.label");	
	  return "redirect:/task/libraryqc/list.do";
  }
 
  
  
  
  private void getJobApproveInfo(List<Job> jobList, ModelMap m){
	  
	  //used by pendingFMApprove(), pendingDaApprove(), pendingPiApprove()
	    Map<Job, List<Sample>> jobSubmittedSamplesMap = new HashMap<Job, List<Sample>>();
		Map<Job, LinkedHashMap<String,String>> jobExtraJobDetailsMap = new HashMap<Job, LinkedHashMap<String,String>>();
		Map<Job, LinkedHashMap<String,String>> jobApprovalsMap = new HashMap<Job, LinkedHashMap<String,String>>();

		Map<Sample, String> sampleSpeciesMap = new HashMap<Sample, String>();
		for(Job job : jobList){
			jobExtraJobDetailsMap.put(job, jobService.getExtraJobDetails(job));
			jobApprovalsMap.put(job, jobService.getJobApprovals(job));
			List<Sample> sampleList = jobService.getSubmittedSamples(job);
			sampleService.sortSamplesBySampleName(sampleList);
			jobSubmittedSamplesMap.put(job, sampleList);
			for(Sample sample : sampleList){
				int speciesFound = 0;
				for(SampleMeta sampleMeta : sample.getSampleMeta()){
					if(sampleMeta.getK().indexOf("species") > -1){
						sampleSpeciesMap.put(sample, sampleMeta.getV());
						speciesFound = 1;
						break;
					}
				}
				if(speciesFound == 0){
					sampleSpeciesMap.put(sample, messageService.getMessage("jobapprovetask.unknown.label"));
				}
			}
		}
		m.addAttribute("jobExtraJobDetailsMap", jobExtraJobDetailsMap);
		m.addAttribute("jobApprovalsMap", jobApprovalsMap);
		m.addAttribute("jobSubmittedSamplesMap", jobSubmittedSamplesMap);
		m.addAttribute("sampleSpeciesMap", sampleSpeciesMap);  
  }
  
  @RequestMapping(value = "/fmapprove/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String pendingFmApprove(ModelMap m) {
	 
	  List<Job> jobsPendingFmApprovalList = jobService.getJobsAwaitingFmApproval();
	  jobService.sortJobsByJobId(jobsPendingFmApprovalList);
	  m.addAttribute("jobspendinglist", jobsPendingFmApprovalList);
	  getJobApproveInfo(jobsPendingFmApprovalList, m);
	
	  return "task/fmapprove/list";
  }
    
	@RequestMapping(value = "/piapprove/list", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('pi-*') or hasRole('lm-*')")
	public String pendingPiApprove(ModelMap m){
		
		List<UserPending> newUsersPendingLmApprovalList = new ArrayList<UserPending>();
		List<LabUser> existingUsersPendingLmApprovalList = new ArrayList<LabUser>();
		List<Job> jobsPendingLmApprovalList = new ArrayList<Job>();
		taskService.getLabManagerPendingTasks(newUsersPendingLmApprovalList, existingUsersPendingLmApprovalList, jobsPendingLmApprovalList);
		
		//finish up with pending jobs		
		jobService.sortJobsByJobId(jobsPendingLmApprovalList);
		
		m.addAttribute("newuserspendinglist", newUsersPendingLmApprovalList); 
		m.addAttribute("existinguserspendinglist", existingUsersPendingLmApprovalList); 
		m.addAttribute("jobspendinglist", jobsPendingLmApprovalList); 
		
		getJobApproveInfo(jobsPendingLmApprovalList, m);

		return "task/piapprove/list";
	}

	@RequestMapping(value = "/daapprove/list", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('da-*') or hasRole('ga-*')")
	public String pendingDaApprove(ModelMap m) {

		List<LabPending> labsPendingDaApprovalList = new ArrayList<LabPending>();
		List<Job> jobsPendingDaApprovalList = new ArrayList<Job>();

		taskService.getDepartmentAdminPendingTasks(labsPendingDaApprovalList, jobsPendingDaApprovalList);
		m.addAttribute("labspendinglist", labsPendingDaApprovalList);
		
		//finish up with pending jobs		
		jobService.sortJobsByJobId(jobsPendingDaApprovalList);
		
		m.addAttribute("jobspendinglist", jobsPendingDaApprovalList);
		
		getJobApproveInfo(jobsPendingDaApprovalList, m);

		return "task/daapprove/list";
	}
  
  
  
  
  
  
  
  private void jobApprove(String jobApproveCode, Integer jobId, String action, String comment){
	  
	  //used by fmJobApprove(), piJobApprove(), and daJobApprove()
	  if(!jobApproveCode.equals("piApprove") && !jobApproveCode.equals("daApprove") && !jobApproveCode.equals("fmApprove")){
		  waspErrorMessage("jobapprovetask.invalidJobApproveCode.error");
		  logger.warn("JobApproveCode is not valid");
		  return;
	  }

	  Job job = jobService.getJobByJobId(jobId);	   
	  if(job.getJobId()==null){
		  waspErrorMessage("jobapprovetask.invalidJob.error");
		  logger.warn("Job not found");
		  return;
	  }	  
	  if(action == null ||  action.equals("")){
		  waspErrorMessage("jobapprovetask.invalidAction.error");
		  logger.warn("Action cannot be empty");
		  return;
	  }
	  if( ! "APPROVED".equalsIgnoreCase(action) && ! "REJECTED".equalsIgnoreCase(action) ){
		  waspErrorMessage("jobapprovetask.invalidAction.error");
		  logger.warn("Action must be APPROVED or REJECTED");
		  return;
	  }
	  if("REJECTED".equalsIgnoreCase(action) && comment.trim().isEmpty() ){
		  waspErrorMessage("jobapprovetask.commentEmpty.error");
		  logger.warn("A reason must be provided when rejecting a job");
		  return;
	  }
	  
	  WaspStatus status = WaspStatus.UNKNOWN;
	  if("APPROVED".equalsIgnoreCase(action)){
		  status = WaspStatus.COMPLETED;
	  }
	  else if("REJECTED".equalsIgnoreCase(action)){
		  status = WaspStatus.ABANDONED;
	  }	
	  try {
		  //jobService.updateJobFmApprovalStatus(job, status);
		  jobService.updateJobApprovalStatus(jobApproveCode, job, status);

	  } catch (WaspMessageBuildingException e) {
		  waspErrorMessage("jobapprovetask.updateFailed.error"); 
		  logger.warn("Update unexpectedly failed");
		  return;
	  }
	  
	  //12-11-12 as per Andy, perform the updateQCstatus and the setSampleQCComment separately
	  //unfortunately, they are not easily linked within a single transaction.
	  try{
		  if(!comment.trim().isEmpty()){
			  jobService.setJobApprovalComment(jobApproveCode, job.getJobId(), comment.trim());
		  }
	  }
	  catch(Exception e){
		  logger.warn(e.getMessage());
		  return;
	  }
	  
	  String message = "APPROVED".equalsIgnoreCase(action)?"jobapprovetask.jobApproved.label":"jobapprovetask.jobRejected.label";
	  waspMessage(message);
  }
  
  @RequestMapping(value = "/fmJobApprove.do", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm') or hasRole('ft')")
	public String fmJobApprove(
			@RequestParam("jobId") Integer jobId, 
			@RequestParam("action") String action, 
			@RequestParam("comment") String comment, 
			ModelMap m) {
	  
	  jobApprove("fmApprove", jobId, action, comment);
	  String referer = request.getHeader("Referer");
	  return "redirect:"+ referer;
	}    
  
  @RequestMapping(value = "/piJobApprove/{labId}.do", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm') or hasRole('ft') or hasRole('lm-' + #labId) or hasRole('pi-' + #labId)")
	public String piJobApprove(
			@PathVariable("labId") Integer labId, 
			@RequestParam("jobId") Integer jobId, 
			@RequestParam("action") String action, 
			@RequestParam("comment") String comment, 
			ModelMap m) {
	  
	  jobApprove("piApprove", jobId, action, comment);
	  String referer = request.getHeader("Referer");
	  return "redirect:"+ referer;
	}
  
  @RequestMapping(value = "/daJobApprove/{deptId}.do", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm') or hasRole('ft') or hasRole('da-' + #deptId)")
	public String daJobApprove(
			@PathVariable("deptId") Integer deptId, 
			@RequestParam("jobId") Integer jobId, 
			@RequestParam("action") String action, 
			@RequestParam("comment") String comment, 
			ModelMap m) {
	  
	  jobApprove("daApprove", jobId, action, comment);
	  String referer = request.getHeader("Referer");
	  return "redirect:"+ referer;	
	}
  
}

