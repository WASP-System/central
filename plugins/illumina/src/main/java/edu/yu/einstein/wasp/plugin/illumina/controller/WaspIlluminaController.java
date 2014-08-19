package edu.yu.einstein.wasp.plugin.illumina.controller;

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
import org.springframework.messaging.MessagingException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import edu.yu.einstein.wasp.controller.PlatformUnitController;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleIndexException;
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
import edu.yu.einstein.wasp.plugin.illumina.service.WaspIlluminaService;
import edu.yu.einstein.wasp.plugin.illumina.util.IlluminaRunFolderNameParser;
import edu.yu.einstein.wasp.plugin.mps.SequenceReadProperties;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.MessageServiceWebapp;
import edu.yu.einstein.wasp.service.ResourceService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl;

@Controller
@RequestMapping("/waspIlluminaHiSeq")
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
			boolean lockedStatus = false;
			for(Run sequenceRun : sequenceRuns){
				SequenceReadProperties runReadProperties = new SequenceReadProperties();
				try {
					runReadProperties = SequenceReadProperties.getSequenceReadProperties(sequenceRun, platformUnitController.RUN_INSTANCE_AREA, RunMeta.class);
				} catch (MetadataException e) {
					logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
				}
				
				Map<String,String> detailMap = new HashMap<String, String>();
				
				detailMap.put(SequenceReadProperties.READ_LENGTH_KEY, runReadProperties.getReadLength().toString());
				detailMap.put(SequenceReadProperties.READ_TYPE_KEY, runReadProperties.getReadType().toString());
				
				String dateRunStarted = messageService.getMessage("run.dateNotSet.label");//new String("not set");
				if(sequenceRun.getStarted()!=null){
					try{				
						dateRunStarted = new String(formatter.format(sequenceRun.getStarted()));//yyyy/MM/dd
					}catch(Exception e){dateRunStarted = messageService.getMessage("run.dateNotFormattedProperly.error");}					
				}
				detailMap.put("dateRunStarted", dateRunStarted);
				
				String dateRunEnded = messageService.getMessage("run.dateNotSet.label");//new String("not set");
				if(sequenceRun.getFinished()!=null){					
					try{				
						dateRunEnded = new String(formatter.format(sequenceRun.getFinished()));//yyyy/MM/dd
					}catch(Exception e){dateRunEnded = messageService.getMessage("run.dateNotFormattedProperly.error");}
					
				}
				detailMap.put("dateRunEnded", dateRunEnded);
				
				if (runService.isRunSuccessfullyCompleted(sequenceRun)){
					detailMap.put("runStatus", messageService.getMessage("run.statusCompleted.label"));//Completed
					lockedStatus = true;
				} else if (runService.isRunActive(sequenceRun)){
					detailMap.put("runStatus", messageService.getMessage("run.statusInProgress.label"));//In Progress
					lockedStatus = true;
				} else {
					detailMap.put("runStatus", messageService.getMessage("run.statusUnknown.label"));//Unknown
				}
				
				
				runDetails.put(sequenceRun.getId(), detailMap);
			}
			m.addAttribute("runDetails", runDetails);
			m.addAttribute("runLocked", lockedStatus);
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
		return "waspIlluminaHiSeq/flowcell/showflowcell";
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
		return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";

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
		//as of 7-08-2014, only permit one control per lane
		//the web page should prevent this, but just in case....
		Sample controlLibrary = sampleService.getSampleById(newControlId);
		Sample cell = sampleService.getSampleById(cellId);		
		try {
			List<Sample> controlLibrariesOnCell = sampleService.getControlLibrariesOnCell(cell);
			if(controlLibrariesOnCell.size()==0){
				sampleService.addControlLibraryToCell(cell, controlLibrary, Float.parseFloat(newControlConcInCellPicoM));
			}
			else{//as of 7-08-2014, only permit one control per lane; the web page should prevent this, but just in case....
				logger.warn("Problem adding control library to cell: Only one controlLibrary permitted per lane");
				waspErrorMessage("platformunit.libAdded.error");
			}
		} catch (Exception e) {
			logger.warn("Problem adding control library to cell: " + e.getLocalizedMessage());
			waspErrorMessage("platformunit.libAdded.error");
		}

		return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
	}
	
	@RequestMapping(value="/flowcell/ajaxReadType.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public @ResponseBody String ajaxReadType(@RequestParam("resourceId") String resourceId){
		//logger.debug("in ajaxReadType and resourceId = " + resourceId);
		String returnString;
		StringBuilder readType = new StringBuilder("<option value=''>---SELECT A READ TYPE---</option>");
		StringBuilder readLength = new StringBuilder("<option value=''>---SELECT A READ LENGTH---</option>");
		Resource resource;
		resource = resourceService.getResourceDao().getResourceByResourceId(new Integer(resourceId));
		ResourceCategory resourceCategory = resource.getResourceCategory();
		for(Option som : resourceService.getAllAvailableResourceCategoryOptions(resourceCategory, SequenceReadProperties.READ_TYPE_KEY))
			readType.append("<option value='"+som.getValue()+"'>"+som.getLabel()+"</option>");
		for(Option som : resourceService.getAllAvailableResourceCategoryOptions(resourceCategory, SequenceReadProperties.READ_LENGTH_KEY))
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
	
	private void setCommonCreateUpdateRunModelData(ModelMap m, Run run)  {
		m.addAttribute("run", run);
		Resource requestedSequencingMachine = run.getResource();
		if (requestedSequencingMachine != null && requestedSequencingMachine.getId() != null){
			m.addAttribute("readLengths", resourceService.getAllAvailableResourceCategoryOptions(requestedSequencingMachine.getResourceCategory(), SequenceReadProperties.READ_LENGTH_KEY));
			m.addAttribute("readTypes", resourceService.getAllAvailableResourceCategoryOptions(requestedSequencingMachine.getResourceCategory(), SequenceReadProperties.READ_TYPE_KEY));
			m.addAttribute("technicians", userService.getFacilityTechnicians());
		}
	}
	
	private void setRunFoldersInModel(ModelMap m, Run run, boolean showAll) throws GridException {
		m.addAttribute("showAll", showAll);
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
				if ( runFolder.toUpperCase().contains(run.getPlatformUnit().getSampleBarcode().get(0).getBarcode().getBarcode().toUpperCase()) )
					runFolderSet.add(runFolder);
		}
		m.addAttribute("runFolderSet", runFolderSet);
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
			setCommonCreateUpdateRunModelData(m, run);
			setRunFoldersInModel(m, run, showAll);
			m.addAttribute("action", "create");
			
		} catch(GridException e1){
			logger.warn("Caught unexpected " + e1.getClass().getName() + " exception: " + e1.getMessage());
			waspErrorMessage("waspIlluminaPlugin.runFolderFind.error"); 
			return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
		} catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
		}

		return "waspIlluminaHiSeq/flowcell/createupdaterun";

	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/flowcell/{platformUnitId}/run/create.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createPost(
			@PathVariable("platformUnitId") Integer platformUnitId,
			@RequestParam(value="showAll", defaultValue="false", required=false) boolean showAll,
			@RequestParam(value="isRunStart", defaultValue="true", required=false) boolean isRunStart,
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
				setCommonCreateUpdateRunModelData(m, runForm);
				setRunFoldersInModel(m, runForm, showAll);
				m.addAttribute("action", "create");
				return "waspIlluminaHiSeq/flowcell/createupdaterun";
			}
			runForm.setResourceCategory(resource.getResourceCategory());
			if (isRunStart){
				runService.updateAndInitiateRun(runForm);
				sampleService.setPlatformUnitLockStatus(platformUnit, SampleServiceImpl.LockStatus.LOCKED);
			} else {
				// special case where super user adds a run but doesn't run the analysis. Designed for adding an externally executed run. 
				// In this situation we need to also set all cells to status sequenced success.
				runService.updateRun(runForm);
				for (Sample cell : sampleService.getIndexedCellsOnPlatformUnit(platformUnit).values())
					sampleService.setCellSequencedSuccessfully(cell, true);
			}
		} catch(GridException e1){
			logger.warn("Caught unexpected " + e1.getClass().getName() + " exception: " + e1.getMessage());
			waspErrorMessage("waspIlluminaPlugin.runFolderFind.error"); 
			return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
		} catch(MessagingException e2){
			logger.warn("Caught unexpected " + e2.getClass().getName() + " exception: " + e2.getMessage());
			waspErrorMessage("waspIlluminaPlugin.runInitialize.error"); 
			return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
		} catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
		}
		waspMessage("runInstance.created_success.label");
		return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnitId + "/show.do";
	}
	
	@RequestMapping(value="/run/{runId}/update.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateRunGet(
			@PathVariable("runId") Integer runId,
			@RequestParam(value="runFolderName", defaultValue="", required=false) String runFolderName,
			ModelMap m) {	
		Sample platformUnit = null;
		try{
			Run existingrun = runService.getSequenceRun(runId);//throws exception if not valid mps Run in database 
			platformUnit = existingrun.getPlatformUnit();
			setCommonPlatformUnitDisplayInfoModelData(m, platformUnit);
			MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp(PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
			existingrun.setRunMeta((List<RunMeta>) metaHelperWebapp.syncWithMaster(existingrun.getRunMeta()) );
			setCommonCreateUpdateRunModelData(m, existingrun);
			m.addAttribute("action", "update");
		}catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnit.getId() + "/show.do";
		}

		return "waspIlluminaHiSeq/flowcell/createupdaterun";
	}
	

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/run/{runId}/update.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateRunPost(
			@PathVariable("runId") Integer runId,
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
				setCommonCreateUpdateRunModelData(m, existingrun);
				m.addAttribute("action", "update");
				return "waspIlluminaHiSeq/flowcell/createupdaterun";
			}
			runService.updateRun(existingrun);
		}catch(Exception e){
			logger.warn("Caught unexpected " + e.getClass().getName() + " exception: " + e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnit.getId() + "/show.do";
		}
		waspMessage("runInstance.updated_success.label");
		return "redirect:/waspIlluminaHiSeq/flowcell/" + platformUnit.getId() + "/show.do";
	}


}
