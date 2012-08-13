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

public class ChipSeqJobActionedTasklet extends WaspTasklet implements Tasklet {
	
	private final Logger logger = Logger.getLogger(ChipSeqJobActionedTasklet.class);

	private Integer jobId;
	
	private MessageChannel messageChannel;
	
	private boolean isAccepted;
	
	
	
	public ChipSeqJobActionedTasklet(MessageChannel outputMessageChannel, Integer jobId, boolean isAccepted) {
		logger.debug("Constructing new instance with jobId='"+jobId+"', isAccepted='"+isAccepted+"'"); 
		this.jobId = jobId;
		this.messageChannel = outputMessageChannel;
		this.isAccepted = isAccepted;
	}
	
	
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.debug("execute() invoked");
		Message<WaspStatus> message;
		if (isAccepted){
			message = WaspJobStatusMessageTemplate.build(jobId, WaspStatus.CREATED, WaspJobTask.NOTIFY_JOB_STATUS);
		} else {
			message = WaspJobStatusMessageTemplate.build(jobId, WaspStatus.ABANDONED, WaspJobTask.NOTIFY_JOB_STATUS);
		}
		logger.debug("sending message: "+message);
		messageChannel.send(message);
		return RepeatStatus.FINISHED;
	}
	

}
