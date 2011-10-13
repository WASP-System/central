package edu.yu.einstein.wasp.batch;

import java.io.File;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public class JobLaunchingMessageHandler {

    private final JobRegistry jobRegistry;
    private final JobLauncher jobLauncher;

    /**
     * Handle job launching request for spring batch from SI
     * 
     * @param jobRegistry
     * @param jobLauncher
     */
    public JobLaunchingMessageHandler(JobRegistry jobRegistry,
                    JobLauncher jobLauncher) {
        super();
        this.jobRegistry = jobRegistry;
        this.jobLauncher = jobLauncher;
    }


    public JobExecution launch(JobLaunchRequest request)
                    throws JobExecutionAlreadyRunningException, JobRestartException,
                    JobInstanceAlreadyCompleteException, JobParametersInvalidException,
                    NoSuchJobException {
        Job job = jobRegistry.getJob(
                        request.getJobName()
                        );
        JobParametersBuilder builder = new JobParametersBuilder();
      
        
        for (Map.Entry<String, String> entry : request.getJobParameters().entrySet()) {
            builder.addString(
                            entry.getKey(), entry.getValue()
                            );
        }
        return jobLauncher.run(job, builder.toJobParameters());
    }
    
    public JobExecution launch(File file)
    throws JobExecutionAlreadyRunningException, JobRestartException,
    JobInstanceAlreadyCompleteException, JobParametersInvalidException,
    NoSuchJobException {
    	Job job = jobRegistry.getJob(
        "beeJob"
        );
    	JobParametersBuilder builder = new JobParametersBuilder();
    	

    	 builder.addString(
                 "file", file.getAbsolutePath()
                 );

    	 
    	return jobLauncher.run(job, builder.toJobParameters());
}
}