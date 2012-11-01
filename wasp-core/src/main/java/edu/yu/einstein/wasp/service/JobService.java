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

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDao;
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
	 * getSubmittedSamples(Job job)
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
	 * sortJobsByJobId 
	 * @param List<Job>
	 * @return void
	 */
	public void sortJobsByJobId(List<Job> jobs);
	
	/**
	 * getExtraJobDetails
	 * @param Job job
	 * @return Map<String,String> for easy display on web
	 */
	public Map<String, String> getExtraJobDetails(Job job);

	/**
	 * Create a Job object from a job draft. Also handle dependencies.
	 * @param jobdraft
	 * @return entity-managed Job object
	 * @throws FileMoveException, WaspMessageBuildingException 
	 */
	public Job createJobFromJobDraft(JobDraft jobdraft, User user) throws FileMoveException, WaspMessageBuildingException;
	
	/**
	 * getJobsAwaitingLibraryCreation() returns list of unique jobs for which at least one library must be created from a sample.
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsAwaitingLibraryCreation();
	
	/**
	 * getJobsWithLibrariesToGoOnPlatformUnit() returns list of unique jobs where one or more of the job's samples (either facility library or user-submitted library) 
	 * are registered as awaiting analysis but not yet assigned to a cell. Only returns those jobs for which the resource category matches that specified.
	 * @param ResourceCategory
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(ResourceCategory resourceCategory);
	
	/**
	 * getJobsWithLibrariesToGoOnPlatformUnit() returns list of unique jobs where one or more of the job's samples (either facility library or user-submitted library) 
	 * are registered as awaiting analysis but not yet assigned to a cell
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
	public Boolean isJobAwaitingPiApproval(Job job);

	
	/**
	 * Returns true if provided job is awaiting administrator approval, otherwise returns false
	 * @param job
	 * @return
	 */
	public Boolean isJobAwaitingDaApproval(Job job);
	
	/**
	 * Returns true if provided job is awaiting quoting, otherwise returns false
	 * @param job
	 * @return
	 */
	public Boolean isJobAwaitingQuote(Job job);
	
	/**
	 * Returns true if provided sample is received, otherwise returns false
	 * @param sample
	 * @return
	 */
	public Boolean isJobAwaitingLibraryCreation(Job job, Sample sample);
	
	/**
	 * Updates the Job Quote Status for job.
	 * Status must be either CREATED or ABANDONED
	 * @param jobId
	 * @param status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobQuoteStatus(Job job, WaspStatus status) throws WaspMessageBuildingException;
	
	/**
	 * Updates the Job DA approval Status for job
	 * Status must be either CREATED or ABANDONED
	 * @param jobId
	 * @param status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobDaApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException;
	
	/**
	 * Updates the Job Pi Approval Status for job
	 * Status must be either CREATED or ABANDONED
	 * @param jobId
	 * @param status
	 * @throws WaspMessageBuildingException
	 */
	public void updateJobPiApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException;
}
