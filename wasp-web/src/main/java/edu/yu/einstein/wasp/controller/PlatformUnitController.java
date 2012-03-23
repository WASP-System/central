package edu.yu.einstein.wasp.controller;

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
import edu.yu.einstein.wasp.dao.SampleBarcodeDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.SampleSourceMetaDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatesampleDao;
import edu.yu.einstein.wasp.dao.SubtypeSampleDao;
import edu.yu.einstein.wasp.dao.SubtypeSampleMetaDao;
import edu.yu.einstein.wasp.dao.SubtypeSampleResourceCategoryDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.TypeResourceDao;
import edu.yu.einstein.wasp.dao.TypeSampleDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Barcode;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
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
import edu.yu.einstein.wasp.model.TypeResource;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;
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
	private SubtypeSampleDao subtypeSampleDao;

	@Autowired
	private SubtypeSampleResourceCategoryDao subtypeSampleResourceCategoryDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private TypeSampleDao typeSampleDao;

	@Autowired
	private TypeResourceDao typeResourceDao;
	
	@Autowired
	private SubtypeSampleMetaDao subtypeSampleMetaDao;


	@Autowired
	private SampleBarcodeDao sampleBarcodeDao;
	
	@Autowired
	private BarcodeDao barcodeDao;
	
	@Autowired
	private MessageService messageService;
	  
	@Autowired
	private AuthenticationService authenticationService;
	
	private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp("platformunit",  "sample",SampleMeta.class, request.getSession());
	}
	
	private final MetaHelperWebapp getMetaHelperWebappPlatformUnitInstance() {
		return new MetaHelperWebapp("platformunitInstance",  "sample",SampleMeta.class, request.getSession());
	}
	
	@RequestMapping(value="/selid/list", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String showSelectedSampleListShell(ModelMap m) {
		m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(SampleMeta.class));
		m.addAttribute(JQFieldTag.AREA_ATTR, "platformunitById");
		m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));

		return "facility/platformunit/selid/list";
	}

	@RequestMapping(value="/list.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
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
		
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");

		Map<String, Object> jqgrid = new HashMap<String, Object>();

		List<Sample> sampleList;

		// First, search for typesampleid which its iname is "platform unit"
		Map<String, String> typeSampleQueryMap = new HashMap<String, String>();
		typeSampleQueryMap.put("iName", "platformunit");
		List<TypeSample> typeSampleList = typeSampleDao.findByMap(typeSampleQueryMap);
		if (typeSampleList.size() == 0)
			return "'Platform Unit' sample type is not defined!";
		// Then, use the typesampleid to pull all platformunits from the sample
		// table
		Map<String, Object> sampleListBaseQueryMap = new HashMap<String, Object>();
		sampleListBaseQueryMap.put("typeSampleId", typeSampleList.get(0).getTypeSampleId());

		if (request.getParameter("_search") == null 
				|| request.getParameter("_search").equals("false") 
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {
			sampleList = sampleDao.findByMap(sampleListBaseQueryMap);

		} else {

			sampleListBaseQueryMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			sampleList = this.sampleDao.findByMap(sampleListBaseQueryMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSampleListBaseQueryMap = new HashMap();
				allSampleListBaseQueryMap.put("typeSampleId", 5);

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
	@PreAuthorize("hasRole('ft')")
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
		
		Map<String, Integer> subtypeSampleMetaMap = new HashMap<String, Integer>();
		subtypeSampleMetaMap.put("subtypeSampleId", new Integer(request.getParameter("subtypeSampleId")));

		List <SubtypeSampleMeta> subtypeSampleMetaList = new ArrayList <SubtypeSampleMeta> (this.subtypeSampleMetaDao.findByMap(subtypeSampleMetaMap));
		
		Integer maxCellNum = null;
		Integer multFactor = null;
		
		class LaneOptions {public Integer laneCount; public String label; public LaneOptions(Integer lc, String l){this.laneCount=lc;this.label=l;}}
		
		List <LaneOptions> laneOptionsMap = new ArrayList <LaneOptions> ();
		
		for (SubtypeSampleMeta subtypeSampleMeta : subtypeSampleMetaList) {
			if (subtypeSampleMeta.getK().matches(".*\\.maxCellNumber")) {
				maxCellNum = new Integer(subtypeSampleMeta.getV());
			}
			else if (subtypeSampleMeta.getK().matches(".*\\.multiplicationFactor")) {
				multFactor = new Integer(subtypeSampleMeta.getV());
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
	 * example: select * from subtypesampleresourcecategory stsrc, subtypesample sts 
	 * where stsrc.resourcecategoryid = 4 and stsrc.subtypesampleid = sts.subtypesampleid;
	 * 
	 */
	
	
	/**
	 * Displays a list of SubtypeSamples (e.g. illuminaFlowcellV3) based on the machine_type/resourcecategory (e.g. Illumina HiSeq 2000) 
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
		
		List<SubtypeSample> subtypeSampleList = new ArrayList<SubtypeSample> ();

		// First, search for typesampleid which its iname is "platform unit"
		Map<String, String> typeSampleQueryMap = new HashMap<String, String>();
		typeSampleQueryMap.put("iName", "platformunit");
		List<TypeSample> typeSampleList = typeSampleDao.findByMap(typeSampleQueryMap);
		if (typeSampleList.size() == 0)
			return "'Platform Unit' sample type is not defined!";
		
		// Then, use the typesampleid to pull all platformunits from the sample
		// table
		Map<String, Object> subtypeSampleListBaseQueryMap = new HashMap<String, Object>();
		subtypeSampleListBaseQueryMap.put("typeSampleId", typeSampleList.get(0).getTypeSampleId());
		
		Map<String, Object> searchParamMap = new HashMap<String, Object>();

		List<String> orderConstraints = new ArrayList<String>();
		orderConstraints.add("name");

		if (request.getParameter("_search") == null 
				|| request.getParameter("_search").equals("false") 
				|| StringUtils.isEmpty(request.getParameter("searchString"))) {

			subtypeSampleList = sidx.isEmpty() ? subtypeSampleDao.findByMap(subtypeSampleListBaseQueryMap) : this.subtypeSampleDao.findByMapDistinctOrderBy(subtypeSampleListBaseQueryMap, null, orderConstraints, sord);


		} else {

			searchParamMap.put(request.getParameter("searchField"), request.getParameter("searchString"));

			subtypeSampleList = this.subtypeSampleDao.findByMap(searchParamMap);

			if ("ne".equals(request.getParameter("searchOper"))) {
				Map allSubtypeSampleListBaseQueryMap = new HashMap();
				allSubtypeSampleListBaseQueryMap.put("typeSampleId", 5);
				
				List<SubtypeSample> allSubtypeSampleList = new ArrayList<SubtypeSample>(sidx.isEmpty() ?  this.subtypeSampleDao.findByMap(allSubtypeSampleListBaseQueryMap) : this.subtypeSampleDao.findByMapDistinctOrderBy(allSubtypeSampleListBaseQueryMap, null, orderConstraints, sord));

				for (SubtypeSample excludeSubtypeSample : allSubtypeSampleList) {
					allSubtypeSampleList.remove(excludeSubtypeSample);
				}
				subtypeSampleList = allSubtypeSampleList;
			}
		}

	try {
		
		Map<String, Integer> resourceCategoryMap = new HashMap<String, Integer>();
		
		//resourceCategoryMap.put("resourcecategoryId", (Integer) request.getSession().getAttribute("resourceCategoryId"));
		resourceCategoryMap.put("resourcecategoryId", new Integer(request.getParameter("resourceCategoryId")));

		List<SubtypeSample> subtypeSampleFilteredList = new ArrayList<SubtypeSample> ();

		for (SubtypeSampleResourceCategory subtypeSampleResCat : this.subtypeSampleResourceCategoryDao.findByMap(resourceCategoryMap)) {
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
			int selIndex = subtypeSampleList.indexOf(this.subtypeSampleDao.findById(selId));
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
			cell.put("subtypeSampleId", subtypeSample.getSubtypeSampleId());


			List<SubtypeSampleMeta> subtypeSampleMetaList = getMetaHelperWebapp().syncWithMaster(subtypeSample.getSubtypeSampleMeta());
			
			List<String> cellList = new ArrayList<String>(
					Arrays.asList(
							new String[] { 
									"<a href=/wasp/facility/platformunit/instance/list.do?subtypeSampleId="+subtypeSample.getSubtypeSampleId()+"&typeSampleId="+subtypeSample.getTypeSampleId()+">" + 
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
	
	/**
	 * Displays Platform Unit list filtered by subtypeSampleId
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

		sampleListBaseQueryMap.put("subtypeSampleId", new Integer(request.getParameter("subtypeSampleId")));
		sampleListBaseQueryMap.put("typeSampleId", new Integer(request.getParameter("typeSampleId")));

		
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
										sample.getName(),
										sample.getSubtypeSample()==null?"": sample.getSubtypeSample().getName(), 
										sample.getUser().getFirstName()+" "+sample.getUser().getLastName(),
										allSampleBarcode.get(sample.getSampleId())==null? "" : allBarcode.get(allSampleBarcode.get(sample.getSampleId())),
										this.sampleMetaDao.getSampleMetaByKSampleId("platformunitInstance.lanecount", sample.getSampleId()).getV()}));

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
		sampleForm.setSubtypeSampleId(new Integer(request.getParameter("subtypeSampleId")));
		preparePlatformUnit(sampleForm, sampleId);
		Integer laneCount = new Integer(request.getParameter("platformunitInstance.lanecount")==null || request.getParameter("platformunitInstance.lanecount").equals("")?request.getParameter("lanecountForEditBox"):request.getParameter("platformunitInstance.lanecount"));
		updatePlatformUnitInstance(sampleForm, laneCount, sampleId);

		try {
			response.getWriter().println(messageService.getMessage("platformunitInstance.updated_success.label"));
			return null;
		} catch (Throwable e) {
			throw new IllegalStateException("Cannot output success message ", e);
		}

	}

	@RequestMapping(value="/view/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String viewPlatformUnit(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleDao.getSampleBySampleId(sampleId);

		sample.setSampleMeta(getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta()));

		m.put("sample", sample);

		return "facility/platformunit/detail_ro";
	}

	@RequestMapping(value="/modify/{sampleId}.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String updatePlatformUnitForm(
			 @PathVariable("sampleId") Integer sampleId,
			 ModelMap m) {
		Sample sample = sampleDao.getSampleBySampleId(sampleId);

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

			TypeSample typeSample = typeSampleDao.getTypeSampleByIName("platformunit");
			sampleForm.setTypeSampleId(typeSample.getTypeSampleId());
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
			sampleForm.setTypeSampleId(sampleDb.getTypeSampleId());
	
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
		
		Integer typeSampleId = typeSampleDao.getTypeSampleByIName("cell").getTypeSampleId();
        Sample sampleDb = null;

		if (sampleId == null || sampleId.intValue() == 0) {

			for (int i = 0; i < laneNumber.intValue(); i++) {
				 Sample cell = new Sample();
				 cell.setSubmitterLabId(sampleForm.getSubmitterLabId());
				 cell.setSubmitterUserId(sampleForm.getSubmitterUserId());
				 cell.setName(sampleForm.getName()+"/"+(i+1));
				 cell.setTypeSampleId(typeSampleId);
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

		List<SampleMeta> sampleMetaList = getMetaHelperWebapp().getFromRequest(request, SampleMeta.class);

		getMetaHelperWebapp().validate(sampleMetaList, result);

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
	public String updatePlatformUnitInstance( Sample sampleForm, Integer laneCount, Integer sampleId) {
	
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
		List<SampleMeta> normalizedSampleMeta = sampleMetaHelper.syncWithMaster(mySampleMeta);
		try {
			sampleMetaHelper.setMetaValueByName("lanecount", laneCount.toString(), normalizedSampleMeta);
		} catch (MetadataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // set a value for a member of the list by name
		sampleMetaDao.updateBySampleId(sampleDb.getSampleId(), normalizedSampleMeta); // now we get the list and persist it

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
	@PreAuthorize("hasRole('ft')")
	public String limitPriorToAssignmentForm(@RequestParam("resourceCategoryId") Integer resourceCategoryId,
			ModelMap m) {
		
		TypeResource typeResource = typeResourceDao.getTypeResourceByIName("mps");
		if(typeResource == null || typeResource.getTypeResourceId()==null || typeResource.getTypeResourceId().intValue()==0){
			waspErrorMessage("platformunit.typeResourceNotFound.error");
			return "redirect:/dashboard.do"; 
		}
		Map filterForResourceCategory = new HashMap();
		filterForResourceCategory.put("typeResourceId", typeResource.getTypeResourceId());
		List<ResourceCategory> resourceCategories = resourceCategoryDao.findByMap(filterForResourceCategory);
		
		m.put("resourceCategoryId", resourceCategoryId);
		m.put("resourceCategories", resourceCategories);
//perhaps a better way to screen is to find all jobs that are not completed, and show those that have at least one library
		if(resourceCategoryId.intValue() > 0){
			//get list of jobs with following: 
			//	1. select states from state where task 106 (now it's 5) Assign Library To Platform Unit and status CREATED
			//	2. select those states from statejob to get those jobs
			//	3. filter jobs to include only those requesting specific sequencing machine (resourceCategoryId) 
		
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
		
			m.put("jobList", jobList);
		}
		
		return "facility/platformunit/limitPriorToAssign"; 
	}
	
	/**
	 * limitPriorToPlatformUnitAssignment
	 */
	@RequestMapping(value="/limitPriorToPlatUnitAssign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String limitPriorToPlatUnitAssignForm(ModelMap m) {
		
		TypeResource typeResource = typeResourceDao.getTypeResourceByIName("mps");
		//if(typeResource.getTypeResourceId()==0){
			//waspErrorMessage("platformunit.resourceCategoryNotFound.error");
			//return "redirect:/dashboard.do";
		//}
		Map filterForResourceCategory = new HashMap();
		filterForResourceCategory.put("typeResourceId", typeResource.getTypeResourceId());
		List<ResourceCategory> resourceCategories = resourceCategoryDao.findByMap(filterForResourceCategory);
		
		m.put("resourceCategories", resourceCategories);
		return "facility/platformunit/limitPriorToPlatUnitAssign"; 
	}
	
	
	
  /**
   * assignmentForm
   *
   */
	@RequestMapping(value="/assign.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
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
		List<SubtypeSampleResourceCategory> stsrcList = subtypeSampleResourceCategoryDao.findByMap(stsrcMap);
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
		
		List<Job> jobs = new ArrayList<Job>();
		
		Task task2 = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
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
	@PreAuthorize("hasRole('ft')")
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
	@PreAuthorize("hasRole('ft')")
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
	@PreAuthorize("hasRole('ft')")
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
		else if ( ! librarySample.getTypeSample().getIName().equals("library")) {
			error = true; waspErrorMessage("platformunit.libraryIsNotLibrary.error");	
		}
		else if ( ! laneSample.getTypeSample().getIName().equals("cell")) { 
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
			if( ! "platformunit".equals(flowCell.getTypeSample().getIName()) ){
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
				if( ! libraryAlreadyOnLane.getTypeSample().getIName().equals("library") ){//confirm it's a library
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
   * assignmentRemove
	 * 
	 * @param sampleSourceId
   *
   */
	@RequestMapping(value="/assignRemove.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ft')")
	public String assignmentRemove(
			@RequestParam("samplesourceid") int sampleSourceId,
			@RequestParam("resourceCategoryId") Integer resourceCategoryId,
			@RequestParam("jobsToWorkWith") Integer jobsToWorkWith,
    ModelMap m) {

		SampleSource sampleSource = sampleSourceDao.getSampleSourceBySampleSourceId(sampleSourceId);//this samplesource should represent a cell->lib link, where sampleid is the cell and source-sampleid is the library 
		if(sampleSource.getSampleSourceId()==0){//check for existence
			waspErrorMessage("platformunit.sampleSourceNotExist.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue() + "&jobsToWorkWith=" + jobsToWorkWith.intValue();
		}
		//check that this represents a cell->lib link
		Sample putativeLibrary = sampleSource.getSampleViaSource();
		Sample putativeCell = sampleSource.getSample();
		if( ! putativeLibrary.getTypeSample().getIName().equals("library") || ! putativeCell.getTypeSample().getIName().equals("cell") ){
			waspErrorMessage("platformunit.samplesourceTypeError.error");
			return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue() + "&jobsToWorkWith=" + jobsToWorkWith.intValue();//with this way, the page is updated but map is not passed, so SUCCESS is not displayed
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
		return "redirect:/facility/platformunit/assign.do?resourceCategoryId=" + resourceCategoryId.intValue() + "&jobsToWorkWith=" + jobsToWorkWith.intValue();//with this way, the page is updated but map is not passed, so SUCCESS is not displayed
  }
	
	
	/**
	   * assignmentRemove (POST)
		 * 
		 * @param sampleSourceId
	   *
	   */
		@RequestMapping(value="/assignRemove.do", method=RequestMethod.POST)
		@PreAuthorize("hasRole('ft')")
		public String assignmentRemove(
				@RequestParam("samplesourceid") Integer sampleSourceId,
				@RequestParam("jobId") Integer jobId,
				ModelMap m) {

			if(1==1){
				 return "redirect:/dashboard.do";
			}
			
			
			SampleSource sampleSource = sampleSourceDao.getSampleSourceBySampleSourceId(sampleSourceId);//this samplesource should represent a cell->lib link, where sampleid is the cell and source-sampleid is the library 
			if(sampleSource.getSampleSourceId()==0){//check for existence
				waspErrorMessage("platformunit.sampleSourceNotExist.error");
				return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
			}
			//check that this represents a cell->lib link
			Sample putativeLibrary = sampleSource.getSampleViaSource();
			Sample putativeCell = sampleSource.getSample();
			if( ! putativeLibrary.getTypeSample().getIName().equals("library") || ! putativeCell.getTypeSample().getIName().equals("cell") ){
				waspErrorMessage("platformunit.samplesourceTypeError.error");
				return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
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
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
}
