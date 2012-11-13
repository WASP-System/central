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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.JobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftJobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.util.MetaHelper;
//import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
//import edu.yu.einstein.wasp.taglib.JQFieldTag;
//

@Service
public class JobDraftServiceImpl extends WaspServiceImpl implements JobDraftService {
	
	final private int DEFAULT_MAX_COLUMNS = 25;

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
	public Map<Integer, List<SampleDraft>> convertWebCellsToMapCells(Map params, List<SampleDraft> samplesOnThisJobDraft){
		
		int maxColumns = DEFAULT_MAX_COLUMNS;
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
	public void confirmAllDraftSamplesOnAtLeastOneCell(Map<Integer, List<SampleDraft>> cellMap, List<SampleDraft> samplesOnThisJobDraft) throws Exception{

		//first, convert sampledraft objects (from the web) in cellMap into a set of sampledrafts
		Set<SampleDraft> samplesOnCellsFromWebSet = new HashSet<SampleDraft>();
		for(Integer index : cellMap.keySet()){
			List<SampleDraft> sampleDraftList = cellMap.get(index);
			for(SampleDraft sd : sampleDraftList){
				samplesOnCellsFromWebSet.add(sd);
			}
		}
		
		//second, check that samples appear at least once in the set
		for(SampleDraft sampleDraft : samplesOnThisJobDraft){
			if(!samplesOnCellsFromWebSet.contains(sampleDraft)){
				throw new Exception("jobDraft.cell_error.label");
			}
		}
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void confirmNoBarcodeOverlapPerCell(Map<Integer, List<SampleDraft>> cellMap) throws Exception{

		for(Integer index : cellMap.keySet()){//for each cell
			List<SampleDraft> sdList = cellMap.get(index);
			Set<String> adaptorsOnCell = new HashSet<String>();
			for(SampleDraft sd : sdList){
				if( sd.getSampleType().getIName().equals("library") || sd.getSampleType().getIName().equals("facilityLibrary") ){//include facilityLib incase this is a resubmit
					String adaptorIdAsString = MetaHelper.getMetaValue("genericLibrary", "adaptor", sd.getSampleDraftMeta());
					Integer adaptorId;
					try{
						adaptorId = Integer.parseInt(adaptorIdAsString);
					}catch(Exception e){throw new Exception("jobDraft.cell_adaptor_error.label");}
					
					Adaptor adaptor = adaptorDao.getAdaptorByAdaptorId(adaptorId);
					if(adaptor.getAdaptorId()==null || adaptor.getAdaptorId()<=0){
						throw new Exception("jobDraft.cell_adaptor_error.label");
					}
					if(adaptorsOnCell.contains(adaptor.getBarcodesequence().toUpperCase())){
						throw new Exception("jobDraft.cell_barcode_error.label");
					}
					adaptorsOnCell.add(adaptor.getBarcodesequence().toUpperCase());
				}				
			}
		}		
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void confirmNONEBarcodeIsUniquePerCell(Map<Integer, List<SampleDraft>> cellMap) throws Exception{
		for(Integer index : cellMap.keySet()){//for each cell
			List<SampleDraft> sdList = cellMap.get(index);
			int totalNumberOfSamplesOnThisCell = sdList.size();//all samples (macromolecules and user-submitted libraries) on this cell
			if(totalNumberOfSamplesOnThisCell <= 1){
				continue;//no need to check since only one (or zero) samples on this cell
			}
			//next, check all cells that have 2 or more samples per cell. (any NONE barcode found is illegal on these cells!) 
			for(SampleDraft sd : sdList){//check each sample (macromolecule and library) on this cell
				if( sd.getSampleType().getIName().equals("library") || sd.getSampleType().getIName().equals("facilityLibrary") ){//include facilityLib incase this is a resubmit
					String adaptorIdAsString = MetaHelper.getMetaValue("genericLibrary", "adaptor", sd.getSampleDraftMeta());
					Integer adaptorId;
					try{
						adaptorId = Integer.parseInt(adaptorIdAsString);
					}catch(Exception e){throw new Exception("jobDraft.cell_adaptor_error.label");}
					
					Adaptor adaptor = adaptorDao.getAdaptorByAdaptorId(adaptorId);
					if(adaptor.getAdaptorId()==null || adaptor.getAdaptorId()<=0){
						throw new Exception("jobDraft.cell_adaptor_error.label");
					}
					if("NONE".equals(adaptor.getBarcodesequence().toUpperCase()) || adaptor.getBarcodenumber().intValue()==0 ){
						throw new Exception("jobDraft.cell_barcode_NONE_error.label");
					}
				}
			}
		}		
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	@Transactional("entityManager")   
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
		
		int maxColumns = DEFAULT_MAX_COLUMNS;
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
	
	public Map<SampleDraft, Adaptor> getAdaptorsOnSampleDrafts(List<SampleDraft> sampleDraftList){
		Map<SampleDraft, Adaptor> map = new HashMap<SampleDraft, Adaptor>();
		for(SampleDraft sd : sampleDraftList){
			if( sd.getSampleType().getIName().equals("library") ){
				try{
					String adaptorIdAsString = MetaHelper.getMetaValue("genericLibrary", "adaptor", sd.getSampleDraftMeta());
					Integer adaptorId = Integer.parseInt(adaptorIdAsString);				
					Adaptor adaptor = adaptorDao.getAdaptorByAdaptorId(adaptorId);
					if(adaptor.getAdaptorId()!=null && adaptor.getAdaptorId()>0){
						map.put(sd, adaptor);
					}
				}catch(Exception e){}
			}		
		}
		return map;
	}
	
}
