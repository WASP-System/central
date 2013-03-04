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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.exception.MetaAttributeNotFoundException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabPending;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserPending;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.taskMapping.TaskMappingRegistry;
import edu.yu.einstein.wasp.taskMapping.WaspTaskMapping;
import edu.yu.einstein.wasp.web.WebHyperlink;

@Controller
@Transactional
@RequestMapping("/task")
public class TaskController extends WaspController {

  @Autowired
  private AuthenticationService authenticationService;

   @Autowired
  private SampleDao sampleDao;

   @Autowired
  private SampleSourceDao sampleSourceDao;

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
  
  @Autowired
  private TaskMappingRegistry taskMappingRegistry;


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

	/**
	 * Request get list of user's tasks
	 * @param ModelMap m
	 * @return String view
	 */
	@RequestMapping(value = "/myTaskList.do", method = RequestMethod.GET)
	public String getMyTasks(ModelMap m)  {
		
		List<WebHyperlink> taskMappingHyperlinksToDisplay = new ArrayList<WebHyperlink>();
		for (String name: taskMappingRegistry.getNames()){
			WaspTaskMapping taskMapping = taskMappingRegistry.getTaskMapping(name);
			if (taskMapping == null){
				logger.warn("Unable to retrieve a taskmapping with name '" + name + "' from the TaskMappingRegistry");
				continue;
			}
			if (taskMapping.isLinkToBeShown()){				
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

 
  @RequestMapping(value = "/cellLibraryQC/list", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm-*')")
	public String listCellLibraryQC(ModelMap m) {

	  class JobIdComparator implements Comparator<Job> {
		  @Override
		  public int compare(Job arg0, Job arg1) {
			  return arg0.getJobId().compareTo(arg1.getJobId());
		  }
	  }
	  //to sort samplesource objects based on macromoleucle name, then library name then platformunit name, then run name
	  class SampleSourceComparator implements Comparator<SampleSource> {
		    @Override
		    public int compare(SampleSource arg0, SampleSource arg1) {
		    	Sample library0 = sampleService.getLibrary(arg0);
		    	Sample macromolecule0 = library0.getParent();
		    	if(macromolecule0==null || macromolecule0.getSampleId()==null){
		    		macromolecule0 = new Sample();
		    		macromolecule0.setName("User-Supplied Library");
		    	}	    	
				Sample cell0 = sampleService.getCell(arg0);
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
			    String str0 = macromolecule0.getName() + library0.getName() + platformUnit0.getName() + arg0.getIndex().toString() + run0.getName(); 
				
		    	Sample library1 = sampleService.getLibrary(arg1);
		    	Sample macromolecule1 = library1.getParent();
		    	if(macromolecule1==null || macromolecule1.getSampleId()==null){
		    		macromolecule1 = new Sample();
		    		macromolecule1.setName("User-Supplied Library");
		    	}		    	
				Sample cell1 = sampleService.getCell(arg1);
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
				String str1 = macromolecule1.getName() + library1.getName() + platformUnit1.getName() + arg1.getIndex().toString() + run1.getName(); 
					
		        return str0.compareToIgnoreCase(str1);
		    }
		}
	  
	  List<Job> activeJobsWithNoSamplesCurrentlyBeingProcessed = jobService.getActiveJobsWithNoSamplesCurrentlyBeingProcessed();//guarantees that all libraries in the job's pipeline have been assigned a value for QC and alignment is complete  
	  //**************will currently be none, so add two jobs.
	  boolean fakeIt = false;
	  if(activeJobsWithNoSamplesCurrentlyBeingProcessed.isEmpty()){
		  activeJobsWithNoSamplesCurrentlyBeingProcessed.add(jobService.getJobByJobId(40));
		  activeJobsWithNoSamplesCurrentlyBeingProcessed.add(jobService.getJobByJobId(46));
		  fakeIt = true;
	  }
	  //**************will currently be none, so add two jobs.
	  
	  List<Job> activeJobsWithNoSamplesCurrentlyBeingProcessedAndAnalysisNotBegun = new ArrayList<Job>();
	  Map<Job, List<SampleSource>> jobCellLibraryMap = new HashMap<Job, List<SampleSource>>();
	  Map<SampleSource, Sample> cellLibraryLibraryMap = new HashMap<SampleSource, Sample>();
	  Map<SampleSource, Sample> cellLibraryMacromoleculeMap = new HashMap<SampleSource, Sample>();
	  Map<SampleSource, Sample> cellLibraryPUMap = new HashMap<SampleSource, Sample>();
	  Map<SampleSource, Run> cellLibraryRunMap = new HashMap<SampleSource, Run>();	  
	  Map<SampleSource, Boolean> cellLibraryInAnalysisMap = new HashMap<SampleSource, Boolean>();
	  Map<SampleSource, String> cellLibraryInAnalysisCommentMap = new HashMap<SampleSource,String>();
	  
	  for(Job job : activeJobsWithNoSamplesCurrentlyBeingProcessed){
		  //make certain that aggregateAnalysis has not yet been kicked-off  for this job
		  if(jobService.isAggregationAnalysisBatchJob(job)){
			  continue;
		  }
		  List<SampleSource> preprocessedCellLibraries = sampleService.getPreprocessedCellLibraries(job);//a preprocessed library is one that is sequenced and aligned
		  //*******************will currently be none, so fake for some data
		  if(fakeIt){
			  for(SampleSource ss : sampleService.getCellLibrariesForJob(job)){
				  preprocessedCellLibraries.add(ss);
			  }
		  }
		  //*******************will currently be none, so fake for some data		  
		  if(preprocessedCellLibraries.size()>0){
			  activeJobsWithNoSamplesCurrentlyBeingProcessedAndAnalysisNotBegun.add(job);
			  Collections.sort(preprocessedCellLibraries, new SampleSourceComparator());//sort the SampleSourceList
			  jobCellLibraryMap.put(job, preprocessedCellLibraries);
			  //fill up the maps
			  for(SampleSource cellLibrary : preprocessedCellLibraries){//TODO this can be a service
				  
				  Sample library = sampleService.getLibrary(cellLibrary);
				  cellLibraryLibraryMap.put(cellLibrary, library);
				  Sample macromolecule = library.getParent();
				  if(macromolecule == null || macromolecule.getSampleId() == null){
					  macromolecule = new Sample();
					  macromolecule.setName("User-Supplied Library");
				  }
				  cellLibraryMacromoleculeMap.put(cellLibrary, macromolecule);
				  
				  Sample cell = sampleService.getCell(cellLibrary);
				  Sample platformUnit = null;
				  try{
					  platformUnit = sampleService.getPlatformUnitForCell(cell);
				  }
				  catch(Exception e){//should not occur
					  platformUnit = new Sample();
					  platformUnit.setName("Not Found");
					  logger.warn("Expected a platformUnit belonging to cell with Id of " + cell.getSampleId()); 
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
				  
				  Boolean b = null;
				  try{
					  b = new Boolean(sampleService.isMetaCellLibraryInAggregateAnalysis(cellLibrary));
				  }catch(Exception e){ }
				  cellLibraryInAnalysisMap.put(cellLibrary, b);//Be careful in the jsp, as this Boolean can be null (not recorded yet)
				  
				  List<MetaMessage> inAnalysisCommentList = sampleService.getMetaInAggregateAnalysisComments(cellLibrary.getSampleSourceId());
				  if(inAnalysisCommentList.size()<=0){
					  cellLibraryInAnalysisCommentMap.put(cellLibrary, "");
				  }
				  else{
					  cellLibraryInAnalysisCommentMap.put(cellLibrary, inAnalysisCommentList.get(0).getValue());
				  }
			  }  
		  }
	  }
	  //sort by job ID desc
	  Collections.sort(activeJobsWithNoSamplesCurrentlyBeingProcessedAndAnalysisNotBegun, new JobIdComparator()); 

	  m.addAttribute("jobs", activeJobsWithNoSamplesCurrentlyBeingProcessedAndAnalysisNotBegun);
	  m.addAttribute("jobCellLibraryMap", jobCellLibraryMap);
	  m.addAttribute("cellLibraryLibraryMap", cellLibraryLibraryMap);
	  m.addAttribute("cellLibraryMacromoleculeMap", cellLibraryMacromoleculeMap);
	  m.addAttribute("cellLibraryPUMap", cellLibraryPUMap);
	  m.addAttribute("cellLibraryRunMap", cellLibraryRunMap);
	  m.addAttribute("cellLibraryInAnalysisMap", cellLibraryInAnalysisMap);//Be careful in the jsp, as this Boolean can be null (not recorded yet)
	  m.addAttribute("cellLibraryInAnalysisCommentMap", cellLibraryInAnalysisCommentMap);

	  return "task/cellLibraryQC/list";
	}
  

  
  @RequestMapping(value = "/cellLibraryQC/qc", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm-*')")
	public String updateCellLibraryQC(
			@RequestParam("jobId") Integer jobId,
			@RequestParam("maxNumCellLibrariesThatCanBeRecorded") Integer maxNumCellLibrariesThatCanBeRecorded,
			@RequestParam("sampleSourceId") List<Integer> sampleSourceIdList,
			@RequestParam("startAnalysis") String startAnalysis,
		    ModelMap m) {
	  
	  //1. check the most basic parameters
	  if(jobId==null){
		  waspErrorMessage("task.cellLibraryqc_invalid_jobId.error");
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  Job job = jobService.getJobByJobId(jobId);
	  if(job==null || job.getJobId()==null){
		  waspErrorMessage("task.cellLibraryqc_jobNotFound.error");
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
	  if(sampleSourceIdList==null || sampleSourceIdList.isEmpty()){//an empty list should never be sent by the webpage
		  waspErrorMessage("task.cellLibraryqc_invalid_samplesource.error");
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  if(startAnalysis == null || "".equals(startAnalysis)){
		  waspErrorMessage("task.cellLibraryqc_invalid_startAnalysis.error");//shouldn't occur as jovascript should prevent, but....
		  return "redirect:/task/cellLibraryQC/list.do";
	  }
	  if( !"Now".equalsIgnoreCase(startAnalysis) && !"Later".equalsIgnoreCase(startAnalysis) && !"Never".equalsIgnoreCase(startAnalysis) ){
		  waspErrorMessage("task.cellLibraryqc_invalidValues_startAnalysis.error");//shouldn't occur as jovascript should prevent, but....
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
		  else if(!qcStatus.trim().equalsIgnoreCase("INCLUDE") && !qcStatus.trim().equalsIgnoreCase("EXCLUDE")){//should not occur
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
	  int numCellLibrariesRecordedAsInclude = 0;
	  int numCellLibrariesRecordedAsExclude = 0;
	  for(int i = 0; i < totalRecordsToRecord; i++){
		  String qcStatus = qcStatusList.get(i);
		  String trimmedComment = commentList.get(i);
		  if( "EXCLUDE".equals(qcStatus) && (trimmedComment.length()==0 || "Provide reason for exclusion".equalsIgnoreCase(trimmedComment)) ){//unlikely, since javascript prevents, but....
			  //record a flash error in the Set errorMessages (to avoid display more than once), since excluding a library-run from analysis requires a valid comment
			  errorMessages.add("task.cellLibraryqc_excludeRequiresComment.error");
			  continue;
		  }
		  try{
			  sampleService.saveMetaCellLibraryInAggregateAnalysisAndComment(sampleSourceList.get(i), qcStatus, trimmedComment);//will deal with insert and update
			  if("EXCLUDE".equals(qcStatus)){numCellLibrariesRecordedAsExclude++;}
			  else if("INCLUDE".equals(qcStatus)){numCellLibrariesRecordedAsInclude++;}
		  }catch(Exception e){
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
	  
	  //finally, deal with kicking off the analysis or terminating the job (don't want to do analysis) (or else postpone decision for analysis for later on)
	  if("Now".equalsIgnoreCase(startAnalysis)){
		  //this can proceed if, for all the cellLibrary records that have been aligned, 
		  //they also have been recorded for in_aggreagte_analysis and at least one is true for in_aggreagate_analysis
		  if(maxNumCellLibrariesThatCanBeRecorded.intValue()==numCellLibrariesRecordedAsInclude + numCellLibrariesRecordedAsExclude 
				  && numCellLibrariesRecordedAsInclude > 0){
			  //kick off analysis
			  jobService.initiateAggregationAnalysisBatchJob(job);
			  waspMessage("task.cellLibraryqc_updateSuccessfulAndAnalysisBegun.label");
		  }
		  else{
			  waspErrorMessage("task.cellLibraryqc_startAnalysisNotPossibleNow.error");
			  return "redirect:/task/cellLibraryQC/list.do";
		  }
	  }
	  else if("Never".equalsIgnoreCase(startAnalysis)){//do not want analysis, so don't care
		  //To terminate the job without analysis, you must mark each record as Exclude and provide a reason for exclusion
		  if(maxNumCellLibrariesThatCanBeRecorded.intValue()==numCellLibrariesRecordedAsExclude){
			  //terminate job
			  try{
				  jobService.terminate(job);// throws WaspMessageBuildingException;
				  waspMessage("task.cellLibraryqc_updateSuccessfulAndJobTerminated.label");
			  }
			  catch(WaspMessageBuildingException e){
				  waspErrorMessage("task.cellLibraryqc_terminateJobUnexpectedlyFailed.error");
				  return "redirect:/task/cellLibraryQC/list.do";
			  }
		  }
		  else{
			  waspErrorMessage("task.cellLibraryqc_terminateJobNotPossibleNow.error");
			  return "redirect:/task/cellLibraryQC/list.do";
		  }
	  }
	  else if("Later".equalsIgnoreCase(startAnalysis)){
		  ;//do nothing
		  waspMessage("task.cellLibraryqc_updateSuccessfulAndAnalysisNotBegun.label");
	  }
	  //waspMessage("task.cellLibraryqc_update_success.label");	
	  return "redirect:/task/cellLibraryQC/list.do";
  }
}

