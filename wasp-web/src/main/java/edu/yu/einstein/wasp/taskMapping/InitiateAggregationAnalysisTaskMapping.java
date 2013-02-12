package edu.yu.einstein.wasp.taskMapping;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.JobService;


public class InitiateAggregationAnalysisTaskMapping extends WaspTaskMapping {
	
	private JobService jobService;

	@Autowired
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}

	public InitiateAggregationAnalysisTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	@Override
	public boolean isRequirementToShowLink() throws WaspException {
		for(Job job: jobService.getActiveJobs()){
			if (!jobService.isAnySampleCurrentlyBeingProcessed(job))
				return true;
		}
		return false;
	}

}
