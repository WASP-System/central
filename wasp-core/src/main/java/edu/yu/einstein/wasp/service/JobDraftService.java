/**
 *
 * JobDraftService.java 
 * @author dubin
 *  
 * the JobDraftService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobDraftMetaDao;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.SampleDraft;

@Service
public interface JobDraftService extends WaspService {

	/**
	 * get JobDraft by it's Id
	 * @param Integer jobDraftId
	 * @return JobDraft
	 */
	public JobDraft getJobDraftById(Integer jobDraftId);
	
	/**
	 * organize cell information from web into Map<Integer, List<SampleDraft>> format, with Integer being cellindex (starting with 1) 
	 * @param Map params
	 * @param List<SampleDraft> samplesOnThisJobDraft
	 * @return Map<Integer, List<SampleDraft>>
	 */
	@SuppressWarnings("rawtypes")
	public Map<Integer, List<SampleDraft>> convertWebCellsToMapCells(Map params, List<SampleDraft> samplesOnThisJobDraft);

	/**
	 * confirm that all samples (from web) have been placed on at least on cell; if not throw exception and use it's message as flash error
	 * @param Map<Integer, List<SampleDraft>> cellMap
	 * @param List<SampleDraft> samplesOnThisJobDraft
	 * @return void
	 */
	public void confirmAllDraftSamplesOnAtLeastOneCell(Map<Integer, List<SampleDraft>> cellMap, List<SampleDraft> samplesOnThisJobDraft) throws Exception;
	
	/**
	 * confirm that no barcodes appear more than once per cell from web; if yes, throw exception and use it's message as flash error
	 * @param Map<Integer, List<SampleDraft>> cellMap
	 * @return void
	 */
	public void confirmNoBarcodeOverlapPerCell(Map<Integer, List<SampleDraft>> cellMap) throws Exception;
	
	/**
	 * confirm that for user-submitted libraries from web, if the library has an adaptor with a NONE barcode, it MUST be the SOLE sample on a cell; if not, throw exception and use it's message as flash error
	 * @param Map<Integer, List<SampleDraft>> cellMap
	 * @return void
	 */
	public void confirmNONEBarcodeIsUniquePerCell(Map<Integer, List<SampleDraft>> cellMap) throws Exception;
	
	/**
	 * remove existing cells and their samples (if any) and add new set 
	 * @param JobDraft jobdraft
	 * @return void
	 */
	@SuppressWarnings("rawtypes")
	public void createUpdateJobDraftCells(JobDraft jobDraft, Map params);

	/**
	 * get Map<SampleDraft, Adaptor> for sampledraft objects in parameter sampleDraftList that are of type library.
	 * If an exception is thrown, it is caught and ignored, to be able to push ahead.  
	 * @param List<SampleDraft> sampleDraftList
	 * @return Map<SampleDraft, Adaptor>
	 */
	public Map<SampleDraft, Adaptor> getAdaptorsOnSampleDrafts(List<SampleDraft> sampleDraftList);
	
	/**
	 * save a User-generated JobDraft Comment; only one such comment permitted per jobDraft.
	 * @param Integer jobDraftId
	 * @param String comment
	 * @return void
	 * @throws Exception
	 */
	public void saveUserJobDraftComment(Integer jobDraftId, String comment) throws Exception;
	
	/**
	 * get the (sole) User-generated JobDraft Comment if it exists; if not return null.
	 * @param Integer jobDraftId
	 * @return String 
	 * @throws Exception
	 */
	public String getUserJobDraftComment(Integer jobDraftId) throws Exception;

	/**
	 * get the set of all pairs of test/control samples for given jobdraft
	 * @param JobDraft jobDraft
	 * @return Set<Map<SampleDraft, SampleDraft>>  
	 * @throws Exception
	 */
	public Set<Map<SampleDraft, SampleDraft>> getSampleDraftPairsByJobDraft(JobDraft jobDraft);

	/**
	 * set all pairs of test/control samples for given jobdraft in the jobdraftmeta
	 * @param JobDraft jobDraft, Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet
	 * @return 
	 * @throws Exception
	 */
	public void setSampleDraftPairsByJobDraft(JobDraft jobDraft, Set<Map<SampleDraft, SampleDraft>> sampleDraftPairSet);

	JobDraftDao getJobDraftDao();

	JobDraftMetaDao getJobDraftMetaDao();
	
	public Map<Integer, Set<SampleDraft>> getReplicateSets(JobDraft jobDraft);
	public void saveReplicateSets(JobDraft jobDraft, SampleDraft sampleDraft);
}