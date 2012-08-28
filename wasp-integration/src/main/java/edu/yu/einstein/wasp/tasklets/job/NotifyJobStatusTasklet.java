package edu.yu.einstein.wasp.tasklets.job;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;

import edu.yu.einstein.wasp.messages.WaspJobStatusMessageTemplate;
import edu.yu.einstein.wasp.messages.WaspJobTask;
import edu.yu.einstein.wasp.messages.WaspStatus;
import edu.yu.einstein.wasp.tasklets.WaspTasklet;

public class NotifyJobStatusTasklet extends WaspTasklet implements Tasklet {
	
	private final Logger logger = Logger.getLogger(NotifyJobStatusTasklet.class);

	private Integer jobId;
	
	private WaspStatus status;
	
	private MessageChannel messageChannel;
	
	
	public NotifyJobStatusTasklet(MessageChannel outputMessageChannel, Integer jobId, String status) {
		logger.debug("Constructing new instance with jobId='"+jobId+"'"); 
		this.jobId = jobId;
		this.status = WaspStatus.valueOf(status);
		this.messageChannel = outputMessageChannel;
	}
	
	
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.debug("execute() invoked");
		Message<WaspStatus> message = WaspJobStatusMessageTemplate.build(jobId, this.status, WaspJobTask.NOTIFY_STATUS);
		logger.debug("sending message: "+message);
		messageChannel.send(message);
		return RepeatStatus.FINISHED;
	}
	

}
