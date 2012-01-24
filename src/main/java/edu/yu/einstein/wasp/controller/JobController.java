package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.model.*;
import edu.yu.einstein.wasp.service.*;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.StringHelper;

@Controller
@Transactional
@RequestMapping("/job")
public class JobController extends WaspController {

	private JobService	jobService;

	@Autowired
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

	public JobService getJobService() {
		return this.jobService;
	}

	private JobUserService	jobUserService;

	@Autowired
	public void setJobUserService(JobUserService jobUserService) {
		this.jobUserService = jobUserService;
	}

	public JobUserService getJobUserService() {
		return this.jobUserService;
	}

	private RoleService	roleService;

	@Autowired
	public void setJobUserService(RoleService roleService) {
		this.roleService = roleService;
	}

	public RoleService getRoleUserService() {
		return this.roleService;
	}

	@Autowired
	private TaskService		taskService;
	@Autowired
	private StateService	stateService;
	@Autowired
	private WorkflowresourcecategoryService workflowresourcecategoryService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("job", JobMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {
		//List<Job> jobList = this.getJobService().findAll();

		//m.addAttribute("job", jobList);

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "job/list";
	}

	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public String getListJSON(HttpServletResponse response) {
		
		String search = request.getParameter("_search");
		String searchStr = request.getParameter("searchString");
	
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		
		String userId = request.getParameter("userId");
		String labId = request.getParameter("labId");
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<Job> jobList;
		
		if (!search.equals("true")	&& userId.isEmpty()	&& labId.isEmpty()) {
			jobList = sidx.isEmpty() ? this.jobService.findAll() : this.userService.findAllOrderBy(sidx, sord);
		} else {
			  Map m = new HashMap();
			  
			  if (search.equals("true") && !searchStr.isEmpty())
				  m.put(request.getParameter("searchField"), request.getParameter("searchString"));
			  
			  if (!userId.isEmpty())
				  m.put("UserId", Integer.parseInt(userId));
			  
			  if (!labId.isEmpty())
				  m.put("labId", Integer.parseInt(labId));
			  				  
			  jobList = this.jobService.findByMap(m);
		}

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
			 
			List<Map> rows = new ArrayList<Map>();
			
			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = jobList.indexOf(jobService.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<Job> jobPage = jobList.subList(frId, toId);
			for (Job job:jobPage) {
				Map cell = new HashMap();
				cell.put("id", job.getJobId());
				 
				List<JobMeta> jobMeta = getMetaHelperWebapp().syncWithMaster(job.getJobMeta());
				
				User user = userService.getById(job.getUserId());
				 					
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							job.getName(),
							user.getFirstName() + " " + user.getLastName(),
							job.getLab().getName(),
							job.getIsActive().intValue()==1?"yes":"no"
				}));
				 
				for (JobMeta meta:jobMeta) {
					cellList.add(meta.getV());
				}
				
				 
				cell.put("cell", cellList);
				 
				rows.add(cell);
			}

			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			 
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jobList,e);
		}
	
	}

	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or User.login == principal.name")
	public String subgridJSON(@RequestParam("id") Integer jobId,ModelMap m, HttpServletResponse response) {
				
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		Job job = this.jobService.getById(jobId);
		
		List<JobSample> jobSampleList = job.getJobSample();
		
	 	ObjectMapper mapper = new ObjectMapper();
	 	
		try {
			List<Map> rows = new ArrayList<Map>();
			for (JobSample jobSample:jobSampleList) {
				// Only show the "biomaterial" type of samples within the job
				if (jobSample.getSample().getTypeSample().getTypeSampleCategory().getIName().equals("biomaterial"))
				{
					Map cell = new HashMap();
					cell.put("id", jobSample.getSampleId());
					 					
					List<String> cellList = new ArrayList<String>(
							Arrays.asList(
									new String[] {
											"<a href=/wasp/sample/list.do?selId=" 
											+ jobSample.getSampleId().intValue() + ">" + 
												jobSample.getSample().getName() + "</a>"
									}
							)
					);
					 
					cell.put("cell", cellList);
					 
					rows.add(cell);
				}
			}
			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON " + jobSampleList, e);
		 }
	
	}
	
	@RequestMapping(value = "/detail/{jobId}", method = RequestMethod.GET)
	public String detail(@PathVariable("jobId") Integer jobId, ModelMap m) {
		String now = (new Date()).toString();

		Job job = this.getJobService().getById(jobId);

		List<JobMeta> jobMetaList = job.getJobMeta();
		jobMetaList.size();

		List<JobSample> jobSampleList = job.getJobSample();
		jobSampleList.size();

		List<JobFile> jobFileList = job.getJobFile();
		jobFileList.size();

		List<JobUser> jobUserList = job.getJobUser();
		jobUserList.size();

		List<Statejob> stateJobList = job.getStatejob();
		stateJobList.size();

		m.addAttribute("now", now);
		m.addAttribute("job", job);
		m.addAttribute("jobmeta", jobMetaList);
		m.addAttribute("jobsample", jobSampleList);
		m.addAttribute("jobfile", jobFileList);
		m.addAttribute("jobuser", jobUserList);
		m.addAttribute("statejob", stateJobList);

		return "job/detail";
	}

  @RequestMapping(value="/user/roleAdd", method=RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('lm-' + #labId) or hasRole('js-' + #jobId)")
  public String jobViewerUserRoleAdd (
      @RequestParam("labId") Integer labId,
      @RequestParam("jobId") Integer jobId,
      @RequestParam("login") String login, //10-11-11 changed from useremail to login, AND 10-20-11 changed login format from jgreally to the AJAX-generated and formatted login of John Greally (jgreally), so must now extract the login from the formatted string 
      ModelMap m) {
 
	Job job = this.jobService.findById(jobId);
	if(job.getJobId() == null || job.getLabId().intValue() != labId.intValue()){
		waspMessage("job.jobViewerUserRoleAdd.error1");//this job not found in database or the labId does not belong to this job
	}
	else{   
		String extractedLogin = StringHelper.getLoginFromFormattedNameAndLogin(login);
		User user = userService.getUserByLogin(extractedLogin);
		if(user.getUserId() == null){
			waspMessage("job.jobViewerUserRoleAdd.error2");//user login name does not exist
		}
		else{
			//check that login does not belong to the job submitter (or is not already a job-viewer)
			JobUser jobUser = this.jobUserService.getJobUserByJobIdUserId(jobId, user.getUserId());
			if(jobUser.getJobUserId() != null){
				if( "js".equals( jobUser.getRole().getRoleName() ) ){
					waspMessage("job.jobViewerUserRoleAdd.error3");//user is submitter (and thus is, by default, a job-viewer)
				}
				else if( "jv".equals( jobUser.getRole().getRoleName() ) ){
					waspMessage("job.jobViewerUserRoleAdd.error4");//user is already a job-viewer
				}
			}
			else{
				Role role = roleService.getRoleByRoleName("jv");
			    JobUser jobUser2 = new JobUser();
			    jobUser2.setJobId(jobId);
			    jobUser2.setUserId(user.getUserId());
			    jobUser2.setRoleId(role.getRoleId());
			    jobUserService.save(jobUser2);
			}
		}
	}

    return "redirect:/job/detail/" + jobId + ".do";
  }


  @RequestMapping(value="/user/roleRemove/{labId}/{jobId}/{userId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('lm-' + #labId)")
  public String departmentUserRoleRemove (
      @PathVariable("labId") Integer labId,
      @PathVariable("jobId") Integer jobId,
      @PathVariable("userId") Integer userId,
      ModelMap m) {
  
    JobUser jobUser = jobUserService.getJobUserByJobIdUserId(jobId, userId);

    // todo check job within lab
    // check user is just a job viewer

    jobUserService.remove(jobUser);

    return "redirect:/job/detail/" + jobId + ".do";
  }

  @RequestMapping(value = "/pending/detail_ro/{deptId}/{labId}/{jobId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId) or hasRole('lm-' + #labId) or hasRole('pi-' + #labId)")
	public String pendingDetailRO(@PathVariable("deptId") Integer deptId,@PathVariable("labId") Integer labId,
			@PathVariable("jobId") Integer jobId, ModelMap m) {
	  
	  String now = (new Date()).toString();


	    Job job = this.getJobService().getById(jobId);

	    List<JobMeta> jobMetaList = job.getJobMeta();
	    jobMetaList.size();

	    List<JobSample> jobSampleList = job.getJobSample();
	    jobSampleList.size();

	    List<JobFile> jobFileList = job.getJobFile();
	    jobFileList.size();

	    List<JobUser> jobUserList = job.getJobUser();
	    jobUserList.size();

	    List<Statejob> stateJobList = job.getStatejob();
	    stateJobList.size();

	    m.addAttribute("now", now);
	    m.addAttribute("job", job);
	    m.addAttribute("jobmeta", jobMetaList);
	    m.addAttribute("jobsample", jobSampleList);
	    m.addAttribute("jobfile", jobFileList);
	    m.addAttribute("jobuser", jobUserList);
	    m.addAttribute("statejob", stateJobList);
	   // m.addAttribute("actingasrole", actingAsRole);
	    
	    return "job/pendingjob/detail_ro";
  }
  
  @RequestMapping(value = "/allpendinglmapproval/{action}/{labId}/{jobId}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('lm-' + #labId) or hasRole('pi-' + #labId)")
	public String allPendingLmApproval(@PathVariable("action") String action, @PathVariable("labId") Integer labId, @PathVariable("jobId") Integer jobId, ModelMap m) {
	  
	  pendingJobApproval(action, jobId, "LM");//could use PI instead of LM
	  
	  return "redirect:/lab/allpendinglmapproval/list.do";	
	}
  
  @RequestMapping(value = "/pendinglmapproval/{action}/{labId}/{jobId}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('lm-' + #labId) or hasRole('pi-' + #labId)")
	public String pendingLmApproval(@PathVariable("action") String action, @PathVariable("labId") Integer labId, @PathVariable("jobId") Integer jobId, ModelMap m) {
	  
	  pendingJobApproval(action, jobId, "LM");//could use PI instead of LM
	  
	  return "redirect:/lab/pendinglmapproval/list/" + labId + ".do";	
	}
  
  @RequestMapping(value = "/pendingdaapproval/{action}/{deptId}/{jobId}.do", method = RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('da-' + #deptId)")
	public String pendingDaApproval(@PathVariable("action") String action, @PathVariable("deptId") Integer deptId, @PathVariable("jobId") Integer jobId, ModelMap m) {
	  
	  pendingJobApproval(action, jobId, "DA");//private method below
	  return "redirect:/department/dapendingtasklist.do";		  
	}
 
  private void pendingJobApproval(String action, Integer jobId, String approver){
	  //logger.debug("ROBERT DUBIN : ACTION: " + action);
	  //logger.debug("ROBERT DUBIN : APPROVER: " + approver);
	 // logger.debug("ROBERT DUBIN : Job ID: " + jobId);
	  Task taskOfInterest;
	  boolean updated = false;
	  //confirm action is either approve or reject
	  Job job = jobService.getJobByJobId(jobId);//confirm id > 0
	  if("DA".equals(approver)){
		  taskOfInterest = taskService.getTaskByIName("DA Approval");//confirm id > 0
	  }
	  else if("LM".equals(approver) || "PI".equals(approver)){
		 taskOfInterest = taskService.getTaskByIName("PI Approval");//confirm id > 0
	  }
	  else{
		  taskOfInterest = taskService.getTaskByIName("");//should never get here
		  waspMessage("job.approval.error"); 
		  return;
	  }
	  List<Statejob> statejobList = job.getStatejob();
	  for(Statejob statejob : statejobList){
		  State state = statejob.getState();
		  if(taskOfInterest.getTaskId()==state.getTaskId()){
			  if("approve".equals(action)){
				  state.setStatus("APPROVED");
				  stateService.save(state);
				  //logger.debug("ROBERT DUBIN : State saved to approved for stateId: " + state.getStateId());
				  updated = true; waspMessage("job.approval.approved"); break;
			  }
			  else if("reject".equals(action)){
				  state.setStatus("REJECTED");
				  stateService.save(state); 
				 // logger.debug("ROBERT DUBIN : State saved to rejected for stateId: " + state.getStateId());
				  updated = true; waspMessage("job.approval.rejected"); break;
			  }			 
		  }
	  }
	  
	 // flow returns to calling method, either pendingDaApproval or pendingLmApproval
  }



	/**
	 * show job/resource data and meta information to be modified.
	 */
	@RequestMapping(value = "/meta/{jobId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm')") 
	public String showJobMetaForm(
		@PathVariable("jobId") Integer jobId, 
			ModelMap m) {
		Job job = jobService.getJobByJobId(jobId);

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
				Workflowresourcecategory workflowresourcecategory = workflowresourcecategoryService.getWorkflowresourcecategoryByWorkflowIdResourcecategoryId(job.getWorkflow().getWorkflowId(), resourceCategory.getResourceCategoryId());

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
}
