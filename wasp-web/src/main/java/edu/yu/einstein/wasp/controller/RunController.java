package edu.yu.einstein.wasp.controller;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import edu.yu.einstein.wasp.controller.PlatformUnitController.SelectOptionsMeta;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunCellFile;
import edu.yu.einstein.wasp.model.RunFile;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/run")
public class RunController extends WaspController {

	@Autowired
	private SampleService sampleService;

	private RunDao	runDao;

	@Autowired
	public void setRunDao(RunDao runDao) {
		this.runDao = runDao;
	}

	public RunDao getRunDao() {
		return this.runDao;
	}

	@Autowired
	private RunMetaDao runMetaDao;

	private RunCellDao	runCellDao;

	@Autowired
	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public ResourceDao getResourceDao() {
		return this.resourceDao;
	}

	private ResourceDao	resourceDao;

	@Autowired
	public void setSampleDao(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
	}

	public SampleDao getSampleDao() {
		return this.sampleDao;
	}

	private SampleDao	sampleDao;

	@Autowired
	public void setSampleSubtypeResourceCategoryDao(SampleSubtypeResourceCategoryDao sampleSubtypeResourceCategoryDao) {
		this.sampleSubtypeResourceCategoryDao = sampleSubtypeResourceCategoryDao;
	}

	public SampleSubtypeResourceCategoryDao getSampleSubtypeResourceCategoryDao() {
		return this.sampleSubtypeResourceCategoryDao;
	}

	private SampleSubtypeResourceCategoryDao	sampleSubtypeResourceCategoryDao;

	@Autowired
	public void setRunCellDao(RunCellDao runCellDao) {
		this.runCellDao = runCellDao;
	}

	public RunCellDao getRunCellDao() {
		return this.runCellDao;
	}

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(RunMeta.class, request.getSession());
	}

	private final MetaHelperWebapp getMetaHelperWebappPlatformUnitInstance() {
		return new MetaHelperWebapp("platformunitInstance", SampleMeta.class, request.getSession());
	}

	private final MetaHelperWebapp getMetaHelperWebappRunInstance() {
		return new MetaHelperWebapp("runInstance", RunMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {
//		List<Run> runList = this.getRunDao().findAll();
//
//		m.addAttribute("run", runList);

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "run/list";
	}

	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		m.addAttribute("machines", this.resourceDao.findAll());
		
		m.addAttribute("flowcells", this.sampleDao.findAllPlatformUnits());
		
		List <User> allUsers = this.userDao.getActiveUsers();
		Map <Integer, String> facUsers = new HashMap<Integer, String> ();
		for (User u : allUsers) {
			List<Userrole> urs = u.getUserrole();
			for (Userrole ur : urs) {
				String rn = ur.getRole().getRoleName();
				if ("fm".equals(rn) || "ft".equals(rn)) {
					facUsers.put(u.getUserId(), u.getNameFstLst());
					break;
				}
			}
		}
		m.addAttribute("techs", facUsers);
	}

	/*
	 * Returns compatible flowcells by given machine
	 * 
	 * @Author AJ Jing
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/samplesByResourceId", method=RequestMethod.GET)	
	public String samplesByResourceId(@RequestParam("resourceId") Integer resourceId, HttpServletResponse response) {

		Map <Integer, String> resultsMap = new LinkedHashMap<Integer, String>();
		
		if (resourceId.intValue() == -1) {
			
			for (Sample sample:sampleDao.findAllPlatformUnits()) {
				resultsMap.put(sample.getSampleId(), sample.getName());
			}
			
		} else {
		
			//first get the resourceCategoryId by resourceId
			Resource machine = this.resourceDao.getById(resourceId);
			//then get all the sampleSubtypeId by resourceCatgegoryId 
			Map queryMap = new HashMap();
			queryMap.put("resourcecategoryId", machine.getResourcecategoryId());
			List <SampleSubtypeResourceCategory> ssrcList = this.sampleSubtypeResourceCategoryDao.findByMap(queryMap);
			List <Integer> idList = new ArrayList<Integer> ();
			for (SampleSubtypeResourceCategory ssrc : ssrcList) {
				idList.add(ssrc.getSampleSubtypeId());
			}
			
			//last filter all platform units by the list of sampleSubtypeId
			for(Sample sample:sampleDao.findAllPlatformUnits()) {
				if (idList.contains(sample.getSampleSubtypeId()))
					resultsMap.put(sample.getSampleId(), sample.getName());
			}
		}

		try {
			return outputJSON(resultsMap, response); 	
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+resultsMap,e);
		}
	}
	/*
	 * Returns compatible machines by given flowcell
	 * 
	 * @Author AJ Jing
	 */	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/resourcesBySampleId", method=RequestMethod.GET)	
	public String resourcesBySampleId(@RequestParam("sampleId") Integer sampleId, HttpServletResponse response) {

		Map <Integer, String> resultsMap = new LinkedHashMap<Integer, String>();
		
		if (sampleId.intValue() == -1) {
			
			for (Resource resource:resourceDao.findAll()) {
				resultsMap.put(resource.getResourceId(), resource.getName());
			}
			
		} else {
		
			//first get the sampleSubtypeId by sampleId
			Sample flowcell = sampleDao.getById(sampleId);
			//then get all the resourceCategoryId by resourceCatgegoryId 
			Map queryMap = new HashMap();
			queryMap.put("sampleSubtypeId", flowcell.getSampleSubtypeId());
			List <SampleSubtypeResourceCategory> ssrcList = this.sampleSubtypeResourceCategoryDao.findByMap(queryMap);
			List <Integer> idList = new ArrayList<Integer> ();
			for (SampleSubtypeResourceCategory ssrc : ssrcList) {
				idList.add(ssrc.getResourcecategoryId());
			}
			
			//last filter all platform units by the list of sampleSubtypeId
			for(Resource resource:resourceDao.findAll()) {
				if (idList.contains(resource.getResourcecategoryId()))
					resultsMap.put(resource.getResourceId(), resource.getName());
			}
		}

		try {
			return outputJSON(resultsMap, response); 	
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON "+resultsMap,e);
		}
	}

	@RequestMapping(value="/listJSON", method=RequestMethod.GET)	
	public String getListJSON(HttpServletResponse response) {
		
		String search = request.getParameter("_search");
		String searchStr = request.getParameter("searchString");
	
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		
		String resourceId = request.getParameter("resourceId") == null ? "" : request.getParameter("resourceId");
		String sampleId = request.getParameter("sampleId") == null ? "" : request.getParameter("sampleId");
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		List<Run> runList;
		
		if (!search.equals("true")	&& resourceId.isEmpty()	&& sampleId.isEmpty()) {
			if ("resourceName".equals(sidx))
				sidx = "resource.name";

			runList = sidx.isEmpty() ? this.runDao.findAll() : this.runDao.findAllOrderBy(sidx, sord);
		} else {
			  Map m = new HashMap();
			  
			  if (search.equals("true") && !searchStr.isEmpty())
				  m.put(request.getParameter("searchField"), request.getParameter("searchString"));
			  
			  if (!resourceId.isEmpty())
				  m.put("resourceId", Integer.parseInt(resourceId));
			  
			  if (!sampleId.isEmpty())
				  m.put("sampleId", Integer.parseInt(sampleId));
			  				  
			  runList = this.runDao.findByMap(m);
		}

		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = runList.size();										// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");
			 
			Map<String, String> runData=new HashMap<String, String>();
			runData.put("page", pageIndex + "");
			runData.put("selId",StringUtils.isEmpty(request.getParameter("selId"))?"":request.getParameter("selId"));
			jqgrid.put("rundata",runData);
			 
			List<Map> rows = new ArrayList<Map>();
			
			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			/* if the selId is set, change the page index to the one contains the selId */
			if(!StringUtils.isEmpty(request.getParameter("selId")))
			{
				int selId = Integer.parseInt(request.getParameter("selId"));
				int selIndex = runList.indexOf(runDao.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<Run> runPage = runList.subList(frId, toId);
			for (Run run:runPage) {
				Map cell = new HashMap();
				cell.put("id", run.getRunId());
				 
				List<RunMeta> runMeta = getMetaHelperWebapp().syncWithMaster(run.getRunMeta());
				
				User user = userDao.getById(run.getUserId());
				 					
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							run.getName(),
							run.getResource().getName(),
							run.getResourceId().toString(),
							run.getSample().getName(),
							run.getSampleId().toString(),
							user.getFirstName() + " " + user.getLastName(),
							"", // placeholder for resourceId, refer to the list of columns on gridcolumns.jsp
							""  // placeholder for sampleId, refer to the list of columns on gridcolumns.jsp
				}));
				 
				for (RunMeta meta:runMeta) {
					cellList.add(meta.getV());
				}
				
				 
				cell.put("cell", cellList);
				 
				rows.add(cell);
			}

			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			 
		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + runList,e);
		}
	
	}

	@RequestMapping(value = "/detail_rw/updateJSON.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('fm') or hasRole('ft')")
	public String updateDetailJSON(@RequestParam("id") Integer runId, Run runForm, ModelMap m, HttpServletResponse response) {
		boolean adding = (runId == null || runId.intValue() == 0);

		List<RunMeta> runMetaList = getMetaHelperWebapp().getFromJsonForm(request, RunMeta.class);

		runForm.setRunMeta(runMetaList);
			
		if (adding) {
			// To add new run to DB
			//check if Resource Name already exists in db; if 'true', do not allow to proceed.
			Map queryMap = new HashMap();
			queryMap.put("name", runForm.getName());
			List<Run> runList = this.runDao.findByMap(queryMap);
			if(runList!=null && runList.size()>0) {
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("run.run_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("run.run_exists.error"),e);
				}
				
			}
			
			runForm.setUserId(Integer.parseInt(request.getParameter("start_esf_staff")));
			runForm.setLastUpdTs(new Date());
			runForm.setIsActive(1);
			
			Run runDb = this.runDao.save(runForm);
			runId = runDb.getRunId();
		} else {
			
			// editing run is not allowed
		}

		runMetaDao.updateByRunId(runId, runMetaList);

		try {
			response.getWriter().println(adding ? messageService.getMessage("run.created_success.label") 
					: messageService.getMessage("run.updated_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cant output success message ", e);
		}
	}

	@RequestMapping(value = "/detail/{strId}", method = RequestMethod.GET)
	public String detail(@PathVariable("strId") String strId, ModelMap m) {
		String now = (new Date()).toString();

		Integer i;
		try {
			i = new Integer(strId);
		} catch (Exception e) {
			return "default";
		}

		Run run = this.getRunDao().getById(i.intValue());

		List<RunMeta> runMetaList = run.getRunMeta();
		runMetaList.size();

		List<RunCell> runCellList = run.getRunCell();
		runCellList.size();

		List<RunFile> runFileList = run.getRunFile();
		runFileList.size();

		m.addAttribute("now", now);
		m.addAttribute("run", run);
		m.addAttribute("runmeta", runMetaList);
		m.addAttribute("runcell", runCellList);
		m.addAttribute("runfile", runFileList);

		return "run/detail";
	}

	@RequestMapping(value = "/lane/detail/{strRunId}/{strId}", method = RequestMethod.GET)
	public String laneDetail(@PathVariable("strRunId") String strRunId, @PathVariable("strId") String strId, ModelMap m) {
		String now = (new Date()).toString();

		Integer i;
		try {
			i = new Integer(strId);
		} catch (Exception e) {
			return "default";
		}

		Integer runId;
		try {
			runId = new Integer(strRunId);
		} catch (Exception e) {
			return "default";
		}

		RunCell runCell = this.getRunCellDao().getById(i.intValue());

		//
		// TODO THROW EXCEPTION IF RUNID != RUNLANE.RUNID
		//

		List<RunCellFile> runCellFileList = runCell.getRunCellFile();
		runCellFileList.size();

		m.addAttribute("now", now);
		m.addAttribute("runcell", runCell);
		m.addAttribute("runcellfile", runCellFileList);

		return "run/lanedetail";
	}
	
	//helper method for createUpdateRun
	private List<SelectOptionsMeta> getResourceCategoryMetaList(ResourceCategory resourceCategory, String meta) {
		
		List<SelectOptionsMeta> list = new ArrayList<SelectOptionsMeta>();
		for(ResourceCategoryMeta rcm : resourceCategory.getResourceCategoryMeta()){
			if( rcm.getK().indexOf(meta) > -1 ){//such as readlength
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
				for(String token : tokens){//token could be single:single
					String[] colonTokens = token.split(":");
					list.add(new SelectOptionsMeta(colonTokens[0], colonTokens[1]));							
				}
			}		
		}	
		return list;
	}
	
	//createUpdatePlatformunit - GET
	@RequestMapping(value="/createUpdateRun.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createUpdateRun(@RequestParam("resourceId") Integer resourceId,
			@RequestParam("runId") Integer runId,
			@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam(value="reset", defaultValue="") String reset,
			ModelMap m) {	
		
		if(platformUnitId.intValue()< 0){
			platformUnitId = new Integer(0);
		}
		if(resourceId.intValue()< 0){
			resourceId = new Integer(0);
		}
		if(runId.intValue()< 0){
			runId = new Integer(0);
		}
		
		
		try{
			Format formatter = new SimpleDateFormat("MM/dd/yyyy");
			String dateRunStarted = new String("");
			String dateRunEnded = new String("COMPLETED_BY_SYSTEM");
			Sample platformUnit = null; 
			platformUnit = sampleService.getPlatformUnit(platformUnitId);
			
			m.addAttribute("typeOfPlatformUnit", platformUnit.getSampleSubtype().getName());
			m.addAttribute("barcodeName", platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
			m.addAttribute("numberOfCellsOnThisPlatformUnit", sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString());
			
			String area = getMetaHelperWebappPlatformUnitInstance().getArea();
			String readlength = MetaHelperWebapp.getMetaValue(area, "readlength", platformUnit.getSampleMeta());
			m.addAttribute("readlength", readlength);
			String readType = MetaHelperWebapp.getMetaValue(area, "readType", platformUnit.getSampleMeta());
			m.addAttribute("readType", readType);	
			String comment = MetaHelperWebapp.getMetaValue(area, "comment", platformUnit.getSampleMeta());
			m.addAttribute("comment", comment);			
			
			List<Resource> resources = sampleService.getSequencingMachinesCompatibleWithPU(platformUnit);
			m.put("resources", resources);
			
			if(resourceId.intValue() > 0){
				
				
				Run runInstance = null;
				MetaHelperWebapp metaHelperWebapp = getMetaHelperWebappRunInstance();
				
				if(runId < 1){//most likely 0
					runInstance = new Run();
					runInstance.setName("COMPLETED_BY_SYSTEM_" + platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
					//for testing the select box only runInstance.setUserId(new Integer(2));
					runInstance.setRunMeta(metaHelperWebapp.getMasterList(RunMeta.class));
				}
				else{
					
					runInstance = sampleService.getSequenceRun(runId);//throws exception if not valid mps Run in database 
					
					metaHelperWebapp.syncWithMaster(runInstance.getRunMeta());
					runInstance.setRunMeta((List<RunMeta>)metaHelperWebapp.getMetaList());
					
					dateRunStarted = new String(formatter.format(runInstance.getStartts()));//MM/dd/yyyy
					
					if(runInstance.getEnDts()!=null){
						try{
							dateRunEnded = new String(formatter.format(runInstance.getEnDts()));//MM/dd/yyyy
						}catch(Exception e){dateRunEnded=new String("UNEXPECTED PROBLEM WITH DATE");}
					}
					
					if(reset.equals("reset")){//reset permitted only when runId > 0
						resourceId = new Integer(runInstance.getResourceId().intValue());
					}
	
				}
				m.addAttribute(metaHelperWebapp.getParentArea(), runInstance);//metaHelperWebapp.getParentArea() is run
				
				Resource requestedSequencingMachine = sampleService.getSequencingMachineByResourceId(resourceId);
				m.addAttribute("readlengths", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readlength"));
				m.addAttribute("readTypes", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readType"));

				m.addAttribute("technicians", userService.getFacilityTechnicians());
				m.addAttribute("dateRunStarted", dateRunStarted);
				m.addAttribute("dateRunEnded", dateRunEnded);
			}
			
			m.addAttribute("runId", runId);
			m.addAttribute("resourceId", resourceId);
			m.addAttribute("platformUnitId", platformUnit.getSampleId().toString());			
			
		}catch(Exception e){logger.debug(e.getMessage());waspErrorMessage("wasp.unexpected_error.error"); return "redirect:/facility/platformunit/showPlatformUnit/"+platformUnitId.intValue()+".do";  /*return "redirect:/dashboard.do";*/}

		//return "redirect:/dashboard.do";
		return "run/createUpdateRun";

	}

	//createUpdatePlatformunit - Post
	@RequestMapping(value="/createUpdateRun.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createUpdatePlatformUnitPost(
			@RequestParam("resourceId") Integer resourceId,
			@RequestParam("runId") Integer runId,
			@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam("dateRunStarted") String dateRunStarted,
			@Valid Run runInstance, 
			 BindingResult result,
			 SessionStatus status, 		
			ModelMap m) throws MetadataException {
	
		System.out.println("Inside the createUpdateRun POST");
		//if(1==1){return "redirect:/dashboard.do";}
		
		try{
			
			String action = null;
			if(resourceId==null || resourceId.intValue()<0 || runId == null || runId.intValue()<0 || platformUnitId==null || platformUnitId.intValue()<=0){
				throw new Exception("Unexpected parameter problems 1: createUpdateRun - POST");
			}
			else if(runId.intValue()==0 && (runInstance.getRunId()==null || runInstance.getRunId().intValue()==0)){
				action = new String("create");
				System.out.println("create new run");
			}
			else if(runId.intValue()>0 && runInstance.getRunId()!=null && runInstance.getRunId().intValue()>0 && runId.intValue()==runInstance.getRunId().intValue()){
				action = new String("update");
				System.out.println("update existing run");
			}			
			else{
				throw new Exception("Unexpected parameter problems 2: createUpdateRun - POST");
			}
			
			MetaHelperWebapp metaHelperWebapp = getMetaHelperWebappRunInstance();
			metaHelperWebapp.getFromRequest(request, RunMeta.class);
			metaHelperWebapp.validate(result);

			boolean otherErrorsExist = false;
			
			/* PLEASE PLEASE KEEP CODE FOR LATER (I need it for reference: it was removed as per Andy, platformunit name will be assigned with barcode; it's not on the form anymore
			//SAVE THIS CODE, JUST IN CASE WE WANT TO PUT NAME BACK ONTO THE FORM
			//check whether name has been used; note that @Valid has already checked for name being the empty 
			if (! result.hasFieldErrors("name")){
				if(sampleService.platformUnitNameUsedByAnother(platformunitInstance, platformunitInstance.getName())==true){
					Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
					errors.rejectValue("name", metaHelperWebapp.getArea()+".name_exists.error", metaHelperWebapp.getArea()+".name_exists.error");
					result.addAllErrors(errors);
				}
			}
			 */
			
			Date dateRunStartedAsDateObject = null;
			
			//check for runInstance.UserId, which cannot be empty 		
			if(runInstance.getUserId()==null || runInstance.getUserId().intValue()<=0){
				String msg = messageService.getMessage(metaHelperWebapp.getArea()+".technician.error");//area here is runInstance
				m.put("technicianError", msg==null?new String("Technician cannot be empty."):msg);
				otherErrorsExist = true;
			}
			//check dateRunStarted
			if(dateRunStarted==null || "".equals(dateRunStarted.trim())){
				String msg = messageService.getMessage(metaHelperWebapp.getArea()+".dateRunStarted.error");//area here is runInstance
				m.put("dateRunStartedError", msg==null?new String("Cannot be empty."):msg);
				otherErrorsExist = true;
			}			
			else{
				try{
		
					Format formatter = new SimpleDateFormat("MM/dd/yyyy");
					dateRunStartedAsDateObject = (Date) formatter.parseObject(dateRunStarted.trim()); 				
				}catch(Exception e){
					String msg = messageService.getMessage(metaHelperWebapp.getArea()+".dateRunStartedFormat.error");//area here is runInstance
					m.put("dateRunStartedError", msg==null?new String("Incorrect Format"):msg);
					otherErrorsExist = true;
				}
			}
			
			
			if (result.hasErrors()||otherErrorsExist){
				
				System.out.println("We see some errors");
				if(otherErrorsExist){System.out.println("other errors exist");}else{System.out.println("other errors DO NOT exist");}
				
				//first deal with filling up info about the platformunit displayed on the left
				Sample platformUnit = null; 
				platformUnit = sampleService.getPlatformUnit(platformUnitId);
				
				m.addAttribute("typeOfPlatformUnit", platformUnit.getSampleSubtype().getName());
				m.addAttribute("barcodeName", platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
				m.addAttribute("numberOfCellsOnThisPlatformUnit", sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString());
				
				String area = getMetaHelperWebappPlatformUnitInstance().getArea();
				String readlength = MetaHelperWebapp.getMetaValue(area, "readlength", platformUnit.getSampleMeta());
				m.addAttribute("readlength", readlength);
				String readType = MetaHelperWebapp.getMetaValue(area, "readType", platformUnit.getSampleMeta());
				m.addAttribute("readType", readType);	
				String comment = MetaHelperWebapp.getMetaValue(area, "comment", platformUnit.getSampleMeta());
				m.addAttribute("comment", comment);			
							
				//now the run
				//fill up the metadata for the run
				runInstance.setRunMeta((List<RunMeta>) metaHelperWebapp.getMetaList());				
				//DO I NEED THIS Next line??? It seems to be sent back automagically, even if the next line is missing (next line added 10-10-12)
				m.addAttribute(metaHelperWebapp.getParentArea(), runInstance);//metaHelperWebapp.getParentArea() is run

				List<Resource> resources = sampleService.getSequencingMachinesCompatibleWithPU(platformUnit);
				m.put("resources", resources);

				Resource requestedSequencingMachine = sampleService.getSequencingMachineByResourceId(resourceId);
				m.addAttribute("readlengths", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readlength"));
				m.addAttribute("readTypes", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readType"));

				m.addAttribute("technicians", userService.getFacilityTechnicians());
				m.addAttribute("dateRunStarted",dateRunStarted);
								
				m.addAttribute("runId", runId);
				m.addAttribute("resourceId", resourceId);
				m.addAttribute("platformUnitId", platformUnit.getSampleId().toString());
				
				return "run/createUpdateRun";				
				
			}//end of if errors
			
			//should really confirm resource is OK, platformunit is ok, resource is OK for the flow cell
			if(action.equals("create")){
				//System.out.println("in create1");
				//if create, then set startts to the date in the parameter (currently that parameter does not exit)
				runInstance.setStartts(dateRunStartedAsDateObject);
				sampleService.createUpdateSequenceRun(runInstance, (List<RunMeta>)metaHelperWebapp.getMetaList(), platformUnitId, resourceId);
			}
			else if(action.equals("update")){
				//System.out.println("in update1");
				runInstance.setStartts(dateRunStartedAsDateObject);
				sampleService.createUpdateSequenceRun(runInstance, (List<RunMeta>)metaHelperWebapp.getMetaList(), platformUnitId, resourceId);
			}
			else{//action == null
				//System.out.println("in Unexpectedly1");
				throw new Exception("Unexpectedly encountered action whose value is neither create or update in createUpdateRun");
			}
			//System.out.println("end of the POST method");	
			
		}catch(Exception e){logger.debug(e.getMessage());waspErrorMessage("wasp.unexpected_error.error"); return "redirect:/facility/platformunit/showPlatformUnit/"+platformUnitId.intValue()+".do";  /*return "redirect:/dashboard.do";*/}

		return "redirect:/facility/platformunit/showPlatformUnit/"+platformUnitId.intValue()+".do";  /*return "redirect:/dashboard.do";*/
	}
	
	
}
