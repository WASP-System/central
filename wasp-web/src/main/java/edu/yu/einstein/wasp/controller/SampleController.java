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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

@Controller
@Transactional
@RequestMapping("/sample")
public class SampleController extends WaspController {

  
  private SampleDao sampleDao;
  
  private SampleTypeDao	sampleTypeDao;
  
  @Autowired
  private AdaptorsetDao	adaptorsetDao;
  
  @Autowired
  private AdaptorDao adaptorDao;
  
  @Autowired
  private SampleSubtypeDao	sampleSubtypeDao;
  
  @Autowired
  private SampleTypeCategoryDao	sampleTypeCategoryDao;
  
  @Autowired
  private JobDao jobDao;

  @Autowired
  private JobSampleDao jobSampleDao;

  @Autowired
  private UserDao userDao;
  
  @Autowired
  private RunDao runDao;
  
  @Autowired
  private SampleService sampleService;
  
  @Autowired
  private AdaptorService adaptorService;
  
  @Autowired
  private SampleMetaDao sampleMetaDao;
  
  @Autowired
  public void setSampleDao(SampleDao sampleDao) {
    this.sampleDao = sampleDao;
  }
  public SampleDao getSampleDao() {
    return this.sampleDao;
  }
  
  @Autowired
  public void setSampleTypeDao(SampleTypeDao sampleTypeDao) {
	  this.sampleTypeDao = sampleTypeDao;
}

  public SampleTypeDao getSampleTypeDao() {
	    return this.sampleTypeDao;
  }

  @RequestMapping("/list")
  public String list(ModelMap m) {
    //List <Sample> sampleList = this.getSampleDao().findAll();
    
    m.addAttribute("_metaList", getMetaHelperWebapp().getMasterList(MetaBase.class));
	m.addAttribute(JQFieldTag.AREA_ATTR, getMetaHelperWebapp().getArea());
	m.addAttribute("_metaDataMessages", MetaHelper.getMetadataMessages(request.getSession()));
    
    //m.addAttribute("sample", sampleList);
	prepareSelectListData(m);


    return "sample/list";
  }

  @RequestMapping(value="/detail/{strId}", method=RequestMethod.GET)
  public String detail(@PathVariable("strId") String strId, ModelMap m) {
    String now = (new Date()).toString();

    Integer i;
    try {
      i = new Integer(strId);
    } catch (Exception e) {
      return "default";
    }

    Sample sample = this.getSampleDao().getById(i.intValue());

    List<SampleMeta> sampleMetaList = sample.getSampleMeta();
    sampleMetaList.size();

    List<JobSample> jobSampleList = sample.getJobSample();
    jobSampleList.size();

    List<SampleFile> sampleFileList = sample.getSampleFile();
    sampleFileList.size();

    List<SampleSource> parentSampleList = sample.getSampleSource();
    parentSampleList.size();

    List<SampleSource> childSampleList = sample.getSourceSampleId();
    childSampleList.size();

    m.addAttribute("now", now);
    m.addAttribute("sample", sample);
    m.addAttribute("samplemeta", sampleMetaList);
    m.addAttribute("jobsample", jobSampleList);
    m.addAttribute("samplefile", sampleFileList);
    m.addAttribute("parentsample", parentSampleList);
    m.addAttribute("childsample", childSampleList);

    return "sample/detail";
  }

  private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(SampleMeta.class, request.getSession());
  }
	
	/**
	 * Prepares page to display JQGrid table with a list of samples where SampleTypeCategory.iName="biomaterial"
	 * 
	 * @Author Natalia Volnova
	 */
  	private static List<Sample>  joinById (List <Sample> listOne, List <SampleType> ListTwo) {
  		
  		List <Sample> finalList = new ArrayList <Sample> ();

		for (Iterator <SampleType> it=ListTwo.iterator(); it.hasNext();) {
			SampleType tempSampleType = it.next();
			for (Iterator <Sample> itSample=listOne.iterator(); itSample.hasNext();) {
				Sample tempSample = itSample.next();
				
				if (tempSample.getSampleTypeId().intValue() == tempSampleType.getSampleTypeId().intValue()) {
					finalList.add(tempSample);
				}
			}
		}
  		
  		return finalList;
  	}
  	
  	/**
  	 * 
  	 * @param response
  	 * @return
  	 */

	@RequestMapping(value = "/listJSON", method = RequestMethod.GET)
	public String getListJSON(HttpServletResponse response) {

		// result
		Map<String, Object> jqgrid = new HashMap<String, Object>();
		
		//Parameters coming from the jobGrid
		String sord = request.getParameter("sord");//grid is set so that this always has a value
		String sidx = request.getParameter("sidx");//grid is set so that this always has a value
		String search = request.getParameter("_search");//from grid (will return true or false, depending on the toolbar's parameters)
		System.out.println("sidx = " + sidx);System.out.println("sord = " + sord);System.out.println("search = " + search);
		//String selIdAsString = request.getParameter("selId");
		//System.out.println("selIdAsString = " + selIdAsString);
		//Parameters coming from grid's toolbar
		//The jobGrid's toolbar's is it's search capability. The toolbar's attribute stringResult is currently set to false, 
		//meaning that each parameter on the toolbar is sent as a key:value pair
		//If stringResult = true, the parameters containing values would have been sent as a key named filters in JSON format 
		//see http://www.trirand.com/jqgridwiki/doku.php?id=wiki:toolbar_searching
		//below we capture parameters on job grid's search toolbar by name (key:value).
		String nameFromGrid = request.getParameter("name")==null?null:request.getParameter("name").trim();//if not passed,  will be null
		String typeFromGrid = request.getParameter("typeFromGrid")==null?null:request.getParameter("typeFromGrid").trim();//if not passed, will be null
		String subTypeFromGrid = request.getParameter("subTypeFromGrid")==null?null:request.getParameter("subTypeFromGrid").trim();//if not passed, will be null
		String jobIdFromGrid = request.getParameter("jobIdFromGrid")==null?null:request.getParameter("jobIdFromGrid").trim();//if not passed, will be null
		String submitterFromGrid = request.getParameter("submitterFromGrid")==null?null:request.getParameter("submitterFromGrid").trim();//if not passed, will be null
		String piFromGrid = request.getParameter("piFromGrid")==null?null:request.getParameter("piFromGrid").trim();//if not passed, will be null
		System.out.println("nameFromGrid = " + nameFromGrid);System.out.println("typeFromGrid = " + typeFromGrid);
		System.out.println("subTypeFromGrid = " + subTypeFromGrid);System.out.println("jobIdFromGrid = " + jobIdFromGrid);
		System.out.println("submitterFromGrid = " + submitterFromGrid);System.out.println("piFromGrid = " + piFromGrid);
		
		List<JobSample> tempJobSampleList = new ArrayList<JobSample>();
		List<JobSample> jobSamplesFoundInSearch = new ArrayList<JobSample>();//not currently used
		List<JobSample> jobSampleList = new ArrayList<JobSample>();
		
		Map queryMap = new HashMap();
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("startts");//start run
		String direction = "desc";
		if(sidx!=null && sidx.equals("jobId") && sord !=null && sord.equals("asc")){
			direction = new String("asc");
		}
		tempJobSampleList = jobSampleDao.findAllOrderBy("jobId", direction);

		jobSampleList = tempJobSampleList;
		
		///////////////////List<Sample> sampleList = sampleDao.findAll();
		String selId = request.getParameter("selId");
		
/*
		String sord = request.getParameter("sord");
		String sidx = request.getParameter("sidx");
		String search = request.getParameter("_search");
		String searchField = request.getParameter("searchField");
		String searchString = request.getParameter("searchString");
		String selId = request.getParameter("selId");

		if (!StringUtils.isEmpty(selId)) {

			sampleList.add(this.sampleDao.getSampleBySampleId(Integer.parseInt(selId)));
		
		} else if (!StringUtils.isEmpty(search) && !StringUtils.isEmpty(searchField) && !StringUtils.isEmpty(searchString) ) {
		
			Map<String, String> m = new HashMap<String, String>();

			m.put(searchField, searchString);

			if (sidx.isEmpty()) {
				sampleList = this.sampleDao.findByMap(m);
			} else {
				List<String> sidxList =  new ArrayList<String>();
				sidxList.add(sidx);
				sampleList = this.sampleDao.findByMapDistinctOrderBy(m, null, sidxList, sord);
			}

			if ("ne".equals(request.getParameter("searchOper"))) {
				List<Sample> allSamples = new ArrayList<Sample>(sidx.isEmpty() ? 
						this.sampleDao.findAll() : this.sampleDao.findAllOrderBy(sidx, sord));

				for (Iterator<Sample> it = sampleList.iterator(); it.hasNext();) {
					Sample excludeSample = it.next();
					allSamples.remove(excludeSample);

				}
				sampleList = allSamples;
			}
			
		} else {
			
			sampleList = sidx.isEmpty() ? this.sampleDao.findAll() : this.sampleDao.findAllOrderBy(sidx, sord);
		}
*/
		try {

			Map<Integer, String> allSampleTypes = new TreeMap<Integer, String>();
			for (SampleType sampleType : this.getSampleTypeDao().findAll()) {
				allSampleTypes.put(sampleType.getSampleTypeId(), sampleType.getName());
			}
			Map<Integer, String> allSubSampleTypes = new TreeMap<Integer, String>();
			for (SampleSubtype sampleSubtype : sampleSubtypeDao.findAll()) {
				allSubSampleTypes.put(sampleSubtype.getSampleSubtypeId(), sampleSubtype.getName());
			}

			Map<Integer, String> allJobs = new TreeMap<Integer, String>();
			for (Job job : jobDao.findAll()) {
				allJobs.put(job.getJobId(), job.getName());
			}

			Map<Integer, String> allUsers = new TreeMap<Integer, String>();
			for (User user : userDao.findAll()) {
				allUsers.put(user.getUserId(), user.getLastName() + ", " + user.getFirstName());
			}

			Map<Integer, String> allRuns = new TreeMap<Integer, String>();
			for (Run run : runDao.findAll()) {
				allRuns.put(run.getSampleId(), run.getName());
			}

			// Remove all samples whose sampletypecategory is not "biomaterial" and also remove all control libraries
//			List<Sample> sampleListFiltered = new ArrayList<Sample> ();
//			for (Sample sample : sampleList) {
//				if (sample.getSampleType().getSampleTypeCategory().getIName().equals("biomaterial")) {
//					if ( ! sample.getSampleSubtype().getIName().equals("controlLibrarySample")){//exclude controlLibraries
//						sampleListFiltered.add(sample);
//					}
//				}
//			}
///			sampleList = sampleListFiltered;
			
			
			
			
			
			
			
			
			
			
			
			
			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
///			int rowNum = sampleList.size();										// total number of rows
int rowNum = jobSampleList.size();	
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");

			Map<String, String> sampleData = new HashMap<String, String>();
			sampleData.put("page", pageIndex + "");
			sampleData.put("selId", StringUtils.isEmpty(selId) ? "" : selId);
			jqgrid.put("sampledata", sampleData);
			 
			/***** Begin Sort by User last name *****/
			class SampleSubmitterNameComparator implements Comparator<Sample> {
				@Override
				public int compare(Sample arg0, Sample arg1) {
					return arg0.getUser().getLastName().compareToIgnoreCase(arg1.getUser().getLastName());
				}
			}

//			if (sidx.equals("submitterUserId")) {
//				Collections.sort(sampleList, new SampleSubmitterNameComparator());
//				if (sord.equals("desc"))
//					Collections.reverse(sampleList);
//			}
			/***** End Sort by User last name *****/

			List<Map> rows = new ArrayList<Map>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			// if the selId is set, change the page index to the one contains the selId 
//			if (!StringUtils.isEmpty(request.getParameter("selId"))) {
//				int selId = Integer.parseInt(request.getParameter("selId"));
//				int selIndex = sampleList.indexOf(sampleDao.findById(selId));
//				frId = selIndex;
//				toId = frId + 1;
//
//				jqgrid.put("records", "1");
//				jqgrid.put("total", "1");
//				jqgrid.put("page", "1");
//			}

			//List<Sample> samplePage = sampleList.subList(frId, toId);
			//for (Sample sample:samplePage) {
//			List<Sample> samplePage = sampleList.subList(frId, toId);
			List<JobSample> jobSamplePage = jobSampleList.subList(frId, toId);
//			for (Sample sample : samplePage) {
			for (JobSample jobSample : jobSamplePage) {

				Map cell = new HashMap();
				cell.put("id", jobSample.getSampleId());

				//List<SampleMeta> sampleMeta = getMetaHelperWebapp().syncWithMaster(sample.getSampleMeta());

/*				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						sample.getName(),
						(sample.getSampleTypeId() == null)? "": allSampleTypes.get(sample.getSampleTypeId()),
						(sample.getSampleSubtypeId() == null)? "": allSubSampleTypes.get(sample.getSampleSubtypeId()),
						(sample.getSubmitterJobId() == null)? "" : allJobs.get(sample.getSubmitterJobId()),
						allUsers.get(sample.getSubmitterUserId()),
						(sample.getSampleSubtype().getIName().indexOf("FacilityLibrary") > -1) ? "N/A" : sampleService.convertReceiveSampleStatusForWeb(sampleService.getReceiveSampleStatus(sample)),//facility-generated libraries have no receive sample information as they were created by the facility
						allRuns.get(sample.getSampleId())
				}));
*/
				
				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						jobSample.getSample().getName(),
						jobSample.getSample().getSampleType().getName(),
						jobSample.getSample().getSampleSubtype().getName(),
						"J"+jobSample.getJobId().intValue(),
						jobSample.getJob().getUser().getFirstName() + " " + jobSample.getJob().getUser().getLastName(),
						jobSample.getJob().getLab().getUser().getFirstName() + " " + jobSample.getJob().getLab().getUser().getLastName(),
						" ",
						" "
				}));
				
				//for (SampleMeta meta : sampleMeta) {
				//	cellList.add(meta.getV());
				//}

				cell.put("cell", cellList);

				rows.add(cell);

				jqgrid.put("rows", rows);

			}
			return outputJSON(jqgrid, response);

		} catch (Throwable e) {
			throw new IllegalStateException("Can't marshall to JSON " + jobSampleList, e);
		}
	}

	@RequestMapping(value="/listControlLibraries", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String listLibraryControls(ModelMap m) {
		
		SampleType sampleType = sampleTypeDao.getSampleTypeByIName("library");
		SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName("controlLibrarySample");
		Map<String, Integer> filterMap = new HashMap<String, Integer>();
		filterMap.put("sampleTypeId", sampleType.getSampleTypeId());
		filterMap.put("sampleSubtypeId", sampleSubtype.getSampleSubtypeId());		
		List<Sample> controlLibraryList = sampleDao.findByMap(filterMap);
		sampleService.sortSamplesBySampleName(controlLibraryList);
		Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();
		for(Sample library : controlLibraryList){
			//System.out.println("Control Lib: " + library.getName());
			Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			if(adaptor==null){
				//message and get out of here
			}
			libraryAdaptorMap.put(library, adaptor);	
		}
		m.addAttribute("controlLibraryList", controlLibraryList);
		m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
		//return "redirect:/dashboard.do";	
		return "sample/controlLibraries/list";
	}
	
	@RequestMapping(value="/createUpdateLibraryControl/{sampleId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String createUpdateLibraryControl(@PathVariable("sampleId") Integer sampleId, ModelMap m) {
		
		//if(sampleId.intValue()==0){
		//	return "redirect:/dashboard.do";
		//}

		Sample controlLibrary = null;
		Adaptor controlLibraryAdaptor = null;
		List<Adaptor> adaptorList = null;
		
		//update an existing controlLibrary
		if(sampleId.intValue() > 0){
			controlLibrary = sampleDao.getSampleBySampleId(sampleId);
			if(controlLibrary.getSampleId() == null || controlLibrary.getSampleId() == null || controlLibrary.getSampleId().intValue() == 0){
				//not found in database
				//error and get out of here
			}
			//get the Adaptor used by this library
			controlLibraryAdaptor = sampleService.getLibraryAdaptor(controlLibrary);
			if(controlLibraryAdaptor==null || controlLibraryAdaptor.getAdaptorId()==null){
				//message and get out of here
			}
			//identify the adaptorSet that this library's adaptor belongs, then get all the Adaptors for that AdaptorSet (for display in dropdown box)
			Map<String,Integer> adaptorFilter = new HashMap<String,Integer>();
			adaptorFilter.put("adaptorsetId", controlLibraryAdaptor.getAdaptorsetId());
			List<String> list = new ArrayList<String>();
			list.add("barcodenumber");//orderby index (index1,2,3,..)
			adaptorList = adaptorDao.findByMapDistinctOrderBy(adaptorFilter, null, list, "ASC");
			
		}
		//create a new controlLibrary
		else if(sampleId.intValue() == 0){
			controlLibrary = new Sample();
			controlLibrary.setSampleId(new Integer(0));//will set the default id of zero
			controlLibrary.setIsActive(new Integer(1));//will set the default isactive value, that will be displayed on the form, to active
			controlLibraryAdaptor = new Adaptor();
			adaptorList = new ArrayList<Adaptor>();
		}		
		
		m.addAttribute("controlLibrary", controlLibrary);
		m.addAttribute("controlLibraryAdaptor", controlLibraryAdaptor);
		m.addAttribute("adaptorList", adaptorList);
		
		List<Adaptorset> adaptorsetList = adaptorsetDao.findAll();
		m.addAttribute("adaptorsetList", adaptorsetList);
		
		return "sample/controlLibraries/createUpdate";
	}
	
	@RequestMapping(value="ajaxAdaptorsByAdaptorId.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public @ResponseBody String ajaxAdaptorsByAdaptorId(@RequestParam("adaptorsetId") String adaptorsetId){
		
		//System.out.println("in ajaxAdaptorsByAdaptorId and adaptorsetId = " + adaptorsetId);
		Adaptorset adaptorSet = adaptorsetDao.getAdaptorsetByAdaptorsetId(new Integer(adaptorsetId));
		List<Adaptor> adaptorList = adaptorSet.getAdaptor();
		if(adaptorList.size()==0){
			return "";
		}
		
		StringBuffer stringBuffer = new StringBuffer("");	
		stringBuffer.append("<option value=''>---SELECT AN ADAPTOR---</option>");
		adaptorService.sortAdaptorsByBarcodenumber(adaptorList);
		for(Adaptor adaptor : adaptorList){
			stringBuffer.append("<option value='"+adaptor.getAdaptorId().intValue()+"'>Index "+adaptor.getBarcodenumber().intValue()+" ("+adaptor.getBarcodesequence()+")</option>");
		}
		String returnString = new String(stringBuffer);
		//System.out.println("Output: " + returnString);
		
		//String returnString = new String("<option value=''>---SELECT AN ADAPTOR---</option><option value='1'>Index 1 (AAGCTT)</option>");
		//System.out.println("The return string = " + returnString);
		//return "<option value=''>---SELECT A READ TYPE---</option><option value='single'>single</option><option value='paired'>paired</option>";
		//String returnString = new String(stringBuffer);
		//return "<option value=''>---SELECT AN ADAPTOR---</option><option value='1'>Index 1 (AAGCTT)</option>";
		return returnString;
	}
	
	@RequestMapping(value="/createUpdateLibraryControl", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	  public String createUpdateLibraryControl(@RequestParam("sampleId") Integer sampleId, @RequestParam("name") String name, 
			  @RequestParam("adaptorsetId") Integer adaptorsetId, 
			  @RequestParam("adaptorId") Integer adaptorId, @RequestParam("active") Integer active, ModelMap m) {
		
		//confirm parameters
		name = name.trim();
		if("".equals(name)){
			//error and get out of here
			return "redirect:/sample/listControlLibraries.do";
		}
		if(active.intValue() != 0 && active.intValue() != 1){
			//error for value of isactive so get out of here
			return "redirect:/sample/listControlLibraries.do";
		}
		//confirm adaptorsetId is consistent with selected adaptor's adaptorset
		Adaptor adaptor = adaptorDao.getAdaptorByAdaptorId(adaptorId);
		if(adaptor.getAdaptorId()==null){
			//error and get out of here
			return "redirect:/sample/listControlLibraries.do";
		}
		if(adaptor.getAdaptorset().getAdaptorsetId().intValue() != adaptorsetId.intValue()){
			//error and get out of here
			return "redirect:/sample/listControlLibraries.do";
		}
		
		if(sampleId.intValue() > 0){//updating an existing control
			Sample controlLibrary = sampleDao.getSampleBySampleId(sampleId);
			if(controlLibrary.getSampleId() == null){//existing sample unexpectedly NOT found in the database
				//message and get outofhere
				return "redirect:/sample/listControlLibraries.do";
			}
			controlLibrary.setName(name);
			controlLibrary.setIsActive(active);
			List<SampleMeta> sampleMetaList = controlLibrary.getSampleMeta();
			for(SampleMeta sampleMeta : sampleMetaList){
				if(sampleMeta.getK().equals("genericLibrary.adaptorset")){
					sampleMeta.setV(adaptorsetId.toString());
				}
				if(sampleMeta.getK().equals("genericLibrary.adaptor")){
					sampleMeta.setV(adaptorId.toString());
				}
			}
			controlLibrary.setSampleMeta(sampleMetaList);
			sampleDao.save(controlLibrary);
		}
		else if(sampleId.intValue() == 0){//create new control
			SampleType sampleType = sampleTypeDao.getSampleTypeByIName("library");
			if(sampleType.getSampleTypeId()==null){
				//error get out of here
			}
			SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeByIName("controlLibrarySample");
			if(sampleSubtype.getSampleSubtypeId()==null){
				//error get out of here
				return "redirect:/sample/listControlLibraries.do";
			}
			
			Sample newControlLibrary = new Sample();
			newControlLibrary.setSampleType(sampleType);
			newControlLibrary.setSampleSubtype(sampleSubtype);
			newControlLibrary.setName(name);
			newControlLibrary.setIsActive(active);
			
			newControlLibrary = sampleDao.save(newControlLibrary);
			Integer newId = newControlLibrary.getSampleId();
			if(newId==null || newId.intValue()==0){
				//rollback and get outahere
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return "redirect:/sample/listControlLibraries.do";
			}
			
			SampleMeta sampleMeta = new SampleMeta();
			sampleMeta.setK("genericLibrary.adaptorset");
			sampleMeta.setV(adaptorsetId.toString());
			sampleMeta.setSampleId(newId);
			sampleMetaDao.save(sampleMeta);
			SampleMeta sampleMeta2 = new SampleMeta();
			sampleMeta2.setK("genericLibrary.adaptor");
			sampleMeta2.setV(adaptorId.toString());
			sampleMeta2.setSampleId(newId);
			sampleMetaDao.save(sampleMeta2);
			if(sampleMeta.getSampleMetaId()==null || sampleMeta.getSampleMetaId()==0 || sampleMeta2.getSampleMetaId()==null || sampleMeta2.getSampleMetaId()==0){
				//rollback and get out
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return "redirect:/sample/listControlLibraries.do";
			}
		}
		
		return "redirect:/sample/listControlLibraries.do";
	}
}

