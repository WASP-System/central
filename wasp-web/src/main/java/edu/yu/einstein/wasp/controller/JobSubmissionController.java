package edu.yu.einstein.wasp.controller;


import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.Strategy.StrategyType;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.dao.JobDraftSoftwareDao;
import edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftJobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowResourceTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.AcctGrant;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftSoftware;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowResourceType;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AccountsService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@RequestMapping("/jobsubmit")
public class JobSubmissionController extends WaspController {

	@Autowired
	protected JobService jobService;

	@Autowired
	protected JobDraftMetaDao jobDraftMetaDao;

	@Autowired
	protected JobDraftCellSelectionDao jobDraftCellSelectionDao;

	@Autowired
	protected JobDraftresourcecategoryDao jobDraftresourcecategoryDao;

	@Autowired
	protected JobDraftSoftwareDao jobDraftSoftwareDao;
	
	@Autowired
	protected JobDraftFileDao jobDraftFileDao;

	@Autowired
	protected JobResourcecategoryDao jobResourcecategoryDao;

	@Autowired
	protected JobSoftwareDao jobSoftwareDao;

	@Autowired
	protected SampleDraftDao sampleDraftDao;

	@Autowired
	protected SampleDraftMetaDao sampleDraftMetaDao;

	@Autowired
	protected SampleDraftJobDraftCellSelectionDao sampleDraftJobDraftCellSelectionDao;

	@Autowired
	protected FileService fileService;

	@Autowired
	protected JobDao jobDao;

	@Autowired
	protected LabDao labDao;

	@Autowired
	protected JobUserDao jobUserDao;

	@Autowired
	protected RoleDao roleDao;

	@Autowired
	protected ResourceDao resourceDao;

	@Autowired
	protected ResourceCategoryDao resourceCategoryDao;

	@Autowired
	protected SoftwareService softwareService;

	@Autowired
	protected ResourceTypeDao resourceTypeDao;

	@Autowired
	protected JobMetaDao jobMetaDao;

	@Autowired
	protected SampleDao sampleDao;

	@Autowired
	protected SampleMetaDao sampleMetaDao;

	@Autowired
	protected JobSampleDao jobSampleDao;
	
	@Autowired
	protected SampleTypeDao sampleTypeDao;
	
	@Autowired
	protected SampleSubtypeDao sampleSubtypeDao;
	
	@Autowired
	protected SampleSubtypeDao subSampleTypeDao;
	
	@Autowired
	protected WorkflowDao workflowDao;
	
	@Autowired
	protected WorkflowService workflowService;

	@Autowired
	protected WorkflowresourcecategoryDao workflowresourcecategoryDao;
	
	@Autowired
	protected WorkflowResourceTypeDao workflowResourceTypeDao;

	@Autowired
	protected WorkflowSoftwareDao workflowSoftwareDao;
	
	@Autowired
	protected AdaptorsetDao adaptorsetDao;
	
	@Autowired
	protected AdaptorDao adaptorDao;
	
	@Autowired
	protected AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;

	@Autowired
	protected JobCellSelectionDao jobCellSelectionDao;
	
	@Autowired
	protected SampleJobCellSelectionDao sampleJobCellSelectionDao;
	
	@Autowired
	protected MessageServiceWebapp messageService;
	
	@Autowired
	protected SampleService sampleService;

	@Autowired
	protected JobDraftService jobDraftService;

	@Autowired
	protected AuthenticationService authenticationService;
	
	@Autowired
	protected GenomeService genomeService;

	@Autowired
	protected StrategyService strategyService;
	
	@Autowired
	protected AccountsService accountsService;
	
	@Value("${wasp.temporary.dir}")
	protected String downloadFolder;
	
	@Value("${wasp.primaryfilehost}")
	protected String fileHost;
	
	@Value("${wasp.analysis.perLibraryFee:0}")
	private Float perLibraryAnalysisFee;


	protected final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(JobDraftMeta.class, request.getSession());
	}
	
	final public String[] defaultPageFlow = {"/jobsubmit/modifymeta/{n}","/jobsubmit/samples/{n}","/jobsubmit/cells/{n}","/jobsubmit/verify/{n}","/jobsubmit/submit/{n}","/jobsubmit/ok"};
	

	@Transactional
	public String nextPage(JobDraft jobDraft) {
		String[] pageFlowArray = workflowService.getPageFlowOrder(workflowDao.getWorkflowByWorkflowId(jobDraft.getWorkflowId()));
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
	
	@Transactional
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*') or hasRole('su') or hasRole('ft-*') or hasRole('fm-*')")
	public String list(ModelMap m) {

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		return "jobsubmit/list";
	}

	@Transactional
	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public String getListJSON(HttpServletResponse response) {
		
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		
		String userId = request.getParameter("userId");
		if(userId==null || "".equals(userId)){//added 2-13-13 by Dubin; this way, if no parameter, get drafts for the authenticated user
			userId = authenticationService.getAuthenticatedUser().getId().toString();
		}
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<JobDraft> jobDraftList = new ArrayList<JobDraft>();//empty list
		
		User viewer = authenticationService.getAuthenticatedUser();//the web viewer that is logged on that wants to see his/her submitted or viewable jobs
		
		if (userId.isEmpty()) {//all drafts are being requested; must be facility personnel to view them
			if(authenticationService.isFacilityMember()){
				jobDraftList = sidx.isEmpty() ? this.jobDraftService.getJobDraftDao().getPendingJobDrafts() : this.jobDraftService.getJobDraftDao().getPendingJobDraftsOrderBy(sidx, sord);
			}			
		} 
		else{
			
			Integer userIdAsInteger = null;
			
			try{
				userIdAsInteger = Integer.parseInt(userId);
			}catch(NumberFormatException e){/*waspErrorMessage("wasp.parseint_error.error");*/}//error won't be visible; this is a jason call!
			
			if(userIdAsInteger!=null){
			  
				if(authenticationService.isFacilityMember() || ( !authenticationService.isFacilityMember() && userIdAsInteger.intValue()==viewer.getId().intValue() ) ){
				
					Map<String, Object> m = new HashMap<String, Object>();	
				
					//if (search.equals("true") && !searchStr.isEmpty()){
					//	m.put(request.getParameter("searchField"), request.getParameter("searchString"));
					//}
					//if (!labId.isEmpty())
					//	  m.put("labId", Integer.parseInt(labId));//NOTE: currently we are NOT anywhere using labId in a url going to this page. This is a Boyle relic

					m.put("status", "PENDING");
					m.put("userId", userIdAsInteger);				  	  
					jobDraftList = this.jobDraftService.getJobDraftDao().findByMap(m);
				}
			}
			else{
				//error won't be visible as this is a json call
				//waspErrorMessage("wasp.permission_error.error");//regular user asking to view someone else's records.
			}
		}

		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = jobDraftList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");
			 
			Map<String, String> userData=new HashMap<String, String>();
			userData.put("page", pageIndex + "");
			userData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("userdata",userData);
			 
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			
			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = jobDraftList.indexOf(jobDao.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<JobDraft> itemPage = jobDraftList.subList(frId, toId);
			for (JobDraft item : itemPage) {
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", item.getId());
				 
				List<JobDraftMeta> itemMeta = getMetaHelperWebapp().syncWithMaster(item.getJobDraftMeta());
				
				User user = userDao.getById(item.getUserId());
				 					
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							"<a href='" + getServletPath() + "/jobsubmit/modify/"+item.getId()+".do'>"+item.getName()+"</a>",
							item.getWorkflow().getName(),
							user.getNameFstLst(),
							item.getLab().getName(),
							item.getLastUpdatedByUser().getNameFstLst(),
							item.getUpdated().toString()
				}));
				 
				for (JobDraftMeta meta:itemMeta) {
					cellList.add(meta.getV());
				}
				 
				cell.put("cell", cellList);
				 
				rows.add(cell);
			}

			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			 
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jobDraftList,e);
		}
	
	}

	@Transactional
	protected String generateCreateForm(JobDraft jobDraft, Strategy thisJobDraftsStrategy, ModelMap m) {
		
		User me = authenticationService.getAuthenticatedUser();

		List <LabUser> labUserAllRoleList = me.getLabUser();
		List <Lab> labList = new ArrayList<Lab>();
		for (LabUser lu: labUserAllRoleList) {
			String roleName =	lu.getRole().getRoleName();

			if (roleName.equals("lu") ||
					roleName.equals("lm") ||
					roleName.equals("pi")) {
				labList.add(lu.getLab());
			}
		}
		if (labList.isEmpty()){
			waspErrorMessage("jobDraft.no_lab.error");
			return "redirect:/dashboard.do";
		}

		m.put("labs", labList);
		
		List<AcctGrant> grantsAvailable = new ArrayList<AcctGrant>();
		if (jobDraft.getLabId() != null){
			grantsAvailable = accountsService.getNonExpiredGrantsForLab(labDao.getById(jobDraft.getLabId()));
		}
		m.put("grantsAvailable", grantsAvailable);
		m.put("thisJobDraftsGrant", (jobDraft.getId() != null) ? accountsService.getGrantForJobDraft(jobDraft) : null);
		
		m.put("thisJobDraftsStrategy", thisJobDraftsStrategy);		

		//if thisJobDraftsStrategy is an new Strategy(), 
		//it MUST HAVE desired strategy.type set for the strategy for this type of job
		List<Strategy> strategies = strategyService.getStrategiesByStrategyType(thisJobDraftsStrategy.getType());//entire list of libraryStrategy entries for web display
		strategyService.orderStrategiesByDisplayStrategy(strategies);
		m.put("strategies", strategies);		
		
		List <Workflow> workflowsForTheRequestedStrategyAsList = strategyService.getActiveWorkflowsForStrategyOrDefaultsOrderByWorkflowName(thisJobDraftsStrategy);
		Map<Integer, String> assayWorkflows = new HashMap<>();
		for (Workflow wf: workflowsForTheRequestedStrategyAsList){//why do we have this?????? why not simply use wf.getName() on the jsp???
			assayWorkflows.put(wf.getId(), messageService.getMessage(wf.getIName() + ".workflow.label"));
		}
		
		m.put("assayWorkflows", assayWorkflows);
		m.put("isAnalysisSelected", jobDraftService.getIsAnalysisSelected(jobDraft));
		//As I was unable to embed a fmt:message within a wasp:tooltip on the create.jsp page, I was forced to use this this alternative solution of creating two new variables within this controller. 
		m.put("libraryStrategyTooltip", messageService.getMessage("jobsubmitCreate.libraryStrategyTooltip.label"));
		m.put("assayWorkflowTooltip", messageService.getMessage("jobsubmitCreate.assayWorkflowTooltip.label"));
		Object[] args = {perLibraryAnalysisFee};
		m.put("selectAnalysisTooltip", messageService.getMessage("jobsubmitCreate.selectAnalysisTooltip.label", args));
		m.put("perLibraryAnalysisFee", perLibraryAnalysisFee);
		return "jobsubmit/create";
	}

	@Transactional
	@RequestMapping(value="/create.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public String showCreateForm(ModelMap m) {
		
		// if we have been tracking another jobDraft remove the session variable
		if (request.getSession().getAttribute("jobDraftId") != null){
			request.getSession().removeAttribute("jobDraftId");
		}
		JobDraft jobDraft = new JobDraft();
		m.put("jobDraft", jobDraft);
		Strategy strategy = new Strategy();
		strategy.setType(StrategyType.LIBRARY_STRATEGY);//need a way to know strategy.Type from the type of jobdraft (which currently does not exist)
		return generateCreateForm(jobDraft, strategy, m);
	}
	
	@RequestMapping(value="/getWorkflowsForAStrategy.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public void getWorkflowsForAStrategy(HttpServletResponse response) {
		
		//THIS IS AN AJAX CALL FROM WEB
		
		String param = request.getParameter("strategy");
		
		Strategy requestedStrategy = strategyService.getStrategyByKey(param);
		
		List <Workflow> workflowsForTheRequestedStrategyAsList = strategyService.getActiveWorkflowsForStrategyOrDefaultsOrderByWorkflowName(requestedStrategy);
		Map<Integer, String> assayWorkflows = new HashMap<>();
		for (Workflow wf: workflowsForTheRequestedStrategyAsList){
			assayWorkflows.put(wf.getId(), messageService.getMessage(wf.getIName() + ".workflow.label"));
		}
		try{
			outputJSON(assayWorkflows, response);
		}catch(Exception e){}
	}
	
	@RequestMapping(value="/getGrantsForLab.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public void getGrantsForLab(HttpServletResponse response) {
		
		//THIS IS AN AJAX CALL FROM WEB
		
		Lab lab = labDao.getById(Integer.parseInt(request.getParameter("selectedLabId")));
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
	
	@RequestMapping(value="/addGrant.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public void addGrant(HttpServletResponse response) {
		
		//THIS IS AN AJAX CALL FROM WEB
		
		String newGrantCode = request.getParameter("newGrantCode");
		String newGrantName = request.getParameter("newGrantName");
		String newGrantExp = request.getParameter("newGrantExp");
		Integer labId = Integer.parseInt(request.getParameter("selectedLabId"));
		Date newGrantExpDate = null;
		Map<String, String> responseMap = new HashMap<>();
		boolean isErrors = false;
		if (newGrantCode == null || newGrantCode.isEmpty()){
			isErrors = true;
			responseMap.put("newGrantCodeError", messageService.getMessage("jobDraft.addGrantCode.error"));
		}
		if (newGrantExp == null || newGrantExp.isEmpty()){
			isErrors = true;
			responseMap.put("newGrantExpError", messageService.getMessage("jobDraft.addGrantExp.error"));
		}
		newGrantExp = newGrantExp.replaceAll("\\/", "-");
		try{
			newGrantExpDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(newGrantExp);
		} catch (IllegalArgumentException | ParseException e){
			isErrors = true;
			responseMap.put("newGrantExpError", messageService.getMessage("jobDraft.addGrantExp.error"));
		}
		if (!isErrors){
			AcctGrant grant = new AcctGrant();
			grant.setCode(newGrantCode);
			if (newGrantName != null && !newGrantName.isEmpty())
			grant.setName(newGrantName);
			grant.setExpirationdt(newGrantExpDate);
			grant.setLabId(labId);
			accountsService.saveGrant(grant);
			responseMap.put("errors", "false");
		}
		try{
			outputJSON(responseMap, response);
			return;
		}catch(Exception e){}
	}

	@Transactional
	@RequestMapping(value="/create.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('lu-*')")
	public String create (
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {
		// this is here if the user has pressed submit twice or used the back button and potentially modified the form
		if (request.getSession().getAttribute("jobDraftId") != null){
			return modify((Integer) request.getSession().getAttribute("jobDraftId"), jobDraftForm, result, status, m);
		}
		
		Errors errors = new BindException(result.getTarget(), "jobDraft");
		
		Map<String, String> jobDraftQuery = new HashMap<String, String>();
		String name = jobDraftForm.getName();
		if (name != null && !name.isEmpty()){
			// check we don't already have a job with this name
			// really should really check both a user's jobDrafts and a user's submitted jobs
			jobDraftQuery.put("name", name);
			if (!jobDraftService.getJobDraftDao().findByMap(jobDraftQuery).isEmpty()){
				errors.rejectValue("name", "jobDraft.name_exists.error", "jobDraft.name_exists.error (no message has been defined for this property");
			}
		}

		if (jobDraftForm.getLabId() == null || jobDraftForm.getLabId().intValue() < 1){
			errors.rejectValue("labId", "jobDraft.labId.error", "jobDraft.labId.error (no message has been defined for this property");
		}
		
		if(jobDraftForm.getWorkflowId() == null || jobDraftForm.getWorkflowId().intValue() <= 0 ){
			errors.rejectValue("workflowId", "jobDraft.workflowId.error", "jobDraft.workflowId.error (no message has been defined for this property)");
		}
		
		//get the strategy info from the web page
		Strategy strategy = new Strategy();
		String strategyError = "";
		String strategyParameter = request.getParameter("strategy"); //I suppose that some types of jobs, not yet defined, may not have any strategy. 
		if(!strategyParameter.isEmpty()){
			if("-1".equals(strategyParameter)){//this is not expected, as the submit button is never displayed when this is true
				strategyError = messageService.getMessage("jobDraft.strategyNotSelected.error");//"Please select a strategy";
				strategy.setType(StrategyType.LIBRARY_STRATEGY);//for now, but we need to do better
			}
			else{
				strategy = strategyService.getStrategyByKey(strategyParameter);
				if(strategy.getId()==null){//rather unlikely event
					strategyError = messageService.getMessage("jobDraft.strategyNotFound.error");//"Selected strategy unexpectedly not found"; 
				}
			}
		}

		// get grant
		Integer selectGrantId = -1;
		String grantSelectError = "";
		selectGrantId = Integer.parseInt(request.getParameter("selectGrantId"));
		if (selectGrantId < 1)
			grantSelectError = messageService.getMessage("jobDraft.grantNotSelected.error");
		
		result.addAllErrors(errors);

		if (!grantSelectError.isEmpty() || !strategyError.isEmpty() || result.hasErrors()) {
			waspErrorMessage("jobDraft.form.error");
			m.put("jobDraft", jobDraftForm); // unexpectedly, the web behaves without this line. Andy thinks that the @Valid automatically adds the form to the map
			m.put("strategyError", strategyError);
			m.put("grantSelectError", grantSelectError);
			return generateCreateForm(jobDraftForm, strategy, m);
		}
		
		User me = authenticationService.getAuthenticatedUser();
		jobDraftForm.setUserId(me.getId());
		jobDraftForm.setStatus("PENDING");
		JobDraft jobDraftDb = jobDraftService.getJobDraftDao().save(jobDraftForm); 
		AcctGrant acctGrant = accountsService.getAcctGrantById(selectGrantId);
		accountsService.saveJobDraftGrant(jobDraftDb, acctGrant);
		String isAnalysisSelectedParam = request.getParameter("isAnalysisSelected");
		if (isAnalysisSelectedParam != null)
			jobDraftService.setIsAnalysisSelected(jobDraftDb, Boolean.valueOf(isAnalysisSelectedParam));
		
		//save the strategy to jobDraftMeta
		strategyService.saveStrategy(jobDraftDb, strategy); // what if save fails?

		// sometimes if user presses submit button twice a job is created but on re-submission it complains
		// that the job name already exists. Also happens if the back button is used on job creation
		// Check session to see if we have already submitted job
		request.getSession().setAttribute("jobDraftId", (Integer) jobDraftDb.getId());
		
		// Adds the jobdraft to authorized list
 		doReauth();

		return nextPage(jobDraftDb);
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
	
	@Transactional
	@RequestMapping(value="/modify/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modify(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {

		// if we have been tracking another jobDraft remove the session variable
		if (request.getSession().getAttribute("jobDraftId") != null){
			request.getSession().removeAttribute("jobDraftId");
		}
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);

		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		m.put("jobDraft", jobDraft);
		//we really require some mechanism to be able to derive, from the type of job, the desired strategy: for example,  mps job wants a libraryStrategy
		String strategyTypeForThisJob = StrategyType.LIBRARY_STRATEGY;
		Strategy strategy = strategyService.getThisJobDraftsStrategy(strategyTypeForThisJob, jobDraft);
		if(strategy.getId()==null){
			strategy.setType(strategyTypeForThisJob);
		}
		return generateCreateForm(jobDraft, strategy, m);
	}


	@Transactional
	@RequestMapping(value="/modify/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modify (
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);

		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		
		Errors errors = new BindException(result.getTarget(), "jobDraft");
		
		Map<String, String> jobDraftQuery = new HashMap<String, String>();
		String name = jobDraftForm.getName();
		if (name != null && !name.isEmpty() && !name.equals(jobDraft.getName())){
			// check we don't already have a job with this name
			//nb: this should really check a users existing jobs and that user's other jobdrafts
			jobDraftQuery.put("name", name);
			if (!jobDraftService.getJobDraftDao().findByMap(jobDraftQuery).isEmpty()){
				errors.rejectValue("name", "jobDraft.name_exists.error", "jobDraft.name_exists.error (no message has been defined for this property");
			}
		}
		
		if (jobDraftForm.getLabId() == null || jobDraftForm.getLabId().intValue() < 1){
			errors.rejectValue("labId", "jobDraft.labId.error", "jobDraft.labId.error (no message has been defined for this property");
		}
		
		if(jobDraftForm.getWorkflowId() == null || jobDraftForm.getWorkflowId().intValue() <= 0 ){
			errors.rejectValue("workflowId", "jobDraft.workflowId.error", "jobDraft.workflowId.error (no message has been defined for this property)");
		}
		else if(jobDraftForm.getWorkflowId() != null && jobDraft.getWorkflowId() != null && jobDraft.getWorkflowId() != jobDraftForm.getWorkflowId() && jobDraft.getSampleDraft().size() > 0){//added 3/29/13; dubin: user is attempting to change workflow but there are samples/libraries already submitted (for example chipseq samples but converting to helpgtag)
			errors.rejectValue("workflowId", "jobDraft.workflowId_change.error");//, "jobDraft.workflowId_change.error (You must remove all samples from the job draft prior to changing the workflow assay)");
			//this next line is not having the effect I expect. must check why not
			jobDraftForm.setWorkflowId(jobDraft.getWorkflowId());//set it back for re-display
		}
		else if(jobDraftForm.getWorkflowId() != null && jobDraft.getWorkflowId() != null && jobDraft.getWorkflowId() != jobDraftForm.getWorkflowId() && jobDraft.getSampleDraft().size() == 0){//added 2/19/15; dubin: we're changing workflow, so remove any jobdraftdsoftware entries (as there could be a conflict; and thus an exception as we saw today)
			List<JobDraftSoftware> jobdraftSoftwareList = jobDraft.getJobDraftSoftware();
			for(JobDraftSoftware jds : jobdraftSoftwareList){
				jobDraftSoftwareDao.remove(jds);
				jobDraftSoftwareDao.flush(jds);
			}
		}
		
		// get grant
		Integer selectGrantId = -1;
		String grantSelectError = "";
		selectGrantId = Integer.parseInt(request.getParameter("selectGrantId"));
		if (selectGrantId < 1)
			grantSelectError = messageService.getMessage("jobDraft.grantNotSelected.error");
		
		//get the strategy info from the web page
		Strategy strategy = new Strategy();
		String strategyError = "";
		String strategyParameter = request.getParameter("strategy"); //I suppose that some types of jobs, not yet defined, may not have any strategy. 
		if(!strategyParameter.isEmpty()){
			if("-1".equals(strategyParameter)){//this is not expected to occur, as the submit button on this web page is not displayed when this value is -1
				strategyError = messageService.getMessage("jobDraft.strategyNotSelected.error");//"Please select a strategy";
				strategy.setType(StrategyType.LIBRARY_STRATEGY);//for now
			}
			else{
				strategy = strategyService.getStrategyByKey(strategyParameter);//searches table Meta
				if(strategy.getId()==null){//rather unlikely event
					strategyError = messageService.getMessage("jobDraft.strategyNotFound.error");//"Selected strategy unexpectedly not found";
				}
			}
		}
		result.addAllErrors(errors);
		if (!grantSelectError.isEmpty() || !strategyError.isEmpty() || result.hasErrors()) {
			waspErrorMessage("jobDraft.form.error");
			m.put("jobDraft", jobDraftForm);
			m.put("strategyError", strategyError);
			m.put("grantSelectError", grantSelectError);
			return generateCreateForm(jobDraftForm, strategy, m);
		}
		jobDraft.setName(jobDraftForm.getName());
		jobDraft.setWorkflowId(jobDraftForm.getWorkflowId());
		jobDraft.setLabId(jobDraftForm.getLabId());

		JobDraft jobDraftDb = jobDraftService.getJobDraftDao().save(jobDraft);//what if save fails?		
		strategyService.saveStrategy(jobDraftDb, strategy);//what if save fails?
		AcctGrant acctGrant = accountsService.getAcctGrantById(selectGrantId);
		accountsService.saveJobDraftGrant(jobDraftDb, acctGrant);
		String isAnalysisSelectedParam = request.getParameter("isAnalysisSelected");
		if (isAnalysisSelectedParam != null)
			jobDraftService.setIsAnalysisSelected(jobDraftDb, Boolean.valueOf(isAnalysisSelectedParam));

		return nextPage(jobDraftDb);
	}

/* dubin 10-10-13 ---does NOT appear to be used anywhere
	@Transactional
	public String doModify (////does not appear to be used in this controller, or anywhere else that I can see
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		// TODO CHECK ACCESS OF LABUSER
		
		Errors errors = new BindException(result.getTarget(), "jobDraft");
		if (jobDraftForm.getLabId() == null || jobDraftForm.getLabId().intValue() < 1){
			errors.rejectValue("labId", "jobDraft.labId.error", "jobDraft.labId.error (no message has been defined for this property");
		}
		result.addAllErrors(errors);
		
		if (result.hasErrors()) {
			waspErrorMessage("jobDraft.form.error");
			m.put("jobDraft", jobDraftForm);
			return generateCreateForm(jobDraftForm, m);
		}

		JobDraft jobDraftDb = jobDraftService.getJobDraftDao().save(jobDraftForm); 

		return nextPage(jobDraftDb);
	}
*/

	@Transactional
	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)") 
	public String showModifyMetaForm(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		 
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		metaHelperWebapp.setArea(jobDraft.getWorkflow().getIName());
		List<JobDraftMeta> jobDraftMeta = metaHelperWebapp.getMasterList(JobDraftMeta.class);
		if (jobDraftMeta.isEmpty()){
			// no metadata to capture
			return nextPage(jobDraft);
		}
			
		jobDraft.setJobDraftMeta(jobDraftMeta);
		// jobDraft.setJobDraftMeta(metaHelperWebapp.syncWithMaster(jobDraft.getJobDraftMeta()));


		m.put("jobDraft", jobDraft);
		m.put("area", metaHelperWebapp.getArea());
		m.put("parentarea", metaHelperWebapp.getParentArea());
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		return "jobsubmit/metaform";
	}


	
	@Transactional
	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modifyMeta(
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		jobDraftForm.setId(jobDraftId);
		jobDraftForm.setUserId(jobDraft.getUserId());
		jobDraftForm.setLabId(jobDraft.getLabId());
		jobDraftForm.setWorkflowId(jobDraft.getWorkflowId());

		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		
		metaHelperWebapp.setArea(jobDraft.getWorkflow().getIName());

		List<JobDraftMeta> jobDraftMetaList = metaHelperWebapp.getFromRequest(request, JobDraftMeta.class);

		jobDraftForm.setJobDraftMeta(jobDraftMetaList);

		metaHelperWebapp.validate(result);

		if (result.hasErrors()) {
			waspErrorMessage("jobDraft.form.error");

			m.put("jobDraft", jobDraft);
			m.put("area", metaHelperWebapp.getArea());
			m.put("parentarea", metaHelperWebapp.getParentArea());
	        m.put("pageFlowMap", getPageFlowMap(jobDraft));
	
			return "jobsubmit/metaform";
		}

		jobDraftMetaDao.replaceByJobDraftId(metaHelperWebapp.getArea(), jobDraftId, jobDraftMetaList);

		return nextPage(jobDraft);
	}
	
	@Transactional
	@RequestMapping(value="/resource/{resourceTypeIName}/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showResourceMetaForm(
			@PathVariable("resourceTypeIName") String resourceTypeIName, 
			@PathVariable("jobDraftId") Integer jobDraftId, 
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		// check at least one resource exists of the requested resource type
		WorkflowResourceType wrt = workflowResourceTypeDao.getWorkflowResourceTypeByWorkflowIdResourceTypeId(jobDraft.getWorkflow().getId(), 
				resourceTypeDao.getResourceTypeByIName(resourceTypeIName).getId());
		if (wrt.getResourceTypeId() == null){
			logger.warn("Resource with iname=" + resourceTypeIName + " has no entry in the database");
			waspErrorMessage("jobDraft.no_resources.error");
			return "redirect:/dashboard.do";
		}
		return showResourceMetaForm(resourceTypeIName, jobDraftId, null, m);
	}
	
	@Transactional
	public String showResourceMetaForm(String resourceTypeIName, Integer jobDraftId, JobDraft jobDraftForm, ModelMap m){
		// make list of available resources
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		List<Workflowresourcecategory> allWorkflowResourceCategories = jobDraft.getWorkflow().getWorkflowresourcecategory();
		List<Workflowresourcecategory> workflowResourceCategories = new ArrayList<Workflowresourcecategory>();
		for (Workflowresourcecategory w: allWorkflowResourceCategories) {
			if (! w.getResourceCategory().getResourceType().getIName().equals(resourceTypeIName)) { continue; }
			workflowResourceCategories.add(w); 
		}
		if (workflowResourceCategories.isEmpty()){
			waspErrorMessage("jobDraft.resourceCategories_not_configured.error");
			return "redirect:/dashboard.do";
		}

		// get selected resource
		JobDraftresourcecategory jobDraftResourceCategory = null; 
		String resourceCategoryArea = ""; 
		String resourceCategoryName = ""; 
		for (JobDraftresourcecategory jdrc: jobDraft.getJobDraftresourcecategory()) {
			if (resourceTypeIName.equals( jdrc.getResourceCategory().getResourceType().getIName())) {
				jobDraftResourceCategory = jdrc;
				resourceCategoryArea = jdrc.getResourceCategory().getIName(); 
				resourceCategoryName = jdrc.getResourceCategory().getName();
				break;
			}
		}

		// Resource Options loading
		Map<String, List<MetaAttribute.Control.Option>> resourceOptions = new HashMap<String, List<MetaAttribute.Control.Option>>();

		if (jobDraftResourceCategory != null) {
			try{
				resourceOptions.putAll(workflowService.getConfiguredOptions(jobDraft.getWorkflow(), jobDraftResourceCategory.getResourceCategory()));
			} catch (MetadataException e){
				waspErrorMessage("jobDraft.resourceCategories_not_configured.error");
				return "redirect:/dashboard.do";
			}
		}

		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		metaHelperWebapp.setArea(resourceCategoryArea);
		
		if (jobDraftForm == null){
			jobDraftForm = jobDraft; // shallow copy
			jobDraftForm.setJobDraftMeta(metaHelperWebapp.syncWithMaster(jobDraft.getJobDraftMeta()));
		}

		m.put("workflowResourceCategories", workflowResourceCategories);
		m.put("jobDraft", jobDraftForm);
		m.put("name", resourceCategoryName);
		m.put("area", metaHelperWebapp.getArea());
		m.put("jobDraftResourceCategory", jobDraftResourceCategory);
		m.put("resourceOptions", resourceOptions);
		m.put("parentarea", metaHelperWebapp.getParentArea());
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		return "jobsubmit/resource";
	}

	@Transactional
	@RequestMapping(value="/resource/{resourceTypeIName}/{jobDraftId}", method=RequestMethod.POST)
	public String modifyResourceMeta (
			@PathVariable String resourceTypeIName,
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		// check at least one resource exists of the requested resource type
		WorkflowResourceType wrt = workflowResourceTypeDao.getWorkflowResourceTypeByWorkflowIdResourceTypeId(jobDraft.getWorkflow().getId(), 
				resourceTypeDao.getResourceTypeByIName(resourceTypeIName).getId());
		if (wrt.getResourceTypeId() == null){
			logger.warn("Resource with iname=" + resourceTypeIName + " has no entry in the database");
			waspErrorMessage("jobDraft.no_resources.error");
			return "redirect:/dashboard.do";
		}
		Map<String, String[]> params = request.getParameterMap();
		Integer changeResource = null;
		try {
			changeResource = Integer.parseInt(((String[])params.get("changeResource"))[0]);
		} catch (Exception e) {
		}

		// The resource is changing
		// set the resource and reload the page.
		// todo: consider wiping out old meta values?
		if (changeResource != null){
			List<JobDraftresourcecategory> oldJdrs = jobDraft.getJobDraftresourcecategory();
			for (JobDraftresourcecategory jdr: oldJdrs) {
				if (jdr.getResourceCategory().getResourceType().getIName().equals(resourceTypeIName)){
					jobDraftresourcecategoryDao.remove(jdr);
					jobDraftresourcecategoryDao.flush(jdr);
				}
			}
			if (changeResource.intValue() == -1) // nothing selected
				return "redirect:/jobsubmit/resource/" + resourceTypeIName + "/" + jobDraftId + ".do";
			JobDraftresourcecategory newJdr = new JobDraftresourcecategory();
			newJdr.setJobDraftId(jobDraftId);
			newJdr.setResourcecategoryId(changeResource);
			jobDraftresourcecategoryDao.save(newJdr);

			return "redirect:/jobsubmit/resource/" + resourceTypeIName + "/" + jobDraftId + ".do";
		}


		// get selected resource
		String resourceCategoryArea = ""; 
		for (JobDraftresourcecategory jdr: jobDraft.getJobDraftresourcecategory()) {
			if (! resourceTypeIName.equals( jdr.getResourceCategory().getResourceType().getIName())) { continue; }
			resourceCategoryArea = jdr.getResourceCategory().getIName();
		}
		
		if (resourceCategoryArea.isEmpty()){
			waspErrorMessage("jobDraft.changeResource.error");
			return "redirect:/jobsubmit/resource/" + resourceTypeIName + "/" + jobDraftId + ".do";
		}
		
		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		metaHelperWebapp.setArea(resourceCategoryArea);

		List<JobDraftMeta> jobDraftMetaList = metaHelperWebapp.getFromRequest(request, JobDraftMeta.class);
		
		metaHelperWebapp.validate(result);
		jobDraftForm.setJobDraftMeta(jobDraftMetaList);
		
		if (result.hasErrors()) {
			waspErrorMessage("jobDraft.form.error");
			jobDraftForm.setName(jobDraft.getName());
			jobDraftForm.setWorkflowId(jobDraft.getWorkflowId());
			jobDraftForm.setLabId(jobDraft.getLabId());
			return showResourceMetaForm(resourceTypeIName, jobDraftId, jobDraftForm, m);
		}


		jobDraftMetaDao.replaceByJobDraftId(metaHelperWebapp.getArea(), jobDraftId, jobDraftMetaList);

		return nextPage(jobDraft);
	}


  /**
   * show software form
   */
	@Transactional
	public String showSoftwareForm(String resourceTypeIName, Integer jobDraftId, JobDraft jobDraftForm, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		// make list of available resources
		List<WorkflowSoftware> allWorkflowSoftwares = jobDraft.getWorkflow().getWorkflowSoftware();
		List<WorkflowSoftware> workflowSoftwares = new ArrayList<WorkflowSoftware>();
		for (WorkflowSoftware w: allWorkflowSoftwares) {
			if (! w.getSoftware().getResourceType().getIName().equals(resourceTypeIName)) { continue; }
			workflowSoftwares.add(w); 
		}
		if (workflowSoftwares.isEmpty()){
			waspErrorMessage("jobDraft.software_not_configured.error");
			return "redirect:/dashboard.do";
		}

		// get selected resource
		JobDraftSoftware jobDraftSoftware = null; 
		String softwareArea = ""; 
		String softwareName = ""; 
		for (JobDraftSoftware jds: jobDraft.getJobDraftSoftware()) {
			if (resourceTypeIName.equals( jds.getSoftware().getResourceType().getIName())) { 
				jobDraftSoftware = jds;
				softwareArea = jds.getSoftware().getIName(); 
				softwareName = jds.getSoftware().getName(); 
				break;
			}
		}
		
		// Resource Options loading
		Map<String, List<MetaAttribute.Control.Option>> resourceOptions = new HashMap<String, List<MetaAttribute.Control.Option>>();

		if (jobDraftSoftware != null) {
			try{
				resourceOptions = workflowService.getConfiguredOptions(jobDraft.getWorkflow(), jobDraftSoftware.getSoftware());
			} catch (MetadataException e){
				waspErrorMessage("jobDraft.software_not_configured.error");
				return "redirect:/dashboard.do";
			}
		}

		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		metaHelperWebapp.setArea(softwareArea);

		if (jobDraftForm == null){
			jobDraftForm = jobDraft; // shallow copy
			jobDraftForm.setJobDraftMeta(metaHelperWebapp.syncWithMaster(jobDraft.getJobDraftMeta()));
		}

		m.put("workflowSoftwares", workflowSoftwares);
		m.put("jobDraft", jobDraftForm);
		m.put("name", softwareName);
		m.put("area", metaHelperWebapp.getArea());
		m.put("jobDraftSoftware", jobDraftSoftware);
		m.put("resourceOptions", resourceOptions);
		m.put("parentarea", metaHelperWebapp.getParentArea());
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		return "jobsubmit/software";
	}
	
	

	@Transactional
	@RequestMapping(value="/software/{resourceTypeIName}/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showSoftwareForm(
			@PathVariable("resourceTypeIName") String resourceTypeIName, 
			@PathVariable("jobDraftId") Integer jobDraftId, 
			ModelMap m) {
		// check at least one resource exists of the requested resource type
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		if (!jobDraftService.getIsAnalysisSelected(jobDraft))
			return nextPage(jobDraft);
		WorkflowResourceType wrt = workflowResourceTypeDao.getWorkflowResourceTypeByWorkflowIdResourceTypeId(jobDraft.getWorkflow().getId(), 
				resourceTypeDao.getResourceTypeByIName(resourceTypeIName).getId());
		if (wrt.getResourceTypeId() == null){
			logger.warn("Resource with iname=" + resourceTypeIName + " has no entry in the database");
			waspErrorMessage("jobDraft.no_resources.error");
			return "redirect:/dashboard.do";
		}
		return showSoftwareForm(resourceTypeIName, jobDraftId, null ,m);
	}

	@Transactional
	@RequestMapping(value="/software/{resourceTypeIName}/{jobDraftId}", method=RequestMethod.POST)
	public String modifySoftwareMeta (
			@PathVariable String resourceTypeIName,
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		WorkflowResourceType wrt = workflowResourceTypeDao.getWorkflowResourceTypeByWorkflowIdResourceTypeId(jobDraft.getWorkflow().getId(), 
				resourceTypeDao.getResourceTypeByIName(resourceTypeIName).getId());
		if (wrt.getResourceTypeId() == null){
			logger.warn("Resource with iname=" + resourceTypeIName + " has no entry in the database");
			waspErrorMessage("jobDraft.no_resources.error");
			return "redirect:/dashboard.do";
		}
		
		Map<String, String[]> params = request.getParameterMap();
		Integer changeResource = null;
		try {
			changeResource = Integer.parseInt(((String[])params.get("changeResource"))[0]);
		} catch (Exception e) {
		}

		// The resource is changing
		// set the resource and reload the page.
		// todo: consider wiping out old meta values?
		if (changeResource != null) {
			List<JobDraftSoftware> oldJds = jobDraft.getJobDraftSoftware();
			for (JobDraftSoftware jds: oldJds) {
				if (jds.getSoftware().getResourceType().getIName().equals(resourceTypeIName))
				jobDraftSoftwareDao.remove(jds);
				jobDraftSoftwareDao.flush(jds);
			}
			if (changeResource.intValue() == -1) // nothing selected
				return "redirect:/jobsubmit/software/" + resourceTypeIName + "/" + jobDraftId + ".do";
			JobDraftSoftware newJdr = new JobDraftSoftware();
			newJdr.setJobDraftId(jobDraftId);
			newJdr.setSoftwareId(changeResource);
			jobDraftSoftwareDao.save(newJdr);

			return "redirect:/jobsubmit/software/" + resourceTypeIName + "/" + jobDraftId + ".do";
		}

		// get selected resource
		String softwareArea = ""; 
		for (JobDraftSoftware jobDraftSoftware: jobDraft.getJobDraftSoftware()) {
			if (! resourceTypeIName.equals( jobDraftSoftware.getSoftware().getResourceType().getIName())) { continue; }
			softwareArea = jobDraftSoftware.getSoftware().getIName();
		}
		
		if (softwareArea.isEmpty()){
			waspErrorMessage("jobDraft.changeSoftwareResource.error");
			return "redirect:/jobsubmit/software/" + resourceTypeIName + "/" + jobDraftId + ".do";
		}
		
		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		metaHelperWebapp.setArea(softwareArea);

		List<JobDraftMeta> jobDraftMetaList = metaHelperWebapp.getFromRequest(request, JobDraftMeta.class);
		jobDraftForm.setJobDraftMeta(jobDraftMetaList);
		metaHelperWebapp.validate(result);
		
		
		
		if (result.hasErrors()) {
			jobDraftForm.setName(jobDraft.getName());
			jobDraftForm.setWorkflowId(jobDraft.getWorkflowId());
			jobDraftForm.setLabId(jobDraft.getLabId());
			waspErrorMessage("jobDraft.form.error");
			String returnPage = showSoftwareForm(resourceTypeIName, jobDraftId, jobDraftForm, m); 
			return returnPage;
		}

		jobDraftMetaDao.replaceByJobDraftId(metaHelperWebapp.getArea(), jobDraftId, jobDraftMetaList);

		return nextPage(jobDraft);
	}


	@Transactional
	@RequestMapping(value="/additionalMeta/{meta}/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showAdditionalMetaForm(@PathVariable("meta") String additionalMetaArea, @PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		
		MetaHelperWebapp workflowMetaHelperWebapp = getMetaHelperWebapp();
		workflowMetaHelperWebapp.setArea(jobDraft.getWorkflow().getIName());

		List<JobDraftMeta> jobDraftMeta = workflowMetaHelperWebapp.syncWithMaster(jobDraft.getJobDraftMeta()); 

		String ametaArea = "";
		for (JobDraftMeta jdm: jobDraftMeta) {
			if (! jdm.getK().equals(workflowMetaHelperWebapp.getArea() + "." + additionalMetaArea)) { continue; }
			ametaArea = jdm.getV();
		}
		if (ametaArea.isEmpty()){
			// no additional meta found with supplied meta area
			return nextPage(jobDraft);
		}

		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		metaHelperWebapp.setArea(ametaArea);
		jobDraft.setJobDraftMeta(metaHelperWebapp.syncWithMaster(jobDraft.getJobDraftMeta()));


		m.put("jobDraft", jobDraft);
		m.put("area", metaHelperWebapp.getArea());
		m.put("parentarea", metaHelperWebapp.getParentArea());
		
        m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		return "jobsubmit/metaform";
	}

	@Transactional
	@RequestMapping(value="/additionalMeta/{meta}/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modifyAdditionalMeta (
			@PathVariable("meta") String additionalMetaArea,
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		MetaHelperWebapp workflowMetaHelperWebapp = getMetaHelperWebapp();
		workflowMetaHelperWebapp.setArea(jobDraft.getWorkflow().getIName());

		List<JobDraftMeta> jobDraftMeta = workflowMetaHelperWebapp.syncWithMaster(jobDraft.getJobDraftMeta()); 

		JobDraftMeta ametaJdm = new JobDraftMeta();
		String ametaArea = "";
		for (JobDraftMeta jdm: jobDraftMeta) {
			if (! jdm.getK().equals(workflowMetaHelperWebapp.getArea() + "." + additionalMetaArea)) { continue; }
			ametaArea = jdm.getV();
			ametaJdm = jdm;
		}
		if (ametaArea.isEmpty()){
			// no additional meta found with supplied meta area
			return nextPage(jobDraft);
		}

		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
		metaHelperWebapp.setArea(ametaArea);
		List<JobDraftMeta> jobDraftMetaList = metaHelperWebapp.getFromRequest(request, JobDraftMeta.class);

		jobDraftForm.setJobDraftMeta(jobDraftMetaList);
		metaHelperWebapp.validate(result);

		if (result.hasErrors()) {
			waspErrorMessage("jobDraft.form.error");
			m.put("jobDraft", jobDraft);
			m.put("area", metaHelperWebapp.getArea());
			m.put("parentarea", metaHelperWebapp.getParentArea());
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
	
			return "jobsubmit/metaform";
		}


		// sync meta data in DB (e.g.removes old aligners)
		for (MetaAttribute.Control.Option opt: ametaJdm.getProperty().getControl().getOptions()) {
			jobDraftMetaDao.replaceByJobDraftId(opt.getValue(), jobDraftId, new ArrayList<JobDraftMeta>());
		}

		jobDraftMetaDao.replaceByJobDraftId(metaHelperWebapp.getArea(), jobDraftId, jobDraftMetaList);

		return nextPage(jobDraft);

	}
	
	
	@Transactional
	@RequestMapping(value="/samples/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showSampleDraftList(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
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
		m.addAttribute("pageFlowMap", getPageFlowMap(jobDraft));
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		m.addAttribute("adaptorSetsUsedOnThisJobDraft", getAdaptorSets(jobDraft));
		
		//for editing existing samples (dubin;2-5-15)
		Set<SampleSubtype> sampleSubtypesOfExistingSampleDraftsSet = new HashSet<SampleSubtype>();
		for(SampleDraft sd : sampleDraftList){
			sampleSubtypesOfExistingSampleDraftsSet.add(sd.getSampleSubtype());
		}
		m.addAttribute("sampleSubtypesOfExistingSampleDraftsList", new ArrayList<SampleSubtype>(sampleSubtypesOfExistingSampleDraftsSet));
		
		return "jobsubmit/sample";
	}
	
	private List<Adaptorset> getAdaptorSets(JobDraft jobDraft){
		List<Adaptorset> jobDraftAdaptorSet = new ArrayList<Adaptorset>();
		for(SampleDraft sampleDraft : jobDraft.getSampleDraft()){
			try{	
	  			Adaptorset adaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", sampleDraft.getSampleDraftMeta())) );
	  			if(!jobDraftAdaptorSet.contains(adaptorset)){
	  				jobDraftAdaptorSet.add(adaptorset);
	  			}
	  		} catch(MetadataException e){
	  			logger.warn("Cannot get metadata genericLibrary.adaptorset. Presumably not be defined: " + e.getMessage());
	  		} catch(NumberFormatException e){
	  			logger.warn("Cannot convert to numeric value for metadata " + e.getMessage());
	  		}
		}
		return jobDraftAdaptorSet;
	}
	
	@Transactional
	@RequestMapping(value="/file/{jobDraftId}/{fileGroupId}/{fileHandleId}/delete", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String deleteUploadedFile(@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("fileGroupId") Integer fileGroupId, @PathVariable("fileHandleId") Integer fileHandleId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		try{
			fileService.removeUploadedFileFromJobDraft(jobDraftId, fileGroupId, fileHandleId);
			waspMessage("jobDraft.upload_file_removed.label");
		}catch (FileNotFoundException e){
			logger.warn("Cannot remove file from jobdraft (id=" + jobDraftId + ") where fileGroupId=" + fileGroupId + " and fileHandleId=" + fileHandleId + " : " + e.getMessage());
			waspErrorMessage("jobDraft.upload_file_removal_failed.label");
		}
		return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
	}

	
	@RequestMapping(value="/samples/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public /*Callable<String>*/ String submitSampleDraftList(
			@PathVariable("jobDraftId") final Integer jobDraftId, 
			@RequestParam("file_description") final List<String> fileDescriptions,
			@RequestParam("file_upload") final List<MultipartFile> mpFiles) {
		final JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		
		final User me = authenticationService.getAuthenticatedUser(); // need to do this here as no access to SecurityContextHolder off the main thread
		/*return new Callable<String>() {

			@Override
			public String call() throws Exception {*/
				if (! isJobDraftEditable(jobDraft, me))
					return "redirect:/dashboard.do";
				
				Random randomNumberGenerator = new Random(System.currentTimeMillis());
				
				if (mpFiles != null){
					int fileCount = -1;
					for (MultipartFile mpFile: mpFiles){
						fileCount++;
						if (mpFile.isEmpty())
							continue;
						try{
							fileService.uploadJobDraftFile(mpFile, jobDraft, fileDescriptions.get(fileCount), randomNumberGenerator);//uploads file and performs database updates
						} catch(Exception e){
							logger.warn(e.getMessage());
							e.printStackTrace();
							if (isInDemoMode)
								waspErrorMessage("jobDraft.upload_file_demo.error");
							else
								waspErrorMessage("jobDraft.upload_file.error");
						}
					}
				}
				if (jobDraft.getSampleDraft().isEmpty()){
					waspErrorMessage("jobDraft.noSamples.error");
					return "redirect:/jobsubmit/samples/"+jobDraftId+".do"; 
				}
				return nextPage(jobDraft);
	//		}
	//	};
		
	}
	
	@Transactional
	@RequestMapping(value="/samples/view/{jobDraftId}/{sampleDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String viewSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("sampleDraftId") Integer sampleDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleDraft sampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(sampleDraftId);
		if (sampleDraft.getId() == null){
			waspErrorMessage("jobDraft.jobDraft_null.error");
			return "redirect:/jobsubmit/sample.do";
		}
		List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
		try {
			normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleDraft.getSampleSubtype(), sampleDraft.getSampleDraftMeta(), SampleDraftMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
		}
		if (sampleService.isLibrary(sampleDraft)){
			// library specific functionality
			prepareAdaptorsetsAndAdaptors(jobDraft, normalizedMeta, m);
		}
		m.addAttribute("normalizedMeta", normalizedMeta);
		m.addAttribute("sampleDraft", sampleDraft);
		m.addAttribute("jobDraft", jobDraft);
		m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
		return "jobsubmit/sample/sampledetail_ro";
	}
	
	@Transactional
	@RequestMapping(value="/samples/remove/{jobDraftId}/{sampleDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String removeSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("sampleDraftId") Integer sampleDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleDraft sampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(sampleDraftId);
		if (sampleDraft.getId() == null){
			waspErrorMessage("jobDraft.jobDraft_null.error");
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		jobDraftService.removeSampleDraftAndAllDependencies(jobDraft,sampleDraft);//1-8-15; dubin	
		waspMessage("sampleDetail.updated_success.label");
		return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
	}
	

	@Transactional
	@RequestMapping(value="/samples/edit/{jobDraftId}/{sampleDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String editSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("sampleDraftId") Integer sampleDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleDraft sampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(sampleDraftId);
		if (sampleDraft.getId() == null){
			waspErrorMessage("jobDraft.jobDraft_null.error");
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
		try {
			normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleDraft.getSampleSubtype(), sampleDraft.getSampleDraftMeta(), SampleDraftMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
		}
		if (sampleService.isLibrary(sampleDraft)){
			prepareAdaptorsetsAndAdaptors(jobDraft, normalizedMeta, m);
		}
		m.addAttribute("organisms", genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
		m.addAttribute("heading", messageService.getMessage("jobDraft.sample_edit_heading.label"));
		m.addAttribute("normalizedMeta", normalizedMeta);
		m.addAttribute("sampleDraft", sampleDraft);
		m.addAttribute("jobDraft", jobDraft);
		return "jobsubmit/sample/sampledetail_rw";
	}
	
	@Transactional
	@RequestMapping(value="/samples/edit/{jobDraftId}/{sampleDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, 
			@PathVariable("sampleDraftId") Integer sampleDraftId,
			@Valid SampleDraft sampleDraftForm, BindingResult result, SessionStatus status, ModelMap m) {
		if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleDraft sampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(sampleDraftId);
		if (sampleDraft.getId() == null){
			waspErrorMessage("jobDraft.sampleDraft_null.error");
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleDraftForm.getSampleSubtypeId());
		List<SampleDraftMeta> metaFromForm = new ArrayList<SampleDraftMeta>();
		try {
			metaFromForm.addAll(SampleAndSampleDraftMetaHelper.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result, SampleDraftMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
		}
		sampleDraftForm.setName(sampleDraftForm.getName().trim());//from the form
		sampleDraftForm.setSampleSubtype(sampleSubtype /*sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleDraftForm.getSampleSubtypeId())*/);
		sampleDraftForm.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(sampleDraftForm.getSampleTypeId()));//this is needed if we make the call sampleService.isLibrary(sampleDraftForm), without which we throw a nullpointer exception

		validateSampleDraftNameUnique(sampleDraftForm.getName(), sampleDraftId, jobDraft, result);
		if (result.hasErrors()){
			waspErrorMessage("sampleDetail.updated.error");
			if (sampleService.isLibrary(sampleDraftForm)){
				// library specific functionality
				prepareAdaptorsetsAndAdaptors(jobDraft, metaFromForm, m);
			}
			m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
			m.addAttribute("heading", messageService.getMessage("jobDraft.sample_edit_heading.label"));
			m.addAttribute("normalizedMeta", metaFromForm);
			m.addAttribute("sampleDraft", sampleDraftForm);
			m.addAttribute("jobDraft", jobDraft);
			return "jobsubmit/sample/sampledetail_rw";
		}
		// all ok so save
		if (!sampleDraft.getName().equals(sampleDraftForm.getName()))
			sampleDraft.setName(sampleDraftForm.getName());
		sampleDraftDao.save(sampleDraft);
		try{
			sampleDraftMetaDao.setMeta(metaFromForm, sampleDraft.getId());
			waspMessage("sampleDetail.updated_success.label");
		} catch (MetadataException e){
			waspErrorMessage("sampleDetail.updated.error");
			logger.warn("Failed to update metadata!!: " + e.getLocalizedMessage());
		}
		//replaced 12-7-12; WASP-181   return "redirect:/jobsubmit/samples/view/"+jobDraftId+"/"+sampleDraftId+".do";
		return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
	}
	
	@Transactional
	@RequestMapping(value="/samples/clone/{jobDraftId}/{sampleDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String cloneSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("sampleDraftId") Integer sampleDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleDraft sampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(sampleDraftId);
		if (sampleDraft.getId() == null){
			waspErrorMessage("jobDraft.jobDraft_null.error");
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		SampleDraft clone = sampleService.cloneSampleDraft(sampleDraft);
		clone.setName("");
		List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
		try {
			normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(clone.getSampleSubtype(), clone.getSampleDraftMeta(), SampleDraftMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
		}
		if (sampleService.isLibrary(clone)){
			prepareAdaptorsetsAndAdaptors(jobDraft, clone.getSampleDraftMeta(), m);
		}
		m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
		m.addAttribute("heading", messageService.getMessage("jobDraft.sample_clone_heading.label"));
		m.addAttribute("normalizedMeta", normalizedMeta);
		m.addAttribute("sampleDraft", clone);
		m.addAttribute("jobDraft", jobDraft);
		return "jobsubmit/sample/sampledetail_rw";
	}
	
	@Transactional
	@RequestMapping(value="/samples/clone/{jobDraftId}/{sdi}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateCloneSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId,
			@PathVariable("sdi") Integer sdi, 
			@Valid SampleDraft cloneForm, BindingResult result, SessionStatus status, ModelMap m){
		String viewString = this.updateNewSampleDraft(jobDraftId, Integer.parseInt(request.getParameter("sampleSubtypeId")), cloneForm, result, status, m);
		m.addAttribute("heading", messageService.getMessage("jobDraft.sample_clone_heading.label"));
		return viewString;
	}
	
	@Transactional
	@RequestMapping(value="/samples/add/{jobDraftId}/{sampleSubtypeId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String newSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("sampleSubtypeId") Integer sampleSubtypeId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleDraft sampleDraft = new SampleDraft();
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleSubtypeId);
		if (sampleSubtype.getId() == null){
			waspErrorMessage("jobDraft.sampleSubtype_null.error");
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		sampleDraft.setSampleSubtypeId(sampleSubtypeId);
		sampleDraft.setSampleSubtype(sampleSubtype);
		sampleDraft.setSampleTypeId(sampleSubtype.getSampleType().getId());
		sampleDraft.setSampleType(sampleSubtype.getSampleType());
		List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
		try {
			normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, SampleDraftMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
		}
		if (sampleService.isLibrary(sampleDraft)){
			prepareAdaptorsetsAndAdaptors(jobDraft, normalizedMeta, m);
		}
		m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
		m.addAttribute("heading", messageService.getMessage("jobDraft.sample_add_heading.label"));
		m.addAttribute("normalizedMeta", normalizedMeta);
		m.addAttribute("sampleDraft", sampleDraft);
		m.addAttribute("jobDraft", jobDraft);
		return "jobsubmit/sample/sampledetail_rw";
	}

	@Transactional
	@RequestMapping(value="/samples/add/{jobDraftId}/{sampleSubtypeId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateNewSampleDraft(
			@PathVariable("jobDraftId") Integer jobDraftId, 
			@PathVariable("sampleSubtypeId") Integer sampleSubtypeId,
			@Valid SampleDraft sampleDraftForm, BindingResult result, SessionStatus status, ModelMap m) {
		
		if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleDraftForm.getSampleSubtypeId());
		List<SampleDraftMeta> metaFromForm = new ArrayList<SampleDraftMeta>();
		try {
			metaFromForm.addAll(SampleAndSampleDraftMetaHelper.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result, SampleDraftMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
		}
		sampleDraftForm.setName(sampleDraftForm.getName().trim());//from the form
		sampleDraftForm.setSampleSubtype(sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleDraftForm.getSampleSubtypeId()));
		sampleDraftForm.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(sampleDraftForm.getSampleTypeId()));
		validateSampleDraftNameUnique(sampleDraftForm.getName(), 0, jobDraft, result);
		
		if (result.hasErrors()){
			if (sampleService.isLibrary(sampleDraftForm)){
				// library specific functionality
				prepareAdaptorsetsAndAdaptors(jobDraft, metaFromForm, m);
			}
			waspErrorMessage("sampleDetail.updated.error");
			m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
			m.addAttribute("heading", messageService.getMessage("jobDraft.sample_add_heading.label"));
			m.addAttribute("normalizedMeta", metaFromForm);
			m.addAttribute("sampleDraft", sampleDraftForm);
			m.addAttribute("jobDraft", jobDraft);
			return "jobsubmit/sample/sampledetail_rw";
		}
		// all ok so save
		sampleDraftForm.setLabId(jobDraft.getLabId());
		sampleDraftForm.setUserId(jobDraft.getUserId());
		sampleDraftForm.setJobDraftId(jobDraftId);
		SampleDraft sampleDraftDb = sampleDraftDao.save(sampleDraftForm);
		try {
			sampleDraftMetaDao.setMeta(metaFromForm, sampleDraftDb.getId());
		} catch (MetadataException e) {
			waspErrorMessage("sampleDetail.updated.error");
			logger.warn("Failed to update metadata!!: " + e.getLocalizedMessage());
		}
		waspMessage("sampleDetail.updated_success.label");
		return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
	}
	
	@Transactional
	@RequestMapping(value="/manysamples/add/{jobDraftId}/{sampleSubtypeId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String newManySampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, @PathVariable("sampleSubtypeId") Integer sampleSubtypeId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleSubtypeId);
		if (sampleSubtype.getId() == null){
			waspErrorMessage("jobDraft.sampleSubtype_null.error");
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		SampleType sampleType = sampleSubtype.getSampleType();
		
		List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
		try {
			normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, SampleDraftMeta.class));
		} catch (MetadataTypeException e) {
			logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
		}
		SampleDraft sampleDraft = new SampleDraft();
		sampleDraft.setSampleDraftMeta(normalizedMeta);
			
		//make web responsive to a list of sampleDrafts, even though this method only sends one, because
		//in the post to this method, may have a list of many sampleDrafts
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		sampleDraftList.add(sampleDraft);
		
		m.addAttribute("jobDraft", jobDraft);
		m.addAttribute("sampleDraftList", sampleDraftList);
		m.addAttribute("sampleSubtype", sampleSubtype);
		m.addAttribute("sampleType", sampleType);
		m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
		m.addAttribute("heading", messageService.getMessage("jobDraft.sample_add_heading.label"));
		
		//these sets (or at least one of them) are required for the next if statement
		sampleDraft.setSampleSubtypeId(sampleSubtypeId);
		sampleDraft.setSampleSubtype(sampleSubtype);
		sampleDraft.setSampleTypeId(sampleSubtype.getSampleType().getId());
		sampleDraft.setSampleType(sampleType);
		if (sampleService.isLibrary(sampleDraft)){
			if(!jobDraft.getWorkflow().getIName().equalsIgnoreCase("bioanalyzer")){//no adaptorsets/adaptors for bioanalyzer libraries
				prepareAdaptorsetsAndAdaptors(jobDraft, normalizedMeta, m);
			}
		}
		
		return "jobsubmit/manysamples";
	}
	@Transactional
	@RequestMapping(value="/manysamples/edit/{jobDraftId}/{sampleSubtypeId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String editManySampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, 
			@PathVariable("sampleSubtypeId") Integer sampleSubtypeId, 
			@RequestParam (value = "theSelectedAdaptorset", required = false) String theSelectedAdaptorset,
			ModelMap m) {
		////************manysamples/edit
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
	////************manysamples/edit
		////IF this is library, must segregate by adaptor type!!!!!!
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleSubtypeId);
		if (sampleSubtype.getId() == null){
			waspErrorMessage("jobDraft.sampleSubtype_null.error");
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		SampleType sampleType = sampleSubtype.getSampleType();
		////************manysamples/edit
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		for(SampleDraft sampleDraft : jobDraft.getSampleDraft()){
			if(sampleDraft.getSampleSubtype().getId().intValue()==sampleSubtype.getId().intValue()){
				List<SampleDraftMeta> normalizedMeta = new ArrayList<SampleDraftMeta>();
				try {			
					normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleDraft.getSampleSubtype(), sampleDraft.getSampleDraftMeta(), SampleDraftMeta.class));
					//not right for here: normalizedMeta.addAll(SampleAndSampleDraftMetaHelper.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, SampleDraftMeta.class));
				} catch (MetadataTypeException e) {
					logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
				}
				sampleDraft.setSampleDraftMeta(normalizedMeta);
				if(sampleService.isLibrary(sampleDraft) && theSelectedAdaptorset!=null && !theSelectedAdaptorset.equals("")){//all libraries except bioanalyzer libraries
					try{	
						Adaptorset adaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", sampleDraft.getSampleDraftMeta())) );
						if(adaptorset.getId().intValue() == Integer.parseInt(theSelectedAdaptorset)){
							sampleDraftList.add(sampleDraft);
						}
					}catch(Exception e){
						logger.warn("Could not get adaptor set from sampledraftmeta: "+ e.getMessage());
					}
				}
				else{//all samples that are not libraries AS Well As bioanalyzer libraries
					sampleDraftList.add(sampleDraft);
				}
			}
		}
		////************manysamples/edit
		m.addAttribute("edit", "true");
		m.addAttribute("jobDraft", jobDraft);
		m.addAttribute("sampleDraftList", sampleDraftList);
		m.addAttribute("sampleSubtype", sampleSubtype);
		m.addAttribute("sampleType", sampleType);
		m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
		m.addAttribute("heading", messageService.getMessage("jobDraft.sample_add_heading.label"));
		
		if(theSelectedAdaptorset!=null && !theSelectedAdaptorset.equals("")){
			m.addAttribute("theSelectedAdaptorset", new Integer(theSelectedAdaptorset));
		}
		else{
			m.addAttribute("theSelectedAdaptorset", "");
		}
		
		///manysamples/edit
		if (! sampleDraftList.isEmpty() && sampleService.isLibrary(sampleDraftList.get(0)) && theSelectedAdaptorset!=null && !theSelectedAdaptorset.equals("")){
			prepareAdaptorsetsAndAdaptors(jobDraft, sampleDraftList.get(0).getSampleDraftMeta(), m);
		}
		
		return "jobsubmit/manysamples";
	}
	@Transactional
	@RequestMapping(value="/manysamples/add/{jobDraftId}/{sampleSubtypeId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateNewManySampleDraft(
			@PathVariable("jobDraftId") Integer jobDraftId, 
			@PathVariable("sampleSubtypeId") Integer sampleSubtypeId,
			@RequestParam (value = "theSelectedAdaptorset", required = false) String theSelectedAdaptorset,
			ModelMap m) {
		
		if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
			return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
		}
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (jobDraft.getId()==null || ! isJobDraftEditable(jobDraft)){
			return "redirect:/dashboard.do";
		}
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleSubtypeId);
		SampleType sampleType = sampleSubtype.getSampleType();

		String[] sampleIdsAsStringArray = request.getParameterValues("sampleId");
		int numberOfIncomingRows = sampleIdsAsStringArray.length;

		String[] sampleDraftNamesAsStringArray = request.getParameterValues("sampleName");
		List<String> sampleDraftNamesAsList = new ArrayList<String>();
		
		String[] deleteRowsAsStringArray = request.getParameterValues("deleteRow");//used only for samples/libraries existing already in the database
		List<String> deleteRowsList = new ArrayList<String>();
		
		List<SampleDraft> sampleDraftList = new ArrayList<SampleDraft>();
		
		List<String> errorList = new ArrayList<String>();
		boolean atLeastOneErrorExists = false;		
		
		int numberOfRowsWithValidSampleId = 0;//represents number of records already in system (updating rather than creating new samples)
		int numberOfCompletelyEmptyRows = 0;
		int counter = -1;
		for(String sampleName : sampleDraftNamesAsStringArray){	
			
			counter++;
			
			String errorsForThisSample = "";
			
			SampleDraft sampleDraft = new SampleDraft();
			String sampleIdAsString = sampleIdsAsStringArray[counter];
			if( !sampleIdAsString.isEmpty() ){	
				numberOfRowsWithValidSampleId++;
				try{
					Integer id = new Integer(sampleIdAsString);
					sampleDraft = sampleDraftDao.getSampleDraftBySampleDraftId(id);
				}
				catch(Exception e){
					continue;//don't know what to do here. for not, skip this 
				}
			}
			sampleDraft.setName(sampleName.trim());//get sampleDraft's sample name
			
			if(deleteRowsAsStringArray!=null){
				deleteRowsList.add(deleteRowsAsStringArray[counter]);//will be yes or no
			}
			
			//get sampleDraftMeta and in process, check it for errors
			List<SampleDraftMeta> sampleDraftMetaList = new ArrayList<SampleDraftMeta>();
			DataBinder dataBinderForMeta = new DataBinder(sampleDraft, "sampleDraft");
			BindingResult resultForMeta = dataBinderForMeta.getBindingResult();
			try {
				sampleDraftMetaList.addAll(SampleAndSampleDraftMetaHelper.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, resultForMeta, SampleDraftMeta.class, counter));
			} catch (MetadataTypeException e) {
				logger.warn("Could not get meta for class 'SampleDraftMeta':" + e.getMessage());
			}
			sampleDraft.setSampleDraftMeta(sampleDraftMetaList);
			
			//we now have all attributes that need to be checked stored within sampleDraft, 
			//so first check for a completely empty row - if completely empty, then ignore the entire row
			if(sampleDraftRowIsCompletelyEmpty(sampleDraft,"adaptorset")){//currently checks sample name and all the metadata; but, ignore adaptorset, as it could be set even if rest of row is empty
				numberOfCompletelyEmptyRows++;
				continue;
			}
			
			//since we know this row is NOT completely empty, check sample name and metaData for entry errors
			
			//first, deal with sample name errors
			if(sampleDraft.getName().isEmpty()){//waspErrorMessage("");
				String sampleNameEmptyMessage = messageService.getMessage("jobsubmitManySamples.sampleNameCannotBeEmpty.error");
				errorsForThisSample += errorsForThisSample.isEmpty()?sampleNameEmptyMessage : "; "+sampleNameEmptyMessage;
			}
			else{				
				//next check against sample names ALREADY in the database, for this draft job
				DataBinder dataBinderForName = new DataBinder(sampleDraft, "sampleDraft");
				BindingResult resultForName = dataBinderForName.getBindingResult();
				if(sampleDraft.getId()==null){
					validateSampleDraftNameUnique(sampleDraft.getName(), 0, jobDraft, resultForName);
				}else{
					validateSampleDraftNameUnique(sampleDraft.getName(), sampleDraft.getId(), jobDraft, resultForName);
				}
			
				if(resultForName.hasErrors()){
					String sampleNameAlreadyUsedOnThisSubmission = messageService.getMessage("jobsubmitManySamples.sampleNameAlreadyUsedOnThisSubmission.error");
					errorsForThisSample += errorsForThisSample.isEmpty()?sampleNameAlreadyUsedOnThisSubmission : "; "+sampleNameAlreadyUsedOnThisSubmission;
				}				
				//finally check against the sample names on this form (not yet in database)
				if(sampleDraftNamesAsList.contains(sampleDraft.getName())){
					String sampleNameAlreadyUsedOnThisForm = messageService.getMessage("jobsubmitManySamples.sampleNameAlreadyUsedOnThisForm.error");
					errorsForThisSample += errorsForThisSample.isEmpty()? sampleNameAlreadyUsedOnThisForm :"; "+sampleNameAlreadyUsedOnThisForm;
				}				
				
				sampleDraftNamesAsList.add(sampleDraft.getName()); //add to this list only if sample name is NOT empty
			}
			
			//second deal with sampleDraftMeta errors, which are currently stored in resultForMeta (a BindingResult object)
			if(resultForMeta.hasErrors()){
				List<FieldError> fieldErrors = resultForMeta.getFieldErrors();
				for(FieldError fe : fieldErrors){
					//logger.debug(fe.getCode());//this is something like chipseqDna.fragmentSize.error
					String metaErrorForDisplay = fe.getCode().substring(fe.getCode().indexOf(".")+1);//something like fragmentSize.error
					metaErrorForDisplay = metaErrorForDisplay.replace(".", " ");//something like fragmentSize error
					errorsForThisSample += errorsForThisSample.isEmpty()?metaErrorForDisplay:"; " + metaErrorForDisplay;
				}
			}
			
			sampleDraftList.add(sampleDraft);
			errorList.add(errorsForThisSample);
			if(!errorsForThisSample.isEmpty()){
				atLeastOneErrorExists = true;				
			}
			
		}
		if(numberOfIncomingRows == numberOfCompletelyEmptyRows){//no data at all was filled in
			if(numberOfRowsWithValidSampleId==0){//all rows are new samples/libraries (none are existing samples for  update)
				return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
			}
		}
		if(atLeastOneErrorExists==true){
			
			/* ********for testing only
			logger.debug("------Print out the errors in errorList:");
			for(String error : errorList){
				logger.debug("------"+error);
			}
			 */
			
			//fill up map, get assorted other info, and return to web page
			waspErrorMessage("sampleDetail.updated.error");
			m.addAttribute("errorList", errorList);
			m.addAttribute("edit", request.getParameter("edit"));//if this is an edit request, send this paramater back to web
			m.addAttribute("jobDraft", jobDraft);
			m.addAttribute("sampleDraftList", sampleDraftList);
			m.addAttribute("sampleSubtype", sampleSubtype);
			m.addAttribute("sampleType", sampleType);
			m.addAttribute("organisms",  genomeService.getOrganismsPlusOther()); // required for metadata control element (select:${organisms}:name:name)
			m.addAttribute("heading", messageService.getMessage("jobDraft.sample_add_heading.label"));
			m.addAttribute("deleteRowsList", deleteRowsList);//used only during editing of existing samples/libraries
			
			//this is not a very good way to do this, but we need to set 
			//these (or at least one of them) in order to execute the next if statement (sampleService.isLibrary())
			sampleDraftList.get(0).setSampleSubtypeId(sampleSubtypeId);
			sampleDraftList.get(0).setSampleSubtype(sampleSubtype);
			sampleDraftList.get(0).setSampleTypeId(sampleSubtype.getSampleType().getId());
			sampleDraftList.get(0).setSampleType(sampleType);
			if (sampleService.isLibrary(sampleDraftList.get(0)) ){
				if(theSelectedAdaptorset!=null && theSelectedAdaptorset.equals("")){
					prepareAdaptorsetsAndAdaptors(jobDraft, sampleDraftList.get(0).getSampleDraftMeta(), m);
					m.addAttribute("theSelectedAdaptorset", "");
				}
				if(theSelectedAdaptorset!=null && !theSelectedAdaptorset.equals("")){
					prepareAdaptorsetsAndAdaptors(jobDraft, sampleDraftList.get(0).getSampleDraftMeta(), m);
					m.addAttribute("theSelectedAdaptorset", new Integer(theSelectedAdaptorset));
				}
				else{
					m.addAttribute("theSelectedAdaptorset", "");
				}
			}
			return "jobsubmit/manysamples";
		}
		
		//no errors, so iterate through sampleDraftList and save each new sampleDraft (excluding rows completely empty)
		int counter2 = -1;
		for(SampleDraft sampleDraftToBeSaved : sampleDraftList){
			
			counter2++;

			//this method now also deals with deleting sampleDrafts (those sampleDrafts that already exist in the database),
			//do if a sampleDraft is due to be deleted, then do execute that next.
			if(!deleteRowsList.isEmpty() && "yes".equals(deleteRowsList.get(counter2).toLowerCase()) && sampleDraftToBeSaved.getId()!=null){
				Map<String, Integer> query = new HashMap<String, Integer>();
				query.put("sampleDraftId", sampleDraftToBeSaved.getId());
				for (SampleDraftJobDraftCellSelection cellSelection : sampleDraftToBeSaved.getSampleDraftJobDraftCellSelection()){
					sampleDraftJobDraftCellSelectionDao.remove(cellSelection);
				}
				for (SampleDraftMeta sdm : sampleDraftMetaDao.findByMap(query)){
					sampleDraftMetaDao.remove(sdm);
				}
				sampleDraftDao.remove(sampleDraftToBeSaved);//obviously a misnomer, since we're deleting this sampleDraft
			}
			else{//else save; will save draft samples/libraries already in the database and new ones as well
				
				//must pull this sampleDraftMeta list out of the sampleDraft and put into a new list, prior to saving sampleDraftToBeSaved; if we did not do this, and the sampleDraft already had an Id, then the meta would be destroyed for some reason, during sampleDraft.save()
				//the meta is subsequently saved, separately, after saving sampleDraftToBeSaved
				List<SampleDraftMeta> sampleDraftMetaToBeSaved = new ArrayList<SampleDraftMeta>(sampleDraftToBeSaved.getSampleDraftMeta());
				
				sampleDraftToBeSaved.setSampleSubtype(sampleSubtype);
				sampleDraftToBeSaved.setSampleType(sampleType);
				sampleDraftToBeSaved.setLabId(jobDraft.getLabId());
				sampleDraftToBeSaved.setUserId(jobDraft.getUserId());
				sampleDraftToBeSaved.setJobDraftId(jobDraft.getId());
				
				SampleDraft sampleDraftSavedToDB = sampleDraftDao.save(sampleDraftToBeSaved);
	
				try {
					sampleDraftMetaDao.setMeta(sampleDraftMetaToBeSaved, sampleDraftSavedToDB.getId());
				} catch (MetadataException e) {
					waspErrorMessage("sampleDetail.updated.error");
					logger.warn("Failed to update metadata!!: " + e.getLocalizedMessage());
					return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
				}
			}
		}		
		waspMessage("sampleDetail.updated_success.label");
		return "redirect:/jobsubmit/samples/"+jobDraftId+".do";
	}
	
	private boolean sampleDraftRowIsCompletelyEmpty(SampleDraft sampleDraftRow, String excludeThisMetaFromConsideration){
		
		if(excludeThisMetaFromConsideration==null){
			excludeThisMetaFromConsideration="";
		}
		
		//checks only for sample name and all meta attributes
		if(!sampleDraftRow.getName().trim().isEmpty()){
			return false;
		}
		for(SampleDraftMeta sdm : sampleDraftRow.getSampleDraftMeta()){
			if(sdm!=null && sdm.getV()!=null && sdm.getK()!=null && !sdm.getK().contains(excludeThisMetaFromConsideration)){//sometimes adaptor might be null
				if(!sdm.getV().trim().isEmpty()){
					return false;
				}
			}
		}
		return true;
	}
	
	@Transactional
	@RequestMapping(value="/samples/addExisting/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String addExistingSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		SampleDraft newSampleDraft = new SampleDraft();
		
		// TODO: functionality here
		
		return "redirect:/jobsubmit/samples/view/"+jobDraftId+"/"+newSampleDraft.getId()+".do";
	}
	
	/**
	   * See if SampleDraft name has changed between sampleDraft objects and if so check if the new name is unique within the jobDraft.
	   * @param formSample
	   * @param originalSample
	   * @param job
	   * @param result
	   */
	@Transactional
	  private void validateSampleDraftNameUnique(String sampleDraftName, Integer sampleDraftId, JobDraft jobDraft, BindingResult result){
		  //confirm that, if a new sample.name was supplied on the form, it is different from all other sample.name in this job
		  List<SampleDraft> sampleDraftsInThisJob = jobDraft.getSampleDraft();
		  for(SampleDraft eachSampleDraftInThisJob : sampleDraftsInThisJob){
			  if(eachSampleDraftInThisJob.getId().intValue() != sampleDraftId.intValue()){
				  if( sampleDraftName.equals(eachSampleDraftInThisJob.getName()) ){
					  // adding an error to 'result object' linked to the 'name' field as the name chosen already exists
					  Errors errors=new BindException(result.getTarget(), "sampleDraft");
					  // reject value on the 'name' field with the message defined in sampleDetail.updated.nameClashError
					  // usage: errors.rejectValue(field, errorString, default errorString)
					  errors.rejectValue("name", "sampleDetail.nameClash.error", "sampleDetail.nameClash.error (no message has been defined for this property)");
					  result.addAllErrors(errors);
					  break;
				  }
			  }
		  }
	  }
	  
	  /**
	   * get adaptorsets and adaptors for populating model. If a selected adaptor is found in the provided SampleDraftMeta
	   * it is used to find appropriate adaptors
	   * @param jobDraft
	   * @param sampleDraftMeta
	   * @param m
	   */
	@Transactional
		private void prepareAdaptorsetsAndAdaptors(JobDraft jobDraft, List<SampleDraftMeta> sampleDraftMeta, ModelMap m){
			List<Adaptorset> adaptorsets = new ArrayList<Adaptorset>();
			for (JobDraftresourcecategory jdrc: jobDraft.getJobDraftresourcecategory()){
				Map<String, Integer> adaptorsetRCQuery = new HashMap<String, Integer>();
				adaptorsetRCQuery.put("resourcecategoryId", jdrc.getResourcecategoryId());
				for (AdaptorsetResourceCategory asrc: adaptorsetResourceCategoryDao.findByMap(adaptorsetRCQuery))
					adaptorsets.add(asrc.getAdaptorset());
			}
			m.addAttribute("adaptorsets", adaptorsets); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
			
			List<Adaptor> adaptors = new ArrayList<Adaptor>();
			Adaptorset selectedAdaptorset = null;
			try{	
	  			selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", sampleDraftMeta)) );
	  		} catch(MetadataException e){
	  			logger.warn("Cannot get metadata genericLibrary.adaptorset. Presumably not be defined: " + e.getMessage());
	  		} catch(NumberFormatException e){
	  			logger.warn("Cannot convert to numeric value for metadata " + e.getMessage());
	  		}
			if (selectedAdaptorset != null){
				adaptors = selectedAdaptorset.getAdaptor();
			} else if (adaptorsets.size() == 1){
				adaptors = adaptorsets.get(0).getAdaptor();
			}
			m.addAttribute("adaptors", adaptors); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		}

	/*
	 * Returns genome builds 
	 * 
	 * @Author asmclellan
	 */	
	@RequestMapping(value="/organism/{organism}/genome/{genomeName}/getBuilds.do", method=RequestMethod.GET)	
	public String adaptorsByAdaptorId(@PathVariable("organism") Integer organism, 
			@PathVariable("genomeName") String genomeName, 
			HttpServletResponse response) {
		Map <String, String> buildsMap = new LinkedHashMap<String, String>();
		try {
			for (String buildName : genomeService.getBuilds(organism, genomeName).keySet()){
				Build build = genomeService.getBuilds(organism, genomeName).get(buildName);
				if (build.isDefault())
					buildName += " (default)";
					buildsMap.put(buildName, build.getDescription());
			}
			return outputJSON(buildsMap, response); 	
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+buildsMap, e);
		}
	}
	
	private boolean setModelParametersForGenomeSelectionReturnIsRequired(JobDraft jobDraft, ModelMap m){
		final String ORGANISM_META_AREA = "genericBiomolecule";
		final String ORGANISM_META_KEY = "organism";
		boolean isGenomeSelectionRequired = false;
		List<SampleDraft> sampleDraftList = jobDraft.getSampleDraft();
		Map<Organism, List<SampleDraft>> sampleDraftsByOrganism = new HashMap<Organism, List<SampleDraft>>();
		Map<Organism, Build> currentBuildByOrganism = new HashMap<Organism, Build>();
		for (SampleDraft sampleDraft : sampleDraftList){
			try {
				List<SampleDraftMeta> sampleDraftMetas = sampleDraft.getSampleDraftMeta();
				if (sampleDraftMetas == null)
					throw new MetadataException("No meta returned for sampleDraft with id: " + sampleDraft.getId());
				Integer organismId = Integer.parseInt(MetaHelperWebapp.getMetaValue(ORGANISM_META_AREA, ORGANISM_META_KEY, sampleDraft.getSampleDraftMeta()));
				if (organismId == 0)
					continue; // Other genome selected
				Organism currentOrganism = genomeService.getOrganismById(organismId);
				if (currentOrganism == null)
					throw new WaspException("No organism found with an id of " + organismId);
				isGenomeSelectionRequired = true;
				if (sampleDraftsByOrganism.get(currentOrganism) == null){
					sampleDraftsByOrganism.put(currentOrganism, new ArrayList<SampleDraft>());
					Build build = null;
					try{
						build = genomeService.getBuild(sampleDraft);
						currentBuildByOrganism.put(currentOrganism, build);
					} catch (ParameterValueRetrievalException e1){} // not found	
				}
				sampleDraftsByOrganism.get(currentOrganism).add(sampleDraft);
			} catch (Exception e) {
				logger.warn(e.getLocalizedMessage());
				waspErrorMessage("jobDraft.organimsNotSelected.error");
			} 
		}
		if (isGenomeSelectionRequired == true){
			m.addAttribute("sampleDraftsByOrganism", sampleDraftsByOrganism);
			m.addAttribute("currentBuildByOrganism", currentBuildByOrganism);
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			m.put("jobDraft", jobDraft);
		}
		return isGenomeSelectionRequired;
	}
	
	
	@RequestMapping(value="/genomes/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String selectGenomesGet(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		boolean isAnalysisSelected = jobDraftService.getIsAnalysisSelected(jobDraft);
		boolean isBuildSelectionRequired = setModelParametersForGenomeSelectionReturnIsRequired(jobDraft, m);
		boolean noBuildButAnalysisSelected = false;
		if (!isBuildSelectionRequired){
			if (!isAnalysisSelected)
				return nextPage(jobDraft);
			else{
				noBuildButAnalysisSelected = true;
				jobDraftService.setIsAnalysisSelected(jobDraft, false);
			}
		}
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		m.put("noBuildButAnalysisSelected", noBuildButAnalysisSelected);
		return "jobsubmit/genomes";
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/genomes/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String selectGenomesPost(
			@PathVariable("jobDraftId") Integer jobDraftId,
			@RequestParam(value="noBuildButAnalysisSelected", required=true) Boolean isNoBuildButAnalysisSelected,
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (isNoBuildButAnalysisSelected)
			return nextPage(jobDraft);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		setModelParametersForGenomeSelectionReturnIsRequired(jobDraft, m);
		Map<Organism, List<SampleDraft>> sampleDraftsByOrganism = (Map<Organism, List<SampleDraft>>) m.get("sampleDraftsByOrganism");
		Map<Organism, String> genomeError = new HashMap<Organism, String>();
		Map<Organism, String> genomesByOrganism = new HashMap<Organism, String>();
		Map<Organism, String> buildsByOrganism = new HashMap<Organism, String>();
		boolean isErrors = false;
		for (Organism organism : sampleDraftsByOrganism.keySet()){
			String buildName = (String) request.getParameter("buildSelect_" + organism.getNcbiID());
			String genomeSelection = (String) request.getParameter("genomeSelect_" + organism.getNcbiID());
			if (buildName == null || buildName.isEmpty() || genomeSelection == null || genomeSelection.isEmpty()){
				genomeError.put(organism, messageService.getMessage("jobDraft.sample_genome_select.error"));
				((Map<Organism, Build>) m.get("currentBuildByOrganism")).remove(organism);
				isErrors = true;
			} else{
				String genomeName = genomeSelection.substring( genomeSelection.lastIndexOf("/") + 1 );
				genomesByOrganism.put(organism, genomeName);
				buildsByOrganism.put(organism, buildName);
				Build build = null;
				try {
					build = genomeService.getBuild(organism.getNcbiID(), genomeName, buildName);
				} catch (ParameterValueRetrievalException e) {
					logger.warn(e.getLocalizedMessage());
					waspErrorMessage("jobDraft.sample_genome_retrieval.error");
					return "jobsubmit/genomes";
				}
				((Map<Organism, Build>) m.get("currentBuildByOrganism")).put(organism, build);
			}
			if (isErrors){
				m.addAttribute("genomeError", genomeError);
				return "jobsubmit/genomes";
			}
		}
		for (Organism organism : sampleDraftsByOrganism.keySet()){
			String genomeName = genomesByOrganism.get(organism);
			String buildName = buildsByOrganism.get(organism);
			logger.debug("genomes: " + organism.getNcbiID() + ":" + genomeName + ":" + buildName);
			Build build = null;
			try {
				build = genomeService.getBuild(organism.getNcbiID(), genomeName, buildName);
			} catch (ParameterValueRetrievalException e) {
				logger.warn(e.getLocalizedMessage());
				waspErrorMessage("jobDraft.sample_genome_retrieval.error");
				return "jobsubmit/genomes";
			}
			Set<SampleDraft> sampleDraftSet = new HashSet<SampleDraft>();
			sampleDraftSet.addAll(sampleDraftsByOrganism.get(organism));
			try{
				genomeService.setBuildToAllSampleDrafts(sampleDraftSet, build);
			} catch (Exception e){
				logger.warn(e.getLocalizedMessage());
				waspErrorMessage("jobDraft.sample_genome_save.error");
				return "jobsubmit/genomes";
			}
			
		}
		return nextPage(jobDraft);
	}

	@Transactional
	@RequestMapping(value="/cells/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showSampleCellDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		
		List<SampleDraft> samples=jobDraft.getSampleDraft();//sampleDraftDao.getSampleDraftByJobId(jobDraftId);
		Map<SampleDraft, Adaptor> adptorsOnSampleDrafts = jobDraftService.getAdaptorsOnSampleDrafts(samples);		

		//Set<String> selectedSampleCell = new HashSet<String>();
		Map<String, String> selectedSampleCell = new HashMap<String, String>();
		//Map<Integer, Integer> cellMap = new HashMap<Integer, Integer>();
		//int cellindexCount = 0;

		for (SampleDraft sd: samples) {
 			for (SampleDraftJobDraftCellSelection sdc: sd.getSampleDraftJobDraftCellSelection()) {
				int cellIndex = sdc.getJobDraftCellSelection().getCellIndex();
				String key = sd.getId() + "_" + cellIndex;
				selectedSampleCell.put(key, "1");
			}
		}
		
		getMetaHelperWebapp().setArea(jobDraft.getWorkflow().getIName());
		jobDraft.setJobDraftMeta(getMetaHelperWebapp().getMasterList(JobDraftMeta.class));
		m.put("jobDraft", jobDraft);
		m.put("sampleDrafts", samples);
		m.put("adptorsOnSampleDrafts", adptorsOnSampleDrafts);
		m.put("selectedSampleCell", selectedSampleCell);
        m.put("pageFlowMap", getPageFlowMap(jobDraft));
		return "jobsubmit/cell";		
	}

	
	@RequestMapping(value="/cells/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateSampleCellDraft(
			@PathVariable("jobDraftId") Integer jobDraftId, 
			ModelMap m) {

		JobDraft jobDraft = jobDraftService.getJobDraftById(jobDraftId);//jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (jobDraft.getId()==null || jobDraft.getId() <= 0){
			logger.warn("jobDraft.jobDraft_null.error");
			waspErrorMessage("jobDraft.jobDraft_null.error");
			return "redirect:/dashboard.do";
			
		}else if(! isJobDraftEditable(jobDraft)){
			logger.warn("jobDraft.not_pending.error");
			waspErrorMessage("jobDraft.not_pending.error");
			return "redirect:/dashboard.do";
		}		

		Map<String, String[]> params = request.getParameterMap();
		List<SampleDraft> samplesOnThisJobDraft = jobDraft.getSampleDraft();
		Map<Integer, List<SampleDraft>> cellMap = jobDraftService.convertWebCellsToMapCells(params, samplesOnThisJobDraft);
		//cellMap is used to confirm validity of cell selections. cellMap stores the info from the web regarding sampleDraft placement on cells
		//if cell selections are not acceptable, then cellMap is used to re-populate cells on the web page return "jobsubmit/cell";
		//if cell selections ARE acceptable, then add selections to database and move on to next page (return nextPage(jobDraft);)
		Boolean errors = false;
		try{
			jobDraftService.confirmAllDraftSamplesOnAtLeastOneCell(cellMap, samplesOnThisJobDraft);//confirm that all samples (from web) have been placed on at least on cell
			jobDraftService.confirmNoBarcodeOverlapPerCell(cellMap);//confirm that no barcodes appear more than once per cell (from web)
			jobDraftService.confirmNONEBarcodeIsUniquePerCell(cellMap);//confirm that any user submitted library with a NONE barcode MUST be the sole library on a cell (from web)
		}catch(Exception e){ errors = true; logger.warn(e.getMessage()); waspErrorMessage(e.getMessage()); }
		
		//if placement of samples is unacceptable (meaning that an exception was thrown), 
		//get data for re-display on the GET, prepare to show flash error message, and navigate to jsp: "return jobsubmit/cell"
		if(errors){			
			Map<String, String> selectedSampleCell = new HashMap<String,String>();//only interested in the key; object is irrelevant, but must not be an empty string
			for(Integer cellIndex : cellMap.keySet()){
				List<SampleDraft> sdList = cellMap.get(cellIndex);
				for(SampleDraft sd : sdList){
					String key = sd.getId() + "_" + cellIndex.intValue();
					selectedSampleCell.put(key,"present");
				}
			}
			getMetaHelperWebapp().setArea(jobDraft.getWorkflow().getIName());
			jobDraft.setJobDraftMeta(getMetaHelperWebapp().getMasterList(JobDraftMeta.class));
			m.put("jobDraft", jobDraft);
			m.put("sampleDrafts", samplesOnThisJobDraft);//m.put("sampleDrafts", samples);
			Map<SampleDraft, Adaptor> adptorsOnSampleDrafts = jobDraftService.getAdaptorsOnSampleDrafts(samplesOnThisJobDraft);		
			m.put("adptorsOnSampleDrafts", adptorsOnSampleDrafts);
			m.put("selectedSampleCell", selectedSampleCell);//m.put("selectedSampleCell", selectedSampleCell);
			m.put("pageFlowMap", getPageFlowMap(jobDraft));
			return "jobsubmit/cell";   
			
		}
		
		//if all is OK		
		jobDraftService.createUpdateJobDraftCells(jobDraft, params);//update and commit to database (the service method is transactional)
		return nextPage(jobDraft);		
	}
	
	@Transactional
	@RequestMapping(value="/comment/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String commentJobDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		m.put("jobDraft", jobDraft);
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		String comment=null;
		try{
			comment = jobDraftService.getUserJobDraftComment(jobDraftId);
		}
		catch(Exception e){logger.warn(e.getMessage()); waspErrorMessage("jobDraft.commentFetch.error");}
		m.put("comment", comment==null?"":comment);
		return "jobsubmit/comment";
	}


	@Transactional
	@RequestMapping(value="/comment/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String commentJobDraftPost(@PathVariable("jobDraftId") Integer jobDraftId, 
			@RequestParam(value="comment", required=false) String comment, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		//String trimmedComment = comment==null?"":StringEscapeUtils.escapeXml(comment.trim());//any standard html/xml [Supports only the five basic XML entities (gt, lt, quot, amp, apos)] will be converted to characters like &gt; //http://commons.apache.org/lang/api-3.1/org/apache/commons/lang3/StringEscapeUtils.html#escapeXml%28java.lang.String%29
		String trimmedComment = comment==null?"":comment.trim();//7-7-14
		
		//as of 10-14-14, no longer optional. We insist on at least a description of sample description method.
		if(trimmedComment.isEmpty()){
			waspErrorMessage("jobDraft.commentNotOptional.error");
			m.put("comment", trimmedComment);
			m.put("jobDraft", jobDraft);
			m.put("pageFlowMap", getPageFlowMap(jobDraft));

			return "jobsubmit/comment";
		}
		
		try{
			jobDraftService.saveUserJobDraftComment(jobDraftId, trimmedComment);
		}catch(Exception e){
			logger.warn(e.getMessage());
			waspErrorMessage("jobDraft.commentCreate.error");
			//return "redirect:/jobsubmit/comment/"+jobDraftId+".do";//forget this, just go to next page
		}

		return nextPage(jobDraft);
	}
	
	@Transactional
	@RequestMapping(value="/verify/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showJobDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		m.put("jobDraft", jobDraft);

		List <SampleDraft> sampleDraftList = jobDraft.getSampleDraft();
		m.put("sampleDraft", sampleDraftList);
        m.put("pageFlowMap", getPageFlowMap(jobDraft));

		return "jobsubmit/verify";
	}

	@Transactional
	@RequestMapping(value="/verify/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String verifyJob(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";

		// TODO ClassLoader for validateJobDraft 

		// JobDraftValidator jdv = getDefaultJobDraftValidator();
		try {
			List<WorkflowMeta> wfmList = jobDraft.getWorkflow().getWorkflowMeta(); 
			for (WorkflowMeta wfm: wfmList) {
				if (wfm.getK().equals("workflow.validatorClass")) {
					ClassLoader cl = JobSubmissionController.class.getClassLoader();
					cl.loadClass(wfm.getV()).newInstance();


					break;
				}
			}
			// JobDraftValidator 
		} catch (Exception e) {
		}

		// jdv.validate(jobDraft);


		return nextPage(jobDraft);
	}


	@RequestMapping(value="/submit/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public /*Callable<String>*/ String submitJob(@PathVariable("jobDraftId") final Integer jobDraftId, final ModelMap m) {
		final User me = authenticationService.getAuthenticatedUser(); // need to do this here as no access to SecurityContextHolder off the main thread
		
		// Use asynchronous request processing to handle the business logic here as job submission process make take a few secs due to daemon delays
		// and we should do this work on a separate thread to free up the servlet for other tasks.
		// NOTE 1: As for all anonymous inner classes, the variables passed in MUST be final to prohibit object re-assignment.
		// NOTE 2: As we use the security context when writing pages, we must use 'redirect' when returning the destination page. 
		
		/*return new Callable<String>() {

			@Override
			public String call() throws Exception {*/
				
				JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
				boolean error = false;
				if (! isJobDraftEditable(jobDraft, me))
					return "redirect:/dashboard.do";
				Job newJob = null;
				try {
					newJob = jobService.createJobFromJobDraft(jobDraft, me);
					if(newJob==null || newJob.getId()==null || newJob.getId().intValue()<=0){
						logger.warn("Error creating new job");
						waspErrorMessage("jobDraft.createJobFromJobDraft.error");
						error = true;
					} 
				} catch (FileMoveException e) {
					logger.warn(e.getMessage());
					waspErrorMessage("jobDraft.createJobFromJobDraft.error");
					error = true;
				} catch (MessagingException e) {
					logger.warn(e.getMessage());
					waspErrorMessage("jobDraft.createJobFromJobDraft.error");
					error = true;
				} catch (Exception e){
					logger.warn("Caught unknown exception: " + e.getMessage());
					waspErrorMessage("jobDraft.createJobFromJobDraft.error");
					error = true;
				}
				if(error){
					// need to re-direct here and provide a function to handle the request mapping rather than simply going 
					// to 'jobsubmit/failed' as we used to.
					// This is because there is no access to the security context within a Callable thread which is required when creating new page).
					// By re-directing we return back to the main servlet thread again and all is well with accessing security information.
					return "redirect:/jobsubmit/failed/" + jobDraftId + ".do";
				}
				return nextPage(jobDraft); // no Security Context problem as evaluates to a redirected link e.g 'redirect:/submitjob/ok.do'
	//		}
	//	};
		
	}
	
	@RequestMapping(value="/failed/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String failed(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftDao().getJobDraftByJobDraftId(jobDraftId);
		if (! isJobDraftEditable(jobDraft))
			return "redirect:/dashboard.do";
		m.put("jobDraft", jobDraft);

		List <SampleDraft> sampleDraftList = jobDraft.getSampleDraft();
		m.put("sampleDraft", sampleDraftList);
        m.put("pageFlowMap", getPageFlowMap(jobDraft));
		return "jobsubmit/failed";
	}

	@RequestMapping(value="/ok.do", method=RequestMethod.GET)
	public String ok(ModelMap m){
		// need this function as using Callable (no access to security context within Callable thread)
		doReauth();
		return "redirect:/jobsubmit/complete.do"; // must insert a redirect to ensure using updated SecurityContextHolder
	}
	
	@RequestMapping(value="/complete.do", method=RequestMethod.GET)
	public String complete(ModelMap m){
		return "jobsubmit/ok";
	}
	
	
	
	/*
	 * Returns adaptors by adaptorsetID 
	 * 
	 * @Author asmclellan
	 */	
	@Transactional
	@RequestMapping(value="/adaptorsByAdaptorsetId", method=RequestMethod.GET)	
	public String adaptorsByAdaptorId(@RequestParam("adaptorsetId") Integer adaptorsetId, HttpServletResponse response) {
	
		//result
		Map <Integer, String> adaptorsMap = new LinkedHashMap<Integer, String>();
		for(Adaptor adaptor:adaptorsetDao.getAdaptorsetByAdaptorsetId(adaptorsetId).getAdaptor()) {
			adaptorsMap.put(adaptor.getId(), adaptor.getName());
		}

		try {
			
			return outputJSON(adaptorsMap, response); 	
			
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+adaptorsMap,e);
		}
	}


	

	@Transactional
	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);
		m.addAttribute("sampleSubtypes",subSampleTypeDao.findAll());
		Map<String, String> statuses=new TreeMap<String, String>();
		for(SampleDraft.Status status:SampleDraft.Status.values()) {
			statuses.put(status.name(), status.name());
		}
		m.addAttribute("statuses",statuses);
		
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
