package edu.yu.einstein.wasp.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.controller.util.SampleWrapperWebapp;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.JobCellDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowSampleSubtypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCell;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleCell;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;


@Controller
@Transactional
@RequestMapping("/sampleDnaToLibrary")
public class SampleDnaToLibraryController extends WaspController {

  private SampleDao sampleDao;
  @Autowired
  public void setSampleDao(SampleDao sampleDao) {
    this.sampleDao = sampleDao;
  }
  public SampleDao getSampleDao() {
    return this.sampleDao;
  }

  @Autowired
  private SampleMetaDao sampleMetaDao;
  @Autowired
  private JobCellDao jobCellDao;
  @Autowired
  private JobDao jobDao;
  @Autowired
  private AdaptorDao adaptorDao;
  @Autowired
  private AdaptorsetDao adaptorsetDao;
  @Autowired
  private SampleTypeDao sampleTypeDao;
  @Autowired
  private SampleSubtypeDao sampleSubtypeDao;
  @Autowired
  private JobSampleDao jobSampleDao;
  @Autowired
  private SampleSourceDao sampleSourceDao;
  @Autowired
  private TaskDao taskDao;
  @Autowired
  private StateDao stateDao;
  @Autowired
  private SampleService sampleService;
  

  
  private final MetaHelperWebapp getMetaHelperWebapp() {
    return new MetaHelperWebapp(SampleMeta.class, request.getSession());
  }



  final public String defaultPageFlow = "/sampleDnaToLibrary/detail/{n};/sampleDnaToLibrary/addLibraryMeta/{n};/sampleDnaToLibrary/verify/{n}";

  public String nextPage(Sample sample) {
     String pageFlow = this.defaultPageFlow;
/*
     try {
       List<WorkflowMeta> wfmList = sample.getWorkflow().getWorkflowMeta();
       for (WorkflowMeta wfm : wfmList) {
         if (wfm.getK().equals("workflow.submitpageflow")) {
           pageFlow = wfm.getV();
           break;
         }
       }
     } catch (Exception e) {
     }
*/


    String context = request.getContextPath();
    String uri = request.getRequestURI();
    String path = request.getServletPath();

    // strips context, lead slash ("/"), spring mapping
    String currentMapping = uri.replaceFirst(context, "").replaceFirst("\\.do.*$", "");


    String pageFlowArray[] = pageFlow.split(";");

    int found = -1;
    for (int i=0; i < pageFlowArray.length -1; i++) {
      String page = pageFlowArray[i];
      page = page.replaceAll("\\{n\\}", ""+sample.getSampleId());

      if (currentMapping.equals(page)) {
        found = i;
        break;
      }
    }

    String targetPage = pageFlowArray[found+1] + ".do";

    targetPage = targetPage.replaceAll("\\{n\\}", ""+sample.getSampleId());

    return "redirect:" + targetPage;
  }


  @RequestMapping(value="/detail/{sampleId}", method=RequestMethod.GET)
  public String detail(@PathVariable("sampleId") Integer sampleId, ModelMap m) {
    Sample sample = sampleDao.getSampleBySampleId(sampleId); 

    MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
    List<SampleMeta> coreSampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());

    metaHelperWebapp.setArea("sampleDnaToLibrary");

    List<SampleMeta> sampleDnaLToLibrarySampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());
    
   
    m.put("sample", sample); 
    m.put("sampleId", sampleId); 
    m.put("coreMeta", coreSampleMeta); 
    m.put("sampleDnaToLibSampleMeta", sampleDnaLToLibrarySampleMeta); 

    m.put("area", metaHelperWebapp.getArea());
    m.put("parentarea", metaHelperWebapp.getParentArea());
  
    return "sampleDnaToLibrary/detail";
  }


  @RequestMapping(value="/addLibraryMeta/{sampleId}", method=RequestMethod.GET)
  public String addLibraryMetaForm(@PathVariable("sampleId") Integer sampleId, ModelMap m) {
    Sample sample = sampleDao.getSampleBySampleId(sampleId); 

    MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
    List<SampleMeta> coreSampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());

    metaHelperWebapp.setArea("sampleDnaToLibraryAddLibraryMeta");

    List<SampleMeta> sampleDnaLToLibrarySampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());
    
    m.put("sample", sample); 
    m.put("sampleId", sampleId); 
    m.put("coreMeta", coreSampleMeta); 
    m.put("sampleDnaToLibSampleMeta", sampleDnaLToLibrarySampleMeta); 

    m.put("area", metaHelperWebapp.getArea());
    m.put("parentarea", metaHelperWebapp.getParentArea());
  
    return "sampleDnaToLibrary/addLibraryMeta";
  }

  @RequestMapping(value="/detail/{sampleId}", method=RequestMethod.POST)
  public String modifyDetail (
     @PathVariable Integer sampleId,
     @Valid Sample sampleForm,
     BindingResult result,
     SessionStatus status,
     ModelMap m) {

     return appendMeta(sampleId, sampleForm, result, status, m, "sampleDnaToLibrary", "sampleDnaToLibrary/detail");
  }

  @RequestMapping(value="/addLibraryMeta/{sampleId}", method=RequestMethod.POST)
  public String modifyLibraryMeta (
     @PathVariable Integer sampleId,
     @Valid Sample sampleForm,
     BindingResult result,
     SessionStatus status,
     ModelMap m) {

     return appendMeta(sampleId, sampleForm, result, status, m, "sampleDnaToLibraryAddLibraryMeta",  "sampleDnaToLibrary/addLibraryMeta");
  }


  protected String appendMeta(
       @PathVariable Integer sampleId,
       @Valid Sample sampleForm,
       BindingResult result,
       SessionStatus status,
       ModelMap m, 
       String area,
       String returnPageDef
     ) {
     Sample sample = sampleDao.getSampleBySampleId(sampleId);

     MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
     metaHelperWebapp.setArea(area);

     List<SampleMeta> sampleMetaList = metaHelperWebapp.getFromRequest(request, SampleMeta.class);

     sampleForm.setSampleMeta(sampleMetaList);
     metaHelperWebapp.validate(result);

     if (result.hasErrors()) {
    	waspErrorMessage("sampleDetail.unexpected.error");

        metaHelperWebapp = getMetaHelperWebapp();
        List<SampleMeta> coreSampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());

        metaHelperWebapp.setArea(area);
    
        List<SampleMeta> sampleDnaLToLibrarySampleMeta = sampleMetaList;
    
        m.put("sample", sample); 
        m.put("sampleId", sampleId); 
        m.put("coreMeta", coreSampleMeta); 
        m.put("sampleDnaToLibSampleMeta", sampleDnaLToLibrarySampleMeta); 

        m.put("area", metaHelperWebapp.getArea());
        m.put("parentarea", metaHelperWebapp.getParentArea());
  
        return returnPageDef;
     }
 
     sampleMetaDao.updateBySampleId(sampleId, sampleMetaList);

     return nextPage(sample);
  } 
  
  @RequestMapping(value="/listJobSamples/{jobId}", method=RequestMethod.GET)
  public String listJobSamples(@PathVariable("jobId") Integer jobId, ModelMap m) {
    
	  if(jobId == null ){
		  waspErrorMessage("sampleDetail.jobParameter.error");
		  return "redirect:/dashboard.do";		  
	  }
	  Job job = jobDao.getJobByJobId(jobId);
	  if(job==null || job.getJobId()==null){
		  waspErrorMessage("sampleDetail.jobNotFound.error");
		  return "redirect:/dashboard.do";
	  }
	  m.addAttribute("job", job);

	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  List<Adaptor> allAdaptors = adaptorDao.findAll();
	  Map adaptorList = new HashMap();
	  for(Adaptor adaptor : allAdaptors){
		  adaptorList.put(adaptor.getAdaptorId().toString(), adaptor);
	  }
	  m.addAttribute("adaptors", adaptorList);
	  List<Adaptorset> adaptorsetList = adaptorsetDao.findAll();
	  m.addAttribute("adaptorsets", adaptorsetList);
  
	  //submittedSamples include all samples (both macromolecules and libraries) that were submitted by user
	  //it DOES NOT include facility-generated libraries
	  //List<Sample> submittedSamples = getSubmittedSamplesViaJobCell(job); 
	  List<Sample> submittedSamples = getSubmittedSamplesViaJobSample(job);
	  	  
	  List<String> receivedList = new ArrayList<String>();
	  List<Integer> librariesPerSampleList = new ArrayList<Integer>();//will be used for rowspan on jsp
	  for(Sample sample : submittedSamples){
		  
		  String sampleReceived = "";
		  int numberLibrariesForThisSample = 0;
		  List<Statesample> statesamples = sample.getStatesample();
		  for(Statesample ss : statesamples){
			if(ss.getState().getTask().getIName().equals("Receive Sample")){
				if(ss.getState().getStatus().equals("CREATED")){
					sampleReceived = "NOT ARRIVED";
				}
				else if(ss.getState().getStatus().equals("RECEIVED") || ss.getState().getStatus().equals("FINALIZED")){
					sampleReceived = "RECEIVED";
				}
				else if(ss.getState().getStatus().equals("WITHDRAWN")){
					sampleReceived = "WITHDRAWN";
				}
			}
	  	  }
		  receivedList.add(sampleReceived);
		  
		  if(sample.getSampleType().getIName().equals("rna") || sample.getSampleType().getIName().equals("dna")){
			  List<SampleSource> librariesForThisSample = sample.getSampleSourceViaSourceSampleId();//how many facility-generated libraries for this macromolecule sample
			  numberLibrariesForThisSample = librariesForThisSample.size();
		  }
		  else if(sample.getSampleType().getIName().equals("library")){
			  numberLibrariesForThisSample++;//must be one
		  }		  
		  librariesPerSampleList.add(new Integer(numberLibrariesForThisSample));
	  }
	 
	  
	  
	  //3-15-12 new stuff to fill up the flowcells compatible with this job
	  // pickup FlowCells limited by states and filter to get only those compatible with the selected machine resourceCategoryId
	  Map stateMap = new HashMap(); 
		Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
		if(task == null || task.getTaskId() == null){
			waspErrorMessage("platformunit.taskNotFound.error");
			return "redirect:/dashboard.do";
		}
		stateMap.put("taskId", task.getTaskId()); 	
		stateMap.put("status", "CREATED"); 
		List<State> stateList = stateDao.findByMap(stateMap);
		
		List<Sample> flowCells = new ArrayList<Sample>();
		
		//Map stsrcMap = new HashMap();//get the ids for the types of flow cells that go on the selected machine
		//stsrcMap.put("resourcecategoryId", resourceCategory.getResourceCategoryId()); 
		//stsrcMap.put("resourcecategoryId", job.getJ); 
		//List<SampleSubtypeResourceCategory> stsrcList = sampleSubtypeResourceCategoryService.findByMap(stsrcMap);
		for(State s : stateList){
			List<Statesample> ssList = s.getStatesample();
			for(Statesample ss : ssList){
				if(ss.getSample().getSampleType().getIName().equals("platformunit")){
					for(SampleSubtypeResourceCategory stsrc: ss.getSample().getSampleSubtype().getSampleSubtypeResourceCategory()){
						for(JobResourcecategory jrc : job.getJobResourcecategory()){
							if(stsrc.getResourcecategoryId().intValue() == jrc.getResourcecategoryId().intValue()){
								flowCells.add(ss.getSample());
							}							
						}
					}
				}
			}
		}
		
		Map jobCellFilter = new HashMap();
		jobCellFilter.put("jobId", job.getJobId().intValue());
		List<String> orderByList = new ArrayList<String>();
		orderByList.add("cellindex");
		List<JobCell> jobCellList = jobCellDao.findByMapDistinctOrderBy(jobCellFilter, null, orderByList, "ASC");
		/* for(JobCell jobCell : jobCellList){
			System.out.println(jobCell.getCellindex().intValue());
		} */
	  
	  
		m.addAttribute("jobCellList", jobCellList);
		m.addAttribute("flowCells", flowCells);
		m.addAttribute("samplesSubmitted", submittedSamples);
		m.addAttribute("received", receivedList);
		m.addAttribute("librariespersample", librariesPerSampleList);

		return "sampleDnaToLibrary/listJobSamples";
  }

   @RequestMapping(value = "/createLibraryFromMacro", method = RequestMethod.GET)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createLibrary(@RequestParam("macromolSampleId") Integer macromolSampleId,
			@RequestParam("adaptorsetId") Integer adaptorsetId,//this is the selectedAdaptorSet's Id
			@RequestParam("jobId") Integer jobId,
			ModelMap m) {
	  
	    if( jobId == null ){
			waspErrorMessage("sampleDetail.jobParameter.error");
			return "redirect:/dashboard.do";
		}
		if (macromolSampleId == null){	//waspErrorMessage("user.updated.error");
			waspErrorMessage("sampleDetail.sampleParameter.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		if (adaptorsetId == null){	//waspErrorMessage("user.updated.error");
			waspErrorMessage("sampleDetail.adaptorsetParameter.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
	  
  		Job job = jobDao.getJobByJobId(jobId);
  		if(job.getJobId()==null){//not found in database
  			waspErrorMessage("sampleDetail.jobNotFound.error");
			return "redirect:/dashboard.do";
		}
  		Map<String, String> extraJobDetailsMap = getExtraJobDetails(job);
  		m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

  		Sample macromoleculeSample = sampleDao.getSampleBySampleId(macromolSampleId);
		if(macromoleculeSample.getSampleId()==null){//not found in database
			waspErrorMessage("sampleDetail.sampleNotFound.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		//confirm these two objects exist and part of same job
		JobSample jobSample = jobSampleDao.getJobSampleByJobIdSampleId(jobId, macromolSampleId);
		if(jobSample.getJobSampleId()== null){
			waspErrorMessage("sampleDetail.jobSampleMismatch.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}

		m.put("macromoleculeSample", macromoleculeSample);
		m.put("job", job);


		Adaptorset selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(adaptorsetId);
		//List<Adaptorset> adaptorsets = new ArrayList<Adaptorset>();
		//adaptorsets.add(selectedAdaptorset);
		//m.put("adaptorsets", adaptorsets);  //
		m.put("adaptorsets", adaptorsetDao.findAll()); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)

		m.put("adaptors", selectedAdaptorset.getAdaptor()); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		List<Adaptorset> otherAdaptorsets = adaptorsetDao.findAll();//should really filter this by the machine requested
		otherAdaptorsets.remove(selectedAdaptorset);//remove this one
		m.put("otherAdaptorsets", otherAdaptorsets); 
		
		List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByLoggedInUserRoles(job.getWorkflow().getWorkflowId(), "library");
		if(librarySampleSubtypes.isEmpty()){
			waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
		}
		Map<String, MetaAttribute.FormVisibility> visibilityElementMap = new HashMap<String, MetaAttribute.FormVisibility>(); // specify meta elements that are to be made immutable or hidden in here
		visibilityElementMap.put("genericLibrary.adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
		SampleSubtype librarySampleSubtype = librarySampleSubtypes.get(0); // should be one
		SampleWrapperWebapp libraryManaged = new SampleWrapperWebapp(new Sample(), sampleSourceDao);
		List<SampleMeta> libraryMeta = libraryManaged.getMetaTemplatedToSampleSybtypeAndSynchronizedToMaster(librarySampleSubtype, visibilityElementMap);
		try {
			MetaHelper.setMetaValueByName("genericLibrary", "adaptorset", selectedAdaptorset.getAdaptorsetId().toString(), libraryMeta);
		} catch (MetadataException e) {
			logger.warn("Cannot set value on 'adaptorset': " + e.getMessage() );
		}
		Sample library = libraryManaged.getSampleObject();
		library.setSampleSubtype(librarySampleSubtype);
		library.setSampleType(sampleTypeDao.getSampleTypeByIName("library"));
		library.setSampleMeta(libraryMeta);
		m.put("sample", library); 	
		
		return "sampleDnaToLibrary/createLibrary";

  	}
 
  
  @RequestMapping(value = "/createLibraryFromMacro", method = RequestMethod.POST)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String createLibrary(@RequestParam("macromolSampleId") Integer macromolSampleId,
		  @RequestParam("adaptorsetId") Integer adaptorsetId,//this is the selectedAdaptorSet's Id
		  @RequestParam("jobId") Integer jobId, 
		  @Valid Sample libraryForm, BindingResult result, 
		  SessionStatus status, 
		  ModelMap m) {

	  if( jobId == null){
		  waspErrorMessage("sampleDetail.jobParameter.error");
		  return "redirect:/dashboard.do";
	  }
	  if(macromolSampleId == null ){
		  waspErrorMessage("sampleDetail.sampleParameter.error");
		  return "redirect:/dashboard.do";
	  }

	  Job jobForThisSample = jobDao.getJobByJobId(jobId);
	  if(jobForThisSample.getJobId()==null){//not found in database
		  waspErrorMessage("sampleDetail.jobSampleMismatch.error");
		  return "redirect:/dashboard.do";
	  }
	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(jobForThisSample);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }

	  Sample parentMacromolecule = sampleDao.getSampleBySampleId(macromolSampleId);
	  if(parentMacromolecule.getSampleId()==null){//not found in database
		  waspErrorMessage("sampleDetail.sampleNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  //confirm the job and the macromoleculeSample are part of same job
	  JobSample jobSample = jobSampleDao.getJobSampleByJobIdSampleId(jobId, macromolSampleId);
	  if(jobSample.getJobSampleId()== null){
		  waspErrorMessage("sampleDetail.jobSampleMismatch.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  
	  String newLibraryName = libraryForm.getName().trim();//from the form
	  //confirm that this new library's name is different from all other sample.name in this job for samples of the same sample type (library)
	  SampleType sampleType = sampleTypeDao.getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId());
	  if (!sampleService.isSampleNameUniqueWithinJob(newLibraryName, sampleType, jobForThisSample)){
		  Errors errors=new BindException(result.getTarget(), "Sample");
		  // reject value on the 'name' field with the message defined in sampleDetail.updated.nameClashError
		  // usage: errors.rejectValue(field, errorString, default errorString)
		  errors.rejectValue("name", "sampleDetail.updated.nameClashError", "sampleDetail.updated.nameClashError (no message has been defined for this property)");
		  result.addAllErrors(errors);
	  }
	  
	  // get validated metadata from 
	  Map<String, MetaAttribute.FormVisibility> visibilityElementMap = new HashMap<String, MetaAttribute.FormVisibility>(); // specify meta elements that are to be made immutable or hidden in here
	  visibilityElementMap.put("genericLibrary.adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
	  SampleWrapperWebapp managedLibraryFromForm = new SampleWrapperWebapp(libraryForm, sampleSourceDao);
	  List<SampleMeta> sampleMetaListFromForm = managedLibraryFromForm.getValidatedMetaFromRequest(request, result, visibilityElementMap);
	  
	  
	  
	  Sample newLibrary = managedLibraryFromForm.getSampleObject();

	  if (result.hasErrors()) {

		  prepareSelectListData(m);//doubt that this is required here; really only needed for meta relating to country or state


		  waspErrorMessage("sampleDetail.updated.error");

		  Adaptorset selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(adaptorsetId);
		  m.put("adaptorsets", adaptorsetDao.findAll()); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		  m.put("adaptors", selectedAdaptorset.getAdaptor()); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		  List<Adaptorset> otherAdaptorsets = adaptorsetDao.findAll();//should really filter this by the machine requested
		  otherAdaptorsets.remove(selectedAdaptorset);//remove this one
		  m.put("otherAdaptorsets", otherAdaptorsets); 

		  m.put("macromoleculeSample", parentMacromolecule);
		  m.put("job", jobForThisSample);
		  libraryForm.setSampleMeta(sampleMetaListFromForm);
		  m.put("sample", libraryForm); 


		  return "sampleDnaToLibrary/createLibrary";
	  }

	  //all OK so create/save new library
	  
	  newLibrary.setName(newLibraryName);
	  //newLibrary.setSampleMeta(sampleMetaListFromForm);//this will not be saved simply by saving newLibrary; use sampleMetaDao.updateBySampleId below
	  newLibrary.setSampleType(sampleType);
	  Map filterMap = new HashMap();
	  filterMap.put("sampleTypeId", sampleType.getSampleTypeId());//restrict search to sampleType is library
	  List<SampleSubtype> sampleSubtypeList = sampleSubtypeDao.findByMap(filterMap);
	  String workflowName = jobForThisSample.getWorkflow().getIName().toLowerCase();//such as chipseq
	  for(SampleSubtype sts : sampleSubtypeList){
		  if( sts.getIName().toLowerCase().indexOf(workflowName) > -1 ){
			  newLibrary.setSampleSubtype(sts); 
			  break;
		  }
	  }
	  if(newLibrary.getSampleSubtype() == null){//no match found in database
		  //error
		  waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  newLibrary.setSubmitterLabId(parentMacromolecule.getSubmitterLabId());//needed??

	  newLibrary.setSubmitterUserId(parentMacromolecule.getSubmitterUserId());//needed??

	  newLibrary.setSubmitterJobId(parentMacromolecule.getSubmitterJobId());//needed??
	  newLibrary.setIsActive(new Integer(1));
	  newLibrary.setLastUpdTs(new Date());

	  newLibrary = sampleDao.save(newLibrary);

	  sampleMetaDao.updateBySampleId(newLibrary.getSampleId(), sampleMetaListFromForm);

	  //add entry to jobsample table to link new library to job
	  JobSample newJobSample = new JobSample();
	  newJobSample.setJob(jobForThisSample);
	  newJobSample.setSample(newLibrary);
	  newJobSample = jobSampleDao.save(newJobSample);

	  //add entry to sample source to link new library to the macromolecule from which it was derived
	  SampleSource sampleSource = new SampleSource();
	  sampleSource.setSample(newLibrary);
	  sampleSource.setSampleViaSource(parentMacromolecule);

	  //find max samplesource.multiplexindex for this macromolecule
	  int maxindex = 0;
	  Map filterMap2 = new HashMap();
	  filterMap2.put("sourceSampleId", parentMacromolecule.getSampleId());
	  List<SampleSource> libFromThisMacromoleculeList = sampleSourceDao.findByMap(filterMap2);
	  for(SampleSource ss : libFromThisMacromoleculeList){
		  if(ss.getMultiplexindex().intValue() > maxindex){
			  maxindex = ss.getMultiplexindex().intValue();
		  }
	  }
	  maxindex++;
	  sampleSource.setMultiplexindex(new Integer(maxindex));
	  sampleSource.setLastUpdTs(new Date());
	  sampleSource = sampleSourceDao.save(sampleSource);

	  //TODO record state change
	  Task task = taskDao.getTaskByIName("Create Library");
	  Map filterMap3 = new HashMap();
	  filterMap3.put("taskId", task.getTaskId());
	  filterMap3.put("status", "CREATED");
	  List<State> stateList = stateDao.findByMap(filterMap3);
	  for(State state : stateList){
		  List <Statesample> statesampleList = state.getStatesample();
		  for(Statesample statesample : statesampleList){
			  if(statesample.getSampleId().intValue() == parentMacromolecule.getSampleId().intValue()){
				  state.setStatus("COMPLETED");
			  }
		  }
	  }


	  waspMessage("libraryCreated.created_success.label");
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";

  } 
  
 
  
  @RequestMapping(value = "/librarydetail_ro/{jobId}/{libraryId}", method = RequestMethod.GET)//sampleId represents an existing library (at this moment both user supplied or facility created)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String libraryDetailRO(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) throws MetadataException{
	  return libraryDetail(jobId, libraryId, m, false);
  }
  
  @RequestMapping(value = "/librarydetail_rw/{jobId}/{libraryId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String libraryDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) throws MetadataException{
	  return libraryDetail(jobId, libraryId, m, true);
  }
  
  	@RequestMapping(value = "/librarydetail_rw/{jobId}/{libraryId}", method = RequestMethod.POST)
  	@PreAuthorize("hasRole('su') or hasRole('ft')")
  	public String libraryDetailEdit(
  			@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, 
  			@Valid Sample libraryForm, BindingResult result, 
  			SessionStatus status, 
  			ModelMap m) throws MetadataException{
  		MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp();
  		//List<SampleMeta> allLibraryMeta = sampleMetaHelper.getFromRequest(request, SampleMeta.class);
  		//for(SampleMeta sm: allLibraryMeta){
  		//	logger.debug("ANDY: metaK="+sm.getK()+", metaV="+sm.getV());
  		//}
  		String sampleIdMapByComponentAreaString = request.getParameter("sampleIdMapByComponentArea");
  		Map<String, Integer> sampleIdMapByComponentArea = new HashMap<String, Integer>();
  		for (String pair: sampleIdMapByComponentAreaString.split(";")){
  			String[] components = pair.split(":");
  			sampleIdMapByComponentArea.put(components[0], Integer.valueOf(components[1]) );
  		}
  		
  		/*Integer libraryId = Integer.valueOf(request.getParameter("libraryId"));
  		Integer jobId = Integer.valueOf(request.getParameter("jobId"));
  		
  		String[] areaList = request.getParameter("componentAreas").split(",");
  		logger.debug("ANDY: " + request.getParameter("libraryId") + ", " + request.getParameter("jobId") + ", " + request.getParameter("componentAreas"));*/
  		//List<String> uniqueAreasInLibraryMeta = sampleMetaHelper.getUniqueAreaList();
  		for(String area : sampleIdMapByComponentArea.keySet()){
  			sampleMetaHelper.setArea(area);
  			List<SampleMeta> metaList = sampleMetaHelper.getFromRequest(request, SampleMeta.class);
  			Integer sampleId = sampleIdMapByComponentArea.get(area);
  			if (sampleId != null){
  				for (SampleMeta meta: metaList){
  					logger.debug("ANDY: sampleId="+sampleId+", metaKey="+meta.getK()+", metaValue="+meta.getV());
  				}
  				//sampleMetaDao.updateBySampleId(sampleId, metaList);
  			}
  			
  		}
  		return libraryDetail(jobId, libraryId, m, false);
  	}
  
  public String libraryDetail(Integer jobId, Integer libraryId, ModelMap m, boolean isRW) throws MetadataException{

	    if( jobId == null ){
			waspErrorMessage("sampleDetail.jobParameter.error");
			return "redirect:/dashboard.do";
		}
		if (libraryId == null){	
			waspErrorMessage("sampleDetail.libraryParameter.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
	  
  		Job job = jobDao.getJobByJobId(jobId);
  		if(job.getJobId()==null){//not found in database
  			waspErrorMessage("sampleDetail.jobNotFound.error");
			return "redirect:/dashboard.do";
		}
  		Map<String, String> extraJobDetailsMap = getExtraJobDetails(job);
  		m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

  		Sample library = sampleDao.getSampleBySampleId(libraryId);
  		if(library.getSampleId()==null || ! "library".equals(library.getSampleType().getIName())){//not found in database or not a library
  			waspErrorMessage("sampleDetail.libraryNotFound.error");
  			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
  		
  		//confirm these two objects exist and part of same job
		JobSample jobSample = jobSampleDao.getJobSampleByJobIdSampleId(jobId, libraryId);
		if(jobSample.getJobSampleId()== null){
			waspErrorMessage("sampleDetail.jobSampleMismatch.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		
		// get the library subtype for this workflow as the job-viewer sees it. We will use this 
		// to synchronize the metadata for display
		String[] roles = {"lu"};
		List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(job.getWorkflow().getWorkflowId(), roles, "library");
		if(librarySampleSubtypes.isEmpty()){
			waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
		}
		SampleSubtype librarySampleSubtype = librarySampleSubtypes.get(0); // should be one
		SampleWrapperWebapp libraryManaged = new SampleWrapperWebapp(library, sampleSourceDao);
  		Adaptorset selectedAdaptorset = null;
		Adaptor adaptor = null;
		
		Map<String, MetaAttribute.FormVisibility>  visibilityElementMap = new HashMap<String, MetaAttribute.FormVisibility>(); // specify meta elements that are to be made immutable or hidden in here
		visibilityElementMap.put("genericLibrary.adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
		List<SampleMeta> normalizedSampleMeta = libraryManaged.getMetaTemplatedToSampleSybtypeAndSynchronizedToMaster(librarySampleSubtype, visibilityElementMap);
		try{	
  			adaptor = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptor", normalizedSampleMeta)) );
  			selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", normalizedSampleMeta)) );
  		} catch(MetadataException e){
  			logger.warn("Cannot get metadata : " + e.getMessage());
  		} catch(NumberFormatException e){
  			logger.warn("Cannot convert to numeric value for metadata " + e.getMessage());
  		}
		
		m.addAttribute("adaptorsets", adaptorsetDao.findAll()); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		m.addAttribute("adaptors", selectedAdaptorset.getAdaptor()); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		List<Adaptorset> otherAdaptorsets = adaptorsetDao.findAll();//should really filter this by the machine requested
		otherAdaptorsets.remove(selectedAdaptorset);//remove this one
		if(isRW){
			m.addAttribute("otherAdaptorsets", otherAdaptorsets);
		} 
		m.addAttribute("job", job);
		m.addAttribute("sample", library);
		m.addAttribute("adaptor", adaptor);
		m.addAttribute("normalizedSampleMeta",normalizedSampleMeta);
		m.addAttribute("componentAreas", librarySampleSubtype.getAreaList());
	
		return isRW?"sampleDnaToLibrary/librarydetail_rw":"sampleDnaToLibrary/librarydetail_ro";
  }
  

  
  
  @RequestMapping(value = "/sampledetail_ro/{jobId}/{sampleId}", method = RequestMethod.GET)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String sampleDetailRO(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, ModelMap m) {
	  return sampleDetail(jobId, sampleId, m, false);
  }
 
  @RequestMapping(value = "/sampledetail_rw/{jobId}/{sampleId}", method = RequestMethod.GET)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String sampleDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, ModelMap m) {
	  return sampleDetail(jobId, sampleId, m, true);
  }
 
  
  public String sampleDetail(Integer jobId, Integer sampleId, ModelMap m, boolean isRW){
	  
	  if( jobId == null){
		  waspErrorMessage("sampleDetail.jobParameter.error");
		  return "redirect:/dashboard.do";
	  }
	  if( sampleId == null){
		  waspErrorMessage("sampleDetail.sampleParameter.error");
		  return "redirect:/dashboard.do";
	  }
	  
	  Job job = jobDao.getJobByJobId(jobId);
	  if(job.getJobId()==null){//not found in database
		  waspErrorMessage("sampleDetail.jobNotFound.error");
		  return "redirect:/dashboard.do";
	  }
	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  Sample sample= sampleDao.getSampleBySampleId(sampleId);
	  if(sample.getSampleId()==null){//not found in database
		  waspErrorMessage("sampleDetail.sampleNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  
	  //confirm these two objects exist and part of same job
	  JobSample jobSample = jobSampleDao.getJobSampleByJobIdSampleId(jobId, sampleId);
	  if(jobSample.getJobSampleId()== null){
		  waspErrorMessage("sampleDetail.jobSampleMismatch.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }

	  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(sample, sampleSourceDao);
	  sample.setSampleMeta(sampleManaged.getMetaTemplatedToSampleSybtypeAndSynchronizedToMaster(sample.getSampleSubtype()));
	  
	  m.put("job", job);
	  m.put("sample", sample); 
	  
	  return isRW?"sampleDnaToLibrary/sampledetail_rw":"sampleDnaToLibrary/sampledetail_ro";
  }
  
  @RequestMapping(value = "/sampledetail_rw/{jobId}/{sampleId}", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateSampleDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, 
								@Valid Sample sampleForm, BindingResult result, 
								SessionStatus status, ModelMap m) throws MetadataException {
		  
	  if( jobId == null ){
		  waspErrorMessage("sampleDetail.jobParameter.error");
		  return "redirect:/dashboard.do";
	  }
	  if( sampleId == null){
		  waspErrorMessage("sampleDetail.sampleParameter.error");
		  return "redirect:/dashboard.do";
	  }
		
	  Job jobForThisSample = jobDao.getJobByJobId(jobId);
	  if(jobForThisSample.getJobId()==null){//not found in database
		  waspErrorMessage("sampleDetail.jobSampleMismatch.error");
		  return "redirect:/dashboard.do";
	  }
	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(jobForThisSample);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  //return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  } 
		  
	  Sample sample = sampleDao.getSampleBySampleId(sampleId); 
	  if(sample.getSampleId()==null){//not found in database
		  waspErrorMessage("sampleDetail.sampleNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  	
	  //confirm macromoleculeSample is actually part of this job
	  JobSample jobSample = jobSampleDao.getJobSampleByJobIdSampleId(jobId, sampleId);
	  if(jobSample.getJobSampleId()== null){
		  waspErrorMessage("sampleDetail.jobNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  
	  String newSampleName = sampleForm.getName().trim();//from the form
	  //confirm that, if a new sample.name was supplied on the form, it is different from all other sample.name in this job
	  if( ! newSampleName.equals(sample.getName().trim() ) ){//name was changed
		  List<Sample> samplesInThisJob = jobForThisSample.getSample();
		  for(Sample eachSampleInThisJob : samplesInThisJob){
			  if(eachSampleInThisJob.getSampleId().intValue() != sampleId.intValue()){
				  if( newSampleName.equals(eachSampleInThisJob.getName()) ){
					  // adding an error to 'result object' linked to the 'name' field as the name chosen already exists
					  Errors errors=new BindException(result.getTarget(), "sample");
					  // reject value on the 'name' field with the message defined in sampleDetail.updated.nameClashError
					  // usage: errors.rejectValue(field, errorString, default errorString)
					  errors.rejectValue("name", "sampleDetail.nameClash.error", "sampleDetail.nameClash.error (no message has been defined for this property)");
					  result.addAllErrors(errors);
					  break;
				  }
			  }
		  }
	  }
	  
	  SampleWrapperWebapp managedSample = new SampleWrapperWebapp(sample, sampleSourceDao);
	   List<SampleMeta> metaFromForm = managedSample.getValidatedMetaFromRequest(request, result);
	  if(result.hasErrors()){
		  sampleForm.setName(newSampleName);
		  sampleForm.setSampleMeta(metaFromForm);
		  m.put("job", jobForThisSample);
		  m.put("sample", sampleForm); 
		  return "sampleDnaToLibrary/sampledetail_rw";
	  }
	  
	  managedSample.updateMetaToList(metaFromForm, sampleMetaDao);
	  managedSample.saveAll(sampleService, sampleSourceDao);

	  waspMessage("sampleDetail.updated_success.label");
	  return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";

  }
  
  
  private Map<String, String> getExtraJobDetails(Job job){
	  
	  Map<String, String> extraJobDetailsMap = new HashMap<String, String>();

	  List<JobResourcecategory> jobResourceCategoryList = job.getJobResourcecategory();
	  for(JobResourcecategory jrc : jobResourceCategoryList){
		  if(jrc.getResourceCategory().getResourceType().getIName().equals("mps")){
			  extraJobDetailsMap.put("Machine", jrc.getResourceCategory().getName());
			  break;
		  }
	  }
	  /* did not work
	  MetaHelperWebapp jobMetaHelper = new MetaHelperWebapp("job", JobMeta.class, request.getSession());
	  try{
		  extraJobDetailsMap.put("Read Length",jobMetaHelper.getMetaValueByName("readLength", job.getJobMeta()));
		  extraJobDetailsMap.put("Read Type",jobMetaHelper.getMetaValueByName("readType", job.getJobMeta()).toUpperCase());
	  } catch(MetadataException e){
			logger.warn("Cannot get metadata for readLength or readType : " + e.getMessage());
	  }
	  */
	  for(JobMeta jobMeta : job.getJobMeta()){
		  if(jobMeta.getK().indexOf("readLength") != -1){
			  extraJobDetailsMap.put("Read Length", jobMeta.getV());
		  }
		  if(jobMeta.getK().indexOf("readType") != -1){
			  extraJobDetailsMap.put("Read Type", jobMeta.getV().toUpperCase());
		  }
	  }
	  
	  return extraJobDetailsMap;	  
  }
  
  private List<Sample> getSubmittedSamplesViaJobCell(Job job){
	  
	  //For a list of the samples initially submitted to a job, pull from table jobcell
	  //exclude duplicates by using a set
	  //transfer to list and order by sample name
	  //Note that table jobsample is not appropriate as it will contain libraries made by the facility (from submitted macromolecules)
	  Set<Sample> samplesSet = new HashSet<Sample>();//use this to store a set of unique samples submitted by the user for a specific job; use treeset as it is ordered (by what I don't know)
	  Map filterJobCell = new HashMap();
	  filterJobCell.put("jobId", job.getJobId());
	  List<JobCell> jobCells = jobCellDao.findByMap(filterJobCell);
	  for(JobCell jobCell : jobCells){
		  List<SampleCell> sampleCells = jobCell.getSampleCell();
		  for(SampleCell sampleCell : sampleCells){
			   samplesSet.add(sampleCell.getSample());
		  }
	  }	  
	  List<Sample> submittedSamples = new ArrayList<Sample>();//need list in order to sort (sets do not sort)
	  for(Sample sample : samplesSet){
		  submittedSamples.add(sample);
	  }
	  Collections.sort(submittedSamples, new SampleNameComparator());//for class SampleNameComparator, see end of this file
	  
	  return submittedSamples;
	  
  }
  
  private List<Sample> getSubmittedSamplesViaJobSample(Job job){
	  
	  //Get list of all samples for a job from table jobsample
	  //This will include gDNA, RNA, libraries submitted by a user, and libraries created by the facility.
	  //Then, filter out all those samples that are libraries created by the facility (any sampleid that appears in samplesource.sampleid)
	  List<Sample> submittedSamples = new ArrayList<Sample>();
	  Map filterJobCell = new HashMap();
	  filterJobCell.put("jobId", job.getJobId());
	  List<JobSample> jobSamples = jobSampleDao.findByMap(filterJobCell);
	  for(JobSample jobSample : jobSamples){
		  Sample sample  = jobSample.getSample();
		  if(sample.getSampleSource().size() == 0){//it's NOT a facility-generated library
			  submittedSamples.add(sample);
		  }
	  }	  

	  Collections.sort(submittedSamples, new SampleNameComparator());//sort by sample's name; for class SampleNameComparator, see end of this file
	  
	  return submittedSamples;
	  
  }
  
}


class SampleNameComparator implements Comparator<Sample> {
    @Override
    public int compare(Sample arg0, Sample arg1) {
        return arg0.getName().compareToIgnoreCase(arg1.getName());
    }
}

