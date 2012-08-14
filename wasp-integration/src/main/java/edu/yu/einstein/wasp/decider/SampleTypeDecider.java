package edu.yu.einstein.wasp.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.SampleService;

public class SampleTypeDecider implements JobExecutionDecider {
	

	private SampleService sampleService;
	
	@Autowired
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}

	private Integer sampleId;
	
	public SampleTypeDecider(Integer sampleId) {
		this.sampleId = sampleId;
	}

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		Sample sample = sampleService.getSampleDao().getSampleBySampleId(sampleId);
		if (sample.getSampleId() == null){
			return FlowExecutionStatus.UNKNOWN;
		} else if (sampleService.isLibrary(sample)){
			return new FlowExecutionStatus("LIBRARY");
		} else {
			return new FlowExecutionStatus("SAMPLE");
		}
	}

}
