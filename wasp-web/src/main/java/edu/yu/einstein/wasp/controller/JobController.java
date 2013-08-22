package edu.yu.einstein.wasp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
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
import edu.yu.einstein.wasp.controller.util.JsonHelperWebapp;
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
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
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
import edu.yu.einstein.wasp.model.ResourceType;
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
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.model.WaspModel;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FilterService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;
import edu.yu.einstein.wasp.util.StringHelper;
import edu.yu.einstein.wasp.web.Tooltip;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

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
	private GenomeService genomeService;
	@Autowired
	private JobService jobService;
	@Autowired
	private UserService UserService;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private AdaptorService adaptorService;
	@Autowired
	private ResourceService resourceService;
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
							// this is the link to the old job homepage "<a href=/wasp/sampleDnaToLibrary/listJobSamples/"+job.getId()+".do>J"+job.getId().intValue()+"</a>",
							"<a href=/wasp/job/"+job.getId()+"/homepage.do>J"+job.getId().intValue()+"</a>"  + " <a href=/wasp/sampleDnaToLibrary/listJobSamples/"+job.getId()+".do>(Old Link)</a>",
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
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	waspErrorMessage("job.jobUnexpectedlyNotFound.error"); 
			return "redirect:/dashboard.do";
		}
		m.addAttribute("job", job);
		return "job/home/homepage";
	}
  
	@RequestMapping(value="/{jobId}/basic", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobBasicPage(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		m.addAttribute("job", job);
		m.addAttribute("jobStatus", jobService.getJobStatus(job));
		
		
		String submitterInstitution = "";
		String pIInstitution = "";
		try{
			submitterInstitution = MetaHelper.getMetaValue("user", "institution", job.getUser().getUserMeta());
			pIInstitution = MetaHelper.getMetaValue("user", "institution", job.getLab().getUser().getUserMeta());
		}catch(Exception e){}
		m.addAttribute("submitterInstitution", submitterInstitution);	
		m.addAttribute("pIInstitution", pIInstitution);	
		
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
	}
  
	@RequestMapping(value="/{jobId}/costManager", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobCostPage(@PathVariable("jobId") Integer jobId,
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		populateCostPage(job, m);
		
		return "job/home/costManager";
	}

	private void populateCostPage(Job job, ModelMap m){
		
		m.addAttribute("job", job);
		
		AcctQuote mostRecentAcctQuote = job.getCurrentQuote();
		if(mostRecentAcctQuote.getId()!=null){
			m.addAttribute("mostRecentQuote", mostRecentAcctQuote.getAmount());
		}		
 		m.addAttribute("localCurrencyIcon", Currency.getInstance(Locale.getDefault()).getSymbol()); 		

		Set<AcctQuote> acctQuoteSet = job.getAcctQuote();
		List<AcctQuote>  acctQuoteList = new ArrayList<AcctQuote>(acctQuoteSet);
		class AcctQuoteCreatedComparator implements Comparator<AcctQuote> {
			@Override
			public int compare(AcctQuote arg0, AcctQuote arg1) {
				return arg1.getCreated().compareTo(arg0.getCreated());
			}
		}
		Collections.sort(acctQuoteList, new AcctQuoteCreatedComparator());//most recent is now first, least recent is last

		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
		
		for(AcctQuote acctQuote : acctQuoteList){//most recent is first, least recent is last
			List<AcctQuoteMeta> acctQuoteMetaList = acctQuote.getAcctQuoteMeta();
			for(AcctQuoteMeta acctQuoteMeta : acctQuoteMetaList){
				if(acctQuoteMeta.getK().toLowerCase().contains("filegroupid")){
					try{
						FileGroup fileGroup = fileService.getFileGroupById(Integer.parseInt(acctQuoteMeta.getV()));
						if(fileGroup.getId()!=null){
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
						break;
					}catch(Exception e){logger.warn("FileGroup unexpectedly not found");}
				}
			}
		}
		/*
		for(JobFile jf: job.getJobFile()){
			FileGroup fileGroup = jf.getFile();//returns a FileGroup
			//include only quotes and invoices
			if( fileGroup.getDescription().toLowerCase().startsWith("quote") || fileGroup.getDescription().toLowerCase().startsWith("invoice")){
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
		}
		*/
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		m.addAttribute("fileHandlesThatCanBeViewedList", fileHandlesThatCanBeViewedList);
	}
	
	@RequestMapping(value="/{jobId}/createQuoteOrInvoice", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')")
	  public String createJobQuoteOrInvoicePage(@PathVariable("jobId") Integer jobId,
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		getSampleLibraryRunData(job, m);
 		//String localCurrencyIcon = Currency.getInstance(Locale.getDefault()).getSymbol();//+String.format("%.2f", price));
 		m.addAttribute("localCurrencyIcon", Currency.getInstance(Locale.getDefault()).getSymbol()); 		
 		m.addAttribute("numberOfLanesRequested", job.getJobCellSelection().size());
 		
 		ResourceType resourceType = resourceService.getResourceTypeDao().getResourceTypeByIName("mps");
 		List<ResourceCategory>  activeSequencingMachineList = new ArrayList<ResourceCategory>();
 		for(ResourceCategory rc : resourceService.getResourceCategoryDao().getActiveResourceCategories()){
 			if(rc.getResourceTypeId()==resourceType.getId().intValue()){
 				activeSequencingMachineList.add(rc);
 			}
 		}
 		m.addAttribute("sequencingMachines", activeSequencingMachineList); 		

		return "job/home/createQuoteOrInvoice";
	}
	
	@RequestMapping(value="/{jobId}/viewerManager", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobViewerManagerPage(@PathVariable("jobId") Integer jobId, 
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		populateViewerManager(job, m);
		return "job/home/viewerManager";
	}

	private void populateViewerManager(Job job, ModelMap m){
		
		m.addAttribute("job", job);
		
		User jobSubmitter = job.getUser();
		m.addAttribute("jobSubmitter", jobSubmitter);
		User jobPI = job.getLab().getUser();
		m.addAttribute("jobPI", jobPI);
		
		List<User> jobViewers = new ArrayList<User>();
		for(JobUser jobUser : job.getJobUser()){
			jobViewers.add(jobUser.getUser());
		}
		class LastNameFirstNameComparator implements Comparator<User> {
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getLastName().concat(arg0.getFirstName()).compareToIgnoreCase(arg1.getLastName().concat(arg1.getFirstName()));
			}
		}
		Collections.sort(jobViewers, new LastNameFirstNameComparator());
		m.addAttribute("jobViewers", jobViewers);
		
		User currentWebViewer = authenticationService.getAuthenticatedUser();
		m.addAttribute("currentWebViewer", currentWebViewer);
		
		m.addAttribute("currentWebViewerIsSuperuserOrJobSubmitterOrJobPI", authenticationService.isSuperUser() || currentWebViewer.getId().intValue() == job.getUserId().intValue() || currentWebViewer.getId().intValue() == job.getLab().getPrimaryUserId().intValue());
	}
	
	@RequestMapping(value="/{jobId}/viewerManager", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobViewerManagerPostPage(@PathVariable("jobId") Integer jobId, @RequestParam("newViewerEmailAddress") String newViewerEmailAddress, ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		newViewerEmailAddress = newViewerEmailAddress==null?"":newViewerEmailAddress.trim();
		try{
			jobService.addJobViewer(jobId, newViewerEmailAddress);//performs checks to see if this is a legal action. Only superuser, jobSubmitter, and the job's PI can do this, else exception 
			m.addAttribute("successMessage", messageService.getMessage("listJobSamples.jobViewerAdded.label"));
		}
		catch(Exception e){	
			String errorMessage = messageService.getMessage(e.getMessage());
			logger.warn(errorMessage);		  
			m.addAttribute("errorMessage", errorMessage);
			m.addAttribute("newViewerEmailAddress", newViewerEmailAddress);
		}
		populateViewerManager(job, m);
		return "job/home/viewerManager";
	}
	
	@RequestMapping(value="/{jobId}/user/{userId}/removeJobViewer", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobViewerManagerRemoveUserPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("userId") Integer userId, ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}

		try{
			jobService.removeJobViewer(jobId, userId);//performs checks to see if this is a legal action. Only superuser, the job submitter, the job PI, and where a jobViewer is the webViewer can do this, else exception 
			m.addAttribute("successMessage", messageService.getMessage("listJobSamples.jobViewerRemoved.label"));

			//DO NOT PERFORM; will result in messy problem since this is now an ajax call!
			//If (me.getId().intValue() == userId.intValue() is true, then a user is removing him/her self from being a job viewer.
			//Normally, I would execute the doReauth() if me.getId().intValue() == userId.intValue()
			//However, if we performed the doReauth(), then, since the return is to a method demanding the viewer is a viewer of this job,
			//an exception will be thrown and it will look like a mess. 
			//To avoid, simply don't execute this doReathu(). It's not a problem, since the user is removing him/her self.
			//User me = authenticationService.getAuthenticatedUser();
			//if (me.getId().intValue() == userId.intValue()) {
			//	doReauth();//do this if the person performing the action is the person being removed from viewing this job (note: it cannot be the submitter or the pi)
			//}	
		}
		catch(Exception e){		    
			String errorMessage = messageService.getMessage(e.getMessage());
			logger.warn(errorMessage);		  
			m.addAttribute("errorMessage", errorMessage);
		}
		populateViewerManager(job, m);
		return "job/home/viewerManager";
	}
	
	@RequestMapping(value="/{jobId}/comments", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobCommentsPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage,
			  @RequestParam(value="successMessage", required=false) String successMessage,
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		populateComments(job, m);
		return "job/home/comments";
	}

	private void populateComments(Job job, ModelMap m){
		
		m.addAttribute("job", job);
		
		class LIFOMetaMessageDateComparator implements Comparator<MetaMessage> {//last date is first
			@Override
			public int compare(MetaMessage arg0, MetaMessage arg1) {
				return arg1.getDate().compareTo(arg0.getDate());
			}
		}
		
		//get user-submitted job comments (if any); order LastInFirstOut
		List<MetaMessage> userSubmittedJobCommentsList = jobService.getUserSubmittedJobComment(job.getId());
		for (MetaMessage metaMessage: userSubmittedJobCommentsList){
			metaMessage.setValue(StringUtils.replace(metaMessage.getValue(), "\r\n" ,"<br />"));//carriage return was inserted at time of INSERT to deal with line-break. Change it to <br /> for proper html display (using c:out escapeXml=false). Note that other html was escpaped at INSERT stage 
		}
		Collections.sort(userSubmittedJobCommentsList, new LIFOMetaMessageDateComparator());		
		m.addAttribute("userSubmittedJobCommentsList", userSubmittedJobCommentsList);
		
		//get the facility-generated job comments (if any); order LastInFirstOut
		List<MetaMessage> facilityJobCommentsList = jobService.getAllFacilityJobComments(job.getId());
		for (MetaMessage metaMessage: facilityJobCommentsList){
			metaMessage.setValue(StringUtils.replace(metaMessage.getValue(), "\r\n" ,"<br />"));
		}
		Collections.sort(facilityJobCommentsList, new LIFOMetaMessageDateComparator());
		m.addAttribute("facilityJobCommentsList", facilityJobCommentsList);
		
		//permit job's submitter and job's PI to add a comment, as well as superuser, ftech, da:
		boolean permissionToAddEditComment = false;//currently, no mechanism to edit exists on these comments on the webpage
		try{
			permissionToAddEditComment = authenticationService.hasPermission("hasRole('su') or hasRole('fm') or hasRole('ft') or hasRole('da-*') ");
		}catch(Exception e){
			logger.warn(e.getMessage());
		}
		User me = authenticationService.getAuthenticatedUser();
		if((permissionToAddEditComment==false) && me.getId().intValue() == job.getUserId().intValue() || me.getId().intValue() == job.getLab().getPrimaryUserId().intValue()){
			permissionToAddEditComment=true;
		}
		m.addAttribute("permissionToAddEditComment", permissionToAddEditComment);
	}
	
	@RequestMapping(value="/{jobId}/comments", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobCommentsPostPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam("comment") String comment,
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		comment = comment==null?"":comment.trim();  //StringEscapeUtils.escapeXml(comment.trim());//any standard html/xml [Supports only the five basic XML entities (gt, lt, quot, amp, apos)] will be converted to characters like &gt; //http://commons.apache.org/lang/api-3.1/org/apache/commons/lang3/StringEscapeUtils.html#escapeXml%28java.lang.String%29

		if("".equals(comment)){
			m.addAttribute("errorMessage", messageService.getMessage("jobComment.jobCommentEmpty.error"));
		}
		else{
			try{
				if(authenticationService.hasPermission("hasRole('su') or hasRole('fm') or hasRole('ft') or hasRole('da-*')")){
					jobService.setFacilityJobComment(jobId, comment);
				}
				else{
					jobService.setUserSubmittedJobComment(jobId, comment);
				}
				m.addAttribute("successMessage", messageService.getMessage("jobComment.jobCommentAdded.label"));
			}catch(Exception e){
				String errorMessage = messageService.getMessage("jobComment.jobCommentCreate.error");
				logger.warn(errorMessage);
				m.addAttribute("errorMessage", errorMessage);
			}
		}
		populateComments(job, m);
		return "job/home/comments";
	}
	
	@RequestMapping(value="/{jobId}/fileUploadManager", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobFileUploadPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage,
			  @RequestParam(value="successMessage", required=false) String successMessage,
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		populateFileUploadPage(job, m);
		return "job/home/fileUploadManager";
	}

	private void populateFileUploadPage(Job job, ModelMap m){
		
		m.addAttribute("job", job);
		
		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
		for(JobFile jf: job.getJobFile()){
			FileGroup fileGroup = jf.getFile();//returns a FileGroup
			//exclude quotes and invoices (as of 8/22/13, no longer needed as these files are stored through acctQuoteMeta or acctInvoiceMeta
			//if(fileGroup.getDescription().toLowerCase().startsWith("job"+job.getId()+"_quote_")||fileGroup.getDescription().toLowerCase().startsWith("job"+job.getId()+"_invoice_")){
			//	continue;
			//}
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
	}
	
	//Note: we use MultipartHttpServletRequest to be able to upload files using Ajax. See http://hmkcode.com/spring-mvc-upload-file-ajax-jquery-formdata/
	@RequestMapping(value="/{jobId}/fileUploadManager", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobFileUploadPostPage(@PathVariable("jobId") Integer jobId,
			  MultipartHttpServletRequest request, 
			  HttpServletResponse response,
			  //since this is now an ajax call, we no longer need/use @RequestParam("file_description") String fileDescription, @RequestParam("file_upload") MultipartFile mpFile,
			  ModelMap m) throws SampleTypeException {
	
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}

		List<MultipartFile> mpFiles = request.getFiles("file_upload");
	    if(mpFiles.isEmpty()){
	    	String errorMessage = messageService.getMessage("listJobSamples.fileUploadFailed_fileEmpty.error");
	    	logger.warn(errorMessage);
	    	m.addAttribute("errorMessage", errorMessage);
	    	populateFileUploadPage(job, m);
			return "job/home/fileUploadManager";
	    }
	   	MultipartFile mpFile = mpFiles.get(0);
	   	if(mpFile==null){
	   		String errorMessage = messageService.getMessage("listJobSamples.fileUploadFailed_fileEmpty.error");
	    	logger.warn(errorMessage);
	    	m.addAttribute("errorMessage", errorMessage);
	    	populateFileUploadPage(job, m);
			return "job/home/fileUploadManager";
		}
		
	   	String fileDescription = request.getParameter("file_description");
	    fileDescription = fileDescription==null?"":fileDescription.trim();
	    
	    if("".equals(fileDescription)){
	    	String errorMessage = messageService.getMessage("listJobSamples.fileUploadFailed_fileDescriptionEmpty.error");
	    	logger.warn(errorMessage);
	    	m.addAttribute("errorMessage", errorMessage);
	    	populateFileUploadPage(job, m);
			return "job/home/fileUploadManager";
	    }	    
	   			
		Random randomNumberGenerator = new Random(System.currentTimeMillis());
		try{
			fileService.uploadJobFile(mpFile, job, fileDescription, randomNumberGenerator);//will upload and perform all database updates
			m.addAttribute("successMessage", messageService.getMessage("listJobSamples.fileUploadedSuccessfully.label"));
		} catch(FileUploadException e){
			String errorMessage = messageService.getMessage("listJobSamples.fileUploadFailed.error");
			logger.warn(errorMessage);
			m.addAttribute("errorMessage", errorMessage);
		}
		populateFileUploadPage(job, m);
		return "job/home/fileUploadManager";
	}
	
	@RequestMapping(value="/{jobId}/saveQuote", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')")
	public void jobSaveQuote(@PathVariable("jobId") Integer jobId,
			  @RequestParam("submittedObjectId") List<Integer> submittedObjectIdList,
			  ModelMap m, HttpServletRequest request, HttpServletResponse response) throws SampleTypeException {

		previewOrSaveQuote(jobId, submittedObjectIdList, m, request, response, "save");
			
	}
	
	@RequestMapping(value="/{jobId}/previewQuote", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')")
	public void jobPreviewQuote(@PathVariable("jobId") Integer jobId,
			  @RequestParam("submittedObjectId") List<Integer> submittedObjectIdList,
			  ModelMap m, HttpServletRequest request, HttpServletResponse response) throws SampleTypeException {

		previewOrSaveQuote(jobId, submittedObjectIdList, m, request, response, "preview");
	}
	
	public static final Font BIG_BOLD =  new Font(FontFamily.TIMES_ROMAN, 13, Font.BOLD );
	public static final Font NORMAL =  new Font(FontFamily.TIMES_ROMAN, 11 );
	public static final Font NORMAL_BOLD =  new Font(FontFamily.TIMES_ROMAN, 11, Font.BOLD );
	public static final Font TINY_BOLD =  new Font(FontFamily.TIMES_ROMAN, 8, Font.BOLD );
	
	private void previewOrSaveQuote(Integer jobId, List<Integer> submittedObjectIdList, ModelMap m, 
			  HttpServletRequest request, HttpServletResponse response, String previewOrSave) throws SampleTypeException {
		
		String headerHtml = "<html><body><h2 style='color:red;font-weight:bold;'>Errors Detected</h2>";
		String errorMessage = "";
		String footerHtml = "<br /></body></html>";
		
		//Firstly deal with any unexpected errors (sections a-e).
		//a. here deal with unexpected job error (ie.: not found in database)
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){//inform user and get out of here
			errorMessage += "<br />"+messageService.getMessage("job.jobUnexpectedlyNotFound.error");
		   	logger.warn(errorMessage);
		   	try{
		   			response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
		   			return;
		   	}catch(Exception e){logger.warn(e.getMessage()); return; }
		}
		
		//b. here deal with unexpected submitted sample errors (sample not in database; sample not in job, both very unexpected)
		List<Sample>  allJobSamplesList = job.getSample();//all samples in this job (from the database)
		
		List<Sample> submittedObjectList = new ArrayList<Sample>();
		for(Integer submittedObjectId : submittedObjectIdList){
			Sample submittedObject = sampleService.getSampleById(submittedObjectId);
			if(submittedObject.getId()==null){
				errorMessage += "<br />Unexpected Problem: Submitted sample (ID: " + submittedObjectId.intValue() + ") unexpectedly not found in database";
			}
			else if(!allJobSamplesList.contains(submittedObject)){
				errorMessage += "<br />Unexpected Problem: Submitted sample (ID: " + submittedObjectId.intValue() + ") unexpectedly not part of this job";
			}			
			submittedObjectList.add(submittedObject);		}
		
		//c. here get the runCost parameters and check for highly unexpected errors
		String param = "runCostMachine";
		String [] runCostMachineArray = request.getParameterValues(param);
		param = "runCostReadLength";
		String [] runCostReadLengthArray = request.getParameterValues(param);
		param = "runCostReadType";
		String [] runCostReadTypeArray = request.getParameterValues(param);
		param = "runCostNumberLanes";
		String [] runCostNumberLanesArray = request.getParameterValues(param);
		param = "runCostPricePerLane";
		String [] runCostPricePerLaneArray = request.getParameterValues(param);	
		if(runCostMachineArray==null || runCostReadLengthArray==null || runCostReadTypeArray==null || 
				runCostNumberLanesArray==null || runCostPricePerLaneArray==null){
			errorMessage = "Unexpected problem interpreting sequence run information";
			try{
		   		response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
		   		return;
			}catch(Exception e){logger.warn(e.getMessage()); return;}
		}			
		int numberOfRunRows = runCostMachineArray.length;
		if(runCostReadLengthArray.length != numberOfRunRows && runCostReadTypeArray.length != numberOfRunRows  &&
				runCostNumberLanesArray.length != numberOfRunRows && runCostPricePerLaneArray.length != numberOfRunRows ){
			errorMessage = "Unexpected problem interpreting sequence run information";
			try{
				response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
				return;
			}catch(Exception e){logger.warn(e.getMessage()); return;}
		}
		
		//d. here get the additionalCost parameters and check for highly unexpected errors
		param = "additionalCostReason";
		String [] additionalCostReasonArray = request.getParameterValues(param);
		param = "additionalCostUnits";
		String [] additionalCostUnitsArray = request.getParameterValues(param);
		param = "additionalCostPricePerUnit";
		String [] additionalCostPricePerUnitArray = request.getParameterValues(param);
		if(additionalCostReasonArray==null || additionalCostUnitsArray==null || additionalCostPricePerUnitArray==null){
			errorMessage = "Unexpected problem interpreting additional cost information";
			try{
		   		response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
		   		return;
			}catch(Exception e){logger.warn(e.getMessage()); return;}
		}			
		int numberOfAdditionalCostRows = additionalCostReasonArray.length;
		if(additionalCostUnitsArray.length != numberOfAdditionalCostRows && additionalCostPricePerUnitArray.length != numberOfAdditionalCostRows){
			errorMessage = "Unexpected problem interpreting additional cost information";
			try{
		   		response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
		   		return;
			}catch(Exception e){logger.warn(e.getMessage()); return;}
		}
		
		//e. here get the discount/credit parameters and check for highly unexpected errors
		param = "discountReason";
		String [] discountReasonArray = request.getParameterValues(param);
		param = "discountType";//to distinguish whether discount is supplied as $ or %
		String [] discountTypeArray = request.getParameterValues(param);
		param = "discountValue";
		String [] discountValueArray = request.getParameterValues(param);
		if(discountReasonArray==null || discountTypeArray==null || discountValueArray==null){
			errorMessage = "Unexpected problem interpreting discount/credit information";
			try{
				response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
				return;
			}catch(Exception e){logger.warn(e.getMessage()); return;}
		}			
		int numberOfDiscountRows = discountReasonArray.length;
		if(discountTypeArray.length != numberOfDiscountRows && discountValueArray.length != numberOfDiscountRows){
			errorMessage = "Unexpected problem interpreting discount/credit information";
			try{
		   		response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
		   		return;
			}catch(Exception e){logger.warn(e.getMessage()); return;}
		}
		//if the job was found in the database, but there was some other unexpected error, inform user and get out of here.		
		if(!"".equals(errorMessage)){
			logger.warn(errorMessage);
		   	try{
		   		response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
		   		return;
		   	}catch(Exception e){logger.warn(e.getMessage()); return;}
		}
		
		//f. get comments, if any
		param = "comments";
		String [] commentsArray = request.getParameterValues(param);
		List<String> commentsList = new ArrayList<String>();
		int numberOfCommentsRows = commentsArray.length;
		for(int i = 0; i < numberOfCommentsRows; i++){
			if(!"".equals(commentsArray[i].trim())){
				commentsList.add(commentsArray[i].trim());
			}
		}
				
		//Secondly, deal with expected errors (person left out data on the form or filled in a letter when a number was needed)
		//1. library construction costs (no empty entries permitted if the submitted sample is a library; must enter 0 if no charge for that library).
		Map<Sample, Integer> submittedObjectLibraryCostMap = new HashMap<Sample, Integer>();
		for(Sample submittedObject : submittedObjectList){
			String param2 = "libraryCost_"+submittedObject.getId().intValue();
			String value = request.getParameter(param2);
			
			if(value != null){
				value = value.trim();
			}
			
			if(value==null || "".equals(value)){
				errorMessage += "<br />No library cost provided for sample " + submittedObject.getName() + "; whole numbers only; if no charge, enter zero";
				continue;
			}
			
			if(value.matches("\\D")){//any character in string that is not a digit 
				errorMessage += "<br />Invalid library cost ("+ value + ") provided for sample " + submittedObject.getName() + "; whole numbers only; if no charge, enter zero";
			}
			else{				
				try{					
					Integer libCost = new Integer(value);
					submittedObjectLibraryCostMap.put(submittedObject, libCost);
				}catch(Exception e){errorMessage += "<br />Invalid library cost ("+ value + ") provided for sample " + submittedObject.getName() + "; whole numbers only; if no charge, enter zero";}
			}
		}
				
		//2. sequence runs and costs (optional, so an empty row is allowed; it just won't be used in any way)		
		boolean [] runRowIsCompletelyEmpty = new boolean[numberOfRunRows];
		boolean [] runRowIsMissingSomething = new boolean[numberOfRunRows];
		boolean [] runRowIsComplete = new boolean[numberOfRunRows];
		int numberRunRowsCompletelyEmpty = 0;
		int numberRunRowsMissingSomething = 0;
		int numberRunRowsComplete = 0;
		
		List<String> machineList = new ArrayList<String>();
		List<String> readLengthList = new ArrayList<String>();
		List<String> readTypeList = new ArrayList<String>();
		List<Integer> numLanesList = new ArrayList<Integer>();
		List<Integer> pricePerLaneList = new ArrayList<Integer>();
		
		for(int i = 0; i < numberOfRunRows; i++){ 
			if( "".equals(runCostMachineArray[i].trim())	 &&
				"".equals(runCostReadLengthArray[i].trim())	 &&
				"".equals(runCostReadTypeArray[i].trim())	 &&
				"".equals(runCostNumberLanesArray[i].trim()) &&
				"".equals(runCostPricePerLaneArray[i].trim()) ){
				numberRunRowsCompletelyEmpty++;
				numberRunRowsMissingSomething++;
				runRowIsCompletelyEmpty[i]=true;
				runRowIsMissingSomething[i]=true;
				runRowIsComplete[i]=false;
				continue;
			}
			if( "".equals(runCostMachineArray[i].trim())    ||
				"".equals(runCostReadLengthArray[i].trim())	||
				"".equals(runCostReadTypeArray[i].trim())	||
				"".equals(runCostNumberLanesArray[i].trim()) ||
				"".equals(runCostPricePerLaneArray[i].trim()) ){
					errorMessage += "<br />Row " +(i+1)+ " in Sequence Run section is missing information - Please review";
			}
			Integer numLanes=null;
			Integer pricePerLane=null;
			try{
				if(!"".equals(runCostNumberLanesArray[i].trim())){
					numLanes = new Integer(runCostNumberLanesArray[i].trim());
				}
			}catch(Exception e){errorMessage += "<br />Row " +(i+1)+ " in Sequence Run section is missing information - whole number required for no. lanes";}
			try{
				if(!"".equals(runCostPricePerLaneArray[i].trim())){
					pricePerLane = new Integer(runCostPricePerLaneArray[i].trim());
				}
			}catch(Exception e){errorMessage += "<br />Row " +(i+1)+ " in Sequence Run section is missing information - whole number required for cost/lane; if no charge, enter zero";}
			if(!"".equals(errorMessage)){
				numberRunRowsMissingSomething++;
				runRowIsCompletelyEmpty[i]=false;
				runRowIsMissingSomething[i]=true;
				runRowIsComplete[i]=false;
			}
			else{
				numberRunRowsComplete++;
				runRowIsCompletelyEmpty[i]=false;
				runRowIsMissingSomething[i]=false;
				runRowIsComplete[i]=true;
				machineList.add(runCostMachineArray[i].trim());
				readLengthList.add(runCostReadLengthArray[i].trim());
				readTypeList.add(runCostReadTypeArray[i].trim());
				numLanesList.add(numLanes); 
				pricePerLaneList.add(pricePerLane);
			}
		}
		
		//3. additional costs (optional, so empty rows allowed; it just won't be used in any way)
		boolean [] additionalCostsCompletelyEmpty = new boolean[numberOfAdditionalCostRows];
		boolean [] additionalCostsIsMissingSomething = new boolean[numberOfAdditionalCostRows];
		boolean [] additionalCostsIsComplete = new boolean[numberOfAdditionalCostRows];
		int numberAdditionalCostsCompletelyEmpty = 0;
		int numberAdditionalCostsMissingSomething = 0;
		int numberAdditionalCostsComplete = 0;
		
		List<String> additionalCostReasonList = new ArrayList<String>();
		List<Integer> additionalCostUnitsList = new ArrayList<Integer>();
		List<Integer> additionalCostPricePerUnitList = new ArrayList<Integer>();

		for(int i = 0; i < numberOfAdditionalCostRows; i++){
			if( "".equals(additionalCostReasonArray[i].trim())	 &&
				"".equals(additionalCostUnitsArray[i].trim())	 &&
				"".equals(additionalCostPricePerUnitArray[i].trim()) ){
					numberAdditionalCostsCompletelyEmpty++;
					numberAdditionalCostsMissingSomething++;
					additionalCostsCompletelyEmpty[i]=true;
					additionalCostsIsMissingSomething[i]=true;
					additionalCostsIsComplete[i]=false;
					continue;
			}
			else if( "".equals(additionalCostReasonArray[i].trim())	 ||
					 "".equals(additionalCostUnitsArray[i].trim())	 ||
					 "".equals(additionalCostPricePerUnitArray[i].trim()) ){
						errorMessage += "<br />Row "+(i+1) + " in Additional Costs section is missing information - Please review";
			}
			Integer numUnits = null;
			Integer pricePerUnit = null;
			try{
				if(!"".equals(additionalCostUnitsArray[i].trim())){
					numUnits = new Integer(additionalCostUnitsArray[i].trim());
				}
			}catch(Exception e){errorMessage += "<br />Row "+(i+1) + " in Additional Costs section is missing information - whole number required for units";}
			try{
				if(!"".equals(additionalCostPricePerUnitArray[i].trim())){
					pricePerUnit = new Integer(additionalCostPricePerUnitArray[i].trim());
				}
			}catch(Exception e){errorMessage += "<br />Row "+(i+1) + " in Additional Costs section is missing information - whole number required for cost/unit; if no charge, enter zero";}
			if(!"".equals(errorMessage)){
				numberAdditionalCostsMissingSomething++;
				additionalCostsCompletelyEmpty[i]=false;
				additionalCostsIsMissingSomething[i]=true;
				additionalCostsIsComplete[i]=false;
			}
			else{
				numberAdditionalCostsComplete++;
				additionalCostsCompletelyEmpty[i]=false;
				additionalCostsIsMissingSomething[i]=false;
				additionalCostsIsComplete[i]=true;
				additionalCostReasonList.add(additionalCostReasonArray[i]);
				additionalCostUnitsList.add(numUnits);
				additionalCostPricePerUnitList.add(pricePerUnit);
			}
		}
				
		//4. discounts/credits (optional, so empty rows allowed; it just won't be used in any way)		
		boolean [] discountsCompletelyEmpty = new boolean[numberOfDiscountRows];
		boolean [] discountsIsMissingSomething = new boolean[numberOfDiscountRows];
		boolean [] discountsIsComplete = new boolean[numberOfDiscountRows];
		int numberDiscountsCompletelyEmpty = 0;
		int numberDiscountsMissingSomething = 0;
		int numberDiscountsComplete = 0;

		int cumulativePercentDiscount = 0;
 	    List<String> discountReasonList = new ArrayList<String>();
 	    Map<String, Integer> discountReasonAbsolutePriceMap = new HashMap<String, Integer>();
 	    Map<String, Integer> discountReasonPercentMap = new HashMap<String, Integer>();

 		String currencyIcon = Currency.getInstance(Locale.getDefault()).getSymbol();//+String.format("%.2f", price));

 		for(int i = 0; i < numberOfDiscountRows; i++){
			if( "".equals(discountReasonArray[i].trim())	 &&
				"".equals(discountTypeArray[i].trim())	 &&
				"".equals(discountValueArray[i].trim()) ){
					numberDiscountsCompletelyEmpty++;
					numberDiscountsMissingSomething++;
					discountsCompletelyEmpty[i]=true;
					discountsIsMissingSomething[i]=true;
					discountsIsComplete[i]=false;
					continue;
			}
			else if( "".equals(discountReasonArray[i].trim())	 ||
					 "".equals(discountTypeArray[i].trim())	 ||
					 "".equals(discountValueArray[i].trim()) ){
						errorMessage += "<br />Row "+(i+1) + " in Discount/Credit section is missing information - Please review";
			}
			if(!"".equals(discountTypeArray[i].trim())){
					if( !currencyIcon.equals(discountTypeArray[i].trim()) && !"%".equals(discountTypeArray[i].trim())){
						errorMessage += "<br />Row "+(i+1) + " in Discount/Credit section is missing information - you must select either " + currencyIcon + " or %";
				}
			}
			Integer discountValue=null;
			try{
				if(!"".equals(discountValueArray[i].trim())){
					discountValue = new Integer(discountValueArray[i].trim());
					if("%".equals(discountTypeArray[i].trim())){
						cumulativePercentDiscount += discountValue;
						if(discountValue >100){
							errorMessage += "<br />Row "+(i+1) + " in Discount/Credit section cannot be greater than 100% - please modify or remove";
						}
					}										
				}
			}catch(Exception e){errorMessage += "<br />Row "+(i+1) + " in Discount/Credit section is missing information - enter a whole number for discount; no fractions allowed (example: enter 25 for 25%)";}
			
			if(discountReasonList.contains(discountReasonArray[i].trim())){
				errorMessage += "<br />Row "+(i+1) + " in Discount/Credit section: Any particular reason for a Discount/Credit may be used only once. ";
			}
			
			if(!"".equals(errorMessage)){
				numberDiscountsMissingSomething++;
				discountsCompletelyEmpty[i]=false;
				discountsIsMissingSomething[i]=true;
				discountsIsComplete[i]=false;
			}
			else{
				numberDiscountsComplete++;
				discountsCompletelyEmpty[i]=false;
				discountsIsMissingSomething[i]=false;
				discountsIsComplete[i]=true;
				discountReasonList.add(discountReasonArray[i].trim());
				if("%".equals(discountTypeArray[i].trim())){
					discountReasonPercentMap.put(discountReasonArray[i].trim(), discountValue);
				}
				else{
					discountReasonAbsolutePriceMap.put(discountReasonArray[i].trim(), discountValue);
				}
			}
		}
 		if(cumulativePercentDiscount>100){
			errorMessage += "<br />Cumulative Discount Percent may not exceed 100%";
		}
 				
 		//if the user input contains errors, inform user and get out of here.
		if(!"".equals(errorMessage)){
			logger.warn(errorMessage);
		   	try{
		   		response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
		   		return;
		   	}catch(Exception e){logger.warn(e.getMessage()); return;}
		}
		
		//all ok, so develop the pdf 
		try{
			Date now = new Date();
			
			OutputStream outputStream = null;
			File localFile = null;
			if("preview".equalsIgnoreCase(previewOrSave)){
				response.setContentType("application/pdf");			
				outputStream = response.getOutputStream();	
			}
			else if ("save".equalsIgnoreCase(previewOrSave)){
				localFile = fileService.createTempFile();
				outputStream = new FileOutputStream(localFile);
			}
	 	    Document document = new Document();
	 	    PdfWriter.getInstance(document, outputStream).setInitialLeading(10);
	 	    document.open();	 	    
	 	    List<String> justUnderLetterheadLineList = new ArrayList<String>();
	 	    justUnderLetterheadLineList.add("Shahina Maqbool PhD (ESF Director), Albert Einstein College of Medicine, 1301 Morris Park Ave (Price 159F)");
	 	    justUnderLetterheadLineList.add("Email:shahina.maqbool@einstein.yu.edu Phone:718-678-1163");
	 	    String imageLocation = "/Users/robertdubin/Documents/images/Einstein_Logo.png";
	 	    String title = "Epigenomics Shared Facility";
	 	    addLetterhead(document, imageLocation, title, justUnderLetterheadLineList);
	 	    DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
	 	    addNoteLine(document, "", dateFormat.format(now));
	 	    addressTheLetterToSubmitterAndPI(document, job);
	 	    addNoteLine(document, "Re.: ", "Estimated costs for Job ID " + job.getId());
	 	    document.add(new LineSeparator());
	 	    Paragraph jobDetailsParagraph = startJobDetailsParagraphAndAddCommonJobDetails(job);//start new paragraph containing common job details (put the paragraph is NOT added to the document in this method, thus permitting more to be added to it)
	 	    jobDetailsParagraph = addMPSDetailsToJobDetailsParagraph(job, jobDetailsParagraph);//add msp-specific info to the jobDetails paragraph
	 	    document.add(jobDetailsParagraph);//add the paragraph to the document
	 	    
	 	    Integer libraryConstructionTotalCost = addSubmittedSamplesMultiplexRequestAndLibraryCostsAsTable(document, job, submittedObjectList, submittedObjectLibraryCostMap);
	 	    Integer sequenceRunsTotalCost = addSequenceRunsAndCostAsTable(document, job, machineList, readLengthList, readTypeList, numLanesList, pricePerLaneList);
	 	    Integer additionalTotalCost = addAdditionalCostsAsTable(document, job, additionalCostReasonList, additionalCostUnitsList, additionalCostPricePerUnitList);
	 	    
	 	    addCommentsAsTable(document, job, commentsList);
	 	    
	 	    List<String> costReasonList = new ArrayList<String>();
	 	    Map<String, Integer> costReasonPriceMap = new HashMap<String, Integer>();
	 	    costReasonList.add("Total Library Costs");
	 	    costReasonPriceMap.put("Total Library Costs",libraryConstructionTotalCost);
	 	    costReasonList.add("Total Sequencing Costs");
	 	    costReasonPriceMap.put("Total Sequencing Costs",sequenceRunsTotalCost);
	 	    if(additionalTotalCost>0){
	 	    	costReasonList.add("Total Additional Costs");
	 	    	costReasonPriceMap.put("Total Additional Costs",additionalTotalCost);
	 	    } 
	 	    Integer totalFinalCost = addCostSummaryTable(document, job, costReasonList, costReasonPriceMap, discountReasonList, discountReasonAbsolutePriceMap, discountReasonPercentMap);

	 	    document.close();
	 	    
	 	    if ("preview".equalsIgnoreCase(previewOrSave)){
	 	    	outputStream.close();//apparently not really needed here but doesn't hurt
	 	    	return;//nothing apparently needs to be done for it to work fine
	 	    }
	 	    else if ("save".equalsIgnoreCase(previewOrSave)){
	 		   outputStream.close();//file has been save to local location
	 		   
	 		   //if new quote, save the file to remote location and create new acctQuote record
	 		   jobService.createNewQuoteAndSaveQuoteFile(job, localFile, new Float(totalFinalCost));
	 		   
	 		   
	 		   
	 		   //next, save the file to remote location and record in database
/*	 		   
	 		   DateFormat dateFormat2 = new SimpleDateFormat("yyyy_MM_dd");		 	   
		 	   Random randomNumberGenerator = new Random(System.currentTimeMillis());
		 	   
		 	   //if this is a new quote, save quote; if invoice, save invoice
		 	   fileService.saveLocalJobFile(job, localFile, "Job"+job.getId()+"_Quote_"+dateFormat2.format(now)+".pdf", "Job"+job.getId()+"_Quote_"+dateFormat2.format(now), randomNumberGenerator);

		 	   
		 	   //if this is a new quote, save quote; if invoice, save invoice
		 	   AcctQuote acctQuote = new AcctQuote();
		 	   acctQuote.setAmount((float)(totalFinalCost.intValue()));		 	   
		 	   jobService.addNewQuote(jobId, acctQuote, new ArrayList<AcctQuoteMeta>());
*/		 	   
		 	   response.setContentType("text/html"); 
		 	   String headerHtml2 = "<html><body>";
		 	   String successMessage = "<h2 style='color:blue;font-weight:bold;'>Your New File Has Been Saved</h2>";
		 	   String footerHtml2 = "<br /></body></html>";
		 	   response.getOutputStream().print(headerHtml2+successMessage+footerHtml2);
		 	   return;
	 	    }	 	    
		}catch(Exception e){
			errorMessage = "Major problems encountered while creating file";
			logger.warn(errorMessage);
			try{
				response.setContentType("text/html"); response.getOutputStream().print(headerHtml+errorMessage+footerHtml);
				return;
			}catch(Exception e2){logger.warn(e.getMessage()); return;}
		}		
	}
	
	private void addLetterhead(Document document, String imageLocation, String title, List<String> justUnderLetterheadLineList) throws DocumentException{
		
		 try{
	 	    	Image image = Image.getInstance(imageLocation);
	 	    	if(image != null){
	 	    		image.setAlignment(Image.MIDDLE);
	 	    		image.scaleToFit(1000, 50);//72 is about 1 inch in height
	 	    		document.add(image);
	 	    	}
	 	    }catch(Exception e){}
	 	    
	 	    Paragraph letterTitle = new Paragraph();
	 	    letterTitle.add(new Chunk(title, BIG_BOLD));
	 	    document.add(letterTitle);	 	    
	 	      	      
	 	    LineSeparator line = new LineSeparator(); 
	 	    line.setOffset(new Float(-5.0));
	 	    document.add(line);

	 	    if(!justUnderLetterheadLineList.isEmpty()){
	 	    	Paragraph justUnderLetterhead = new Paragraph(); 
	 	    	justUnderLetterhead.setSpacingBefore(4);
	 	    	justUnderLetterhead.setSpacingAfter(15);
	 	    	justUnderLetterhead.setLeading(10);
	 	    	for(String text : justUnderLetterheadLineList){
	 	    		Chunk textUnderTheLetterheadLine = new Chunk(text, TINY_BOLD);
	 	    		justUnderLetterhead.add(textUnderTheLetterheadLine);
	 	    		justUnderLetterhead.add(Chunk.NEWLINE);
	 	    	}
		 	/*
	 	    Chunk facilityManagerNameAndAddress = new Chunk("Shahina Maqbool PhD (ESF Director), Albert Einstein College of Medicine, 1301 Morris Park Ave (Price 159F)", TINY_BOLD);
	 	    facilityManager.add(facilityManagerNameAndAddress);
		 	facilityManager.add(Chunk.NEWLINE);
	 	    Chunk facilityManagerEmailPhone = new Chunk("Email:shahina.maqbool@einstein.yu.edu Phone:718-678-1163", TINY_BOLD);
	 	    facilityManager.add(facilityManagerEmailPhone);
	 	    facilityManager.add(Chunk.NEWLINE);
	 	    */
	 	    	justUnderLetterhead.setAlignment(Element.ALIGN_CENTER);
	 	    	document.add(justUnderLetterhead);
	 	    }
	}
	
	private void addressTheLetterToSubmitterAndPI(Document document, Job job) throws DocumentException, MetadataException{
		User submitter = job.getUser();
		List<UserMeta> userMetaList = submitter.getUserMeta();
		String submitterTitle = MetaHelper.getMetaValue("user", "title", userMetaList);
		String submitterInstitution = MetaHelper.getMetaValue("user", "institution", userMetaList);
		String submitterBuildingRoom = MetaHelper.getMetaValue("user", "building_room", userMetaList);
		String submitterAddress = MetaHelper.getMetaValue("user", "address", userMetaList);
		String submitterCity = MetaHelper.getMetaValue("user", "city", userMetaList);
		String submitterState = MetaHelper.getMetaValue("user", "state", userMetaList);
		String submitterCountry = MetaHelper.getMetaValue("user", "country", userMetaList);
		String submitterZip = MetaHelper.getMetaValue("user", "zip", userMetaList);
		String submitterPhone = MetaHelper.getMetaValue("user", "phone", userMetaList);		
		
		Lab lab = job.getLab();
		String labDepartment = lab.getDepartment().getName();//Genetics, Internal, External, Cell Biology (External means not Einstein, and used for pricing)
		String pricingSchedule = "Internal";
		if(labDepartment.equalsIgnoreCase("external")){
			pricingSchedule = "External";
		}
		
		User pI = lab.getUser();
		if(submitter.getId().intValue()!=pI.getId().intValue()){
			List<UserMeta> pIMetaList = pI.getUserMeta();
			String pITitle = MetaHelper.getMetaValue("user", "title", pIMetaList);
			String pIInstitution = MetaHelper.getMetaValue("user", "institution", pIMetaList);
			String pIBuildingRoom = MetaHelper.getMetaValue("user", "building_room", pIMetaList);
			String pIAddress = MetaHelper.getMetaValue("user", "address", pIMetaList);
			String pICity = MetaHelper.getMetaValue("user", "city", pIMetaList);
			String pIState = MetaHelper.getMetaValue("user", "state", pIMetaList);
			String pICountry = MetaHelper.getMetaValue("user", "country", pIMetaList);
			String pIZip = MetaHelper.getMetaValue("user", "zip", pIMetaList);
			String pIPhone = MetaHelper.getMetaValue("user", "phone", pIMetaList);
			
	 	    PdfPTable toTheAttentionOftable = new PdfPTable(2);
	 	    toTheAttentionOftable.getDefaultCell().setBorder(0);
	 	    toTheAttentionOftable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	    toTheAttentionOftable.addCell(new Phrase("Submitter:", NORMAL_BOLD));
	 	    toTheAttentionOftable.addCell(new Phrase("Lab PI:", NORMAL_BOLD));
	 	  	toTheAttentionOftable.addCell(new Phrase(submitterTitle + " " + submitter.getNameFstLst(), NORMAL));
	 	  	toTheAttentionOftable.addCell(new Phrase(pITitle + " " + pI.getNameFstLst(), NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterCity + ", " + submitterState + " " + submitterCountry, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pICity + ", " + pIState + " " + pICountry, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(submitter.getEmail(), NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(pI.getEmail(), NORMAL));	 	    
	 	    toTheAttentionOftable.addCell(new Phrase(submitterPhone, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(pIPhone, NORMAL));	 	   
	 	    document.add(toTheAttentionOftable);
		}
		else{//submitter is the lab PI
	 	    PdfPTable toTheAttentionOftable = new PdfPTable(1);
	 	    toTheAttentionOftable.getDefaultCell().setBorder(0);
	 	    toTheAttentionOftable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	    toTheAttentionOftable.addCell(new Phrase("Submitter/Lab PI:", NORMAL_BOLD));
	 	  	toTheAttentionOftable.addCell(new Phrase(submitterTitle + " " + submitter.getNameFstLst(), NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterCity + ", " + submitterState + " " + submitterCountry, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(submitter.getEmail(), NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(submitterPhone, NORMAL));
	 	    document.add(toTheAttentionOftable);				
		}
	}
	
	private void addNoteLine(Document document, String reason, String theReason) throws DocumentException{
 	    Paragraph reasonForDocument = new Paragraph();
 	    reasonForDocument.setSpacingBefore(15);
 	    reasonForDocument.setSpacingAfter(15);
 	    reasonForDocument.add(new Chunk(reason, NORMAL_BOLD));
 	    reasonForDocument.add(new Phrase(theReason, NORMAL));
 	    document.add(reasonForDocument);
	}
	
	private Paragraph startJobDetailsParagraphAndAddCommonJobDetails(Job job){
 	    Paragraph commonJobDetailsParagraph = new Paragraph();
 	    commonJobDetailsParagraph.setSpacingBefore(15);
 	    commonJobDetailsParagraph.setSpacingAfter(5);
 	   	commonJobDetailsParagraph.add(new Chunk("Job Details:", NORMAL_BOLD));commonJobDetailsParagraph.add(Chunk.NEWLINE);
 	  	commonJobDetailsParagraph.setLeading(15);
 		commonJobDetailsParagraph.add(new Phrase("Job ID: " + job.getId(), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
 		commonJobDetailsParagraph.add(new Phrase("Job Name: " + job.getName(), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);	 	 	
 	    SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy"); //("yyyy/MM/dd");
 	    commonJobDetailsParagraph.add(new Phrase("Submitted: " + formatter.format(job.getCreated()), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
 	 	//if(job is completed????){
 	 	//	commonJobDetails.add(new Phrase("Completed: " + formatter.format(get the date, ask andy how), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
 	 	//}
 	    commonJobDetailsParagraph.add(new Phrase("Assay: " + job.getWorkflow().getName(), NORMAL));commonJobDetailsParagraph.add(Chunk.NEWLINE);
 	    return commonJobDetailsParagraph;
	}
	
	private Paragraph addMPSDetailsToJobDetailsParagraph(Job job, Paragraph jobDetailsParagraph){
		
		List<JobResourcecategory> jobResourcecategoryList = job.getJobResourcecategory();
		StringBuffer jobMachineListSB = new StringBuffer();
		int count = 0;
 	 	for(JobResourcecategory jrc: jobResourcecategoryList){
 	 		if(count==0){
 	 			jobMachineListSB.append(jrc.getResourceCategory().getName());
 	 		}else{ jobMachineListSB.append(", ").append(jrc.getResourceCategory().getName()); }
 	 		count++;	 	 		
 	 	}
		String jobMachineList = new String(jobMachineListSB);
		List<JobMeta> jobMetaList = job.getJobMeta();
		String readLength = null;
		String readType = null;
		for(JobMeta jm : jobMetaList){
			if(jm.getK().toLowerCase().indexOf("readlength")>-1){
				readLength = jm.getV();
			}
			else if(jm.getK().toLowerCase().indexOf("readtype")>-1){
				readType = jm.getV();
			}
		}
		
		int numberOfLanesRequested = job.getJobCellSelection().size();
		String platform = jobResourcecategoryList.size()==1?"Platform: ":"Platforms: ";
		jobDetailsParagraph.add(new Phrase(platform + jobMachineList, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
 	 	if(readType!=null){
 	 		jobDetailsParagraph.add(new Phrase("Read Type: " + readType, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
 	 	}
 	 	if(readLength!=null){
 	 		jobDetailsParagraph.add(new Phrase("Read Length: " + readLength, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
 	 	}
 	 	if(numberOfLanesRequested>0){
 	 		jobDetailsParagraph.add(new Phrase("Lanes Requested: " + numberOfLanesRequested, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
 	 	}
 	 	jobDetailsParagraph.add(new Phrase("Samples: " + job.getSample().size(), NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
 	 	
 	 	/* Simply let the user select any appropriate discounts
 	 	Lab lab = job.getLab();
		String labDepartment = lab.getDepartment().getName();//Genetics, Internal, External, Cell Biology (External means not Einstein, and used for pricing)
		String pricingSchedule = "Internal";
		if(labDepartment.equalsIgnoreCase("external")){
			pricingSchedule = "External";
		} 	 	
 	 	jobDetailsParagraph.add(new Phrase("Pricing Schecule: " + pricingSchedule, NORMAL));jobDetailsParagraph.add(Chunk.NEWLINE);
 	 	*/
		
		return jobDetailsParagraph;
	}
	
	private Integer addSubmittedSamplesMultiplexRequestAndLibraryCostsAsTable(Document document, Job job, List<Sample> submittedSampleList, Map<Sample, Integer> submittedObjectLibraryCostMap) throws DocumentException{
		
 	 	Paragraph sampleLibraryTitle = new Paragraph();
 	 	sampleLibraryTitle.setSpacingBefore(5);
 	 	sampleLibraryTitle.setSpacingAfter(5);
 	 	sampleLibraryTitle.add(new Chunk("Submitted Samples, Lane/Multiplex Request & Library Construction Costs:", NORMAL_BOLD));
 	 	document.add(sampleLibraryTitle);
 	 	
 	 	PdfPTable sampleLibraryTable = new PdfPTable(5);
 	 	sampleLibraryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
 	 	sampleLibraryTable.setWidths(new float[]{0.3f, 2f, 0.6f, 1f, 1f});
 		PdfPCell cellNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
 		cellNo.setHorizontalAlignment(Element.ALIGN_CENTER);
 		sampleLibraryTable.addCell(cellNo);
 		PdfPCell cellSample = new PdfPCell(new Phrase("Sample", NORMAL_BOLD));
 		cellSample.setHorizontalAlignment(Element.ALIGN_CENTER);
 		sampleLibraryTable.addCell(cellSample);
 		PdfPCell cellMaterial = new PdfPCell(new Phrase("Material", NORMAL_BOLD));
 		cellMaterial.setHorizontalAlignment(Element.ALIGN_CENTER);
 		sampleLibraryTable.addCell(cellMaterial);
 		PdfPCell runOnLane = new PdfPCell(new Phrase("Run On Lane(s)", NORMAL_BOLD));
 		runOnLane.setHorizontalAlignment(Element.ALIGN_CENTER);
 		sampleLibraryTable.addCell(runOnLane);
 		PdfPCell libCostCell = new PdfPCell(new Phrase("Library Cost", NORMAL_BOLD));
 		libCostCell.setHorizontalAlignment(Element.ALIGN_CENTER);
 		sampleLibraryTable.addCell(libCostCell);

 		int sampleCounter = 1;
 		int cumulativeCostForAllLibraries = 0;
 		Map<Sample,String> coverageMap = jobService.getCoverageMap(job);//a user-submitted request: which samples are to be run on which lanes 
		String currencyIcon = Currency.getInstance(Locale.getDefault()).getSymbol();

 		for(Sample sample : submittedSampleList){
 			
 			sampleLibraryTable.addCell(new Phrase(""+sampleCounter, NORMAL));
 			sampleLibraryTable.addCell(new Phrase(sample.getName(), NORMAL));
 			sampleLibraryTable.addCell(new Phrase(sample.getSampleType().getName(), NORMAL));
 
 			String coverageString = coverageMap.get(sample);
 			StringBuffer runOnWhichLanesSB = new StringBuffer();
 			char testChar = '1';
 			for(int i = 0; i < coverageString.length(); i++){
 				if(coverageString.charAt(i) == testChar){//run on lane i+1
 					if(runOnWhichLanesSB.length()>0){
 						runOnWhichLanesSB.append(", " + (i+1));
 					}
 					else{
 						runOnWhichLanesSB.append(i+1);
 					}
 				}
 			}
 			String runOnWhichLanes = new String(runOnWhichLanesSB);
 			sampleLibraryTable.addCell(new Phrase(runOnWhichLanes, NORMAL));
 
 			if("library".equalsIgnoreCase(sample.getSampleType().getIName())){
 				PdfPCell cost = new PdfPCell(new Phrase("N/A", NORMAL));
 				cost.setHorizontalAlignment(Element.ALIGN_RIGHT);
 				sampleLibraryTable.addCell(cost);
 			}
 			else{
 				Integer libCost = submittedObjectLibraryCostMap.get(sample);
 				cumulativeCostForAllLibraries += libCost.intValue();
 				////sampleLibraryTable.addCell(new Phrase(currencyIcon+" "+libCost.toString(), NORMAL));
 				PdfPCell cost = new PdfPCell(new Phrase(currencyIcon+" "+libCost.toString(), NORMAL));
 				cost.setHorizontalAlignment(Element.ALIGN_RIGHT);
 				sampleLibraryTable.addCell(cost);
 			}
 			sampleCounter++;
 		}
 		
 		for(int i = 0; i < 4; i++){//4 empty cells with no border
 			PdfPCell cell = new PdfPCell(new Phrase(""));
 			cell.setBorder(Rectangle.NO_BORDER);
 			sampleLibraryTable.addCell(cell);
 		}
 		PdfPCell totalLibCost = new PdfPCell(new Phrase("Total: " + currencyIcon+" "+cumulativeCostForAllLibraries, NORMAL_BOLD));
 		totalLibCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
 		totalLibCost.setBorderWidth(2f);
 		totalLibCost.setBorderColor(BaseColor.BLACK);
		sampleLibraryTable.addCell(totalLibCost);
			
 		document.add(sampleLibraryTable);
 		return new Integer (cumulativeCostForAllLibraries);
	}
	
	private Integer addSequenceRunsAndCostAsTable(Document document, Job job, List<String> machineList, 
											List<String> readLengthList, List<String> readTypeList, 
											List<Integer> numLanesList, List<Integer> pricePerLaneList) throws DocumentException{
		
		int cumulativeCostForAllSequenceRuns = 0;
		
	 	Paragraph sequenceRunTitle = new Paragraph();
	 	sequenceRunTitle.setSpacingBefore(5);
	 	sequenceRunTitle.setSpacingAfter(5);
	 	if(machineList.size()==0){
		 	sequenceRunTitle.add(new Chunk("Sequence Runs: No Sequence Runs To Be Performed", NORMAL_BOLD));
		 	document.add(sequenceRunTitle);
		 	return cumulativeCostForAllSequenceRuns;
	 	}
	 	sequenceRunTitle.add(new Chunk("Sequence Runs And Costs:", NORMAL_BOLD));
 	 	document.add(sequenceRunTitle);
 
 	 	PdfPTable runTable = new PdfPTable(7);
 	 	runTable.setHorizontalAlignment(Element.ALIGN_LEFT);
 	 	runTable.setWidths(new float[]{0.3f, 1.1f, 0.4f, 0.5f, 0.4f, 0.6f, 0.9f});
 		PdfPCell cellRunNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
 		cellRunNo.setHorizontalAlignment(Element.ALIGN_CENTER);
 		runTable.addCell(cellRunNo);
 		PdfPCell cellMachine = new PdfPCell(new Phrase("Machine", NORMAL_BOLD));
 		cellMachine.setHorizontalAlignment(Element.ALIGN_CENTER);
 		runTable.addCell(cellMachine);
 		PdfPCell cellReadLength = new PdfPCell(new Phrase("Length", NORMAL_BOLD));
 		cellReadLength.setHorizontalAlignment(Element.ALIGN_CENTER);
 		runTable.addCell(cellReadLength);
 		PdfPCell cellReadType = new PdfPCell(new Phrase("Type", NORMAL_BOLD));
 		cellReadType.setHorizontalAlignment(Element.ALIGN_CENTER);
 		runTable.addCell(cellReadType);
 		PdfPCell cellNumLanes = new PdfPCell(new Phrase("Lanes", NORMAL_BOLD));
 		cellNumLanes.setHorizontalAlignment(Element.ALIGN_CENTER);
 		runTable.addCell(cellNumLanes);
 		PdfPCell cellPricePerLane = new PdfPCell(new Phrase("Cost/Lane", NORMAL_BOLD));
 		cellPricePerLane.setHorizontalAlignment(Element.ALIGN_CENTER);
 		runTable.addCell(cellPricePerLane);
 		PdfPCell totalPerRun = new PdfPCell(new Phrase("Cost/Run", NORMAL_BOLD));
 		totalPerRun.setHorizontalAlignment(Element.ALIGN_CENTER);
 		runTable.addCell(totalPerRun);

 		int runCounter = 1;
		String currencyIcon = Currency.getInstance(Locale.getDefault()).getSymbol();

		for(int i = 0; i < machineList.size(); i++){
			runTable.addCell(new Phrase(""+runCounter, NORMAL));
			runTable.addCell(new Phrase(machineList.get(i), NORMAL));
			runTable.addCell(new Phrase(readLengthList.get(i), NORMAL));
			runTable.addCell(new Phrase(readTypeList.get(i), NORMAL));
			Integer numLanes = numLanesList.get(i);
			runTable.addCell(new Phrase(numLanes.toString(), NORMAL));
			Integer pricePerLane = pricePerLaneList.get(i);
			runTable.addCell(new Phrase(currencyIcon + " " + pricePerLane.toString(), NORMAL));
			Integer totalCostPerSequenceRun = numLanes * pricePerLane;
			PdfPCell totalCostPerSequenceRunCell = new PdfPCell(new Phrase(currencyIcon + " " + totalCostPerSequenceRun.toString(), NORMAL));
			totalCostPerSequenceRunCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			runTable.addCell(totalCostPerSequenceRunCell);
				
			cumulativeCostForAllSequenceRuns += totalCostPerSequenceRun.intValue();
			runCounter++;
		}
 		for(int i = 0; i < 6; i++){//6 empty cells with no border
 			PdfPCell cell = new PdfPCell(new Phrase(""));
 			cell.setBorder(Rectangle.NO_BORDER);
 			runTable.addCell(cell);
 		}
 		PdfPCell totalRunCost = new PdfPCell(new Phrase("Total: " + currencyIcon+" "+cumulativeCostForAllSequenceRuns, NORMAL_BOLD));
 		totalRunCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
 		totalRunCost.setBorderWidth(2f);
 		totalRunCost.setBorderColor(BaseColor.BLACK);
		runTable.addCell(totalRunCost);

		document.add(runTable);		
		return new Integer(cumulativeCostForAllSequenceRuns);
	}
	
	private Integer addAdditionalCostsAsTable(Document document, Job job, List<String> additionalCostReasonList, 
			List<Integer> additionalCostUnitsList, List<Integer> additionalCostPricePerUnitList) throws DocumentException{

		int cumulativeAdditionalCost = 0;

		if(additionalCostReasonList.size()==0){
			return cumulativeAdditionalCost;
		}
		
		Paragraph additionalCostTitle = new Paragraph();
		additionalCostTitle.setSpacingBefore(5);
		additionalCostTitle.setSpacingAfter(5);
		additionalCostTitle.add(new Chunk("Additional Costs:", NORMAL_BOLD));
		document.add(additionalCostTitle);
		
 	 	PdfPTable additionalCostTable = new PdfPTable(5);
 	 	additionalCostTable.setHorizontalAlignment(Element.ALIGN_LEFT);
 	 	additionalCostTable.setWidths(new float[]{0.2f, 1.4f, 0.3f, 0.5f, 0.9f});
 		PdfPCell celladditionalCostNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
 		celladditionalCostNo.setHorizontalAlignment(Element.ALIGN_CENTER);
 		additionalCostTable.addCell(celladditionalCostNo);
 		PdfPCell cellReason = new PdfPCell(new Phrase("Reason", NORMAL_BOLD));
 		cellReason.setHorizontalAlignment(Element.ALIGN_CENTER);
 		additionalCostTable.addCell(cellReason);
 		PdfPCell cellUnits = new PdfPCell(new Phrase("Units", NORMAL_BOLD));
 		cellUnits.setHorizontalAlignment(Element.ALIGN_CENTER);
 		additionalCostTable.addCell(cellUnits);
 		PdfPCell cellCostPerUnit = new PdfPCell(new Phrase("Cost/Unit", NORMAL_BOLD));
 		cellCostPerUnit.setHorizontalAlignment(Element.ALIGN_CENTER);
 		additionalCostTable.addCell(cellCostPerUnit);
 		PdfPCell cellTotalCost = new PdfPCell(new Phrase("Additional Cost", NORMAL_BOLD));
 		cellTotalCost.setHorizontalAlignment(Element.ALIGN_CENTER);
 		additionalCostTable.addCell(cellTotalCost);
 		

 		int additionalCostCounter = 1;
		String currencyIcon = Currency.getInstance(Locale.getDefault()).getSymbol();

		for(int i = 0; i < additionalCostReasonList.size(); i++){
			additionalCostTable.addCell(new Phrase(""+additionalCostCounter, NORMAL));
			additionalCostTable.addCell(new Phrase(additionalCostReasonList.get(i), NORMAL));
			Integer units = additionalCostUnitsList.get(i);
			additionalCostTable.addCell(new Phrase(units.toString(), NORMAL));
			Integer pricePerUnit = additionalCostPricePerUnitList.get(i);
			additionalCostTable.addCell(new Phrase(currencyIcon + " " + pricePerUnit.toString(), NORMAL));
			Integer totalCostPerAdditionalCost = units * pricePerUnit;
			PdfPCell totalCostPerAdditionalCostCell = new PdfPCell(new Phrase(currencyIcon + " " + totalCostPerAdditionalCost.toString(), NORMAL));
			totalCostPerAdditionalCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			additionalCostTable.addCell(totalCostPerAdditionalCostCell);
				
			cumulativeAdditionalCost += totalCostPerAdditionalCost.intValue();
			additionalCostCounter++;
		}
 		for(int i = 0; i < 4; i++){//4 empty cells with no border
 			PdfPCell cell = new PdfPCell(new Phrase(""));
 			cell.setBorder(Rectangle.NO_BORDER);
 			additionalCostTable.addCell(cell);
 		}
 		PdfPCell totalAdditionalCost = new PdfPCell(new Phrase("Total: " + currencyIcon+" "+cumulativeAdditionalCost, NORMAL_BOLD));
 		totalAdditionalCost.setHorizontalAlignment(Element.ALIGN_RIGHT);
 		totalAdditionalCost.setBorderWidth(2f);
 		totalAdditionalCost.setBorderColor(BaseColor.BLACK);
 		additionalCostTable.addCell(totalAdditionalCost);
		
		document.add(additionalCostTable);
		return new Integer(cumulativeAdditionalCost);
	}
	
	
	private void addCommentsAsTable(Document document, Job job, List<String> commentsList) throws DocumentException{

		if(commentsList.size()==0){
			return;
		}
		
		Paragraph commentsTitle = new Paragraph();
		commentsTitle.setSpacingBefore(5);
		commentsTitle.setSpacingAfter(5);		
		commentsTitle.add(new Chunk("Comments:", NORMAL_BOLD));
		document.add(commentsTitle);
		
		PdfPTable commentsTable = new PdfPTable(2);
		commentsTable.setHorizontalAlignment(Element.ALIGN_LEFT);
		commentsTable.setWidths(new float[]{0.3f, 5f});
		PdfPCell commentsNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
		commentsNo.setHorizontalAlignment(Element.ALIGN_CENTER);
		commentsTable.addCell(commentsNo);
		PdfPCell commentHeader = new PdfPCell(new Phrase("Comments", NORMAL_BOLD));
		commentHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
		commentsTable.addCell(commentHeader);
		
		int commentsCounter = 1;
		for(int i = 0; i < commentsList.size(); i++){
			PdfPCell numberCell = new PdfPCell(new Phrase(""+commentsCounter, NORMAL));
			numberCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 		commentsTable.addCell(numberCell);
	 		
			commentsTable.addCell(new Phrase(commentsList.get(i), NORMAL));
			commentsCounter++;			
		}
		
		document.add(commentsTable);		
		
	}	
	
	private Integer addCostSummaryTable(Document document, Job job, List<String>costReasonList, Map<String, Integer>costReasonPriceMap, List<String>discountReasonList, Map<String, Integer>discountReasonAbsolutePriceMap, Map<String, Integer> discountReasonPercentMap) throws DocumentException{
	
		String currencyIcon = Currency.getInstance(Locale.getDefault()).getSymbol();

		int totalFinalCost = 0;
		int totalCosts = 0;
		int totalDiscounts = 0;
		
 		Paragraph anticipatedCosts = new Paragraph();
 		anticipatedCosts.setSpacingBefore(15);
 		anticipatedCosts.setSpacingAfter(5);
 		anticipatedCosts.add(new Chunk("Cost Summary:", NORMAL_BOLD));
 	 	document.add(anticipatedCosts);
 		
 	    PdfPTable costTable = new PdfPTable(2);
 	    costTable.getDefaultCell().setBorder(0);
 	    costTable.setHorizontalAlignment(Element.ALIGN_LEFT);
 	    costTable.setWidthPercentage(60);
 	   
 	    for(String costReason : costReasonList){
 	    	
 	    	costTable.addCell(new Phrase(costReason, NORMAL_BOLD));
 	    	
 	    	int thisCost = costReasonPriceMap.get(costReason).intValue();
 	    	totalCosts += thisCost;
 		    PdfPCell secondCell = new PdfPCell(new Phrase(currencyIcon + " " + thisCost, NORMAL_BOLD));
 		    secondCell.setBorder(0);
 		    secondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
 	 	    costTable.addCell(secondCell); 	 
 	    }
 
 	    if(discountReasonList.size()>0){
 		    costTable.addCell(new Phrase("Subtotal", NORMAL_BOLD));
 	 	   
 	 	    PdfPCell subtotalCell = new PdfPCell(new Phrase(currencyIcon + " " + totalCosts, NORMAL_BOLD));
 	 	    subtotalCell.setBorder(0);
 	 	    subtotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
 	 	    costTable.addCell(subtotalCell);
 	 	  
 	 	    //blank line
 	 	    costTable.addCell(new Phrase(" ", NORMAL_BOLD));	 	   
	 	    PdfPCell blankCell = new PdfPCell(new Phrase(" ", NORMAL_BOLD));
	 	    blankCell.setBorder(0);
	 	    blankCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	 	    costTable.addCell(blankCell);
	 	    
	 	    //next add the discounts and get it's discountTotal
	 	    for(String discountReason : discountReasonList){
	 	    	
	 	    	if(discountReasonPercentMap.containsKey(discountReason)){
	 	    		int percentOff = discountReasonPercentMap.get(discountReason).intValue();
	 	    		costTable.addCell(new Phrase(discountReason + " (" + percentOff + "%)", NORMAL_BOLD));
	 	    		int thisDiscount = totalCosts * percentOff / 100;
	 	    		totalDiscounts += thisDiscount;
	 	    		PdfPCell secondCell = new PdfPCell(new Phrase("("+currencyIcon + " " + thisDiscount+")", NORMAL_BOLD));
		 		    secondCell.setBorder(0);
		 		    secondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
		 	 	    costTable.addCell(secondCell); 
	 	    	}
	 	    	else if(discountReasonAbsolutePriceMap.containsKey(discountReason)){
	 	    		int thisDiscount = discountReasonAbsolutePriceMap.get(discountReason).intValue();
	 	    		costTable.addCell(new Phrase(discountReason, NORMAL_BOLD));
	 	    		totalDiscounts += thisDiscount;
	 	    		PdfPCell secondCell = new PdfPCell(new Phrase("("+currencyIcon + " " + thisDiscount+")", NORMAL_BOLD));
	 	    		secondCell.setBorder(0);
	 	    		secondCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	 	    		costTable.addCell(secondCell); 	
	 	    	}
	 	    } 	 
 	    }
 	    //blank line
 	    costTable.addCell(new Phrase(" ", NORMAL_BOLD));	 	   
 	    PdfPCell blankCell = new PdfPCell(new Phrase(" ", NORMAL_BOLD));
	    blankCell.setBorder(0);
	    blankCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
	    costTable.addCell(blankCell);
	    
    	totalFinalCost = totalCosts - totalDiscounts;
    	if(totalFinalCost < 0){
    		totalFinalCost = 0;
    	}
    	
    	PdfPCell totalFinalCostWordsCell = new PdfPCell(new Phrase("Total Cost", NORMAL_BOLD));
    	totalFinalCostWordsCell.setBorderWidth(2f);
    	totalFinalCostWordsCell.setBorderColorBottom(BaseColor.BLACK);
    	totalFinalCostWordsCell.setBorderColorLeft(BaseColor.BLACK);
    	totalFinalCostWordsCell.setBorderColorTop(BaseColor.BLACK);
    	totalFinalCostWordsCell.setBorderColorRight(BaseColor.WHITE);
    	totalFinalCostWordsCell.setHorizontalAlignment(Element.ALIGN_LEFT);	 		
 	    costTable.addCell(totalFinalCostWordsCell);
 	    
 	    PdfPCell totalFinalCostCell = new PdfPCell(new Phrase(currencyIcon + " " + totalFinalCost, NORMAL_BOLD));
 	    totalFinalCostCell.setBorderWidth(2f);
 	    totalFinalCostWordsCell.setBorderColorBottom(BaseColor.BLACK);
 	    totalFinalCostWordsCell.setBorderColorRight(BaseColor.BLACK);
 	    totalFinalCostWordsCell.setBorderColorLeft(BaseColor.WHITE);
 	    totalFinalCostWordsCell.setBorderColorTop(BaseColor.BLACK);
 	    totalFinalCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);	 		
 	    costTable.addCell(totalFinalCostCell);	 	    
	 	    
	 	document.add(costTable);		
		
		return new Integer(totalFinalCost);
	}
	
	@RequestMapping(value="/{jobId}/createQuote", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobCreateQuotePage(@PathVariable("jobId") Integer jobId,
			   ModelMap m) throws SampleTypeException {
		
		//see here for simple use of com.itextpdf.text.pdf.PdfWriter    http://viralpatel.net/blogs/generate-pdf-file-in-java-using-itext-jar/
 		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		Random randomNumberGenerator = new Random(System.currentTimeMillis());
		try{
	 		String currencyIcon = Currency.getInstance(Locale.getDefault()).getSymbol();//+String.format("%.2f", price));
		 	int costPerLibrary = 500;
		 	int costPerRun = 1000;

			User submitter = job.getUser();
			List<UserMeta> userMetaList = submitter.getUserMeta();
			String submitterTitle = MetaHelper.getMetaValue("user", "title", userMetaList);
			String submitterInstitution = MetaHelper.getMetaValue("user", "institution", userMetaList);
			String submitterBuildingRoom = MetaHelper.getMetaValue("user", "building_room", userMetaList);
			String submitterAddress = MetaHelper.getMetaValue("user", "address", userMetaList);
			String submitterCity = MetaHelper.getMetaValue("user", "city", userMetaList);
			String submitterState = MetaHelper.getMetaValue("user", "state", userMetaList);
			String submitterCountry = MetaHelper.getMetaValue("user", "country", userMetaList);
			String submitterZip = MetaHelper.getMetaValue("user", "zip", userMetaList);
			String submitterPhone = MetaHelper.getMetaValue("user", "phone", userMetaList);		
			
			Lab lab = job.getLab();
			String labDepartment = lab.getDepartment().getName();//Genetics, Internal, External, Cell Biology (External means not Einstein, and used for pricing)
			String pricingSchedule = "Internal";
			if(labDepartment.equalsIgnoreCase("external")){
				pricingSchedule = "External";
			}
			
			User pI = lab.getUser();
			List<UserMeta> pIMetaList = pI.getUserMeta();
			String pITitle = MetaHelper.getMetaValue("user", "title", pIMetaList);
			String pIInstitution = MetaHelper.getMetaValue("user", "institution", pIMetaList);
			String pIBuildingRoom = MetaHelper.getMetaValue("user", "building_room", pIMetaList);
			String pIAddress = MetaHelper.getMetaValue("user", "address", pIMetaList);
			String pICity = MetaHelper.getMetaValue("user", "city", pIMetaList);
			String pIState = MetaHelper.getMetaValue("user", "state", pIMetaList);
			String pICountry = MetaHelper.getMetaValue("user", "country", pIMetaList);
			String pIZip = MetaHelper.getMetaValue("user", "zip", pIMetaList);
			String pIPhone = MetaHelper.getMetaValue("user", "phone", pIMetaList);
			
			List<JobResourcecategory> jobResourcecategoryList = job.getJobResourcecategory();
			StringBuffer jobMachineListSB = new StringBuffer();
			int count = 0;
	 	 	for(JobResourcecategory jrc: jobResourcecategoryList){
	 	 		if(count==0){
	 	 			jobMachineListSB.append(jrc.getResourceCategory().getName());
	 	 		}else{ jobMachineListSB.append(", ").append(jrc.getResourceCategory().getName()); }
	 	 		count++;	 	 		
	 	 	}
			String jobMachineList = new String(jobMachineListSB);
			List<JobMeta> jobMetaList = job.getJobMeta();
			String readLength = null;
			String readType = null;
			for(JobMeta jm : jobMetaList){
				if(jm.getK().toLowerCase().indexOf("readlength")>-1){
					readLength = jm.getV();
				}
				else if(jm.getK().toLowerCase().indexOf("readtype")>-1){
					readType = jm.getV();
				}
			}
			
			int numberOfLanesRequested = job.getJobCellSelection().size();
	 	 	
	 	    File localFile = fileService.createTempFile();
	 	    OutputStream fileOS = new FileOutputStream(localFile);			
			//OutputStream fileOS = new FileOutputStream(new File("/Users/robertdubin/testQuote.pdf"));
	 	    Document document = new Document();
	 	    PdfWriter.getInstance(document, fileOS).setInitialLeading(10);
	 	    document.open();
	 	    
	 	    
	 	    Image image;
	 	    try{
	 	    	image = Image.getInstance("/Users/robertdubin/Documents/images/Einstein_Logo.png");
	 	    	image.setAlignment(Image.MIDDLE);
	 	    	image.scaleToFit(1000, 50);//72 is about 1 inch in height
	 	    	document.add(image);
	 	    }catch(Exception e){}
	 	    
	 	    Paragraph header = new Paragraph();
	 	    header.add(new Chunk("Epigenomics Shared Facility", BIG_BOLD));
	 	    document.add(header);	 	    
	 	      	      
	 	    LineSeparator line = new LineSeparator(); 
	 	    line.setOffset(new Float(-5.0));
	 	    document.add(line);
	 	        
	 	    Paragraph facilityManager = new Paragraph(); 
	 	    facilityManager.setSpacingBefore(4);
	 	    facilityManager.setSpacingAfter(25);
		 	facilityManager.setLeading(10);
	 	    Chunk facilityManagerNameAndAddress = new Chunk("Shahina Maqbool PhD (ESF Director), Albert Einstein College of Medicine, 1301 Morris Park Ave (Price 159F)", TINY_BOLD);
	 	    facilityManager.add(facilityManagerNameAndAddress);
		 	facilityManager.add(Chunk.NEWLINE);
	 	    Chunk facilityManagerEmailPhone = new Chunk("Email:shahinaq.maqbool@einstein.yu.edu Phone:718-678-1163", TINY_BOLD);
	 	    facilityManager.add(facilityManagerEmailPhone);
	 	    facilityManager.add(Chunk.NEWLINE);
	 	    facilityManager.setAlignment(Element.ALIGN_CENTER);
	 	    document.add(facilityManager);
	 	    
	 	    PdfPTable toTheAttentionOftable = new PdfPTable(2);
	 	    toTheAttentionOftable.getDefaultCell().setBorder(0);
	 	    toTheAttentionOftable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	    toTheAttentionOftable.addCell(new Phrase("Submitter:", NORMAL_BOLD));
	 	    toTheAttentionOftable.addCell(new Phrase("PI:", NORMAL_BOLD));
	 	  	toTheAttentionOftable.addCell(new Phrase(submitterTitle + " " + submitter.getNameFstLst(), NORMAL));
	 	  	toTheAttentionOftable.addCell(new Phrase(pITitle + " " + pI.getNameFstLst(), NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIInstitution, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIBuildingRoom, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pIAddress, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(submitterCity + ", " + submitterState + " " + submitterCountry, NORMAL));
	 		toTheAttentionOftable.addCell(new Phrase(pICity + ", " + pIState + " " + pICountry, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(submitter.getEmail(), NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(pI.getEmail(), NORMAL));	 	    
	 	    toTheAttentionOftable.addCell(new Phrase(submitterPhone, NORMAL));
	 	    toTheAttentionOftable.addCell(new Phrase(pIPhone, NORMAL));	 	   
	 	    document.add(toTheAttentionOftable);

	 	    Paragraph reasonForDocument = new Paragraph();
	 	    reasonForDocument.setSpacingBefore(15);
	 	    reasonForDocument.setSpacingAfter(15);
	 	    reasonForDocument.add(new Chunk("Re: ", NORMAL_BOLD));
	 	    reasonForDocument.add(new Phrase("Estimated costs for Job ID " + job.getId() +". ", NORMAL));
	 	    document.add(reasonForDocument);
	 	
	 	    LineSeparator line2 = new LineSeparator(); 
	 	    document.add(line2);
	 	    
	 	    
	 	    Paragraph jobDetails = new Paragraph();
	 	    jobDetails.setSpacingBefore(15);
	 	    jobDetails.setSpacingAfter(5);
	 	 
	 	    
	 	    jobDetails.add(new Chunk("Job Details:", NORMAL_BOLD));jobDetails.add(Chunk.NEWLINE);
	 	 	jobDetails.setLeading(15);
	 	 	jobDetails.add(new Phrase("Job ID: " + job.getId(), NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	jobDetails.add(new Phrase("Job Name: " + job.getName(), NORMAL));jobDetails.add(Chunk.NEWLINE);	 	 	
	 	    SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy"); //("yyyy/MM/dd");
	 	 	jobDetails.add(new Phrase("Submitted: " + formatter.format(job.getCreated()), NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	jobDetails.add(new Phrase("Assay: " + job.getWorkflow().getName(), NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	String platform = jobResourcecategoryList.size()==1?"Platform: ":"Platforms: ";
	 	 	jobDetails.add(new Phrase(platform + jobMachineList, NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	if(readType!=null){
	 	 		jobDetails.add(new Phrase("Read Type: " + readType, NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	}
	 	 	if(readLength!=null){
	 	 		jobDetails.add(new Phrase("Read Length: " + readLength, NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	}
	 	 	if(numberOfLanesRequested>0){
	 	 		jobDetails.add(new Phrase("Lanes Requested: " + numberOfLanesRequested, NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	}
	 	 	jobDetails.add(new Phrase("Samples: " + job.getSample().size(), NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	jobDetails.add(new Phrase("Pricing Schecule: " + pricingSchedule, NORMAL));jobDetails.add(Chunk.NEWLINE);
	 	 	document.add(jobDetails);
	 	
	 	 	Paragraph sampleLibraryTitle = new Paragraph();
	 	 	sampleLibraryTitle.setSpacingBefore(5);
	 	 	sampleLibraryTitle.setSpacingAfter(5);
	 	 	sampleLibraryTitle.add(new Chunk("Samples & Library Preparations:", NORMAL_BOLD));
	 	 	document.add(sampleLibraryTitle);
	 	 	
	 	 	PdfPTable sampleLibraryTable = new PdfPTable(5);
	 	 	sampleLibraryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	 	sampleLibraryTable.setWidths(new float[]{0.3f, 2f, 0.6f, 1f, 1f});
	 		PdfPCell cellNo = new PdfPCell(new Phrase("No.", NORMAL_BOLD));
	 		cellNo.setHorizontalAlignment(Element.ALIGN_CENTER);
	 		sampleLibraryTable.addCell(cellNo);
	 		PdfPCell cellSample = new PdfPCell(new Phrase("Sample", NORMAL_BOLD));
	 		cellSample.setHorizontalAlignment(Element.ALIGN_CENTER);
	 		sampleLibraryTable.addCell(cellSample);
	 		PdfPCell cellMaterial = new PdfPCell(new Phrase("Material", NORMAL_BOLD));
	 		cellMaterial.setHorizontalAlignment(Element.ALIGN_CENTER);
	 		sampleLibraryTable.addCell(cellMaterial);
	 		PdfPCell needLib = new PdfPCell(new Phrase("Need Library", NORMAL_BOLD));
	 		needLib.setHorizontalAlignment(Element.ALIGN_CENTER);
	 		sampleLibraryTable.addCell(needLib);
	 		PdfPCell runOnLane = new PdfPCell(new Phrase("On Lane(s)", NORMAL_BOLD));
	 		runOnLane.setHorizontalAlignment(Element.ALIGN_CENTER);
	 		sampleLibraryTable.addCell(runOnLane);
	 		
	 		int sampleCounter = 1;
	 		int libraryConstructionsRequired = 0;
	 		int cumulativeCostForAllLibraries = 0;
	 		Map<Sample,String> coverageMap = jobService.getCoverageMap(job);
	 		
	 		for(Sample sample : job.getSample()){
	 			if(sample.getParentId()!=null){
	 				continue;
	 			}
	 			sampleLibraryTable.addCell(new Phrase(""+sampleCounter, NORMAL));
	 			sampleLibraryTable.addCell(new Phrase(sample.getName(), NORMAL));
	 			sampleLibraryTable.addCell(new Phrase(sample.getSampleType().getName(), NORMAL));
	 			if(!"library".equalsIgnoreCase(sample.getSampleType().getIName())){
	 				libraryConstructionsRequired++;
	 				sampleLibraryTable.addCell(new Phrase("YES  ("+currencyIcon + costPerLibrary + ")", NORMAL));
	 				cumulativeCostForAllLibraries += costPerLibrary;
	 			}
	 			else{
	 				sampleLibraryTable.addCell(new Phrase("NO", NORMAL));
	 			}
	 			String coverageString = coverageMap.get(sample);
	 			StringBuffer runOnWhichLanesSB = new StringBuffer();
	 			char testChar = '1';
	 			for(int i = 0; i < coverageString.length(); i++){
	 				if(coverageString.charAt(i) == testChar){//run on lane i+1
	 					if(runOnWhichLanesSB.length()>0){
	 						runOnWhichLanesSB.append(", " + (i+1));
	 					}
	 					else{
	 						runOnWhichLanesSB.append(i+1);
	 					}
	 				}
	 			}
	 			String runOnWhichLanes = new String(runOnWhichLanesSB);
	 			sampleLibraryTable.addCell(new Phrase(runOnWhichLanes, NORMAL));
	 			sampleCounter++;
	 		}
	 		document.add(sampleLibraryTable);
	 	
	 		Paragraph anticipatedCosts = new Paragraph();
	 		anticipatedCosts.setSpacingBefore(15);
	 		anticipatedCosts.setSpacingAfter(5);
	 		anticipatedCosts.add(new Chunk("Anticipated Costs:", NORMAL_BOLD));
	 	 	document.add(anticipatedCosts);
	 		
	 	    PdfPTable costTable = new PdfPTable(2);
	 	    costTable.getDefaultCell().setBorder(0);
	 	    costTable.setHorizontalAlignment(Element.ALIGN_LEFT);
	 	    int costForAllLibraries = libraryConstructionsRequired * costPerLibrary;
	 	    costTable.addCell(new Phrase("Library Constructions (" + libraryConstructionsRequired + " x " + currencyIcon+costPerLibrary + ")", NORMAL_BOLD));
	 	    costTable.addCell(new Phrase(currencyIcon + costForAllLibraries, NORMAL_BOLD));
	 	    costTable.addCell(new Phrase("Library Constructions", NORMAL_BOLD));
	 	    costTable.addCell(new Phrase(currencyIcon + cumulativeCostForAllLibraries, NORMAL_BOLD));
	 	    int costForAllRuns = costPerRun * numberOfLanesRequested;
	 	    costTable.addCell(new Phrase("Sequencing Runs (" + numberOfLanesRequested + " x " + currencyIcon+costPerRun + ")", NORMAL_BOLD));
	 	    costTable.addCell(new Phrase(currencyIcon + costForAllRuns, NORMAL_BOLD));
	 	    int additionalMultiplexingCost = 0;
	 	    costTable.addCell(new Phrase("Additional Multiplexing Charge", NORMAL_BOLD));
	 	    costTable.addCell(new Phrase(currencyIcon + additionalMultiplexingCost, NORMAL_BOLD));
	    	int subtotal = costForAllLibraries + costForAllRuns + additionalMultiplexingCost;
	    	int finalCost = subtotal;
	 	    if("internal".equalsIgnoreCase(pricingSchedule)){
	 	    	
	 		    costTable.addCell(new Phrase("Subtotal", NORMAL_BOLD));
		    	costTable.addCell(new Phrase(currencyIcon + subtotal, NORMAL_BOLD));
			    costTable.addCell(new Phrase(" ", NORMAL_BOLD));
		    	costTable.addCell(new Phrase(" ", NORMAL_BOLD));
		 
	 	    	float institutionalDiscountFraction = 0.25f;
	 	    	float institutionalDiscountPercentForDisplay = institutionalDiscountFraction * 100;
	 	    	float discount = subtotal * institutionalDiscountFraction;
	 	    	finalCost = subtotal - (int)discount;
	 	    	costTable.addCell(new Phrase("Institutional Cost Share (" + institutionalDiscountPercentForDisplay + "%)", NORMAL_BOLD));
	 	    	costTable.addCell(new Phrase("(" + currencyIcon + (int)discount + ")", NORMAL_BOLD));
	 	    	
	 	    }
	 	    else{
	 		    costTable.addCell(new Phrase(" ", NORMAL_BOLD));
		    	costTable.addCell(new Phrase(" ", NORMAL_BOLD));
	 	    }
	 	    costTable.addCell(new Phrase("Estimated Total Cost", NORMAL_BOLD));
	    	costTable.addCell(new Phrase(currencyIcon + finalCost, NORMAL_BOLD));
	 	    document.add(costTable);
	 	    	 		
	 	    document.close();
	 	    
	 	    fileOS.close(); 
	 	     
	 	    DateFormat dateFormat2 = new SimpleDateFormat("yyyy_MM_dd");
	 	   	Date now = new Date();
	 	    fileService.saveLocalJobFile(job, localFile, "Job"+job.getId()+"_Quote_"+dateFormat2.format(now)+".pdf", "Job"+job.getId()+"_Quote_"+dateFormat2.format(now), randomNumberGenerator);
	 	    
			m.addAttribute("successMessage", "New Quote Saved");
		} catch(Exception e){
			String errorMessage = "Major problems saving this pdf";
			logger.warn(errorMessage);
			m.addAttribute("errorMessage", errorMessage);
			logger.warn("-------the exception message is : " + e.getMessage());
		}
		
		populateCostPage(job, m);
		return "job/home/costManager";
	}
	
	
	@RequestMapping(value="/{jobId}/requests", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobRequestsPage(@PathVariable("jobId") Integer jobId, 
			  @RequestParam(value="onlyDisplayCellsRequested", required=false) String onlyDisplayCellsRequested,//used as flag to only display coverage map, from button on samples page
			  ModelMap m) throws SampleTypeException {
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}		
		m.addAttribute("job", job);
		m.addAttribute("onlyDisplayCellsRequested", onlyDisplayCellsRequested==null?"false":onlyDisplayCellsRequested);
		
		//request for which libraries/samples should go on which lanes
		getCellsRequested(job, m);		
		if("true".equalsIgnoreCase(onlyDisplayCellsRequested)){//just display coverageMap
			return "job/home/requests";
		}
		
		//samplePairingRequest
		getSamplePairsRequested(job, m);		
		//software request
		getSoftwareRequested(job, m);
		//getSubmittedSamplesAndOrganism_Genome_BuildForAlignment
		getOrganism_Genome_BuildForAlignment(job, m);
		List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
		Map<Sample, List<String>> sampleGenomesForAlignmentListMap = new HashMap<Sample, List<String>>();
		for(Sample submittedSample : submittedSamplesList){
			String organismOfTheSample = sampleService.getNameOfOrganism(submittedSample, "?");
			String organismForAlignment = sampleService.getNameOfOrganismForAlignmentRequest(submittedSample, "?");
			String genomeForAlignment = sampleService.getNameOfGenomeForAlignmentRequest(submittedSample, "?");
			String buildForAlignment = sampleService.getNameOfGenomeBuildForAlignmentRequest(submittedSample, "?");
			List<String> encodedList = new ArrayList<String>();
			encodedList.add(organismOfTheSample);
			encodedList.add(organismForAlignment);
			encodedList.add(genomeForAlignment);
			encodedList.add(buildForAlignment);
			sampleGenomesForAlignmentListMap.put(submittedSample, encodedList);
		}
		m.addAttribute("submittedSamplesList", submittedSamplesList);
		m.addAttribute("sampleGenomesForAlignmentListMap", sampleGenomesForAlignmentListMap);
		
		return "job/home/requests";
	}
	
	private void getCellsRequested(Job job, ModelMap m){
		//which libraries/samples should go on which lanes
		m.addAttribute("cellsRequestedMap", jobService.getCoverageMap(job));
		m.addAttribute("totalNumberCellsRequested", job.getJobCellSelection()==null?0:job.getJobCellSelection().size());		
	}
	private void getSamplePairsRequested(Job job, ModelMap m){
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
	}
	private void getSoftwareRequested(Job job, ModelMap m){
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
		m.addAttribute("parentArea", "job");//do not remove; it's needed for the metadata related to the software display below
	}
	private void getOrganism_Genome_BuildForAlignment(Job job, ModelMap m){
		List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
		Map<Sample, List<String>> sampleGenomesForAlignmentListMap = new HashMap<Sample, List<String>>();
		for(Sample submittedSample : submittedSamplesList){
			String organismOfTheSample = sampleService.getNameOfOrganism(submittedSample, "?");
			String organismForAlignment = sampleService.getNameOfOrganismForAlignmentRequest(submittedSample, "?");
			String genomeForAlignment = sampleService.getNameOfGenomeForAlignmentRequest(submittedSample, "?");
			String buildForAlignment = sampleService.getNameOfGenomeBuildForAlignmentRequest(submittedSample, "?");
			List<String> encodedList = new ArrayList<String>();
			encodedList.add(organismOfTheSample);
			encodedList.add(organismForAlignment);
			encodedList.add(genomeForAlignment);
			encodedList.add(buildForAlignment);
			sampleGenomesForAlignmentListMap.put(submittedSample, encodedList);
			//System.out.println("------"+ submittedSample.getName() + " : " + organismOfTheSample + " : " + organismForAlignment + " : " + genomeForAlignment + " : " + buildForAlignment);
		}
		m.addAttribute("submittedSamplesList", submittedSamplesList);
		m.addAttribute("sampleGenomesForAlignmentListMap", sampleGenomesForAlignmentListMap);
	}
 	@RequestMapping(value="/{jobId}/addLibrariesToCell", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobAddLibrariesToCellPage(@PathVariable("jobId") Integer jobId, 
			  ModelMap m) throws SampleTypeException {						
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}

		getSampleLibraryRunData(job, m);
		
		return "job/home/addLibrariesToCell";
	}
	 
	@RequestMapping(value="/{jobId}/addLibrariesToCell", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobAddLibrariesToCellPostPage(@PathVariable("jobId") Integer jobId,
			  @RequestParam("cellId") Integer cellId, 
			  @RequestParam("libraryId") List<Integer> libraryIdList,
			  ModelMap m) throws SampleTypeException {						

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		String addLibrariesToPlatformUnitErrorMessage = "";
		String addLibrariesToPlatformUnitSuccessMessage = "";

		if(cellId==null || cellId.intValue()<=0){//could occur, user forgot to select a cell
			logger.warn("No Cell Selected");
			m.addAttribute("addLibrariesToPlatformUnitErrorMessage", messageService.getMessage("platformunit.noCellSelected.error")); 
			getSampleLibraryRunData(job, m);			
			return "job/home/addLibrariesToCell";
		}
		Sample cell = sampleService.getSampleById(cellId);
		if(cell==null || cell.getId()==null || cell.getId().intValue()<=0){//rather unlikely error
			logger.warn("CellId not valid");
			m.addAttribute("addLibrariesToPlatformUnitErrorMessage", messageService.getMessage("platformunit.invalidCellId.error")); 
			getSampleLibraryRunData(job, m);			
			return "job/home/addLibrariesToCell";
		}
		
		List<String> libConcInCellPicoMAsStringList = new ArrayList<String>();
		for(Integer libraryId : libraryIdList){
			String param = "libConcInCellPicoM_"+libraryId.intValue();
			String value = request.getParameter(param);			
			libConcInCellPicoMAsStringList.add(value);
		}

		boolean parameterMismatchError = false;
		if(libConcInCellPicoMAsStringList.size()!=libraryIdList.size()){
			addLibrariesToPlatformUnitErrorMessage=messageService.getMessage("platformunit.unexpectedParamaterMismatch.error");
			parameterMismatchError=true;
		}
		
		boolean platformUnitError = false;
		if( ! parameterMismatchError ){
			// ensure platform unit is available
			try{
				Sample platformUnit = sampleService.getPlatformUnitForCell(sampleService.getSampleById(cellId));
				if( ! sampleService.getAvailableAndCompatiblePlatformUnits(jobService.getJobByJobId(jobId)).contains(platformUnit) ){
					addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.flowcellStateError.error");
					platformUnitError = true;
				}
			}catch(Exception e){
				addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.flowcellNotFoundNotUnique.error");
				platformUnitError = true;
			}
		}		
		int numLibrariesSuccessfullyAdded = 0;
		int numLibrariesFailedAddedDueToSampleTypeOrAdaptorError = 0;
		int numLibrariesFailedAddedDueToMultiplexError = 0;
		int numLibrariesFailedAddedDueToParseFloatError = 0;
		int numLibrariesFailedAddedDueToLibraryNotInDatabaseError = 0;
		int numLibrariesFailedAddedDueToUnknownError = 0;
		int numLibrariesWithoutpMConcentration = 0;
		List<Sample> librariesThatFailedDueToMultiplexErrors = new ArrayList<Sample>();
		List<Sample> librariesThatFailedDueToParseFloatErrors = new ArrayList<Sample>();
		List<Sample> librariesThatFailedDueToSampleTypeOrAdaptorErrors = new ArrayList<Sample>();
		List<Sample> librariesThatFailedDueToUnknownErrors = new ArrayList<Sample>();
		
		if( parameterMismatchError==false && platformUnitError==false ){
			for(int i = 0; i < libraryIdList.size(); i++){
				
				if("".equals(libConcInCellPicoMAsStringList.get(i).trim())){//user chose to NOT add this library to this cell
					numLibrariesWithoutpMConcentration++;
					continue;
				}
				Sample library = sampleService.getSampleById(libraryIdList.get(i));
				if(library==null || library.getId()==null || library.getId().intValue()<=0){//rather unlikely error
					numLibrariesFailedAddedDueToLibraryNotInDatabaseError++;
					continue;
				}
				try{
					Float libConcInCellPicoMFloat = new Float(Float.parseFloat(libConcInCellPicoMAsStringList.get(i).trim()));					
					sampleService.addLibraryToCell(cell, library, libConcInCellPicoMFloat, job);
					//addLibrariesToPlatformUnitSuccessMessage = messageService.getMessage("platformunit.libAdded.success");
					numLibrariesSuccessfullyAdded++;
				} catch(NumberFormatException e){//libConcInCellPicoMAsStringList.get(i).trim() didn't parse to float; javascript on webpage should reduce this a lot
					logger.warn(e.getMessage());
					numLibrariesFailedAddedDueToParseFloatError++;
					librariesThatFailedDueToParseFloatErrors.add(library);						
				} catch(SampleMultiplexException sme){//index already on cell
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.multiplex.error");//index already on cell; might happen alot
					logger.warn(sme.getMessage()); // print more detailed error to debug logs
					numLibrariesFailedAddedDueToMultiplexError++;
					librariesThatFailedDueToMultiplexErrors.add(library);
				} catch(SampleTypeException ste){//unlikely to occur
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.sampleType.error");
					logger.warn(ste.getMessage()); // print more detailed error to warn logs
					numLibrariesFailedAddedDueToSampleTypeOrAdaptorError++;
					librariesThatFailedDueToSampleTypeOrAdaptorErrors.add(library);
				} catch(SampleException se){//unlikely to occur
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorNotFound.error");
					logger.warn(se.getMessage()); // print more detailed error to warn logs
					numLibrariesFailedAddedDueToSampleTypeOrAdaptorError++;
					librariesThatFailedDueToSampleTypeOrAdaptorErrors.add(library);
				} catch(MetadataException me){//unlikely to occur
					//addLibrariesToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorBarcodeNotFound.error");
					logger.warn(me.getMessage()); // print more detailed error to warn logs
					numLibrariesFailedAddedDueToSampleTypeOrAdaptorError++;
					librariesThatFailedDueToSampleTypeOrAdaptorErrors.add(library);
				} catch(Exception e){//unlikely
					numLibrariesFailedAddedDueToUnknownError++;
					librariesThatFailedDueToUnknownErrors.add(library);
				}
			}
		}
		if(numLibrariesWithoutpMConcentration==libraryIdList.size()){//no library concentrations at all were provided by user
			if( ! "".equals(addLibrariesToPlatformUnitErrorMessage) ){
				addLibrariesToPlatformUnitErrorMessage += "<br />";
			}
			addLibrariesToPlatformUnitErrorMessage += messageService.getMessage("platformunit.libraryConcentrationsNotProvided.error");
		}
		
		if(numLibrariesFailedAddedDueToLibraryNotInDatabaseError >0){//unexpected
			if( ! "".equals(addLibrariesToPlatformUnitErrorMessage) ){
				addLibrariesToPlatformUnitErrorMessage += "<br />";
			}
			
			if(numLibrariesFailedAddedDueToLibraryNotInDatabaseError==1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToLibraryNotInDatabaseError + " " + messageService.getMessage("platformunit.libraryNotFoundInDatabase.error");
			}
			else if(numLibrariesFailedAddedDueToLibraryNotInDatabaseError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToLibraryNotInDatabaseError + " " + messageService.getMessage("platformunit.librariesNotFoundInDatabase.error");
			}
			//there is no way to enumerate them
		}		
		if(numLibrariesFailedAddedDueToParseFloatError >0){
			if( ! "".equals(addLibrariesToPlatformUnitErrorMessage) ){
				addLibrariesToPlatformUnitErrorMessage += "<br />";
			}
			
			if(numLibrariesFailedAddedDueToParseFloatError==1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToParseFloatError + " " + messageService.getMessage("platformunit.libraryConcentrationNotNumber.error");
			}
			else if(numLibrariesFailedAddedDueToParseFloatError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToParseFloatError + " " + messageService.getMessage("platformunit.librariesConcentrationNotNumber.error");
			}
			for(Sample s : librariesThatFailedDueToParseFloatErrors){
				addLibrariesToPlatformUnitErrorMessage += "<br />"+s.getName();
			}
		}
		if(numLibrariesFailedAddedDueToMultiplexError>0){
			if( ! "".equals(addLibrariesToPlatformUnitErrorMessage) ){
				addLibrariesToPlatformUnitErrorMessage += "<br />";
			}
			
			if(numLibrariesFailedAddedDueToMultiplexError==1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToMultiplexError + " "+ messageService.getMessage("platformunit.libraryIndexConflict.error");
			}
			else if(numLibrariesFailedAddedDueToMultiplexError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToMultiplexError + " "+ messageService.getMessage("platformunit.librariesIndexConflict.error");
			}
			for(Sample s : librariesThatFailedDueToMultiplexErrors){
				addLibrariesToPlatformUnitErrorMessage += "<br />"+s.getName();
			}
		}
		if(numLibrariesFailedAddedDueToSampleTypeOrAdaptorError >0){
			if( ! "".equals(addLibrariesToPlatformUnitErrorMessage) ){
				addLibrariesToPlatformUnitErrorMessage += "<br />";
			}
			
			if(numLibrariesFailedAddedDueToSampleTypeOrAdaptorError==1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToSampleTypeOrAdaptorError + " " + messageService.getMessage("platformunit.librarySampleTypeOrAdaptorProblem.error");
			}
			else if(numLibrariesFailedAddedDueToSampleTypeOrAdaptorError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToSampleTypeOrAdaptorError + " " + messageService.getMessage("platformunit.librariesSampleTypeOrAdaptorProblem.error");
			}
			for(Sample s : librariesThatFailedDueToSampleTypeOrAdaptorErrors){
				addLibrariesToPlatformUnitErrorMessage += "<br />"+s.getName();
			}
		}
		if(numLibrariesFailedAddedDueToUnknownError >0){
			if( ! "".equals(addLibrariesToPlatformUnitErrorMessage) ){
				addLibrariesToPlatformUnitErrorMessage += "<br />";
			}
			
			if(numLibrariesFailedAddedDueToUnknownError==1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToUnknownError + " " + messageService.getMessage("platformunit.libraryUnknownProblem.error");
			}
			else if(numLibrariesFailedAddedDueToSampleTypeOrAdaptorError>1){
				addLibrariesToPlatformUnitErrorMessage += numLibrariesFailedAddedDueToUnknownError + " " + messageService.getMessage("platformunit.librariesUnknownProblem.error");
			}
			for(Sample s : librariesThatFailedDueToUnknownErrors){
				addLibrariesToPlatformUnitErrorMessage += "<br />"+s.getName();
			}
		}
		
		if( numLibrariesSuccessfullyAdded == 1 ){
			addLibrariesToPlatformUnitSuccessMessage = numLibrariesSuccessfullyAdded + " " + messageService.getMessage("platformunit.libraryAdded.label");
		}
		else if( numLibrariesSuccessfullyAdded > 1 ){
			addLibrariesToPlatformUnitSuccessMessage = numLibrariesSuccessfullyAdded + " " + messageService.getMessage("platformunit.librariesAdded.label");
		}
		
		getSampleLibraryRunData(job, m);
		
		m.addAttribute("addLibrariesToPlatformUnitErrorMessage", addLibrariesToPlatformUnitErrorMessage);
		m.addAttribute("addLibrariesToPlatformUnitSuccessMessage", addLibrariesToPlatformUnitSuccessMessage);

		return "job/home/addLibrariesToCell";
	}
	
	@RequestMapping(value="/{jobId}/mpsResultsListedBySample", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobMpsResultsListedBySamplePage(@PathVariable("jobId") Integer jobId, 
			  ModelMap m) throws SampleTypeException {
			
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		getSampleLibraryRunData(job, m);
		
		return "job/home/mpsResultsListedBySample";
	}
	
	@RequestMapping(value="/{jobId}/samples", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobSamplesPage(@PathVariable("jobId") Integer jobId, 
			  ModelMap m) throws SampleTypeException {
			
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		getSampleLibraryRunData(job, m);
		
		return "job/home/samples";
	}

	private void getSampleLibraryRunData(Job job, ModelMap m) throws SampleTypeException {
		
		  m.addAttribute("job", job);
		
		  List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		  List<Sample> submittedMacromoleculeList = new ArrayList<Sample>();
		  List<Sample> submittedLibraryList = new ArrayList<Sample>();
		  List<Sample> facilityLibraryList = new ArrayList<Sample>();
		  sampleService.enumerateSamplesForMPS(allJobSamples, submittedMacromoleculeList, submittedLibraryList, facilityLibraryList);

		  m.addAttribute("submittedMacromoleculeList", submittedMacromoleculeList);//needed to distinguish submittedMacromoleucle from submitted Library
		  m.addAttribute("submittedLibraryList", submittedLibraryList);
		  //not used on webpage: m.addAttribute("facilityLibraryList", facilityLibraryList);
	
		  //consolidate job's libraries
		  List<Sample> allJobLibraries = new ArrayList<Sample>();//could have gotten this from allJobLibraries = jobService.getLibraries(job);
		  allJobLibraries.addAll(submittedLibraryList);
		  allJobLibraries.addAll(facilityLibraryList);
		  
		  //consolidate job's submitted objects: submitted macromolecules and submitted libraries
		  List<Sample> submittedObjectList = new ArrayList<Sample>();//could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
		  submittedObjectList.addAll(submittedMacromoleculeList);
		  submittedObjectList.addAll(submittedLibraryList);
		  m.addAttribute("submittedObjectList", submittedObjectList);	//main object list used on the web page 
		  	  
		  //For each job's sample, get the qcStatus and comments; note the call changes if library (getLibraryQCStatus) or macromolecule (getSampleQCStatus)
		  Map<Sample, String> qcStatusMap = new HashMap<Sample, String>();
		  Map<Sample, List<MetaMessage>> qcStatusCommentsMap = new HashMap<Sample, List<MetaMessage>>();
		  for(Sample s : allJobSamples){
			  //if(s.getSampleType().getIName().toLowerCase().contains("library")){
			  if(allJobLibraries.contains(s)){//user-submitted libraries and facility-generated libraries
				  qcStatusMap.put(s, sampleService.convertSampleQCStatusForWeb(sampleService.getLibraryQCStatus(s)));
				  qcStatusCommentsMap.put(s, sampleService.getSampleQCComments(s.getId()));
			  }
			  else if (submittedMacromoleculeList.contains(s)){
				  qcStatusMap.put(s, sampleService.convertSampleQCStatusForWeb(sampleService.getSampleQCStatus(s)));
				  qcStatusCommentsMap.put(s, sampleService.getSampleQCComments(s.getId()));
			  }
		  }	 
		  m.addAttribute("qcStatusMap", qcStatusMap);
		  m.addAttribute("qcStatusCommentsMap", qcStatusCommentsMap);
		  
		  //for each submittedMacromolecule, get list of it's facility-generated libraries; also determine if this sampleMacromolecule should have a library made from it
		  Map<Sample, List<Sample>> submittedMacromoleculeFacilityLibraryListMap = new HashMap<Sample, List<Sample>>();
		  Map<Sample, Boolean> createLibraryStatusMap = new HashMap<Sample, Boolean>();
		  for(Sample macromolecule : submittedMacromoleculeList){
			  submittedMacromoleculeFacilityLibraryListMap.put(macromolecule, macromolecule.getChildren());//could also have used sampleService.getFacilityGeneratedLibraries(macromolecule)
			  boolean isSampleWaitingForLibraryCreation = sampleService.isSampleAwaitingLibraryCreation(macromolecule);
			  //logger.debug("setting sample " + macromolecule.getId() + " (" + macromolecule.getName() + ") is waiting for library creation = "+ isSampleWaitingForLibraryCreation);
			  createLibraryStatusMap.put(macromolecule, isSampleWaitingForLibraryCreation);
		  }
		  m.addAttribute("submittedMacromoleculeFacilityLibraryListMap", submittedMacromoleculeFacilityLibraryListMap);
		  m.addAttribute("createLibraryStatusMap", createLibraryStatusMap);
		  
		  //for each submittedLibrary, get list of it's libraries (done for consistency, as this is actually a list of one, the library itself)
		  Map<Sample, List<Sample>> submittedLibrarySubmittedLibraryListMap = new HashMap<Sample, List<Sample>>();
		  for(Sample userSubmittedLibrary : submittedLibraryList){//do this just to get the userSubmitted Library in a list
			  List<Sample> tempUserSubmittedLibraryList = new ArrayList<Sample>();
			  tempUserSubmittedLibraryList.add(userSubmittedLibrary);
			  submittedLibrarySubmittedLibraryListMap.put(userSubmittedLibrary, tempUserSubmittedLibraryList);
		  }
		  m.addAttribute("submittedLibrarySubmittedLibraryListMap", submittedLibrarySubmittedLibraryListMap);

		  //for each submittedObject (a submittedMacromolecule or a submittedLibrary), get species and get receviedStatus
		  Map<Sample, String> submittedObjectOrganismMap = new HashMap<Sample, String>();		  
		  Map<Sample, String> receivedStatusMap = new HashMap<Sample, String>();		 
		  for(Sample submittedObject : submittedObjectList){
			  submittedObjectOrganismMap.put(submittedObject, sampleService.getNameOfOrganism(submittedObject, "???"));
			  receivedStatusMap.put(submittedObject, sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(submittedObject)));
		  }
		  m.addAttribute("submittedObjectOrganismMap", submittedObjectOrganismMap);
		  m.addAttribute("receivedStatusMap", receivedStatusMap);
		  
		  //for each job's library, get its adaptor info and determine whether the library should be assigned to a platformUnit
		  Map<Sample, Adaptorset> libraryAdaptorsetMap = new HashMap<Sample, Adaptorset>();
		  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();
		  Map<Sample, Boolean> assignLibraryToPlatformUnitStatusMap = new HashMap<Sample, Boolean>();
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
		  m.addAttribute("libraryAdaptorsetMap", libraryAdaptorsetMap);
		  m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
		  m.addAttribute("assignLibraryToPlatformUnitStatusMap", assignLibraryToPlatformUnitStatusMap);
		  m.addAttribute("numberOfLibrariesAwaitingPlatformUnitPlacement", numberOfLibrariesAwaitingPlatformUnitPlacement);
		 
		  Map<Sample, List<Sample>> libraryCellListMap = new HashMap<Sample, List<Sample>>();
		  Map<Sample, Integer> cellIndexMap = new HashMap<Sample, Integer>();
		  Map<Sample, Sample> cellPUMap = new HashMap<Sample, Sample>();
		  Map<Sample, Run> cellRunMap = new HashMap<Sample, Run>();	 

		  Map<Sample, Map<Sample, Float>> cellLibraryPMLoadedMap = new HashMap<Sample, Map<Sample, Float>>();
		  
		  Map<Sample, String> showPlatformunitViewMap = new HashMap<Sample, String>();//for displaying web anchor link to platformunit
		  
		  //for each job's library, get cell, platformUnit, and run info
		  for(Sample library : allJobLibraries){
			  List<Sample>  cellsForLibrary = sampleService.getCellsForLibrary(library, job);
			  libraryCellListMap.put(library, cellsForLibrary);
			  for(Sample cell : cellsForLibrary){			  
				  try{
					  cellIndexMap.put(cell, sampleService.getCellIndex(cell));//cell's position on flowcell (ie.: lane 3)
					  Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
					  cellPUMap.put(cell, platformUnit);
					  
					  Float pMLoaded = sampleService.getConcentrationOfLibraryAddedToCell(cell, library);
					  if(cellLibraryPMLoadedMap.containsKey(cell)){
						  cellLibraryPMLoadedMap.get(cell).put(library, pMLoaded);
					  }
					  else{
						  Map<Sample, Float> libraryPMLoadedMap = new HashMap<Sample, Float>();
						  libraryPMLoadedMap.put(library, pMLoaded);
						  cellLibraryPMLoadedMap.put(cell, libraryPMLoadedMap);
					  }
					  
					  showPlatformunitViewMap.put(platformUnit, sampleService.getPlatformunitViewLink(platformUnit));//for displaying web anchor link to platformunit
					  
					  //List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST rather than a singleton?
					  //For testing only:  
					  List<Run> runList = runService.getRunsForPlatformUnit(platformUnit);
					  if(!runList.isEmpty()){
						  Run run = runList.get(0);
						  cellRunMap.put(cell, run);						  
					  }
				  }catch(Exception e){}
			  }
		  }
		  m.addAttribute("libraryCellListMap", libraryCellListMap);
		  m.addAttribute("cellIndexMap", cellIndexMap);
		  m.addAttribute("cellPUMap", cellPUMap); 
		  m.addAttribute("cellRunMap", cellRunMap);
		  m.addAttribute("cellLibraryPMLoadedMap", cellLibraryPMLoadedMap);
		  m.addAttribute("showPlatformunitViewMap", showPlatformunitViewMap); //for displaying web anchor link to platformunit
		 
		  //Next calculations ONLY NEEDED FOR mpsResultsListedBySample.jsp; do NOT remove this part please; needed for proper table display
		  //submittedObjectCellRowspan and submittedObjectCellRowspan
		  //calculate the rowspans needed for the web, as the table display is rather complex, and determining these numbers is very hard to do at the web, as there are multiple dependencies. It is easier to perform here.
		  Map<Sample, Integer> submittedObjectLibraryRowspan = new HashMap<Sample, Integer>();//number of libraries for each submitted Object (be it a submitted macromolecule or a submitted library)
		  Map<Sample, Integer> submittedObjectCellRowspan = new HashMap<Sample, Integer>();//number of runs (zero, one, many) for each library
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
		  m.addAttribute("submittedObjectCellRowspan", submittedObjectCellRowspan);
		  m.addAttribute("submittedObjectLibraryRowspan", submittedObjectLibraryRowspan);  
		  
		  //fill up drop-down box that is used to assign a library to a flow cell's lane
		  List<Sample> availableAndCompatiblePlatformUnitListOnForm = sampleService.getAvailableAndCompatiblePlatformUnits(job);//available flowCells that are compatible with this job
		  m.addAttribute("availableAndCompatiblePlatformUnitListOnForm", availableAndCompatiblePlatformUnitListOnForm);
		  Map<Sample, List<Sample>> platformUnitCellListMapOnForm = new HashMap<Sample, List<Sample>>();
		  Map<Sample, List<Sample>> cellControlLibraryListMapOnForm = new HashMap<Sample, List<Sample>>();
		  Map<Sample, List<Sample>> cellLibraryWithoutControlListMapOnForm = new HashMap<Sample, List<Sample>>();
		  Map<Sample, Adaptorset> libraryAdaptorsetMapOnForm = new HashMap<Sample, Adaptorset>();
		  Map<Sample, Adaptor> libraryAdaptorMapOnForm = new HashMap<Sample, Adaptor>();

		  for(Sample platformUnit : availableAndCompatiblePlatformUnitListOnForm){
			  Map<Integer, Sample> indexedCellsOnPlatformUnitMap = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);
			  
			  List<Sample> cellList = new ArrayList<Sample>();
			  int numberOfIndexedCellsOnPlatformUnit = indexedCellsOnPlatformUnitMap.size();
			  for(int i = 1; i <= numberOfIndexedCellsOnPlatformUnit; i++){
				  Sample cell = indexedCellsOnPlatformUnitMap.get(new Integer(i));
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
				  
				  //for each library on this drop-down list, get its adaptor info
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

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		try {
			sampleService.setLibraryOnCellConcentration(sampleService.getCellLibrary(sampleService.getSampleById(cellId), sampleService.getSampleById(libraryId)), Float.parseFloat(newConcentrationInPM.trim()));
			m.addAttribute("updateConcentrationToCellLibrarySuccessMessage", messageService.getMessage("listJobSamples.updateConcentrationToCellLibrarySuccessMessage.label")); 
		} catch (Exception e) {
			logger.warn("Problem occurred updating library concentration on cell: " + e.getLocalizedMessage());
			m.addAttribute("updateConcentrationToCellLibraryErrorMessage", messageService.getMessage("listJobSamples.updateConcentrationToCellLibraryErrorMessage.error"));
		}
		m.addAttribute("libraryIdAssociatedWithMessage", libraryId);
		getSampleLibraryRunData(job, m);		
		return "job/home/samples";
	}
	
	@RequestMapping(value="/{jobId}/cell/{cellId}/library/{libraryId}/removeLibrary", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobRemoveLibraryFromCellPage(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("cellId") Integer cellId, 
			  @PathVariable("libraryId") Integer libraryId, 
			  ModelMap m) throws SampleTypeException{ 

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		try{
			sampleService.removeLibraryFromCellOfPlatformUnit(sampleService.getSampleById(cellId), sampleService.getSampleById(libraryId));
			m.addAttribute("removeLibraryFromCellSuccessMessage", messageService.getMessage("listJobSamples.removeLibraryFromCellSuccessMessage.label"));
		}
		catch(Exception e){
			logger.warn(e.getMessage() + ": removeLibraryFromCellOfPlatformUnit(cell, library) threw exception");
			m.addAttribute("removeLibraryFromCellErrorMessage", messageService.getMessage("listJobSamples.removeLibraryFromCellErrorMessage.error"));
		}
		m.addAttribute("libraryIdAssociatedWithMessage", libraryId);
		getSampleLibraryRunData(job, m);		
		return "job/home/samples";	
	}
	
	@RequestMapping(value="/{jobId}/library/{libraryId}/addToCell", method=RequestMethod.POST)
	  @PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String jobAddLibraryToCellPostPage(
			  @PathVariable("jobId") Integer jobId, 
			  @PathVariable("libraryId") Integer libraryId, 
			  @RequestParam("cellId") Integer cellId, 
			  @RequestParam("libConcInCellPicoM") String libConcInCellPicoM, ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		Sample cell = sampleService.getSampleById(cellId);
		Sample library = sampleService.getSampleById(libraryId);
		List<Sample> jobLibraries = jobService.getLibraries(job);		
		Float libConcInCellPicoMFloat = 0.0f;

		String addLibraryToPlatformUnitErrorMessage = null;

		if(cellId == null || cellId == 0){//user selected a flowcell from dropdown box (parameter cellSampleId == 0); we should actually prevent this with javascript
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
		
		if( addLibraryToPlatformUnitErrorMessage==null ){
			try{
				  sampleService.addLibraryToCell(cell, library, libConcInCellPicoMFloat, job);
				  m.addAttribute("addLibraryToPlatformUnitSuccessMessage", messageService.getMessage("platformunit.libAdded.success"));
			} catch(SampleTypeException ste){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.sampleType.error");
				//logger.warn(ste.getMessage()); // print more detailed error to warn logs
			} catch(SampleMultiplexException sme){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.multiplex.error");
				//logger.warn(sme.getMessage()); // print more detailed error to debug logs
			} catch(SampleException se){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorNotFound.error");
				//logger.warn(se.getMessage()); // print more detailed error to warn logs
			} catch(MetadataException me){
				addLibraryToPlatformUnitErrorMessage = messageService.getMessage("platformunit.adaptorBarcodeNotFound.error");
				//logger.warn(me.getMessage()); // print more detailed error to warn logs
			}	
		}

		if( addLibraryToPlatformUnitErrorMessage!=null && addLibraryToPlatformUnitErrorMessage.length()>0 ){
			m.addAttribute("addLibraryToPlatformUnitErrorMessage", addLibraryToPlatformUnitErrorMessage);
			logger.warn(addLibraryToPlatformUnitErrorMessage);
		}
		m.addAttribute("libraryIdAssociatedWithMessage", libraryId);
		getSampleLibraryRunData(job, m);		
		return "job/home/samples";	
	}
	
	@RequestMapping(value="/{jobId}/macromolecule/{macromolSampleId}/createLibrary", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')")
	  public String jobCreateLibraryFromMacromoleculePage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("macromolSampleId") Integer macromolSampleId, 
			  @RequestParam(value="errorMessage", required=false) String errorMessage,
			  ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		m.addAttribute("job", job);
		
		Sample macromoleculeSample = null;
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		for(Sample sample : allJobSamples){
			if(sample.getId().intValue()==macromolSampleId){
				macromoleculeSample = sample;
				break;
			}
		}
		if(macromoleculeSample==null){
			logger.warn("Macromolecule sample unexpectedly not part of this job");
		   	m.addAttribute("errorMessage", "Macromolecule sample unexpectedly not part of this job"); 
			return "job/home/message";
		}		
		m.addAttribute("macromoleculeSample", macromoleculeSample);
		m.addAttribute("organism", sampleService.getNameOfOrganism(macromoleculeSample, "Other"));
		
		String[] roles = {"ft"};
		List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(job.getWorkflow().getId(), roles, "library");
		if(librarySampleSubtypes.isEmpty()){
			errorMessage="Unexpected Error: sampleSubtype Not Found";
			m.addAttribute("errorMessage", errorMessage);
			return "job/home/message";
		}
		SampleSubtype librarySampleSubtype = librarySampleSubtypes.get(0); // should be one
		List<SampleMeta> libraryMeta = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(librarySampleSubtype);
		prepareAdaptorsetsAndAdaptors(job, libraryMeta, m);
		
		Sample library = new Sample();
		library.setSampleSubtype(librarySampleSubtype);
		library.setSampleType(sampleService.getSampleTypeDao().getSampleTypeByIName("facilityLibrary"));
		library.setSampleMeta(libraryMeta);
		m.addAttribute("sample", library); 
			
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
		  
			Job job = jobService.getJobByJobId(jobId);
			if(job.getId()==null){
			   	logger.warn("Job unexpectedly not found");
			   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
				return "job/home/message";
			}
			m.addAttribute("job", job);
			
			Sample parentMacromolecule = null;
			List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
			for(Sample sample : allJobSamples){
				if(sample.getId().intValue()==macromolSampleId){
					parentMacromolecule = sample;
					break;
				}
			}
			if(parentMacromolecule==null){
				logger.warn("Macromolecule sample unexpectedly not part of this job");
			   	m.addAttribute("errorMessage", "Macromolecule sample unexpectedly not part of this job"); 
				return "job/home/message";
			}	
		  
		  
		  try{			  
				Sample libraryForm = JsonHelperWebapp.constructInstanceFromJson(jsonString, Sample.class);//this is set to silently ignore any paramaters that are not part of a Sample
				libraryForm.setName(libraryForm.getName().trim());//from the form

				//programmatically validate constraints on Sample attributes (note: will NOT examine sample.metadata); see http://static.springsource.org/spring/docs/3.0.0.RC3/reference/html/ch05s07.html (see part 5.7.3 Configuring a DataBinder)
				WebDataBinder binder = new WebDataBinder(libraryForm, "sample");//setting the objectName to "sample" is very important
				initBinder(binder);//WaspController method that sets the validator to an autowired BeanValidator. This call replaces: binder.setValidator(validator);
				//binder.bind(propertyValues); //Don't know how to set this, but apparently works fine (at least in this case) without it. Perhaps the validator from WASPController has already dealt with this step??
				binder.validate();// validate the target object, in this case, sampleForm
				BindingResult result = binder.getBindingResult(); // get BindingResult that currently includes any validation errors from the Sample validation, and also use it later for additional, direct, validations (such as validating metadata)
				//perform an additional validation of Sample to make sure that Sample.name is unique within this job
				//TODO: Actually, if samples are shared between  jobs, then we should also extend the validation to include ALL jobs this sample is a part of
				sampleService.validateSampleNameUniqueWithinJob(libraryForm.getName(), new Integer(-1), job, result);
				
				//retrieve Sample.metadata from the form AND validate it too
				List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromJsonAndTemplateToSubtype(JsonHelperWebapp.constructMapFromJson(jsonString), sampleService.getSampleSubtypeById(libraryForm.getSampleSubtypeId()), result); 
							
				if(result.hasErrors()){
					libraryForm.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId()));
					libraryForm.setSampleSubtype(sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()));
					libraryForm.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(libraryForm.getSampleSubtype(), metaFromForm));
					m.put("sample", libraryForm);
					m.put("job", job);
					m.put("macromoleculeSample", parentMacromolecule);
					prepareAdaptorsetsAndAdaptors(job, libraryForm.getSampleMeta(), m);
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
				sampleService.createFacilityLibraryFromMacro(job, managedLibraryFromForm, metaFromForm);
				String successMessage = "New Library Successfully Created";
				int newLibraryId = managedLibraryFromForm.getSampleObject().getId().intValue();
				return "redirect:/job/"+jobId+"/library/"+newLibraryId+"/librarydetail_ro.do?successMessage="+successMessage;			  
		  }
		  catch(Exception e){
				String errorMessage = "Creation Of New Library Record Failed: Unexpected Error";
				logger.warn(errorMessage);
			   	m.addAttribute("errorMessage", errorMessage); 
				return "job/home/message";
		  }
	 }
	
	@RequestMapping(value="/{jobId}/sample/{sampleId}/sampledetail_ro", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
	  public String jobSampleDetailPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("sampleId") Integer sampleId, 
			  @RequestParam(value="successMessage", required=false) String successMessage,//used when a macromolecule sample is successfully updated
			  ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		m.addAttribute("job", job);
		
		Sample theRequestedSample = null;
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		for(Sample sample : allJobSamples){
			if(sample.getId().intValue()==sampleId){
				theRequestedSample = sample;
				break;
			}
		}
		if(theRequestedSample==null){
			logger.warn("Sample unexpectedly not part of this job");
		   	m.addAttribute("errorMessage", "Sample unexpectedly not part of this job"); 
			return "job/home/message";
		}
				
		SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(theRequestedSample);
		m.addAttribute("normalizedSampleMeta", SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(theRequestedSample.getSampleSubtype(), sampleManaged.getAllSampleMeta()) );
		m.addAttribute("organisms", sampleService.getOrganismsPlusOther());//needed for the organism meta to be interpreted properly during metadata display
		
		m.addAttribute("sample", theRequestedSample);
		
		if(successMessage==null){
			successMessage="";
		}
		m.addAttribute("successMessage", successMessage);
		
		return "job/home/sampledetail_ro";
	}
	@RequestMapping(value="/{jobId}/sample/{sampleId}/sampledetail_rw", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')") /* or hasRole('jv-' + #jobId) */
	  public String jobSampleDetailRWPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("sampleId") Integer sampleId, 
			  ModelMap m) throws SampleTypeException {

		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		m.addAttribute("job", job);
		
		Sample theRequestedSample = null;
		List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
		for(Sample sample : allJobSamples){
			if(sample.getId().intValue()==sampleId){
				theRequestedSample = sample;
				break;
			}
		}
		if(theRequestedSample==null){
			logger.warn("Sample unexpectedly not part of this job");
		   	m.addAttribute("errorMessage", "Sample unexpectedly not part of this job"); 
			return "job/home/message";
		}
				
		SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(theRequestedSample);
		m.addAttribute("normalizedSampleMeta", SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(theRequestedSample.getSampleSubtype(), sampleManaged.getAllSampleMeta()) );
		m.addAttribute("organisms", sampleService.getOrganismsPlusOther());//needed for the organism meta to be interpreted properly during metadata display
		
		m.addAttribute("sample", theRequestedSample);
		
		return "job/home/sampledetail_rw";
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
		
		Job job = jobService.getJobByJobId(jobId);
		if(job.getId()==null){
		   	logger.warn("Job unexpectedly not found");
		   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
			return "job/home/message";
		}
		
		boolean sampleIsPartOfJob = false;		
		for(Sample sample : job.getSample()){//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
			if(sample.getId().intValue()==sampleId){
				sampleIsPartOfJob = true;
				break;
			}
		}
		if(!sampleIsPartOfJob){
			logger.warn("Sample unexpectedly not part of this job");
		   	m.addAttribute("errorMessage", "Sample unexpectedly not part of this job"); 
			return "job/home/message";
		}

		try{
			Sample sampleForm = JsonHelperWebapp.constructInstanceFromJson(jsonString, Sample.class);//this is set to silently ignore any paramaters that are not part of a Sample
			sampleForm.setName(sampleForm.getName().trim());//from the form

			//programmatically validate constraints on Sample attributes (note: will NOT examine sample.metadata); see http://static.springsource.org/spring/docs/3.0.0.RC3/reference/html/ch05s07.html (see part 5.7.3 Configuring a DataBinder)
			WebDataBinder binder = new WebDataBinder(sampleForm, "sample");//setting the objectName to "sample" is very important
			initBinder(binder);//WaspController method that sets the validator to an autowired BeanValidator. This call replaces: binder.setValidator(validator);
			//binder.bind(propertyValues); //Don't know how to set this, but apparently works fine (at least in this case) without it. Perhaps the validator from WASPController has already dealt with this step??
			binder.validate();// validate the target object, in this case, sampleForm
			BindingResult result = binder.getBindingResult(); // get BindingResult that currently includes any validation errors from the Sample validation, and also use it later for additional, direct, validations (such as validating metadata)
			//next, validate Sample to make sure that Sample.name is unique within this job
			//TODO: Actually, if samples are shared between  jobs, then we should also extend the validation to include ALL jobs this sample is a part of
			sampleService.validateSampleNameUniqueWithinJob(sampleForm.getName(), sampleId, job, result);
			
			//retrieve Sample.metadata from the form AND validate it too
			List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromJsonAndTemplateToSubtype(JsonHelperWebapp.constructMapFromJson(jsonString), sampleService.getSampleSubtypeById(sampleForm.getSampleSubtypeId()), result); 
									
			if(result.hasErrors()){
				sampleForm.setId(sampleId);//since this sampleForm was created thru json, it curretly lacks id
				sampleForm.setSampleType(sampleService.getSampleTypeDao().getSampleTypeBySampleTypeId(sampleForm.getSampleTypeId()));
				sampleForm.setSampleSubtype(sampleService.getSampleSubtypeDao().getSampleSubtypeBySampleSubtypeId(sampleForm.getSampleSubtypeId()));
				m.put("job", job);
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
			String successMessage = "Update Successfully Completed";
			return "redirect:/job/"+jobId+"/sample/"+sampleId+"/sampledetail_ro.do?successMessage="+successMessage;

		}catch(Exception e){
			String errorMessage = "Update Failed: Unexpected Error";
			logger.warn(e.getMessage() + ": " + errorMessage);
		   	m.addAttribute("errorMessage", errorMessage); 
			return "job/home/message";
		}
	}
		
	 
	  @RequestMapping(value="/{jobId}/library/{libraryId}/librarydetail_ro", method=RequestMethod.GET)
		  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
		  public String jobLibraryDetailPage(@PathVariable("jobId") Integer jobId, 
				  @PathVariable("libraryId") Integer libraryId, 
				  @RequestParam(value="successMessage", required=false) String successMessage,//used to display update success message 
				  ModelMap m) throws SampleTypeException {

		  	Job job = jobService.getJobByJobId(jobId);
			if(job.getId()==null){
			   	logger.warn("Job unexpectedly not found");
			   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
				return "job/home/message";
			}
			
			boolean sampleIsPartOfJob = false;		
			for(Sample sample : job.getSample()){//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
				if(sample.getId().intValue()==libraryId){
					sampleIsPartOfJob = true;
					break;
				}
			}
			if(!sampleIsPartOfJob){
				logger.warn("Sample unexpectedly not part of this job");
			   	m.addAttribute("errorMessage", "Sample unexpectedly not part of this job"); 
				return "job/home/message";
			}
			
			try{
				libraryDetail(jobId, libraryId, m);
			}catch(Exception e){
				logger.warn(e.getMessage());
			   	m.addAttribute("errorMessage", e.getMessage()); 
				return "job/home/message";				
			}			
			
			if(successMessage==null){
				successMessage="";
			}
			m.addAttribute("successMessage", successMessage);//only coming from CreateLibrary and update the library; do not ever set this 
			return "job/home/librarydetail_ro";
		}
	  @RequestMapping(value="/{jobId}/library/{libraryId}/librarydetail_rw", method=RequestMethod.GET)
	  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*')") /* or hasRole('jv-' + #jobId) */
	  public String jobLibraryDetailRWPage(@PathVariable("jobId") Integer jobId, 
			  @PathVariable("libraryId") Integer libraryId, ModelMap m) throws SampleTypeException {

		  Job job = jobService.getJobByJobId(jobId);
			if(job.getId()==null){
			   	logger.warn("Job unexpectedly not found");
			   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
				return "job/home/message";
			}
			
			boolean sampleIsPartOfJob = false;		
			for(Sample sample : job.getSample()){//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
				if(sample.getId().intValue()==libraryId){
					sampleIsPartOfJob = true;
					break;
				}
			}
			if(!sampleIsPartOfJob){
				logger.warn("Sample unexpectedly not part of this job");
			   	m.addAttribute("errorMessage", "Sample unexpectedly not part of this job"); 
				return "job/home/message";
			}
			
			try{
				libraryDetail(jobId, libraryId, m);
			}catch(Exception e){
				logger.warn(e.getMessage());
			   	m.addAttribute("errorMessage", e.getMessage()); 
				return "job/home/message";				
			}		
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
			
			Job job = jobService.getJobByJobId(jobId);
			if(job.getId()==null){
			   	logger.warn("Job unexpectedly not found");
			   	m.addAttribute("errorMessage", messageService.getMessage("job.jobUnexpectedlyNotFound.error")); 
				return "job/home/message";
			}
			
			boolean sampleIsPartOfJob = false;		
			for(Sample sample : job.getSample()){//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
				if(sample.getId().intValue()==libraryId){
					sampleIsPartOfJob = true;
					break;
				}
			}
			if(!sampleIsPartOfJob){
				logger.warn("Sample unexpectedly not part of this job");
			   	m.addAttribute("errorMessage", "Sample unexpectedly not part of this job"); 
				return "job/home/message";
			}
			
			try{				
				Sample libraryForm = JsonHelperWebapp.constructInstanceFromJson(jsonString, Sample.class);//this is set to silently ignore any paramaters that are not part of a Sample
				libraryForm.setName(libraryForm.getName().trim());//from the form

				//programmatically validate constraints on Sample attributes (note: will NOT examine sample.metadata); see http://static.springsource.org/spring/docs/3.0.0.RC3/reference/html/ch05s07.html (see part 5.7.3 Configuring a DataBinder)
				WebDataBinder binder = new WebDataBinder(libraryForm, "sample");//setting the objectName to "sample" is very important
				initBinder(binder);//WaspController method that sets the validator to an autowired BeanValidator. This call replaces: binder.setValidator(validator);
				//binder.bind(propertyValues); //Don't know how to set this, but apparently works fine (at least in this case) without it. Perhaps the validator from WASPController has already dealt with this step??
				binder.validate();// validate the target object, in this case, sampleForm
				BindingResult result = binder.getBindingResult(); // get BindingResult that currently includes any validation errors from the Sample validation, and also use it later for additional, direct, validations (such as validating metadata)
				//perform an additional validation of Sample to make sure that Sample.name is unique within this job
				//TODO: Actually, if samples are shared between  jobs, then we should also extend the validation to include ALL jobs this sample is a part of
				sampleService.validateSampleNameUniqueWithinJob(libraryForm.getName(), libraryId, job, result);
				
				//retrieve Sample.metadata from the form AND validate it too
				List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromJsonAndTemplateToSubtype(JsonHelperWebapp.constructMapFromJson(jsonString), sampleService.getSampleSubtypeById(libraryForm.getSampleSubtypeId()), result); 
										
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
				String successMessage = "Update Successfully Completed";
				return "redirect:/job/"+jobId+"/library/"+libraryId+"/librarydetail_ro.do?successMessage="+successMessage;

			}catch(Exception e){
				String errorMessage = "Update Failed: Unexpected Error";
				logger.warn(e.getMessage() + ": " + errorMessage);
			   	m.addAttribute("errorMessage", errorMessage); 
				return "job/home/message";
			}
	}
				
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
