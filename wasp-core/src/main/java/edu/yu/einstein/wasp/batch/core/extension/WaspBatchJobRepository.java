package edu.yu.einstein.wasp.batch.core.extension;

import org.springframework.batch.core.repository.dao.ExecutionContextDao;
import org.springframework.batch.core.repository.dao.JobExecutionDao;
import org.springframework.batch.core.repository.dao.JobInstanceDao;
import org.springframework.batch.core.repository.dao.StepExecutionDao;
import org.springframework.batch.core.repository.support.SimpleJobRepository;

import edu.yu.einstein.wasp.batch.core.extension.dao.WaspStepExecutionDao;

public class WaspBatchJobRepository extends SimpleJobRepository implements JobRepositoryWasp {
	
	private WaspStepExecutionDao stepExecutionDao;
	
	public WaspBatchJobRepository(JobInstanceDao jobInstanceDao, JobExecutionDao jobExecutionDao, StepExecutionDao stepExecutionDao, ExecutionContextDao executionContextDao) {
		super(jobInstanceDao, jobExecutionDao, stepExecutionDao, executionContextDao);
		this.stepExecutionDao = (WaspStepExecutionDao) stepExecutionDao;
	}

	@Override
	public void deleteStepExecution(Long stepExecutionId) {
		stepExecutionDao.deleteStepExecution(stepExecutionId);
	}

}
