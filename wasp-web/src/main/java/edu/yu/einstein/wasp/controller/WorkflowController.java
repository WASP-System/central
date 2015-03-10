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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.SoftwareMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowsoftwareMetaDao;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowResourceType;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/workflow")
public class WorkflowController extends WaspController {

	@Autowired
	private WorkflowDao workflowDao;

	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	
	@Autowired
	private SoftwareMetaDao softwareMetaDao;
	
	@Autowired
	private WorkflowresourcecategoryDao workflowResourceCategoryDao;
	
	@Autowired
	private WorkflowresourcecategoryMetaDao workflowResourceCategoryMetaDao;
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private SoftwareDao softwareDao;
	
	@Autowired
	private WorkflowSoftwareDao workflowSoftwareDao;
	
	@Autowired
	private WorkflowsoftwareMetaDao workflowSoftwareMetaDao;
	
	@Autowired
	private StrategyService strategyService;
	
	@Autowired
	private AdaptorService adaptorService;

	@Autowired
	private WorkflowService workflowService;

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
			workflowList = workflowDao.findAll();
		} else {
			Map<String, String> m = new HashMap<String, String>();

			m.put(request.getParameter("searchField"),
					request.getParameter("searchString"));

			workflowList = workflowDao.findByMap(m);

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Workflow> allWorkflows = new ArrayList<Workflow>(
						workflowDao.findAll());
				for (Iterator<Workflow> it = workflowList.iterator(); it
						.hasNext();) {
					Workflow excludeworkflow = it.next();
					allWorkflows.remove(excludeworkflow);
				}
				workflowList = allWorkflows;
			}
		}

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
			
			
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;
			
			List<Workflow> workflowPage = workflowList.subList(frId, toId);

			for (Workflow workflow: workflowPage) {
				if (workflow.getIsActive().intValue() == 0)
					continue;
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", workflow.getId());

				List<WorkflowMeta> workflowMeta = getMetaHelperWebapp()
						.syncWithMaster(workflow.getWorkflowMeta());

				List<String> cellList = new ArrayList<String>(
						Arrays.asList(new String[] {
								new Integer(workflow.getId())
										.toString(), 
								workflow.getName(),
								"yes",
								workflow.getIsActive().intValue() == 1 ? "configure" : ""}));

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

		Workflow workflow = workflowDao.getWorkflowByWorkflowId(workflowId); 

		// gets all workflow resources
		List<WorkflowResourceType> workflowResourceTypes = workflow.getWorkflowResourceType();

		List<Workflowresourcecategory> workflowResourceCategories = workflow.getWorkflowresourcecategory();
		Map<String, Workflowresourcecategory> workflowResourceCategoryMap = new HashMap<String, Workflowresourcecategory>();

		// stores the previously selected workflowResource options
		Map<String, Set<String>> workflowResourceOptions = new HashMap<String, Set<String>>();
		for (Workflowresourcecategory wrc: workflowResourceCategories) {
			if (wrc.getResourceCategory().getIsActive() == 0){
				continue;
			}
			workflowResourceCategoryMap.put(wrc.getResourceCategory().getIName(), wrc);
			// TODO: refactor the following to use WorkflowService.getConfiguredOptions();
			for (WorkflowresourcecategoryMeta wrcm: wrc.getWorkflowresourcecategoryMeta()) {
				if (wrcm.getK().matches(".*\\.allowableUiField\\..*")) {
					String optionName = wrc.getResourceCategory().getIName() + ";" + 
							wrcm.getK().replaceAll(".*\\.allowableUiField\\.", "");
					Set<String> options = new HashSet<String>();
					for (String option : wrcm.getV().split(";")){
						options.add(option);
					}
					workflowResourceOptions.put(optionName, options);
				}
			}
		}
		
		// loads software mapping
		List<WorkflowSoftware> workflowSoftwares = workflow.getWorkflowSoftware();
		Map<String, WorkflowSoftware> workflowSoftwareMap = new HashMap<String, WorkflowSoftware>();
		

		// stores the previously selected workflowSoftware options
		Map<String, Set<String>> workflowSoftwareOptions = new HashMap<String, Set<String>>();
		for (WorkflowSoftware ws: workflowSoftwares) {
			if (ws.getSoftware().getIsActive().intValue() == 0){
				continue;
			}
			String area = ws.getSoftware().getIName();
			workflowSoftwareMap.put(area, ws);
			// TODO: refactor the following to use WorkflowService.getConfiguredOptions();
			for (WorkflowsoftwareMeta wsm: ws.getWorkflowsoftwareMeta()) {
				if (wsm.getK().matches(".*\\.allowableUiField\\..*")) {
					String optionName = ws.getSoftware().getIName() + ";" + 
							wsm.getK().replaceAll(".*\\.allowableUiField\\.", "");
					Set<String> options = new HashSet<String>();
					for (String option : wsm.getV().split(";")){
						options.add(option);
					}
					workflowSoftwareOptions.put(optionName, options);
				}
			}
		}

		// loads all strategies and get this workflow's strategy (if assigned)
		List<Strategy> strategies = new ArrayList<Strategy>();//complete list of strategies of a particular strategytype (such as libraryStrategy)
		List<Strategy> thisWorkflowsStrategies = new ArrayList<Strategy>();//those associated with with this workflow 
		for(WorkflowResourceType wfrt :workflowResourceTypes){
			if(wfrt.getResourceType().getIName().toLowerCase().contains("strategy")){
				String strategyType = wfrt.getResourceType().getIName();
				strategies = strategyService.getStrategiesByStrategyType(strategyType);
				strategyService.orderStrategiesByDisplayStrategy(strategies);
				thisWorkflowsStrategies = strategyService.getThisWorkflowsStrategies(strategyType, workflow);
				break;//only one strategytype permitted per workflow!!!
			}
		}
		
		//3-9-15; configure workflows for adaptorsets
		List<Adaptorset> adaptorsets = adaptorService.getAllAdaptorsets();//active and inactive
		class AdaptorsetNameComparator implements Comparator<Adaptorset> {
		    @Override
		    public int compare(Adaptorset arg0, Adaptorset arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		Collections.sort(adaptorsets, new AdaptorsetNameComparator());//sort by Adaptorset name
		List<Adaptorset> thisWorkflowsAdaptorsets = workflowService.getAdaptorsetsForWorkflow(workflow);		
		
		m.put("workflowId", workflowId);
		m.put("workflow", workflow);
		m.put("workflowResourceTypeMap", workflowResourceTypes);
		
		m.put("workflowResourceCategoryMap", workflowResourceCategoryMap);
		m.put("workflowResourceOptions", workflowResourceOptions);

		m.put("workflowSoftwareMap", workflowSoftwareMap);
		m.put("workflowSoftwareOptions", workflowSoftwareOptions);
		
		m.put("strategies", strategies);
		m.put("thisWorkflowsStrategies", thisWorkflowsStrategies);
		
		m.put("adaptorsets", adaptorsets);
		m.put("thisWorkflowsAdaptorsets", thisWorkflowsAdaptorsets);

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
			@RequestParam("requiredResourceCategoryOptions") String requiredResourceCategoryOptionsString,
			@RequestParam("requiredSoftwareOptions") String requiredSoftwareOptionsString,
			@RequestParam(value="resourceCategory", required=false) String[] resourceCategoryParams,
			@RequestParam(value="resourceCategoryOption", required=false) String[] resourceCategoryOptionParams,
			@RequestParam(value="software", required=false) String[] softwareParams,
			@RequestParam(value="softwareOption", required=false) String[] softwareOptionParams,
			@RequestParam(value="strategyKey", required=false) List<String> strategyKeyList,
			@RequestParam(value="adaptorsetId", required=false) List<Integer> adaptorsetIdList,
			ModelMap m) {
		// return to list if cancel button pressed
		String submitValue = request.getParameter("submit");
		if ( submitValue.equals(messageService.getMessage("workflow.cancel.label")) ){
			return "redirect:/workflow/list.do"; 
		}
		
		Set<String> requiredResourceCategoryOptions = new HashSet<String>();
		
		for (String rrco: requiredResourceCategoryOptionsString.split(";")){
			if (!rrco.isEmpty()){ 
				if (resourceCategoryParams == null) continue;
				for (String rc : resourceCategoryParams){
					if (rrco.startsWith(rc)){
						requiredResourceCategoryOptions.add(rrco);
						break;
					}
				}
			}
		}
		Set<String> requiredSoftwareOptions = new HashSet<String>();
		for (String rso: requiredSoftwareOptionsString.split(";")){
			if (!rso.isEmpty()){ 
				if (softwareParams == null) continue;
				for (String sp : softwareParams){
					if (rso.startsWith(sp)){
						requiredSoftwareOptions.add(rso);
						break;
					}
				}
			}
		}
		Workflow workflow = workflowDao.getWorkflowByWorkflowId(workflowId); 
		m.put("workflowId", workflowId);
		m.put("workflow", workflow);

		Set<Integer> resourceTypeIds = new HashSet<Integer>();
		for(WorkflowResourceType wrt: workflow.getWorkflowResourceType()){
			resourceTypeIds.add(wrt.getResourceTypeId());
		}
		
		// maps software to meta
		Map<String, Set<String>> sSmMap = new HashMap<String, Set<String>>();
		// maps meta to options
		Map<String, String> smOptionMap = new HashMap<String, String>();
		if (softwareOptionParams != null) {
			for (int i=0; i < softwareOptionParams.length; i++) {
				String[] tokenized = softwareOptionParams[i].split(";");
				String s = tokenized[0];
				String name = tokenized[1].replaceAll(".*\\.allowableUiField\\.", "");
				
				if (requiredSoftwareOptions.contains(tokenized[1])){
					requiredSoftwareOptions.remove(tokenized[1]);
				}
				String option = tokenized[2];
			
				String key = s + ".allowableUiField." + name;
				String value = option + ";";
			
				if (sSmMap.containsKey(s)) {
					if (! sSmMap.get(s).contains(key)) {
						sSmMap.get(s).add(key);
					}
				} else {
					Set<String> meta = new HashSet<String>();
					meta.add(key);
					sSmMap.put(s, meta);
				}
	
				if (smOptionMap.containsKey(key)) {
					smOptionMap.put(key, smOptionMap.get(key) + value);
				} else {
					smOptionMap.put(key, value);
				}
			}
		}
			
		// update the database
		
		// removes all the software
		List<WorkflowSoftware> workflowSoftwares = workflow.getWorkflowSoftware();
		for (WorkflowSoftware ws: workflowSoftwares) {
			for (WorkflowsoftwareMeta wsm : ws.getWorkflowsoftwareMeta()) {
				workflowSoftwareMetaDao.remove(wsm);
				workflowSoftwareMetaDao.flush(wsm);
			}

			workflowSoftwareDao.remove(ws);
			workflowSoftwareDao.flush(ws);
		}
		if (softwareParams != null) {
			// puts software back in
			for (int i=0; i< softwareParams.length; i++) {
				WorkflowSoftware workflowSoftware = new WorkflowSoftware();
		
				Software software = softwareDao.getSoftwareByIName(softwareParams[i]);
				workflowSoftware.setWorkflowId(workflowId);
				workflowSoftware.setSoftwareId(software.getId());
				workflowSoftwareDao.save(workflowSoftware);
				if (resourceTypeIds.contains(software.getResourceTypeId())){
					resourceTypeIds.remove(software.getResourceTypeId());
				}
				int count = 0; 	// counter for position
				
				if (sSmMap.containsKey(software.getIName())) {	
					for (String metaKey : sSmMap.get(software.getIName())) {
						count++; 
						WorkflowsoftwareMeta wsm = new WorkflowsoftwareMeta();
						wsm.setWorkflowsoftwareId(workflowSoftware.getId());
						wsm.setK(metaKey);
						wsm.setV(smOptionMap.get(metaKey));
						wsm.setPosition(count); 
						workflowSoftwareMetaDao.save(wsm);
					}
				}
			}
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
				if (requiredResourceCategoryOptions.contains(tokenized[1])){
					requiredResourceCategoryOptions.remove(tokenized[1]);
				}
				String option = tokenized[2];
			
				String key = rc + ".allowableUiField." + name;
				String value = option + ";";
			
				if (rcRcmMap.containsKey(rc)) {
					if (! rcRcmMap.get(rc).contains(key)) {
						rcRcmMap.get(rc).add(key);
					}
				} else {
					Set<String> meta = new HashSet<String>();
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

		// updates database
		
		// removes all the resource categories and meta
		List<Workflowresourcecategory> workflowresourcecategorys = workflow.getWorkflowresourcecategory();
		for (Workflowresourcecategory wrc: workflowresourcecategorys) {
			for (WorkflowresourcecategoryMeta wrcm : wrc.getWorkflowresourcecategoryMeta()) {
				workflowResourceCategoryMetaDao.remove(wrcm);
				workflowResourceCategoryMetaDao.flush(wrcm);
			}

			workflowResourceCategoryDao.remove(wrc);
			workflowResourceCategoryDao.flush(wrc);
		}
		if (resourceCategoryParams != null) {
			// puts resource categories back in
			for (int i=0; i< resourceCategoryParams.length; i++) {
				Workflowresourcecategory workflowResourceCategory = new Workflowresourcecategory();
			
				ResourceCategory rc = resourceCategoryDao.getResourceCategoryByIName(resourceCategoryParams[i]);
			
				workflowResourceCategory.setWorkflowId(workflowId);
				workflowResourceCategory.setResourcecategoryId(rc.getId());
				workflowResourceCategoryDao.save(workflowResourceCategory);
				if (resourceTypeIds.contains(rc.getResourceTypeId())){
					resourceTypeIds.remove(rc.getResourceTypeId());
				}
				int count = 0; 	// counter for position
	
				if (rcRcmMap.containsKey(rc.getIName())) {	
					for (String metaKey : rcRcmMap.get(rc.getIName())) {
						count++; 
						WorkflowresourcecategoryMeta wrcm = new WorkflowresourcecategoryMeta();
						wrcm.setWorkflowresourcecategoryId(workflowResourceCategory.getId());
						wrcm.setK(metaKey);
						wrcm.setV(rcmOptionMap.get(metaKey));
						wrcm.setPosition(count); 
		
						workflowResourceCategoryMetaDao.save(wrcm);
					}
				}
			}
		}

		String strategyType = "";
		Integer strategyResourceTypeId = null;
		for(WorkflowResourceType wrt: workflow.getWorkflowResourceType()){
			if(wrt.getResourceType().getIName().toLowerCase().contains("strategy")){
				strategyType = wrt.getResourceType().getIName();
				strategyResourceTypeId = wrt.getResourceTypeId();
				break;//only one strategytype per workflow
			}
		}
		if(!strategyType.isEmpty()){//there is a strategy for this workflow
			if(strategyKeyList!=null){//there are selections from webpage
				if(!strategyKeyList.isEmpty()){//there are selections from webpage
					try{
						WorkflowMeta workflowMeta  = strategyService.saveStrategiesToWorkflowMeta(workflow, strategyKeyList, strategyType);
						if(workflowMeta.getId()!=null){
							if (resourceTypeIds.contains(strategyResourceTypeId)){
								resourceTypeIds.remove(strategyResourceTypeId);
							}
						}
					}catch(Exception e){}			
				}
			}
		}
		
		//deal with adaptorsets		
		List<Adaptorset> adaptorsetListForThisWorkflow = new ArrayList<Adaptorset>();
		if(adaptorsetIdList!=null){//null if no adaptors selected or if bioanalyzer workflow
			for(Integer adaptorsetId : adaptorsetIdList){
				Adaptorset adaptorset = adaptorService.getAdaptorsetDao().findById(adaptorsetId.intValue());
				if(adaptorset!=null && adaptorset.getId()!=null){
					adaptorsetListForThisWorkflow.add(adaptorset);
				}
			}
		}
		workflowService.setAdaptorsetsForWorkflow(workflow, adaptorsetListForThisWorkflow);			
		
		if (!requiredResourceCategoryOptions.isEmpty() || !requiredSoftwareOptions.isEmpty() || !resourceTypeIds.isEmpty()){
			if (!requiredResourceCategoryOptions.isEmpty() || !requiredSoftwareOptions.isEmpty()){
				// at least one required parameter was not processed from the returned request parameters
				waspErrorMessage("workflow.non-configured_parameter.error");
			}
			if (!resourceTypeIds.isEmpty()){
				// at least one resource category or software of each required resource type must be checked
				waspErrorMessage("workflow.missing_resource_type.error");
			}
			
			return("redirect:/workflow/resource/configure.do?selId="+ workflowId);
		}

		return "redirect:/workflow/list.do"; 
	}



}
