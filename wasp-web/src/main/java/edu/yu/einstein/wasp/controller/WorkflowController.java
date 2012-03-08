package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;
import edu.yu.einstein.wasp.model.Workflowtyperesource;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.SoftwareMetaService;
import edu.yu.einstein.wasp.service.SoftwareService;
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
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private SoftwareMetaService softwareMetaService;
	@Autowired
	private WorkflowresourcecategoryService workflowResourceCategoryService;
	@Autowired
	private WorkflowresourcecategoryMetaService workflowResourceCategoryMetaService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private SoftwareService softwareService;
	@Autowired
	private WorkflowSoftwareService workflowSoftwareService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("workflow", WorkflowMeta.class,
				request.getSession());
	}


	@RequestMapping("/list")
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String list(ModelMap m) {
		m.addAttribute("_metaList",
				getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());

		prepareSelectListData(m);

		return "workflow/list";
	}

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('sa') or hasRole('ga') or hasRole('fm')")
	public String getListJSON(HttpServletResponse response) {
		
		String selId = request.getParameter("selId");
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

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
			
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = workflowList.size();									// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");

			Map<String, String> workflowData = new HashMap<String, String>();
			workflowData.put("page", pageIndex + "");
			workflowData.put("selId", StringUtils.isEmpty(selId) ? "" : selId);
			jqgrid.put("workflowdata", workflowData);
			 
			/***** Begin Sort by Workflow Name *****/
			class WorkflowNameComparator implements Comparator<Workflow> {
				@Override
				public int compare(Workflow arg0, Workflow arg1) {
					return arg0.getName().compareToIgnoreCase(arg1.getName());
				}
			}
			if (sidx.equals("name")) {
				Collections.sort(workflowList, new WorkflowNameComparator());
				if (sord.equals("desc"))
					Collections.reverse(workflowList);
			}
			/***** End Sort by Workflow Name *****/
			
			
			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;
			
			List<Workflow> workflowPage = workflowList.subList(frId, toId);

			for (Workflow workflow: workflowPage) {
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
								"configure"}));

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






	/**
	 * Assigns resourcecategory/software to workflow
	 *
	 *
	 */ 

	@RequestMapping(value = "/resource/configure", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('fm')")
	public String showSoftwareForm(
			@RequestParam("selId") Integer workflowId,
			ModelMap m) {

		Workflow workflow = workflowService.getWorkflowByWorkflowId(workflowId); 

		// gets all workflow resources
		List<Workflowtyperesource> workflowTypeResources = workflow.getWorkflowtyperesource();

		List<Workflowresourcecategory> workflowResourceCategories = workflow.getWorkflowresourcecategory();
		Map<String, Workflowresourcecategory> workflowResourceCategoryMap = new HashMap<String, Workflowresourcecategory>();

		// stores the resource options
		// cheating just using workflowIname,uifieldId as key
		Map<String, Set<String>> workflowResourceOptions = new HashMap<String, Set<String>>();
		for (Workflowresourcecategory wrc: workflowResourceCategories) {
			if (wrc.getResourceCategory().getIsActive() == 0){
				continue;
			}
			workflowResourceCategoryMap.put(wrc.getResourceCategory().getIName(), wrc);
			for (WorkflowresourcecategoryMeta wrcm: wrc.getWorkflowresourcecategoryMeta()) {
				if (wrcm.getK().matches(".*\\.allowableUiField\\..*")) {
					String optionName = wrc.getResourceCategory().getIName() + ";" + 
							wrcm.getK().replaceAll(".*\\.allowableUiField\\.", "");
					Set<String> options = new HashSet();
					for (String option : wrcm.getV().split(";")){
						options.add(option);
					}
					workflowResourceOptions.put(optionName, options);
				}
			}
		}
		
		// gets names and versions of all software 
		Map<String, String> workflowSoftwareVersionedNameMap = new HashMap<String, String>();
		for(Workflowtyperesource wtr : workflowTypeResources){
			for (Software s : wtr.getTypeResource().getSoftware()){
				if (s.getIsActive().intValue() == 0){
					continue;
				}
				String area = s.getIName();
				String version = softwareMetaService.getSoftwareMetaByKSoftwareId(area+".currentVersion", s.getSoftwareId()).getV();
				version = (version == null) ? "" : version; 
				workflowSoftwareVersionedNameMap.put(area, s.getName() + " (version: " + version +")");
			}
		}
		
		// loads software mapping
		List<WorkflowSoftware> workflowSoftwares = workflow.getWorkflowSoftware();
		Map<String, WorkflowSoftware> workflowSoftwareMap = new HashMap<String, WorkflowSoftware>();
		

		// stores the resource options
		// cheating just using workflowIname,uifieldId as key
		Map<String, Set<String>> workflowSoftwareOptions = new HashMap<String, Set<String>>();
		for (WorkflowSoftware ws: workflowSoftwares) {
			if (ws.getSoftware().getIsActive().intValue() == 0){
				continue;
			}
			String area = ws.getSoftware().getIName();
			String version = softwareMetaService.getSoftwareMetaByKSoftwareId(area+".currentVersion", ws.getSoftware().getSoftwareId()).getV();
			version = (version == null) ? "" : version; 
			workflowSoftwareMap.put(area, ws);
			
			for (WorkflowsoftwareMeta wsm: ws.getWorkflowsoftwareMeta()) {
				if (wsm.getK().matches(".*\\.allowableUiField\\..*")) {
					String optionName = ws.getSoftware().getIName() + ";" + 
							wsm.getK().replaceAll(".*\\.allowableUiField\\.", "");
					Set<String> options = new HashSet();
					for (String option : wsm.getV().split(";")){
						options.add(option);
					}
					workflowSoftwareOptions.put(optionName, options);
				}
			}
		}

		m.put("workflowId", workflowId);
		m.put("workflow", workflow);
		m.put("workflowTypeResourceMap", workflowTypeResources);
		
		m.put("workflowResourceCategoryMap", workflowResourceCategoryMap);
		m.put("workflowResourceOptions", workflowResourceOptions);

		m.put("workflowSoftwareMap", workflowSoftwareMap);
		m.put("workflowSoftwareOptions", workflowSoftwareOptions);
		m.put("workflowSoftwareVersionedNameMap", workflowSoftwareVersionedNameMap);

		return "workflow/resource/configure";
	}
	
/**
	 * Assigns resourcecatetory/software to workflow
	 *
	 *
	 */ 

	@RequestMapping(value = "/resource/configure", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm')")
	public String updateSoftware(
			@RequestParam("workflowId") Integer workflowId,
			@RequestParam(value="resourceCategory", required=false) String[] resourceCategoryParams,
			@RequestParam(value="resourceCategoryOption", required=false) String[] resourceCategoryOptionParams,
			@RequestParam(value="software", required=false) String[] softwareParams,

			ModelMap m) {
		// return to list if cancel button pressed
		String submitValue = request.getParameter("submit");
		if ( submitValue.equals(messageService.getMessage("workflow.cancel.label")) ){
			return "redirect:/workflow/list.do"; 
		}
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

		return "redirect:/workflow/list.do"; 
	}



}
