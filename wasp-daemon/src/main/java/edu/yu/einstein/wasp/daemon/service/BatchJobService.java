package edu.yu.einstein.wasp.daemon.service;

import java.util.Set;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.explore.JobExplorer;

public interface BatchJobService {

	public abstract Set<JobExecution> findAllRunningJobExecutions();

	public void setJobExplorer(JobExplorer jobExplorer);


}
