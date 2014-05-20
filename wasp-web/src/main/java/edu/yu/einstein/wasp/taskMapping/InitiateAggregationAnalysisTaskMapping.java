package edu.yu.einstein.wasp.taskMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.JobService;

/**
 * 
 * @author asmclellan
 *
 */
public class InitiateAggregationAnalysisTaskMapping extends WaspTaskMapping {
	
	private JobService jobService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

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
			if (jobService.isJobActive(job) && !jobService.isAggregationAnalysisBatchJob(job) && !jobService.isAnySampleCurrentlyBeingProcessed(job))
				return true;
		}
		return false;
	}

}
