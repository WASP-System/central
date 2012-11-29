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
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCellSelection;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.SampleService;
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
  private JobCellSelectionDao jobCellSelectionDao;
  @Autowired
  private JobDao jobDao;
  @Autowired
  private AdaptorDao adaptorDao;
  @Autowired
  private AdaptorsetDao adaptorsetDao;
  @Autowired
  private AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;
  @Autowired
  private SampleTypeDao sampleTypeDao;
  @Autowired
  private SampleSubtypeDao sampleSubtypeDao;
  @Autowired
  private JobSampleDao jobSampleDao;
  
  @Autowired
  private RoleService roleService; 
  @Autowired
  private SampleService sampleService;
  @Autowired
  private JobService jobService;
  @Autowired
  private AuthenticationService authenticationService;
  

  
  private final MetaHelperWebapp getMetaHelperWebapp() {
    return new MetaHelperWebapp(SampleMeta.class, request.getSession());
  }



  final public String defaultPageFlow = "/sampleDnaToLibrary/detail/{n};/sampleDnaToLibrary/addLibraryMeta/{n};/sampleDnaToLibrary/verify/{n}";

  public String nextPage(Sample sample) {
     String pageFlow = this.defaultPageFlow;

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
  public String listJobSamples(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
    
	
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
	  
	  List<JobUser> jobUserList = job.getJobUser();
	  List<User> additionalJobViewers = new ArrayList<User>();
	  for(JobUser jobUser : jobUserList){
		  if(jobUser.getUser().getUserId().intValue() != job.getUserId().intValue() && jobUser.getUser().getUserId().intValue() != job.getLab().getPrimaryUserId().intValue()){
			  additionalJobViewers.add(jobUser.getUser());
		  }
	  }
	  class SubmitterLastNameFirstNameComparator implements Comparator<User> {
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getLastName().concat(arg0.getFirstName()).compareToIgnoreCase(arg1.getLastName().concat(arg1.getFirstName()));
			}
		}
	  Collections.sort(additionalJobViewers, new SubmitterLastNameFirstNameComparator());
	  m.addAttribute("additionalJobViewers", additionalJobViewers);
	  
	  User currentWebViewer = authenticationService.getAuthenticatedUser();
	  Boolean currentWebViewerIsSuperuserSubmitterOrPI = false;
	  if(authenticationService.isSuperUser() || currentWebViewer.getUserId().intValue() == job.getUserId().intValue() || currentWebViewer.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()){
		  currentWebViewerIsSuperuserSubmitterOrPI = true; //superuser, job's submitter, job's PI
	  }
	  m.addAttribute("currentWebViewerIsSuperuserSubmitterOrPI", currentWebViewerIsSuperuserSubmitterOrPI);
	  m.addAttribute("currentWebViewer", currentWebViewer);
	  
	  LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	  
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);
	  
	  List<Adaptorset> adaptorsetList = adaptorsetDao.findAll();
	  m.addAttribute("adaptorsets", adaptorsetList);
		
		
		
	  List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
	  List<Sample> macromoleculeSubmittedSamplesList = new ArrayList<Sample>();
	  List<Sample> librarySubmittedSamplesList = new ArrayList<Sample>();
	  Map<Sample, String> speciesMap = new HashMap<Sample, String>();
	  Map<Sample, String> receivedStatusMap = new HashMap<Sample, String>();
	  Map<Sample, Boolean> receiveSampleStatusMap = new HashMap<Sample, Boolean>();// created 5/7/12
	  Map<Sample, Boolean> createLibraryStatusMap = new HashMap<Sample, Boolean>();
	  Map<Sample, Boolean> assignLibraryToPlatformUnitStatusMap = new HashMap<Sample, Boolean>();
	  Map<Sample, List<Sample>> facilityLibraryMap = new HashMap<Sample, List<Sample>>();//key is a submitted sample (macromolecule) and value is list of facility-generated libraries created from that macromolecule)
	  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();//key is library and value is the adaptor used for that library
	  for(Sample sample : submittedSamplesList){
		  	if (!sampleService.isDnaOrRna(sample) && !sampleService.isLibrary(sample))
				throw new SampleTypeException("sample is not of expected type of DNA, RNA, Library or Facility Library");
			if(sampleService.isDnaOrRna(sample)){
				macromoleculeSubmittedSamplesList.add(sample);
				List<Sample> facilityGeneratedLibrariesList = sampleService.getFacilityGeneratedLibraries(sample);//get list of facility-generated libraries from a user-submitted macromolecule
				facilityLibraryMap.put(sample, facilityGeneratedLibrariesList);
				createLibraryStatusMap.put(sample, sampleService.isSampleAwaitingLibraryCreation(sample));
			}
			else if(sampleService.isLibrary(sample)){
				librarySubmittedSamplesList.add(sample);
				assignLibraryToPlatformUnitStatusMap.put(sample, sampleService.isLibraryAwaitingPlatformUnitPlacement(sample));
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
			
			//for each task, determine if it's status is CREATED (if it has many states for a single status, simply determine if at least one is CREATED) 
			receiveSampleStatusMap.put(sample, sampleService.isSampleReceived(sample));
				
		}
		sampleService.sortSamplesBySampleName(macromoleculeSubmittedSamplesList);
		sampleService.sortSamplesBySampleName(librarySubmittedSamplesList);
		
		//for each library (those in facilityLibraryMap and those in librarySubmittedSamplesList) get flowcells/runs and add to map Map<Sample, List<Sample>>
		for(Sample library : librarySubmittedSamplesList){
			Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			if(adaptor==null){
				//message and get out of here
			}
			libraryAdaptorMap.put(library, adaptor);	
		}
		for(List<Sample> libraryList : facilityLibraryMap.values()){
			for(Sample library : libraryList){
				Adaptor adaptor = sampleService.getLibraryAdaptor(library);
				if(adaptor==null){
					//message and get out of here
				}
				libraryAdaptorMap.put(library, adaptor);
			}
		}
		
		List<Sample> availableAndCompatibleFlowCells = sampleService.getAvailableAndCompatiblePlatformUnits(job);//available flowCells that are compatible with this job
		for(Sample flowCell : availableAndCompatibleFlowCells){
			try{
				for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(flowCell).values()){
					for (Sample library: sampleService.getLibrariesOnCell(cell)){
						Adaptor adaptor = sampleService.getLibraryAdaptor(library);
						if(adaptor==null){
							logger.warn("Expected an adaptor but found none in library id="+library.getSampleId().toString());
						}
						libraryAdaptorMap.put(library, adaptor);
					}
				}
			} catch(SampleTypeException e){
				logger.warn(e.getMessage());
			}
		}
		
		Map jobCellFilter = new HashMap();
		jobCellFilter.put("jobId", job.getJobId().intValue());
		List<String> orderByList = new ArrayList<String>();
		orderByList.add("cellIndex");
		List<JobCellSelection> jobCellSelectionList = jobCellSelectionDao.findByMapDistinctOrderBy(jobCellFilter, null, orderByList, "ASC");
	  
		//attempt at getting the requested coverage in a better format:
		int totalNumberCellsRequested = jobCellSelectionList.size();
		Map<Sample, String> coverageMap = new LinkedHashMap<Sample, String>();
		List<Sample> tempListOfAllSubmittedSamples = new ArrayList<Sample>();
		tempListOfAllSubmittedSamples.addAll(macromoleculeSubmittedSamplesList);
		tempListOfAllSubmittedSamples.addAll(librarySubmittedSamplesList);
		for(Sample sample : tempListOfAllSubmittedSamples){
			StringBuffer stringBuffer = new StringBuffer("");
			for(int i = 1; i <= totalNumberCellsRequested; i++){
				boolean found = false;
				for(JobCellSelection jobCellSelection : jobCellSelectionList){
					List<SampleJobCellSelection> sampleJobCellSelectionList = jobCellSelection.getSampleJobCellSelection();
					for(SampleJobCellSelection sampleJobCellSelection : sampleJobCellSelectionList){
						if(sampleJobCellSelection.getSampleId().intValue() == sample.getSampleId().intValue()){
							if(jobCellSelection.getCellIndex().intValue() == i){
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
		
		
		
		// get files associated with this job
		List<File> files = new ArrayList<File>();
		for (JobFile jf: job.getJobFile())
			files.add(jf.getFile());
		
		m.addAttribute("macromoleculeSubmittedSamplesList", macromoleculeSubmittedSamplesList);
		m.addAttribute("facilityLibraryMap", facilityLibraryMap);
		m.addAttribute("librarySubmittedSamplesList", librarySubmittedSamplesList);
		m.addAttribute("speciesMap", speciesMap);
		m.addAttribute("receivedStatusMap", receivedStatusMap);
		m.addAttribute("receiveSampleStatusMap", receiveSampleStatusMap);
		m.addAttribute("createLibraryStatusMap", createLibraryStatusMap);
		m.addAttribute("assignLibraryToPlatformUnitStatusMap", assignLibraryToPlatformUnitStatusMap);
		m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
		m.addAttribute("availableAndCompatibleFlowCells", availableAndCompatibleFlowCells);
		m.addAttribute("files", files);
		
		return "sampleDnaToLibrary/listJobSamples";
  }
  
  @RequestMapping(value="/removeViewerFromJob/{jobId}/{userId}.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('jv-' + #jobId)")
  public String removeViewerFromJob(@PathVariable("jobId") Integer jobId, @PathVariable("userId") Integer userId, ModelMap m) {
	  
	  try{
		  jobService.removeJobViewer(jobId, userId);//performs checks to see if this is a legal action. 
	  }
	  catch(Exception e){
		  logger.warn(e.getMessage());
		  waspErrorMessage(e.getMessage());
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }	   
	  waspMessage("listJobSamples.jobViewerRemoved.label");
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
  }
  
  @RequestMapping(value="/addJobViewer.do", method=RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('jv-' + #jobId)")
  public String addJobViewer(@RequestParam("jobId") Integer jobId,
		  @RequestParam("newViewerEmailAddress") String newViewerEmailAddress,
		  ModelMap m)  {	
	  try{
		   jobService.addJobViewer(jobId, newViewerEmailAddress);//performs checks to see if this is a legal action. 
	  }
	  catch(Exception e){		    
		  logger.warn(e.getMessage());
		  waspErrorMessage(e.getMessage());
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  waspMessage("listJobSamples.jobViewerAdded.label");
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";	  
  }
  
  /**
   * get adaptorsets and adaptors for populating model. If a selected adaptor is found in the provided SampleDraftMeta
   * it is used to find appropriate adaptors
   * @param jobDraft
   * @param sampleDraftMeta
   * @param m
   */
	private void prepareAdaptorsetsAndAdaptors(Job job, List<SampleMeta> sampleMeta, ModelMap m){
		List<Adaptorset> adaptorsets = new ArrayList<Adaptorset>();
		for (JobResourcecategory jrc: job.getJobResourcecategory()){
			Map<String, Integer> adaptorsetRCQuery = new HashMap<String, Integer>();
			adaptorsetRCQuery.put("resourcecategoryId", jrc.getResourcecategoryId());
			for (AdaptorsetResourceCategory asrc: adaptorsetResourceCategoryDao.findByMap(adaptorsetRCQuery))
				adaptorsets.add(asrc.getAdaptorset());
		}
		m.addAttribute("adaptorsets", adaptorsets); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		
		List<Adaptor> adaptors = new ArrayList<Adaptor>();
		Adaptorset selectedAdaptorset = null;
		try{	
  			selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", sampleMeta)) );
  		} catch(MetadataException e){
  			logger.warn("Cannot get metadata genericLibrary.adaptorset. Presumably not be defined: " + e.getMessage());
  		} catch(NumberFormatException e){
  			logger.warn("Cannot convert to numeric value for metadata " + e.getMessage());
  		}
		if (selectedAdaptorset != null){
			adaptors = selectedAdaptorset.getAdaptor();
		} else if (adaptorsets.size() == 1){
			adaptors = adaptorsets.get(0).getAdaptor();
		}
		m.addAttribute("adaptors", adaptors); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
	}
	

  @RequestMapping(value = "/createLibraryFromMacro/{jobId}/{macromolSampleId}", method = RequestMethod.GET)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String createLibrary(@PathVariable("macromolSampleId") Integer macromolSampleId,
		  @PathVariable("jobId") Integer jobId,
		  ModelMap m) {

	  String returnString = validateJobIdAndSampleId(jobId, macromolSampleId, null);
	  if (returnString != null)
		  return returnString;

	  Job job = jobDao.getJobByJobId(jobId);
	  Map<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  Sample macromoleculeSample = sampleDao.getSampleBySampleId(macromolSampleId);

	  m.put("macromoleculeSample", macromoleculeSample);
	  m.put("job", job);

	   
	  String[] roles = {"ft"};
	  List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(job.getWorkflow().getWorkflowId(), roles, "library");
	  if(librarySampleSubtypes.isEmpty()){
		  waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
	  }
	  SampleSubtype librarySampleSubtype = librarySampleSubtypes.get(0); // should be one
	  List<SampleMeta> libraryMeta = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(librarySampleSubtype);
	  prepareAdaptorsetsAndAdaptors(job, libraryMeta, m);
	  
	  Sample library = new Sample();
	  library.setSampleSubtype(librarySampleSubtype);
	  library.setSampleType(sampleTypeDao.getSampleTypeByIName("facilityLibrary"));
	  library.setSampleMeta(libraryMeta);
	  m.put("sample", library); 	

	  return "sampleDnaToLibrary/createLibrary";

  }


  @RequestMapping(value = "/createLibraryFromMacro/{jobId}/{macromolSampleId}", method = RequestMethod.POST)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String createLibrary(@PathVariable("macromolSampleId") Integer macromolSampleId,
		  @PathVariable("jobId") Integer jobId, 
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

	  Map<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(jobForThisSample);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  libraryForm.setName(libraryForm.getName().trim());
	  //confirm that this new library's name is different from all other sample.name in this job for samples of the same sample type (library)
	  SampleType sampleType = sampleTypeDao.getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId());
	  SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId());
	  validateSampleNameUnique(libraryForm.getName(), macromolSampleId, jobForThisSample, result);

	  // get validated metadata from 
	  List<SampleMeta> sampleMetaListFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result);

	  if (result.hasErrors()) {
		  libraryForm.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, sampleMetaListFromForm));
		  libraryForm.setSampleSubtype(sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()));
		  libraryForm.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId()));
		  prepareSelectListData(m);//doubt that this is required here; really only needed for meta relating to country or state
		  waspErrorMessage("sampleDetail.updated.error");
		  prepareAdaptorsetsAndAdaptors(jobForThisSample, libraryForm.getSampleMeta(), m);
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
	  SampleWrapperWebapp managedLibraryFromForm = new SampleWrapperWebapp(libraryForm);
	  try {
		  managedLibraryFromForm.setParent(parentMacromolecule);
	  } catch (SampleParentChildException e) {
		  e.printStackTrace();
	  }
	  managedLibraryFromForm.updateMetaToList(sampleMetaListFromForm, sampleMetaDao);
	  managedLibraryFromForm.saveAll(sampleService);
	  
	  //add entry to jobsample table to link new library to job
	  JobSample newJobSample = new JobSample();
	  newJobSample.setJob(jobForThisSample);
	  newJobSample.setSample(libraryForm);
	  newJobSample = jobSampleDao.save(newJobSample);

	  try {
		  sampleService.updateLibraryCreatedStatus(parentMacromolecule, WaspStatus.CREATED);
		  waspMessage("libraryCreated.created_success.label");
		  logger.warn("Library created successfully but there was a failure to send status message to wasp-daemon");
	  } catch (WaspMessageBuildingException e) {
		  waspErrorMessage("libraryCreated.message_fail.error");
	  } 
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
	  SampleWrapperWebapp managedLibrary = new SampleWrapperWebapp(library);
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
	  managedLibrary.saveAll(sampleService);

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
		if (libraryIn.getParentId() != null)
			modelLibrary.setParent(libraryIn.getParent());
		modelLibrary.setSampleId(libraryIn.getSampleId());
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
		SampleWrapperWebapp libraryInManaged = new SampleWrapperWebapp(libraryIn);
		
  		Adaptorset selectedAdaptorset = null;
		Adaptor adaptor = null;
		
		libraryIn.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(
				sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryIn.getSampleSubtypeId()), 
				libraryInManaged.getAllSampleMeta()));
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
			persistentLibraryManaged = new SampleWrapperWebapp(sampleDao.getSampleBySampleId(libraryInId));
		} else {
			persistentLibraryManaged = libraryInManaged;
		}
		Sample parentMacromolecule = null;
		if (persistentLibraryManaged.getParentWrapper() != null)
			parentMacromolecule = persistentLibraryManaged.getParentWrapper().getSampleObject();
		
		prepareAdaptorsetsAndAdaptors(job, libraryIn.getSampleMeta(), m);
		if (libraryIn.getSampleId() == null)
			libraryIn.setSampleId(libraryInId);
		m.addAttribute("job", job);
		m.addAttribute("extraJobDetailsMap", jobService.getExtraJobDetails(job));
		m.addAttribute("sample", libraryIn);
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
	  	  
	  Map<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);

	  Sample sample= sampleDao.getSampleBySampleId(sampleId);
	  //confirm these two objects exist and part of same job
	  
	  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(sample);
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
	  
	  Map<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(jobForThisSample);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);
		  	  		  
	  sampleForm.setName(sampleForm.getName().trim());//from the form
	  validateSampleNameUnique(sampleForm.getName(), sampleId, jobForThisSample, result);
	  
	  Sample sample = sampleDao.getSampleBySampleId(sampleId); 
	  SampleWrapperWebapp managedSample = new SampleWrapperWebapp(sample);
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
	  managedSample.saveAll(sampleService);

	  waspMessage("sampleDetail.updated_success.label");
	  return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";

  }
  
  
  private Map<String, String> getExtraJobDetails(Job job){
	  
	  //replaced by JobService.getExtraJobDetails(job)
	  
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
	  List<JobCellSelection> jobCellSelections = jobCellSelectionDao.findByMap(filterJobCell);
	  for(JobCellSelection jobCellSelection : jobCellSelections){
		  List<SampleJobCellSelection> sampleJobCellSelections = jobCellSelection.getSampleJobCellSelection();
		  for(SampleJobCellSelection sampleJobCellSelection : sampleJobCellSelections){
			   samplesSet.add(sampleJobCellSelection.getSample());
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



