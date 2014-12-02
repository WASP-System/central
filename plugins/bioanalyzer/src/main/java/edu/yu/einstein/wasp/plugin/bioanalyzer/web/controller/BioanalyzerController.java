/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AccountsService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.viewpanel.Action;
import edu.yu.einstein.wasp.viewpanel.Action.CallbackFunctionType;
import edu.yu.einstein.wasp.viewpanel.Action.GroupActionAlignType;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.BioanalyzerService;
import edu.yu.einstein.wasp.plugin.bioanalyzer.software.Bioanalyzer;
import edu.yu.einstein.wasp.plugin.mps.genomebrowser.GenomeBrowserProviding;


@Controller
@RequestMapping("/bioanalyzer")
public class BioanalyzerController extends WaspController {
	@Autowired
	protected JobDraftresourcecategoryDao jobDraftresourcecategoryDao;
	@Autowired
	protected AccountsService accountsService;
	@Autowired
	protected AuthenticationService authenticationService;
	@Autowired
	private BioanalyzerService bioanalyzerService;
	@Autowired
	private FileService fileService;
	@Autowired
	private JobService jobService;
	@Autowired
	private JobDraftService jobDraftService;
	@Autowired
	private LabService labService;
	@Autowired
	private MessageServiceWebapp messageService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private StrategyService strategyService;
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private Bioanalyzer bioanalyzer;
	
	@Autowired
	private FileType pdfFileType;//pdf file 
	
	
	@RequestMapping(value="/displayDescription", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		logger.debug("service said: " + bioanalyzerService.performAction());
		return "bioanalyzer/description";
	}
	
	final public String[] defaultPageFlow = {"/jobsubmit/modifymeta/{n}","/jobsubmit/samples/{n}","/jobsubmit/cells/{n}","/jobsubmit/verify/{n}","/jobsubmit/submit/{n}","/jobsubmit/ok"};
	
	@Transactional
	public String nextPage(JobDraft jobDraft) {
		String[] pageFlowArray = workflowService.getPageFlowOrder(workflowService.getWorkflowDao().getWorkflowByWorkflowId(jobDraft.getWorkflowId()));
		if (pageFlowArray.length == 0)
			pageFlowArray = defaultPageFlow;

		
		String context = request.getContextPath();
		String uri = request.getRequestURI();
	
		// strips context, lead slash ("/"), spring mapping
		String currentMapping = uri.replaceFirst(context, "").replaceFirst("\\.do.*$", "");

		int found = -1;
		for (int i=0; i < pageFlowArray.length; i++) {
			String page = pageFlowArray[i];
			page = page.replaceAll("\\{n\\}", ""+jobDraft.getId());
	
			if (currentMapping.equals(page)) {
				found = i;
				if (found == pageFlowArray.length - 1){
					waspErrorMessage("jobDraft.noMoreWorkflowPages.error");
					return "redirect:/dashboard.do";
				}
				break;
			}
		}

		int currentWorkflowIndex = found + 1;
		String targetPage = pageFlowArray[currentWorkflowIndex] + ".do"; 
		targetPage = targetPage.replaceAll("\\{n\\}", ""+jobDraft.getId());
		logger.debug("Next page: " + targetPage);

		return "redirect:" + targetPage;
	}
	/**
	 * getPageFlowMap
	 * @param jobDraft - jobdraft (used to get workflow)
	 *
	 * requires request to stop user from going on future screens
	 *
	 * sets request attribute "forcePageTitle" to current page title
	 * returns the pageflow map for nav bar
	 *
	 */
	@Transactional
	protected List<String[]> getPageFlowMap(JobDraft jobDraft) {
		String[] pageFlowArray = workflowService.getPageFlowOrder(jobDraft.getWorkflow());
		if (pageFlowArray.length == 0){
			logger.debug("No page flow defined so using default page flow");
			pageFlowArray = defaultPageFlow;
		}
		
		String context = request.getContextPath();
		String uri = request.getRequestURI();
	
		// strips context, lead slash ("/"), spring mapping
		String currentMapping = uri.replaceFirst(context, "").replaceFirst("\\.do.*$", "");


		List<String[]> rt = new ArrayList<String[]>(); 
		//add the jobDraft's start page breadcrumb (3/29/13; dubin)
		String jobDraftStartPageBreadcrumbMessage = messageService.getMessage("jobDraft.startPageBreadcrumbMessage.label");
		if(jobDraftStartPageBreadcrumbMessage != null && !"".equals(jobDraftStartPageBreadcrumbMessage)){
			String[] startPage = {"/jobsubmit/modify/"+jobDraft.getId().toString(), jobDraftStartPageBreadcrumbMessage};
			rt.add(startPage);
		}
		
		
		for (int i=0; i < pageFlowArray.length -1; i++) {
			String page = pageFlowArray[i];
			String mapPage = page.replaceAll("^/", "");
			mapPage = mapPage.replaceAll("/\\{n\\}", "");


			String expandPage = page.replaceAll("\\{n\\}", ""+jobDraft.getId());
			expandPage = expandPage.replace("^/", "");//added 6-11-14; dubin to repair the breadcrumbs anchor on jobsubmission pages (see additional change a few lines below)
			//logger.debug("page: " + page);
			//logger.debug("mapPage: " + mapPage);
			//logger.debug("expandPage: " + expandPage);
			//logger.debug("currentMapping: " + currentMapping);
			
			if (currentMapping.equals(expandPage)) {
				request.setAttribute("forcePageTitle", getPageTitle(mapPage, jobDraft.getWorkflow().getIName()));
				break;

			}

			expandPage = expandPage.replaceAll("^/", "");//added 6-11-14; dubin yep, do this yet again, to repair the breadcrumbs anchor on jobsubmission pages
			//logger.debug("expandPage again: " + expandPage);
			String[] r = {expandPage, getPageTitle(mapPage, jobDraft.getWorkflow().getIName())};
			rt.add(r);
	
		}

		return rt; 
	}

	/**
	 * getPageTitle gets page title for jobsubmission page corresponding to workflow
	 * 
	 * @param pageDef 
	 * @parm workflowIname
	 *
	 * getPageTitle expect [workflowIName].[pageDef].label
	 * where page is in w/o leading slash or jobDraftId
	 *
	 */
	@Transactional
	private String getPageTitle(String pageDef, String workflowIName) {
		Locale locale=(Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		String code=workflowIName+"."+pageDef+".label";
			
		try {	
		
		String pageTitle=DBResourceBundle.MESSAGE_SOURCE.getMessage(code, null, locale);
		
		if (pageTitle!=null) {		
			return pageTitle;
		}
		
		} catch (Throwable e) {
			//log.error("Cant get page title from uifield "+tilesDef+"|"+workflowIName+". Falling back to default page name ",e);
		}
		
		return pageDef;
	}
	/**
	 * Returns true if the current logged in user is the job drafter, the jobDraft status is pending
	 * and the jobDraft object is not null and has a not-null jobDraftId
	 * @param jobDraft
	 * @return boolean
	 */
	protected boolean isJobDraftEditable(JobDraft jobDraft){
		User me = authenticationService.getAuthenticatedUser();
		return isJobDraftEditable(jobDraft, me);
	}
	
	/**
	 * Returns true if the current logged in user is the job drafter, the jobDraft status is pending
	 * and the jobDraft object is not null and has a not-null jobDraftId
	 * @param jobDraft
	 * @return boolean
	 */
	protected boolean isJobDraftEditable(JobDraft jobDraft, User me){
		if (jobDraft == null || jobDraft.getId() == null){
			waspErrorMessage("jobDraft.jobDraft_null.error");
			return false;
		}
		
		// check if i am the drafter
		if (me.getId().intValue() != jobDraft.getUserId().intValue()) {
			waspErrorMessage("jobDraft.user_incorrect.error");
			return false;
		}
		
		// check that the status is PENDING
		if (! jobDraft.getStatus().equals("PENDING")) {
			waspErrorMessage("jobDraft.not_pending.error");
			return false;
		}
		return true;
	}
	@RequestMapping(value="/chipChoiceAndInfo/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipChoiceAndInfoForm (@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if ( ! isJobDraftEditable(jobDraft) ){
			return "redirect:/dashboard.do";
		}
		m.put("jobDraft", jobDraft);
		
		Workflow wf = jobDraft.getWorkflow();
		if(!wf.getIName().equalsIgnoreCase("bioanalyzer")){
			waspErrorMessage("bioanalyzer.chipChoiceAndInfo_workflowError.error");
			return "redirect:/dashboard.do";
		}
		
		List<String> availableBioanalyzerChipList = bioanalyzerService.getAvailableBioanalyzerChips(wf);//new ArrayList<String>();
		m.put("availableBioanalyzerChipList", availableBioanalyzerChipList);
		m.put("userSelectedBioanalyzerChip", bioanalyzerService.getMeta(jobDraft, bioanalyzerService.bioanalyzerChipMeta));
		m.put("assayLibrariesAreFor", bioanalyzerService.getMeta(jobDraft, bioanalyzerService.bioanalyzerAssayLibrariesAreForMeta));
		m.put("assayLibrariesAreForToolTip", messageService.getMessage("bioanalyzer.chipChoiceAndInfo_assayLibrariesAreForToolTip.label"));
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		return "bioanalyzer/chipChoiceAndInfo";
	}
	@RequestMapping(value="/chipChoiceAndInfo/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showChipChoiceAndInfoFormPost (@PathVariable("jobDraftId") Integer jobDraftId, 
				@RequestParam(value="bioanalyzerChip") String bioanalyzerChip,			 
				@RequestParam(value="assayLibrariesAreFor") String assayLibrariesAreFor, 
				ModelMap m) {
		
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if ( ! isJobDraftEditable(jobDraft) ){
			return "redirect:/dashboard.do";
		}
		m.put("jobDraft", jobDraft);
		
		Workflow wf = jobDraft.getWorkflow();
		if(!wf.getIName().equalsIgnoreCase("bioanalyzer")){
			waspErrorMessage("bioanalyzer.chipChoiceAndInfo_workflowError.error");
			return "redirect:/dashboard.do";
		}
		
		if(bioanalyzerChip==null || bioanalyzerChip.isEmpty() || bioanalyzerChip.equals("-1") ||assayLibrariesAreFor==null || assayLibrariesAreFor.trim().isEmpty()){
			
			if(bioanalyzerChip==null || bioanalyzerChip.isEmpty() || bioanalyzerChip.equals("-1")){
				m.put("chipError", messageService.getMessage("bioanalyzer.chipChoiceAndInfo_chipMissing.error"));
			}
			if(assayLibrariesAreFor==null || assayLibrariesAreFor.trim().isEmpty()){
				m.put("assayLibrariesAreForError", messageService.getMessage("bioanalyzer.chipChoiceAndInfo_assayMissing.error"));
			}
			
			List<String> availableBioanalyzerChipList = bioanalyzerService.getAvailableBioanalyzerChips(wf);			
			m.put("availableBioanalyzerChipList", availableBioanalyzerChipList);
			m.put("userSelectedBioanalyzerChip", bioanalyzerChip);
			m.put("assayLibrariesAreFor", assayLibrariesAreFor);
			m.put("assayLibrariesAreForToolTip", messageService.getMessage("bioanalyzer.chipChoiceAndInfo_assayLibrariesAreForToolTip.label"));
			m.put("pageFlowMap", getPageFlowMap(jobDraft));	
			waspErrorMessage("bioanalyzer.chipChoiceAndInfo_errorsExist.error");
			return "bioanalyzer/chipChoiceAndInfo";
		}
		
		bioanalyzerService.saveOrUpdateJobDraftMeta(jobDraft, bioanalyzerService.bioanalyzerChipMeta, bioanalyzerChip);
		bioanalyzerService.saveOrUpdateJobDraftMeta(jobDraft, bioanalyzerService.bioanalyzerAssayLibrariesAreForMeta, assayLibrariesAreFor.trim());
		
		//have to do this next line somewhere; this is best place for it:
		//set isAnalysisSelected to false; bioanalyzer workflow does NOT require it (default setting is true)
		jobDraftService.setIsAnalysisSelected(jobDraft, false);
		
		//set jobdraftresourcecategory
		ResourceCategory bioanalyzerRC = resourceService.getResourceCategoryDao().getResourceCategoryByIName("bioanalyzer");
		List<JobDraftresourcecategory> jdrcList = jobDraft.getJobDraftresourcecategory();
		boolean jdrcRecorded = false;
		for(JobDraftresourcecategory jdrc : jdrcList){
			if(jdrc.getJobDraftId().intValue()==jobDraft.getId().intValue() && jdrc.getResourcecategoryId().intValue()==bioanalyzerRC.getId().intValue()){
				jdrcRecorded=true;
			}
		}
		if(!jdrcRecorded){
			JobDraftresourcecategory newJdr = new JobDraftresourcecategory();
			newJdr.setJobDraftId(jobDraftId);
			newJdr.setResourcecategoryId(bioanalyzerRC.getId());
			jobDraftresourcecategoryDao.save(newJdr);
		}
		waspMessage("bioanalyzer.chipChoiceAndInfo_updateSuccessfullyRecorded.error");
		return nextPage(jobDraft);
	}
	
	
	
	  @Transactional
	  @RequestMapping(value="/job/{jobId}/fileUploadManager", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobFileUploadPage(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		  
		  Job job = jobService.getJobByJobId(jobId);
		  if(job.getId()==null){
		   	logger.warn("Bioanalyzer job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		  }
		  populateFileUploadPage(job, m);
		  return "bioanalyzer/fileUploadManager";
	  }

	  @Transactional
	  private void populateFileUploadPage(Job job, ModelMap m){
		
			m.addAttribute("job", job);
			
			m.addAttribute("userIsFacilityPersonel", false);
			if(authenticationService.hasRole("su")||authenticationService.hasRole("fm")||authenticationService.hasRole("ft")
				||authenticationService.hasRole("sa")||authenticationService.hasRole("ga")||authenticationService.hasRole("da")){
				m.addAttribute("userIsFacilityPersonel", true);
			}
			
			List<FileGroup> fileGroups = new ArrayList<FileGroup>();
			Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
			List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
			for(JobFile jf: job.getJobFile()){
				FileGroup fileGroup = jf.getFile();//returns a FileGroup
				//no need to explicitly exclude quotes and invoices; as of 8/22/13 such files are stored through acctQuoteMeta or acctInvoiceMeta
				fileGroups.add(fileGroup);
				List<FileHandle> fileHandles = new ArrayList<FileHandle>();
				for(FileHandle fh : fileGroup.getFileHandles()){
					fileHandles.add(fh);
					String mimeType = fileService.getMimeType(fh.getFileName());
					if(!mimeType.isEmpty()){
						fileHandlesThatCanBeViewedList.add(fh);
					}
				}
				fileGroupFileHandlesMap.put(fileGroup, fileHandles);
			}
			String currentJobStatus = jobService.getDetailedJobStatusString(job);
			
			m.addAttribute("fileGroups", fileGroups);
			m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
			m.addAttribute("fileHandlesThatCanBeViewedList", fileHandlesThatCanBeViewedList);
			m.addAttribute("currentJobStatus", currentJobStatus);
	  }
	
	  //Remember, this is an ajax call
	  //Note: we use MultipartHttpServletRequest to be able to upload files using Ajax. See http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/
	  @RequestMapping(value="/job/{jobId}/fileUploadManager", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobFileUploadPostPage(@PathVariable("jobId") final Integer jobId,
			  final MultipartHttpServletRequest request, 
			  final HttpServletResponse response,
			  final ModelMap m) throws SampleTypeException {
	
			Job job = jobService.getJobByJobId(jobId);
			if(job.getId()==null){
			   	logger.debug("Bioanalyzer job unexpectedly not found");
			   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
				return "job/home/message";
			}
			
			List<String> errorMessageList = new ArrayList<String>();
			
			List<MultipartFile> mpFiles = request.getFiles("file_upload");
		    if(mpFiles.isEmpty() || mpFiles.get(0) == null){
		    	String errorMessage = messageService.getMessage("listJobSamples.fileUploadFailed_fileEmpty.error");
		    	logger.warn(errorMessage);
		    	errorMessageList.add(errorMessage);					    	
		    }
		    
		   	String fileDescription = request.getParameter("file_description");
		    fileDescription = fileDescription==null?"":fileDescription.trim();
		    if(fileDescription.isEmpty()){
		    	String errorMessage = messageService.getMessage("listJobSamples.fileUploadFailed_fileDescriptionEmpty.error");
		    	logger.warn(errorMessage);
		    	errorMessageList.add(errorMessage);
		    }
		    
		    //fileIsFromBioanalyzer will appear on the web page only if the web user is a facility member.
		    //if the select box containing fileIsFromBioanalyzer is not on web page, then fileIsFromBioanalyzer is null
		    //if it's on the web page and no selection made, then its value is -1; other valid values are "yes" and "no"
		    String fileIsFromBioanalyzer = request.getParameter("fileIsFromBioanalyzer");
		    
		    /* diagnostic
		    if(fileIsFromBioanalyzer==null){//not on web page, so ignore
		    	logger.debug("dubin 12-1-14 fileIsFromBioanalyzer: NULL");
		    }
		    else{
		    	logger.debug("dubin 12-1-14   fileIsFromBioanalyzer: " + fileIsFromBioanalyzer);
		    }	
		    */	
		    
		    if( fileIsFromBioanalyzer!=null && fileIsFromBioanalyzer.equals("-1") ){//fileIsFromBioanalyzer is on web page, but no selection was made
		    	String errorMessage = messageService.getMessage("bioanalyzer.fileUploadFileIsBioanalyzerFileNotSelected.error");
		    	logger.warn(errorMessage);
		    	errorMessageList.add(errorMessage);
		    }
		    
		    if(!errorMessageList.isEmpty()){//errors exist
		    	m.addAttribute("errorMessageList", errorMessageList);
		    	populateFileUploadPage(job, m);
		    	if(fileIsFromBioanalyzer!=null){
		    		m.addAttribute("userSelectedFileIsFromBioanalyzer", fileIsFromBioanalyzer);
		    	}
		    	m.addAttribute("userProvidedFileDescription", fileDescription);
		    	m.addAttribute("fadingErrorMessage", messageService.getMessage("bioanalyzer.fileUpload_errorsExist.error"));
		    	return "bioanalyzer/fileUploadManager";
		    }		   	    
		   			
			Random randomNumberGenerator = new Random(System.currentTimeMillis());
			try{
				MultipartFile mpFile = mpFiles.get(0);
				FileGroup fileGroup = fileService.uploadJobFile(mpFile, job, fileDescription, randomNumberGenerator);//will upload and perform all database updates
				//if this uploaded file is a bionalayzer file, then set filetype for fileHandle AND set filetype and software for fileGroup; this will enable automatic display of the bionalayzer files on Data page - files tab
				if( fileIsFromBioanalyzer!=null && fileIsFromBioanalyzer.equals("yes") ){
					for(FileHandle fileHandle : fileGroup.getFileHandles()){
						fileHandle.setFileType(pdfFileType);
						fileService.getFileHandleDao().save(fileHandle);
					}
					fileGroup.setFileType(pdfFileType);
					fileGroup.setSoftwareGeneratedBy(bioanalyzer);
					fileService.getFileGroupDao().save(fileGroup);
				}
				m.addAttribute("fadingSuccessMessage", messageService.getMessage("listJobSamples.fileUploadedSuccessfully.label"));
			} catch(FileUploadException e){
				String errorMessage = messageService.getMessage("listJobSamples.fileUploadFailed.error");
				logger.warn(errorMessage);
				errorMessageList.add(errorMessage);
				m.addAttribute("errorMessage", errorMessageList);
				if(fileIsFromBioanalyzer!=null){
		    		m.addAttribute("userSelectedFileIsFromBioanalyzer", fileIsFromBioanalyzer);
		    	}
		    	m.addAttribute("userProvidedFileDescription", fileDescription);
		    	m.addAttribute("fadingErrorMessage", messageService.getMessage("bioanalyzer.fileUpload_errorsExist.error"));		    	
			}
			populateFileUploadPage(job, m);
			return "bioanalyzer/fileUploadManager";		
	  }
	  
	  @Transactional
	  @RequestMapping(value="/{jobId}/markBioanalyzerJobAsCompleted", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')")
	  public String markBioanalyzerJobAsCompleted(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		  
		  Job job = jobService.getJobByJobId(jobId);
		  if(job.getId()==null){
		   	logger.warn("Bioanalyzer job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		  }
		  //mark it as completed and return to starting page
		  logger.warn("dubin 12-1-14 in markBioanalyzerJobAsCompleted");
		  logger.warn("dubin 12-1-14 in markBioanalyzerJobAsCompleted");
		  logger.warn("dubin 12-1-14 in markBioanalyzerJobAsCompleted");
		  logger.warn("dubin 12-1-14 in markBioanalyzerJobAsCompleted");
		  logger.warn("dubin 12-1-14 in markBioanalyzerJobAsCompleted");
		  try{
			  jobService.sendNotifyJobCompleteMessage(job);
			  m.addAttribute("fadingSuccessMessage", messageService.getMessage("bioanalyzer.fileUpload_jobMarkedAsCompleted.label"));
			  
		  }catch(Exception e){
			  m.addAttribute("fadingErrorMessage", messageService.getMessage("bioanalyzer.fileUpload_jobMarkedAsCompletedUnexpectedlyFailed.label"));
			  logger.debug("error marking bioanalyzer job complete: " + e.getMessage());
		  }	  
		  
		  populateFileUploadPage(job, m);
		  return "bioanalyzer/fileUploadManager";
	  }
}
