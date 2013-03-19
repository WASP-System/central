package edu.yu.einstein.wasp.controller.illumina;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import edu.yu.einstein.wasp.controller.PlatformUnitController.SelectOptionsMeta;
import edu.yu.einstein.wasp.controller.WaspController;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Resource;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceCategoryMeta;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
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
import edu.yu.einstein.wasp.service.impl.SampleServiceImpl;

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

	@RequestMapping(value="/description", method=RequestMethod.GET)
	public String displayDescription(ModelMap m){
		return "waspillumina/description";
	}
	
	@RequestMapping(value="/task/qc/list", method=RequestMethod.GET)
	public String display(ModelMap m){
		return "waspillumina/task/qc/list";
	}
	
	@RequestMapping(value = "/flowcell/showFlowcell/{platformUnitId}.do", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String showPlatformUnit(@PathVariable("platformUnitId") Integer platformUnitId, ModelMap m){
		
		Sample platformUnit; 
		try{
			platformUnit = sampleService.getPlatformUnit(platformUnitId);
			m.addAttribute("platformUnitSampleId", platformUnit.getId().toString());
			m.addAttribute("platformUnitSampleSubtypeId", platformUnit.getSampleSubtype().getId().toString());
			m.addAttribute("typeOfPlatformUnit", platformUnit.getSampleSubtype().getName());
			m.addAttribute("barcodeName", platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
			m.addAttribute("numberOfCellsOnThisPlatformUnit", sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString());
			SequenceReadProperties readProperties = new SequenceReadProperties();
			try {
				readProperties = SequenceReadProperties.getSequenceReadProperties(platformUnit, platformUnitController.PLATFORM_UNIT_INSTANCE_AREA, SampleMeta.class);
			} catch (MetadataException e) {
				logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
			}
			m.addAttribute("readlength", readProperties.getReadLength());
			m.addAttribute("readType", readProperties.getReadType());	
			String comment = MetaHelperWebapp.getMetaValue(PlatformUnitController.PLATFORM_UNIT_INSTANCE_AREA, "comment", platformUnit.getSampleMeta());
			m.addAttribute("comment", comment);			
			
			List<Run> sequenceRuns = platformUnit.getRun();
			m.addAttribute("sequenceRuns", sequenceRuns);
			
			Format formatter = new SimpleDateFormat("yyyy/MM/dd");
			
			Map<Integer, Map<String, String>> runDetails = new HashMap<Integer, Map<String, String>>();
			for(Run sequenceRun : sequenceRuns){
				SequenceReadProperties runReadProperties = new SequenceReadProperties();
				try {
					runReadProperties = SequenceReadProperties.getSequenceReadProperties(sequenceRun, RunMeta.class);
				} catch (MetadataException e) {
					logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
				}
				
				Map<String,String> detailMap = new HashMap<String, String>();
				
				detailMap.put("readlength", runReadProperties.getReadLength().toString());
				detailMap.put("readType", runReadProperties.getReadType());
				
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
			logger.warn(e.getMessage());
			waspErrorMessage("wasp.unexpected_error.error");
			return "redirect:/dashboard.do";
		}
	
		
		//10-17-12 the remainder of this page (the items on the flow cell) was not reviewed; it can no doubt do with work
		
		
		//is this flowcell on a run?
		//////10-17-12List<Run> runList = platformUnit.getRun();
		//////10-17-12m.put("runList", runList);
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
		m.put("platformUnitLockStatus", platformUnitLockStatus.toString());
		
		Map<Integer, String> technicians = new HashMap<Integer, String>();
		List<User> allUsers = userDao.findAll();
		for(User user : allUsers){
			for(Userrole userrole : user.getUserrole()){
				if(userrole.getRole().getRoleName().equals("fm") || userrole.getRole().getRoleName().equals("ft")){
					technicians.put(userrole.getUser().getId(), user.getNameFstLst());
				}
			}
		}
		m.put("technicians", technicians);
		
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
		m.put("resources", filteredResourceList);
		
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
		
		m.put("platformUnit", platformUnit);
		return "wasp-illumina/flowcell/showflowcell";
	}
	
	/*
	 * update sampleSourceMetaData libConcInCellPicoM
	 */
	@RequestMapping(value="/flowcell/showFlowcell/updateConcInLane.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateConcInCell(
			@RequestParam("sampleSourceMetaId") Integer sampleSourceMetaId,
			@RequestParam("libConcInCellPicoM") String libConcInCellPicoM,
			@RequestParam("platformUnitId") Integer platformUnitId,
			ModelMap m) {
		
		//TODO confirm parameters
		//confirm libConcInCellPicoM is integer or float
		//confirm platformUnitId is Id of sample that is a platformUnit
		//confirm that sampleSourceMetaId exists and k == "libConcInCellPicoM"
		SampleSourceMeta sampleSourceMeta = sampleService.getSampleSourceMetaDao().getSampleSourceMetaBySampleSourceMetaId(sampleSourceMetaId);
		sampleSourceMeta.setV(libConcInCellPicoM);
		return "redirect:/wasp-illumina/flowcell/showFlowcell/" + platformUnitId + ".do";

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
			@RequestParam("newControlConcInLanePicoM") String newControlConcInLanePicoM,
			ModelMap m){
		
		Sample controlLibrary = sampleService.getSampleById(newControlId);
		Sample cell = sampleService.getSampleById(cellId);
		try {
			sampleService.addLibraryToCell(cell, controlLibrary, Float.parseFloat(newControlConcInLanePicoM));
		} catch (Exception e) {
			logger.warn("Problem adding library to cell: " + e.getLocalizedMessage());
		}

		return "redirect:/wasp-illumina/flowcell/showFlowcell/" + platformUnitId + ".do";
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
		for(SelectOptionsMeta som : getResourceCategoryMetaList(resourceCategory, "readType"))
			readType.append("<option value='"+som.getValuePassedBack()+"'>"+som.getValueVisible()+"</option>");
		for(SelectOptionsMeta som : getResourceCategoryMetaList(resourceCategory, "readlength"))
			readLength.append("<option value='"+som.getValuePassedBack()+"'>"+som.getValueVisible()+"</option>");
		returnString = new String(readType + "****" + readLength);
		//logger.debug("The return string = " + returnString);
		//return "<option value=''>---SELECT A READ TYPE---</option><option value='single'>single</option><option value='paired'>paired</option>";
		return returnString;
	}
	

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/flowcell/createUpdateRun.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createUpdateRun(@RequestParam("resourceId") Integer resourceId,
			@RequestParam("runId") Integer runId,
			@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam(value="reset", defaultValue="") String reset,
			ModelMap m) {	
		
		if(platformUnitId.intValue()< 0){
			platformUnitId = new Integer(0);
		}
		if(resourceId.intValue()< 0){
			resourceId = new Integer(0);
		}
		if(runId.intValue()< 0){
			runId = new Integer(0);
		}
		
		Sample platformUnit = null; 
		try{
			Format formatter = new SimpleDateFormat("yyyy/MM/dd");
			String dateRunStarted = new String("");
			String dateRunEnded = new String("COMPLETED_BY_SYSTEM");
			
			platformUnit = sampleService.getPlatformUnit(platformUnitId);
			
			m.addAttribute("typeOfPlatformUnit", platformUnit.getSampleSubtype().getName());
			m.addAttribute("barcodeName", platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
			m.addAttribute("numberOfCellsOnThisPlatformUnit", sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString());
			SequenceReadProperties readProperties = new SequenceReadProperties();
			try {
				readProperties = SequenceReadProperties.getSequenceReadProperties(platformUnit, PlatformUnitController.PLATFORM_UNIT_INSTANCE_AREA, SampleMeta.class);
			} catch (MetadataException e) {
				logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
			}
			m.addAttribute("readlength", readProperties.getReadLength());
			m.addAttribute("readType", readProperties.getReadType());
			String comment = MetaHelperWebapp.getMetaValue(PlatformUnitController.PLATFORM_UNIT_INSTANCE_AREA, "comment", platformUnit.getSampleMeta());
			m.addAttribute("comment", comment);			
			
			List<Resource> resources = sampleService.getSequencingMachinesCompatibleWithPU(platformUnit);
			m.put("resources", resources);
			
			if(resourceId.intValue() > 0){
				
				
				Run runInstance = null;
				MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp(PlatformUnitController.RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
				
				if(runId < 1){//most likely 0
					runInstance = new Run();
					//runInstance.setName("COMPLETED_BY_SYSTEM_" + platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
					runInstance.setName("");
					//for testing the select box only runInstance.setUserId(new Integer(2));
					runInstance.setRunMeta(metaHelperWebapp.getMasterList(RunMeta.class));
				}
				else{
					
					runInstance = sampleService.getSequenceRun(runId);//throws exception if not valid mps Run in database 
					
					metaHelperWebapp.syncWithMaster(runInstance.getRunMeta());
					runInstance.setRunMeta((List<RunMeta>)metaHelperWebapp.getMetaList());
					
					dateRunStarted = new String(formatter.format(runInstance.getStarted()));//yyyy/MM/dd
					
					if(runInstance.getFinished()!=null){
						try{
							dateRunEnded = new String(formatter.format(runInstance.getFinished()));//yyyy/MM/dd
						}catch(Exception e){dateRunEnded=new String("UNEXPECTED PROBLEM WITH DATE");}
					}
					
					if(reset.equals("reset")){//reset permitted only when runId > 0
						resourceId = new Integer(runInstance.getResourceId().intValue());
					}
	
				}
				m.addAttribute(metaHelperWebapp.getParentArea(), runInstance);//metaHelperWebapp.getParentArea() is run
				
				Resource requestedSequencingMachine = sampleService.getSequencingMachineByResourceId(resourceId);
				m.addAttribute("readlengths", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readlength"));
				m.addAttribute("readTypes", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readType"));

				m.addAttribute("technicians", userService.getFacilityTechnicians());
				m.addAttribute("dateRunStarted", dateRunStarted);
				m.addAttribute("dateRunEnded", dateRunEnded);
			}
			
			m.addAttribute("runId", runId);
			m.addAttribute("resourceId", resourceId);
			m.addAttribute("platformUnitId", platformUnit.getId().toString());			
			
		}catch(Exception e){
			logger.warn(e.getMessage());waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/wasp-illumina/flowcell/showFlowcell/" + platformUnitId + ".do";
		}

		return "run/createUpdateRun";

	}
	
	//helper method for createUpdateRun
	private List<SelectOptionsMeta> getResourceCategoryMetaList(ResourceCategory resourceCategory, String metaKey) {
			
		List<SelectOptionsMeta> list = new ArrayList<SelectOptionsMeta>();
		for(ResourceCategoryMeta rcm : resourceCategory.getResourceCategoryMeta()){
			if( rcm.getK().indexOf(metaKey) > -1 ){//such as readlength
				String[] tokens = rcm.getV().split(";");//rcm.getV() will be single:single;paired:paired
				for(String token : tokens){//token could be single:single
					String[] colonTokens = token.split(":");
					list.add(new SelectOptionsMeta(colonTokens[0], colonTokens[1]));							
				}
			}		
		}	
		return list;
	}

	//createUpdateRun - Post
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/flowcell/createUpdateRun.do", method=RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createUpdateRunPost(
			@RequestParam("resourceId") Integer resourceId,
			@RequestParam("runId") Integer runId,
			@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam("dateRunStarted") String dateRunStarted,
			@RequestParam("dateRunEnded") String dateRunEnded,
			@Valid Run runInstance, 
			 BindingResult result,
			 SessionStatus status, 		
			ModelMap m) throws MetadataException {
	
		logger.debug("Inside the createUpdateRun POST");
		//if(1==1){return "redirect:/dashboard.do";}
		Sample platformUnit = null; 
		try{
			
			String action = null;
			if(resourceId==null || resourceId.intValue()<0 || runId == null || runId.intValue()<0 || platformUnitId==null || platformUnitId.intValue()<=0){
				throw new Exception("Unexpected parameter problems 1: createUpdateRun - POST");
			}
			else if(runId.intValue()==0 && (runInstance.getId()==null || runInstance.getId().intValue()==0)){
				action = new String("create");
				logger.debug("create new run");
			}
			else if(runId.intValue()>0 && runInstance.getId()!=null && runInstance.getId().intValue()>0 && runId.intValue()==runInstance.getId().intValue()){
				action = new String("update");
				logger.debug("update existing run");
			}			
			else{
				throw new Exception("Unexpected parameter problems 2: createUpdateRun - POST");
			}
			
			MetaHelperWebapp metaHelperWebapp = new MetaHelperWebapp(platformUnitController.RUN_INSTANCE_AREA, RunMeta.class, request.getSession());
			metaHelperWebapp.getFromRequest(request, RunMeta.class);
			metaHelperWebapp.validate(result);

			boolean otherErrorsExist = false;
			
			 
			//note that @Valid should have  checked for name being the empty, 
			//but it doesn't appear to be working, so I'll test directly) 
			
			if(runInstance.getName().isEmpty() || runInstance.getName().trim().isEmpty()){
				Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
				errors.rejectValue("name", metaHelperWebapp.getArea()+".name.error", metaHelperWebapp.getArea()+".name.error");
				result.addAllErrors(errors);
			}
			if (! result.hasFieldErrors("name")){//also check whether run's name has been used
				Map<String,String> queryMap = new HashMap<String,String>();
				queryMap.put("name", runInstance.getName().trim());
				List<Run> runList = runService.getRunDao().findByMap(queryMap);
				if(runList.size()>0){
					Errors errors=new BindException(result.getTarget(), metaHelperWebapp.getParentArea());
					errors.rejectValue("name", metaHelperWebapp.getArea()+".name_exists.error", metaHelperWebapp.getArea()+".name_exists.error");
					result.addAllErrors(errors);
				}
			}
			
			
			Date dateRunStartedAsDateObject = null;
			
			//check for runInstance.UserId, which cannot be empty 		
			if(runInstance.getUserId()==null || runInstance.getUserId().intValue()<=0){
				String msg = messageService.getMessage(metaHelperWebapp.getArea()+".technician.error");//area here is runInstance
				m.put("technicianError", msg==null?new String("Technician cannot be empty."):msg);
				otherErrorsExist = true;
			}
			//check dateRunStarted
			if(dateRunStarted==null || "".equals(dateRunStarted.trim())){
				String msg = messageService.getMessage(metaHelperWebapp.getArea()+".dateRunStarted.error");//area here is runInstance
				m.put("dateRunStartedError", msg==null?new String("Cannot be empty."):msg);
				otherErrorsExist = true;
			}			
			else{
				try{
		
					Format formatter = new SimpleDateFormat("yyyy/MM/dd");
					dateRunStartedAsDateObject = (Date) formatter.parseObject(dateRunStarted.trim()); 				
				}catch(Exception e){
					String msg = messageService.getMessage(metaHelperWebapp.getArea()+".dateRunStartedFormat.error");//area here is runInstance
					m.put("dateRunStartedError", msg==null?new String("Incorrect Format"):msg);
					otherErrorsExist = true;
				}
			}
			
			
			if (result.hasErrors()||otherErrorsExist){
				
				logger.debug("We see some errors");
				if(otherErrorsExist){logger.debug("other errors exist");}else{logger.debug("other errors DO NOT exist");}
				
				//first deal with filling up info about the platformunit displayed on the left
				
				
				
				m.addAttribute("typeOfPlatformUnit", platformUnit.getSampleSubtype().getName());
				m.addAttribute("barcodeName", platformUnit.getSampleBarcode().get(0).getBarcode().getBarcode());
				m.addAttribute("numberOfCellsOnThisPlatformUnit", sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit).toString());
				
				String area = platformUnitController.PLATFORM_UNIT_INSTANCE_AREA;
				String readlength = MetaHelperWebapp.getMetaValue(area, "readlength", platformUnit.getSampleMeta());
				m.addAttribute("readlength", readlength);
				String readType = MetaHelperWebapp.getMetaValue(area, "readType", platformUnit.getSampleMeta());
				m.addAttribute("readType", readType);	
				String comment = MetaHelperWebapp.getMetaValue(area, "comment", platformUnit.getSampleMeta());
				m.addAttribute("comment", comment);			
							
				//now the run
				//fill up the metadata for the run
				runInstance.setRunMeta((List<RunMeta>) metaHelperWebapp.getMetaList());				
				//DO I NEED THIS Next line??? It seems to be sent back automagically, even if the next line is missing (next line added 10-10-12)
				m.addAttribute(metaHelperWebapp.getParentArea(), runInstance);//metaHelperWebapp.getParentArea() is run

				List<Resource> resources = sampleService.getSequencingMachinesCompatibleWithPU(platformUnit);
				m.put("resources", resources);

				Resource requestedSequencingMachine = sampleService.getSequencingMachineByResourceId(resourceId);
				m.addAttribute("readlengths", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readlength"));
				m.addAttribute("readTypes", getResourceCategoryMetaList(requestedSequencingMachine.getResourceCategory(), "readType"));

				m.addAttribute("technicians", userService.getFacilityTechnicians());
				m.addAttribute("dateRunStarted",dateRunStarted);
				m.addAttribute("dateRunEnded",dateRunEnded);
								
				m.addAttribute("runId", runId);
				m.addAttribute("resourceId", resourceId);
				m.addAttribute("platformUnitId", platformUnit.getId().toString());
				
				return "run/createUpdateRun";				
				
			}//end of if errors
			
			//should really confirm resource is OK, platformunit is ok, resource is OK for the flow cell
			if(action.equals("create")){
				//logger.debug("in create1");
				//if create, then set started to the date in the parameter (currently that parameter does not exit)
				runInstance.setStarted(dateRunStartedAsDateObject);
				runService.createUpdateSequenceRun(runInstance, (List<RunMeta>)metaHelperWebapp.getMetaList(), platformUnitId, resourceId);
				waspMessage("runInstance.created_success.label");
			}
			else if(action.equals("update")){
				//logger.debug("in update1");
				runInstance.setStarted(dateRunStartedAsDateObject);
				runService.createUpdateSequenceRun(runInstance, (List<RunMeta>)metaHelperWebapp.getMetaList(), platformUnitId, resourceId);
				waspMessage("runInstance.updated_success.label");
			}
			else{//action == null
				//logger.debug("in Unexpectedly1");
				throw new Exception("Unexpectedly encountered action whose value is neither create or update in createUpdateRun");
			}
			//logger.debug("end of the POST method");	
			
		}catch(Exception e){
			logger.warn(e.getMessage());waspErrorMessage("wasp.unexpected_error.error"); 
			return "redirect:/wasp-illumina/flowcell/showFlowcell/" + platformUnitId + ".do";
		}

		return "redirect:/wasp-illumina/flowcell/showFlowcell/" + platformUnitId + ".do";
	}
	
	@RequestMapping(value="/flowcell/deleteRun.do", method=RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String deleteRun(@RequestParam("runId") Integer runId,
			ModelMap m) {
		try{
			Run run = sampleService.getSequenceRun(runId);//exception if not msp run or not in db
			sampleService.deleteSequenceRun(run);
			return "redirect:/wasp-illumina/flowcell/showFlowcell/" + run.getPlatformUnit().getId() + ".do";
		}catch(Exception e){
			logger.warn(e.getMessage());waspErrorMessage("wasp.unexpected_error.error");
			return "redirect:/dashboard.do";
		}
	}
	
	/*
	@RequestMapping(value = "/flowcell/showFlowcell/createNewRun.do", method = RequestMethod.POST)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String createNewRun(@RequestParam("platformUnitId") Integer platformUnitId,
			@RequestParam("runName") String runName,
			@RequestParam("resourceId") Integer resourceId,
			@RequestParam("readLength") String readLength,
			@RequestParam("readType") String readType,
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
			if( rcm.getK().indexOf("readType") > -1 ){
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
			if( rcm.getK().indexOf("readlength") > -1 ){
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
