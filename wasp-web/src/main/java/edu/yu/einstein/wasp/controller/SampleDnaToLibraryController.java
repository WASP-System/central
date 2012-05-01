package edu.yu.einstein.wasp.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import edu.yu.einstein.wasp.dao.RunDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSourceDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCell;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.Run;
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
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.util.MetaHelper;


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
  private RunDao runDao;
  @Autowired
  private SampleSourceDao sampleSourceDao;
  @Autowired
  private TaskDao taskDao;
  @Autowired
  private StateDao stateDao;
  @Autowired
  private SampleService sampleService;
  @Autowired
  private JobService jobService;
  

  
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
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
  public String listJobSamples(@PathVariable("jobId") Integer jobId, ModelMap m) {
    
	  //experimental code 3 lines:
	  //List<Sample> list1 = new ArrayList();
	  //List<Sample> list2 = new ArrayList();
	  ///List<ArrayList<Sample>> darray2 = new ArrayList();
	  
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
	  //and it DOES NOT include facility-generated libraries
	  List<Sample> submittedSamples = jobService.getSubmittedSamples(job);
	  //order by sample name
	  sampleService.sortSamplesBySampleName(submittedSamples);
  	  
	  List<String> receivedList = new ArrayList<String>();
	  List<Integer> librariesPerSampleList = new ArrayList<Integer>();//will be used for rowspan on jsp
	  for(Sample sample : submittedSamples){
		  
		  String sampleReceived = sampleService.getReceiveSampleStatus(sample);
		  receivedList.add(sampleService.convertReceiveSampleStatusForWeb(sampleReceived));
		  
		  int numberLibrariesForThisSample = 0;
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
	  
		//attempt at getting the requested coverage in a better format:
		int totalNumberCellsRequested = jobCellList.size();
		Map<Sample, String> coverageMap = new LinkedHashMap<Sample, String>();
		for(Sample sample : submittedSamples){
			StringBuffer stringBuffer = new StringBuffer("");
			for(int i = 1; i <= totalNumberCellsRequested; i++){
				boolean found = false;
				for(JobCell jobCell : jobCellList){
					List<SampleCell> sampleCellList = jobCell.getSampleCell();
					for(SampleCell sampleCell : sampleCellList){
						if(sampleCell.getSampleId().intValue() == sample.getSampleId().intValue()){
							if(jobCell.getCellindex().intValue() == i){
								//System.out.print(i + " ");
								stringBuffer.append("1");
								found = true;
							}
						}
					}
				}
				if(found == false){
					stringBuffer.append("0");
				}
			}
			coverageMap.put(sample, new String(stringBuffer));
  		}	
		
		m.addAttribute("coverageMap", coverageMap);
		m.addAttribute("totalNumberCellsRequested", totalNumberCellsRequested);
		
		m.addAttribute("jobCellList", jobCellList);
		m.addAttribute("flowCells", flowCells);
		m.addAttribute("samplesSubmitted", submittedSamples);
		m.addAttribute("received", receivedList);
		m.addAttribute("librariespersample", librariesPerSampleList);

		///////////
		
		List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
		List<Sample> macromoleculeSubmittedSamplesList = new ArrayList<Sample>();
		List<Sample> librarySubmittedSamplesList = new ArrayList<Sample>();
		Map<Sample, String> speciesMap = new HashMap<Sample, String>();
		Map<Sample, String> receivedStatusMap = new HashMap<Sample, String>();
		Map<Sample, List<Sample>> facilityLibraryMap = new HashMap<Sample, List<Sample>>();//key is a submitted sample (macromolecule) and value is list of facility-generated libraries created from that macromolecule)
		Map<Sample, List<Sample>> flowCellMap = new HashMap<Sample, List<Sample>>();//key is a library and value is list (really a set) of (unique) flow cells that library is on
		Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();//key is library and value is the adaptor used for that library
		Map<Sample, List<Run>> flowCellRunMap = new HashMap<Sample, List<Run>>();//key is flowcell and value is list of runs that the flow cell is on (should be one, but...)
		
		Map<Sample, List<Sample>> cellMap = new HashMap<Sample, List<Sample>>();//key is a library and value is list of cells that library is on
		Map<Sample, List<Sample>> platformUnitMap = new HashMap<Sample, List<Sample>>();//key is a cell and value is list of platformUnits of which that cell is a part (its really a single Platform Unit)
		Map<Sample, List<Run>> platformUnitRunMap = new HashMap<Sample, List<Run>>();//key is platformUnit and value is list of runs that the platformUnit is on (should be one, but...)

		
		
		SampleType macromoleculeDnaType = sampleTypeDao.getSampleTypeByIName("dna");
		SampleType macromoleculeRnaType = sampleTypeDao.getSampleTypeByIName("rna");
		SampleType libraryType = sampleTypeDao.getSampleTypeByIName("library");
		if(macromoleculeDnaType==null || macromoleculeRnaType==null || libraryType == null || 
				macromoleculeDnaType.getSampleTypeId().intValue()==0 || 
				macromoleculeRnaType.getSampleTypeId().intValue()==0 || 
				libraryType.getSampleTypeId().intValue()==0){
			//error message and get outta here
		}
		for(Sample sample : submittedSamplesList){
			if(sample.getSampleType().getIName().equals(macromoleculeDnaType.getIName()) || sample.getSampleType().getIName().equals(macromoleculeRnaType.getIName())){
				macromoleculeSubmittedSamplesList.add(sample);
				List<Sample> facilityGeneratedLibrariesList = sampleService.getFacilityGeneratedLibraries(sample);//get list of facility-generated libraries from a user-submitted macromoleucle
				facilityLibraryMap.put(sample, facilityGeneratedLibrariesList);				
			}
			else if(sample.getSampleType().getIName().equals(libraryType.getIName())){
				librarySubmittedSamplesList.add(sample);
				//for each library get flowcells/runs
			}
			try{		
				speciesMap.put(sample, MetaHelper.getMetaValue("genericBiomolecule", "species", sample.getSampleMeta()));
			}
			catch(MetadataException me){
				speciesMap.put(sample, "Species Not Found");
				logger.warn("Unable to identify species for sampleId " + sample.getSampleId());
				//print a message and get outta here
			}
			receivedStatusMap.put(sample, sampleService.convertReceiveSampleStatusForWeb(sampleService.getReceiveSampleStatus(sample)));
		}
		sampleService.sortSamplesBySampleName(macromoleculeSubmittedSamplesList);
		sampleService.sortSamplesBySampleName(librarySubmittedSamplesList);
		
		//for each library (those in facilityLibraryMap and those in librarySubmittedSamplesList) get flowcells/runs and add to map Map<Sample, List<Sample>>
		for(Sample library : librarySubmittedSamplesList){
			List<Sample> flowCellList = sampleService.getFlowCellsThatThisLibraryIsOn(library);
			flowCellMap.put(library, flowCellList);
			Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			if(adaptor==null){
				//message and get out of here
			}
			libraryAdaptorMap.put(library, adaptor);	
			List<Sample> cellList = sampleService.getCellsThatThisLibraryIsOn(library);
			cellMap.put(library, cellList);
		}
		for(List<Sample> libraryList : facilityLibraryMap.values()){
			for(Sample library : libraryList){
				List<Sample> flowCellList = sampleService.getFlowCellsThatThisLibraryIsOn(library);
				flowCellMap.put(library, flowCellList);
				Adaptor adaptor = sampleService.getLibraryAdaptor(library);
				if(adaptor==null){
					//message and get out of here
				}
				libraryAdaptorMap.put(library, adaptor);
				List<Sample> cellList = sampleService.getCellsThatThisLibraryIsOn(library);
				cellMap.put(library, cellList);
			}
		}
		
		for(List<Sample> cellList : cellMap.values()){
			for(Sample cell : cellList){
				List<Sample> platformUnitList = sampleService.getCellsThatThisLibraryIsOn(cell);//it's ok to use this method here
				platformUnitMap.put(cell, platformUnitList);
			}
		}
		
		//for all the flowcells that we've identified, which are on a sequence run
		for(List<Sample> flowCellList : flowCellMap.values()){
			for(Sample flowCell : flowCellList){
				List<Run> runsList = flowCell.getRun();
				flowCellRunMap.put(flowCell, runsList);
			}
		}
		
		//for all the flowcells that we've identified, which are on a sequence run
		for(List<Sample> platformUnitList : platformUnitMap.values()){
			for(Sample platformUnit : platformUnitList){
				List<Run> runsList = platformUnit.getRun();
				platformUnitRunMap.put(platformUnit, runsList);
			}
		}
		
		//let's have some sanity checking
		System.out.println("1. User-Submitted Macromolecules");
		for(Sample macromoleculeSubmittedSample : macromoleculeSubmittedSamplesList){
			System.out.println("Macromolecule Name: " + macromoleculeSubmittedSample.getName() + "[" + macromoleculeSubmittedSample.getSampleType().getName() +"]");
			System.out.println("--Macromolecule Species: " + speciesMap.get(macromoleculeSubmittedSample));
			System.out.println("--Received Status: " + receivedStatusMap.get(macromoleculeSubmittedSample));
			List<Sample> facilityLibrariesForThisMacromoleculeList = facilityLibraryMap.get(macromoleculeSubmittedSample);
			System.out.println("--Libraries Made From This Macromolecule: " + facilityLibrariesForThisMacromoleculeList.size());
			for(Sample facilityLibrary : facilityLibrariesForThisMacromoleculeList){
				System.out.println("----Library: " + facilityLibrary.getName() + "[" + facilityLibrary.getSampleType().getName() +"]");
				Adaptor adaptor = libraryAdaptorMap.get(facilityLibrary);
				if(adaptor != null){
					System.out.println("------Adaptor: " + adaptor.getAdaptorset().getName() + " Index " + adaptor.getBarcodenumber().intValue() + " [" + adaptor.getBarcodesequence() + "]");
				}
				else{
					System.out.println("------Adaptor: Not Found");
				}
				List<Sample> flowCellList = flowCellMap.get(facilityLibrary);
				System.out.println("--------Flow Cells Library Has Been Run On: " + flowCellList.size());
				for(Sample flowCell : flowCellList){
					System.out.println("----------Flow Cell: " + flowCell.getName()); 
					List<Run> runs = flowCellRunMap.get(flowCell);
					System.out.println("------------Run For This Flow Cell: " + runs.size());
					for(Run run : runs){
						System.out.println("--------------Run: " + run.getName());
					}					
				}
			}
		}
		//more sanity checking
		System.out.println(" ");
		System.out.println("2. User-Submitted Libraries");
		for(Sample librarySubmittedSample : librarySubmittedSamplesList){
			System.out.println("Library Name: " + librarySubmittedSample.getName() + "[" + librarySubmittedSample.getSampleType().getName() +"]");
			System.out.println("--Macromolecule Species: " + speciesMap.get(librarySubmittedSample));
			System.out.println("--Received Status: " + receivedStatusMap.get(librarySubmittedSample));
			Adaptor adaptor = libraryAdaptorMap.get(librarySubmittedSample);
			if(adaptor != null){
				System.out.println("----Adaptor: " + adaptor.getAdaptorset().getName() + " Index " + adaptor.getBarcodenumber().intValue() + " [" + adaptor.getBarcodesequence() + "]");
			}
			else{
				System.out.println("----Adaptor: Not Found");
			}
			List<Sample> flowCellList = flowCellMap.get(librarySubmittedSample);
			System.out.println("------Flow Cells Library Has Been Run On: " + flowCellList.size());
			for(Sample flowCell : flowCellList){
				System.out.println("--------Flow Cell: " + flowCell.getName()); 
				List<Run> runs = flowCellRunMap.get(flowCell);
				System.out.println("----------Run For This Flow Cell: " + runs.size());
				for(Run run : runs){
					System.out.println("------------Run: " + run.getName());
				}					
			}
		}
		
		m.addAttribute("macromoleculeSubmittedSamplesList", macromoleculeSubmittedSamplesList);
		m.addAttribute("facilityLibraryMap", facilityLibraryMap);
		m.addAttribute("librarySubmittedSamplesList", librarySubmittedSamplesList);
		m.addAttribute("speciesMap", speciesMap);
		m.addAttribute("receivedStatusMap", receivedStatusMap);
		m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
		m.addAttribute("flowCellMap", flowCellMap);
		m.addAttribute("flowCellRunMap", flowCellRunMap);
		
		m.addAttribute("cellMap", cellMap);
		m.addAttribute("platformUnitMap", platformUnitMap);
		m.addAttribute("platformUnitRunMap", platformUnitRunMap);
		
		return "sampleDnaToLibrary/listJobSamples";
  }

  @RequestMapping(value = "/createLibraryFromMacro", method = RequestMethod.GET)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String createLibrary(@RequestParam("macromolSampleId") Integer macromolSampleId,
		  @RequestParam("adaptorsetId") Integer adaptorsetId,//this is the selectedAdaptorSet's Id
		  @RequestParam("jobId") Integer jobId,
		  ModelMap m) {

	  String returnString = validateJobIdAndSampleId(jobId, macromolSampleId, null);
	  if (returnString != null)
		  return returnString;

	  if (adaptorsetId == null){	//waspErrorMessage("user.updated.error");
		  waspErrorMessage("sampleDetail.adaptorsetParameter.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }

	  Job job = jobDao.getJobByJobId(jobId);
	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  Sample macromoleculeSample = sampleDao.getSampleBySampleId(macromolSampleId);

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
	  String[] roles = {"ft"};
	  List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(job.getWorkflow().getWorkflowId(), roles, "library");
	  if(librarySampleSubtypes.isEmpty()){
		  waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
	  }
	  Map<String, MetaAttribute.FormVisibility> visibilityElementMap = new HashMap<String, MetaAttribute.FormVisibility>(); // specify meta elements that are to be made immutable or hidden in here
	  visibilityElementMap.put("genericLibrary.adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
	  SampleSubtype librarySampleSubtype = librarySampleSubtypes.get(0); // should be one
	  List<SampleMeta> libraryMeta = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(librarySampleSubtype, visibilityElementMap);
	  try {
		  MetaHelper.setMetaValueByName("genericLibrary", "adaptorset", selectedAdaptorset.getAdaptorsetId().toString(), libraryMeta);
	  } catch (MetadataException e) {
		  logger.warn("Cannot set value on 'adaptorset': " + e.getMessage() );
	  }
	  Sample library = new Sample();
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

	  String returnString = validateJobIdAndSampleId(jobId, macromolSampleId, null);
	  if (returnString != null)
		  return returnString;

	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }


	  Sample parentMacromolecule = sampleDao.getSampleBySampleId(macromolSampleId);
	  Job jobForThisSample = jobDao.getJobByJobId(jobId);

	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(jobForThisSample);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  libraryForm.setName(libraryForm.getName().trim());
	  //confirm that this new library's name is different from all other sample.name in this job for samples of the same sample type (library)
	  SampleType sampleType = sampleTypeDao.getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId());
	  SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId());
	  validateSampleNameUnique(libraryForm.getName(), macromolSampleId, jobForThisSample, result);

	  // get validated metadata from 
	  Map<String, MetaAttribute.FormVisibility> visibilityElementMap = new HashMap<String, MetaAttribute.FormVisibility>(); // specify meta elements that are to be made immutable or hidden in here
	  visibilityElementMap.put("genericLibrary.adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
	  List<SampleMeta> sampleMetaListFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result, visibilityElementMap);

	  if (result.hasErrors()) {
		  libraryForm.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, sampleMetaListFromForm, visibilityElementMap));
		  libraryForm.setSampleSubtype(sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()));
		  libraryForm.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId()));
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
		  m.put("sample", libraryForm); 
		  return "sampleDnaToLibrary/createLibrary";
	  }

	  //all OK so create/save new library
	  libraryForm.setSubmitterLabId(parentMacromolecule.getSubmitterLabId());//needed??
	  libraryForm.setSubmitterUserId(parentMacromolecule.getSubmitterUserId());//needed??
	  libraryForm.setSubmitterJobId(parentMacromolecule.getSubmitterJobId());//needed??
	  libraryForm.setIsActive(new Integer(1));
	  libraryForm.setLastUpdTs(new Date());
	  SampleWrapperWebapp managedLibraryFromForm = new SampleWrapperWebapp(libraryForm, sampleSourceDao);
	  try {
		  managedLibraryFromForm.setParent(parentMacromolecule, sampleSourceDao);
	  } catch (SampleParentChildException e) {
		  e.printStackTrace();
	  }
	  managedLibraryFromForm.updateMetaToList(sampleMetaListFromForm, sampleMetaDao);
	  managedLibraryFromForm.saveAll(sampleService, sampleSourceDao);
	  
	  //add entry to jobsample table to link new library to job
	  JobSample newJobSample = new JobSample();
	  newJobSample.setJob(jobForThisSample);
	  newJobSample.setSample(libraryForm);
	  newJobSample = jobSampleDao.save(newJobSample);

	  //add entry to sample source to link new library to the macromolecule from which it was derived
	  SampleSource sampleSource = null; 

	  //find max samplesource.multiplexindex for this macromolecule
	  int maxindex = 0;
	  Map filterMap2 = new HashMap();
	  filterMap2.put("sourceSampleId", parentMacromolecule.getSampleId());
	  List<SampleSource> libFromThisMacromoleculeList = sampleSourceDao.findByMap(filterMap2);
	  for(SampleSource ss : libFromThisMacromoleculeList){
		  if (ss.getSampleId().intValue() == libraryForm.getSampleId().intValue()){
			  sampleSource = ss;
	  	  } else if(ss.getMultiplexindex().intValue() > maxindex){
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
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
	public String libraryDetailRO(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) throws MetadataException{
	  return validateJobIdAndSampleId(jobId, libraryId, 
			  libraryDetail(jobId, libraryId, m, false) );
  }
  
  @RequestMapping(value = "/librarydetail_rw/{jobId}/{libraryId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String libraryDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) throws MetadataException{
	  return validateJobIdAndSampleId(jobId, libraryId, 
			  libraryDetail(jobId, libraryId, m, true) );
  }
  
  @RequestMapping(value = "/librarydetail_rw/{jobId}/{libraryId}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String libraryDetailEdit(
		  @PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, 
		  @Valid Sample libraryForm, BindingResult result, 
		  SessionStatus status, 
		  ModelMap m) throws MetadataException{

	  // validate path variables
	  String returnString = validateJobIdAndSampleId(jobId, libraryId, null);
	  if (returnString != null)
		  return returnString;
	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  //return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  } 
	  
	  libraryForm.setName(libraryForm.getName().trim());
	  
	  Sample library = sampleDao.getSampleBySampleId(libraryId); 
	  validateSampleNameUnique(libraryForm.getName(), libraryId, jobDao.getJobByJobId(jobId), result);
	  SampleWrapperWebapp managedLibrary = new SampleWrapperWebapp(library, sampleSourceDao);
	  List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, 
			  sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()), result); 
	  if(result.hasErrors()){
		  waspErrorMessage("sampleDetail.updated.error");
		  libraryForm.setSampleMeta(metaFromForm);
		  return libraryDetail(jobId, libraryForm, libraryId, m, true);
	  }
	  // all ok so save 
	  library.setName(libraryForm.getName());
	  managedLibrary.updateMetaToList(metaFromForm, sampleMetaDao);
	  managedLibrary.saveAll(sampleService, sampleSourceDao);

	  waspMessage("sampleDetail.updated_success.label");
	  return "redirect:/sampleDnaToLibrary/librarydetail_ro/"+jobId+"/"+libraryId+".do";
  }
  	
  /**
   * Handles preparation of model for display of library details. Makes a detached Sample object.
   * @param jobId
   * @param libraryInId
   * @param m
   * @param isRW
   * @return
   * @throws MetadataException
   */
  public String libraryDetail(Integer jobId, Integer libraryInId, ModelMap m, boolean isRW) throws MetadataException{
		// get the library subtype for this workflow as the job-viewer sees it. We will use this 
		// to synchronize the metadata for display.
	    // We make a new Sample object 'modelLibrary' so that we can use it with the model and adjust the sample subtype / metadata freely
	    // without affecting the info in the database
		Sample libraryIn = sampleDao.getSampleBySampleId(libraryInId);
		String[] roles = {"lu"};
		List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(jobDao.getJobByJobId(jobId).getWorkflow().getWorkflowId(), roles, "library");
		if(librarySampleSubtypes.isEmpty()){
			waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
		}
		Sample modelLibrary = new Sample();
		modelLibrary.setSampleSubtype(librarySampleSubtypes.get(0)); // should be one
		modelLibrary.setSampleId(libraryIn.getSampleId());
		modelLibrary.setSampleTypeId(libraryIn.getSampleTypeId());
		modelLibrary.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(libraryIn.getSampleTypeId()));
		modelLibrary.setName(libraryIn.getName());
		modelLibrary.setSampleMeta(libraryIn.getSampleMeta());
		return libraryDetail(jobId, modelLibrary, libraryInId, m, isRW);
  }
  
  
  /**
   * Handles preparation of model for display of library details. 
   * @param jobId
   * @param libraryIn: should be a detached entity
   * @param libraryInId: necessary if libraryIn has no id (e.g. from form)
   * @param m
   * @param isRW
   * @return
   * @throws MetadataException
   */
  public String libraryDetail(Integer jobId, Sample libraryIn, Integer libraryInId, ModelMap m, boolean isRW) throws MetadataException{
	  	Job job = jobDao.getJobByJobId(jobId);
	  	
	  	// libraryIn should be a detached Sample object. If the sampleId is null then the Sample object is from a form and all the metadata from the form is 
	  	// assumed to be associated with it. Otherwise we assume that the library info is cloned from a persisted object.
		SampleWrapperWebapp libraryInManaged = new SampleWrapperWebapp(libraryIn, sampleSourceDao);
		
  		Adaptorset selectedAdaptorset = null;
		Adaptor adaptor = null;
		
		Map<String, MetaAttribute.FormVisibility>  visibilityElementMap = new HashMap<String, MetaAttribute.FormVisibility>(); // specify meta elements that are to be made immutable or hidden in here
		visibilityElementMap.put("genericLibrary.adaptorset", MetaAttribute.FormVisibility.immutable); // adaptor is a list control but we just want to display its value
		libraryIn.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(
				sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryIn.getSampleSubtypeId()), 
				libraryInManaged.getAllSampleMeta(), visibilityElementMap));
		try{	
  			adaptor = adaptorDao.getAdaptorByAdaptorId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptor", libraryIn.getSampleMeta())) );
  			selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", libraryIn.getSampleMeta())) );
  		} catch(MetadataException e){
  			logger.warn("Cannot get metadata : " + e.getMessage());
  		} catch(NumberFormatException e){
  			logger.warn("Cannot convert to numeric value for metadata " + e.getMessage());
  		}
		SampleWrapperWebapp persistentLibraryManaged;
		if (libraryIn.getSampleId() == null){
			persistentLibraryManaged = new SampleWrapperWebapp(sampleDao.getSampleBySampleId(libraryInId), sampleSourceDao);
		} else {
			persistentLibraryManaged = libraryInManaged;
		}
		Sample parentMacromolecule = null;
		if (persistentLibraryManaged.getParentWrapper() != null)
			parentMacromolecule = persistentLibraryManaged.getParentWrapper().getSampleObject();
		
		m.addAttribute("adaptorsets", adaptorsetDao.findAll()); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		m.addAttribute("adaptors", selectedAdaptorset.getAdaptor()); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
		List<Adaptorset> otherAdaptorsets = adaptorsetDao.findAll();//should really filter this by the machine requested
		otherAdaptorsets.remove(selectedAdaptorset);//remove this one
		if(isRW){
			m.addAttribute("otherAdaptorsets", otherAdaptorsets);
		} 
		if (libraryIn.getSampleId() == null)
			libraryIn.setSampleId(libraryInId);
		m.addAttribute("job", job);
		m.addAttribute("extraJobDetailsMap", getExtraJobDetails(job));
		m.addAttribute("sample", libraryIn);
		m.addAttribute("adaptor", adaptor);
		m.addAttribute("parentMacromolecule", parentMacromolecule);
		return isRW?"sampleDnaToLibrary/librarydetail_rw":"sampleDnaToLibrary/librarydetail_ro";
  }
  

  
  
  @RequestMapping(value = "/sampledetail_ro/{jobId}/{sampleId}", method = RequestMethod.GET)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
	public String sampleDetailRO(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, ModelMap m) {
	  return sampleDetail(jobId, sampleId, m, false);
  }
 
  @RequestMapping(value = "/sampledetail_rw/{jobId}/{sampleId}", method = RequestMethod.GET)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String sampleDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, ModelMap m) {
	  return sampleDetail(jobId, sampleId, m, true);
  }
 
  
  public String sampleDetail(Integer jobId, Integer sampleId, ModelMap m, boolean isRW){
	  // validate path variables
	  String returnString = validateJobIdAndSampleId(jobId, sampleId, null);
	  if (returnString != null)
		  return returnString;
	  
	  Job job = jobDao.getJobByJobId(jobId);
	  	  
	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  Sample sample= sampleDao.getSampleBySampleId(sampleId);
	  //confirm these two objects exist and part of same job
	  
	  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(sample, sampleSourceDao);
	  m.addAttribute("normalizedSampleMeta", SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sample.getSampleSubtype(), sampleManaged.getAllSampleMeta()) );
	  m.put("job", job);
	  m.put("sample", sample); 
	  
	  return isRW?"sampleDnaToLibrary/sampledetail_rw":"sampleDnaToLibrary/sampledetail_ro";
  }
  
  @RequestMapping(value = "/sampledetail_rw/{jobId}/{sampleId}", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateSampleDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, 
								@Valid Sample sampleForm, BindingResult result, 
								SessionStatus status, ModelMap m) throws MetadataException {
		  
	  // validate path variables
	  String returnString = validateJobIdAndSampleId(jobId, sampleId, null);
	  if (returnString != null)
		  return returnString;
	  
	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  //return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  } 
		
	  Job jobForThisSample = jobDao.getJobByJobId(jobId);
	  
	  Map<String, String> extraJobDetailsMap = getExtraJobDetails(jobForThisSample);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);
		  	  		  
	  sampleForm.setName(sampleForm.getName().trim());//from the form
	  validateSampleNameUnique(sampleForm.getName(), sampleId, jobForThisSample, result);
	  
	  Sample sample = sampleDao.getSampleBySampleId(sampleId); 
	  SampleWrapperWebapp managedSample = new SampleWrapperWebapp(sample, sampleSourceDao);
	  List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, sample.getSampleSubtype(), result); // gets meta and adds back to managed sampleForm as it is not persisted
	  if(result.hasErrors()){
		  sampleForm.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(sampleForm.getSampleTypeId()));
		  sampleForm.setSampleSubtype(sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleForm.getSampleSubtypeId()));
		  m.put("job", jobForThisSample);
		  m.put("sample", sampleForm); 
		  m.addAttribute("normalizedSampleMeta",SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sample.getSampleSubtype(), metaFromForm));
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
	  
	  //replaced by jobService.getSubmittedSamples(job)
	  //so do not use this private method
	  
	  
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
	  class SampleNameComparator implements Comparator<Sample> {
		    @Override
		    public int compare(Sample arg0, Sample arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
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
	  class SampleNameComparator implements Comparator<Sample> {
		    @Override
		    public int compare(Sample arg0, Sample arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
	  Collections.sort(submittedSamples, new SampleNameComparator());//sort by sample's name; for class SampleNameComparator, see end of this file
	  
	  return submittedSamples;
	  
  }
  
  /**
   * See if Sample name has changed between sample objects and if so check if the new name is unique within the job.
   * @param formSample
   * @param originalSample
   * @param job
   * @param result
   */
  private void validateSampleNameUnique(String sampleName, Integer sampleId, Job job, BindingResult result){
	  //confirm that, if a new sample.name was supplied on the form, it is different from all other sample.name in this job
	  List<Sample> samplesInThisJob = job.getSample();
	  for(Sample eachSampleInThisJob : samplesInThisJob){
		  if(eachSampleInThisJob.getSampleId().intValue() != sampleId.intValue()){
			  if( sampleName.equals(eachSampleInThisJob.getName()) ){
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
  
  /**
   * Validates the job and sample id supplied to make sure they are refer to actual persisted model objects and are linked
   * @param jobId
   * @param sampleId
   * @param defaultReturnString
   * @return a redirect path on error, otherwise the defaultResturnString
   */
  private String validateJobIdAndSampleId(Integer jobId, Integer sampleId, String defaultReturnString){
	  	if( jobId == null ){
			waspErrorMessage("sampleDetail.jobParameter.error");
			return "redirect:/dashboard.do";
		}
		if (sampleId == null){	
			waspErrorMessage("sampleDetail.sampleParameter.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		
		if(jobDao.getJobByJobId(jobId).getJobId()==null){//not found in database
			waspErrorMessage("sampleDetail.jobNotFound.error");
			return "redirect:/dashboard.do";
		}
		
		if(sampleDao.getSampleBySampleId(sampleId).getSampleId()==null){
			waspErrorMessage("sampleDetail.sampleNotFound.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		
		//confirm these two objects exist and part of same job
		if(jobSampleDao.getJobSampleByJobIdSampleId(jobId, sampleId).getJobSampleId()== null){
			waspErrorMessage("sampleDetail.jobSampleMismatch.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		return defaultReturnString;
  }
  
}



