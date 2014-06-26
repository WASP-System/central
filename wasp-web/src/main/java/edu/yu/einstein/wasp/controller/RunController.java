package edu.yu.einstein.wasp.controller;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;

@Controller
@RequestMapping("/run")
public class RunController extends WaspController {
	
	@Autowired
	private RunService runService;

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
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	public ResourceService getResourceService() {
		return this.resourceService;
	}

	private ResourceService	resourceService;

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
	private MessageServiceWebapp messageService;
	
	@Autowired
	private UserService userService;

	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(RunMeta.class, request.getSession());
	}
	
	private static final String PLATFORM_UNIT_INSTANCE_AREA = "platformunitInstance";
	private static final String RUN_INSTANCE_AREA = "runInstance";

	private final MetaHelperWebapp getMetaHelperWebappPlatformUnitInstance() {
		return new MetaHelperWebapp(PLATFORM_UNIT_INSTANCE_AREA, SampleMeta.class, request.getSession());
	}

	private final MetaHelperWebapp getMetaHelperWebappRunInstance() {
		return new MetaHelperWebapp(RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
	}

	@RequestMapping("/list")
	public String list(ModelMap m) {
//		List<Run> runList = this.getRunDao().findAll();
//
//		m.addAttribute("run", runList);

		m.addAttribute("_metaList",	getMetaHelperWebapp().getMasterList(MetaBase.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		prepareSelectListData(m);

		return "run/list";
	}

	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		m.addAttribute("machines", this.resourceService.getResourceDao().findAll());
		
		m.addAttribute("flowcells", this.sampleDao.getPlatformUnits());
		
		List <User> allUsers = this.userDao.getActiveUsers();
		Map <Integer, String> facUsers = new HashMap<Integer, String> ();
		for (User u : allUsers) {
			List<Userrole> urs = u.getUserrole();
			for (Userrole ur : urs) {
				String rn = ur.getRole().getRoleName();
				if ("fm".equals(rn) || "ft".equals(rn)) {
					facUsers.put(u.getId(), u.getNameFstLst());
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
			
			for (Sample sample:sampleDao.getPlatformUnits()) {
				resultsMap.put(sample.getId(), sample.getName());
			}
			
		} else {
		
			//first get the resourceCategoryId by resourceId
			Resource machine = this.resourceService.getResourceDao().getById(resourceId);
			//then get all the sampleSubtypeId by resourceCatgegoryId 
			Map queryMap = new HashMap();
			queryMap.put("resourcecategoryId", machine.getResourcecategoryId());
			List <SampleSubtypeResourceCategory> ssrcList = this.sampleSubtypeResourceCategoryDao.findByMap(queryMap);
			List <Integer> idList = new ArrayList<Integer> ();
			for (SampleSubtypeResourceCategory ssrc : ssrcList) {
				idList.add(ssrc.getSampleSubtypeId());
			}
			
			//last filter all platform units by the list of sampleSubtypeId
			for(Sample sample:sampleDao.getPlatformUnits()) {
				if (idList.contains(sample.getSampleSubtypeId()))
					resultsMap.put(sample.getId(), sample.getName());
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
			
			for (Resource resource:resourceService.getResourceDao().findAll()) {
				resultsMap.put(resource.getId(), resource.getName());
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
			for(Resource resource:resourceService.getResourceDao().findAll()) {
				if (idList.contains(resource.getResourcecategoryId()))
					resultsMap.put(resource.getId(), resource.getName());
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
		
		//result
		Map <String, Object> jqgrid = new HashMap<String, Object>();

		//Parameters coming from the jobGrid
		String sord = request.getParameter("sord");//grid is set so that this always has a value
		String sidx = request.getParameter("sidx");//grid is set so that this always has a value
		String search = request.getParameter("_search");//from grid (will return true or false, depending on the toolbar's parameters)
		logger.debug("sidx = " + sidx);logger.debug("sord = " + sord);logger.debug("search = " + search);
//String selIdAsString = request.getParameter("selId");
//logger.debug("selIdAsString = " + selIdAsString);
		//Parameters coming from grid's toolbar
		//The jobGrid's toolbar's is it's search capability. The toolbar's attribute stringResult is currently set to false, 
		//meaning that each parameter on the toolbar is sent as a key:value pair
		//If stringResult = true, the parameters containing values would have been sent as a key named filters in JSON format 
		//see http://www.trirand.com/jqgridwiki/doku.php?id=wiki:toolbar_searching
		//below we capture parameters on job grid's search toolbar by name (key:value).
		String nameFromGrid = request.getParameter("name")==null?null:request.getParameter("name").trim();//if not passed,  will be null
		String platformUnitBarcodeFromGrid = request.getParameter("platformUnitBarcode")==null?null:request.getParameter("platformUnitBarcode").trim();//if not passed, will be null
		String machineAndMachineTypeFromGrid = request.getParameter("machine")==null?null:request.getParameter("machine").trim();//if not passed, will be null
		String readTypeFromGrid = request.getParameter(SequenceReadProperties.READ_TYPE_KEY)==null?null:request.getParameter(SequenceReadProperties.READ_TYPE_KEY).trim();//if not passed, will be null
		String readLengthFromGrid = request.getParameter(SequenceReadProperties.READ_LENGTH_KEY)==null?null:request.getParameter(SequenceReadProperties.READ_LENGTH_KEY).trim();//if not passed, will be null
		String dateRunStartedFromGridAsString = request.getParameter("dateRunStarted")==null?null:request.getParameter("dateRunStarted").trim();//if not passed, will be null
		String dateRunEndedFromGridAsString = request.getParameter("dateRunEnded")==null?null:request.getParameter("dateRunEnded").trim();//if not passed, will be null
		String statusForRunFromGrid = request.getParameter("statusForRun")==null?null:request.getParameter("statusForRun").trim();//if not passed, will be null
		logger.debug("nameFromGrid = " + nameFromGrid);logger.debug("platformUnitBarcodeFromGrid = " + platformUnitBarcodeFromGrid);
		logger.debug("machineAndMachineTypeFromGrid = " + machineAndMachineTypeFromGrid); 
		logger.debug("readTypeFromGrid = " + readTypeFromGrid);logger.debug("readLengthFromGrid = " + readLengthFromGrid);
		logger.debug("dateRunStartedFromGridAsString = " + dateRunStartedFromGridAsString);logger.debug("dateRunEndedFromGridAsString = " + dateRunEndedFromGridAsString);
		logger.debug("statusForRunFromGrid = " + statusForRunFromGrid);
	
		List<Run> tempRunList =  new ArrayList<Run>();
		List<Run> runsFoundInSearch = new ArrayList<Run>();
		List<Run> runList = new ArrayList<Run>();

		//convert dates (as string) to datatype Date
		Date dateRunStartedFromGridAsDate = null;
		if(dateRunStartedFromGridAsString != null){//this is yyyy/MM/dd coming from grid
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			try{				
				dateRunStartedFromGridAsDate = (Date)formatter.parse(dateRunStartedFromGridAsString); 
			}
			catch(Exception e){ 
				dateRunStartedFromGridAsDate = new Date(0);//fake it; parameter of 0 sets date to 01/01/1970 which is NOT in this database. So result set will be empty
			}
		}		
		Date dateRunEndedFromGridAsDate = null;
		if(dateRunEndedFromGridAsString != null){//this is yyyy/MM/dd coming from grid
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			try{				
				dateRunEndedFromGridAsDate = (Date)formatter.parse(dateRunEndedFromGridAsString); 
			}
			catch(Exception e){ 
				dateRunEndedFromGridAsDate = new Date(0);//fake it; parameter of 0 sets date to 01/01/1970 which is NOT in this database. So result set will be empty
			}
		}
		
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("resource.resourceType.iName", "mps");//restrict to mps
		queryMap.put("resourceCategory.resourceType.iName", "mps");//restrict to mps
		//deal with those attributes that can be searched for directly in table sample (sample.name and sample.sampleSubtype)
		if(nameFromGrid != null){
			queryMap.put("name", nameFromGrid);//and restrict to the passed name
		}		
		
		Map<String, Date> dateMap = new HashMap<String, Date>();
		if(dateRunStartedFromGridAsDate != null){
			dateMap.put("started", dateRunStartedFromGridAsDate);
		}
		if(dateRunEndedFromGridAsDate != null){
			dateMap.put("finished", dateRunEndedFromGridAsDate);
		}

		List<String> orderByColumnAndDirection = new ArrayList<String>();		
		if(sidx!=null && !"".equals(sidx)){//sord is apparently never null; default is desc
			if(sidx.equals("dateRunStarted")){
				orderByColumnAndDirection.add("started " + sord);
			}
			else if(sidx.equals("dateRunEnded")){
				orderByColumnAndDirection.add("finished " + sord);
			}
			else if(sidx.equals("name")){//run.name
				orderByColumnAndDirection.add("name " + sord);
			}
		}
		else if(sidx==null || "".equals(sidx)){
			orderByColumnAndDirection.add("started desc");
		}
		tempRunList = runDao.findByMapsIncludesDatesDistinctOrderBy(queryMap, dateMap, null, orderByColumnAndDirection);

		//deal with searching for attributes that cannot directly be dealt with by the SQL statement
		if(platformUnitBarcodeFromGrid != null || machineAndMachineTypeFromGrid != null || readTypeFromGrid != null 
				|| readLengthFromGrid != null || statusForRunFromGrid != null){
			
			if(platformUnitBarcodeFromGrid != null){
				for(Run run : tempRunList){
					List<SampleBarcode> sbList = run.getPlatformUnit().getSampleBarcode();
					if(sbList.get(0).getBarcode().getBarcode().equalsIgnoreCase(platformUnitBarcodeFromGrid)){
						runsFoundInSearch.add(run);
					}
				}
				tempRunList.retainAll(runsFoundInSearch);
				runsFoundInSearch.clear();
			}			
			if(readTypeFromGrid != null){
				for(Run run : tempRunList){
					try {
						SequenceReadProperties runReadProperties = SequenceReadProperties.getSequenceReadProperties(run, PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class);
						if(runReadProperties.getReadType().toString().equalsIgnoreCase(readTypeFromGrid))
							runsFoundInSearch.add(run);
					} catch (MetadataException e) {
						logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
					}					
				}
				tempRunList.retainAll(runsFoundInSearch);
				runsFoundInSearch.clear();
			}			
			if(readLengthFromGrid != null){
				for(Run run : tempRunList){
					try {
						SequenceReadProperties runReadProperties = SequenceReadProperties.getSequenceReadProperties(run, PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class);
						if(runReadProperties.getReadLength().equals(Integer.parseInt(readLengthFromGrid)))
							runsFoundInSearch.add(run);
					} catch (MetadataException e) {
						logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
					}				
				}
				tempRunList.retainAll(runsFoundInSearch);
				runsFoundInSearch.clear();
			}		
		
			if(machineAndMachineTypeFromGrid != null){
				String[] tokens =  machineAndMachineTypeFromGrid.split("-");
				String machineName = tokens[0].trim();
				for(Run run: tempRunList){
					if(machineName.equalsIgnoreCase(run.getResource().getName())){
						runsFoundInSearch.add(run);
					}
				}
				tempRunList.retainAll(runsFoundInSearch);
				runsFoundInSearch.clear();
			}
						
			//awful way to deal with this
			if(statusForRunFromGrid != null && !statusForRunFromGrid.isEmpty()){
				
				for(Run run : tempRunList){
					String s = "";
					if (runService.isRunSuccessfullyCompleted(run)){
						s = "Completed";
					} else if (runService.isRunActive(run)){
						s =  "In Progress";
					} else {
						s =  "Unknown";
					}
					if(s.toLowerCase().contains(statusForRunFromGrid.toLowerCase())){
						runsFoundInSearch.add(run);
					}
				}
				tempRunList.retainAll(runsFoundInSearch);
				runsFoundInSearch.clear();
			}
		}
		
		runList.addAll(tempRunList);
		
		//finally deal with sorting of items that cannot be sorted directly by the SQL statement
		if( sidx != null && !sidx.isEmpty() && !sidx.equals("dateRunStarted") && sord != null && !sord.isEmpty() ){//if sidx==dateRunStarted, it's taken care of above
			
			boolean indexSorted = false;
			
			if(sidx.equals("platformUnitBarcode")){Collections.sort(runList, new RunPlatformUnitBarcodeComparator()); indexSorted = true;}
			else if(sidx.equals("machine")){Collections.sort(runList, new MachineNameComparator()); indexSorted = true;}
			else if(sidx.equals(SequenceReadProperties.READ_LENGTH_KEY)){Collections.sort(runList, new RunMetaIsStringComparator(SequenceReadProperties.READ_LENGTH_KEY)); indexSorted = true;}
			else if(sidx.equals(SequenceReadProperties.READ_TYPE_KEY)){Collections.sort(runList, new RunMetaIsStringComparator(SequenceReadProperties.READ_TYPE_KEY)); indexSorted = true;}
			else if(sidx.equals("statusForRun")){Collections.sort(runList, new StatusForRunComparator()); indexSorted = true;}
			
			if(indexSorted == true && sord.equals("desc")){//must be last
				Collections.reverse(runList);
			}
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
			 
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			
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
				
			  //10-17-12
				MetaHelperWebapp metaHelperWebapp = getMetaHelperWebappRunInstance();
				String area2 = metaHelperWebapp.getArea();
				Format formatter = new SimpleDateFormat("yyyy/MM/dd");
				SequenceReadProperties readProperties = new SequenceReadProperties();
				try {
					readProperties = SequenceReadProperties.getSequenceReadProperties(run, PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class);
				} catch (MetadataException e) {
					logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
				}
				
				String dateRunStarted = messageService.getMessage("run.dateNotSet.label");//new String("not set");
				if(run.getStarted()!=null){
					try{				
						dateRunStarted = new String(formatter.format(run.getStarted()));//yyyy/MM/dd
					}catch(Exception e){dateRunStarted = messageService.getMessage("run.dateNotFormattedProperly.error");}					
				}
				
				String dateRunEnded = messageService.getMessage("run.dateNotSet.label");//new String("not set");
				if(run.getFinished()!=null){					
					try{				
						dateRunEnded = new String(formatter.format(run.getFinished()));//yyyy/MM/dd
					}catch(Exception e){dateRunEnded = messageService.getMessage("run.dateNotFormattedProperly.error");}					
				}
				
				String statusForRun = "";// = run.getStatus();//new String("???");
				if (runService.isRunSuccessfullyCompleted(run)){
					statusForRun = messageService.getMessage("run.statusCompleted.label");//"Completed";
				} else if (runService.isRunActive(run)){
					statusForRun =  messageService.getMessage("run.statusInProgress.label");//"In Progress";
				} else {
					statusForRun =  messageService.getMessage("run.statusUnknown.label");//"Unknown";
				}
				
				
				
				//deal with platformUnit and its barcode
				Sample platformUnit = null;
				String platformUnitBarcode = null;
				try{
					platformUnit = sampleService.getPlatformUnit(run.getSampleId());
					List<SampleBarcode> sampleBarcodeList = platformUnit.getSampleBarcode();
					platformUnitBarcode = sampleBarcodeList.size()>0 ? sampleBarcodeList.get(0).getBarcode().getBarcode() : new String("");
					
				}catch(Exception e){platformUnitBarcode = new String("???");}
				
								
				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", run.getId());	//used??			 
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						"<a href=" + getServletPath() + "/" + sampleService.getPlatformunitViewLink(platformUnit) + ">"+run.getName()+"</a>",
						"<a href=" + getServletPath() + "/" + sampleService.getPlatformunitViewLink(platformUnit) + ">"+platformUnitBarcode+"</a>",
						run.getResource().getName() + " - " + run.getResource().getResourceCategory().getName(),
						readProperties.getReadLength().toString(),
						readProperties.getReadType().toString(),
						dateRunStarted,
						dateRunEnded,
						statusForRun
				}));
				
				//for (RunMeta meta:runMeta) {//actually used??
				//	cellList.add(meta.getV());
				//}					 
				cell.put("cell", cellList);			 
				rows.add(cell);
			}
			 
			jqgrid.put("rows",rows);
			 
			return outputJSON(jqgrid, response); 	
			 
		} catch (Throwable e) {throw new IllegalStateException("Can't marshall to JSON " + runList,e);}
	
	}

	
	@RequestMapping(value = "/cell/detail/{strRunId}/{strId}", method = RequestMethod.GET)
	public String cellDetail(@PathVariable("strRunId") String strRunId, @PathVariable("strId") String strId, ModelMap m) {
		String now = (new Date()).toString();

		Integer i;
		try {
			i = new Integer(strId);
		} catch (Exception e) {
			return "default";
		}

		@SuppressWarnings("unused")
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

		m.addAttribute("now", now);
		m.addAttribute("runcell", runCell);

		return "run/celldetail";
	}
	
	@RequestMapping(value="/{runId}/delete.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String deleteRun(@PathVariable("runId") Integer runId, ModelMap m) {
		try{
			Run run = runService.getSequenceRun(runId);//exception if not msp run or not in db
			runService.delete(run);
		}catch(Exception e){
			//throw new RuntimeException(e);
			logger.warn(e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error");
		}
		return "redirect:"+ request.getHeader("Referer");
	}
	

}

//comparators - need to be moved
class RunNameComparator implements Comparator<Run> {
	@Override
	public int compare(Run arg0, Run arg1) {
		return arg0.getName().compareToIgnoreCase(arg1.getName());
	}
}
class RunStatusComparator implements Comparator<Run> {
	@Override
	public int compare(Run arg0, Run arg1) {
		////return arg0.getStatus().compareToIgnoreCase(arg1.getStatus());
		return 1;
	}
}
class MachineNameComparator implements Comparator<Run> {
	@Override
	public int compare(Run arg0, Run arg1) {
		return arg0.getResource().getName().compareToIgnoreCase(arg1.getResource().getName());
	}
}
class RunPlatformUnitBarcodeComparator implements Comparator<Run> {
	@Override
	public int compare(Run arg0, Run arg1) {
		return arg0.getPlatformUnit().getSampleBarcode().get(0).getBarcode().getBarcode().compareToIgnoreCase(arg1.getPlatformUnit().getSampleBarcode().get(0).getBarcode().getBarcode());
	}
}
class DateRunEndedComparator implements Comparator<Run> {
	@Override
	public int compare(Run arg0, Run arg1) {
		Date date0 = arg0.getFinished()==null?new Date(0):arg0.getFinished();
		Date date1 = arg1.getFinished()==null?new Date(0):arg1.getFinished();
		
		return date0.compareTo(date1);
	}
}
class RunMetaIsStringComparator implements Comparator<Run> {
	
	String metaKey;
	
	RunMetaIsStringComparator(String metaKey){
		this.metaKey = new String(metaKey);
	}
	@Override
	public int compare(Run arg0, Run arg1) {
		
		String metaValue0 = null;
		String metaValue1 = null;
		
		List<RunMeta> metaList0 = arg0.getRunMeta();
		for(RunMeta rm : metaList0){
			if(rm.getK().indexOf(metaKey) > -1){
				metaValue0 = new String(rm.getV());
				break;
			}
		}		
		
		List<RunMeta> metaList1 = arg1.getRunMeta();
		for(RunMeta rm : metaList1){
			if(rm.getK().indexOf(metaKey) > -1){
				metaValue1 = new String(rm.getV());
				break;
			}
		}
		
		if(metaValue0==null){metaValue0=new String("");} 
		if(metaValue1==null){metaValue1=new String("");}
		
		return metaValue0.compareToIgnoreCase(metaValue1);
	}
}
class StatusForRunComparator implements Comparator<Run> {
	@Autowired
	private MessageServiceWebapp messageService;
	@Autowired
	private RunService runService;

	@Override
	public int compare(Run arg0, Run arg1) {
		
		String s1 = "";
		if (runService.isRunSuccessfullyCompleted(arg0)){
			s1 = "Completed";
		} else if (runService.isRunActive(arg0)){
			s1 =  "In Progress";
		} else {
			s1 =  "Unknown";
		}
		String s2 = "";
		if (runService.isRunSuccessfullyCompleted(arg1)){
			s2 = "Completed";
		} else if (runService.isRunActive(arg1)){
			s2 =  "In Progress";
		} else {
			s2 =  "Unknown";
		}
				
		return s1.compareToIgnoreCase(s2);
	}
}
