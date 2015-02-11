package edu.yu.einstein.wasp.taskMapping;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * 
 * @author asmclellan
 *
 */
public class InitiateAggregationAnalysisTaskMapping extends WaspTaskMapping {
	
	private JobService jobService;
	
	private SampleService sampleService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}
	
	@Autowired
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}

	public InitiateAggregationAnalysisTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	@Override
	public boolean isRequirementToShowLink(Object o) throws WaspException {
		@SuppressWarnings("unchecked")
		List<Job> jobList = (List<Job>) o;
		for(Job job: jobList){
			if (!jobService.isAggregationAnalysisBatchJob(job) && 
					!jobService.isAnySampleCurrentlyBeingProcessed(job) && 
					!sampleService.getCellLibrariesForJob(job).isEmpty() )
				return true;
		}
		return false;
	}

}
