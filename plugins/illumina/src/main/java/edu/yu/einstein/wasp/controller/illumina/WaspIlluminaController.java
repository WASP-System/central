package edu.yu.einstein.wasp.controller.illumina;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.PlatformUnitController;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.MetaAttribute.Control.Option;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleSubtypeResourceCategory;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Userrole;
import edu.yu.einstein.wasp.sequence.SequenceReadProperties;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.illumina.WaspIlluminaService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl;
import edu.yu.einstein.wasp.util.illumina.IlluminaRunFolderNameParser;

@Controller
@RequestMapping("/wasp-illumina")
public class WaspIlluminaController extends WaspController {
	
	@Autowired
	SampleService sampleService;
	
	@Autowired
	AdaptorService adaptorService;
	
	@Autowired
	ResourceService resourceService;
	
	@Autowired
	PlatformUnitController platformUnitController;
	
	@Autowired
	RunService runService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private MessageServiceWebapp messageService;
	
	@Autowired
	private WaspIlluminaService waspIlluminaService;
	
	

	@RequestMapping(value="/description", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		return "waspillumina/description";
	}
	
	@RequestMapping(value="/task/qc/list", method=RequestMethod.GET)
	public String display(ModelMap m){
		return "waspillumina/task/qc/list";
	}
	
	@RequestMapping(value = "/flowcell/{platformUnitId}/show.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showPlatformUnit(@PathVariable("platformUnitId") Integer platformUnitId, ModelMap m){
		
		Sample platformUnit; 
		try{
			platformUnit = sampleService.getPlatformUnit(platformUnitId);
			m.addAttribute("platformUnitSampleId", platformUnit.getId().toString());
			m.addAttribute("platformUnitSampleSubtypeId", platformUnit.getSampleSubtype().getId().toString());
			setCommonPlatformUnitDisplayInfoModelData(m, platformUnit);
			List<Run> sequenceRuns = platformUnit.getRun();
			m.addAttribute("sequenceRuns", sequenceRuns);
			
			Format formatter = new SimpleDateFormat("yyyy/MM/dd");
			
			Map<Integer, Map<String, String>> runDetails = new HashMap<Integer, Map<String, String>>();
			for(Run sequenceRun : sequenceRuns){
				SequenceReadProperties runReadProperties = new SequenceReadProperties();
				try {
					runReadProperties = SequenceReadProperties.getSequenceReadProperties(sequenceRun, platformUnitController.RUN_INSTANCE_AREA, RunMeta.class);
				} catch (MetadataException e) {
					logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
				}
				
				Map<String,String> detailMap = new HashMap<String, String>();
				
				detailMap.put(SequenceReadProperties.READ_LENGTH_KEY, runReadProperties.getReadLength().toString());
				detailMap.put(SequenceReadProperties.READ_TYPE_KEY, runReadProperties.getReadType());
				
				String dateRunStarted = new String("not set");
				if(sequenceRun.getStarted()!=null){
					try{				
						dateRunStarted = new String(formatter.format(sequenceRun.getStarted()));//yyyy/MM/dd
					}catch(Exception e){}					
				}
				////startDateForRuns.add(dateRunStarted);
				detailMap.put("dateRunStarted", dateRunStarted);
				
				String dateRunEnded = new String("not set");
				if(sequenceRun.getFinished()!=null){					
					try{				
						dateRunEnded = new String(formatter.format(sequenceRun.getFinished()));//yyyy/MM/dd
					}catch(Exception e){}
					
				}
				////endDateForRuns.add(dateRunEnded);
				detailMap.put("dateRunEnded", dateRunEnded);
				
				////statusForRuns.add(new String("???"));
				detailMap.put("runStatus", new String("???"));
				
				runDetails.put(sequenceRun.getId(), detailMap);
			}
			m.addAttribute("runDetails", runDetails);
		}catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error");
			return "redirect:" + request.getHeader("Referer");
		}
	
		
		//10-17-12 the remainder of this page (the items on the flow cell) was not reviewed; it can no doubt do with work
		
		
		//is this flowcell on a run?
		//////10-17-12List<Run> runList = platformUnit.getRun();
		//////10-17-12m.addAttribute("runList", runList);
		//if this flowcell is on a run, it gets locked to the addition of new user-libraries
		//There are conditions under which the flow cell may need to be unlocked. Currently this will be 
		//visible on a the platform unit form only to superuser, who may unlock and later relock the flow cell.
		//Here, just determine if it's locked
		SampleServiceImpl.LockStatus platformUnitLockStatus = SampleServiceImpl.LockStatus.UNKOWN;
		try {
			platformUnitLockStatus = sampleService.getPlatformUnitLockStatus(platformUnit);
		} catch(SampleTypeException e){
			logger.warn(e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error");
			return "redirect:/dashboard.do";
		}
		m.addAttribute("platformUnitLockStatus", platformUnitLockStatus.toString());
		
		Map<Integer, String> technicians = new HashMap<Integer, String>();
		List<User> allUsers = userDao.findAll();
		for(User user : allUsers){
			for(Userrole userrole : user.getUserrole()){
				if(userrole.getRole().getRoleName().equals("fm") || userrole.getRole().getRoleName().equals("ft")){
					technicians.put(userrole.getUser().getId(), user.getNameFstLst());
				}
			}
		}
		m.addAttribute("technicians", technicians);
		
		List<Resource> resourceList= resourceService.getResourceDao().findAll(); 
		List<Resource> filteredResourceList = new ArrayList<Resource>();
		for(Resource resource : resourceList){
			//logger.debug("resource: " + resource.getName());
			for(SampleSubtypeResourceCategory ssrc : resource.getResourceCategory().getSampleSubtypeResourceCategory()){
				if(ssrc.getSampleSubtypeId().intValue() == platformUnit.getSampleSubtypeId().intValue()){
					filteredResourceList.add(resource);
					//logger.debug("it's a match");					
				}
			}
		}
		m.addAttribute("resources", filteredResourceList);
		
		List<Adaptor> allAdaptors = adaptorService.getAdaptorDao().findAll();
		Map<String, Adaptor> adaptorList = new HashMap<String, Adaptor>();
		for(Adaptor adaptor : allAdaptors){
			adaptorList.put(adaptor.getId().toString(), adaptor);
		}
		m.addAttribute("adaptors", adaptorList);
		
		SampleSubtype sampleSubtype = sampleService.getSampleSubtypeDao().getSampleSubtypeByIName("controlLibrarySample");
		if(sampleSubtype.getId().intValue()==0){
			//TODO throw error and get out of here ; probably go to dashboard, but would be best to go back from where you came from
			logger.warn("Unable to find sampleSubtype of controlLibrarySample");
		}
		
		Map<String, Integer> controlFilterMap = new HashMap<String, Integer>();
		controlFilterMap.put("sampleSubtypeId", sampleSubtype.getId());
		List<Sample> controlLibraryList = sampleService.getSampleDao().findByMap(controlFilterMap);
		for(Sample sample : controlLibraryList){
			logger.debug("control: " + sample.getName());
			if(sample.getIsActive().intValue()==0){
				controlLibraryList.remove(sample);
			}
		}
		sampleService.sortSamplesBySampleName(controlLibraryList);
		m.addAttribute("controlLibraryList", controlLibraryList);
		
		m.addAttribute("platformUnit", platformUnit);
		return "wasp-illumina/flowcell/showflowcell";
	}
	
	/*
	 * update sampleSourceMetaData libConcInCellPicoM
	 */
	@RequestMapping(value="/flowcell/updateConcInLane.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateConcInCell(
			@RequestParam("cellLibraryId") Integer cellLibraryId,
			@RequestParam("libConcInCellPicoM") String libConcInCellPicoM,
			@RequestParam("platformUnitId") Integer platformUnitId,
			ModelMap m) {
		logger.debug("sampleSourceMetaId=" + cellLibraryId);
		logger.debug("libConcInCellPicoM=" + libConcInCellPicoM);
		logger.debug("platformUnitId=" + platformUnitId);
		//TODO confirm parameters
		//confirm libConcInCellPicoM is integer or float
		//confirm platformUnitId is Id of sample that is a platformUnit
		//confirm that sampleSourceMetaId exists and k == "libConcInCellPicoM"
		try {
			sampleService.setLibraryOnCellConcentration(sampleService.getSampleSourceDao().getById(cellLibraryId), Float.parseFloat(libConcInCellPicoM));
		} catch (Exception e) {
			logger.warn("Problem occurred setting library concentration on cell: " + e.getLocalizedMessage());
			waspErrorMessage("platformunit.libUpdated.error");
		}
		return "redirect:/wasp-illumina/flowcell/" + platformUnitId + "/show.do";

	}
	
	/*
	 * addNewControlToCell
	 */
	@RequestMapping(value="/flowcell/addNewControlToLane.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String addNewControlToCell(
			@RequestParam("platformUnitId") String platformUnitId,
			@RequestParam("cellId") Integer cellId,
			@RequestParam("newControlId") Integer newControlId,	
			@RequestParam("newControlConcInCellPicoM") String newControlConcInCellPicoM,
			ModelMap m){
		
		Sample controlLibrary = sampleService.getSampleById(newControlId);
		Sample cell = sampleService.getSampleById(cellId);
		try {
			sampleService.addLibraryToCell(cell, controlLibrary, Float.parseFloat(newControlConcInCellPicoM));
		} catch (Exception e) {
			logger.warn("Problem adding library to cell: " + e.getLocalizedMessage());
			waspErrorMessage("platformunit.libAdded.error");
		}

		return "redirect:/wasp-illumina/flowcell/" + platformUnitId + "/show.do";
	}
	
	@RequestMapping(value="/flowcell/ajaxReadType.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public @ResponseBody String ajaxReadType(@RequestParam("resourceId") String resourceId){
		//logger.debug("in ajaxReadType and resourceId = " + resourceId);
		String returnString;
		StringBuffer readType = new StringBuffer("<option value=''>---SELECT A READ TYPE---</option>");
		StringBuffer readLength = new StringBuffer("<option value=''>---SELECT A READ LENGTH---</option>");
		Resource resource;
		resource = resourceService.getResourceDao().getResourceByResourceId(new Integer(resourceId));
		ResourceCategory resourceCategory = resource.getResourceCategory();
		for(Option som : resourceService.getResourceCategorySelectOptions(resourceCategory, SequenceReadProperties.READ_TYPE_KEY))
			readType.append("<option value='"+som.getValue()+"'>"+som.getLabel()+"</option>");
		for(Option som : resourceService.getResourceCategorySelectOptions(resourceCategory, SequenceReadProperties.READ_LENGTH_KEY))
			readLength.append("<option value='"+som.getValue()+"'>"+som.getLabel()+"</option>");
		returnString = new String(readType + "****" + readLength);
		//logger.debug("The return string = " + returnString);
		//return "<option value=''>---SELECT A READ TYPE---</option><option value='single'>single</option><option value='paired'>paired</option>";
		return returnString;
	}
	
	private void setCommonPlatformUnitDisplayInfoModelData(ModelMap m, Sample platformUnit) throws SampleException, MetadataException{
		if (platformUnit.getId() == null)
			throw new SampleIndexException("platform unit Id provided does not match a known sample");
		if (!sampleService.isPlatformUnit(platformUnit))
			throw new SampleTypeException("sample is not platformunit");
		String barcode = platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode();
		m.addAttribute("typeOfPlatformUnit", platformUnit.getSampleSubtype().getName());
		m.addAttribute("barcodeName", barcode);
		m.addAttribute("numberOfCellsOnThisPlatformUnit", sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString());
		SequenceReadProperties readProperties = new SequenceReadProperties();
		try {
			readProperties = SequenceReadProperties.getSequenceReadProperties(platformUnit, platformUnitController.PLATFORM_UNIT_INSTANCE_AREA, SampleMeta.class);
		} catch (MetadataException e) {
			logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
		}
		m.addAttribute(SequenceReadProperties.READ_LENGTH_KEY, readProperties.getReadLength());
		m.addAttribute(SequenceReadProperties.READ_TYPE_KEY, readProperties.getReadType());	
		String comment = MetaHelperWebapp.getMetaValue(PlatformUnitController.PLATFORM_UNIT_INSTANCE_AREA, "comment", platformUnit.getSampleMeta());
		m.addAttribute("comment", comment);	
	}
	
	private void setCommonCreateUpdateRunModelData(ModelMap m, Run run, boolean showAll) throws GridException {
		m.addAttribute("run", run);
		m.addAttribute("showAll", showAll);
		Sample platformUnit = run.getPlatformUnit();
		
		Set<String> runFolderSet = new LinkedHashSet<String>();
		if (showAll){
			for (String runFolder : waspIlluminaService.getIlluminaRunFolders()){
				Map<String, String> searchMap = new HashMap<String, String>();
				searchMap.put("name", runFolder);
				if (runService.getRunDao().findByMap(searchMap).isEmpty() || (run.getName() != null && run.getName().equals(runFolder)))
					runFolderSet.add(runFolder); // only record folders not already associated with runs in WASP
			}
		} else {
			for (String runFolder : waspIlluminaService.getIlluminaRunFolders())
				if ( runFolder.toUpperCase().contains(platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode().toUpperCase()) )
					runFolderSet.add(runFolder);
		}
		m.addAttribute("runFolderSet", runFolderSet);
		
		Resource requestedSequencingMachine = run.getResource();
		if (requestedSequencingMachine != null && requestedSequencingMachine.getId() != null){
			m.addAttribute("readLengths", resourceService.getResourceCategorySelectOptions(requestedSequencingMachine.getResourceCategory(), SequenceReadProperties.READ_LENGTH_KEY));
			m.addAttribute("readTypes", resourceService.getResourceCategorySelectOptions(requestedSequencingMachine.getResourceCategory(), SequenceReadProperties.READ_TYPE_KEY));
			m.addAttribute("technicians", userService.getFacilityTechnicians());
		}
	}
	

	
	@RequestMapping(value="/flowcell/{platformUnitId}/run/create.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createRunGet(
			@PathVariable("platformUnitId") Integer platformUnitId,
			@RequestParam(value="runFolderName", defaultValue="", required=false) String runFolderName,
			@RequestParam(value="showAll", defaultValue="false", required=false) boolean showAll,
			ModelMap m) {	
		
		try{
			Sample platformUnit = sampleService.getPlatformUnit(platformUnitId);
			if (platformUnit.getId() == null)
				throw new SampleIndexException("platform unit Id provided does not match a known sample");
			if (!sampleService.isPlatformUnit(platformUnit))
				throw new SampleTypeException("sample is not platformunit");
			setCommonPlatformUnitDisplayInfoModelData(m, platformUnit);
			Run run = new Run();
			run.setPlatformUnit(platformUnit);
			
			MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp(PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
			run.setRunMeta(metaHelperWebapp.getMasterList(RunMeta.class));
			if (!runFolderName.isEmpty()){
				IlluminaRunFolderNameParser runFolderParser = new IlluminaRunFolderNameParser(runFolderName);
				run.setName(runFolderParser.getRunFolderName());
				run.setStarted(runFolderParser.getDate());
				Resource resource = resourceService.getResourceDao().getResourceByName(runFolderParser.getMachineName());
				if (resource.getId() == null){
					m.addAttribute("resourceNameError", messageService.getMessage(metaHelperWebapp.getArea()+".resourceNameNotFound.error"));
					resource.setName(runFolderParser.getMachineName());
				} 
				run.setResource(resource);
			}
			setCommonCreateUpdateRunModelData(m, run, showAll);
			m.addAttribute("action", "create");
			
		}catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/wasp-illumina/flowcell/" + platformUnitId + "/show.do";
		}

		return "wasp-illumina/flowcell/createupdaterun";

	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/flowcell/{platformUnitId}/run/create.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createPost(
			@PathVariable("platformUnitId") Integer platformUnitId,
			@RequestParam(value="showAll", defaultValue="false", required=false) boolean showAll,
			@Valid Run runForm, 
			 BindingResult result,
			 SessionStatus status, 		
			ModelMap m) throws MetadataException {
	
		MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp(PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
		//check for runInstance.UserId, which cannot be empty 		
		if (runForm.getUserId() == null || runForm.getUserId().intValue() <= 0){
			Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
			errors.rejectValue("userId", metaHelperWebapp.getArea()+".technician.error", metaHelperWebapp.getArea()+".technician.error (no message has been defined for this property)");
			result.addAllErrors(errors);
		}
		metaHelperWebapp.getFromRequest(request, RunMeta.class);
		metaHelperWebapp.validate(result);
		try{
			Sample platformUnit = sampleService.getPlatformUnit(platformUnitId);
			if (platformUnit.getId() == null)
				throw new SampleIndexException("platform unit Id provided does not match a known sample");
			if (!sampleService.isPlatformUnit(platformUnit))
				throw new SampleTypeException("sample is not platformunit");
			runForm.setPlatformUnit(platformUnit);
			runForm.setRunMeta( (List<RunMeta>) metaHelperWebapp.getMetaList());
			IlluminaRunFolderNameParser runFolderParser = new IlluminaRunFolderNameParser(runForm.getName());
			runForm.setStarted(runFolderParser.getDate());
			Resource resource = resourceService.getResourceDao().getResourceByName(runFolderParser.getMachineName());
			if (resource.getId() == null){
				result.reject(metaHelperWebapp.getArea()+".resourceNameNotFound.error");
				m.addAttribute("resourceNameError", messageService.getMessage(metaHelperWebapp.getArea()+".resourceNameNotFound.error"));
				resource = new Resource();
				resource.setName(runFolderParser.getMachineName());
			} 
			runForm.setResource(resource);
			if (result.hasErrors()){
				setCommonPlatformUnitDisplayInfoModelData(m, platformUnit);
				setCommonCreateUpdateRunModelData(m, runForm, showAll);
				m.addAttribute("action", "create");
				return "wasp-illumina/flowcell/createupdaterun";
			}
			runForm.setResourceCategory(resource.getResourceCategory());
			runService.updateRun(runForm);
		}catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/wasp-illumina/flowcell/" + platformUnitId + "/show.do";
		}
		waspMessage("runInstance.created_success.label");
		return "redirect:/wasp-illumina/flowcell/" + platformUnitId + "/show.do";
	}
	
	@RequestMapping(value="/run/{runId}/update.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateRunGet(
			@PathVariable("runId") Integer runId,
			@RequestParam(value="runFolderName", defaultValue="", required=false) String runFolderName,
			@RequestParam(value="showAll", defaultValue="false", required=false) boolean showAll,
			ModelMap m) {	
		Sample platformUnit = null;
		try{
			Run existingrun = runService.getSequenceRun(runId);//throws exception if not valid mps Run in database 
			platformUnit = existingrun.getPlatformUnit();
			setCommonPlatformUnitDisplayInfoModelData(m, platformUnit);
			MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp(PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
			existingrun.setRunMeta((List<RunMeta>) metaHelperWebapp.syncWithMaster(existingrun.getRunMeta()) );
			setCommonCreateUpdateRunModelData(m, existingrun, showAll);
			m.addAttribute("action", "update");
		}catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/wasp-illumina/flowcell/" + platformUnit.getId() + "/show.do";
		}

		return "wasp-illumina/flowcell/createupdaterun";
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/run/{runId}/update.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateRunPost(
			@PathVariable("runId") Integer runId,
			@RequestParam(value="showAll", defaultValue="false", required=false) boolean showAll,
			@Valid Run runForm, 
			 BindingResult result,
			 SessionStatus status, 		
			ModelMap m) throws MetadataException {
	
		MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp(PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
		//check for runInstance.UserId, which cannot be empty 		
		if (runForm.getUserId() == null || runForm.getUserId().intValue() <= 0){
			Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
			errors.rejectValue("userId", metaHelperWebapp.getArea()+".technician.error", metaHelperWebapp.getArea()+".technician.error (no message has been defined for this property)");
			result.addAllErrors(errors);
		}
		metaHelperWebapp.getFromRequest(request, RunMeta.class);
		metaHelperWebapp.validate(result);
		Sample platformUnit = null;
		try{
			Run existingrun = runService.getSequenceRun(runId);//throws exception if not valid mps Run in database 
			existingrun.setRunMeta((List<RunMeta>) metaHelperWebapp.getMetaList());
			platformUnit = existingrun.getPlatformUnit();
			if (result.hasErrors()){
				setCommonPlatformUnitDisplayInfoModelData(m, platformUnit);
				setCommonCreateUpdateRunModelData(m, existingrun, showAll);
				m.addAttribute("action", "update");
				return "wasp-illumina/flowcell/createupdaterun";
			}
			runService.updateRun(existingrun);
		}catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/wasp-illumina/flowcell/" + platformUnit.getId() + "/show.do";
		}
		waspMessage("runInstance.updated_success.label");
		return "redirect:/wasp-illumina/flowcell/" + platformUnit.getId() + "/show.do";
	}
	
	
	
	/*
	@RequestMapping(value = "/flowcell/showFlowcell/createNewRun.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createNewRun(@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam("runName") String runName,
			@RequestParam("resourceId") Integer resourceId,
			@RequestParam(SequenceReadProperties.READ_LENGTH_KEY) String readLength,
			@RequestParam(SequenceReadProperties.READ_TYPE_KEY) String readType,
			@RequestParam("technicianId") Integer technicianId,
			@RequestParam("runStartDate") String runStartDate,
			@RequestParam(value="runId", required=false) Integer runId,
			ModelMap m){
		
		String return_string = "redirect:/wasp-illumina/flowcell/showFlowcell/" + platformUnitId + ".do";
		//must confirm validity of parameters
		//must add success or failure messages
		
		//first, is the flowcell (via the platformUnitId, such as an Illumina Flowcell Version 3) compatible with the resourceId (the machine instance, such as an Illumina HiSeq 2000)
		Sample platformUnit = sampleService.getSampleDao().getSampleBySampleId(platformUnitId);
		if(platformUnit.getId() == null){
			//message unable to find platform unit record
			logger.warn("unable to find platform unit record");
			return "redirect:/dashboard.do";
		}
		//confirm flowcell (platformUnit)
		if( !sampleService.isPlatformUnit(platformUnit)){
			//message - not a flow cell
			logger.warn("Not a flow cell");
			return return_string;
		}
		//record for machine exists
		Resource machineInstance = resourceService.getResourceDao().getResourceByResourceId(resourceId);
		if(machineInstance.getId().intValue() == 0){
			//message: unable to find record for requested machine
			logger.warn("unable to find record for requested sequencing machine");
			return return_string;
		}
		//confirm the machine and the flow cell are compatible (via sampleSubtpeResourceCategory)
		boolean platformUnitAndMachineAreCompatible = false;
		for(SampleSubtypeResourceCategory ssrc : machineInstance.getResourceCategory().getSampleSubtypeResourceCategory()){
			if(ssrc.getSampleSubtypeId().intValue() == platformUnit.getSampleSubtypeId().intValue()){
				platformUnitAndMachineAreCompatible = true;
			}
		}
		if(platformUnitAndMachineAreCompatible==false){
			//message Platform Unit (flowcell) and Resource (sequencing machine) are Not compatible
			logger.warn("Platform Unit (flowcell) and Resource (sequencing machine) are Not compatible");
			return return_string;
		}
		Integer cellCount = null;
		try {
			cellCount = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit);
		} catch (SampleTypeException e1) {
			logger.warn(e1.getLocalizedMessage());
			return return_string;
		}
		if(cellCount == null){
			logger.warn("Unable to capture platformUnit cellcount from sampleMetaData");
			return return_string;
		}
		if(cellCount != platformUnit.getSampleSource().size()){
			logger.warn("cellcount from sampleMetaData and from samplesource are discordant: unable to continue");
			return return_string;
		}
		//confirm machine and parameters readLength and readType are compatible
		boolean readTypeIsValid = false;
		boolean readLengthIsValid = false;
		ResourceCategory resourceCategory = machineInstance.getResourceCategory();
		List<ResourceCategoryMeta> resourceCategoryMetaList = resourceCategory.getResourceCategoryMeta();
		for(ResourceCategoryMeta rcm : resourceCategoryMetaList){
			if( rcm.getK().indexOf(SequenceReadProperties.READ_TYPE_KEY) > -1 ){
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
				for(String token : tokens){//token could be single:single
					String [] innerTokens = token.split(":");
					for(String str : innerTokens){
						if(readType.equals(str)){
							readTypeIsValid = true;
						}
					}
				}
			}
			if( rcm.getK().indexOf(SequenceReadProperties.READ_LENGTH_KEY) > -1 ){
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be 50:50;100:100
				for(String token : tokens){//token could be 50:50
					String [] innerTokens = token.split(":");
					for(String str : innerTokens){
						if(readLength.equals(str)){
							readLengthIsValid = true;
						}
					}
				}
			}
		}
		if(readTypeIsValid == false){
			logger.warn("Readtype incompatible with selected machine: unable to continue");
			return return_string;			
		}
		if(readLengthIsValid == false){
			logger.warn("Readlength incompatible with selected machine: unable to continue");
			return return_string;			
		}		
		if(runName.trim() == ""){
			logger.warn("Run name is Not valid");
			return return_string;			
		}

		//check technician
		User tech = userDao.getUserByUserId(technicianId);
		List<Userrole> userRoleList = tech.getUserrole();
		boolean userIsTechnician = false;
		for(Userrole ur : userRoleList){
			if(ur.getRole().getRoleName().equals("ft") || ur.getRole().getRoleName().equals("fm")){
				userIsTechnician = true;
			}
		}
		if(userIsTechnician == false){
			logger.warn("Selected Technical Personnel is NOT listed as technician or manager");
			return return_string;				
		}
		
		Date dateStart;
		try{
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			dateStart = (Date)formatter.parse(runStartDate);  
		}
		catch(Exception e){
			logger.warn("Start Date format must be yyyy/MM/dd.");
			return return_string;	
		}
		
		//new run
		if(runId==null){//new run
			try {
				runService.initiateRun(runName, machineInstance, platformUnit, tech, readLength, readType, dateStart);
				//With the creation of this new sequence run record, we want to make it that the flow cell on this
				//sequence run is no longer able to accept additional User libraries:
				sampleService.setPlatformUnitLockStatus(platformUnit, SampleServiceImpl.LockStatus.LOCKED);
				waspMessage("runInstance.created_success.label");
			} catch (MetadataException e){
				waspErrorMessage("platformunit.locking.error");
				logger.warn(e.getLocalizedMessage());
			} catch (SampleTypeException e) {
				waspErrorMessage("platformunit.notFoundOrNotCorrectType.error");
				logger.warn(e.getLocalizedMessage());
			} catch (MessagingException e) {
				waspErrorMessage("wasp.integration_message_send.error");
				logger.warn(e.getLocalizedMessage());
			}
		}
		else if(runId != null){//update run
			Run run = runService.getRunDao().getRunByRunId(runId);
			if(run.getId().intValue()==0){
				//unable to locate run record; 
				logger.warn("Update Failed: Unable to locate Sequence Run record");
				return return_string;
			}
			//confirm that the platformUnit on this run is actually the same platformUnit passed in via parameter platformUnitId
			List<Run> runList = platformUnit.getRun();
			if(runList.size() == 0){
				//platformUnit referenced through parameter platformUnitId is NOT on any sequence run
				logger.warn("Update Failed: Platform Unit " + platformUnit.getId() + " is not on any sequence run");
				return return_string;
			}
			if(runList.get(0).getId().intValue() != run.getId().intValue()){
				//platformUnit referenced through parameter platformUnitId is NOT on part of This sequence run
				logger.warn("Update Failed: Platform Unit " + platformUnit.getId() + " is not part of this sequence run");
				return return_string;
			}
			runService.updateRun(run, runName, machineInstance, platformUnit, tech, readLength, readType, dateStart);
			waspMessage("run.updated_success.label");
			logger.warn("Sequence run has been updated now: runStDate = " + runStartDate);
		}
		
		return "redirect:/wasp-illumina/flowcell/showFlowcell/" + platformUnitId + ".do";
	}
	
	*/
	

}
