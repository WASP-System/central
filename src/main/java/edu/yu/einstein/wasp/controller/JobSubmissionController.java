package edu.yu.einstein.wasp.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.controller.validator.MetaHelper;
import edu.yu.einstein.wasp.dao.impl.DBResourceBundle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCell;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCell;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftresource;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResource;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleCell;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftCell;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.Workflowresource;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobCellService;
import edu.yu.einstein.wasp.service.JobDraftCellService;
import edu.yu.einstein.wasp.service.JobDraftMetaService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobDraftresourceService;
import edu.yu.einstein.wasp.service.JobMetaService;
import edu.yu.einstein.wasp.service.JobResourceService;
import edu.yu.einstein.wasp.service.JobSampleService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.service.LabService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.SampleCellService;
import edu.yu.einstein.wasp.service.SampleDraftCellService;
import edu.yu.einstein.wasp.service.SampleDraftMetaService;
import edu.yu.einstein.wasp.service.SampleDraftService;
import edu.yu.einstein.wasp.service.SampleFileService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatejobService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/jobsubmit")
public class JobSubmissionController extends WaspController {

	@Autowired
	protected JobDraftService jobDraftService;

	@Autowired
	protected JobDraftMetaService jobDraftMetaService;

	@Autowired
	protected JobDraftCellService jobDraftCellService;

	@Autowired
	protected JobDraftresourceService jobDraftresourceService;

	@Autowired
	protected JobResourceService jobResourceService;

	@Autowired
	protected SampleDraftService sampleDraftService;

	@Autowired
	protected SampleDraftMetaService sampleDraftMetaService;

	@Autowired
	protected SampleDraftCellService sampleDraftCellService;


	@Autowired
	protected JobService jobService;

	@Autowired
	protected LabService labService;

	@Autowired
	protected JobUserService jobUserService;

	@Autowired
	protected RoleService roleService;

	@Autowired
	protected ResourceService resourceService;

	@Autowired
	protected TypeResourceService typeResourceService;

	@Autowired
	protected JobMetaService jobMetaService;

	@Autowired
	protected SampleService sampleService;

	@Autowired
	protected SampleMetaService sampleMetaService;

	@Autowired
	protected SampleFileService sampleFileService;

	@Autowired
	protected JobSampleService jobSampleService;
	
	@Autowired
	protected TypeSampleService typeSampleService;

	@Autowired
	protected StatejobService statejobService;

	@Autowired
	protected StateService stateService;

	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected SubtypeSampleService subTypeSampleService;
	
	@Autowired
	protected WorkflowService workflowService;

	@Autowired
	protected File sampleDir;
	
	@Autowired
	protected FileService fileService;

	@Autowired
	protected JobCellService jobCellService;

	@Autowired
	protected java.net.URI jobRunnerHost;
	
	@Autowired
	protected SampleCellService sampleCellService;
	
	@Autowired
	protected MessageService messageService;
		
	@Autowired
	protected AuthenticationService authenticationService;

	protected final MetaHelper getMetaHelper() {
		return new MetaHelper("jobDraft", JobDraftMeta.class, request.getSession());
	}
	
	final public String defaultPageFlow = "/jobsubmit/modifymeta/{n};/jobsubmit/samples/{n};/jobsubmit/cells/{n};/jobsubmit/verify/{n};/jobsubmit/submit/{n};/jobsubmit/ok";

	public String nextPage(JobDraft jobDraft) {
		String pageFlow = this.defaultPageFlow;

		try {
			List<WorkflowMeta> wfmList = jobDraft.getWorkflow().getWorkflowMeta();
			for (WorkflowMeta wfm : wfmList) {
				if (wfm.getK().equals("workflow.submitpageflow")) {
					pageFlow = wfm.getV();
					break;
			}
		}
		} catch (Exception e) {
		}

		String context = request.getContextPath();
		String uri = request.getRequestURI();
	
		// strips context, lead slash ("/"), spring mapping
		String currentMapping = uri.replaceFirst(context, "").replaceFirst("\\.do.*$", "");


		String pageFlowArray[] = pageFlow.split(";");

		int found = -1;
		for (int i=0; i < pageFlowArray.length -1; i++) {
			String page = pageFlowArray[i];
			page = page.replaceAll("\\{n\\}", ""+jobDraft.getJobDraftId());
	
			if (currentMapping.equals(page)) {
				found = i;
				break;
			}
		}


		String targetPage = pageFlowArray[found+1] + ".do"; 

		targetPage = targetPage.replaceAll("\\{n\\}", ""+jobDraft.getJobDraftId());

		return "redirect:" + targetPage;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public String list(ModelMap m) {
		User me = authenticationService.getAuthenticatedUser();

		Map jobDraftQueryMap = new HashMap();
		jobDraftQueryMap.put("UserId", me.getUserId());
		jobDraftQueryMap.put("status", "PENDING");

		List<JobDraft> jobDraftList = jobDraftService.findByMap(jobDraftQueryMap);

		m.put("jobdrafts", jobDraftList); 

		return "jobsubmit/list";
	}

	protected void generateCreateForm(ModelMap m) {
		User me = authenticationService.getAuthenticatedUser();

		List <LabUser> labUserAllRoleList = me.getLabUser();

		List <Lab> labList = new ArrayList();
		for (LabUser lu: labUserAllRoleList) {
			String roleName =	lu.getRole().getRoleName();

			if (roleName.equals("lu") ||
					roleName.equals("lm") ||
					roleName.equals("pi")) {
				labList.add(lu.getLab());
			}
		}


		// filter active
		Map workflowQueryMap = new HashMap();
		m.put("isActive", 1);
		List <Workflow> workflowList = workflowService.findByMap(workflowQueryMap);

		m.put("labs", labList); 
		m.put("workflows", workflowList); 
	}

	@RequestMapping(value="/create.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public String showCreateForm(ModelMap m) {

		generateCreateForm(m);
		m.put("jobDraft", new JobDraft()); 

		return "jobsubmit/create";
	}

	@RequestMapping(value="/create.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('lu-*')")
	public String create (
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		User me = authenticationService.getAuthenticatedUser();

		jobDraftForm.setUserId(me.getUserId());
		jobDraftForm.setStatus("PENDING");
		jobDraftForm.setCreatets(new Date());

		String rt = doModify(jobDraftForm, result, status, m); 

		// Adds the jobdraft to authorized list
 		doReauth();

		return rt;
	}

	@RequestMapping(value="/modify/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modify(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
	
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		// check if i am the drafter
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() != jobDraft.getUserId().intValue()) {
			return "hello";
		}

		// check that the status is PENDING
		if (! jobDraft.getStatus().equals("PENDING")) {
			return "hello";
		}

		generateCreateForm(m);
		m.put("jobDraft", jobDraft);

		return "jobsubmit/create";
	}


	@RequestMapping(value="/modify/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modify (
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		JobDraft jobDraftDb = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		// check if i am the drafter
		User me = authenticationService.getAuthenticatedUser();
		if (me.getUserId().intValue() != jobDraftDb.getUserId().intValue()) {
			return "hello";
		}

		if (! jobDraftDb.getStatus().equals("PENDING")) {
			return "hello";
		}

		jobDraftDb.setName(jobDraftForm.getName());
		jobDraftDb.setWorkflowId(jobDraftForm.getWorkflowId());
		jobDraftDb.setLabId(jobDraftForm.getLabId());


		// TODO CHECK PERMS IT IS MY JOB
		return doModify(jobDraftDb, result, status, m); 
	}

	public String doModify (
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		// TODO CHECK ACCESS OF LABUSER

		Errors errors = new BindException(result.getTarget(), "jobDraft");
		result.addAllErrors(errors);

		if (result.hasErrors()) {
			waspMessage("hello.error");
			generateCreateForm(m);
			return "jobsubmit/create";
		}

		JobDraft jobDraftDb = jobDraftService.save(jobDraftForm); 

		return nextPage(jobDraftDb);
	}


	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)") 
	public String showModifyMetaForm(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		 
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		MetaHelper metaHelper = getMetaHelper();
		metaHelper.setArea(jobDraft.getWorkflow().getIName());

		jobDraft.setJobDraftMeta(metaHelper.getMasterList(JobDraftMeta.class));
		// jobDraft.setJobDraftMeta(metaHelper.syncWithMaster(jobDraft.getJobDraftMeta()));


		m.put("jobDraftDb", jobDraft);
		m.put("jobDraft", jobDraft);
		m.put("area", metaHelper.getArea());
		m.put("parentarea", metaHelper.getParentArea());

		m.put("workflowiname", jobDraft.getWorkflow().getIName());
		
		m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		return "jobsubmit/metaform";
	}


	
	
	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modifyMeta(
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {

		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		jobDraftForm.setJobDraftId(jobDraftId);
		jobDraftForm.setUserId(jobDraft.getUserId());
		jobDraftForm.setLabId(jobDraft.getLabId());
		jobDraftForm.setWorkflowId(jobDraft.getWorkflowId());

		MetaHelper metaHelper = getMetaHelper();
		
		metaHelper.setArea(jobDraft.getWorkflow().getIName());

		List<JobDraftMeta> jobDraftMetaList = metaHelper.getFromRequest(request, JobDraftMeta.class);

		jobDraftForm.setJobDraftMeta(jobDraftMetaList);

		metaHelper.validate(jobDraftMetaList, result);

		if (result.hasErrors()) {
			waspMessage("hello.error");

			m.put("jobDraftDb", jobDraft);
			m.put("area", metaHelper.getArea());
			m.put("parentarea", metaHelper.getParentArea());

	                m.put("pageFlowMap", getPageFlowMap(jobDraft));
	
			return "jobsubmit/metaform";
		}

		jobDraftMetaService.updateByJobdraftId(metaHelper.getArea(), jobDraftId, jobDraftMetaList);

		return nextPage(jobDraft);
	}

	@RequestMapping(value="/resource/{typeresourceiname}/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showResourceMetaForm(
			@PathVariable("typeresourceiname") String typeresourceiname, 
			@PathVariable("jobDraftId") Integer jobDraftId, 
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		// make list of available resources
		List<Workflowresource> allWorkflowResources = jobDraft.getWorkflow().getWorkflowresource();
		List<Workflowresource> workflowResources = new ArrayList();
		for (Workflowresource w: allWorkflowResources) {
			if (! w.getResource().getTypeResource().getIName().equals(typeresourceiname)) { continue; }
			workflowResources.add(w); 
		}

		// get selected resource
		JobDraftresource jobDraftResource = null; 
		String resourceArea = ""; 
		String resourceName = ""; 
		for (JobDraftresource jdr: jobDraft.getJobDraftresource()) {
			if (! typeresourceiname.equals( jdr.getResource().getTypeResource().getIName())) { continue; }

			jobDraftResource = jdr;
			resourceArea = jdr.getResource().getIName(); 
			resourceName = jdr.getResource().getName(); 
		}

		// Resource Options loading
		Map<String, List<MetaAttribute.Control.Option>> resourceOptions = new HashMap<String, List<MetaAttribute.Control.Option>>();

		Map<String, String> resourceOptionMap= new HashMap(); 
		resourceOptionMap.put("readLength", "r:r;s:s;t:t;");
		resourceOptionMap.put("readType", "a:a;b:b;c:c;");

		for (String key: resourceOptionMap.keySet()) {
			List<MetaAttribute.Control.Option> options=new ArrayList<MetaAttribute.Control.Option>();
			for(String el: org.springframework.util.StringUtils.tokenizeToStringArray(resourceOptionMap.get(key),";")) {
				String [] pair=StringUtils.split(el,":");
				MetaAttribute.Control.Option option = new MetaAttribute.Control.Option();
				option.setValue(pair[0]);
				option.setLabel(pair[1]);
				options.add(option);
			}
			resourceOptions.put(key, options);
		}



		MetaHelper metaHelper = getMetaHelper();
		metaHelper.setArea(resourceArea);

		// jobDraft.setJobDraftMeta(metaHelper.getMasterList(JobDraftMeta.class));
		jobDraft.setJobDraftMeta(metaHelper.syncWithMaster(jobDraft.getJobDraftMeta()));

		// no metadata fields, skip page
/*
		if ( workflowResources.size() <= 1 && jobDraft.getJobDraftMeta().size() == 0 ) {
			return nextPage(jobDraft);
		}
*/

		m.put("workflowResources", workflowResources);
		m.put("jobDraftDb", jobDraft);
		m.put("jobDraft", jobDraft);
		m.put("name",	resourceName);
		m.put("area", metaHelper.getArea());
		m.put("jobDraftResource", jobDraftResource);
		m.put("resourceOptions", resourceOptions);
		m.put("parentarea", metaHelper.getParentArea());

                m.put("pageFlowMap", getPageFlowMap(jobDraft));

		
		return "jobsubmit/resource";
	}

	@RequestMapping(value="/resource/{typeresourceiname}/{jobDraftId}", method=RequestMethod.POST)
	public String modifyResourceMeta (
			@PathVariable String typeresourceiname,
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		m.put("bleh",	"bleh");

		Map params = request.getParameterMap();
		Integer changeResource = null;
		try {
			changeResource = Integer.parseInt(((String[])params.get("changeResource"))[0]);
		} catch (Exception e) {
		}

		// The resource is changing
		// set the resource and reload the page.
		// todo: consider wiping out old meta values?
		if (changeResource != null) {
			List<JobDraftresource> oldJdrs = jobDraft.getJobDraftresource();
			for (JobDraftresource jdr: oldJdrs) {
				if (jdr.getResource().getTypeResource().getIName().equals(typeresourceiname))
				jobDraftresourceService.remove(jdr);
				jobDraftresourceService.flush(jdr);
			}

			JobDraftresource newJdr = new JobDraftresource();
			newJdr.setJobdraftId(jobDraftId);
			newJdr.setResourceId(changeResource);
			jobDraftresourceService.save(newJdr);

			return "redirect:/jobsubmit/resource/" + typeresourceiname + "/" + jobDraftId + ".do";
		}


		String resourceArea = ""; 
		String resourceName = ""; 
		for (JobDraftresource jdr: jobDraft.getJobDraftresource()) {
			if (! typeresourceiname.equals( jdr.getResource().getTypeResource().getIName())) { continue; }

			resourceArea = jdr.getResource().getIName(); 
			resourceName = jdr.getResource().getName(); 
		}

		MetaHelper metaHelper = getMetaHelper();
		metaHelper.setArea(resourceArea);

		List<JobDraftMeta> jobDraftMetaList = metaHelper.getFromRequest(request, JobDraftMeta.class);

		jobDraftForm.setJobDraftMeta(jobDraftMetaList);
		metaHelper.validate(jobDraftMetaList, result);

		if (result.hasErrors()) {
			waspMessage("hello.error");

			return showResourceMetaForm(typeresourceiname, jobDraftId, m);
		}



		jobDraftMetaService.updateByJobdraftId(metaHelper.getArea(), jobDraftId, jobDraftMetaList);

		return nextPage(jobDraft);
	}



	@RequestMapping(value="/additionalMeta/{meta}/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showAdditionalMetaForm(@PathVariable("meta") String additionalMetaArea, @PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		MetaHelper workflowMetaHelper = getMetaHelper();
		workflowMetaHelper.setArea(jobDraft.getWorkflow().getIName());

		List<JobDraftMeta> jobDraftMeta = workflowMetaHelper.syncWithMaster(jobDraft.getJobDraftMeta()); 

		JobDraftMeta alignerJdm = new JobDraftMeta();
		String alignerArea = "";
		for (JobDraftMeta jdm: jobDraftMeta) {
			if (! jdm.getK().equals(workflowMetaHelper.getArea() + "." + additionalMetaArea)) { continue; }
			alignerArea = jdm.getV();
			alignerJdm = jdm;
		}

		MetaHelper metaHelper = getMetaHelper();
		metaHelper.setArea(alignerArea);
		jobDraft.setJobDraftMeta(metaHelper.syncWithMaster(jobDraft.getJobDraftMeta()));


		m.put("jobDraftDb", jobDraft);
		m.put("jobDraft", jobDraft);
		m.put("area", metaHelper.getArea());
		m.put("parentarea", metaHelper.getParentArea());
		
                m.put("pageFlowMap", getPageFlowMap(jobDraft));
		
		return "jobsubmit/aligner";
	}

	@RequestMapping(value="/additionalMeta/{meta}/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modifyAdditionalMeta (
			@PathVariable String additionalMetaArea,
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		MetaHelper workflowMetaHelper = getMetaHelper();
		workflowMetaHelper.setArea(jobDraft.getWorkflow().getIName());

		List<JobDraftMeta> jobDraftMeta = workflowMetaHelper.syncWithMaster(jobDraft.getJobDraftMeta()); 

		JobDraftMeta ametaJdm = new JobDraftMeta();
		String ametaArea = "";
		for (JobDraftMeta jdm: jobDraftMeta) {
			if (! jdm.getK().equals(workflowMetaHelper.getArea() + "." + additionalMetaArea)) { continue; }
			ametaArea = jdm.getV();
			ametaJdm = jdm;
		}

		MetaHelper metaHelper = getMetaHelper();
		metaHelper.setArea(ametaArea);
		List<JobDraftMeta> jobDraftMetaList = metaHelper.getFromRequest(request, JobDraftMeta.class);

		jobDraftForm.setJobDraftMeta(jobDraftMetaList);
		metaHelper.validate(jobDraftMetaList, result);

		if (result.hasErrors()) {
			waspMessage("hello.error");
			m.put("jobDraftDb", jobDraft);
			m.put("area", metaHelper.getArea());
			m.put("parentarea", metaHelper.getParentArea());

			
	                m.put("pageFlowMap", getPageFlowMap(jobDraft));
	
			return "jobsubmit/metaform";
		}


		// removes old aligners
		for (edu.yu.einstein.wasp.model.MetaAttribute.Control.Option opt: ametaJdm.getProperty().getControl().getOptions()) {
			jobDraftMetaService.updateByJobdraftId(opt.getValue(), jobDraftId, new ArrayList());

		}

		jobDraftMetaService.updateByJobdraftId(metaHelper.getArea(), jobDraftId, jobDraftMetaList);

		return nextPage(jobDraft);

	}


	@RequestMapping(value="/aligner/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showAlignerForm(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		return showAdditionalMetaForm("aligner", jobDraftId, m); 
	}


	@RequestMapping(value="/aligner/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String modifyAligner(
			@PathVariable Integer jobDraftId,
			@Valid JobDraft jobDraftForm,
			BindingResult result,
			SessionStatus status,
			ModelMap m) {
		return modifyAdditionalMeta("aligner", jobDraftId, jobDraftForm, result, status, m); 
	}


	/*
	 * Prepares page to manage sample drafts
	 * 
	 * @Author Sasha Levchuk
	 */	
	@RequestMapping(value="/samples/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
	
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		//get list of meta fields that are 'allowed' for the given workflowId 
		int workflowId=jobDraftService.findById(jobDraftId).getWorkflow().getWorkflowId();
		Map<SubtypeSample,List<SampleDraftMeta>>allowedMetaFields=sampleDraftMetaService.getAllowableMetaFields(workflowId);
		
		Set<SampleDraftMeta> allowedMetaFieldsSet = new LinkedHashSet<SampleDraftMeta>(); 
		
		for(List<SampleDraftMeta> metaList:allowedMetaFields.values()) {
			allowedMetaFieldsSet.addAll(metaList);
		}
		
		m.addAttribute("_metaList", allowedMetaFieldsSet);
		m.addAttribute("_metaBySubtypeList", allowedMetaFields);
		
		
		Map<Integer,Map<Integer,String>> jobsBySampleSubtype=new LinkedHashMap<Integer,Map<Integer,String>>();
		for(Map.Entry<Integer, List<Job>> e:jobService.getJobSamplesByWorkflow(workflowId).entrySet()) {
			Map<Integer,String> jm = new LinkedHashMap<Integer,String>();
			jobsBySampleSubtype.put(e.getKey(),jm);
			for(Job j:e.getValue()) {
				jm.put(j.getJobId(), j.getName());
			}		
		}
		m.addAttribute("_jobsBySampleSubtype",jobsBySampleSubtype);
		

		m.addAttribute(JQFieldTag.AREA_ATTR, "sampleDraft");		
		prepareSelectListData(m);
		m.addAttribute("jobdraftId",jobDraftId);
		m.addAttribute("jobDraftDb",jobDraft);
		m.addAttribute("uploadStartedMessage",messageService.getMessage("sampleDraft.fileupload_wait.data"));
		
		
                m.put("pageFlowMap", getPageFlowMap(jobDraft));
	
		return "jobsubmit/sample";
		

	}

	@RequestMapping(value="/samples/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String submitSampleDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);
		return nextPage(jobDraft);
	};

	@RequestMapping(value="/cells/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showSampleCellDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		List<SampleDraft> samples=sampleDraftService.getSampleDraftByJobId(jobDraftId);

		Set<String> selectedSampleCell = new HashSet<String>();
		Map<Integer, Integer> cellMap = new HashMap<Integer, Integer>();
		int cellindexCount = 0;

		for (SampleDraft sd: samples) {
 			for (SampleDraftCell sdc: sd.getSampleDraftCell()) {
/*
				int cellId = sdc.getJobdraftcellId();
				if (! cellMap.containsKey(cellId)) {
					cellindexCount++;
					cellMap.put(cellId, cellindexCount); 
				}
				int cellIndex = cellMap.get(cellId);
*/
				int cellIndex = sdc.getJobDraftCell().getCellindex();

				String key = sd.getSampleDraftId() + "_" + cellIndex;

				selectedSampleCell.add(key);
			}
		}


		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		getMetaHelper().setArea(jobDraft.getWorkflow().getIName());

		jobDraft.setJobDraftMeta(getMetaHelper().getMasterList(JobDraftMeta.class));

		m.put("jobDraftDb", jobDraft);
		m.put("jobDraft", jobDraft);
		m.put("area", getMetaHelper().getArea());
		m.put("parentarea", getMetaHelper().getParentArea());

		m.put("sampleDrafts", samples);
		m.put("selectedSampleCell", selectedSampleCell);

                m.put("pageFlowMap", getPageFlowMap(jobDraft));

		return "jobsubmit/cell";
		
	}

	@RequestMapping(value="/cells/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String updateSampleCellDraft(
			@PathVariable("jobDraftId") Integer jobDraftId, 
			ModelMap m) {

		//	Removes Old Entries, premature?
		List<JobDraftCell> oldJobDraftCells = jobDraftService.getJobDraftByJobDraftId(jobDraftId).getJobDraftCell();

		for (JobDraftCell jdc: oldJobDraftCells) {
			List<SampleDraftCell> oldSampleDraftCells = jdc.getSampleDraftCell();
			for (SampleDraftCell sdc: oldSampleDraftCells) {
				sampleDraftCellService.remove(sdc);
				sampleDraftCellService.flush(sdc);
			}
			jobDraftCellService.remove(jdc);
			jobDraftCellService.flush(jdc);
		}


		List<SampleDraft> samples=sampleDraftService.getSampleDraftByJobId(jobDraftId);

		Map params = request.getParameterMap();
		int maxColumns = 10;
		try {
			maxColumns = Integer.parseInt(((String[])params.get("jobcells"))[0]);
		} catch (Exception e) {
		}

		List<String> checkedList = new ArrayList<String>();

		int cellindex = 0;

		for (int i = 1; i <= maxColumns; i++) {
			int libraryindex = 0;
			boolean cellfound = false;

			JobDraftCell thisJobDraftCell = new JobDraftCell();
			thisJobDraftCell.setJobdraftId(jobDraftId);
			thisJobDraftCell.setCellindex(cellindex + 1);

			for (SampleDraft sd: samples) {
				String checked = "0";
				try {
					checked = ((String[])params.get("sdc_" + sd.getSampleDraftId() + "_" + i ))[0];
				} catch (Exception e) {
				}

				if (checked == null || checked.equals("0")) {
					continue;
				}

				if (! cellfound) {
					cellfound = true;
					cellindex++;

					JobDraftCell jobDraftCellDb = jobDraftCellService.save(thisJobDraftCell);
					thisJobDraftCell = jobDraftCellDb;

					jobDraftCellService.flush(thisJobDraftCell);
				}

				libraryindex++;

				SampleDraftCell sampleDraftCell = new SampleDraftCell();

				sampleDraftCell.setJobdraftcellId(thisJobDraftCell.getJobDraftCellId());
				sampleDraftCell.setSampledraftId(sd.getSampleDraftId());
				sampleDraftCell.setLibraryindex(libraryindex);

				SampleDraftCell sampleDraftCellDb = sampleDraftCellService.save(sampleDraftCell);

				// checkedList.add("sdc_" + sd.getSampleDraftId() + "_" + i + " " + cellindex + " " + libraryindex);

			}
		}

		JobDraft jobDraftDb = jobDraftService.getJobDraftByJobDraftId(jobDraftId);
		return nextPage(jobDraftDb);
	}

	@RequestMapping(value="/verify/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showJobDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		m.put("jobDraft", jobDraft);

		List <SampleDraft> sampleDraftList = jobDraft.getSampleDraft();
		m.put("sampleDraft", sampleDraftList);
                m.put("pageFlowMap", getPageFlowMap(jobDraft));

		return "jobsubmit/verify";
	}

	@RequestMapping(value="/verify/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String verifyJob(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);


		// TODO ClassLoader for validateJobDraft 

		// JobDraftValidator jdv = getDefaultJobDraftValidator();
		try {
			List<WorkflowMeta> wfmList = jobDraft.getWorkflow().getWorkflowMeta(); 
			for (WorkflowMeta wfm: wfmList) {
				if (wfm.getK().equals("workflow.validatorClass")) {
					ClassLoader cl = JobSubmissionController.class.getClassLoader();
					// jdv = (JobDraftValidator) cl.loadClass(wfm.getV()).newInstance();
					Object o = cl.loadClass(wfm.getV()).newInstance();


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
	public String submitJob(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		User me = authenticationService.getAuthenticatedUser();

		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		
		getMetaHelper().setArea(jobDraft.getWorkflow().getIName());

		// no sync w/ master
		// jobDraft.setJobDraftMeta(getMetaHelper().syncWithMaster(jobDraft.getJobDraftMeta()));


		// Copies JobDraft to a new Job
		Job job = new Job();
		job.setUserId(me.getUserId());
		job.setLabId(jobDraft.getLabId());
		job.setName(jobDraft.getName());
		job.setWorkflowId(jobDraft.getWorkflowId());
		job.setIsActive(1);
		job.setCreatets(new Date());

		job.setViewablebylab(0); // Todo: get from lab? // really not being used yet

		Job jobDb = jobService.save(job); 

		// Saves the metadata
		for (JobDraftMeta jdm: jobDraft.getJobDraftMeta()) {
			JobMeta jobMeta = new JobMeta();
			jobMeta.setJobId(jobDb.getJobId());
			jobMeta.setK(jdm.getK());
			jobMeta.setV(jdm.getV());

			jobMetaService.save(jobMeta); 
		}

		for (JobDraftresource jdr: jobDraft.getJobDraftresource()) {
			JobResource jobResource = new JobResource();
			jobResource.setJobId(jobDb.getJobId());
			jobResource.setResourceId(jdr.getResourceId());

			jobResourceService.save(jobResource); 
		}

		// Creates the JobUser Permission
		JobUser jobUser = new JobUser(); 
		jobUser.setUserId(me.getUserId());
		jobUser.setJobId(jobDb.getJobId());
		Role role = roleService.getRoleByRoleName("js");
		jobUser.setRoleId(role.getRoleId());
		jobUserService.save(jobUser);
		
		// added 10-20-11 by rob dubin: with job submission, add lab PI as job viewer ("jv")
		//note: could use similar logic in loop to assign jv to all the lab members
		Lab lab = labService.getLabByLabId(jobDb.getLabId());		
		// if the pi is different from the job user
		if (jobUser.getUserId() != lab.getPrimaryUserId()) {
			JobUser jobUser2 = new JobUser();		
			jobUser2.setUserId(lab.getPrimaryUserId());//the lab PI
			jobUser2.setJobId(jobDb.getJobId());
			Role role2 = roleService.getRoleByRoleName("jv");
			jobUser2.setRoleId(role2.getRoleId());
			jobUserService.save(jobUser2);
		}

		// Job Cells (oldid, newobj)
		Map<Integer,JobCell> jobDraftCellMap = new HashMap<Integer,JobCell>();

		for (JobDraftCell jdc: jobDraft.getJobDraftCell()) {
			JobCell jobCell = new JobCell();
			jobCell.setJobId(jobDb.getJobId());
			jobCell.setCellindex(jdc.getCellindex());

			JobCell jobCellDb =	jobCellService.save(jobCell);	

			jobDraftCellMap.put(jdc.getJobDraftCellId(), jobCellDb);
		}

		// Create Samples
		for (SampleDraft sd: jobDraft.getSampleDraft()) {
			// existing sample...
			Sample sampleDb;

			if (sd.getSourceSampleId() != null) {
				sampleDb = sampleService.getSampleBySampleId(sd.getSourceSampleId());
			} else { 

				Sample sample = new Sample();
				sample.setName(sd.getName()); 
				sample.setTypeSampleId(sd.getTypeSampleId()); 
				sample.setSubtypeSampleId(sd.getSubtypeSampleId()); 
				sample.setSubmitterLabId(jobDb.getLabId()); 
				sample.setSubmitterUserId(me.getUserId()); 
				sample.setSubmitterJobId(jobDb.getJobId()); 
				sample.setIsReceived(0);
				sample.setIsActive(1);
	
				sampleDb = sampleService.save(sample); 
	
				// sample file
				if (sd.getFileId() != null) {
					SampleFile sampleFile = new SampleFile();
					sampleFile.setSampleId(sampleDb.getSampleId());
					sampleFile.setFileId(sd.getFileId());
	
					sampleFile.setIsActive(1);
	
					// TODO ADD NAME AND INAME
	
					sampleFileService.save(sampleFile);
				}
	
				// Sample Draft Meta Data
				for (SampleDraftMeta sdm: sd.getSampleDraftMeta()) {
					SampleMeta sampleMeta = new SampleMeta();
	
					sampleMeta.setSampleId(sampleDb.getSampleId());	
					sampleMeta.setK(sdm.getK());	
					sampleMeta.setV(sdm.getV());	
					sampleMeta.setPosition(sdm.getPosition());	
	
					SampleMeta sampleMetaDb = sampleMetaService.save(sampleMeta); 
				}
			}

			// Job Sample
			JobSample jobSample = new JobSample();
			jobSample.setJobId(jobDb.getJobId());
			jobSample.setSampleId(sampleDb.getSampleId());

			jobSampleService.save(jobSample);

			for (SampleDraftCell sdc: sd.getSampleDraftCell()) {
				SampleCell sampleCell = new SampleCell();
				sampleCell.setSampleId(sampleDb.getSampleId());
				sampleCell.setJobcellId(jobDraftCellMap.get(sdc.getJobdraftcellId()).getJobCellId());
				sampleCell.setLibraryindex(sdc.getLibraryindex());

				SampleCell sampleCellDb = sampleCellService.save(sampleCell);
			}
		}

		// something like this:
		State state = new State(); 

		Task jobCreateTask = taskService.getTaskByIName("Start Job");
		state.setTaskId(jobCreateTask.getTaskId());
		state.setName(jobCreateTask.getName());
		state.setStartts(new Date());
		state.setStatus("CREATED"); 
		stateService.save(state);
		
		Statejob statejob = new Statejob();
		statejob.setStateId(state.getStateId());
		statejob.setJobId(job.getJobId());
		statejobService.save(statejob);

		// update the jobdraft
		jobDraft.setStatus("SUBMITTED");
		jobDraft.setSubmittedjobId(jobDb.getJobId());
		jobDraftService.save(jobDraft); 

		// Adds new Job to Authorized List
		doReauth();

		return nextPage(jobDraft);
	}


	/*
	 * Returns sample drafts by job draft ID 
	 * 
	 * @Author Sasha Levchuk
	 */	
	@RequestMapping(value="/listSampleDraftsJSON", method=RequestMethod.GET)	
	public String getSampleDraftListJSON(@RequestParam("jobdraftId") Integer jobdraftId, HttpServletResponse response) {
	
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<SampleDraft> drafts=sampleDraftService.getSampleDraftByJobId(jobdraftId);
		
		int workflowId=jobDraftService.findById(jobdraftId).getWorkflow().getWorkflowId();
		
		ObjectMapper mapper = new ObjectMapper();

		try {
			//String users = mapper.writeValueAsString(userList);
			jqgrid.put("page","1");
			jqgrid.put("records",drafts.size()+"");
			jqgrid.put("total",drafts.size()+"");
			
			
			List<Map> rows = new ArrayList<Map>();
			
			Map<Integer, String> allSampleSubTypes=new TreeMap<Integer, String>();
			for(SubtypeSample type:(List<SubtypeSample>)subTypeSampleService.findAll()) {
				allSampleSubTypes.put(type.getSubtypeSampleId(),type.getName());
			}
			
			Set<SampleDraftMeta> allowedMetaFields=new LinkedHashSet<SampleDraftMeta>();
			
			for(List<SampleDraftMeta> metaList:sampleDraftMetaService.getAllowableMetaFields(workflowId).values()) {
				allowedMetaFields.addAll(metaList);
			}
			
			for (SampleDraft draft : drafts) {
				Map cell = new HashMap();
				cell.put("id", draft.getSampleDraftId());
			
				MetaHelper sampleMetaHelper = new MetaHelper("sampleDraft", SampleDraftMeta.class, request.getSession());
								
				List<SampleDraftMeta> draftMeta=sampleMetaHelper.syncWithMaster(draft.getSampleDraftMeta(),new ArrayList<SampleDraftMeta>(allowedMetaFields));
				
				String fileCell=getFileCell(draft.getFile());
				
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						draft.getName(),
						allSampleSubTypes.get(draft.getSubtypeSampleId()),							
						draft.getStatus(),						
						fileCell,
						"",
						draft.getSourceSampleId()+"",
						draft.getSourceSampleId()==null?"No":"Yes"
				})); 
			
				for(SampleDraftMeta meta:draftMeta) {
					cellList.add(meta.getV());
				}
			
				cell.put("cell", cellList);
			 
				rows.add(cell);
			}

			jqgrid.put("rows",rows);
		
			return outputJSON(jqgrid, response); 	
			
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+drafts,e);
		}
	}


	/*
	 * Returns samples by Job ID 
	 * 
	 * @Author Sasha Levchuk
	 */	
	@RequestMapping(value="/samplesByJobId", method=RequestMethod.GET)	
	public String samplesByJobId(@RequestParam("jobId") Integer jobId, HttpServletResponse response) {
	
		//result
		Map <Integer, String> samplesMap = new LinkedHashMap<Integer, String>();
		for(Sample sample:sampleService.getSamplesByJobId(jobId)) {
			samplesMap.put(sample.getSampleId(), sample.getName());
		}

		try {
			
			return outputJSON(samplesMap, response); 	
			
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+samplesMap,e);
		}
	}
	

	/*
	 * The call just checks if sampleDraft has non-null sourceSampleId field
	 * I can't get sourceSampleId value while editing form due to JQGrid specifics
	 * 
	 * @Author Sasha Levchuk
	 */	
	@RequestMapping(value = "/getOldSample", method = RequestMethod.GET)
	public String getOldSample(@RequestParam("id") Integer sampleDraftId, HttpServletResponse response) {

		SampleDraft samplDraft = sampleDraftService.findById(sampleDraftId);

		try {
			
			List<Integer> result = new ArrayList<Integer>();

			if (samplDraft.getSourceSampleId() == null) return outputJSON(result, response);

			result.add(samplDraft.getSourceSampleId());
			
			return outputJSON(result, response);
		} catch (Throwable e) {
			
			throw new IllegalStateException("Can't marshall to JSON ", e);

		}

	}

	/*
	 * Renders meta info by smaple ID
	 * @Author Sasha Levchuk
	 */
	@RequestMapping(value="/sampleMetaBySampleId", method=RequestMethod.GET)	
	public String sampleMetaBySampleId(@RequestParam("sampleId") Integer sampleId, HttpServletResponse response) {
	
		//result
		Map <String, String> metaMap = new LinkedHashMap<String, String>();
		for(SampleMeta meta:sampleMetaService.getSamplesMetaBySampleId(sampleId)) {
			metaMap.put(meta.getK(), meta.getV());
		}

		try {
			
			return outputJSON(metaMap, response); 	
			
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+metaMap,e);
		}
	
	}

	/*
	 * Renders URL to download draft file
	 * @Author Sasha Levchuk
	 */
	protected String getFileCell( edu.yu.einstein.wasp.model.File file) {
		if (file==null) return "";
		String fileName=file.getFilelocation();
		if (fileName!=null && ( fileName.indexOf('/')>-1 || fileName.indexOf('\\')>-1)) {
			int idx = fileName.lastIndexOf('/');
			if (idx==-1) idx = fileName.lastIndexOf('\\');
			fileName=fileName.substring(idx+1);						
		}
		
		if (fileName!=null && fileName.indexOf('.')>-1) {
			int idx = fileName.lastIndexOf('.');
			fileName=fileName.substring(0,idx);		
		}
		
		return "<a href='/wasp/jobsubmit/downloadFile.do?id="+file.getFileId()+"'>"+fileName+"</a> "+file.getSizek()+"kB";
	}
	
	
	/*
	 * Uploads sample draft file
	 * @Author Sasha Levchuk
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)		
	public String uploadFile(@RequestParam("id") Integer sampleDraftId,SampleDraft sampleDraftForm, ModelMap m, HttpServletResponse response) {
		 	
		String jsCallback="<html>\n"+
"<head>\n"+
"<script type='text/javascript'>\n"+
"function init() {\n"+
	"if(top.uploadDone) top.uploadDone('{message}'); \n"+
"}\n"+
"window.onload=init;\n"+
"</script>\n"+
"<body>\n"+
"</body>\n";
 
			
		try {
			
			if (sampleDraftForm.getFileData()!=null) {//uploading just file				
					
					CommonsMultipartFile fileData=sampleDraftForm.getFileData();
					
					SampleDraft samplDraft=sampleDraftService.findById(sampleDraftId);
					
					Lab lab=samplDraft.getLab();
					
					User piUser=userService.getById(lab.getPrimaryUserId());
					
					final String login = 
					((org.springframework.security.core.userdetails.User)
							SecurityContextHolder.getContext().getAuthentication().getPrincipal()
					).getUsername();
				 				 
					String[] path=new String[] {
							this.sampleDir.toString(),
							piUser.getLogin(),
							login,
							"jobdraft_"+sampleDraftForm.getJobdraftId(),
							fileData.getOriginalFilename()+"."+System.currentTimeMillis()
							};
					
					String destStr=StringUtils.join(path, System.getProperty("file.separator"));
					
					File dest=new File(destStr);
					
					FileUtils.forceMkdir(dest);
					
					fileData.transferTo(dest);
								 	
					edu.yu.einstein.wasp.model.File file = new edu.yu.einstein.wasp.model.File();
					 
					file.setFilelocation(dest.getAbsolutePath());
					file.setIsActive(1);
					file.setContenttype("?");
					file.setMd5hash("xxx");
					file.setSizek((int)(fileData.getSize()/1024));
										
					fileService.persist(file);
					
					samplDraft.setFileId(file.getFileId());
					
					sampleDraftService.merge(samplDraft);
								
					//submit file for processing by Spring Batch / Spring Intgeration instance 
					RestTemplate template = new RestTemplate();
					try {
						template.postForLocation(jobRunnerHost+"/bee/jobs/launchBeeJob.do?file={file}",String.class, dest.getAbsolutePath());
					} catch (Throwable ee) {
						//ignoring until we have "no downtime" instance to run jobs
					}
					
					jsCallback=jsCallback.replace("{message}", messageService.getMessage("sampleDraft.fileupload_done.data"));
					response.setContentType( "text/html; charset=UTF-8" );
					response.getWriter().print(jsCallback);
				
			} else {
				jsCallback=jsCallback.replace("{message}",messageService.getMessage("sampleDraft.fileupload_nofile.data"));
					response.setContentType( "text/html; charset=UTF-8" );
					response.getWriter().print(jsCallback);				
			}
		} catch(Throwable e) {
			throw new IllegalStateException("cant get file to upload",e);
		}		
		return null;
	}
	
	
	/*
	 * Updates sample draft record
	 * @Author Sasha Levchuk
	 */
	@RequestMapping(value = "/updateSampleDraft", method = RequestMethod.POST)
	public String updateSampleDraft(@RequestParam("id") Integer sampleDraftId,
			SampleDraft sampleDraftForm, ModelMap m,
			HttpServletResponse response) {

		
		boolean adding = sampleDraftId == 0;

		// get from jobdraft table
		JobDraft jd = jobDraftService.findById(sampleDraftForm.getJobdraftId());
		sampleDraftForm.setUserId(jd.getUserId());
		sampleDraftForm.setLabId(jd.getLabId());
		
		SubtypeSample subtype=subTypeSampleService.findById(sampleDraftForm.getSubtypeSampleId());
		
		if (adding) {
						
			int typeSampleId=subtype.getTypeSample().getTypeSampleId();
			sampleDraftForm.setTypeSampleId(typeSampleId);
			
			SampleDraft sampleDraftDb = this.sampleDraftService
					.save(sampleDraftForm);
			
			sampleDraftId = sampleDraftDb.getSampleDraftId();
		} else {

			SampleDraft sampleDraftDb = this.sampleDraftService
					.getById(sampleDraftId);

			sampleDraftDb.setName(sampleDraftForm.getName());
			sampleDraftDb.setStatus(sampleDraftForm.getStatus());
			sampleDraftDb.setSubtypeSampleId(sampleDraftForm.getSubtypeSampleId());
			sampleDraftDb.setSourceSampleId(sampleDraftForm.getSourceSampleId());

			this.sampleDraftService.merge(sampleDraftDb);
		}

		String area=subtype.getIName().substring(0,subtype.getIName().length()-"Sample".length());//chop Sample suffix
		
		MetaHelper sampleMetaHelper = new MetaHelper(area,SampleDraftMeta.class, request.getSession());
		
		List<SampleDraftMeta> sampleDraftMetaList = sampleMetaHelper.getFromJsonForm(request, SampleDraftMeta.class);

		sampleDraftForm.setSampleDraftMeta(sampleDraftMetaList);
		
		for (Iterator<SampleDraftMeta> it=sampleDraftMetaList.iterator();it.hasNext();) {
			SampleDraftMeta meta = it.next();
			if (StringUtils.isEmpty(meta.getV())) it.remove();//remove blank entries
			else meta.setSampledraftId(sampleDraftId);
		}

		sampleDraftMetaService.updateBySampledraftId(sampleDraftId,
				sampleDraftMetaList);

		try {
			response.getWriter()
					.println(
							sampleDraftId
									+ "|"
									+ (adding ? messageService.getMessage("sampleDraft.created.data")
											: messageService.getMessage("sampleDraft.updated.data")));
			return null;

		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ", e);
		}
	}
	
	
	/*
	 * Deletes sample draft record
	 * @Author Sasha Levchuk
	 */
	@RequestMapping(value = "/deleteSampleDraftJSON", method = RequestMethod.DELETE)	
	public String deleteSampleDraftJSON(@RequestParam("id") Integer sampleDraftId,HttpServletResponse response) {

		sampleDraftMetaService.updateBySampledraftId(sampleDraftId, new ArrayList<SampleDraftMeta>());
		this.sampleDraftService.remove(sampleDraftService.findById(sampleDraftId));
		
		try {
			response.getWriter().println(messageService.getMessage("sampleDraft.removed.data"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	}
	
	/*
	 * Downloads sample draft file
	 * 
	 * @Author Sasha Levchuk
	 */
	@RequestMapping(value = "/downloadFile.do", method = RequestMethod.GET)	
	public String downloadSampleDraftFile(@RequestParam("id") Integer fileId,HttpServletResponse response) {
		
		int FILEBUFFERSIZE=1000000;//megabyte
		
		edu.yu.einstein.wasp.model.File file=fileService.findById(fileId);
		
		if (file==null) {
				String html="<html>\n"+
				"<head>\n"+				
				"</script>\n"+
				"<body>\n"+
				"<script>alert('Error: file id "+fileId+" not foud');</script>\n"+
				"</body>\n";
				response.setContentType( "text/html; charset=UTF-8" );
				try {
					response.getWriter().print(html);
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output error message ",e);
				}
				return null;
		}
		try {
			ServletOutputStream out = response.getOutputStream();
			
			File diskFile=new File(file.getFilelocation());
			InputStream in = new FileInputStream(diskFile);
			
			String mimeType = "application/octet-stream";
			byte[] bytes = new byte[FILEBUFFERSIZE];
			int bytesRead;

			response.setContentType(mimeType);
			
			response.setContentLength( (int)diskFile.length() );
			
			String fileName=diskFile.getName();
				
			if (fileName!=null && fileName.indexOf('.')>-1) {
				int idx = fileName.lastIndexOf('.');
				fileName=fileName.substring(0,idx);		
			}
			
			response.setHeader( "Content-Disposition", "attachment; filename=\"" + fileName + "\"" );

			while ((bytesRead = in.read(bytes)) != -1) {
				out.write(bytes, 0, bytesRead);
			}

			// do the following in a finally block:
			in.close();
			out.close();
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant download file id "+fileId,e);
		}
	}
	
	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);
		m.addAttribute("subtypeSamples",subTypeSampleService.findAll());
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

	private Map getPageFlowMap(JobDraft jobDraft) {
		String pageFlow = this.defaultPageFlow;

		try {
			List<WorkflowMeta> wfmList = jobDraft.getWorkflow().getWorkflowMeta();
			for (WorkflowMeta wfm : wfmList) {
				if (wfm.getK().equals("workflow.submitpageflow")) {
					pageFlow = wfm.getV();
					break;
			}
		}
		} catch (Exception e) {
		}

		String context = request.getContextPath();
		String uri = request.getRequestURI();
	
		// strips context, lead slash ("/"), spring mapping
		String currentMapping = uri.replaceFirst(context, "").replaceFirst("\\.do.*$", "");


		String pageFlowArray[] = pageFlow.split(";");
		Map<String, String> rt = new TreeMap<String, String>();

		int found = -1;
		for (int i=0; i < pageFlowArray.length -1; i++) {
			String page = pageFlowArray[i];
			String mapPage = page.replaceAll("^/", "");
			mapPage = mapPage.replaceAll("/\\{n\\}", "");


			String expandPage = page.replaceAll("\\{n\\}", ""+jobDraft.getJobDraftId());
			if (currentMapping.equals(expandPage)) {
				request.setAttribute("forcePageTitle", getPageTitle(mapPage, jobDraft.getWorkflow().getIName()));
				break;

			}

			rt.put(expandPage, getPageTitle(mapPage, jobDraft.getWorkflow().getIName())); 
	
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
	
	private String getPageTitle(String pageDef, String workflowIName) {
		Locale locale=(Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		String code=workflowIName+"."+pageDef+".label";
			
		String pageTitle=DBResourceBundle.MESSAGE_SOURCE.getMessage(code, null, locale);
		
		if (pageTitle!=null) {		
			return pageTitle;
		}
		return pageDef;
	}

/*
	private void setPageTitle(String tilesDef, String workflowIName) {
		
		Locale locale=(Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		
		String code=workflowIName+"."+tilesDef+".label";
			
		String pageTitle=DBResourceBundle.MESSAGE_SOURCE.getMessage(code, null, locale);
		
		if (pageTitle!=null) {		
			request.setAttribute("forcePageTitle", pageTitle);		
		}
	}
*/
}
