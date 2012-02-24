package edu.yu.einstein.wasp.controller;

import java.util.ArrayList;
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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
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
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AdaptorsetService;
import edu.yu.einstein.wasp.service.SampleMetaService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.JobService;
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

}
