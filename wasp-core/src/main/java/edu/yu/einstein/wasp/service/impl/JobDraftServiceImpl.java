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
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.JobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.dao.SampleDraftDao;
import edu.yu.einstein.wasp.dao.SampleDraftJobDraftCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleDraftMetaDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCellSelection;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.service.JobDraftService;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.MetaHelper;
//import edu.yu.einstein.wasp.controller.util.SampleAndSampleDraftMetaHelper;
//import edu.yu.einstein.wasp.taglib.JQFieldTag;
//

@Service
@Transactional("entityManager")
public class JobDraftServiceImpl extends WaspServiceImpl implements JobDraftService {
	
	final private int DEFAULT_MAX_COLUMNS = 25;
	
	final private String META_MESSAGE_GROUP_FOR_USER_SUBMITTED_COMMENT = "userSubmittedJobComment";
	final private String META_MESSAGE_NAME_FOR_USER_SUBMITTED_COMMENT = "User-submitted Job Comment";
	
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
	
	@Autowired
	protected JobDraftMetaDao jobDraftMetaDao;

	@Autowired
	protected MetaMessageService metaMessageService;
	
	@Autowired
	protected SampleService sampleService;

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
	@SuppressWarnings("rawtypes")
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
				if( sampleService.isLibrary(sd)){//include facilityLib incase this is a resubmit
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
				if( sampleService.isLibrary(sd) ){//include facilityLib incase this is a resubmit
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
	@SuppressWarnings("rawtypes")
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
			thisJobDraftCellSelection.setJobDraftId(jobDraft.getJobDraftId());
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
			if( sampleService.isLibrary(sd) ){
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
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public void saveUserJobDraftComment(Integer jobDraftId, String comment) throws Exception{
		String trimmedComment = comment==null?"":comment;
		try{
			List<MetaMessage> metaMessageList = metaMessageService.read(META_MESSAGE_GROUP_FOR_USER_SUBMITTED_COMMENT, jobDraftId, JobDraftMeta.class, jobDraftMetaDao);
			if(metaMessageList.size()>0){//already exists, so get it. edit it (if trimmedComment.size()>0) or delete it if size==0
				MetaMessage metaMessage = metaMessageList.get(0);//there will only be one
				if("".equals(trimmedComment)){
					metaMessageService.delete(metaMessage, jobDraftId, JobDraftMeta.class, jobDraftMetaDao);
				}
				else{
					metaMessageService.edit(metaMessage, trimmedComment, jobDraftId, JobDraftMeta.class, jobDraftMetaDao);
				}
			}
			else if(metaMessageList.size()==0 && ! "".equals(trimmedComment)){//need to create new entry
				metaMessageService.saveToGroup(META_MESSAGE_GROUP_FOR_USER_SUBMITTED_COMMENT, META_MESSAGE_NAME_FOR_USER_SUBMITTED_COMMENT, trimmedComment, jobDraftId, JobDraftMeta.class, jobDraftMetaDao);
			}			
		}catch(Exception e){ throw new Exception(e.getMessage());}		
	}
	
	/**
	* {@inheritDoc}
	*/
	@Override
	public String getUserJobDraftComment(Integer jobDraftId) throws Exception{
		String comment = null;//if(1==1){return new String("new string message to avoid exception");}
		try{
			List<MetaMessage> metaMessageList = metaMessageService.read(META_MESSAGE_GROUP_FOR_USER_SUBMITTED_COMMENT, jobDraftId, JobDraftMeta.class, jobDraftMetaDao);
			if(metaMessageList.size()>0){//only one allowed
				comment = metaMessageList.get(0).getValue();
			}
		}catch(Exception e){
			logger.warn("caught unexpected exception in getUserJobDraftComment(): " + e.getLocalizedMessage());
			throw new Exception(e.getMessage());
		}
		return comment;
	}
	
	public static final String SAMPLE_PAIR_META_KEY = "samplePairsTvsC";
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public Set<Map<SampleDraft,SampleDraft>> getSampleDraftPairsByJobDraft(JobDraft jobDraft){
		Set<Map<SampleDraft,SampleDraft>> sampleDraftPairSet = new HashSet<Map<SampleDraft,SampleDraft>>();
		
		String samplePairsKey = jobDraft.getWorkflow().getIName()+"."+SAMPLE_PAIR_META_KEY;
		
		JobDraftMeta samplePairsTvsC = jobDraftMetaDao.getJobDraftMetaByKJobDraftId(samplePairsKey, jobDraft.getId());
		if (samplePairsTvsC.getId() != null){
			for(String pair: samplePairsTvsC.getV().split(";")){
				String[] pairList = pair.split(":");
				
				Map<SampleDraft,SampleDraft> sampleDraftPair = new HashMap<SampleDraft,SampleDraft>();
				Integer T = Integer.valueOf(pairList[0]);
				Integer C = Integer.valueOf(pairList[1]);
				sampleDraftPair.put(sampleDraftDao.getSampleDraftBySampleDraftId(T.intValue()), 
						sampleDraftDao.getSampleDraftBySampleDraftId(C.intValue()));	
				
				sampleDraftPairSet.add(sampleDraftPair);
			}
		}
		return sampleDraftPairSet;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void setSampleDraftPairsByJobDraft(JobDraft jobDraft, Set<Map<SampleDraft,SampleDraft>> sampleDraftPairSet){
		
	    String samplePairsKey = jobDraft.getWorkflow().getIName()+"."+SAMPLE_PAIR_META_KEY;
		JobDraftMeta samplePairsTvsC = jobDraftMetaDao.getJobDraftMetaByKJobDraftId(samplePairsKey, jobDraft.getId());
		
		// remove old paired sample for jobdraft
		if (samplePairsTvsC.getId() != null){
			jobDraftMetaDao.remove(samplePairsTvsC);
			jobDraftMetaDao.flush(samplePairsTvsC);
		}
		
		String pairMetaString = ""; 
		
	    if (!sampleDraftPairSet.isEmpty()){
			for(Map<SampleDraft, SampleDraft> pair: sampleDraftPairSet){
				Entry<SampleDraft, SampleDraft> e = pair.entrySet().iterator().next();
				pairMetaString += e.getKey().getId()+":"+e.getValue().getId()+";";
			}
		}
	    
		if (!pairMetaString.isEmpty()){
			// persist pair meta string
			JobDraftMeta jdm = new JobDraftMeta(); 
			jdm.setJobDraftId(jobDraft.getId());
			jdm.setK(samplePairsKey);
			jdm.setV(pairMetaString); 
			jobDraftMetaDao.save(jdm);
		}
	}

	@Override
	public JobDraftDao getJobDraftDao() {
		return jobDraftDao;
	}

	@Override
	public JobDraftMetaDao getJobDraftMetaDao() {
		return jobDraftMetaDao;
	}

}
