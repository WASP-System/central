package edu.yu.einstein.wasp.decider;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.service.SampleService;

public class SampleTypeDecider implements JobExecutionDecider{
	
	protected static Logger logger = Logger.getLogger(SampleTypeDecider.class);

	private SampleService sampleService;
	
	@Autowired
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}


	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecutionIn) {
		// It appears that stepExecutionIn is null so we need to get a StepExecution from a previously
		// executed step registered in jobExecution (the first one we find will do) to get the JobParameter list
		List<StepExecution> stepExecutions = new ArrayList<StepExecution>();
		stepExecutions.addAll(jobExecution.getStepExecutions());
		if (stepExecutions.isEmpty()){
			logger.error("No StepExecution objects obtained");
			return FlowExecutionStatus.UNKNOWN;
		}
		// now we can get the sampleId from the provided job parameter
		Integer sampleId = Integer.valueOf(stepExecutions.get(0).getJobParameters().getString("sampleId"));
		logger.debug("JobParameter: sampleId="+sampleId);
		Sample sample = sampleService.getSampleDao().getSampleBySampleId(sampleId);
		if (sample.getSampleId() == null){
			logger.error("No sample Id obtained from job parameters");
			return FlowExecutionStatus.UNKNOWN;
		} else if (sampleService.isLibrary(sample)){
			return new FlowExecutionStatus("LIBRARY");
		} else {
			return new FlowExecutionStatus("SAMPLE");
		}
	}



	

}
