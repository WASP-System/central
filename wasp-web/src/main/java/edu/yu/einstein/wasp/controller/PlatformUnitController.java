package edu.yu.einstein.wasp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;

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
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.BarcodeDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.RunCellDao;
import edu.yu.einstein.wasp.dao.RunMetaDao;
import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeResourceCategoryDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.UserroleDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.ResourceMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleBarcode;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeMeta;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunCell;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/facility/platformunit")
public class PlatformUnitController extends WaspController {

	@Autowired
	private AdaptorsetDao adaptorsetDao;

	@Autowired
	private AdaptorDao adaptorDao;
	
	@Autowired
	private SampleDao sampleDao;

	@Autowired
	private JobDao jobDao;

	@Autowired
	private JobSampleDao jobSampleDao;

	@Autowired
	private JobResourcecategoryDao jobResourcecategoryDao;
	
	@Autowired
	private ResourceCategoryDao resourceCategoryDao;
	
	@Autowired
	private ResourceDao resourceDao;

	@Autowired
	private StateDao stateDao;

	@Autowired
	private SampleMetaDao sampleMetaDao;

	@Autowired
	private SampleSourceDao sampleSourceDao;

	@Autowired
	private SampleSourceMetaDao sampleSourceMetaDao;

	@Autowired
	private StatesampleDao stateSampleDao;
	
	@Autowired
	private SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	private SampleSubtypeResourceCategoryDao sampleSubtypeResourceCategoryDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private SampleTypeDao sampleTypeDao;

	@Autowired
	private ResourceTypeDao resourceTypeDao;
	
	@Autowired
	private SampleSubtypeMetaDao sampleSubtypeMetaDao;


	@Autowired
	private SampleBarcodeDao sampleBarcodeDao;
	
	@Autowired
	private RunDao runDao;
	
	@Autowired
	private RunMetaDao runMetaDao;

	@Autowired
	private RunCellDao runCellDao;
	
	@Autowired
	private BarcodeDao barcodeDao;
	
	@Autowired
	private UserroleDao userroleDao;
	
	@Autowired
	private MessageService messageService;
	  
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private SampleService sampleService;
	
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("platformunit",  "sample",SampleMeta.class, request.getSession());
	}
	
	private final MetaHelperWebapp getMetaHelperWebappPlatformUnitInstance() {
		return new MetaHelperWebapp("platformunitInstance",  "sample",SampleMeta.class, request.getSession());
	}
	
	@RequestMapping(value="/selid/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showSelectedSampleListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunitById");
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		return "facility/platformunit/selid/list";
	}

	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunit");
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));
		
		return "facility/platformunit/list";
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
			sampleList = sampleDao.findByMap(sampleListBaseQueryMap);

		} else {

			sampleListBaseQueryMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleList = this.sampleDao.findByMap(sampleListBaseQueryMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSampleListBaseQueryMap = new HashMap();
				allSampleListBaseQueryMap.put("sampleTypeId", 5);

				List<Sample> allSampleList = sampleDao.findByMap(allSampleListBaseQueryMap);
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
			int selIndex = sampleList.indexOf(this.sampleDao.findById(selId));
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
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));
		
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
		
		class LaneOptions {public Integer laneCount; public String label; public LaneOptions(Integer lc, String l){this.laneCount=lc;this.label=l;}}
		
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
			sampleList = sidx.isEmpty() ? this.sampleDao.findByMap(sampleListBaseQueryMap) : this.sampleDao.findByMapDistinctOrderBy(sampleListBaseQueryMap, null, orderConstraints, sord);
		} else {

			searchParamMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleList = this.sampleDao.findByMap(searchParamMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Sample> allSamples = new ArrayList<Sample>(sidx.isEmpty() ?  this.sampleDao.findByMap(sampleListBaseQueryMap) : this.sampleDao.findByMapDistinctOrderBy(sampleListBaseQueryMap, null, orderConstraints, sord));

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
			int selIndex = sampleList.indexOf(sampleDao.findById(selId));
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
			if(this.sampleDao.getSampleByName(request.getParameter("name")).getName() != null) {
				
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
		Sample sample = sampleDao.getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_ro";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updatePlatformUnitForm(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleDao.getSampleBySampleId(sampleId);

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
			Sample sampleDb =	sampleDao.getSampleBySampleId(sampleId);
			
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
				 sampleDb = this.sampleDao.save(cell);
				 
				 SampleSource sampleSource = new SampleSource();
				 sampleSource.setSampleId(sampleForm.getSampleId());
				 sampleSource.setSourceSampleId(sampleDb.getSampleId());
				 sampleSource.setMultiplexindex(i+1);
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
				Sample cell = this.sampleDao.findById(tr.getSourceSampleId());
				String cellName = cell.getName();
				String subString = cellName.substring(cellName.lastIndexOf("/"), cellName.length());
				cell.setName(sampleForm.getName().concat(subString));
				this.sampleDao.merge(cell);
								
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
			sampleDb = sampleDao.save(sampleForm);
		} else {
			sampleDb = sampleDao.merge(sampleForm);
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
			
			sampleDb = sampleDao.save(sampleForm);
			
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
			sampleDb = sampleDao.merge(sampleForm);
			
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
		createState(sampleId, sampleDb);
		
		return "redirect:/facility/platformunit/ok";
	}
	
	/**
	 * Inserts a record in State table and Sets state name to  "Platform Unit" and state status to "CREATED" 
	 * Also inserts a new record in Samplestate table
	 * 
	 * @param sampleDb
	 */
	public void createState(Integer sampleId, Sample sampleDb) {
		
		Map<String, String> taskQueryMap = new HashMap<String, String>();
		taskQueryMap.put("iName", "assignLibraryToPlatformUnit");
		List <Task> task = new ArrayList <Task> (this.taskDao.findByMap(taskQueryMap));

		if (sampleId == null || sampleId.intValue() == 0) {

			State state = new State();
			state.setTaskId(task.get(0).getTaskId());
			state.setName(task.get(0).getName());
			state.setStatus("CREATED");
			state.setLastUpdTs(new Date());
			State stateDb = this.stateDao.save(state);
			
			Statesample stateSample = new Statesample();
			stateSample.setSampleId(sampleDb.getSampleId());
			stateSample.setStateId(stateDb.getStateId());
			this.stateSampleDao.save(stateSample);
		}

	}

	/**
	 * limitPriorToAssignment
	 */
	@RequestMapping(value="/limitPriorToAssign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String limitPriorToAssignmentForm(@RequestParam("resourceCategoryId") Integer resourceCategoryId,
			ModelMap m) {
		
		ResourceType resourceType = resourceTypeDao.getResourceTypeByIName("mps");
		if(resourceType == null || resourceType.getResourceTypeId()==null || resourceType.getResourceTypeId().intValue()==0){
			waspErrorMessage("platformunit.resourceTypeNotFound.error");
			return "redirect:/dashboard.do"; 
		}
		Map filterForResourceCategory = new HashMap();
		filterForResourceCategory.put("resourceTypeId", resourceType.getResourceTypeId());
		List<ResourceCategory> resourceCategories = resourceCategoryDao.findByMap(filterForResourceCategory);
		
		m.put("resourceCategoryId", resourceCategoryId);
		m.put("resourceCategories", resourceCategories);
//perhaps a better way to screen is to find all jobs that are not completed, and show those that have at least one library
		if(resourceCategoryId.intValue() > 0){
			//get list of jobs with following: 
			//	1. select states from state where task 106 (now it's 5) Assign Library To Platform Unit and status CREATED
			//	2. select those states from statejob to get those jobs
			//	3. filter jobs to include only those requesting specific sequencing machine (resourceCategoryId) 
/**************		
			Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
			if(task==null || task.getTaskId()==null || task.getTaskId().intValue()==0){
				waspErrorMessage("platformunit.taskNotFound.error");
				return "redirect:/dashboard.do"; 
			}
			Map filterForState = new HashMap();
			filterForState.put("taskId", task.getTaskId());
			filterForState.put("status", "CREATED");
			List<State> states = stateDao.findByMap(filterForState);
			List<Job> jobList = new ArrayList();
			for(State state : states){
				if(state.getStatejob().isEmpty()){
					continue;
				}
				Job job = state.getStatejob().get(0).getJob();//should be one
				JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategoryId, job.getJobId());
				if(jrc!=null && jrc.getJobResourcecategoryId()!=null && jrc.getJobResourcecategoryId().intValue() != 0){
					jobList.add(state.getStatejob().get(0).getJob());
				}
				
			}
		
			//if(jobList.size()==0){
			//	int v = 10216;
			//	jobList.add(jobDao.getJobByJobId(v));
			//}
*****************/
			// 4/16/12 I (Dubin) think it's actually better to be able to add a job's libraries to flow cells at any point until the job is closed
			// so for redo the task test
			Task task = taskDao.getTaskByIName("Start Job");
			if(task==null || task.getTaskId()==null || task.getTaskId().intValue()==0){
				waspErrorMessage("platformunit.taskNotFound.error");
				return "redirect:/dashboard.do"; 
			}
			Map filterForState = new HashMap();
			filterForState.put("taskId", task.getTaskId());
			filterForState.put("status", "CREATED");
			List<State> states = stateDao.findByMap(filterForState);
			Set<Job> jobSet = new HashSet();
			List<Job> jobList = new ArrayList<Job>();
			for(State state : states){
				if(state.getStatejob().isEmpty()){
					continue;
				}
				Job job = state.getStatejob().get(0).getJob();//should be one
				JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategoryId, job.getJobId());
				if(jrc!=null && jrc.getJobResourcecategoryId()!=null && jrc.getJobResourcecategoryId().intValue() != 0){
					jobSet.add(state.getStatejob().get(0).getJob());
				}				
			}
			Iterator<Job> it = jobSet.iterator();
			 
			while(it.hasNext()){
				jobList.add((Job)it.next());
			}
			Collections.sort(jobList, new JobComparator());
			
			m.put("jobList", jobList);
		}
		
		return "facility/platformunit/limitPriorToAssign"; 
	}
	
	/**
	 * limitPriorToPlatformUnitAssignment
	 */
	@RequestMapping(value="/limitPriorToPlatUnitAssign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String limitPriorToPlatUnitAssignForm(ModelMap m) {
		
		ResourceType resourceType = resourceTypeDao.getResourceTypeByIName("mps");
		//if(resourceType.getResourceTypeId()==0){
			//waspErrorMessage("platformunit.resourceCategoryNotFound.error");
			//return "redirect:/dashboard.do";
		//}
		Map filterForResourceCategory = new HashMap();
		filterForResourceCategory.put("resourceTypeId", resourceType.getResourceTypeId());
		List<ResourceCategory> resourceCategories = resourceCategoryDao.findByMap(filterForResourceCategory);
		
		m.put("resourceCategories", resourceCategories);
		return "facility/platformunit/limitPriorToPlatUnitAssign"; 
	}
	
	
	
  /**
   * assignmentForm
   *
   */
	@RequestMapping(value="/assign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String assignmentForm(@RequestParam("resourceCategoryId") Integer resourceCategoryId,
			@RequestParam("jobsToWorkWith") Integer jobsToWorkWith,//this will be a single jobId or it will be 0 [error] or -1 [indicating find all jobs that are not complete and have libraries to go onto flow cells]
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
				
		// pickup FlowCells limited by states and filter to get only those compatible with the selected machine resourceCategoryId
		Map stateMap = new HashMap(); 

		Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");

		if(task == null || task.getTaskId() == null){
			waspErrorMessage("platformunit.taskNotFound.error");
			return "redirect:/dashboard.do";
		}
		stateMap.put("taskId", task.getTaskId()); 	
		stateMap.put("status", "CREATED"); 
		List<State> temp_platformUnitStates = stateDao.findByMap(stateMap);
		List<State> platformUnitStates = new ArrayList<State>();//for the filtered states (but would really just need a filtered list of flow cells)
		
		List<Sample> flowCells = new ArrayList<Sample>();
		
		Map stsrcMap = new HashMap();//get the ids for the types of flow cells that go on the selected machine
		stsrcMap.put("resourcecategoryId", resourceCategory.getResourceCategoryId()); 
		List<SampleSubtypeResourceCategory> stsrcList = sampleSubtypeResourceCategoryDao.findByMap(stsrcMap);
		for(State s : temp_platformUnitStates){
			List<Statesample> ssList = s.getStatesample();
			for(Statesample ss : ssList){
				for(SampleSubtypeResourceCategory stsrc : stsrcList){
					if(stsrc.getSampleSubtypeId().intValue() == ss.getSample().getSampleSubtypeId().intValue()){
						platformUnitStates.add(s);//consider really just taking the flowcell sample, which is done next line
						flowCells.add(ss.getSample());
					}
				}
			}
		}

		// picks up jobs
		
		List<Job> jobs = new ArrayList<Job>();
		
		Task task2 = taskDao.getTaskByIName("Start Job");
		if(task2==null || task2.getTaskId()==null || task2.getTaskId().intValue()==0){
			waspErrorMessage("platformunit.taskNotFound.error");
			return "redirect:/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=" + resourceCategoryId;
		}
		Map filterForState = new HashMap();
		filterForState.put("taskId", task2.getTaskId());
		filterForState.put("status", "CREATED");
		List<State> states = stateDao.findByMap(filterForState);
		
		if(jobsToWorkWith.intValue() > 0){//get the single job selected from the dropdown box; so parameter jobsToWorkWith has a value > 0, representing a single jobId; confirm it meets state and resourceCategory criteria
			Job job = jobDao.getJobByJobId(jobsToWorkWith);
			if(job==null || job.getJobId()==null || job.getJobId().intValue()==0){
				waspErrorMessage("platformunit.jobNotFound.error");
				return "redirect:/facility/platformunit/limitPriorToAssign.do?resourceCategoryId=" + resourceCategoryId;
			}
			for(State state : states){
				if(state.getStatejob().isEmpty()){
					continue;
				}
				Job job2 = state.getStatejob().get(0).getJob();//should be one
				if(job2.getJobId().intValue()==job.getJobId().intValue()){
					JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategoryId, job2.getJobId());
					if(jrc!=null && jrc.getJobResourcecategoryId()!=null && jrc.getJobResourcecategoryId().intValue() != 0){
						jobs.add(state.getStatejob().get(0).getJob());
					}
				}
			}
		}
		else if(jobsToWorkWith.intValue()==-1){//asking for list of all available jobs that meet the resourceCategoryId and state criteria; so parameter jobsToWorkWith has a value > -1 which means it's asking for all available jobs that meet state and resourceCategory criteria

			for(State state : states){
				if(state.getStatejob().isEmpty()){
					continue;
				}
				Job job = state.getStatejob().get(0).getJob();//should be one
				JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategoryId, job.getJobId());
				if(jrc!=null && jrc.getJobResourcecategoryId()!=null && jrc.getJobResourcecategoryId().intValue() != 0){
					jobs.add(state.getStatejob().get(0).getJob());
				}
			}
		}
	
		//map of adaptors for display; this really needs to be a part of the library sample
		Map adaptors = new HashMap();
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
				
		m.put("jobsToWorkWith", jobsToWorkWith);
		m.put("machineName", resourceCategory.getName());
		m.put("resourceCategoryId", resourceCategoryId);
		m.put("jobs", jobs); 
		m.put("platformUnitStates", platformUnitStates); 
		m.put("hello", "hello world"); 
		m.put("adaptors", adaptors);
		m.put("flowCells", flowCells);
		
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

		Job job = jobDao.getJobByJobId(jobId);
		Sample laneSample = sampleDao.getSampleBySampleId(laneSampleId); 
		Sample librarySample = sampleDao.getSampleBySampleId(librarySampleId); 
		JobSample jobSample = jobSampleDao.getJobSampleByJobIdSampleId(jobId, librarySampleId);//confirm library is really part of this jobId

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
		else if ( ! librarySample.getSampleType().getIName().equals("library")) {
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
			Float libConcInLanePicoMFloat;
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
						
		// ensure flowcell in the "CREATED" state 
		
		boolean flowCellIsAvailable = false;
		List<SampleSource> parentSampleSources = laneSample.getSampleSourceViaSourceSampleId();//should be one
		if(parentSampleSources == null || parentSampleSources.size()!=1){
			error=true; waspErrorMessage("platformunit.flowcellNotFoundNotUnique.error");
		}
		else{
			Sample flowCell = parentSampleSources.get(0).getSample();
			if( ! "platformunit".equals(flowCell.getSampleType().getIName()) ){
				error=true; waspErrorMessage("platformunit.flowcellNotFoundNotUnique.error");
			}
			else{
				Map stateSampleMap = new HashMap(); 
				stateSampleMap.put("sampleId", flowCell.getSampleId());
				List<Statesample> stateSampleList = stateSampleDao.findByMap(stateSampleMap);
				for(Statesample stateSample : stateSampleList){
					if(stateSample.getState().getTask().getIName().equals("assignLibraryToPlatformUnit")){
						flowCellIsAvailable=true;
						break;
					}
				}
				if(!flowCellIsAvailable){
					error=true; waspErrorMessage("platformunit.flowcellStateError.error");
				}
			}
		}
		if(error){
			return;
		}
	
		//(1) identify the barcode sequence on the library being added. If problem then terminate. 
		//(2) if the library being added has a barcode that is NONE, and the lane contains ANY OTHER LIBRARY, then terminate. 
		//(3) identify barcode of libraries already on lane; if problem, terminate. Should also get their jobIds.
		//(4) if the lane already has a library with a barcode of NONE, then terminate
		//(5) if the library being added has a bardcode that is something other than NONE (meaning a real barcode sequence) AND if a library already on the lane has that same barcode, then terminate. 
		//(6) do we want to maintain only a single jobId for a lane???
		
		//case 1: identify the adaptor barcode for the library being added; it's barcode is either NONE (no multiplexing) or has some more interesting barcode sequence (for multiplexing, such as AACTG)
		Adaptor adaptorOnLibraryBeingAdded = null;

		SampleMeta sampleMeta = sampleMetaDao.getSampleMetaByKSampleId("genericLibrary.adaptor", librarySampleId);

		if(sampleMeta==null || sampleMeta.getSampleMetaId()==null){
			error=true; waspErrorMessage("platformunit.adaptorNotFound.error");
		}
		else{
			try{
				adaptorOnLibraryBeingAdded = adaptorDao.getAdaptorByAdaptorId(new Integer(sampleMeta.getV()));
				if(adaptorOnLibraryBeingAdded==null || adaptorOnLibraryBeingAdded.getAdaptorId()==null){
					error=true; waspErrorMessage("platformunit.adaptorNotFound.error");
				}
				else if( adaptorOnLibraryBeingAdded.getBarcodesequence()==null || "".equals(adaptorOnLibraryBeingAdded.getBarcodesequence()) ){
					error=true; waspErrorMessage("platformunit.adaptorBarcodeNotFound.error");
				}
			}
			catch(Exception e){
				error=true; waspErrorMessage("platformunit.adaptorNotFound.error");
			}
		}
		if(error){
			return;
		}
		
		int maxIndex = 0; 
		List<SampleSource> siblingSampleSource = laneSample.getSampleSource(); //Samplesource objects that have this lane's sampleId as samplesource.sampleId
		String barcodeOnLibBeingAdded = new String(adaptorOnLibraryBeingAdded.getBarcodesequence());

		//case 2: dispense with this easy check 
		if( "NONE".equals(barcodeOnLibBeingAdded) && siblingSampleSource != null && siblingSampleSource.size() > 0  ){//case 2: the library being added has a barcode of "NONE" AND the lane to which user wants to add this library already contains one or more libraries (such action is prohibited)
			waspErrorMessage("platformunit.libNoneLaneOthers.error");
			return;
		}
		
		//cases 3, 4, 5, 6 
		if (siblingSampleSource != null) {//siblingSampleSource is list of samplesource objects that harbor libraries on this cell (lane) through source_sampleid
		
			for (SampleSource ss: siblingSampleSource) {
				
				if (ss.getMultiplexindex().intValue() > maxIndex) {//housekeeping; will be needed for the INSERT into samplesource (at end of method)
					maxIndex = ss.getMultiplexindex().intValue(); 
				}
				
				Sample libraryAlreadyOnLane = ss.getSampleViaSource();//this is a library already on the selected lane
				if( ! libraryAlreadyOnLane.getSampleType().getIName().equals("library") ){//confirm it's a library
					error=true; waspErrorMessage("platformunit.libOnLaneNotLib.error");
				}
				else{					
					SampleMeta sampleMeta2 = sampleMetaDao.getSampleMetaByKSampleId("genericLibrary.adaptor", libraryAlreadyOnLane.getSampleId());
					if(sampleMeta2==null || sampleMeta2.getSampleMetaId()==null){
						error=true; waspErrorMessage("platformunit.adaptorOnLaneNotFound.error");
					}
					else{
						try{
							Adaptor adaptorOnLane = adaptorDao.getAdaptorByAdaptorId(new Integer(sampleMeta2.getV()));
							if(adaptorOnLane==null || adaptorOnLane.getAdaptorId()==null){
								error=true; waspErrorMessage("platformunit.adaptorOnLaneNotFound.error");
							}
							else if( adaptorOnLane.getBarcodesequence()==null || "".equals(adaptorOnLane.getBarcodesequence()) ){
								error=true; waspErrorMessage("platformunit.adaptorBarcodeOnLaneNotFound.error");
							}
							else if( "NONE".equals(adaptorOnLane.getBarcodesequence()) ){//case 4
								error=true; waspErrorMessage("platformunit.libWithNoneOnLane.error");
							}
							else if(adaptorOnLane.getBarcodesequence().equals(barcodeOnLibBeingAdded)){//case 5, lib already on lane with same barcode as library user wants to add to the lane
								error=true;  waspErrorMessage("platformunit.barcodeAlreadyOnLane.error");
							}
							else{//case 6
								JobSample jobSample2 = jobSampleDao.getJobSampleByJobIdSampleId(jobId, libraryAlreadyOnLane.getSampleId());//confirm library is really part of this jobId
								if(jobSample2 == null || jobSample2.getJobSampleId()==null){//this library, already on the lane, is from a different job
									;//for now do nothing
									//If Einstein, then terminate (lane restricted to libraries from single job) 
									//error=true;  waspErrorMessage("platformunit.libJobClash.error");
									//if SloanKettering, then do nothing (what's the flag)
								}
							}
						}
						catch(Exception e){
							error=true; waspErrorMessage("platformunit.adaptorOnLaneNotFound.error");//exception from new Integer();
						}
					}					
				}
				if(error){
					return;
				}	
			}	
		}
		
		SampleSource newSampleSource = new SampleSource(); 
		newSampleSource.setSampleId(laneSampleId);
		newSampleSource.setSourceSampleId(librarySampleId);
		newSampleSource.setMultiplexindex(new Integer(maxIndex + 1));
		newSampleSource = sampleSourceDao.save(newSampleSource);//capture the new samplesourceid
		SampleSourceMeta newSampleSourceMeta = new SampleSourceMeta();
		newSampleSourceMeta.setSampleSourceId(newSampleSource.getSampleSourceId());
		newSampleSourceMeta.setK("libConcInLanePicoM");
		newSampleSourceMeta.setV(libConcInLanePicoM.toString());
		newSampleSourceMeta.setPosition(new Integer(0));
		sampleSourceMetaDao.save(newSampleSourceMeta);
		
		SampleSourceMeta newSampleSourceMeta2 = new SampleSourceMeta();
		newSampleSourceMeta2.setSampleSourceId(newSampleSource.getSampleSourceId());
		newSampleSourceMeta2.setK("jobId");//Ed says we donot need this here
		newSampleSourceMeta2.setV(jobId.toString());
		newSampleSourceMeta2.setPosition(new Integer(1));
		sampleSourceMetaDao.save(newSampleSourceMeta2);
		
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

		removeLibraryFromLane(sampleSourceId);
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
			//TODO message stating parameter problem
			return "redirect:/dashboard.do";
		}
		else if(jobId != null && platformUnitId != null){//don't know what to do
			//TODO message stating parameter problem
			return "redirect:/dashboard.do";
		}
		removeLibraryFromLane(sampleSourceId);
		if(jobId != null){
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		else if(platformUnitId != null){
			return "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId + ".do";
		}
		return "redirect:/dashboard.do";//it really wants to see some return statement outside the if clauses
	  }
	
	private void removeLibraryFromLane(Integer sampleSourceId){

		SampleSource sampleSource = sampleSourceDao.getSampleSourceBySampleSourceId(sampleSourceId);//this samplesource should represent a cell->lib link, where sampleid is the cell and source-sampleid is the library 
		if(sampleSource.getSampleSourceId()==0){//check for existence
			waspErrorMessage("platformunit.sampleSourceNotExist.error");
			return;
		}
		//check that this represents a cell->lib link
		Sample putativeLibrary = sampleSource.getSampleViaSource();
		Sample putativeCell = sampleSource.getSample();
		if( ! putativeLibrary.getSampleType().getIName().equals("library") || ! putativeCell.getSampleType().getIName().equals("cell") ){
			waspErrorMessage("platformunit.samplesourceTypeError.error");
			return; 
		}
		//delete the metadata 
		List<SampleSourceMeta> sampleSourceMetaList = sampleSourceMetaDao.getSampleSourceMetaBySampleSourceId(sampleSource.getSampleSourceId());
		for(SampleSourceMeta ssm : sampleSourceMetaList){
			sampleSourceMetaDao.remove(ssm);
			sampleSourceMetaDao.flush(ssm);
		}
		sampleSourceDao.remove(sampleSource);
		sampleSourceDao.flush(sampleSource);

		waspErrorMessage("platformunit.libraryRemoved.success");
		return;  
	}
	
	@RequestMapping(value = "/showPlatformUnit/{sampleId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showPlatformUnit(@PathVariable("sampleId") Integer sampleId, ModelMap m){
		
		Sample platformUnit = sampleDao.getSampleBySampleId(sampleId.intValue());
		if(platformUnit.getSampleId().intValue()==0){
			//TODO return where you came from (wherever that was; in this case it was listJobSamples and needs a jobId, whcih I don't have)			
		}
		//confirm this sample is typesampleid of 5 (platformunit); highly unlikely that it is not so
		if( ! "platformunit".equals(platformUnit.getSampleType().getIName()) ){
			waspErrorMessage("platformunit.flowcellNotFoundNotUnique.error");
			return "redirect:/dashboard.do";
		}
		
		//is this flowcell on a run?
		List<Run> runList = platformUnit.getRun();
		m.put("runList", runList);
		//if this flowcell is on a run, it gets locked to the addition of new user-libraries
		//locking the flowcell is recorded as its task, Assign Library To Platform Unit, changing from CREATED to COMPLETED
		//There are conditions under which the flow cell may need to be unlocked. Currently this will be 
		//visible on a the platform unit form only to superuser, who may unlock and later relock the flow cell.
		//Here, just determine if it's locked
		String platformUnitStatus = "UNKNOWN";
		List<Statesample> stateSampleList = platformUnit.getStatesample();
		Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
		if(task==null || task.getTaskId().intValue()==0){
			//TODO we have a problem; don't know how to deal with this
			//however we'll simply leave platformUnitIsLocked as "UNKNOWN"
		}
		for(Statesample stateSample : stateSampleList){
			if(stateSample.getState().getTaskId().intValue() == task.getTaskId().intValue()){
				platformUnitStatus = stateSample.getState().getStatus().toUpperCase();
			}
		}
		m.put("platformUnitStatus", platformUnitStatus);
		
		StringBuffer readType = new StringBuffer("<option value=''>---SELECT A READ TYPE---</option>");
		StringBuffer readLength = new StringBuffer("<option value=''>---SELECT A READ LENGTH---</option>");
		
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
		
		if(runList.size() > 0){//a run for this platform unit exists
			Run run = runList.get(0);//should only be one, I hope
			String currentReadLength = "";
			String currentReadType = "";
			List<RunMeta> runMetaList = run.getRunMeta();
			for(RunMeta rm : runMetaList){
				if(rm.getK().indexOf("readlength") > -1){
					currentReadLength = new String(rm.getV());
				}
				if(rm.getK().indexOf("readType") > -1){
					currentReadType = new String(rm.getV());
				}
			}
			Resource resource = run.getResource();
			ResourceCategory resourceCategory = resource.getResourceCategory();
			List<ResourceCategoryMeta> resourceCategoryMetaList = resourceCategory.getResourceCategoryMeta();
			for(ResourceCategoryMeta rcm : resourceCategoryMetaList){
				if( rcm.getK().indexOf("readType") > -1 ){
					String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
					for(String token : tokens){//token could be single:single
						String [] innerTokens = token.split(":");
						if(currentReadType.equalsIgnoreCase(innerTokens[0])){
							readType.append("<option SELECTED value='"+innerTokens[0]+"'>"+innerTokens[1]+"</option>");
						}
						else{
							readType.append("<option value='"+innerTokens[0]+"'>"+innerTokens[1]+"</option>");
						}
					}
				}
				if( rcm.getK().indexOf("readlength") > -1 ){
					String[] tokens = rcm.getV().split(";");//rcm.getV() will be 50:50;100:100
					for(String token : tokens){//token could be 50:50
						String [] innerTokens = token.split(":");
						if(currentReadLength.equalsIgnoreCase(innerTokens[0])){
							readLength.append("<option SELECTED value='"+innerTokens[0]+"'>"+innerTokens[1]+"</option>");
						}
						else{
							readLength.append("<option value='"+innerTokens[0]+"'>"+innerTokens[1]+"</option>");
						}
					}
				}
			}
			m.put("readType", new String(readType));
			m.put("readLength", new String(readLength));
		}
		
		List<Resource> resourceList= resourceDao.findAll(); 
		List<Resource> filteredResourceList = new ArrayList();
		for(Resource resource : resourceList){
			System.out.println("resource: " + resource.getName());
			for(SampleSubtypeResourceCategory ssrc : resource.getResourceCategory().getSampleSubtypeResourceCategory()){
				if(ssrc.getSampleSubtypeId().intValue() == platformUnit.getSampleSubtypeId().intValue()){
					filteredResourceList.add(resource);
					System.out.println("it's a match");					
				}
			}
		}
		m.put("resources", filteredResourceList);
		
		List<Adaptor> allAdaptors = adaptorDao.findAll();
		Map adaptorList = new HashMap();
		for(Adaptor adaptor : allAdaptors){
			adaptorList.put(adaptor.getAdaptorId().toString(), adaptor);
		}
		m.addAttribute("adaptors", adaptorList);
		
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName("controlLibrarySample");
		if(sampleSubtype.getSampleSubtypeId().intValue()==0){
			//TODO throw error and get out of here ; probably go to dashboard, but would be best to go back from where you came from
			System.out.println("Unable to find sampleSubtype of controlLibrarySample");
		}
		
		Map controlFilterMap = new HashMap();
		controlFilterMap.put("sampleSubtypeId", sampleSubtype.getSampleSubtypeId());
		List<Sample> controlLibraryList = sampleDao.findByMap(controlFilterMap);
		for(Sample sample : controlLibraryList){
			System.out.println("control: " + sample.getName());
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
		
		//System.out.println("ID: " + platformUnitId.intValue());
		//System.out.println("lock: " + lock);
		String ret_value = "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId.intValue() + ".do";
		
		if( ! lock.equals("CREATED") && ! lock.equals("COMPLETED")){
			//parameter locked invalid
			return ret_value;
		}
		
		Sample platformUnit = sampleDao.getSampleBySampleId(platformUnitId);
		if(platformUnit == null || platformUnit.getSampleId().intValue()==0){
			//message that platform unit not found
			return ret_value;
		}
		if(! platformUnit.getSampleType().getIName().equals("platformunit")){
			//message that sample is not a platform unit 
			return ret_value;
		}
		
		Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
		if(task==null || task.getTaskId().intValue()==0){
			//TODO we have a problem; 
			//task not found
			return ret_value;
		}
		
		boolean stateFoundAndUpdated = false;
		List<Statesample> stateSampleList = platformUnit.getStatesample();
		for(Statesample stateSample : stateSampleList){
			if(stateSample.getState().getTaskId().intValue() == task.getTaskId().intValue()){
				State state = stateSample.getState();
				state.setStatus(lock);
				stateDao.save(state);
				stateFoundAndUpdated = true;
			}
		}
		if(stateFoundAndUpdated==false){
			//message unalbe to locate/save new state
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
		Sample cell = sampleDao.getSampleBySampleId(cellId);
		List<SampleSource> sampleSourceList = cell.getSampleSource();
		int maxMultipleIndex = 0;
		for(SampleSource ss : sampleSourceList){
			if(ss.getMultiplexindex().intValue() > maxMultipleIndex){
				maxMultipleIndex = ss.getMultiplexindex().intValue();
			}
		}
		sampleSource.setMultiplexindex(new Integer(maxMultipleIndex + 1));
		sampleSource = sampleSourceDao.save(sampleSource);
		//System.out.println("checking next multiplexIndex; new one is " + sampleSource.getMultiplexindex().intValue());
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
		//System.out.println("in ajaxReadType and resourceId = " + resourceId);
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
		//System.out.println("The return string = " + returnString);
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
		Sample platformUnit = sampleDao.getSampleBySampleId(platformUnitId);
		if(platformUnit.getSampleId().intValue()==0){
			//message unable to find platform unit record
			System.out.println("unable to find platform unit record");
			return "redirect:/dashboard.do";
		}
		//confirm flowcell (platformUnit)
		if( !platformUnit.getSampleType().getIName().equals("platformunit") ){
			//message - not a flow cell
			System.out.println("PlatformUnit not a flow cell");
			return return_string;
		}
		//record for machine exists
		Resource machineInstance = resourceDao.getResourceByResourceId(resourceId);
		if(machineInstance.getResourceId().intValue() == 0){
			//message: unable to find record for requested machine
			System.out.println("unable to find record for requested sequencing machine");
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
			System.out.println("Platform Unit (flowcell) and Resource (sequencing machine) are Not compatible");
			return return_string;
		}
		Integer laneCountFromMeta = null;
		for(SampleMeta sm : platformUnit.getSampleMeta()){
			if(sm.getK().indexOf("lanecount") > -1){
				try{
					laneCountFromMeta = new Integer(sm.getV());
				}
				catch(Exception e){
					System.out.println("Unable to capture platformUnit lanecount from sampleMetaData");
					return return_string;
				}
			}
		}
		if(laneCountFromMeta == null){
			System.out.println("Unable to capture platformUnit lanecount from sampleMetaData");
			return return_string;
		}
		if(laneCountFromMeta.intValue() != platformUnit.getSampleSource().size()){
			System.out.println("lanecount from sampleMetaData and from samplesource are discordant: unable to continue");
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
			System.out.println("Readtype incompatible with selected machine: unable to continue");
			return return_string;			
		}
		if(readLengthIsValid == false){
			System.out.println("Readlength incompatible with selected machine: unable to continue");
			return return_string;			
		}		
		if(runName.trim() == ""){
			System.out.println("Run name is Not valid");
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
			System.out.println("Selected Technical Personnel is NOT listed as technician or manager");
			return return_string;				
		}
		
		Date dateStart;
		try{
			DateFormat formatter;
			formatter = new SimpleDateFormat("MM/dd/yyyy");
			dateStart = (Date)formatter.parse(runStartDate);  
		}
		catch(Exception e){
			System.out.println("Start Date format must be MM/dd/yyyy.");
			return return_string;	
		}
		
		//new run
		if(runId==null){//new run
			Run newRun = new Run();
			newRun.setResource(machineInstance);
			newRun.setName(runName.trim());
			newRun.setSample(platformUnit);//set the flow cell
			newRun.setUserId(technicianId);
			newRun.setStartts(dateStart);
			newRun = runDao.save(newRun);
			System.out.println("-----");
			System.out.println("saved new run runid=" + newRun.getRunId().intValue());
			//runmeta : readlength and readType
			RunMeta newRunMeta = new RunMeta();
			newRunMeta.setRun(newRun);
			newRunMeta.setK("run.readlength");
			newRunMeta.setV(readLength);
			newRunMeta.setPosition(new Integer(0));//do we really use this???
			newRunMeta = runMetaDao.save(newRunMeta);
			System.out.println("saved new run Meta for readLength id=" + newRunMeta.getRunMetaId().intValue());
			newRunMeta = new RunMeta();
			newRunMeta.setRun(newRun);
			newRunMeta.setK("run.readType");
			newRunMeta.setV(readType);
			newRunMeta.setPosition(new Integer(0));//do we really use this???
			newRunMeta = runMetaDao.save(newRunMeta);
			System.out.println("saved new run Meta for readType runmetaid=" + newRunMeta.getRunMetaId().intValue());	
			//runlane
			for(SampleSource ss : platformUnit.getSampleSource()){
				Sample cell = ss.getSampleViaSource();
				System.out.println("cellid = " + cell.getSampleId().intValue());
				RunCell runCell = new RunCell();
				runCell.setRun(newRun);//the runid
				runCell.setSample(cell);//the cellid
				runCell = runCellDao.save(runCell);
				System.out.println("saved new run cell runcellid=" + runCell.getRunCellId().intValue());
			}
			System.out.println("-----");
			//4/16/12
			//With the creation of this new sequence run record, we want to make it that the flow cell on this
			//sequence run is no longer able to accept additional User libraries. To do this, set this platform 
			//unit's task of "Assign Library To Platform Unit" to something like "COMPLETED"
			//with this, the platform unit associated with this sequence run will not appear on the "Add Library To Flow Cell" drop-down list of available flow cells.
			Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
			//well if we do this, then there will be no rollback, so just let it throw its exception if this task is not found.
			//if(task==null || task.getTaskId()==null || task.getTaskId().intValue()==0){
				//TODO fix this error message
			//	waspErrorMessage("platformunit.taskNotFound.error");
				//return return_string;
			//}
			Map taskFilterMap = new HashMap();
			taskFilterMap.put("taskId", task.getTaskId());
			taskFilterMap.put("status", "CREATED");
			List<State> stateList = stateDao.findByMap(taskFilterMap);
			for(State state : stateList){
				List <Statesample> statesampleList = state.getStatesample();
				for(Statesample statesample : statesampleList){
					if(statesample.getSampleId().intValue() == platformUnit.getSampleId().intValue()){
						state.setStatus("COMPLETED");
						stateDao.save(state);
						System.out.println("StateId " + state.getStateId().intValue() + " is now set to Completed");
					}
				}
			}
		}
		else if(runId != null){//update run
			Run run = runDao.getRunByRunId(runId);
			if(run.getRunId().intValue()==0){
				//unable to locate run record; 
				System.out.println("Update Failed: Unable to locate Sequence Run record");
				return return_string;
			}
			//confirm that the platformUnit on this run is actually the same platformUnit passed in via parameter platformUnitId
			List<Run> runList = platformUnit.getRun();
			if(runList.size() == 0){
				//platformUnit referenced through parameter platformUnitId is NOT on any sequence run
				System.out.println("Update Failed: Platform Unit " + platformUnit.getSampleId() + " is not on any sequence run");
				return return_string;
			}
			if(runList.get(0).getRunId().intValue() != run.getRunId().intValue()){
				//platformUnit referenced through parameter platformUnitId is NOT on part of This sequence run
				System.out.println("Update Failed: Platform Unit " + platformUnit.getSampleId() + " is not part of this sequence run");
				return return_string;
			}
			run.setResource(machineInstance);
			run.setName(runName.trim());
			run.setSample(platformUnit);
			run.setUserId(technicianId);
			run.setStartts(dateStart);

			run = runDao.save(run);//since this object was pulled from the database, it is persistent, and any alterations are automatically updated; thus this line is superfluous
			
			List<RunMeta> runMetaList = run.getRunMeta();
			boolean readLengthIsSet = false;
			boolean readTypeIsSet = false;
			for(RunMeta rm : runMetaList){
				if(rm.getK().indexOf("readlength") > -1){
					rm.setV(readLength);
					runMetaDao.save(rm);//probably superfluous
					readLengthIsSet = true;
				}
				if(rm.getK().indexOf("readType") > -1){
					rm.setV(readType);
					runMetaDao.save(rm);//probably superfluous
					readTypeIsSet = true;
				}
			}
			if(readLengthIsSet == false){
				RunMeta newRunMeta = new RunMeta();
				newRunMeta.setRun(run);
				newRunMeta.setK("run.readlength");
				newRunMeta.setV(readLength);
				newRunMeta.setPosition(new Integer(0));//do we really use this???
				newRunMeta = runMetaDao.save(newRunMeta);
			}
			if(readTypeIsSet == false){
				RunMeta newRunMeta = new RunMeta();
				newRunMeta.setRun(run);
				newRunMeta.setK("run.readType");
				newRunMeta.setV(readType);
				newRunMeta.setPosition(new Integer(0));//do we really use this???
				newRunMeta = runMetaDao.save(newRunMeta);
			}
			
			System.out.println("Sequence run has been updated now: runStDate = " + runStartDate);
		}
		
		//need message to confirm update completed sucessfully
		//return "redirect:/dashboard.do";
		return "redirect:/facility/platformunit/showPlatformUnit/" + platformUnitId.intValue() + ".do";
	}
}

class JobComparator implements Comparator<Job> {
    @Override
    public int compare(Job arg0, Job arg1) {
    	return arg0.getJobId().compareTo(arg1.getJobId());
    }
}