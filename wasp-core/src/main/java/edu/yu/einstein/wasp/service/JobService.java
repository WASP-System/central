/**
 *
 * JobService.java 
 * @author dubin
 *  
 * the JobService
 *
 *
 **/

package edu.yu.einstein.wasp.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;

@Service
public interface JobService extends WaspMessageHandlingService {

	/**
	 * setJobDao(JobDao jobDao)
	 * 
	 * @param jobDao
	 * 
	 */
	public void setJobDao(JobDao jobDao);

	/**
	 * getJobDao();
	 * 
	 * @return jobDao
	 * 
	 */
	public JobDao getJobDao();

	/**
	 * getJobByJobId();
	 * 
	 * @return Job
	 * 
	 */
	public Job getJobByJobId(Integer jobId);

	/**
	 * get a list of samples for the submitted job. Returns an empty list if none found.
	 * 
	 * @param Job
	 * 
	 * @return List<Sample>
	 */
	public List<Sample> getSubmittedSamples(Job job);
	
	/**
	 * getSubmittedSamplesNotYetReceived
	 * @param Job 
	 * @return List<Sample>
	 */
	public List<Sample> getSubmittedSamplesNotYetReceived(Job job);
	
	/**
	 * getSubmittedSamplesNotYetQC
	 * @param Job 
	 * @return List<Sample>
	 */
	public List<Sample> getSubmittedSamplesNotYetQC(Job job);
	
	/**
	 * getLibrariesNotYetReceived
	 * @param Job 
	 * @return List<Sample>
	 */
	public List<Sample> getLibrariesNotYetQC(Job job);

	/**
	 * getActive Jobs() returns list of active jobs (executing)
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getActiveJobs();
	
	/**
	 * getJobsAwaitingReceivingOfSamples() returns list of jobs which include samples not registered as received yet.
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsAwaitingReceivingOfSamples();
	
	/**
	 * returns list of jobs awaiting QC of at least one Sample
	 * @return
	 */
	public List<Job> getJobsAwaitingSampleQC();

	/**
	 * returns true if any jobs exist which are awaiting QC of a Sample
	 * @return
	 */
	public boolean isJobsAwaitingSampleQC();

	/**
	 * returns list of jobs awaiting QC of at least one Library
	 * @return
	 */
	public List<Job> getJobsAwaitingLibraryQC();

	/**
	 * returns true if any jobs exist which are awaiting QC of a library
	 * @return
	 */
	public boolean isJobsAwaitingLibraryQC();

	
	/**
	 * sortJobsByJobId 
	 * @param List<Job>
	 * @return void
	 */
	public void sortJobsByJobId(List<Job> jobs);
	
	/**
	 * getExtraJobDetails
	 * @param Job job
	 * @return Map<String,String> for easy display on web. first string is a fmt:message stored in messages.properties which acts as a label; second string is displayed value, such as a cost value or a machine name
	 */
	public LinkedHashMap<String, String> getExtraJobDetails(Job job);

	/**
	 * getJobApprovals
	 * @param Job job
	 * @return Map<String,String> for easy display on web. both strings are fmt:message stored in messages.properties file
	 */
	public LinkedHashMap<String, String> getJobApprovals(Job job);

	/**
	 * Create a Job object from a job draft. Also handle dependencies.
	 * @param jobdraft
	 * @return entity-managed Job object
	 * @throws FileMoveException
	 */
	public Job createJobFromJobDraft(JobDraft jobdraft, User user) throws FileMoveException;
	
	/**
	 * getJobsAwaitingLibraryCreation() returns list of unique jobs for which at least one library must be created from a sample.
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsAwaitingLibraryCreation();
	
	/**
	 * getJobsWithLibrariesToGoOnPlatformUnit() returns list of unique jobs with libraries created (and passed QC) for which the actual coverage on
	 * currently running or successfully completed flowcells is less than the requested coverage. Only returns those jobs for which the resource category matches that specified.
	 * @param ResourceCategory
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(ResourceCategory resourceCategory);
	
	/**
	 * getJobsWithLibrariesToGoOnPlatformUnit() returns list of unique jobs with libraries created (and passed QC) for which the actual coverage on
	 * currently running or successfully completed flowcells is less than the requested coverage.
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit();
	
	/**
	 * getJobsSubmittedOrViewableByUser() returns list of jobs that was submitted by, as well as viewable by, a specific user. 
	 * Note that some jobs viewable to the user may have been submitted by someone else.
	 * Note that a PI can see there own jobs AND jobs submitted by members of their lab
	 * Note that returned job list is ordered by jobId/descending
	 * @param User user
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsSubmittedOrViewableByUser(User user);
	
	/**
	 * Returns true if provided job is awaiting PI approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingPiApproval(Job job);

	
	/**
	 * Returns true if provided job is awaiting administrator approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingDaApproval(Job job);
	
	/**
	 * Returns true if provided job is awaiting quoting, otherwise returns false
	 * @param job
	 * @return
	 */
	public boolean isJobAwaitingQuote(Job job);
	
	/**
	 * Returns true if provided sample has no library has been made yet, otherwise returns false
	 * @param sample
	 * @return
	 */
	public boolean isJobAwaitingLibraryCreation(Job job, Sample sample);
	
	/**
	 * Updates the Job Quote Status for job.
	 * Status must be either COMPLETED or ABANDONED
	 * @param jobId
	 * @param status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobQuoteStatus(Job job, WaspStatus status) throws WaspMessageBuildingException;
	
	/**
	 * Updates the Job DA approval Status for job
	 * Status must be either COMPLETED or ABANDONED
	 * @param jobId
	 * @param status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobDaApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException;
	
	/**
	 * Updates the Job Pi Approval Status for job
	 * Status must be either COMPLETED or ABANDONED
	 * @param jobId
	 * @param status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobPiApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException;

	/**
	 * removeJobViewer() removes a viewer from a specific job. Performs checks to determine if this is a legal option and if not, throw exception 
	 * @param Integer jobId (the job from which the viewer is to be removed)
	 * @param Integer userId (the user to be removed as a jobviewer)
	 * @return void (maybe should be an int or boolean)
	 * 
	 */
	public void removeJobViewer(Integer jobId, Integer userId) throws Exception;
	
	/**
	 * addJobViewer() adds a viewer to a specific job. Performs checks to determine if this is a legal option and if not, throw exception 
	 * @param Integer jobId (the job to which the viewer is to be added)
	 * @param String newViewerEmailAddress (the new viewer's email address)
	 * @return void (maybe should be an int or boolean)
	 * 
	 */
	public void addJobViewer(Integer jobId, String newViewerEmailAddress) throws Exception;

	/**
	 * Kick of batch job for job
	 * @param job
	 * @throws WaspMessageBuildingException
	 */
	public void initiateBatchJobForJobSubmission(Job job) throws WaspMessageBuildingException;


	/**
	 * Returns true if there a re jobs awaiting receiving of sample
	 * @return
	 */
	public boolean isJobsAwaitingReceivingOfSamples();

	
	/**
	 * save a Facility-generated Job Comment
	 * @param Integer jobId
	 * @param String comment
	 * @return void
	 * @throws Exception
	 */
	public void setFacilityJobComment(Integer jobId, String comment)throws Exception;
	
	/**
	 * get all facility job comments (supposedly chronologically ordered)
	 * @param Integer jobId
	 * @return List<MetaMessage>
	 */
	public List<MetaMessage> getAllFacilityJobComments(Integer jobId);
	
	/**
	 * get user-submitted job comment (there will be zero or one only)
	 * @param Integer jobId
	 * @return List<MetaMessage>
	 */
	public List<MetaMessage> getUserSubmittedJobComment(Integer jobId);

	/**
	 * get a list of all jobs awaiting quoting (returns an empty list if none)
	 * @return
	 */
	public List<Job> getJobsAwaitingQuote();

	/**
	 * returns true if one or more jobs are awaiting quoting
	 * @return
	 */
	public boolean isJobsAwaitingQuote();

	public void setSampleService(SampleService sampleService);

	public void setSampleDao(SampleDao mockSampleDao);

	public void setJobDao(SampleDao mockSampleDao);

	public void setJobMetaDao(JobMetaDao mockJobMetaDao);

	public void setJobSoftwareDao(JobSoftwareDao mockJobSoftwareDao);

	public void setJobResourcecategoryDao(
			JobResourcecategoryDao mockJobResourcecategoryDao);

	public void setRoleDao(RoleDao mockRoleDao);

	public void setJobUserDao(JobUserDao mockJobUserDao);

	public void setLabDao(LabDao mockLabDao);

	public void setJobCellSelectionDao(
			JobCellSelectionDao mockJobCellSelectionDao);

	public void setSampleFileDao(SampleFileDao mockSampleFileDao);

	public void setSampleMetaDao(SampleMetaDao mockSampleMetaDao);

	public void setSampleJobCellSelectionDao(
			SampleJobCellSelectionDao mockSampleJobCellSelectionDao);

	public void setJobSampleDao(JobSampleDao mockJobSampleDao);

	public void setJobDraftDao(JobDraftDao mockJobDraftDao);

	public void setLogger(Logger logger);

	public void setSampleTypeDao(SampleTypeDao mockSampleTypeDao);

	
}
