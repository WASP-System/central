package edu.yu.einstein.wasp.macstwo.batch.decider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ExecutionContext;

import edu.yu.einstein.wasp.macstwo.service.MacstwoService;

public class ModelGeneratedDecider implements JobExecutionDecider {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelGeneratedDecider.class);

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		
		String isModelGenerated = (String) executionContext.get(MacstwoService.IS_MODEL_FILE_CREATED);
		
		if (isModelGenerated != null){	
			logger.warn("in ModelGeneratedDecider.decide(), isModelGenerated is not null and it has a value of: " + isModelGenerated);
	        return new FlowExecutionStatus(isModelGenerated);	       
		}
		logger.warn("isModelGenerated=" + isModelGenerated + " so returning 'UNKNOWN'");
		return FlowExecutionStatus.UNKNOWN;
	}
}
