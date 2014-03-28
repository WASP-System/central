package edu.yu.einstein.wasp.batch.launch;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.WaspMessageTemplate;

/**
 * BatchJobLaunchServiceImpl. Launch Spring Batch jobs
 * @author asmclellan
 *
 */
public class BatchJobLaunchServiceImpl implements BatchJobLaunchService{
	
	private JobLauncher jobLauncher;
	
	private JobRegistry jobRegistry;
	
	private static Logger logger = LoggerFactory.getLogger(BatchJobLaunchServiceImpl.class);
	
	public BatchJobLaunchServiceImpl(){};
	
	public BatchJobLaunchServiceImpl(JobLauncher jobLauncher, JobRegistry jobRegistry) {
		this.jobLauncher = jobLauncher;
		this.jobRegistry = jobRegistry;
	}

	public JobRegistry getJobRegistry() {
		return jobRegistry;
	}

	public void setJobRegistry(JobRegistry jobRegistry) {
		this.jobRegistry = jobRegistry;
	}

	
	public JobLauncher getjobLauncher() {
		return jobLauncher;
	}

	public void setjobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Message<WaspStatus> launch(BatchJobLaunchContext batchJobLaunchContext){
		try{
			launchBatchJob(batchJobLaunchContext, jobLauncher, jobRegistry);
			return MessageBuilder.withPayload(WaspStatus.COMPLETED).build();
		} catch (WaspBatchJobExecutionException e){
			return MessageBuilder.withPayload(WaspStatus.FAILED).setHeader(WaspTask.EXCEPTION, e).build();
		}
	}
	
	public static JobExecution launchBatchJob(BatchJobLaunchContext batchJobLaunchContext, JobLauncher jobLauncher, JobRegistry jobRegistry) throws WaspBatchJobExecutionException{
		try{
			Assert.assertParameterNotNull(batchJobLaunchContext, "No BatchJobLaunchContext set");
			Assert.assertParameterNotNull(batchJobLaunchContext.getJobParameters(), "No BatchJobLaunchContext.jobParameters set");
			Assert.assertParameterNotNull(batchJobLaunchContext.getJobName(), "No BatchJobLaunchContext.jobName set");
			Assert.assertParameterNotNull(jobLauncher, "No JobLauncher set");
			Assert.assertParameterNotNull(jobRegistry, "No JobRegistry set");
		} catch (InvalidParameterException e){
			throw new WaspBatchJobExecutionException("Method parameters incomplete: " + e.getMessage(), e);
		}
		
		if (batchJobLaunchContext.getJobParameters().containsKey(WaspMessageTemplate.PARENT_ID)) 
		    logger.debug("launching job " + batchJobLaunchContext.getJobName() + " with child id " +
		            batchJobLaunchContext.getJobParameters().get(WaspMessageTemplate.CHILD_MESSAGE_ID) +
		            " of many spawned by parent " + batchJobLaunchContext.getJobParameters().get(WaspMessageTemplate.PARENT_ID));
		
		String jobName = batchJobLaunchContext.getJobName();
		Job job = null;
		try{
			job = jobRegistry.getJob(jobName);
		} catch(NoSuchJobException e){
			throw new WaspBatchJobExecutionException("Unable to find a job with name '" + jobName + "' in the job registry");
		}
		Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();
		String logMessage = "Starting batch job '" + jobName + "' with parameters: ";
		for (String paramName: batchJobLaunchContext.getJobParameters().keySet()){
			jobParameters.put(paramName, new JobParameter(batchJobLaunchContext.getJobParameters().get(paramName)));
			logMessage += paramName + "=" + batchJobLaunchContext.getJobParameters().get(paramName) + ",";
		}
		logger.debug(StringUtils.chop(logMessage));
		try {
			return jobLauncher.run(job, new JobParameters(jobParameters));
		} catch (JobExecutionAlreadyRunningException e) {
			throw new WaspBatchJobExecutionException("Job Execution already running: " + e.getMessage(), e);
		} catch (JobRestartException e) {
			throw new WaspBatchJobExecutionException("Job Execution restart error: " + e.getMessage(), e);
		} catch (JobInstanceAlreadyCompleteException e) {
			throw new WaspBatchJobExecutionException("Job instance complete error: " + e.getMessage(), e);
		} catch (JobParametersInvalidException e) {
			throw new WaspBatchJobExecutionException("Job parameter error: " + e.getMessage(), e);
		}
	}

}
