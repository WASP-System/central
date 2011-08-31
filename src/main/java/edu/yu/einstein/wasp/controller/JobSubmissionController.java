package edu.yu.einstein.wasp.controller;

import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.JobDraftMetaService;
import edu.yu.einstein.wasp.service.SampleDraftService;
import edu.yu.einstein.wasp.service.SampleDraftMetaService;

import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobMetaService;
import edu.yu.einstein.wasp.service.JobUserService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SampleMetaService;

import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.controller.validator.MetaValidator;
import edu.yu.einstein.wasp.model.*;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.transaction.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;


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
	@PreAuthorize("hasRole('jd-' + #jobDraftId)")
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

		// result.addAllErrors(errors);


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

		return "jobsubmit/sample";
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
}
