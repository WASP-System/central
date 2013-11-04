package edu.yu.einstein.wasp.batch.core.extension;

import org.springframework.batch.core.repository.JobRepository;

public interface JobRepositoryWasp extends JobRepository {

	public void deleteStepExecution(Long stepExecutionId);
	
}
