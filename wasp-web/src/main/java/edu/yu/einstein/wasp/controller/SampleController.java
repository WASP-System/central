package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeCategoryDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.StringHelper;

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
  private LabDao labDao;
  
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
	m.addAttribute("_metaDataMessages", MetaHelperWebapp.getMetadataMessages(request.getSession()));
    
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

    Set<FileGroup> sampleFileSet = sample.getFileGroups();

    List<SampleSource> parentSampleList = sample.getSampleSource();

    List<SampleSource> childSampleList = sample.getSourceSample();

    m.addAttribute("now", now);
    m.addAttribute("sample", sample);
    m.addAttribute("samplemeta", sampleMetaList);
    m.addAttribute("jobsample", jobSampleList);
    List<FileGroup> sampleFileList = new ArrayList<FileGroup>();
    sampleFileList.addAll(sampleFileSet);
    m.addAttribute("samplefile", sampleFileList);
    m.addAttribute("parentsample", parentSampleList);
    m.addAttribute("childsample", childSampleList);

    return "sample/detail";
  }

  private final MetaHelperWebapp getMetaHelperWebapp() {
		return new MetaHelperWebapp(SampleMeta.class, request.getSession());
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
		String selIdAsString = request.getParameter("selId");//not really used here
		//logger.debug("sidx = " + sidx);logger.debug("sord = " + sord);logger.debug("search = " + search);
		//String selIdAsString = request.getParameter("selId");
		//logger.debug("selIdAsString = " + selIdAsString);
		//Parameters coming from grid's toolbar
		//The jobGrid's toolbar's is it's search capability. The toolbar's attribute stringResult is currently set to false, 
		//meaning that each parameter on the toolbar is sent as a key:value pair
		//If stringResult = true, the parameters containing values would have been sent as a key named filters in JSON format 
		//see http://www.trirand.com/jqgridwiki/doku.php?id=wiki:toolbar_searching
		//below we capture parameters on job grid's search toolbar by name (key:value).
		String sampleNameFromGrid = request.getParameter("name")==null?null:request.getParameter("name").trim();//if not passed,  will be null
		String typeFromGrid = request.getParameter("type")==null?null:request.getParameter("type").trim();//this is the iname; if not passed, will be null
		//not used String subTypeFromGrid = request.getParameter("subTypeFromGrid")==null?null:request.getParameter("subTypeFromGrid").trim();//if not passed, will be null
		String jobIdFromGridAsString = request.getParameter("jobId")==null?null:request.getParameter("jobId").trim();//if not passed, will be null
		String submitterNameAndLoginFromGrid = request.getParameter("submitter")==null?null:request.getParameter("submitter").trim();//if not passed, will be null
		String piNameAndLoginFromGrid = request.getParameter("pi")==null?null:request.getParameter("pi").trim();//if not passed, will be null
		//logger.debug("sampleNameFromGrid = " + sampleNameFromGrid);
		//logger.debug("typeFromGrid = " + typeFromGrid);
		//not used logger.debug("subTypeFromGrid = " + subTypeFromGrid);
		//logger.debug("jobIdFromGridAsString = " + jobIdFromGridAsString);
		//logger.debug("submitterNameAndLoginFromGrid = " + submitterNameAndLoginFromGrid);logger.debug("piNameAndLoginFromGrid = " + piNameAndLoginFromGrid);
		
		//DEAL WITH PARAMETERS
		
		//deal with jobId
		Integer jobId = null;
		if(jobIdFromGridAsString != null){//something was passed
			jobId = StringHelper.convertStringToInteger(jobIdFromGridAsString);//returns null is unable to convert
			if(jobId == null){//perhaps the passed value was abc, which is not a valid jobId
				jobId = new Integer(0);//fake it so that result set will be empty; this way, the search will be performed with jobId = 0 and will come up with an empty result set
			}
		}		
	
		//deal with submitter from grid 
		User submitter = null;
		//from grid
		if(submitterNameAndLoginFromGrid != null){//something was passed; expecting firstname lastname (login)
			String submitterLogin = StringHelper.getLoginFromFormattedNameAndLogin(submitterNameAndLoginFromGrid.trim());//if fails, returns empty string
			if(submitterLogin.isEmpty()){//most likely incorrect format !!!!for later, if some passed in amy can always do search for users with first or last name of amy, but would need to be done by searching every job
				submitter = new User();
				submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
			}
			else{
				submitter = userDao.getUserByLogin(submitterLogin);
				if(submitter.getUserId()==null){//if not found in database, submitter is NOT null and getUserId()=null
					submitter.setUserId(new Integer(0));//fake it; perform search below and no user will appear in the result set
				}
			}
		}
		
		//deal with PI (lab)
		User pi = null;
		Lab piLab = null;//this is what's tested below
		if(piNameAndLoginFromGrid != null){//something was passed; expecting firstname lastname (login)
			String piLogin = StringHelper.getLoginFromFormattedNameAndLogin(piNameAndLoginFromGrid.trim());//if fails, returns empty string
			if(piLogin.isEmpty()){//likely incorrect format
				piLab = new Lab();
				piLab.setLabId(new Integer(0));//fake it; result set will come up empty
			}
			else{
				pi = userDao.getUserByLogin(piLogin);//if User not found, pi object is NOT null and pi.getUnserId()=null
				if(pi.getUserId()==null){
					piLab = new Lab();
					piLab.setLabId(new Integer(0));//fake it; result set will come up empty
				}
				else{
					piLab = labDao.getLabByPrimaryUserId(pi.getUserId().intValue());//if the Lab not found, piLab object is NOT null and piLab.getLabId()=null
					if(piLab.getLabId()==null){
						piLab.setLabId(new Integer(0));//fake it; result set will come up empty
					}
				}
			}
		}
		
		List<JobSample> tempJobSampleList = new ArrayList<JobSample>();
		//List<JobSample> jobSamplesFoundInSearch = new ArrayList<JobSample>();//not currently used
		List<JobSample> jobSampleList = new ArrayList<JobSample>();
		
		Map<String, Object> queryMap = new HashMap<String, Object>();
		if(jobId != null){
			queryMap.put("jobId", jobId.intValue());
		}
		if(submitter != null){
			queryMap.put("job.UserId", submitter.getUserId().intValue());
		}
		if(piLab != null){
			queryMap.put("job.labId", piLab.getLabId().intValue());
		}
		if(typeFromGrid != null){
			queryMap.put("sample.sampleType.name", typeFromGrid);
		}
		if(sampleNameFromGrid != null){
			queryMap.put("sample.name", sampleNameFromGrid);
		}
		if(selIdAsString != null && !"".equals(selIdAsString)){//coming from job grid's list of samples
			queryMap.put("sampleId", Integer.parseInt(selIdAsString));
		}
		List<String> orderByColumnNames = new ArrayList<String>();
		if(sidx!=null && !"".equals(sidx)){//sord is apparently never null; default is desc
			if(sidx.equals("jobId")){
				orderByColumnNames.add("jobId");
			}
			else if(sidx.equals("name")){
				orderByColumnNames.add("sample.name");
			}
			else if(sidx.equals("type")){
				orderByColumnNames.add("sample.sampleType.name");
			}
			else if(sidx.equals("submitter")){
				orderByColumnNames.add("job.user.lastName"); orderByColumnNames.add("job.user.firstName");
			}
			else if(sidx.equals("pi")){
				orderByColumnNames.add("job.lab.user.lastName"); orderByColumnNames.add("job.lab.user.firstName");
			}
		}
		else if(sidx==null || "".equals(sidx)){
			orderByColumnNames.add("jobId");
		}
		tempJobSampleList = jobSampleDao.findByMapDistinctOrderBy(queryMap, null, orderByColumnNames, sord);		
		jobSampleList = tempJobSampleList;
		
		//****** will need some mechanism to handle the column RECEIVED? which is currently blank;
		//will likely need comparator to order by that non-existent column (or even select by that column)

		try {

			int pageIndex = Integer.parseInt(request.getParameter("page"));		// index of page
			int pageRowNum = Integer.parseInt(request.getParameter("rows"));	// number of rows in one page
			///			int rowNum = sampleList.size();										// total number of rows
			int rowNum = jobSampleList.size();	// total number of rows
			int pageNum = (rowNum + pageRowNum - 1) / pageRowNum;				// total number of pages
			
			jqgrid.put("records", rowNum + "");
			jqgrid.put("total", pageNum + "");
			jqgrid.put("page", pageIndex + "");

			Map<String, String> sampleData = new HashMap<String, String>();
			sampleData.put("page", pageIndex + "");
			sampleData.put("selId", StringUtils.isEmpty(selIdAsString) ? "" : selIdAsString);
			jqgrid.put("sampledata", sampleData);

			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

			int frId = pageRowNum * (pageIndex - 1);
			int toId = pageRowNum * pageIndex;
			toId = toId <= rowNum ? toId : rowNum;

			// if the selId is set, change the page index to the one contains the selId 
/*			if (!StringUtils.isEmpty(request.getParameter("selIdAsString"))) {
				int selId = Integer.parseInt(request.getParameter("selIdAsString"));
				int selIndex = jobSampleList.indexOf(sampleDao.findById(selId));
				frId = selIndex;
				toId = frId + 1;

				jqgrid.put("records", "1");
				jqgrid.put("total", "1");
				jqgrid.put("page", "1");
			}
*/
			List<JobSample> jobSamplePage = jobSampleList.subList(frId, toId);
			for (JobSample jobSample : jobSamplePage) {

				Map<String, Object> cell = new HashMap<String, Object>();
				cell.put("id", jobSample.getSampleId());

				List<String> cellList=new ArrayList<String>(Arrays.asList(new String[] {
						jobSample.getSample().getName(),
						jobSample.getSample().getSampleType().getName(),
						jobSample.getSample().getSampleSubtype().getName(),
						"<a href=" + getServletPath() + "/job/"+jobSample.getJobId().intValue()+"/homepage.do>J"+jobSample.getJobId().intValue() +"</a>",
						jobSample.getJob().getUser().getFirstName() + " " + jobSample.getJob().getUser().getLastName(),
						jobSample.getJob().getLab().getUser().getFirstName() + " " + jobSample.getJob().getLab().getUser().getLastName(),
						sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(jobSample.getSample())),//is sample received
						" "
				}));
				
				cell.put("cell", cellList);
				rows.add(cell);
				
			}
			jqgrid.put("rows", rows);
			return outputJSON(jqgrid, response);

		} catch (Throwable e) {	throw new IllegalStateException("Can't marshall to JSON " + jobSampleList, e);}
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
			//logger.debug("Control Lib: " + library.getName());
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
		
		//logger.debug("in ajaxAdaptorsByAdaptorId and adaptorsetId = " + adaptorsetId);
		Adaptorset adaptorSet = adaptorsetDao.getAdaptorsetByAdaptorsetId(new Integer(adaptorsetId));
		List<Adaptor> adaptorList = adaptorSet.getAdaptor();
		if(adaptorList.size()==0){
			return "";
		}
		
		StringBuilder stringBuffer = new StringBuilder("");	
		stringBuffer.append("<option value=''>---SELECT AN ADAPTOR---</option>");
		adaptorService.sortAdaptorsByBarcodenumber(adaptorList);
		for(Adaptor adaptor : adaptorList){
			stringBuffer.append("<option value='"+adaptor.getAdaptorId().intValue()+"'>Index "+adaptor.getBarcodenumber().intValue()+" ("+adaptor.getBarcodesequence()+")</option>");
		}
		String returnString = new String(stringBuffer);
		//logger.debug("Output: " + returnString);
		
		//String returnString = new String("<option value=''>---SELECT AN ADAPTOR---</option><option value='1'>Index 1 (AAGCTT)</option>");
		//logger.debug("The return string = " + returnString);
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

