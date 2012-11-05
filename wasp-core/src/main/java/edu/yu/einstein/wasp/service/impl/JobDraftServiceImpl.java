/**
 *
 * JobDraftServiceImpl.java 
 * @author dubin
 *  
 * the JobDraftService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import edu.yu.einstein.wasp.util.MetaHelper;
//import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.dao.JobDraftSoftwareDao;
import edu.yu.einstein.wasp.dao.JobDraftresourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftJobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.WorkflowResourceTypeDao;
import edu.yu.einstein.wasp.dao.WorkflowSoftwareDao;
import edu.yu.einstein.wasp.dao.WorkflowresourcecategoryDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.MetadataTypeException;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCellSelection;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftSoftware;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.LabUser;
import edu.yu.einstein.wasp.model.MetaAttribute;
import edu.yu.einstein.wasp.model.MetaBase;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.model.WorkflowMeta;
import edu.yu.einstein.wasp.model.WorkflowResourceType;
import edu.yu.einstein.wasp.model.WorkflowSoftware;
import edu.yu.einstein.wasp.model.Workflowresourcecategory;
import edu.yu.einstein.wasp.model.WorkflowresourcecategoryMeta;
import edu.yu.einstein.wasp.model.WorkflowsoftwareMeta;
import edu.yu.einstein.wasp.resourcebundle.DBResourceBundle;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.SampleService;
//import edu.yu.einstein.wasp.taglib.JQFieldTag;
import edu.yu.einstein.wasp.util.MetaHelper;

//
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.FileDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.StateDao;
import edu.yu.einstein.wasp.dao.StatejobDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.model.File;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCellSelection;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCellSelection;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftSoftware;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.util.StringHelper;

@Service
public class JobDraftServiceImpl extends WaspServiceImpl implements JobDraftService {

	@Autowired
	protected AdaptorDao adaptorDao;

	@Autowired
	protected SampleDraftDao sampleDraftDao;

	@Autowired
	protected SampleDraftMetaDao sampleDraftMetaDao;

	@Autowired
	protected SampleDraftJobDraftCellSelectionDao sampleDraftJobDraftCellSelectionDao;

	@Autowired
	protected JobDraftCellSelectionDao jobDraftCellSelectionDao;

	@Autowired
	protected JobDraftDao jobDraftDao;
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public JobDraft getJobDraftById(Integer jobDraftId){
		return jobDraftDao.getJobDraftByJobDraftId(jobDraftId);
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional   /////(propagation=Propagation.REQUIRES_NEW)
	public void createUpdateJobDraftCells(JobDraft jobDraft, Map params){
		List<JobDraftCellSelection> oldJobDraftCellSelections = jobDraft.getJobDraftCellSelection();

		for (JobDraftCellSelection jdc: oldJobDraftCellSelections) {
			List<SampleDraftJobDraftCellSelection> oldSampleDraftJobDraftCellSelections = jdc.getSampleDraftJobDraftCellSelection();
			for (SampleDraftJobDraftCellSelection sdc: oldSampleDraftJobDraftCellSelections) {
				sampleDraftJobDraftCellSelectionDao.remove(sdc);
				sampleDraftJobDraftCellSelectionDao.flush(sdc);
			}
			jobDraftCellSelectionDao.remove(jdc);
			jobDraftCellSelectionDao.flush(jdc);
		}

		List<SampleDraft> samples=jobDraft.getSampleDraft();//sampleDraftDao.getSampleDraftByJobId(jobDraft.getJobDraftId());

		
		int maxColumns = 10;
		try {
			maxColumns = Integer.parseInt(((String[])params.get("jobcells"))[0]);
		} catch (Exception e) {
		}

		//List<String> checkedList = new ArrayList<String>();

		int cellindex = 0;

		for (int i = 1; i <= maxColumns; i++) {
			int libraryindex = 0;
			boolean cellfound = false;

			JobDraftCellSelection thisJobDraftCellSelection = new JobDraftCellSelection();
			thisJobDraftCellSelection.setJobdraftId(jobDraft.getJobDraftId());
			thisJobDraftCellSelection.setCellIndex(cellindex + 1);

			for (SampleDraft sd: samples) {
				String checked = "0";
				try {
					checked = ((String[])params.get("sdc_" + sd.getSampleDraftId() + "_" + i ))[0];
				} catch (Exception e) {
				}

				if (checked == null || checked.equals("0")) {
					continue;
				}

				if (! cellfound) {
					cellfound = true;
					cellindex++;

					JobDraftCellSelection jobDraftCellSelectionDb = jobDraftCellSelectionDao.save(thisJobDraftCellSelection);
					thisJobDraftCellSelection = jobDraftCellSelectionDb;

					jobDraftCellSelectionDao.flush(thisJobDraftCellSelection);
				}

				libraryindex++;

				SampleDraftJobDraftCellSelection sampleDraftJobDraftCellSelection = new SampleDraftJobDraftCellSelection();

				sampleDraftJobDraftCellSelection.setJobDraftCellSelectionId(thisJobDraftCellSelection.getJobDraftCellSelectionId());
				sampleDraftJobDraftCellSelection.setSampledraftId(sd.getSampleDraftId());
				sampleDraftJobDraftCellSelection.setLibraryIndex(libraryindex);

				SampleDraftJobDraftCellSelection sampleDraftJobDraftCellSelectionDb = sampleDraftJobDraftCellSelectionDao.save(sampleDraftJobDraftCellSelection);
				sampleDraftJobDraftCellSelectionDao.flush(sampleDraftJobDraftCellSelectionDb);
				// checkedList.add("sdc_" + sd.getSampleDraftId() + "_" + i + " " + cellindex + " " + libraryindex);

			}
		}
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	public Map<Integer, List<SampleDraft>> convertWebCellsToMapCells(Map params, List<SampleDraft> samplesOnThisJobDraft){
		
		int maxColumns = 100;
		try {
			maxColumns = Integer.parseInt(((String[])params.get("jobcells"))[0]);
		} catch (Exception e) {  }

		Map<Integer, List<SampleDraft>> cellsMap = new HashMap<Integer, List<SampleDraft>>();
		for (int i = 1; i <= maxColumns; i++) {
			List<SampleDraft> draftSamplesOnCellFromWebList = new ArrayList<SampleDraft>();
			for (SampleDraft sd: samplesOnThisJobDraft) {
				String checked = "0";
				try {
					checked = ((String[])params.get("sdc_" + sd.getSampleDraftId() + "_" + i ))[0];
				} catch (Exception e) { }

				if (checked == null || checked.equals("0")) {
					continue;
				}
				else{
					draftSamplesOnCellFromWebList.add(sd);
				}
			}
			cellsMap.put(new Integer(i), draftSamplesOnCellFromWebList);
		}
		return cellsMap;
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	public void validateSampleDraftsOnCellsFromWeb(Map params, JobDraft jobDraft)throws Exception{

		
		JobDraft tempJobDraft = new JobDraft();
		////////
		List<SampleDraft> samples=jobDraft.getSampleDraft();//sampleDraftDao.getSampleDraftByJobId(jobDraft.getJobDraftId());

		
		int maxColumns = 10;
		try {
			maxColumns = Integer.parseInt(((String[])params.get("jobcells"))[0]);
		} catch (Exception e) { throw new Exception("jobDraft.cell_unexpected_error.label"); }

		//List<String> checkedList = new ArrayList<String>();

		Set<SampleDraft> draftSamplesOnAtLeastOneCellSet = new HashSet<SampleDraft>();
		Map<Integer, List<SampleDraft>> cellsMap = new HashMap<Integer, List<SampleDraft>>();
		for (int i = 1; i <= maxColumns; i++) {
			List<SampleDraft> draftSamplesOnCellList = new ArrayList<SampleDraft>();
			for (SampleDraft sd: samples) {
				String checked = "0";
				try {
					checked = ((String[])params.get("sdc_" + sd.getSampleDraftId() + "_" + i ))[0];
				} catch (Exception e) {
				}

				if (checked == null || checked.equals("0")) {
					continue;
				}
				else{
					draftSamplesOnAtLeastOneCellSet.add(sd);
					draftSamplesOnCellList.add(sd);
				}
			}
			cellsMap.put(new Integer(i), draftSamplesOnCellList);
		}
		
		//first check that all draftsamples for this jobdraft appear at least once in draftSamplesOnAtLeastOneCellSet
		Boolean sampleIsMissing = false;
		for(SampleDraft sd : samples){			
			if(!draftSamplesOnAtLeastOneCellSet.contains(sd)){
				sampleIsMissing = true;
				logger.warn("---SampleMissingFromSomeCell is " + sd.getName());
			}
		}
		if(sampleIsMissing){
			throw new Exception("jobDraft.cell_error.label"); 
		}
		
		//second, confirm that for user-submitted libraries, barcodes do NOT appear twice within a cell 
		for (int i = 1; i <= maxColumns; i++) {
			List<SampleDraft> sdList = cellsMap.get(new Integer(i));
			Set<String> adaptorsOnCell = new HashSet<String>();
			for(SampleDraft sd : sdList){
				if( sd.getSampleType().getIName().equals("library") || sd.getSampleType().getIName().equals("facilityLibrary") ){
					String adaptorIdAsString = MetaHelper.getMetaValue("genericLibrary", "adaptor", sd.getSampleDraftMeta());
					Integer adaptorId;
					try{
						adaptorId = Integer.parseInt(adaptorIdAsString);
					}catch(Exception e){throw new Exception("jobDraft.cell_adaptor_error.label");}
					
					Adaptor adaptor = adaptorDao.getAdaptorByAdaptorId(adaptorId);
					logger.warn("SampleLibrary name: " + sd.getName() + "; Barcode: " + adaptor.getBarcodesequence());
					if(adaptor.getAdaptorId()==null || adaptor.getAdaptorId()<=0){
						throw new Exception("jobDraft.cell_adaptor_error.label");
					}
					/////////if(!adaptorsOnCell.contains(adaptor.getBarcodesequence().toUpperCase())){adaptorsOnCell.add(adaptor.getBarcodesequence().toUpperCase());}
					////////////else{throw new Exception("jobDraft.cell_barcode_error.label");}
					if(adaptorsOnCell.contains(adaptor.getBarcodesequence().toUpperCase())){
						throw new Exception("jobDraft.cell_barcode_error.label");
					}
					adaptorsOnCell.add(adaptor.getBarcodesequence().toUpperCase());
				}				
			}
		}
		
		
		
		
		/*
		int cellindex = 0;

		for (int i = 1; i <= maxColumns; i++) {
			int libraryindex = 0;
			boolean cellfound = false;

			JobDraftCellSelection thisJobDraftCellSelection = new JobDraftCellSelection();
			thisJobDraftCellSelection.setJobdraftId(jobDraft.getJobDraftId());
			thisJobDraftCellSelection.setCellIndex(cellindex + 1);

			for (SampleDraft sd: samples) {
				String checked = "0";
				try {
					checked = ((String[])params.get("sdc_" + sd.getSampleDraftId() + "_" + i ))[0];
				} catch (Exception e) {
				}

				if (checked == null || checked.equals("0")) {
					continue;
				}

				if (! cellfound) {
					cellfound = true;
					cellindex++;

					JobDraftCellSelection jobDraftCellSelectionDb = jobDraftCellSelectionDao.save(thisJobDraftCellSelection);
					thisJobDraftCellSelection = jobDraftCellSelectionDb;

					jobDraftCellSelectionDao.flush(thisJobDraftCellSelection);
				}

				libraryindex++;

				SampleDraftJobDraftCellSelection sampleDraftJobDraftCellSelection = new SampleDraftJobDraftCellSelection();

				sampleDraftJobDraftCellSelection.setJobDraftCellSelectionId(thisJobDraftCellSelection.getJobDraftCellSelectionId());
				sampleDraftJobDraftCellSelection.setSampledraftId(sd.getSampleDraftId());
				sampleDraftJobDraftCellSelection.setLibraryIndex(libraryindex);

				SampleDraftJobDraftCellSelection sampleDraftJobDraftCellSelectionDb = sampleDraftJobDraftCellSelectionDao.save(sampleDraftJobDraftCellSelection);
				sampleDraftJobDraftCellSelectionDao.flush(sampleDraftJobDraftCellSelectionDb);
				// checkedList.add("sdc_" + sd.getSampleDraftId() + "_" + i + " " + cellindex + " " + libraryindex);

			}
		}
		*/
		///////
		/////return tempJobDraft;
	}
	
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void confirmAllDraftSamplesOnAtLeastOneCell(Map<Integer, List<SampleDraft>> cellMap, List<SampleDraft> samplesOnThisJobDraft) throws Exception{

		//first, convert sampledraft objects in cellMap into a set of sampledrafts
		Set<SampleDraft> samplesOnCellsFromWebSet = new HashSet<SampleDraft>();
		for(Integer index : cellMap.keySet()){
			List<SampleDraft> sampleDraftList = cellMap.get(index);
			for(SampleDraft sd : sampleDraftList){
				samplesOnCellsFromWebSet.add(sd);
			}
		}
		
		//second, check that samples appear at least once in the set
		Boolean sampleIsMissing = false;
		for(SampleDraft sampleDraft : samplesOnThisJobDraft){
			if(!samplesOnCellsFromWebSet.contains(sampleDraft)){
				sampleIsMissing = true;
				logger.warn("SampleMissingFromSomeCell is " + sampleDraft.getName());//for testing only 
			}
		}
		if(sampleIsMissing){
			throw new Exception("jobDraft.cell_error.label"); 
		}
/*		
		List<SampleDraft> samplesOnJobDraftList = jobDraft.getSampleDraft();
		Set<SampleDraft> samplesOnCellsOnJobDraftSet = new HashSet<SampleDraft>();
		List<JobDraftCellSelection> jobDraftCellSelectionsList = jobDraft.getJobDraftCellSelection();
		for(JobDraftCellSelection jdcs : jobDraftCellSelectionsList){
			List <SampleDraftJobDraftCellSelection> sampleDraftJobCraftCellSelectionList = jdcs.getSampleDraftJobDraftCellSelection();
			for(SampleDraftJobDraftCellSelection sdjdcs : sampleDraftJobCraftCellSelectionList){
				samplesOnCellsOnJobDraftSet.add(sdjdcs.getSampleDraft());
			}
		}
		Boolean sampleIsMissing = false;
		for(SampleDraft sampleDraft : samplesOnJobDraftList){
			if(!samplesOnCellsOnJobDraftSet.contains(sampleDraft)){
				sampleIsMissing = true;
				System.out.println("SampleMissingFromSomeCell iss " + sampleDraft.getName());
				//throw new Exception("jobDraft.cell_error.label"); 
			}
		}
		if(sampleIsMissing){
			throw new Exception("jobDraft.cell_error.label"); 
		}
*/
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void confirmNoBarcodeOverlapPerCell(Map<Integer, List<SampleDraft>> cellMap) throws Exception{
/*		
		List<JobDraftCellSelection> jobDraftCellSelectionsList = jobDraft.getJobDraftCellSelection();//these are the cells
		for(JobDraftCellSelection jdcs : jobDraftCellSelectionsList){
			List <SampleDraftJobDraftCellSelection> sampleDraftJobCraftCellSelectionList = jdcs.getSampleDraftJobDraftCellSelection();//these are the samples on each cell
			Set<String> adaptorsOnCell  = new HashSet<String>();
			for(SampleDraftJobDraftCellSelection sdjdcs : sampleDraftJobCraftCellSelectionList){
				SampleDraft sampleDraft = sdjdcs.getSampleDraft();
				if( sampleDraft.getSampleType().getIName().equals("library") || sampleDraft.getSampleType().getIName().equals("facilityLibrary") ){
					String adaptorIdAsString = MetaHelper.getMetaValue("genericLibrary", "adaptor", sampleDraft.getSampleDraftMeta());
					Integer adaptorId;
					try{
						adaptorId = Integer.parseInt(adaptorIdAsString);
					}catch(Exception e){throw new Exception("jobDraft.cell_adaptor_error.label");}
					
					Adaptor adaptor = adaptorDao.getAdaptorByAdaptorId(adaptorId);
					System.out.println("SampleLibrary namee: " + sampleDraft.getName() + "; Barcode: " + adaptor.getBarcodesequence());
					if(adaptor.getAdaptorId()==null || adaptor.getAdaptorId()<=0){
						throw new Exception("jobDraft.cell_adaptor_error.label");
					}
					if(!adaptorsOnCell.contains(adaptor.getBarcodesequence().toUpperCase())){adaptorsOnCell.add(adaptor.getBarcodesequence().toUpperCase());}
					else{throw new Exception("jobDraft.cell_barcode_error.label");}
				}
			}
		}
*/
		for(Integer index : cellMap.keySet()){
			List<SampleDraft> sdList = cellMap.get(index);
			Set<String> adaptorsOnCell = new HashSet<String>();
			for(SampleDraft sd : sdList){
				if( sd.getSampleType().getIName().equals("library") || sd.getSampleType().getIName().equals("facilityLibrary") ){
					String adaptorIdAsString = MetaHelper.getMetaValue("genericLibrary", "adaptor", sd.getSampleDraftMeta());
					Integer adaptorId;
					try{
						adaptorId = Integer.parseInt(adaptorIdAsString);
					}catch(Exception e){throw new Exception("jobDraft.cell_adaptor_error.label");}
					
					Adaptor adaptor = adaptorDao.getAdaptorByAdaptorId(adaptorId);
					logger.warn("SampleLibrary name: " + sd.getName() + "; Barcode: " + adaptor.getBarcodesequence());
					if(adaptor.getAdaptorId()==null || adaptor.getAdaptorId()<=0){
						throw new Exception("jobDraft.cell_adaptor_error.label");
					}
					/////////if(!adaptorsOnCell.contains(adaptor.getBarcodesequence().toUpperCase())){adaptorsOnCell.add(adaptor.getBarcodesequence().toUpperCase());}
					////////////else{throw new Exception("jobDraft.cell_barcode_error.label");}
					if(adaptorsOnCell.contains(adaptor.getBarcodesequence().toUpperCase())){
						throw new Exception("jobDraft.cell_barcode_error.label");
					}
					adaptorsOnCell.add(adaptor.getBarcodesequence().toUpperCase());
				}				
			}
		}
		
	}
	
}
