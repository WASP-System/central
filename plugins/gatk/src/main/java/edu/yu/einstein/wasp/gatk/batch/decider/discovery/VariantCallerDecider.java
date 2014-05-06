package edu.yu.einstein.wasp.gatk.batch.decider.discovery;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import edu.yu.einstein.wasp.gatk.service.GatkService;

public class VariantCallerDecider implements JobExecutionDecider {
	
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		String variantCallingMethod = jobExecution.getJobParameters().getString("variantCallingMethod"); // TODO: make sure that this is in the job parameters!!
		if (variantCallingMethod != null){
			if (variantCallingMethod.equals(GatkService.HAPLOTYPE_CALLER_CODE)) {
	            return new FlowExecutionStatus(GatkService.HAPLOTYPE_CALLER_CODE.toUpperCase());
	        } else if (variantCallingMethod.equals(GatkService.UNIFIED_GENOTYPER_CODE)) {
	        	return new FlowExecutionStatus(GatkService.UNIFIED_GENOTYPER_CODE.toUpperCase());
	        }
		}
		return FlowExecutionStatus.UNKNOWN;
	}

}
