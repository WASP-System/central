/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AccountsService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.plugin.bioanalyzer.service.BioanalyzerService;


@Controller
@RequestMapping("/bioanalyzer")
public class BioanalyzerController extends WaspController {
	@Autowired
	protected AccountsService accountsService;
	@Autowired
	protected AuthenticationService authenticationService;
	@Autowired
	private BioanalyzerService bioanalyzerService;
	@Autowired
	private JobDraftService jobDraftService;
	@Autowired
	private LabService labService;
	@Autowired
	private MessageServiceWebapp messageService;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private StrategyService strategyService;
	@Autowired
	private WorkflowService workflowService;
	
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
		List<String> availableBioanalyzerChipList = new ArrayList<String>();
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChipHighSensitivity.label"));
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip7500.label"));
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip1000.label"));
		m.put("availableBioanalyzerChipList", availableBioanalyzerChipList);
		
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

		if(bioanalyzerChip==null || bioanalyzerChip.isEmpty() || bioanalyzerChip.equals("-1") ||assayLibrariesAreFor==null || assayLibrariesAreFor.trim().isEmpty()){
			
			if(bioanalyzerChip==null || bioanalyzerChip.isEmpty() || bioanalyzerChip.equals("-1")){
				m.put("chipError", messageService.getMessage("bioanalyzer.chipChoiceAndInfo_chipMissing.error"));
			}
			if(assayLibrariesAreFor==null || assayLibrariesAreFor.trim().isEmpty()){
				m.put("assayLibrariesAreForError", messageService.getMessage("bioanalyzer.chipChoiceAndInfo_assayMissing.error"));
			}
			m.put("jobDraft", jobDraft);
			m.put("userSelectedBioanalyzerChip", bioanalyzerChip);
			m.put("", assayLibrariesAreFor);
			m.put("pageFlowMap", getPageFlowMap(jobDraft));			
			List<String> availableBioanalyzerChipList = new ArrayList<String>();
			availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChipHighSensitivity.label"));
			availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip7500.label"));
			availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip1000.label"));
			m.put("availableBioanalyzerChipList", availableBioanalyzerChipList);			
			waspErrorMessage("bioanalyzer.chipChoiceAndInfo_errorsExist.error");
			return "bioanalyzer/chipChoiceAndInfo";
		}
		
		bioanalyzerService.saveOrUpdateMeta(jobDraft, bioanalyzerService.bioanalyzerChipMeta, bioanalyzerChip);
		bioanalyzerService.saveOrUpdateMeta(jobDraft, bioanalyzerService.bioanalyzerAssayLibrariesAreForMeta, assayLibrariesAreFor.trim());
		
		waspMessage("bioanalyzer.chipChoiceAndInfo_updateSuccessfullyRecorded.error");
		return nextPage(jobDraft);
	}
	/*
	@RequestMapping(value="/submitSampleAndUploadFiles/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String submitSampleAndUploadFiles(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m){
		
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		List<SampleDraft> sampleDraftList = jobDraft.getSampleDraft();
		String[] roles = new String[1];
		roles[0] = "lu";
		List<SampleSubtype> sampleSubtypeList = sampleService.getSampleSubtypesForWorkflowByRole(jobDraft.getWorkflowId(), roles);
		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		for(JobDraftFile jdf: jobDraft.getJobDraftFile()){
			FileGroup fileGroup = jdf.getFileGroup();
			fileGroups.add(fileGroup);
			List<FileHandle> fileHandles = new ArrayList<FileHandle>();
			for(FileHandle fh : fileGroup.getFileHandles()){
				fileHandles.add(fh);
			}
			fileGroupFileHandlesMap.put(fileGroup, fileHandles);
		}
		m.addAttribute("jobDraft", jobDraft);
		m.addAttribute("sampleDraftList", sampleDraftList);
		m.addAttribute("sampleSubtypeList", sampleSubtypeList);
		//m.addAttribute("pageFlowMap", getPageFlowMap(jobDraft));
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		//m.addAttribute("adaptorSetsUsedOnThisJobDraft", getAdaptorSets(jobDraft));
		//return "jobsubmit/sample";
		return "bioanalyzer/submitSampleAndUploadFiles";
	}
	*/
	@RequestMapping(value="/create", method=RequestMethod.GET)
	public String createNewBioanalyzerJobGet(ModelMap m){
		/*
		Workflow bioanalyzerWorkflow = workflowService.getWorkflowDao().getWorkflowByIName("bioanalyzer");
		if(bioanalyzerWorkflow.getId()==null){
			waspErrorMessage("bioanalyzer.create_thisWorkflowUnexpectedlyNotFound.error");
			return "redirect:/dashboard.do";
		}
		*/
		prepareCreateBioanalyzerForm(m);
		
		if ( ((List<Lab>)m.get("labList")).isEmpty()){
			waspErrorMessage("bioanalyzer.create_labList.error");
			return "redirect:/dashboard.do";
		}
		
		SampleSubtype sampleSubtype = sampleService.getSampleSubtypeDao().getSampleSubtypeByIName("bioanalyzerLibrarySample");
		if (sampleSubtype.getId() == null){
			waspErrorMessage("bioanalyzer.create_sampleSubtype.error");
			return "redirect:/dashboard.do";
		}
		
		List<SampleMeta> normalizedMeta = new ArrayList<SampleMeta>();
		try {
			normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, SampleMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleMeta' with sampleSubtype of bioanalyzerLibrarySample:" + e.getMessage());
			waspErrorMessage("bioanalyzer.create_sampleMeta.error");
			return "redirect:/dashboard.do";
		}
		Sample sample = new Sample();
		sample.setSampleMeta(normalizedMeta);
			
		//make web responsive to a list of samples, even though this method only sends one,
		// because in the post to this method, we may have a list of many samples
		List<Sample> librarySampleList = new ArrayList<Sample>();
		librarySampleList.add(sample);		
		m.addAttribute("librarySampleList", librarySampleList);		
		
		return "bioanalyzer/create";
	}
	
	private void prepareCreateBioanalyzerForm(ModelMap m){
		
		User me = authenticationService.getAuthenticatedUser();

		List <LabUser> labUserAllRoleList = me.getLabUser();
		List <Lab> labList = new ArrayList<Lab>();
		for (LabUser lu: labUserAllRoleList) {
			String roleName =	lu.getRole().getRoleName();
			if (roleName.equals("lu") || roleName.equals("lm") || roleName.equals("pi")) {
				labList.add(lu.getLab());
			}
		}
		m.put("labList", labList);
		Map<Lab, String> labPiInstitutionMap = new HashMap<Lab, String>();
		for(Lab lab : labList){
			String institution = labService.getInstitutionOfLabPI(lab);
			if(!institution.isEmpty()){
				labPiInstitutionMap.put(lab, institution);
			}
		}
		m.put("labPiInstitutionMap", labPiInstitutionMap);
		
		List<String> availableBioanalyzerChipList = new ArrayList<String>();
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChipHighSensitivity.label"));
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip7500.label"));
		availableBioanalyzerChipList.add(messageService.getMessage("bioanalyzer.create_bioanalyzerChip1000.label"));
		m.put("availableBioanalyzerChipList", availableBioanalyzerChipList);
		
		List<Workflow> workflowList = new ArrayList<Workflow>();
		for(Workflow workflow : workflowService.getWorkflowDao().findAll()){
			if(!workflow.getIName().equalsIgnoreCase("bioanalyzer")){//exclude this from list
				workflowList.add(workflow);
			}
		}
		class WorkflowNameComparator implements Comparator<Workflow> {
		    @Override
		    public int compare(Workflow arg0, Workflow arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		Collections.sort(workflowList, new WorkflowNameComparator());
		m.put("workflowList", workflowList);
	}
	
	@RequestMapping(value="/getGrantsForLab.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public void getGrantsForLab(HttpServletResponse response) {
		
		//THIS IS AN AJAX CALL FROM WEB
		
		Lab lab = labService.getLabByLabId(Integer.parseInt(request.getParameter("selectedLabId")));
		List <AcctGrant> grantsForTheRequestedLab = accountsService.getNonExpiredGrantsForLab(lab);
		Map<Integer, String> grants = new HashMap<>();
		for (AcctGrant grant: grantsForTheRequestedLab){
			String value = grant.getCode();
			if (grant.getName() != null && !grant.getName().isEmpty())
				value += " (" + grant.getName() + ")";
			grants.put(grant.getId(), value);
		}
		try{
			outputJSON(grants, response);
		}catch(Exception e){}
	}
	
	private List<AcctGrant> getGrantsForLab(Integer labId) {		
		Lab lab = labService.getLabByLabId(labId);
		return accountsService.getNonExpiredGrantsForLab(lab);		 
	}

	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	
	public String createNewBioanalyzerJobPost( @RequestParam(value="labId") Integer labId,
			@RequestParam(value="selectGrantId") Integer grantId,
			 @RequestParam(value="bioanalyzerChip") String bioanalyzerChip,			 
			 @RequestParam(value="workflowIdLibrariesAreDesignedFor") Integer workflowIdLibrariesAreDesignedFor,
			 @RequestParam(value="jobName") String jobName,
			ModelMap m){
		
		//NOTE: parameter workflowIdLibrariesAreDesignedFor is the type of workflow that these libraries are for and is a jobMeta attribute. 
		//By contrast, job.workflowId is bioanalyzer workflow
		/*
		Workflow bioanalyzerWorkflow = workflowService.getWorkflowDao().getWorkflowByIName("bioanalyzer");
		if(bioanalyzerWorkflow.getId()==null){
			waspErrorMessage("bioanalyzer.create_thisWorkflowUnexpectedlyNotFound.error");
			return "redirect:/dashboard.do";
		}
		*/
		User me = authenticationService.getAuthenticatedUser();		
		
		SampleSubtype sampleSubtype = sampleService.getSampleSubtypeDao().getSampleSubtypeByIName("bioanalyzerLibrarySample");		
		if (sampleSubtype.getId() == null){
			waspErrorMessage("bioanalyzer.create_sampleSubtype.error");
			return "redirect:/dashboard.do";
		}		
		SampleType sampleType = sampleSubtype.getSampleType();		
		
		boolean errorsExist = false;
		
		logger.debug("labId: " + labId.toString());
		if(labId == -1){
			m.addAttribute("labError", messageService.getMessage("bioanalyzer.create_labMissing.error"));
			errorsExist=true;
		}
		//confirm user is labmember of selected lab
		if(!labService.isUserLabMember(labService.getLabByLabId(labId), me)){
			waspErrorMessage("bioanalyzer.create_userUnexpectedlyNotMemberOfSelectedLab.error");
			return "redirect:/dashboard.do";
		}
		logger.debug("grantId: " + grantId.toString());
		if(grantId == -1 || grantId == 0){
			m.addAttribute("grantSelectError", messageService.getMessage("bioanalyzer.create_grantMissing.error"));
			errorsExist=true;
		}
		logger.debug("bioanalyzerChip: " + bioanalyzerChip);
		if(bioanalyzerChip.equalsIgnoreCase("-1") || bioanalyzerChip.isEmpty()){
			m.addAttribute("chipError", messageService.getMessage("bioanalyzer.create_chipMissing.error"));
			errorsExist=true;
		}
		
		logger.debug("workflowIdLibrariesAreDesignedFor: " + workflowIdLibrariesAreDesignedFor.toString());
		if(workflowIdLibrariesAreDesignedFor == -1){
			m.addAttribute("workflowError", messageService.getMessage("bioanalyzer.create_workflowLibrariesDesignedForMissing.error"));
			errorsExist=true;
		}
		logger.debug("jobName: " + jobName);
		if(jobName.isEmpty() || jobName.trim().isEmpty()){
			m.addAttribute("jobNameError", messageService.getMessage("bioanalyzer.create_jobNameMissing.error"));
			errorsExist=true;
		}
		
		//deal with the new library requests
		String[] libraryNamesAsStringArray = request.getParameterValues("sampleName");
		
		List<String> libraryNamesAsList = new ArrayList<String>();//used to make sure names only used once (see below)
		
		int counter = -1;
		List<String> libraryErrorList = new ArrayList<String>();//errors associated with the samples (libraries) only
		List<Sample> librarySampleList = new ArrayList<Sample>();
		
		for(String libraryName : libraryNamesAsStringArray){	
			
			counter++;			
			String errorsForThisSample = "";			
			Sample library = new Sample();			
			library.setName(libraryName.trim());
			logger.debug("libraryName: " + libraryName);
			
			//get sampleMeta and in process, check it for errors
			List<SampleMeta> sampleMetaList = new ArrayList<SampleMeta>();
			DataBinder dataBinderForMeta = new DataBinder(library, "sample");
			BindingResult resultForMeta = dataBinderForMeta.getBindingResult();
			try {
				sampleMetaList.addAll(SampleAndSampleDraftMetaHelper.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, resultForMeta, SampleMeta.class, counter));
			} catch (MetadataTypeException e) {
				logger.warn("Could not get meta for class 'SampleMeta' with sampleSubtype of bioanalyzerLibrarySample:" + e.getMessage());
				
			}
			library.setSampleMeta(sampleMetaList);
			//for testing only
			logger.debug("sampleMetaList size = " + sampleMetaList.size() + " and its output follows: ");			
			for(SampleMeta sm : sampleMetaList){
				logger.debug(sm.getK() + ":" + sm.getV());
			}
			if(libraryRowIsCompletelyEmpty(library)){//sample name is absent and all the sample meta is blank; ignore this row
				continue;
			}
			
			//OK the sample row is NOT COMPLETELY EMPTY, so we have to deal with it
			//first, deal with sample name errors
			if(library.getName().isEmpty()){				
				String sampleNameEmptyMessage = messageService.getMessage("bioanalyzer.create_libraryNameEmpty.error");
				errorsForThisSample += errorsForThisSample.isEmpty()?sampleNameEmptyMessage : "; "+sampleNameEmptyMessage;
			}
			else{				
				//check against the sample names on this form (not yet in database)
				if(libraryNamesAsList.contains(library.getName())){					
					String sampleNameAlreadyUsedOnThisForm = messageService.getMessage("bioanalyzer.create_libraryNameAlreadyUsedOnThisForm.error");
					errorsForThisSample += errorsForThisSample.isEmpty()? sampleNameAlreadyUsedOnThisForm :"; "+sampleNameAlreadyUsedOnThisForm;
				}				
				libraryNamesAsList.add(library.getName()); //add to this list  if library name is NOT empty
			}
			
			//second deal with sampleMeta errors, which are currently stored in resultForMeta (a BindingResult object)
			if(resultForMeta.hasErrors()){			
				List<FieldError> fieldErrors = resultForMeta.getFieldErrors();
				for(FieldError fe : fieldErrors){
					logger.debug("getCode: " + fe.getCode());//this is something like chipseqDna.fragmentSize.error
					logger.debug("getDefaultMessage: " +fe.getDefaultMessage());//
					logger.debug("getField: " +fe.getField());//
					logger.debug("getObjectName: " +fe.getObjectName());//
					String metaErrorForDisplay = fe.getCode().substring(fe.getCode().indexOf(".")+1);//something like fragmentSize.error
					metaErrorForDisplay = metaErrorForDisplay.replace(".", " ");//something like fragmentSize error
					errorsForThisSample += errorsForThisSample.isEmpty()?metaErrorForDisplay:"; " + metaErrorForDisplay;
				}
			}
			
			librarySampleList.add(library);
			libraryErrorList.add(errorsForThisSample);//yes, execute this line even if no errors exist for this particular sample (add "" here even if no errors for this sample).
			if(!errorsForThisSample.isEmpty()){
				errorsExist = true;				
			}			
		}
		
		if(errorsExist){			
			
			if(librarySampleList.isEmpty()){//all library rows were empty (user filled in no info at all for any library row)
				
				List<SampleMeta> normalizedMeta = new ArrayList<SampleMeta>();
				try {
					normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, SampleMeta.class));
				} catch (MetadataTypeException e) {
					logger.warn("Could not get meta for class 'SampleMeta' with sampleSubtype of bioanalyzerLibrarySample:" + e.getMessage());
					waspErrorMessage("bioanalyzer.create_sampleMeta.error");
					return "redirect:/dashboard.do";
				}
				Sample sample = new Sample();
				sample.setSampleMeta(normalizedMeta);				
				librarySampleList.add(sample);
				//Since no sample info was provided at all for ANY sample, give error message for this empty sample saying "PLEASE COMPLETE"???
				libraryErrorList.add(messageService.getMessage("bioanalyzer.create_libraryPleaseFillInLibraryInfo.error"));
			}
			
			prepareCreateBioanalyzerForm(m);
			m.addAttribute("userSelectedLabId", labId);//get all the labs for this user is done in prepareCreateBioanalyzerForm(); 
			m.addAttribute("grantsAvailable", getGrantsForLab(labId));
			m.addAttribute("userSelectedGrantId", grantId);
			m.addAttribute("userSelectedBioanalyzerChip", bioanalyzerChip);//availableBioanalyzerChipList is filled up in prepareCreateBioanalyzerForm()
			m.addAttribute("userSelectedWorkflowIdLibrariesAreDesignedFor", workflowIdLibrariesAreDesignedFor);//all workflows list is filled up in prepareCreateBioanalyzerForm()	
			m.addAttribute("userSelectedJobName", jobName.trim());	
			m.addAttribute("librarySampleList", librarySampleList);
			m.addAttribute("libraryErrorList", libraryErrorList);
			
			waspErrorMessage("bioanalyzer.create_errorsExist.error");//appears, then disappears from the top of web page
			return "bioanalyzer/create";
			
		}
		
		//no errors, so iterate through librarySampleList (which already excludes completely empty rows) and save each new library		
		//so, first the new job
		Job job = new Job();		
		job.setName(jobName.trim());
		job.setUserId(me.getId());
		job.setLabId(labId);
		//job.setWorkflowId(bioanalyzerWorkflow.getId());//the incoming parameter workflowId is actually the workflow that these libraries are ultimately designed for
		
		//second, the samples
		waspMessage("sampleDetail.updated_success.label");
		return "redirect:/bioanalyzer/create.do";
	}

	private boolean libraryRowIsCompletelyEmpty(Sample libraryRow){
		
		//checks only for sample name and all meta attributes
		if(!libraryRow.getName().trim().isEmpty()){
			return false;
		}
		for(SampleMeta sm : libraryRow.getSampleMeta()){
			if(sm!=null && sm.getV()!=null ){
				if(!sm.getV().trim().isEmpty()){
					return false;
				}
			}
		}
		return true;
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

}
