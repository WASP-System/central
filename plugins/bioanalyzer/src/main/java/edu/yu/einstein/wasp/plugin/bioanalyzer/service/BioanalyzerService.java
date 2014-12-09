/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.plugin.bioanalyzer.service;

import java.util.List;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Workflow;
import edu.yu.einstein.wasp.service.WaspMessageHandlingService;
import edu.yu.einstein.wasp.service.WaspService;

/**
 * 
 */
public interface BioanalyzerService extends WaspMessageHandlingService {

	
	public static final String bioanalyzerChipMeta = "bioanalyzer.chip";
	public static final String bioanalyzerAssayLibrariesAreForMeta = "bioanalyzer.assayLibrariesAreFor";
	public static final String bioanalyzerAtLeastOneBioanalyzerFileUploadedByFacilityMeta = "bioanalyzer.atLeastOneBioanalyzerFileUploadedByFacility";


		/**
		 * Perform Service
		 * @return String
		 */
		public String performAction();

		public void saveOrUpdateJobDraftMeta (JobDraft jobDraft, String metaK, String metaV);
		public String getMeta(JobDraft jobDraft, String metaK);
		public String getMeta(Job job, String metaK);
		public List<String> getAvailableBioanalyzerChips(Workflow wf);
		public boolean atLeastOneBioanalyzerFileUploadedByFacility(Job job);
		public void updateBioanalyzerJobStatus(Job job, WaspStatus status, String task, String comment, boolean checkForJobActive) throws WaspMessageBuildingException;

		public boolean isJobsAwaitingBioanalyzerCompleteTask(List<Job>jobList);
		public boolean isThisJobAwaitingBioanalyzerCompleteTask(Job job);
}
