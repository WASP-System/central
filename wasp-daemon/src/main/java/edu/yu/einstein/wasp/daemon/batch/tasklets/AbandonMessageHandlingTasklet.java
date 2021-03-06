package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.StatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * Listens on the provided subscribable channel for a abort messages with a task and status specified in the
 * provided message template. 
 * @author asmclellan
 */
@Transactional
public class AbandonMessageHandlingTasklet implements MessageHandler, NameAwareTasklet, BeanNameAware, StepExecutionListener, InitializingBean, DisposableBean {
	
	private static final Logger logger = LoggerFactory.getLogger(AbandonMessageHandlingTasklet.class);
	
	protected Set<WaspStatusMessageTemplate> abandonTemplates = new HashSet<>();
	
	protected List<Message<?>> abandonMessageQueue = new ArrayList<>();
	
	protected String name = "";
	
	@Autowired
	@Qualifier("wasp.channel.reply")
	SubscribableChannel replyChannel;
	
	@Autowired
	@Qualifier("wasp.channel.notification.batch")
	SubscribableChannel subscribeChannel;
	
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setBeanName(String name) {
		this.name = name;
		
	}
	
	public AbandonMessageHandlingTasklet() {
		// proxy
	}
	
	public void setAbandonMessages(final Set<WaspStatusMessageTemplate> abandonTemplates){
		this.abandonTemplates.clear();
		this.abandonTemplates.addAll(abandonTemplates);
	}
	
	public void setAbandonMessage(final WaspStatusMessageTemplate abandonTemplate){
		Set<WaspStatusMessageTemplate> templates = new HashSet<>();
		templates.add(abandonTemplate);
		setAbandonMessages(templates);
	}

	
	@Override
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		if (isExpectedMessageReceived(context))
			return RepeatStatus.FINISHED;
		return RepeatStatus.CONTINUABLE;	
	}
	
	protected boolean isExpectedMessageReceived(ChunkContext context){
		StepExecution stepExecution =  context.getStepContext().getStepExecution();
		Long stepExecutionId =stepExecution.getId();
		Long jobExecutionId = context.getStepContext().getStepExecution().getJobExecutionId();
		if ((!abandonMessageQueue.isEmpty()) && context.getStepContext().getStepExecution().getJobExecution().getStatus().isRunning()){
			logger.info("StepExecution (id=" + stepExecutionId + ", JobExecution id=" + jobExecutionId + ") received an expected message so finishing step.");
			sendSuccessReplyToAllMessagesInQueue(abandonMessageQueue);
			return true;
		}
		return false;
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// set exit status to equal the most severe outcome of all received messages
		ExitStatus exitStatus = stepExecution.getExitStatus();
		this.abandonMessageQueue.clear(); // clean up in case of restart
		logger.debug(stepExecution.getStepName() + " AbandonMessageHandlingTasklet afterStep() returning ExitStatus=" + exitStatus);
		return exitStatus;
	}
	
	protected void sendSuccessReplyToAllMessagesInQueue(Collection<Message<?>> queue){
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		logger.debug("Going to send " + queue.size()  + " reply message(s)...");
		for (Message<?> message: queue){
			try{
				logger.debug("sending reply to message: " + message.toString());
				// this is a little complex. What we do here is attach the temporary point-to-point reply channel generated by the gateway
				// and attached to the source message to the reply message and send it on the 'wasp.channel.reply' channel. 
				// The Gateway will create a bridge from it to the temporary, anonymous reply channel that is stored in the header.
				// Of course if there is no reply channel specified then no reply will be sent.
				if ( message.getHeaders().containsKey(MessageHeaders.REPLY_CHANNEL)){
					Message<WaspStatus> replyMessage = MessageBuilder
							.withPayload(WaspStatus.COMPLETED)
							.setReplyChannel((MessageChannel) message.getHeaders().get(MessageHeaders.REPLY_CHANNEL))
							.build();
					logger.debug("sending reply message: " + replyMessage.toString());
					messagingTemplate.send(replyChannel, replyMessage);
				} else
					logger.debug("No reply message sent because no reply channel was specified in the original message");
			} catch (Exception e){
				logger.warn("Failure to send reply message (reason: " + e.getLocalizedMessage() + ") to reply channel specified in source message : " +
						message.toString() + ". Original exception stack: ");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		logger.debug(name + "handleMessage() invoked. Received message: " + message.toString());
		if (! WaspStatus.class.isInstance(message.getPayload()))
			return;
		// first check if any abort / failure messages have been delivered from a monitored message template
		WaspStatus statusFromMessage = (WaspStatus) message.getPayload();
		for (StatusMessageTemplate messageTemplate: abandonTemplates){
			if (messageTemplate.actUponMessage(message) && statusFromMessage.equals(messageTemplate.getStatus())){
				this.abandonMessageQueue.add(message);
				logger.debug(name + "handleMessage() found ABANDONED message for abort-monitored template " + 
						messageTemplate.getClass().getName() + ". Going to fail step.");
			}
		}
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		try{
			// subscribe to injected message channel
			logger.debug("subscribing to injected message channel");
			subscribeChannel.subscribe(this);
		} catch (Throwable e){
			throw new WaspRuntimeException("Caught unexpected exception of type " + e.getClass().getName(), e);
		}
	}

	@Override
	public void destroy() throws Exception {
		try{
			// unregister from message channel only if this object gets garbage collected
			if (subscribeChannel != null){
				subscribeChannel.unsubscribe(this); 
				subscribeChannel = null;
			}
		} catch (Throwable e){
			throw new WaspRuntimeException("Caught unexpected exception of type " + e.getClass().getName(), e);
		}
	}

}
