package edu.yu.einstein.wasp.tasklets;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.core.MessageHandler;

public class WaspJobQuoteTasklet implements Tasklet, MessageHandler {

	private Integer jobId;
	
	public WaspJobQuoteTasklet(Integer jobId) {
		this.jobId = jobId;
	}
	

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void handleMessage(Message<?> arg0) throws MessagingException {
		// TODO Auto-generated method stub

	}

}
