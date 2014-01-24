package edu.yu.einstein.wasp.daemon.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.service.BatchJobService;

/**
 * 
 * @author asmclellan
 * 
 */
@Service
@Transactional("entityManager")
public class BatchJobServiceImpl implements BatchJobService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private JobExplorerWasp jobExplorer;
	
	@Override
	@Autowired
	public void setJobExplorer(JobExplorer jobExplorer) {
		this.jobExplorer = (JobExplorerWasp) jobExplorer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.yu.einstein.wasp.daemon.service.impl.JobService#
	 * findAllRunningJobExecutions()
	 */
	@Override
	public Set<JobExecution> findAllRunningJobExecutions() {
		Set<JobExecution> runningJobExecutionSet = new HashSet<JobExecution>();
		for (String jobName : jobExplorer.getJobNames()) {
			Set<JobExecution> runningJobExecutionSubset = jobExplorer.findRunningJobExecutions(jobName);
			if (runningJobExecutionSubset != null && !runningJobExecutionSubset.isEmpty()) {
				runningJobExecutionSet.addAll(runningJobExecutionSubset);
			}
		}
		return runningJobExecutionSet;
	}

}
