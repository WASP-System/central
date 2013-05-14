package edu.yu.einstein.wasp.controller;


import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.MetaMessage;
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
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.CellWrapper;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;

@Controller
@RequestMapping("/datadisplay")
public class DataDisplayController extends WaspController {

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
  private RunService runService; 
  @Autowired
  private SampleService sampleService;
  @Autowired
  private JobService jobService;
  @Autowired
  private FileService fileService;
  @Autowired
  private AuthenticationService authenticationService;
  @Autowired
  private FileUrlResolver fileUrlResolver;
  @Autowired
  private GenomeService genomeService;
  @Autowired
  private AdaptorService adaptorService;
  
  private final MetaHelperWebapp getMetaHelperWebapp() {
	    return new MetaHelperWebapp(SampleMeta.class, request.getSession());
	  }
    
  @RequestMapping(value="/mps/jobs/{jobId}/runs", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsMainPageByJob(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
	  
	  Job job = jobService.getJobByJobId(jobId);

	  //really need !!!!:   jobService.getSuccessfullyCompletedRuns(job)	  
	  
	  /***** NOT USED - instead we're going through sampleService.getCellLibrariesForJob(job); 
	  ///THIS IS NOT GOOD, THE RESULT SHOULD BE A SET!!!!!!
	  List<Sample> platformUnitList = jobService.getPlatformUnitWithLibrariesOnForJob(job);	  
	  System.out.println("platformUnits from the one with the List: " + platformUnitList.size());
	  *******/
	  
	  Set<Sample> platformUnitSet = new HashSet<Sample>();
	  Set<Run> runSet = new HashSet<Run>();
	  List<Sample> platformUnitsSuccessfullyRun = new ArrayList<Sample>();
	  List<SampleSource> cellLibrariesThatPassedQCForJobList =  sampleService.getCellLibrariesThatPassedQCForJob(job);
	  System.out.println("Num. entries in cellLibrariesThatPassedQCForJobList: " + cellLibrariesThatPassedQCForJobList.size());
	  Set<SampleSource> cellLibrariesForJob =  sampleService.getCellLibrariesForJob(job); //FOR TESTING ONLY; THIS IS NOT WHAT WE NEED!
	  System.out.println("Num. entries in cellLibrariesForJob: " + cellLibrariesForJob.size());
	  Map<Run,Sample> runPlatformUnitMap = new HashMap<Run, Sample>();
	  Map<Sample, Run> platformUnitRunMap = new HashMap<Sample, Run>();
	  Map<Sample, Set<Sample>> platformUnitCellSetMap = new HashMap<Sample, Set<Sample>>();
	  Map<Sample, List<Sample>> platformUnitOrderedCellListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, List<Sample>> cellLibraryListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, List<Sample>> cellControlLibraryListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, Integer> cellIndexMap = new HashMap<Sample, Integer>();
	  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();
	  Map<Sample, Sample> libraryMacromoleculeMap = new HashMap<Sample, Sample>();
	  Random randomInt = new Random(System.currentTimeMillis());//for FAKE_name_
	  
	  for(SampleSource cellLibrary : cellLibrariesThatPassedQCForJobList/*cellLibrariesForJob*/){
		  try{
			  Sample cell = sampleService.getCell(cellLibrary);//cellLibrary.getSample();
			  Sample library = sampleService.getLibrary(cellLibrary);//cellLibrary.getSourceSample();
			  Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			  Sample platformUnit = sampleService.getPlatformUnitForCell(cell);//sampleService.getPlatformUnitForCell(cellLibrary.getSample());
			  List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST????
			  //really need runService.getSuccessfullyCompletedRuns(job)
			  
			  //TODO MUST FIX THIS*************!!!!!!!!!!!!!***************
			  //if(runList.isEmpty()){
				//  continue;
			  //}
			  
			  if(runList.isEmpty()){//really just want to do a CONTINUE HERE, but for test, show somehting
				  List<Run> otherRunList = runService.getRunsForPlatformUnit(platformUnit);//more fake it
				  if(!otherRunList.isEmpty()){
					  platformUnitRunMap.put(platformUnit, otherRunList.get(0));//for testing only
				  }
				  else{
					  Run run = new Run();				  
					  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					  Date date = new Date();			  
					  run.setName("FAKE_name_"+dateFormat.format(date)+"_"+platformUnit.getName());
					  run.setId(randomInt.nextInt(500) + 500);
					  platformUnitRunMap.put(platformUnit, run);
				  }
			  }
			  else{
				  platformUnitRunMap.put(platformUnit, runList.get(0));//THIS LIST SHOULD ONLY HAVE ONE ENTRY. WHY IS IT A LIST??
			  }
			  
			  
			  platformUnitSet.add(platformUnit);
			  if(platformUnitCellSetMap.containsKey(platformUnit)){
				  platformUnitCellSetMap.get(platformUnit).add(cell);
			  }
			  else{
				  Set<Sample> cellSet = new HashSet<Sample>();
				  cellSet.add(cell);
				  platformUnitCellSetMap.put(platformUnit, cellSet);
			  }
			  
			  if(cellLibraryListMap.containsKey(cell)){
				  cellLibraryListMap.get(cell).add(library);
			  }
			  else{
				  List<Sample> libraryList = new ArrayList<Sample>();
				  libraryList.add(library);
				  cellLibraryListMap.put(cell, libraryList);
			  }
			
			  Sample macromolecule = library.getParent();
			  if(macromolecule != null && macromolecule.getId()!=null){
				  libraryMacromoleculeMap.put(library, macromolecule);
			  }
			  
			  if(adaptor != null && adaptor.getId()!=null){
				  libraryAdaptorMap.put(library, adaptor);
			  }
			  
			  cellIndexMap.put(cell, sampleService.getCellIndex(cell));
			  
			  /*forget the run for now
			  
			  //WHY DOES THIS RETURN A LIST????????? runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);
			  List<Run> runList = runService.getRunsForPlatformUnit(platformUnit);
			  for(Run run : runList){
				  //if( true runService.isRunSuccessfullyCompleted(run)  ){//fake it for now
					  runSet.add(run);
					  runPlatformUnitMap.put(run, platformUnit);
					  Sample cell = sampleService.getCell(cellLibrary);
					  platformUnitCellMap.put(platformUnit, cell);
					  List<Sample> librariessampleService.getLibrariesOnCellWithoutControls(cell);
					  
					  
					  
					  
					  break;//needed because if statement is faking it
					  
				  }
			  }
			   */
			  
		  }catch(Exception e){logger.debug("Unable to properly gather run info: " + e.getMessage());}
	  } 
	  System.out.println("platformUnits from the one with the SET: " + platformUnitSet.size());
	  for(Sample platformUnit : platformUnitSet){
		  
		  Set<Sample> set = platformUnitCellSetMap.get(platformUnit);
		  List<Sample> orderedCellList = new ArrayList<Sample>(set);
		  
		  //ORDER BY INDEX
		  class CellIndexComparator implements Comparator<Sample> {
			    @Override
			    public int compare(Sample cell0, Sample cell1) {
			    	try{
			    		return sampleService.getCellIndex(cell0).compareTo(sampleService.getCellIndex(cell1));
			    	}catch(Exception e){logger.debug("Unable to sort orderedCellList: " + e.getMessage()); return 0;}
			    }
			}
			Collections.sort(orderedCellList, new CellIndexComparator());//sort by cell's index

			platformUnitOrderedCellListMap.put(platformUnit, orderedCellList);	
			
			//while we have a set of cells for each platform unit, use it to retrieve each cell's list of control libraries, if any
			for(Sample cell : orderedCellList){
				List<Sample> controlLibraryList = sampleService.getControlLibrariesOnCell(cell);
				if(!controlLibraryList.isEmpty()){
					cellControlLibraryListMap.put(cell, controlLibraryList);
					for(Sample controlLibrary : controlLibraryList){
						Adaptor adaptor = sampleService.getLibraryAdaptor(controlLibrary);
						if(adaptor != null && adaptor.getId()!=null){
							  libraryAdaptorMap.put(controlLibrary, adaptor);
						}					
					}
				}
			}
	  }
	 
	//MUST GET THE RUN (if any)
	  //MUST get the adaptors/index
	  
	  //check out the data:
	  /*
	    Map<Sample, List<Sample>> platformUnitOrderedCellListMap = new HashMap<Sample, List<Sample>>();
	  	Map<Sample, List<Sample>> cellLibraryListMap = new HashMap<Sample, List<Sample>>();
	  	Map<Sample, Sample> libraryMacromoleculeMap = new HashMap<Sample, Sample>();
	   */
	  for (Sample platformUnit : platformUnitSet){
		  System.out.println("Platform Unit: " + platformUnit.getName());
		  List<Sample> cells = platformUnitOrderedCellListMap.get(platformUnit);
		  for(Sample cell : cells){
			  System.out.println("--Cell: " + cell.getName());
			  List<Sample> libraries = cellLibraryListMap.get(cell);
			  for(Sample library : libraries){
				  System.out.print("----Libraries: " + library.getName());
				  Sample macromolecule = libraryMacromoleculeMap.get(library);
				  if(macromolecule != null && macromolecule.getId()!=null){
					  System.out.println(" (Parent Sample: " + macromolecule.getName() + ")");
				  }
				  else{System.out.println("");}
			  }
		  }
	  }
	  
	  m.addAttribute("job", job);
	  m.addAttribute("platformUnitRunMap", platformUnitRunMap);
	  m.addAttribute("platformUnitSet", platformUnitSet);
	  m.addAttribute("platformUnitOrderedCellListMap", platformUnitOrderedCellListMap);
	  m.addAttribute("cellLibraryListMap", cellLibraryListMap);
	  m.addAttribute("cellIndexMap", cellIndexMap);
	  m.addAttribute("libraryMacromoleculeMap", libraryMacromoleculeMap);
	  m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
	  m.addAttribute("cellControlLibraryListMap", cellControlLibraryListMap);
	  
	  //return "sampleDnaToLibrary/resultsView";
	  return "datadisplay/mps/jobs/runs/mainpage";
  }

  @RequestMapping(value="/showplay", method=RequestMethod.GET)
  //@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public void showPlay(ModelMap m, HttpServletResponse response) throws SampleTypeException {
	  try{
		//FileHandle fileHandle = fileService.getFileHandleById(1);//this is an text file
		  //response.setContentType("text/html");
	  //FileHandle fileHandle = fileService.getFileHandleById(27);//this is an html file
	  //response.setContentType("text/html");
	  FileHandle fileHandle = fileService.getFileHandleById(28);//this is a pdf file
	  response.setContentType("application/pdf");
	  //FileHandle fileHandle = fileService.getFileHandleById(29);//this is a png file
	  //response.setContentType("image/png");
	  
	  fileService.copyFileHandleToOutputStream(fileHandle, response.getOutputStream());
	  //do not flush, it's not needed and it screws things up
	  }catch(Exception e){logger.debug("unable to get file for display");}
  }
  
  @RequestMapping(value="mps/jobs/{jobId}/jobdetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsJobDetailsByJob(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
	  
	  Job job = jobService.getJobByJobId(jobId);
	  m.addAttribute("job", job);
	  LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	  
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
	 
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);	  
	  //get the jobApprovals Comments (if any)
	  HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
	  m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	  //get the current jobStatus
	  m.addAttribute("jobStatus", jobService.getJobStatus(job));

		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
		for(JobFile jf: job.getJobFile()){
			FileGroup fileGroup = jf.getFile();//returns a FileGroup
			fileGroups.add(fileGroup);
			List<FileHandle> fileHandles = new ArrayList<FileHandle>();
			for(FileHandle fh : fileGroup.getFileHandles()){
				fileHandles.add(fh);
				String mimeType = fileService.getMimeType(fh.getFileName());
				if(!mimeType.isEmpty()){
					fileHandlesThatCanBeViewedList.add(fh);
				}
			}
			fileGroupFileHandlesMap.put(fileGroup, fileHandles);
		}
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		m.addAttribute("fileHandlesThatCanBeViewedList", fileHandlesThatCanBeViewedList);
		
	  return "datadisplay/mps/jobs/jobdetails";
  }
  
  
//  @RequestMapping(value="/runDetails/{runId}", method=RequestMethod.GET)
  //  //@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
//  public String runDetails(@PathVariable("runId") Integer runId, ModelMap m) throws SampleTypeException {

  @RequestMapping(value="/mps/jobs/{jobId}/runs/{runId}/rundetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsRunDetailsByJob(@PathVariable("jobId") Integer jobId, @PathVariable("runId") Integer runId, ModelMap m) throws SampleTypeException {

	  Run run = runService.getRunById(runId);
	  List<RunMeta> runMetaList = run.getRunMeta();
	  String runReadLength = "???";
	  String runReadType = "???";
	  String runStartDate = "???";
	  String runEndDate = "???";
	  
	  for(RunMeta runMeta : runMetaList){
		  if(runMeta.getK().contains("readLength")){
			  runReadLength = runMeta.getV();
		  }
		  else if (runMeta.getK().contains("readType")){
			  runReadType = runMeta.getV();			  
		  }
	  }	  
	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	  if(run.getStarted()!=null){
		  runStartDate= dateFormat.format(run.getStarted());
	  }
	  if(run.getFinished()!=null){
		  runEndDate= dateFormat.format(run.getFinished());
	  }
	  	  
	  Sample platformUnit = run.getPlatformUnit();
	  List<SampleMeta> platformUnitMetaList = platformUnit.getSampleMeta();
	  String totalLanesOnPlatformUnit = "???";
	  for(SampleMeta platformUnitMeta : platformUnitMetaList){
		  if(platformUnitMeta.getK().contains("lanecount")){
			  totalLanesOnPlatformUnit = platformUnitMeta.getV();
		  }
	  }
	  m.addAttribute("run", run);
	  m.addAttribute("runReadLength", runReadLength);//actual on run
	  m.addAttribute("runReadType", runReadType);//actual on run
	  m.addAttribute("runStartDate", runStartDate);
	  m.addAttribute("runEndDate", runEndDate);
	  
	  m.addAttribute("platformUnit", platformUnit);
	  m.addAttribute("totalLanesOnPlatformUnit", totalLanesOnPlatformUnit);
	  
	  return "datadisplay/mps/jobs/rundetails";
  }
  
  @RequestMapping(value="/mps/jobs/{jobId}/runs/{runId}/cells/{cellId}/celldetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsCellDetailsByJobByRunByCell(@PathVariable("jobId") Integer jobId, @PathVariable("runId") Integer runId, 
		  @PathVariable("cellId") Integer cellId, ModelMap m) throws SampleTypeException {
	  
	  Run run = runService.getRunById(runId);
	  List<RunMeta> runMetaList = run.getRunMeta();
	  String runReadLength = "???";
	  String runReadType = "???";
	  String runStartDate = "???";
	  String runEndDate = "???";
	  
	  for(RunMeta runMeta : runMetaList){
		  if(runMeta.getK().contains("readLength")){
			  runReadLength = runMeta.getV();
		  }
		  else if (runMeta.getK().contains("readType")){
			  runReadType = runMeta.getV();			  
		  }
	  }	  
	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	  if(run.getStarted()!=null){
		  runStartDate= dateFormat.format(run.getStarted());
	  }
	  if(run.getFinished()!=null){
		  runEndDate= dateFormat.format(run.getFinished());
	  }
	  	  
	  Sample platformUnit = run.getPlatformUnit();
	  List<SampleMeta> platformUnitMetaList = platformUnit.getSampleMeta();
	  String totalLanesOnPlatformUnit = "???";
	  for(SampleMeta platformUnitMeta : platformUnitMetaList){
		  if(platformUnitMeta.getK().contains("lanecount")){
			  totalLanesOnPlatformUnit = platformUnitMeta.getV();
		  }
	  }
	  m.addAttribute("run", run);
	  m.addAttribute("runReadLength", runReadLength);//actual on run
	  m.addAttribute("runReadType", runReadType);//actual on run
	  m.addAttribute("runStartDate", runStartDate);
	  m.addAttribute("runEndDate", runEndDate);
	  
	  m.addAttribute("platformUnit", platformUnit);
	  m.addAttribute("totalLanesOnPlatformUnit", totalLanesOnPlatformUnit);
	  
	  Sample cell = sampleService.getSampleById(cellId);
	  try{
		  m.addAttribute("cellIndex", sampleService.getCellIndex(cell));
	  }catch(Exception e){}
	  
	  
	  //TODO !!!!****$$$$****MUST MUST REALLY SHOULD BE: sampleService.getCellLibrariesThatPassedQCForJob(jobService.getJobByJobId(jobId));
	  Set<SampleSource> cellLibrariesForJobSet = sampleService.getCellLibrariesForJob(jobService.getJobByJobId(jobId));
	  List<Sample> librariesThatPassedQCForThisCellList = new ArrayList<Sample>();
	  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample,Adaptor>();
	  Map<Sample, String> libraryAdaptorSetShortNameMap = new HashMap<Sample,String>();
	  Map<Sample, String> librarypMAddedMap = new HashMap<Sample,String>();
	  Map<Sample, Sample> libraryMacromoleculeMap = new HashMap<Sample,Sample>();
	  Map<Sample, String> libraryOrganismMap = new HashMap<Sample,String>();//if a parent macromoleucle was submitted, species is stored with it's meta; if only a library was submitted, species stored with the library's mete
	  List<Sample> contolLibrariesForThisCell = new ArrayList<Sample>();
	  Map<Sample, List<FileHandle>> librarySequenceFileMap = new HashMap<Sample, List<FileHandle>>();
	  Random randomGenerator = new Random();
	  
	  for(SampleSource ss : cellLibrariesForJobSet){
		  if(sampleService.getCell(ss) == cell){
			  Sample library = sampleService.getLibrary(ss);
			  librariesThatPassedQCForThisCellList.add(library);//this is just a fix to display anything now!
			  Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			  libraryAdaptorMap.put(library, adaptor);
			  libraryAdaptorSetShortNameMap.put(library, adaptor.getAdaptorset().getName().split("\\s+")[0]);
			  List<SampleSourceMeta> sampleSourceMetaList = ss.getSampleSourceMeta();
			  String pMApplied = "???";
			  for(SampleSourceMeta ssm : sampleSourceMetaList){
				  if(ssm.getK().contains("libConcInCellPicoM")){
					  pMApplied = ssm.getV();
				  }
			  }
			  librarypMAddedMap.put(library, pMApplied);
			  Sample parentMacromolecule = library.getParent();
			  if(parentMacromolecule!=null && parentMacromolecule.getId()!=null){
				  libraryMacromoleculeMap.put(library, parentMacromolecule);
				  libraryOrganismMap.put(library, sampleService.getNameOfOrganism(parentMacromolecule, "???"));
			  }
			  else{
				  libraryOrganismMap.put(library, sampleService.getNameOfOrganism(library, "???"));
			  }
			  //will also require (from samplesource) the passfilter reads
			  
			  //sequence files
			  //MUST KNOW WHICH IS pair1 and pair2 if this is a paired end read
			  List<FileHandle> sequenceFileList = new ArrayList<FileHandle>();			  
			  FileHandle fileHandle = fileService.getFileHandleById(randomGenerator.nextInt(30) + 1);
			  sequenceFileList.add(fileHandle);
			  if("paired".equalsIgnoreCase(runReadType)){
				  FileHandle fileHandle2 = fileService.getFileHandleById(26);//(randomGenerator.nextInt(30) + 1);
				  sequenceFileList.add(fileHandle2);
			  }
			  librarySequenceFileMap.put(library, sequenceFileList);
		  }
	  }
	  
	  //any controls on this lane?
	  List<Sample> controlLibrariesForThisCellList = sampleService.getControlLibrariesOnCell(cell);
	  if(!controlLibrariesForThisCellList.isEmpty()){
		  for(Sample controlLibrary : controlLibrariesForThisCellList){
			  Adaptor adaptor = sampleService.getLibraryAdaptor(controlLibrary);
			  if(adaptor != null && adaptor.getId()!=null){
				  libraryAdaptorMap.put(controlLibrary, adaptor);
				  libraryAdaptorSetShortNameMap.put(controlLibrary, adaptor.getAdaptorset().getName().split("\\s+")[0]);
			  }
			  SampleSource ss = sampleService.getCellLibrary(cell, controlLibrary);
			  List<SampleSourceMeta> sampleSourceMetaList = ss.getSampleSourceMeta();
			  String pMApplied = "???";
			  for(SampleSourceMeta ssm : sampleSourceMetaList){
				  if(ssm.getK().contains("libConcInCellPicoM")){
					  pMApplied = ssm.getV();
				  }
			  }
			  librarypMAddedMap.put(controlLibrary, pMApplied);
			  libraryOrganismMap.put(controlLibrary, sampleService.getNameOfOrganism(controlLibrary, "???"));
			  //will also require (from samplesource) the passfilter reads
		  }
	  }

	  m.addAttribute("controlLibrariesForThisCellList", controlLibrariesForThisCellList);
	  m.addAttribute("librariesThatPassedQCForThisCellList", librariesThatPassedQCForThisCellList);
	  m.addAttribute("libraryMacromoleculeMap", libraryMacromoleculeMap);
	  m.addAttribute("libraryOrganismMap", libraryOrganismMap);
	  m.addAttribute("libraryAdaptorSetShortNameMap", libraryAdaptorSetShortNameMap);
	  m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
	  m.addAttribute("librarypMAddedMap", librarypMAddedMap);
	  
	  //m.addAttribute("", );passfilter reads
		 	 	 	 	 	 
	  for(Sample library : librariesThatPassedQCForThisCellList){
		  if(libraryMacromoleculeMap.get(library)!=null){
			  System.out.println("Parent: "+ libraryMacromoleculeMap.get(library).getName());
		  }else {System.out.println("Parent: N/A");}		  
		  System.out.println("Lib: "+ library.getName());
		  System.out.println("Species: "+ libraryOrganismMap.get(library));
		  System.out.println("AdaptorSetShortname: "+ libraryAdaptorSetShortNameMap.get(library));
		  System.out.println("Adaptor Index: "+ libraryAdaptorMap.get(library).getBarcodenumber());
		  System.out.println("AdaptorTag: "+ libraryAdaptorMap.get(library).getBarcodesequence());
		  System.out.println("pM applied: "+ librarypMAddedMap.get(library));
		  
		  System.out.println("");
		  
	  }
	  //should order libraries by adaptor index
	  //must get the controls for this lane
	  
	  //must get the sequence files for these lanes
	  m.addAttribute("librarySequenceFileMap", librarySequenceFileMap);
	  
	  return "datadisplay/mps/jobs/celldetails";
  }
  
  @RequestMapping(value="/mps/jobs/{jobId}/runs/{runId}/cells/{cellId}/sequencedetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsSequenceDetailsByJobByRun(@PathVariable("jobId") Integer jobId, @PathVariable("runId") Integer runId, 
		  @PathVariable("cellId") Integer cellId, ModelMap m) throws SampleTypeException {
	  
	  mpsCellDetailsByJobByRunByCell(jobId, runId, cellId, m);
	  return "datadisplay/mps/jobs/sequencedetails";
	  
  }
  
  @RequestMapping(value="/mps/jobs/{jobId}/runs/{runId}/cells/{cellId}/alignmentdetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsAlignmentDetailsByJobByRunByCell(@PathVariable("jobId") Integer jobId, @PathVariable("runId") Integer runId, 
		  @PathVariable("cellId") Integer cellId, ModelMap m) throws SampleTypeException {
	  
	  mpsCellDetailsByJobByRunByCell(jobId, runId, cellId, m);
	  return "datadisplay/mps/jobs/alignmentdetails";
	  
  }
  
  @RequestMapping(value="/mps/jobs/{jobId}/libraries/{libraryId}/librarydetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsLibraryDetailsByJob(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, 
		  ModelMap m) throws SampleTypeException {
	  
	  Job job = jobService.getJobByJobId(jobId);
	  List<Sample> jobSamples = job.getSample();//list of samples in this job; should be submitted macromolecules, submitted libraries, and facility-generated libraries
	  Sample library = sampleService.getSampleById(libraryId);
	  Sample parentMacromolecule = library.getParent();//could be null, if user submitted a user-generated library

	  boolean libraryBelongsToJob = true;
	  if(!jobSamples.contains(library)){//unexpected error
		  libraryBelongsToJob = false;
		  logger.debug("Library with id " + libraryId + " does not appear to belong to job with id " + jobId);
		  System.out.println("Library with id " + libraryId + " does not appear to belong to job with id " + jobId);
		  m.addAttribute("libraryBelongsToJob", libraryBelongsToJob);
		  return "datadisplay/mps/jobs/librarydetails";
	  }

	  m.addAttribute("libraryBelongsToJob", libraryBelongsToJob);
	  m.addAttribute("library", library);
	  m.addAttribute("parentMacromolecule", parentMacromolecule);

	  //metadata for parent macromolecule
	  if(parentMacromolecule!=null){
		  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(parentMacromolecule);
		  List<SampleMeta> normalizedMacromoleculeMetaList = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(parentMacromolecule.getSampleSubtype(), sampleManaged.getAllSampleMeta());
		  m.addAttribute("normalizedMacromoleculeMeta", normalizedMacromoleculeMetaList);
	  }  
	  
	  //metadata for library
	  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(library);	  
	  List<SampleMeta> normalizedLibraryMetaList = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(library.getSampleSubtype(), sampleManaged.getAllSampleMeta());
	  m.addAttribute("normalizedLibraryMeta", normalizedLibraryMetaList );
	  
	  //this is needed for the adaptor and adaptorset meta to be interpreted properly during metadata display
	  List<Adaptorset> adaptorsets = new ArrayList<Adaptorset>();
	  List<Adaptor> adaptors = new ArrayList<Adaptor>();
	  Adaptor adaptor;
	  try{ 
		  adaptor = adaptorService.getAdaptor(library);
		  adaptors.add(adaptor);
		  adaptorsets.add(adaptor.getAdaptorset());
	  }catch(Exception e){}
	  m.addAttribute("adaptorsets", adaptorsets);
	  m.addAttribute("adaptors", adaptors);
	  
	  //this is needed for the organism meta to be interpreted properly during metadata display
	  m.addAttribute("organisms", sampleService.getOrganismsPlusOther());

	  return "datadisplay/mps/jobs/librarydetails";
  }

  @RequestMapping(value="/mps/jobs/{jobId}/samples", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsSampleSummaryByJob(@PathVariable("jobId") Integer jobId, 
		  ModelMap m) throws SampleTypeException {
	  
	  String referer = request.getHeader("Referer");

	  //mpsMainPageByJob(jobId, m);
	  Job job = jobService.getJobByJobId(jobId);
	  if(job.getId()==null){		  
		  logger.warn("Unable to find job in db: " + referer);		  		
		  waspErrorMessage("");
		  return referer;
	  }
	  
	  List<Sample> allJobSamples = job.getSample();//userSubmitted Macro, userSubmitted Library, facilityGenerated Library
	  List<Sample> allJobLibraries = new ArrayList<Sample>();  //could have gotten this from allJobLibraries = jobService.getLibraries(job);
	  List<Sample> submittedMacromoleculeList = new ArrayList<Sample>();
	  List<Sample> submittedLibraryList = new ArrayList<Sample>();
	  List<Sample> facilityLibraryList = new ArrayList<Sample>();
	  
	  List<Sample> submittedObjectList = new ArrayList<Sample>();//could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
	  
	  Map<Sample, List<Sample>> submittedMacromoleculeFacilityLibraryListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, List<Sample>> submittedLibrarySubmittedLibraryListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, String> submittedObjectOrganismMap = new HashMap<Sample, String>();
	  Map<Sample, Adaptorset> libraryAdaptorsetMap = new HashMap<Sample, Adaptorset>();
	  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();
	  	  
	  for(Sample s : allJobSamples){
		  if(s.getParent()==null){
			  if(s.getSampleType().getIName().toLowerCase().contains("library")){
				  submittedLibraryList.add(s);
			  }
			  else{
				  submittedMacromoleculeList.add(s);
			  }
		  }
		  else{
			  facilityLibraryList.add(s);
		  }
	  }
	  
	  //consolidate job's libraries
	  allJobLibraries.addAll(submittedLibraryList);
	  allJobLibraries.addAll(facilityLibraryList);
	  
	  //consolidate job's submitted objects: submitted macromoleucles and submitted libraries
	  //could have gotten this from submittedObjectList = jobService.getSubmittedSamples(job);
	  submittedObjectList.addAll(submittedMacromoleculeList);
	  submittedObjectList.addAll(submittedLibraryList);
	  
	  //for each submittedMacromolecule, get list of it's facility-generated libraries
	  for(Sample macromolecule : submittedMacromoleculeList){
		  submittedMacromoleculeFacilityLibraryListMap.put(macromolecule, macromolecule.getChildren());//could also have used sampleService.getFacilityGeneratedLibraries(macromolecule)
	  }
	  
	  //for each submittedLibrary, get list of it's libraries (done for consistency, as this is actually a list of one, the library itself)
	  for(Sample userSubmittedLibrary : submittedLibraryList){//do this just to get the userSubmitted Library in a list
		  List<Sample> tempUserSubmittedLibraryList = new ArrayList<Sample>();
		  tempUserSubmittedLibraryList.add(userSubmittedLibrary);
		  submittedLibrarySubmittedLibraryListMap.put(userSubmittedLibrary, tempUserSubmittedLibraryList);
	  }
	  
	  //for each submittedMacromolecule or submittedLibrary, get species
	  for(Sample submittedObject : submittedObjectList){
		  submittedObjectOrganismMap.put(submittedObject, sampleService.getNameOfOrganism(submittedObject, "???"));
	  }
	  
	  //for each job's library, get its adaptor info
	  for(Sample library : allJobLibraries){
		  Adaptor adaptor;
		  try{ 
			  adaptor = adaptorService.getAdaptor(library);
			  libraryAdaptorMap.put(library, adaptor);
			  libraryAdaptorsetMap.put(library, adaptor.getAdaptorset()); 
		  }catch(Exception e){}		  
	  }
	  
	  //???want it?? Set<SampleSource> cellLibrariesForJob = sampleService.getCellLibrariesForJob(job);
	  Map<Sample, List<Sample>> libraryCellListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, Integer> cellIndexMap = new HashMap<Sample, Integer>();
	  Map<Sample, Sample> cellPUMap = new HashMap<Sample, Sample>();
	  Map<Sample, Run> cellRunMap = new HashMap<Sample, Run>();	 

	  for(Sample library : allJobLibraries){
		  List<Sample>  cellsForLibrary = sampleService.getCellsForLibrary(library, job);
		  libraryCellListMap.put(library, cellsForLibrary);
		  for(Sample cell : cellsForLibrary){			  
			  try{
				  cellIndexMap.put(cell, sampleService.getCellIndex(cell));
				  Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
				  cellPUMap.put(cell, platformUnit);
				  /////******MUST USE THIS FOR REAL List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST rather than a singleton?
				  List<Run> runList = runService.getRunsForPlatformUnit(platformUnit);
				  if(!runList.isEmpty()){
					  Run run = runList.get(0);
					  cellRunMap.put(cell, run);
					  
				  }
			  }catch(Exception e){}
		  }
	  }
	  

	  Map<Sample, Integer> submittedObjectLibraryRowspan = new HashMap<Sample, Integer>();//number of libraries for each submitted Object (be it a submitted macromolecule or a submitted library)
	  Map<Sample, Integer> submittedObjectCellRowspan = new HashMap<Sample, Integer>();//number of runs (zero, one, many) for each library

	  //calculate the rowspans needed for the web, as the table display is rather complex. 
	  //this is very very hard to do at the web, as there are multiple dependencies. easier to perform here
	  for(Sample submittedObject : submittedObjectList){
		  
		  int numLibraries = 0;
		  int numCells = 0;
		  
		  //calculate numLibraries
		  List<Sample> facilityLibraryList2 = submittedMacromoleculeFacilityLibraryListMap.get(submittedObject);
		  List<Sample> submittedLibraryList2 = submittedLibrarySubmittedLibraryListMap.get(submittedObject);
		  numLibraries = (facilityLibraryList2==null?0:facilityLibraryList2.size()) + (submittedLibraryList2==null?0:submittedLibraryList2.size());
		  
		  //put numLibraries into its map for eventual use in web
		  if(numLibraries==0){
			  submittedObjectLibraryRowspan.put(submittedObject, 1);
		  }
		  else{
			  submittedObjectLibraryRowspan.put(submittedObject, numLibraries);
		  }
		  
		  //calculate numCells
		  if(facilityLibraryList2!=null){
			  for(Sample library : facilityLibraryList2){
				  numCells += libraryCellListMap.get(library)==null?0:libraryCellListMap.get(library).size();
			  }
		  }
		  if(submittedLibraryList2!=null){
			  for(Sample library : submittedLibraryList2){
				  numCells += libraryCellListMap.get(library)==null?0:libraryCellListMap.get(library).size();
			  }
		  }
		  
		  //put numCells into its map for eventual use in web
		  if(numCells==0){
			  submittedObjectCellRowspan.put(submittedObject, 1);
		  }
		  else{
			  submittedObjectCellRowspan.put(submittedObject, numCells);
		  }
	  }
	  
	  m.addAttribute("job",job);
	  //not actually used by webpage   m.addAttribute("submittedLibraryList", submittedLibraryList);
	  //not actually used on webpage m.addAttribute("facilityLibraryList", facilityLibraryList);
	  m.addAttribute("submittedObjectList", submittedObjectList);	//main object list  
	  m.addAttribute("submittedMacromoleculeList", submittedMacromoleculeList);//needed to distinguish submittedMacromoleucle from submitted Library

	  m.addAttribute("submittedMacromoleculeFacilityLibraryListMap", submittedMacromoleculeFacilityLibraryListMap);
	  m.addAttribute("submittedLibrarySubmittedLibraryListMap", submittedLibrarySubmittedLibraryListMap);
	  m.addAttribute("submittedObjectOrganismMap", submittedObjectOrganismMap);
	  m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
	  m.addAttribute("libraryAdaptorsetMap", libraryAdaptorsetMap);

	  m.addAttribute("libraryCellListMap", libraryCellListMap);
	  m.addAttribute("cellIndexMap", cellIndexMap);
	  m.addAttribute("cellPUMap", cellPUMap);//currently not used on web
	  m.addAttribute("cellRunMap", cellRunMap);
	  
	  m.addAttribute("submittedObjectCellRowspan", submittedObjectCellRowspan);
	  m.addAttribute("submittedObjectLibraryRowspan", submittedObjectLibraryRowspan);  
	  
	  return "datadisplay/mps/jobs/samples/mainpage";
  }
  
}

