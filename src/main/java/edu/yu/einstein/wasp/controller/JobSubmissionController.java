package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaAttribute.Area;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.MetaUtil;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.JobDraftMetaService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobMetaService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.SampleDraftMetaService;
import edu.yu.einstein.wasp.service.SampleDraftService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;


@Controller
@Transactional
@RequestMapping("/jobsubmit")
public class JobSubmissionController extends WaspController {

	@Autowired
	private JobDraftService jobDraftService;

	@Autowired
	private JobDraftMetaService jobDraftMetaService;

	@Autowired
	private SampleDraftService sampleDraftService;

	@Autowired
	private SampleDraftMetaService sampleDraftMetaService;


	@Autowired
	private JobService jobService;

	@Autowired
	private JobUserService jobUserService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private JobMetaService jobMetaService;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private SampleMetaService sampleMetaService;


	@Autowired
	private BeanValidator validator;

	@Autowired
	private TypeSampleService typeSampleService;  
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}


	@Autowired
	private WorkflowService workflowService;

	@RequestMapping(value="/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('lu-*')")
	public String list(ModelMap m) {
		User me = getAuthenticatedUser();

		Map jobDraftQueryMap = new HashMap();
		jobDraftQueryMap.put("UserId", me.getUserId());
		jobDraftQueryMap.put("status", "PENDING");

		List<JobDraft> jobDraftList = jobDraftService.findByMap(jobDraftQueryMap);

		m.put("jobdrafts", jobDraftList); 

		return "jobsubmit/list";
	}

	protected void generateCreateForm(ModelMap m) {
		User me = getAuthenticatedUser();

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

		List <Workflow> workflowList = workflowService.findAll();

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

		User me = getAuthenticatedUser();

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
		User me = getAuthenticatedUser();
		if (me.getUserId() != jobDraft.getUserId()) {
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
		User me = getAuthenticatedUser();
		if (me.getUserId() != jobDraftDb.getUserId()) {
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

		return "redirect:/jobsubmit/modifymeta/" + jobDraftDb.getJobDraftId() + ".do";
	}


	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showModifyMetaForm(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);

		final MetaAttribute.Area AREA = MetaAttribute.Area.valueOf(jobDraft.getWorkflow().getIName());
		final MetaAttribute.Area PARENTAREA = MetaAttribute.Area.jobDraft;

		jobDraft.setJobDraftMeta(MetaUtil.syncWithMaster(jobDraft.getJobDraftMeta(), AREA, JobDraftMeta.class));
		MetaUtil.setAttributesAndSort(jobDraft.getJobDraftMeta(), AREA,getBundle());

		m.put("jobDraftDb", jobDraft);
		m.put("jobDraft", jobDraft);
		m.put("area", AREA.name());
		m.put("parentarea", PARENTAREA.name());

		return "jobsubmit/metaform";
	}
	
	@RequestMapping(value="/modifymeta/{jobDraftId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('lu-*')")
	public String showModifyMetaForm(
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

		final MetaAttribute.Area AREA = MetaAttribute.Area.valueOf(jobDraft.getWorkflow().getIName());
		final MetaAttribute.Area PARENTAREA = MetaAttribute.Area.jobDraft;


		List<JobDraftMeta> jobDraftMetaList = MetaUtil.getMetaFromForm(request,
				AREA, PARENTAREA, JobDraftMeta.class, getBundle());

		jobDraftForm.setJobDraftMeta(jobDraftMetaList);

		Errors errors = new BindException(result.getTarget(), PARENTAREA.name());

		result.addAllErrors(errors);


		List<String> validateList = new ArrayList<String>();
		for (JobDraftMeta meta : jobDraftMetaList) {
			if (meta.getProperty() != null
					&& meta.getProperty().getConstraint() != null) {
				validateList.add(meta.getK());
				validateList.add(meta.getProperty().getConstraint());
			}
		}
		MetaValidator validator = new MetaValidator(
				validateList.toArray(new String[] {}));

		validator.validate(jobDraftMetaList, result, PARENTAREA);

		if (result.hasErrors()) {
			waspMessage("hello.error");

			m.put("jobDraftDb", jobDraft);
			m.put("area", AREA.name());
			m.put("parentarea", PARENTAREA.name());

			return "jobsubmit/metaform";
		}

		for (JobDraftMeta jdm : jobDraftMetaList) {
			jdm.setJobdraftId(jobDraftId);
		}
		jobDraftMetaService.updateByJobdraftId(jobDraftId, jobDraftMetaList);

		m.addAttribute("_metaList", MetaUtil.getMasterList(MetaBase.class, MetaAttribute.Area.sampleDraft, getBundle()));
		m.addAttribute(JQFieldTag.AREA_ATTR, MetaAttribute.Area.sampleDraft.name());		
		
		prepareSelectListData(m);
		m.addAttribute("jobdraftId",jobDraftId);
		return "jobsubmit-sample";
	}
	

	@RequestMapping(value="/verify/{jobDraftId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String showJobDraft(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);
		final MetaAttribute.Area AREA = MetaAttribute.Area.valueOf(jobDraft.getWorkflow().getIName());

		jobDraft.setJobDraftMeta(MetaUtil.syncWithMaster(jobDraft.getJobDraftMeta(), AREA, JobDraftMeta.class));
		MetaUtil.setAttributesAndSort(jobDraft.getJobDraftMeta(), AREA,getBundle());

		m.put("jobDraft", jobDraft);

		return "jobsubmit/verify";
	}

	@RequestMapping(value="/submit/{jobDraftId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
	public String submitJob(@PathVariable("jobDraftId") Integer jobDraftId, ModelMap m) {
		User me = getAuthenticatedUser();

		JobDraft jobDraft = jobDraftService.getJobDraftByJobDraftId(jobDraftId);
		final MetaAttribute.Area AREA = MetaAttribute.Area.valueOf(jobDraft.getWorkflow().getIName());

		jobDraft.setJobDraftMeta(MetaUtil.syncWithMaster(jobDraft.getJobDraftMeta(), AREA, JobDraftMeta.class));
		MetaUtil.setAttributesAndSort(jobDraft.getJobDraftMeta(), AREA,getBundle());

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

		// Creates the JobUser Permission
		JobUser jobUser = new JobUser(); 
		jobUser.setUserId(me.getUserId());
		jobUser.setJobId(jobDb.getJobId());
		Role role = roleService.getRoleByRoleName("js");
		jobUser.setRoleId(role.getRoleId());
		jobUserService.save(jobUser);

		// update the jobdraft
		jobDraft.setStatus("SUBMITTED");
		jobDraft.setSubmittedjobId(jobDb.getJobId());
		jobDraftService.save(jobDraft); 

		// TODO!!! ADD!!! WORKFLOW!!! STEPS!!!

		// Adds new Job to Authorized List
		doReauth();

		return "jobsubmit/ok";
	}


@RequestMapping(value="/listSampleDraftsJSON", method=RequestMethod.GET)	
	public @ResponseBody String getSampleDraftListJSON(@RequestParam("jobdraftId") Integer jobdraftId) {
	
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<SampleDraft> drafts=sampleDraftService.getSampleDraftByJobId(jobdraftId);

		
		//private SampleDraftMetaService sampleDraftMetaService;

		/*
		if (request.getParameter("_search")==null || StringUtils.isEmpty(request.getParameter("searchString"))) {
			userList = this.userService.findAll();
		} else {
			
			  Map<String, String> m = new HashMap<String, String>();
			  
			  m.put(request.getParameter("searchField"), request.getParameter("searchString"));
			  				  
			  userList = this.userService.findByMap(m);
			  
			  if ("ne".equals(request.getParameter("searchOper"))) {
				  List<User> allUsers=new ArrayList<User>(this.userService.findAll());
				  for(Iterator<User> it=userList.iterator();it.hasNext();)  {
					  User excludeUser=it.next();
					  allUsers.remove(excludeUser);
				  }
				  userList=allUsers;
			  }
		}*/

    	ObjectMapper mapper = new ObjectMapper();
    	
		 try {
			 //String users = mapper.writeValueAsString(userList);
			 jqgrid.put("page","1");
			 jqgrid.put("records",drafts.size()+"");
			 jqgrid.put("total",drafts.size()+"");
			 
			 /*
			 Map<String, String> userData=new HashMap<String, String>();
			 userData.put("page","1");
			 userData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			 jqgrid.put("userdata",userData);
			 */
			 List<Map> rows = new ArrayList<Map>();
			 
			 Map<Integer, String> allSampleTypes=new TreeMap<Integer, String>();
		     for(TypeSample type:(List<TypeSample>)typeSampleService.findAll()) {
					allSampleTypes.put(type.getTypeSampleId(),type.getName());
			 }
			 
			 for (SampleDraft draft:drafts) {
				 Map cell = new HashMap();
				 cell.put("id", draft.getSampleDraftId());
				 
				 List<SampleDraftMeta> draftMeta=MetaUtil.syncWithMaster(draft.getSampleDraftMeta(), Area.sampleDraft, SampleDraftMeta.class);
				 					
				 MetaUtil.setAttributesAndSort(draftMeta, Area.sampleDraft, getBundle());
				 
				 List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							draft.getName(),
							allSampleTypes.get(draft.getTypeSampleId()),							
							draft.getStatus()							
				}));
				 
				for(SampleDraftMeta meta:draftMeta) {
					cellList.add(meta.getV());
				}
				
				 
				 cell.put("cell", cellList);
				 
				 rows.add(cell);
			 }

			 
			 jqgrid.put("rows",rows);
			 
			 String json=mapper.writeValueAsString(jqgrid);
			 
			 return json;
		 } catch (Throwable e) {
			 throw new IllegalStateException("Can't marshall to JSON "+drafts,e);
		 }
	
	}

	
	@RequestMapping(value = "/updateSampleDraftsJSON", method = RequestMethod.POST)	
	public String updateDetailJSON(@RequestParam("id") Integer sampleDraftId,SampleDraft sampleDraftForm, ModelMap m, HttpServletResponse response) {

		List<SampleDraftMeta> sampleDraftMetaList = MetaUtil.getMetaFromJSONForm(request,Area.sampleDraft, SampleDraftMeta.class, getBundle());
		
		sampleDraftForm.setSampleDraftMeta(sampleDraftMetaList);

		boolean adding=sampleDraftId==0;
		
		//get from jobdraft table
		JobDraft jd=jobDraftService.findById(sampleDraftForm.getJobdraftId());
		sampleDraftForm.setUserId(jd.getUserId());
		sampleDraftForm.setLabId(jd.getLabId());
		
		
		if (adding) {
					
			SampleDraft sampleDraftDb = this.sampleDraftService.save(sampleDraftForm);
			
			sampleDraftId=sampleDraftDb.getSampleDraftId();
		} else {
			
			SampleDraft sampleDraftDb = this.sampleDraftService.getById(sampleDraftId);
			
			sampleDraftDb.setName(sampleDraftForm.getName());			
			sampleDraftDb.setStatus(sampleDraftForm.getStatus());
			sampleDraftDb.setTypeSampleId(sampleDraftForm.getTypeSampleId());

			this.sampleDraftService.merge(sampleDraftDb);
		}


		for (SampleDraftMeta meta : sampleDraftMetaList) {
			meta.setSampledraftId(sampleDraftId);
		}

		sampleDraftMetaService.updateBySampledraftId(sampleDraftId, sampleDraftMetaList);
		
		try {
			response.getWriter().println(adding?getMessage("sampleDraft.created.success"):getMessage("sampleDraft.updated.success"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ",e);
		}
	
	    
	}
	

protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);
		m.addAttribute("typeSamples",typeSampleService.findAll());
		Map<String, String> statuses=new TreeMap<String, String>();
		for(SampleDraft.Status status:SampleDraft.Status.values()) {
			statuses.put(status.name(), status.name());
		}
		m.addAttribute("statuses",statuses);
		
	}

}
