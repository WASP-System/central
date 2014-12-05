package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.exception.MetaAttributeNotFoundException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.taskMapping.TaskMappingRegistry;
import edu.yu.einstein.wasp.taskMapping.WaspTaskMapping;
import edu.yu.einstein.wasp.web.WebHyperlink;

@Controller
@RequestMapping("/task")
public class TaskController extends WaspController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private SampleSourceDao sampleSourceDao;

 @Autowired
  private JobService jobService;

  @Autowired
  private MessageServiceWebapp messageService;

  @Autowired
  private SampleService sampleService;
  
  @Autowired
  private TaskService taskService;
  
  @Autowired
  private TaskMappingRegistry taskMappingRegistry;
  
  @Autowired
  private GenomeService genomeService;
   

  @RequestMapping(value = "/assignLibraries/lists", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String assignLibrariesLists(ModelMap m) {

	  List<Job> jobsWithLibrariesToGoOnFlowCell = jobService.getJobsWithLibrariesToGoOnPlatformUnit();
	  List<Job> jobsActive = jobService.getActiveJobs();
	    
	  List<Job> jobsActiveAndWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
	  for(Job jobActive : jobsActive){
		  for(Job jobAwaiting : jobsWithLibrariesToGoOnFlowCell){
			  if(jobActive.getId().intValue()==jobAwaiting.getId().intValue()){
	    			jobsActiveAndWithLibrariesToGoOnFlowCell.add(jobActive);
	    			break;
	    	  }
	      }
	  }
	  jobService.sortJobsByJobId(jobsActiveAndWithLibrariesToGoOnFlowCell);	  
	  m.addAttribute("jobList", jobsActiveAndWithLibrariesToGoOnFlowCell);
	  List<Sample> activePlatformUnits = sampleService.getAvailablePlatformUnits();
	  sampleService.sortSamplesBySampleName(activePlatformUnits);
	  Map<Sample, String> activePlatformUnitsWithViewLinks = new LinkedHashMap<Sample, String>();
	  for (Sample pu: activePlatformUnits){
		  String puViewLink = "#";
		  try{
			  puViewLink = sampleService.getPlatformunitViewLink(pu);
		  } catch (WaspException e){
			  logger.warn("Unable to get link to display platform unit view: " + e.getLocalizedMessage()); //for displaying web anchor link to platformunit
		  }
		  activePlatformUnitsWithViewLinks.put(pu, puViewLink);
	  }
	 
	  m.addAttribute("activePlatformUnitsWithViewLinks", activePlatformUnitsWithViewLinks);
	  
	  List<String> barcodes = new ArrayList<String>();
	  List<String> cells = new ArrayList<String>();
	  for(Sample platformUnit : activePlatformUnits){
		  String barcode = platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode();
		  if(barcode==null || barcode.equals("")){
			  barcode = messageService.getMessage("jobListAssignLibrary.unknown.label=Unknown");//new String("Unknown");
		  }
		  barcodes.add(barcode);
		  String cellCountStr = messageService.getMessage("jobListAssignLibrary.unknown.label=Unknown");//"Unknown";
		  try {
			  cellCountStr = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString();
		  } catch (Exception e) {
			  logger.warn(e.getLocalizedMessage());
		  }
		  cells.add(cellCountStr);
	  }
	  
	  m.addAttribute("barcodes", barcodes);
	  m.addAttribute("cells", cells);
	 
	  return "task/assignLibraries/lists";
  }
  
  @RequestMapping(value = "/samplereceive/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String listSampleReceive(ModelMap m) {

    List<Job> jobsActiveAndAwaitingSubmittedSamples = jobService.getJobsAwaitingReceivingOfSamples();
        
    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingSubmittedSamples){
    	logger.debug("processing samples for job with id='" + job.getId() + "'");
    	List<Sample> newSampleList = jobService.getSubmittedSamplesNotYetReceived(job);
    	for (Sample sample: newSampleList)
    		logger.debug("    .... sample: id='" + sample.getId() + "'");
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
  public /*Callable<String>*/ String recordSampleReceive(@RequestParam("sampleId") final List<Integer> sampleIdList, final ModelMap m) {
	  
	 /* return new Callable<String>() {

		@Override
		public String call() throws Exception {*/
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
					  Sample sample = sampleService.getSampleDao().getSampleBySampleId(sampleIdList.get(i).intValue());
					  if(sample.getId()<=0){
						  waspErrorMessage("task.samplereceive_receivedstatus_unexpected.error");
						  logger.warn("unable to find sampleId " + sampleIdList.get(i).intValue() + " in task/samplereceive/receive - POST");
						  return "redirect:/task/samplereceive/list.do";
					  }
					  try{
						  if(receivedStatusList.get(i).equals("RECEIVED")){	
							  logger.debug("Received Sample = " + sample.getName());
							  sampleService.updateSampleReceiveStatus(sample, WaspStatus.CREATED);
							  transitionDelay();
						  }
						  else if(receivedStatusList.get(i).equals("WITHDRAWN")){
							  logger.debug("Withdrawn Sample = " + sample.getName());
							  sampleService.updateSampleReceiveStatus(sample, WaspStatus.ABANDONED);
							  transitionDelay();
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
	//	}
	//};
  }
  

  @RequestMapping(value = "/sampleqc/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String listSampleQC(ModelMap m) {

    List<Job> jobsActiveAndAwaitingSampleQC = jobService.getJobsAwaitingSampleQC();
        
    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingSampleQC){
    	logger.debug("processing samples for job with id='" + job.getId() + "'");
    	List<Sample> newSampleList = jobService.getSubmittedSamplesNotYetQC(job);
    	for (Sample sample: newSampleList)
    		logger.debug("    .... sample: id='" + sample.getId() + "'");
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
  public /*Callable<String>*/ String updateSampleQC(
      @RequestParam("sampleId") final Integer sampleId,
      @RequestParam("qcStatus") final String qcStatus,
      @RequestParam("comment") final String comment,
      final ModelMap m) {	
	  
	  /*return new Callable<String>() {

		@Override
		public String call() throws Exception {*/
			Sample sample = sampleService.getSampleDao().getSampleBySampleId(sampleId);
			  if(sample.getId()==null){
				  waspErrorMessage("task.sampleqc_invalid_sample.error");
				  return "redirect:/task/sampleqc/list.do";
			  }
			  if(qcStatus == null ||  qcStatus.equals("")){
				  waspErrorMessage("task.sampleqc_qcStatus_invalid.error");
				  return "redirect:/task/sampleqc/list.do";
			  }
			  if( ! SampleService.STATUS_FAILED.equals(qcStatus) && ! SampleService.STATUS_PASSED.equals(qcStatus) ){
				  waspErrorMessage("task.sampleqc_qcStatus_invalid.error");	
				  return "redirect:/task/sampleqc/list.do";
			  }
			  if(SampleService.STATUS_FAILED.equals(qcStatus) && comment.trim().isEmpty() ){
				  waspErrorMessage("task.sampleqc_comment_empty.error");	
				  return "redirect:/task/sampleqc/list.do";
			  }

			  try{
				  if(qcStatus.equals(SampleService.STATUS_PASSED)){
					  sampleService.updateQCStatus(sample, WaspStatus.COMPLETED);
					  transitionDelay();
				  }
				  else if(qcStatus.equals(SampleService.STATUS_FAILED)){
					  sampleService.updateQCStatus(sample, WaspStatus.FAILED);
					  transitionDelay();
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
					  sampleService.setSampleQCComment(sample.getId(), comment.trim());
				  }
			  }
			  catch(Exception e){
				  logger.warn(e.getMessage());
			  }
			  
			  waspMessage("task.sampleqc_update_success.label");	
			  return "redirect:/task/sampleqc/list.do";
//		}
//	};
  }
  
  @RequestMapping(value = "/libraryqc/list", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
  public String listLibraryQC(ModelMap m) {

    List<Job> jobsActiveAndAwaitingLibraryQC = jobService.getJobsAwaitingLibraryQC();
        
    Map<Job, List<Sample>> jobAndSampleMap = new HashMap<Job, List<Sample>>();
    for(Job job : jobsActiveAndAwaitingLibraryQC){
    	logger.debug("processing libraries for job with id='" + job.getId() + "'");
    	List<Sample> newLibraryList = jobService.getLibrariesNotYetQC(job);
    	for (Sample sample: newLibraryList)
    		logger.debug("    .... sample: id='" + sample.getId() + "'");
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
  public /*Callable<String>*/ String updateLibraryQC(
      @RequestParam("sampleId") final Integer sampleId,
      @RequestParam("qcStatus") final String qcStatus,
      @RequestParam("comment") final String comment,
      final ModelMap m) {
	  
	  /*return new Callable<String>() {

		@Override
		public String call() throws Exception {*/
			 Sample sample = sampleService.getSampleDao().getSampleBySampleId(sampleId);
			  if(sample.getId()==null){
				  waspErrorMessage("task.libraryqc_invalid_sample.error");
				  return "redirect:/task/libraryqc/list.do";
			  }
			  if(qcStatus == null ||  qcStatus.equals("")){
				  waspErrorMessage("task.libraryqc_qcStatus_invalid.error");
				  return "redirect:/task/libraryqc/list.do";
			  }
			  if( ! SampleService.STATUS_FAILED.equals(qcStatus) && ! SampleService.STATUS_PASSED.equals(qcStatus) ){
				  waspErrorMessage("task.libraryqc_qcStatus_invalid.error");	
				  return "redirect:/task/libraryqc/list.do";
			  }
			  if(SampleService.STATUS_FAILED.equals(qcStatus) && comment.trim().isEmpty() ){
				  waspErrorMessage("task.libraryqc_comment_empty.error");	
				  return "redirect:/task/libraryqc/list.do";
			  }

			  try{
				  if(qcStatus.equals(SampleService.STATUS_PASSED)){
					  sampleService.updateQCStatus(sample, WaspStatus.COMPLETED);
					  transitionDelay();
				  }
				  else if(qcStatus.equals(SampleService.STATUS_FAILED)){
					  sampleService.updateQCStatus(sample, WaspStatus.FAILED);
					  transitionDelay();
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
					  sampleService.setSampleQCComment(sample.getId(), comment.trim());
				  }
			  }
			  catch(Exception e){
				  logger.warn(e.getMessage());
			  }
			  
			  waspMessage("task.libraryqc_update_success.label");	
			  return "redirect:/task/libraryqc/list.do";
	//	}
	//};
 
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
			for(Sample sample : sampleList)
				sampleSpeciesMap.put(sample, sampleService.getNameOfOrganism(sample));
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
	@PreAuthorize("hasRole('su') or hasRole('da-*') or hasRole('ga-*') or hasRole('fm')")
	public String pendingDaApprove(ModelMap m) {

		List<LabPending> labsPendingDaApprovalList = new ArrayList<LabPending>();
		List<Job> jobsPendingDaApprovalList = new ArrayList<Job>();

		taskService.getDepartmentAdminPendingTasks(labsPendingDaApprovalList, jobsPendingDaApprovalList);
		m.addAttribute("labspendinglist", labsPendingDaApprovalList);
		
		//finish up with pending jobs		
		jobService.sortJobsByJobId(jobsPendingDaApprovalList);
		
		m.addAttribute("jobspendinglist", jobsPendingDaApprovalList);
		
		getJobApproveInfo(jobsPendingDaApprovalList, m);
		
		 //does the jobPendingDaApproval also require a quote?
		//added so that a DA must quote the job before approving the job
		  Map<Job, String> quoteMap = new HashMap<Job, String>();
		  for(Job job : jobsPendingDaApprovalList){
			  if(jobService.isJobAwaitingQuote(job)){
				  quoteMap.put(job, "true");
			  }
			  else{quoteMap.put(job, "false");}
		  }
		  m.addAttribute("quotemap", quoteMap);

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
	  if(job.getId()==null){
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
		  jobService.setJobApprovalStatusAndComment(jobApproveCode, job, status, comment.trim());
		  transitionDelay();
	  } catch (Exception e) {
		  waspErrorMessage("jobapprovetask.updateFailed.error"); 
		  logger.warn("Update unexpectedly failed");
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
	public /*Callable<String>*/ String piJobApprove(
			@PathVariable("labId") final Integer labId, 
			@RequestParam("jobId") final Integer jobId, 
			@RequestParam("action") final String action, 
			@RequestParam("comment") final String comment, 
			final ModelMap m) {
	  
	  /*return new Callable<String>(){
			@Override
			public String call() throws Exception {*/
			  jobApprove("piApprove", jobId, action, comment);
			  String referer = request.getHeader("Referer");
			  return "redirect:"+ referer;
			}
//	  };
//	}
  
  @RequestMapping(value = "/daJobApprove/{deptId}.do", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm') or hasRole('ft') or hasRole('da-' + #deptId)")
	public /*Callable<String>*/ String daJobApprove(
			@PathVariable("deptId") final Integer deptId, 
			@RequestParam("jobId") final Integer jobId, 
			@RequestParam("action") final String action, 
			@RequestParam("comment") final String comment, 
			final ModelMap m) {
						
			/*return new Callable<String>(){
				@Override
				public String call() throws Exception {*/
					  jobApprove("daApprove", jobId, action, comment);
					  String referer = request.getHeader("Referer");
					  return "redirect:"+ referer;	
		//		}
		//	};
	}  

	/**
	 * Request get list of user's tasks
	 * @param ModelMap m
	 * @return String view
	 */
	@RequestMapping(value = "/myTaskList.do", method = RequestMethod.GET)
	public String getMyTasks(ModelMap m)  {
		List<Job> activeJobs = jobService.getActiveJobs();
		List<WebHyperlink> taskMappingHyperlinksToDisplay = new ArrayList<WebHyperlink>();
		for (String name: taskMappingRegistry.getNames()){
			WaspTaskMapping taskMapping = taskMappingRegistry.getTaskMapping(name);
			if (taskMapping == null){
				logger.warn("Unable to retrieve a taskmapping with name '" + name + "' from the TaskMappingRegistry");
				continue;
			}
			if (taskMapping.isLinkToBeShown(activeJobs)){				
					taskMappingHyperlinksToDisplay.add(taskMapping);
			}
		}
		
		if(taskMappingHyperlinksToDisplay.size()>1){
			class WebHyperlinkComparator implements Comparator<WebHyperlink> {
			    @Override
			    public int compare(WebHyperlink arg0, WebHyperlink arg1) {
			        return arg0.getLabel().compareToIgnoreCase(arg1.getLabel());//sort by label
			    }
			}
			Collections.sort(taskMappingHyperlinksToDisplay, new WebHyperlinkComparator());
		}
		m.addAttribute("taskHyperlinks",taskMappingHyperlinksToDisplay);
		m.addAttribute("isTasks", taskMappingHyperlinksToDisplay.size() > 0);
		return "task/myTaskList";
	}
	
	private class JobIdComparator implements Comparator<Job> {
		  @Override
		  public int compare(Job job1, Job job2) {
			  return job1.getId().compareTo(job2.getId());
		  }
	}
	// TODO: This is extremely slow!!!! 
	//to sort samplesource objects based on macromoleucle name, then library name then platformunit name, then run name
	private class SampleSourceComparator implements Comparator<SampleSource> {
	    @Override
	    public int compare(SampleSource sample1, SampleSource sample2) {
	    	Sample library0 = sampleService.getLibrary(sample1);
	    	Sample macromolecule0 = library0.getParent();
	    	if(macromolecule0==null || macromolecule0.getId()==null){
	    		macromolecule0 = new Sample();
	    		macromolecule0.setName("User-Supplied Library");
	    	}	    	
			Sample cell0 = sampleService.getCell(sample1);
			Sample platformUnit0 = null;
			try{
				platformUnit0 = sampleService.getPlatformUnitForCell(cell0);
			}catch(SampleException e){logger.warn(e.getMessage());}
			List<Run> runs = platformUnit0.getRun();
			Run run0=null;
			if(!runs.isEmpty()){
				run0 = runs.get(0);
			}
			else{
				run0 = new Run();
				run0.setName("Not Run");
			}
		    String str0 = macromolecule0.getName() + library0.getName() + platformUnit0.getName() + sample1.getIndex().toString() + run0.getName(); 
			
	    	Sample library1 = sampleService.getLibrary(sample2);
	    	Sample macromolecule1 = library1.getParent();
	    	if(macromolecule1==null || macromolecule1.getId()==null){
	    		macromolecule1 = new Sample();
	    		macromolecule1.setName("User-Supplied Library");
	    	}		    	
			Sample cell1 = sampleService.getCell(sample2);
			Sample platformUnit1 = null;
			try{
				platformUnit1 = sampleService.getPlatformUnitForCell(cell1);
			}catch(SampleException e){logger.warn(e.getMessage());}
			List<Run> moreRuns = platformUnit0.getRun();
			Run run1;
			if(!moreRuns.isEmpty()){
				run1 = moreRuns.get(0);
			}
			else{
				run1 = new Run();
				run1.setName("Not Run");
			}
			String str1 = macromolecule1.getName() + library1.getName() + platformUnit1.getName() + sample2.getIndex().toString() + run1.getName(); 
				
	        return str0.compareToIgnoreCase(str1);
	    }
	}

	private void populateModelMapWithCommonCellLibraryAssociatedData(List<SampleSource> cellLibraries, ModelMap m){
		Assert.assertParameterNotNull(cellLibraries, "cellLibraries cannot be null");
		Assert.assertParameterNotNull(m, "model map cannot be null");
  		Map<SampleSource, Sample> cellLibraryLibraryMap = new HashMap<SampleSource, Sample>();
		Map<SampleSource, Sample> cellLibraryMacromoleculeMap = new HashMap<SampleSource, Sample>();
		Map<SampleSource, Sample> cellLibraryPUMap = new HashMap<SampleSource, Sample>();
		Map<SampleSource, Run> cellLibraryRunMap = new HashMap<SampleSource, Run>();
		Map<SampleSource, Integer> cellLibraryLaneMap = new HashMap<SampleSource, Integer>();
		Map<SampleSource, String> cellLibraryQcStatusCommentMap = new HashMap<SampleSource,String>();
		// Collections.sort(cellLibraries, new SampleSourceComparator());//sort the SampleSourceList
  		for(SampleSource cellLibrary : cellLibraries){
			Sample library = sampleService.getLibrary(cellLibrary);
			cellLibraryLibraryMap.put(cellLibrary, library);
			Sample macromolecule = library.getParent();
			if(macromolecule == null || macromolecule.getId() == null){
				macromolecule = new Sample();
				macromolecule.setName("User-Supplied Library");
			}
			cellLibraryMacromoleculeMap.put(cellLibrary, macromolecule);
			  
			Sample cell = sampleService.getCell(cellLibrary);
			logger.debug("cell: " + cell.getName());
			try{
				Integer laneNumber = sampleService.getCellIndex(cell);
				logger.debug("cell lane number: " + laneNumber);
				cellLibraryLaneMap.put(cellLibrary, laneNumber);//cell's position on flowcell (ie.: lane 3)
			}catch(Exception e){
				logger.warn("Unable to locate lane number for cell with Id " + cell.getId());
			}
			logger.debug("done");
			Sample platformUnit = null;
			try{
				platformUnit = sampleService.getPlatformUnitForCell(cell);
			}
			catch(Exception e){//should not occur
				platformUnit = new Sample();
				platformUnit.setName("Not Found");
				logger.warn("Expected a platformUnit belonging to cell with Id of " + cell.getId()); 
			}
			cellLibraryPUMap.put(cellLibrary, platformUnit);
			  
			List<Run> runs = platformUnit.getRun();
			Run run = null;
			if(!runs.isEmpty()){
				run = platformUnit.getRun().get(0);
			}
			else{
				run = new Run();
				run.setName("Not run");					  
			}
			cellLibraryRunMap.put(cellLibrary, run);	
			  
			  
			List<MetaMessage> inAnalysisCommentList = sampleService.getCellLibraryQCComments(cellLibrary.getId());
			if(inAnalysisCommentList.size()<=0){
				cellLibraryQcStatusCommentMap.put(cellLibrary, "");
			}
			else{
				cellLibraryQcStatusCommentMap.put(cellLibrary, inAnalysisCommentList.get(0).getValue());
			}
		}
  		for(SampleSource cellLibrary : cellLibraries){
  			Integer laneNo = cellLibraryLaneMap.get(cellLibrary);
  			logger.debug("laneNo = " + laneNo);
  			
  		}
  		m.addAttribute("cellLibraryLibraryMap", cellLibraryLibraryMap);
  		m.addAttribute("cellLibraryMacromoleculeMap", cellLibraryMacromoleculeMap);
  		m.addAttribute("cellLibraryPUMap", cellLibraryPUMap);
  		m.addAttribute("cellLibraryRunMap", cellLibraryRunMap);
  		m.addAttribute("cellLibraryQcStatusCommentMap", cellLibraryQcStatusCommentMap);
  		m.addAttribute("cellLibraryLaneMap", cellLibraryLaneMap);///added by rob, 7-17-14
  	}
 
  @RequestMapping(value = "/cellLibraryQC/list", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm-*')")
	public String listCellLibraryQC(ModelMap m) throws SampleTypeException {
	  List<SampleSource> allPreprocessedCellLibraries = new ArrayList<SampleSource>();
	  List<Job> activeJobsWithCellLibrariesAwaitingQC = new ArrayList<Job>();
	  Map<Job, List<SampleSource>> jobCellLibraryMap = new HashMap<Job, List<SampleSource>>();
	  Map<SampleSource, Boolean> cellLibraryQcStatusMap = new HashMap<SampleSource, Boolean>();
	  for(Job job : jobService.getActiveJobs()){
		  //make certain that aggregateAnalysis has not yet been kicked-off  for this job
		  if(jobService.isAggregationAnalysisBatchJob(job)){
			  continue;
		  }
		  boolean atLeastOneCellLibraryAwaitingQC = false;
		  Map<SampleSource, ExitStatus> jobCellLibrariesWithPreprocessingStatus = sampleService.getCellLibrariesWithPreprocessingStatus(job);//a preprocessed library is one that is sequenced and aligned
		  List<SampleSource> preprocessedCellLibraries = new ArrayList<SampleSource>();
		  for (SampleSource cellLibrary: jobCellLibrariesWithPreprocessingStatus.keySet()){
			  ExitStatus exitStatus = jobCellLibrariesWithPreprocessingStatus.get(cellLibrary);
			  if (!exitStatus.isCompleted())
				  continue;
			  preprocessedCellLibraries.add(cellLibrary);
			  allPreprocessedCellLibraries.add(cellLibrary);
			  if (sampleService.isCellLibraryAwaitingQC(cellLibrary))
				  atLeastOneCellLibraryAwaitingQC = true;
			  Boolean isCellLibraryPassedQC = null;
			  try{
				  isCellLibraryPassedQC = sampleService.isCellLibraryPassedQC(cellLibrary);
			  } catch (MetaAttributeNotFoundException e){} // no value set
			  cellLibraryQcStatusMap.put(cellLibrary, isCellLibraryPassedQC);
		  }
		  if (atLeastOneCellLibraryAwaitingQC){
			  activeJobsWithCellLibrariesAwaitingQC.add(job);
			  jobCellLibraryMap.put(job, preprocessedCellLibraries);
		  }
	  }
	  populateModelMapWithCommonCellLibraryAssociatedData(allPreprocessedCellLibraries, m);
	  //sort by job ID desc
	  Collections.sort(activeJobsWithCellLibrariesAwaitingQC, new JobIdComparator()); 

	  m.addAttribute("jobs", activeJobsWithCellLibrariesAwaitingQC);
	  m.addAttribute("jobCellLibraryMap", jobCellLibraryMap);
	  m.addAttribute("cellLibraryQcStatusMap", cellLibraryQcStatusMap);//Be careful in the jsp, as this Boolean can be null (not recorded yet)
	  return "task/cellLibraryQC/list";
	}
  

  
  @RequestMapping(value = "/cellLibraryQC/list", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm-*')")
	public String updateCellLibraryQC(
			@RequestParam("jobId") Integer jobId,
			@RequestParam("maxNumCellLibrariesThatCanBeRecorded") Integer maxNumCellLibrariesThatCanBeRecorded,
			@RequestParam("sampleSourceId") List<Integer> sampleSourceIdList,
			ModelMap m) {
	  
	  //1. check the most basic parameters
	  if(jobId==null){
		  waspErrorMessage("task.cellLibraryqc_invalid_jobId.error");
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  Job job = jobService.getJobByJobId(jobId);
	  if(job==null || job.getId()==null){
		  waspErrorMessage("task.cellLibraryqc_jobNotFound.error");
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  if(sampleSourceIdList==null || sampleSourceIdList.isEmpty()){//an empty list should never be sent by the webpage
		  waspErrorMessage("task.cellLibraryqc_invalid_samplesource.error");
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  
	  //check whether this job has already been terminated -- unlikely, but not impossible in a multi-user system
	  if(jobService.isTerminated(job)){
	  	waspErrorMessage("task.cellLibraryqc_jobPreviouslyTerminated.error");
  		return "redirect:/task/cellLibraryQC/list.do";
		  }
	  //check whether aggregate analysis has already been started -- unlikely, but not impossible in a multi-user system
	  if(jobService.isAggregationAnalysisBatchJob(job)){
		  waspErrorMessage("task.cellLibraryqc_aggregateAnalysisAlreadyUnderway.error");
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  
	  //gather info from web
	  List<SampleSource> sampleSourceList = new ArrayList<SampleSource>();
	  List<String> qcStatusList = new ArrayList<String>();
	  List<String> commentList = new ArrayList<String>(); 
	  Set<String> errorMessages = new HashSet<String>();//use a set so as not to repeat a message
	  
	  for(Integer sampleSourceId : sampleSourceIdList){
			
		  String qcStatus = request.getParameter("qcStatus" + sampleSourceId.toString());//qcStatus is radio button, so may or may not be sent
		  if(qcStatus==null){//this particular item not sent by web so we do NOT have to deal with it
			  continue;
		  }
		  else if(!qcStatus.trim().equalsIgnoreCase(SampleService.STATUS_PASSED) && !qcStatus.trim().equalsIgnoreCase(SampleService.STATUS_FAILED)){//should not occur
			  waspErrorMessage("task.cellLibraryqc_invalid_qcStatus_value.error");
			  return "redirect:/task/cellLibraryQC/list.do";
		  }
		  qcStatusList.add(qcStatus);
		  String comment = request.getParameter("comment" + sampleSourceId.toString());//should always be sent
		  if(comment==null){//should always be sent, so null is a problem (however the empty string is just fine)
			  waspErrorMessage("task.cellLibraryqc_comment_not_sent.error");
			  return "redirect:/task/cellLibraryQC/list.do";
		  }
		  commentList.add(comment);
		  SampleSource sampleSource = sampleSourceDao.getSampleSourceBySampleSourceId(sampleSourceId);
		  if(sampleSource==null || sampleSource.getSampleId()==null){//unlikely, but...
			  waspErrorMessage("task.cellLibraryqc_sampleSourceNotFound.error");
			  return "redirect:/task/cellLibraryQC/list.do";
		  }
		  sampleSourceList.add(sampleSource);
	  }
	  
	  //update database
	  int totalRecordsToRecord = sampleSourceList.size();
	  if(totalRecordsToRecord<=0){//are there any to deal with?? It's very unlikely to be zero, but......
		  waspErrorMessage("task.cellLibraryqc_noRecordsSelected.error");
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  
	  //perform as many database saves as possible
	  for(int i = 0; i < totalRecordsToRecord; i++){
		  String qcStatus = qcStatusList.get(i);
		  String trimmedComment = commentList.get(i);
		  if( SampleService.STATUS_FAILED.equals(qcStatus) && (trimmedComment.length()==0 || "Provide reason for exclusion".equalsIgnoreCase(trimmedComment)) ){//unlikely, since javascript prevents, but....
			  //record a flash error in the Set errorMessages (to avoid display more than once), since excluding a library-run from analysis requires a valid comment
			  errorMessages.add("task.cellLibraryqc_excludeRequiresComment.error");
			  continue;
		  }
		  try{
			  sampleService.saveCellLibraryQCStatusAndComment(sampleSourceList.get(i), qcStatus, trimmedComment);//will deal with insert and update
		  }catch(Exception e){
			  logger.warn(e.getLocalizedMessage());
			  errorMessages.add("task.cellLibraryqc_message.error");
		  }		  
	  }
	  
	  if(!errorMessages.isEmpty()){//some error occurred during the database save(s). Note that if any errors, do not start any analysis or terminate job.
		  //add all to waspErrorMessage
		  for(String s : errorMessages){
			  waspErrorMessage(s);
		  }
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  
	  waspMessage("task.cellLibraryqc_update_success.label");	
	  return "redirect:/task/cellLibraryQC/list.do";
  }
  
  	
  
  @RequestMapping(value = "/aggregationAnalysis/list", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm-*')")
	public String aggregationAnalysisGet(ModelMap m) throws SampleTypeException {

	  List<Job> activeJobsWithCellLibrariesToDisplay = new ArrayList<Job>();
	  Map<Job, List<SampleSource>> jobCellLibraryMap = new HashMap<Job, List<SampleSource>>();
	  List<SampleSource> allCellLibraries = new ArrayList<SampleSource>();
	  Map<SampleSource, Boolean> cellLibraryQcStatusMap = new HashMap<SampleSource, Boolean>();
	  
	  Map<SampleSource, String> cellLibraryWithPreprocessingStatusMap = new HashMap<SampleSource,String>();
	  for(Job job : jobService.getActiveJobs()){
		  List<SampleSource> allCellLibrariesForJob = new ArrayList<SampleSource>();
		  //make certain that aggregateAnalysis has not yet been kicked-off for this job
		  if(jobService.isAnySampleCurrentlyBeingProcessed(job) || jobService.isAggregationAnalysisBatchJob(job)){
			  continue;
		  }
		  Map<SampleSource, ExitStatus> jobCellLibrariesWithPreprocessingStatus = sampleService.getCellLibrariesWithPreprocessingStatus(job);//a preprocessed library is one that is sequenced and aligned
		  for (SampleSource cellLibrary: jobCellLibrariesWithPreprocessingStatus.keySet()){
			  cellLibraryWithPreprocessingStatusMap.put(cellLibrary, jobCellLibrariesWithPreprocessingStatus.get(cellLibrary).getExitCode());
			  Boolean isCellLibraryPassedQC = null;
			  try{
				  isCellLibraryPassedQC = sampleService.isCellLibraryPassedQC(cellLibrary);
			  } catch (MetaAttributeNotFoundException e){} // no value set
			  cellLibraryQcStatusMap.put(cellLibrary, isCellLibraryPassedQC);
			  allCellLibrariesForJob.add(cellLibrary);
			  allCellLibraries.add(cellLibrary);
		  }
		  if (!jobCellLibrariesWithPreprocessingStatus.isEmpty()){
			  activeJobsWithCellLibrariesToDisplay.add(job);
			  jobCellLibraryMap.put(job, allCellLibrariesForJob);
		  }
	  }
	  populateModelMapWithCommonCellLibraryAssociatedData(allCellLibraries, m);
	  //sort by job ID desc
	  Collections.sort(activeJobsWithCellLibrariesToDisplay, new JobIdComparator()); 

	  m.addAttribute("jobs", activeJobsWithCellLibrariesToDisplay);
	  m.addAttribute("jobCellLibraryMap", jobCellLibraryMap);
	  m.addAttribute("cellLibraryWithPreprocessingStatusMap", cellLibraryWithPreprocessingStatusMap);
	  m.addAttribute("cellLibraryQcStatusMap", cellLibraryQcStatusMap); //Be careful in the jsp, as this Boolean can be null (not recorded yet)
	
	  return "task/aggregationAnalysis/list";
	}
  

  
  @RequestMapping(value = "/aggregationAnalysis/list", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm-*')")
	public /*Callable<String>*/ String aggregationAnalysisPost(
			@RequestParam("jobId") final Integer jobId,
			@RequestParam("startAnalysis") final String startAnalysis,
		    final ModelMap m) {
	  
	  /*return new Callable<String>() {

		@Override
		public String call() throws Exception {*/
			//1. check the most basic parameters
			  if(jobId==null){
				  waspErrorMessage("task.aggregateAnalysis_invalid_jobId.error");
				  return "redirect:/task/aggregationAnalysis/list.do";
			  }
			  Job job = jobService.getJobByJobId(jobId);
			  if(job==null || job.getId()==null){
				  waspErrorMessage("task.aggregateAnalysis_jobNotFound.error");
				  return "redirect:/task/aggregationAnalysis/list.do";
			  }
			 
			  //check whether this job has already been terminated -- unlikely, but not impossible in a multi-user system
			  if(jobService.isTerminated(job)){
			  	waspErrorMessage("task.aggregateAnalysis_jobPreviouslyTerminated.error");
		  		return "redirect:/task/aggregationAnalysis/list.do";
				  }
			  //check whether aggregate analysis has already been started -- unlikely, but not impossible in a multi-user system
			  if(jobService.isAggregationAnalysisBatchJob(job)){
				  waspErrorMessage("task.aggregateAnalysis_aggregateAnalysisAlreadyUnderway.error");
				  return "redirect:/task/aggregationAnalysis/list.do";
			  }
			  if(startAnalysis == null || "".equals(startAnalysis)){
				  waspErrorMessage("task.aggregateAnalysis_invalid_startAnalysis.error");//shouldn't occur as jovascript should prevent, but....
				  return "redirect:/task/aggregationAnalysis/list.do";
			  }
			  if( !"Now".equalsIgnoreCase(startAnalysis) && !"Later".equalsIgnoreCase(startAnalysis) && !"Never".equalsIgnoreCase(startAnalysis) ){
				  waspErrorMessage("task.aggregateAnalysis_invalidValues_startAnalysis.error");//shouldn't occur as jovascript should prevent, but....
				  return "redirect:/task/aggregationAnalysis/list.do";
			  }
			  
			  // Kick off the analysis or terminate the job (don't want to do analysis) (or else postpone decision for analysis for later on)
			  if("Now".equalsIgnoreCase(startAnalysis)){
				  //kick off analysis
				  try{
					  jobService.initiateAggregationAnalysisBatchJob(job);
					  waspMessage("task.aggregateAnalysis_analysisBegun.label");
				  } catch (Exception e){
					  logger.warn(e.getLocalizedMessage());
					  waspErrorMessage("task.aggregateAnalysis_startAnalysisNotPossibleNow.error");
					  return "redirect:/task/aggregationAnalysis/list.do";
				  }
			  }
				 
			  else if("Never".equalsIgnoreCase(startAnalysis)){//do not want analysis, so don't care
				  //terminate job
				  try{
					  jobService.terminate(job);// throws WaspMessageBuildingException;
					  waspMessage("task.aggregateAnalysis_jobTerminated.label");
				  }
				  catch(WaspMessageBuildingException e){
					  logger.warn(e.getLocalizedMessage());
					  waspErrorMessage("task.aggregateAnalysis_terminateJobUnexpectedlyFailed.error");
					  return "redirect:/task/aggregationAnalysis/list.do";
				  }
			  }
			  else if("Later".equalsIgnoreCase(startAnalysis)){
				  waspMessage("task.aggregateAnalysis_analysisNotBegun.label");
				  return "redirect:/task/aggregationAnalysis/list.do";
			  }
			  //waspMessage("task.aggregateAnalysis_update_success.label");	
			  return "redirect:/task/aggregationAnalysis/list.do";
		}
//	};
	  
//  }
}

