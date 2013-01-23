package edu.yu.einstein.wasp.daemon.batch.tasklets.analysis;

import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

public class WaspJobSoftwareLaunchTasklet implements Tasklet {
	
	private JobService jobservice;
	
	private Integer cellLibraryId;
	
	private Integer jobId;
	
	private ResourceType softwareResourceType;
	 
	
	@Autowired
	public void setJobservice(JobService jobservice) {
		this.jobservice = jobservice;
	}
	
	public WaspJobSoftwareLaunchTasklet(Integer jobId, Integer cellLibraryId, ResourceType softwareResourceType) {
		this.cellLibraryId = cellLibraryId;
		this.jobId = jobId;
		this.softwareResourceType = softwareResourceType;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		WaspJobContext waspJobContext = new WaspJobContext(jobservice.getJobByJobId(jobId));
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(softwareResourceType);
		if (softwareConfig == null){
			throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + softwareResourceType.getIName());
		}
		Map<String, String> jobParameters = softwareConfig.getParameters();
		jobParameters.put(WaspJobParameters.LIBRARY_CELL, cellLibraryId.toString());
		return RepeatStatus.FINISHED;
	}

	public void setCellLibraryId(Integer cellLibraryId) {
		this.cellLibraryId = cellLibraryId;
	}
	
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	

}
