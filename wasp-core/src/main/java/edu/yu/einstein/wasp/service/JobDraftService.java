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

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.User;

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
	 * remove existing cells and their samples (if any) and add new set 
	 * @param JobDraft jobdraft
	 * @return void
	 */
	public void createUpdateJobDraftCells(JobDraft jobDraft, Map params);

}