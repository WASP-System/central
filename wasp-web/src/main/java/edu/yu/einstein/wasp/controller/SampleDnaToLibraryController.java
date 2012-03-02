package edu.yu.einstein.wasp.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
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
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleCell;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.SubtypeSample;
import edu.yu.einstein.wasp.model.TypeSample;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.UserMeta;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.Workflowsubtypesample;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AdaptorsetService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.SubtypeSampleService;
import edu.yu.einstein.wasp.service.TypeSampleService;
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
    
	  Job job = jobService.getJobByJobId(jobId);
	  if(job==null || job.getJobId()==null || job.getJobId().intValue()==0){
		  //TODO waspMessage and return
	  }
	  //For a list of the samples initially submitted to a job, pull from table jobcell and exclude duplicates
	  //(table jobsample is not appropriate, as it will contain libraries made by the facility from submitted macromolecules
	  Set<Sample> samples = new HashSet<Sample>();//use this to store a set of unique samples submitted by the user for a specific job
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
			@RequestParam("adaptorsetId") Integer adaptorsetId,
			@RequestParam("jobId") Integer jobId,
			ModelMap m) {
	  
		if( jobId == 0 || jobId == null ){
			//need error
			return "redirect:/dashboard.do";
		}
		else if (macromolSampleId == 0 || macromolSampleId == null || adaptorsetId == 0 || adaptorsetId == null){	//waspMessage("user.updated.error");
			//need error
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
	  
  		Job job = jobService.getJobByJobId(jobId);
  		if(job.getJobId()==null || job.getJobId().intValue()==0){//not found in database
			//waspMessage("user.updated.error");
			return "redirect:/dashboard.do";
		}
  		Sample macromoleculeSample = sampleService.getSampleBySampleId(macromolSampleId);
		if(macromoleculeSample.getSampleId()==null || macromoleculeSample.getSampleId().intValue()==0){//not found in database
			//waspMessage("user.updated.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		
		m.put("macromoleculeSample", macromoleculeSample);
		m.put("job", job);
		  
		//now let's work on the library
		Sample library = new Sample();
	  
		MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
		sampleMetaHelper.setArea("genericLibrary");	//I suppose there could be an assay-specific set of metadat, but for now leave this	  
		library.setSampleMeta(sampleMetaHelper.getMasterList(SampleMeta.class));
		  
		m.put("library", library); 
		
		//Adaptorset adaptorsetList = adaptorsetService.
		Adaptorset selectedAdaptorset = adaptorsetService.getAdaptorsetByAdaptorsetId(adaptorsetId);
		m.put("selectedAdaptorset", selectedAdaptorset);
		List<Adaptorset> otherAdaptorsets = adaptorsetService.findAll();//should really filter this by the machine requested
		otherAdaptorsets.remove(selectedAdaptorset);//remove this one
		m.put("otherAdaptorsets", otherAdaptorsets); 
		
		
		return "sampleDnaToLibrary/createLibrary";
		
		
  		/*
  		
	  String workflowINameLowerCase = job.getWorkflow().getIName().toLowerCase();//at this time, it should be chipseq
	  List<SubtypeSample> subtypeSampleList = subtypeSampleService.findAll();
	  int subtypeSampleIdForLib = 0;
	  for(SubtypeSample sts : subtypeSampleList){//better way is to use the workflowsubtypesampel then use a map
		  
		  if(sts.getIName().toLowerCase().indexOf(workflowINameLowerCase) >= 0 && sts.getIName().toLowerCase().indexOf("librarysample") >= 0){
			  subtypeSampleIdForLib = sts.getSubtypeSampleId().intValue();//should be 2 for chipseq library
		  }
	  }
	  if(subtypeSampleIdForLib == 0){
		  //throw exception
		  ;
	  }
	  
	  Sample macromoleculeSample = sampleService.getSampleBySampleId(sampleId); //this is the macromolecule sample
	  //TODO should confirm that this is really a macromolecule and that it has arrived.
	  Integer typeSampleId = typeSampleService.getTypeSampleByIName("library").getTypeSampleId();
	  //TODO confirm typeSampleId exists
	  Sample librarySample = new Sample();//should be a new empty sample
	  librarySample.setTypeSampleId(typeSampleId);
	  librarySample.setSubtypeSampleId(new Integer(subtypeSampleIdForLib));	  
	  librarySample.setSubmitterLabId(macromoleculeSample.getSubmitterLabId());
	  librarySample.setSubmitterUserId(macromoleculeSample.getSubmitterUserId());
	  librarySample.setJob(jobService.getJobByJobId(jobId));//What is this really for?? We have the jobsample table?
	  
	  List<SampleMeta> sampleMeta = librarySample.getSampleMeta();
	  
	  MetaHelperWebapp sampleMetaHelper = getMetaHelperWebapp(); //new MetaHelperWebapp("sample", SampleMeta.class, request.getSession());
	  sampleMetaHelper.setArea("genericLibrary");
	  List<SampleMeta> normalizedSampleMeta = sampleMetaHelper.getMasterList(SampleMeta.class);
	  librarySample.setSampleMeta(normalizedSampleMeta);

	  m.put("library", librarySample);
	  m.put("macromoleculeSampleId", macromoleculeSample.getSampleId());
	  m.put("jobId", jobId);
	  
	  //return "redirect:/dashboard.do";
	   
	   */
	  //return "sampleDnaToLibrary/createLibrary";
  	}
 
  
  
  
  
  
  
  @RequestMapping(value = "/createLibraryFromMacro", method = RequestMethod.POST)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createLibrary(@RequestParam("macromolSampleId") Integer macromolSampleId,
			@RequestParam("adaptorsetId") Integer adaptorsetId,//needed only to return to sister get method
			@RequestParam("jobId") Integer jobId, 
			@Valid Sample libraryForm, BindingResult result, 
			SessionStatus status, 
			ModelMap m) {
	  
		if( jobId == 0 || jobId == null || macromolSampleId == 0 || macromolSampleId == null ){
			waspMessage("user.updated.error");
			return "redirect:/dashboard.do";
		}
		else if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		
		Job jobForThisSample = jobService.getJobByJobId(jobId);
		if(jobForThisSample.getJobId()==null || jobForThisSample.getJobId().intValue()==0){//not found in database
			waspMessage("user.updated.error");
			return "redirect:/dashboard.do";
		}
		Sample parentMacromolecule = sampleService.getSampleBySampleId(macromolSampleId);
		if(parentMacromolecule.getSampleId()==null || parentMacromolecule.getSampleId().intValue()==0){//not found in database
			waspMessage("user.updated.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		return "redirect:/dashboard.do";
/*	
		//confirm sampleForm.name (the new library's name) is not empty
		if("".equals(libraryForm.getName().trim())){
			waspMessage("user.updated.error");
			return "redirect:/sampleDnaToLibrary/sampledetail_rw/" + jobId + "/" + sampleId + ".do";
		}
		//confirm that sampleForm.name (on the form) has not been changed to a name used by another sample in this job (macromolecule or library)
		List<Sample> samplesInThisJob = jobForThisSample.getSample();
		for(Sample eachSampleInThisJob : samplesInThisJob){
			if(eachSampleInThisJob.getSampleId().intValue() != sampleId.intValue()){
				if( newSampleName.equals(eachSampleInThisJob.getName()) ){
					waspMessage("user.updated.error");
					return "redirect:/sampleDnaToLibrary/sampledetail_rw/" + jobId + "/" + sampleId + ".do";
				}
			}
		}
			
		//apparently sampleForm.name is OK, so re-set it, as if may have been changed on the form:
		sampleToSave.setName(newSampleName);
		
		//get list of metadata areas for a sample of this job's workflow
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

		getMetaHelperWebapp().validate(sampleMetaListFromForm, result);
		if (result.hasErrors()) {
			prepareSelectListData(m);
			waspMessage("user.updated.error");
			return "redirect:/sampleDnaToLibrary/sampledetail_rw/" + jobId + "/" + sampleId + ".do";
		}
		for (SampleMeta metaFromForm : sampleMetaListFromForm) {
			boolean foundIt = false;
			for(SampleMeta metaFromSample : sampleToSave.getSampleMeta()){
				if(metaFromForm.getK().equals(metaFromSample.getK())){
					foundIt = true;
					if( ! metaFromForm.getV().equals(metaFromSample.getV()) ){
						metaFromSample.setV(metaFromForm.getV());//however if a new metafield has been added to the form, it won't be added back to the sample
					}
				}
			}
			if(!foundIt){
				SampleMeta metaToAdd = new SampleMeta();
				metaToAdd.setSampleId(sampleId);
				metaToAdd.setK(metaFromForm.getK());
				metaToAdd.setV(metaFromForm.getV());
				metaToAdd = sampleMetaService.save(metaToAdd);
				sampleToSave.getSampleMeta().add(metaToAdd);//don't believe this line is actually required
			}
		}
		//message
		sampleService.save(sampleToSave); 
		return "redirect:/sampleDnaToLibrary/sampledetail_rw/" + jobId + "/" + sampleId + ".do";
		*/
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
		  waspMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  Job job = jobService.getJobByJobId(jobId);
	  if(job.getJobId()==null || job.getJobId().intValue()==0){//not found in database
		  waspMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  Sample sample= sampleService.getSampleBySampleId(sampleId);
	  if(sample.getSampleId()==null || sample.getSampleId().intValue()==0){//not found in database
		  waspMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  
	  //TODO confirm these two objects exist and part of same job
	  JobSample jobSample = jobSampleService.getJobSampleByJobIdSampleId(jobId, sampleId);
	  if(jobSample.getJobSampleId()== null || jobSample.getJobSampleId().intValue()==0){
		  waspMessage("sampleDetail.updated.unexpectedError");
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
		  
	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  //return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  else if( jobId == 0 || jobId == null || sampleId == 0 || sampleId == null){
		  waspMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
		
	  Job jobForThisSample = jobService.getJobByJobId(jobId);
	  if(jobForThisSample.getJobId()==null || jobForThisSample.getJobId().intValue()==0){//not found in database
		  waspMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/dashboard.do";
	  }
	  
	  Sample sampleToSave = sampleService.getSampleBySampleId(sampleId); 
	  if(sampleToSave.getSampleId()==null || sampleToSave.getSampleId().intValue()==0){//not found in database
		  waspMessage("sampleDetail.updated.unexpectedError");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	
	  JobSample jobSample = jobSampleService.getJobSampleByJobIdSampleId(jobId, sampleId);
	  if(jobSample.getJobSampleId()== null || jobSample.getJobSampleId().intValue()==0){
		  waspMessage("sampleDetail.updated.unexpectedError");
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
			  waspMessage("sampleDetail.updated.nameClashError");
		  }
		  else{
			  waspMessage("sampleDetail.updated.error");
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
	  
	  this.sampleService.merge(sampleToSave);
	  this.sampleMetaService.updateBySampleId(sampleId, sampleMetaListFromForm);
	  
	  waspMessage("sampleDetail.updated.success");
	  return "redirect:/sampleDnaToLibrary/sampledetail_rw/" + jobId + "/" + sampleId + ".do";

  }
  
  
}
