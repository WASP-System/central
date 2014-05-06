package edu.yu.einstein.wasp.gatk.batch.tasklet.discovery;

import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.LaunchManyJobsTasklet;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.JobService;

/**
 * 
 * @author asmclellan
 *
 */
public class MergeSampleBamFilesManyJobsTasklet extends LaunchManyJobsTasklet {
		
	@Autowired
	private JobService jobService;
	
	private Integer jobId;

	public MergeSampleBamFilesManyJobsTasklet(Integer jobId) {
		this.jobId = jobId;
	}
	
	@Override
	public void doExecute() {
		Job job = jobService.getJobByJobId(jobId);
		Assert.assertTrue(job.getId() > 0);

	}

}
