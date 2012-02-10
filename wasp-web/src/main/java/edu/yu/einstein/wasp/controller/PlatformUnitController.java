package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.ResourceBarcode;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.SubtypeSampleMeta;
import edu.yu.einstein.wasp.model.SubtypeSampleResourceCategory;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.AdaptorsetService;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.BarcodeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.ResourceCategoryService;
import edu.yu.einstein.wasp.service.SampleBarcodeService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SampleSourceService;
import edu.yu.einstein.wasp.service.SampleSourceMetaService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.StatesampleService;

import edu.yu.einstein.wasp.service.SubtypeSampleResourceCategoryService;

import edu.yu.einstein.wasp.service.SubtypeSampleService;

import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.TypeResourceService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@Transactional
@RequestMapping("/facility/platformunit")
public class PlatformUnitController extends WaspController {

	@Autowired
	private AdaptorsetService adaptorsetService;

	@Autowired
	private AdaptorService adaptorService;

	@Autowired
	private SampleService sampleService;

	@Autowired
	private JobService jobService;

	@Autowired
	private ResourceCategoryService resourceCategoryService;

	@Autowired
	private StateService stateService;

	@Autowired
	private SampleMetaService sampleMetaService;

	@Autowired
	private SampleSourceService sampleSourceService;

	@Autowired
	private SampleSourceMetaService sampleSourceMetaService;

	@Autowired
	private StatesampleService stateSampleService;
	
	@Autowired
	private SubtypeSampleService subtypeSampleService;

	@Autowired
	private SubtypeSampleResourceCategoryService subtypeSampleResourceCategoryService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private WorkflowService workflowService;

	@Autowired
	private TypeSampleService typeSampleService;

	@Autowired
	private TypeResourceService typeResourceService;

	@Autowired
	private SampleBarcodeService sampleBarcodeService;
	
	@Autowired
	private BarcodeService barcodeService;


	
	@Autowired
	private MessageService messageService;
	  
	@Autowired
	private AuthenticationService authenticationService;
	
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("platformunit",  "sample",SampleMeta.class, request.getSession());
	}

	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String showListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunit");
		
		request.getSession().setAttribute("resourceCategoryId", new Integer (request.getParameter("resourceCategoryId")));

		
		
		return "facility/platformunit/list";
	}
	
	@RequestMapping(value="/instance/list.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String showPlatformunitInstanceListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunitInstance");
		return "facility/platformunit/instance/list";
	}

	
	/* SELECT FOR Subtypes List based on what type of machine was selected by user
	 * select * from subtypesampleresourcecategory stsrc, subtypesample sts 
	 * where stsrc.resourcecategoryid = 4 and stsrc.subtypesampleid = sts.subtypesampleid;
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/listJSON.do", method=RequestMethod.GET)
	public @ResponseBody
	String getListJson() {

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		Map<String, Object> jqgrid = new HashMap<String, Object>();
		
		List<SubtypeSample> subtypeSampleList = new ArrayList<SubtypeSample> ();

		


		// First, search for typesampleid which its iname is "platform unit"
		Map<String, String> typeSampleQueryMap = new HashMap<String, String>();
		typeSampleQueryMap.put("iName", "platformunit");
		List<TypeSample> typeSampleList = typeSampleService.findByMap(typeSampleQueryMap);
		if (typeSampleList.size() == 0)
			return "'Platform Unit' sample type is not defined!";
		
		// Then, use the typesampleid to pull all platformunits from the sample
		// table
		Map<String, Object> subtypeSampleListBaseQueryMap = new HashMap<String, Object>();
		subtypeSampleListBaseQueryMap.put("typeSampleId", typeSampleList.get(0).getTypeSampleId());

		if (request.getParameter("_search") == null 
				|| request.getParameter("_search").equals("false") 
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {
			subtypeSampleList = subtypeSampleService.findByMap(subtypeSampleListBaseQueryMap);

		} else {

			subtypeSampleListBaseQueryMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			subtypeSampleList = this.subtypeSampleService.findByMap(subtypeSampleListBaseQueryMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSubtypeSampleListBaseQueryMap = new HashMap();
				allSubtypeSampleListBaseQueryMap.put("typeSampleId", 5);

				List<SubtypeSample> allSubtypeSampleList = this.subtypeSampleService.findByMap(allSubtypeSampleListBaseQueryMap);
				for (SubtypeSample excludeSubtypeSample : allSubtypeSampleList) {
					allSubtypeSampleList.remove(excludeSubtypeSample);
				}
				subtypeSampleList = allSubtypeSampleList;
			}
		}

	try {
		
		Map<String, Integer> resourceCategoryMap = new HashMap<String, Integer>();
		
		resourceCategoryMap.put("resourcecategoryId", (Integer) request.getSession().getAttribute("resourceCategoryId"));
		
		List<SubtypeSample> subtypeSampleFilteredList = new ArrayList<SubtypeSample> ();

		for (SubtypeSampleResourceCategory subtypeSampleResCat : (List<SubtypeSampleResourceCategory>) this.subtypeSampleResourceCategoryService.findByMap(resourceCategoryMap)) {
			for(SubtypeSample subtypeSample : subtypeSampleList) {
				if (subtypeSample.getSubtypeSampleId().intValue() == subtypeSampleResCat.getSubtypeSampleId().intValue());
				subtypeSampleFilteredList.add(subtypeSample);
			}
		
		}
		subtypeSampleList = subtypeSampleFilteredList;
	
		
		ObjectMapper mapper = new ObjectMapper();

		int pageIndex = Integer.parseInt(request.getParameter("page")); // index of page
		int pageRowNum = Integer.parseInt(request.getParameter("rows")); // number of rows in one page
		int rowNum = subtypeSampleList.size(); // total number of rows
		int pageNum = (rowNum + pageRowNum - 1) / pageRowNum; // total number of pages

		jqgrid.put("records", rowNum + "");
		jqgrid.put("total", pageNum + "");
		jqgrid.put("page", pageIndex + "");

		Map<String, String> sampleData = new HashMap<String, String>();

		sampleData.put("page", pageIndex + "");
		sampleData.put("selId", StringUtils.isEmpty(request.getParameter("selId")) ? "" : request.getParameter("selId"));
		jqgrid.put("sampledata", sampleData);

		List<Map> rows = new ArrayList<Map>();

		int frId = pageRowNum * (pageIndex - 1);
		int toId = pageRowNum * pageIndex;
		toId = toId <= rowNum ? toId : rowNum;

		/*
		 * if the selId is set, change the page index to the one contains
		 * the selId
		 */
		if (!StringUtils.isEmpty(request.getParameter("selId"))) {
			int selId = Integer.parseInt(request.getParameter("selId"));
			int selIndex = subtypeSampleList.indexOf(this.subtypeSampleService.findById(selId));
			frId = selIndex;
			toId = frId + 1;

			jqgrid.put("records", "1");
			jqgrid.put("total", "1");
			jqgrid.put("page", "1");
		}

		List<SubtypeSample> subtypeSamplePage = subtypeSampleList.subList(frId, toId);
		for (SubtypeSample subtypeSample : subtypeSamplePage) {
			Map cell = new HashMap();
			cell.put("id", subtypeSample.getSubtypeSampleId());

			List<SubtypeSampleMeta> subtypeSampleMetaList = getMetaHelperWebapp().syncWithMaster(subtypeSample.getSubtypeSampleMeta());
			
			List<String> cellList = new ArrayList<String>(
					Arrays.asList(
							new String[] { 
									"<a href=/wasp/facility/platformunit/instance/list.do?selId="+subtypeSample.getSubtypeSampleId()+">" + 
											subtypeSample.getName() + "</a>" }));

			for (SubtypeSampleMeta meta : subtypeSampleMetaList) {
				cellList.add(meta.getV());
			}

			cell.put("cell", cellList);

			rows.add(cell);
		}

		jqgrid.put("rows", rows);

		String json = mapper.writeValueAsString(jqgrid);
		return json;

	} catch (Exception e) {
			throw new IllegalStateException("Can't marshall to JSON " + subtypeSampleList, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/instance/listJSON.do", method=RequestMethod.GET)
	public @ResponseBody
	String getPlatformInstanceListJson() {

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Sample> sampleList;

		// First, search for typesampleid which its iname is "platform unit"
		Map<String, String> typeSampleQueryMap = new HashMap<String, String>();
		typeSampleQueryMap.put("iName", "platformunit");
		List<TypeSample> typeSampleList = typeSampleService.findByMap(typeSampleQueryMap);
		if (typeSampleList.size() == 0)
			return "'Platform Unit' sample type is not defined!";
		// Then, use the typesampleid to pull all platformunits from the sample
		// table
		Map<String, Object> sampleListBaseQueryMap = new HashMap<String, Object>();
		sampleListBaseQueryMap.put("typeSampleId", typeSampleList.get(0).getTypeSampleId());

		if (request.getParameter("_search") == null 
				|| request.getParameter("_search").equals("false") 
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {
			sampleList = sampleService.findByMap(sampleListBaseQueryMap);

		} else {

			sampleListBaseQueryMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleList = this.sampleService.findByMap(sampleListBaseQueryMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSampleListBaseQueryMap = new HashMap();
				allSampleListBaseQueryMap.put("typeSampleId", 5);

				List<Sample> allSampleList = sampleService.findByMap(allSampleListBaseQueryMap);
				for (Sample excludeSample : allSampleList) {
					allSampleList.remove(excludeSample);
				}
				sampleList = allSampleList;
			}
		}

	try {
		ObjectMapper mapper = new ObjectMapper();
		
		Map<Integer, Integer> allSampleBarcode = new TreeMap<Integer, Integer>();
		for (SampleBarcode sampleBarcode : (List<SampleBarcode>) this.sampleBarcodeService.findAll()) {
			if (sampleBarcode != null) {
				
				allSampleBarcode.put(sampleBarcode.getSampleId(), sampleBarcode.getBarcodeId());
			}
		}
		
		Map<Integer, String> allBarcode = new TreeMap<Integer, String>();
		for (Barcode barcode : (List<Barcode>) this.barcodeService.findAll()) {
			if (barcode != null) {
				
				allBarcode.put(barcode.getBarcodeId(), barcode.getBarcode());
			}
		}

		int pageIndex = Integer.parseInt(request.getParameter("page")); // index of page
		int pageRowNum = Integer.parseInt(request.getParameter("rows")); // number of rows in one page
		int rowNum = sampleList.size(); // total number of rows
		int pageNum = (rowNum + pageRowNum - 1) / pageRowNum; // total number of pages

		jqgrid.put("records", rowNum + "");
		jqgrid.put("total", pageNum + "");
		jqgrid.put("page", pageIndex + "");

		Map<String, String> sampleData = new HashMap<String, String>();

		sampleData.put("page", pageIndex + "");
		sampleData.put("selId", StringUtils.isEmpty(request.getParameter("selId")) ? "" : request.getParameter("selId"));
		jqgrid.put("sampledata", sampleData);

		List<Map> rows = new ArrayList<Map>();

		int frId = pageRowNum * (pageIndex - 1);
		int toId = pageRowNum * pageIndex;
		toId = toId <= rowNum ? toId : rowNum;

		/*
		 * if the selId is set, change the page index to the one contains
		 * the selId
		 */
		if (!StringUtils.isEmpty(request.getParameter("selId"))) {
			int selId = Integer.parseInt(request.getParameter("selId"));
			int selIndex = sampleList.indexOf(sampleService.findById(selId));
			frId = selIndex;
			toId = frId + 1;

			jqgrid.put("records", "1");
			jqgrid.put("total", "1");
			jqgrid.put("page", "1");
		}

		List<Sample> samplePage = sampleList.subList(frId, toId);
		for (Sample sample : samplePage) {
			Map cell = new HashMap();
			cell.put("id", sample.getSampleId());

			List<SampleMeta> sampleMetaList = getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta());
			
			List<String> cellList = new ArrayList<String>(
					Arrays.asList(
							new String[] { 
										sample.getName(),
										sample.getSubtypeSample()==null?"": sample.getSubtypeSample().getName(), 
										sample.getUser().getFirstName()+" "+sample.getUser().getLastName(),
										allSampleBarcode.get(sample.getSampleId())==null? "" : allBarcode.get(allSampleBarcode.get(sample.getSampleId()))}));

			for (SampleMeta meta : sampleMetaList) {
				cellList.add(meta.getV());
			}

			cell.put("cell", cellList);

			rows.add(cell);
		}

		jqgrid.put("rows", rows);

		String json = mapper.writeValueAsString(jqgrid);
		return json;

	} catch (Exception e) {
			throw new IllegalStateException("Can't marshall to JSON " + sampleList, e);
		}
	}

	

	@RequestMapping(value="/instance/updateJSON.do", method=RequestMethod.POST)
	public String updateJson(
			@RequestParam("id") Integer sampleId,
			@Valid Sample sampleForm, 
			ModelMap m, 
			HttpServletResponse response) {

		List<SampleMeta> sampleMetaList = getMetaHelperWebapp().getFromJsonForm(request, SampleMeta.class);
		sampleForm.setSampleMeta(sampleMetaList);
		sampleForm.setSampleId(sampleId);

		//preparePlatformUnit(sampleForm);
		//updatePlatformUnit(sampleForm);

		try {
			response.getWriter().println(messageService.getMessage("platformunitInstance.updated_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ", e);
		}

	}


	@RequestMapping(value="/view/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String viewPlatformUnit(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleService.getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_ro";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String updatePlatformUnitForm(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleService.getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_rw";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ft')")
	public String modifyPlatformUnit(
		@PathVariable("sampleId") Integer sampleId,
		@Valid Sample sampleForm,
		BindingResult result,
		SessionStatus status,
		ModelMap m) {

		preparePlatformUnit(sampleForm);

		return validateAndUpdatePlatformUnit(sampleForm, result, status, m);
	}

	/* ****************************** */

	public Sample preparePlatformUnit(Sample sampleForm) {
		if (sampleForm.getSampleId() == null) {
			User me = authenticationService.getAuthenticatedUser();
			sampleForm.setSubmitterUserId(me.getUserId());

			TypeSample typeSample = typeSampleService.getTypeSampleByIName("platformunit");
			sampleForm.setTypeSampleId(typeSample.getTypeSampleId());

			sampleForm.setSubmitterLabId(1);
	
			sampleForm.setReceiverUserId(sampleForm.getSubmitterUserId());
			sampleForm.setReceiveDts(new Date());
			sampleForm.setIsReceived(1);
			sampleForm.setIsActive(1);
		} else {
			Sample sampleDb =	sampleService.getSampleBySampleId(sampleForm.getSampleId());

			// TODO do compares that i am the same sample as sampleform, and not new
	
			// fetches the updates
			// sampleDb.setName(sampleForm.getName());
	
	
			sampleForm.setSubmitterUserId(sampleDb.getSubmitterUserId());
			sampleForm.setSubmitterLabId(sampleDb.getSubmitterLabId());
			sampleForm.setTypeSampleId(sampleDb.getTypeSampleId());
	
			sampleForm.setReceiverUserId(sampleDb.getReceiverUserId());
			sampleForm.setReceiveDts(sampleDb.getReceiveDts());
			sampleForm.setIsReceived(sampleDb.getIsReceived());
			sampleForm.setIsActive(sampleDb.getIsActive());
		}
		return sampleForm;
	}

	public String validateAndUpdatePlatformUnit(
		Sample sampleForm,
		BindingResult result,
		SessionStatus status,
		ModelMap m) {

		List<SampleMeta> sampleMetaList = getMetaHelperWebapp().getFromRequest(request, SampleMeta.class);

		getMetaHelperWebapp().validate(sampleMetaList, result);

		if (result.hasErrors()) {
			// TODO REAL ERROR
			waspMessage("hello.error");

			sampleForm.setSampleMeta(sampleMetaList);
			m.put("sample", sampleForm);

			return "facility/platformunit/detail_rw";
		}

		sampleForm.setSampleMeta(sampleMetaList);

		String returnString = updatePlatformUnit(sampleForm);


		return returnString;
	}


	// TODO move to service?
	public String updatePlatformUnit( Sample sampleForm ) {
	
		Sample sampleDb;
		if (sampleForm.getSampleId() == null) {
			sampleDb = sampleService.save(sampleForm);
		} else {
			sampleDb = sampleService.merge(sampleForm);
		}

		sampleMetaService.updateBySampleId(sampleDb.getSampleId(), sampleForm.getSampleMeta());

		/// TODO depending on how many *.resourceId is set, create sample lanes
		/// query resourceServices.getLaneCount(resourceId)

		/// TODO depending on how many *.lanecount is set, create sample lanes
		// int xxx = somefunction(getLaneCount());
		// for (int i = 0; i < xxxx; i++) {
		//	 Sample lane = new Sample()
		// }


		//	return "facility/platformunit/ok";
		return "redirect:/facility/platformunit/ok";
	}

	/**
	 * limitPriorToAssignment
	 */
	@RequestMapping(value="/limitPriorToAssign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String limitPriorToAssignmentForm(ModelMap m) {
		
		TypeResource typeResource = typeResourceService.getTypeResourceByIName("mps");
		//if(typeResource.getTypeResourceId()==0){
			//waspMessage("platformunit.resourceCategoryNotFound.error");
			//return "redirect:/dashboard.do";
		//}
		Map filterForResourceCategory = new HashMap();
		filterForResourceCategory.put("typeResourceId", typeResource.getTypeResourceId());
		List<ResourceCategory> resourceCategories = resourceCategoryService.findByMap(filterForResourceCategory);
		
		m.put("resourceCategories", resourceCategories);
		return "facility/platformunit/limitPriorToAssign"; 
	}	
	
	
	
  /**
   * assignmentForm
   *
   */
	@RequestMapping(value="/assign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String assignmentForm(@RequestParam("resourceCategoryId") Integer resourceCategoryId, 
			ModelMap m) {

		if(resourceCategoryId.intValue()==0){//no machine type selected by user through the drop-down box
			waspMessage("platformunit.resourceCategoryNotSelected.error");
			return "redirect:/facility/platformunit/limitPriorToAssign.do";
		}
		ResourceCategory resourceCategory = resourceCategoryService.getResourceCategoryByResourceCategoryId(resourceCategoryId);
		if(resourceCategory.getResourceCategoryId() == 0){//machine type not found in database
			waspMessage("platformunit.resourceCategoryInvalidValue.error");
			return "redirect:/facility/platformunit/limitPriorToAssign.do";
		}
		
		
		// pickups FlowCells limited by states
		//TODO We really need to filter to display only those flow cells that are compatible with the selected machine resourceCategoryId
		Map stateMap = new HashMap(); 
		Task task = taskService.getTaskByIName("Flowcell/Add Library To Lane");
		if(task.getTaskId() == 0){
			//TODO error message and return to dashboard 
			return "redirect:/dashboard.do";
		}
		stateMap.put("taskId", task.getTaskId()); 	
		stateMap.put("status", "CREATED"); 
		List<State> temp_platformUnitStates = stateService.findByMap(stateMap);
		List<State> platformUnitStates = new ArrayList<State>();//for the filtered states (but would really just need a filtered list of flow cells)
		
		List<Sample> flowCells = new ArrayList<Sample>();
		
		Map stsrcMap = new HashMap();//get the ids for the types of flow cells that go on the selected machine
		stsrcMap.put("resourcecategoryId", resourceCategory.getResourceCategoryId()); 
		List<SubtypeSampleResourceCategory> stsrcList = subtypeSampleResourceCategoryService.findByMap(stsrcMap);
		//subtypeSampleResourceCategoryService.g
		for(State s : temp_platformUnitStates){
			List<Statesample> ssList = s.getStatesample();
			for(Statesample ss : ssList){
				for(SubtypeSampleResourceCategory stsrc : stsrcList){
					if(stsrc.getSubtypeSampleId().intValue() == ss.getSample().getSubtypeSampleId().intValue()){
						platformUnitStates.add(s);//consider really just taking the flowcell sample, which is done next line
						flowCells.add(ss.getSample());
					}
				}
			}
		}

		// picks up jobs
		// FAKING IT HERE TOO
		//What is really needed is to get all jobs which have a task of libraryWaitingForPlatformUnit
		Workflow workflow = workflowService.getWorkflowByWorkflowId(1);
		List<Job> tempJobs = workflow.getJob(); 
		
		Job aJob = jobService.getJobByJobId(10215);//add this
		tempJobs.add(aJob);
		
		List<Job> jobs = new ArrayList<Job>();
		for(Job job : tempJobs){
			List<JobResourcecategory> jrcList = job.getJobResourcecategory();
			for(JobResourcecategory jrc : jrcList){
				if(jrc.getResourcecategoryId().intValue() == resourceCategoryId.intValue()){
					jobs.add(job);
					break;
				}
			}
		}

		//fake even more for the moment, but basically, when a job is completed, it shouldn't appear on this list
/*		List<Job> tempJobs = new ArrayList<Job>();
		List<Job> jobs = new ArrayList<Job>();
		Job aJob = jobService.getJobByJobId(10091);
		tempJobs.add(aJob);
		aJob = jobService.getJobByJobId(10001);
		tempJobs.add(aJob);
		aJob = jobService.getJobByJobId(10215);
		tempJobs.add(aJob);
		
		for(Job job : tempJobs){
			List<JobResourcecategory> jrcList = job.getJobResourcecategory();
			for(JobResourcecategory jrc : jrcList){
				if(jrc.getResourcecategoryId().intValue() == resourceCategoryId.intValue()){
					jobs.add(job);
					break;
				}
			}
		}
*/		
		//map of adaptors for display
		Map adaptors = new HashMap();
		List<Adaptorset> adaptorsetList = adaptorsetService.findAll();
		//logger.debug("ROB: adaptorsetList.size = " + adaptorsetList.size());
		for(Adaptorset as : adaptorsetList){
			String adaptorsetname = new String(as.getName());
			//logger.debug("ROB: adaptorsetname = " + adaptorsetname);
			List<Adaptor> adaptorList = as.getAdaptor();
			//logger.debug("ROB: adaptorList.size = " + adaptorList.size());
			for(Adaptor adaptor : adaptorList){
				if( "".equals(adaptor.getBarcodesequence()) ){
					adaptors.put(adaptor.getAdaptorId().toString(), adaptorsetname);
				}
				else{
					//logger.debug("ROB: barcodesequence = " + adaptor.getBarcodesequence());
					adaptors.put(adaptor.getAdaptorId().toString(), adaptorsetname + " (" + adaptor.getBarcodesequence() + ")");
				}
			}
		}
		//logger.debug("ROB: adaptors.size = " + adaptors.size());
		//logger.debug("ROB: Begin");
		for (Object key: adaptors.keySet()){
			//logger.debug("ROB: key="+key.toString() + ", value="+ adaptors.get(key));
		 }
		//logger.debug("ROB: End");
		
		
		
		m.put("machineName", resourceCategory.getName());
		m.put("resourceCategoryId", resourceCategoryId);
		m.put("jobs", jobs); 
		m.put("platformUnitStates", platformUnitStates); 
		m.put("hello", "hello world"); 
		m.put("adaptors", adaptors);
		m.put("flowCells", flowCells);
		
		return "facility/platformunit/assign"; 
	}

  /**
   * assignmentAdd
	 * 
	 * @param librarySampleId
	 * @param laneSampleId
   *
   */
	@RequestMapping(value="/assignAdd.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ft')")
	public String assignmentAdd(
			@RequestParam("librarysampleid") int librarySampleId,
			@RequestParam("lanesampleid") int laneSampleId,
			@RequestParam("jobid") Integer jobId,
			@RequestParam(value="pmolAdded", required=false) String pmolAdded,
			@RequestParam("resourceCategoryId") Integer resourceCategoryId,
    ModelMap m) {

		String error = null;

		Job job = jobService.getJobByJobId(jobId);
		Sample laneSample = sampleService.getSampleBySampleId(laneSampleId); 
		Sample librarySample = sampleService.getSampleBySampleId(librarySampleId); 

		if (job.getJobId().intValue() == 0) {
			waspMessage("platformunit.jobIdInvalidValue.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		
		if (laneSampleId == 0) {
			waspMessage("platformunit.cellNotSelectedInvalidValue.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}

		if (laneSample == null || librarySample == null || laneSample.getSampleId() == null || librarySample.getSampleId() == null){
			waspMessage("platformunit.laneOrLibraryInvalidValue.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}

		if ( ! laneSample.getTypeSample().getIName().equals("cell")) { 
			waspMessage("platformunit.laneIsNotLaneInvalidValue.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		
		if ( ! librarySample.getTypeSample().getIName().equals("library")) {
			waspMessage("platformunit.libraryIsNotLibraryInvalidValue.error");	
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		
		if ("".equals(pmolAdded)) {
			waspMessage("platformunit.pmoleAddedInvalidValue.error");	
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		
		Integer pmolAddedInteger;
		try{
			pmolAddedInteger = new Integer(Integer.parseInt(pmolAdded));
			if(pmolAddedInteger.intValue() <= 0){
				waspMessage("platformunit.pmoleAddedInvalidValue.error");
				return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
			}
		}
		catch(Exception e){
			waspMessage("platformunit.pmoleAddedInvalidValue.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
				
/*		if (error != null) {
			//waspMessage("hello"); // use error
			m.put("librarySelected", librarySampleId); 
			m.put("error", error);
			return assignmentForm(resourceCategoryId.intValue(), m);
		}
*/				
		// ensure lane/flowcell is not locked
		//a blocked flowcell: one where it's task (102) of Flowcell/Add Library To Lane is FINAL
		//a blocked lane: something w/ a task of "Flowcell/Add Library" not at the "CREATED" state 
		//(such as the minor case in case that while the page was worked on someone else put the flow cell into a machine or canceled the flowcell)
		//At the end of the day, simply ensure that the flowcell exists and has a task of Flowcell/Add Library To Lane and a status of CREATED
		boolean flowCellIsAvailable = false;
		List<SampleSource> parentSampleSources = laneSample.getSampleSourceViaSourceSampleId();//should be one
		//error = "num flow cells is " + parentSampleSources.size();
		if(parentSampleSources.size()==0){
			waspMessage("platformunit.flowcellRecordNotFound.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		Sample flowCell = parentSampleSources.get(0).getSample();
		if( ! "platformunit".equals(flowCell.getTypeSample().getIName()) ){
			waspMessage("platformunit.flowcellRecordNotFound.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		Map stateMap = new HashMap(); 
		Task task = taskService.getTaskByIName("Flowcell/Add Library To Lane");
		if(task.getTaskId() == 0){
			waspMessage("platformunit.taskFlowcellAddLibraryNotFound.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		stateMap.put("taskId", task.getTaskId()); 	
		stateMap.put("status", "CREATED"); 
		List<State> platformUnitStates = stateService.findByMap(stateMap);
		for(State s : platformUnitStates){
			if(flowCellIsAvailable){break;}
			List<Statesample> stateSampleFlowCells = s.getStatesample();
			for(Statesample stateSampleFlowCell : stateSampleFlowCells){
				if(stateSampleFlowCell.getSampleId().intValue() == flowCell.getSampleId().intValue()){
					flowCellIsAvailable = true;
					break;
				}
			}
		}
		if(!flowCellIsAvailable){
			waspMessage("platformunit.flowcellStateError.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		
		// TODO ensure lanesample.meta.jobid is the either null or the samejob as librarysampleid
		//Mt Sinai does not want this restriction implemented(but Einstein does, so must write it later
		
		//(1) identify the barcode sequence on the library being added. If problem then terminate. Also identify the barcode of the libraries already on the flowcell; if problem terminate.
		//(2) if the library being added has a barcode that is NONE, ensure that the lane does not contain other libraries before adding the library to the lane. (if the lane does contain other libraries, then terminate)
		//(3) if the library being added has a bardcode that is something other than NONE (meaning a real barcode sequence): ensure that the lane does NOT contain any other library with that identical barcode. (if the lane does contain a library with the sampe barcode, then terminate)
		//(4) if the flowcell already has a library with a barcode of NONE, then the selected library may NOT be added
	
		//case 1: identify the barcode for the library being added
		List<SampleMeta> sampleMetaList = librarySample.getSampleMeta();//should be only one flow cell 
		Adaptor adaptorOnLibraryBeingAdded = null;
		//int counter = 1;
		//logger.debug("ROB: 0");
		for(SampleMeta sm : sampleMetaList){//sift through the metadata for this sample, looking for sample.library.adaptorid
			//logger.debug("ROB: " + counter++);
			//logger.debug("ROB: " + counter++ + "  sm.getK() =  " + sm.getK());
			//int index = sm.getK().lastIndexOf(".");
			//logger.debug("ROB: " + counter++ + "  sm.getK().lastIndexOf(.) = " + index);
			if("adaptorid".equals(sm.getK().substring(sm.getK().lastIndexOf(".") + 1))){
				//logger.debug("ROB: " + counter++);
				try{//creating new Integer()
					adaptorOnLibraryBeingAdded = adaptorService.getAdaptorByAdaptorId(new Integer(sm.getV()));//should really make sure this doesn't throw exception
					break;//better only be one entry
				}
				catch(Exception e){
					waspMessage("platformunit.adaptorIdError.error");
					return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
				}
			}
		}
		
		if(adaptorOnLibraryBeingAdded == null){
			waspMessage("platformunit.adaptorNotFoundError.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		if(adaptorOnLibraryBeingAdded.getBarcodesequence()==null || "".equals(adaptorOnLibraryBeingAdded.getBarcodesequence())){
			waspMessage("platformunit.barcodeNotFoundError.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		String barcodeOnLibBeingAdded = new String(adaptorOnLibraryBeingAdded.getBarcodesequence());
		
		//Set seenAdaptors = new HashSet(); //doesn't seem to be used
		//Integer jobId = null; //doesn't seem to be used

		int counter = 1;
		logger.debug("0 ROB : 0");
		int maxIndex = 0; 
		List<SampleSource> siblingSampleSource = laneSample.getSampleSource(); //these are the libraries already on the flowcell
		List<Adaptor> adaptorsAlreadyOnFlowCell  = new ArrayList<Adaptor>();
		
		if(siblingSampleSource != null && siblingSampleSource.size() > 0 && "NONE".equals(barcodeOnLibBeingAdded) ){//case 2: the library being added has barcode NONE AND the lane to which user wants to add it already contains one or more libraries, then not permitted
			waspMessage("platformunit.libLackBarcodeFlowcellHasLibErr.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		
		logger.debug("1 ROB : " + counter++);
		if (siblingSampleSource != null) {//siblingSampleSource is list of samplesource objects that harbor libraries on this cell (lane) through source_sampleid
			//logger.debug("2  ROB : " + counter++);
			for (SampleSource ss: siblingSampleSource) {
				
				//TODO HERE IS THE CODE TO MAKE SURE THAT EACH LIBRARY ON A LANE IS FROM THE SAME JOB. 
				//REMEMBER THAT THIS RULE MAY NOT BE WANTED BY Mt. Sinai 
				//THIS WILL FAIL IMMEDIATELY (now) since jobId is not yet on existing records of libraries on flow cells.
			/*	SampleSourceMeta record = sampleSourceMetaService.getSampleSourceMetaByKSampleSourceId("jobId", ss.getSampleSourceId());
				if(record.getV() != null && record.getV() != "" && record.getV().equals(jobId.toString()) ){
					error = "Lanes must contain samples from the same job. Unable to add library to this lane.";
					m.put("librarySelected", librarySampleId); 
					m.put("error", error);
					return assignmentForm(resourceCategoryId.intValue(), m);//must fix this fix
				}
				*/
				
				logger.debug("3 ROB : " + counter++);
				//the source_sampleid of the each samplesource object will be the sampleid of the libraries on this cell (lane)
				Sample libraryAlreadyOnLane = ss.getSampleViaSource();
				logger.debug("4 ROB : " + counter++);
				logger.debug("4.5 ROB : " + counter++ + " sample name = " + libraryAlreadyOnLane.getName());

				List<SampleMeta> libraryAlreadyOnLaneMetaList = libraryAlreadyOnLane.getSampleMeta();
				logger.debug("5 ROB : " + counter++);
				for(SampleMeta sm : libraryAlreadyOnLaneMetaList){
					logger.debug("6 ROB : " + counter++ + " sampleid = " + sm.getSampleId() + " sampleMetaId = " + sm.getSampleMetaId());
					logger.debug("6 ROB : " + counter++ + " sampleid = " + sm.getSampleId() + " sampleMetaId = " + sm.getSampleMetaId());
					if( sm.getK() != null && "adaptorid".equals(sm.getK().substring(sm.getK().lastIndexOf(".") + 1)) ){
						logger.debug("7 TRUE they equal ROB : " + counter++);
						Adaptor libraryAdaptorAlreadyOnFlowCell;
						try{
							libraryAdaptorAlreadyOnFlowCell = adaptorService.getAdaptorByAdaptorId(new Integer(sm.getV()));
						}
						catch(Exception e){
							waspMessage("platformunit.adaptorIdError.error");
							return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
						}
						adaptorsAlreadyOnFlowCell.add(libraryAdaptorAlreadyOnFlowCell);
						/*
						if(libraryAdaptorAlreadyOnFlowCell.getBarcodesequence()==null || "".equals(libraryAdaptorAlreadyOnFlowCell.getBarcodesequence())){
							error = "The Library on this flow cell has an Adaptor with no barcode. Therefore, you may not add another library to this lane.";
						}else if(libraryAdaptorAlreadyOnFlowCell.getAdaptorId().intValue() == adaptorOnLibraryBeingAdded.getAdaptorId().intValue()){
							error = "A Library on this flow cell carries the identical Adaptor as the library you wish to add. Operation Denied.";
						}
						else if(libraryAdaptorAlreadyOnFlowCell.getBarcodesequence().equals(adaptorOnLibraryBeingAdded.getBarcodesequence())){
							error = "A library on this flow cell carries the same index barcode as the library you wish to add. Operation Denied.";
						}
						*/
					}
				}
				
				if (ss.getMultiplexindex().intValue() > maxIndex) {
					maxIndex = ss.getMultiplexindex().intValue(); 
				}
				//THIS IS NOT GOOD. This job_id would not take into account samples that are on multiple jobs.
				//what exactly is this next line for?
				//jobId = ss.getSampleViaSource().getSubmitterJobId();//useful only if one job per lane (may not always be the case).
			}
		}
		
		if(siblingSampleSource != null && siblingSampleSource.size() != adaptorsAlreadyOnFlowCell.size()){//a library is missing a valid barcode
			waspMessage("platformunit.adaptorsOnLibrariesError.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
		}
		
		for(Adaptor adaptor : adaptorsAlreadyOnFlowCell){
			if(adaptor.getBarcodesequence()==null || "".equals(adaptor.getBarcodesequence())){//case 1; adaaptors on libraries already on flowcell
				waspMessage("platformunit.barcodeMissingOnFlowcellError.error");
				return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
			}
			else if("NONE".equals(adaptor.getBarcodesequence())){//case 4, library on lane without a multiplex barcode sequece
				waspMessage("platformunit.libWithNoneOnLaneError.error");
				return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
			}
			else if(adaptor.getBarcodesequence().equals(barcodeOnLibBeingAdded)){//case 3, lib on lane with same barcode
				waspMessage("platformunit.barcodeAlreadyOnFlowcellError.error");
				return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();
			}			
		}
		
		//REMOVE THIS
/*		waspMessage("platformunit.TESTING.success");
		//return assignmentForm(resourceCategoryId.intValue(), m);//with this way, the page is not updated, so the newly added library does NOT appear on the left side of the page
		if(1==1){
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();//with this way, the page is updated but map is not passed, so SUCCESS is not displayed
		}
*/
		
		SampleSource newSampleSource = new SampleSource(); 
		newSampleSource.setSampleId(laneSampleId);
		newSampleSource.setSourceSampleId(librarySampleId);
		newSampleSource.setMultiplexindex(new Integer(maxIndex + 1));
		newSampleSource = sampleSourceService.save(newSampleSource);//capture the new samplesourceid
		SampleSourceMeta newSampleSourceMeta = new SampleSourceMeta();
		newSampleSourceMeta.setSampleSourceId(newSampleSource.getSampleSourceId());
		newSampleSourceMeta.setK("pmoleAdded");
		newSampleSourceMeta.setV(pmolAdded.toString());
		//newSampleSourceMeta.setK("jobId");
		//newSampleSourceMeta.setV(jobId.toString());
		newSampleSourceMeta.setPosition(new Integer(0));
		sampleSourceMetaService.save(newSampleSourceMeta);
		
		SampleSourceMeta newSampleSourceMeta2 = new SampleSourceMeta();
		newSampleSourceMeta2.setSampleSourceId(newSampleSource.getSampleSourceId());
		newSampleSourceMeta2.setK("jobId");
		newSampleSourceMeta2.setV(jobId.toString());
		newSampleSourceMeta2.setPosition(new Integer(1));
		sampleSourceMetaService.save(newSampleSourceMeta2);
		
		waspMessage("platformunit.libAdded.success");
		//return assignmentForm(resourceCategoryId.intValue(), m);//with this way, the page is not updated, so the newly added library does NOT appear on the left side of the page
		return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue();//with this way, the page is updated but map is not passed, so SUCCESS is not displayed
	}	

  /**
   * assignmentRemove
	 * 
	 * @param sampleSourceId
   *
   */
	@RequestMapping(value="/assignRemove.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('ft')")
	public String assignmentRemove(
			@RequestParam("samplesourceid") int sampleSourceId,
    ModelMap m) {

		SampleSource sampleSource = sampleSourceService.getSampleSourceBySampleSourceId(sampleSourceId);

		// TODO 
		// - check existence
		// - check that it is lib->lane link

		sampleSourceService.remove(sampleSource);
		sampleSourceService.flush(sampleSource);

		return "redirect:/facility/platformunit/assign.do";
  }
}
