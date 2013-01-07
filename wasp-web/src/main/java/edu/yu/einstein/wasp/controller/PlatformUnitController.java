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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
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
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleMultiplexException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl;
import edu.yu.einstein.wasp.taglib.JQFieldTag;




@Controller
@Transactional
@RequestMapping("/facility/platformunit")
public class PlatformUnitController extends WaspController {
	
	public static class LaneOptions {
		private Integer laneCount; 
		private String label; 
		
		public LaneOptions(Integer lc, String l){
			this.laneCount=lc;
			this.label=l;
		}
		
		public Integer getLaneCount(){
			return laneCount;
		}
		
		public String getLabel(){
			return label;
		}
	}
	
	public static class SelectOptionsMeta {
		private String valuePassedBack; 
		private String valueVisible; 
		
		public SelectOptionsMeta(String valuePassedBack, String valueVisible){
			this.valuePassedBack=valuePassedBack;
			this.valueVisible=valueVisible;
		}
		
		public String getValuePassedBack(){
			return valuePassedBack;
		}
		
		public String getValueVisible(){
			return valueVisible;
		}
	}

	@Autowired
	private AdaptorsetDao adaptorsetDao;

	@Autowired
	private AdaptorDao adaptorDao;

	@Autowired
	private JobService jobService;

	@Autowired
	private JobSampleDao jobSampleDao;

	@Autowired
	private JobResourcecategoryDao jobResourcecategoryDao;
	
	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	
	@Autowired
	private ResourceDao resourceDao;


	@Autowired
	private SampleMetaDao sampleMetaDao;

	@Autowired
	private SampleSourceDao sampleSourceDao;

	@Autowired
	private SampleSourceMetaDao sampleSourceMetaDao;

	
	@Autowired
	private SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	private SampleSubtypeResourceCategoryDao sampleSubtypeResourceCategoryDao;


	@Autowired
	private SampleTypeDao sampleTypeDao;

	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Autowired
	private SampleSubtypeMetaDao sampleSubtypeMetaDao;


	@Autowired
	private SampleBarcodeDao sampleBarcodeDao;
	
	@Autowired
	private RunService runService;
	
	
	
	@Autowired
	private BarcodeDao barcodeDao;
	
	@Autowired
	private UserroleDao userroleDao;
	
	@Autowired
	private MessageServiceWebapp messageService;
	  
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private SampleService sampleService;
	
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("platformunit", SampleMeta.class, request.getSession());
	}
	
	private final MetaHelperWebapp getMetaHelperWebappPlatformUnitInstance() {
		return new MetaHelperWebapp("platformunitInstance", SampleMeta.class, request.getSession());
	}

	private final MetaHelperWebapp getMetaHelperWebappRunInstance() {
		return new MetaHelperWebapp("runInstance", RunMeta.class, request.getSession());
	}

	//entry to platformunit grid
	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunit");
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));
		
		return "facility/platformunit/list";
	}
	
	//the platformunit grid
	@RequestMapping(value="/listJSON_platformUnitGrid", method=RequestMethod.GET)
	public String getListJSON(HttpServletResponse response) {
		
		Map <String, Object> jqgrid = new HashMap<String, Object>();
		
		//Parameters coming from the jobGrid
		String sord = request.getParameter("sord");//grid is set so that this always has a value
		String sidx = request.getParameter("sidx");//grid is set so that this always has a value
		String search = request.getParameter("_search");//from grid (will return true or false, depending on the toolbar's parameters)
		logger.debug("sidx = " + sidx);logger.debug("sord = " + sord);logger.debug("search = " + search);

		//Parameters coming from grid's toolbar
		//The jobGrid's toolbar's is it's search capability. The toolbar's attribute stringResult is currently set to false, 
		//meaning that each parameter on the toolbar is sent as a key:value pair
		//If stringResult = true, the parameters containing values would have been sent as a key named filters in JSON format 
		//see http://www.trirand.com/jqgridwiki/doku.php?id=wiki:toolbar_searching
		//below we capture parameters on job grid's search toolbar by name (key:value).
		String nameFromGrid = request.getParameter("name")==null?null:request.getParameter("name").trim();//if not passed, jobIdAsString will be null
		String barcodeFromGrid = request.getParameter("barcode")==null?null:request.getParameter("barcode").trim();//if not passed, will be null
		String sampleSubtypeNameFromGrid = request.getParameter("sampleSubtypeName")==null?null:request.getParameter("sampleSubtypeName").trim();//if not passed, will be null
		String readTypeFromGrid = request.getParameter("readType")==null?null:request.getParameter("readType").trim();//if not passed, will be null
		String readlengthFromGrid = request.getParameter("readlength")==null?null:request.getParameter("readlength").trim();//if not passed, will be null
		String lanecountFromGrid = request.getParameter("lanecount")==null?null:request.getParameter("lanecount").trim();//if not passed, will be null
		String dateFromGridAsString = request.getParameter("date")==null?null:request.getParameter("date").trim();//if not passed, will be null
		//next one no longer used
		String resourceCategoryNameFromGrid = request.getParameter("resourceCategoryName")==null?null:request.getParameter("resourceCategoryName").trim();//if not passed, will be null
		//logger.debug("nameFromGrid = " + nameFromGrid);logger.debug("barcodeFromGrid = " + barcodeFromGrid);
		//logger.debug("sampleSubtypeNameFromGrid = " + sampleSubtypeNameFromGrid); 
		//logger.debug("readTypeFromGrid = " + readTypeFromGrid);logger.debug("readlengthFromGrid = " + readlengthFromGrid);
		//logger.debug("lanecountFromGrid = " + lanecountFromGrid);logger.debug("dateFromGridAsString = " + dateFromGridAsString);
		//logger.debug("resourceCategoryNameFromGrid = " + resourceCategoryNameFromGrid);
		
		List<Sample> tempPlatformUnitList =  new ArrayList<Sample>();
		List<Sample> platformUnitsFoundInSearch = new ArrayList<Sample>();
		List<Sample> platformUnitList = new ArrayList<Sample>();
		
		Date dateFromGridAsDate = null;
		if(dateFromGridAsString != null){//this is MM/dd/yyyy coming from grid
			DateFormat formatter;
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			try{				
				dateFromGridAsDate = (Date)formatter.parse(dateFromGridAsString); 
			}
			catch(Exception e){ 
				dateFromGridAsDate = new Date(0);//fake it; parameter of 0 sets date to 01/01/1970 which is NOT in this database. So result set will be empty
			}
		}		
		
		Map queryMap = new HashMap();
		queryMap.put("sampleType.iName", "platformunit");//restrict to platformUnit
		//deal with those attributes that can be searched for directly in table sample (sample.name and sample.sampleSubtype)
		if(nameFromGrid != null){
			queryMap.put("name", nameFromGrid);//and restrict to the passed name
		}
		if(sampleSubtypeNameFromGrid != null){
			queryMap.put("sampleSubtype.name", sampleSubtypeNameFromGrid);//and restrict to the passed sampleSubtypeName
		}
		
		Map dateMap = new HashMap();
		if(dateFromGridAsDate != null){
			dateMap.put("receiveDts", dateFromGridAsDate);
		}
		
		List<String> orderByColumnAndDirection = new ArrayList<String>();		
		if(sidx!=null && !"".equals(sidx)){//sord is apparently never null; default is desc
			if(sidx.equals("date")){
				orderByColumnAndDirection.add("receiveDts " + sord);
			}
			else if(sidx.equals("name")){//job.name
				orderByColumnAndDirection.add("name " + sord);
			}
			else if(sidx.equals("sampleSubtypeName")){
				orderByColumnAndDirection.add("sampleSubtype.name " + sord); 
			}
		}
		else if(sidx==null || "".equals(sidx)){
			orderByColumnAndDirection.add("receiveDts desc");
		}
		
		tempPlatformUnitList = sampleService.getSampleDao().findByMapsIncludesDatesDistinctOrderBy(queryMap, dateMap, null, orderByColumnAndDirection);
		
		//have to deal with these separately. FIND certain attributes that cannot be dealt with via direct SQL
		if(barcodeFromGrid != null || readTypeFromGrid != null || readlengthFromGrid != null 
				|| lanecountFromGrid != null || resourceCategoryNameFromGrid != null){
			
			if(barcodeFromGrid != null){
				for(Sample sample : tempPlatformUnitList){
					List<SampleBarcode> sbList = sample.getSampleBarcode();
					if(sbList.get(0).getBarcode().getBarcode().equalsIgnoreCase(barcodeFromGrid)){
						platformUnitsFoundInSearch.add(sample);
					}
				}
				tempPlatformUnitList.retainAll(platformUnitsFoundInSearch);
				platformUnitsFoundInSearch.clear();
			}			
			if(readTypeFromGrid != null){
				for(Sample sample : tempPlatformUnitList){
					List<SampleMeta> sampleMetaList = sample.getSampleMeta();
					for(SampleMeta sm : sampleMetaList){
						if(sm.getK().indexOf("readType") > -1){
							if(sm.getV().equalsIgnoreCase(readTypeFromGrid)){
								platformUnitsFoundInSearch.add(sample);
							}
							break;//out of inner for loop
						}
					}					
				}
				tempPlatformUnitList.retainAll(platformUnitsFoundInSearch);
				platformUnitsFoundInSearch.clear();
			}			
			if(readlengthFromGrid != null){
				for(Sample sample : tempPlatformUnitList){
					List<SampleMeta> sampleMetaList = sample.getSampleMeta();
					for(SampleMeta sm : sampleMetaList){
						if(sm.getK().indexOf("readlength") > -1){
							if(sm.getV().equalsIgnoreCase(readlengthFromGrid)){//remember both readlengthFromGrid and the metadata value are strings (in this case, they are string representations of numbers)
								platformUnitsFoundInSearch.add(sample);
							}
							break;
						}
					}					
				}
				tempPlatformUnitList.retainAll(platformUnitsFoundInSearch);
				platformUnitsFoundInSearch.clear();
			}			
			if(lanecountFromGrid != null){
				for(Sample sample : tempPlatformUnitList){
					
					Integer numberOfIndexedCellsOnPlatformUnit;
					try{
						numberOfIndexedCellsOnPlatformUnit = sampleService.getNumberOfIndexedCellsOnPlatformUnit(sample);
					}catch(Exception e){numberOfIndexedCellsOnPlatformUnit = new Integer(0);}
					
					if(lanecountFromGrid.equals(numberOfIndexedCellsOnPlatformUnit.toString())){
						platformUnitsFoundInSearch.add(sample);
					}
				}
				tempPlatformUnitList.retainAll(platformUnitsFoundInSearch);
				platformUnitsFoundInSearch.clear();
			}			
			if(resourceCategoryNameFromGrid != null){//not used
				ResourceCategory rcRequested = resourceCategoryDao.getResourceCategoryByName(resourceCategoryNameFromGrid);
				if(rcRequested != null && rcRequested.getResourceCategoryId()!=null){//if not found, then platformUnitsFoundInSearch will be empty, and no records retained
					for(Sample sample : tempPlatformUnitList){
						List<SampleMeta> sampleMetaList = sample.getSampleMeta();
						for(SampleMeta sm : sampleMetaList){
							if(sm.getK().indexOf("resourceCategoryId") > -1){
								if(sm.getV().equals(rcRequested.getResourceCategoryId().toString())){//remember metadata is string (here representing a number)
									platformUnitsFoundInSearch.add(sample);
								}
								break;
							}
						}
					}
				}				
				tempPlatformUnitList.retainAll(platformUnitsFoundInSearch);
				platformUnitsFoundInSearch.clear();
			}
		}
		
		platformUnitList.addAll(tempPlatformUnitList);
		
		//finally deal with sorting those attributes that cannot be performed by SQL statement
		if(sidx != null && !sidx.isEmpty() && sord != null && !sord.isEmpty() ){
			
			boolean indexSorted = false;
			
			if(sidx.equals("barcode")){Collections.sort(platformUnitList, new SampleBarcodeComparator()); indexSorted = true;}
			else if(sidx.equals("readlength")){Collections.sort(platformUnitList, new SampleMetaIsIntegerComparator("readlength")); indexSorted = true;}
			else if(sidx.equals("readType")){Collections.sort(platformUnitList, new SampleMetaIsStringComparator("readType")); indexSorted = true;}
			//replaced 9-25-12    else if(sidx.equals("lanecount")){Collections.sort(platformUnitList, new SampleMetaIsIntegerComparator("lanecount")); indexSorted = true;}
			else if(sidx.equals("lanecount")){Collections.sort(platformUnitList, new SampleLanecountComparator(sampleService)); indexSorted = true;}
			else if(sidx.equals("resourceCategoryName")){Collections.sort(platformUnitList, new SampleMetaIsResourceCategoryNameComparator(resourceCategoryDao)); indexSorted = true;}

			if(indexSorted == true && sord.equals("desc")){//must be last
				Collections.reverse(platformUnitList);
			}
		}

		//Format output for grid by pages
		try {
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			int rowNum = platformUnitList.size();										// total number of rows
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
				int selIndex = platformUnitList.indexOf(jobService.getJobDao().findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}				

			List<Sample> samplePage = platformUnitList.subList(frId, toId);
			for (Sample sample : samplePage) {
				Map cell = new HashMap();
				cell.put("id", sample.getSampleId());
				 
				List<SampleMeta> sampleMetaList = sample.getSampleMeta();//getMetaHelperWebappPlatformUnitInstance().syncWithMaster(sample.getSampleMeta());
				
				String lanecount = sampleService.getNumberOfIndexedCellsOnPlatformUnit(sample).toString();//throws exception if sample is not platformUnit
				
				String readlength = "";
				String readType = "";
				
				String comment = "";
				String resourceCategoryIdAsString = "";
				Integer resourceCategoryIdAsInteger;
				ResourceCategory resourceCategory;
				for(SampleMeta sm : sampleMetaList){
					if( sm.getK().indexOf("readlength") > -1 ){
						readlength = sm.getV();
					}
					if( sm.getK().indexOf("readType") > -1 ){
						readType = sm.getV();
					}
					//9-25-12 for lanecount begin to use sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnitInDatabase);
					//if( sm.getK().indexOf("lanecount") > -1 ){
					//	lanecount = sm.getV();
					//}
					if( sm.getK().indexOf("comment") > -1 ){
						comment = sm.getV();
					}
					if( sm.getK().indexOf("resourceCategoryId") > -1 ){
						resourceCategoryIdAsString = sm.getV();
					}
				}
				//logger.debug("resourceCategoryIdAsString: " + resourceCategoryIdAsString);
				try{
					resourceCategoryIdAsInteger = Integer.valueOf(resourceCategoryIdAsString);
				}catch(NumberFormatException e){
					resourceCategoryIdAsInteger = new Integer(0);
				}
				//logger.debug("resourceCategoryIdAsInteger: " + resourceCategoryIdAsInteger.toString());
				resourceCategory = resourceCategoryDao.getResourceCategoryByResourceCategoryId(resourceCategoryIdAsInteger);
				//logger.debug("resourceCategoryName: " + resourceCategory.getName());
				
				String barcode = "";
				List<SampleBarcode> sampleBarcodeList = sample.getSampleBarcode();
				if(sampleBarcodeList != null && sampleBarcodeList.size() > 0){
					barcode = sampleBarcodeList.get(0).getBarcode().getBarcode();
				}
				
				Format formatter = new SimpleDateFormat("MM/dd/yyyy");	
				
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
							formatter.format(sample.getReceiveDts()),//use in this case as record created date
							//resourceCategory.getName(),
							"<a href=/wasp/facility/platformunit/showPlatformUnit/"+sample.getSampleId()+".do>"+sample.getName()+"</a>",
							barcode,
							sample.getSampleSubtype()==null?"": sample.getSampleSubtype().getName(),
							readType,
							readlength,
							lanecount
				}));
				 
				for (SampleMeta meta:sampleMetaList) {
					cellList.add(meta.getV());
				}				
				 
				cell.put("cell", cellList);				 
				rows.add(cell);
			}
			 
			jqgrid.put("rows",rows);
			
			return outputJSON(jqgrid, response); 	
			 
		} 
		catch (Throwable e) {throw new IllegalStateException("Can't marshall to JSON " + platformUnitList, e);}	
	}	

	//helper method for createUpdatePlatformUnit
	private List<SelectOptionsMeta> getDistinctResourceCategoryMetaListForSampleSubtype(SampleSubtype sampleSubtype, String meta) {
		
		List<SelectOptionsMeta> list = new ArrayList<SelectOptionsMeta>();
		List<String> stringList = new ArrayList<String>();
		
		List<SampleSubtypeResourceCategory> sampleSubtypeResourceCategoryList = sampleSubtype.getSampleSubtypeResourceCategory();
		for(SampleSubtypeResourceCategory ssrc : sampleSubtypeResourceCategoryList){			
		
			List<ResourceCategoryMeta> rcMetaList = ssrc.getResourceCategory().getResourceCategoryMeta();
			for(ResourceCategoryMeta rcm : rcMetaList){
			
				if( rcm.getK().indexOf(meta) > -1 ){
					String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
					for(String token : tokens){//token could be single:single
						String[] colonTokens = token.split(":");
						if(!stringList.contains(colonTokens[0])){//for distinct
							stringList.add(colonTokens[0]);
							list.add(new SelectOptionsMeta(colonTokens[0], colonTokens[1]));							
						}
					}
				}		
			}
		}	
		return list;
	}

	//deletePlatformunit - GET
	@RequestMapping(value="/deletePlatformUnit.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String deletePlatformUnit(@RequestParam("sampleId") Integer sampleId, ModelMap m) {	

		try{			
			sampleService.deletePlatformUnit(sampleId);
			
		}catch(Exception e){logger.warn(e.getMessage());waspErrorMessage("wasp.unexpected_error.error");return "redirect:/dashboard.do";}

		waspMessage("platformunitInstance.deleted_success.label");
		//return "redirect:facilit/platformunit/list.do";
		return "redirect:/dashboard.do";
	}
	
	
	//createUpdatePlatformunit - GET
	@RequestMapping(value="/createUpdatePlatformUnit.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createUpdatePlatformUnit(@RequestParam("sampleSubtypeId") Integer sampleSubtypeId, 
			@RequestParam("sampleId") Integer sampleId,
			@RequestParam(value="reset", defaultValue="") String reset,
			ModelMap m) {	
	
		if(sampleSubtypeId.intValue()<0){
			sampleSubtypeId = new Integer(0);
		}
		if(sampleId.intValue()<0){
			sampleId = new Integer(0);
		}
		
		try{
			List<SampleSubtype> sampleSubtypes = sampleService.getSampleSubtypesBySampleTypeIName("platformunit");//throws exception if SampleTypeIName not valid, otherwise return empty (size=0) or full list
			if(sampleSubtypes.size()==0){
				throw new Exception("No SampleSubtypes with SampleType of 'platformunit' found in database");
			}
			m.put("sampleSubtypes", sampleSubtypes);
			
			if(sampleSubtypeId.intValue()>0){//a type of platformunit (flowcell) has been chosen
			
				SampleSubtype sampleSubtype = null;
				Sample platformunitInstance = null;
				String barcode;
				MetaHelperWebapp metaHelperWebapp = getMetaHelperWebappPlatformUnitInstance();			
				
				if(sampleId.intValue() < 1){//most likely it's zero; new platformunit
					platformunitInstance = new Sample();
					platformunitInstance.setName("fake name to suppress name not empty requirement");//9-28-12
					platformunitInstance.setSampleMeta(metaHelperWebapp.getMasterList(SampleMeta.class));
					barcode = new String("");
					m.addAttribute("numberOfCellsOnThisPlatformUnit", new Integer(0));
				}
				else{//valid sampleId
					platformunitInstance = sampleService.getPlatformUnit(sampleId.intValue());//throws exception if not valid platformunit in database (checks both sampleType and sampleSubtype)
					
					metaHelperWebapp.syncWithMaster(platformunitInstance.getSampleMeta());
					platformunitInstance.setSampleMeta((List<SampleMeta>)metaHelperWebapp.getMetaList());
					
					if(reset.equals("reset")){//reset permitted only when sampleId > 0
						sampleSubtypeId = new Integer(platformunitInstance.getSampleSubtypeId().intValue());
					}
					
					//deal with barcode
					List<SampleBarcode> sampleBarcodeList = platformunitInstance.getSampleBarcode();
					m.addAttribute("barcode", sampleBarcodeList.size()>0 ? sampleBarcodeList.get(0).getBarcode().getBarcode() : new String(""));
					
					//deal with numberOfCellsOnPlatformUnit
					Integer numberOfCellsOnThisPlatformUnit = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformunitInstance);
					m.addAttribute("numberOfCellsOnThisPlatformUnit", numberOfCellsOnThisPlatformUnit);
				}
				m.addAttribute(metaHelperWebapp.getParentArea(), platformunitInstance);
				
				sampleSubtype = sampleService.getSampleSubtypeConfirmedForPlatformunit(sampleSubtypeId);//if not in database or not of type and subtype platformunit, throw exception
				m.addAttribute("readlengths", getDistinctResourceCategoryMetaListForSampleSubtype(sampleSubtype, "readlength"));
				m.addAttribute("readTypes", getDistinctResourceCategoryMetaListForSampleSubtype(sampleSubtype, "readType"));
				m.addAttribute("numberOfCellsList", sampleService.getNumberOfCellsListForThisTypeOfPlatformUnit(sampleSubtype));//throws exception if problems
			
			}//end of if(sampleSubtypeId.intValue()>0)				
		}catch(Exception e){logger.warn(e.getMessage());waspErrorMessage("wasp.unexpected_error.error");return "redirect:/dashboard.do";}
		
		m.put("sampleSubtypeId", sampleSubtypeId);//must be down here, as value can cahnge if "reset"
		m.put("sampleId", sampleId);

		return "facility/platformunit/createUpdatePlatformUnit";
	}
	
	//createUpdatePlatformunit - Post
	@RequestMapping(value="/createUpdatePlatformUnit.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createUpdatePlatformUnitPost(
			@RequestParam("sampleSubtypeId") Integer sampleSubtypeId,
			@RequestParam("sampleId") Integer sampleId,
			@RequestParam("barcode") String barcode,
			@RequestParam("numberOfLanesRequested") Integer numberOfLanesRequested,
			@Valid Sample platformunitInstance, 
			 BindingResult result,
			 SessionStatus status, 		
			ModelMap m) throws MetadataException {
	
		try{		
			String action = null;
			Sample platformUnitInDatabase = null;
			SampleSubtype sampleSubtype = null;
			
			sampleSubtype = sampleService.getSampleSubtypeConfirmedForPlatformunit(sampleSubtypeId);//if not in database or not of type and subtype platformunit, throw exception
			
			if(sampleId.intValue()==0 && (platformunitInstance.getSampleId()==null || platformunitInstance.getSampleId().intValue()==0)){//new platform unit
				action = "create";
			}
			else if(sampleId.intValue()>0 && platformunitInstance.getSampleId().intValue()>0 && sampleId.intValue()==platformunitInstance.getSampleId().intValue()){//update existing platform unit
				
				platformUnitInDatabase = sampleService.getPlatformUnit(sampleId);//throws exception if not valid platformunit in database (checks both sampleType and sampleSubtype)
				action = "update";	
			}
			else{//action==null
				throw new Exception("PlatformUnitController.java/createUpdatePlatformUnit.do POST: Unexpected parameter problems");
			}
			logger.debug("Action = "+ action);
	
			MetaHelperWebapp metaHelperWebapp = getMetaHelperWebappPlatformUnitInstance();
			metaHelperWebapp.getFromRequest(request, SampleMeta.class);
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
			
			//check for barcode. Since barcode is outside of Sample, @Valid is unable to to perform this check		
			if(barcode==null || "".equals(barcode)){
				String msg = messageService.getMessage(metaHelperWebapp.getArea()+".barcode.error");
				m.put("barcodeError", msg==null?new String("Barcode cannot be empty."):msg);//"Barcode cannot be empty"
				otherErrorsExist = true;
			}
			else if(sampleService.isBarcodeNameExisting(barcode)){
				if(platformUnitInDatabase==null /* this is new record, name used, so prevent */ || ( platformUnitInDatabase!=null && !barcode.equalsIgnoreCase(platformUnitInDatabase.getSampleBarcode().get(0).getBarcode().getBarcode()) /* existing record so update is requested, but barcode name used is not my barcode name, so prevent */  ) ){
					String msg = messageService.getMessage(metaHelperWebapp.getArea()+".barcode_exists.error");
					m.put("barcodeError", msg==null?new String("Barcode already exists in database."):msg);//"Barcode already exists in database."
					otherErrorsExist = true;
				}
			}
	
			Integer numberOfLanesInDatabase = null;
			
			if(numberOfLanesRequested == null || numberOfLanesRequested.intValue()<=0){//error on form
				String msg = messageService.getMessage(metaHelperWebapp.getArea()+".numberOfLanesRequested.error");
				m.put("numberOfLanesRequestedError", msg==null?new String("Lane Count cannot be empty."):msg);//"Lane count cannot be empty"
				otherErrorsExist = true;
			}	
			else if(action.equals("update")){	//if update, CHECK FOR CHANGE IN NUMBER OF LANES and begin to deal with any potential loss of libraries such a lane change would require
				numberOfLanesInDatabase = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnitInDatabase);
				if(numberOfLanesInDatabase==null || numberOfLanesInDatabase.intValue()<=0){//should never be 0 lanes on a platformunit
					throw new Exception("lanecount in database is not valid for platformunit with Id " + platformUnitInDatabase.getSampleId().intValue());
				}
				if(numberOfLanesRequested.intValue() > numberOfLanesInDatabase.intValue()){//request to add lanes, so not a problem
					;
				}
				else if(numberOfLanesRequested.intValue() < numberOfLanesInDatabase.intValue()){//request to remove lanes; a potential problem if libraries are on the lanes to be removed
					// perform next test
					if(sampleService.isRequestedReductionInCellNumberProhibited(platformUnitInDatabase, numberOfLanesRequested)){//value of true means libraries are assigned to those lanes being asked to be removed. Prohibit this action and inform user to first remove those libraries from the lanes being requested to be removed
						String msg = messageService.getMessage(metaHelperWebapp.getArea()+".numberOfLanesRequested_conflict.error");
						m.put("numberOfLanesRequestedError", msg==null?new String("Action not permitted at this time. To reduce the number of lanes, remove libraries on the lanes that will be lost."):msg);//"Lane count cannot be empty"
						otherErrorsExist = true;
					}
				}
			}	
		
			if (result.hasErrors() || otherErrorsExist == true){
				m.put("sampleSubtypeId", sampleSubtypeId);
				m.put("sampleId", sampleId);
				m.put("barcode", barcode);
				m.put("numberOfCellsOnThisPlatformUnit", numberOfLanesRequested);
				platformunitInstance.setSampleMeta((List<SampleMeta>) metaHelperWebapp.getMetaList());	
				//DO I NEED THIS Next line??? It seems to be sent back automagically, even if the next line is missing (next line added 10-10-12)
				m.addAttribute(metaHelperWebapp.getParentArea(), platformunitInstance);//metaHelperWebapp.getParentArea() is sample
				
				m.put("sampleSubtypes", sampleService.getSampleSubtypesBySampleTypeIName("platformunit"));//throws exception if SampleTypeIName not valid, otherwise return empty (size=0) or full list
				m.addAttribute("readlengths", getDistinctResourceCategoryMetaListForSampleSubtype(sampleSubtype, "readlength"));
				m.addAttribute("readTypes", getDistinctResourceCategoryMetaListForSampleSubtype(sampleSubtype, "readType"));
				m.addAttribute("numberOfCellsList", sampleService.getNumberOfCellsListForThisTypeOfPlatformUnit(sampleSubtype));//throws exception if problems
	
				return "facility/platformunit/createUpdatePlatformUnit";			
			}
			
			if(action.equals("create")){
				//logger.debug("in create1");
				sampleService.createUpdatePlatformUnit(platformunitInstance, sampleSubtype, barcode, numberOfLanesRequested, (List<SampleMeta>)metaHelperWebapp.getMetaList());
				waspMessage("platformunitInstance.created_success.label");
			}
			else if(action.equals("update")){
				//logger.debug("in update1");
				sampleService.createUpdatePlatformUnit(platformUnitInDatabase, sampleSubtype, barcode, numberOfLanesRequested, (List<SampleMeta>)metaHelperWebapp.getMetaList());
				waspMessage("platformunitInstance.updated_success.label");
			}
			else{//action == null
				//logger.debug("in Unexpectedly1");
				throw new Exception("Unexpectedly encountered action whose value is neither create or update");
			}
			//logger.debug("end of the POST method");		
			
		}catch(Exception e){logger.warn(e.getMessage());waspErrorMessage("wasp.unexpected_error.error");return "redirect:/dashboard.do";}
	
		return "redirect:/facility/platformunit/list.do"; 
	}
	

	@RequestMapping(value = "/showPlatformUnit/{sampleId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showPlatformUnit(@PathVariable("sampleId") Integer sampleId, ModelMap m){
		
		Sample platformUnit; 
		try{
			platformUnit = sampleService.getPlatformUnit(sampleId);
			m.addAttribute("platformUnitSampleId", platformUnit.getSampleId().toString());
			m.addAttribute("platformUnitSampleSubtypeId", platformUnit.getSampleSubtype().getSampleSubtypeId().toString());
			m.addAttribute("typeOfPlatformUnit", platformUnit.getSampleSubtype().getName());
			m.addAttribute("barcodeName", platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
			m.addAttribute("numberOfCellsOnThisPlatformUnit", sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString());
			
			MetaHelperWebapp metaHelperWebapp = getMetaHelperWebappPlatformUnitInstance();
			String area = metaHelperWebapp.getArea();
			String readlength = MetaHelperWebapp.getMetaValue(area, "readlength", platformUnit.getSampleMeta());
			m.addAttribute("readlength", readlength);
			String readType = MetaHelperWebapp.getMetaValue(area, "readType", platformUnit.getSampleMeta());
			m.addAttribute("readType", readType);	
			String comment = MetaHelperWebapp.getMetaValue(area, "comment", platformUnit.getSampleMeta());
			m.addAttribute("comment", comment);			
			
			/*
			Map<Integer, Sample> indexedCellMap = sampleService.getIndexedCellsOnPlatformUnit(platformUnit2);//Integer is the index in the samplesource.
			for(int i = 1; i <= indexedCellMap.size(); i++){
				
				Sample cell = indexedCellMap.get(new Integer(i));
				List<Sample> libraryList = sampleService.getLibrariesOnCell(cell);//throws exception
				
				
			}
			*/
			
			List<Run> sequenceRuns = platformUnit.getRun();
			m.addAttribute("sequenceRuns", sequenceRuns);
			
			metaHelperWebapp = getMetaHelperWebappRunInstance();//********note this is now runInstance
			String area2 = metaHelperWebapp.getArea();
			Format formatter = new SimpleDateFormat("MM/dd/yyyy");
			
			Map<Integer, Map<String, String>> runDetails = new HashMap<Integer, Map<String, String>>();
			for(Run sequenceRun : sequenceRuns){
				
				Map<String,String> detailMap = new HashMap<String, String>();
				
				String readlength2 = new String("unknown");
				try{
					readlength2 = MetaHelperWebapp.getMetaValue(area2, "readlength", sequenceRun.getRunMeta());					
				}catch(Exception e){}
				////readLengthForRuns.add(readlength2);
				detailMap.put("readlength", readlength2);
				
				String readType2 = new String("unknown");
				try{
					readType2 = MetaHelperWebapp.getMetaValue(area2, "readType", sequenceRun.getRunMeta());
				}catch(Exception e){}
				////readTypeForRuns.add(readType2);
				detailMap.put("readType", readType2);
				
				String dateRunStarted = new String("not set");
				if(sequenceRun.getStartts()!=null){
					try{				
						dateRunStarted = new String(formatter.format(sequenceRun.getStartts()));//MM/dd/yyyy
					}catch(Exception e){}					
				}
				////startDateForRuns.add(dateRunStarted);
				detailMap.put("dateRunStarted", dateRunStarted);
				
				String dateRunEnded = new String("not set");
				if(sequenceRun.getEnDts()!=null){					
					try{				
						dateRunEnded = new String(formatter.format(sequenceRun.getEnDts()));//MM/dd/yyyy
					}catch(Exception e){}
					
				}
				////endDateForRuns.add(dateRunEnded);
				detailMap.put("dateRunEnded", dateRunEnded);
				
				////statusForRuns.add(new String("???"));
				detailMap.put("runStatus", new String("???"));
				
				runDetails.put(sequenceRun.getRunId(), detailMap);
			}
			m.addAttribute("runDetails", runDetails);
		}catch(Exception e){
			logger.warn(e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error");
			return "redirect:/dashboard.do";
		}
	
		
		//10-17-12 the remainder of this page (the items on the flow cell) was not reviewed; it can no doubt do with work
		
		
		//is this flowcell on a run?
		//////10-17-12List<Run> runList = platformUnit.getRun();
		//////10-17-12m.put("runList", runList);
		//if this flowcell is on a run, it gets locked to the addition of new user-libraries
		//There are conditions under which the flow cell may need to be unlocked. Currently this will be 
		//visible on a the platform unit form only to superuser, who may unlock and later relock the flow cell.
		//Here, just determine if it's locked
		SampleServiceImpl.LockStatus platformUnitLockStatus = SampleServiceImpl.LockStatus.UNKOWN;
		try {
			platformUnitLockStatus = sampleService.getPlatformUnitLockStatus(platformUnit);
		} catch(SampleTypeException e){
			logger.warn(e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error");
			return "redirect:/dashboard.do";
		}
		m.put("platformUnitLockStatus", platformUnitLockStatus.toString());
		
		Map<Integer, String> technicians = new HashMap<Integer, String>();
		List<User> allUsers = userDao.findAll();
		for(User user : allUsers){
			for(Userrole userrole : user.getUserrole()){
				if(userrole.getRole().getRoleName().equals("fm") || userrole.getRole().getRoleName().equals("ft")){
					technicians.put(userrole.getUser().getUserId(), user.getNameFstLst());
				}
			}
		}
		m.put("technicians", technicians);
		
		List<Resource> resourceList= resourceDao.findAll(); 
		List<Resource> filteredResourceList = new ArrayList<Resource>();
		for(Resource resource : resourceList){
			//logger.debug("resource: " + resource.getName());
			for(SampleSubtypeResourceCategory ssrc : resource.getResourceCategory().getSampleSubtypeResourceCategory()){
				if(ssrc.getSampleSubtypeId().intValue() == platformUnit.getSampleSubtypeId().intValue()){
					filteredResourceList.add(resource);
					//logger.debug("it's a match");					
				}
			}
		}
		m.put("resources", filteredResourceList);
		
		List<Adaptor> allAdaptors = adaptorDao.findAll();
		Map<String, Adaptor> adaptorList = new HashMap<String, Adaptor>();
		for(Adaptor adaptor : allAdaptors){
			adaptorList.put(adaptor.getAdaptorId().toString(), adaptor);
		}
		m.addAttribute("adaptors", adaptorList);
		
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName("controlLibrarySample");
		if(sampleSubtype.getSampleSubtypeId().intValue()==0){
			//TODO throw error and get out of here ; probably go to dashboard, but would be best to go back from where you came from
			logger.warn("Unable to find sampleSubtype of controlLibrarySample");
		}
		
		Map<String, Integer> controlFilterMap = new HashMap<String, Integer>();
		controlFilterMap.put("sampleSubtypeId", sampleSubtype.getSampleSubtypeId());
		List<Sample> controlLibraryList = sampleService.getSampleDao().findByMap(controlFilterMap);
		for(Sample sample : controlLibraryList){
			logger.debug("control: " + sample.getName());
			if(sample.getIsActive().intValue()==0){
				controlLibraryList.remove(sample);
			}
		}
		sampleService.sortSamplesBySampleName(controlLibraryList);
		m.addAttribute("controlLibraryList", controlLibraryList);
		
		m.put("platformUnit", platformUnit);
		return "facility/platformunit/showPlatformUnit";
		//return "redirect:/dashboard.do";
	}
	

	
	
	/**
	 * limitPriorToAssignment: limit choices based on machine type and job having libraries to go on a platformunit 
	 */
	@RequestMapping(value="/limitPriorToAssign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String limitPriorToAssignmentForm(@RequestParam("resourceCategoryId") Integer resourceCategoryId, ModelMap m) {
		
		ResourceType resourceType = resourceTypeDao.getResourceTypeByIName("mps");
		if(resourceType == null || resourceType.getResourceTypeId()==null || resourceType.getResourceTypeId().intValue()==0){
			waspErrorMessage("platformunit.resourceTypeNotFound.error");
			return "redirect:/dashboard.do"; 
		}
		Map<String, Integer> filterForResourceCategory = new HashMap<String, Integer>();
		filterForResourceCategory.put("resourceTypeId", resourceType.getResourceTypeId());
		List<ResourceCategory> resourceCategories = resourceCategoryDao.findByMap(filterForResourceCategory);
		
		m.put("resourceCategoryId", resourceCategoryId);
		m.put("resourceCategories", resourceCategories);
		List<Job> jobList = new ArrayList<Job>();
		for (Job job: jobService.getJobsWithLibrariesToGoOnPlatformUnit()){
			JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategoryId, job.getJobId());
			if(jrc!=null && jrc.getJobResourcecategoryId()!=null && jrc.getJobResourcecategoryId().intValue() != 0){
				jobList.add(job);
			}	
		}
		Collections.sort(jobList, new JobComparator());
		m.put("jobList", jobList);

		return "facility/platformunit/limitPriorToAssign"; 
	}
	

	
	
	
  /**
   * assignmentForm
   *
   */
	@RequestMapping(value="/assign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String assignmentForm(@RequestParam("resourceCategoryId") Integer resourceCategoryId,
			@RequestParam("jobsToWorkWith") Integer jobsToWorkWith,// this will be a single jobId or it will be 0 [error] or -1 [indicating find all jobs that are not complete and have libraries to go onto flow cells]
			ModelMap m) {

		if(jobsToWorkWith.intValue()==0){//no job selected by user through the drop-down box
			waspErrorMessage("platformunit.jobIdNotSelected.error");
			return "redirect:/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0";
		}
		ResourceCategory resourceCategory = resourceCategoryDao.getResourceCategoryByResourceCategoryId(resourceCategoryId);
		if(resourceCategory.getResourceCategoryId() == 0){//machine type not found in database
			waspErrorMessage("platformunit.resourceCategoryInvalidValue.error");
			return "redirect:/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=0";
		}
		
		List<Sample> puList = sampleService.getAvailableAndCompatiblePlatformUnits(resourceCategory);
		Map<Sample, Boolean> assignLibraryToPlatformUnitStatusMap = new HashMap<Sample, Boolean>();
		Map<Sample, String> qcStatusMap = new HashMap<Sample, String>();
		Map<Sample, String> receivedStatusMap = new HashMap<Sample, String>();

		// picks up jobs
		
		List<Job> jobs = new ArrayList<Job>();
		
		if(jobsToWorkWith.intValue() > 0){//get the single job selected from the dropdown box; so parameter jobsToWorkWith has a value > 0, representing a single jobId; confirm it meets state and resourceCategory criteria
			Job job = jobService.getJobDao().getJobByJobId(jobsToWorkWith);
			if(job==null || job.getJobId()==null || job.getJobId().intValue()==0){
				waspErrorMessage("platformunit.jobNotFound.error");
				return "redirect:/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=" + resourceCategoryId;
			}
			if (jobService.getJobsWithLibrariesToGoOnPlatformUnit(resourceCategory).contains(job))
				jobs.add(job);
		}
		else if(jobsToWorkWith.intValue()==-1){//asking for list of all available jobs that meet the resourceCategoryId and state criteria; so parameter jobsToWorkWith has a value > -1 which means it's asking for all available jobs that meet state and resourceCategory criteria
			jobs =  jobService.getJobsWithLibrariesToGoOnPlatformUnit(resourceCategory);
		}
		
		// for all libraries associated with job, check they have passed QC and are awaiting platformunit placement
		for (Job job: jobs){
			for (Sample sample: job.getSample()){
				receivedStatusMap.put(sample, sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(sample)));
				if (sampleService.isLibrary(sample)){
					qcStatusMap.put(sample, sampleService.convertSampleQCStatusForWeb(sampleService.getLibraryQCStatus(sample)));
					try {
						assignLibraryToPlatformUnitStatusMap.put(sample, sampleService.isLibraryAwaitingPlatformUnitPlacement(sample) && sampleService.isLibraryPassQC(sample));
					} catch (SampleTypeException e){
						logger.warn(e.getLocalizedMessage());
					}
				} else if(sampleService.isDnaOrRna(sample)){
					qcStatusMap.put(sample, sampleService.convertSampleQCStatusForWeb(sampleService.getSampleQCStatus(sample)));
					if (sample.getChildren() == null) // no libraries made 
						continue;
					for (Sample library: sample.getChildren()){
						qcStatusMap.put(library, sampleService.convertSampleQCStatusForWeb(sampleService.getLibraryQCStatus(library)));
						try{
							assignLibraryToPlatformUnitStatusMap.put(library, sampleService.isLibraryAwaitingPlatformUnitPlacement(library) && sampleService.isLibraryPassQC(library));
						} catch (SampleTypeException e){
							logger.warn(e.getLocalizedMessage());
						}
					}
				}
			}
		}
	
		//map of adaptors for display; this really needs to be a part of the library sample
		Map<String, String> adaptors = new HashMap<String, String>();
		List<Adaptorset> adaptorsetList = adaptorsetDao.findAll();
		for(Adaptorset as : adaptorsetList){
			String adaptorsetname = new String(as.getName());
			List<Adaptor> adaptorList = as.getAdaptor();
			for(Adaptor adaptor : adaptorList){
				if( "".equals(adaptor.getBarcodesequence()) ){
					adaptors.put(adaptor.getAdaptorId().toString(), adaptorsetname);
				}
				else{
					adaptors.put(adaptor.getAdaptorId().toString(), adaptorsetname + " (Index " + adaptor.getBarcodenumber() + "; " + adaptor.getBarcodesequence() + ")");
				}
			}
		}
		
		// 12/17/12 deal with coverage
		Map<Job, Map<Sample,String>> jobCoverageMap = new HashMap<Job, Map<Sample,String>>(); 
		Map<Job, Integer> jobTotalNumberOfCellsRequested = new HashMap<Job, Integer>();
		for(Job job : jobs){
			jobTotalNumberOfCellsRequested.put(job, new Integer(job.getJobCellSelection().size()));
			jobCoverageMap.put(job, jobService.getCoverageMap(job));
		}
		m.put("jobCoverageMap", jobCoverageMap);
		m.put("jobTotalNumberOfCellsRequested", jobTotalNumberOfCellsRequested);
		
		m.put("jobsToWorkWith", jobsToWorkWith);
		m.put("machineName", resourceCategory.getName());
		m.put("resourceCategoryId", resourceCategoryId);
		m.put("jobs", jobs); 
		m.put("adaptors", adaptors);
		m.put("assignLibraryToPlatformUnitStatusMap", assignLibraryToPlatformUnitStatusMap);
		m.put("receivedStatusMap", receivedStatusMap);
		m.put("qcStatusMap", qcStatusMap);
		m.put("platformUnits", puList);
		
		return "facility/platformunit/assign"; 
	}

	
	@RequestMapping(value="/assignAdd1.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String assignmentAdd1(
			@RequestParam("librarysampleid") Integer librarySampleId,
			@RequestParam("lanesampleid") Integer laneSampleId,
			@RequestParam("jobid") Integer jobId,
			@RequestParam(value="libConcInLanePicoM", required=false) String libConcInLanePicoM,
			@RequestParam("resourceCategoryId") Integer resourceCategoryId,
			@RequestParam("jobsToWorkWith") Integer jobsToWorkWith,
			ModelMap m) {
	
		assignmentAdd(librarySampleId, laneSampleId, jobId, libConcInLanePicoM);
		return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue() + "&jobsToWorkWith=" + jobsToWorkWith.intValue();
	}
	
	@RequestMapping(value="/assignAdd2.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String assignmentAdd2(
			@RequestParam("librarysampleid") Integer librarySampleId,
			@RequestParam("lanesampleid") Integer laneSampleId,
			@RequestParam("jobid") Integer jobId,
			@RequestParam(value="libConcInLanePicoM", required=false) String libConcInLanePicoM,
			ModelMap m) {
	
		assignmentAdd(librarySampleId, laneSampleId, jobId, libConcInLanePicoM);
		return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	}
	
  /**
   * assignmentAdd
	 * 
	 * @param librarySampleId (the library being added to a flowcell lane)
	 * @param laneSampleId (lane of the flow cell)
	 * @param jobId
	 * @param libConcInLanePicoM (final concentration in pM of library being added to flow cell)
   *
   */
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public void assignmentAdd(
			Integer librarySampleId, Integer laneSampleId,
			Integer jobId, String libConcInLanePicoM) {

		Job job = jobService.getJobDao().getJobByJobId(jobId);
		Sample laneSample = sampleService.getSampleDao().getSampleBySampleId(laneSampleId); 
		Sample librarySample = sampleService.getSampleDao().getSampleBySampleId(librarySampleId); 
		JobSample jobSample = jobSampleDao.getJobSampleByJobIdSampleId(jobId, librarySampleId);//confirm library is really part of this jobId
		Float libConcInLanePicoMFloat = 0.0f;
		boolean error = false;
		
		if (jobId == null || jobId == 0 || job == null || job.getJobId() == null) {
			error = true; waspErrorMessage("platformunit.jobIdNotFound.error"); 
		}
		else if(laneSampleId == null || laneSampleId == 0){//user selected a flowcell from dropdown box (parameter laneSampleId == 0); we should actually prevent this with javascript
			error = true; waspErrorMessage("platformunit.laneIsFlowCell.error");
		}
		else if (laneSample == null || laneSample.getSampleId() == null) {
			error = true; waspErrorMessage("platformunit.laneIdNotFound.error"); 
		}
		else if (librarySampleId == null || librarySampleId == 0 || librarySample == null || librarySample.getSampleId() == null) {
			error = true; waspErrorMessage("platformunit.libraryIdNotFound.error");
		}
		else if ( ! sampleService.isLibrary(librarySample)) {
			error = true; waspErrorMessage("platformunit.libraryIsNotLibrary.error");	
		}
		else if ( ! laneSample.getSampleType().getIName().equals("cell")) { 
			error = true; waspErrorMessage("platformunit.laneIsNotLane.error");
		}
		else if(jobSample.getJobSampleId()==null || jobSample.getJobSampleId()==0){//confirm library is really part of this jobId
			error = true; waspErrorMessage("platformunit.libraryJobMismatch.error");	
		}
		else if ("".equals(libConcInLanePicoM)) {
			error = true; waspErrorMessage("platformunit.pmoleAddedInvalidValue.error");	
		}
		else{
			try{
				libConcInLanePicoMFloat = new Float(Float.parseFloat(libConcInLanePicoM));
				if(libConcInLanePicoMFloat.floatValue() <= 0){
					error = true; waspErrorMessage("platformunit.pmoleAddedInvalidValue.error");
				}
			}
			catch(Exception e){
				error = true; waspErrorMessage("platformunit.pmoleAddedInvalidValue.error");
			}
		}		

		if(error){
			return;
		}
						
		// ensure platform unit is available
		
		boolean puIsAvailable = false;
				
		List<SampleSource> parentSampleSources = laneSample.getSourceSample();//should be one
		if(parentSampleSources == null || parentSampleSources.size()!=1){
			error=true; waspErrorMessage("platformunit.flowcellNotFoundNotUnique.error");
		}
		else{
			Sample platformUnit = parentSampleSources.get(0).getSample();
			if( ! sampleService.isPlatformUnit(platformUnit) ){
				error=true; waspErrorMessage("platformunit.flowcellNotFoundNotUnique.error");
			}
			else{
				for (Sample currentPlatformUnit : sampleService.getAvailableAndCompatiblePlatformUnits(job)){
					if (currentPlatformUnit.getSampleId().equals(platformUnit.getSampleId()));
					puIsAvailable=true;
					break;
				}
				if(!puIsAvailable){
					error=true; waspErrorMessage("platformunit.flowcellStateError.error");
				}
			}
		}
		if(error){
			return;
		}
		try{
			sampleService.addLibraryToCell(laneSample, librarySample, libConcInLanePicoMFloat);
		} catch(SampleTypeException ste){
			waspErrorMessage("platformunit.sampleType.error");
			logger.warn(ste.getMessage()); // print more detailed error to warn logs
			return;
		} catch(SampleMultiplexException sme){
			waspErrorMessage("platformunit.multiplex.error");
			logger.warn(sme.getMessage()); // print more detailed error to debug logs
			return;
		} catch(SampleException se){
			waspErrorMessage("platformunit.adaptorNotFound.error");
			logger.warn(se.getMessage()); // print more detailed error to warn logs
			return;
		} catch(MetadataException me){
			waspErrorMessage("platformunit.adaptorBarcodeNotFound.error");
			logger.warn(me.getMessage()); // print more detailed error to warn logs
			return;
		}
		
		waspMessage("platformunit.libAdded.success");
		return;
	}	
	
  /**
   * assignmentRemove (GET)
	 * 
	 * @param sampleSourceId
   *
   */
	@RequestMapping(value="/assignRemove.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String assignmentRemove(
			@RequestParam("samplesourceid") int sampleSourceId,
			@RequestParam("resourceCategoryId") Integer resourceCategoryId,
			@RequestParam("jobsToWorkWith") Integer jobsToWorkWith,
			ModelMap m) {

		SampleSource cellLibraryLink = sampleSourceDao.getSampleSourceBySampleSourceId(sampleSourceId);//this samplesource should represent a cell->lib link, where sampleid is the cell and source-sampleid is the library 
		if(cellLibraryLink.getSampleSourceId()==null){//check for existence
			waspErrorMessage("platformunit.sampleSourceNotExist.error");
		}
		try{
			sampleService.removeLibraryFromCellOfPlatformUnit(cellLibraryLink);
		} catch (SampleTypeException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("platformunit.samplesourceTypeError.error");
		}
		return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue() + "&jobsToWorkWith=" + jobsToWorkWith.intValue();//with this way, the page is updated but map is not passed, so SUCCESS is not displayed
	}
	
	
	/**
	   * assignmentRemove (POST)
		 * 
		 * @param sampleSourceId
	   *
	   */
	@RequestMapping(value="/assignRemove.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String assignmentRemove(
			@RequestParam("samplesourceid") Integer sampleSourceId,
			@RequestParam(value="jobId", required=false) Integer jobId,
			@RequestParam(value="platformUnitId", required=false) Integer platformUnitId,
			ModelMap m) {

		if( jobId == null  &&  platformUnitId == null ){
			logger.warn("should provide either jobId or platformUnitId. Neither provided");
			waspErrorMessage("platformunit.parameter.error");
			return "redirect:/dashboard.do";
		}
		else if(jobId != null && platformUnitId != null){//don't know what to do
			logger.warn("should provide either jobId or platformUnitId. Both provided");
			waspErrorMessage("platformunit.parameter.error");
			return "redirect:/dashboard.do";
		}
		SampleSource cellLibraryLink = sampleSourceDao.getSampleSourceBySampleSourceId(sampleSourceId);//this samplesource should represent a cell->lib link, where sampleid is the cell and source-sampleid is the library 
		if(cellLibraryLink.getSampleSourceId()==null){//check for existence
			waspErrorMessage("platformunit.sampleSourceNotExist.error");
		}
		try{
			sampleService.removeLibraryFromCellOfPlatformUnit(cellLibraryLink);
		} catch (SampleTypeException e){
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("platformunit.samplesourceTypeError.error");
		}
		if(jobId != null){
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		else if(platformUnitId != null){
			return "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId + ".do";
		}
		return "redirect:/dashboard.do";//it really wants to see some return statement outside the if clauses
	  }
	
	

	
	/*
	 * update State of PlatformUnit (as far as it being available for accepting user libraries)
	 * a superuser can unlock or re-lock the flow cell
	 */
	@RequestMapping(value="/lockPlatformUnit.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su')")
	public String lockPlatformUnit(
			@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam("lock") String lock,
			ModelMap m) {
		
		//logger.debug("ID: " + platformUnitId.intValue());
		//logger.debug("lock: " + lock);
		String ret_value = "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId.intValue() + ".do";
		
		SampleServiceImpl.LockStatus lockStatus = SampleServiceImpl.LockStatus.UNKOWN;
		try{
			lockStatus = SampleServiceImpl.LockStatus.valueOf(lock);
		} catch(IllegalArgumentException e){
			waspErrorMessage("platformunit.lock_status.error");
			return ret_value;
		}
			
		try{
			sampleService.setPlatformUnitLockStatus(sampleService.getPlatformUnit(platformUnitId), lockStatus);
		} catch (Exception e){
			//message that platform unit not found
			logger.warn(e.getLocalizedMessage());
			waspErrorMessage("platformunit.notFoundOrNotCorrectType.error");
			return ret_value;
		}
		
		//message success
		return ret_value;
	}

	/*
	 * update sampleSourceMetaData libConcInLanePicoM
	 */
	@RequestMapping(value="/updateConcInLane.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateConcInLane(
			@RequestParam("sampleSourceMetaId") Integer sampleSourceMetaId,
			@RequestParam("libConcInLanePicoM") String libConcInLanePicoM,
			@RequestParam("platformUnitId") Integer platformUnitId,
			ModelMap m) {
		
		//TODO confirm parameters
		//confirm libConcInLanePicoM is integer or float
		//confirm platformUnitId is Id of sample that is a platformUnit
		//confirm that sampleSourceMetaId exists and k == "libConcInLanePicoM"
		SampleSourceMeta sampleSourceMeta = sampleSourceMetaDao.getSampleSourceMetaBySampleSourceMetaId(sampleSourceMetaId);
		sampleSourceMeta.setV(libConcInLanePicoM);
		sampleSourceMetaDao.save(sampleSourceMeta);
		//return "redirect:/dashboard.do";
		return "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId.intValue() + ".do";

	}
	
	/*
	 * addNewControlToLane
	 */
	@RequestMapping(value="addNewControlToLane.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String addNewControlToLane(
			@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam("cellId") Integer cellId,
			@RequestParam("newControlId") Integer newControlId,	
			@RequestParam("newControlConcInLanePicoM") String newControlConcInLanePicoM,
			ModelMap m){
		
		//TODO confirm parameters
		//confirm platformUnitId is of a flow cell
		//confirm cellId is a cell of this platformUnit
		//confirm newControlId is the id of a controlLibrary
		//confirm newControlConcInLanePicoM is a float (but store it as a string with setV)
		
		SampleSource sampleSource = new SampleSource();
		sampleSource.setSampleId(cellId);
		sampleSource.setSourceSampleId(newControlId);
		//figure out the next multiplexindex
		Sample cell = sampleService.getSampleDao().getSampleBySampleId(cellId);
		List<SampleSource> sampleSourceList = cell.getSampleSource();
		int maxMultipleIndex = 0;
		for(SampleSource ss : sampleSourceList){
			if(ss.getIndex().intValue() > maxMultipleIndex){
				maxMultipleIndex = ss.getIndex().intValue();
			}
		}
		sampleSource.setIndex(new Integer(maxMultipleIndex + 1));
		sampleSource = sampleSourceDao.save(sampleSource);
		//logger.debug("checking next multiplexIndex; new one is " + sampleSource.getMultiplexindex().intValue());
		//deal with sampleSourceMeta to set newControlConcInLanePicoM for the control
		SampleSourceMeta sampleSourceMeta = new SampleSourceMeta();
		sampleSourceMeta.setSampleSourceId(sampleSource.getSampleSourceId());
		sampleSourceMeta.setK("libConcInLanePicoM");
		sampleSourceMeta.setV(newControlConcInLanePicoM);
		sampleSourceMetaDao.save(sampleSourceMeta);
		
		//return "redirect:/dashboard.do";
		return "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId.intValue() + ".do";
	}
	
	@RequestMapping(value="ajaxReadType.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public @ResponseBody String ajaxReadType(@RequestParam("resourceId") String resourceId){
		//logger.debug("in ajaxReadType and resourceId = " + resourceId);
		String returnString;
		StringBuffer readType = new StringBuffer("<option value=''>---SELECT A READ TYPE---</option>");
		StringBuffer readLength = new StringBuffer("<option value=''>---SELECT A READ LENGTH---</option>");
		Resource resource;
		resource = resourceDao.getResourceByResourceId(new Integer(resourceId));
		ResourceCategory resourceCategory = resource.getResourceCategory();
		List<ResourceCategoryMeta> resourceCategoryMetaList = resourceCategory.getResourceCategoryMeta();
		for(ResourceCategoryMeta rcm : resourceCategoryMetaList){
			if( rcm.getK().indexOf("readType") > -1 ){
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
				for(String token : tokens){//token could be single:single
					String [] innerTokens = token.split(":");
					readType.append("<option value='"+innerTokens[0]+"'>"+innerTokens[1]+"</option>");
				}
			}
			if( rcm.getK().indexOf("readlength") > -1 ){
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be 50:50;100:100
				for(String token : tokens){//token could be 50:50
					String [] innerTokens = token.split(":");
					readLength.append("<option value='"+innerTokens[0]+"'>"+innerTokens[1]+"</option>");
				}
			}
		}
		returnString = new String(readType + "****" + readLength);
		//logger.debug("The return string = " + returnString);
		//return "<option value=''>---SELECT A READ TYPE---</option><option value='single'>single</option><option value='paired'>paired</option>";
		return returnString;
	}
	
	/*
	 * createNewRun
	 */
	@RequestMapping(value = "/createNewRun.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createNewRun(@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam("runName") String runName,
			@RequestParam("resourceId") Integer resourceId,
			@RequestParam("readLength") String readLength,
			@RequestParam("readType") String readType,
			@RequestParam("technicianId") Integer technicianId,
			@RequestParam("runStartDate") String runStartDate,
			@RequestParam(value="runId", required=false) Integer runId,
			ModelMap m){
		
		String return_string = "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId.intValue() + ".do";
		//must confirm validity of parameters
		//must add success or failure messages
		
		//first, is the flowcell (via the platformUnitId, such as an Illumina Flowcell Version 3) compatible with the resourceId (the machine instance, such as an Illumina HiSeq 2000)
		Sample platformUnit = sampleService.getSampleDao().getSampleBySampleId(platformUnitId);
		if(platformUnit.getSampleId().intValue()==0){
			//message unable to find platform unit record
			logger.warn("unable to find platform unit record");
			return "redirect:/dashboard.do";
		}
		//confirm flowcell (platformUnit)
		if( !platformUnit.getSampleType().getIName().equals("platformunit") ){
			//message - not a flow cell
			logger.warn("PlatformUnit not a flow cell");
			return return_string;
		}
		//record for machine exists
		Resource machineInstance = resourceDao.getResourceByResourceId(resourceId);
		if(machineInstance.getResourceId().intValue() == 0){
			//message: unable to find record for requested machine
			logger.warn("unable to find record for requested sequencing machine");
			return return_string;
		}
		//confirm the machine and the flow cell are compatible (via sampleSubtpeResourceCategory)
		boolean platformUnitAndMachineAreCompatible = false;
		for(SampleSubtypeResourceCategory ssrc : machineInstance.getResourceCategory().getSampleSubtypeResourceCategory()){
			if(ssrc.getSampleSubtypeId().intValue() == platformUnit.getSampleSubtypeId().intValue()){
				platformUnitAndMachineAreCompatible = true;
			}
		}
		if(platformUnitAndMachineAreCompatible==false){
			//message Platform Unit (flowcell) and Resource (sequencing machine) are Not compatible
			logger.warn("Platform Unit (flowcell) and Resource (sequencing machine) are Not compatible");
			return return_string;
		}
		Integer laneCountFromMeta = null;
		for(SampleMeta sm : platformUnit.getSampleMeta()){
			if(sm.getK().indexOf("lanecount") > -1){
				try{
					laneCountFromMeta = new Integer(sm.getV());
				}
				catch(Exception e){
					logger.warn("Unable to capture platformUnit lanecount from sampleMetaData");
					return return_string;
				}
			}
		}
		if(laneCountFromMeta == null){
			logger.warn("Unable to capture platformUnit lanecount from sampleMetaData");
			return return_string;
		}
		if(laneCountFromMeta.intValue() != platformUnit.getSampleSource().size()){
			logger.warn("lanecount from sampleMetaData and from samplesource are discordant: unable to continue");
			return return_string;
		}
		//confirm machine and parameters readLength and readType are compatible
		boolean readTypeIsValid = false;
		boolean readLengthIsValid = false;
		ResourceCategory resourceCategory = machineInstance.getResourceCategory();
		List<ResourceCategoryMeta> resourceCategoryMetaList = resourceCategory.getResourceCategoryMeta();
		for(ResourceCategoryMeta rcm : resourceCategoryMetaList){
			if( rcm.getK().indexOf("readType") > -1 ){
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
				for(String token : tokens){//token could be single:single
					String [] innerTokens = token.split(":");
					for(String str : innerTokens){
						if(readType.equals(str)){
							readTypeIsValid = true;
						}
					}
				}
			}
			if( rcm.getK().indexOf("readlength") > -1 ){
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be 50:50;100:100
				for(String token : tokens){//token could be 50:50
					String [] innerTokens = token.split(":");
					for(String str : innerTokens){
						if(readLength.equals(str)){
							readLengthIsValid = true;
						}
					}
				}
			}
		}
		if(readTypeIsValid == false){
			logger.warn("Readtype incompatible with selected machine: unable to continue");
			return return_string;			
		}
		if(readLengthIsValid == false){
			logger.warn("Readlength incompatible with selected machine: unable to continue");
			return return_string;			
		}		
		if(runName.trim() == ""){
			logger.warn("Run name is Not valid");
			return return_string;			
		}

		//check technician
		User tech = userDao.getUserByUserId(technicianId);
		List<Userrole> userRoleList = tech.getUserrole();
		boolean userIsTechnician = false;
		for(Userrole ur : userRoleList){
			if(ur.getRole().getRoleName().equals("ft") || ur.getRole().getRoleName().equals("fm")){
				userIsTechnician = true;
			}
		}
		if(userIsTechnician == false){
			logger.warn("Selected Technical Personnel is NOT listed as technician or manager");
			return return_string;				
		}
		
		Date dateStart;
		try{
			DateFormat formatter;
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			dateStart = (Date)formatter.parse(runStartDate);  
		}
		catch(Exception e){
			logger.warn("Start Date format must be MM/dd/yyyy.");
			return return_string;	
		}
		
		//new run
		if(runId==null){//new run
			try {
				runService.initiateRun(runName, machineInstance, platformUnit, tech, readLength, readType, dateStart);
				//With the creation of this new sequence run record, we want to make it that the flow cell on this
				//sequence run is no longer able to accept additional User libraries:
				sampleService.setPlatformUnitLockStatus(platformUnit, SampleServiceImpl.LockStatus.LOCKED);
				waspMessage("runInstance.created_success.label");
			} catch (SampleTypeException e) {
				waspErrorMessage("platformunit.notFoundOrNotCorrectType.error");
				logger.warn(e.getLocalizedMessage());
			} catch (MessagingException e) {
				waspErrorMessage("wasp.integration_message_send.error");
				logger.warn(e.getLocalizedMessage());
			}
		}
		else if(runId != null){//update run
			Run run = runService.getRunDao().getRunByRunId(runId);
			if(run.getRunId().intValue()==0){
				//unable to locate run record; 
				logger.warn("Update Failed: Unable to locate Sequence Run record");
				return return_string;
			}
			//confirm that the platformUnit on this run is actually the same platformUnit passed in via parameter platformUnitId
			List<Run> runList = platformUnit.getRun();
			if(runList.size() == 0){
				//platformUnit referenced through parameter platformUnitId is NOT on any sequence run
				logger.warn("Update Failed: Platform Unit " + platformUnit.getSampleId() + " is not on any sequence run");
				return return_string;
			}
			if(runList.get(0).getRunId().intValue() != run.getRunId().intValue()){
				//platformUnit referenced through parameter platformUnitId is NOT on part of This sequence run
				logger.warn("Update Failed: Platform Unit " + platformUnit.getSampleId() + " is not part of this sequence run");
				return return_string;
			}
			runService.updateRun(run, runName, machineInstance, platformUnit, tech, readLength, readType, dateStart);
			waspMessage("run.updated_success.label");
			logger.warn("Sequence run has been updated now: runStDate = " + runStartDate);
		}
		
		//return "redirect:/dashboard.do";
		return "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId.intValue() + ".do";
	}
	
	
//****************************************************************************
///no longer used I think
	@RequestMapping(value="/selid/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showSelectedSampleListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunitById");
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));

		return "facility/platformunit/selid/list";
	}
	
	/**
	 * View platform unit by selId parameter
	 * 
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/selid/listJSON", method=RequestMethod.GET)
	public @ResponseBody
	String getPlatformUnitBySelIdListJson(HttpServletResponse response) {
		
		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Sample> sampleList;

		// First, search for sampletypeid which its iname is "platform unit"
		Map<String, String> sampleTypeQueryMap = new HashMap<String, String>();
		sampleTypeQueryMap.put("iName", "platformunit");
		List<SampleType> sampleTypeList = sampleTypeDao.findByMap(sampleTypeQueryMap);
		if (sampleTypeList.size() == 0)
			return "'Platform Unit' sample type is not defined!";
		// Then, use the sampletypeid to pull all platformunits from the sample
		// table
		Map<String, Object> sampleListBaseQueryMap = new HashMap<String, Object>();
		sampleListBaseQueryMap.put("sampleTypeId", sampleTypeList.get(0).getSampleTypeId());

		if (request.getParameter("_search") == null 
				|| request.getParameter("_search").equals("false") 
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {
			sampleList = sampleService.getSampleDao().findByMap(sampleListBaseQueryMap);

		} else {

			sampleListBaseQueryMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleList = this.sampleService.getSampleDao().findByMap(sampleListBaseQueryMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSampleListBaseQueryMap = new HashMap();
				allSampleListBaseQueryMap.put("sampleTypeId", 5);

				List<Sample> allSampleList = sampleService.getSampleDao().findByMap(allSampleListBaseQueryMap);
				for (Sample excludeSample : allSampleList) {
					allSampleList.remove(excludeSample);
				}
				sampleList = allSampleList;
			}
		}

	try {
		ObjectMapper mapper = new ObjectMapper();
		
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
			int selIndex = sampleList.indexOf(this.sampleService.getSampleDao().findById(selId));
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
			List<String> cellList = new ArrayList<String>(Arrays.asList(new String[] { sample.getName(), sample.getUser().getFirstName() }));

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

	@RequestMapping(value="/instance/list.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showPlatformunitInstanceListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebappPlatformUnitInstance().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunitInstance");
		m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));
		
		prepareSelectListData(m);

		return "facility/platformunit/instance/list";
	}
	@Override
	protected void prepareSelectListData(ModelMap m) {
		super.prepareSelectListData(m);

		/**** Begin Lane Count calculations  ****/
		
		Map<String, Integer> sampleSubtypeMetaMap = new HashMap<String, Integer>();
		sampleSubtypeMetaMap.put("sampleSubtypeId", new Integer(request.getParameter("sampleSubtypeId")));

		List <SampleSubtypeMeta> sampleSubtypeMetaList = new ArrayList <SampleSubtypeMeta> (this.sampleSubtypeMetaDao.findByMap(sampleSubtypeMetaMap));
		
		Integer maxCellNum = null;
		Integer multFactor = null;
		
		
		List <LaneOptions> laneOptionsMap = new ArrayList <LaneOptions> ();
		
		for (SampleSubtypeMeta sampleSubtypeMeta : sampleSubtypeMetaList) {
			if (sampleSubtypeMeta.getK().matches(".*\\.maxCellNumber")) {
				maxCellNum = new Integer(sampleSubtypeMeta.getV());
			}
			else if (sampleSubtypeMeta.getK().matches(".*\\.multiplicationFactor")) {
				multFactor = new Integer(sampleSubtypeMeta.getV());
			}
		}
		if (multFactor == null || multFactor.intValue() <= 1) {
			laneOptionsMap.add(new LaneOptions(maxCellNum, maxCellNum.toString()));
		}
		else {
			laneOptionsMap.add(new LaneOptions(maxCellNum, maxCellNum.toString()));
			Integer cellNum = maxCellNum;
			while (cellNum >= multFactor){
				cellNum = new Integer(cellNum/multFactor.intValue());
				laneOptionsMap.add(new LaneOptions(cellNum, cellNum.toString()));
			}
		}
		m.addAttribute("lanes", laneOptionsMap);
		
		/**** End Lane Count calculations  ****/

	}

	
	/* SELECT FOR Subtypes List based on what type of machine was selected by user
	 * example: select * from samplesubtyperesourcecategory stsrc, samplesubtype sts 
	 * where stsrc.resourcecategoryid = 4 and stsrc.samplesubtypeid = sts.samplesubtypeid;
	 * 
	 */
	
	
	/**
	 * Displays a list of SampleSubtypes (e.g. illuminaFlowcellV3) based on the machine_type/resourcecategory (e.g. Illumina HiSeq 2000) 
	 * selected by the user.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/listJSON.do", method=RequestMethod.GET)
	public @ResponseBody
	String getListJson() {

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		Map<String, Object> jqgrid = new HashMap<String, Object>();
		
		List<SampleSubtype> sampleSubtypeList = new ArrayList<SampleSubtype> ();

		// First, search for sampletypeid which its iname is "platform unit"
		Map<String, String> sampleTypeQueryMap = new HashMap<String, String>();
		sampleTypeQueryMap.put("iName", "platformunit");
		List<SampleType> sampleTypeList = sampleTypeDao.findByMap(sampleTypeQueryMap);
		if (sampleTypeList.size() == 0)
			return "'Platform Unit' sample type is not defined!";
		
		// Then, use the sampletypeid to pull all platformunits from the sample
		// table
		Map<String, Object> sampleSubtypeListBaseQueryMap = new HashMap<String, Object>();
		sampleSubtypeListBaseQueryMap.put("sampleTypeId", sampleTypeList.get(0).getSampleTypeId());
		
		Map<String, Object> searchParamMap = new HashMap<String, Object>();

		List<String> orderConstraints = new ArrayList<String>();
		orderConstraints.add("name");

		if (request.getParameter("_search") == null 
				|| request.getParameter("_search").equals("false") 
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {

			sampleSubtypeList = sidx.isEmpty() ? sampleSubtypeDao.findByMap(sampleSubtypeListBaseQueryMap) : this.sampleSubtypeDao.findByMapDistinctOrderBy(sampleSubtypeListBaseQueryMap, null, orderConstraints, sord);


		} else {

			searchParamMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleSubtypeList = this.sampleSubtypeDao.findByMap(searchParamMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSampleSubtypeListBaseQueryMap = new HashMap();
				allSampleSubtypeListBaseQueryMap.put("sampleTypeId", 5);
				
				List<SampleSubtype> allSampleSubtypeList = new ArrayList<SampleSubtype>(sidx.isEmpty() ?  this.sampleSubtypeDao.findByMap(allSampleSubtypeListBaseQueryMap) : this.sampleSubtypeDao.findByMapDistinctOrderBy(allSampleSubtypeListBaseQueryMap, null, orderConstraints, sord));

				for (SampleSubtype excludeSampleSubtype : allSampleSubtypeList) {
					allSampleSubtypeList.remove(excludeSampleSubtype);
				}
				sampleSubtypeList = allSampleSubtypeList;
			}
		}

	try {
		
		Map<String, Integer> resourceCategoryMap = new HashMap<String, Integer>();
		
		//resourceCategoryMap.put("resourcecategoryId", (Integer) request.getSession().getAttribute("resourceCategoryId"));
		resourceCategoryMap.put("resourcecategoryId", new Integer(request.getParameter("resourceCategoryId")));

		List<SampleSubtype> sampleSubtypeFilteredList = new ArrayList<SampleSubtype> ();

		for (SampleSubtypeResourceCategory sampleSubtypeResCat : this.sampleSubtypeResourceCategoryDao.findByMap(resourceCategoryMap)) {
			for(SampleSubtype sampleSubtype : sampleSubtypeList) {
				if (sampleSubtype.getSampleSubtypeId().intValue() == sampleSubtypeResCat.getSampleSubtypeId().intValue()) 
					sampleSubtypeFilteredList.add(sampleSubtype);
				
			}
		
		}
		sampleSubtypeList = sampleSubtypeFilteredList;
		
		ObjectMapper mapper = new ObjectMapper();

		int pageIndex = Integer.parseInt(request.getParameter("page")); // index of page
		int pageRowNum = Integer.parseInt(request.getParameter("rows")); // number of rows in one page
		int rowNum = sampleSubtypeList.size(); // total number of rows
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
			int selIndex = sampleSubtypeList.indexOf(this.sampleSubtypeDao.findById(selId));
			frId = selIndex;
			toId = frId + 1;

			jqgrid.put("records", "1");
			jqgrid.put("total", "1");
			jqgrid.put("page", "1");
		}

		List<SampleSubtype> sampleSubtypePage = sampleSubtypeList.subList(frId, toId);
		for (SampleSubtype sampleSubtype : sampleSubtypePage) {
			Map cell = new HashMap();
			cell.put("id", sampleSubtype.getSampleSubtypeId());
			cell.put("sampleSubtypeId", sampleSubtype.getSampleSubtypeId());


			List<SampleSubtypeMeta> sampleSubtypeMetaList = getMetaHelperWebapp().syncWithMaster(sampleSubtype.getSampleSubtypeMeta());
			
			List<String> cellList = new ArrayList<String>(
					Arrays.asList(
							new String[] { 
									"<a href=/wasp/facility/platformunit/instance/list.do?sampleSubtypeId="+sampleSubtype.getSampleSubtypeId()+"&sampleTypeId="+sampleSubtype.getSampleTypeId()+">" + 
											sampleSubtype.getName() + "</a>" }));

			for (SampleSubtypeMeta meta : sampleSubtypeMetaList) {
				cellList.add(meta.getV());
			}

			cell.put("cell", cellList);

			rows.add(cell);
		}

		jqgrid.put("rows", rows);

		String json = mapper.writeValueAsString(jqgrid);
		return json;

	} catch (Exception e) {
			throw new IllegalStateException("Can't marshall to JSON " + sampleSubtypeList, e);
		}
	}
	
	/**
	 * Displays Platform Unit list filtered by sampleSubtypeId
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/instance/listJSON.do", method=RequestMethod.GET)
	public @ResponseBody
	String getPlatformInstanceListJson() {

		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Sample> sampleList;

		Map<String, Integer> sampleListBaseQueryMap = new HashMap<String, Integer>();
		Map<String, String> searchParamMap = new HashMap<String, String>();

		sampleListBaseQueryMap.put("sampleSubtypeId", new Integer(request.getParameter("sampleSubtypeId")));
		sampleListBaseQueryMap.put("sampleTypeId", new Integer(request.getParameter("sampleTypeId")));

		
		List<String> orderConstraints = new ArrayList<String>();
		orderConstraints.add("name");

		if (request.getParameter("_search") == null || StringUtils.isEmpty(request.getParameter("searchString"))) {
			sampleList = sidx.isEmpty() ? this.sampleService.getSampleDao().findByMap(sampleListBaseQueryMap) : this.sampleService.getSampleDao().findByMapDistinctOrderBy(sampleListBaseQueryMap, null, orderConstraints, sord);
		} else {

			searchParamMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleList = this.sampleService.getSampleDao().findByMap(searchParamMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Sample> allSamples = new ArrayList<Sample>(sidx.isEmpty() ?  this.sampleService.getSampleDao().findByMap(sampleListBaseQueryMap) : this.sampleService.getSampleDao().findByMapDistinctOrderBy(sampleListBaseQueryMap, null, orderConstraints, sord));

				for (Iterator<Sample> it = sampleList.iterator(); it.hasNext();) {
					Sample excludeSample = it.next();
					allSamples.remove(excludeSample);
				
				}
				sampleList = allSamples;
			}
		}

	try {
		ObjectMapper mapper = new ObjectMapper();
		
		Map<Integer, Integer> allSampleBarcode = new TreeMap<Integer, Integer>();
		for (SampleBarcode sampleBarcode : this.sampleBarcodeDao.findAll()) {
			if (sampleBarcode != null) {
				
				allSampleBarcode.put(sampleBarcode.getSampleId(), sampleBarcode.getBarcodeId());
			}
		}
		
		Map<Integer, String> allBarcode = new TreeMap<Integer, String>();
		for (Barcode barcode : this.barcodeDao.findAll()) {
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
		
		/***** Begin Sort by name *****/
		class PlatformUnitNameComparator implements Comparator<Sample> {
			@Override
			public int compare(Sample arg0, Sample arg1) {
				return arg0.getName().compareToIgnoreCase(arg1.getName());
			}
		}
		if (sidx.equals("name")) {
			Collections.sort(sampleList, new PlatformUnitNameComparator());
			if (sord.equals("desc"))
				Collections.reverse(sampleList);
		}
		/***** End Sort by name *****/

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
			int selIndex = sampleList.indexOf(sampleService.getSampleDao().findById(selId));
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
										sample.getName() + " (<a href=/wasp/facility/platformunit/showPlatformUnit/"+sample.getSampleId()+".do>details</a>)",
										allSampleBarcode.get(sample.getSampleId())==null? "" : allBarcode.get(allSampleBarcode.get(sample.getSampleId())),
										sample.getSampleSubtype()==null?"": sample.getSampleSubtype().getName(), 
										sample.getUser().getFirstName()+" "+sample.getUser().getLastName(),
										this.sampleMetaDao.getSampleMetaByKSampleId("platformunitInstance.lanecount", sample.getSampleId()).getV(),
										this.sampleMetaDao.getSampleMetaByKSampleId("platformunitInstance.comment", sample.getSampleId()).getV()}));

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

	@RequestMapping(value="/instance/updateJSON", method=RequestMethod.POST)
	public String updateJson(
			@RequestParam("id") Integer sampleId,
			@Valid Sample sampleForm, 
			ModelMap m, 
			HttpServletResponse response) {

		List<SampleMeta> sampleMetaList = getMetaHelperWebapp().getFromJsonForm(request, SampleMeta.class);
		sampleForm.setSampleMeta(sampleMetaList);
		//sampleForm.setSampleId(sampleId); //do not set id here.  It will throw the "detached entity exception" when calling persist() on this object.
		if (sampleId == null || sampleId == 0) {
			//check if barcode already exists in Db; if 'true', do not allow to proceed.
			if(this.barcodeDao.getBarcodeByBarcode(request.getParameter("barcode")).getBarcode() != null && 
					this.barcodeDao.getBarcodeByBarcode(request.getParameter("barcode")).getBarcode().length() != 0) {
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("platformunitInstance.barcode_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("platformunitInstance.barcode_exists.error"),e);
				}
				
			}
			
			//check if Sample Name already exists in db; if 'true', do not allow to proceed.
			if(this.sampleService.getSampleDao().getSampleByName(request.getParameter("name")).getName() != null) {
				
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("platformunitInstance.name_exists.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("platformunitInstance.name_exists.error"),e);
				}
				
			}
			if(request.getParameter("platformunitInstance.lanecount") == null || request.getParameter("platformunitInstance.lanecount").equals("")) {
				
				try{
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					response.getWriter().println(messageService.getMessage("platformunitInstance.lanecount_empty.error"));
					return null;
				} catch (Throwable e) {
					throw new IllegalStateException("Cant output validation error "+messageService.getMessage("platformunitInstance.lanecount_empty.error"),e);
				}
			}
		}
		sampleForm.setSampleSubtypeId(new Integer(request.getParameter("sampleSubtypeId")));
		preparePlatformUnit(sampleForm, sampleId);
		Integer laneCount = new Integer(request.getParameter("platformunitInstance.lanecount")==null || request.getParameter("platformunitInstance.lanecount").equals("")?request.getParameter("lanecountForEditBox"):request.getParameter("platformunitInstance.lanecount"));
		updatePlatformUnitInstance(sampleForm, laneCount, sampleId, request.getParameter("platformunitInstance.comment"));

		try {
			response.getWriter().println(messageService.getMessage("platformunitInstance.updated_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cannot output success message ", e);
		}

	}

	@RequestMapping(value="/view/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String viewPlatformUnit(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleService.getSampleDao().getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_ro";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updatePlatformUnitForm(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleService.getSampleDao().getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_rw";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String modifyPlatformUnit(
		@PathVariable("sampleId") Integer sampleId,
		@Valid Sample sampleForm,
		BindingResult result,
		SessionStatus status,
		ModelMap m) {

		preparePlatformUnit(sampleForm, sampleId);

		return validateAndUpdatePlatformUnit(sampleForm, result, status, m);
	}


	/**
	 * 
	 * @param sampleForm
	 * @return
	 */
	public Sample preparePlatformUnit(Sample sampleForm, Integer sampleId) {
		if (sampleId == null || sampleId.intValue() == 0) {
			User me = authenticationService.getAuthenticatedUser();
			sampleForm.setSubmitterUserId(me.getUserId());

			SampleType sampleType = sampleTypeDao.getSampleTypeByIName("platformunit");
			sampleForm.setSampleTypeId(sampleType.getSampleTypeId());
			sampleForm.setSubmitterLabId(1);
	
			sampleForm.setReceiverUserId(sampleForm.getSubmitterUserId());
			sampleForm.setReceiveDts(new Date());
			sampleForm.setIsReceived(1);
			sampleForm.setIsActive(1);
			sampleForm.setIsGood(1);
			
		} else {
			Sample sampleDb =	sampleService.getSampleDao().getSampleBySampleId(sampleId);
			
			//SampleBarcode resourceBarcodeDB = this.sampleBarcodeDao.getSampleBarcodeBySampleId(sampleForm.getSampleId());


			// TODO do compares that i am the same sample as sampleform, and not new
	
			// fetches the updates
			// sampleDb.setName(sampleForm.getName());
	
	
			sampleForm.setSubmitterUserId(sampleDb.getSubmitterUserId());
			sampleForm.setSubmitterLabId(sampleDb.getSubmitterLabId());
			sampleForm.setSampleTypeId(sampleDb.getSampleTypeId());
	
			sampleForm.setReceiverUserId(sampleDb.getReceiverUserId());
			sampleForm.setReceiveDts(sampleDb.getReceiveDts());
			sampleForm.setIsReceived(sampleDb.getIsReceived());
			sampleForm.setIsActive(sampleDb.getIsActive());
			sampleForm.setIsGood(1);
			sampleForm.setSampleId(sampleId);
		}
		return sampleForm;
	}
	

	/**
	 * Creates cells based on number of lanes selected by the user 
	 * 
	 * @param sampleForm
	 * @param laneNumber
	 * @return
	 */
	public String createUpdateCell(Sample sampleForm, Integer laneNumber, Integer sampleId) {
		
		Integer sampleTypeId = sampleTypeDao.getSampleTypeByIName("cell").getSampleTypeId();
        Sample sampleDb = null;

		if (sampleId == null || sampleId.intValue() == 0) {

			for (int i = 0; i < laneNumber.intValue(); i++) {
				 Sample cell = new Sample();
				 cell.setSubmitterLabId(sampleForm.getSubmitterLabId());
				 cell.setSubmitterUserId(sampleForm.getSubmitterUserId());
				 cell.setName(sampleForm.getName()+"/"+(i+1));
				 cell.setSampleTypeId(sampleTypeId);
				 cell.setIsGood(1);
				 cell.setIsActive(1);
				 cell.setIsReceived(1);
				 cell.setReceiverUserId(sampleForm.getSubmitterUserId());
				 cell.setReceiveDts(new Date());
				 sampleDb = this.sampleService.getSampleDao().save(cell);
				 
				 SampleSource sampleSource = new SampleSource();
				 sampleSource.setSampleId(sampleForm.getSampleId());
				 sampleSource.setSourceSampleId(sampleDb.getSampleId());
				 sampleSource.setIndex(i+1);
				 this.sampleSourceDao.save(sampleSource);
	 			 
			 }
		}
		else {
			
			Map<String, Integer> sampleSourceMap = new HashMap<String, Integer>();
			Map<String, Integer> sampleMap = new HashMap<String, Integer>();

			sampleSourceMap.put("sampleId", sampleForm.getSampleId());
			List <SampleSource> sampleSourceList= this.sampleSourceDao.findByMap(sampleSourceMap);
			/** If the user changed the sample name, update all corresponding cells names **/
			for (Iterator<SampleSource> it = sampleSourceList.iterator(); it.hasNext();) {
				SampleSource tr = it.next();
				Sample cell = this.sampleService.getSampleDao().findById(tr.getSourceSampleId());
				String cellName = cell.getName();
				String subString = cellName.substring(cellName.lastIndexOf("/"), cellName.length());
				cell.setName(sampleForm.getName().concat(subString));
				this.sampleService.getSampleDao().merge(cell);
								
			}
			
		}
		return "redirect:/facility/platformunit/ok";
	}

	public String validateAndUpdatePlatformUnit(
		Sample sampleForm,
		BindingResult result,
		SessionStatus status,
		ModelMap m) {
		MetaHelperWebapp metaHelper = getMetaHelperWebapp();
		List<SampleMeta> sampleMetaList = metaHelper.getFromRequest(request, SampleMeta.class);

		metaHelper.validate(result);

		if (result.hasErrors()) {
			// TODO REAL ERROR
			waspErrorMessage("hello.error");

			sampleForm.setSampleMeta(sampleMetaList);
			m.put("sample", sampleForm);

			return "facility/platformunit/detail_rw";
		}

		sampleForm.setSampleMeta(sampleMetaList);

		String returnString = updatePlatformUnit(sampleForm);


		return returnString;
	}


	// TODO move to service?
	public String updatePlatformUnit( Sample sampleForm) {
	
		Sample sampleDb;
		if (sampleForm.getSampleId() == null || sampleForm.getSampleId().intValue() == 0) {
			sampleDb = sampleService.getSampleDao().save(sampleForm);
		} else {
			sampleDb = sampleService.getSampleDao().merge(sampleForm);
		}

		sampleMetaDao.updateBySampleId(sampleDb.getSampleId(), sampleForm.getSampleMeta());

		return "redirect:/facility/platformunit/ok";
	}
	
	/**
	 * 
	 * @param sampleForm
	 * @param sampleBarcode
	 * @param laneCount
	 * @return
	 */
	public String updatePlatformUnitInstance( Sample sampleForm, Integer laneCount, Integer sampleId, String comment) {
	
		Sample sampleDb;
		if (sampleId == null || sampleId.intValue() == 0) {
			
			sampleDb = sampleService.getSampleDao().save(sampleForm);
			
			SampleBarcode sampleBarcode = new SampleBarcode();
			Barcode barcode = new Barcode();
			
			barcode.setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
			barcode.setIsActive(new Integer(1));

			sampleBarcode.setBarcode(barcode);
			
			Barcode barcodeDB = this.barcodeDao.save(barcode);//save new barcode
			sampleBarcode.setBarcodeId(barcodeDB.getBarcodeId()); // set new barcodeId in samplebarcode

			sampleBarcode.setSampleId(sampleDb.getSampleId());
			this.sampleBarcodeDao.save(sampleBarcode);
		
		} else {
			sampleDb = sampleService.getSampleDao().merge(sampleForm);
			
			SampleBarcode sampleBarcodeDb = this.sampleBarcodeDao.getSampleBarcodeBySampleId(sampleId);

			if (sampleBarcodeDb == null || sampleBarcodeDb.getBarcode() == null) {
				SampleBarcode sampleBarcode = new SampleBarcode();
				Barcode barcode = new Barcode();
				
				barcode.setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
				barcode.setIsActive(1);
				sampleBarcode.setBarcode(barcode);
				
				Barcode barcodeDb = this.barcodeDao.save(barcode);
				sampleBarcode.setBarcodeId(barcodeDb.getBarcodeId());
			
				sampleBarcode.setSampleId(sampleForm.getSampleId());
				this.sampleBarcodeDao.save(sampleBarcode);
				
			}
			else { 
				sampleBarcodeDb.getBarcode().setBarcode(request.getParameter("barcode")==null? "" : request.getParameter("barcode"));
				this.sampleBarcodeDao.merge(sampleBarcodeDb);
			}

		}
		
		List<SampleMeta> mySampleMeta = sampleDb.getSampleMeta();
		MetaHelperWebapp sampleMetaHelper = getMetaHelperWebappPlatformUnitInstance();
		sampleMetaHelper.syncWithMaster(mySampleMeta);
		try {
			sampleMetaHelper.setMetaValueByName("lanecount", laneCount.toString());
			sampleMetaHelper.setMetaValueByName("comment", comment);
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // set a value for a member of the list by name
		sampleMetaDao.updateBySampleId(sampleDb.getSampleId(), (List<SampleMeta>) sampleMetaHelper.getMetaList()); // now we get the list and persist it

		createUpdateCell(sampleDb, laneCount, sampleId);
		
		return "redirect:/facility/platformunit/ok";
	}
	

	/**
	 * limitPriorToPlatformUnitAssignment
	 */
	@RequestMapping(value="/limitPriorToPlatUnitAssign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String limitPriorToPlatUnitAssignForm(ModelMap m) {
		
		ResourceType resourceType = resourceTypeDao.getResourceTypeByIName("mps");
		if(resourceType.getResourceTypeId()==null){
			waspErrorMessage("platformunit.resourceCategoryNotFound.error");
			return "redirect:/dashboard.do";
		}
		Map filterForResourceCategory = new HashMap();
		filterForResourceCategory.put("resourceTypeId", resourceType.getResourceTypeId());
		List<ResourceCategory> resourceCategories = resourceCategoryDao.findByMap(filterForResourceCategory);
		
		m.put("resourceCategories", resourceCategories);
		return "facility/platformunit/limitPriorToPlatUnitAssign"; 
	}
	

//****************************************************************************//****************************************************************************	
//there are comparator classes below that need to be moved	
}

class JobComparator implements Comparator<Job> {
    @Override
    public int compare(Job arg0, Job arg1) {
    	return arg0.getJobId().compareTo(arg1.getJobId());
    }
}
class SampleNameComparator implements Comparator<Sample> {
	@Override
	public int compare(Sample arg0, Sample arg1) {
		return arg0.getName().compareToIgnoreCase(arg1.getName());
	}
}
class SampleSubtypeNameComparator implements Comparator<Sample> {
	@Override
	public int compare(Sample arg0, Sample arg1) {
		return arg0.getSampleSubtype().getName().compareToIgnoreCase(arg1.getSampleSubtype().getName());
	}
}
class SampleReceiveDateComparator implements Comparator<Sample> {
	@Override
	public int compare(Sample arg0, Sample arg1) {
		return arg0.getReceiveDts().compareTo(arg1.getReceiveDts());
	}
}
class SampleBarcodeComparator implements Comparator<Sample> {
	@Override
	public int compare(Sample arg0, Sample arg1) {
		return arg0.getSampleBarcode().get(0).getBarcode().getBarcode().compareToIgnoreCase(arg1.getSampleBarcode().get(0).getBarcode().getBarcode());
	}
}
class SampleLanecountComparator implements Comparator<Sample> {
	SampleService sampleService;
	SampleLanecountComparator(SampleService sampleService){
		this.sampleService = sampleService;
	}
	@Override
	public int compare(Sample arg0, Sample arg1) {
		
		Integer lanecount0 = null;
		Integer lanecount1 = null;
		
		try{lanecount0 = sampleService.getNumberOfIndexedCellsOnPlatformUnit(arg0);
		}catch(Exception e){lanecount0 = new Integer(0);}
		try{lanecount1 = sampleService.getNumberOfIndexedCellsOnPlatformUnit(arg1);
		}catch(Exception e){lanecount1 = new Integer(0);}
		
		return lanecount0.compareTo(lanecount1);
	}
}
class SampleMetaIsStringComparator implements Comparator<Sample> {
	
	String metaKey;
	
	SampleMetaIsStringComparator(String metaKey){
		this.metaKey = new String(metaKey);
	}
	@Override
	public int compare(Sample arg0, Sample arg1) {
		
		String metaValue0 = null;
		String metaValue1 = null;
		
		List<SampleMeta> metaList0 = arg0.getSampleMeta();
		for(SampleMeta sm : metaList0){
			if(sm.getK().indexOf(metaKey) > -1){
				metaValue0 = new String(sm.getV());
				break;
			}
		}		
		
		List<SampleMeta> metaList1 = arg1.getSampleMeta();
		for(SampleMeta sm : metaList1){
			if(sm.getK().indexOf(metaKey) > -1){
				metaValue1 = new String(sm.getV());
				break;
			}
		}
		
		if(metaValue0==null){metaValue0=new String("");} 
		if(metaValue1==null){metaValue1=new String("");}
		
		return metaValue0.compareToIgnoreCase(metaValue1);
	}
}
class SampleMetaIsIntegerComparator implements Comparator<Sample> {
	
	String metaKey;
	
	SampleMetaIsIntegerComparator(String metaKey){
		this.metaKey = new String(metaKey);
	}
	@Override
	public int compare(Sample arg0, Sample arg1) {
		
		String metaValue0 = null;
		String metaValue1 = null;
		
		List<SampleMeta> metaList0 = arg0.getSampleMeta();
		for(SampleMeta sm : metaList0){
			if(sm.getK().indexOf(metaKey) > -1){
				metaValue0 = new String(sm.getV());
				break;
			}
		}		
		
		List<SampleMeta> metaList1 = arg1.getSampleMeta();
		for(SampleMeta sm : metaList1){
			if(sm.getK().indexOf(metaKey) > -1){
				metaValue1 = new String(sm.getV());
				break;
			}
		}
		
		if(metaValue0==null){metaValue0=new String("0");} 
		if(metaValue1==null){metaValue1=new String("0");}
		
		Integer metaIntegerValue0;
		try{
			metaIntegerValue0 = Integer.valueOf(metaValue0);
		}
		catch(NumberFormatException e){
			metaIntegerValue0 = new Integer(0);
		}
		Integer metaIntegerValue1;
		try{
			metaIntegerValue1 = Integer.valueOf(metaValue1);
		}
		catch(NumberFormatException e){
			metaIntegerValue1 = new Integer(0);
		}
		
		return metaIntegerValue0.compareTo(metaIntegerValue1);

	}
}

class SampleMetaIsResourceCategoryNameComparator implements Comparator<Sample> {

	ResourceCategoryDao resourceCategoryDao;
	
	SampleMetaIsResourceCategoryNameComparator(ResourceCategoryDao resourceCategoryDao){
		this.resourceCategoryDao = resourceCategoryDao;
	}

	@Override
	public int compare(Sample arg0, Sample arg1) {
			
		String metaValue0 = null;
		String metaValue1 = null;
		
		List<SampleMeta> metaList0 = arg0.getSampleMeta();
		for(SampleMeta sm : metaList0){
			if(sm.getK().indexOf("resourceCategoryId") > -1){
				metaValue0 = new String(sm.getV());
				break;
			}
		}		
		
		List<SampleMeta> metaList1 = arg1.getSampleMeta();
		for(SampleMeta sm : metaList1){
			if(sm.getK().indexOf("resourceCategoryId") > -1){
				metaValue1 = new String(sm.getV());
				break;
			}
		}
		
		if(metaValue0==null){metaValue0=new String("0");} 
		if(metaValue1==null){metaValue1=new String("0");}
		
		Integer metaIntegerValue0;
		try{
			metaIntegerValue0 = Integer.valueOf(metaValue0);
		}
		catch(NumberFormatException e){
			metaIntegerValue0 = new Integer(0);
		}
		Integer metaIntegerValue1;
		try{
			metaIntegerValue1 = Integer.valueOf(metaValue1);
		}
		catch(NumberFormatException e){
			metaIntegerValue1 = new Integer(0);
		}
				
		ResourceCategory rc0 = this.resourceCategoryDao.getResourceCategoryByResourceCategoryId(metaIntegerValue0);
		ResourceCategory rc1 = this.resourceCategoryDao.getResourceCategoryByResourceCategoryId(metaIntegerValue1);
		
		return (rc0.getName()==null?"":rc0.getName()).compareToIgnoreCase(  (rc1.getName()==null?"":rc1.getName())  );
	}
}