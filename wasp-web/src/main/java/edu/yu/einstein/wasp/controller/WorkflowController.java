package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.model.Workflowtyperesource;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.SoftwareService;
import edu.yu.einstein.wasp.service.WorkflowMetaService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.service.WorkflowSoftwareService;
import edu.yu.einstein.wasp.service.WorkflowresourcecategoryMetaService;
import edu.yu.einstein.wasp.service.WorkflowresourcecategoryService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/workflow")
public class WorkflowController extends WaspController {

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private WorkflowMetaService workflowMetaService;

	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private WorkflowresourcecategoryService workflowResourceCategoryService;
	@Autowired
	private WorkflowresourcecategoryMetaService workflowResourceCategoryMetaService;

	@Autowired
	private SoftwareService softwareService;
	@Autowired
	private WorkflowSoftwareService workflowSoftwareService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("workflow", WorkflowMeta.class,
				request.getSession());
	}

//	protected void prepareSelectListData(ModelMap m) {
//		super.prepareSelectListData(m);
//
//	}

	@RequestMapping("/list")
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String list(ModelMap m) {
		m.addAttribute("_metaList",
				getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());

		prepareSelectListData(m);

		// List<Workflow> workflowList = workflowService.findAll();
		//
		// m.addAttribute("workflow", workflowList);

		return "workflow/list";
	}

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String getListJSON(HttpServletResponse response) {
		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Workflow> workflowList;

		if (request.getParameter("_search") == null
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {
			workflowList = workflowService.findAll();
		} else {
			Map<String, String> m = new HashMap<String, String>();

			m.put(request.getParameter("searchField"),
					request.getParameter("searchString"));

			workflowList = workflowService.findByMap(m);

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Workflow> allWorkflows = new ArrayList<Workflow>(
						workflowService.findAll());
				for (Iterator<Workflow> it = workflowList.iterator(); it
						.hasNext();) {
					Workflow excludeworkflow = it.next();
					allWorkflows.remove(excludeworkflow);
				}
				workflowList = allWorkflows;
			}
		}

		ObjectMapper mapper = new ObjectMapper();

		try {
			// String users = mapper.writeValueAsString(userList);
			jqgrid.put("page", "1");
			jqgrid.put("records", workflowList.size() + "");
			jqgrid.put("total", workflowList.size() + "");

			Map<String, String> workflowData = new HashMap<String, String>();
			workflowData.put("page", "1");
			workflowData.put("selId",
					StringUtils.isEmpty(request.getParameter("selId")) ? ""
							: request.getParameter("selId"));
			jqgrid.put("workflowdata", workflowData);

			List<Map> rows = new ArrayList<Map>();

			for (Workflow workflow: workflowList) {
				Map cell = new HashMap();
				cell.put("id", workflow.getWorkflowId());

				List<WorkflowMeta> workflowMeta = getMetaHelperWebapp()
						.syncWithMaster(workflow.getWorkflowMeta());

				List<String> cellList = new ArrayList<String>(
						Arrays.asList(new String[] {
								new Integer(workflow.getWorkflowId())
										.toString(), 
								workflow.getName(),
								workflow.getIsActive().intValue() == 1 ? "yes" : "no",
								"<a href=/wasp/workflow/software/"+workflow.getWorkflowId().toString()+".do >configure</a>"}));

				for (WorkflowMeta meta : workflowMeta) {
					cellList.add(meta.getV());
				}

				cell.put("cell", cellList);

				rows.add(cell);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "
					+ workflowList, e);
		}

	}

	@RequestMapping(value = "/subgridJSON.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String subgridJSON(@RequestParam("id") Integer workflowId,
			ModelMap m, HttpServletResponse response) {

		Map<String, Object> jqgrid = new HashMap<String, Object>();

		Workflow workflowDb = workflowService.getById(workflowId);

		List<Job> jobs = workflowDb.getJob();

		// get max lenth of the previous 4 lists
		int max = jobs.size();

		String[] mtrx = new String[max];

		ObjectMapper mapper = new ObjectMapper();

		try {

			jqgrid.put("page", "1");
			jqgrid.put("records", max + "");
			jqgrid.put("total", max + "");

			String text;

			int i = 0;
			int j = 0;
			for (Job job: jobs) {
				text = job.getJobId() == null ? "No Runs"
						: "<a href=/wasp/job/detail/" + job.getJobId() + ".do>"
								+ job.getName() + "</a>";
				mtrx[j] = text;

				j++;

			}

			List<Map> rows = new ArrayList<Map>();

			for (j = 0; j < max; j++) {
				Map cell = new HashMap();
				rows.add(cell);

				cell.put("id", j + "");
				List<String> cellList = Arrays.asList(mtrx[j]);
				cell.put("cell", cellList);
			}

			jqgrid.put("rows", rows);

			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jobs, e);
		}

	}

	@RequestMapping(value = "/detail_rw/{workflowId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String detailRW(@PathVariable("workflowId") Integer workflowId,
			ModelMap m) {
		return detail(workflowId, m, true);
	}

	@RequestMapping(value = "/detail_ro/{workflowId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String detailRO(@PathVariable("workflowId") Integer workflowId,
			ModelMap m) {
		return detail(workflowId, m, false);
	}

	private String detail(Integer workflowId, ModelMap m, boolean isRW) {
		Workflow workflow = workflowService.getById(workflowId);

		workflow.setWorkflowMeta(getMetaHelperWebapp().syncWithMaster(
				workflow.getWorkflowMeta()));

		List<Job> jobList = workflow.getJob();
		jobList.size();


		// m.addAttribute("now", now);
		m.addAttribute("workflow", workflow);

		// m.addAttribute("workflowmeta", workflowMetaList);
		m.addAttribute("job", jobList);

		prepareSelectListData(m);

		return isRW ? "workflow/detail_rw" : "workflow/detail_ro";
	}

	@RequestMapping(value = "/detail_rw/{workflowId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String updateWorkflow (
			@PathVariable("workflowId") Integer workflowId,
			@Valid Workflow workflowForm, BindingResult result,
			SessionStatus status, ModelMap m) {
		return validateAndUpdateWorkflow(workflowId.intValue(), workflowForm,
				result, status, m);
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String showCreateForm(ModelMap m) {

		Workflow workflow = new Workflow();

		workflow.setWorkflowMeta(getMetaHelperWebapp().getMasterList(
				WorkflowMeta.class));

		m.put("workflow", workflow);

		prepareSelectListData(m);

		return "worfkflow/create";
	}

	@RequestMapping(value = "/create.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String createWorkflow(@Valid Workflow workflowForm,
			BindingResult result, SessionStatus status, ModelMap m) {
		return validateAndUpdateWorkflow(-1, workflowForm, result, status, m);
	}

	public String validateAndUpdateWorkflow(int workflowId,
			Workflow workflowForm, BindingResult result, SessionStatus status,
			ModelMap m) {

		boolean newWorkflow = true;
		if (workflowId > 0) {
			newWorkflow = false;
		}

		List<WorkflowMeta> workflowMetaList = getMetaHelperWebapp().getFromRequest(
				request, WorkflowMeta.class);

		getMetaHelperWebapp().validate(workflowMetaList, result);

		if (result.hasErrors()) {
			workflowForm.setWorkflowMeta(workflowMetaList);
			m.put("workflow", workflowForm);
			prepareSelectListData(m);

			if (newWorkflow) {
				waspMessage("workflow.created.error");
				return "workflow/create";
			} else {
				waspMessage("workflow.updated.error");
				return "workflow/detail_rw";
			}
		}

		for (WorkflowMeta meta : workflowMetaList) {
//			if (meta.getK().contains("assay_platform")) {
//				workflowForm.setPlatform(meta.getV());
//				break;
//			}
		}

		workflowForm.setWorkflowMeta(workflowMetaList);

		Workflow workflowDb;
		if (newWorkflow) {
			workflowDb = workflowService.save(workflowForm);
		} else {
			workflowForm.setWorkflowId(workflowId);
			workflowDb = workflowService.merge(workflowForm);
		}

		workflowId = workflowDb.getWorkflowId();
		workflowMetaService.updateByWorkflowId(workflowId,
				workflowForm.getWorkflowMeta());

		status.setComplete();

		waspMessage(newWorkflow ? "workflow.created_success.label"
				: "workflow.updated_success.label");

		return "redirect:/workflow/detail_ro/" + workflowId + ".do";
	}


	/**
	 * Assigns resourcecatetory/software to workflow
	 *
	 *
	 */ 

	@RequestMapping(value = "/software/{workflowId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm')")
	public String showSoftwareForm(
			@PathVariable("workflowId") Integer workflowId,
			ModelMap m) {

		Workflow workflow = workflowService.getWorkflowByWorkflowId(workflowId); 

		// gets all workflow resources
		List<Workflowtyperesource> workflowTypeResources = workflow.getWorkflowtyperesource();
		List<Workflowtyperesource> workflowTypeResourceMap = new ArrayList<Workflowtyperesource>();

		for (Workflowtyperesource wtr: workflowTypeResources) {
			TypeResource tr = wtr.getTypeResource();  //ED209
			workflowTypeResourceMap.add(wtr);
		}

		List<Workflowresourcecategory> workflowResourceCategorys = workflow.getWorkflowresourcecategory();
		Map<String, Workflowresourcecategory> workflowResourceCategoryMap = new HashMap<String, Workflowresourcecategory>();

		// stores the reource options
		// cheating just using workflowIname,uifieldId as key
		Map<String, Set<String>> workflowResourceOptions = new HashMap<String, Set<String>>();
		for (Workflowresourcecategory wrc: workflowResourceCategorys) {
			workflowResourceCategoryMap.put(wrc.getResourceCategory().getIName(), wrc);
			for (WorkflowresourcecategoryMeta wrcm: wrc.getWorkflowresourcecategoryMeta()) {
				if (wrcm.getK().matches(".*\\.allowableUiField\\..*")) {
					String optionName = wrc.getResourceCategory().getIName() + ";" + 
							wrcm.getK().replaceAll(".*\\.allowableUiField\\.", "");
					String[] os = wrcm.getV().split(";");
					Set<String> options = new HashSet();

					for (int i=0; i < os.length; i++) {
						options.add(os[i]);
					}
					workflowResourceOptions.put(optionName, options);
				}
			}
		}

	// loads software mapping
	List<WorkflowSoftware> workflowSoftwares = workflow.getWorkflowSoftware();
	Map<String, WorkflowSoftware> workflowSoftwareMap = new HashMap<String, WorkflowSoftware>();
	for (WorkflowSoftware wrc: workflowSoftwares) {
		workflowSoftwareMap.put(wrc.getSoftware().getIName(), wrc);
	}

		m.put("workflowId", workflowId);
		m.put("workflow", workflow);

		m.put("workflowTypeResourceMap", workflowTypeResourceMap);
		m.put("workflowResourceCategoryMap", workflowResourceCategoryMap);
		m.put("workflowResourceOptions", workflowResourceOptions);

		m.put("workflowSoftwareMap", workflowSoftwareMap);

		return "workflow/software/form";
	}
	
/**
	 * Assigns resourcecatetory/software to workflow
	 *
	 *
	 */ 

	@RequestMapping(value = "/software/{workflowId}.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm')")
	public String updateSoftware(
			@PathVariable("workflowId") Integer workflowId,

			@RequestParam(value="resourceCategory", required=false) String[] resourceCategoryParams,
			@RequestParam(value="resourceCategoryOption", required=false) String[] resourceCategoryOptionParams,
			@RequestParam(value="software", required=false) String[] softwareParams,

			ModelMap m) {

		Workflow workflow = workflowService.getWorkflowByWorkflowId(workflowId); 
		m.put("workflowId", workflowId);
		m.put("workflow", workflow);

		// removes all the software
		List<WorkflowSoftware> workflowSoftwares = workflow.getWorkflowSoftware();
		for (WorkflowSoftware ws: workflowSoftwares) {
			workflowSoftwareService.remove(ws);
			workflowSoftwareService.flush(ws);
		}
	
		// puts software back in
		if (softwareParams != null) {
		for (int i=0; i< softwareParams.length; i++) {
			WorkflowSoftware workflowSoftware = new WorkflowSoftware();
	
			Software s = softwareService.getSoftwareByIName(softwareParams[i]);
			workflowSoftware.setWorkflowId(workflowId);
			workflowSoftware.setSoftwareId(s.getSoftwareId());
			workflowSoftwareService.save(workflowSoftware);
		}
		}

		// removes all the resource categories and meta
		List<Workflowresourcecategory> workflowresourcecategorys = workflow.getWorkflowresourcecategory();
		for (Workflowresourcecategory wrc: workflowresourcecategorys) {
			for (WorkflowresourcecategoryMeta wrcm : wrc.getWorkflowresourcecategoryMeta()) {
				workflowResourceCategoryMetaService.remove(wrcm);
				workflowResourceCategoryMetaService.flush(wrcm);
			}

			workflowResourceCategoryService.remove(wrc);
			workflowResourceCategoryService.flush(wrc);
		}

		// maps resourceCategory to meta
		Map<String, Set<String>> rcRcmMap = new HashMap<String, Set<String>>();
		// maps meta to options
		Map<String, String> rcmOptionMap = new HashMap<String, String>();
		if (resourceCategoryOptionParams != null) {
		for (int i=0; i < resourceCategoryOptionParams.length; i++) {
			String[] tokenized = resourceCategoryOptionParams[i].split(";");
			String rc = tokenized[0];
			String name = tokenized[1].replaceAll(".*\\.allowableUiField\\.", "");
			String option = tokenized[2];
		
			String key = rc + ".allowableUiField." + name;
			String value = option + ";";
		
			if (rcRcmMap.containsKey(rc)) {
				if (! rcRcmMap.get(rc).contains(key)) {
					rcRcmMap.get(rc).add(key);
				}
			} else {
				Set<String> meta = new HashSet();
				meta.add(key);
				rcRcmMap.put(rc, meta);
			}

			if (rcmOptionMap.containsKey(key)) {
				rcmOptionMap.put(key, rcmOptionMap.get(key) + value);
			} else {
				rcmOptionMap.put(key, value);
			}
		}
		}

		// puts resource categories back in
		if (resourceCategoryParams != null) {
		for (int i=0; i< resourceCategoryParams.length; i++) {
			Workflowresourcecategory workflowResourceCategory = new Workflowresourcecategory();
		
			ResourceCategory rc = resourceCategoryService.getResourceCategoryByIName(resourceCategoryParams[i]);
		
			workflowResourceCategory.setWorkflowId(workflowId);
			workflowResourceCategory.setResourcecategoryId(rc.getResourceCategoryId());
			workflowResourceCategoryService.save(workflowResourceCategory);
	
			int count = 0; 	// counter for position

			if (rcRcmMap.containsKey(rc.getIName())) {	
			for (String metaKey : rcRcmMap.get(rc.getIName())) {
				count++; 
				WorkflowresourcecategoryMeta wrcm = new WorkflowresourcecategoryMeta();
				wrcm.setWorkflowresourcecategoryId(workflowResourceCategory.getWorkflowresourcecategoryId());
				wrcm.setK(metaKey);
				wrcm.setV(rcmOptionMap.get(metaKey));
				wrcm.setPosition(count); 

				workflowResourceCategoryMetaService.save(wrcm);
			}
			}
		}
		}

		return "redirect:/workflow/software/" + workflowId + ".do"; 
	}



}
