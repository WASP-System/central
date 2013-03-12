package edu.yu.einstein.wasp.integration.serviceactivators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;

import edu.yu.einstein.wasp.service.EmailService;
import edu.yu.einstein.wasp.service.JobService;

import edu.yu.einstein.wasp.model.Job;

public class JobEmailServiceActivator {
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private JobService jobService;

	private static final Logger logger = LoggerFactory.getLogger(JobEmailServiceActivator.class);

	@ServiceActivator
	public void isSuccessfulRun(Message<WaspStatus> jobStatusMessage) {
		if (!JobStatusMessageTemplate.isMessageOfCorrectType(jobStatusMessage)){
			logger.warn("Message is not of the correct type (a Job message). Check service activator and input channel are correct");
		}
		JobStatusMessageTemplate jobStatusMessageTemplate = new JobStatusMessageTemplate(jobStatusMessage);
		if (jobStatusMessageTemplate.getStatus().equals(WaspStatus.STARTED) && jobStatusMessageTemplate.getTask().equals(WaspTask.NOTIFY_STATUS)){
			Job job = jobService.getJobByJobId(jobStatusMessageTemplate.getJobId());
			if(job != null && job.getJobId() != null){
				if(job.getUserId().intValue() != job.getLab().getPrimaryUserId().intValue()){//submitter is not the lab PI
					emailService.sendSubmitterJobStarted(job);
					emailService.sendPIJobStartedConfirmRequest(job);
				}
				else{
					emailService.sendSubmitterWhoIsAlsoThePIJobStartedConfirmRequest(job);
				}				
				emailService.sendLabManagerJobStartedConfirmRequest(job);//the designated lab manager in the submitter's lab
				emailService.sendDAJobStartedConfirmRequest(job);
				emailService.sendFacilityManagerJobStartedConfirmRequest(job);//the shared facility
			}
		}		
	}

}