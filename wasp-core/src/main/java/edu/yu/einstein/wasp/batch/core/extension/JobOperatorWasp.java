package edu.yu.einstein.wasp.batch.core.extension;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * 
 * @author asmclellan
 *
 */
public interface JobOperatorWasp extends JobOperator {
	
	/**
	 * Wake a hibernating job
	 * @param executionId
	 * @return
	 * @throws JobInstanceAlreadyCompleteException
	 * @throws NoSuchJobExecutionException
	 * @throws NoSuchJobException
	 * @throws JobRestartException
	 */
	public Long wake(long executionId) throws JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, NoSuchJobException, JobRestartException, JobParametersInvalidException;
	
	public boolean hibernate(long executionId) throws NoSuchJobExecutionException, JobExecutionNotRunningException;

}
