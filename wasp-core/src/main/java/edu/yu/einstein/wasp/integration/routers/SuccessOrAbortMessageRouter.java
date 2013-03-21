package edu.yu.einstein.wasp.integration.routers;

import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.annotation.Router;

import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;

/**
 * Routes messages with a task of NOTIFY_STATUS reporting a WaspStatus of FAILED or ABANDONED to the abort channel. All
 * other messages go to the success channel
 * @author asmclellan
 *
 */
public class SuccessOrAbortMessageRouter {
	
	private MessageChannel successChannel;
	
	private MessageChannel abortChannel;
	

	public SuccessOrAbortMessageRouter(MessageChannel successChannel,	MessageChannel abortChannel) {
		this.successChannel = successChannel;
		this.abortChannel = abortChannel;
	}

	public MessageChannel getSuccessChannel() {
		return successChannel;
	}

	public void setSuccessChannel(MessageChannel successChannel) {
		this.successChannel = successChannel;
	}

	public MessageChannel getFailureChannel() {
		return abortChannel;
	}

	public void setFailureChannel(MessageChannel failureChannel) {
		this.abortChannel = failureChannel;
	}
	
	/**
	 * Routes messages with a task of NOTIFY_STATUS reporting a WaspStatus of FAILED or ABANDONED to the failure channel. All
	 * other messages go to the success channel
	 * @param message
	 * @return
	 */
	@Router
	public MessageChannel route(Message<?> message){
		if (!WaspStatus.class.isInstance(message.getPayload()))
			return successChannel;
		WaspStatus status = (WaspStatus) message.getPayload();
		if (status.isUnsuccessful() && 
				message.getHeaders().containsKey(WaspTask.HEADER_KEY) && 
				message.getHeaders().get(WaspTask.HEADER_KEY).equals(WaspTask.NOTIFY_STATUS) )
			return abortChannel;
		return successChannel;
	}
	
	

}
