package edu.yu.einstein.wasp.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.service.JobExplorerWasp;

@Service
@Transactional
public class JobStatusServiceImpl {

	private JobExplorerWasp jobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorerWasp jobExplorer){
		this.jobExplorer = jobExplorer;
	}
	
	public Set<JobExecution> findAllRunningJobExecutions(){
		Set<JobExecution> runningJobExecutionSet = new HashSet<JobExecution>();
		for(String jobName: jobExplorer.getJobNames()){
			Set<JobExecution> runningJobExecutionSubset = jobExplorer.findRunningJobExecutions(jobName);
			if (runningJobExecutionSubset != null && !runningJobExecutionSubset.isEmpty()){
				runningJobExecutionSet.addAll(runningJobExecutionSubset);
			}
		}
		return runningJobExecutionSet;
	}
	
	
}
