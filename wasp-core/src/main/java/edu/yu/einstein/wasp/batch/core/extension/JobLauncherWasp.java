package edu.yu.einstein.wasp.batch.core.extension;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * 
 * @author asmclellan
 *
 */
public interface JobLauncherWasp extends JobLauncher {
	
	public JobExecution wake(final WaspFlowJob job, final JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException, 
		JobInstanceAlreadyCompleteException, JobParametersInvalidException;
	
}
