/**
 *
 * JobServiceImpl.java 
 * @author dubin
 *  
 * the JobService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.dao.TaskDao;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.SampleType;
import edu.yu.einstein.wasp.model.State;
import edu.yu.einstein.wasp.model.Statejob;
import edu.yu.einstein.wasp.model.Statesample;
import edu.yu.einstein.wasp.model.Task;
import edu.yu.einstein.wasp.model.WorkflowSampleSubtype;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.util.MetaHelper;

@Service
public class JobServiceImpl extends WaspServiceImpl implements JobService {

	private JobDao	jobDao;

	/**
	 * setJobDao(JobDao jobDao)
	 * 
	 * @param jobDao
	 * 
	 */
	@Override
	@Autowired
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	/**
	 * getJobDao();
	 * 
	 * @return jobDao
	 * 
	 */
	@Override
	public JobDao getJobDao() {
		return this.jobDao;
	}
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private WorkflowDao workflowDao;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;
	
	 @Autowired
	 private TaskDao taskDao;

	 @Autowired
	 private TaskService taskService;


	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Sample> getSubmittedSamples(Job job){
		
		List<Sample> submittedSamplesList = new ArrayList();
		if(job != null && job.getJobId().intValue()>0){
			for(JobSample jobSample : job.getJobSample()){
				  Sample sample  = jobSample.getSample();//includes submitted samples that are macromolecules, submitted samples that are libraries, and facility-generated libraries generated from a macromolecule
				  if(sample.getSampleSource().size() == 0){//this sample is NOT a facility-generated library (since if size>0 records a facility-generated library), so add it to the submittedSample list
					  submittedSamplesList.add(sample);
				  }
			  }	
		}
		return submittedSamplesList;		
	}
	
	/**
	   * {@inheritDoc}
	   */
	@Override
	public List<Sample> getSubmittedSamplesAwaitingSubmission(Job job){
		
		List<Sample> submittedSamplesAwaitingSubmissionList = new ArrayList<Sample>();
		
		List<Sample> submittedSampleList = getSubmittedSamples(job);
		
		Task receiveSampleTask = taskDao.getTaskByIName("Receive Sample");
		
		for(Sample sample : submittedSampleList){
			List<Statesample> stateSampleList = sample.getStatesample();
			for(Statesample stateSample : stateSampleList){
				if(stateSample.getState().getTask().getIName().equals(receiveSampleTask.getIName()) && stateSample.getState().getStatus().equals("CREATED")){
					submittedSamplesAwaitingSubmissionList.add(sample);
					break;
				}
			}
		}
		return submittedSamplesAwaitingSubmissionList;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getActiveJobs(){
		
		List<Job> activeJobList = new ArrayList<Job>();
		
		Task task = taskDao.getTaskByIName("Start Job");
		List<State> states = taskService.getStatesByTaskMappingRule(task, "CREATED");
		Set<Job> jobsSet = new HashSet<Job>();//to filter out any duplicates
		for(State state : states){
			List<Statejob> stateJobList = state.getStatejob();
			for(Statejob stateJob : stateJobList){
				jobsSet.add(stateJob.getJob());
			}
		}
		for(Job job : jobsSet){
			activeJobList.add(job);
		}
		
		sortJobsByJobId(activeJobList);
		
		return activeJobList;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getJobsAwaitingSubmittedSamples(){
		
		List<Job> jobsAwaitingSubmittedSamplesList = new ArrayList<Job>();
		
		Task task = taskDao.getTaskByIName("Receive Sample");
		List<State> states = taskService.getStatesByTaskMappingRule(task, "CREATED");
		Set<Sample> sampleSet = new HashSet<Sample>();//to filter out any duplicates
		for(State state : states){
			List<Statesample> stateSampleList = state.getStatesample();
			for(Statesample stateSample : stateSampleList){
				sampleSet.add(stateSample.getSample());//all samples with task receive sample = created
			}
		}
		Set<Job> jobSet = new HashSet<Job>();
		for(Sample sample : sampleSet){
			List<JobSample> jobSampleList = sample.getJobSample();
			for(JobSample jobSample : jobSampleList){
				jobSet.add(jobSample.getJob());//unique set of jobs that have at least one sample with task receive sample = created
			}
		}
		
		for(Job job : jobSet){
			jobsAwaitingSubmittedSamplesList.add(job);
		}
		
		sortJobsByJobId(jobsAwaitingSubmittedSamplesList);
		
		return jobsAwaitingSubmittedSamplesList;
	}
	
	/**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortJobsByJobId(List<Job> jobs){
		  class JobIdComparator implements Comparator<Job> {
			    @Override
			    public int compare(Job arg0, Job arg1) {
			        return arg0.getJobId().compareTo(arg1.getJobId());
			    }
		  }
		  Collections.sort(jobs, new JobIdComparator());//sort by job ID 
	  }
}
