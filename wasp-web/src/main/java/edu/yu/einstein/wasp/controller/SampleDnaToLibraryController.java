package edu.yu.einstein.wasp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.Valid;

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

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobCell;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleCell;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AdaptorsetService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SampleSourceService;
import edu.yu.einstein.wasp.service.StatesampleService;
import edu.yu.einstein.wasp.service.StateService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobSampleService;
import edu.yu.einstein.wasp.service.JobCellService;

@Controller
@Transactional
@RequestMapping("/sampleDnaToLibrary")
public class SampleDnaToLibraryController extends WaspController {

  private SampleService sampleService;
  @Autowired
  public void setSampleService(SampleService sampleService) {
    this.sampleService = sampleService;
  }
  public SampleService getSampleService() {
    return this.sampleService;
  }

  @Autowired
  private SampleMetaService sampleMetaService;
  @Autowired
  private JobCellService jobCellService;
  @Autowired
  private JobService jobService;
  @Autowired
  private AdaptorService adaptorService;
  @Autowired
  private AdaptorsetService adaptorsetService;
  @Autowired
  private TypeSampleService typeSampleService;
  @Autowired
  private SubtypeSampleService subtypeSampleService;
  @Autowired
  private JobSampleService jobSampleService;
  @Autowired
  private SampleSourceService sampleSourceService;
  @Autowired
  private TaskService taskService;
  @Autowired
  private StateService stateService;
  @Autowired
  private StatesampleService statesampleService;

  private final MetaHelperWebapp getMetaHelperWebapp() {
    return new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
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
    Sample sample = sampleService.getSampleBySampleId(sampleId); 

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
    Sample sample = sampleService.getSampleBySampleId(sampleId); 

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
     Sample sample = sampleService.getSampleBySampleId(sampleId);

     MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
     metaHelperWebapp.setArea(area);

     List<SampleMeta> sampleMetaList = metaHelperWebapp.getFromRequest(request, SampleMeta.class);

     sampleForm.setSampleMeta(sampleMetaList);
     metaHelperWebapp.validate(sampleMetaList, result);

     if (result.hasErrors()) {
        waspMessage("hello.error");

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
 
     sampleMetaService.updateBySampleId(metaHelperWebapp.getArea(), sampleId, sampleMetaList);

     return nextPage(sample);
  } 
  
  @RequestMapping(value="/listJobSamples/{jobId}", method=RequestMethod.GET)
  public String listJobSamples(@PathVariable("jobId") Integer jobId, ModelMap m) {
    
	  if(jobId == null || jobId == 0){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";		  
	  }
	  Job job = jobService.getJobByJobId(jobId);
	  if(job==null || job.getJobId()==null || job.getJobId().intValue()==0){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  Map<String, String> extraJobDetailsMap = new HashMap<String, String>();
	  //get some additional job info:
	  List<JobResourcecategory> jobResourceCategoryList = job.getJobResourcecategory();
	  for(JobResourcecategory jrc : jobResourceCategoryList){
		  if(jrc.getResourceCategory().getTypeResource().getIName().equals("mps")){
			  extraJobDetailsMap.put("resource", jrc.getResourceCategory().getName());
			  break;
		  }
	  }
	  for(JobMeta jobMeta : job.getJobMeta()){
		  if(jobMeta.getK().indexOf("readLength") != -1){
			  extraJobDetailsMap.put("readLength", jobMeta.getV());
		  }
		  if(jobMeta.getK().indexOf("readType") != -1){
			  extraJobDetailsMap.put("readType", jobMeta.getV());
		  }
	  }
	  
	  
	  //For a list of the samples initially submitted to a job, pull from table jobcell and exclude duplicates
	  //(table jobsample is not appropriate, as it will contain libraries made by the facility from submitted macromolecules
	  Set<Sample> samples = new HashSet<Sample>();//use this to store a set of unique samples submitted by the user for a specific job; use treeset as it is ordered (by what I don't know)
	  Map filterJobCell = new HashMap();
	  filterJobCell.put("jobId", job.getJobId());
	  List<JobCell> jobCells = jobCellService.findByMap(filterJobCell);
	  for(JobCell jobCell : jobCells){
		  List<SampleCell> sampleCells = jobCell.getSampleCell();
		  for(SampleCell sampleCell : sampleCells){
			   samples.add(sampleCell.getSample());
		  }
	  }
	  
	  List<Sample> userSampleList = new ArrayList<Sample>();
	  List<String> receivedList = new ArrayList<String>();
	  List<Integer> librariesPerSampleList = new ArrayList<Integer>();
	  
	  List<Sample> userSuppliedMacromoleculeList = new ArrayList<Sample>();
	  List<Sample> userSuppliedLibraryList = new ArrayList<Sample>();
	  List<String> sampleMacroReceivedList = new ArrayList<String>();
	  List<String> sampleLibReceivedList = new ArrayList<String>();
	  for(Sample sample : samples){
		  
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
				else if(ss.getState().getStatus().equals("ABANDONED")){
					sampleReceived = "WITHDRAWN";
				}
			}
	  	  }
		  		  
		  if(sample.getTypeSample().getIName().equals("rna") || sample.getTypeSample().getIName().equals("dna")){
			userSuppliedMacromoleculeList.add(sample);
			sampleMacroReceivedList.add(sampleReceived);
			//are there any libraries for this sample?
			List<SampleSource> librariesForThisSample = sample.getSampleSourceViaSourceSampleId();
			numberLibrariesForThisSample = librariesForThisSample.size();
		  }
		  else if(sample.getTypeSample().getIName().equals("library")){
			  userSuppliedLibraryList.add(sample);
			  sampleLibReceivedList.add(sampleReceived);
			  numberLibrariesForThisSample++;
		  }
		  userSampleList.add(sample);
		  receivedList.add(sampleReceived);
		  librariesPerSampleList.add(new Integer(numberLibrariesForThisSample));
	  }
	
	  List<Adaptor> allAdaptors = adaptorService.findAll();
	  Map adaptorList = new HashMap();
	  for(Adaptor adaptor : allAdaptors){
		  adaptorList.put(adaptor.getAdaptorId().toString(), adaptor);
	  }
	  List<Adaptorset> adaptorsetList = adaptorsetService.findAll();
	  
	  /*
	  Integer jobSubmitterId = job.getUserId();
	  for(JobSample jobSample : job.getJobSample()){
		  if(jobSample.getSample().getTypeSample().getTypeSampleId()==1 || jobSample.getSample().getTypeSample().getTypeSampleId()==2){
			  userSuppliedMacromoleculeList.add(jobSample.getSample());
		  }
		  else if(jobSample.getSample().getTypeSample().getTypeSampleId()==3){
			  userSuppliedLibraryList.add(jobSample.getSample());
		  }
	  }
	  */
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);
	  m.addAttribute("adaptorsets", adaptorsetList);
	  m.addAttribute("adaptors", adaptorList);
	  m.addAttribute("samples", userSampleList);
	  m.addAttribute("received", receivedList);
	  m.addAttribute("librariespersample", librariesPerSampleList);
	  
	  m.addAttribute("sampleMacroReceivedList", sampleMacroReceivedList);
	  m.addAttribute("sampleLibReceivedList", sampleLibReceivedList);
	  m.addAttribute("job", job);
	  m.addAttribute("userSuppliedMacromoleculeList", userSuppliedMacromoleculeList);
	  m.addAttribute("userSuppliedLibraryList", userSuppliedLibraryList);
	  //m.put("libraries", libraries);

	  /*
	  Sample sample = sampleService.getSampleBySampleId(sampleId); 

    MetaHelper metaHelper = getMetaHelper();
    List<SampleMeta> coreSampleMeta = metaHelper.syncWithMaster(sample.getSampleMeta());

    metaHelper.setArea("sampleDnaToLibrary");

    List<SampleMeta> sampleDnaLToLibrarySampleMeta = metaHelper.syncWithMaster(sample.getSampleMeta());
    
    m.put("sample", sample); 
    m.put("sampleId", sampleId); 
    m.put("coreMeta", coreSampleMeta); 
    m.put("sampleDnaToLibSampleMeta", sampleDnaLToLibrarySampleMeta); 

    m.put("area", metaHelper.getArea());
    m.put("parentarea", metaHelper.getParentArea());
  */

    return "sampleDnaToLibrary/listJobSamples";
  }

  @RequestMapping(value = "/updateLibrary/{sampleId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateLibrary(@PathVariable("sampleId") Integer sampleId,
		/*	@Valid User userForm, BindingResult result, SessionStatus status, */
			ModelMap m) {
	  
	  Sample sample= sampleService.getSampleBySampleId(sampleId);
	  List<SampleMeta> sampleMeta = sample.getSampleMeta();//mySample.getMetaList();
	  MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
	  sampleMetaHelper.setArea("genericLibrary");
	  List<SampleMeta> normalizedSampleMeta = sampleMetaHelper.syncWithMaster(sampleMeta);
	  
	  sampleMetaHelper.setArea("genericBiomolecule");
	  normalizedSampleMeta.addAll(sampleMetaHelper.syncWithMaster(sampleMeta));
	  
	  
	  sample.setSampleMeta(normalizedSampleMeta);
	  
	  
	  
	  m.put("sample", sample); 
	  
	  //return "redirect:/dashboard.do";
	  return "sampleDnaToLibrary/updateLibrary";
  }
  
  @RequestMapping(value = "/createLibraryFromMacro", method = RequestMethod.GET)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createLibrary(@RequestParam("macromolSampleId") Integer macromolSampleId,
			@RequestParam("adaptorsetId") Integer adaptorsetId,//this is the selectedAdaptorSet's Id
			@RequestParam("jobId") Integer jobId,
			ModelMap m) {
	  
	    if( jobId == 0 || jobId == null ){
			waspErrorMessage("sampleDetail.updated.unexpectedError");
			return "redirect:/dashboard.do";
		}
		else if (macromolSampleId == 0 || macromolSampleId == null || adaptorsetId == 0 || adaptorsetId == null){	//waspErrorMessage("user.updated.error");
			waspErrorMessage("sampleDetail.updated.unexpectedError");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
	  
  		Job job = jobService.getJobByJobId(jobId);
  		if(job.getJobId()==null || job.getJobId().intValue()==0){//not found in database
  			waspErrorMessage("sampleDetail.updated.unexpectedError");
			return "redirect:/dashboard.do";
		}
  		Sample macromoleculeSample = sampleService.getSampleBySampleId(macromolSampleId);
		if(macromoleculeSample.getSampleId()==null || macromoleculeSample.getSampleId().intValue()==0){//not found in database
			waspErrorMessage("sampleDetail.updated.unexpectedError");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		//confirm these two objects exist and part of same job
		JobSample jobSample = jobSampleService.getJobSampleByJobIdSampleId(jobId, macromolSampleId);
		if(jobSample.getJobSampleId()== null || jobSample.getJobSampleId().intValue()==0){
			waspErrorMessage("sampleDetail.updated.unexpectedError");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}

		m.put("macromoleculeSample", macromoleculeSample);
		m.put("job", job);

		Adaptorset selectedAdaptorset = adaptorsetService.getAdaptorsetByAdaptorsetId(adaptorsetId);
		m.put("adaptorsets", adaptorsetService.findAll()); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		m.put("adaptors", selectedAdaptorset.getAdaptor()); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		List<Adaptorset> otherAdaptorsets = adaptorsetService.findAll();//should really filter this by the machine requested
		otherAdaptorsets.remove(selectedAdaptorset);//remove this one
		m.put("otherAdaptorsets", otherAdaptorsets); 
		  
		//prepare empty library
		Sample library = new Sample();
		Map visibilityElementMap = new HashMap(); // specify meta elements that are to be made immutable or hidden in here
		visibilityElementMap.put("adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
		MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
		sampleMetaHelper.setArea("genericLibrary");	//only should need this, as we're creating a new library from a DNA or RNA at the facility, but I suppose there could be an assay-specific set of metadat, but for now leave this	  
		sampleMetaHelper.getMasterList(visibilityElementMap, SampleMeta.class); // pass in visibilityElementMap here to apply our specifications
		try {
			sampleMetaHelper.setMetaValueByName("adaptorset", selectedAdaptorset.getAdaptorsetId().toString());
		} catch (MetadataException e) {
			logger.warn("Cannot set value on 'adaptorset': " + e.getMessage() );
		}
		library.setSampleMeta((List<SampleMeta>) sampleMetaHelper.getMetaList());
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
	  
	  if( jobId == 0 || jobId == null || macromolSampleId == 0 || macromolSampleId == null ){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  Job jobForThisSample = jobService.getJobByJobId(jobId);
	  if(jobForThisSample.getJobId()==null || jobForThisSample.getJobId().intValue()==0){//not found in database
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }

	  Sample parentMacromolecule = sampleService.getSampleBySampleId(macromolSampleId);
	  if(parentMacromolecule.getSampleId()==null || parentMacromolecule.getSampleId().intValue()==0){//not found in database
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  //confirm the job and the macromoleculeSample are part of same job
	  JobSample jobSample = jobSampleService.getJobSampleByJobIdSampleId(jobId, macromolSampleId);
	  if(jobSample.getJobSampleId()== null || jobSample.getJobSampleId().intValue()==0){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }

	  boolean nameEmpty = false;
	  boolean nameClash = false;
		
	  String newLibraryName = libraryForm.getName().trim();//from the form
	  //confirm sample.name (new library's name) not empty 
	  if("".equals(newLibraryName)){
		  nameEmpty = true;
	  }
	  //confirm that this new library's name is different from all other sample.name in this job
	  //this could probably be an else to previous if
	  List<Sample> samplesInThisJob = jobForThisSample.getSample();
	  for(Sample eachSampleInThisJob : samplesInThisJob){
		  if( newLibraryName.equals(eachSampleInThisJob.getName()) ){
			  nameClash = true;
			  newLibraryName = "";
			  break;
		  }
	  }
	  Map visibilityElementMap = new HashMap(); // specify meta elements that are to be made immutable or hidden in here
	  visibilityElementMap.put("adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
	  MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
	  List<SampleMeta> sampleMetaListFromForm = new ArrayList<SampleMeta>();
	  sampleMetaHelper.setArea("genericLibrary");
	  sampleMetaListFromForm.addAll(sampleMetaHelper.getFromRequest(request, visibilityElementMap, SampleMeta.class));
	  
	  //check of errors in the metadat
	  getMetaHelperWebapp().validate(sampleMetaListFromForm, result);
		
	  if (result.hasErrors() || nameEmpty || nameClash ) {
		  
		  prepareSelectListData(m);//doubt that this is required here; really only needed for meta relating to country or state
		  
		  if(nameClash){
			  waspErrorMessage("sampleDetail.updated.nameClashError");
		  }
		  else{
			  waspErrorMessage("sampleDetail.updated.error");
		  }
		  
		  
		  Adaptorset selectedAdaptorset = adaptorsetService.getAdaptorsetByAdaptorsetId(adaptorsetId);
		  m.put("adaptorsets", adaptorsetService.findAll()); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		  m.put("adaptors", selectedAdaptorset.getAdaptor()); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		  List<Adaptorset> otherAdaptorsets = adaptorsetService.findAll();//should really filter this by the machine requested
		  otherAdaptorsets.remove(selectedAdaptorset);//remove this one
		  m.put("otherAdaptorsets", otherAdaptorsets); 

		  m.put("macromoleculeSample", parentMacromolecule);
		  m.put("job", jobForThisSample);
		  libraryForm.setName(newLibraryName);
		  libraryForm.setSampleMeta(sampleMetaListFromForm);
		  m.put("sample", libraryForm); 		  

		  return "sampleDnaToLibrary/createLibrary";
	  }

	  //all OK so create/save new library
	  Sample newLibrary = new Sample();
	  newLibrary.setName(newLibraryName);	
	  //newLibrary.setSampleMeta(sampleMetaListFromForm);//this will not be saved simply by saving newLibrary; use sampleMetaService.updateBySampleId below
	  TypeSample typeSample = typeSampleService.getTypeSampleByIName("library");
	  newLibrary.setTypeSample(typeSample);
	  Map filterMap = new HashMap();
	  filterMap.put("typeSampleId", typeSample.getTypeSampleId());//restrict search to typeSample is library
	  List<SubtypeSample> subtypeSampleList = subtypeSampleService.findByMap(filterMap);
	  String workflowName = jobForThisSample.getWorkflow().getIName().toLowerCase();//such as chipseq
	  for(SubtypeSample sts : subtypeSampleList){
		  if( sts.getIName().toLowerCase().indexOf(workflowName) > -1 ){
			  newLibrary.setSubtypeSample(sts); 
			  break;
		  }
	  }
	  if(newLibrary.getSubtypeSample() == null || newLibrary.getSubtypeSample().getSubtypeSampleId()==0){//no match found in database
		  //error
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  newLibrary.setSubmitterLabId(parentMacromolecule.getSubmitterLabId());//needed??	  
	  newLibrary.setSubmitterUserId(parentMacromolecule.getSubmitterUserId());//needed??	  
	  newLibrary.setSubmitterJobId(parentMacromolecule.getSubmitterJobId());//needed??	
	  newLibrary.setIsActive(new Integer(1));
	  newLibrary.setLastUpdTs(new Date());	  
	  newLibrary = sampleService.save(newLibrary);
	  
	  sampleMetaService.updateBySampleId(newLibrary.getSampleId(), sampleMetaListFromForm);
	  
	  //add entry to jobsample table to link new library to job
	  JobSample newJobSample = new JobSample();
	  newJobSample.setJob(jobForThisSample);
	  newJobSample.setSample(newLibrary);
	  newJobSample = jobSampleService.save(newJobSample);
	  
	  //add entry to sample source to link new library to the macromolecule from which it was derived
	  SampleSource sampleSource = new SampleSource();
	  sampleSource.setSample(newLibrary);
	  sampleSource.setSampleViaSource(parentMacromolecule);
	  
	  //find max samplesource.multiplexindex for this macromolecule
	  int maxindex = 0;
	  Map filterMap2 = new HashMap();
	  filterMap2.put("sourceSampleId", parentMacromolecule.getSampleId());
	  List<SampleSource> libFromThisMacromoleculeList = sampleSourceService.findByMap(filterMap2);
	  for(SampleSource ss : libFromThisMacromoleculeList){
		  if(ss.getMultiplexindex().intValue() > maxindex){
			  maxindex = ss.getMultiplexindex().intValue();
		  }
	  }
	  maxindex++;
	  sampleSource.setMultiplexindex(new Integer(maxindex));
	  sampleSource.setLastUpdTs(new Date());	
	  sampleSource = sampleSourceService.save(sampleSource);
	  
	  //TODO record state change
	  Task task = taskService.getTaskByIName("Create Library");
	  Map filterMap3 = new HashMap();
	  filterMap3.put("taskId", task.getTaskId());
	  filterMap3.put("status", "CREATED");
	  List<State> stateList = stateService.findByMap(filterMap3);
	  for(State state : stateList){
		  List <Statesample> statesampleList = state.getStatesample();
		  for(Statesample statesample : statesampleList){
			  if(statesample.getSampleId().intValue() == parentMacromolecule.getSampleId().intValue()){
				  state.setStatus("COMPLETED");
			  }
		  }
	  }
	  
	  
	  waspMessage("libraryCreated.created.success");
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";

	}
  
 
  
  
  @RequestMapping(value = "/librarydetail_ro/{jobId}/{libraryId}", method = RequestMethod.GET)//sampleId represents an existing library (at this moment both user supplied or facility created)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String libraryDetailRO(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) {
	  return libraryDetail(jobId, libraryId, m, false);
  }
  @RequestMapping(value = "/librarydetail_rw/{jobId}/{libraryId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String libraryDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) {
	  return libraryDetail(jobId, libraryId, m, true);
  }
  
  public String libraryDetail(Integer jobId, Integer libraryId, ModelMap m, boolean isRW){

	    if( jobId == 0 || jobId == null ){
			waspErrorMessage("sampleDetail.updated.unexpectedError");System.out.println("ROB: error 1");
			return "redirect:/dashboard.do";
		}
		else if (libraryId == 0 || libraryId == null){	
			waspErrorMessage("sampleDetail.updated.unexpectedError");System.out.println("ROB: error 2");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
	  
  		Job job = jobService.getJobByJobId(jobId);
  		if(job.getJobId()==null || job.getJobId().intValue()==0){//not found in database
  			waspErrorMessage("sampleDetail.updated.unexpectedError");System.out.println("ROB: error 3");
			return "redirect:/dashboard.do";
		}
  		TypeSample typeSampleLibrary = typeSampleService.getTypeSampleByIName("library");
  		Sample library = sampleService.getSampleBySampleId(libraryId);
  		if(library.getSampleId()==null || library.getSampleId().intValue()==0 || ! "library".equals(library.getTypeSample().getIName())){//not found in database or not a library
  			waspErrorMessage("sampleDetail.updated.unexpectedError");System.out.println("ROB: error 4");
  			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
  		//pull out adaptor
  		Adaptor adaptor = null;
  		MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
  		sampleMetaHelper.setArea("genericLibrary");
  		sampleMetaHelper.syncWithMaster(library.getSampleMeta());
  		library.setSampleMeta((List<SampleMeta>)sampleMetaHelper.getMetaList()); // synchronized with uifields
  		try{
  			adaptor = adaptorService.getAdaptorByAdaptorId(Integer.valueOf( sampleMetaHelper.getMetaByName("adaptor").getV()) );
  		} catch(MetadataException e){
  			logger.warn("Cannot get metadata for'adaptor' : " + e.getMessage());
  		} catch(NumberFormatException e){
  			logger.warn("Cannot convert to numeric value for metadata for'adaptor' " + e.getMessage());
  		}
  		
  		
  		//is library user-submitted or facility-generated?
  		boolean libraryIsUserSubmitted = false;
  		Sample parentMacromolecule = null;//if this remains null then this library is user-generated 
  		List<SampleSource> sampleSource = library.getSampleSource();//if library is facility-generated, there should be one row; if user-submitted library then no rows 
  		if(sampleSource.size() > 1 || sampleSource.size() < 0){
  			waspErrorMessage("sampleDetail.updated.unexpectedError");System.out.println("ROB: error 5");
  			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
  		}
  		else if(sampleSource.size() == 1){//facility generated library
  			System.out.println("ROB: there is one sampleSource");
  			parentMacromolecule = sampleSource.get(0).getSampleViaSource(); 
  			System.out.println("ROB: there is one sampleSource with name/id of " + parentMacromolecule.getName() + " / " + parentMacromolecule.getSampleId().intValue());
  			if(parentMacromolecule.getSampleId()==null || parentMacromolecule.getSampleId()==0){
  	  			waspErrorMessage("sampleDetail.updated.unexpectedError");System.out.println("ROB: error 6");
  	  			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
  			}
  		}
  		
  		//confirm these two objects exist and part of same job
		JobSample jobSample = jobSampleService.getJobSampleByJobIdSampleId(jobId, libraryId);
		if(jobSample.getJobSampleId()== null || jobSample.getJobSampleId().intValue()==0){
			waspErrorMessage("sampleDetail.updated.unexpectedError");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		Adaptorset selectedAdaptorset = adaptorsetService.getAdaptorsetByAdaptorsetId(adaptor.getAdaptorsetId());
		m.put("adaptorsets", adaptorsetService.findAll()); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		m.put("adaptors", selectedAdaptorset.getAdaptor()); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		m.put("job", job);
		m.put("parentMacromolecule", parentMacromolecule);
		m.put("library", library);
		m.put("adaptor", adaptor);
		
		return isRW? "sampleDnaToLibrary/librarydetail_rw":"sampleDnaToLibrary/librarydetail_ro";
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
	  
	  if( jobId == 0 || jobId == null || sampleId == 0 || sampleId == null){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  Job job = jobService.getJobByJobId(jobId);
	  if(job.getJobId()==null || job.getJobId().intValue()==0){//not found in database
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  Sample sample= sampleService.getSampleBySampleId(sampleId);
	  if(sample.getSampleId()==null || sample.getSampleId().intValue()==0){//not found in database
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  
	  //confirm these two objects exist and part of same job
	  JobSample jobSample = jobSampleService.getJobSampleByJobIdSampleId(jobId, sampleId);
	  if(jobSample.getJobSampleId()== null || jobSample.getJobSampleId().intValue()==0){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  
	  List<String> areaList = new ArrayList<String>();
	  Workflow workflow = job.getWorkflow();
	  List<Workflowsubtypesample> wfssList = workflow.getWorkflowsubtypesample();
	  for(Workflowsubtypesample wfss: wfssList){
		  if(wfss.getSubtypeSample().getTypeSampleId().intValue() == sample.getTypeSampleId().intValue()){
			  String[] items = wfss.getSubtypeSample().getAreaList().split(",");
			  for(String item : items){
				  areaList.add(item);
			  }			  
		  }
	  }
	  
	  List<SampleMeta> sampleMeta = sample.getSampleMeta();
	  MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
	  List<SampleMeta> normalizedSampleMeta = new ArrayList<SampleMeta>();
	  for(String area : areaList){
		  sampleMetaHelper.setArea(area);
		  normalizedSampleMeta.addAll(sampleMetaHelper.syncWithMaster(sampleMeta));
	  }
	  sample.setSampleMeta(normalizedSampleMeta);
	  
	  m.put("job", job);
	  m.put("sample", sample); 
	  //m.put("areaList", areaList);//appears that this is really not used in the jsp.
	  
	  return isRW?"sampleDnaToLibrary/sampledetail_rw":"sampleDnaToLibrary/sampledetail_ro";
	  //return "redirect:/dashboard.do";
  }
  @RequestMapping(value = "/sampledetail_rw/{jobId}/{sampleId}", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateSampleDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, @RequestParam("typeSampleId") Integer typeSampleId, 
								@Valid Sample sampleForm, BindingResult result, 
								SessionStatus status, ModelMap m) throws MetadataException {
		  
	  if( jobId == 0 || jobId == null || sampleId == 0 || sampleId == null){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
		
	  Job jobForThisSample = jobService.getJobByJobId(jobId);
	  if(jobForThisSample.getJobId()==null || jobForThisSample.getJobId().intValue()==0){//not found in database
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  //return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  } 
		  
	  Sample sampleToSave = sampleService.getSampleBySampleId(sampleId); 
	  if(sampleToSave.getSampleId()==null || sampleToSave.getSampleId().intValue()==0){//not found in database
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	
	  //confirm macromoleculeSample is actually part of this job
	  JobSample jobSample = jobSampleService.getJobSampleByJobIdSampleId(jobId, sampleId);
	  if(jobSample.getJobSampleId()== null || jobSample.getJobSampleId().intValue()==0){
		  waspErrorMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  } 
	  
	  boolean nameEmpty = false;
	  boolean nameClash = false;
		
	  String newSampleName = sampleForm.getName().trim();//from the form
	  //confirm sample.name not empty 
	  if("".equals(newSampleName)){
		  nameEmpty = true;
	  }
	  //confirm that, if a new sample.name was supplied on the form, it is different from all other sample.name in this job
	  if( ! newSampleName.equals(sampleToSave.getName().trim() ) ){//name was changed
		  List<Sample> samplesInThisJob = jobForThisSample.getSample();
		  for(Sample eachSampleInThisJob : samplesInThisJob){
			  if(eachSampleInThisJob.getSampleId().intValue() != sampleId.intValue()){
				  if( newSampleName.equals(eachSampleInThisJob.getName()) ){
					  nameClash = true;
					  newSampleName = "";
					  break;
				  }
			  }
		  }
	  }
		
	  //get list of metadata areas for a sample of this job's workflow; it's in subtypesample.arealist (example: genericBiomolecule,chipseqDna,genericLibrary)
	  //likely candidate for a method somewhere
	  List<String> areaList = new ArrayList<String>();
	  Workflow workflow = jobService.getJobByJobId(jobId).getWorkflow(); 
	  List<Workflowsubtypesample> wfssList = workflow.getWorkflowsubtypesample();
	  for(Workflowsubtypesample wfss: wfssList){
		  if(wfss.getSubtypeSample().getTypeSampleId().intValue() == sampleToSave.getTypeSampleId().intValue()){
			  String[] items = wfss.getSubtypeSample().getAreaList().split(",");
			  for(String item : items){
				  areaList.add(item);
			  }			  
		  }
	  }
		  
	  MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
	  List<SampleMeta> sampleMetaListFromForm = new ArrayList<SampleMeta>();
	  for(String area : areaList){
		  sampleMetaHelper.setArea(area);
		  sampleMetaListFromForm.addAll(sampleMetaHelper.getFromRequest(request, SampleMeta.class));
	  }

	  //check of errors in the metadat 
	  getMetaHelperWebapp().validate(sampleMetaListFromForm, result);
		
	  if (result.hasErrors() || nameEmpty || nameClash ) {
		  
		  prepareSelectListData(m);//doubt that this is required here; really only needed for meta relating to country or state
		  
		  if(nameClash){
			  waspErrorMessage("sampleDetail.updated.nameClashError");
		  }
		  else{
			  waspErrorMessage("sampleDetail.updated.error");
		  }
		  m.put("job", jobForThisSample);
		  sampleForm.setSampleId(sampleId);
		  sampleForm.setName(newSampleName);
		  sampleForm.setTypeSample(typeSampleService.getTypeSampleByTypeSampleId(typeSampleId));
		  sampleForm.setSampleMeta(sampleMetaListFromForm);
		  m.put("sample", sampleForm);
		  return "sampleDnaToLibrary/sampledetail_rw";
	  }

	  //workup the metafields (don't need this explicit method; just use this.sampleMetaService.updateBySampleId(sampleId, sampleMetaListFromForm); below
/*	  for (SampleMeta metaFromForm : sampleMetaListFromForm) {
		  //first, check to see if this metafield on the form is already a metafield in the sample
		  boolean foundIt = false;
		  for(SampleMeta metaFromSample : sampleToSave.getSampleMeta()){
			  if(metaFromForm.getK().equals(metaFromSample.getK())){
				  foundIt = true;
				  if( ! metaFromForm.getV().equals(metaFromSample.getV()) ){//if the v value has changed, then update it's new value in the sample object
						metaFromSample.setV(metaFromForm.getV());//however if a new metafield is on the form, it won't be added back to the sample
				  }
			  }
		  }
		  if( ( ! foundIt ) && ( ! "".equals(metaFromForm.getV()) ) ){//if this is a new metafield that is on the form and not in the sample and the user has provided a value, then add it
			  SampleMeta metaToAdd = new SampleMeta();
			  metaToAdd.setSampleId(sampleId);
			  metaToAdd.setK(metaFromForm.getK());
			  metaToAdd.setV(metaFromForm.getV());
			  metaToAdd = sampleMetaService.save(metaToAdd);
			  sampleToSave.getSampleMeta().add(metaToAdd);//don't believe this line is actually required
		  }
	  }
*/
	  sampleToSave.setName(newSampleName);
	  sampleToSave.setLastUpdTs(new Date());
	  
	  this.sampleService.merge(sampleToSave);//can you do: sampleToSave.setSampleMeta(sampleMetaListFromForm); and just save the sample (and omit next line)?
	  this.sampleMetaService.updateBySampleId(sampleId, sampleMetaListFromForm);
	  
	  waspMessage("sampleDetail.updated.success");
	  return "redirect:/sampleDnaToLibrary/sampledetail_rw/" + jobId + "/" + sampleId + ".do";

  }
  
  
}
