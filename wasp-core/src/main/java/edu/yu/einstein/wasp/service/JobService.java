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
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.User;

@Service
public interface JobService extends WaspService {

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
	 * getSubmittedSamplesAwaitingSubmission
	 * @param Job 
	 * @return List<Sample>
	 */
	public List<Sample> getSubmittedSamplesAwaitingSubmission(Job job);

	/**
	 * getActive Jobs() returns list of active jobs (state of Start Job = Created)
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getActiveJobs();
	
	/**
	 * getJobsAwaitingSubmittedSamples() returns list of jobs (with samples having task of Receive Sample = Created)
	 * @param none
	 * @return List<Job>
	 * 
	 */
	public List<Job> getJobsAwaitingSubmittedSamples();
	
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
	 * @throws FileMoveException 
	 */
	public Job createJobFromJobDraft(JobDraft jobdraft, User user) throws FileMoveException;
	
}
