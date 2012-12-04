package edu.yu.einstein.wasp.taskMapping;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.WaspException;
import edu.yu.einstein.wasp.service.JobService;

public class SampleQCTaskMapping extends WaspTaskMapping {
	
	private JobService jobService;

	@Autowired
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}
	
	public SampleQCTaskMapping(String localizedLabelKey, String targetLink, String permission) {
		super(localizedLabelKey, targetLink, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRequirementToShowLink() throws WaspException {
		return jobService.isJobsAwaitingSampleQC();
	}

}
