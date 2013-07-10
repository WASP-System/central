package edu.yu.einstein.wasp.controller;

import java.io.BufferedReader;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.controller.util.SampleWrapperWebapp;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCellSelection;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.WaspModel;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;
import edu.yu.einstein.wasp.util.StringHelper;
import edu.yu.einstein.wasp.web.Tooltip;



@Controller
@Transactional
@RequestMapping("/job")
public class JobController extends WaspController {

	private JobDao	jobDao;

	@Autowired
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	public JobDao getJobDao() {
		return this.jobDao;
	}

	private JobUserDao	jobUserDao;

	@Autowired
	public void setJobUserDao(JobUserDao jobUserDao) {
		this.jobUserDao = jobUserDao;
	}

	public JobUserDao getJobUserDao() {
		return this.jobUserDao;
	}

	private RoleDao	roleDao;

	@Autowired
	public void setJobUserDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public RoleDao getRoleUserDao() {
		return this.roleDao;
	}


	@Autowired
	private LabDao		labDao;
	@Autowired
	private WorkflowresourcecategoryDao workflowresourcecategoryDao;
	@Autowired
	private JobCellSelectionDao jobCellSelectionDao;
	@Autowired
	private AdaptorsetDao adaptorsetDao;
	@Autowired
	private AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;
	@Autowired
	private SampleService sampleService;
	@Autowired
	private FilterService filterService;
	@Autowired
	private FileService fileService;
	@Autowired
	private JobService jobService;
	@Autowired
	private UserService UserService;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private AdaptorService adaptorService;
	@Autowired
	private RunService runService;
	@Autowired
	private MessageServiceWebapp messageService;
	
	// list of baserolenames (da-department admin, lu- labuser ...)
	// see role table
	// higher level roles such as 'lm' or 'js' are used on the view
	public static enum DashboardEntityRolename {
		da, lu, jv, jd, su, ga
	};
	
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(JobMeta.class, request.getSession());
	}
	
	@RequestMapping(value = "/analysisParameters/{jobId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
	public String jobAnalysisParameters(@PathVariable("jobId") Integer jobId, ModelMap m) {
	
		Job job = jobService.getJobByJobId(jobId);
		if(job==null || job.getJobId()==null || job.getJobId().intValue()<=0){
			waspErrorMessage("jobComment.job.error");
			return "redirect:/dashboard.do";
		}		
		m.addAttribute("job", job);
		m.addAttribute("parentArea", "job");
		
		//deal with software
		List<Software> softwareList = jobService.getSoftwareForJob(job);
		m.addAttribute("softwareList", softwareList);
		Map<Software, List<JobMeta>> softwareAndSyncdMetaMap = new HashMap<Software, List<JobMeta>>();
		MetaHelperWebapp mhwa = getMetaHelperWebapp();
		List<JobMeta> jobMetaList = job.getJobMeta();
		for(Software sw : softwareList){
			mhwa.setArea(sw.getIName());
			List<JobMeta> softwareMetaList = mhwa.syncWithMaster(jobMetaList);
			softwareAndSyncdMetaMap.put(sw, softwareMetaList);
		}	
		m.addAttribute("softwareAndSyncdMetaMap", softwareAndSyncdMetaMap);
		
		//deal with samplePairs
		String samplePairs = null;
		for(JobMeta jm : jobMetaList){
			if(jm.getK().indexOf("samplePairs")>-1){
				samplePairs = jm.getV();
				break;
			}
		}
		
		List <Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
		m.addAttribute("submittedSamplesList", submittedSamplesList);
		
		Map<Sample, List<String>> samplePairsMap = jobService.decodeSamplePairs(samplePairs, submittedSamplesList); 
		List<String> controlIsReferenceList = new ArrayList<String>();
		List<String> testIsReferenceList = new ArrayList<String>();
		jobService.decodeSamplePairsWithReference(samplePairs, submittedSamplesList, controlIsReferenceList, testIsReferenceList);
		m.addAttribute("samplePairsMap", samplePairsMap);
		m.addAttribute("controlIsReferenceList", controlIsReferenceList);
		m.addAttribute("testIsReferenceList", testIsReferenceList);
		
		return "job/analysisParameters";
	}
	
	@RequestMapping(value = "/comments/{jobId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
	public String jobComments(@PathVariable("jobId") Integer jobId, ModelMap m) {
	
		Job job = jobService.getJobByJobId(jobId);
		if(job==null || job.getJobId()==null || job.getJobId().intValue()<=0){
			waspErrorMessage("jobComment.job.error");
			return "redirect:/dashboard.do";
		}		
		m.addAttribute("job", job);
		
		//get the user-submitted job comment (if any); should be zero or one
		List<MetaMessage> userSubmittedJobCommentsList = jobService.getUserSubmittedJobComment(jobId);
		for (MetaMessage metaMessage: userSubmittedJobCommentsList){
			metaMessage.setValue(StringUtils.replace(metaMessage.getValue(), "\r\n" ,"<br />"));//carriage return was inserted at time of INSERT to deal with line-break. Change it to <br /> for proper html display (using c:out escapeXml=false). Note that other html was escpaped at INSERT stage (see line 180 below) 
		}
		m.addAttribute("userSubmittedJobCommentsList", userSubmittedJobCommentsList);
		
		//get the facility-generated job comments (if any)
		List<MetaMessage> facilityJobCommentsList = jobService.getAllFacilityJobComments(jobId);
		for (MetaMessage metaMessage: facilityJobCommentsList){
			metaMessage.setValue(StringUtils.replace(metaMessage.getValue(), "\r\n" ,"<br />"));
		}
		m.addAttribute("facilityJobCommentsList", facilityJobCommentsList);
		
		boolean permissionToAddEditComment = false;
		try{
			permissionToAddEditComment = authenticationService.hasPermission("hasRole('su') or hasRole('fm') or hasRole('ft')");
		}catch(Exception e){
			waspErrorMessage("jobComment.jobCommentAuth.error");
			return "redirect:/dashboard.do";
		}
		m.addAttribute("permissionToAddEditComment", permissionToAddEditComment);
		return "job/comments";
	}
	
	@RequestMapping(value = "/comments/{jobId}", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String jobCommentsPost(@PathVariable("jobId") Integer jobId,  @RequestParam("comment") String comment, ModelMap m) {
	
		Job job = jobService.getJobByJobId(jobId);
		if(job==null || job.getJobId()==null || job.getJobId().intValue()<=0){
			waspErrorMessage("jobComment.job.error");
			return "redirect:/dashboard.do";
		}

		String trimmedComment = comment==null?null:StringEscapeUtils.escapeXml(comment.trim());//any standard html/xml [Supports only the five basic XML entities (gt, lt, quot, amp, apos)] will be converted to characters like &gt; //http://commons.apache.org/lang/api-3.1/org/apache/commons/lang3/StringEscapeUtils.html#escapeXml%28java.lang.String%29
		if(trimmedComment==null||trimmedComment.length()==0){
			waspErrorMessage("jobComment.jobCommentEmpty.error");
			return "redirect:/job/comments/"+jobId+".do";
		}
		
		try{
			jobService.setFacilityJobComment(jobId, trimmedComment);
		}catch(Exception e){
			logger.warn(e.getMessage());
			waspErrorMessage("jobComment.jobCommentCreate.error");
			return "redirect:/job/comments/"+jobId+".do";
		}
		
		waspMessage("jobComment.jobCommentAdded.label");
		return "redirect:/job/comments/"+jobId+".do";
	}
	
	@RequestMapping("/list")
	public String list(ModelMap m) {
		//List<Job> jobList = this.getJobDao().findAll();

		//m.addAttribute("job", jobList);

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);
		
		m.addAttribute("viewerIsFacilityMember", "false");
		if(authenticationService.isFacilityMember()){
			m.addAttribute("viewerIsFacilityMember", "true");//send to jsp; this way don't have to perform multiple sec:authorize access= tests!!
		}
		return "job/list";
	}
	
	@RequestMapping(value="/listJSON", method=RequestMethod.GET)
	public String getListJSON(HttpServletResponse response) {
		
		//This method is aware of the web viewer, based on roles, and  the output from this method is to display jobs that the user is permitted to see
		
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<Job> tempJobList = new ArrayList<Job>();
		List<Job> jobList = new ArrayList<Job>();

		//Parameters coming from the jobGrid
		String sord = request.getParameter("sord");//grid is set so that this always has a value
		String sidx = request.getParameter("sidx");//grid is set so that this always has a value
		//Parameters coming from grid's toolbar
		//The jobGrid's toolbar's is it's search capability. The toolbar's attribute stringResult is currently set to false, 
		//meaning that each parameter on the toolbar is sent as a key:value pair
		//If stringResult = true, the parameters containing values would have been sent as a key named filters in JSON format 
		//see http://www.trirand.com/jqgridwiki/doku.php?id=wiki:toolbar_searching
		//below we capture parameters on job grid's search toolbar by name (key:value).
		String jobIdAsString = request.getParameter("jobId")==null?null:request.getParameter("jobId").trim();//if not passed, jobIdAsString will be null
		String jobname = request.getParameter("name")==null?null:request.getParameter("name").trim();//if not passed, will be null
		String submitterNameAndLogin = request.getParameter("submitter")==null?null:request.getParameter("submitter").trim();//if not passed, will be null
		String piNameAndLogin = request.getParameter("pi")==null?null:request.getParameter("pi").trim();//if not passed, will be null
		String createDateAsString = request.getParameter("createts")==null?null:request.getParameter("createts").trim();//if not passed, will be null
		//logger.debug("jobIdAsString = " + jobIdAsString);logger.debug("jobname = " + jobname);logger.debug("submitterNameAndLogin = " + submitterNameAndLogin);logger.debug("piNameAndLogin = " + piNameAndLogin);logger.debug("createDateAsString = " + createDateAsString);

		//Additional URL parameters coming from a call from the userGrid (example: job/list.do?UserId=2&labId=3). [A similar url call came from dashboard, but on 8/16/12 it was altered and no longer sends any parameter]  
		//Note that these two request parameters attached to the URL SHOULD BE mutually exclusive with submitter and pi coming from the jobGrid's toolbar
		String userIdFromURL = request.getParameter("UserId");//if not passed, UserId is the empty string (interestingly, it's value is not null)
		String labIdFromURL = request.getParameter("labId");//if not passed, labId is the empty string (interestingly, it's value is not null)
		//logger.debug("userIdFromURL = " + userIdFromURL);logger.debug("labIdFromURL = " + labIdFromURL);
		
		//DEAL WITH PARAMETERS
		
		//deal with jobId
		Integer jobId = null;
		if(jobIdAsString != null){//something was passed
			jobId = StringHelper.convertStringToInteger(jobIdAsString);//returns null is unable to convert
			if(jobId == null){//perhaps the passed value was abc, which is not a valid jobId
				jobId = new Integer(0);//fake it so that result set will be empty; this way, the search will be performed with jobId = 0 and will come up with an empty result set
			}
		}		
		
		//nothing to do to deal with jobname
		
		//deal with submitter from grid and UserId from URL (note that submitterNameAndLogin and userIdFromURL can both be null, but if either is not null, only one should be not null)
		User submitter = null;
		//from grid
		if(submitterNameAndLogin != null){//something was passed; expecting firstname lastname (login)
			String submitterLogin = StringHelper.getLoginFromFormattedNameAndLogin(submitterNameAndLogin.trim());//if fails, returns empty string
			if(submitterLogin.isEmpty()){//most likely incorrect format !!!!for later, if some passed in amy can always do search for users with first or last name of amy, but would need to be done by searching every job
				submitter = new User();
				submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
			}
			else{
				submitter = userDao.getUserByLogin(submitterLogin);
				if(submitter.getUserId()==null){//if not found in database, submitter is NOT null and getUserId()=null
					submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
				}
			}
		}//else deal with the UserId from URL next
		else if(userIdFromURL != null && !userIdFromURL.isEmpty()){//something was passed; should be a number 
			Integer submitterIdAsInteger = StringHelper.convertStringToInteger(userIdFromURL);//returns null is unable to convert
			if(submitterIdAsInteger == null){
				submitter = new User();
				submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
			}
			else{
				submitter = userDao.getUserByUserId(submitterIdAsInteger.intValue());
				if(submitter.getUserId()==null){//if not found in database, submitter is NOT null and getUserId()=null
					submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
				}
			}
		}
		
		//deal with PI (lab)
		User pi = null;
		Lab piLab = null;//this is what's tested below
		if(piNameAndLogin != null){//something was passed; expecting firstname lastname (login)
			String piLogin = StringHelper.getLoginFromFormattedNameAndLogin(piNameAndLogin.trim());//if fails, returns empty string
			if(piLogin.isEmpty()){//likely incorrect format
				piLab = new Lab();
				piLab.setLabId(new Integer(0));//fake it; result set will come up empty
			}
			else{
				pi = userDao.getUserByLogin(piLogin);//if User not found, pi object is NOT null and pi.getUnserId()=null
				if(pi.getUserId()==null){
					piLab = new Lab();
					piLab.setLabId(new Integer(0));//fake it; result set will come up empty
				}
				else{
					piLab = labDao.getLabByPrimaryUserId(pi.getUserId().intValue());//if the Lab not found, piLab object is NOT null and piLab.getLabId()=null
					if(piLab.getLabId()==null){
						piLab.setLabId(new Integer(0));//fake it; result set will come up empty
					}
				}
			}
		}//else deal with the labId from URL next
		else if(labIdFromURL != null && !labIdFromURL.isEmpty()){
			Integer labIdAsInteger = StringHelper.convertStringToInteger(labIdFromURL);//returns null is unable to convert
			if(labIdAsInteger == null){
				piLab = new Lab();
				piLab.setLabId(new Integer(0));//fake it; result set will come up empty
			}
			else{
				piLab = labDao.getLabByLabId(labIdAsInteger.intValue());//if the Lab not found, piLab object is NOT null and piLab.getLabId()=null
				if(piLab.getLabId()==null){
					piLab.setLabId(new Integer(0));//fake it; result set will come up empty
				}
			}
		}
		
		//deal with createts
		Date createts = null;
		if(createDateAsString != null){
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			try{				
				createts = (Date)formatter.parse(createDateAsString); 
			}
			catch(Exception e){ 
				createts = new Date(0);//fake it; parameter of 0 sets date to 01/01/1970 which is NOT in this database. So result set will be empty
			}
		}
						
		//web viewer is a member of the facility or administration
		//if(authenticationService.hasRole("su")||authenticationService.hasRole("fm")||authenticationService.hasRole("ft")
		//		||authenticationService.hasRole("sa")||authenticationService.hasRole("ga")||authenticationService.hasRole("da")){
		//if(authenticationService.isFacilityMember()){//true if viewer has role of su, fm, ft, sa, ga, da	
		Map<String, Object> m = new HashMap<String, Object>();
		if(jobId != null){
			m.put("id", jobId.intValue());
		}
		if(jobname != null){
			m.put("name", jobname.trim());
		}
		if(submitter != null){
			m.put("userId", submitter.getId().intValue());
		}
		if(piLab != null){
			m.put("labId", piLab.getId().intValue());
		}
		Map<String, Date> dateMap = new HashMap<String, Date>();
		if(createts != null){
			dateMap.put("createts", createts);
		}

		List<String> orderByColumnAndDirection = new ArrayList<String>();		
		if(sidx!=null && !"".equals(sidx)){//sord is apparently never null; default is desc
			if(sidx.equals("jobId")){
				orderByColumnAndDirection.add("id " + sord);
			}
			else if(sidx.equals("name")){//job.name
				orderByColumnAndDirection.add("name " + sord);
			}
			else if(sidx.equals("submitter")){
				orderByColumnAndDirection.add("user.lastName " + sord);
				orderByColumnAndDirection.add("user.firstName " + sord);
			}
			else if(sidx.equals("pi")){
				orderByColumnAndDirection.add("lab.user.lastName " + sord);
				orderByColumnAndDirection.add("lab.user.firstName " + sord);
			}
			else if(sidx.equals("createts")){
				orderByColumnAndDirection.add("createts " + sord);
			}
		}
		else if(sidx==null || "".equals(sidx)){
			orderByColumnAndDirection.add("id desc");
		}
			
		tempJobList = this.jobDao.findByMapsIncludesDatesDistinctOrderBy(m, dateMap, null, orderByColumnAndDirection);

		if(authenticationService.isFacilityMember()){//true if viewer has role of su, fm, ft, sa, ga, da
			if(authenticationService.isOnlyDepartmentAdministrator()){//turns out that the DA doesn't appear to have access to this page
				//perform ONLY if the viewer is A DA but is NOT any other type of facility member
				//in order to remove jobs not in the DA's department
				List<Job> jobsToKeep = filterService.filterJobListForDA(tempJobList);//this returned list is jobs in the DA's departments
				tempJobList.retainAll(jobsToKeep);
			}
		}
		else{//not a facility member of any type, so regular viewer (labmember or PI)
		
			//note that as of now, no jobGrid searching capacity is permitted for this type of viewer - 
			//instead, simply show all jobs that the person may view (note, if PI, (s)he see's all jobs in that lab)
			//this should be changed in future

			User viewer = authenticationService.getAuthenticatedUser();//the web viewer that is logged on that wants to see his/her submitted or viewable jobs
			List<Job> jobsToKeep = jobService.getJobsSubmittedOrViewableByUser(viewer);//default order is by jobId/desc
			tempJobList.retainAll(jobsToKeep);
		}
		jobList.addAll(tempJobList);
	
		
		
		
		//Format output for grid by pages
		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = jobList.size();										// total number of rows
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
				int selIndex = jobList.indexOf(jobDao.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<Job> jobPage = jobList.subList(frId, toId);
			for (Job job:jobPage) {
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", job.getJobId());
				 
				List<JobMeta> jobMeta = getMetaHelperWebapp().syncWithMaster(job.getJobMeta());
				
				User user = userDao.getById(job.getUserId());
				Format formatter = new SimpleDateFormat("yyyy/MM/dd");	
				AcctQuote currentQuote = job.getCurrentQuote();
				String quoteAsString;
				if(currentQuote == null || currentQuote.getId() == null){
					quoteAsString = "?.??";
				}
				else{
					try{
						  Float price = new Float(currentQuote.getAmount());
						  quoteAsString = String.format("%.2f", price);
					}
					catch(Exception e){
						  quoteAsString = "?.??"; 
					}					
				}
				
				String currentStatus = jobService.getJobStatus(job);
				String jobStatusComment = jobService.getJobStatusComment(job);
				if (jobStatusComment != null)
					currentStatus += Tooltip.getCommentHtmlString(jobStatusComment);
				
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							//"J" + job.getJobId().intValue() + " (<a href=/wasp/sampleDnaToLibrary/listJobSamples/"+job.getJobId()+".do>details</a>)",
							"<a href=/wasp/sampleDnaToLibrary/listJobSamples/"+job.getId()+".do>J"+job.getId().intValue()+"</a>",
							job.getName(),
							user.getNameFstLst(),
							//job.getLab().getName() + " (" + pi.getNameLstCmFst() + ")",
							job.getLab().getUser().getNameFstLst(),
							formatter.format(job.getCreated()),
							//String.format("%.2f", amount),
							quoteAsString,
							currentStatus,
							//"<a href=/wasp/"+job.getWorkflow().getIName()+"/viewfiles/"+job.getJobId()+".do>View files</a>"
							//"<a href=/wasp/jobresults/treeview.do?id="+job.getJobId()+"&type=job>View Results</a>"
							"<a href=/wasp/jobresults/treeview/job/"+job.getId()+".do>View Results</a>"
				}));
				 
				for (JobMeta meta:jobMeta) {
					cellList.add(meta.getV());
				}				
				 
				cell.put("cell", cellList);				 
				rows.add(cell);
			}
			 
			jqgrid.put("rows",rows);
			
			return outputJSON(jqgrid, response); 	
			 
		} 
		catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jobList,e);
		}	
	}
	
	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
	public String subgridJSON(@RequestParam("id") Integer jobId,ModelMap m, HttpServletResponse response) {
				
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		Job job = this.jobDao.getById(jobId);
		
		//List<JobSample> jobSampleList = job.getJobSample();//don't do it this way; dubin 2-23-12		
	 	//ObjectMapper mapper = new ObjectMapper();//doesn't appear to be used

		//For a list of the macromolecule and library samples initially submitted to a job, pull from table jobcell and exclude duplicates
		//Note that table jobsample is not appropriate, as it will eventually contain records for libraries made by the facility 
		Set<Sample> samplesAsSet = new HashSet<Sample>();//used to store set of unique samples submitted by the user for a specific job
		Map<String, Integer> filter = new HashMap<String, Integer>();
		filter.put("jobId", job.getJobId());
		List<JobCellSelection> jobCellSelections = jobCellSelectionDao.findByMap(filter);
		for(JobCellSelection jobCellSelection : jobCellSelections){
			List<SampleJobCellSelection> sampleJobCellSelections = jobCellSelection.getSampleJobCellSelection();
			for(SampleJobCellSelection sampleJobCellSelection : sampleJobCellSelections){
				samplesAsSet.add(sampleJobCellSelection.getSample());
			}
		}
		List<Sample> samples = new ArrayList<Sample>();//this List is needed in order to be able to sort the list (so that it appears the same each time it is displayed on the web; you can't sort a set)
		for(Sample sample : samplesAsSet){
			samples.add(sample);
		}
		class SampleNameComparator implements Comparator<Sample> {
		    @Override
		    public int compare(Sample arg0, Sample arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		Collections.sort(samples, new SampleNameComparator());//sort by sample's name using class SampleNameComparator immediately above this line (we needed a list, as you can't sort a set)

		try {
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			for (Sample sample:samples) {
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", sample.getSampleId());
					 					
				List<String> cellList = new ArrayList<String>(
						Arrays.asList(
								new String[] {
										"<a href=/wasp/sample/list.do?selId=" + sample.getSampleId().intValue() + ">" + 
											sample.getName() + "</a>",
										sample.getSampleType().getName(),
										sample.getSampleSubtype().getName(), 
										sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(sample))
								}
						)
				);
					 
				cell.put("cell", cellList);
				rows.add(cell);
			}
			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON " + samples, e);
		 }
	
	}
	
	@RequestMapping(value = "/detail/{jobId}", method = RequestMethod.GET)
	public String detail(@PathVariable("jobId") Integer jobId, ModelMap m) {
		String now = (new Date()).toString();

		Job job = jobService.getJobByJobId(jobId); //this.getJobDao().getById(jobId);

		List<JobMeta> jobMetaList = job.getJobMeta();
		jobMetaList.size();

		List<JobSample> jobSampleList = job.getJobSample();
		jobSampleList.size();

		List<JobFile> jobFileList = job.getJobFile();
		jobFileList.size();

		List<JobUser> jobUserList = job.getJobUser();
		jobUserList.size();

		m.addAttribute("now", now);
		m.addAttribute("job", job);
		m.addAttribute("jobmeta", jobMetaList);
		m.addAttribute("jobsample", jobSampleList);
		m.addAttribute("jobfile", jobFileList);
		m.addAttribute("jobuser", jobUserList);

		return "job/detail";
	}
	
  @RequestMapping(value="/user/roleAdd", method=RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('lm-' + #labId) or hasRole('js-' + #jobId)")
  public String jobViewerUserRoleAdd (
      @RequestParam("labId") Integer labId,
      @RequestParam("jobId") Integer jobId,
      @RequestParam("login") String login, //10-11-11 changed from useremail to login, AND 10-20-11 changed login format from jgreally to the AJAX-generated and formatted login of John Greally (jgreally), so must now extract the login from the formatted string 
      ModelMap m) {
 
	Job job = jobService.getJobByJobId(jobId);//this.jobDao.findById(jobId);
	if(job.getJobId() == null || job.getLabId().intValue() != labId.intValue()){
		waspErrorMessage("job.jobViewerUserRoleAdd.error1");//this job not found in database or the labId does not belong to this job
	}
	else{   
		String extractedLogin = StringHelper.getLoginFromFormattedNameAndLogin(login);
		User user = userDao.getUserByLogin(extractedLogin);
		if(user.getUserId() == null){
			waspErrorMessage("job.jobViewerUserRoleAdd.error2");//user login name does not exist
		}
		else{
			//check that login does not belong to the job submitter (or is not already a job-viewer)
			JobUser jobUser = this.jobUserDao.getJobUserByJobIdUserId(jobId, user.getUserId());
			if(jobUser.getJobUserId() != null){
				if( "js".equals( jobUser.getRole().getRoleName() ) ){
					waspErrorMessage("job.jobViewerUserRoleAdd.error3");//user is submitter (and thus is, by default, a job-viewer)
				}
				else if( "jv".equals( jobUser.getRole().getRoleName() ) ){
					waspErrorMessage("job.jobViewerUserRoleAdd.error4");//user is already a job-viewer
				}
			}
			else{
				Role role = roleDao.getRoleByRoleName("jv");
			    JobUser jobUser2 = new JobUser();
			    jobUser2.setJobId(jobId);
			    jobUser2.setUserId(user.getUserId());
			    jobUser2.setRoleId(role.getRoleId());
			    jobUserDao.save(jobUser2);
			}
		}
	}

    return "redirect:/job/detail/" + jobId + ".do";
  }

	
  @RequestMapping(value="/user/roleRemove/{labId}/{jobId}/{UserId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('lm-' + #labId)")
  public String departmentUserRoleRemove (
      @PathVariable("labId") Integer labId,
      @PathVariable("jobId") Integer jobId,
      @PathVariable("UserId") Integer userId,
      ModelMap m) {
  
    JobUser jobUser = jobUserDao.getJobUserByJobIdUserId(jobId, userId);

    // todo check job within lab
    // check user is just a job viewer

    jobUserDao.remove(jobUser);

    return "redirect:/job/detail/" + jobId + ".do";
  }

 
  	/**
	 * show job/resource data and meta information to be modified.
	 */
  
	@RequestMapping(value = "/meta/{jobId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm')") 
	public String showJobMetaForm(
		@PathVariable("jobId") Integer jobId, 
			ModelMap m) {
		Job job = jobDao.getJobByJobId(jobId);

		MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();

		Map<ResourceCategory, List<JobMeta>> resourceMap = new HashMap<ResourceCategory, List<JobMeta>>();
		Map<ResourceCategory, Map<String, List<MetaAttribute.Control.Option>>> resourceOptionsMap = new HashMap<ResourceCategory, Map<String, List<MetaAttribute.Control.Option>>>();



		for (JobResourcecategory jobResourceCategory: job.getJobResourcecategory()) {
		  ResourceCategory resourceCategory = jobResourceCategory.getResourceCategory(); 
			metaHelperWebapp.setArea(resourceCategory.getIName());
			List<JobMeta> jobResourceCategoryMetas = metaHelperWebapp.syncWithMaster(job.getJobMeta());


			resourceMap.put(resourceCategory, jobResourceCategoryMetas); 

			Map<String, List<MetaAttribute.Control.Option>> resourceOptions = new HashMap<String, List<MetaAttribute.Control.Option>>();

			if (resourceCategory != null) {
				Workflowresourcecategory workflowresourcecategory = workflowresourcecategoryDao.getWorkflowresourcecategoryByWorkflowIdResourcecategoryId(job.getWorkflow().getWorkflowId(), resourceCategory.getResourceCategoryId());

				for (WorkflowresourcecategoryMeta wrm: workflowresourcecategory.getWorkflowresourcecategoryMeta()) {
					String key = wrm.getK();

//				if (! key.matches("^.*allowableUiField\\.")) { continue; }
					key = key.replaceAll("^.*allowableUiField\\.", "");
					List<MetaAttribute.Control.Option> options=new ArrayList<MetaAttribute.Control.Option>();
					for(String el: org.springframework.util.StringUtils.tokenizeToStringArray(wrm.getV(),";")) {
						String [] pair=StringUtils.split(el,":");
						MetaAttribute.Control.Option option = new MetaAttribute.Control.Option();
						option.setValue(pair[0]);
						option.setLabel(pair[1]);
						options.add(option);
					}
					resourceOptions.put(key, options);
				}
				resourceOptionsMap.put(resourceCategory, resourceOptions);
			}
		}



		Map<Software, List<JobMeta>> softwareMap = new HashMap<Software, List<JobMeta>>();
		for (JobSoftware jobSoftware: job.getJobSoftware()) {
		  Software software = jobSoftware.getSoftware(); 
			metaHelperWebapp.setArea(software.getIName());
			List<JobMeta> jobSoftwareMetas = metaHelperWebapp.syncWithMaster(job.getJobMeta());

			softwareMap.put(software, jobSoftwareMetas); 
		}

	
		metaHelperWebapp.setArea(job.getWorkflow().getIName());
		List<JobMeta> baseMetas = metaHelperWebapp.syncWithMaster(job.getJobMeta());


		m.put("job", job); 
		m.put("baseMetas", baseMetas); 
		m.put("resourceMap", resourceMap); 
		m.put("resourceOptionsMap", resourceOptionsMap); 
		m.put("softwareMap", softwareMap); 

		m.put("metaHelper", metaHelperWebapp); 

		return "job/metaform_rw";
	}
  
	@RequestMapping(value = "/jobsAwaitingLibraryCreation/jobsAwaitingLibraryCreationList.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('fm') or hasRole('ft') or hasRole('ga')")
	public String jobsAwaitingLibraryCreation(ModelMap m) {
		  
		List<Job> jobsAwaitingLibraryCreation = jobService.getJobsAwaitingLibraryCreation();
		List<Job> jobsActive = jobService.getActiveJobs();
		    
		List<Job> jobsActiveAndWithLibraryCreatedTask = new ArrayList<Job>();
		for(Job jobActive : jobsActive){
		    for(Job jobAwaiting : jobsAwaitingLibraryCreation){
		    	if(jobActive.getJobId().intValue()==jobAwaiting.getJobId().intValue()){
		    		jobsActiveAndWithLibraryCreatedTask.add(jobActive);
		    		break;
		    	}
		    }
		}
		jobService.sortJobsByJobId(jobsActiveAndWithLibraryCreatedTask);
		m.put("jobList", jobsActiveAndWithLibraryCreatedTask);
		return "job/jobsAwaitingLibraryCreation/jobsAwaitingLibraryCreationList";	  
	}
  
	@RequestMapping(value="/{jobId}/homepage", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobHomePage(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		return "job/home/homepage";
	}
  
	@RequestMapping(value="/{jobId}/basic", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobBasicPage(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		m.addAttribute("jobStatus", jobService.getJobStatus(job));
		
		//linkedHashMap because insert order is guaranteed
		LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
		m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	

		//linkedHashMap because insert order is guaranteed
		LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
		m.addAttribute("jobApprovalsMap", jobApprovalsMap);	  
		//get the jobApprovals Comments (if any)
		HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
		m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	
		return "job/home/basic";
	}/*
	@RequestMapping(value="/{jobId}/commentFeed", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobCommentsPage(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);

		
		
		
		return "job/home/commentFeed";
	}
	*/
  
	@RequestMapping(value="/{jobId}/workflow", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobWorkflowPage(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		//linkedHashMap because insert order is guaranteed
		LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
		m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	
		return "job/home/workflow";
	}
  
	@RequestMapping(value="/{jobId}/approvals", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobApprovalsPage(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		//linkedHashMap because insert order is guaranteed
		LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
		m.addAttribute("jobApprovalsMap", jobApprovalsMap);	  
		//get the jobApprovals Comments (if any)
		HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
		m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	
		return "job/home/approvals";
	}
  
	@RequestMapping(value="/{jobId}/requests", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobRequestsPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="coverageMapOnly", required=false) String coverageMapOnly,
			  ModelMap m) throws SampleTypeException {
		
		if(coverageMapOnly==null){
			coverageMapOnly="";
		}
		m.addAttribute("coverageMapOnly", coverageMapOnly);
		
		Job job = jobService.getJobByJobId(jobId);
		if(job==null || job.getJobId()==null || job.getJobId().intValue()<=0){
			waspErrorMessage("jobComment.job.error");
			return "redirect:/dashboard.do";//not a good idea if ajax call
		}		
		m.addAttribute("job", job);
		m.addAttribute("parentArea", "job");//do not remove; it's needed for the metadata related to the software display below
		
		//request for which libraries/samples should go on which lanes
		m.addAttribute("coverageMap", jobService.getCoverageMap(job));
		m.addAttribute("totalNumberCellsRequested", job.getJobCellSelection().size());		

		if("true".equalsIgnoreCase(coverageMapOnly)){//just display coverageMap
			return "job/home/requests";
		}
		
		//samplePairingRequest
		List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
		List<Sample> controlList = new ArrayList<Sample>();
		//m.addAttribute("submittedSamplesList", submittedSamplesList);
		Map<Sample, List<Sample>> samplePairsMap = new HashMap<Sample, List<Sample>>();
		Set<SampleSource> sampleSourceSet = sampleService.getSamplePairsByJob(job);
		for(Sample submittedSample : submittedSamplesList){
			List<Sample> list = new ArrayList<Sample>();
			for(SampleSource ss : sampleSourceSet){
				Sample test = ss.getSample();
				Sample control = ss.getSourceSample();
				//System.out.println("----control = " + control.getName() + " AND test = " + test.getName());
				if(submittedSample == control){
					list.add(test);
				}
			}
			if(!list.isEmpty()){
				samplePairsMap.put(submittedSample, list);
				controlList.add(submittedSample);
			}
		}
		m.addAttribute("samplePairsMap", samplePairsMap);
		m.addAttribute("controlList", controlList);
		String temp = job.getWorkflow().getIName() + ".control.label";
		String controlLabel = messageService.getMessage(job.getWorkflow().getIName() + ".control.label");
		if(controlLabel.equalsIgnoreCase(temp)){controlLabel = "Control";}
		temp = job.getWorkflow().getIName() + ".test.label";
		String testLabel = messageService.getMessage(job.getWorkflow().getIName() + ".test.label");
		if(testLabel.equalsIgnoreCase(temp)){testLabel = "Test";}
		m.addAttribute("controlLabel", controlLabel);
		m.addAttribute("testLabel", testLabel);
		
		//software request
		List<Software> softwareList = jobService.getSoftwareForJob(job);
		m.addAttribute("softwareList", softwareList);
		Map<Software, List<JobMeta>> softwareAndSyncdMetaMap = new HashMap<Software, List<JobMeta>>();
		MetaHelperWebapp mhwa = getMetaHelperWebapp();
		List<JobMeta> jobMetaList = job.getJobMeta();
		for(Software sw : softwareList){
			mhwa.setArea(sw.getIName());
			List<JobMeta> softwareMetaList = mhwa.syncWithMaster(jobMetaList);
			softwareAndSyncdMetaMap.put(sw, softwareMetaList);
		}	
		m.addAttribute("softwareAndSyncdMetaMap", softwareAndSyncdMetaMap);
		
		return "job/home/requests";
	}
  
	@RequestMapping(value="/{jobId}/comments", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobCommentsPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage,
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		//get the user-submitted job comment (if any); should be zero or one
		List<MetaMessage> userSubmittedJobCommentsList = jobService.getUserSubmittedJobComment(jobId);
		for (MetaMessage metaMessage: userSubmittedJobCommentsList){
			metaMessage.setValue(StringUtils.replace(metaMessage.getValue(), "\r\n" ,"<br />"));//carriage return was inserted at time of INSERT to deal with line-break. Change it to <br /> for proper html display (using c:out escapeXml=false). Note that other html was escpaped at INSERT stage (see line 180 below) 
		}
		m.addAttribute("userSubmittedJobCommentsList", userSubmittedJobCommentsList);
		
		//get the facility-generated job comments (if any)
		List<MetaMessage> facilityJobCommentsList = jobService.getAllFacilityJobComments(jobId);
		for (MetaMessage metaMessage: facilityJobCommentsList){
			metaMessage.setValue(StringUtils.replace(metaMessage.getValue(), "\r\n" ,"<br />"));
		}
		m.addAttribute("facilityJobCommentsList", facilityJobCommentsList);
		
		boolean permissionToAddEditComment = false;
		try{
			permissionToAddEditComment = authenticationService.hasPermission("hasRole('su') or hasRole('fm') or hasRole('ft')");
		}catch(Exception e){
			//waspErrorMessage("jobComment.jobCommentAuth.error");
			//return "redirect:/dashboard.do";
		}
		//override this constraint:
		permissionToAddEditComment=true;
		m.addAttribute("permissionToAddEditComment", permissionToAddEditComment);
		
		if(errorMessage==null){
			errorMessage="";
		}
		m.addAttribute("errorMessage", errorMessage);

		return "job/home/comments";
	}
  
	@RequestMapping(value="/{jobId}/comments", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobCommentsPostPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam("comment") String comment,
			  ModelMap m) throws SampleTypeException {
		
		String errorMessage = "";
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
			errorMessage = messageService.getMessage("jobComment.job.error");
		}
		m.addAttribute("job", job);
		
		String trimmedComment = comment==null?null:StringEscapeUtils.escapeXml(comment.trim());//any standard html/xml [Supports only the five basic XML entities (gt, lt, quot, amp, apos)] will be converted to characters like &gt; //http://commons.apache.org/lang/api-3.1/org/apache/commons/lang3/StringEscapeUtils.html#escapeXml%28java.lang.String%29
		if(trimmedComment==null||trimmedComment.length()==0){
			errorMessage = messageService.getMessage("jobComment.jobCommentEmpty.error");
		}

		if("".equals(errorMessage)){
			try{
				if(authenticationService.hasPermission("hasRole('su') or hasRole('fm') or hasRole('ft') or hasRole('da-*')")){
					jobService.setFacilityJobComment(jobId, trimmedComment);
				}
				else{
					jobService.setUserSubmittedJobComment(jobId, trimmedComment);
				}
			}catch(Exception e){
				logger.warn(e.getMessage());
				errorMessage = messageService.getMessage("jobComment.jobCommentCreate.error");
			}
		}
		return "redirect:/job/"+jobId+"/comments.do?errorMessage="+errorMessage;
	}
  
	@RequestMapping(value="/{jobId}/viewerManager", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobViewerManagerPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage,
			  @RequestParam(value="successMessage", required=false) String successMessage,
			  @RequestParam(value="newViewerEmailAddress", required=false) String newViewerEmailAddress,
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		List<JobUser> jobUserList = job.getJobUser();
		List<User> additionalJobViewers = new ArrayList<User>();
		for(JobUser jobUser : jobUserList){
			if(jobUser.getUser().getId().intValue() != job.getUserId().intValue() && jobUser.getUser().getId().intValue() != job.getLab().getPrimaryUserId().intValue()){
				additionalJobViewers.add(jobUser.getUser());
			}
		}
		class SubmitterLastNameFirstNameComparator implements Comparator<User> {
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getLastName().concat(arg0.getFirstName()).compareToIgnoreCase(arg1.getLastName().concat(arg1.getFirstName()));
			}
		}
		Collections.sort(additionalJobViewers, new SubmitterLastNameFirstNameComparator());
		m.addAttribute("additionalJobViewers", additionalJobViewers);
  
		User currentWebViewer = authenticationService.getAuthenticatedUser();
		Boolean currentWebViewerIsSuperuserSubmitterOrPI = false;
		if(authenticationService.isSuperUser() || currentWebViewer.getId().intValue() == job.getUserId().intValue() || currentWebViewer.getId().intValue() == job.getLab().getPrimaryUserId().intValue()){
			currentWebViewerIsSuperuserSubmitterOrPI = true; //superuser, job's submitter, job's PI
		}
		m.addAttribute("currentWebViewerIsSuperuserSubmitterOrPI", currentWebViewerIsSuperuserSubmitterOrPI);
		m.addAttribute("currentWebViewer", currentWebViewer);
		
		if(errorMessage==null){
			errorMessage="";
		}
		m.addAttribute("errorMessage", errorMessage);
		
		if(successMessage==null){
			successMessage="";
		}
		m.addAttribute("successMessage", successMessage);

		if(newViewerEmailAddress==null){
			newViewerEmailAddress="";
		}
		m.addAttribute("newViewerEmailAddress", newViewerEmailAddress);
		
		return "job/home/viewerManager";
	}
 
	@RequestMapping(value="/{jobId}/viewerManager", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobViewerManagerPostPage(@PathVariable("jobId") Integer jobId, @RequestParam("newViewerEmailAddress") String newViewerEmailAddress, ModelMap m) throws SampleTypeException {
		
		String errorMessage = "";
		String successMessage = "";
		if(newViewerEmailAddress==null){
			newViewerEmailAddress="";
		}

		if("".equals(newViewerEmailAddress)){
			errorMessage = "Update Failed: Please provide an email address";
			return "redirect:/job/"+jobId+"/viewerManager.do?errorMessage="+errorMessage+"&successMessage="+successMessage+"&newViewerEmailAddress="+newViewerEmailAddress;
		}

		try{
			   jobService.addJobViewer(jobId, newViewerEmailAddress);//performs checks to see if this is a legal action. 
			   successMessage="Update Successful: New job viewer added.";
		}
		catch(Exception e){		    
		  logger.warn(e.getMessage());
		  errorMessage = messageService.getMessage(e.getMessage());
		}
		
		return "redirect:/job/"+jobId+"/viewerManager.do?errorMessage="+errorMessage+"&successMessage="+successMessage+"&newViewerEmailAddress="+newViewerEmailAddress;
	}
	
	@RequestMapping(value="/{jobId}/user/{userId}/removeJobViewer", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobViewerManagerRemoveUserPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("userId") Integer userId, ModelMap m) throws SampleTypeException {

		String errorMessage = "";
		String successMessage = "";
		try{
			jobService.removeJobViewer(jobId, userId);//performs checks to see if this is a legal action. 
			
			//the next few lines make for problems if this is an ajax call, so do not do the doReauth().It's only important if the user is removing him/herself, so it is fine.
			//User me = authenticationService.getAuthenticatedUser();
			//if (me.getId().intValue() == userId.intValue()) {
			//	doReauth();//do this if the person performing the action is the person being removed from viewing this job (note: it cannot be the submitter or the pi)
			//}			
			successMessage="Database sucessfully updated: User removed";
		}
		catch(Exception e){		    
		  logger.warn(e.getMessage());
		  errorMessage = messageService.getMessage(e.getMessage());
		}
		return "redirect:/job/"+jobId+"/viewerManager.do?errorMessage="+errorMessage+"&successMessage="+successMessage;
	}

	@RequestMapping(value="/{jobId}/addLibrariesToCell", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobAddLibrariesToCellPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="addLibrariesToPlatformUnitErrorMessage", required=false) String addLibrariesToPlatformUnitErrorMessage,
			  @RequestParam(value="addLibrariesToPlatformUnitSuccessMessage", required=false) String addLibrariesToPlatformUnitSuccessMessage,
			  ModelMap m) throws SampleTypeException {						
		
		if(addLibrariesToPlatformUnitErrorMessage==null){
			addLibrariesToPlatformUnitErrorMessage="";
		}
		m.addAttribute("addLibrariesToPlatformUnitErrorMessage", addLibrariesToPlatformUnitErrorMessage);
		
		if(addLibrariesToPlatformUnitSuccessMessage==null){
			addLibrariesToPlatformUnitSuccessMessage="";
		}
		m.addAttribute("addLibrariesToPlatformUnitSuccessMessage", addLibrariesToPlatformUnitSuccessMessage);
			
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		getSampleLibraryRunData(jobId, m);
		
		return "job/home/addLibrariesToCell";
	}
	
	@RequestMapping(value="/{jobId}/addLibrariesToCell", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobAddLibrariesToCellPostPage(@PathVariable("jobId") Integer jobId,
			  @RequestParam("cellId") Integer cellId, 
			  @RequestParam("libConcInCellPicoM") List<String> libConcInCellPicoMAsStringList,
			  @RequestParam("libraryId") List<Integer> libraryIdList,
			  ModelMap m) throws SampleTypeException {						
	/*
		System.out.println("---cellId="+cellId);
		for(String s : libConcInCellPicoMAsStringList){
			System.out.println("------"+s);
		}
		for(Integer integer : libraryIdList){
			System.out.println("--------"+integer);
		}
	*/
		String addLibrariesToPlatformUnitErrorMessage = "";
		String addLibrariesToPlatformUnitSuccessMessage = "";
		
		if(libConcInCellPicoMAsStringList.size()!=libraryIdList.size()){
			addLibrariesToPlatformUnitErrorMessage="Update Failed: Unexpected parameter mismatch";
		}
		
		if( "".equals(addLibrariesToPlatformUnitErrorMessage) ){
			// ensure platform unit is available
			try{
				Sample platformUnit = sampleService.getPlatformUnitForCell(sampleService.getSampleById(cellId));
				if( ! sampleService.getAvailableAndCompatiblePlatformUnits(jobService.getJobByJobId(jobId)).contains(platformUnit) ){
					addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.flowcellStateError.error");
				}
			}catch(Exception e){
				addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.flowcellNotFoundNotUnique.error");
			}
		}		
		int numLibrariesSuccessfullyAdded = 0;
		int numLibrariesFailedAddedDueToSampleTypeOrAdaptorError = 0;
		int numLibrariesFailedAddedDueToMultiplexError = 0;
		int numLibrariesFailedAddedDueToParseFloatError = 0;
		List<Sample> librariesThatFailedDueToMultiplexErrors = new ArrayList<Sample>();
		List<Sample> librariesThatFailedDueToParseFloatErrors = new ArrayList<Sample>();
		
		if( "".equals(addLibrariesToPlatformUnitErrorMessage) ){
			for(int i = 0; i < libraryIdList.size(); i++){
				
				if("".equals(libConcInCellPicoMAsStringList.get(i).trim())){
					  continue;
				}
				
				try{
					  Float libConcInCellPicoMFloat = new Float(Float.parseFloat(libConcInCellPicoMAsStringList.get(i).trim()));
					  sampleService.addLibraryToCell(sampleService.getSampleById(cellId), sampleService.getSampleById(libraryIdList.get(i)), libConcInCellPicoMFloat, jobService.getJobByJobId(jobId));
					  //addLibrariesToPlatformUnitSuccessMessage = messageService.getMessage("platformunit.libAdded.success");
					  numLibrariesSuccessfullyAdded++;
				} catch(SampleTypeException ste){
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.sampleType.error");
					logger.warn(ste.getMessage()); // print more detailed error to warn logs
					numLibrariesFailedAddedDueToSampleTypeOrAdaptorError++;
				} catch(SampleMultiplexException sme){
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.multiplex.error");//index already on cell
					logger.warn(sme.getMessage()); // print more detailed error to debug logs
					numLibrariesFailedAddedDueToMultiplexError++;
					librariesThatFailedDueToMultiplexErrors.add(sampleService.getSampleById(libraryIdList.get(i)));
				} catch(SampleException se){
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorNotFound.error");
					logger.warn(se.getMessage()); // print more detailed error to warn logs
					numLibrariesFailedAddedDueToSampleTypeOrAdaptorError++;
				} catch(MetadataException me){
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorBarcodeNotFound.error");
					logger.warn(me.getMessage()); // print more detailed error to warn logs
					numLibrariesFailedAddedDueToSampleTypeOrAdaptorError++;
				} catch(NumberFormatException e){//libConcInCellPicoMAsStringList.get(i) didn't parse to float
					logger.warn(e.getMessage());
					numLibrariesFailedAddedDueToParseFloatError++;
					librariesThatFailedDueToParseFloatErrors.add(sampleService.getSampleById(libraryIdList.get(i)));						
				}
			}
		}
		
		if(numLibrariesFailedAddedDueToMultiplexError>0){
			if(numLibrariesFailedAddedDueToMultiplexError==1){
				addLibrariesToPlatformUnitErrorMessage += "One Library Not Added Due To Indexing Conflicts:";
			}
			if(numLibrariesFailedAddedDueToMultiplexError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToMultiplexError + " Libraries Not Added Due To Indexing Conflicts:";
			}
			for(Sample s : librariesThatFailedDueToMultiplexErrors){
				addLibrariesToPlatformUnitErrorMessage += "<br />"+s.getName();
			}
		}
		if(numLibrariesFailedAddedDueToSampleTypeOrAdaptorError >0){
			if(numLibrariesFailedAddedDueToSampleTypeOrAdaptorError==1){
				addLibrariesToPlatformUnitErrorMessage += "One Library Not Added Due To Unexpected SampleType or Adaptor Errors";
			}
			if(numLibrariesFailedAddedDueToSampleTypeOrAdaptorError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToSampleTypeOrAdaptorError + " Libraries Not Added Due To Unexpected SampleType or Adaptor Errors";
			}
		}
		if(numLibrariesFailedAddedDueToParseFloatError >0){
			if(numLibrariesFailedAddedDueToParseFloatError==1){
				addLibrariesToPlatformUnitErrorMessage += "One Library Not Added Due To Concentration Not Expressed As Number";
			}
			if(numLibrariesFailedAddedDueToParseFloatError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToParseFloatError + " Libraries Not Added Due To Concentration Not Expressed As Number";
			}
			for(Sample s : librariesThatFailedDueToParseFloatErrors){
				addLibrariesToPlatformUnitErrorMessage += "<br />"+s.getName();
			}
		}
		
		if( numLibrariesSuccessfullyAdded == 1 ){
			addLibrariesToPlatformUnitSuccessMessage = "One Library Successfully Added";
		}
		else if( numLibrariesSuccessfullyAdded > 1 ){
			addLibrariesToPlatformUnitSuccessMessage = numLibrariesSuccessfullyAdded + " Libraries Successfully Added";
		}
		return "redirect:/job/"+jobId+"/addLibrariesToCell.do?addLibrariesToPlatformUnitErrorMessage="+addLibrariesToPlatformUnitErrorMessage+"&addLibrariesToPlatformUnitSuccessMessage="+addLibrariesToPlatformUnitSuccessMessage;
	}
	
	
	@RequestMapping(value="/{jobId}/samples", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobSamplesPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="addLibraryToPlatformUnitErrorMessage", required=false) String addLibraryToPlatformUnitErrorMessage,
			  @RequestParam(value="addLibraryToPlatformUnitSuccessMessage", required=false) String addLibraryToPlatformUnitSuccessMessage,
			  @RequestParam(value="libraryIdAssociatedWithMessage", required=false) Integer libraryIdAssociatedWithMessage,
			  @RequestParam(value="removeLibraryFromCellErrorMessage", required=false) String removeLibraryFromCellErrorMessage,
			  @RequestParam(value="removeLibraryFromCellSuccessMessage", required=false) String removeLibraryFromCellSuccessMessage,
			  @RequestParam(value="updateConcentrationToCellLibraryErrorMessage", required=false) String updateConcentrationToCellLibraryErrorMessage,
			  @RequestParam(value="updateConcentrationToCellLibrarySuccessMessage", required=false) String updateConcentrationToCellLibrarySuccessMessage,
			  ModelMap m) throws SampleTypeException {
						
		if(updateConcentrationToCellLibraryErrorMessage==null){
			updateConcentrationToCellLibraryErrorMessage="";
		}
		m.addAttribute("updateConcentrationToCellLibraryErrorMessage", updateConcentrationToCellLibraryErrorMessage);
		
		if(updateConcentrationToCellLibrarySuccessMessage==null){
			updateConcentrationToCellLibrarySuccessMessage="";
		}
		m.addAttribute("updateConcentrationToCellLibrarySuccessMessage", updateConcentrationToCellLibrarySuccessMessage);

		
		if(addLibraryToPlatformUnitErrorMessage==null){
			addLibraryToPlatformUnitErrorMessage="";
		}
		m.addAttribute("addLibraryToPlatformUnitErrorMessage", addLibraryToPlatformUnitErrorMessage);
		
		if(addLibraryToPlatformUnitSuccessMessage==null){
			addLibraryToPlatformUnitSuccessMessage="";
		}
		m.addAttribute("addLibraryToPlatformUnitSuccessMessage", addLibraryToPlatformUnitSuccessMessage);
		
		if(removeLibraryFromCellErrorMessage==null){
			removeLibraryFromCellErrorMessage="";
		}
		m.addAttribute("removeLibraryFromCellErrorMessage", removeLibraryFromCellErrorMessage);

		if(removeLibraryFromCellSuccessMessage==null){
			removeLibraryFromCellSuccessMessage="";
		}
		m.addAttribute("removeLibraryFromCellSuccessMessage", removeLibraryFromCellSuccessMessage);

		if(libraryIdAssociatedWithMessage==null){
			libraryIdAssociatedWithMessage = new Integer(-1);
		}
		m.addAttribute("libraryIdAssociatedWithMessage", libraryIdAssociatedWithMessage);
		
		getSampleLibraryRunData(jobId, m);
		
		return "job/home/samples";
	}

	private void getSampleLibraryRunData(Integer jobId, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		  List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		  List<Sample> allJobLibraries = new ArrayList<Sample>();  //could have gotten this from allJobLibraries = jobService.getLibraries(job);
		  List<Sample> submittedMacromoleculeList = new ArrayList<Sample>();
		  List<Sample> submittedLibraryList = new ArrayList<Sample>();
		  List<Sample> facilityLibraryList = new ArrayList<Sample>();
		  
		  List<Sample> submittedObjectList = new ArrayList<Sample>();//could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
		  
		  Map<Sample, List<Sample>> submittedMacromoleculeFacilityLibraryListMap = new HashMap<Sample, List<Sample>>();
		  Map<Sample, List<Sample>> submittedLibrarySubmittedLibraryListMap = new HashMap<Sample, List<Sample>>();
		  Map<Sample, String> submittedObjectOrganismMap = new HashMap<Sample, String>();
		  Map<Sample, Adaptorset> libraryAdaptorsetMap = new HashMap<Sample, Adaptorset>();
		  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();

		  Map<Sample, String> receivedStatusMap = new HashMap<Sample, String>();
		  Map<Sample, String> qcStatusMap = new HashMap<Sample, String>();
		  Map<Sample, List<MetaMessage>> qcStatusCommentsMap = new HashMap<Sample, List<MetaMessage>>();
		  Map<Sample, Boolean> createLibraryStatusMap = new HashMap<Sample, Boolean>();
		  Map<Sample, Boolean> assignLibraryToPlatformUnitStatusMap = new HashMap<Sample, Boolean>();

		  for(Sample s : allJobSamples){
			  if(s.getParent()==null){
				  if(s.getSampleType().getIName().toLowerCase().contains("library")){
					  submittedLibraryList.add(s);
				  }
				  else{
					  submittedMacromoleculeList.add(s);
				  }
			  }
			  else{
				  facilityLibraryList.add(s);
			  }
			  
			  //also for each job's sample, get the qcStatus; note the call changes if library (getLibraryQCStatus) or macromolecule (getSampleQCStatus)
			  if(s.getSampleType().getIName().toLowerCase().contains("library")){
				  qcStatusMap.put(s, sampleService.convertSampleQCStatusForWeb(sampleService.getLibraryQCStatus(s)));
				  qcStatusCommentsMap.put(s, sampleService.getSampleQCComments(s.getId()));
			  }
			  else{
				  qcStatusMap.put(s, sampleService.convertSampleQCStatusForWeb(sampleService.getSampleQCStatus(s)));
				  qcStatusCommentsMap.put(s, sampleService.getSampleQCComments(s.getId()));
			  }
		  }
		  
		  //consolidate job's libraries
		  allJobLibraries.addAll(submittedLibraryList);
		  allJobLibraries.addAll(facilityLibraryList);
		  
		  //consolidate job's submitted objects: submitted macromoleucles and submitted libraries
		  //could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
		  submittedObjectList.addAll(submittedMacromoleculeList);
		  submittedObjectList.addAll(submittedLibraryList);
		  
		  //for each submittedMacromolecule, get list of it's facility-generated libraries; also determine if this sampleMacromolecule should have a library made from it
		  for(Sample macromolecule : submittedMacromoleculeList){
			  submittedMacromoleculeFacilityLibraryListMap.put(macromolecule, macromolecule.getChildren());//could also have used sampleService.getFacilityGeneratedLibraries(macromolecule)
			  boolean isSampleWaitingForLibraryCreation = sampleService.isSampleAwaitingLibraryCreation(macromolecule);
			  logger.debug("setting sample " + macromolecule.getId() + " (" + macromolecule.getName() + ") is waiting for library creation = "+ isSampleWaitingForLibraryCreation);
			  createLibraryStatusMap.put(macromolecule, isSampleWaitingForLibraryCreation);
		  }
		  
		  //for each submittedLibrary, get list of it's libraries (done for consistency, as this is actually a list of one, the library itself)
		  for(Sample userSubmittedLibrary : submittedLibraryList){//do this just to get the userSubmitted Library in a list
			  List<Sample> tempUserSubmittedLibraryList = new ArrayList<Sample>();
			  tempUserSubmittedLibraryList.add(userSubmittedLibrary);
			  submittedLibrarySubmittedLibraryListMap.put(userSubmittedLibrary, tempUserSubmittedLibraryList);
		  }
		  
		  //for each submittedMacromolecule or submittedLibrary, get species and get receviedStatus
		  for(Sample submittedObject : submittedObjectList){
			  submittedObjectOrganismMap.put(submittedObject, sampleService.getNameOfOrganism(submittedObject, "???"));
			  receivedStatusMap.put(submittedObject, sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(submittedObject)));
		  }
		  
		  //for each job's library, get its adaptor info and determine whether the library should be assigned to a platformUnit
		  int numberOfLibrariesAwaitingPlatformUnitPlacement = 0;
		  for(Sample library : allJobLibraries){
			  
			  boolean b = sampleService.isLibraryAwaitingPlatformUnitPlacement(library);
			  if(b==true){
				  numberOfLibrariesAwaitingPlatformUnitPlacement++;
			  }
			  assignLibraryToPlatformUnitStatusMap.put(library, b);

			  Adaptor adaptor;
			  try{ 
				  adaptor = adaptorService.getAdaptor(library);
				  libraryAdaptorMap.put(library, adaptor);
				  libraryAdaptorsetMap.put(library, adaptor.getAdaptorset()); 
			  }catch(Exception e){  }		  
		  }
		  
		  //???want it?? Set<SampleSource> cellLibrariesForJob = sampleService.getCellLibrariesForJob(job);
		  Map<Sample, List<Sample>> libraryCellListMap = new HashMap<Sample, List<Sample>>();
		  Map<Sample, Integer> cellIndexMap = new HashMap<Sample, Integer>();
		  Map<Sample, Sample> cellPUMap = new HashMap<Sample, Sample>();
		  Map<Sample, Run> cellRunMap = new HashMap<Sample, Run>();	 

		  Map<Sample, Map<Sample, Float>> cellLibraryPMLoadedMap = new HashMap<Sample, Map<Sample, Float>>();
		  
		  for(Sample library : allJobLibraries){
			  List<Sample>  cellsForLibrary = sampleService.getCellsForLibrary(library, job);
			  libraryCellListMap.put(library, cellsForLibrary);
			  for(Sample cell : cellsForLibrary){			  
				  try{
					  cellIndexMap.put(cell, sampleService.getCellIndex(cell));
					  Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
					  cellPUMap.put(cell, platformUnit);
					  
					  Map<Sample, Float> libraryPMLoadedMap = new HashMap<Sample, Float>();
					  Float pMLoaded = sampleService.getConcentrationOfLibraryAddedToCell(cell, library);
					  libraryPMLoadedMap.put(library, pMLoaded);
					  cellLibraryPMLoadedMap.put(cell, libraryPMLoadedMap);
					  
					  /////******MUST USE THIS FOR REAL List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST rather than a singleton?
					  List<Run> runList = runService.getRunsForPlatformUnit(platformUnit);
					  if(!runList.isEmpty()){
						  Run run = runList.get(0);
						  cellRunMap.put(cell, run);
						  
					  }
				  }catch(Exception e){}
			  }
		  }
		  
		  for(Sample library : allJobLibraries){
			  List<Sample> cellList = libraryCellListMap.get(library);
			  for(Sample cell : cellList){
				  Map<Sample,Float> libraryPMLoadedMap = cellLibraryPMLoadedMap.get(cell);
				  Float pmLoaded = libraryPMLoadedMap.get(library);
				  System.out.println("----------Library : cell : pmLoaded = " + library.getName() + " : " + cell.getName() + " : " + pmLoaded);
			  }
		  }

		  Map<Sample, Integer> submittedObjectLibraryRowspan = new HashMap<Sample, Integer>();//number of libraries for each submitted Object (be it a submitted macromolecule or a submitted library)
		  Map<Sample, Integer> submittedObjectCellRowspan = new HashMap<Sample, Integer>();//number of runs (zero, one, many) for each library

		  //calculate the rowspans needed for the web, as the table display is rather complex. 
		  //this is very very hard to do at the web, as there are multiple dependencies. easier to perform here
		  for(Sample submittedObject : submittedObjectList){
			  
			  int numLibraries = 0;
			  int numCells = 0;
			  
			  //calculate numLibraries
			  List<Sample> facilityLibraryList2 = submittedMacromoleculeFacilityLibraryListMap.get(submittedObject);
			  List<Sample> submittedLibraryList2 = submittedLibrarySubmittedLibraryListMap.get(submittedObject);
			  numLibraries = (facilityLibraryList2==null?0:facilityLibraryList2.size()) + (submittedLibraryList2==null?0:submittedLibraryList2.size());
			  
			  //put numLibraries into its map for eventual use in web
			  if(numLibraries==0){
				  submittedObjectLibraryRowspan.put(submittedObject, 1);
			  }
			  else{
				  submittedObjectLibraryRowspan.put(submittedObject, numLibraries);
			  }
			  
			  //calculate numCells
			  if(facilityLibraryList2!=null){
				  for(Sample library : facilityLibraryList2){
					  numCells += libraryCellListMap.get(library)==null?0:libraryCellListMap.get(library).size();
				  }
			  }
			  if(submittedLibraryList2!=null){
				  for(Sample library : submittedLibraryList2){
					  numCells += libraryCellListMap.get(library)==null?0:libraryCellListMap.get(library).size();
				  }
			  }
			  
			  //put numCells into its map for eventual use in web
			  if(numCells==0){
				  submittedObjectCellRowspan.put(submittedObject, 1);
			  }
			  else{
				  submittedObjectCellRowspan.put(submittedObject, numCells);
			  }
		  }
		  

		  
		  m.addAttribute("job",job);
		  //not actually used by webpage   m.addAttribute("submittedLibraryList", submittedLibraryList);
		  //not actually used on webpage m.addAttribute("facilityLibraryList", facilityLibraryList);
		  m.addAttribute("submittedObjectList", submittedObjectList);	//main object list  
		  m.addAttribute("submittedMacromoleculeList", submittedMacromoleculeList);//needed to distinguish submittedMacromoleucle from submitted Library

		  m.addAttribute("submittedMacromoleculeFacilityLibraryListMap", submittedMacromoleculeFacilityLibraryListMap);
		  m.addAttribute("submittedLibrarySubmittedLibraryListMap", submittedLibrarySubmittedLibraryListMap);
		  m.addAttribute("submittedObjectOrganismMap", submittedObjectOrganismMap);
		  m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
		  m.addAttribute("libraryAdaptorsetMap", libraryAdaptorsetMap);
		  m.addAttribute("assignLibraryToPlatformUnitStatusMap", assignLibraryToPlatformUnitStatusMap);
		  m.addAttribute("numberOfLibrariesAwaitingPlatformUnitPlacement", numberOfLibrariesAwaitingPlatformUnitPlacement);
		  
		  m.addAttribute("libraryCellListMap", libraryCellListMap);
		  m.addAttribute("cellIndexMap", cellIndexMap);
		  m.addAttribute("cellPUMap", cellPUMap);//currently not used on web
		  m.addAttribute("cellRunMap", cellRunMap);
		  m.addAttribute("cellLibraryPMLoadedMap", cellLibraryPMLoadedMap);
		  
		  m.addAttribute("submittedObjectCellRowspan", submittedObjectCellRowspan);
		  m.addAttribute("submittedObjectLibraryRowspan", submittedObjectLibraryRowspan);  
		 
		  //turns out I need this
		  m.addAttribute("submittedLibraryList", submittedLibraryList);
		
		  m.addAttribute("receivedStatusMap", receivedStatusMap);
		  m.addAttribute("qcStatusMap", qcStatusMap);
		  m.addAttribute("qcStatusCommentsMap", qcStatusCommentsMap);
		  m.addAttribute("createLibraryStatusMap", createLibraryStatusMap);
		  
		  
		  //the dropdown box
		  List<Sample> availableAndCompatiblePlatformUnitListOnForm = sampleService.getAvailableAndCompatiblePlatformUnits(job);//available flowCells that are compatible with this job
		  m.addAttribute("availableAndCompatiblePlatformUnitListOnForm", availableAndCompatiblePlatformUnitListOnForm);
		  Map<Sample, List<Sample>> platformUnitCellListMapOnForm = new HashMap<Sample, List<Sample>>();
		  Map<Sample, List<Sample>> cellControlLibraryListMapOnForm = new HashMap<Sample, List<Sample>>();
		  Map<Sample, List<Sample>> cellLibraryWithoutControlListMapOnForm = new HashMap<Sample, List<Sample>>();
		  Map<Sample, Adaptorset> libraryAdaptorsetMapOnForm = new HashMap<Sample, Adaptorset>();
		  Map<Sample, Adaptor> libraryAdaptorMapOnForm = new HashMap<Sample, Adaptor>();

		  //fill up drop-down box
		  for(Sample platformUnit : availableAndCompatiblePlatformUnitListOnForm){
			  //System.out.println("PU: " + platformUnit.getName());
			  Map<Integer, Sample> indexedCellsOnPlatformUnitMap = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);
			  
			  List<Sample> cellList = new ArrayList<Sample>();
			  int numberOfIndexedCellsOnPlatformUnit = indexedCellsOnPlatformUnitMap.size();
			  ///System.out.println("---numberOfIndexedCellsOnPlatformUnit: " + numberOfIndexedCellsOnPlatformUnit);
			  for(int i = 1; i <= numberOfIndexedCellsOnPlatformUnit; i++){
				  ///System.out.println("------"+i);
				  Sample cell = indexedCellsOnPlatformUnitMap.get(new Integer(i));
				  ///System.out.println("----------"+cell.getName());
				  cellList.add(cell);
				  List<Sample> controlLibrariesOnCellList = sampleService.getControlLibrariesOnCell(cell);
				  cellControlLibraryListMapOnForm.put(cell, controlLibrariesOnCellList);

				  //should order next list by index???
				  List<Sample> librariesWithoutControlsOnCellList = sampleService.getLibrariesOnCellWithoutControls(cell);
				  cellLibraryWithoutControlListMapOnForm.put(cell, librariesWithoutControlsOnCellList);
				  //need to order by index.
				  
				  List<Sample> tempLibraryList = new ArrayList<Sample>();
				  tempLibraryList.addAll(controlLibrariesOnCellList);
				  tempLibraryList.addAll(librariesWithoutControlsOnCellList);
				  
				  //for each library on this form, get its adaptor info
				  for(Sample library : tempLibraryList){
					  Adaptor adaptor;
					  try{ 
						  adaptor = adaptorService.getAdaptor(library);
						  libraryAdaptorMapOnForm.put(library, adaptor);
						  libraryAdaptorsetMapOnForm.put(library, adaptor.getAdaptorset()); 
					  }catch(Exception e){  }		  
				  }
			  }
			  platformUnitCellListMapOnForm.put(platformUnit, cellList);
		  }
		  m.addAttribute("platformUnitCellListMapOnForm", platformUnitCellListMapOnForm);		  
		  m.addAttribute("cellControlLibraryListMapOnForm", cellControlLibraryListMapOnForm);
		  m.addAttribute("cellLibraryWithoutControlListMapOnForm", cellLibraryWithoutControlListMapOnForm);
		  m.addAttribute("libraryAdaptorMapOnForm", libraryAdaptorMapOnForm);
		  m.addAttribute("libraryAdaptorsetMapOnForm", libraryAdaptorsetMapOnForm);
		  
		
	}
	@RequestMapping(value="/{jobId}/cell/{cellId}/library/{libraryId}/updateConcentration", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobUpdateConcentrationToCellPostPage(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("libraryId") Integer libraryId, 
			  @PathVariable("cellId") Integer cellId, 
			  @RequestParam("newConcentrationInPM") String newConcentrationInPM, ModelMap m) throws SampleTypeException {

		String updateConcentrationToCellLibraryErrorMessage = "";
		String updateConcentrationToCellLibrarySuccessMessage = "";
		
		try {
			sampleService.setLibraryOnCellConcentration(sampleService.getCellLibrary(sampleService.getSampleById(cellId), sampleService.getSampleById(libraryId)), Float.parseFloat(newConcentrationInPM.trim()));
			updateConcentrationToCellLibrarySuccessMessage="Concentration successfully updated";
		} catch (Exception e) {
			logger.warn("Problem occurred updating library concentration on cell: " + e.getLocalizedMessage());
			updateConcentrationToCellLibraryErrorMessage ="Unexpected error. Update failed.";
		}
		
		return "redirect:/job/"+jobId+"/samples.do?updateConcentrationToCellLibraryErrorMessage="+updateConcentrationToCellLibraryErrorMessage+"&libraryIdAssociatedWithMessage="+libraryId + "&updateConcentrationToCellLibrarySuccessMessage=" + updateConcentrationToCellLibrarySuccessMessage;
	}
	
	@RequestMapping(value="/{jobId}/cell/{cellId}/library/{libraryId}/removeLibrary", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobRemoveLibraryFromCellPage(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("cellId") Integer cellId, 
			  @PathVariable("libraryId") Integer libraryId){ 

		String removeLibraryFromCellErrorMessage = "";
		String removeLibraryFromCellSuccessMessage = "";
		
		try{
			sampleService.removeLibraryFromCellOfPlatformUnit(sampleService.getSampleById(cellId), sampleService.getSampleById(libraryId));
			removeLibraryFromCellSuccessMessage="Library successfully removed from specified cell";
		}
		catch(Exception e){
			logger.warn(e.getMessage() + ": removeLibraryFromCellOfPlatformUnit(cell, library) threw exception");
			removeLibraryFromCellErrorMessage = "Unexpected error. Unable to remove library.";
		}
			
		return "redirect:/job/"+jobId+"/samples.do?removeLibraryFromCellErrorMessage="+removeLibraryFromCellErrorMessage+"&libraryIdAssociatedWithMessage="+libraryId + "&removeLibraryFromCellSuccessMessage=" + removeLibraryFromCellSuccessMessage;
	}
	
	@RequestMapping(value="/{jobId}/library/{libraryId}/addToCell", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobAddLibraryToCellPostPage(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("libraryId") Integer libraryId, 
			  @RequestParam("cellId") Integer cellId, 
			  @RequestParam("libConcInCellPicoM") String libConcInCellPicoM, ModelMap m) throws SampleTypeException {

		String addLibraryToPlatformUnitErrorMessage = "";
		String addLibraryToPlatformUnitSuccessMessage = "";
		
		Job job = jobService.getJobByJobId(jobId);		
		Sample cell = sampleService.getSampleById(cellId);
		Sample library = sampleService.getSampleById(libraryId);
		List<Sample> jobLibraries = jobService.getLibraries(job);
		
		Float libConcInCellPicoMFloat = 0.0f;

		if (jobId == null || jobId == 0 || job == null || job.getId() == null) {
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.jobIdNotFound.error"); 
		}
		else if(cellId == null || cellId == 0){//user selected a flowcell from dropdown box (parameter cellSampleId == 0); we should actually prevent this with javascript
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.cellIsFlowCell.error");
		}
		else if (cell == null || cell.getId() == null) {
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.cellIdNotFound.error"); 
		}
		else if (libraryId == null || libraryId == 0 || library== null || library.getId() == null) {
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.libraryIdNotFound.error");
		}
		else if ( ! sampleService.isLibrary(library)) {
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.libraryIsNotLibrary.error");	
		}
		else if ( ! cell.getSampleType().getIName().equals("cell")) { 
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.cellIsNotCell.error");
		}
		else if( ! jobLibraries.contains(library) ){//confirm library is really part of this jobId
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.libraryJobMismatch.error");	
		}
		else if ( "".equals(libConcInCellPicoM) || "".equals(libConcInCellPicoM.trim())) {
			addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.pmoleAddedInvalidValue.error");	
		}
		else{
			try{
				libConcInCellPicoMFloat = new Float(Float.parseFloat(libConcInCellPicoM.trim()));
				if(libConcInCellPicoMFloat.floatValue() <= 0){
					addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.pmoleAddedInvalidValue.error");
				}
			}
			catch(Exception e){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.pmoleAddedInvalidValue.error");
			}
		}		

		if( "".equals(addLibraryToPlatformUnitErrorMessage) ){
			// ensure platform unit is available
			try{
				Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
				if( ! sampleService.getAvailableAndCompatiblePlatformUnits(job).contains(platformUnit) ){
					addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.flowcellStateError.error");
				}
			}catch(Exception e){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.flowcellNotFoundNotUnique.error");
			}
		}		
		
		if( "".equals(addLibraryToPlatformUnitErrorMessage) ){
			try{
				  sampleService.addLibraryToCell(cell, library, libConcInCellPicoMFloat, job);
				  addLibraryToPlatformUnitSuccessMessage = messageService.getMessage("platformunit.libAdded.success");
			} catch(SampleTypeException ste){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.sampleType.error");
				logger.warn(ste.getMessage()); // print more detailed error to warn logs
			} catch(SampleMultiplexException sme){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.multiplex.error");
				logger.warn(sme.getMessage()); // print more detailed error to debug logs
			} catch(SampleException se){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorNotFound.error");
				logger.warn(se.getMessage()); // print more detailed error to warn logs
			} catch(MetadataException me){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorBarcodeNotFound.error");
				logger.warn(me.getMessage()); // print more detailed error to warn logs
			}	
		}

		return "redirect:/job/"+jobId+"/samples.do?addLibraryToPlatformUnitErrorMessage="+addLibraryToPlatformUnitErrorMessage+"&libraryIdAssociatedWithMessage="+libraryId + "&addLibraryToPlatformUnitSuccessMessage=" + addLibraryToPlatformUnitSuccessMessage;
		
	}
	
	@RequestMapping(value="/{jobId}/macromolecule/{macromolSampleId}/createLibrary", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')")
	  public String jobCreateLibraryFromMacromoleculePage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("macromolSampleId") Integer macromolSampleId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage,
			  ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		Sample macromoleculeSample;		
		
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		for(Sample s : allJobSamples){
			if(s.getId().intValue() == macromolSampleId.intValue()){
				macromoleculeSample = s;
				//errorMessage="";
				m.addAttribute("macromoleculeSample", macromoleculeSample);
				m.addAttribute("organism", sampleService.getNameOfOrganism(macromoleculeSample, "Other"));
				
				String[] roles = {"ft"};
				List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(job.getWorkflow().getId(), roles, "library");
				if(librarySampleSubtypes.isEmpty()){
					//////waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
					errorMessage="Unexpected Error: sampleSubtype Not Found";
					m.addAttribute("errorMessage", errorMessage);
					return "job/home/createLibrary";
				}
				SampleSubtype librarySampleSubtype = librarySampleSubtypes.get(0); // should be one
				List<SampleMeta> libraryMeta = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(librarySampleSubtype);
				prepareAdaptorsetsAndAdaptors(job, libraryMeta, m);
				
				Sample library = new Sample();
				library.setSampleSubtype(librarySampleSubtype);
				library.setSampleType(sampleService.getSampleTypeDao().getSampleTypeByIName("facilityLibrary"));
				library.setSampleMeta(libraryMeta);
				m.addAttribute("sample", library); 
				
				break;
			}
		}
		m.addAttribute("errorMessage", errorMessage);
			
		return "job/home/createLibrary";
	}
	
	  @RequestMapping(value = "/{jobId}/macromolecule/{macromolSampleId}/createLibrary", method = RequestMethod.POST)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String createLibrary1234(
			  @PathVariable("jobId") Integer jobId,
			  @PathVariable("macromolSampleId") Integer macromolSampleId,
			  @RequestBody String jsonString, //we cannot do @RequestBody Sample libraryForm due to the metadata
				/*
				 * BindingResult result, //cannot use this BindingResult as it would be bound to the jsonString, which is NOT useful
				 * SessionStatus status, //do not need SessionStatus 
				 */ 
				ModelMap m) throws MetadataException {
			
		  //creating a new facilityGenerated Library via a JSON call
		  
		  String errorMessage = "";
		  
		  try{			  
				Job jobForThisSample = jobService.getJobByJobId(jobId);
				Sample parentMacromolecule = sampleService.getSampleById(macromolSampleId);
				//TODO confirm job exists; confirm parentMacromolecule exists; confirm parentMacromolecule part of this job. If not, throw new Excpetion
	
				Sample libraryForm = constructInstanceFromJson(jsonString, Sample.class);//this is set to silently ignore any paramaters that are not part of a Sample
				libraryForm.setName(libraryForm.getName().trim());//from the form

				//programmatically validate constraints on Sample attributes (note: will NOT examine sample.metadata); see http://static.springsource.org/spring/docs/3.0.0.RC3/reference/html/ch05s07.html (see part 5.7.3 Configuring a DataBinder)
				WebDataBinder binder = new WebDataBinder(libraryForm, "sample");//setting the objectName to "sample" is very important
				initBinder(binder);//WaspController method that sets the validator to an autowired BeanValidator. This call replaces: binder.setValidator(validator);
				//binder.bind(propertyValues); //Don't know how to set this, but apparently works fine (at least in this case) without it. Perhaps the validator from WASPController has already dealt with this step??
				binder.validate();// validate the target object, in this case, sampleForm
				BindingResult result = binder.getBindingResult(); // get BindingResult that currently includes any validation errors from the Sample validation, and also use it later for additional, direct, validations (such as validating metadata)
				//perform an additional validation of Sample to make sure that Sample.name is unique within this job
				//TODO: Actually, if samples are shared between  jobs, then we should also extend the validation to include ALL jobs this sample is a part of
				validateSampleNameUnique(libraryForm.getName(), new Integer(-1), jobForThisSample, result);
				
				//retrieve Sample.metadata from the form AND validate it too
				List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromJsonAndTemplateToSubtype(constructMapFromJson(jsonString), sampleService.getSampleSubtypeById(libraryForm.getSampleSubtypeId()), result); 
							
				if(result.hasErrors()){
					libraryForm.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId()));
					libraryForm.setSampleSubtype(sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()));
					libraryForm.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(libraryForm.getSampleSubtype(), metaFromForm));
					m.put("sample", libraryForm);
					m.put("job", jobForThisSample);
					m.put("macromoleculeSample", parentMacromolecule);
					prepareAdaptorsetsAndAdaptors(jobForThisSample, libraryForm.getSampleMeta(), m);
					m.addAttribute("organism", sampleService.getNameOfOrganism(parentMacromolecule, "Other"));
					//need next line to send the bindingResult, with the errors, to the jsp (it's not automatic in this case)
					m.addAttribute(BindingResult.MODEL_KEY_PREFIX+result.getObjectName(), result);//http://static.springsource.org/autorepo/docs/spring/2.5.x/api/org/springframework/validation/BindingResult.html
					
					return "job/home/createLibrary";
				}
			 
				//all OK so create/save new library
				libraryForm.setSubmitterLabId(parentMacromolecule.getSubmitterLabId());//needed??
				libraryForm.setSubmitterUserId(parentMacromolecule.getSubmitterUserId());//needed??
				libraryForm.setSubmitterJobId(parentMacromolecule.getSubmitterJobId());//needed??
				libraryForm.setIsActive(new Integer(1));
				SampleWrapper managedLibraryFromForm = new SampleWrapperWebapp(libraryForm);
				managedLibraryFromForm.setParent(parentMacromolecule);
				sampleService.createFacilityLibraryFromMacro(jobForThisSample, managedLibraryFromForm, metaFromForm);
				
				return "redirect:/job/"+jobId+"/samples.do";			  
		  }
		  catch(Exception e){
			  	e.printStackTrace();
				errorMessage = "Creation Of New Library Failed: Unexpected Error";
				return "redirect:/job/"+jobId+"/macromolecule/"+macromolSampleId+"/createLibrary.do?errorMessage="+errorMessage;
		  }
	 }
	
	  /* ********************* NO LONGER USED	
	  @RequestMapping(value = "/{jobId}/macromolecule/{macromolSampleId}/createLibrary", method = RequestMethod.POST)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String createLibrary(@PathVariable("macromolSampleId") Integer macromolSampleId,
			  @PathVariable("jobId") Integer jobId, 
			  @Valid Sample libraryForm, BindingResult result, 
			  SessionStatus status, 
			  ModelMap m) {

		  //display the new library as read-only. add success message, and display. Cannot go directly back to samples.jsp, as that is displayed in a div, but this POST will land back in IFRAME
		  
		  // must add something like this
		  //String returnString = validateJobIdAndSampleId(jobId, macromolSampleId, null);
		  //if (returnString != null)
			//  return returnString;
		  // 
		  
		  //cannot do it this way; cancel would return via Iframe and that is NOT desired. Instead, cancel is handled directly in the jsp
		  ///if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
			//  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		  //}

		  String successMessage = "";
		  String errorMessage = "";
		  Sample parentMacromolecule = sampleService.getSampleDao().getSampleBySampleId(macromolSampleId);
		  Job jobForThisSample = jobService.getJobByJobId(jobId);

		  m.addAttribute("organism", sampleService.getNameOfOrganism(parentMacromolecule, "Other"));
		  
		  libraryForm.setName(libraryForm.getName().trim());
		  SampleSubtype sampleSubtype = sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId());
		  validateSampleNameUnique(libraryForm.getName(), macromolSampleId, jobForThisSample, result);

		  // get validated metadata from 
		  List<SampleMeta> sampleMetaListFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result);

		  if (result.hasErrors()) {
			  libraryForm.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, sampleMetaListFromForm));
			  libraryForm.setSampleSubtype(sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()));
			  libraryForm.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId()));
			  prepareSelectListData(m);//doubt that this is required here; really only needed for meta relating to country or state
			  //waspErrorMessage("sampleDetail.updated.error");
			  prepareAdaptorsetsAndAdaptors(jobForThisSample, libraryForm.getSampleMeta(), m);
			  m.put("macromoleculeSample", parentMacromolecule);
			  m.put("job", jobForThisSample);
			  m.put("sample", libraryForm); 
			  return "job/home/createLibrary";
		  }

		  //all OK so create/save new library
		  libraryForm.setSubmitterLabId(parentMacromolecule.getSubmitterLabId());//needed??
		  libraryForm.setSubmitterUserId(parentMacromolecule.getSubmitterUserId());//needed??
		  libraryForm.setSubmitterJobId(parentMacromolecule.getSubmitterJobId());//needed??
		  libraryForm.setIsActive(new Integer(1));
		  SampleWrapper managedLibraryFromForm = new SampleWrapperWebapp(libraryForm);
		   try {
			  managedLibraryFromForm.setParent(parentMacromolecule);
			  sampleService.createFacilityLibraryFromMacro(jobForThisSample, managedLibraryFromForm, sampleMetaListFromForm);
			  successMessage="New Library Successfully Created";
			  return "redirect:/job/"+jobId+"/library/"+managedLibraryFromForm.getSampleObject().getId()+"/librarydetail_ro.do?successMessage="+successMessage;
		  } catch (SampleParentChildException e) {
			  logger.warn(e.getLocalizedMessage());
			  errorMessage="Library Creation Unexpectedly Failed.";
			  m.put("errorMessage", errorMessage); 
			  m.put("job", jobService.getJobByJobId(jobId));
			  //waspErrorMessage("libraryCreated.sample_problem.error");
			  return "job/home/error";
		  } catch (MessagingException e) {
			  logger.warn(e.getLocalizedMessage());
			  errorMessage="Library Creation Unexpectedly Failed.";
			  m.put("errorMessage", errorMessage); 
			  m.put("job", jobService.getJobByJobId(jobId));
			  //waspErrorMessage("libraryCreated.message_fail.error");
			  return "job/home/message";
		  } 
	 }
	 **************************/
	@RequestMapping(value="/{jobId}/sample/{sampleId}/sampledetail_ro", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobSampleDetailPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("sampleId") Integer sampleId, ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		String errorMessage = "Error locating requested Job or Sample in database";

		Sample theRequestedSample = null;
		SampleWrapperWebapp sampleManaged;
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		for(Sample sample : allJobSamples){
			if(sample.getId().intValue() == sampleId.intValue()){
				theRequestedSample = sample;
				errorMessage="";
				sampleManaged = new SampleWrapperWebapp(theRequestedSample);
				m.addAttribute("normalizedSampleMeta", SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(theRequestedSample.getSampleSubtype(), sampleManaged.getAllSampleMeta()) );
				
				//this is needed for the organism meta to be interpreted properly during metadata display; added by Rob 5-2-13
				m.addAttribute("organisms", sampleService.getOrganismsPlusOther());
				
				m.addAttribute("sample", theRequestedSample);
				break;
			}
		}
		m.addAttribute("errorMessage", errorMessage);
		return "job/home/sampledetail_ro";
	}
	@RequestMapping(value="/{jobId}/sample/{sampleId}/sampledetail_rw", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobSampleDetailRWPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("sampleId") Integer sampleId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage,
			  ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		Sample theRequestedSample = null;
		SampleWrapperWebapp sampleManaged;
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		for(Sample sample : allJobSamples){
			if(sample.getId().intValue() == sampleId.intValue()){
				theRequestedSample = sample;
				sampleManaged = new SampleWrapperWebapp(theRequestedSample);
				m.addAttribute("normalizedSampleMeta", SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(theRequestedSample.getSampleSubtype(), sampleManaged.getAllSampleMeta()) );
				
				//this is needed for the organism meta to be interpreted properly during metadata display; added by Rob 5-2-13
				m.addAttribute("organisms", sampleService.getOrganismsPlusOther());
				
				m.addAttribute("sample", theRequestedSample);
				break;
			}
		}
		m.addAttribute("errorMessage", errorMessage);
		return "job/home/sampledetail_rw";
	}	
	private <T extends WaspModel> T constructInstanceFromJson(String jsonString, Class<T> clazz) throws Exception{
		
		//convert jsonString to some object (if meta entries present, they will be ignored due to the om.configure setting)
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);//http://stackoverflow.com/questions/4486787/jackson-with-json-unrecognized-field-not-marked-as-ignorable
		T obj = om.readValue(jsonString, clazz);
		return obj;
	}
	private Map<String,String> constructMapFromJson(String jsonString) throws Exception{
		
		ObjectMapper om = new ObjectMapper();
		Map<String,String> map = om.readValue(jsonString, new TypeReference<HashMap<String,String>>() { });
		return map;
	}
	
	@RequestMapping(value = "/{jobId}/sample/{sampleId}/sampledetail_rw", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateJobSampleDetailRW(@PathVariable("jobId") Integer jobId, 
			@PathVariable("sampleId") Integer sampleId, 
			@RequestBody String jsonString, //we cannot do @RequestBody Sample sampleForm due to the metadata
			/*
			 * BindingResult result, //cannot use this BindingResult as it would be bound to the jsonString, which is NOT useful
			 * SessionStatus status, //do not need SessionStatus 
			 */ 
			ModelMap m) throws MetadataException {
		
		String errorMessage = "";

		try{
			Job jobForThisSample = jobService.getJobByJobId(jobId);	
			//TODO confirm job exists; confirm sample exists; confirm sample part of this job. It not, throw new Excpetion

			Sample sampleForm = constructInstanceFromJson(jsonString, Sample.class);//this is set to silently ignore any paramaters that are not part of a Sample
			sampleForm.setName(sampleForm.getName().trim());//from the form

			//programmatically validate constraints on Sample attributes (note: will NOT examine sample.metadata); see http://static.springsource.org/spring/docs/3.0.0.RC3/reference/html/ch05s07.html (see part 5.7.3 Configuring a DataBinder)
			WebDataBinder binder = new WebDataBinder(sampleForm, "sample");//setting the objectName to "sample" is very important
			initBinder(binder);//WaspController method that sets the validator to an autowired BeanValidator. This call replaces: binder.setValidator(validator);
			//binder.bind(propertyValues); //Don't know how to set this, but apparently works fine (at least in this case) without it. Perhaps the validator from WASPController has already dealt with this step??
			binder.validate();// validate the target object, in this case, sampleForm
			BindingResult result = binder.getBindingResult(); // get BindingResult that currently includes any validation errors from the Sample validation, and also use it later for additional, direct, validations (such as validating metadata)
			//perform an additional validation of Sample to make sure that Sample.name is unique within this job
			//TODO: Actually, if samples are shared between  jobs, then we should also extend the validation to include ALL jobs this sample is a part of
			validateSampleNameUnique(sampleForm.getName(), sampleId, jobForThisSample, result);
			
			//retrieve Sample.metadata from the form AND validate it too
			List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromJsonAndTemplateToSubtype(constructMapFromJson(jsonString), sampleService.getSampleSubtypeById(sampleForm.getSampleSubtypeId()), result); 
									
			if(result.hasErrors()){
				sampleForm.setId(sampleId);//since this sampleForm was created thru json, it curretly lacks id
				sampleForm.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(sampleForm.getSampleTypeId()));
				sampleForm.setSampleSubtype(sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(sampleForm.getSampleSubtypeId()));
				m.put("job", jobForThisSample);
				m.put("sample", sampleForm); 
				m.addAttribute("normalizedSampleMeta",SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sampleForm.getSampleSubtype(), metaFromForm));
				m.addAttribute("organisms", sampleService.getOrganismsPlusOther());
				//need next line to send the bindingResult, with the errors, to the jsp (it's not automatic in this case)
				m.addAttribute(BindingResult.MODEL_KEY_PREFIX+result.getObjectName(), result);//http://static.springsource.org/autorepo/docs/spring/2.5.x/api/org/springframework/validation/BindingResult.html
				
				return "job/home/sampledetail_rw";
			}
			
			Sample sample = sampleService.getSampleById(sampleId);
			sample.setName(sampleForm.getName());
			SampleWrapperWebapp managedSample = new SampleWrapperWebapp(sample);
			sampleService.updateExistingSampleViaSampleWrapper(managedSample, metaFromForm);
			return "redirect:/job/"+jobId+"/sample/"+sampleId+"/sampledetail_ro.do";

		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "Update Failed: Unexpected Error";
			return "redirect:/job/"+jobId+"/sample/"+sampleId+"/sampledetail_rw.do?errorMessage="+errorMessage;
		}
	}
	/* ********************* NO LONGER USED
	@RequestMapping(value = "/{jobId}/sample/{sampleId}/sampledetail_rw", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateJobSampleDetailRW(@PathVariable("jobId") Integer jobId, 
			@PathVariable("sampleId") Integer sampleId, @Valid Sample sampleForm, BindingResult result, 
			SessionStatus status, ModelMap m) throws MetadataException {
		
			if ( request.getParameter("submit").equals("Cancel") ){
				return "redirect:/job/"+jobId+"/sample/"+sampleId+"/sampledetail_ro.do";
			} 

			Job jobForThisSample = jobDao.getJobByJobId(jobId);
			
			String errorMessage = "Error locating requested Job or Sample in database";

			List<Sample> allJobSamples = jobForThisSample.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
			for(Sample s : allJobSamples){
				if(s.getId().intValue() == sampleId.intValue()){			
					errorMessage="";
					break;
				}
			}
			m.addAttribute("errorMessage", errorMessage);
		  	if(!"".equals(errorMessage)){
		  		return "redirect:/job/"+jobId+"/sample/"+sampleId+"/sampledetail_ro.do";
		  	}
			
			
			sampleForm.setName(sampleForm.getName().trim());//from the form
			validateSampleNameUnique(sampleForm.getName(), sampleId, jobForThisSample, result);
	  
			Sample sample = sampleService.getSampleById(sampleId);//sampleDao.getSampleBySampleId(sampleId); 
			SampleWrapperWebapp managedSample = new SampleWrapperWebapp(sample);
			List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, sample.getSampleSubtype(), result); // gets meta and adds back to managed sampleForm as it is not persisted
			if(result.hasErrors()){
				sampleForm.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(sampleForm.getSampleTypeId()));
				sampleForm.setSampleSubtype(sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(sampleForm.getSampleSubtypeId()));
				m.put("job", jobForThisSample);
				m.put("sample", sampleForm); 
				m.addAttribute("normalizedSampleMeta",SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sample.getSampleSubtype(), metaFromForm));

				//this is needed for the organism meta to be interpreted properly during metadata display; added by Rob 5-2-13
				m.addAttribute("organisms", sampleService.getOrganismsPlusOther());

				return "job/home/sampledetail_rw";
			}
			sample.setName(sampleForm.getName());
	  
			sampleService.updateExistingSampleViaSampleWrapper(managedSample, metaFromForm);

			///waspMessage("sampleDetail.updated_success.label");
			return "redirect:/job/"+jobId+"/sample/"+sampleId+"/sampledetail_ro.do";

		}
		//end no longer used *****************/
	
	
	
	  /**
	   * See if Sample name has changed between sample objects and if so check if the new name is unique within the job.
	   * @param formSample
	   * @param originalSample
	   * @param job
	   * @param result
	   */
	  private void validateSampleNameUnique(String sampleName, Integer sampleId, Job job, BindingResult result){
		  //confirm that, if a new sample.name was supplied on the form, it is different from all other sample.name in this job
		  List<Sample> samplesInThisJob = job.getSample();
		  for(Sample eachSampleInThisJob : samplesInThisJob){
			  if(eachSampleInThisJob.getId().intValue() != sampleId.intValue()){
				  if( sampleName.equals(eachSampleInThisJob.getName()) ){
					  // adding an error to 'result object' linked to the 'name' field as the name chosen already exists
					  Errors errors=new BindException(result.getTarget(), "sample");
					  // reject value on the 'name' field with the message defined in sampleDetail.updated.nameClashError
					  // usage: errors.rejectValue(field, errorString, default errorString)
					  errors.rejectValue("name", "sampleDetail.nameClash.error", "sampleDetail.nameClash.error (no message has been defined for this property)");
					  result.addAllErrors(errors);
					  break;
				  }
			  }
		  }
	  }	 
	  @RequestMapping(value="/{jobId}/library/{libraryId}/librarydetail_ro", method=RequestMethod.GET)
		  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
		  public String jobLibraryDetailPage(@PathVariable("jobId") Integer jobId, 
				  @PathVariable("libraryId") Integer libraryId, 
				  @RequestParam(value="successMessage", required=false) String successMessage, 
				  ModelMap m) throws SampleTypeException {

			Job job = jobService.getJobByJobId(jobId);
			m.addAttribute("job", job);
			
			String errorMessage = "Error locating requested Job or Sample in database";

			Sample theRequestedSample = null;
			SampleWrapperWebapp sampleManaged;
			List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
			for(Sample sample : allJobSamples){
				if(sample.getId().intValue() == libraryId.intValue()){
					theRequestedSample = sample;
					errorMessage="";
					break;
				}
			}
			
			try{
				libraryDetail(jobId, libraryId, m);
			}catch(Exception e){errorMessage = e.getMessage();}
			
			m.addAttribute("errorMessage", errorMessage);
			m.addAttribute("successMessage", successMessage);//only coming from CreateLibrary; do not ever set this 
			return "job/home/librarydetail_ro";
		}
	  @RequestMapping(value="/{jobId}/library/{libraryId}/librarydetail_rw", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobLibraryDetailRWPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("libraryId") Integer libraryId, ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		String errorMessage = "Error locating requested Job or Sample in database";

		Sample theRequestedSample = null;
		SampleWrapperWebapp sampleManaged;
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		for(Sample sample : allJobSamples){
			if(sample.getId().intValue() == libraryId.intValue()){
				theRequestedSample = sample;
				errorMessage="";
				break;
			}
		}
		
		try{
			libraryDetail(jobId, libraryId, m);
		}catch(Exception e){errorMessage = e.getMessage();}
		
		m.addAttribute("errorMessage", errorMessage);
		return "job/home/librarydetail_rw";
	}
	  
	@RequestMapping(value = "/{jobId}/library/{libraryId}/librarydetail", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateJobLibraryDetailRW(@PathVariable("jobId") Integer jobId, 
		@PathVariable("libraryId") Integer libraryId,
		@RequestBody String jsonString, //we cannot do @RequestBody Sample libraryForm due to the metadata
		/*
		 * BindingResult result, //cannot use this BindingResult as it would be bound to the jsonString, which is NOT useful
		 * SessionStatus status, //do not need SessionStatus 
		 */ 
		ModelMap m) throws MetadataException {
			
			String errorMessage = "";

			try{
				Job jobForThisSample = jobDao.getJobByJobId(jobId);	
				//TODO confirm job exists; confirm sample exists; confirm sample part of this job. It not, throw new Excpetion

				Sample libraryForm = constructInstanceFromJson(jsonString, Sample.class);//this is set to silently ignore any paramaters that are not part of a Sample
				libraryForm.setName(libraryForm.getName().trim());//from the form

				//programmatically validate constraints on Sample attributes (note: will NOT examine sample.metadata); see http://static.springsource.org/spring/docs/3.0.0.RC3/reference/html/ch05s07.html (see part 5.7.3 Configuring a DataBinder)
				WebDataBinder binder = new WebDataBinder(libraryForm, "sample");//setting the objectName to "sample" is very important
				initBinder(binder);//WaspController method that sets the validator to an autowired BeanValidator. This call replaces: binder.setValidator(validator);
				//binder.bind(propertyValues); //Don't know how to set this, but apparently works fine (at least in this case) without it. Perhaps the validator from WASPController has already dealt with this step??
				binder.validate();// validate the target object, in this case, sampleForm
				BindingResult result = binder.getBindingResult(); // get BindingResult that currently includes any validation errors from the Sample validation, and also use it later for additional, direct, validations (such as validating metadata)
				//perform an additional validation of Sample to make sure that Sample.name is unique within this job
				//TODO: Actually, if samples are shared between  jobs, then we should also extend the validation to include ALL jobs this sample is a part of
				validateSampleNameUnique(libraryForm.getName(), libraryId, jobForThisSample, result);
				
				//retrieve Sample.metadata from the form AND validate it too
				List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromJsonAndTemplateToSubtype(constructMapFromJson(jsonString), sampleService.getSampleSubtypeById(libraryForm.getSampleSubtypeId()), result); 
										
				if(result.hasErrors()){
					
					libraryForm.setSampleMeta(metaFromForm);
					libraryDetail(jobId, libraryForm, libraryId, m);
					
					//all these things below and commented out will be performed in the call above: libraryDetail(jobId, libraryForm, libraryId, m);
					//libraryForm.setId(libraryId);//since this libraryForm was created thru json, it curretly lacks id
					//libraryForm.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId()));
					//libraryForm.setSampleSubtype(sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()));
					//m.put("job", jobForThisSample);
					//m.put("sample", libraryForm); 
					//m.addAttribute("normalizedSampleMeta",SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(libraryForm.getSampleSubtype(), metaFromForm));
					//m.addAttribute("organisms", sampleService.getOrganismsPlusOther());
					
					//need next line to send the bindingResult, with the errors, to the jsp (it's not automatic in this case)
					m.addAttribute(BindingResult.MODEL_KEY_PREFIX+result.getObjectName(), result);//http://static.springsource.org/autorepo/docs/spring/2.5.x/api/org/springframework/validation/BindingResult.html
					
					return "job/home/librarydetail_rw";
				}
				
				Sample library = sampleService.getSampleById(libraryId);
				library.setName(libraryForm.getName());
				SampleWrapperWebapp managedLibrary = new SampleWrapperWebapp(library);
				sampleService.updateExistingSampleViaSampleWrapper(managedLibrary, metaFromForm);
				return "redirect:/job/"+jobId+"/library/"+libraryId+"/librarydetail_ro.do";

			}catch(Exception e){
				e.printStackTrace();
				errorMessage = "Update Failed: Unexpected Error";
				return "redirect:/job/"+jobId+"/library/"+libraryId+"/librarydetail_rw.do?errorMessage="+errorMessage;
			}
	}
		
	/* ********************* NO LONGER USED
	@RequestMapping(value = "/{jobId}/library/{libraryId}/librarydetail_rw", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
		@PreAuthorize("hasRole('su') or hasRole('ft')")
		public String updateJobLibraryDetailRW(@PathVariable("jobId") Integer jobId, 
				@PathVariable("libraryId") Integer libraryId, @Valid Sample libraryForm, BindingResult result, 
				SessionStatus status, ModelMap m) throws MetadataException {
			
				if ( request.getParameter("submit").equals("Cancel") ){
					return "redirect:/job/"+jobId+"/library/"+libraryId+"/librarydetail_ro.do";
				} 

				Job jobForThisSample = jobDao.getJobByJobId(jobId);
				
				String errorMessage = "Error locating requested Job or Sample in database";

				List<Sample> allJobSamples = jobForThisSample.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
				for(Sample s : allJobSamples){
					if(s.getId().intValue() == libraryId.intValue()){			
						errorMessage="";
						break;
					}
				}
				m.addAttribute("errorMessage", errorMessage);
			  	if(!"".equals(errorMessage)){
			  		return "redirect:/job/"+jobId+"/library/"+libraryId+"/librarydetail_ro.do";
			  	}
			  	libraryForm.setName(libraryForm.getName().trim());
			  
			  	Sample library = sampleService.getSampleDao().getSampleBySampleId(libraryId); 
			  	validateSampleNameUnique(libraryForm.getName(), libraryId, jobDao.getJobByJobId(jobId), result);
			  	SampleWrapperWebapp managedLibrary = new SampleWrapperWebapp(library);
			  	List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, 
					  sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()), result); 
			  	if(result.hasErrors()){
				  //waspErrorMessage("sampleDetail.updated.error");
				  libraryForm.setSampleMeta(metaFromForm);
				  libraryDetail(jobId, libraryForm, libraryId, m);
				  return "job/home/librarydetail_rw";
			  	}
			  // all ok so save 
			  library.setName(libraryForm.getName());
			  sampleService.updateExistingSampleViaSampleWrapper(managedLibrary, metaFromForm);			  	

			  	//what if the update fails??
			  	
			  	return "redirect:/job/"+jobId+"/library/"+libraryId+"/librarydetail_ro.do";
		}
		//end no longer used *****************/
		
	 /**
	   * Handles preparation of model for display of library details. Makes a detached Sample object.
	   * @param jobId
	   * @param libraryInId
	   * @param m
	   * @param isRW
	   * @return
	   * @throws MetadataException
	   */
	  public void libraryDetail(Integer jobId, Integer libraryInId, ModelMap m) throws MetadataException{
			// get the library subtype for this workflow as the job-viewer sees it. We will use this 
			// to synchronize the metadata for display.
		    // We make a new Sample object 'modelLibrary' so that we can use it with the model and adjust the sample subtype / metadata freely
		    // without affecting the info in the database
			Sample libraryIn = sampleService.getSampleDao().getSampleBySampleId(libraryInId);
			String[] roles = {"lu"};
			List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(jobDao.getJobByJobId(jobId).getWorkflow().getWorkflowId(), roles, "library");
			if(librarySampleSubtypes.isEmpty()){
				throw new MetadataException("Sample Subtype Not Found");
				//waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
				//return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
			}
			Sample modelLibrary = new Sample();
			modelLibrary.setSampleSubtype(librarySampleSubtypes.get(0)); // should be one
			if (libraryIn.getParentId() != null)
				modelLibrary.setParent(libraryIn.getParent());
			modelLibrary.setId(libraryIn.getId());
			modelLibrary.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(libraryIn.getSampleTypeId()));
			modelLibrary.setName(libraryIn.getName());
			modelLibrary.setSampleMeta(libraryIn.getSampleMeta());
			libraryDetail(jobId, modelLibrary, libraryInId, m);
	  }
	  
	  
	  /**
	   * Handles preparation of model for display of library details. 
	   * @param jobId
	   * @param libraryIn: should be a detached entity
	   * @param libraryInId: necessary if libraryIn has no id (e.g. from form)
	   * @param m
	   * @param isRW
	   * @return
	   * @throws MetadataException
	   */
	  public void libraryDetail(Integer jobId, Sample libraryIn, Integer libraryInId, ModelMap m) throws MetadataException{
		  	Job job = jobDao.getJobByJobId(jobId);
		  	
		  	// libraryIn should be a detached Sample object. If the sampleId is null then the Sample object is from a form and all the metadata from the form is 
		  	// assumed to be associated with it. Otherwise we assume that the library info is cloned from a persisted object.
			SampleWrapperWebapp libraryInManaged = new SampleWrapperWebapp(libraryIn);
			
	  		
			libraryIn.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(
					sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(libraryIn.getSampleSubtypeId()), 
					libraryInManaged.getAllSampleMeta()));
			

			SampleWrapperWebapp persistentLibraryManaged;
			if (libraryIn.getId() == null){
				persistentLibraryManaged = new SampleWrapperWebapp(sampleService.getSampleDao().getSampleBySampleId(libraryInId));
			} else {
				persistentLibraryManaged = libraryInManaged;
			}
			Sample parentMacromolecule = null;
			if (persistentLibraryManaged.getParentWrapper() != null)
				parentMacromolecule = persistentLibraryManaged.getParentWrapper().getSampleObject();
			
			prepareAdaptorsetsAndAdaptors(job, libraryIn.getSampleMeta(), m);
			  
			//this is needed for the organism meta to be interpreted properly during metadata display (added by Rob; 5-2-13)
			//TODO do not want organism for facility-generated library: 
			m.addAttribute("organisms", sampleService.getOrganismsPlusOther());

			if (libraryIn.getId() == null)
				libraryIn.setSampleId(libraryInId);
			m.addAttribute("job", job);
			m.addAttribute("extraJobDetailsMap", jobService.getExtraJobDetails(job));
			LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
			m.addAttribute("jobApprovalsMap", jobApprovalsMap);
			//get the jobApprovals Comments (if any)
			HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
			m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
			//get the current jobStatus
			m.addAttribute("jobStatus", jobService.getJobStatus(job));

			m.addAttribute("sample", libraryIn);
			m.addAttribute("parentMacromolecule", parentMacromolecule);
			
	  }
	  /**
	   * get adaptorsets and adaptors for populating model. If a selected adaptor is found in the provided SampleDraftMeta
	   * it is used to find appropriate adaptors
	   * @param jobDraft
	   * @param sampleDraftMeta
	   * @param m
	   */
		private void prepareAdaptorsetsAndAdaptors(Job job, List<SampleMeta> sampleMeta, ModelMap m){
			List<Adaptorset> adaptorsets = new ArrayList<Adaptorset>();
			for (JobResourcecategory jrc: job.getJobResourcecategory()){
				Map<String, Integer> adaptorsetRCQuery = new HashMap<String, Integer>();
				adaptorsetRCQuery.put("resourcecategoryId", jrc.getResourcecategoryId());
				for (AdaptorsetResourceCategory asrc: adaptorsetResourceCategoryDao.findByMap(adaptorsetRCQuery))
					adaptorsets.add(asrc.getAdaptorset());
			}
			m.addAttribute("adaptorsets", adaptorsets); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
			
			List<Adaptor> adaptors = new ArrayList<Adaptor>();
			Adaptorset selectedAdaptorset = null;
			try{	
	  			selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", sampleMeta)) );
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

	
	
	
	
	
	
	
	@RequestMapping(value="/{jobId}/fileUploadManager", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobFileUploadPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage, 
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		m.addAttribute("job", job);
		
		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
		for(JobFile jf: job.getJobFile()){
			FileGroup fileGroup = jf.getFile();//returns a FileGroup
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
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		m.addAttribute("fileHandlesThatCanBeViewedList", fileHandlesThatCanBeViewedList);
		
		if(errorMessage==null){
			errorMessage="";
		}
		m.addAttribute("errorMessage", errorMessage);
		
		return "job/home/fileUploadManager";
	}
	
	@RequestMapping(value="/{jobId}/fileUploadManager", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobFileUploadPostPage(@PathVariable("jobId") Integer jobId,
				@RequestParam("file_description") String fileDescription,
				@RequestParam("file_upload") MultipartFile mpFile, ModelMap m) throws SampleTypeException {

		String errorMessage = "";
		
		Job job = jobService.getJobByJobId(jobId);
		
		if(mpFile.isEmpty() && !"".equals(fileDescription)){
			//waspErrorMessage("listJobSamples.fileUploadFailed_fileEmpty.error");
			errorMessage="File upload failed: no file provided";
		}
		else if(!mpFile.isEmpty()  && "".equals(fileDescription)){
			//waspErrorMessage("listJobSamples.fileUploadFailed_fileDescriptionEmpty.error");
			errorMessage="File upload failed: please provide a file description";
		}
		else if(mpFile.isEmpty()  && "".equals(fileDescription)){
			//waspErrorMessage("listJobSamples.fileUploadFailed_fileDescriptionEmpty.error");
			errorMessage="File upload failed: please select a file and provide a file description";
		}
		
		if( "".equals(errorMessage) ){
			Random randomNumberGenerator = new Random(System.currentTimeMillis());
			try{
				fileService.uploadJobFile(mpFile, job, fileDescription, randomNumberGenerator);//will upload and perform all database updates
			} catch(FileUploadException e){
				logger.warn(e.getMessage());
				//waspErrorMessage("listJobSamples.fileUploadFailed.error");
				errorMessage="File upload unexpectedly failed. Please try again.";
			}
		}
		
		//waspMessage("listJobSamples.fileUploadedSuccessfully.label");	
		//String referer = request.getHeader("Referer");//return "redirect:"+ referer; 
		return "redirect:/job/"+jobId+"/fileUploadManager.do?errorMessage="+errorMessage;
	}
	
	@RequestMapping(value="/{jobId}/fileUploadManager1234", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobFileUploadPostPage1234(@PathVariable("jobId") Integer jobId,
			  MultipartHttpServletRequest request, HttpServletResponse response) throws SampleTypeException {

		String errorMessage = "";
				
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	System.out.println("----The uploaded file is unexpectedly null");
		   	errorMessage = "Upload Failed: Unexpected Error. Job Not found in database.";
	    	return "redirect:/job/"+jobId+"/fileUploadManager.do?errorMessage="+errorMessage;
		}

		List<MultipartFile> mpFiles = request.getFiles("file_upload");
	    if(mpFiles.isEmpty()){
	    	System.out.println("----The uploaded file List is unexpectedly empty");
	    	errorMessage = "Upload Failed: Select a file AND provide a description";
	    	return "redirect:/job/"+jobId+"/fileUploadManager.do?errorMessage="+errorMessage;
	    }
	   	MultipartFile mpFile = mpFiles.get(0);
	   	if(mpFile==null){
		   	System.out.println("----The uploaded file is unexpectedly null");
		   	errorMessage = "Upload Failed: Unexpected Error";
	    	return "redirect:/job/"+jobId+"/fileUploadManager.do?errorMessage="+errorMessage;
		}
		
	   	String fileDescription = request.getParameter("file_description");
	    
	    if(fileDescription==null || "".equals(fileDescription)){
	    	System.out.println("--The fileDescription is unexpectedly null");
	    	errorMessage = "Upload Failed: Select a file AND provide a description";
	    	return "redirect:/job/"+jobId+"/fileUploadManager.do?errorMessage="+errorMessage;
	    }
	    /*		
		if(mpFile.isEmpty() && !"".equals(fileDescription)){
			//waspErrorMessage("listJobSamples.fileUploadFailed_fileEmpty.error");
			errorMessage="File upload failed: no file provided";
		}
		else if(!mpFile.isEmpty()  && "".equals(fileDescription)){
			//waspErrorMessage("listJobSamples.fileUploadFailed_fileDescriptionEmpty.error");
			errorMessage="File upload failed: please provide a file description";
		}
		else if(mpFile.isEmpty()  && "".equals(fileDescription)){
			//waspErrorMessage("listJobSamples.fileUploadFailed_fileDescriptionEmpty.error");
			errorMessage="File upload failed: please select a file and provide a file description";
		}
	    */			
		Random randomNumberGenerator = new Random(System.currentTimeMillis());
		try{
			fileService.uploadJobFile(mpFile, job, fileDescription, randomNumberGenerator);//will upload and perform all database updates
		} catch(FileUploadException e){
			logger.warn(e.getMessage());
			//waspErrorMessage("listJobSamples.fileUploadFailed.error");
			errorMessage = "Upload Failed: Unexpected Error";
		}
		return "redirect:/job/"+jobId+"/fileUploadManager.do?errorMessage="+errorMessage;
	}
}

class JobIdComparator implements Comparator<Job> {
	@Override
	public int compare(Job arg0, Job arg1) {
		return arg0.getJobId().intValue() >= arg1.getJobId().intValue()?1:0;
	}
}
class SubmitterLastNameFirstNameComparator implements Comparator<Job> {
	@Override
	public int compare(Job arg0, Job arg1) {
		return arg0.getUser().getLastName().concat(arg0.getUser().getFirstName()).compareToIgnoreCase(arg1.getUser().getLastName().concat(arg1.getUser().getFirstName()));
	}
}
class PILastNameFirstNameComparator implements Comparator<Job> {
	@Override
	public int compare(Job arg0, Job arg1) {
		return arg0.getLab().getUser().getLastName().concat(arg0.getLab().getUser().getFirstName()).compareToIgnoreCase(arg1.getLab().getUser().getLastName().concat(arg1.getLab().getUser().getFirstName()));
	}
}
class JobNameComparator implements Comparator<Job> {
	@Override
	public int compare(Job arg0, Job arg1) {
		return arg0.getName().compareToIgnoreCase(arg1.getName());
	}
}
class JobCreatetsComparator implements Comparator<Job> {
	@Override
	public int compare(Job arg0, Job arg1) {
		return arg0.getCreatets().compareTo(arg1.getCreatets());
	}
}

class Filters{//not used 

// inner class Rules
public class Rules{
private String field;
private String op;
private String data;

public String getField() {
return field;
}
public void setField(String field) {
this.field = field;
}
public String getOp() {
return op;
}
public void setOp(String op) {
this.op = op;
}
public String getData() {
return data;
}
public void setData(String data) {
this.data = data;
}
}

private String groupOp;

private List<Rules> rules;

public String getGroupOp() {
return groupOp;
}

public void setGroupOp(String groupOp) {
this.groupOp = groupOp;
}

public List<Rules> getRules() {
return rules;
}

public void setRules(List<Rules> rules) {
this.rules = rules;
}

}
